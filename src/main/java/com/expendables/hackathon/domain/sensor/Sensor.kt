package com.expendables.hackathon.domain.sensor

data class Sensor(val sensorId: String, val loraSenorId: String, var location: Location, var sensorState: SensorState?)

data class Location(val type:String = "point", var coordinates: DoubleArray)
//** water 0 or 10 10 is true
data class SensorState(val sensorId: String, val water: Boolean, val temprature: Int, val moisture: Int)
