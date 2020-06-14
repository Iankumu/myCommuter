package com.example.mycommuter;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;
import static android.webkit.ConsoleMessage.MessageLevel.LOG;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.example.mycommuter.RestApi.ApiClient;
import com.example.mycommuter.RestApi.theCommuterApiendpoints;

import com.example.mycommuter.databinding.ActivityLoginBinding;
import com.example.mycommuter.factory.LoginViewHolderModelFactory;
import com.example.mycommuter.interfaces.LoginResultCallback;
import com.example.mycommuter.model.LoginUser;
import com.example.mycommuter.model.User;
import com.example.mycommuter.sharedPrefs.saveSharedPref;
import com.example.mycommuter.viewmodels.LoginActivityModelView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
//
//import com.example.mycommuter.sharedPrefs.saveSharedPref;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.Activity;
import android.content.Context;
import android.widget.Switch;
import android.widget.Toast;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity implements LoginResultCallback {
    private Button loginBtn;
    private EditText emailEdit, passwordEdit;
    private Switch switch1;




    private LoginActivityModelView loginActivityModelView;
    //
////
//
//    /
    ActivityLoginBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (saveSharedPref.getLoggedStatus(getApplicationContext())) {
            Intent intent = new Intent(getApplicationContext(), BottomNavigationActivity.class);
            startActivity(intent);
        }
        loginActivityModelView = new ViewModelProvider(this, new LoginViewHolderModelFactory(this))
                .get(LoginActivityModelView.class);


        binding = DataBindingUtil.setContentView(LoginActivity.this, R.layout.activity_login);
        binding.setLifecycleOwner(this);

        binding.setLoginViewModel(loginActivityModelView);


    }

    @Override
    public void onSuccess(String message,String token) {
        saveSharedPref.setLoggedIn(getApplicationContext(), new Pair<>(true,token));
        Toasty.success(this, message, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), BottomNavigationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }

    @Override
    public void onError(String message) {
        Toasty.success(this, message, Toast.LENGTH_SHORT).show();
    }


//    public void login(View view) {
//        final theCommuterApiendpoints apiService = ApiClient.getClient().create(theCommuterApiendpoints.class);
//
//        String emailT = emailEdit.getText().toString().trim();
//        String passwordT = passwordEdit.getText().toString().trim();
//
//
//        Call<User> call = apiService.User(emailT, passwordT);
//        call.enqueue(new Callback<User>() {
//            @Override
//            public void onResponse(Call<User> call, Response<User> response) {
//
//                Log.d(TAG, "login successful");
//                if (response.body().getAccess_token() != null) {
//                    token = response.body().getAccess_token();
//                    Log.d(TAG, token);
//                    SharedPreferences mPreferences = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
//                    saveSharedPref.setLoggedIn(getApplicationContext(), true);
//                    SharedPreferences.Editor preferencesEditor = mPreferences.edit();
//                    switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                        @Override
//                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                            if (isChecked) {
//                                Log.d(TAG, "it is checked");
//                                preferencesEditor.putString(email, emailT);
//                                preferencesEditor.putString(password, passwordT);
//
//                            } else {
//                                Log.d(TAG, "it not checked");
//                            }
//                        }
//                    });
//
//
//                    preferencesEditor.putString(toks, token);
//                    preferencesEditor.apply();
//                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TASK);
//                    startActivity(intent);
//                } else {
//                    token = "no access token generated";
//                }
//
//
//            }
//
//            @Override
//            public void onFailure(Call<User> call, Throwable t) {
//                Log.d(TAG, "login failed");
//                t.printStackTrace();
//            }
//        });

//    }


}