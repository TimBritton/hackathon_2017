package com.expendables.hackathon.domain.sensor.Repository.mongo

import com.expendables.hackathon.domain.sensor.Location
import com.expendables.hackathon.domain.sensor.Repository.SensorService
import com.expendables.hackathon.domain.sensor.Sensor
import io.vertx.core.AsyncResult
import io.vertx.core.Handler
import io.vertx.core.Vertx

class MongoSensorService(vertx: Vertx) : SensorService{


    override fun readSensorStateBySensorId(sensorId: String, callbax: Handler<AsyncResult<Sensor>>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createSensor(loraSenorId: String, location: Location, callbax: Handler<AsyncResult<Sensor>>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateSensor(sensorId: String, sensor: Sensor, callbax: Handler<AsyncResult<Sensor>>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateSensorState(sensorId: String, water: Boolean, temprature: Int, moisture: Int, callbax: Handler<AsyncResult<Boolean>>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
