package com.example.mycommuter.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ActionMenuView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.mycommuter.BottomNavigationActivity;
import com.example.mycommuter.R;
import com.example.mycommuter.adapter.TaskAdapter;
import com.example.mycommuter.model.Tasks;
import com.example.mycommuter.viewmodels.HomeActivityViewModel;
import com.google.android.material.datepicker.MaterialDatePicker;

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
    ActionMenuView actionMenuView;
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
        actionMenuView = view.findViewById(R.id.actionmenu);
        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        homeActivityViewModel = new ViewModelProvider(this).get(HomeActivityViewModel.class);

        homeActivityViewModel.getTasks().observe(getViewLifecycleOwner(), new Observer<List<Tasks>>() {

            @Override
            public void onChanged(@Nullable List<Tasks> tasks) {

                listinit = tasks;
                initRecyclerView(listinit);
                adapter.setShimmer(false);
                Log.e("mainact", "" + tasks.toString());
                adapter.notifyDataSetChanged();
            }
        });
        homeActivityViewModel.getUpdate().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {

                if (aBoolean) {
                 adapter.setShimmer(true);
                } else {
                  adapter.setShimmer(false);
                    recyclerView.smoothScrollToPosition(homeActivityViewModel.getTasks().getValue().size() - 1);
                }
            }
        });
        // TODO: Use the ViewModel
        initRecyclerView(listinit);
    }


    public void initRecyclerView(List<Tasks> tasks) {
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

//        Log.e("mainactivity1",""+ homeActivityViewModel.getTasks().getValue().toString());

        adapter = new TaskAdapter(getContext(), tasks);

        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onStart() {
        super.onStart();
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
    }

    @Override
    public void onStop() {
        listinit.clear();
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        super.onStop();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

        super.onCreateOptionsMenu(menu, inflater);

        Menu taskmenu=actionMenuView.getMenu();
        inflater.inflate(R.menu.taskmenu, taskmenu);
        for (int i = 0; i < taskmenu.size(); i++) {
            taskmenu.getItem(i).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    return onOptionsItemSelected(item);
                }
            });
        }

    }

    @Override
        public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        switch (id){
            case R.id.search:
                break;
            case R.id.add:
                additem();

        }
        return super.onOptionsItemSelected(item);
    }

    private void additem() {
        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("add new");
        MaterialDatePicker materialDatePicker = builder.build();
        materialDatePicker.show(getParentFragmentManager(),"Date picker");
    }
}