package com.ermile.khadije;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;

public class splash extends AppCompatActivity {

    Boolean ok_getToken = false;
    String versionName = null ;
    int versionCode = 0 ;
    String url_json = "https://khadije.com/en/api/v6/android";
    TextView txt_load;

    String depver_title = "This Version Deprecated";
    String depver_desc = "Update Now!";
    String depver_btn_title = "Google Play";
    String depver_url = "https://play.google.com/store/apps/details?id=com.ermile.khadije";

    String update_url = "https://play.google.com/store/apps/details?id=com.ermile.khadije";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        try {
            PackageInfo pInfo = getApplicationContext().getPackageManager().getPackageInfo(getPackageName(), 0);
            versionName = pInfo.versionName;
            versionCode = pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        MOST_UPDATE_APK();
        final SharedPreferences shared = getSharedPreferences("Prefs", MODE_PRIVATE);
        final Boolean login_one = shared.getBoolean("login_one", false);
        final Boolean farsi = shared.getBoolean("farsi", false);
        final Boolean arabic = shared.getBoolean("arabic", false);
        final Boolean english = shared.getBoolean("english", false);

        txt_load = findViewById(R.id.title_loading);
        txt_load.setText("");
        if (login_one){
            if (farsi){
                txt_load.setText("درحال اتصال به سرور ...");
            }
            if (arabic){
                txt_load.setText("جارٍ الاتصال بالخادم ...");
            }
            if (english){
                txt_load.setText("Connecting to server ...");
            }
        }



    }




