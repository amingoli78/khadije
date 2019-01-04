package com.ermile.khadijeh;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ermile.khadijeh.network.AppContoroler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    public Handler mHandler;
    public boolean continue_or_stop;

    private Fragment fragment;
    private FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final int versionCode = BuildConfig.VERSION_CODE;
        String versionName = BuildConfig.VERSION_NAME;







        // JSON
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, "https://khadije.com/hook/app/android", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONArray navigation_btn = response.getJSONArray("navigation");
                    JSONObject pay = navigation_btn.getJSONObject(0);
                    JSONObject home = navigation_btn.getJSONObject(1);
                    JSONObject trip = navigation_btn.getJSONObject(2);

                    String trip_title = pay.getString("title");
                    final String trip_url = pay.getString("url");

                    String home_title = home.getString("title");
                    final String home_url = home.getString("url");

                    String pay_title = trip.getString("title");
                    final String pay_url = trip.getString("url");

                    //static
                    final BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
                    fragmentManager = getSupportFragmentManager();
                    bottomNav.setSelectedItemId(R.id.item_home);

                    final WebView webView = findViewById(R.id.webview);
                    WebSettings webSettings = webView.getSettings();
                    webSettings.setJavaScriptEnabled(true);
                    final SwipeRefreshLayout swipe = findViewById(R.id.swipref);
                    //------------------------------------------------------------
                    swipe.setRefreshing(true);
                    webView.setWebViewClient(new WebViewClient() {
                        @Override
                        public void onPageFinished(WebView view, String url) {
                            swipe.setRefreshing(false);
                        }});
                    // download json
                    final String url = home_url;
                    webView.loadUrl(url);

                    swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            webView.loadUrl(webView.getUrl());
                            Toast.makeText(MainActivity.this, "link is:"+webView.getUrl(), Toast.LENGTH_SHORT).show();
                        }
                    });



                    // Chek net every 5 seconds
                    mHandler = new Handler();
                    continue_or_stop = true;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (continue_or_stop) {
                                try {
                                    Thread.sleep(1000);
                                    mHandler.post(new Runnable() {
                                        @Override
                                        public void run() {

                                            if(bottomNav.getSelectedItemId() == R.id.item_home){

                                                if (webView.getUrl().equals("https://khadije.com")){}
                                                if (webView.getUrl().equals("https://khadije.com/donate")){bottomNav.setSelectedItemId(R.id.item_trip);}
                                            }
                                            if(bottomNav.getSelectedItemId() == R.id.item_trip){

                                                if (webView.getUrl().equals("https://khadije.com/donate")){}
                                                if (webView.getUrl().equals("https://khadije.com")){bottomNav.setSelectedItemId(R.id.item_home);}
                                            }

                                        }
                                    });
                                } catch (Exception e) {
                                }
                            }
                        }
                    }).start();





                    Menu menu = bottomNav.getMenu();

                    MenuItem trip_menu = menu.findItem(R.id.item_trip);
                    trip_menu.setTitle(trip_title);
                    // set size title for pay item
                    SpannableString spanString_tasharof = new SpannableString(menu.findItem(R.id.item_trip).getTitle().toString());
                    int end_tasharof = spanString_tasharof.length();
                    spanString_tasharof.setSpan(new RelativeSizeSpan(1.0f), 0, end_tasharof, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    trip_menu.setTitle(spanString_tasharof);

                    MenuItem home_menu = menu.findItem(R.id.item_home);
                    home_menu.setTitle(home_title);
                    // set size title for pay item
                    SpannableString spanString_home = new SpannableString(menu.findItem(R.id.item_home).getTitle().toString());
                    int end_home = spanString_home.length();
                    spanString_home.setSpan(new RelativeSizeSpan(0.8f), 0, end_home, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    home_menu.setTitle(spanString_home);

                    MenuItem pay_menu = menu.findItem(R.id.item_pay);
                    pay_menu.setTitle(pay_title);
                    // set size title for pay item
                    SpannableString spanString_pay = new SpannableString(menu.findItem(R.id.item_pay).getTitle().toString());
                    int end_pay = spanString_pay.length();
                    spanString_pay.setSpan(new RelativeSizeSpan(0.8f), 0, end_pay, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    pay_menu.setTitle(spanString_pay);

                    // toolbar and tab Top or Bottom?

                    bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                            switch (item.getItemId()) {

                                case R.id.item_trip:
                                    webView.loadUrl(trip_url);
                                    swipe.setRefreshing(true);
                                    webView.setWebViewClient(new WebViewClient() {
                                        @Override
                                        public void onPageFinished(WebView view, String url) {
                                            swipe.setRefreshing(false);
                                        }});

                                    break;

                                case R.id.item_home:
                                    webView.loadUrl(home_url);
                                    swipe.setRefreshing(true);
                                    webView.setWebViewClient(new WebViewClient() {
                                        @Override
                                        public void onPageFinished(WebView view, String url) {
                                            swipe.setRefreshing(false);
                                        }});
                                    break;

                                case R.id.item_pay:
                                    startActivity(new Intent(MainActivity.this,deviceinfo.class));
//                                    webView.loadUrl(pay_url);
//                                    swipe.setRefreshing(true);
//                                    webView.setWebViewClient(new WebViewClient() {
//                                        @Override
//                                        public void onPageFinished(WebView view, String url) {
//                                            swipe.setRefreshing(false);
//                                        }});
                                    break;

                            }
                            return true;
                        }
                    });

                    //////////////

                    // new version for app
                    int app_version = response.getInt("version");
                    if (versionCode < app_version) {
                        Notification.Builder nb = new Notification.Builder(MainActivity.this);
                        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                        nb.setContentTitle("بروزرسانی")
                                .setContentText("نسخه جدید رو دانلود کنید!")
                                .setTicker("برو دانلود کن دیگه")
                                .setSmallIcon(android.R.drawable.stat_sys_download)
                                .setAutoCancel(false)
                                .setSound(alarmSound);
                        Notification notif = nb.build();
                        NotificationManager notifManager = (NotificationManager) MainActivity.this.getSystemService(Context.NOTIFICATION_SERVICE);
                        notifManager.notify(0, notif);
                    }
                    // notif
                    boolean notif_bol = response.getBoolean("notif");
                    String notif_title = response.getString("title_notif");
                    String notif_des = response.getString("des_notif");
                    if (notif_bol == true) {
                        Notification.Builder nb = new Notification.Builder(MainActivity.this);
                        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                        nb.setContentTitle(notif_title)
                                .setContentText(notif_des)
                                .setTicker("برو دانلود کن دیگه")
                                .setSmallIcon(android.R.drawable.stat_sys_download)
                                .setAutoCancel(false)
                                .setSound(alarmSound);
                        Notification notif = nb.build();
                        NotificationManager notifManager = (NotificationManager) MainActivity.this.getSystemService(Context.NOTIFICATION_SERVICE);
                        notifManager.notify(1, notif);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });AppContoroler.getInstance().addToRequestQueue(req);
        // END JSON


    }



}
