package com.example.mycommuter.factory;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.mycommuter.viewmodels.HomeActivityViewModel;
import com.example.mycommuter.viewmodels.ProfileViewModel;

public class ProfileViewModelFactory extends ViewModelProvider.AndroidViewModelFactory {

   private Application application;

    public ProfileViewModelFactory(@NonNull Application application) {
        super(application);
        this.application = application;
    }
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return(T) new ProfileViewModel(application);
    }
}
