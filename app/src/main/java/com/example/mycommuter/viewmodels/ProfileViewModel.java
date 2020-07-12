package com.example.mycommuter.viewmodels;

import android.app.Application;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.mycommuter.interfaces.ProfileUpdateCallback;
import com.example.mycommuter.model.User;
import com.example.mycommuter.repository.ProfileRepo;
import com.example.mycommuter.repository.uploadTrepo;

import es.dmoral.toasty.Toasty;

public class ProfileViewModel extends AndroidViewModel {
    private Context context;
    private ProfileUpdateCallback profileUpdateCallback;
    private User user;
    private ProfileRepo profileRepo;
    public MutableLiveData<String> username = new MutableLiveData<>();

    public ProfileViewModel(@NonNull Application application) {
        super(application);
        this.context = application.getApplicationContext();
        user = new User();
        profileUpdateCallback = new ProfileUpdateCallback() {
            @Override
            public void onSuccess(String message) {
                Toasty.success(context, message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String message) {
                Toasty.error(context, message, Toast.LENGTH_SHORT).show();
            }
        };
    }

    public TextWatcher getNameOnTextChangeListener() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                user.setUsername(s.toString());
            }
        };
    }

    public void onProfileUpdateClick(View view) {
        int error = user.isUserNameValid();
        if (error == -1) {
            profileUpdateCallback.onError("Your must enter a task");
        } else {
            username.setValue(user.getUsername());

            setProfileUpdate();

        }
    }

    private void setProfileUpdate() {
        profileRepo = ProfileRepo.getInstance(context, username);
        profileRepo.postProfileUpdate();
    }


}
