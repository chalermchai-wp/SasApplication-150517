package com.example.realz.sasapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class UserActivity extends AppCompatActivity {

    private TextView textView;
    private LinearLayout DoAssess;
    private LinearLayout ViewAssess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        DoAssess = (LinearLayout)findViewById(R.id.do_assess);

        ViewAssess = (LinearLayout)findViewById(R.id.view_assess);

        DoAssess.setOnClickListener(Do_assess);

        ViewAssess.setOnClickListener(View_assess);

        //Intent intent = getIntent();

       // String username = intent.getStringExtra(MainActivity.USER_NAME);

        //textView.setText("Welcome User "+username);
    }

    private View.OnClickListener Do_assess = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(),ListAssess.class);
            startActivity(intent);
        }
    };

    private View.OnClickListener View_assess = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(),DoneAssess.class);
            startActivity(intent);
        }
    };



}
