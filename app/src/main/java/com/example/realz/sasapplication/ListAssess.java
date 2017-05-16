package com.example.realz.sasapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
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

public class ListAssess extends AppCompatActivity {

    private ListView ListViewJSon;
    private ArrayList<String> exData;
    private ArrayList<String> exData2;
    private ArrayAdapter<String> namesAA;
    private ProgressDialog progressDialog;
    private String alumni_id;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_assess);

        MainActivity main = new MainActivity();
        alumni_id = main.getAlumni_id();

//      btnHit2 = (Button) findViewById(R.id.btn3);
//      txtJson2 = (TextView) findViewById(R.id.textjson2);
        ListViewJSon = (ListView)findViewById(R.id.listvieww);

        exData = new ArrayList<String>();
        exData.clear();

        exData2 = new ArrayList<String>();
        exData2.clear();

//        exData2.add("adsadasd1");
//        exData2.add("adsadasd2");
//        exData2.add("adsadasd3");
//        exData2.add("adsadasd4");
//        exData2.add("adsadasd5");
//        exData2.add("adsadasd6");
//        exData2.add("adsadasd7");

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = new ProgressDialog(ListAssess.this);
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Downloding ..... ");
                progressDialog.show();
            }

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    disableSSLCertificateChecking();
                    URL url = new URL("https://10.51.4.17/TSP57/PCK/index.php/sas/Alumni/DoAssess/ListAssess?id="+alumni_id);

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
                        exArray = jsonObject.getJSONArray("assess");
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
                            exData.add(jsonObj.getString("assess_detail"));
                            exData2.add(jsonObj.getString("assess_id"));

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
                final ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(ListAssess.this,R.layout.mylistviewdone,R.id.listview_text,exData);
                final ArrayAdapter<String> myAdapter2 = new ArrayAdapter<String>(ListAssess.this,R.layout.mylistviewdone,R.id.id_assess,exData2);
                ListViewJSon.setAdapter(myAdapter);

                ListViewJSon.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //Toast.makeText(getApplicationContext(),String.valueOf(adapter.getItem(position)),
                        //Toast.LENGTH_SHORT).show();

                        //UserActivity แก้เป็น Class ที่จะให้ไป
                        Intent editIntent = new Intent(getApplicationContext(), DoAssess.class);
                        editIntent.putExtra("editTodolist", (Serializable) myAdapter.getItem(position));
                        editIntent.putExtra("assess_id", (Serializable) myAdapter2.getItem(position));
                        //Toast.makeText(getApplicationContext(), myAdapter2.getItem(position), Toast.LENGTH_SHORT).show();
                        startActivity(editIntent);
                    }
                });

            }
        }.execute();

        //namesAA = new ArrayAdapter<String>( this, android.R.layout.simple_list_item_1, android.R.id.text1, exData );
        //ListViewJSon.setAdapter(namesAA);

        //ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,android.R.id.text1,exData);
        //ListViewJSon.setAdapter(myAdapter);

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
