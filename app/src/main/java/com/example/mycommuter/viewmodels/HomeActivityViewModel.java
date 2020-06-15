package com.example.mycommuter.viewmodels;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mycommuter.model.Tasks;
import com.example.mycommuter.repository.TasksRepo;
import com.google.gson.Gson;

import java.util.List;

public class HomeActivityViewModel extends AndroidViewModel {
    private MutableLiveData<List<Tasks>> tasks;
    private TasksRepo tasksRepo;
    private MutableLiveData<Boolean> taskUpdate = new MutableLiveData<>();
    private Context context;

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
