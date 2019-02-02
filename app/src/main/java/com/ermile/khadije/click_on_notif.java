package com.ermile.khadije;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ermile.khadije.network.AppContoroler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class click_on_notif extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.click_on_notif);

        final ProgressBar progressBar = findViewById(R.id.progress_notif);
        final RelativeLayout relativeLayout = findViewById(R.id.Layout_webview_notif);
        final SwipeRefreshLayout swipeRefresh = findViewById(R.id.swipref_notif);
        final WebView webView = findViewById(R.id.webview_notif);

        final String getFrom_notif_value = getIntent().getStringExtra("put_notif");
        final String url_other_website = getIntent().getStringExtra("url_other_website");
        final Boolean browser_oppen_inapp = getIntent().getBooleanExtra("notif_otherBrowser_inApp" , false);

        // import SharedPreferences > <Prefs.java>
        final SharedPreferences shared = getSharedPreferences("Prefs", MODE_PRIVATE);
        // import Method for lang > <Prefs.java>
        final Boolean farsi = shared.getBoolean("farsi", false);
        final Boolean arabic = shared.getBoolean("arabic", false);
        final Boolean english = shared.getBoolean("english", false);


        String url = "";
        if (farsi){ url = "https://khadije.com/api/v5/android"; }
        if (arabic){ url = "https://khadije.com/ar/api/v5/android"; }
        if (english){ url = "https://khadije.com/en/api/v5/android"; }
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

                    switch (getFrom_notif_value){

                        case "N_Ihome":
                            Intent goto_home = new Intent(getApplicationContext(), MainActivity.class);
                            goto_home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            goto_home.putExtra("home_bol", true);
                            startActivity(goto_home);
                            finish();
                            break;
                        case "N_about":
                            Intent sendURL_about = new Intent(getApplicationContext(), abuot_get_notif.class);
                            sendURL_about.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            sendURL_about.putExtra("about_bol", true);
                            sendURL_about.putExtra("about_url" , aboutus_url);
                            startActivity(sendURL_about);
                            finish();
                            break;
                        case "N_call":
                            // put url to <about_us.java>
                            Intent sendURL_call = new Intent(getApplicationContext(), abuot_get_notif.class);
                            sendURL_call.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            sendURL_call.putExtra("call_bol", true);
                            sendURL_call.putExtra("call_url" , callus_url);
                            startActivity(sendURL_call);
                            finish();
                            break;
                        case "N_futrue":
                            // put url to <about_us.java>
                            Intent sendURL_future = new Intent(getApplicationContext(), abuot_get_notif.class);
                            sendURL_future.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            sendURL_future.putExtra("future_bol", true);
                            sendURL_future.putExtra("future_url" , futureview_url);
                            startActivity(sendURL_future);
                            break;
                        case "N_mission":
                            // put url to <about_us.java>
                            Intent sendURL_mission = new Intent(getApplicationContext(), abuot_get_notif.class);
                            sendURL_mission.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            sendURL_mission.putExtra("mission_bol", true);
                            sendURL_mission.putExtra("mission_url" , mission_url);
                            startActivity(sendURL_mission);
                            finish();
                            break;
                        case "website":
                            Intent browser_khadije = new Intent ( Intent.ACTION_VIEW );
                            browser_khadije.setData ( Uri.parse ( website_url ) );
                            startActivity ( browser_khadije );
                            finish();
                            break;
                        case "other_website":
                            if (browser_oppen_inapp){
                                progressBar.setVisibility(View.GONE);
                                relativeLayout.setVisibility(View.VISIBLE);
                                // load in Start
                                swipeRefresh.setRefreshing(true);
                                webView.loadUrl(url_other_website ,sernd_headers);
                                swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
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
                                        return true;
                                    }
                                    @Override
                                    public void onPageFinished(WebView view, String url) {
                                        swipeRefresh.setRefreshing(false);
                                    }});

                            }else {
                                Intent browser_website = new Intent ( Intent.ACTION_VIEW );
                                browser_website.setData ( Uri.parse ( url_other_website ) );
                                startActivity ( browser_website );
                                finish();
                                break;
                            }


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                finish();
                startActivity ( new Intent(getApplicationContext() , errornet.class ) );
            }
        });
        AppContoroler.getInstance().addToRequestQueue(Json_MainActivityGET);




    }
}
