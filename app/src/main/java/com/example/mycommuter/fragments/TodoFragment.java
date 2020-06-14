package com.example.mycommuter.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.mycommuter.R;
import com.example.mycommuter.adapter.TaskAdapter;
import com.example.mycommuter.model.Tasks;
import com.example.mycommuter.viewmodels.HomeActivityViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TodoFragment extends Fragment {
    private ProgressBar progressBar;

    private TaskAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private TextView description, due, title;
    private RecyclerView recyclerView;
    List<Tasks> listinit = new ArrayList<>();
    private HomeActivityViewModel homeActivityViewModel;

    public TodoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todo, container, false);
        progressBar = view.findViewById(R.id.progressBar);
        title = view.findViewById(R.id.title);
        description = view.findViewById(R.id.description);
        due = view.findViewById(R.id.due);
        recyclerView = (RecyclerView) view.findViewById(R.id.myListView);


        //implement onscroll
//

//        startProgressIndicator();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        startProgressIndicator();
        homeActivityViewModel = new ViewModelProvider(this).get(HomeActivityViewModel.class);
//        homeActivityViewModel.init();
        homeActivityViewModel.getTasks().observe(getViewLifecycleOwner(), new Observer<List<Tasks>>() {

            @Override
            public void onChanged(@Nullable List<Tasks> tasks) {

                initRecyclerView(tasks);
                stopProgressIndicator();
                Log.e("mainact", "" + tasks.toString());
                adapter.notifyDataSetChanged();
            }
        });
        homeActivityViewModel.getUpdate().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                stopProgressIndicator();
                if (aBoolean) {
                    startProgressIndicator();
                } else {
                    stopProgressIndicator();
                    recyclerView.smoothScrollToPosition(homeActivityViewModel.getTasks().getValue().size() - 1);
                }
            }
        });
        // TODO: Use the ViewModel
        initRecyclerView(listinit);
    }

    private void stopProgressIndicator() {
        progressBar.setVisibility(View.GONE);
    }

    private void startProgressIndicator() {
        progressBar.setVisibility(View.VISIBLE);
    }

    public void initRecyclerView(List<Tasks> tasks) {
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
//        Log.e("mainactivity1",""+ homeActivityViewModel.getTasks().getValue().toString());

        adapter = new TaskAdapter(getContext(), tasks);
        recyclerView.setAdapter(adapter);

    }


}