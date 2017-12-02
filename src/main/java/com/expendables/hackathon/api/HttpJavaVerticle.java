package com.expendables.hackathon.api;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;


public class HttpJavaVerticle extends AbstractVerticle{
    Logger LOG = LoggerFactory.getLogger(HttpJavaVerticle.class);
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

            event.response().end();
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
