package com.poly.eventplus.view;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.poly.eventplus.R;
import com.poly.eventplus.activity.NewEvent;
import com.poly.eventplus.adapter.EventAdapter;
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

public class ThreeFragment extends Fragment {
    ListView lv;
    ArrayList<Event> mang;
    SwipeRefreshLayout mSwipeRefreshLayout;

    public ThreeFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.threefragment, container, false);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mSwipeRefreshLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.activity_main_swipe_refresh_layout3);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mang.clear();
                new ReadJson().execute("http://trieu.svnteam.net/Api/selectlistOrderbyTime.php");
            }
        });
        lv = (ListView) getActivity().findViewById(R.id.lv_3);
        mang = new ArrayList<Event>();
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new ReadJson().execute("http://trieu.svnteam.net/Api/selectlistOrderbyTime.php");
            }
        });

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
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject sp = jsonArray.getJSONObject(i);
                    mang.add(new Event(
                            sp.getString("name"),
                            sp.getString("time"),
                            sp.getString("img"),
                            sp.getString("danhmuc"),
                            sp.getString("khuvuc"),
                            sp.getString("sokhach"),
                            sp.getString("donvi"),
                            sp.getString("mota"),
                            sp.getString("diadiem"),
                            sp.getInt("sdt")
                    ));
                }
                EventAdapter eventAdapter = new EventAdapter(getActivity(), R.layout.event_adapter, mang);
                if (mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
                lv.setAdapter(eventAdapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String ten = mang.get(position).getTens();
                    Log.d("i", ten);
                    String img = mang.get(position).getHinhs();
                    Log.d("i", img);
                    int sdt = mang.get(position).getSdt();
                    String time = mang.get(position).getThoigian();
                    String danhmuc = mang.get(position).getDanhmuc();
                    String khuvuc = mang.get(position).getKhuvuc();
                    String sokhach = mang.get(position).getSokhach();
                    String donvi = mang.get(position).getDonvi();
                    String diadiem = mang.get(position).getDiadiem();
                    String mota = mang.get(position).getMota();
                    Intent intent = new Intent(getActivity(), NewEvent.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("ten", ten);
                    bundle.putString("img", img);
                    bundle.putString("time", time);
                    bundle.putString("danhmuc", danhmuc);
                    bundle.putString("khuvuc", khuvuc);
                    bundle.putString("sokhach", sokhach);
                    bundle.putString("donvi", donvi);
                    bundle.putInt("sdt", sdt);
                    bundle.putString("diadiem", diadiem);
                    bundle.putString("mota", mota);
                    intent.putExtra("mybundle", bundle);
                    startActivity(intent);
                }
            });
            // Toast.makeText(two.this, s, Toast.LENGTH_LONG).show();
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
