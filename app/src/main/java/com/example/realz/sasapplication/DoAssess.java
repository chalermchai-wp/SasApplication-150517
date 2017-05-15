package com.example.realz.sasapplication;

import android.app.ProgressDialog;
import android.content.Context;
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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
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

    private ListView ListViewJSon;
    private ProgressDialog progressDialog;
    private Button ButtonSub;
    private RadioGroup GroupRadio1;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private Button btnDisplay;
    final ArrayList<String> exData = new ArrayList<String>();


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_assess);
        ListViewJSon = (ListView)findViewById(R.id.listview_do);
        ButtonSub = (Button)findViewById(R.id.ButtonSubmit);



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
                    URL url = new URL("https://10.51.4.17/TSP57/PCK/index.php/sas/Alumni/DoAssess/ListQuestion");

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
            }
        }.execute();

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
        public View getView(final int position, View convertView, ViewGroup parent) {
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
                                        Log.d("CheckID", "onCheckedChanged: "+checkedId+"  POST" + exData.get(pos)+ " Group "+ exData.indexOf(exData.get(position)));
                                        Toast.makeText(DoAssess.this,  "onCheckedChanged: "+checkedId+"  POST" + exData.get(pos)+ " Group "+ exData.indexOf(exData.get(position)), Toast.LENGTH_SHORT).show();
                                        //element.current = Model.ANSWER_ONE_SELECTED;
                                        break;
                                    case R.id.radio2:
                                        Log.d("CheckID", "onCheckedChanged: "+checkedId+"  POST" + exData.get(pos));
                                        Toast.makeText(DoAssess.this,  "onCheckedChanged: "+checkedId+"  POST" + exData.get(pos)+ " Group "+ exData.indexOf(exData.get(position)), Toast.LENGTH_SHORT).show();
                                        //element.current = Model.ANSWER_TWO_SELECTED;
                                        break;
                                    case R.id.radio3:
                                        Log.d("CheckID", "onCheckedChanged: "+checkedId+"  POST" + exData.get(pos));
                                        Toast.makeText(DoAssess.this,  "onCheckedChanged: "+checkedId+"  POST" + exData.get(pos)+ " Group "+ exData.indexOf(exData.get(position)), Toast.LENGTH_SHORT).show();
                                        //element.current = Model.ANSWER_THREE_SELECTED;
                                        break;
                                    case R.id.radio4:
                                        Log.d("CheckID", "onCheckedChanged: "+checkedId+"  POST" + exData.get(pos));
                                        Toast.makeText(DoAssess.this,  "onCheckedChanged: "+checkedId+"  POST" + exData.get(pos)+ " Group "+ exData.indexOf(exData.get(position)), Toast.LENGTH_SHORT).show();

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



//    public void onRadioButtonClicked(View view) {
//        // Is the button now checked?
//        boolean checked = ((RadioButton) view).isChecked();
//
//        // Check which radio button was clicked
//        switch(view.getId()) {
//            case radio1:
//                if (checked)
//                    Toast.makeText(DoAssess.this, ((RadioButton) view).getText(), Toast.LENGTH_LONG).show();
//                break;
//            case R.id.radio2:
//                if (checked)
//                    Toast.makeText(DoAssess.this, ((RadioButton) view).getText(), Toast.LENGTH_LONG).show();
//                break;
//            case R.id.radio3:
//                if (checked)
//                    Toast.makeText(DoAssess.this, ((RadioButton) view).getText(), Toast.LENGTH_LONG).show();
//                break;
//            case R.id.radio4:
//                if (checked)
//                    Toast.makeText(DoAssess.this, ((RadioButton) view).getText(), Toast.LENGTH_LONG).show();
//                break;
//        }
//    }

//    public void addListenerOnButton() {
//
//        radioGroup = (RadioGroup) findViewById(R.id.GroupRadio);
//        btnDisplay = (Button) findViewById(R.id.ButtonSubmit);
//
//        btnDisplay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // get selected radio button from radioGroup
//                int selectedId = radioGroup.getCheckedRadioButtonId();
//
//                Log.d("SelectID", "onClick: "+ selectedId);
//
//                // find the radiobutton by returned id
//                radioButton = (RadioButton) findViewById(selectedId);
//
//                Toast.makeText(DoAssess.this,
//                        radioButton.getText(), Toast.LENGTH_SHORT).show();
//
//            }
//        });
//    }
}
