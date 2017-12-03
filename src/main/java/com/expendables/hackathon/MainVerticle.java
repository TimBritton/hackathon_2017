package com.expendables.hackathon;

import com.expendables.hackathon.api.HttpJavaVerticle;
import com.expendables.hackathon.api.HttpVerticle;
import com.expendables.hackathon.domain.sensor.Sensor;
import com.expendables.hackathon.domain.sensor.SensorState;
import com.expendables.hackathon.helper.ConfigStore;
import io.reactivex.Observable;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.FindOptions;
import io.vertx.reactivex.ext.mongo.MongoClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class MainVerticle extends AbstractVerticle {
    Logger log = LoggerFactory.getLogger(MainVerticle.class);
    MongoClient mongoClient;

    /**
     * Initialise the verticle.<p>
     * This is called by Vert.x when the verticle instance is deployed. Don't call it yourself.
     *
     * @param vertx   the deploying Vert.x instance
     * @param context the context of the verticle
     */
    @Override
    public void init(Vertx vertx, Context context) {
        mongoClient = MongoClient.createShared(io.vertx.reactivex.core.Vertx.vertx(), ConfigStore.getMongoConfig());

        super.init(vertx, context);
    }

    @Override
    public void start() throws Exception {
        List<JsonObject> farse = new ArrayList<>();
        farse.add(new JsonObject());
        vertx.deployVerticle(new HttpJavaVerticle());
        vertx.deployVerticle(new HexEncodingVerticle());
        System.out.println("HTTP server started on port 8080");

        vertx.eventBus().consumer("sensor:retrieve:all", json -> {
            String jon = "";
            mongoClient.rxFind("frsty:sensor", new JsonObject())
                .toObservable()
                .flatMap(list -> Observable.fromIterable(list))
                .flatMap(jsonObject -> {
                    log.info(jsonObject.encode());
                    Sensor sen = Sensor.fromJsonObject(jsonObject);
                    //todo retrieve stat data if avaiable
                    JsonObject stateQuery = new JsonObject().put( "sensorId" , sen.getSensorId());


//                    FindOptions findOptions = new FindOptions();
//                    findOptions.setSort(new JsonObject().put(
//                        "createdTime" , -1
//                    ));
//                    findOptions.setLimit(1);
//
                    log.info("query" + stateQuery.encode());

                    return  mongoClient.rxFind("frsty:sensor:state",stateQuery).toObservable().defaultIfEmpty(farse)
                        .flatMap(results ->
                    {
                        log.info("Zipping components " + results.toString());
                        if(results.size() > 0){
                        if(!results.get(0).isEmpty()) {
                            SensorState state = SensorState.fromJsonObject(results.get(0));
                            sen.setSensorState(state);
                            return Observable.just(sen);
                        }

                        else
                            return Observable.just(sen);

                        }

                        else
                            return Observable.just(sen);
                    });
                }).filter(filter -> filter.getSensorState() != null).toList()
                .subscribe(items2 -> json.reply(Json.encode(items2)), error -> {
                    error.printStackTrace();
                    log.error("error" + error.getMessage());
                    json.reply(new JsonArray().encodePrettily());
                });

        });
    }
}
