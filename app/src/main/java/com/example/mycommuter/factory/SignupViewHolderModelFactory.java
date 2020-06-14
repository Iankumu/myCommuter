package com.example.mycommuter.factory;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.mycommuter.interfaces.SignupResultCallback;
import com.example.mycommuter.model.SignupUser;
import com.example.mycommuter.viewmodels.MainActivityViewModel;

public class SignupViewHolderModelFactory extends ViewModelProvider.NewInstanceFactory {
    private SignupResultCallback signupResultCallback;


    public SignupViewHolderModelFactory(SignupResultCallback signupResultCallback) {
        this.signupResultCallback = signupResultCallback;

    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MainActivityViewModel(signupResultCallback);
    }
}
