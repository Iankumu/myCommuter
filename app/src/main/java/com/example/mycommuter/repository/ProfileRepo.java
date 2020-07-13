package com.example.mycommuter.repository;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mycommuter.BottomNavigationActivity;
import com.example.mycommuter.RestApi.ApiClient;
import com.example.mycommuter.RestApi.theCommuterApiendpoints;
import com.example.mycommuter.interfaces.TaskupdateCallback;
import com.example.mycommuter.model.User;
import com.example.mycommuter.sharedPrefs.saveSharedPref;
import com.google.gson.JsonObject;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;
import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;

public class ProfileRepo {
    private Context context;
    private static ProfileRepo instance;
    public MutableLiveData<String> username;

    public ProfileRepo(Context context, MutableLiveData<String> username) {
        this.context = context;
        this.username = username;
    }

    public static ProfileRepo getInstance(Context context, MutableLiveData<String> username) {

        if (instance == null) {
            instance = new ProfileRepo(context, username);

        }
        return instance;
    }

    public boolean postProfileUpdate() {

        setProfileUpdate(new TaskupdateCallback() {
            @Override
            public void onSuccess(String message) {

                Toasty.success(context, message, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, BottomNavigationActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);
                Log.e(TAG, "onSuccess: Login complete");
            }

            @Override
            public void onError(String message) {
                Toasty.error(context, message, Toast.LENGTH_SHORT).show();

            }
        });
        return true;
    }

    private void setProfileUpdate(TaskupdateCallback taskupdateCallback) {
        final theCommuterApiendpoints apiService = ApiClient.getClient().create(theCommuterApiendpoints.class);
        String token = saveSharedPref.getToken(context);
        LiveData<String> liveUsername = username;

        Log.e(TAG, "settatokesk: " + username.getValue());

        Log.e(TAG, "settatokesk: " + token);
        Call<User> call = apiService.setProfile(liveUsername.getValue(), "Bearer " + token);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                try {


                    if (response.body() != null) {
//                    LoginActivity.token = response.body().getAccess_token();

                        ;
                        taskupdateCallback.onSuccess("update Successful");
                        Log.e(TAG, "onSuccess: Login complete");

                    } else {
                        Log.e(TAG, "onSuccess: Login failed" + response.message());

                        taskupdateCallback.onError("update failed");
                    }

                } catch (Exception e) {
                    Log.e(TAG, "catch: Login failed" + e.toString());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

                t.printStackTrace();
                Log.e(TAG, "taskrepos" + t);
                taskupdateCallback.onError("Invalid update");
            }
        });
    }
}
