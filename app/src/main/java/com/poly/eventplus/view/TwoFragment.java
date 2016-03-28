package com.poly.eventplus.view;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.poly.eventplus.R;
import com.poly.eventplus.activity.NewEvent;
import com.poly.eventplus.adapter.RecyclerAdapter;
import com.poly.eventplus.model.Event;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class TwoFragment extends Fragment {


    private RecyclerAdapter recyclerAdapter;
    ArrayList<Event> mang;
    SwipeRefreshLayout refreshLayout;
    RecyclerView recyclerView;


    public TwoFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.twofragment, container, false);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        new ReadJson().execute("http://trieu.svnteam.net/Api/SelectListOrderbyTime1.php");
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);

    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final TwoFragment.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }


        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }


    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        refreshLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.swipe_layout2);
        recyclerView = (RecyclerView) getActivity().findViewById(R.id.recyclerview2);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mang.clear();
                new ReadJson().execute("http://trieu.svnteam.net/Api/SelectListOrderbyTime1.php");

            }


        });
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new ReadJson().execute("http://trieu.svnteam.net/Api/SelectListOrderbyTime1.php");
            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                String ten1 = mang.get(position).getTens();
                Log.d("i", ten1);
                String img = mang.get(position).getHinhs();
                Log.d("i", img);
                int sdt1 = mang.get(position).getSdt();
                int id = mang.get(position).getId();
                double rate = mang.get(position).getRate();
                String time1 = mang.get(position).getThoigian();
                String danhmuc1 = mang.get(position).getDanhmuc();
                String khuvuc1 = mang.get(position).getKhuvuc();
                String sokhach1 = mang.get(position).getSokhach();
                String donvi1 = mang.get(position).getDonvi();
                String diadiem1 = mang.get(position).getDiadiem();
                String mota1 = mang.get(position).getMota();
                int countorder = mang.get(position).getCountOrder();
                Intent intent = new Intent(getActivity(), NewEvent.class);
                Bundle bundle = new Bundle();
                bundle.putInt("id",id);
                bundle.putString("ten", ten1);
                bundle.putString("img", img);
                bundle.putString("time", time1);
                bundle.putString("danhmuc", danhmuc1);
                bundle.putString("khuvuc", khuvuc1);
                bundle.putString("sokhach", sokhach1);
                bundle.putString("donvi", donvi1);
                bundle.putInt("Countoder", countorder);
                bundle.putInt("sdt", sdt1);
                bundle.putString("diadiem", diadiem1);
                bundle.putString("mota", mota1);
                bundle.putInt("Countoder", countorder);
                bundle.putDouble("rate", rate);
                intent.putExtra("mybundle", bundle);
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


    }

    class ReadJson extends AsyncTask<String, Integer, String> {
        //Load json ngam
        @Override
        protected String doInBackground(String... params) {
            return docNoiDung_Tu_URL(params[0]);
        }

        //Hien thi va cac tac vu vs json doc dc tu json
        @Override
        protected void onPostExecute(String s) {
            Log.d("Json ", s);
            try {
                JSONArray jsonArray = new JSONArray(s);
                mang = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject sp = jsonArray.getJSONObject(i);
                    Event item = new Event();
                    item.setRate(sp.getDouble("Avgrate"));
                    item.setCountOrder(sp.getInt("Countoder"));
                    item.setTens(sp.getString("name"));
                    item.setDanhmuc(sp.getString("danhmuc"));
                    item.setDiadiem(sp.getString("diadiem"));
                    item.setDonvi(sp.getString("donvi"));
                    item.setKhuvuc(sp.getString("khuvuc"));
                    item.setMota(sp.getString("mota"));
                    String time1 = sp.getString("time");
                    String subtime = time1.substring(0, 16);
                    item.setThoigian(subtime);
                    item.setSdt(sp.getInt("sdt"));
                    item.setSokhach(sp.getString("sokhach"));
                    item.setHinhs(sp.getString("img"));
                    item.setId(sp.getInt("id"));
                    mang.add(item);
                    Log.d("a", "" + mang);
                    recyclerAdapter = new RecyclerAdapter(getActivity(), i, mang);
                    recyclerView.setAdapter(recyclerAdapter);
                    recyclerAdapter.notifyDataSetChanged();
                    // stopping swipe refresh
                    refreshLayout.setRefreshing(false);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


    }

    //DOc file json ve may
    private static String docNoiDung_Tu_URL(String theUrl) {
        StringBuilder content = new StringBuilder();

        // many of these calls can throw exceptions, so i've just
        // wrapped them all in one try/catch statement.
        try {
            // create a url object
            URL url = new URL(theUrl);

            // create a urlconnection object
            URLConnection urlConnection = url.openConnection();

            // wrap the urlconnection in a bufferedreader
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            String line;

            // read from the urlconnection via the bufferedreader
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line + "\n");
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content.toString();
    }

    private String getXmlFromUrl(String urlString) {
        String xml = null;
        try {
            // defaultHttpClient lấy toàn bộ dữ liệu trong http đổ vào 1 chuỗi String
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(urlString);

            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            xml = EntityUtils.toString(httpEntity, HTTP.UTF_8);
            // set UTF-8 cho ra chữ unikey
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return xml;
    }
}

