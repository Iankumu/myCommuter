package com.example.mycommuter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mycommuter.RestApi.ApiClient;
import com.example.mycommuter.RestApi.theCommuterApiendpoints;
import com.example.mycommuter.databinding.ActivityEditProfileBinding;
import com.example.mycommuter.databinding.ActivityLoginBinding;
import com.example.mycommuter.factory.LoginViewHolderModelFactory;
import com.example.mycommuter.factory.ProfileViewModelFactory;
import com.example.mycommuter.interfaces.UserProfile;
import com.example.mycommuter.model.User;
import com.example.mycommuter.sharedPrefs.saveSharedPref;
import com.example.mycommuter.viewmodels.LoginActivityModelView;
import com.example.mycommuter.viewmodels.ProfileViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;


public class EditProfile extends AppCompatActivity {
    Toolbar toolbar;
    FloatingActionButton uploadphoto;
    EditText username;
    TextView useremail;
    private ProfileViewModel profileViewModel;

    ActivityEditProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
//        toolbar = findViewById(R.id.editproftoolbar);
//        setSupportActionBar(toolbar);
//        useremail = findViewById(R.id.usersemail);
//        username = findViewById(R.id.usersusername);
//        uploadphoto = findViewById(R.id.uploadpic);
//        uploadphoto.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                uploadpic();
//            }
//        });
        profileViewModel = new ViewModelProvider(this, new ProfileViewModelFactory(getApplication()))
                .get(ProfileViewModel.class);
        binding = DataBindingUtil.setContentView(EditProfile.this, R.layout.activity_edit_profile);
        binding.setLifecycleOwner(this);

        binding.setEditProfile(profileViewModel);
//        profile(new UserProfile() {
//            @Override
//            public void getProfile(User user) {
//                useremail.setText(user.getEmailAddress());
//                username.setText(user.getUsername());
//            }
//        });
    }

    private void uploadpic() {
    }

    public void profile(UserProfile userProfile) {

        final theCommuterApiendpoints apiService = ApiClient.getClient().create(theCommuterApiendpoints.class);

        String token = saveSharedPref.getToken(this);
        Call<JsonObject> call = apiService.getProfile("Bearer " + token);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {


                if (response.body() != null) {
                    String data = new Gson().toJson(response.body());

                    JSONObject jo2 = null;
                    try {

                        JsonObject object = response.body().get("data").getAsJsonObject();
                        Log.e(TAG, "onResponseprof: " + object.get("2"));
                        JsonElement obj = object.get("2");
//                    JsonElement jsonElement = jsonArray.get(0);
//                    JsonObject obj = new JsonParser().parse(String.valueOf(jsonElement)).getAsJsonObject();
                        jo2 = new JSONObject(obj.toString());
                        User user = new User();
                        user.setEmailAddress(jo2.getString("email"));
                        user.setUsername(jo2.getString("name"));
                        userProfile.getProfile(user);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d(TAG, "Registration failed");
                System.out.println(t);
            }
        });

    }
}
