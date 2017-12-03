package com.expendables.hackathon.api;

import com.expendables.hackathon.domain.sensor.Location;
import com.expendables.hackathon.domain.sensor.Repository.SensorService;
import com.expendables.hackathon.domain.sensor.Repository.mongo.MongoSensorService;
import com.expendables.hackathon.domain.sensor.Sensor;
import com.expendables.hackathon.domain.sensor.SensorState;
import com.expendables.hackathon.helper.ConfigStore;
import com.expendables.hackathon.helper.GeoSpatialHelper;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.geojson.Position;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.vertx.core.*;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;

import java.time.Instant;

import java.util.ArrayList;
import java.util.List;


public class HttpJavaVerticle extends AbstractVerticle {
    Logger LOG = LoggerFactory.getLogger(HttpJavaVerticle.class);
    SensorService sensorService;
    MongoClient mongoClient;

    /**
     * Initialise the verticle.<p>
     * This is called by Vert.x when the verticle instance is deployed. Don't call it yourself.
     *
     * @param vertx   the deploying Vert.x instance
     * @param context the context of the verticle
     */
    @Override
    public void init(Vertx vertx, Context context) {
        super.init(vertx, context);

        sensorService = new MongoSensorService(vertx);
        mongoClient = MongoClient.createShared(vertx, ConfigStore.getMongoConfig());
        //setupMongo();
    }

    public void setupMongo() {

   /*     mongoClient = new MongoClient();
        database = mongoClient.getDatabase(ConfigStore.MONGO_DB_NAME);
        MongoCollection<Document> collection = database.getCollection(ConfigStore.MONGO_DB_COLLECTION);
        collection.createIndex(Indexes.geo2dsphere("location"));*/

        /*getCollection().createIndex(new BasicDBObject("location", "2dsphere"));
        _mongo = new Mongo(new DBAddress(ConfigStore.MONGO_HOST, ConfigStore.MONGO_PORT, ConfigStore.MONGO_DB_NAME));*/
    }

    /**
     * If your verticle does a simple, synchronous start-up then override this method and put your start-up
     * code in here.
     *
     * @throws Exception
     */
    @Override
    public void start() throws Exception {

        HttpServer server = vertx.createHttpServer();
        Router router = Router.router(vertx);



        router.get("/api/v1/status/road/:address").handler(
            event -> {
                String requestedRoad = event.request().getParam("address");

                //Okay at this point we need to find the path that represents the given road

                //Then we check to see if we have any nodes near that path

                //Then we check the status of all of those points and determine the likelihood that
                //there is ice
//            val retJson = JsonObject(
//            "icy" to true,
//            "certainty" to 80.0
//            )

                event.response().putHeader(HttpHeaderNames.CONTENT_TYPE.toString(), HttpHeaderValues.APPLICATION_JSON.toString()).end(new JsonObject().put("icy", new Boolean(true)).put("certainty", 80.0d).encodePrettily());

            });

        router.post().handler(BodyHandler.create());


        //
        router.post("/api/v1/status/point").handler(event -> {

            LOG.info("POST body: " + event.getBodyAsString());
            JsonObject postBody = event.getBodyAsJson();
            JsonArray locationArray = postBody.getJsonArray("location");
            Double longi = locationArray.getDouble(0);
            Double lati = locationArray.getDouble(1);
            Integer maxDistance = postBody.getInteger("maxDistance");
            JsonObject query = GeoSpatialHelper.help(longi,lati,maxDistance);

            mongoClient.find(ConfigStore.MONGO_DB_COLLECTION, query, res -> {
                if (res.succeeded()) {
                        event.response().setStatusCode(200);
                        event.response().putHeader(HttpHeaders.CONTENT_TYPE, "application/json");
                        event.response().end(res.result().toString());

                }else {
                    res.cause().printStackTrace();
                    event.response().setStatusCode(400).putHeader(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.TEXT_PLAIN).end(res.cause().getMessage());
                }

            });
        });
        //

        router.post("/api/v1/sensor/:sensorId").handler(event -> {
            LOG.info("" + event.getBodyAsString());
            JsonObject postBody = new JsonObject(event.getBodyAsString()).getJsonObject("DevEUI_uplink");

            String loraId = postBody.getString("DevEUI");

            //TODO: This will come from the gps module
            Double lat = Double.valueOf(postBody.getString("LrrLAT"));
            Double lon = Double.valueOf(postBody.getString("LrrLON"));

            String payload = postBody.getString("payload_hex");

                Location loc = new Location("point", new double[]{lat.doubleValue(), lon.doubleValue()});

                sensorService.readSensorStateByLoraId(loraId, callback -> {

                    if (callback.succeeded()) {
                        if (callback.result() == null) {
                            sensorService.createSensor(loraId, loc, bax -> {
                                if (bax.succeeded()) {
                                    processUpdate(bax.result().getSensorId(), payload, event);
                                } else {
                                    event.response().setStatusCode(400).end();
                                }


                            });
                        } else {
                            Sensor sensor = callback.result();

                            processUpdate(sensor.getSensorId(), payload, event);
                        }
                    } else {
//                   event.response().setStatusCode(404).putHeader(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.TEXT_PLAIN).end(callback.cause().getMessage());
                        sensorService.createSensor(loraId, loc, bax -> {
                            if (bax.succeeded()) {
                                processUpdate(bax.result().getSensorId(), payload, event);
                            } else {
                                event.response().setStatusCode(400).putHeader(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.TEXT_PLAIN).end(callback.cause().getMessage());
                            }


                        });

                    }
                });


        });

        router.get("/api/v1/status/:sensorId").handler(
            event -> {

                String sensorId = event.request().getParam("sensorId");


            });

        router.route("/*").handler(StaticHandler.create());

        server.requestHandler(it -> router.accept(it)).listen(8080);
        super.start();

    }

