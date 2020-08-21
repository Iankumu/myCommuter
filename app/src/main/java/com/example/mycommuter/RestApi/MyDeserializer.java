package com.example.mycommuter.RestApi;

import com.example.mycommuter.model.Tasks;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collection;

public class MyDeserializer<T> implements JsonDeserializer<T> {
    @Override
    public T deserialize(JsonElement je, Type type, JsonDeserializationContext jdc) throws JsonParseException {
        JsonElement content = je.getAsJsonObject().get("data");

//            T result = new Gson().fromJson(content, type);
        Type collectionType = new TypeToken<Collection<Tasks>>() {
        }.getType();
        Collection<Tasks> enums = new Gson().fromJson(content, collectionType);
        return (T) enums;
    }
}
