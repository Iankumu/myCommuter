package com.example.mycommuter.factory;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.mycommuter.viewmodels.ForecastActivityViewModel;
import com.example.mycommuter.viewmodels.NewTaskViewModel;

public class SearchWeatherFactory extends ViewModelProvider.AndroidViewModelFactory {

    Application application;
    String location;

    public SearchWeatherFactory(@NonNull Application application, String location) {
        super(application);
        this.application = application;
        this.location = location;
    }

//    @NonNull
//    @Override
//    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
//
//        return (T) new ForecastActivityViewModel(application,location);
//    }
}