    private void processUpdate(String sensorId, String payload, RoutingContext event) {
        if(payload.startsWith("2"))
        {
            processGPSUpdate(sensorId, payload, event);
        }
        else {


            vertx.eventBus().send("hex:to:ascii", payload, handle -> {

                LOG.info("HEX: " + handle.result().body());
                JsonObject statebody = (JsonObject) handle.result().body();

                Double temp = statebody.getDouble("temp");
                Integer humidity = statebody.getInteger("humidity");
                Integer moisture = statebody.getInteger("moisture");
                Boolean water = statebody.getBoolean("water");

                SensorState state = new SensorState(sensorId, Instant.now(), water, temp, humidity, moisture);

                sensorService.updateSensorState(state, callback -> {

                    if (callback.succeeded()) {
                        event.response().setStatusCode(201).putHeader(HttpHeaderNames.CONTENT_TYPE.toString(), HttpHeaderValues.TEXT_PLAIN.toString()).end(callback.result());
                    } else {
                        event.response().setStatusCode(400).putHeader(HttpHeaderNames.CONTENT_TYPE.toString(), HttpHeaderValues.TEXT_PLAIN.toString()).end(callback.cause().getMessage());
                    }

                });

            });
        }
    }

    private void processGPSUpdate(String sensorId, String payload, RoutingContext event) {
        vertx.eventBus().send("hex:to:ascii:lon:lat", payload, handle -> {

            LOG.info("HEX: " + handle.result().body());
            JsonObject gps = (JsonObject) handle.result().body();

            double lon = gps.getDouble("lon");
            double lat = gps.getDouble("lat");

            sensorService.readSensorStateBySensorId(sensorId, call -> {

                if (call.succeeded()) {

                    Sensor sensor = call.result();
                    sensor.setLocation(new Location("point", new double[]{lon, lat}));

                    sensorService.updateSensor(sensorId, sensor, cb -> {

                        if (cb.succeeded()) {
                            event.response().setStatusCode(201).putHeader(HttpHeaderNames.CONTENT_TYPE.toString(), HttpHeaderValues.APPLICATION_JSON.toString()).end(cb.result().encode());
                        } else {
                            event.response().setStatusCode(400).putHeader(HttpHeaderNames.CONTENT_TYPE.toString(), HttpHeaderValues.TEXT_PLAIN.toString()).end(cb.cause().getMessage());
                        }

                    });


                } else {
                    event.response().setStatusCode(400).putHeader(HttpHeaderNames.CONTENT_TYPE.toString(), HttpHeaderValues.TEXT_PLAIN.toString()).end(call.cause().getMessage());
                }
            });
        });
    }

    private void getIDsFromTheList(List<JsonObject> jsons, Handler<AsyncResult<List<String>>> callback){
       List<String> ids = new ArrayList<>();

        for(JsonObject json : jsons)
        {
            //ides.add()
            ids.add(json.getString(""));
        }


        callback.handle(Future.succeededFuture(ids));

    }

}
