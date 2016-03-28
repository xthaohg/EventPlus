package com.poly.eventplus.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.poly.eventplus.R;
import com.poly.eventplus.view.OneFragment;
import com.poly.eventplus.view.ThreeFragment;
import com.poly.eventplus.view.TwoFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TextView txtUserName, txtEmail;

    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkInternet();
        controller();
        getBunlde();
    }

    public void controller() {
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        txtUserName = (TextView) findViewById(R.id.tv_username);
        txtEmail = (TextView) findViewById(R.id.tv_email);
        txtUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("click: ", "Clicked user");
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.mipmap.ic_menu_white_18dp);
        actionBar.setDisplayHomeAsUpEnabled(true);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(3);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_item_Hoithao:
                        Intent intent = new Intent(getApplicationContext(), Seminor.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("title", getText(R.string.hoithao).toString());
                        bundle.putString("API", "http://trieu.svnteam.net/Api/SelectDanhmuc1.php");
                        intent.putExtra("mybundle", bundle);
                        startActivity(intent);
                        return true;
                    case R.id.navigation_item_Vanhoa:
                        intent = new Intent(getApplicationContext(), Seminor.class);
                        bundle = new Bundle();
                        bundle.putString("title", getText(R.string.vanhoa).toString());
                        bundle.putString("API", "http://trieu.svnteam.net/Api/SelectDanhmuc2.php");
                        intent.putExtra("mybundle", bundle);
                        startActivity(intent);
                        return true;
                    case R.id.navigation_item_Hoicho:
                        intent = new Intent(getApplicationContext(), Seminor.class);
                        bundle = new Bundle();
                        bundle.putString("title", getText(R.string.hoicho).toString());
                        bundle.putString("API", "http://trieu.svnteam.net/Api/SelectDanhmuc3.php");
                        intent.putExtra("mybundle", bundle);
                        startActivity(intent);
                        return true;
                    case R.id.navigation_item_Thethao:
                        intent = new Intent(getApplicationContext(), Seminor.class);
                        bundle = new Bundle();
                        bundle.putString("title", getText(R.string.thethao).toString());
                        bundle.putString("API", "http://trieu.svnteam.net/Api/SelectDanhmuc4.php");
                        intent.putExtra("mybundle", bundle);
                        startActivity(intent);
                        return true;
                    case R.id.navigation_item_Khaichuong:
                        intent = new Intent(getApplicationContext(), Seminor.class);
                        bundle = new Bundle();
                        bundle.putString("title", getText(R.string.khaitruong).toString());
                        bundle.putString("API", "http://trieu.svnteam.net/Api/SelectDanhmuc5.php");
                        intent.putExtra("mybundle", bundle);
                        startActivity(intent);
                        return true;
                    case R.id.navigation_item_Khac:
                        intent = new Intent(getApplicationContext(), Seminor.class);
                        bundle = new Bundle();
                        bundle.putString("title", getText(R.string.skkhac).toString());
                        bundle.putString("API", "http://trieu.svnteam.net/Api/SelectDanhmuc6.php");
                        intent.putExtra("mybundle", bundle);
                        startActivity(intent);
                        return true;
                    case R.id.history:
                        intent = new Intent(getApplicationContext(), HistoryActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.logout:
                        clearData();
                        finish();
                        Intent lg = new Intent(getApplicationContext(), Login.class);
                        startActivity(lg);
                }
                return true;
            }
        });
    }

    public void checkInternet() {
        if (isOnline() == true) {

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
                    }
            );

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

    public void getBunlde() {
        Bundle extras = getIntent().getExtras();
        String value1 = extras.getString("username");
        String value2 = extras.getString("email");
        txtUserName.setText("ID: " + value1);
        txtEmail.setText("Email:" + value2);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new OneFragment(), "Nổi bật");
        adapter.addFragment(new TwoFragment(), "Sắp diễn ra");
        adapter.addFragment(new ThreeFragment(), "Mới đăng");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private void clearData() {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();
        editor.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;

            case R.id.search_bar:
                Intent o = new Intent(getApplicationContext(), Search.class);
                startActivity(o);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            showExitDialog();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void showExitDialog() {
        // TODO Auto-generated method stub
        AlertDialog.Builder adb = new AlertDialog.Builder(MainActivity.this);
        adb.setTitle("Cảnh báo");
        adb.setMessage("Bạn có thật sự muốn thoát ?");
        adb.setPositiveButton("Có", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                finish();
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
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
