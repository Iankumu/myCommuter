package com.example.mycommuter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.widget.LinearLayout;

import com.example.mycommuter.adapter.WeatherAdapter;
import com.example.mycommuter.databinding.ActivityWeatherSearchBinding;
import com.example.mycommuter.factory.ForecastViewHolderFactory;
import com.example.mycommuter.factory.NewTaskViewFactory;
import com.example.mycommuter.factory.SearchWeatherFactory;
import com.example.mycommuter.model.Weather;
import com.example.mycommuter.viewmodels.ForecastActivityViewModel;
import com.example.mycommuter.viewmodels.NewTaskViewModel;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class WeatherSearchActivity extends AppCompatActivity {
    private ForecastActivityViewModel forecastActivityViewModel;
    private List<Weather> listinit = new ArrayList<>();
    private RecyclerView recyclerView;
    private WeatherAdapter weatherAdapter;
    private LinearLayout linearLayout;
    private String locationSearch;
    ActivityWeatherSearchBinding activityWeatherSearchBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       activityWeatherSearchBinding= DataBindingUtil.setContentView(this,R.layout.activity_weather_search);
       activityWeatherSearchBinding.getRoot();


        linearLayout = findViewById(R.id.linearcard);
        recyclerView = findViewById(R.id.myweatherRe1);
        initRecyclerView(listinit);
//        forecastActivityViewModel = new ViewModelProvider(this).get(ForecastActivityViewModel.class);
//        forecastActivityViewModel.init();
        Intent intent = getIntent();
        locationSearch= intent.getStringExtra("location");
        Log.e(TAG, "Search: "+locationSearch );
    forecastActivityViewModel = new ViewModelProvider(this, new ForecastViewHolderFactory(getApplication()))
                .get(ForecastActivityViewModel.class);
        forecastActivityViewModel.Search(locationSearch).observe(this, new Observer<Pair<List, Weather>>() {
            @Override
            public void onChanged(Pair<List, Weather> listWeatherPair) {
                Weather weather1 = listWeatherPair.second;
                forecastActivityViewModel.city.setValue(weather1.getCity());
                forecastActivityViewModel.description.setValue(weather1.getDescription());
                forecastActivityViewModel.temp.setValue(weather1.getTemp() + "°");
                forecastActivityViewModel.weathericon.setValue(weather1.getMain());

                listinit = listWeatherPair.first;
                initRecyclerView(listinit);
                weatherAdapter.notifyDataSetChanged();
            }
        });


        activityWeatherSearchBinding.setWeathermodel(forecastActivityViewModel);
        activityWeatherSearchBinding.setLifecycleOwner(this);


    }

    public void initRecyclerView(List<Weather> weather) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setHasFixedSize(true);


        weatherAdapter = new WeatherAdapter(this, weather);

        recyclerView.setAdapter(weatherAdapter);

    }

}