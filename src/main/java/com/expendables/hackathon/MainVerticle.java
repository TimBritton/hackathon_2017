package com.expendables.hackathon;

import com.expendables.hackathon.api.HttpJavaVerticle;
import com.expendables.hackathon.api.HttpVerticle;
import io.vertx.core.AbstractVerticle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainVerticle extends AbstractVerticle {
    Logger log = LoggerFactory.getLogger(MainVerticle.class);
    @Override
    public void start() throws Exception {
        vertx.deployVerticle(new HttpJavaVerticle());
//        vertx.deployVerticle("service:io.vertx.vertx-mongo-embedded-db", handler -> {
//            if(handler.succeeded())
//            {
//                log.info("MONGO UP");
//            }
//            else
//            {
//                log.error("{}", handler.cause());
//            }
//
//        });
        vertx.deployVerticle(new HexEncodingVerticle());

        System.out.println("HTTP server started on port 8080");
    }
}
