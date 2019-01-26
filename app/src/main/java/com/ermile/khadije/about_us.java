package com.ermile.khadije;

import android.content.ClipData;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ermile.khadije.network.AppContoroler;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

public class about_us extends AppCompatActivity {

    LinearLayout layout;
    TextView desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_us);

        layout = findViewById(R.id.mother_about_us);
        desc  = findViewById(R.id.about_desc);

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
                startActivity(new Intent(about_us.this,errornet.class));
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
        }

        return super.onOptionsItemSelected(item);
    }

}
