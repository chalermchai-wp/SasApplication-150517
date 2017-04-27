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
    private LinearLayout ViewProfile;
    private String username;
    private String password;
    private String alumni_id;
    private String alumni_tname;
    private String alumni_tsurname;
    private String alumni_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            username = bundle.getString("username");
            password = bundle.getString("password");
            alumni_id = bundle.getString("alumni_id");
            alumni_code = bundle.getString("alumni_code");
            alumni_tname = bundle.getString("alumni_tname");
            alumni_tsurname = bundle.getString("alumni_tsurname");

            //String result = String.format("username is %s, password : %s",username, password);
            //Toast.makeText(this, "Name : " + result, Toast.LENGTH_SHORT).show();
        }

        DoAssess = (LinearLayout)findViewById(R.id.do_assess);
        ViewAssess = (LinearLayout)findViewById(R.id.view_assess);
        ViewProfile = (LinearLayout)findViewById(R.id.view_profile);

        DoAssess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ListAssess.class);
                intent.putExtra("alumni_id",alumni_id);
                startActivity(intent);
            }
        });

        ViewAssess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),DoneAssess.class);
                intent.putExtra("alumni_id",alumni_id);
                startActivity(intent);
            }
        });

        ViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ProfileActivity.class);
                intent.putExtra("username",username);
                intent.putExtra("password",password);
                intent.putExtra("alumni_id",alumni_id);
                intent.putExtra("alumni_code",alumni_code);
                intent.putExtra("alumni_tname", alumni_tname);
                intent.putExtra("alumni_tsurname", alumni_tsurname);
                startActivity(intent);
            }
        });
    }
}
