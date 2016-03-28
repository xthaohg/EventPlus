package com.poly.eventplus.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.ion.Ion;
import com.poly.eventplus.R;
import com.poly.eventplus.model.Event;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class NewEvent extends AppCompatActivity {
    TextView tieude, time, danhmuc, sdt, sokhach, donvi, diadiem, mota, songuoithamdu, tvRate;
    ImageView img;
    private int countorder;
    private String ids, txtRating;
    private RatingBar ratingBar, rbStatus;
    private Dialog rankDialog;
    Button btn, btnRate;
    private WebView webView;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String USERNAME = "userNameKey";
    public static final String PASS = "passKey";
    SharedPreferences sharedpreferences;
    public static final String IDUSER = "iduserKey";
    public static final String EMAIl = "emailKey";
    private boolean statusOrder = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newsevent);
        controller();
        Action();
        new StatusOrder().execute("http://trieu.svnteam.net/Api/checkoder.php");
        new EventDetails().execute("http://trieu.svnteam.net/Api/selectlistbyid.php");
    }

    public void Action() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.mipmap.ic_arrow_back_white_24dp);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }

        });

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int width = displaymetrics.widthPixels;
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("mybundle");
        String tens = bundle.getString("ten");
        actionBar.setTitle(tens);
        int id = bundle.getInt("id");
        ids = id + "";
        countorder = bundle.getInt("Countoder");
        String imgs = bundle.getString("img");
        String times = bundle.getString("time");
        String danhmucs = bundle.getString("danhmuc");
        String khuvucs = bundle.getString("khuvuc");
        String sokhachs = bundle.getString("sokhach");
        String donvis = bundle.getString("donvi");
        String diadiems = bundle.getString("diadiem");
        String motas = bundle.getString("mota");
        Double drate = bundle.getDouble("rate");
        Float rate = drate.floatValue();
        int sdts = bundle.getInt("sdt");

        sdt.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder adb = new AlertDialog.Builder(NewEvent.this);
                adb.setTitle("Thông báo");
                adb.setMessage("Bạn có muốn gọi đến người quản lí sự kiện này không?");
                adb.setPositiveButton("Có", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        String phone_no = sdt.getText().toString().replaceAll("-", "");
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + phone_no));
                        callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(callIntent);
                    }
                });

                adb.setNegativeButton("Không", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        arg0.dismiss();
                    }
                });
                AlertDialog ad = adb.create();
                ad.show();
                return true;
            }
        });
        sdt.setText("0" + sdts);
        time.setText(times);
        tieude.setText(tens);
        danhmuc.setText(danhmucs);
        sokhach.setText(sokhachs);
        donvi.setText(donvis);
        diadiem.setText(diadiems);
        mota.setText(motas);
        songuoithamdu.setText(countorder + "");
        rbStatus.setRating(rate);
        tvRate.setText(new DecimalFormat("0.0").format(rate));
        Ion.with(getApplicationContext())
                .load(imgs)
                .withBitmap()
                .placeholder(R.drawable.camera)
                .error(R.drawable.ic_launcher)
                .resizeWidth(width)
                .intoImageView(img);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (statusOrder) {
                    new CancelOrder().execute("http://trieu.svnteam.net/Api/huydangky.php");
                } else {
                    new Order().execute("http://trieu.svnteam.net/Api/oder.php");
                }
            }
        });

        rbStatus.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                rbStatus.setRating(rating);
                txtRating = String.valueOf(rating);
                new Rate().execute("http://trieu.svnteam.net/Api/rate.php");
            }
        });

        Log.d("id", ids);

        btnRate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                rankDialog = new Dialog(NewEvent.this);
                rankDialog.setContentView(R.layout.rank_dialog);
                rankDialog.setCancelable(true);
                ratingBar = (RatingBar) rankDialog.findViewById(R.id.ratingBar);
                ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                        txtRating = String.valueOf(rating);
                    }
                });
                Button updateButton = (Button) rankDialog.findViewById(R.id.rank_dialog_button);
                updateButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new Rate().execute("http://trieu.svnteam.net/Api/rate.php");
                            }
                        });
                        rankDialog.dismiss();
                    }
                });
                //now that the dialog is set up, it's time to show it
                rankDialog.show();
            }
        });
        webView.getSettings().setJavaScriptEnabled(true);
        String html =motas ;
        webView.loadData(html, "text/html", "UTF-8");
    }

    public void controller() {
        webView = (WebView) findViewById(R.id.webview);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        songuoithamdu = (TextView) findViewById(R.id.txtSonguoithamdu);
        btnRate = (Button) findViewById(R.id.btn_rate);
        btn = (Button) findViewById(R.id.btn_thamdu);
        sdt = (TextView) findViewById(R.id.txtSdt);
        img = (ImageView) findViewById(R.id.imageView);
        tieude = (TextView) findViewById(R.id.txtTieude);
        time = (TextView) findViewById(R.id.txtthoigan);
        danhmuc = (TextView) findViewById(R.id.txtdanhmuc);
//        khuvuc = (TextView) findViewById(R.id.txtTieude);
        sokhach = (TextView) findViewById(R.id.txtSokhach);
        donvi = (TextView) findViewById(R.id.txtDonvi);
        diadiem = (TextView) findViewById(R.id.txtDiadiem);
        mota = (TextView) findViewById(R.id.txtNoidungchitet);
        rbStatus = (RatingBar) findViewById(R.id.rbStatus);
        tvRate = (TextView) findViewById(R.id.tvRate);
        LayerDrawable stars = (LayerDrawable) rbStatus.getProgressDrawable();
        stars.getDrawable(2).setColorFilter((Color.parseColor("#F1C40F")), PorterDuff.Mode.SRC_ATOP); // for filled stars
        stars.getDrawable(1).setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP); // for half filled stars
        stars.getDrawable(0).setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP); // for empty stars
    }

    class EventDetails extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            return detailsRequest(params[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("Tra ve ", result);
            try {
                JSONArray jsonArray = new JSONArray(result);
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                Double rate = jsonObject.getDouble("Avgrate");
                tvRate.setText((new DecimalFormat("0.0").format(rate)));
            } catch (Exception e) {

            }
        }
    }

    private String detailsRequest(String u) {
        HttpClient httpClient = new DefaultHttpClient();

        // URL của trang web nhận request
        HttpPost httpPost = new HttpPost(u);

        // Các tham số truyền
        List nameValuePair = new ArrayList(2);
        nameValuePair.add(new BasicNameValuePair("action", "connect"));
        Log.d("USERID", sharedpreferences.getString(IDUSER, ""));
        nameValuePair.add(new BasicNameValuePair("baivietid", ids));
        Log.d("IDBAIVIET", ids);


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

    class StatusOrder extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            return StatusRequest(params[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("Tra ve ", result);
            String msg;
            try {
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.has("join")) {
                    msg = jsonObject.getString("join");
                    if (msg.equals("yes")) {
                        statusOrder = true;
                        btn.setText("Hủy");
                    } else if (msg.equals("no")) {
                        btn.setText("Tham Dự");
                        statusOrder = false;
                    }
                }
            } catch (Exception e) {

            }
        }
    }

    private String StatusRequest(String u) {
        HttpClient httpClient = new DefaultHttpClient();

        // URL của trang web nhận request
        HttpPost httpPost = new HttpPost(u);

        // Các tham số truyền
        List nameValuePair = new ArrayList(3);
        nameValuePair.add(new BasicNameValuePair("action", "connect"));
        nameValuePair.add(new BasicNameValuePair("userid", sharedpreferences.getString(IDUSER, "")));
        Log.d("USERID", sharedpreferences.getString(IDUSER, ""));
        nameValuePair.add(new BasicNameValuePair("baivietid", ids));
        Log.d("IDBAIVIET", ids);


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

    class Order extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            return makePostRequest(params[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("Tra ve ", result);
            try {
                JSONArray jsonArray = new JSONArray(result);
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                String status = jsonObject.getString("status");
                if (status.equals("OK")) {
                    statusOrder = true;
                    btn.setText("Hủy");
                    songuoithamdu.setText(jsonObject.getString("songuoidangky"));
                    Toast.makeText(getApplicationContext(), "Đăng kí thành công", Toast.LENGTH_LONG).show();
                }
                if (result.equals("NO")) {
                    Toast.makeText(getApplicationContext(), "Bạn đã đăng kí sự kiện này", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {

            }
        }
    }

    private String makePostRequest(String u) {
        HttpClient httpClient = new DefaultHttpClient();

        // URL của trang web nhận request
        HttpPost httpPost = new HttpPost(u);

        // Các tham số truyền
        List nameValuePair = new ArrayList(3);
        nameValuePair.add(new BasicNameValuePair("action", "connect"));
        nameValuePair.add(new BasicNameValuePair("userid", sharedpreferences.getString(IDUSER, "")));
        Log.d("USERID", sharedpreferences.getString(IDUSER, ""));
        nameValuePair.add(new BasicNameValuePair("baivietid", ids));
        Log.d("IDBAIVIET", ids);

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

    class CancelOrder extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            return CancelRequest(params[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("Tra ve ", result);
            try {
                JSONArray jsonArray = new JSONArray(result);
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                String status = jsonObject.getString("status");
                if (status.equals("OK")) {
                    statusOrder = false;
                    btn.setText("Tham Dự");
                    songuoithamdu.setText(jsonObject.getString("songuoidangky"));
                    Toast.makeText(getApplicationContext(), "Hủy thành công", Toast.LENGTH_LONG).show();
                }
                if (result.equals("NO")) {
                    Toast.makeText(getApplicationContext(), "Bạn đã đăng kí sự kiện này", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {

            }
        }
    }

    private String CancelRequest(String u) {
        HttpClient httpClient = new DefaultHttpClient();

        // URL của trang web nhận request
        HttpPost httpPost = new HttpPost(u);

        // Các tham số truyền
        List nameValuePair = new ArrayList(3);
        nameValuePair.add(new BasicNameValuePair("action", "huy"));
        nameValuePair.add(new BasicNameValuePair("userid", sharedpreferences.getString(IDUSER, "")));
        Log.d("USERID", sharedpreferences.getString(IDUSER, ""));
        nameValuePair.add(new BasicNameValuePair("baivietid", ids));
        Log.d("IDBAIVIET", ids);


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

    class Rate extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            return Request(params[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            Log.d("Tra ve ", s);
            if (s.equals("OK")) {
                Toast.makeText(getApplicationContext(), "Đánh giá thành công! Cảm ơn bạn đã quan tâm", Toast.LENGTH_LONG).show();
                new EventDetails().execute("http://trieu.svnteam.net/Api/selectlistbyid.php");
            }
            if (s.equals("SAI")) {
                Toast.makeText(getApplicationContext(), "Có lỗi khi đánh giá, vui lòng đăng nhập lại", Toast.LENGTH_LONG).show();
            }

        }

    }

    private String Request(String u) {
        HttpClient httpClient = new DefaultHttpClient();

        // URL của trang web nhận request
        HttpPost httpPost = new HttpPost(u);

        // Các tham số truyền
        List nameValuePair = new ArrayList(4);
        nameValuePair.add(new BasicNameValuePair("action", "rate"));
        nameValuePair.add(new BasicNameValuePair("userid", sharedpreferences.getString(IDUSER, "")));
        nameValuePair.add(new BasicNameValuePair("baivietid", ids));
        nameValuePair.add(new BasicNameValuePair("postid", txtRating));


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
