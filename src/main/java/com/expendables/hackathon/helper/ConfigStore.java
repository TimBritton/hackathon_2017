package com.expendables.hackathon.helper;

import io.vertx.core.json.JsonObject;

public class ConfigStore {

    public static final String MONGO_HOST = "10.91.22.57";
    public static final Integer MONGO_PORT = 27017;
    public static final String MONGO_DB_NAME = "frsty";
    public static final String MONGO_DB_COLLECTION = "frsty:sensor";

    public static JsonObject getMongoConfig(){
        JsonObject mongoConfig = new JsonObject().put("host",  MONGO_HOST)
                .put("port", MONGO_PORT)
                .put("db_name", MONGO_DB_NAME);
        return mongoConfig;
    }
}
