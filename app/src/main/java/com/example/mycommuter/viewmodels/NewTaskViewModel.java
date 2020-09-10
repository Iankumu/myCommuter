package com.example.mycommuter.viewmodels;

import android.app.Application;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.mycommuter.common.TimePickerDialogue;
import com.example.mycommuter.interfaces.LoginResultCallback;
import com.example.mycommuter.interfaces.TaskupdateCallback;
import com.example.mycommuter.model.Tasks;
import com.example.mycommuter.repository.uploadTrepo;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import es.dmoral.toasty.Toasty;

import static android.content.ContentValues.TAG;

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
        this.context = application.getApplicationContext();
        this.fragmentManager = fragmentManager;
        tasksM = new Tasks();
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
                Log.e(TAG, "afterTextChanged: "+s.toString() );
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
        materialDatePicker.show(fragmentManager, "Date picker");
        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {

                tasksM.setDue(materialDatePicker.getHeaderText());
                Toasty.info(context, materialDatePicker.getHeaderText()).show();
            }
        });
        materialDatePicker.addOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
            }
        });
    }
//    public void timePick(String date) {
//        TimePickerDialogue timePickerDialogue=new TimePickerDialogue();
//        timePickerDialogue.show(fragmentManager,"timepicker");
//
//    }


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

        uploadtrepo = uploadTrepo.getInstance(context, fragmentManager, title, description, due);
        Log.e(TAG, "setTask: title is"+title.getValue() );
        uploadtrepo.postTask();
        destroy();
    }
    public void destroy(){
        title=new MutableLiveData<>();
        description=new MutableLiveData<>();
        due=new MutableLiveData<>();
    }

//    @Override
//    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//        tasksM.setDue(materialDatePicker.getHeaderText());
//    }
}
