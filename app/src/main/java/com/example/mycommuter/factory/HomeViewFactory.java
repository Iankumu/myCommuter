package com.example.mycommuter.factory;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.mycommuter.viewmodels.HomeActivityViewModel;
import com.example.mycommuter.viewmodels.LoginActivityModelView;

public class HomeViewFactory extends ViewModelProvider.AndroidViewModelFactory {
    Application application;

    public HomeViewFactory(@NonNull Application application) {
        super(application);
        this.application = application;

    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new HomeActivityViewModel(application);
    }
}
