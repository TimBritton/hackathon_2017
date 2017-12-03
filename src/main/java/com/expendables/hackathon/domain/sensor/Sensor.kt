package com.expendables.hackathon.domain.sensor

import io.vertx.core.json.JsonObject
import java.time.Instant

data class Sensor(val sensorId: String, val loraSenorId: String, var location: Location, var sensorState: SensorState?){
    init {

    }

    companion object {
       @JvmStatic fun fromJsonObject(jsonObject: JsonObject): Sensor
        {
           return Sensor(jsonObject.getString("sensorId"), jsonObject.getString("sensorId"), Location.fromJsonObject(jsonObject.getJsonObject("location")), null)
        }
    }
}

data class Location(val type:String = "point", var coordinates: DoubleArray) {
    init {

    }

    companion object {
        @JvmStatic fun fromJsonObject(jsonObject: JsonObject): Location {
            var arra = kotlin.DoubleArray(2)

            var doublea: Double = jsonObject.getJsonArray("coordinates").list.get(0) as Double
            var doubleb: Double = jsonObject.getJsonArray("coordinates").list.get(1) as Double

            arra.set(0, doublea)
            arra.set(1, doubleb)
            return Location("point", arra)
        }
    }
}
//** water 0 or 10 10 is true
data class SensorState(val sensorId: String, val createdTime: Instant, val water: Boolean, val temprature: Double, val humidity: Int, val moisture: Int){
    init {

    }

    companion object {
        @JvmStatic fun fromJsonObject(jsonObject: JsonObject): SensorState = SensorState(jsonObject.getString("sensorId"), jsonObject.getInstant("createdTime"), jsonObject.getBoolean("water"), jsonObject.getDouble("temprature"), jsonObject.getInteger("humidity"), jsonObject.getInteger("moisture"))

        }

}
