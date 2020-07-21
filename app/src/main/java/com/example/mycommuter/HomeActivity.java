package com.example.mycommuter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import androidx.lifecycle.ViewModelProvider;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.mycommuter.adapter.TaskAdapter;


import com.example.mycommuter.model.Tasks;
import com.example.mycommuter.viewmodels.HomeActivityViewModel;
import com.google.gson.Gson;

import java.util.List;



public class HomeActivity extends AppCompatActivity {




  private ProgressBar progressBar;

  private TaskAdapter adapter;
  private RecyclerView.LayoutManager layoutManager;

  private TextView description, due, title;
  private RecyclerView recyclerView;

  private HomeActivityViewModel homeActivityViewModel;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);

    recyclerView = (RecyclerView) findViewById(R.id.myListView);
    progressBar = findViewById(R.id.progressBar);
    title = findViewById(R.id.title);
//        description = findViewById(R.id.description);
//        due = findViewById(R.id.due);


    homeActivityViewModel= new ViewModelProvider(this).get(HomeActivityViewModel.class);
//        homeActivityViewModel.init();
    homeActivityViewModel.getTasks().observe(this, new Observer<List<Tasks>>() {
      @Override
      public void onChanged(@Nullable List<Tasks> tasks) {
        initRecyclerView( tasks);

        Log.e("mainact",""+ tasks.toString());
        adapter.notifyDataSetChanged();
      }
    });
    homeActivityViewModel.getUpdate().observe(this, new Observer<Boolean>() {
      @Override
      public void onChanged(Boolean aBoolean) {
        if(aBoolean){
          startProgressIndicator();
        }else{
          stopProgressIndicator();
          recyclerView.smoothScrollToPosition(homeActivityViewModel.getTasks().getValue().size()-1);
        }
      }
    });
    //implement onscroll
//
//        initRecyclerView();
//        startProgressIndicator();

  }

  private void stopProgressIndicator() {
    progressBar.setVisibility(View.GONE);
  }

  private void startProgressIndicator() {
    progressBar.setVisibility(View.VISIBLE);
  }

  public void initRecyclerView(List<Tasks> tasks) {
//    layoutManager = new LinearLayoutManager(this);
//    recyclerView.setLayoutManager(layoutManager);
//    recyclerView.setHasFixedSize(true);
////        Log.e("mainactivity1",""+ homeActivityViewModel.getTasks().getValue().toString());
//
//    adapter = new TaskAdapter(getApplicationContext());
//    recyclerView.setAdapter(adapter);

  }


}