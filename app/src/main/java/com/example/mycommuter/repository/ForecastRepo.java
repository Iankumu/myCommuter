package com.example.mycommuter.repository;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.util.JsonToken;
import android.util.Log;
import android.util.Pair;

import androidx.core.app.ActivityCompat;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.example.mycommuter.RestApi.ApiClient;
import com.example.mycommuter.RestApi.theCommuterApiendpoints;
import com.example.mycommuter.common.GPSTracker;
import com.example.mycommuter.fragments.WeatherFragment;
import com.example.mycommuter.interfaces.TaskInterface;
import com.example.mycommuter.interfaces.WeatherResultCallback;
import com.example.mycommuter.model.Tasks;
import com.example.mycommuter.model.Weather;
import com.example.mycommuter.sharedPrefs.saveSharedPref;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

import static android.content.ContentValues.TAG;

public class ForecastRepo {
    private List<Weather> arrayofWeather = new ArrayList<>();
    Weather weatherc=new Weather();
    private static ForecastRepo instance;
    private Context context;

    public ForecastRepo(Context context) {
        this.context = context;
    }

    //singleton
    public static ForecastRepo getInstance(Context context) {

        if (instance == null) {
            instance = new ForecastRepo(context);

        }
        return instance;
    }
    public MutableLiveData <Pair<List,Weather>> getSearchWeather(String location) {
        MutableLiveData <Pair <List,Weather>> weatherp=new MutableLiveData<>();
        searchWeather(new WeatherResultCallback() {
            @Override
            public void getWeather(List<Weather> weathers, Weather weather) {
                weatherp.setValue(new Pair<>(arrayofWeather,weatherc));
//                data.setValue(arrayofWeather);
                Log.e("data", "" + weatherp);

            }
        },location);

        return weatherp;


    }
    public MutableLiveData <Pair<List,Weather>> getWeather(Map<String, Double> coord) {
        MutableLiveData <Pair <List,Weather>> weatherp=new MutableLiveData<>();
        setWeather(coord,new WeatherResultCallback() {
            @Override
            public void getWeather(List<Weather> weathers, Weather weather) {
            weatherp.setValue(new Pair<>(arrayofWeather,weatherc));
//                data.setValue(arrayofWeather);
                Log.e("data", "" + weatherp);

            }
        });

        return weatherp;


    }

    //data retrieval from api
    public void setWeather(  Map<String, Double> coord,WeatherResultCallback weatherResultCallback) {
        String token = saveSharedPref.getToken(context);


        WeatherFragment weatherFragment=new WeatherFragment();


            Log.e(TAG, "setweathercoord " + coord.get("latitude"));

        final theCommuterApiendpoints apiService = ApiClient.getClient().create(theCommuterApiendpoints.class);


        Call<JsonObject> call = apiService.getCurrentWeather(coord.get("longitude"),coord.get("latitude"),"Bearer " + token);


        call.enqueue(new Callback<JsonObject>() {


            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {


                if (response.body() != null) {



                        String data = new Gson().toJson(response.body());

                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(String.valueOf(response.body()));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    JSONArray datas = null;
                    try {
                        datas = jsonObject.getJSONArray("data");

                        for (int i = 1; i < datas.length(); i++) {

                                try {
                                    JSONObject jo2 = datas.getJSONObject(i);

                                    Log.w(TAG, "Response: " + jo2);
                                    Weather weather = new Weather();

                            weather.setDate(jo2.getString("date"));
                            weather.setDescription(jo2.getString("description"));
                            weather.setFeels_like(jo2.getString("feels_like"));
                            weather.setMain(jo2.getString("main"));
                            weather.setTemp(jo2.getString("temp"));

                                    arrayofWeather.add(weather);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        JSONObject jo3=datas.getJSONObject(0);


                        weatherc.setCity(jo3.getString("city"));
                        weatherc.setDescription(jo3.getString("description"));
                        weatherc.setFeels_like(jo3.getString("feels_like"));
                        weatherc.setMain(jo3.getString("main"));
                        weatherc.setTemp(jo3.getString("temp"));



                        weatherResultCallback.getWeather(arrayofWeather,weatherc);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } else {
                    System.out.println("Weather body empty");
                    Log.e(TAG, "Weather body empty");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {


                Log.e(TAG, "weather request failed");
                t.printStackTrace();
            }
        });


    }


    public void searchWeather(WeatherResultCallback weatherResultCallback, String location) {
        String token = saveSharedPref.getToken(context);

        Log.e(TAG, "setweather: " + token);
        final theCommuterApiendpoints apiService = ApiClient.getClient().create(theCommuterApiendpoints.class);


        Call<JsonObject> call = apiService.getsearchWeather(location,"Bearer " + token);


        call.enqueue(new Callback<JsonObject>() {


            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {


                if (response.body() != null) {



                    String data = new Gson().toJson(response.body());

                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(String.valueOf(response.body()));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    JSONArray datas = null;
                    try {
                        datas = jsonObject.getJSONArray("data");

                        for (int i = 1; i < datas.length(); i++) {

                            try {
                                JSONObject jo2 = datas.getJSONObject(i);

                                Log.w(TAG, "Response: " + jo2);
                                Weather weather = new Weather();

                                weather.setDate(jo2.getString("date"));
                                weather.setDescription(jo2.getString("description"));
                                weather.setFeels_like(jo2.getString("feels_like"));
                                weather.setMain(jo2.getString("main"));
                                weather.setTemp(jo2.getString("temp"));

                                arrayofWeather.add(weather);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        JSONObject jo3=datas.getJSONObject(0);


                        weatherc.setCity(jo3.getString("city"));
                        weatherc.setDescription(jo3.getString("description"));
                        weatherc.setFeels_like(jo3.getString("feels_like"));
                        weatherc.setMain(jo3.getString("main"));
                        weatherc.setTemp(jo3.getString("temp"));



                        weatherResultCallback.getWeather(arrayofWeather,weatherc);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } else {
                    System.out.println("Weather body empty");
                    Log.e(TAG, "Weather body empty");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {


                Log.e(TAG, "weather request failed");
                t.printStackTrace();
            }
        });


    }
}
