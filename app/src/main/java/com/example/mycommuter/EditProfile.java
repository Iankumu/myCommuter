package com.example.mycommuter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class EditProfile extends AppCompatActivity {
    Toolbar toolbar;
    FloatingActionButton uploadphoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        toolbar = findViewById(R.id.editproftoolbar);
        setSupportActionBar(toolbar);
        uploadphoto=findViewById(R.id.uploadpic);
        uploadphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadpic();
            }
        });

    }

    private void uploadpic() {
    }
}
