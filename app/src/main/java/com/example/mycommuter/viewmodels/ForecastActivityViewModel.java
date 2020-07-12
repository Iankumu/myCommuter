package com.example.mycommuter.viewmodels;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


import com.example.mycommuter.model.Weather;
import com.example.mycommuter.repository.ForecastRepo;

import java.util.List;

public class ForecastActivityViewModel extends AndroidViewModel {
    private MutableLiveData<List<Weather>> weathers;
    private ForecastRepo forecastRepo;
    private MutableLiveData<Boolean> weatherUpdate = new MutableLiveData<>();
    private Context context;

    public ForecastActivityViewModel(@NonNull Application application) {
        super(application);
        this.context = application.getApplicationContext();
    }

    public LiveData<List<Weather>> getWeather() {
        forecastRepo =ForecastRepo.getInstance(context);
        return forecastRepo.getWeather();
    }

    public void addnewt(final Weather weather) {
        weatherUpdate.setValue(true);
        List<Weather> current = weathers.getValue();
        current.add(weather);
        weathers.postValue(current);
        weatherUpdate.setValue(false);
    }


    public LiveData<Boolean> getUpdate() {
        return weatherUpdate;
    }
}


