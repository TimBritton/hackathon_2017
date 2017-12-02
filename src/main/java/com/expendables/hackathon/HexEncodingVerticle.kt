package com.expendables.hackathon

import io.vertx.core.AbstractVerticle
import io.vertx.core.Handler
import io.vertx.core.eventbus.Message

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

            val output = StringBuilder()
            var i = 0
            while (i < hexString.length) {
                val str = hexString.substring(i, i + 2)
                output.append(Integer.parseInt(str, 16).toChar())
                i += 2
            }

            event.reply(output)
        })
        super.start()
    }
}
