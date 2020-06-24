package com.example.mycommuter.viewmodels;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;

import com.example.mycommuter.LoginActivity;
import com.example.mycommuter.MainActivity;
import com.example.mycommuter.RestApi.ApiClient;
import com.example.mycommuter.RestApi.theCommuterApiendpoints;
import com.example.mycommuter.interfaces.SignupResultCallback;
import com.example.mycommuter.model.SignupUser;
import com.example.mycommuter.model.User;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;

public class MainActivityViewModel extends AndroidViewModel {
    private static final String TAG = "MainActivitymodelview";
    private SignupResultCallback signupResultCallback;
    private SignupUser signupUser;
    private Context context;


    public MainActivityViewModel(SignupResultCallback signupResultCallback, Application application) {
        super(application);
        context=application;
        this.signupResultCallback =signupResultCallback;
        this.signupUser = new SignupUser();
    }

    public TextWatcher getEmailTextWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                signupUser.setEmailAddress(s.toString());
            }
        };
    }

    public TextWatcher getPasswordWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                signupUser.setPassword(s.toString());
            }
        };
    }

    public TextWatcher getConfirmPasswordwatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                signupUser.setConfirmPassword(s.toString());
            }
        };
    }

    public TextWatcher getUsernameWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                signupUser.setUsername(s.toString());
            }
        };
    }
    public void  launchLogin(View view){
        Log.e(TAG, "launchLogin: clicked on login");
        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }
    public void onSignupClick(View view) {
        Log.e(TAG, "launchLogin: clicked on login");
        System.out.println("email is " + signupUser.getPassword());
        int error = signupUser.isValidSignup();
        if (error == 0) {
            signupResultCallback.onError("Your must enter an EmailAddress");
        } else if (error == 1) {
            signupResultCallback.onError("Your must enter a Valid EmailAddress");

        } else if (error == 2) {
            signupResultCallback.onError("Password is too short");
        } else {
            Signup(signupUser.getUsername(), signupUser.getEmailAddress(), signupUser.getPassword(), signupUser.getConfirmPassword());
        }

    }

    private void Signup(String name, String email, String password, String confirmpass) {
        final theCommuterApiendpoints apiService = ApiClient.getClient().create(theCommuterApiendpoints.class);


        Call<User> call = apiService.User(name, email, password, confirmpass);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Log.d(TAG, "Registration successful");
                if (response.body().getAccess_token() != null) {
                    String token = response.body().getAccess_token();
                    signupResultCallback.onSuccess("Login Successful", token);
                } else {
                    signupResultCallback.onError("Network during sign up, please try again");
                }

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d(TAG, "Registration failed");
                System.out.println(t);
                signupResultCallback.onError("Error in sign up, please try again");
            }
        });
    }

}
