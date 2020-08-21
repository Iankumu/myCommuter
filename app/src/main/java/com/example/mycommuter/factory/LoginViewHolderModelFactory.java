package com.example.mycommuter.factory;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.mycommuter.LoginActivity;
import com.example.mycommuter.interfaces.LoginResultCallback;
import com.example.mycommuter.viewmodels.LoginActivityModelView;

public class LoginViewHolderModelFactory extends ViewModelProvider.AndroidViewModelFactory {

    Application application;
    LoginResultCallback loginResultCallback;

    public LoginViewHolderModelFactory(@NonNull Application application) {
        super(application);
        this.application = application;
    }


    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new LoginActivityModelView(application, loginResultCallback);
    }
}
