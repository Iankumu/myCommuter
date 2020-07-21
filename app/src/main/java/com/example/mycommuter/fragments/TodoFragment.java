package com.example.mycommuter.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ActionMenuView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.mycommuter.BottomNavigationActivity;
import com.example.mycommuter.NewTaskActivity;
import com.example.mycommuter.R;
import com.example.mycommuter.TaskDetail;
import com.example.mycommuter.adapter.RecyclerItemClickListener;
import com.example.mycommuter.adapter.TaskAdapter;
import com.example.mycommuter.model.Tasks;
import com.example.mycommuter.viewmodels.HomeActivityViewModel;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

/**
 * A simple {@link Fragment} subclass.
 */
public class TodoFragment extends Fragment {
    private static final String TAG = "TODOFRAGMENT";
    private ProgressBar progressBar;


    private RecyclerView.LayoutManager layoutManager;
    private Tasks tasks;
    private TextView description, due, title;
    private RecyclerView recyclerView;
    List<Tasks> listinit = new ArrayList<>();
    private TaskAdapter adapter;
    Toolbar toolbar, contextualtoolbar;
    private HomeActivityViewModel homeActivityViewModel;
    private ShimmerFrameLayout shimmerFrameLayout;
    public boolean isContextModeEnabled = false;
    StaggeredGridLayoutManager staggeredGridLayoutManager;

    public TodoFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todo, container, false);
        setRetainInstance(true);
        shimmerFrameLayout = view.findViewById(R.id.shimmerframelay);
        progressBar = view.findViewById(R.id.progressBar);
        contextualtoolbar = view.findViewById(R.id.contextualToolbar);
        recyclerView = view.findViewById(R.id.myListView);
        toolbar = view.findViewById(R.id.tasktoolbar);

        setShimmer(true);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initRecyclerView();

        homeActivityViewModel = new ViewModelProvider(this).get(HomeActivityViewModel.class);
        homeActivityViewModel.init();
        homeActivityViewModel.getTasks().observe(getViewLifecycleOwner(), new Observer<List<Tasks>>() {
            @Override
            public void onChanged(List<Tasks> tasks) {


                listinit = tasks;
                adapter = new TaskAdapter(getContext(), listinit);
                adapter.setTasks(tasks);
                Log.e("mainact", "" + tasks.toString());
                recyclerView.setAdapter(adapter);
                setShimmer(false);

            }
        });

        homeActivityViewModel.getUpdate().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {

                if (aBoolean) {

                } else {

                    recyclerView.smoothScrollToPosition(homeActivityViewModel.getTasks().getValue().size() - 1);
                }
            }
        });
        // TODO: Use the ViewModel

        toolbar.setOnMenuItemClickListener(this::onOptionsItemSelected);
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(),
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                                tasks = listinit.get(position);
                                Intent dintent = new Intent(getActivity(), TaskDetail.class);

                                dintent.putExtra("task", tasks);

                                startActivity(dintent);


                            }
                        }));
    }


    public void initRecyclerView() {
        Log.e(TAG, "initRecyclerView: " + listinit);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
//        layoutManager = new LinearLayoutManager(getContext());
//        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
//        adapter = new TaskAdapter(getContext(), listinit);

        recyclerView.setAdapter(new TaskAdapter(getContext(), listinit));

    }

    public void setShimmer(boolean shimmer) {
        if (shimmer) {
            shimmerFrameLayout.startShimmer();
        } else {

            shimmerFrameLayout.stopShimmer();
            shimmerFrameLayout.setVisibility(View.GONE);
        }

    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case 120:
                displaymessage("Task deleted");
                break;
            case 121:
                displaymessage("Task opened");
                break;
        }
        return super.onContextItemSelected(item);
    }

    public void displaymessage(String message) {
        Toasty.info(getContext(), message).show();
    }

    @Override
    public void onResume() {
        listinit.clear();
        super.onResume();

    }

    @Override
    public void onStart() {
        super.onStart();
        shimmerFrameLayout.startShimmer();
        setShimmer(true);

    }

    @Override
    public void onStop() {
        setShimmer(false);
        listinit.clear();
        super.onStop();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.search:
                break;
            case R.id.add:
                additem();

        }
        return super.onOptionsItemSelected(item);
    }

    private void additem() {

        Intent dintent = new Intent(getActivity(), NewTaskActivity.class);
        startActivity(dintent);

    }


}
