package com.expendables.hackathon.domain.sensor.Repository

import com.expendables.hackathon.domain.sensor.Location
import com.expendables.hackathon.domain.sensor.Sensor
import io.vertx.core.AsyncResult
import io.vertx.core.Handler

interface SensorService {

    fun readSensorStateBySensorId(sensorId: String, callbax: Handler<AsyncResult<Sensor>>)

    fun createSensor(loraSenorId: String, location: Location, callbax: Handler<AsyncResult<Sensor>>)

    fun updateSensor(sensorId: String, sensor:Sensor, callbax: Handler<AsyncResult<Sensor>>)

    fun updateSensorState(sensorId: String,  water: Boolean, temprature: Int, moisture: Int, callbax: Handler<AsyncResult<Boolean>>)

}
