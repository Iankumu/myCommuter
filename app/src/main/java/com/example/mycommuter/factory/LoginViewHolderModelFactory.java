package com.example.mycommuter.factory;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.mycommuter.interfaces.LoginResultCallback;
import com.example.mycommuter.viewmodels.LoginActivityModelView;

public class LoginViewHolderModelFactory extends ViewModelProvider.NewInstanceFactory {
    private LoginResultCallback loginResultCallback;
    public LoginViewHolderModelFactory(LoginResultCallback loginResultCallback){
        this.loginResultCallback=loginResultCallback;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return(T)new LoginActivityModelView(loginResultCallback);
    }
}
