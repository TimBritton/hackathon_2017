package com.expendables.hackathon.domain.sensor

import io.vertx.core.json.JsonObject

data class Sensor(val sensorId: String, val loraSenorId: String, var location: Location, var sensorState: SensorState?){
    init {

    }

    companion object {
        fun fromJsonObject(jsonObject: JsonObject): Sensor
        {
           return Sensor(jsonObject.getString("sensorId"), jsonObject.getString("sensorId"), Location.fromJsonObject(jsonObject.getJsonObject("location")), null)
        }
    }
}

data class Location(val type:String = "point", var coordinates: DoubleArray) {
    init {

    }

    companion object {
        fun fromJsonObject(jsonObject: JsonObject): Location {
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
data class SensorState(val sensorId: String, val water: Boolean, val temprature: Int, val moisture: Int){
    init {

    }

    companion object {
        fun fromJsonObject(jsonObject: JsonObject): SensorState {

            return SensorState(jsonObject.getString("sensorId"), jsonObject.getBoolean("water"), jsonObject.getInteger("temprature"), jsonObject.getInteger("moisture"))
        }
        }

}