    public void first_open(){
        String lan = Locale.getDefault().getLanguage();
        final SharedPreferences shared = getSharedPreferences("Prefs", MODE_PRIVATE);
        final SharedPreferences.Editor editor = shared.edit();

        if (lan.equals("fa")){
            editor.putBoolean("farsi",true);
            editor.apply();
            start_intro();

        }else {
            lay_loading_GONE();
            lay_change_language_VISIBEL();
            findViewById(R.id.farsi).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editor.putBoolean("farsi",true);
                    editor.apply();
                    start_intro();
                }
            });
            findViewById(R.id.arabic).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editor.putBoolean("arabic",true);
                    editor.apply();
                    start_intro();
                }
            });
            findViewById(R.id.english).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editor.putBoolean("english",true);
                    editor.apply();
                    start_intro();
                }
            });
        }


    }

    public void lay_change_language_VISIBEL(){
        findViewById(R.id.lay_change_language).setVisibility(View.VISIBLE);
        findViewById(R.id.lay_change_language).animate().alpha(1).setDuration(500);
    }
    public void lay_change_language_GONE(){
        findViewById(R.id.lay_change_language).animate().alpha(0).setDuration(500);
        findViewById(R.id.lay_change_language).setVisibility(View.GONE);
    }
    public void lay_loading_VISIBEL(){
        findViewById(R.id.lay_loading).setVisibility(View.VISIBLE);
        findViewById(R.id.lay_loading).animate().alpha(1).setDuration(500);
    }
    public void lay_loading_GONE(){
        findViewById(R.id.lay_loading).animate().alpha(0).setDuration(500);
        findViewById(R.id.lay_loading).setVisibility(View.GONE);
    }

    public void start_intro(){
        lay_change_language_GONE();
        if (findViewById(R.id.lay_loading).getVisibility() == View.GONE){
            lay_loading_VISIBEL();
        }
        NEW_APK();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(splash.this, Intro.class);
                startActivity(i);
                finish();
            }
        }, 1500);
    }
    public void start_main(){
        NEW_APK();
        if (findViewById(R.id.lay_loading).getVisibility() == View.GONE){
            lay_loading_VISIBEL();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                send_title();
            }
        }, 1500);
    }


    public void ERROR_GETING(){
        String lan = Locale.getDefault().getLanguage();
        String title_snackbar = "Error connecting to server";
        String btn_snackbar = "Try again";
        final SharedPreferences shared = getSharedPreferences("Prefs", MODE_PRIVATE);
        final Boolean login_one = shared.getBoolean("login_one", false);
        final Boolean farsi = shared.getBoolean("farsi", false);
        final Boolean arabic = shared.getBoolean("arabic", false);
        final Boolean english = shared.getBoolean("english", false);
        txt_load = findViewById(R.id.title_loading);
        LinearLayout lin_splash = findViewById(R.id.linearLayout_splash);
        ViewCompat.setLayoutDirection(lin_splash,ViewCompat.LAYOUT_DIRECTION_LTR);

        if (!login_one){
            if (lan.equals("fa")){
                ViewCompat.setLayoutDirection(lin_splash,ViewCompat.LAYOUT_DIRECTION_RTL);
                title_snackbar = "خطا در اتصال با سرور";
                btn_snackbar = "تلاش مجدد";
            }
            if (lan.equals("ar")){
                ViewCompat.setLayoutDirection(lin_splash,ViewCompat.LAYOUT_DIRECTION_RTL);
                title_snackbar = "خطأ في الاتصال بالخادم";
                btn_snackbar = "حاول مرة أخرى";
            }
        }else {
            if (farsi){
                ViewCompat.setLayoutDirection(lin_splash,ViewCompat.LAYOUT_DIRECTION_RTL);
                title_snackbar = "خطا در اتصال با سرور";
                btn_snackbar = "تلاش مجدد";
            }
            if (arabic){
                ViewCompat.setLayoutDirection(lin_splash,ViewCompat.LAYOUT_DIRECTION_RTL);
                title_snackbar = "خطأ في الاتصال بالخادم";
                btn_snackbar = "حاول مرة أخرى";
            }
            if (english){
                ViewCompat.setLayoutDirection(lin_splash,ViewCompat.LAYOUT_DIRECTION_LTR);
                title_snackbar = "Error connecting to server";
                btn_snackbar = "Try again";
            }
        }


        txt_load.setText(title_snackbar);
        Snackbar snackbar = Snackbar.make(lin_splash, title_snackbar, Snackbar.LENGTH_INDEFINITE)
                .setAction(btn_snackbar, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                        startActivity(getIntent());
                    }
                });
        snackbar.setActionTextColor(Color.WHITE);
        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        snackbar.setDuration(999999999);
        snackbar.show();
    }

    public void get_token(){
        final SharedPreferences shared = getSharedPreferences("Prefs", MODE_PRIVATE);
        final SharedPreferences.Editor editor = shared.edit();
        StringRequest post_getToken = new StringRequest(Request.Method.POST, "https://khadije.com/fa/api/v6/token", new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject mainObject = new JSONObject(response);
                    ok_getToken = mainObject.getBoolean("ok");
                    if (ok_getToken){
                        JSONObject result = mainObject.getJSONObject("result");
                        String token = result.getString("token");

                        editor.putString("token", token);
                        editor.apply();
                        add_user();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ERROR_GETING();
            }
        })
                // Send Headers
        {
            @Override
            public Map<String, String> getHeaders()  {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("appkey", "6167c3b0f96ad6869d0f4898eb06fb45");
                return headers;
            }

        };AppContoroler.getInstance().addToRequestQueue(post_getToken);
    }
    public void add_user(){


            final String model = Build.MODEL;
            final String serial = Build.SERIAL;
            final String manufacturer = Build.MANUFACTURER;
            final String hardware = Build.HARDWARE;
            final String type = Build.TYPE;
            final String board = Build.BOARD;
            final String id = Build.ID;
            final String product =  Build.PRODUCT;
            final String device = Build.DEVICE;
            final String brand = Build.BRAND;

            final SharedPreferences shared = getSharedPreferences("Prefs", MODE_PRIVATE);
            final SharedPreferences.Editor editor = shared.edit();
            final String token_saved = shared.getString("token", "no-tooken");
            StringRequest post_user_add = new StringRequest(Request.Method.POST, "https://khadije.com/fa/api/v6/android/user/add", new Response.Listener<String>(){
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject mainObject = new JSONObject(response);
                        ok_getToken = mainObject.getBoolean("ok");
                        if (ok_getToken){
                            JSONObject result = mainObject.getJSONObject("result");
                            String usercode = result.getString("usercode");
                            String zoneid = result.getString("zoneid");
                            String apikey_one = result.getString("apikey");

                            editor.putString("usercode", usercode);
                            editor.putString("zoneid", zoneid);
                            editor.putString("apikey", apikey_one);
                            editor.putString("token",null);
                            editor.apply();
                            final String apikey = shared.getString("apikey"  , null);
                            if (apikey !=null){
                                editor.putBoolean("login_one",true);
                                editor.apply();
                                first_open();
                            }else {
                                ERROR_GETING();
                            }

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    ERROR_GETING();
                }
            })
            {
                @Override
                public Map<String, String> getHeaders()  {
                    HashMap<String, String> headers = new HashMap<>();
                    headers.put("token", token_saved );
                    return headers;
                }
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> posting = new HashMap<>();
                    posting.put("model", model );
                    posting.put("serial", serial );
                    posting.put("manufacturer", manufacturer );
                    posting.put("version", versionName );
                    posting.put("hardware", hardware );
                    posting.put("type", type );
                    posting.put("board", board );
                    posting.put("id", id );
                    posting.put("product", product );
                    posting.put("device", device );
                    posting.put("brand", brand );
                    return posting;
                }

            };AppContoroler.getInstance().addToRequestQueue(post_user_add);
        }

    public void MOST_UPDATE_APK(){
        final SharedPreferences shared = getSharedPreferences("Prefs", MODE_PRIVATE);
        final Boolean login_one = shared.getBoolean("login_one", false);

        String lan = Locale.getDefault().getLanguage();
        if (lan.equals("fa")){
            url_json = "https://khadije.com/api/v6/android";
        }
        if (lan.equals("ar")){
            url_json = "https://khadije.com/ar/api/v6/android";
        }
        // JSON
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url_json, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject responses) {
                try {
                    Boolean ok = responses.getBoolean("ok");
                    if (ok){
                        JSONObject response = responses.getJSONObject("result");
                        JSONObject deprecated_version = response.getJSONObject("deprecated_version");
                        JSONObject btn = deprecated_version.getJSONObject("btn");
                        int reject_version = deprecated_version.getInt("reject_version");
                        if (!deprecated_version.isNull("title")){
                            depver_title = deprecated_version.getString("title");
                        }
                        if (!deprecated_version.isNull("desc")){
                            depver_desc = deprecated_version.getString("desc");
                        }
                        if (!btn.isNull("title")){
                            depver_btn_title = btn.getString("title");
                        }
                        if (!btn.isNull("url")){
                            depver_url = btn.getString("url");
                        }

                        if (reject_version >= versionCode  ){
                            lay_loading_GONE();
                            LinearLayout layy_dep = findViewById(R.id.lay_deprecated);
                            layy_dep.setVisibility(View.VISIBLE);
                            layy_dep.animate().setDuration(300).alpha(1);
                            TextView title_dep = findViewById(R.id.title_deprecated);
                            TextView desc_dep = findViewById(R.id.desc_deprecated);
                            TextView btn_dep = findViewById(R.id.btn_deprecated);

                            title_dep.setText(depver_title);
                            desc_dep.setText(depver_desc);
                            btn_dep.setText(depver_btn_title);
                            btn_dep.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent browser_website = new Intent ( Intent.ACTION_VIEW );
                                    browser_website.setData ( Uri.parse ( depver_url ) );
                                    startActivity ( browser_website );
                                    finish();
                                }
                            });
                        }else {
                            if (!login_one){
                                get_token();

                            }else {
                                start_main();
                            }
                        }



                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ERROR_GETING();
            }
        });
        AppContoroler.getInstance().addToRequestQueue(req);
        // END JSON

    }
    public void NEW_APK(){
        final SharedPreferences shared = getSharedPreferences("Prefs", MODE_PRIVATE);
        final Boolean farsi = shared.getBoolean("farsi", false);
        final Boolean arabic = shared.getBoolean("arabic", false);
        final Boolean english = shared.getBoolean("english", false);

        String url_json = "";
        if (farsi){
            url_json = "https://khadije.com/api/v6/android";
        }
        if (arabic){
            url_json = "https://khadije.com/ar/api/v6/android";
        }
        if (english){
            url_json = "https://khadije.com/en/api/v6/android";
        }
        // JSON
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url_json, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject responses) {
                try {
                    Boolean ok = responses.getBoolean("ok");
                    if (ok){
                        JSONObject response = responses.getJSONObject("result");
                        JSONObject new_version = response.getJSONObject("app_version");

                        int code = new_version.getInt("code");
                        if (!new_version.isNull("content_text")){
                            update_url = new_version.getString("content_text");
                        }
                        if (code > versionCode ){
                            String title_snackbar = "Update!";
                            String btn_snackbar = "Get the new version";
                            final SharedPreferences shared = getSharedPreferences("Prefs", MODE_PRIVATE);
                            final Boolean farsi = shared.getBoolean("farsi", false);
                            final Boolean arabic = shared.getBoolean("arabic", false);
                            final Boolean english = shared.getBoolean("english", false);
                            LinearLayout lin_splash = findViewById(R.id.linearLayout_splash);
                            ViewCompat.setLayoutDirection(lin_splash,ViewCompat.LAYOUT_DIRECTION_LTR);

                            if (farsi){
                                ViewCompat.setLayoutDirection(lin_splash,ViewCompat.LAYOUT_DIRECTION_RTL);
                                title_snackbar = "بروز باشید!";
                                btn_snackbar = "دریافت نسخه جدید";
                            }
                            if (arabic){
                                ViewCompat.setLayoutDirection(lin_splash,ViewCompat.LAYOUT_DIRECTION_RTL);
                                title_snackbar = "تحديث البقاء!";
                                btn_snackbar = "احصل على النسخة الجديدة";
                            }
                            if (english){
                                ViewCompat.setLayoutDirection(lin_splash,ViewCompat.LAYOUT_DIRECTION_RTL);
                                title_snackbar = "Update!";
                                btn_snackbar = "Get the new version";
                            }

                            Snackbar snackbar = Snackbar.make(lin_splash, title_snackbar, Snackbar.LENGTH_INDEFINITE)
                                    .setAction(btn_snackbar, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            finish();
                                            Intent browser_website = new Intent ( Intent.ACTION_VIEW );
                                            browser_website.setData ( Uri.parse ( update_url ) );
                                            startActivity ( browser_website );
                                        }
                                    });
                            snackbar.setActionTextColor(Color.GREEN);
                            View sbView = snackbar.getView();
                            TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
                            textView.setTextColor(Color.WHITE);
                            snackbar.setDuration(2000);
                            snackbar.show();

                        }


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ERROR_GETING();
            }
        });
        AppContoroler.getInstance().addToRequestQueue(req);
        // END JSON
    }

    public void send_title(){

        // import SharedPreferences
        final SharedPreferences shared = getSharedPreferences("Prefs", MODE_PRIVATE);
        // import manual
        final Boolean farsi = shared.getBoolean("farsi", false);
        final Boolean arabic = shared.getBoolean("arabic", false);
        final Boolean english = shared.getBoolean("english", false);

        // set lang
        String url = "";
        if (farsi){
            txt_load.setText("درحال دریافت اطلاعات..");
            url = "https://khadije.com/api/v6/android";
        }
        if (arabic){
            txt_load.setText("تلقي المعلومات..");
            url = "https://khadije.com/ar/api/v6/android";
        }
        if (english){
            txt_load.setText("Receiving information..");
            url = "https://khadije.com/en/api/v6/android";
        }

        // JSON
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject responses) {
                try {
                    Boolean ok = responses.getBoolean("ok");

                    if (ok) {
                        JSONObject response = responses.getJSONObject("result");
                        JSONArray navigation_btn = response.getJSONArray("navigation");
                        JSONObject home = navigation_btn.getJSONObject(0);
                        JSONObject delneveshte = navigation_btn.getJSONObject(1);
                        JSONObject setting = navigation_btn.getJSONObject(2);

                        final String home_title = home.getString("title");
                        final String delneveshte_title = delneveshte.getString("title");
                        final String setting_title = setting.getString("title");

                        Intent goTo_setting_welcome_title = new Intent(splash.this, MainActivity.class);
                        goTo_setting_welcome_title.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        goTo_setting_welcome_title.putExtra("welcome_title", true);
                        goTo_setting_welcome_title.putExtra("homeTitle", home_title);
                        goTo_setting_welcome_title.putExtra("delneveshteTitle", delneveshte_title);
                        goTo_setting_welcome_title.putExtra("settingTitle", setting_title);
                        startActivity(goTo_setting_welcome_title);

                        if (farsi){
                            txt_load.setText("درحال بارگذاری..");
                        }
                        if (arabic){
                            txt_load.setText("جار التحميل ...");
                        }
                        if (english){
                            txt_load.setText("Loading ...");
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ERROR_GETING();
            }
        });
        AppContoroler.getInstance().addToRequestQueue(req);
        // END JSON
    }







}

