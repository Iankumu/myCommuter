package com.example.mycommuter.repository;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.mycommuter.RestApi.ApiClient;
import com.example.mycommuter.RestApi.theCommuterApiendpoints;
import com.example.mycommuter.interfaces.TaskInterface;
import com.example.mycommuter.model.Tasks;
import com.example.mycommuter.sharedPrefs.saveSharedPref;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

import static android.content.ContentValues.TAG;

public class TasksRepo {
    private List<Tasks> arrayofTask = new ArrayList<>();
    private static TasksRepo instance;
    private Context context;


    public TasksRepo(Context context) {
        this.context = context;
    }

    //singleton
    public static TasksRepo getInstance(Context context) {

        if (instance == null) {
            instance = new TasksRepo(context);

        }
        return instance;
    }

    public MutableLiveData<List<Tasks>> getTasks() {
        MutableLiveData<List<Tasks>> data = new MutableLiveData<>();
        setTasks(new TaskInterface() {
            @Override
            public void getTask(List<Tasks> tasks) {

                data.setValue(arrayofTask);


            }
        });

        return data;


    }

    //data retrieval from api
    public void setTasks(TaskInterface taskscallback) {
        String token = saveSharedPref.getToken(context);

        final theCommuterApiendpoints apiService = ApiClient.getClient().create(theCommuterApiendpoints.class);


        Call<Tasks> call = apiService.getTasks("Bearer " + token);


        call.enqueue(new Callback<Tasks>() {


            @Override
            public void onResponse(Call<Tasks> call, retrofit2.Response<Tasks> response) {


                if (response.body() != null) {

                    String data = new Gson().toJson(response.body());

                    JsonArray jsonArray = new JsonParser().parse(data).getAsJsonArray();
                    for (JsonElement e : jsonArray) {

                        JsonObject obj = new JsonParser().parse(String.valueOf(e)).getAsJsonObject();
                        try {
                            JSONObject jo2 = new JSONObject(obj.toString());

                            Tasks task = new Tasks();
                            task.setId(jo2.getInt("id"));
                            task.setTitle(jo2.getString("title"));
                            task.setDescritpion(jo2.getString("descritpion"));
                            task.setDue(jo2.getString("due"));


                            arrayofTask.add(task);

                        } catch (JSONException ex) {
                            ex.printStackTrace();
                        }

                        taskscallback.getTask(arrayofTask);
                    }


                } else {


                }
            }

            @Override
            public void onFailure(Call<Tasks> call, Throwable t) {



                t.printStackTrace();
            }
        });


    }
}
