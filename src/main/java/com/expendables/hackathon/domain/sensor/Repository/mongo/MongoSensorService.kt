package com.expendables.hackathon.domain.sensor.Repository.mongo

import com.expendables.hackathon.domain.sensor.Location
import com.expendables.hackathon.domain.sensor.Repository.SensorService
import com.expendables.hackathon.domain.sensor.Sensor
import com.expendables.hackathon.domain.sensor.SensorState
import com.expendables.hackathon.helper.ConfigStore
import io.vertx.core.AsyncResult
import io.vertx.core.Future
import io.vertx.core.Handler
import io.vertx.core.Vertx
import io.vertx.core.json.Json
import io.vertx.core.json.JsonObject
import io.vertx.ext.mongo.FindOptions
import io.vertx.ext.mongo.MongoClient
import io.vertx.kotlin.lang.json.JsonObject
import io.vertx.kotlin.lang.json.json_
import org.slf4j.LoggerFactory
import java.util.*

class MongoSensorService(vertx: Vertx) : SensorService{
    val log = LoggerFactory.getLogger(MongoSensorService::class.java)
//    override fun readAllSensors(callbax: Handler<AsyncResult<List<Sensor>>>) {
//        mongoClient.find("frsty:sensor", io.vertx.core.json.JsonObject(), {
//            callbackA ->
//        })
//    }

    override fun readSensorStateByLoraId(loraSensorId: String, callbax: Handler<AsyncResult<Sensor>>) {
        log.info("loraId" + loraSensorId)
        val query = JsonObject(
            "loraSenorId" to loraSensorId
        )

        mongoClient.find("frsty:sensor", query, {callback ->

            if(callback.succeeded())
            {
                if(callback.result().size > 0) {
                    var jsonObject = callback.result().get(0)

                    var sen = Sensor.fromJsonObject(jsonObject)
                    //todo retrieve stat data if avaiable
                    var stateQuery = JsonObject(
                        "sensorId" to sen.sensorId
                    )

                    var findOptions = FindOptions()
                    findOptions.setSort(JsonObject(
                        "createdTime" to -1
                    ))

                    findOptions.setLimit(1)

                    mongoClient.findWithOptions("frsty:sensor:state", stateQuery, findOptions, {
                        rez ->
                        if(rez.succeeded()) {
                            if(rez.result().size > 0) {
                                var jsobj = rez.result().get(0)

                                val sensorState = SensorState.fromJsonObject(jsobj)

                                sen.sensorState = sensorState

                                callbax.handle(Future.succeededFuture(sen))
                            }
                            else
                            {
                                callbax.handle(Future.succeededFuture(sen))
                            }
                        }
                    } )

                }
                else
                {
                    callbax.handle(Future.failedFuture(Exception("Sensor not found!!!!!!")))
                }
            }
            else
            {
                callbax.handle(Future.failedFuture(callback.cause()))
            }

        })
    }

    val mongoClient: MongoClient = MongoClient.createShared(vertx, ConfigStore.getMongoConfig())

    override fun readSensorStateBySensorId(sensorId: String, callbax: Handler<AsyncResult<Sensor>>) {
        log.info("SensorID" + sensorId)
        val query = JsonObject(
            "sensorId" to sensorId
        )

        mongoClient.find("frsty:sensor", query, {callback ->

            if(callback.succeeded())
            {
                if(callback.result().size > 0) {
                    var jsonObject = callback.result().get(0)

                    var sen = Sensor.fromJsonObject(jsonObject)
                    //todo retrieve stat data if avaiable
                    var stateQuery = JsonObject(
                        "sensorId" to sen.sensorId
                    )

                    var findOptions = FindOptions()
                    findOptions.setSort(JsonObject(
                        "createdTime" to -1
                    ))
                    findOptions.setLimit(1)

                    mongoClient.findWithOptions("frsty:sensor:state", stateQuery, findOptions, {
                        rez ->
                        if(rez.succeeded()) {
                            if(jsonObject.size() > 0) {
                                var jsonObject = rez.result().get(0)

                                val sensorState = SensorState.fromJsonObject(jsonObject)

                                sen.sensorState = sensorState

                                callbax.handle(Future.succeededFuture(sen))
                            }
                            else
                            {
                                callbax.handle(Future.succeededFuture(sen))
                            }
                        }
                    } )

                }
                else
                {
                    callbax.handle(Future.failedFuture(Exception("Sensor not found!!!!!!")))
                }
            }
            else
            {
                callbax.handle(Future.failedFuture(callback.cause()))
            }

        })
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

    override fun updateSensor(sensorId: String, sensor: Sensor, callbax: Handler<AsyncResult<JsonObject>>) {
        var query = JsonObject(
            "sensorId" to sensorId
        )
        mongoClient.findOneAndReplace("frsty:sensor", query, JsonObject.mapFrom(sensor), {
            callbaz->

            if(callbaz.succeeded())
            {
                callbax.handle(Future.succeededFuture(callbaz.result()))
            }

            callbax.handle(Future.failedFuture(callbaz.cause()))

        })
    }

    override fun updateSensorState(sensor: SensorState, callbax: Handler<AsyncResult<String>>) {
        mongoClient.save("frsty:sensor:state", JsonObject.mapFrom(sensor), {
            callback ->

            if(callback.succeeded())
            {
                callbax.handle(Future.succeededFuture(callback.result()))
            }
            else {
                callbax.handle(Future.failedFuture(callback.cause()))
            }

        })
    }


}
