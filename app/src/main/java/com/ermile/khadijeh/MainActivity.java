package com.ermile.khadijeh;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ermile.khadijeh.network.AppContoroler;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbars;
    ViewPager viewPager;
    TabLayout tabLayout;
    RelativeLayout tab_top, tab_bottom;

    //Fragment's
    f_one oneFragment = new f_one();
    f_two twoFragment = new f_two();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final int versionCode = BuildConfig.VERSION_CODE;
        String versionName = BuildConfig.VERSION_NAME;

        tab_bottom = findViewById(R.id.include_tabBottom);
        tab_top = findViewById(R.id.include_tabTop);


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
//                    is top
                    boolean tab_pos = response.getBoolean("Tab_IsTop");
                    if (tab_pos == true) {
                        tab_top.setVisibility(View.VISIBLE);
                        viewPager = findViewById(R.id.viewPager_top);
                        tabLayout = findViewById(R.id.tabLayout_top);
                        toolbars = findViewById(R.id.toolbars_top);

                        String appname = response.getString("name");
                        toolbars.setTitle(appname);

                        setupViewPager(viewPager);
                        tabLayout.setupWithViewPager(viewPager);
                        setSupportActionBar(toolbars);

                    } else {
                        tab_bottom.setVisibility(View.VISIBLE);
                        viewPager = findViewById(R.id.viewPager_bottom);
                        tabLayout = findViewById(R.id.tabLayout_bottom);
                        toolbars = findViewById(R.id.toolbars_bottom);

                        String appname = response.getString("name");
                        toolbars.setTitle(appname);

                        setupViewPager(viewPager);
                        tabLayout.setupWithViewPager(viewPager);
                        setSupportActionBar(toolbars);

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
    // add fragment
    private void setupViewPager(final ViewPager viewPager) {
        final Utils.ViewPagerAdapter adapter = new Utils.ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(oneFragment, "اخبار سایت");
        adapter.addFragment(twoFragment, "تیکت ها");
        viewPager.setAdapter(adapter);
    }

}
