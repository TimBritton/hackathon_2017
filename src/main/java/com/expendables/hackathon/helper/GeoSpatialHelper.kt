package com.expendables.hackathon.helper

import io.vertx.core.json.JsonObject
import io.vertx.kotlin.lang.json.JsonArray
import io.vertx.kotlin.lang.json.JsonObject


class GeoSpatialHelper {
    companion object {
        fun help(lon: Double, lat: Double, distance: Int): JsonObject {
            return JsonObject(
                "location" to JsonObject(
                    "\$near" to JsonObject(

                        "\$geometry" to JsonObject("type" to "Point", "coordinates" to JsonArray(lon, lat)),
                        "\$maxDistance" to distance
                    )
                ))
        }
    }
}

