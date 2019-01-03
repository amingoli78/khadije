package com.ermile.khadijeh;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import java.lang.reflect.Field;

public class MainActivity extends AppCompatActivity {


    private Fragment fragment;
    private FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final int versionCode = BuildConfig.VERSION_CODE;
        String versionName = BuildConfig.VERSION_NAME;


        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        fragmentManager = getSupportFragmentManager();

        disableShiftMode(bottomNav);

        bottomNav.setSelectedItemId(R.id.item_1);

        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.item_1:
                        fragment = new f_one();
                        break;

                    case R.id.item_2:
                        fragment = new f_two();
                        break;

                    case R.id.item_3:
                        fragment = new f_one();
                        break;
                    case R.id.item_4:
                        fragment = new f_one();
                        break;
                    case R.id.item_5:
                        fragment = new f_one();
                        break;

                }

                final FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.main_container, fragment).commit();
                return true;
            }
        });




//        // JSON
//        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, "http://mimsg.ir/json_app/app.json", null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                try {
//
//                    // new version for app
//                    int app_version = response.getInt("version");
//                    if (versionCode < app_version) {
//                        Notification.Builder nb = new Notification.Builder(MainActivity.this);
//                        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//                        nb.setContentTitle("بروزرسانی")
//                                .setContentText("نسخه جدید رو دانلود کنید!")
//                                .setTicker("برو دانلود کن دیگه")
//                                .setSmallIcon(android.R.drawable.stat_sys_download)
//                                .setAutoCancel(false)
//                                .setSound(alarmSound);
//                        Notification notif = nb.build();
//                        NotificationManager notifManager = (NotificationManager) MainActivity.this.getSystemService(Context.NOTIFICATION_SERVICE);
//                        notifManager.notify(0, notif);
//                    }
//                    // notif
//                    boolean notif_bol = response.getBoolean("notif");
//                    String notif_title = response.getString("title_notif");
//                    String notif_des = response.getString("des_notif");
//                    if (notif_bol == true) {
//                        Notification.Builder nb = new Notification.Builder(MainActivity.this);
//                        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//                        nb.setContentTitle(notif_title)
//                                .setContentText(notif_des)
//                                .setTicker("برو دانلود کن دیگه")
//                                .setSmallIcon(android.R.drawable.stat_sys_download)
//                                .setAutoCancel(false)
//                                .setSound(alarmSound);
//                        Notification notif = nb.build();
//                        NotificationManager notifManager = (NotificationManager) MainActivity.this.getSystemService(Context.NOTIFICATION_SERVICE);
//                        notifManager.notify(1, notif);
//                    }
//                    // toolbar and tab Top or Bottom?
////                    is top
//                    boolean tab_pos = response.getBoolean("Tab_IsTop");
//                    if (tab_pos == true) {
//                        tab_top.setVisibility(View.VISIBLE);
//                        viewPager = findViewById(R.id.viewPager_top);
//                        tabLayout = findViewById(R.id.tabLayout_top);
//
//                        setupViewPager(viewPager);
//                        tabLayout.setupWithViewPager(viewPager);
//
//                    } else {
//                        tab_bottom.setVisibility(View.VISIBLE);
//                        viewPager = findViewById(R.id.viewPager_bottom);
//                        tabLayout = findViewById(R.id.tabLayout_bottom);
//
//                        setupViewPager(viewPager);
//                        tabLayout.setupWithViewPager(viewPager);
//
//                        tabLayout.getTabAt(0).setIcon(R.drawable.ic_home);
//
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//            }
//        });AppContoroler.getInstance().addToRequestQueue(req);
//        // END JSON


    }

    // Method for disabling ShiftMode of BottomNavigationView
    @SuppressLint("RestrictedApi")
    private void disableShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                item.setShifting(false);
                // set once again checked value, so view will be updated
                item.setChecked(item.getItemData().isChecked());
            }
        } catch (NoSuchFieldException e) {
            Log.e("BNVHelper", "Unable to get shift mode field", e);
        } catch (IllegalAccessException e) {
            Log.e("BNVHelper", "Unable to change value of shift mode", e);
        }
    }


}
