package com.example.mycommuter.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.mycommuter.BottomNavigationActivity;
import com.example.mycommuter.EditProfile;
import com.example.mycommuter.LoginActivity;
import com.example.mycommuter.R;
import com.example.mycommuter.model.LoginUser;
import com.example.mycommuter.sharedPrefs.saveSharedPref;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
Button button;
FloatingActionButton floatingActionButton;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_profile, container, false);
        ((BottomNavigationActivity)requireActivity()).getSupportActionBar().hide();
        button=view.findViewById(R.id.logoutbtn);
        floatingActionButton=view.findViewById(R.id.editprof);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EditProfile.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSharedPref.setLoggedIn(getContext(), new Pair<>(false,""),"");
                Intent intent = new Intent(getContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        return view;
    }
   void logout(View view){


    }
    @Override
    public void onStart() {
        super.onStart();
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }
    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }

}
