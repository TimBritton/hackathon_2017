package com.expendables.hackathon

import com.expendables.hackathon.helper.ConfigStore
import io.vertx.core.AbstractVerticle
import io.vertx.core.Handler
import io.vertx.core.eventbus.Message
import io.vertx.core.json.JsonObject
import io.vertx.ext.mongo.FindOptions
import io.vertx.kotlin.lang.json.JsonObject
import io.vertx.reactivex.ext.mongo.MongoClient
import io.reactivex.Observable

import java.util.*

class HexEncodingVerticle : AbstractVerticle() {
    /**
     * If your verticle does a simple, synchronous start-up then override this method and put your start-up
     * code in here.
     * @throws Exception
     */


    override fun start() {

        vertx.eventBus().consumer<String>("hex:to:ascii", {
            event ->

            val hexString = event.body()

            var json = JsonObject(
                "temp" to (hexString.substringAfter("a").substringBefore("b").toInt() / 100.00),
                "humidity" to hexString.substringAfter("b").substringBefore("c").toInt(),
                "moisture" to hexString.substringAfter("c").substringBefore("d").toInt(),
                "water" to (hexString.substringAfter("d").substringBefore("e") == ("10")))

            event.reply(json)
        })

        vertx.eventBus().consumer<String>("hex:to:ascii:lon:lat", {
            event ->

            val hexString = event.body()

            var json = JsonObject(
                "lon" to (hexString.substringAfter("c").substringBefore("f").toInt() / 1000000.0) * -1.0,
                        "lat" to (hexString.substringAfter("f").toInt() / 1000000.0))

            event.reply(json)
        })


        super.start()
    }
}
