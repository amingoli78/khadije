package com.ermile.khadije;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ermile.khadije.network.AppContoroler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class about_us extends AppCompatActivity {

    boolean ok = false;
    boolean ok_us = false;
    Toolbar toolbar_about;
    DrawerLayout drawerLayout;
    NavigationView navigation_menu;

    // get Version for > new version apk
    int versionCode = 0 ;
    String versionName = "";

    LinearLayout layout;
    TextView desc;
    ProgressBar progressBar;

    Button show_sendPM , sendPM;
    CardView box_sendPM;
    EditText edt_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_us);

        // Change Version from > build.gradle(Module:app)
        try {
            PackageInfo pInfo = getApplicationContext().getPackageManager().getPackageInfo(getPackageName(), 0);
            versionCode = pInfo.versionCode;
            versionName = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        // import SharedPreferences > <Prefs.java>
        final SharedPreferences shared = getSharedPreferences("Prefs", MODE_PRIVATE);
        final SharedPreferences.Editor editor = shared.edit();
        // import Method for lang > <Prefs.java>
        final Boolean farsi = shared.getBoolean("farsi", false);
        final Boolean arabic = shared.getBoolean("arabic", false);
        final Boolean english = shared.getBoolean("english", false);

        toolbar_about = findViewById(R.id.toolbar_about);
        drawerLayout = findViewById(R.id.drawerLayout_about);
        navigation_menu = findViewById(R.id.navigation_view_about);

        show_sendPM = findViewById(R.id.show_sendPM);
        box_sendPM = findViewById(R.id.box_sendPM);
        sendPM = findViewById(R.id.sendPM);
        edt_user = findViewById(R.id.text_user);

        show_sendPM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                box_sendPM.setVisibility(View.VISIBLE);
                box_sendPM.animate().alpha(1).setDuration(300);
                show_sendPM.animate().alpha(0).setDuration(150);
            }
        });

        sendPM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edt_user.length() >= 15){
                    edt_user.getText().clear();
                    Toast.makeText(about_us.this, "پیام شما ارسال شد!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // get Header
        View header_navmenu=navigation_menu.getHeaderView(0);
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
                    Toast.makeText(getApplicationContext(), "English language was chosen!", Toast.LENGTH_SHORT).show();
                    Change_lang();
                }else {Toast.makeText(getApplicationContext(), "language is English !", Toast.LENGTH_SHORT).show();}
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
                    Toast.makeText(getApplicationContext(), "تم اختيار اللغة العربية!", Toast.LENGTH_SHORT).show();
                    Change_lang();
                }else {Toast.makeText(getApplicationContext(), "اللغة العربية!", Toast.LENGTH_SHORT).show();}
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
                    Toast.makeText(getApplicationContext(), "زبان فارسی انتخاب شد!", Toast.LENGTH_SHORT).show();
                    Change_lang();
                }else {Toast.makeText(getApplicationContext(), "زبان فارسی شده!", Toast.LENGTH_SHORT).show();}
            }
        });

        Menu menu_navmenu = navigation_menu.getMenu();
        final MenuItem about_us = menu_navmenu.findItem(R.id.about_us);
        final MenuItem call_us = menu_navmenu.findItem(R.id.call_us);
        final MenuItem future_view = menu_navmenu.findItem(R.id.future_view);
        final MenuItem mission = menu_navmenu.findItem(R.id.mission);
        final MenuItem website = menu_navmenu.findItem(R.id.website);

        progressBar = findViewById(R.id.progress_about);
        desc  = findViewById(R.id.about_desc);


        setSupportActionBar(toolbar_about);
        if(getSupportActionBar()!=null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        String about_titile = getIntent().getStringExtra("aboutus_title");
        String call_title  = getIntent().getStringExtra("callus_title");
        String fetrue_title = getIntent().getStringExtra("futureview_title");
        String mission_title = getIntent().getStringExtra("mission_title");
        String setting_title = getIntent().getStringExtra("setting_title");

        String url = "";

        if (getIntent().getBooleanExtra("about_bol", false)) {
            url = getIntent().getStringExtra("about_url");
        }
        if (getIntent().getBooleanExtra("call_bol", false)) {
            url = getIntent().getStringExtra("call_url");
//            show_sendPM.setVisibility(View.VISIBLE);
        }
        if (getIntent().getBooleanExtra("future_bol", false)) {
            url = getIntent().getStringExtra("future_url");
        }
        if (getIntent().getBooleanExtra("mission_bol", false)) {
            url = getIntent().getStringExtra("mission_url");
        }
        if (getIntent().getBooleanExtra("website_bol", false)) {
            url = getIntent().getStringExtra("website_url");
        }


        // JSON
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject responses) {
                try {
                    ok_us = responses.getBoolean("ok");
                    if (ok_us) {
                        progressBar.setVisibility(View.GONE);
                        JSONObject response = responses.getJSONObject("result");
                        String title = response.getString("title");
                        getSupportActionBar().setTitle(title);
                        String dees = response.getString("content");
                        desc.setText(Html.fromHtml(dees));
                    }else {

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

        String url_json = "";
        if (farsi ){
            btn_fa.setSelected(true);
            btn_fa.setTextColor(Color.parseColor("#ffffff"));
            url_json = "https://khadije.com/api/v6/android";
            ViewCompat.setLayoutDirection(drawerLayout,ViewCompat.LAYOUT_DIRECTION_RTL);
        }
        if (arabic){
            btn_ar.setSelected(true);
            btn_ar.setTextColor(Color.parseColor("#ffffff"));
            url_json = "https://khadije.com/ar/api/v6/android";
            ViewCompat.setLayoutDirection(drawerLayout,ViewCompat.LAYOUT_DIRECTION_RTL);
        }
        if (english){
            btn_en.setSelected(true);
            btn_en.setTextColor(Color.parseColor("#ffffff"));
            url_json = "https://khadije.com/en/api/v6/android";
            ViewCompat.setLayoutDirection(drawerLayout,ViewCompat.LAYOUT_DIRECTION_LTR);
        }
        // JSON
        JsonObjectRequest get_menu = new JsonObjectRequest(Request.Method.GET, url_json, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject responses) {
                try {

                    // set Title Header
                    ok = responses.getBoolean("ok");
                    if (ok){
                        JSONObject response = responses.getJSONObject("result");
                        String json_title_header = response.getString("name");
                        String json_desc_header = response.getString("desc");
                        header_title.setText(json_title_header);
                        header_desc.setText(json_desc_header);

                        JSONObject trans_header = response.getJSONObject("transalate");
                        String json_ver_hed = trans_header.getString("version");
                        ver_hed.setText(json_ver_hed + " " + versionName);

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


                        final Handler mHandler_jsonMain = new Handler();
                        final boolean continueORstop_jsonMain = true;
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
                                                if ( call_us.getTitle().toString().equals(""))
                                                {
                                                    // set menu nav title
                                                    about_us.setTitle(aboutus_title);
                                                    call_us.setTitle(callus_title);
                                                    future_view.setTitle(futureview_title);
                                                    mission.setTitle(mission_title);
                                                    website.setTitle(website_title);
                                                }

                                            }
                                        });
                                    } catch (Exception e) {
                                    }
                                }
                            }
                        }).start();

                        navigation_menu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                            @Override
                            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                                int menuId = item.getItemId();
                                switch (menuId) {
                                    case R.id.about_us:
                                        // put url to <about_us.java>
                                        Intent sendURL_about = new Intent(getApplicationContext(), about_us.class);
                                        sendURL_about.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        sendURL_about.putExtra("about_bol", true);
                                        sendURL_about.putExtra("about_url" , aboutus_url);
                                        startActivity(sendURL_about);
                                        break;
                                    case R.id.call_us:
                                        // put url to <about_us.java>
                                        Intent sendURL_call = new Intent(getApplicationContext(), about_us.class);
                                        sendURL_call.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        sendURL_call.putExtra("call_bol", true);
                                        sendURL_call.putExtra("call_url" , callus_url);
                                        startActivity(sendURL_call);
                                        break;
                                    case R.id.future_view:
                                        // put url to <about_us.java>
                                        Intent sendURL_future = new Intent(getApplicationContext(), about_us.class);
                                        sendURL_future.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        sendURL_future.putExtra("future_bol", true);
                                        sendURL_future.putExtra("future_url" , futureview_url);
                                        startActivity(sendURL_future);
                                        break;
                                    case R.id.mission:
                                        // put url to <about_us.java>
                                        Intent sendURL_mission = new Intent(getApplicationContext(), about_us.class);
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
        AppContoroler.getInstance().addToRequestQueue(get_menu);
        // END JSON
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if( id == android.R.id.home ){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    // back for Exit
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {finish();}
    }
    public void Change_lang() {
        startActivity(new Intent(getApplicationContext(), changing_lang.class));
        finish();
    }

    public void ERROR_GETING(){
        String title_snackbar = "Error connecting to server";
        String btn_snackbar = "Try again";
        final SharedPreferences shared = getSharedPreferences("Prefs", MODE_PRIVATE);
        final Boolean farsi = shared.getBoolean("farsi", false);
        final Boolean arabic = shared.getBoolean("arabic", false);
        final Boolean english = shared.getBoolean("english", false);
        View parentLayout = findViewById(android.R.id.content);
        ViewCompat.setLayoutDirection(parentLayout,ViewCompat.LAYOUT_DIRECTION_LTR);

        if (farsi){
            ViewCompat.setLayoutDirection(parentLayout,ViewCompat.LAYOUT_DIRECTION_RTL);
            title_snackbar = "خطا در اتصال با سرور";
            btn_snackbar = "تلاش مجدد";
        }
        if (arabic){
            ViewCompat.setLayoutDirection(parentLayout,ViewCompat.LAYOUT_DIRECTION_RTL);
            title_snackbar = "خطأ في الاتصال بالخادم";
            btn_snackbar = "حاول مرة أخرى";
        }
        if (english){
            ViewCompat.setLayoutDirection(parentLayout,ViewCompat.LAYOUT_DIRECTION_LTR);
            title_snackbar = "Error connecting to server";
            btn_snackbar = "Try again";
        }

        Snackbar snackbar = Snackbar.make(parentLayout, title_snackbar, Snackbar.LENGTH_INDEFINITE)
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

}
