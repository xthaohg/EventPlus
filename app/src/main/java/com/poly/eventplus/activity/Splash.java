package com.poly.eventplus.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import com.poly.eventplus.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class Splash extends Activity {
    private static final int REQUEST_CODE = 10;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String USERNAME = "userNameKey";
    public static final String PASS = "passKey";
    SharedPreferences sharedpreferences;

    TextView textUser, textPass;
    private final int SPLASH_DISPLAY_LENGTH = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        textPass = (TextView) findViewById(R.id.txtPass);
        textUser = (TextView) findViewById(R.id.txtUser);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                loadData();
            }
        }, SPLASH_DISPLAY_LENGTH);

    }

    class goiweb extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            return makePostRequest(params[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            Log.d("Tra ve: ", s);
            if (s.equals("NO")) {
                Intent i = new Intent(getApplicationContext(), Login.class);
                startActivity(i);
            }
            if (s.equals("OK")) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.putExtra("username", textUser.getText().toString());
                i.putExtra("pass", textPass.getText().toString());
                startActivityForResult(i, REQUEST_CODE);
                finish();
            }

        }

    }

    private void loadData() {
        textUser.setText(sharedpreferences.getString(USERNAME, ""));
        textPass.setText(sharedpreferences.getString(PASS, ""));
        if (sharedpreferences.getString(USERNAME, "").equals("") && sharedpreferences.getString(PASS, "").equals("")) {
            Intent i = new Intent(getApplicationContext(), Login.class);
            startActivity(i);
        } else {
            new goiweb().execute("http://trieu.svnteam.net/Api/login.php");
        }
    }

    private String makePostRequest(String u) {

        HttpClient httpClient = new DefaultHttpClient();

        // URL của trang web nhận request
        HttpPost httpPost = new HttpPost(u);

        // Các tham số truyền
        List nameValuePair = new ArrayList(3);
        nameValuePair.add(new BasicNameValuePair("action", "selectuser"));
        nameValuePair.add(new BasicNameValuePair("username", sharedpreferences.getString(USERNAME, "")));
        nameValuePair.add(new BasicNameValuePair("pass", sharedpreferences.getString(PASS, "")));

        //Encoding POST data
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String kq = "";
        try {
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            kq = EntityUtils.toString(entity);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return kq;
    }
}
