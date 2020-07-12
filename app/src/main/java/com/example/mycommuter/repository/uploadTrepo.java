package com.example.mycommuter.repository;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mycommuter.BottomNavigationActivity;
import com.example.mycommuter.MainActivity;
import com.example.mycommuter.RestApi.ApiClient;
import com.example.mycommuter.RestApi.theCommuterApiendpoints;
import com.example.mycommuter.interfaces.LoginResultCallback;
import com.example.mycommuter.interfaces.TaskupdateCallback;
import com.example.mycommuter.model.LoginUser;
import com.example.mycommuter.model.Tasks;
import com.example.mycommuter.model.User;
import com.example.mycommuter.sharedPrefs.saveSharedPref;
import com.google.gson.JsonObject;

import java.util.Date;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;
import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;


public class uploadTrepo {
    private Context context;
    private static uploadTrepo instance;
    private MutableLiveData<Tasks> taskMutableLiveData;
    public MutableLiveData<String> title = new MutableLiveData<>();
    public MutableLiveData<String> description = new MutableLiveData<>();
     public MutableLiveData<String> due = new MutableLiveData<>();



    public uploadTrepo(Context context, MutableLiveData<String> title, MutableLiveData<String> description,MutableLiveData<String> due ) {
        this.context = context;
        this.title = title;
        this.description = description;
        this.due=due;

    }

    //singleton
    public static uploadTrepo getInstance(Context context,  MutableLiveData<String> title, MutableLiveData<String> description,MutableLiveData<String> due ) {

        if (instance == null) {
            instance = new uploadTrepo(context, title, description,due);

        }
        return instance;
    }
    public boolean postTask() {

        settask(new TaskupdateCallback() {
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

    private void settask(TaskupdateCallback taskupdateCallback) {
        final theCommuterApiendpoints apiService = ApiClient.getClient().create(theCommuterApiendpoints.class);
        String token = saveSharedPref.getToken(context);
        LiveData<String> livetitle = title;
        LiveData<String> Livedescription = description;
        LiveData<String> livedue = due;
        Log.e(TAG, "settatokesk: "+title.getValue() );
        Log.e(TAG, "settatokesk: "+description.getValue() );
        Log.e(TAG, "settatokesk: "+token );
        Call<JsonObject> call = apiService.uploadTask(livetitle.getValue(), Livedescription.getValue(),"Bearer "+token);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {


                if (response.body() != null) {
//                    LoginActivity.token = response.body().getAccess_token();

;
                    taskupdateCallback.onSuccess("update Successful");
                    Log.e(TAG, "onSuccess: Login complete");

                } else {
                    Log.e(TAG, "onSuccess: Login failed"+response.message());

                    taskupdateCallback.onError("update failed");
                }


            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

                t.printStackTrace();
                Log.e(TAG, "taskrepos"+t);
                taskupdateCallback.onError("Invalid update");
            }
        });


    }
}
