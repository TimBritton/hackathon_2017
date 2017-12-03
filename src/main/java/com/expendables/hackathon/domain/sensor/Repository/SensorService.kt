package com.expendables.hackathon.domain.sensor.Repository

import com.expendables.hackathon.domain.sensor.Location
import com.expendables.hackathon.domain.sensor.Sensor
import com.expendables.hackathon.domain.sensor.SensorState
import io.vertx.core.AsyncResult
import io.vertx.core.Handler

interface SensorService {

    fun readSensorStateBySensorId(sensorId: String, callbax: Handler<AsyncResult<Sensor>>)

    fun readSensorStateByLoraId(loraSensorId: String, callbax: Handler<AsyncResult<Sensor>>)

    fun createSensor(loraSensorId: String, location: Location, callbax: Handler<AsyncResult<Sensor>>)

    fun updateSensor(sensorId: String, sensor:Sensor, callbax: Handler<AsyncResult<Sensor>>)

    fun updateSensorState(sensor: SensorState, callbax: Handler<AsyncResult<String>>)

    fun readAllSensors(callbax: Handler<AsyncResult<List<Sensor>>>)

}
