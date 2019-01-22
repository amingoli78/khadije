package com.ermile.khadije;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ermile.khadije.network.AppContoroler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class NotificationService extends Service {

    Timer timer;
    TimerTask timerTask;
    String TAG = "Timers";
    int Your_X_SECS = 60;


    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);

        startTimer();

        return START_STICKY;
    }


    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate");


    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        stoptimertask();
        super.onDestroy();


    }

    //we are going to use a handler to be able to run in our TimerTask
    final Handler handler = new Handler();


    public void startTimer() {
        //set a new Timer
        timer = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask();

        //schedule the timer, after the first 5000ms the TimerTask will run every 10000ms
        timer.schedule(timerTask, 60000, Your_X_SECS * 1000); //
        //timer.schedule(timerTask, 5000,1000); //
    }

    public void stoptimertask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void initializeTimerTask() {

        timerTask = new TimerTask() {
            public void run() {

                //use a handler to run a toast that shows the current timestamp
                handler.post(new Runnable() {
                    public void run() {

                        //TODO CALL NOTIFICATION FUNC
                        post_smile();

                    }
                });
            }
        };
    }


    /**
     * Post Smile
     */
    public void post_smile(){
        // import SharedPreferences
        final SharedPreferences shared = getSharedPreferences("Prefs", MODE_PRIVATE);
        final SharedPreferences.Editor editor = shared.edit();
        // import manual
        final String myTokengName = shared.getString("myTokengName", "no-tooken");
        final String myTokengName_code = shared.getString("myTokengName_code", "no-tooken");

        final Boolean farsi = shared.getBoolean("farsi", false);
        final Boolean arabic = shared.getBoolean("arabic", false);
        final Boolean english = shared.getBoolean("english", false);

        // set lang
        String url_post = "";
        if (farsi){
            url_post = "https://khadije.com/api/v5/smile";
        }
        if (arabic){
            url_post = "https://khadije.com/ar/api/v5/smile";
        }
        if (english){
            url_post = "https://khadije.com/en/api/v5/smile";
        }

        // Post Method
        StringRequest post_id = new StringRequest(Request.Method.POST, url_post, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject getRespone = new JSONObject(response);

                    Boolean newNotif = getRespone.getBoolean("notif_new");

                    if (newNotif){
                        Toast.makeText(getApplicationContext(), "حالت اجرا در پس زمینه", Toast.LENGTH_SHORT).show();
                        post_notif();
                    }

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
                posting.put("user_token", "hfhggsdda" );
                posting.put("user_code", "52" );

                return posting;
            }
        };AppContoroler.getInstance().addToRequestQueue(post_id);
    }


    /**
     * Post Notif
     */
    public void post_notif(){
        // import SharedPreferences
        final SharedPreferences shared = getSharedPreferences("Prefs", MODE_PRIVATE);
        final SharedPreferences.Editor editor = shared.edit();
        // import manual
        final String myTokengName = shared.getString("myTokengName", "no-tooken");
        final String myTokengName_code = shared.getString("myTokengName_code", "no-tooken");

        final Boolean farsi = shared.getBoolean("farsi", false);
        final Boolean arabic = shared.getBoolean("arabic", false);
        final Boolean english = shared.getBoolean("english", false);

        // set lang
        String url_post = "";
        if (farsi){
            url_post = "https://khadije.com/api/v5/notif";
        }
        if (arabic){
            url_post = "https://khadije.com/ar/api/v5/notif";
        }
        if (english){
            url_post = "https://khadije.com/en/api/v5/notif";
        }


        // Post Method
        StringRequest post_id = new StringRequest(Request.Method.POST, url_post, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                try {

                    JSONArray getRespone = new JSONArray(response);

                    for (int respone = 0 ; respone <= 2 ; respone++){
                        JSONObject one = getRespone.getJSONObject(respone);
                        String notif_title = one.getString("title");
                        String notif_des = one.getString("cat");
                        // Notif
                        Notification.Builder nb = new Notification.Builder(getApplicationContext());
                        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALL);
                        nb.setContentTitle( notif_title )
                                .setContentText( notif_des )
                                .setSmallIcon(android.R.drawable.ic_dialog_email)
                                .setSound(alarmSound);
                        Notification notifs = nb.build();
                        NotificationManager notifManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                        notifManager.notify(10 + respone + 9, notifs);
                    }

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
                posting.put("user_token", myTokengName );
                posting.put("user_code", myTokengName_code );

                return posting;
            }
        };AppContoroler.getInstance().addToRequestQueue(post_id);

    }
}