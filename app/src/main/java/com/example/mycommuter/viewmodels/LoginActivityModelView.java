package com.example.mycommuter.viewmodels;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mycommuter.BottomNavigationActivity;
import com.example.mycommuter.LoginActivity;
import com.example.mycommuter.MainActivity;
import com.example.mycommuter.PasswordResetActivity;
import com.example.mycommuter.RestApi.ApiClient;
import com.example.mycommuter.RestApi.theCommuterApiendpoints;
import com.example.mycommuter.interfaces.LoginResultCallback;
import com.example.mycommuter.interfaces.TaskupdateCallback;
import com.example.mycommuter.model.LoginUser;
import com.example.mycommuter.model.Tasks;
import com.example.mycommuter.model.User;
import com.example.mycommuter.repository.LoginRepo;
import com.example.mycommuter.repository.TasksRepo;
import com.google.gson.JsonObject;

import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;
import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;

public class LoginActivityModelView extends AndroidViewModel {

    private LoginResultCallback loginResultCallback;
    private LoginUser loginUser;
    private LoginRepo loginRepo;
    private Context context;
    public MutableLiveData<String> EmailAddress = new MutableLiveData<>();
    public MutableLiveData<String> Password = new MutableLiveData<>();


    public LoginActivityModelView(@NonNull Application application, LoginResultCallback loginResultCallback) {
        super(application);
        this.loginResultCallback = new LoginResultCallback() {
            @Override
            public void onSuccess(String message, String token) {
                Toasty.success(context, message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String message) {
                Toasty.error(context, message, Toast.LENGTH_SHORT).show();
            }
        };

        this.context = application.getApplicationContext();
        loginUser = new LoginUser();
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

        int error = loginUser.isValidlogin();
        if (error == 0) {
            loginResultCallback.onError("Your must enter an EmailAddress");
        } else if (error == 1) {
            loginResultCallback.onError("Your must enter a Valid EmailAddress");

        } else if (error == 2) {
            loginResultCallback.onError("You must enter a password");
        } else if (error == 3) {
            loginResultCallback.onError("Password is too short");
        } else {
            EmailAddress.setValue(loginUser.getEmailAddress());
            Password.setValue(loginUser.getPassword());
            loginUser();
        }


    }

    public void launchRegister(View view) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    public boolean loginUser() {
        loginRepo = LoginRepo.getInstance(context, EmailAddress, Password);

        return loginRepo.getUser();
    }

    public void launchPasswordReset(View view) {
        Intent intent = new Intent(context, PasswordResetActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    public void onResetClick(View view) {
        int error = loginUser.isValidlogin();
        if (error == 0) {
            loginResultCallback.onError("Your must enter an EmailAddress");
        } else if (error == 1) {
            loginResultCallback.onError("Your must enter a Valid EmailAddress");

        } else {
            EmailAddress.setValue(loginUser.getEmailAddress());
            sendResetEmail(new TaskupdateCallback() {
                @Override
                public void onSuccess(String message) {

                    Toasty.success(context, message, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(context, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TASK);
                    context.startActivity(intent);
                }

                @Override
                public void onError(String message) {
                    Toasty.error(context, message, Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void sendResetEmail(TaskupdateCallback taskupdateCallback) {

        final theCommuterApiendpoints apiService = ApiClient.getClient().create(theCommuterApiendpoints.class);


        Call<JsonObject> call = apiService.ResetEmail(EmailAddress.getValue());
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {


                try {


                    if (response.body() != null) {
//
                        taskupdateCallback.onSuccess("Password reset email sent");
                        Log.e(TAG, "onSuccess: Login complete");

                    } else {
                        Log.e(TAG, "onSuccess: Login failed");
                        taskupdateCallback.onError("Invalid credentials");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "onResponselogin mans " + response.errorBody());
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d(TAG, "login failed");
                t.printStackTrace();
                Log.e(TAG, ":Login failed");
                taskupdateCallback.onError("Invalid credentials");
            }
        });
    }

    @BindingAdapter("android:visibility")
    public static void setVisibility(View view, boolean visible) {
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

}