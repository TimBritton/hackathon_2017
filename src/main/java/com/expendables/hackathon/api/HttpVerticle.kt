package com.expendables.hackathon.api

import io.vertx.core.AbstractVerticle
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.handler.StaticHandler

class HttpVerticle : AbstractVerticle()
{
    /**
     * If your verticle does a simple, synchronous start-up then override this method and put your start-up
     * code in here.
     * @throws Exception
     */
    override fun start() {

        val router = Router.router(vertx)

        router.get("/api/v1/status/road/:road").handler({
            event: RoutingContext? ->

            val requestedRoad: String = event?.get("road")!!

            //Okay at this point we need to find the path that represents the given road

            //Then we check to see if we have any nodes near that path

            //Then we check the status of all of those points and determine the likelihood that
            //there is ice

        })

        router.get("/api/v1/status/:sensorId").handler({
            event: RoutingContext? ->

            val sensorId: String = event?.get("sensorId")!!



        })

        router.route("/*").handler(StaticHandler.create())

        super.start()
    }
}
