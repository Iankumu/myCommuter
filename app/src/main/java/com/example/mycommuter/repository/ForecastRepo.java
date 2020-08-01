package com.example.mycommuter.repository;

import android.content.Context;
import android.util.JsonToken;
import android.util.Log;

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
import com.example.mycommuter.interfaces.TaskInterface;
import com.example.mycommuter.interfaces.WeatherResultCallback;
import com.example.mycommuter.model.Tasks;
import com.example.mycommuter.model.Weather;
import com.example.mycommuter.sharedPrefs.saveSharedPref;
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
    private static ForecastRepo instance;
    private Context context;
//    GPSTracker gpsTracker = new GPSTracker(context);
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

    public MutableLiveData<List<Weather>> getWeather() {
        MutableLiveData<List<Weather>> data = new MutableLiveData<>();
        setWeather(new WeatherResultCallback() {
            @Override
            public void getWeather(List<Weather> weather) {
                data.setValue(arrayofWeather);
                Log.e("data", "" + data);

            }
        });

        return data;


    }

    //data retrieval from api
    public void setWeather(WeatherResultCallback weatherResultCallback) {
        String token = saveSharedPref.getToken(context);
        String stringlongitude = String.valueOf("36.8219");
        String stringLatitude = String.valueOf("1.2921");
        Log.e(TAG, "setweather: " + token);
        final theCommuterApiendpoints apiService = ApiClient.getClient().create(theCommuterApiendpoints.class);


        Call<JsonObject> call = apiService.getWeather(stringlongitude,stringLatitude,"Bearer " + token);


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
                        Log.e(TAG, "onResponse: "+datas);
                        for (int i = 0; i < datas.length(); i++) {

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

                        weatherResultCallback.getWeather(arrayofWeather);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } else {
                    System.out.println("Weather body empty");

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {


                Log.d(TAG, "weather request failed");
                t.printStackTrace();
            }
        });


    }
}
