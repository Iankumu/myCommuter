package com.example.mycommuter.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.mycommuter.sharedPrefs.saveSharedPref;

import com.example.mycommuter.R;
import com.example.mycommuter.model.LocationModel;
import com.example.mycommuter.model.LoginUser;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment {
    LoginUser loginUser;
    LocationModel locationModel;
    TextView textView;
    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view= inflater.inflate(R.layout.fragment_map, container, false);
        String email = saveSharedPref.getEmail(getContext());
        textView = view.findViewById(R.id.email);
        textView.setText(email);
        Log.d("mapemail",email);


        return view;
    }




}
