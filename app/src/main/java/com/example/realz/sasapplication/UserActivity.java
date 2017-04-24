package com.example.realz.sasapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class UserActivity extends AppCompatActivity {

    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        //getActionBar().setTitle("เมนู");
        //getSupportActionBar().setTitle("เมนู");

        //Intent intent = getIntent();

       // String username = intent.getStringExtra(MainActivity.USER_NAME);

        //textView.setText("Welcome User "+username);
    }

}
