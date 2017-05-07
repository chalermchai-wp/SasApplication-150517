package com.example.realz.sasapplication;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;

import static com.example.realz.sasapplication.R.id.alumni_code;
import static com.example.realz.sasapplication.R.id.edulv_name;


public class ProfileActivity extends AppCompatActivity {

    private String imageProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

//        TextView textview = (TextView) findViewById(R.id.usernamep);
//        TextView textview2 = (TextView) findViewById(R.id.passwordp);
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        TextView Alumni_code = (TextView) findViewById(alumni_code);
        TextView Alumni_name = (TextView) findViewById(R.id.alumni_name);
        TextView Pro_name = (TextView) findViewById(R.id.pro_name);
        TextView Edulv_name = (TextView) findViewById(edulv_name);
        TextView yAdmit = (TextView) findViewById(R.id.yadmit);
        TextView yGradute = (TextView) findViewById(R.id.ygraduate);
        TextView Email = (TextView) findViewById(R.id.email);
        TextView Grade = (TextView) findViewById(R.id.grade);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String u1 = bundle.getString("username");
            String p1 = bundle.getString("password");
            String alumni_id = bundle.getString("alumni_id");
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
            String ImgProfile = bundle.getString("imgProfile");

//            textview.setText(u1);
//            textview2.setText(p1);

            Alumni_code.setText(alumni_code);
            Alumni_name.setText(alumni_tprefixdetail + " " + alumni_name + " " + alumni_tsname);
            if (alumni_typeprogram.equals("alu")) {
                Pro_name.setText(program_tname);
            } else if (alumni_typeprogram.equals("reg")) {
                Pro_name.setText(ccName);
            }
            Edulv_name.setText(edulv_name);
            yAdmit.setText(alumni_yadmit);
            yGradute.setText(alumni_ygraduate);
            if (alumni_email.isEmpty()) {
                Email.setText("ไม่ระบุ");
            } else {
                Email.setText(alumni_email);
            }
            Grade.setText(alumni_gpa.substring(0, 4));

            if(ImgProfile.equals("null")){
                ImgProfile = "data:jpg;base64,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";
            }

//                 // String encodedDataString = img;
            ImgProfile = ImgProfile.replace("data:jpg;base64,","");

            byte[] imageAsBytes = Base64.decode(ImgProfile.getBytes(), 0);
            imageView.setImageBitmap(BitmapFactory.decodeByteArray(
                    imageAsBytes, 0, imageAsBytes.length));


        }






    }

}
//        String encodedDataString = img;
//        encodedDataString = encodedDataString.replace("data:jpg;base64,","");
//
//        byte[] imageAsBytes = Base64.decode(encodedDataString.getBytes(), 0);
//        imageView.setImageBitmap(BitmapFactory.decodeByteArray(
//                imageAsBytes, 0, imageAsBytes.length));
