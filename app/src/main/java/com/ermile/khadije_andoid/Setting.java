package com.ermile.khadije_andoid;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ermile.khadije_andoid.network.AppContoroler;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Setting extends AppCompatActivity {

    TextView change_lang;
    Button fa,ar,en;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
        // save  BY Shared Preferences
        final SharedPreferences shared = getSharedPreferences("Prefs", MODE_PRIVATE);
        final SharedPreferences.Editor editor = shared.edit();

        change_lang = findViewById(R.id.change_setting);
        fa = findViewById(R.id.farsi_setting);
        ar = findViewById(R.id.arabic_setting);
        en = findViewById(R.id.english_setting);

        final Boolean farsi = shared.getBoolean("farsi", false);
        final Boolean arabic = shared.getBoolean("arabic", false);
        final Boolean english = shared.getBoolean("english", false);


        if (farsi) {
            fa.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.lang_changed));
        }
        if (arabic) {
            ar.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.lang_changed));
            change_lang.setText("اختر لغتك");
        }
        if (english) {
            en.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.lang_changed));
            change_lang.setText("Choose your language");
        }



        fa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putBoolean("farsi",true);
                editor.putBoolean("arabic",false);
                editor.putBoolean("english",false);
                editor.apply();
                Toast.makeText(Setting.this, " برنامه را مجددا اجرا کنید.", Toast.LENGTH_SHORT).show();
                EXIT();
            }
        });
        ar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putBoolean("arabic",true);
                editor.putBoolean("farsi",false);
                editor.putBoolean("english",false);
                editor.apply();
                Toast.makeText(Setting.this, "تشغيل البرنامج مرة أخرى.", Toast.LENGTH_SHORT).show();
                EXIT();

            }
        });
        en.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putBoolean("english",true);
                editor.putBoolean("farsi",false);
                editor.putBoolean("arabic",false);
                editor.apply();
                Toast.makeText(Setting.this, "run app again.", Toast.LENGTH_SHORT).show();
                EXIT();
            }
        });
        ///////////////////////////////////////////////////////////////

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

                    //static
                    final BottomNavigationViewEx bottomNav = findViewById(R.id.bottom_navigation_setting);
                    bottomNav.enableAnimation(false);
                    bottomNav.enableShiftingMode(false);
                    bottomNav.enableItemShiftingMode(false);
                    bottomNav.setTextSize(10f);
                    bottomNav.setIconSize(28,28);
                    bottomNav.setSelectedItemId(R.id.item_setting);

                    //------------------------------------------------------------
                    Menu menu = bottomNav.getMenu();
                    final MenuItem pay_menu = menu.findItem(R.id.item_pay);
                    final MenuItem home_menu = menu.findItem(R.id.item_home);
                    final MenuItem trip_menu = menu.findItem(R.id.item_trip);
                    final MenuItem delneveshte_menu = menu.findItem(R.id.item_delneveshte);
                    final MenuItem setting_menu = menu.findItem(R.id.item_setting);



                    // Chek net every 5 seconds
                    mHandler = new Handler();
                    continue_or_stop = true;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (continue_or_stop) {
                                try {
                                    Thread.sleep(300);
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
                                    startActivity(new Intent(Setting.this, MainActivity.class));
                                    break;

                                case R.id.item_home:
                                    startActivity(new Intent(Setting.this, MainActivity.class));
                                    break;

                                case R.id.item_trip:
                                    startActivity(new Intent(Setting.this, MainActivity.class));
                                    break;

                                case R.id.item_delneveshte:
                                    startActivity(new Intent(Setting.this, MainActivity.class));
                                    break;

                                case R.id.item_setting:
                                    startActivity(new Intent(Setting.this, MainActivity.class));
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
        });
        AppContoroler.getInstance().addToRequestQueue(req);
        // END JSON















    }

    public void EXIT(){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("EXIT", true);
        startActivity(intent);
    }
}
