package com.ermile.khadije;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
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
import com.ermile.khadije.network.AppContoroler;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    // get Version for > new version apk
    int versionCode = 0 ;
    String versionName = "";
    // Handle check > Notif for user
    Handler mHandler_checkNotif;
    boolean continueORstop_checkNotif;

    /**
     * On Create
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get uri form > Browser
        Uri data = getIntent().getData();
        Uri uri = Uri.parse(String.valueOf(data));
        String status = uri.getQueryParameter("status");
        String amount = uri.getQueryParameter("amount");
        Toast.makeText(this, "O: "+status + " Pric: "+ amount, Toast.LENGTH_SHORT).show();

        // Change Version from > build.gradle(Module:app)
        try {
            PackageInfo pInfo = getApplicationContext().getPackageManager().getPackageInfo(getPackageName(), 0);
            versionCode = pInfo.versionCode;
            versionName = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        // check Notif
        post_smile();
        // check post_smile in > 30 sec <
        mHandler_checkNotif = new Handler();
        continueORstop_checkNotif = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (continueORstop_checkNotif) {
                    try {
                        Thread.sleep(30000);
                        mHandler_checkNotif.post(new Runnable() {
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
        final String titlehome = getIntent().getStringExtra("homeTitle");
        final String titledelneveshte = getIntent().getStringExtra("delneveshteTitle");
        final String titlesetting = getIntent().getStringExtra("settingTitle");

        // Sync id in <xml> to <java>
        final SwipeRefreshLayout swipe = findViewById(R.id.swipref);
        final BottomNavigationViewEx bottomNav = findViewById(R.id.bottom_navigation);
            bottomNav.setSelectedItemId(R.id.item_home);
            bottomNav.enableAnimation(false);
            bottomNav.enableShiftingMode(false);
            bottomNav.enableItemShiftingMode(false);
            bottomNav.setTextSize(10f);
            bottomNav.setIconSize(28,28);
        final WebView webView = findViewById(R.id.webview);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // Sync Menu to <java>
        Menu menu = bottomNav.getMenu();
        final MenuItem home_menu = menu.findItem(R.id.item_home);
        final MenuItem delneveshte_menu = menu.findItem(R.id.item_delneveshte);
        final MenuItem setting_menu = menu.findItem(R.id.item_setting);

        // Back load for Title > load from <splash.java>
        if (getIntent().getBooleanExtra("welcome_title", false)) {
            home_menu.setTitle(titlehome);
            delneveshte_menu.setTitle(titledelneveshte);
            setting_menu.setTitle(titlesetting);
        }
        // Back load for Title > load from <Setting.java>
        if (getIntent().getBooleanExtra("home", false)) {
            bottomNav.setSelectedItemId(R.id.item_home);
            home_menu.setTitle(titlehome);
            delneveshte_menu.setTitle(titledelneveshte);
            setting_menu.setTitle(titlesetting);
        }
        if (getIntent().getBooleanExtra("hert", false)) {
            bottomNav.setSelectedItemId(R.id.item_delneveshte);
            home_menu.setTitle(titlehome);
            delneveshte_menu.setTitle(titledelneveshte);
            setting_menu.setTitle(titlesetting);
        }


        // import SharedPreferences > <Prefs.java>
        final SharedPreferences shared = getSharedPreferences("Prefs", MODE_PRIVATE);
        final SharedPreferences.Editor editor = shared.edit();
        // import Method for lang > <Prefs.java>
        final Boolean farsi = shared.getBoolean("farsi", false);
        final Boolean arabic = shared.getBoolean("arabic", false);
        final Boolean english = shared.getBoolean("english", false);
        // set lang for load URL JSON
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

        // JSON Request
        JsonObjectRequest Json_MainActivityGET = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    // Handler for get title bottom & notif update
                    final Handler mHandler_jsonMain;
                    final boolean continueORstop_jsonMain;
                    // send Header
                    final Map<String, String> sernd_headers = new HashMap<String, String>();
                    sernd_headers.put("x-app-request", "android");

                    // get Param for bottom
                    JSONArray navigation_btn = response.getJSONArray("navigation");
                    JSONObject home = navigation_btn.getJSONObject(0);
                    JSONObject hert = navigation_btn.getJSONObject(1);
                    JSONObject setting = navigation_btn.getJSONObject(2);
                    // Get Url item <HOME>
                    final String home_title = home.getString("title");
                    final String home_url = home.getString("url");
                    // Get Url item <HERT>
                    final String hert_title = hert.getString("title");
                    final String hert_url = hert.getString("url");
                    // Get Url item <SETTING>
                    final String setting_title = setting.getString("title");
                    final String setting_url = setting.getString("url");

                    // Check for Title not Empty in > 300 mil <
                    mHandler_jsonMain = new Handler();
                    continueORstop_jsonMain = true;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (continueORstop_jsonMain) {
                                try {
                                    Thread.sleep(300);
                                    mHandler_jsonMain.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            // if home is empty load title again
                                            if (home_menu.getTitle().toString().equals(""))
                                            {
                                                home_menu.setTitle(home_title);
                                                delneveshte_menu.setTitle(hert_title);
                                                setting_menu.setTitle(setting_title);
                                            }
                                        }
                                    });
                                } catch (Exception e) {
                                }
                            }
                        }
                    }).start();

                    // load in Start
                    swipe.setRefreshing(true);
                    webView.loadUrl(home_url,sernd_headers);
                    swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            webView.loadUrl(webView.getUrl(),sernd_headers);
                        }
                    });
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

                    // set for On Click bottom
                    bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.item_home:
                                    webView.loadUrl(home_url, sernd_headers);
                                    swipe.setRefreshing(true);
                                    webView.setWebViewClient(new WebViewClient() {
                                        // in refresh send header
                                        @Override
                                        public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                            HashMap<String, String> headerMap = new HashMap<>();
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
                                    webView.loadUrl(hert_url, sernd_headers);
                                    swipe.setRefreshing(true);
                                    webView.setWebViewClient(new WebViewClient() {
                                        // in refresh send header
                                        @Override
                                        public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                            HashMap<String, String> headerMap = new HashMap<>();
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
                                    // put title to <Setting.java>
                                    Intent sendTitle_setting = new Intent(MainActivity.this, Setting.class);
                                    sendTitle_setting.putExtra("homeTitle" , home_title);
                                    sendTitle_setting.putExtra("delneveshteTitle" , hert_title);
                                    sendTitle_setting.putExtra("settingTitle" , setting_title);
                                    startActivity(sendTitle_setting);
                                    break;
                            }
                            return true;
                        }
                    });

                    // get Title from <Setting.java>
                    if (getIntent().getBooleanExtra("home", false)) {
                        webView.loadUrl(home_url, sernd_headers);
                        swipe.setRefreshing(true);
                        webView.setWebViewClient(new WebViewClient() {
                            @Override
                            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                HashMap<String, String> headerMap = new HashMap<>();
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
                        webView.loadUrl(hert_url, sernd_headers);
                        swipe.setRefreshing(true);
                        webView.setWebViewClient(new WebViewClient() {
                            @Override
                            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                HashMap<String, String> headerMap = new HashMap<>();
                                headerMap.put("x-app-request", "android");
                                view.loadUrl(url, headerMap);
                                return true;
                            }
                            @Override
                            public void onPageFinished(WebView view, String url) {
                                swipe.setRefreshing(false);
                            }});
                    }

                    // Notification for New version APK
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
                        Notification notif_update = nb.build();
                        NotificationManager notifManager_update = (NotificationManager) MainActivity.this.getSystemService(Context.NOTIFICATION_SERVICE);
                        notifManager_update.notify(0, notif_update);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // in Error Net
            }
        });
        AppContoroler.getInstance().addToRequestQueue(Json_MainActivityGET);
    }

    // if Finish App > get Notification
    @Override
    protected void onStop() {
        super.onStop();
        startService(new Intent(MainActivity.this, NotificationService.class));
    }

    // Notification send header and user Token > new Notif for me?
    public void post_smile(){
        // import SharedPreferences
        final SharedPreferences sharedPerf_smile = getSharedPreferences("Prefs", MODE_PRIVATE);
        // import manual Token & Code & lang
        final String myTokengName = sharedPerf_smile.getString("myTokengName", "no-tooken");
        final String myTokengName_code = sharedPerf_smile.getString("myTokengName_code", "no-tooken");
        final Boolean farsi = sharedPerf_smile.getBoolean("farsi", false);
        final Boolean arabic = sharedPerf_smile.getBoolean("arabic", false);
        final Boolean english = sharedPerf_smile.getBoolean("english", false);

        // set lang
        String url_postSmile = "";
        if (farsi){
            url_postSmile = "https://khadije.com/api/v5/smile";
        }
        if (arabic){
            url_postSmile = "https://khadije.com/ar/api/v5/smile";
        }
        if (english){
            url_postSmile = "https://khadije.com/en/api/v5/smile";
        }

        // Json <Post Smile> Method
        StringRequest PostSmile_Request = new StringRequest(Request.Method.POST, url_postSmile, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject get_postRequest = new JSONObject(response);

                    // Check New Notif
                    Boolean newNotif = get_postRequest.getBoolean("notif_new");
                    if (newNotif){
                        Notif_is();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                continueORstop_checkNotif = false ;
                finish();
                startActivity(new Intent(MainActivity.this,errornet.class));
            }
        })
         // Send Header
        {
            @Override
            public Map<String, String> getHeaders()  {
                HashMap<String, String> headers_postSmile = new HashMap<>();
                headers_postSmile.put("x-app-request", "android");
                headers_postSmile.put("authorization", "$2y$07$J5lyhNSfVCEVxPZvEmrXhemZpzwekNKJRPHC1kwth3yPw6U6cUBPC");
                return headers_postSmile;
            }
            // Send Body Token & Code
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> body_postSmile = new HashMap<>();
                body_postSmile.put("user_token", myTokengName );
                body_postSmile.put("user_code", myTokengName_code );
                return body_postSmile;
            }
        }
        ;AppContoroler.getInstance().addToRequestQueue(PostSmile_Request);
    }

    // get Notification and run for user > Yes Notif is ..
    public void Notif_is(){
        // import SharedPreferences
        final SharedPreferences shared_notif_is = getSharedPreferences("Prefs", MODE_PRIVATE);
        // import manual Token & code & lang
        final String myTokengName = shared_notif_is.getString("myTokengName", "no-tooken");
        final String myTokengName_code = shared_notif_is.getString("myTokengName_code", "no-tooken");
        final Boolean farsi = shared_notif_is.getBoolean("farsi", false);
        final Boolean arabic = shared_notif_is.getBoolean("arabic", false);
        final Boolean english = shared_notif_is.getBoolean("english", false);

        // set lang for get notif
        String url_notif_is = "";
        if (farsi){
            url_notif_is = "https://khadije.com/api/v5/notif";
        }
        if (arabic){
            url_notif_is = "https://khadije.com/ar/api/v5/notif";
        }
        if (english){
            url_notif_is = "https://khadije.com/en/api/v5/notif";
        }
        // Post Method
        StringRequest Notif_is_Request = new StringRequest(Request.Method.POST, url_notif_is, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray get_Notif_is = new JSONArray(response);
                    // Notif Cunt
                    int notif_length = 1;
                    if (get_Notif_is.length() < 4){
                        switch (get_Notif_is.length()){
                            case 1:
                                notif_length = 1;
                                break;
                            case 2:
                                notif_length = 2;
                                break;
                            case 3:
                                notif_length = 3;
                                break;
                        }
                    }
                    if (get_Notif_is.length() > 4){
                        notif_length = 3;
                    }

                    // Your Notif is
                    for (int notif_is = 0 ; notif_is < notif_length ; notif_is++){
                        JSONObject one = get_Notif_is.getJSONObject(notif_is);
                        // get object from json
                        String notif_title = one.getString("title");
                        String notif_des = one.getString("cat");
                        // set Object for Notif
                        Notification.Builder nb = new Notification.Builder(MainActivity.this);
                        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALL);
                        nb.setContentTitle( notif_title )
                                .setContentText( notif_des )
                                .setSmallIcon(android.R.drawable.ic_dialog_email)
                                .setSound(alarmSound);
                        Notification notifs = nb.build();
                        NotificationManager notifManager = (NotificationManager) MainActivity.this.getSystemService(Context.NOTIFICATION_SERVICE);
                        notifManager.notify(10 + notif_is + 9, notifs);
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
                HashMap<String, String> headers_notif_is = new HashMap<>();
                headers_notif_is.put("x-app-request", "android");
                headers_notif_is.put("authorization", "$2y$07$J5lyhNSfVCEVxPZvEmrXhemZpzwekNKJRPHC1kwth3yPw6U6cUBPC");
                return headers_notif_is;
            }
            // Send body
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> body_notif_is = new HashMap<>();
                body_notif_is.put("user_token", myTokengName );
                body_notif_is.put("user_code", myTokengName_code );
                return body_notif_is;
            }
        }
        ;AppContoroler.getInstance().addToRequestQueue(Notif_is_Request);

    }

    // Double back for Exit
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

        if (farsi){
            Toast.makeText(this, "برای خروج مجددا کلید برگشت را لمس کنید", Toast.LENGTH_SHORT).show();
        }
        if (arabic){
            Toast.makeText(this, "للخروج المس زر الرجوع مرة أخرى", Toast.LENGTH_SHORT).show();
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

}
