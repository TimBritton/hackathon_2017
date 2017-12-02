package com.expendables.hackathon.api;

import com.expendables.hackathon.domain.sensor.Location;
import com.expendables.hackathon.domain.sensor.Repository.SensorService;
import com.expendables.hackathon.domain.sensor.Repository.mongo.MongoSensorService;
import com.expendables.hackathon.domain.sensor.Sensor;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;

import java.util.ArrayList;
import java.util.List;


public class HttpJavaVerticle extends AbstractVerticle{
    Logger LOG = LoggerFactory.getLogger(HttpJavaVerticle.class);
    SensorService sensorService;

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
                String requestedRoad  = event.request().getParam("address");

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
        router.post("/api/v1/sensor/:sensorId").handler(event -> {
            LOG.info("" + event.getBodyAsString());
            JsonObject postBody = event.getBodyAsJson();

            String loraId = postBody.getString("DevEUI");

            //TODO: This will come from the gps module
            Double lat = postBody.getDouble("LrrLAT");
            Double lon = postBody.getDouble("LrrLON");

            String payload = postBody.getString("payload_hex");

            Location location = new Location("point", new double[]{lat, lon});

           sensorService.readSensorStateByLoraId(loraId, callback -> {

               if(callback.succeeded())
               {
                   if(callback.result() == null) {
                       sensorService.createSensor(loraId, location, bax -> {
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
                   event.response().setStatusCode(404).putHeader(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.TEXT_PLAIN).end(callback.cause().getMessage());
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


}
