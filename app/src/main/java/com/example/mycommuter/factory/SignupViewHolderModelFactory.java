package com.example.mycommuter.factory;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.mycommuter.interfaces.SignupResultCallback;
import com.example.mycommuter.model.SignupUser;
import com.example.mycommuter.viewmodels.MainActivityViewModel;

import es.dmoral.toasty.Toasty;

public class SignupViewHolderModelFactory extends ViewModelProvider.AndroidViewModelFactory {
    private SignupResultCallback signupResultCallback;

    private Application application;

    /**
     * Creates a {@code AndroidViewModelFactory}
     *
     * @param application an application to pass in {@link AndroidViewModel}
     */
    public SignupViewHolderModelFactory(@NonNull Application application) {
        super(application);
        this.application = application;
        signupResultCallback = new SignupResultCallback() {
            @Override
            public void onSuccess(String message, String token) {
                Toasty.success(application, message, Toasty.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String message) {
                Toasty.error(application, message, Toasty.LENGTH_SHORT).show();
            }
        };
    }


    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MainActivityViewModel(signupResultCallback, application);
    }
}
