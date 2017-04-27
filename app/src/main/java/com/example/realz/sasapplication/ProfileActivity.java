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
        TextView Alumni_code = (TextView) findViewById(R.id.alumni_code);
        TextView Alumni_name = (TextView) findViewById(R.id.alumni_name);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String u1 = bundle.getString("username");
            String p1 = bundle.getString("password");
            String a_code = bundle.getString("alumni_code");
            String a_name = bundle.getString("alumni_tname");
            String a_tsname = bundle.getString("alumni_tsurname");


            String result = String.format("username is %s, password : %s",
                    u1, p1);

            Toast.makeText(this, "Name : " + result, Toast.LENGTH_SHORT).show();

            textview.setText(u1);
            textview2.setText(p1);
            Alumni_code.setText(a_code);
            Alumni_name.setText(a_name+" "+a_tsname);

        }



    }
}
