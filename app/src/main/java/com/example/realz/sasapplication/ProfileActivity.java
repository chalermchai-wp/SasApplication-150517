package com.example.realz.sasapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;


public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        TextView textview = (TextView) findViewById(R.id.usernamep);
        TextView textview2 = (TextView) findViewById(R.id.passwordp);
        TextView Alumni_code = (TextView) findViewById(R.id.alumni_code);
        TextView Alumni_name = (TextView) findViewById(R.id.alumni_name);
        TextView Pro_name = (TextView) findViewById(R.id.pro_name);
        TextView Edulv_name = (TextView) findViewById(R.id.edulv_name);
        TextView yAdmit = (TextView) findViewById(R.id.yadmit);
        TextView yGradute = (TextView) findViewById(R.id.ygraduate);
        TextView Email = (TextView) findViewById(R.id.email);
        TextView Grade = (TextView) findViewById(R.id.grade);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String u1 = bundle.getString("username");
            String p1 = bundle.getString("password");
            String alumni_code = bundle.getString("alumni_code");
            String alumni_name = bundle.getString("alumni_tname");
            String alumni_tsname = bundle.getString("alumni_tsurname");
            String alumni_tprefixdetail = bundle.getString("alumni_tprefixdetail");
            String alumni_typeprogram = bundle.getString("alumni_typeprogram");
            String program_tname = bundle.getString("program_tname");
            String ccName = bundle.getString("ccName");
            String edulv_name = bundle.getString("edulv_name");
            String alumni_yadmit = bundle.getString("alumni_yadmit");
            String alumni_ygraduate = bundle.getString("alumni_ygraduate");
            String alumni_phone = bundle.getString("alumni_phone");
            String alumni_email = bundle.getString("alumni_email");
            String alumni_birthdate = bundle.getString("alumni_birthdate");
            String alumni_gpa = bundle.getString("alumni_gpa");

            textview.setText(u1);
            textview2.setText(p1);
            Alumni_code.setText(alumni_code);
            Alumni_name.setText(alumni_tprefixdetail+" "+alumni_name+" "+alumni_tsname);
            if(alumni_typeprogram.equals("alu"))
            {
                Pro_name.setText(program_tname);
            }else if (alumni_typeprogram.equals("reg")){
                Pro_name.setText(ccName);
            }
            Edulv_name.setText(edulv_name);
            yAdmit.setText(alumni_yadmit);
            yGradute.setText(alumni_ygraduate);
            if(alumni_email.isEmpty()){
                Email.setText("ไม่ระบุ");
            }else {
                Email.setText(alumni_email);
            }
            Grade.setText(alumni_gpa.substring(0,4));


        }



    }
}
