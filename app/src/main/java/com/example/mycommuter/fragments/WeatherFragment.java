package com.example.mycommuter.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mycommuter.R;
import com.example.mycommuter.sharedPrefs.saveSharedPref;

/**
 * A simple {@link Fragment} subclass.
 */
public class WeatherFragment extends Fragment {

    public WeatherFragment() {
        // Required empty public constructor
    }
TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_weather, container, false);
//        String email = saveSharedPref.getEmail(getContext());
//        textView = view.findViewById(R.id.weatheremail);
//        textView.setText(email);
        return view;
    }
}
