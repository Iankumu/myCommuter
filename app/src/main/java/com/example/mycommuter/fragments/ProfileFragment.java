package com.example.mycommuter.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.mycommuter.BottomNavigationActivity;
import com.example.mycommuter.EditProfile;
import com.example.mycommuter.LoginActivity;
import com.example.mycommuter.R;
import com.example.mycommuter.RestApi.ApiClient;
import com.example.mycommuter.RestApi.theCommuterApiendpoints;
import com.example.mycommuter.interfaces.UserProfile;
import com.example.mycommuter.model.User;
import com.example.mycommuter.model.Weather;
import com.example.mycommuter.sharedPrefs.saveSharedPref;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    Button button;
    TextView usernameTV, emailTv;
    FloatingActionButton floatingActionButton;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        usernameTV = view.findViewById(R.id.useremail);
        emailTv = view.findViewById(R.id.usersusername);
        button = view.findViewById(R.id.logoutbtn);
        floatingActionButton = view.findViewById(R.id.editprof);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EditProfile.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSharedPref.setLoggedIn(getContext(), new Pair<>(false, ""));
                Intent intent = new Intent(getContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        profile(new UserProfile() {
            @Override
            public void getProfile(User user) {
                emailTv.setText(user.getEmailAddress());
                usernameTV.setText(user.getUsername());
            }
        });
    }

    public void profile(UserProfile userProfile) {

        final theCommuterApiendpoints apiService = ApiClient.getClient().create(theCommuterApiendpoints.class);

        String token = saveSharedPref.getToken(getContext());
        Call<JsonObject> call = apiService.getProfile("Bearer " + token);
        call.enqueue(new Callback<JsonObject>()

        {
            @Override
            public void onResponse (Call < JsonObject > call, Response< JsonObject > response){


                if (response.body() != null) {
                    String data = new Gson().toJson(response.body());

                    JSONObject jo2 = null;
                    try {

                    JsonObject object = response.body().get("data").getAsJsonObject();
                        Log.e(TAG, "onResponseprof: "+object.get("2"));
                        JsonElement obj=object.get("2");
//                    JsonElement jsonElement = jsonArray.get(0);
//                    JsonObject obj = new JsonParser().parse(String.valueOf(jsonElement)).getAsJsonObject();
                    jo2 = new JSONObject(obj.toString());
                    User user =new User();
                        user.setEmailAddress(jo2.getString("email"));
                        user.setUsername(jo2.getString("name"));
                        userProfile.getProfile(user);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }

            @Override
            public void onFailure (Call < JsonObject > call, Throwable t){
                Log.d(TAG, "Registration failed");
                System.out.println(t);
            }
        });

    }

    @Override
    public void onStop() {
        super.onStop();

    }


}
