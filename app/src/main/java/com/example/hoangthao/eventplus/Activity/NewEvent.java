package com.example.hoangthao.eventplus.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hoangthao.eventplus.R;
import com.koushikdutta.ion.Ion;

/**
 * Created by hoangthao on 20/02/16.
 */
public class NewEvent extends AppCompatActivity {
    TextView tieude, time, danhmuc, sdt, sokhach, donvi, diadiem, mota;
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newsevent);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }

        });
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
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int width = displaymetrics.widthPixels;

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("mybundle");
        String tens = bundle.getString("ten");
        actionBar.setTitle(tens);
        String imgs = bundle.getString("img");
        String times = bundle.getString("time");
        String danhmucs = bundle.getString("danhmuc");
        String khuvucs = bundle.getString("khuvuc");
        String sokhachs = bundle.getString("sokhach");
        String donvis = bundle.getString("donvi");
        String diadiems = bundle.getString("diadiem");
        String motas = bundle.getString("mota");
        int sdts = bundle.getInt("sdt");

        sdt.setText(""  + sdts);
        time.setText(times);
        tieude.setText(tens);
        danhmuc.setText(danhmucs);
        sokhach.setText(sokhachs);
        donvi.setText(donvis);
        diadiem.setText(diadiems);
        mota.setText(motas);
        Ion.with(getApplicationContext())
                .load(imgs)
                .withBitmap()
                .placeholder(R.drawable.ic_launcher)
                .error(R.drawable.ic_launcher)
                .resizeWidth(width)
                .intoImageView(img);
    }

}
