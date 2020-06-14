package com.example.mycommuter.viewmodels;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import androidx.lifecycle.ViewModel;

import com.example.mycommuter.RestApi.ApiClient;
import com.example.mycommuter.RestApi.theCommuterApiendpoints;
import com.example.mycommuter.interfaces.SignupResultCallback;
import com.example.mycommuter.model.SignupUser;
import com.example.mycommuter.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivityViewModel extends ViewModel {
    private static final String TAG = "MainActivitymodelview";
    private SignupResultCallback signupResultCallback;
    private SignupUser signupUser;


    public MainActivityViewModel(SignupResultCallback signupResultCallback) {
        this.signupResultCallback = signupResultCallback;
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

    public void onSignupClick(View view) {
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
