package com.example.realz.sasapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class UserActivity extends AppCompatActivity {

    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        textView = (TextView) findViewById(R.id.textlogin);

        Intent intent = getIntent();

       // String username = intent.getStringExtra(MainActivity.USER_NAME);

        //textView.setText("Welcome User "+username);
    }

}
