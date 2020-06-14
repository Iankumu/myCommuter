package com.example.mycommuter.RestApi;



import android.content.Context;
import android.content.SharedPreferences;

//import com.example.mycommuter.fragments.Home_frag;
import com.example.mycommuter.model.JSONObj;

import com.example.mycommuter.model.Tasks;
import com.example.mycommuter.model.User;

import java.util.ArrayList;
import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;




public interface theCommuterApiendpoints {


    @FormUrlEncoded
    @POST("api/register/")
    Call<User> User(@Field("name") String username,
                    @Field("email") String emailAddress,
                    @Field("password") String password,
                    @Field("password_confirmation") String confirmPassword);
    @FormUrlEncoded
    @POST("api/login/")
    Call<User> User(
                    @Field("email") String emailAddress,
                    @Field("password") String password);

    @Headers({
            "Accept: application/json",

    })
    @GET("api/tasks")

    Call<Tasks> getTasks(
            @Header("Authorization") String authorization);

    @GET("api/tasks")
    Call<List<Tasks>> getallTasks(
            @Header("Authorization") String authorization);

    @GET("api/email/resend")
    Call<User> getVerified(
            @Header("Authorization") String authorization);
}
//    @Path("title") String title,
//    @Path("descritpion") String descritpion ,
//    @Path("due") String due,
//    @Path("created_at") String created_at