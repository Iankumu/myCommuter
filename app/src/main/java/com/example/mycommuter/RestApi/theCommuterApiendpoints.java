package com.example.mycommuter.RestApi;


import android.content.Context;
import android.content.SharedPreferences;

//import com.example.mycommuter.fragments.Home_frag;


import com.example.mycommuter.model.Tasks;
import com.example.mycommuter.model.User;
import com.example.mycommuter.model.Weather;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;


public interface theCommuterApiendpoints {
    @Headers({
            "Accept: application/json",

    })
    @FormUrlEncoded
    @POST("api/tasks")
    Call<JsonObject> uploadTask(@Field("title") String title,
                                @Field("descritpion") String descritpion,
                                @Field("due") String due,
                                @Header("Authorization") String authorization);

    @Headers({
            "Accept: application/json",

    })

    @FormUrlEncoded
    @POST("api/register")
    Call<User> User(@Field("name") String username,
                    @Field("email") String emailAddress,
                    @Field("password") String password,
                    @Field("password_confirmation") String confirmPassword);

    @Headers({
            "Accept: application/json",

    })
    @FormUrlEncoded
    @POST("api/login")
    Call<User> User(
            @Field("email") String emailAddress,
            @Field("password") String password);

    @Headers({
            "Accept: application/json",

    })
    @FormUrlEncoded
    @POST("api/password/requestpassreset")
    Call<JsonObject> ResetEmail(
            @Field("email") String emailAddress);
    @Headers({
            "Accept: application/json",

    })
    @GET("api/tasks")
    Call<Tasks> getTasks(
            @Header("Authorization") String authorization);


    @Headers(
            "Accept: application/json"

    )
    @FormUrlEncoded
    @POST("api/forecastWeather")
    Call<JsonObject> getWeather(
            @Field("longitude") String longitude,
            @Field("latitude") String latitude,
            @Header("Authorization") String authorization);

    @FormUrlEncoded
    @POST("api/currentWeather")
    Call<JsonObject> getCurrentWeather(
            @Field("longitude") double longitude,
            @Field("latitude") double latitude,
            @Header("Authorization") String authorization);

    @Headers(
            "Accept: application/json"

    )
    @FormUrlEncoded
    @POST("api/searchWeather")
    Call<JsonObject> getsearchWeather(
            @Field("location") String location,

            @Header("Authorization") String authorization);

    @Headers(
            "Accept: application/json"

    )
    @GET("api/profile")
    Call<JsonObject> getProfile(

            @Header("Authorization") String authorization);

    @Headers(
            "Accept: application/json"

    )
    @FormUrlEncoded
    @POST("api/profile")
    Call<User> setProfile(
            @Field("name") String name,
            @Header("Authorization") String authorization);

    @Headers(
            "Accept: application/json"

    )
    @GET("api/email/verifysend")
    Call<User> getVerified(
            @Header("Authorization") String authorization);

    @Headers(
            "Accept: application/json"

    )

    @DELETE("/api/tasks/{id}")
    Call<JsonObject> deleteTask(@Path("id") int taskid, @Header("Authorization") String authorization);

    @Headers(
            "Accept: application/json"

    )
    @FormUrlEncoded
    @PUT("/api/tasks/{id}")
    Call<JsonObject> updateTask(@Path("id") int taskid,
                                @Field("title") String title,
                                @Field("descritpion") String descritpion,
                                @Field("due") String due,
                                @Header("Authorization") String authorization);
}
