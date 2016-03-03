package com.example.hoangthao.eventplus.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hoangthao.eventplus.R;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by hoangthao on 20/02/16.
 */
public class Adapter extends ArrayAdapter<ArrayNew> {

    public Adapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public Adapter(Context context, int resource, List<ArrayNew> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.listview, null);
        }

        ArrayNew p = getItem(position);

        if (p != null) {
            // Anh xa + Gan gia tri
            TextView tt1 = (TextView) v.findViewById(R.id.tieude);
            tt1.setText(p.Tens);
            TextView tt2 = (TextView) v.findViewById(R.id.danhmuc);
            tt2.setText(String.valueOf("Danh mục: " + p.Danhmuc));
            TextView tt3 = (TextView) v.findViewById(R.id.thoigan);
            tt3.setText(String.valueOf("Thời gian: " + p.Thoigian));
            TextView tt4 = (TextView) v.findViewById(R.id.khuvuc);
            tt4.setText(String.valueOf("Khu vực: " + p.Khuvuc));

            //Ion.with((ImageView) v.findViewById(R.id.imageView)).load(p.Hinh);
            Ion.with(getContext())
                    .load(p.Hinhs)
                    .withBitmap()
                    .placeholder(R.drawable.ic_launcher)
                    .error(R.drawable.ic_launcher)
                    .intoImageView((ImageView) v.findViewById(R.id.imgv_1));

        }

        return v;
    }
}

