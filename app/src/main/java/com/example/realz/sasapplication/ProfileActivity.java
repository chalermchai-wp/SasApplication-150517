package com.example.realz.sasapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        TextView textview = (TextView) findViewById(R.id.usernamep);
        TextView textview2 = (TextView) findViewById(R.id.passwordp);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String u1 = bundle.getString("username");
            String p1 = bundle.getString("password");
            String id1 = bundle.getString("u_id");

            String result = String.format("id is %s username is %s, password : %s",
                    id1, u1, p1);

            Toast.makeText(this, "Name : " + result, Toast.LENGTH_SHORT).show();

            textview.setText(u1);
            textview2.setText(p1);

        }



    }
}
