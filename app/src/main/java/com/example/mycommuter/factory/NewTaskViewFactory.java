package com.example.mycommuter.factory;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.mycommuter.viewmodels.HomeActivityViewModel;
import com.example.mycommuter.viewmodels.NewTaskViewModel;

public class NewTaskViewFactory extends ViewModelProvider.AndroidViewModelFactory {
    /**
     * Creates a {@code AndroidViewModelFactory}
     *
     * @param application an application to pass in {@link AndroidViewModel}
     */
    Application application;
    FragmentManager fragmentManager;

    public NewTaskViewFactory(@NonNull Application application, FragmentManager fragmentManager) {
        super(application);
        this.application = application;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new NewTaskViewModel(application, fragmentManager);
    }
}
