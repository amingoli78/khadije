package com.ermile.khadije;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ermile.khadije.network.AppContoroler;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Setting extends AppCompatActivity {



    TextView change_lang;
    Button fa,ar,en;

     Handler mHandler;
     boolean continue_or_stop;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);

        final String titlepay = getIntent().getStringExtra("payTitle");
        final String titlehome = getIntent().getStringExtra("homeTitle");
        final String titletrip = getIntent().getStringExtra("tripTitle");
        final String titledelneveshte = getIntent().getStringExtra("delneveshteTitle");
        final String titlesetting = getIntent().getStringExtra("settingTitle");

        // for false animate in bottom navigation
        final BottomNavigationViewEx bottomNav = findViewById(R.id.bottom_navigation_setting);
        bottomNav.setSelectedItemId(R.id.item_setting);
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


        pay_menu.setTitle(titlepay);
        home_menu.setTitle(titlehome);
        trip_menu.setTitle(titletrip);
        delneveshte_menu.setTitle(titledelneveshte);
        setting_menu.setTitle(titlesetting);


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
                Toast.makeText(Setting.this, "زبان فارسی انتخاب شد!", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(Setting.this, "تم اختيار اللغة العربية!", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(Setting.this, "English language was chosen!", Toast.LENGTH_SHORT).show();
                EXIT();
            }
        });
        ///////////////////////////////////////////////////////////////
        // Chek net every 5 seconds
        mHandler = new Handler();
        continue_or_stop = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (continue_or_stop) {
                    try {
                        Thread.sleep(2000);
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                                boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
                                if (!isConnected){
                                    continue_or_stop = false;
                                    finishActivity(1);
                                    finishActivity(0);
                                    startActivity(new Intent(Setting.this,errornet.class));
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
                Intent goTo_setting_pay = new Intent(Setting.this, MainActivity.class);
                goTo_setting_pay.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                goTo_setting_pay.putExtra("pay", true);
                goTo_setting_pay.putExtra("payTitle" , titlepay);
                goTo_setting_pay.putExtra("homeTitle" , titlehome);
                goTo_setting_pay.putExtra("tripTitle" , titletrip);
                goTo_setting_pay.putExtra("delneveshteTitle" , titledelneveshte);
                goTo_setting_pay.putExtra("settingTitle" , titlesetting);
                startActivity(goTo_setting_pay);
                break;

            case R.id.item_home:
                Intent goTo_setting_home = new Intent(Setting.this, MainActivity.class);
                goTo_setting_home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                goTo_setting_home.putExtra("home", true);
                goTo_setting_home.putExtra("payTitle" , titlepay);
                goTo_setting_home.putExtra("homeTitle" , titlehome);
                goTo_setting_home.putExtra("tripTitle" , titletrip);
                goTo_setting_home.putExtra("delneveshteTitle" , titledelneveshte);
                goTo_setting_home.putExtra("settingTitle" , titlesetting);
                startActivity(goTo_setting_home);
                break;

            case R.id.item_trip:
                Intent goTo_setting_trip = new Intent(Setting.this, MainActivity.class);
                goTo_setting_trip.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                goTo_setting_trip.putExtra("trip", true);
                goTo_setting_trip.putExtra("payTitle" , titlepay);
                goTo_setting_trip.putExtra("homeTitle" , titlehome);
                goTo_setting_trip.putExtra("tripTitle" , titletrip);
                goTo_setting_trip.putExtra("delneveshteTitle" , titledelneveshte);
                goTo_setting_trip.putExtra("settingTitle" , titlesetting);
                startActivity(goTo_setting_trip);
                break;

            case R.id.item_delneveshte:
                Intent goTo_setting_hert = new Intent(Setting.this, MainActivity.class);
                goTo_setting_hert.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                goTo_setting_hert.putExtra("hert", true);
                goTo_setting_hert.putExtra("payTitle" , titlepay);
                goTo_setting_hert.putExtra("homeTitle" , titlehome);
                goTo_setting_hert.putExtra("tripTitle" , titletrip);
                goTo_setting_hert.putExtra("delneveshteTitle" , titledelneveshte);
                goTo_setting_hert.putExtra("settingTitle" , titlesetting);
                startActivity(goTo_setting_hert);
                break;
        }
        return true;
            }
        });

    }

    public void EXIT(){
        startActivity(new Intent(Setting.this,changing_lang.class));
        finish();
    }


}
