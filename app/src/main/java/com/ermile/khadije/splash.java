package com.ermile.khadije;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class splash extends AppCompatActivity {

    ImageView logo_splash;
    LinearLayout lang;
    TextView far , ara , eng;
    ProgressBar progress_splash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        // MY value
        logo_splash = findViewById(R.id.logo_splash);
        lang = findViewById(R.id.lang);
        far = findViewById(R.id.farsi);
        ara = findViewById(R.id.arabic);
        eng = findViewById(R.id.english);
        progress_splash = findViewById(R.id.progress_splash);
        // Chake net
        new splash.NetCheck().execute();
        boolean connected = true;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        }
        else
        {
            connected = false;
        }
        if (!connected)
        {
            new splash.NetCheck().execute();
        }
        // save  BY Shared Preferences
        final SharedPreferences shared = getSharedPreferences("Prefs", MODE_PRIVATE);
        final SharedPreferences.Editor editor = shared.edit();
        // Device info
        final String Board = Build.BOARD;
        final String bot_loader = Build.BOOTLOADER;
        final String Brand = Build.BRAND;
        final String host = Build.HOST;
        final String device = Build.DEVICE;
        final String product =  Build.PRODUCT;
        final String display = Build.DISPLAY;
        final String tag = Build.TAGS;
        final String fingerprint = Build.FINGERPRINT;
        final String type = Build.TYPE;
        final String hardware = Build.HARDWARE;
        final String unknown = Build.UNKNOWN;
        final String id = Build.ID;
        final String user = Build.USER;
        final String manufacturer = Build.MANUFACTURER;
        final String modle = Build.MODEL;
        final String serial = Build.SERIAL;
        final String sdk_version = String.valueOf(Build.VERSION.SDK_INT);
        final String time = String.valueOf(Build.TIME);
        // Shared Preferences for Tooken & Language
        // Tooken cod and tooken Chakeed?
        final String myTokengName = shared.getString("myTokengName", "no-tooken");
        final String myTokengName_code = shared.getString("myTokengName_code", "no-tooken");
        final Boolean token_sending = shared.getBoolean("token_sending", false);
        // first oppen and set lang
        final Boolean firstoppen = shared.getBoolean("firstoppen", true);
        final Boolean farsi = shared.getBoolean("farsi", false);
        final Boolean arabic = shared.getBoolean("arabic", false);
        final Boolean english = shared.getBoolean("english", false);
        // get Language Devices
        String lan = Locale.getDefault().getLanguage();
        // Chang Language by user if Device not farsi
        if (connected){
            if (!lan.equals("fa") && firstoppen) {
                logo_splash.animate().translationY(0).setDuration(1000);
                chang_lang();

                far.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        editor.putBoolean("firstoppen",false);
                        editor.putBoolean("farsi",true);
                        editor.apply();
                        changing();
                    }
                });
                ara.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        editor.putBoolean("firstoppen",false);
                        editor.putBoolean("arabic",true);
                        editor.apply();
                        changing();
                    }
                });
                eng.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        editor.putBoolean("firstoppen",false);
                        editor.putBoolean("english",true);
                        editor.apply();
                        changing();
                    }
                });
            }
        }
        //Chang Language fa if Device is farsi
        if (lan.equals("fa") && firstoppen)
        {
            progress_splash.animate().alpha(1).setDuration(200);
            editor.putBoolean("firstoppen",false);
            editor.putBoolean("farsi",true);
            editor.apply();
            going();
        }
        // start other oppen
        if (connected && !firstoppen)
        {
            progress_splash.animate().alpha(1).setDuration(200);
            going();
        }
        // Post Method
        StringRequest post_id = new StringRequest(Request.Method.POST, "https://khadije.com/api/v5/user/add", new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject mainObject = new JSONObject(response);

                    // get user Token
                    JSONObject result = mainObject.getJSONObject("result");
                    String token = result.getString("user_token");
                    String token_code = result.getString("user_code");
                    // save TOKEN
                    editor.putString("myTokengName", token);
                    editor.putString("myTokengName_code", token_code);
                    editor.putBoolean("token_sending",true);
                    editor.apply();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error net", Toast.LENGTH_SHORT).show();
            }
        })
        // Send Headers
        {
            @Override
            public Map<String, String> getHeaders()  {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("x-app-request", "android");
                headers.put("authorization", "$2y$07$J5lyhNSfVCEVxPZvEmrXhemZpzwekNKJRPHC1kwth3yPw6U6cUBPC");
                return headers;
            }
            // Send Device info
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> posting = new HashMap<>();

                posting.put("board", Board );
                posting.put("bot_loader", bot_loader );
                posting.put("brand", Brand );
                posting.put("host", host );
                posting.put("device", device );
                posting.put("product", product );
                posting.put("display", display );
                posting.put("tag", tag );
                posting.put("fingerprint", fingerprint );
                posting.put("type", type );
                posting.put("hardware", hardware );
                posting.put("unknown", unknown );
                posting.put("id", id );
                posting.put("manufacturer", manufacturer );
                posting.put("user", user );
                posting.put("serial", serial );
                posting.put("sdk_version", sdk_version );
                posting.put("time", time );
                posting.put("modle", modle );
                if (token_sending)
                {
                    posting.put("user_token", myTokengName );
                    posting.put("user_code", myTokengName_code );
                }
                return posting;
            }
        };AppContoroler.getInstance().addToRequestQueue(post_id);

    }

    // go to Intro
    public void going(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progress_splash.animate().alpha(1).setDuration(500);
                Intent i = new Intent(splash.this, Intro.class);
                send_title();
                startActivity(i);
                finish();
            }
        }, 2000);
    }
    // lang Visible and animate
    public void chang_lang(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                lang.animate().alpha(1).setDuration(500);
                lang.setVisibility(View.VISIBLE);
            }
        }, 400);
    }
    // Chang lang by user
    public void changing(){
        lang.animate().alpha(0).setDuration(500);
        lang.setVisibility(View.INVISIBLE);
        logo_splash.animate().translationY(100).setDuration(700);
        progress_splash.animate().alpha(1).setDuration(400);
        going();
    }

    /**
     * Send Title
     */

    public void send_title(){

        // import SharedPreferences
        final SharedPreferences shared = getSharedPreferences("Prefs", MODE_PRIVATE);
        final SharedPreferences.Editor editor = shared.edit();
        // import manual
        final Boolean farsi = shared.getBoolean("farsi", false);
        final Boolean arabic = shared.getBoolean("arabic", false);
        final Boolean english = shared.getBoolean("english", false);
        final Boolean firstoppen = shared.getBoolean("firstoppen", true);

        // set lang
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


                    //------------------------------------------------------------

                    if (!firstoppen){
                        Intent goTo_setting_welcome_title = new Intent(splash.this, MainActivity.class);
                        goTo_setting_welcome_title.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        goTo_setting_welcome_title.putExtra("welcome_title", true);
                        goTo_setting_welcome_title.putExtra("payTitle" , pay_title);
                        goTo_setting_welcome_title.putExtra("homeTitle" , home_title);
                        goTo_setting_welcome_title.putExtra("tripTitle" , trip_title);
                        goTo_setting_welcome_title.putExtra("delneveshteTitle" , delneveshte_title);
                        goTo_setting_welcome_title.putExtra("settingTitle" , setting_title);
                        startActivity(goTo_setting_welcome_title);
                    }
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

    /**
     * Check Network
     */
    public class NetCheck extends AsyncTask<String,String,Boolean>
    {
        @Override
        protected void onPreExecute(){
        }
        /**
         * Gets current device state and checks for working internet connection by trying Google.
         **/
        @Override
        protected Boolean doInBackground(String... args){
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                try {
                    URL url = new URL("http://www.google.com");
                    HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                    urlc.setConnectTimeout(3000);
                    urlc.connect();
                    if (urlc.getResponseCode() == 200) {
                        return true;
                    }
                } catch (MalformedURLException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            return false;
        }
        @Override
        protected void onPostExecute(Boolean th){
            if(th == false){
                View parentLayout = findViewById(android.R.id.content);
                Snackbar snackbar = Snackbar.make(parentLayout, "به اینترنت متصل شوید", Snackbar.LENGTH_INDEFINITE)
                        .setAction("تلاش مجدد", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                finish();
                                startActivity(getIntent());
                            }
                        });
                snackbar.setActionTextColor(Color.RED);
                View sbView = snackbar.getView();
                TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(Color.YELLOW);
                snackbar.setDuration(999999999);
                snackbar.show();
            }
            else{

            }
        }
    }
}

