package com.ermile.khadije;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.ermile.khadije.network.AppContoroler;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    int versionCode = 0 ;
    String versionName = "";

    Handler mHandler_one;
    boolean continue_or_stop_one;
    Handler mHandler_two;
    boolean continue_or_stop_two;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Uri data = getIntent().getData();

        Uri uri = Uri.parse(String.valueOf(data));

        String status = uri.getQueryParameter("status");
        String amount = uri.getQueryParameter("amount");


        Toast.makeText(this, "O: "+status + " Pric: "+ amount, Toast.LENGTH_SHORT).show();


        try {
            PackageInfo pInfo = getApplicationContext().getPackageManager().getPackageInfo(getPackageName(), 0);
            versionCode = pInfo.versionCode;
            versionName = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        // chake Notif
        post_smile();
        mHandler_two = new Handler();
        continue_or_stop_two = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (continue_or_stop_two) {
                    try {
                        Thread.sleep(30000);
                        mHandler_two.post(new Runnable() {
                            @Override
                            public void run() {
                                post_smile();
                            }
                        });
                    } catch (Exception e) {
                    }
                }
            }
        }).start();



        // get title in setting
        final String titlepay = getIntent().getStringExtra("payTitle");
        final String titlehome = getIntent().getStringExtra("homeTitle");
        final String titletrip = getIntent().getStringExtra("tripTitle");
        final String titledelneveshte = getIntent().getStringExtra("delneveshteTitle");
        final String titlesetting = getIntent().getStringExtra("settingTitle");



        // for false animate in bottom navigation
        final SwipeRefreshLayout swipe = findViewById(R.id.swipref);

        final BottomNavigationViewEx bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.item_home);
        bottomNav.enableAnimation(false);
        bottomNav.enableShiftingMode(false);
        bottomNav.enableItemShiftingMode(false);
        bottomNav.setTextSize(10f);
        bottomNav.setIconSize(28,28);

        // menu
        Menu menu = bottomNav.getMenu();
        final MenuItem pay_menu = menu.findItem(R.id.item_pay);
        final MenuItem home_menu = menu.findItem(R.id.item_home);
        final MenuItem trip_menu = menu.findItem(R.id.item_trip);
        final MenuItem delneveshte_menu = menu.findItem(R.id.item_delneveshte);
        final MenuItem setting_menu = menu.findItem(R.id.item_setting);

        if (getIntent().getBooleanExtra("welcome_title", false)) {
            pay_menu.setTitle(titlepay);
            home_menu.setTitle(titlehome);
            trip_menu.setTitle(titletrip);
            delneveshte_menu.setTitle(titledelneveshte);
            setting_menu.setTitle(titlesetting);
        }

        if (getIntent().getBooleanExtra("pay", false)) {
            bottomNav.setSelectedItemId(R.id.item_pay);
            pay_menu.setTitle(titlepay);
            home_menu.setTitle(titlehome);
            trip_menu.setTitle(titletrip);
            delneveshte_menu.setTitle(titledelneveshte);
            setting_menu.setTitle(titlesetting);
        }
        if (getIntent().getBooleanExtra("home", false)) {
            bottomNav.setSelectedItemId(R.id.item_home);
            pay_menu.setTitle(titlepay);
            home_menu.setTitle(titlehome);
            trip_menu.setTitle(titletrip);
            delneveshte_menu.setTitle(titledelneveshte);
            setting_menu.setTitle(titlesetting);
        }
        if (getIntent().getBooleanExtra("trip", false)) {
            bottomNav.setSelectedItemId(R.id.item_trip);
            pay_menu.setTitle(titlepay);
            home_menu.setTitle(titlehome);
            trip_menu.setTitle(titletrip);
            delneveshte_menu.setTitle(titledelneveshte);
            setting_menu.setTitle(titlesetting);
        }
        if (getIntent().getBooleanExtra("hert", false)) {
            bottomNav.setSelectedItemId(R.id.item_delneveshte);
            pay_menu.setTitle(titlepay);
            home_menu.setTitle(titlehome);
            trip_menu.setTitle(titletrip);
            delneveshte_menu.setTitle(titledelneveshte);
            setting_menu.setTitle(titlesetting);
        }




        // import SharedPreferences
        final SharedPreferences shared = getSharedPreferences("Prefs", MODE_PRIVATE);
        final SharedPreferences.Editor editor = shared.edit();
        // import manual
        final Boolean farsi = shared.getBoolean("farsi", false);
        final Boolean arabic = shared.getBoolean("arabic", false);
        final Boolean english = shared.getBoolean("english", false);
        // set lang
        String url = "";
        if (farsi){
            url = "https://khadije.com/api/v5/android";
        }
        if (arabic){
            url = "https://khadije.com/ar/api/v5/android";
        }
        if (english){
            url = "https://khadije.com/en/api/v5/android";
        }

        // JSON
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    final Handler mHandler;
                    final boolean continue_or_stop;


                    final Map<String, String> sernd_headers = new HashMap<String, String>();
                    sernd_headers.put("x-app-request", "android");

                    JSONArray navigation_btn = response.getJSONArray("navigation");
                    final JSONObject pay = navigation_btn.getJSONObject(0);
                    JSONObject home = navigation_btn.getJSONObject(1);
                    JSONObject trip = navigation_btn.getJSONObject(2);
                    JSONObject delneveshte = navigation_btn.getJSONObject(3);
                    JSONObject setting = navigation_btn.getJSONObject(4);

                    final String pay_title = pay.getString("title");
                    final String pay_url = pay.getString("url");

                    final String home_title = home.getString("title");
                    final String home_url = home.getString("url");

                    final String trip_title = trip.getString("title");
                    final String trip_url = trip.getString("url");

                    final String delneveshte_title = delneveshte.getString("title");
                    final String delneveshte_url = delneveshte.getString("url");

                    final String setting_title = setting.getString("title");
                    final String setting_url = setting.getString("url");

                    final WebView webView = findViewById(R.id.webview);
                    WebSettings webSettings = webView.getSettings();
                    webSettings.setJavaScriptEnabled(true);


                    //------------------------------------------------------------
                    swipe.setRefreshing(true);
                    webView.setWebViewClient(new WebViewClient() {
                        @Override
                        public void onPageFinished(WebView view, String url) {
                            swipe.setRefreshing(false);
                        }});

                    // download json
                    webView.loadUrl(home_url,sernd_headers);
                    swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            webView.loadUrl(webView.getUrl(),sernd_headers);
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
                                    Thread.sleep(5000);
                                    mHandler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (pay_menu.getTitle().toString().equals(""))
                                            {
                                                pay_menu.setTitle(pay_title);
                                                home_menu.setTitle(home_title);
                                                trip_menu.setTitle(trip_title);
                                                delneveshte_menu.setTitle(delneveshte_title);
                                                setting_menu.setTitle(setting_title);
                                            }
                                        }
                                    });
                                } catch (Exception e) {
                                }
                            }
                        }
                    }).start();

                    bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.item_pay:
                                    webView.loadUrl(pay_url, sernd_headers);
                                    swipe.setRefreshing(true);
                                    webView.setWebViewClient(new WebViewClient() {
                                        @Override
                                        public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                            HashMap<String, String> headerMap = new HashMap<>();
                                            //put all headers in this header map
                                            headerMap.put("x-app-request", "android");
                                            view.loadUrl(url, headerMap);
                                            return true;
                                        }
                                        @Override
                                        public void onPageFinished(WebView view, String url) {
                                            swipe.setRefreshing(false);
                                        }});
                                    break;

                                case R.id.item_home:
                                    webView.loadUrl(home_url, sernd_headers);
                                    swipe.setRefreshing(true);
                                    webView.setWebViewClient(new WebViewClient() {
                                        @Override
                                        public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                            HashMap<String, String> headerMap = new HashMap<>();
                                            //put all headers in this header map
                                            headerMap.put("x-app-request", "android");
                                            view.loadUrl(url, headerMap);
                                            return true;
                                        }
                                        @Override
                                        public void onPageFinished(WebView view, String url) {
                                            swipe.setRefreshing(false);
                                        }});
                                    break;

                                case R.id.item_trip:
                                    webView.loadUrl(trip_url, sernd_headers);
                                    swipe.setRefreshing(true);
                                    Toast.makeText(MainActivity.this, trip_url, Toast.LENGTH_SHORT).show();
                                    webView.setWebViewClient(new WebViewClient() {
                                        @Override
                                        public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                            HashMap<String, String> headerMap = new HashMap<>();
                                            //put all headers in this header map
                                            headerMap.put("x-app-request", "android");
                                            view.loadUrl(url, headerMap);
                                            return true;
                                        }
                                        @Override
                                        public void onPageFinished(WebView view, String url) {
                                            swipe.setRefreshing(false);
                                        }});
                                    break;

                                case R.id.item_delneveshte:
                                    webView.loadUrl(delneveshte_url, sernd_headers);
                                    swipe.setRefreshing(true);
                                    webView.setWebViewClient(new WebViewClient() {
                                        @Override
                                        public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                            HashMap<String, String> headerMap = new HashMap<>();
                                            //put all headers in this header map
                                            headerMap.put("x-app-request", "android");
                                            view.loadUrl(url, headerMap);
                                            return true;
                                        }
                                        @Override
                                        public void onPageFinished(WebView view, String url) {
                                            swipe.setRefreshing(false);
                                        }});
                                    break;

                                case R.id.item_setting:
                                    Intent goTo_setting = new Intent(MainActivity.this, Setting.class);
                                    goTo_setting.putExtra("payTitle" , pay_title);
                                    goTo_setting.putExtra("homeTitle" , home_title);
                                    goTo_setting.putExtra("tripTitle" , trip_title);
                                    goTo_setting.putExtra("delneveshteTitle" , delneveshte_title);
                                    goTo_setting.putExtra("settingTitle" , setting_title);
                                    startActivity(goTo_setting);
                                    break;
                            }
                            return true;
                        }
                    });
                    // set in setting
                    if (getIntent().getBooleanExtra("pay", false)) {
                        webView.loadUrl(pay_url, sernd_headers);
                        swipe.setRefreshing(true);
                        webView.setWebViewClient(new WebViewClient() {
                            @Override
                            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                HashMap<String, String> headerMap = new HashMap<>();
                                //put all headers in this header map
                                headerMap.put("x-app-request", "android");
                                view.loadUrl(url, headerMap);
                                return true;
                            }
                            @Override
                            public void onPageFinished(WebView view, String url) {
                                swipe.setRefreshing(false);
                            }});
                    }
                    if (getIntent().getBooleanExtra("home", false)) {
                        webView.loadUrl(home_url, sernd_headers);
                        swipe.setRefreshing(true);
                        webView.setWebViewClient(new WebViewClient() {
                            @Override
                            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                HashMap<String, String> headerMap = new HashMap<>();
                                //put all headers in this header map
                                headerMap.put("x-app-request", "android");
                                view.loadUrl(url, headerMap);
                                return true;
                            }
                            @Override
                            public void onPageFinished(WebView view, String url) {
                                swipe.setRefreshing(false);
                            }});
                    }
                    if (getIntent().getBooleanExtra("trip", false)) {
                        webView.loadUrl(trip_url, sernd_headers);
                        Toast.makeText(MainActivity.this, trip_url, Toast.LENGTH_SHORT).show();
                        swipe.setRefreshing(true);
                        webView.setWebViewClient(new WebViewClient() {
                            @Override
                            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                HashMap<String, String> headerMap = new HashMap<>();
                                //put all headers in this header map
                                headerMap.put("x-app-request", "android");
                                view.loadUrl(url, headerMap);
                                return true;
                            }
                            @Override
                            public void onPageFinished(WebView view, String url) {
                                swipe.setRefreshing(false);
                            }});
                    }
                    if (getIntent().getBooleanExtra("hert", false)) {
                        webView.loadUrl(delneveshte_url, sernd_headers);
                        swipe.setRefreshing(true);
                        webView.setWebViewClient(new WebViewClient() {
                            @Override
                            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                HashMap<String, String> headerMap = new HashMap<>();
                                //put all headers in this header map
                                headerMap.put("x-app-request", "android");
                                view.loadUrl(url, headerMap);
                                return true;
                            }
                            @Override
                            public void onPageFinished(WebView view, String url) {
                                swipe.setRefreshing(false);
                            }});
                    }
                    // new version for app
                    JSONObject new_version = response.getJSONObject("app_version");
                    int nv_code = new_version.getInt("code");
                    String nv_title = new_version.getString("title");
                    String nv_des = new_version.getString("content_text");
                    Boolean nv_caselable = new_version.getBoolean("auto_hide");
                    if (versionCode < nv_code) {
                        Notification.Builder nb = new Notification.Builder(MainActivity.this);
                        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                        nb.setContentTitle( nv_title )
                                .setContentText( nv_des )
                                .setSmallIcon(android.R.drawable.stat_sys_download)
                                .setAutoCancel( nv_caselable )
                                .setSound(alarmSound);

                        Notification notifs = nb.build();
                        NotificationManager notifManager = (NotificationManager) MainActivity.this.getSystemService(Context.NOTIFICATION_SERVICE);
                        notifManager.notify(9, notifs);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        AppContoroler.getInstance().addToRequestQueue(req);
        // END JSON

    }

    @Override
    protected void onStop() {
        super.onStop();
        startService(new Intent(MainActivity.this, NotificationService.class));
    }

    /**
     * Post Smile
     */
    public void post_smile(){
        // import SharedPreferences
        final SharedPreferences shared = getSharedPreferences("Prefs", MODE_PRIVATE);
        final SharedPreferences.Editor editor = shared.edit();
        // import manual
        final String myTokengName = shared.getString("myTokengName", "no-tooken");
        final String myTokengName_code = shared.getString("myTokengName_code", "no-tooken");

        final Boolean farsi = shared.getBoolean("farsi", false);
        final Boolean arabic = shared.getBoolean("arabic", false);
        final Boolean english = shared.getBoolean("english", false);

        // set lang
        String url_post = "";
        if (farsi){
            url_post = "https://khadije.com/api/v5/smile";
        }
        if (arabic){
            url_post = "https://khadije.com/ar/api/v5/smile";
        }
        if (english){
            url_post = "https://khadije.com/en/api/v5/smile";
        }

        // Post Method
        StringRequest post_id = new StringRequest(Request.Method.POST, url_post, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject getRespone = new JSONObject(response);

                    Boolean newNotif = getRespone.getBoolean("notif_new");

                    if (newNotif){
                        post_notif();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                continue_or_stop_two = false ;
                finish();
                startActivity(new Intent(MainActivity.this,errornet.class));
            }
        })
                // Send Headers
        {
            @Override
            public Map<String, String> getHeaders()  {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("x-app-request", "android");
                headers.put("authorization", "$2y$07$J5lyhNSfVCEVxPZvEmrXhemZpzwekNKJRPHC1kwth3yPw6U6cUBPC");
                return headers;
            }
            // Send Device info
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> posting = new HashMap<>();
                posting.put("user_token", myTokengName );
                posting.put("user_code", myTokengName_code );

                return posting;
            }
        };AppContoroler.getInstance().addToRequestQueue(post_id);
    }


    /**
     * Post Notif
     */
    public void post_notif(){
        // import SharedPreferences
        final SharedPreferences shared = getSharedPreferences("Prefs", MODE_PRIVATE);
        final SharedPreferences.Editor editor = shared.edit();
        // import manual
        final String myTokengName = shared.getString("myTokengName", "no-tooken");
        final String myTokengName_code = shared.getString("myTokengName_code", "no-tooken");

        final Boolean farsi = shared.getBoolean("farsi", false);
        final Boolean arabic = shared.getBoolean("arabic", false);
        final Boolean english = shared.getBoolean("english", false);

        // set lang
        String url_post = "";
        if (farsi){
            url_post = "https://khadije.com/api/v5/notif";
        }
        if (arabic){
            url_post = "https://khadije.com/ar/api/v5/notif";
        }
        if (english){
            url_post = "https://khadije.com/en/api/v5/notif";
        }


        // Post Method
        StringRequest post_id = new StringRequest(Request.Method.POST, url_post, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                try {

                    JSONArray getRespone = new JSONArray(response);

                    int Len_notif = 1;

                    if (getRespone.length() <4){
                        switch (getRespone.length()){
                            case 1:
                                Len_notif = 1;
                                break;
                            case 2:
                                Len_notif = 2;
                                break;
                            case 3:
                                Len_notif = 3;
                                break;
                        }
                    }
                    if (getRespone.length() >4){
                        Len_notif = 3;
                    }


                    for (int respone = 0 ; respone < Len_notif ; respone++){
                        JSONObject one = getRespone.getJSONObject(respone);
                        String notif_title = one.getString("title");
                        String notif_des = one.getString("cat");
                        // Notif
                        Notification.Builder nb = new Notification.Builder(MainActivity.this);
                        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALL);
                        nb.setContentTitle( notif_title )
                                .setContentText( notif_des )
                                .setSmallIcon(android.R.drawable.ic_dialog_email)
                                .setSound(alarmSound);
                        Notification notifs = nb.build();
                        NotificationManager notifManager = (NotificationManager) MainActivity.this.getSystemService(Context.NOTIFICATION_SERVICE);
                        notifManager.notify(10 + respone + 9, notifs);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        })
                // Send Headers
        {
            @Override
            public Map<String, String> getHeaders()  {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("x-app-request", "android");
                headers.put("authorization", "$2y$07$J5lyhNSfVCEVxPZvEmrXhemZpzwekNKJRPHC1kwth3yPw6U6cUBPC");
                return headers;
            }
            // Send Device info
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> posting = new HashMap<>();
                posting.put("user_token", myTokengName );
                posting.put("user_code", myTokengName_code );

                return posting;
            }
        };AppContoroler.getInstance().addToRequestQueue(post_id);

    }

    /**
     * Double Back For Exit
     */
    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        final SharedPreferences shareds = getSharedPreferences("Prefs", MODE_PRIVATE);
        final Boolean farsi = shareds.getBoolean("farsi", false);
        final Boolean arabic = shareds.getBoolean("arabic", false);
        final Boolean english = shareds.getBoolean("english", false);
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        if (farsi || arabic){
            Toast.makeText(this, "برای خروج مجددا کلید برگشت را لمس کنید", Toast.LENGTH_SHORT).show();
        }
        if (english){
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
        }
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    /**
     * Net Checking
     */
    public class NetCheck extends AsyncTask<String,String,Boolean>
    {
        @Override
        protected void onPreExecute(){
        }
        /**
         * Gets current device state and checks for working internet connection by trying Google.
         **/
        @Override
        protected Boolean doInBackground(String... args){
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                try {
                    URL url = new URL("http://www.google.com");
                    HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                    urlc.setConnectTimeout(3000);
                    urlc.connect();
                    if (urlc.getResponseCode() == 200) {
                        return true;
                    }
                } catch (MalformedURLException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            return false;
        }
        @Override
        protected void onPostExecute(Boolean th){
            if(th == false){
                finish();
                startActivity(new Intent(MainActivity.this,errornet.class));
            }
            else{

            }
        }
    }


}
