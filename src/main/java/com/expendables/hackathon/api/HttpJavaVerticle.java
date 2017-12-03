package com.expendables.hackathon.api;

import com.expendables.hackathon.domain.sensor.Location;
import com.expendables.hackathon.domain.sensor.Repository.SensorService;
import com.expendables.hackathon.domain.sensor.Repository.mongo.MongoSensorService;
import com.expendables.hackathon.domain.sensor.Sensor;
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
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;

/*import com.mongodb.Block;
import com.mongodb.async.client.MongoClient;
import com.mongodb.async.client.MongoCollection;
import com.mongodb.async.client.MongoDatabase;
import com.mongodb.async.client.model.geojson.*;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.Filters;
import org.bson.Document;*/

import java.util.ArrayList;
import java.util.List;


public class HttpJavaVerticle extends AbstractVerticle{
    Logger LOG = LoggerFactory.getLogger(HttpJavaVerticle.class);
    SensorService sensorService;
    MongoClient mongoClient;
   /* MongoDatabase database;*/
    //private Mongo _mongo;

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
            Double lat = Double.valueOf( postBody.getString("LrrLAT"));
            Double lon = Double.valueOf(postBody.getString("LrrLON"));

            String payload = postBody.getString("payload_hex");

            Location loc = new Location("point", new double[]{lat.doubleValue(), lon.doubleValue()});

           sensorService.readSensorStateByLoraId(loraId, callback -> {

               if(callback.succeeded())
               {
                   if(callback.result() == null) {
                       sensorService.createSensor(loraId, loc, bax -> {
                           if(bax.succeeded()){

                               vertx.eventBus().send("hex:to:ascii", payload, handle -> {

                                   LOG.info("" + handle.result());
//                                    sensorService.updateSensorState();
                                   event.response().setStatusCode(201).end();
                               });


                           }
                           else
                           {
                               event.response().setStatusCode(400).end();
                           }


                       });
                   } else {
                       Sensor sensor = callback.result();

                       vertx.eventBus().send("hex:to:ascii", payload, handle -> {

                           LOG.info("" + handle.result());

//                           sensorService.updateSensorState(sensor.getSensorId(), );
                           event.response().setStatusCode(201).end();
                       });
                   }
               }
               else
               {
//                   event.response().setStatusCode(404).putHeader(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.TEXT_PLAIN).end(callback.cause().getMessage());
                   sensorService.createSensor(loraId, loc, bax -> {
                       if(bax.succeeded()){

                           vertx.eventBus().send("hex:to:ascii", payload, handle -> {

                               LOG.info("" + handle.result());
//                                    sensorService.updateSensorState();
                               event.response().setStatusCode(201).end();
                           });


                       }
                       else
                       {
                           event.response().setStatusCode(400).putHeader(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.TEXT_PLAIN).end(callback.cause().getMessage());
                       }


                   });

               }
           });


        });

        router.get("/api/v1/status/:sensorId").handler(
            event ->{

            String sensorId = event.request().getParam("sensorId");


        });

        router.route("/*").handler(StaticHandler.create());

        server.requestHandler(it -> router.accept(it) ).listen(8080);
        super.start();

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
