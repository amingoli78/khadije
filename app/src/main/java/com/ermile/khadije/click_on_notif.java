package com.ermile.khadije;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
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
import android.widget.TextView;

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

        final String getFrom_notif_value = getIntent().getStringExtra("put_notif");
        final String url_other_website = getIntent().getStringExtra("url_other_website");

        switch (getFrom_notif_value){

            case "N_Ihome":
                Intent goto_home = new Intent(getApplicationContext(), MainActivity.class);
                goto_home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                goto_home.putExtra("home_bol", true);
                startActivity(goto_home);
                finish();
                break;

            case "other_website":
                Intent browser_website = new Intent ( Intent.ACTION_VIEW );
                browser_website.setData ( Uri.parse ( url_other_website ) );
                startActivity ( browser_website );
                finish();
                break;

        }

    }
}
