package com.ermile.khadije;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
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
    //nav button
    BottomNavigationViewEx bottomNav;
    //nav Menu
    DrawerLayout drawerLayout;
    NavigationView navigation_menu;


    /**
     * On Create
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // URL for <JSON MAIN>
        String url = "";
        // import SharedPreferences > <Prefs.java>
        final SharedPreferences shared = getSharedPreferences("Prefs", MODE_PRIVATE);
        final SharedPreferences.Editor editor = shared.edit();
        // import Method for lang > <Prefs.java>
        final Boolean farsi = shared.getBoolean("farsi", false);
        final Boolean arabic = shared.getBoolean("arabic", false);
        final Boolean english = shared.getBoolean("english", false);

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

        //Sync id > Setting Menu
        drawerLayout = findViewById(R.id.drawer_layout);
        navigation_menu = findViewById(R.id.navigation_view);
        // get Header
        View header_navmenu=navigation_menu.getHeaderView(0);
        final TextView header_title = header_navmenu.findViewById(R.id.header_title);
        final TextView header_desc = header_navmenu.findViewById(R.id.header_desc);
        final TextView ver_hed = header_navmenu.findViewById(R.id.virsioin_hed);
        final TextView change_lang = header_navmenu.findViewById(R.id.btn_change_lang);
        final TextView close_lang = header_navmenu.findViewById(R.id.btn_close_lang);
        final LinearLayout language =header_navmenu.findViewById(R.id.set_language);
        Button btn_en = header_navmenu.findViewById(R.id.header_english);
        Button btn_ar = header_navmenu.findViewById(R.id.header_arabic);
        Button btn_fa = header_navmenu.findViewById(R.id.header_farsi);
        // set Header
        change_lang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                change_lang.setVisibility(View.GONE);
                close_lang.setVisibility(View.VISIBLE);
                language.setVisibility(View.VISIBLE);
                language.animate().setDuration(400).alpha(1);
            }
        });
        close_lang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                language.animate().setDuration(400).alpha(0);
                language.setVisibility(View.GONE);
                language.setAlpha(0);
                close_lang.setVisibility(View.GONE);
                change_lang.setVisibility(View.VISIBLE);
            }
        });
        btn_en.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!english){
                    editor.putBoolean("english",true);
                    editor.putBoolean("farsi",false);
                    editor.putBoolean("arabic",false);
                    editor.apply();
                    Toast.makeText(MainActivity.this, "English language was chosen!", Toast.LENGTH_SHORT).show();
                    Change_lang();
                }else {Toast.makeText(MainActivity.this, "language is English !", Toast.LENGTH_SHORT).show();}
            }
        });

        btn_ar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!arabic){
                    editor.putBoolean("arabic",true);
                    editor.putBoolean("farsi",false);
                    editor.putBoolean("english",false);
                    editor.apply();
                    Toast.makeText(MainActivity.this, "تم اختيار اللغة العربية!", Toast.LENGTH_SHORT).show();
                    Change_lang();
                }else {Toast.makeText(MainActivity.this, "اللغة العربية!", Toast.LENGTH_SHORT).show();}
            }
        });

        btn_fa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!farsi){
                    editor.putBoolean("farsi",true);
                    editor.putBoolean("arabic",false);
                    editor.putBoolean("english",false);
                    editor.apply();
                    Toast.makeText(MainActivity.this, "زبان فارسی انتخاب شد!", Toast.LENGTH_SHORT).show();
                    Change_lang();
                }else {Toast.makeText(MainActivity.this, "زبان فارسی شده!", Toast.LENGTH_SHORT).show();}
            }
        });
        // Get Menu from Setting Menu
        Menu menu_navmenu = navigation_menu.getMenu();
        final MenuItem about_us = menu_navmenu.findItem(R.id.about_us);
        final MenuItem call_us = menu_navmenu.findItem(R.id.call_us);
        final MenuItem future_view = menu_navmenu.findItem(R.id.future_view);
        final MenuItem mission = menu_navmenu.findItem(R.id.mission);
        final MenuItem website = menu_navmenu.findItem(R.id.website);



        // get title in setting
        final String titlehome = getIntent().getStringExtra("homeTitle");
        final String titledelneveshte = getIntent().getStringExtra("delneveshteTitle");
        final String titlesetting = getIntent().getStringExtra("settingTitle");

        // Sync id in <xml> to <java>
        final SwipeRefreshLayout swipe = findViewById(R.id.swipref);
        bottomNav = findViewById(R.id.bottom_navigation);
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

        if (farsi){
            btn_fa.setSelected(true);
            btn_fa.setTextColor(Color.parseColor("#ffffff"));
            url = "https://khadije.com/api/v5/android";
            ViewCompat.setLayoutDirection(drawerLayout,ViewCompat.LAYOUT_DIRECTION_RTL);

        }
        if (arabic){
            btn_ar.setSelected(true);
            btn_ar.setTextColor(Color.parseColor("#ffffff"));
            url = "https://khadije.com/ar/api/v5/android";
            ViewCompat.setLayoutDirection(drawerLayout,ViewCompat.LAYOUT_DIRECTION_RTL);
        }
        if (english){
            btn_en.setSelected(true);
            btn_en.setTextColor(Color.parseColor("#ffffff"));
            url = "https://khadije.com/en/api/v5/android";
            ViewCompat.setLayoutDirection(drawerLayout,ViewCompat.LAYOUT_DIRECTION_LTR);
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

                    // set Title Header
                    String json_title_header = response.getString("name");
                    String json_desc_header = response.getString("desc");
                    header_title.setText(json_title_header);
                    header_desc.setText(json_desc_header);

                    JSONObject trans_header = response.getJSONObject("transalate");
                    String json_chaneg_lang = trans_header.getString("changelang");
                    String json_close_lang = trans_header.getString("close");
                    String json_ver_hed = trans_header.getString("version");
                    change_lang.setText(json_chaneg_lang);
                    close_lang.setText(json_close_lang);
                    ver_hed.setText(json_ver_hed + " " + versionName);


                    // get Param for <bottom nav>
                    final JSONArray navigation_btn = response.getJSONArray("navigation");
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

                    // get Param for <menu nav>
                    JSONArray android_menu = response.getJSONArray("android_menu");
                    JSONObject json_about_us = android_menu.getJSONObject(0);
                    JSONObject json_call_us = android_menu.getJSONObject(1);
                    JSONObject json_future_view = android_menu.getJSONObject(2);
                    JSONObject json_mission = android_menu.getJSONObject(3);
                    JSONObject json_website = android_menu.getJSONObject(4);
                    // Get Url item <about_us>
                    final String aboutus_title = json_about_us.getString("title");
                    final String aboutus_url = json_about_us.getString("url");
                    // Get Url item <call_us>
                    final String callus_title = json_call_us.getString("title");
                    final String callus_url = json_call_us.getString("url");
                    // Get Url item <future_view>
                    final String futureview_title = json_future_view.getString("title");
                    final String futureview_url = json_future_view.getString("url");
                    // Get Url item <mission>
                    final String mission_title = json_mission.getString("title");
                    final String mission_url = json_mission.getString("url");
                    // Get Url item <website>
                    final String website_title = json_website.getString("title");
                    final String website_url = json_website.getString("url");

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
                                            if (home_menu.getTitle().toString().equals("") || call_us.getTitle().toString().equals(""))
                                            {
                                                //set Bottom nav title
                                                home_menu.setTitle(home_title);
                                                delneveshte_menu.setTitle(hert_title);
                                                setting_menu.setTitle(setting_title);
                                                // set menu nav title
                                                about_us.setTitle(aboutus_title);
                                                call_us.setTitle(callus_title);
                                                future_view.setTitle(futureview_title);
                                                mission.setTitle(mission_title);
                                                website.setTitle(website_title);
                                            }
                                            // set out nav menu
                                            if (!drawerLayout.isDrawerOpen(GravityCompat.START ) && bottomNav.getSelectedItemId() == R.id.item_setting)
                                            {
                                                bottomNav.setSelectedItemId(R.id.item_home);
                                                close_lang.setVisibility(View.GONE);
                                                change_lang.setVisibility(View.VISIBLE);
                                                language.setVisibility(View.GONE);
                                                language.setAlpha(0);
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

                            if (!url.equals(home_url)){
                                if(url.startsWith("https://khadije.com/pay/") ||url.startsWith("https://khadije.com/ar/pay/") || url.startsWith("https://khadije.com/en/pay/")){
                                    // Handle the tel: link
                                    Intent browser = new Intent ( Intent.ACTION_VIEW );
                                    browser.setData ( Uri.parse ( url ) );
                                    startActivity ( browser );
                                    bottomNav.setSelectedItemId(R.id.item_home);

                                    // Return true means, leave the current web view and handle the url itself
                                    return true;
                                }
                                if (url.equals(hert_url)){
                                    bottomNav.getMenu().findItem(R.id.item_delneveshte).setCheckable(true);
                                    return true;
                                }else {
                                    bottomNav.getMenu().findItem(R.id.item_home).setCheckable(false);
                                    return true;
                                }
                            }
                            return false;
                        }
                        @Override
                        public void onPageFinished(WebView view, String url) {
                            swipe.setRefreshing(false);
                        }});

                    // set for On Click bottom
                    bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(@NonNull final MenuItem item) {
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

                                            if (!url.equals(home_url)){
                                                if(url.startsWith("https://khadije.com/pay/") ||url.startsWith("https://khadije.com/ar/pay/") || url.startsWith("https://khadije.com/en/pay/")){
                                                    // Handle the tel: link
                                                    Intent browser = new Intent ( Intent.ACTION_VIEW );
                                                    browser.setData ( Uri.parse ( url ) );
                                                    startActivity ( browser );
                                                    bottomNav.setSelectedItemId(R.id.item_home);

                                                    // Return true means, leave the current web view and handle the url itself
                                                    return true;
                                                }
                                                if (url.equals(hert_url)){
                                                    bottomNav.getMenu().findItem(R.id.item_delneveshte).setCheckable(true);
                                                    return true;
                                                }else {
                                                    bottomNav.getMenu().findItem(R.id.item_home).setCheckable(false);
                                                    return true;
                                                }
                                            }

                                            return false;

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

                                            if (!url.equals(hert_url)){
                                                if(url.startsWith("https://khadije.com/pay/") ||url.startsWith("https://khadije.com/ar/pay/") || url.startsWith("https://khadije.com/en/pay/")){
                                                    // Handle the tel: link
                                                    Intent browser = new Intent ( Intent.ACTION_VIEW );
                                                    browser.setData ( Uri.parse ( url ) );
                                                    startActivity ( browser );
                                                    bottomNav.setSelectedItemId(R.id.item_home);

                                                    // Return true means, leave the current web view and handle the url itself
                                                    return true;
                                                }
                                                if (url.equals(hert_url)){
                                                    bottomNav.getMenu().findItem(R.id.item_delneveshte).setCheckable(true);
                                                    return true;
                                                }else {
                                                    bottomNav.getMenu().findItem(R.id.item_delneveshte).setCheckable(false);
                                                    return true;
                                                }
                                            }

                                            return false;
                                        }
                                        @Override
                                        public void onPageFinished(WebView view, String url) {
                                            swipe.setRefreshing(false);
                                        }});
                                    break;

                                case R.id.item_setting:
                                    drawerLayout.openDrawer(navigation_menu);
                                    break;
                            }
                            return true;
                        }
                    });

                    navigation_menu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                            int menuId = item.getItemId();
                            switch (menuId) {
                                case R.id.about_us:
                                    // put url to <about_us.java>
                                    Intent sendURL_about = new Intent(MainActivity.this, about_us.class);
                                    sendURL_about.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    sendURL_about.putExtra("about_bol", true);
                                    sendURL_about.putExtra("about_url" , aboutus_url);
                                    startActivity(sendURL_about);
                                    break;
                                case R.id.call_us:
                                    // put url to <about_us.java>
                                    Intent sendURL_call = new Intent(MainActivity.this, about_us.class);
                                    sendURL_call.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    sendURL_call.putExtra("call_bol", true);
                                    sendURL_call.putExtra("call_url" , callus_url);
                                    startActivity(sendURL_call);
                                    break;
                                case R.id.future_view:
                                    // put url to <about_us.java>
                                    Intent sendURL_future = new Intent(MainActivity.this, about_us.class);
                                    sendURL_future.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    sendURL_future.putExtra("future_bol", true);
                                    sendURL_future.putExtra("future_url" , futureview_url);
                                    startActivity(sendURL_future);
                                    break;
                                case R.id.mission:
                                    // put url to <about_us.java>
                                    Intent sendURL_mission = new Intent(MainActivity.this, about_us.class);
                                    sendURL_mission.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    sendURL_mission.putExtra("mission_bol", true);
                                    sendURL_mission.putExtra("mission_url" , mission_url);
                                    startActivity(sendURL_mission);
                                    break;
                                case R.id.website:
                                    // Go to Browser
                                    Intent browser_khadije = new Intent ( Intent.ACTION_VIEW );
                                    browser_khadije.setData ( Uri.parse ( website_url ) );
                                    startActivity ( browser_khadije );
                                    break;
                            }
                            drawerLayout.closeDrawer(GravityCompat.START);
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
                continueORstop_checkNotif = false ;
                finish();
                startActivity(new Intent(MainActivity.this,errornet.class));
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

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            bottomNav.setSelectedItemId(R.id.item_home);
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
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
            }, 1500);
        }
    }

    public void Change_lang(){
        startActivity(new Intent(MainActivity.this,changing_lang.class));
        finish();
    }

}
