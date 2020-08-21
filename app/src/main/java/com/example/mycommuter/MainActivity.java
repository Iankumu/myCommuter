package com.example.mycommuter;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.mycommuter.RestApi.ApiClient;
import com.example.mycommuter.RestApi.theCommuterApiendpoints;
import com.example.mycommuter.databinding.ActivityMainBinding;
import com.example.mycommuter.factory.LoginViewHolderModelFactory;
import com.example.mycommuter.factory.SignupViewHolderModelFactory;
import com.example.mycommuter.model.User;
import com.example.mycommuter.sharedPrefs.saveSharedPref;
import com.example.mycommuter.viewmodels.LoginActivityModelView;
import com.example.mycommuter.viewmodels.MainActivityViewModel;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Registration";

    public static final String SHARED_PREF = "sharedPrefs";

    public static final String Verified = "VerifiedPref";
    ActivityMainBinding mainBinding;
    MainActivityViewModel mainActivityViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainActivityViewModel = new ViewModelProvider(this, new SignupViewHolderModelFactory(getApplication()))
                .get(MainActivityViewModel.class);


        mainBinding = DataBindingUtil.setContentView(MainActivity.this, R.layout.activity_main);
        mainBinding.setLifecycleOwner(this);

        mainBinding.setMainActivityViewModel(mainActivityViewModel);


    }


//        final theCommuterApiendpoints apiService = ApiClient.getClient().create(theCommuterApiendpoints.class);
//        String name = nameEdit.getText().toString().trim();
//        String email = emailEdit.getText().toString().trim();
//        String password = passwordEdit.getText().toString().trim();
//        String confirmpass = confirmpassEdit.getText().toString().trim();
//
//
//        Call<User> call = apiService.User(name, email, password, confirmpass);
//        call.enqueue(new Callback<User>() {
//            @Override
//            public void onResponse(Call<User> call, Response<User> response) {
//                Log.d(TAG, "Registration successful");
//                if (response.body().getAccess_token() != null) {
//                    token = response.body().getAccess_token();
//                } else {
//                    token = "no access token generated";
//                }
//                showAlertDialogButtonClicked();
//            }
//
//            @Override
//            public void onFailure(Call<User> call, Throwable t) {
//                Log.d(TAG, "Registration failed");
//                System.out.println(t);
//            }
//        });


//    public void showAlertDialogButtonClicked() {
//        View view;
//        // setup the alert builder
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("AlertDialog");
//        builder.setMessage("Would you like to continue learning how to use Android alerts?");
//        // add the buttons
//
//        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//
//            public void onClick(DialogInterface arg0, int arg1) {
//                verifyAcc();
//            }
//        });
//
//        builder.setNegativeButton("Cancel", null);
//        // create and show the alert dialog
//        AlertDialog dialog = builder.create();
//        dialog.show();
//    }
//
//    private void verifyAcc() {
//        final theCommuterApiendpoints apiService = ApiClient.getClient().create(theCommuterApiendpoints.class);
//
//
//        Call<User> call = apiService.getVerified("Bearer " + token);
//        call.enqueue(new Callback<User>() {
//            @Override
//            public void onResponse(Call<User> call, Response<User> response) {
//
//                Log.d(TAG, "login successful");
//                showDiag();
//
//            }
//
//            @Override
//            public void onFailure(Call<User> call, Throwable t) {
//                Log.d(TAG, "login failed");
//                t.printStackTrace();
//            }
//        });
//    }
//
//    private void showDiag() {
//        View view;
//        // setup the alert builder
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//
//        builder.setMessage("Proceed to your email address to verify you account");
//
//
//        AlertDialog dialog = builder.create();
//        dialog.show();
//    }


}

