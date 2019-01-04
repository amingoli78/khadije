package com.ermile.khadijeh;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.ermile.khadijeh.network.AppContoroler;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {


    private Fragment fragment;
    private FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final int versionCode = BuildConfig.VERSION_CODE;
        String versionName = BuildConfig.VERSION_NAME;

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


        final String url = "https://khadije.com";
        webView.loadUrl(url);

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                webView.loadUrl(url);
            }
        });

        Menu menu = bottomNav.getMenu();

        MenuItem tasharof = menu.findItem(R.id.item_tasharof);
        tasharof.setTitle("درخواست تشرف");
        // set size title for pay item
        SpannableString spanString_tasharof = new SpannableString(menu.findItem(R.id.item_tasharof).getTitle().toString());
        int end_tasharof = spanString_tasharof.length();
        spanString_tasharof.setSpan(new RelativeSizeSpan(0.8f), 0, end_tasharof, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tasharof.setTitle(spanString_tasharof);

        MenuItem home = menu.findItem(R.id.item_home);
        home.setTitle("صفحه اصلی");
        // set size title for pay item
        SpannableString spanString_home = new SpannableString(menu.findItem(R.id.item_home).getTitle().toString());
        int end_home = spanString_home.length();
        spanString_home.setSpan(new RelativeSizeSpan(0.8f), 0, end_home, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        home.setTitle(spanString_home);

        MenuItem pay = menu.findItem(R.id.item_pay);
        pay.setTitle("پرداخت نذورات");
        // set size title for pay item
        SpannableString spanString_pay = new SpannableString(menu.findItem(R.id.item_pay).getTitle().toString());
        int end_pay = spanString_pay.length();
        spanString_pay.setSpan(new RelativeSizeSpan(1.2f), 0, end_pay, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        pay.setTitle(spanString_pay);





        // JSON
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, "http://mimsg.ir/json_app/app.json", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

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
                    // toolbar and tab Top or Bottom?

                    bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                            switch (item.getItemId()) {

                                case R.id.item_tasharof:
                                    webView.loadUrl("https://khadije.com/trip");
                                    swipe.setRefreshing(true);
                                    webView.setWebViewClient(new WebViewClient() {
                                        @Override
                                        public void onPageFinished(WebView view, String url) {
                                            swipe.setRefreshing(false);
                                        }});

                                    break;

                                case R.id.item_home:
                                    webView.loadUrl("https://khadije.com");
                                    swipe.setRefreshing(true);
                                    webView.setWebViewClient(new WebViewClient() {
                                        @Override
                                        public void onPageFinished(WebView view, String url) {
                                            swipe.setRefreshing(false);
                                        }});
                                    break;

                                case R.id.item_pay:
                                    webView.loadUrl("https://khadije.com/donate");
                                    swipe.setRefreshing(true);
                                    webView.setWebViewClient(new WebViewClient() {
                                        @Override
                                        public void onPageFinished(WebView view, String url) {
                                            swipe.setRefreshing(false);
                                        }});
                                    break;

                            }
                            return true;
                        }
                    });

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
