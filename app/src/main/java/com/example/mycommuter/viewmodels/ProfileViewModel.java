package com.example.mycommuter.viewmodels;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class ProfileViewModel extends AndroidViewModel {
    private Context context;

    public ProfileViewModel(@NonNull Application application) {
        super(application);
        this.context = application.getApplicationContext();
    }

}
