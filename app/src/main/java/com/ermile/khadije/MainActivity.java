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
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.ermile.khadije.network.AppContoroler;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    int randomNumber = new Random().nextInt(976431 ) + 20;

    Boolean back_inhome = false;
    // get Version for > new version apk
    int versionCode = 0 ;
    String versionName = "";
    PendingIntent onClick_notif, Button_onclick_notif , onclick_notifUpdate_close;
    // Handle check > Notif for user
    Handler mHandler_checkNotif;
    boolean continueORstop_checkNotif;
    //nav button
    BottomNavigationViewEx bottomNav;
    //nav Menu
    DrawerLayout drawerLayout;
    NavigationView navigation_menu;


    int icon_home = R.drawable.ic_home;
    int icon_hert = R.drawable.ic_delneveshte ;
    int icon_setting = R.drawable.ic_seting ;
    int icon_about = R.drawable.ic_abut_us ;
    int icon_contact = R.drawable.ic_call_us ;
    int icon_vision = R.drawable.ic_future_view ;
    int icon_mission = R.drawable.ic_mission ;
    int icon_website = R.drawable.ic_website ;
    int icon_net_setting = R.drawable.ic_setting_net ;
    int icon_chake = R.drawable.ic_chake;
    int icon_close = R.drawable.ic_close;



    /**
     * On Create
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Notif_is();

        // URL for <JSON MAIN>
        String url = "";
        // import SharedPreferences > <Prefs.java>
        final SharedPreferences shared = getSharedPreferences("Prefs", MODE_PRIVATE);
        final SharedPreferences.Editor editor = shared.edit();
        // import Method for lang > <Prefs.java>
        final Boolean farsi = shared.getBoolean("farsi", false);
        final Boolean arabic = shared.getBoolean("arabic", false);
        final Boolean english = shared.getBoolean("english", false);

        final String myTokengName = shared.getString("myTokengName", "no-tooken");
        final String myTokengName_code = shared.getString("myTokengName_code", "no-tooken");

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

        //Sync id >  Menu
        drawerLayout = findViewById(R.id.drawer_layout);
        navigation_menu = findViewById(R.id.navigation_view);
        // get Header
        View header_navmenu = navigation_menu.getHeaderView(0);
        final TextView header_title = header_navmenu.findViewById(R.id.header_title);
        final TextView header_desc = header_navmenu.findViewById(R.id.header_desc);
        final TextView ver_hed = header_navmenu.findViewById(R.id.virsioin_hed);
        Button btn_en = header_navmenu.findViewById(R.id.header_english);
        final Button btn_ar = header_navmenu.findViewById(R.id.header_arabic);
        Button btn_fa = header_navmenu.findViewById(R.id.header_farsi);

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
        // Get Menu from  Menu
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
        // Back load for Title > load from <.java>
        if (getIntent().getBooleanExtra("home_bol", false)) { bottomNav.setSelectedItemId(R.id.item_home); }


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
                    back_inhome = true;
                    swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            webView.loadUrl(webView.getUrl(),sernd_headers);
                        }
                    });
                    webView.setWebViewClient(new WebViewClient() {
                        @Override
                        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error){
                            startActivity(new Intent(getApplicationContext(), errornet.class));
                        }
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
                                    back_inhome =false;

                                    // Return true means, leave the current web view and handle the url itself
                                    return true;
                                }
                                if (url.startsWith(hert_url)) {
                                    bottomNav.setSelectedItemId(R.id.item_delneveshte);
                                    back_inhome =false;
                                } else {
                                    bottomNav.getMenu().findItem(R.id.item_home).setCheckable(false);
                                    back_inhome =false;
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
                                    bottomNav.getMenu().findItem(R.id.item_home).setCheckable(true);
                                    webView.loadUrl(home_url, sernd_headers);
                                    back_inhome = true;
                                    swipe.setRefreshing(true);
                                    webView.setWebViewClient(new WebViewClient() {
                                        @Override
                                        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error){
                                            startActivity(new Intent(getApplicationContext(), errornet.class));
                                        }
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
                                                    back_inhome =false;

                                                    // Return true means, leave the current web view and handle the url itself
                                                    return true;
                                                }
                                                if (url.startsWith(hert_url)) {
                                                    bottomNav.setSelectedItemId(R.id.item_delneveshte);
                                                    back_inhome =false;
                                                } else {
                                                    bottomNav.getMenu().findItem(R.id.item_home).setCheckable(false);
                                                    back_inhome =false;
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
                                    bottomNav.getMenu().findItem(R.id.item_delneveshte).setCheckable(true);
                                    webView.loadUrl(hert_url, sernd_headers);
                                    back_inhome =false;
                                    swipe.setRefreshing(true);
                                    webView.setWebViewClient(new WebViewClient() {
                                        @Override
                                        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error){
                                            startActivity(new Intent(getApplicationContext(), errornet.class));
                                        }
                                        // in refresh send header
                                        @Override
                                        public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                            HashMap<String, String> headerMap = new HashMap<>();
                                            headerMap.put("x-app-request", "android");
                                            view.loadUrl(url, headerMap);

                                            if (!url.equals(hert_url)) {
                                                if (url.startsWith("https://khadije.com/pay/") || url.startsWith("https://khadije.com/ar/pay/") || url.startsWith("https://khadije.com/en/pay/")) {
                                                    // Handle the tel: link
                                                    Intent browser = new Intent(Intent.ACTION_VIEW);
                                                    browser.setData(Uri.parse(url));
                                                    startActivity(browser);
                                                    bottomNav.setSelectedItemId(R.id.item_home);
                                                    back_inhome = false;

                                                    // Return true means, leave the current web view and handle the url itself
                                                    return true;
                                                }
                                                if (url.startsWith(home_url)) {
                                                    bottomNav.setSelectedItemId(R.id.item_home);
                                                    back_inhome = true;
                                                } else {
                                                    bottomNav.getMenu().findItem(R.id.item_delneveshte).setCheckable(false);
                                                    back_inhome = false;
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
                                    sendURL_about
                                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                            .putExtra("about_bol", true)
                                            .putExtra("about_url" , aboutus_url);

                                    startActivity(sendURL_about);
                                    break;
                                case R.id.call_us:
                                    // put url to <about_us.java>
                                    Intent sendURL_call = new Intent(MainActivity.this, about_us.class);
                                    sendURL_call
                                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                            .putExtra("call_bol", true)
                                            .putExtra("call_url" , callus_url);
                                    startActivity(sendURL_call);
                                    break;
                                case R.id.future_view:
                                    // put url to <about_us.java>
                                    Intent sendURL_future = new Intent(MainActivity.this, about_us.class);
                                    sendURL_future
                                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                            .putExtra("future_bol", true)
                                            .putExtra("future_url" , futureview_url);
                                    startActivity(sendURL_future);
                                    break;
                                case R.id.mission:
                                    // put url to <about_us.java>
                                    Intent sendURL_mission = new Intent(MainActivity.this, about_us.class);
                                    sendURL_mission
                                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                            .putExtra("mission_url" , mission_url)
                                            .putExtra("mission_bol", true);

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

                    // get Title from <.java>
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
        StringRequest Notif_is_Request = new StringRequest(Request.Method.POST, "http://mimsg.ir/test.json", new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray get_Notif_is = new JSONArray(response);

                    Intent sendURL_about = new Intent(getApplicationContext() , click_on_notif.class);
                    Intent close_notif = new Intent("com.ermile.khadije.cancel");

                    onClick_notif = null;
                    Button_onclick_notif = null;
                    onclick_notifUpdate_close = PendingIntent.getBroadcast(getApplicationContext(), (int) System.currentTimeMillis(), close_notif, PendingIntent.FLAG_UPDATE_CURRENT);

                    String CHANNEL_ID = "m";
                    final NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext() , CHANNEL_ID);

                    for (int notif_is = 0 ; notif_is <= get_Notif_is.length() ; notif_is++){
                        JSONObject one = get_Notif_is.getJSONObject(notif_is);
                        // get object from json
                        String notif_title = one.getString("title");
                        String notif_txt_small = one.getString("small");
                        String notif_txt_big = one.getString("big");
                        String notif_txt_from = one.getString("from");
                        String notif_group = one.getString("group");
                        String notif_icon = one.getString("small_icon");
                        String notif_large_icon = one.getString("large_icon");
                        String notif_big_image = one.getString("big_image");
                        String notif_on_click = one.getString("on_click");
                        String notif_otherBrowser_link = one.getString("link");
                        Boolean notif_otherBrowser_inApp = one.getBoolean("external");


                        switch (notif_icon){
                            case "home":
                                notif_icon = String.valueOf(icon_home);
                                break;
                            case "hert":
                                notif_icon = String.valueOf(icon_hert);
                                break;
                            case "setting":
                                notif_icon = String.valueOf(icon_setting);
                                break;
                            case "about":
                                notif_icon = String.valueOf(icon_about);
                                break;
                            case "contact":
                                notif_icon = String.valueOf(icon_contact);
                                break;
                            case "vision":
                                notif_icon = String.valueOf(icon_vision);
                                break;
                            case "mission":
                                notif_icon = String.valueOf(icon_mission);
                                break;
                            case "website":
                                notif_icon = String.valueOf(icon_website);
                                break;
                            case "net-setting":
                                notif_icon = String.valueOf(icon_net_setting);
                                break;
                            case "chake":
                                notif_icon = String.valueOf(icon_chake);
                                break;
                            case "close":
                                notif_icon = String.valueOf(icon_close);
                                break;
                        }


                        switch (notif_on_click){
                            case "home":
                                onClick_notif = PendingIntent.getActivity(getApplicationContext() , randomNumber , sendURL_about
                                        .putExtra("put_notif","N_Ihome") , 0);
                                break;
                            case "about":
                                onClick_notif = PendingIntent.getActivity(getApplicationContext() , randomNumber , sendURL_about
                                        .putExtra("put_notif","N_about") , 0);
                                break;
                            case "call":
                                onClick_notif = PendingIntent.getActivity(getApplicationContext() , randomNumber , sendURL_about
                                        .putExtra("put_notif","N_call") , 0);
                                break;
                            case "vision":
                                onClick_notif = PendingIntent.getActivity(getApplicationContext() , randomNumber, sendURL_about
                                        .putExtra("put_notif","N_futrue") , 0);
                                break;
                            case "mission":
                                onClick_notif = PendingIntent.getActivity(getApplicationContext() , randomNumber , sendURL_about
                                        .putExtra("put_notif","N_mission") , 0);
                                break;
                            case "website":
                                onClick_notif = PendingIntent.getActivity(getApplicationContext() , randomNumber , sendURL_about
                                        .putExtra("put_notif","website") , 0);
                                break;
                            case "other_website":
                                onClick_notif = PendingIntent.getActivity(getApplicationContext(), randomNumber , sendURL_about
                                        .putExtra("put_notif", "other_website")
                                        .putExtra("url_other_website", notif_otherBrowser_link)
                                        .putExtra("notif_otherBrowser_inApp" , notif_otherBrowser_inApp ), 0);
                                break;
                            case "close":
                                onClick_notif = PendingIntent.getBroadcast(getApplicationContext(), (int)
                                        System.currentTimeMillis(), close_notif, PendingIntent.FLAG_UPDATE_CURRENT);
                                break;
                        }

                        builder.setSmallIcon(Integer.parseInt(notif_icon))
                                .setContentTitle(notif_title)
                                .setContentText(notif_txt_small)
                                .setStyle(new NotificationCompat
                                        .BigTextStyle()
                                        .bigText(notif_txt_big))
                                .setGroup(notif_group)
                                .setContentInfo(notif_txt_from)
                                .setContentIntent(onClick_notif)
                                .setWhen(System.currentTimeMillis())
                                .setAutoCancel(true)
                                .setDefaults(Notification.DEFAULT_ALL)
                                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI);

                        JSONArray notif_button = one.getJSONArray("btn");
                        for(int i = 0; i <= notif_button.length() ; i++) {
                            Toast.makeText(MainActivity.this, "" + notif_button.length() , Toast.LENGTH_SHORT).show();
                            builder.addAction(R.drawable.ic_chake, "admin" , Button_onclick_notif);
                        }

                        Glide.with(getApplicationContext())
                                .asBitmap()
                                .load(notif_large_icon)
                                .into(new SimpleTarget<Bitmap>() {
                                    @Override
                                    public void onResourceReady(Bitmap resource_LargeIcon, Transition<? super Bitmap> transition) {
                                        builder.setLargeIcon(resource_LargeIcon);
                                    }
                                });

                        Glide.with(getApplicationContext())
                                .asBitmap()
                                .load(notif_big_image)
                                .into(new SimpleTarget<Bitmap>() {
                                    @Override
                                    public void onResourceReady(Bitmap resource_bigPicture, Transition<? super Bitmap> transition) {
                                        builder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(Bitmap.createBitmap(resource_bigPicture)));
                                    }
                                });
                        
                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
                        notificationManager.notify(1000 + notif_is , builder.build());
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
//        {
//            @Override
//            public Map<String, String> getHeaders()  {
//                HashMap<String, String> headers_notif_is = new HashMap<>();
//                headers_notif_is.put("x-app-request", "android");
//                headers_notif_is.put("authorization", "$2y$07$J5lyhNSfVCEVxPZvEmrXhemZpzwekNKJRPHC1kwth3yPw6U6cUBPC");
//                return headers_notif_is;
//            }
            // Send body
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> body_notif_is = new HashMap<>();
//                body_notif_is.put("user_token", myTokengName );
//                body_notif_is.put("user_code", myTokengName_code );
//                return body_notif_is;
//            }
//        }
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
        } else if (back_inhome ){
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
        }else {
            bottomNav.setSelectedItemId(R.id.item_home);
        }
    }

    public void Change_lang(){
        startActivity(new Intent(MainActivity.this,changing_lang.class));
        finish();
    }

}
