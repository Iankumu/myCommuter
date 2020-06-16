package com.example.mycommuter.viewmodels;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mycommuter.LoginActivity;
import com.example.mycommuter.RestApi.ApiClient;
import com.example.mycommuter.RestApi.theCommuterApiendpoints;
import com.example.mycommuter.interfaces.LoginResultCallback;
import com.example.mycommuter.model.LocationModel;
import com.example.mycommuter.model.LoginUser;
import com.example.mycommuter.model.User;
import com.example.mycommuter.repository.LoginRepo;
import com.example.mycommuter.sharedPrefs.saveSharedPref;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class LoginActivityModelView extends ViewModel {
    private LoginResultCallback loginResultCallback;
    private LoginUser loginUser;
    private LoginRepo loginRepo;
    LocationModel locationModel ;

    public LoginActivityModelView(LoginResultCallback loginResultCallback) {
        this.loginResultCallback = loginResultCallback;
        this.loginUser = new LoginUser();
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
                loginUser.setEmailAddress(s.toString());
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
                loginUser.setPassword(s.toString());
            }
        };
    }

    public void onLoginClick(View view) {
        System.out.println("email is " + loginUser.getEmailAddress());
        int error = loginUser.isValidlogin();
        if (error == 0) {
            loginResultCallback.onError("Your must enter an EmailAddress");
        } else if (error == 1) {
            loginResultCallback.onError("Your must enter a Valid EmailAddress");

        } else if (error == 2) {
            loginResultCallback.onError("Password is too short");
        } else {
            login(loginUser.getEmailAddress(), loginUser.getPassword());
        }


//
//        if(loginUser.isValidlogin(EmailAddress.getValue(), Password.getValue()))
//
//        loginRepo.login(EmailAddress.getValue(),Password.getValue());
////        LoginUser loginUser = new LoginUser(EmailAddress.getValue(), Password.getValue());
//
//        userMutableLiveData.setValue(loginUser);

    }

    public void login(String Email, String Password) {
        final theCommuterApiendpoints apiService = ApiClient.getClient().create(theCommuterApiendpoints.class);


        Call<User> call = apiService.User(Email, Password);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                Log.d(TAG, "login successful");
                if (response.body().getAccess_token() != null) {
//                    LoginActivity.token = response.body().getAccess_token();

                    String token = response.body().getAccess_token();
                    loginResultCallback.onSuccess("Login Successful",token, Email);

//                    locationModel.setEmail(Email);

                } else {

                    loginResultCallback.onError("Invalid credentials");
                }


            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d(TAG, "login failed");
                t.printStackTrace();
                loginResultCallback.onError("Invalid credentials");
            }
        });


    }
}