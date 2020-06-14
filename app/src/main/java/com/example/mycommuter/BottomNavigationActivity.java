package com.example.mycommuter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.mycommuter.fragments.MapFragment;
import com.example.mycommuter.fragments.ProfileFragment;
import com.example.mycommuter.fragments.TodoFragment;
import com.example.mycommuter.fragments.WeatherFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomNavigationActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation);
        bottomNavigationView=findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,new MapFragment()).commit();
            bottomNavigationView.setSelectedItemId(R.id.maps_id);

        }
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){

            case R.id.maps_id:
                getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,new MapFragment()).commit();
                break;
            case R.id.todo_id:
                getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,new TodoFragment( )).commit();
                break;
            case R.id.weather_id:
                getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,new WeatherFragment()).commit();
                break;

            case R.id.account_id:
                getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,new ProfileFragment()).commit();
                break;

        }
        return true;
    }
}
