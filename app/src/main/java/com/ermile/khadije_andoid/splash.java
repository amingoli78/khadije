package com.ermile.khadije_andoid;


import android.content.Context;
import android.content.DialogInterface;
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
import android.support.v7.app.AlertDialog;
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
import com.android.volley.toolbox.StringRequest;
import com.ermile.khadije_andoid.network.AppContoroler;
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


        logo_splash = findViewById(R.id.logo_splash);
        lang = findViewById(R.id.lang);
        far = findViewById(R.id.farsi);
        ara = findViewById(R.id.arabic);
        eng = findViewById(R.id.english);
        progress_splash = findViewById(R.id.progress_splash);


        new splash.NetCheck().execute();
        boolean connected = true;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        } else{ connected = false; }

        if (!connected)
        {
            new splash.NetCheck().execute();
        }

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

        final String tooken = shared.getString("myStringName", "no-tooken");
        final Boolean byss = shared.getBoolean("by", false);

        final Boolean firstoppen = shared.getBoolean("firstoppen", true);
        final Boolean farsi = shared.getBoolean("farsi", false);
        final Boolean arabic = shared.getBoolean("arabic", false);
        final Boolean english = shared.getBoolean("english", false);


        /**
         * Alert Dialog
         */



        String lan = Locale.getDefault().getLanguage();
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
        if (lan.equals("fa") && firstoppen)
        {
            editor.putBoolean("firstoppen",false);
            editor.putBoolean("farsi",true);
            editor.apply();
            going();
        }
        if (connected && !firstoppen)
        {
            progress_splash.animate().alpha(1).setDuration(200);
            going();
        }
        //////////////post
        StringRequest post_id = new StringRequest(Request.Method.POST, "https://khadije.com/api/v5/user/add", new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject mainObject = new JSONObject(response);

                    String sending = mainObject.getString("ok");
                    JSONObject result = mainObject.getJSONObject("result");
                    String token = result.getString("usertoken");
                    // save TOKEN
                    editor.putString("myStringName", token);
                    editor.putBoolean("by",true);
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

        {
            @Override
            public Map<String, String> getHeaders()  {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("x-app-request", "android");
                headers.put("authorization", "$2y$07$J5lyhNSfVCEVxPZvEmrXhemZpzwekNKJRPHC1kwth3yPw6U6cUBPC");
                return headers;
            }

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
                if (byss == true){
                    posting.put("app_tooken", tooken );
                }



                return posting;
            }
        };AppContoroler.getInstance().addToRequestQueue(post_id);

    }


    public void going(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progress_splash.animate().alpha(1).setDuration(500);
                Intent i = new Intent(splash.this, Intro.class);
                startActivity(i);
                finish();
            }
        }, 2000);
    }
    public void chang_lang(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                lang.animate().alpha(1).setDuration(500);
                lang.setVisibility(View.VISIBLE);
            }
        }, 700);
    }
    public void changing(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                lang.animate().alpha(0).setDuration(500);
                lang.setVisibility(View.INVISIBLE);
                logo_splash.animate().translationY(100).setDuration(700);
                progress_splash.animate().alpha(1).setDuration(400);
                going();
            }
        }, 300);
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

