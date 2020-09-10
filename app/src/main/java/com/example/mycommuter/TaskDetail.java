package com.example.mycommuter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.TextViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mycommuter.RestApi.ApiClient;
import com.example.mycommuter.RestApi.theCommuterApiendpoints;
import com.example.mycommuter.fragments.TodoFragment;
import com.example.mycommuter.interfaces.ProfileUpdateCallback;
import com.example.mycommuter.model.Tasks;
import com.example.mycommuter.model.User;
import com.example.mycommuter.sharedPrefs.saveSharedPref;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class TaskDetail extends AppCompatActivity {
    TextView title, due, description;

    Tasks tasksdetail;
    Toolbar contextualtoolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        contextualtoolbar = findViewById(R.id.contextualToolbar);
        setSupportActionBar(contextualtoolbar);
        title = findViewById(R.id.detailtitle);
        due = findViewById(R.id.detaildue);
        description = findViewById(R.id.detaildescription);

        tasksdetail = (Tasks) getIntent().getSerializableExtra("task");

        title.setText(tasksdetail.getTitle());
        description.setText(tasksdetail.getDescritpion());
        due.setText(tasksdetail.getDue());

    }

    public void delete(View view) {
        showDeletePromptDialog();
    }

    private void showDeletePromptDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        // Setting Dialog Title
        alertDialog.setTitle("Delete task");

        // Setting Dialog Message
        alertDialog.setMessage("Delete " + tasksdetail.getTitle() + "?");

        // Setting Positive "Yes" Btn
        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // create game and go there

                        // navigate to game screen
                        deleteAPIcall(new ProfileUpdateCallback() {
                            @Override
                            public void onSuccess(String message) {
                                Toasty.success(TaskDetail.this, message, Toast.LENGTH_SHORT).show();
                                Intent dintent = new Intent(TaskDetail.this, BottomNavigationActivity.class);
                                dintent.putExtra("fragmentId","todo_id");
                                startActivity(dintent);

                            }

                            @Override
                            public void onError(String message) {
                                Toasty.error(TaskDetail.this, message, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

        // Setting Negative "NO" Btn
        alertDialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();
                    }
                });

        // Showing Alert Dialog
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        Intent dintent = new Intent(TaskDetail.this, BottomNavigationActivity.class);
        dintent.putExtra("fragmentId","todo_id");
        startActivity(dintent);

    }

    private void deleteAPIcall(ProfileUpdateCallback profileUpdateCallback) {
        final theCommuterApiendpoints apiService = ApiClient.getClient().create(theCommuterApiendpoints.class);

        String token = saveSharedPref.getToken(TaskDetail.this);
        Call<JsonObject> call = apiService.deleteTask(tasksdetail.getId(), "Bearer " + token);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {




                    JSONObject jo2 = null;
                    try {


//                        myfragmentManager.beginTransaction().add(R.id.frag_container, new TodoFragment()).commit();
                        finish();
                        profileUpdateCallback.onSuccess("successful delete");
                        JsonObject object = response.body();
                        Log.e(TAG, "onResponsesuccess: " + response.message());

                    } catch (Exception e) {
                        profileUpdateCallback.onSuccess("issues delete");
                        e.printStackTrace();
                    }


            }


            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d(TAG, "Registration failed");
                System.out.println(t);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.contextual_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.delete_id:
                Log.e(TAG, "onOptionsItemSelecteddelete ");
                showDeletePromptDialog();
                break;
            case R.id.edit_id:
                showUpdatePromptDialog();
                break;

        }
        return super.onOptionsItemSelected(item);

    }

    private void showUpdatePromptDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        // Setting Dialog Title
        alertDialog.setTitle("Update task");

        // Setting Dialog Message
        alertDialog.setMessage("Update " + tasksdetail.getTitle() + "?");

        // Setting Positive "Yes" Btn
        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // create game and go there

                        // navigate to game screen
                        UpdateApiCall(new ProfileUpdateCallback() {
                            @Override
                            public void onSuccess(String message) {
                                Toasty.success(TaskDetail.this, message, Toast.LENGTH_SHORT).show();

                                Intent dintent = new Intent(TaskDetail.this, BottomNavigationActivity.class);
                                dintent.putExtra("fragmentId","todo_id");
                                startActivity(dintent);
                            }

                            @Override
                            public void onError(String message) {
                                Toasty.error(TaskDetail.this, message, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

        // Setting Negative "NO" Btn
        alertDialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();
                    }
                });

        // Showing Alert Dialog
        alertDialog.show();
    }

    private void UpdateApiCall(ProfileUpdateCallback profileUpdateCallback) {
        final theCommuterApiendpoints apiService = ApiClient.getClient().create(theCommuterApiendpoints.class);
        String titletxt = title.getText().toString().trim();
        String descriptiontxt = description.getText().toString().trim();
        String duetext = due.getText().toString().trim();
        String token = saveSharedPref.getToken(TaskDetail.this);
        Call<JsonObject> call = apiService.updateTask(tasksdetail.getId(), titletxt, descriptiontxt, duetext, "Bearer " + token);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {


                if (response.body() != null) {
                    String data = new Gson().toJson(response.body());

                    JSONObject jo2 = null;
                    try {
                        profileUpdateCallback.onSuccess("Task successfully updated");
                        JsonObject object = response.body();


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {

                    profileUpdateCallback.onError("Task update failed");
                }
            }


            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

                System.out.println(t);
            }
        });

    }

    public void datePicking(View view) {
        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Due date");
        MaterialDatePicker materialDatePicker = builder.build();
        materialDatePicker.show(getSupportFragmentManager(), "Date picker");
        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {

                due.setText(materialDatePicker.getHeaderText());
                Toasty.info(TaskDetail.this, materialDatePicker.getHeaderText()).show();
            }
        });
        materialDatePicker.addOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
            }
        });
    }
}