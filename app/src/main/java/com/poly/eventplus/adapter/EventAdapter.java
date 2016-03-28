package com.poly.eventplus.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.poly.eventplus.R;
import com.poly.eventplus.model.Event;
import com.squareup.picasso.Picasso;

import java.util.List;

public class EventAdapter extends ArrayAdapter<Event> {

    public EventAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public EventAdapter(Context context, int resource, List<Event> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.event_adapter, null);
        }

        Event p = getItem(position);

        if (p != null) {
            // Anh xa + Gan gia tri
            TextView tt1 = (TextView) v.findViewById(R.id.tieude);
            tt1.setText(p.getTens());
            TextView tt2 = (TextView) v.findViewById(R.id.danhmuc);
            tt2.setText(p.getDanhmuc());
            TextView tt3 = (TextView) v.findViewById(R.id.thoigan);
            tt3.setText(p.getThoigian());
            TextView tt4 = (TextView) v.findViewById(R.id.khuvuc);
            tt4.setText(p.getKhuvuc());
            Picasso.with(getContext())
                    .load(p.getHinhs())
                    .placeholder(R.drawable.ic_launcher)
                    .error(R.drawable.ic_launcher)
                    .into((ImageView) v.findViewById(R.id.imgv_1));
//            Ion.with(getContext())
//                    .load(p.getHinhs())
//                    .withBitmap()
//                    .placeholder(R.drawable.ic_launcher)
//                    .error(R.drawable.ic_launcher)
//                    .intoImageView((ImageView) v.findViewById(R.id.imgv_1));
        }
        return v;
    }
}

