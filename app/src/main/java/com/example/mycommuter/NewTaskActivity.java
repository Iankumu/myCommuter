package com.example.mycommuter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.example.mycommuter.databinding.ActivityNewTaskBinding;
import com.example.mycommuter.factory.NewTaskViewFactory;
import com.example.mycommuter.viewmodels.HomeActivityViewModel;
import com.example.mycommuter.viewmodels.NewTaskViewModel;

public class NewTaskActivity extends AppCompatActivity {
    NewTaskViewModel newTaskViewModel;
    private ActivityNewTaskBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        newTaskViewModel = new ViewModelProvider(this, new NewTaskViewFactory(getApplication(), getSupportFragmentManager()))
                .get(NewTaskViewModel.class);
//        loginActivityModelView=new ViewModelProvider(this).get(LoginActivityModelView.class);

//        loginActivityModelView = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(LoginActivityModelView.class);

        binding = DataBindingUtil.setContentView(NewTaskActivity.this, R.layout.activity_new_task);
        binding.setLifecycleOwner(this);

        binding.setNewTaskViewModel(newTaskViewModel);

    }
}