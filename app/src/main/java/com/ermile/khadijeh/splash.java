package com.ermile.khadijeh;

import android.app.Dialog;
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
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ermile.khadijeh.network.AppContoroler;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class splash extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new splash.NetCheck().execute();
        boolean connected = true;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        } else{ connected = false; }

        if (connected == false){
            new splash.NetCheck().execute();
        }
        if (connected == true){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent(splash.this, Intro.class);
                    startActivity(i);
                    finish();
                }
            }, 2000);
        }


        Dialog dialog = new Dialog(this);
        dialog.setTitle("asdasdas");




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

        final Boolean firstoppen = shared.getBoolean("by", false);
        final Boolean farsi = shared.getBoolean("by", false);
        final Boolean pf_arabic = shared.getBoolean("by", false);
        final Boolean pf_english = shared.getBoolean("by", false);



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
                    if (byss == true){
                        Toast.makeText(splash.this, "ss: " +tooken , Toast.LENGTH_SHORT).show();
                    }else {Toast.makeText(splash.this, "no", Toast.LENGTH_SHORT).show();}








                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
            }
        })

        {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> posting = new HashMap<>();

                posting.put("Board", Board );
                posting.put("bot_loader", bot_loader );
                posting.put("Brand", Brand );
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
                    posting.put("tooken", tooken );
                }



                return posting;
            }
        };AppContoroler.getInstance().addToRequestQueue(post_id);

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

