package com.example.mycommuter.repository;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mycommuter.BottomNavigationActivity;
import com.example.mycommuter.LoginActivity;
import com.example.mycommuter.RestApi.ApiClient;
import com.example.mycommuter.RestApi.theCommuterApiendpoints;
import com.example.mycommuter.interfaces.LoginResultCallback;
import com.example.mycommuter.model.LoginUser;
import com.example.mycommuter.model.User;
import com.example.mycommuter.sharedPrefs.saveSharedPref;


import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;
import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;


public class LoginRepo {
    private Context context;
    private static LoginRepo instance;
    private MutableLiveData<LoginUser> userMutableLiveData;
    public MutableLiveData<String> Email = new MutableLiveData<>();
    public MutableLiveData<String> Password = new MutableLiveData<>();


    public LoginRepo(Context context, MutableLiveData<String> email, MutableLiveData<String> pass) {
        this.context = context;
        this.Email = email;
        this.Password = pass;

    }

    //singleton
    public static LoginRepo getInstance(Context context, MutableLiveData<String> email, MutableLiveData<String> pass) {

        if (instance == null) {
            instance = new LoginRepo(context, email, pass);

        }
        return instance;
    }

    public boolean getUser() {
        MutableLiveData<Boolean> status = new MutableLiveData<>();
        checkCredentials(new LoginResultCallback() {
            @Override
            public void onSuccess(String message, String token) {
                try {
                    saveSharedPref.setLoggedIn(context, new Pair<>(true, token));
                    Toasty.success(context, message, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, BottomNavigationActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TASK);
                    context.startActivity(intent);
                    Log.e(TAG, "onSuccess: Login complete");

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "onErrormans: " + 1);

                }
                status.setValue(true);
            }

            @Override
            public void onError(String message) {
                Toasty.error(context, message, Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onSuccess: Login failed");
                status.setValue(false);
            }
        });
        return true;


    }

    private void checkCredentials(LoginResultCallback loginResultCallback) {
        final theCommuterApiendpoints apiService = ApiClient.getClient().create(theCommuterApiendpoints.class);

        LiveData<String> Liveemail = Email;
        LiveData<String> Livepassword = Password;
        Call<User> call = apiService.User(Liveemail.getValue(), Livepassword.getValue());
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {


                try {


                    if (response.body().getAccess_token() != null) {
//                    LoginActivity.token = response.body().getAccess_token();
                        Log.d(TAG, "login successful" + response.body().getAccess_token());
                        String token = response.body().getAccess_token();
                        loginResultCallback.onSuccess("Login Successful", token);
                        Log.e(TAG, "onSuccess: Login complete");

                    } else {
                        Log.e(TAG, "onSuccess: Login failed");
                        loginResultCallback.onError("Invalid credentials");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "onResponselogin mans " + response.errorBody());
                }

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d(TAG, "login failed");
                t.printStackTrace();
                Log.e(TAG, ":Login failed");
                loginResultCallback.onError("Invalid credentials");
            }
        });


    }
}
