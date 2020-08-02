package com.example.mycommuter.viewmodels;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;


import com.example.mycommuter.model.Weather;
import com.example.mycommuter.repository.ForecastRepo;
import com.example.mycommuter.utils.IconProvider;
import com.example.mycommuter.utils.myPairs;

import java.util.List;

import timber.log.Timber;

import static android.content.ContentValues.TAG;

public class ForecastActivityViewModel extends AndroidViewModel {
    private MutableLiveData<List<Weather>> weathers;
    private MutableLiveData<Weather> weather;
    private ForecastRepo forecastRepo;
    MutableLiveData<Pair<List, Weather>> wth;
    private MutableLiveData<Boolean> weatherUpdate = new MutableLiveData<>();
    private Context context;

    public MutableLiveData<String> city = new MutableLiveData<>();
    public MutableLiveData<String> temp = new MutableLiveData<>();
    public MutableLiveData<String> description = new MutableLiveData<>();
    public MutableLiveData<String> weathericon = new MutableLiveData<>();

    public ForecastActivityViewModel(@NonNull Application application) {
        super(application);
        this.context = application.getApplicationContext();


    }

//    public ForecastActivityViewModel(Application application) {
//        super(application);
//    }

    public MutableLiveData<Pair<List, Weather>> Search(String location) {
        Log.e(TAG, "Search: "+location );
        forecastRepo = ForecastRepo.getInstance(context);
        return forecastRepo.getSearchWeather(location);
    }

    public void init() {
        if (wth != null) {
            return;
        }
        forecastRepo = ForecastRepo.getInstance(context);
        wth = forecastRepo.getWeather();
    }


    public MutableLiveData<Pair<List, Weather>> getCurrentWeather() {
        return wth;

    }

    public void addnewt(final Weather weather) {
        weatherUpdate.setValue(true);
        List<Weather> current = weathers.getValue();
        current.add(weather);
        weathers.postValue(current);
        weatherUpdate.setValue(false);
    }
//    public void setitems(Weather weather){
//        city.setValue();
//
//    }

    public LiveData<Boolean> getUpdate() {
        return weatherUpdate;
    }

}


