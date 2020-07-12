package com.example.mycommuter.viewmodels;

import android.app.Application;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.mycommuter.interfaces.LoginResultCallback;
import com.example.mycommuter.interfaces.TaskupdateCallback;
import com.example.mycommuter.model.Tasks;
import com.example.mycommuter.repository.uploadTrepo;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import es.dmoral.toasty.Toasty;

public class NewTaskViewModel extends AndroidViewModel {
    FragmentManager fragmentManager;
    private Tasks tasksM;
    private uploadTrepo uploadtrepo;
    private TaskupdateCallback taskupdateCallback;
    public MutableLiveData<String> title = new MutableLiveData<>();
    public MutableLiveData<String> description = new MutableLiveData<>();
    public MutableLiveData<String> due = new MutableLiveData<>();
    Context context;
    public NewTaskViewModel(@NonNull Application application, FragmentManager fragmentManager) {
        super(application);
        this.context=application.getApplicationContext();
        this.fragmentManager=fragmentManager;
        tasksM=new Tasks();
        this.taskupdateCallback = new TaskupdateCallback() {
            @Override
            public void onSuccess(String message) {
                Toasty.success(context, message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String message) {
                Toasty.error(context, message, Toast.LENGTH_SHORT).show();
            }
        };
    }

    public TextWatcher getTitleOnTextChangeListener() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                tasksM.setTitle(s.toString());
            }
        };
    }

    public TextWatcher getdescriptionOnTextChangeListener() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                tasksM.setDescritpion(s.toString());
            }
        };
    }

    public void datePick(View view) {
        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Due date");
        MaterialDatePicker materialDatePicker = builder.build();
        materialDatePicker.show( fragmentManager, "Date picker");
        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                tasksM.setDue(materialDatePicker.getHeaderText());
            }
        });
    }


    public void onPost(View view) {
        int error = tasksM.isValidTask();
        if (error == 0) {
            taskupdateCallback.onError("Your must enter a task");
        } else {
            title.setValue(tasksM.getTitle());
            description.setValue(tasksM.getDescritpion());
            due.setValue(tasksM.getDue());
            setTask();

        }
    }

    private void setTask() {

        uploadtrepo = uploadTrepo.getInstance(context, title, description, due);
        uploadtrepo.postTask();
    }

}
