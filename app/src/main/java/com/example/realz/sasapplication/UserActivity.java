package com.example.realz.sasapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import static com.example.realz.sasapplication.MainActivity.CONNECTION_TIMEOUT;
import static com.example.realz.sasapplication.MainActivity.READ_TIMEOUT;

public class UserActivity extends AppCompatActivity {

    private TextView textView;
    private LinearLayout DoAssess;
    private LinearLayout ViewAssess;
    private LinearLayout ViewProfile;
    private String username;
    private String password;
    private String imageProfile;
    private String alumni_id;
    private String alumni_dpid;
    private String alumni_tname;
    private String alumni_tsurname;
    private String alumni_code;
    private String alumni_tprefixdetail;
    private String alumni_typeprogram;
    private String program_tname;
    private String ccName;
    private String edulv_name;
    private String alumni_yadmit;
    private String alumni_ygraduate;
    private String alumni_phone;
    private String alumni_email;
    private String alumni_birthdate;
    private String alumni_gpa;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        MainActivity main = new MainActivity();
        alumni_id = main.getAlumni_id();
        alumni_dpid = main.getAlummi_dpid();
        //alumni_id = main.alumni_id;

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            username = bundle.getString("username");
            password = bundle.getString("password");

//            alumni_id = bundle.getString("alumni_id");
//            alumni_dpid = bundle.getString("alumni_dpid");
        }

        Bundle recdData = getIntent().getExtras();
        String myVal = recdData.getString("editTodolist");
        String myVal2 = recdData.getString("editTodolist2");

        //Toast.makeText(UserActivity.this,myVal+"  "+myVal2+" "+main.getAlumni_id(), Toast.LENGTH_LONG).show();


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

                //Toast.makeText(UserActivity.this,alumni_id+"----"+alumni_dpid, Toast.LENGTH_LONG).show();

                new AsyncProfile().execute(alumni_id,alumni_dpid,imageProfile);

            }
        });
    }




    private class AsyncProfile extends AsyncTask<String, String, String>
    {
        ProgressDialog pdLoading = new ProgressDialog(UserActivity.this);
        HttpsURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }
        @Override
        protected String doInBackground(String... params) {
            disableSSLCertificateChecking();
            try {
                // Enter URL address where your php file resides
                //https://team3.ml/Login/check_loginapp
                //https://10.51.4.17/TSP57/PCK/index.php/sas/Alumni/LoginApp/check_login
                url = new URL("https://10.51.4.17/TSP57/PCK/index.php/sas/Alumni/LoginApp/info_alumni/");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "exception";
            }
            try {

                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpsURLConnection)url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");

                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);

                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("id", params[0])
                        .appendQueryParameter("dpid", params[1]);
                String query = builder.build().getEncodedQuery();

                // Open connection for sending data
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();
                Log.d("catch","11");

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                Log.d("catch","catch");
                return "exception";
            }

            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    return(result.toString());

                }else{

                    return("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return "exception";
            } finally {
                conn.disconnect();
            }


        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //this method will be running on UI thread

            pdLoading.dismiss();

            if (result.equalsIgnoreCase("false")){

                // If username and password does not match display a error message
                Toast.makeText(UserActivity.this, "Invalid email or password", Toast.LENGTH_LONG).show();

            } else if (result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {

                Toast.makeText(UserActivity.this, "OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();

            } else {

                JSONObject re_json;

                try {
                    re_json = new JSONObject(result);
                    alumni_id = re_json.getString("alumni_id");
                    alumni_tname = re_json.getString("alumni_tname");
                    alumni_code = re_json.getString("alumni_code");
                    alumni_tsurname = re_json.getString("alumni_tsurname");
                    alumni_tprefixdetail = re_json.getString("alumni_tprefixdetail");
                    alumni_typeprogram = re_json.getString("alumni_typeprogram");
                    program_tname = re_json.getString("program_tname");
                    ccName = re_json.getString("ccName");
                    edulv_name = re_json.getString("edulv_name");
                    alumni_yadmit = re_json.getString("alumni_yadmit");
                    alumni_ygraduate = re_json.getString("alumni_ygraduate");
                    alumni_phone = re_json.getString("alumni_phone");
                    alumni_email = re_json.getString("alumni_email");
                    alumni_birthdate = re_json.getString("alumni_birthdate");
                    alumni_gpa = re_json.getString("alumni_gpa");
                    imageProfile = re_json.getString("0");

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                Intent intent = new Intent(UserActivity.this,ProfileActivity.class);
//                intent.putExtra("username", etEmail.getText().toString());
//                intent.putExtra("password", etPassword.getText().toString());
                    intent.putExtra("alumni_id", alumni_id);
                    intent.putExtra("alumni_code", alumni_code);
                    intent.putExtra("alumni_tname", alumni_tname);
                    intent.putExtra("alumni_tsurname", alumni_tsurname);
                    intent.putExtra("alumni_tprefixdetail", alumni_tprefixdetail);
                    intent.putExtra("alumni_typeprogram", alumni_typeprogram);
                    intent.putExtra("program_tname", program_tname);
                    intent.putExtra("ccName", ccName);
                    intent.putExtra("edulv_name", edulv_name);
                    intent.putExtra("alumni_yadmit", alumni_yadmit);
                    intent.putExtra("alumni_ygraduate", alumni_ygraduate);
                    intent.putExtra("alumni_phone", alumni_phone);
                    intent.putExtra("alumni_email", alumni_email);
                    intent.putExtra("alumni_birthdate", alumni_birthdate);
                    intent.putExtra("alumni_gpa", alumni_gpa);
                    intent.putExtra("imgProfile", imageProfile);
                    //intent.putExtra("alumni_img",re_json.getString("alumni_img"));

                startActivity(intent);

                //Toast.makeText(UserActivity.this, imageProfile, Toast.LENGTH_LONG).show();
            }
        }

    }

    private static void disableSSLCertificateChecking() {
        TrustManager[] trustAllCerts = new TrustManager[] {
                new X509TrustManager() {

                    @Override
                    public void checkClientTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) throws java.security.cert.CertificateException {
                        // not implemented
                    }

                    @Override
                    public void checkServerTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) throws java.security.cert.CertificateException {
                        // not implemented
                    }

                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                }
        };

        try {

            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {

                @Override
                public boolean verify(String s, SSLSession sslSession) {
                    return true;
                }

            });
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
