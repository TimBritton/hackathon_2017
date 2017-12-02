package com.expendables.hackathon.domain.sensor.Repository.mongo

import com.expendables.hackathon.domain.sensor.Location
import com.expendables.hackathon.domain.sensor.Repository.SensorService
import com.expendables.hackathon.domain.sensor.Sensor
import com.expendables.hackathon.helper.ConfigStore
import io.vertx.core.AsyncResult
import io.vertx.core.Future
import io.vertx.core.Handler
import io.vertx.core.Vertx
import io.vertx.core.json.Json
import io.vertx.core.json.JsonObject
import io.vertx.ext.mongo.MongoClient
import io.vertx.kotlin.lang.json.json_
import java.util.*

class MongoSensorService(vertx: Vertx) : SensorService{
    override fun readSensorStateByLoraId(loraSensorId: String, callbax: Handler<AsyncResult<Sensor>>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    val mongoClient: MongoClient = MongoClient.createShared(vertx, ConfigStore.getMongoConfig())

    override fun readSensorStateBySensorId(sensorId: String, callbax: Handler<AsyncResult<Sensor>>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createSensor(loraSenorId: String, location: Location, callbax: Handler<AsyncResult<Sensor>>) {
        val sensorId = "FRSTY:ID:" + UUID.randomUUID()

        val sensor = Sensor(sensorId, loraSenorId, location, null)

        mongoClient.save("frsty:sensor", JsonObject.mapFrom(sensor), {
            event ->

            if(event.succeeded())
            {
                callbax.handle(Future.succeededFuture(sensor))
            }
            else {
                callbax.handle(Future.failedFuture(event.cause()))
            }
        })
    }

    override fun updateSensor(sensorId: String, sensor: Sensor, callbax: Handler<AsyncResult<Sensor>>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateSensorState(sensorId: String, water: Boolean, temprature: Int, moisture: Int, callbax: Handler<AsyncResult<Boolean>>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
