package com.example.mycommuter;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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

import com.example.mycommuter.common.BlurBuilder;
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
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    private Button loginBtn;
    private EditText emailEdit, passwordEdit;
    private Switch switch1;


    private LoginActivityModelView loginActivityModelView;

    ActivityLoginBinding binding;
    LinearLayout login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getActionBar().hide();
        if (saveSharedPref.getLoggedStatus(getApplicationContext())) {
            Intent intent = new Intent(getApplicationContext(), BottomNavigationActivity.class);
            startActivity(intent);
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        login = findViewById(R.id.loginlay);
        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.background);
        Bitmap blurredBitmap = BlurBuilder.blur(this, originalBitmap);
        login.setBackground(new BitmapDrawable(getResources(), blurredBitmap));


        loginActivityModelView = new ViewModelProvider(this, new LoginViewHolderModelFactory(getApplication()))
                .get(LoginActivityModelView.class);


        binding = DataBindingUtil.setContentView(LoginActivity.this, R.layout.activity_login);
        binding.setLifecycleOwner(this);

        binding.setLoginViewModel(loginActivityModelView);


    }


}