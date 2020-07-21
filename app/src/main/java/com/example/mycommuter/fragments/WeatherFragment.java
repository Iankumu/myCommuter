package com.example.mycommuter.fragments;

import android.content.Intent;
import android.icu.lang.UCharacter;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mycommuter.R;

import com.example.mycommuter.RestApi.ApiClient;
import com.example.mycommuter.RestApi.theCommuterApiendpoints;
import com.example.mycommuter.adapter.RecyclerItemClickListener;

import com.example.mycommuter.adapter.WeatherAdapter;
import com.example.mycommuter.factory.ForecastViewHolderFactory;

import com.example.mycommuter.interfaces.CurrentWeather;
import com.example.mycommuter.interfaces.WeatherResultCallback;
import com.example.mycommuter.model.Weather;
import com.example.mycommuter.sharedPrefs.saveSharedPref;
import com.example.mycommuter.utils.IconProvider;
import com.example.mycommuter.viewmodels.ForecastActivityViewModel;
import com.example.mycommuter.viewmodels.HomeActivityViewModel;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class WeatherFragment extends Fragment {
    private static final String TAG = "TODOFRAGMENT";
    private WeatherAdapter weatherAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private Weather weather;
    private RecyclerView recyclerView;
    private List<Weather> listinit = new ArrayList<>();
    private LinearLayout linearLayout;
    private ForecastActivityViewModel forecastActivityViewModel;
    private ImageView weatherimg;
    private TextView temp, feelslike, description, city;

    public WeatherFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_weather, container, false);
        linearLayout = view.findViewById(R.id.linearcard);
        recyclerView = view.findViewById(R.id.myweatherRe);
        weatherimg = view.findViewById(R.id.WeatherCardWeatherIcon);
        temp = view.findViewById(R.id.WeatherCardCurrentTemp);
        feelslike = view.findViewById(R.id.WeatherCardfeelsLike);
        description = view.findViewById(R.id.weatherCardWeatherDescription);
        city = view.findViewById(R.id.WeatherCardCityName);


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        forecastActivityViewModel = new ViewModelProvider(this).get(ForecastActivityViewModel.class);

        forecastActivityViewModel.getWeather().observe(getViewLifecycleOwner(), new Observer<List<Weather>>() {

            @Override
            public void onChanged(@Nullable List<Weather> tasks) {

                listinit = tasks;
                initRecyclerView(listinit);


                weatherAdapter.notifyDataSetChanged();


            }
        });
        forecastActivityViewModel.getUpdate().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {

                if (aBoolean) {

                } else {

                    recyclerView.smoothScrollToPosition(forecastActivityViewModel.getWeather().getValue().size() - 1);
                }
            }
        });
        initRecyclerView(listinit);
        weatherset(new CurrentWeather() {
            @Override
            public void getCurrentWeather(Weather weather) {
                weatherimg.setBackgroundResource(IconProvider.getImageIcon(weather.getMain()));
                city.setText(weather.getCity());
                temp.setText(weather.getTemp());
                feelslike.setText(weather.getFeels_like());
                description.setText(weather.getDescription());
            }
        });

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(),
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                                weather = listinit.get(position);



                            }
                        }));

    }

    public void initRecyclerView(List<Weather> weather) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setHasFixedSize(true);


        weatherAdapter = new WeatherAdapter(getContext(), weather);

        recyclerView.setAdapter(weatherAdapter);

    }

    public void weatherset(CurrentWeather currentWeather) {
        String token = saveSharedPref.getToken(getContext());
        String stringlongitude = String.valueOf("36.8219");
        String stringLatitude = String.valueOf("1.2921");

        final theCommuterApiendpoints apiService = ApiClient.getClient().create(theCommuterApiendpoints.class);


        Call<JsonObject> call = apiService.getCurrentWeather(stringlongitude, stringLatitude, "Bearer " + token);


        call.enqueue(new Callback<JsonObject>() {


            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {


                if (response.body() != null) {


                    String data = new Gson().toJson(response.body());

                    JSONObject jo2 = null;
                    try {
                        JsonArray jsonArray = response.body().get("data").getAsJsonArray();

                        JsonElement jsonElement = jsonArray.get(0);
                        JsonObject obj = new JsonParser().parse(String.valueOf(jsonElement)).getAsJsonObject();

                        jo2 = new JSONObject(obj.toString());

                        Weather weather = new Weather();

                        weather.setCity(jo2.getString("city"));
                        weather.setDescription(jo2.getString("description"));
                        weather.setFeels_like(jo2.getString("feels_like"));
                        weather.setMain(jo2.getString("main"));
                        weather.setTemp(jo2.getString("temp"));
                        currentWeather.getCurrentWeather(weather);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }





                } else {
                    System.out.println("Weather body empty");

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {



                t.printStackTrace();
            }
        });


    }
}


