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

/**
 * A simple {@link Fragment} subclass.
 */
public class TodoFragment extends Fragment {
    private static final String TAG = "TODOFRAGMENT";
    private ProgressBar progressBar;

    private TaskAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    Tasks tasks;
    private TextView description, due, title;
    private RecyclerView recyclerView;
    List<Tasks> listinit = new ArrayList<>();
    LinearLayout linearLayout;
    Toolbar toolbar;
    private HomeActivityViewModel homeActivityViewModel;
    private ShimmerFrameLayout shimmerFrameLayout;

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
        linearLayout = view.findViewById(R.id.linearcard);
        recyclerView = view.findViewById(R.id.myListView);
        toolbar = view.findViewById(R.id.tasktoolbar);

        setShimmer(true);
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

                Log.e("mainact", "" + tasks.toString());
                adapter.notifyDataSetChanged();
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
        initRecyclerView(listinit);
        toolbar.setOnMenuItemClickListener(this::onOptionsItemSelected);
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(),
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                                tasks = listinit.get(position);
                                Intent dintent = new Intent(getActivity(), TaskDetail.class);
                                dintent.putExtra("Dtitle", tasks.getTitle());
                                dintent.putExtra("Ddue", tasks.getDue());
                                dintent.putExtra("Ddescription", tasks.getDescritpion());


//                                Log.e(TAG, "onItemClick: "+ gitHubUser.getUrl());
                                startActivity(dintent);


//                                Intent intent = new Intent();
//                                String transitionName = getString(R.string.cardslidin);
//                                ActivityOptionsCompat options =
//                                        ActivityOptionsCompat.makeSceneTransitionAnimation(TaskDetail,
//                                                linearLayout,   // The view which starts the transition
//                                                transitionName    // The transitionName of the view we’re transitioning to
//                                        );
//                                ActivityCompat.startActivity(TaskDetail, intent, options.toBundle());
                            }
                        }));
    }


    public void initRecyclerView(List<Tasks> tasks) {
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
//        layoutManager = new LinearLayoutManager(getContext());
//        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);


        adapter = new TaskAdapter(getContext(), tasks);

        recyclerView.setAdapter(adapter);

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
    public void onResume() {
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
//        listinit.clear();

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
        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("add new");
        MaterialDatePicker materialDatePicker = builder.build();
        materialDatePicker.show(getParentFragmentManager(), "Date picker");
    }

}