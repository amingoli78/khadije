package com.ermile.khadije;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ermile.khadije.network.AppContoroler;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import static org.jsoup.nodes.Entities.escape;
import static org.jsoup.nodes.Entities.getByName;

public class abuot_get_notif extends AppCompatActivity {

    LinearLayout layout;
    TextView desc;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.abuot_get_notif);

        progressBar = findViewById(R.id.progress_about_get_notif);
        layout = findViewById(R.id.mother_about_get_notif);
        desc  = findViewById(R.id.about_get_notif_desc);

        final SharedPreferences shared = getSharedPreferences("Prefs", MODE_PRIVATE);
        final Boolean farsi = shared.getBoolean("farsi", false);
        final Boolean arabic = shared.getBoolean("arabic", false);
        final Boolean english = shared.getBoolean("english", false);

        if (farsi || arabic){
            ViewCompat.setLayoutDirection(layout,ViewCompat.LAYOUT_DIRECTION_RTL);
        }
        if (english){
            ViewCompat.setLayoutDirection(layout,ViewCompat.LAYOUT_DIRECTION_LTR);
        }

        if(getSupportActionBar()!=null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }



        String url = "";

        if (getIntent().getBooleanExtra("about_bol", false)) {
            url = getIntent().getStringExtra("about_url");
        }
        if (getIntent().getBooleanExtra("call_bol", false)) {
            url = getIntent().getStringExtra("call_url");
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
            public void onResponse(JSONObject response) {
                try {

                    progressBar.setVisibility(View.GONE);
                    String title = response.getString("title");
                    getSupportActionBar().setTitle(title);
                    String dees = response.getString("content");
                    String desc_decod = Jsoup.parse(dees).body().text();
                    desc.setText(desc_decod);




                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                finish();
                startActivity(new Intent(abuot_get_notif.this,errornet.class));
            }


        });
        AppContoroler.getInstance().addToRequestQueue(req);
        // END JSON

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if( id == android.R.id.home ){
            finish();
            startActivity(new Intent(abuot_get_notif.this, MainActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

}
