package com.example.mycommuter.RestApi;

import com.example.mycommuter.model.Tasks;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String Base_URL = "https://radiant-lowlands-66469.herokuapp.com/";

    public static Retrofit getClient() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .build();

        return new Retrofit.Builder()
                .baseUrl(Base_URL)
                .addConverterFactory(GsonConverterFactory.create(getConverter()))
                .client(okHttpClient)
                .build();
    }


    private static Gson getConverter() {
        return new GsonBuilder()
                .registerTypeAdapter(Tasks.class, new MyDeserializer<Tasks>())
                .setLenient()
                .create();
    }
}
