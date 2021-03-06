package com.expendables.hackathon.api

import io.netty.handler.codec.http.HttpHeaderNames
import io.netty.handler.codec.http.HttpHeaderValues
import io.vertx.core.AbstractVerticle
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.handler.StaticHandler
import io.vertx.kotlin.lang.json.JsonObject
import io.vertx.kotlin.lang.json.json_

class HttpVerticle : AbstractVerticle()
{
    /**
     * If your verticle does a simple, synchronous start-up then override this method and put your start-up
     * code in here.
     * @throws Exception
     */
    override fun start() {
        var server = vertx.createHttpServer()

        val router = Router.router(vertx)

        router.get("/api/v1/status/road/:address").handler({
            event: RoutingContext? ->

            val requestedRoad: String = event?.request()?.getParam("address")!!

            //Okay at this point we need to find the path that represents the given road

            //Then we check to see if we have any nodes near that path

            //Then we check the status of all of those points and determine the likelihood that
            //there is ice
            val retJson = JsonObject(
                "icy" to true,
                "certainty" to 80.0
            )

            val response = event.response().putHeader(HttpHeaderNames.CONTENT_TYPE.toString(), HttpHeaderValues.APPLICATION_JSON.toString()).end(retJson.encodePrettily())

        })

        router.get("/api/v1/status/:sensorId").handler({
            event: RoutingContext? ->

            val sensorId: String = event?.get("sensorId")!!



        })

        router.route("/*").handler(StaticHandler.create())

        server.requestHandler({ router.accept(it) }).listen(8080)
        super.start()
    }
}
