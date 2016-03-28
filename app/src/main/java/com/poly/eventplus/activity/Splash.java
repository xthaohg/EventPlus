package com.poly.eventplus.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
    public static final String IDUSER = "iduserKey";
    public static final String EMAIl = "emailKey";
    SharedPreferences sharedpreferences;

    TextView textUser, textPass;
    private final int SPLASH_DISPLAY_LENGTH = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        checkInternet();
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        ImageView img = (ImageView) findViewById(R.id.logonew);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(getIntent());
            }
        });


    }

    public void checkInternet() {
        if (isOnline() == true) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                    loadData();
                }
            }, SPLASH_DISPLAY_LENGTH);

        } else {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                    this);

            // Setting Dialog Title
            alertDialog.setTitle("Thông báo");

            // Setting Dialog Message
            alertDialog.setMessage("Bạn có muốn bật wifi để sử dụng Event Plus không?");

            // Setting Icon to Dialog
            // alertDialog.setIcon(R.drawable.ic_launcher);

            // Setting Positive "Yes" Button
            alertDialog.setPositiveButton("Có",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            onPause();
                            // Activity transfer to wifi settings
                            startActivityForResult(new Intent(Settings.ACTION_WIFI_SETTINGS), 0);
                            onDestroy();
                        }
                    });

            // Setting Negative "NO" Button
            alertDialog.setNegativeButton("Không",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to invoke NO event
                            dialog.cancel();
                            finish();
                        }
                    });

            // Showing Alert Message
            alertDialog.show();

        }
    }

    class goiweb extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            return makePostRequest(params[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            Log.d("Tra ve: ", s);
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            i.putExtra("username", sharedpreferences.getString(USERNAME, ""));
            i.putExtra("email", sharedpreferences.getString(EMAIl, ""));
            startActivityForResult(i, REQUEST_CODE);
            finish();
//            if (s.equals("NO")) {
//                Intent i = new Intent(getApplicationContext(), Login.class);
//                startActivity(i);
//            }
//            if (s.equals("OK")) {
//                Intent i = new Intent(getApplicationContext(), MainActivity.class);
//                i.putExtra("username", textUser.getText().toString());
//                i.putExtra("pass", textPass.getText().toString());
//                startActivityForResult(i, REQUEST_CODE);
//                finish();
//            }

        }

    }

    private void loadData() {
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

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


}
