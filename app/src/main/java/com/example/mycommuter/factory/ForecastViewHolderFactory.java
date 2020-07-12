package com.example.mycommuter.factory;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.mycommuter.viewmodels.ForecastActivityViewModel;
import com.example.mycommuter.viewmodels.HomeActivityViewModel;

public class ForecastViewHolderFactory extends ViewModelProvider.AndroidViewModelFactory {
    /**
     * Creates a {@code AndroidViewModelFactory}
     *
     * @param application an application to pass in {@link AndroidViewModel}
     */
    Application application;
    public ForecastViewHolderFactory(@NonNull Application application) {
        super(application);
        this.application=application;
    }
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return(T)new ForecastActivityViewModel(application);
    }
}
