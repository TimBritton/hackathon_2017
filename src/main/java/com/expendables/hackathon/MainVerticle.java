package com.expendables.hackathon;

import com.expendables.hackathon.api.HttpVerticle;
import io.vertx.core.AbstractVerticle;

public class MainVerticle extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        vertx.deployVerticle(new HttpVerticle());
        vertx.deployVerticle("service:io.vertx.vertx-mongo-embedded-db");
        System.out.println("HTTP server started on port 8080");
    }
}
