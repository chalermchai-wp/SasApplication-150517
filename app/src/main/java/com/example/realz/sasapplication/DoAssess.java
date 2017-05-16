package com.example.realz.sasapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
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
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

//import static com.example.realz.sasapplication.R.id.radio1;

public class DoAssess extends AppCompatActivity {

    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;
    private ListView ListViewJSon;
    private ProgressDialog progressDialog;
    private Button ButtonSub;
    private RadioGroup GroupRadio1;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private Button btnDisplay;
    private String alumni_id;
    final ArrayList<String> exData = new ArrayList<String>();
    private int[] score;
    private String assess_id;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_assess);
        ListViewJSon = (ListView)findViewById(R.id.listview_do);
        ButtonSub = (Button)findViewById(R.id.ButtonSubmit);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            assess_id = bundle.getString("assess_id");
        }

        exData.clear();
//        final ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(DoAssess.this,R.layout.list_question,R.id.listview_text,exData);
//        ListViewJSon.setAdapter(myAdapter);
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = new ProgressDialog(DoAssess.this);
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Downloding ..... ");
                progressDialog.show();
            }

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    disableSSLCertificateChecking();
                    URL url = new URL("https://10.51.4.17/TSP57/PCK/index.php/sas/Alumni/DoAssess/ListQuestion/?id="+assess_id);

                    URLConnection urlConnection = url.openConnection();

                    HttpURLConnection httpURLConnection = (HttpsURLConnection)urlConnection;
                    httpURLConnection.setAllowUserInteraction(false);
                    httpURLConnection.setInstanceFollowRedirects(true);
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.connect();

                    InputStream inputStream = null;

                    if(httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK)
                        inputStream = httpURLConnection.getInputStream();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"), 8);

                    StringBuilder jsonbuild = new StringBuilder();
                    StringBuilder stringBuilder = new StringBuilder();
                    String line = null;

                    while ((line=reader.readLine()) != null ){
                        stringBuilder.append(line + "\n");
                    }

                    inputStream.close();
                    Log.d("JSON Result", stringBuilder.toString());

                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(stringBuilder.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JSONArray exArray = null;
                    try {
                        exArray = jsonObject.getJSONArray("question");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    for(int i = 0; i < exArray.length(); i++){
                        JSONObject jsonObj = null;
                        try {
                            jsonObj = exArray.getJSONObject(i);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            exData.add(jsonObj.getString("quest_detail"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;

            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                progressDialog.dismiss();
                //final ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(DoAssess.this,R.layout.list_question,R.id.listview_text,exData);
                //ListViewJSon.setAdapter(myAdapter);
                RadioButtonAdapter rdio = new RadioButtonAdapter(getApplicationContext(),exData);

                ListViewJSon.setAdapter(rdio);
                Log.d("EXDATA", "onPostExecute: "+exData);
                score = new int[exData.size()];

            }
        }.execute();

        //Toast.makeText(DoAssess.this,  , Toast.LENGTH_SHORT).show();

        ButtonSub.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                StringBuilder builder = new StringBuilder();
                for(int i : score)
                {
                    builder.append("" + i + " ");
                }
                Log.d("SCOREEEE", "onClick: " + builder);
                String answer = builder.toString();

                //Toast.makeText(DoAssess.this, answer , Toast.LENGTH_SHORT).show();

                MainActivity main = new MainActivity();
                alumni_id = main.getAlumni_id();

                new AsyncSendAns().execute(alumni_id,assess_id,answer);
            }
        });

    }



    private class RadioButtonAdapter extends ArrayAdapter<String> {

        class ViewHolder {
            TextView t = null;
            RadioGroup group;

            ViewHolder(View v) {
                t = (TextView) v.findViewById(R.id.listview_text);
                group = (RadioGroup) v.findViewById(R.id.GroupRadio);
            }
        }

        private LayoutInflater mInflater;

        public RadioButtonAdapter(Context context, ArrayList<String> exData) {
            super(context, R.layout.list_question, exData);
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(final int position, final View convertView, ViewGroup parent) {
            View v = convertView;
            ViewHolder holder = null;

            if (v == null) {
                v = mInflater.inflate(R.layout.list_question, parent, false);
                holder = new ViewHolder(v);
                holder.t = (TextView)v.findViewById(R.id.listview_text);
                v.setTag(holder);

                holder.group
                        .setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                            public void onCheckedChanged(RadioGroup group, int checkedId) {
                                Integer pos = (Integer) group.getTag(); // To identify the Model object i get from the RadioGroup with getTag()
                                //  an integer representing the actual position
                                //Model element = exData.get(pos);
                                switch (checkedId) { //set the Model to hold the answer the user picked
                                    case R.id.radio1:
                                        addscore(exData.indexOf(exData.get(position)),1);
//                                        Log.d("CheckID", "onCheckedChanged: "+checkedId+"  POST" + exData.get(pos)+ " Group "+ exData.indexOf(exData.get(position)));
//                                        Toast.makeText(DoAssess.this,  "onCheckedChanged: "+checkedId+"  POST" + exData.get(pos)+ " Group "+ exData.indexOf(exData.get(position)), Toast.LENGTH_SHORT).show();
//                                        //element.current = Model.ANSWER_ONE_SELECTED;
                                        break;
                                    case R.id.radio2:
                                        addscore(exData.indexOf(exData.get(position)),2);
//                                        Log.d("CheckID", "onCheckedChanged: "+checkedId+"  POST" + exData.get(pos));
//                                        Toast.makeText(DoAssess.this,  "onCheckedChanged: "+checkedId+"  POST" + exData.get(pos)+ " Group "+ exData.indexOf(exData.get(position)), Toast.LENGTH_SHORT).show();
//                                        //element.current = Model.ANSWER_TWO_SELECTED;
                                        break;
                                    case R.id.radio3:
                                        addscore(exData.indexOf(exData.get(position)),3);
//                                        Log.d("CheckID", "onCheckedChanged: "+checkedId+"  POST" + exData.get(pos));
//                                        Toast.makeText(DoAssess.this,  "onCheckedChanged: "+checkedId+"  POST" + exData.get(pos)+ " Group "+ exData.indexOf(exData.get(position)), Toast.LENGTH_SHORT).show();
//                                        //element.current = Model.ANSWER_THREE_SELECTED;
                                        break;
                                    case R.id.radio4:
                                        addscore(exData.indexOf(exData.get(position)),4);
//                                        Log.d("CheckID", "onCheckedChanged: "+checkedId+"  POST" + exData.get(pos));
//                                        Toast.makeText(DoAssess.this,  "onCheckedChanged: "+checkedId+"  POST" + exData.get(pos)+ " Group "+ exData.indexOf(exData.get(position)), Toast.LENGTH_SHORT).show();

                                        //element.current = Model.ANSWER_FOUR_SELECTED;
                                        break;
                                    default:
                                        //element.current = Model.NONE; // Something was wrong set to the default
                                }

                            }
                        });
            } else {
                holder = (ViewHolder) v.getTag();
            }
            holder.group.setTag(new Integer(position)); // I passed the current position as a tag

            holder.t.setText(exData.get(position));
            //holder.t.setText(list.get(position).question); // Set the question body

//            if (list.get(position).current != Model.NONE) {
//                RadioButton r = (RadioButton) holder.group.getChildAt(list
//                        .get(position).current);
//                r.setChecked(true);
//            } else {
//                holder.group.clearCheck(); // This is required because although the Model could have the current
//                // position to NONE you could be dealing with a previous row where
//                // the user already picked an answer.
//
//            }
            return v;
        }

    }

    public void addscore(int position,int point){

        score[position] = point;
    }

    private class AsyncSendAns extends AsyncTask<String, String, String>
    {
        ProgressDialog pdLoading = new ProgressDialog(DoAssess.this);
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
                url = new URL("https://10.51.4.17/TSP57/PCK/index.php/sas/Alumni/DoAssess/SubmitAnswer/");

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
                        .appendQueryParameter("alumni_id", params[0])
                        .appendQueryParameter("assess_id", params[1])
                        .appendQueryParameter("answer", params[2]);
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

            if (result.equalsIgnoreCase("Success")){

                // If username and password does not match display a error message
                Toast.makeText(DoAssess.this, "บันทึกคำตอบเรียบร้อยแล้ว", Toast.LENGTH_LONG).show();

            } else if (result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {

                Toast.makeText(DoAssess.this, "OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();

            } else {

//              JSONObject jsonObject=new JSONObject(result);
//              String strLoginID=jsonObject.optString("LoginID");
//              int status=jsonObject.optInt("status");
                /* Here launching another activity when login successful. If you persist login state
                use sharedPreferences of Android. and logout button to clear sharedPreferences.
                 */



//                JSONObject re_json;
//
//                try {
//                    re_json = new JSONObject(result);
//                    alumni_id = re_json.getString("alumni_id");
//                    alumni_tname = re_json.getString("alumni_tname");
//                    alumni_code = re_json.getString("alumni_code");
//                    alummi_dpid = re_json.getString("alumni_dpid");
//                    alumni_tsurname = re_json.getString("alumni_tsurname");
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//
//                Intent intent = new Intent(MainActivity.this,UserActivity.class);
//                intent.putExtra("username", etEmail.getText().toString());
//                intent.putExtra("password", etPassword.getText().toString());
//                intent.putExtra("alumni_id", alumni_id);
//                intent.putExtra("alumni_dpid", alummi_dpid);
////                    intent.putExtra("alumni_code", alumni_code);
////                    intent.putExtra("alumni_tname", alumni_tname);
////                    intent.putExtra("alumni_tsurname", alumni_tsurname);
//
//                startActivity(intent);

                //Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
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
