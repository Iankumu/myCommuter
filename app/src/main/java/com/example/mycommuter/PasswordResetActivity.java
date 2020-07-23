package com.example.mycommuter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.example.mycommuter.databinding.ActivityLoginBinding;
import com.example.mycommuter.databinding.ActivityPasswordResetBinding;
import com.example.mycommuter.factory.LoginViewHolderModelFactory;
import com.example.mycommuter.viewmodels.LoginActivityModelView;

public class PasswordResetActivity extends AppCompatActivity {

    private LoginActivityModelView loginActivityModelView;

    ActivityPasswordResetBinding binding;
    LinearLayout login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);

        loginActivityModelView = new ViewModelProvider(this, new LoginViewHolderModelFactory(getApplication()))
                .get(LoginActivityModelView.class);


        binding = DataBindingUtil.setContentView(PasswordResetActivity.this, R.layout.activity_password_reset);
        binding.setLifecycleOwner(this);

     binding.setLoginViewModel(loginActivityModelView);
    }
}