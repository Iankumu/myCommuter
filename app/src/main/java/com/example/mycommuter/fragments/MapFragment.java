package com.example.mycommuter.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.mycommuter.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment {
Button btn;
    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view= inflater.inflate(R.layout.fragment_map, container, false);
//        btn=view.findViewById(R.id.datepick);
//        MaterialDatePicker.Builder builder=MaterialDatePicker.Builder.datePicker();
//        builder.setTitleText("add new");
//        MaterialDatePicker materialDatePicker=builder.build();
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                materialDatePicker.show(getParentFragmentManager(),"Date picker");
//            }
//        });


        return view;
    }
}
