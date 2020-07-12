package com.example.mycommuter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.TextViewCompat;

import android.os.Bundle;
import android.widget.TextView;

public class TaskDetail extends AppCompatActivity {
  TextView title,due,description;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_task_detail);


    title=findViewById(R.id.detailtitle);
    due=findViewById(R.id.detaildue);
    description=findViewById(R.id.detaildescription);
    title.setText(getIntent().getStringExtra("Dtitle"));
    description.setText(getIntent().getStringExtra("Ddescription"));
    due.setText(getIntent().getStringExtra("Ddue"));

  }
}