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
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mycommuter.interfaces.LoginResultCallback;
import com.example.mycommuter.model.Tasks;
import com.example.mycommuter.repository.TasksRepo;
import com.example.mycommuter.repository.uploadTrepo;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.util.List;

import es.dmoral.toasty.Toasty;

public class HomeActivityViewModel extends AndroidViewModel {
    private MutableLiveData<List<Tasks>> tasks;
    private TasksRepo tasksRepo;

    private MutableLiveData<Boolean> taskUpdate = new MutableLiveData<>();
    private Context context ,ctx;



    public HomeActivityViewModel(@NonNull Application application) {
        super(application);
        this.context = application.getApplicationContext();


    }

    public LiveData<List<Tasks>> getTasks() {
        tasksRepo = TasksRepo.getInstance(context);
        return tasksRepo.getTasks();
    }

    public void addnewt(final Tasks task) {
        taskUpdate.setValue(true);
        List<Tasks> current = tasks.getValue();
        current.add(task);
        tasks.postValue(current);
        taskUpdate.setValue(false);
    }


    public LiveData<Boolean> getUpdate() {
        return taskUpdate;
    }

}