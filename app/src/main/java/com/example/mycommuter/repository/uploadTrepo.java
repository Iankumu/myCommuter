package com.example.mycommuter.repository;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mycommuter.BottomNavigationActivity;
import com.example.mycommuter.MainActivity;
import com.example.mycommuter.RestApi.ApiClient;
import com.example.mycommuter.RestApi.theCommuterApiendpoints;
import com.example.mycommuter.TaskDetail;
import com.example.mycommuter.adapter.TaskAdapter;
import com.example.mycommuter.fragments.TodoFragment;
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
    private FragmentManager fragmentManager;

    private TaskAdapter adapter;

    public uploadTrepo(Context context, FragmentManager fragmentManager, MutableLiveData<String> title, MutableLiveData<String> description, MutableLiveData<String> due) {
        this.context = context;
        this.title = title;
        this.description = description;
        this.due = due;

    }

    //singleton
    public static uploadTrepo getInstance(Context context, FragmentManager fragmentManager, MutableLiveData<String> title, MutableLiveData<String> description, MutableLiveData<String> due) {

        if (instance == null) {
            instance = new uploadTrepo(context, fragmentManager, title, description, due);

        }
        return instance;
    }

    public boolean postTask() {

        settask(new TaskupdateCallback() {
            @Override
            public void onSuccess(String message) {

                adapter = new TaskAdapter();
                adapter.notifyDataSetChanged();
                instance=null;
                Toasty.success(context, message, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, BottomNavigationActivity.class);

                intent.putExtra("fragmentId","todo_id");


                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);



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
        MutableLiveData<String> livetitle = title;
        MutableLiveData<String> Livedescription = description;
        MutableLiveData<String> livedue = due;
        Log.i(TAG, "settask: "+title);

        Call<JsonObject> call = apiService.uploadTask(livetitle.getValue(), Livedescription.getValue(), livedue.getValue(), "Bearer " + token);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {


                if (response.body() != null) {


                    ;
                    taskupdateCallback.onSuccess("update Successful");


                } else {


                    taskupdateCallback.onError("update failed");
                }


            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

                t.printStackTrace();

                taskupdateCallback.onError("Invalid update");
            }
        });


    }
}
