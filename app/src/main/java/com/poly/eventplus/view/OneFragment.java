package com.poly.eventplus.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
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

import com.poly.eventplus.activity.NewEvent;
import com.poly.eventplus.R;
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

public class OneFragment extends Fragment {

    private RecyclerAdapter recyclerAdapter;
    ArrayList<Event> mang;
    SwipeRefreshLayout refreshLayout;
    RecyclerView recyclerView;
    private ProgressDialog dialog;
    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences sharedpreferences;

    public OneFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.onefragment, container, false);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        return view;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        new ReadJson().execute("http://trieu.svnteam.net/Api/selectlist.php");
        super.onResume();
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private OneFragment.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final OneFragment.ClickListener clickListener) {
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
        refreshLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.swipe_layout);
        recyclerView = (RecyclerView) getActivity().findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mang.clear();
                new ReadJson().execute("http://trieu.svnteam.net/Api/selectlist.php");

            }


        });
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new ReadJson().execute("http://trieu.svnteam.net/Api/selectlist.php");
            }
        });
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                String ten = mang.get(position).getTens();
                String img = mang.get(position).getHinhs();
                int sdt = mang.get(position).getSdt();
                int id = mang.get(position).getId();
                double rate = mang.get(position).getRate();
                String time = mang.get(position).getThoigian();
                Log.i("Time", time.toString());
                String subtime = time.substring(0, 16);
                String danhmuc = mang.get(position).getDanhmuc();
                String khuvuc = mang.get(position).getKhuvuc();
                String sokhach = mang.get(position).getSokhach();
                String donvi = mang.get(position).getDonvi();
                String diadiem = mang.get(position).getDiadiem();
                String mota = mang.get(position).getMota();
                int countorder = mang.get(position).getCountOrder();
                Log.d("count", countorder + "");
                Intent intent = new Intent(getActivity(), NewEvent.class);
                Bundle bundle = new Bundle();
                bundle.putInt("id", id);
                bundle.putString("ten", ten);
                bundle.putString("img", img);
                bundle.putString("time", subtime);
                bundle.putString("danhmuc", danhmuc);
                bundle.putString("khuvuc", khuvuc);
                bundle.putString("sokhach", sokhach);
                bundle.putString("donvi", donvi);
                bundle.putInt("sdt", sdt);
                bundle.putString("diadiem", diadiem);
                bundle.putString("mota", mota);
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

}

