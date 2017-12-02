package com.expendables.hackathon;

import io.vertx.core.AbstractVerticle;

public class MainVerticle extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        vertx.createHttpServer().requestHandler(req -> {
              req.response()
                .putHeader("content-type", "text/plain")
                .end("Hello from Vert.x!");
            }).listen(8080);

        vertx.deployVerticle("service:io.vertx.vertx-mongo-embedded-db");
        System.out.println("HTTP server started on port 8080");
    }
}
