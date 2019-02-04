package com.ermile.khadije;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.ermile.khadije.network.AppContoroler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class NotificationService extends Service {

    PendingIntent onClick_notif, Button_onclick_notif , onclick_notifUpdate_close;
    int randomNumber = new Random().nextInt(976431 ) + 20;

    int icon_home = R.drawable.ic_home;
    int icon_hert = R.drawable.ic_delneveshte ;
    int icon_setting = R.drawable.ic_seting ;
    int icon_moreVert = R.drawable.ic_more_vert ;
    int icon_about = R.drawable.ic_abut_us ;
    int icon_contact = R.drawable.ic_call_us ;
    int icon_vision = R.drawable.ic_future_view ;
    int icon_mission = R.drawable.ic_mission ;
    int icon_website = R.drawable.ic_website ;
    int icon_net_setting = R.drawable.ic_setting_net ;
    int icon_chake = R.drawable.ic_chake;
    int icon_close = R.drawable.ic_close;

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
                        post_notif();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
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

                    JSONArray get_Notif_is = new JSONArray(response);

                    Intent sendURL_about = new Intent(getApplicationContext() , click_on_notif.class);
                    Intent close_notif = new Intent("com.ermile.khadije.cancel");

                    onClick_notif = null;
                    Button_onclick_notif = null;
                    onclick_notifUpdate_close = PendingIntent.getBroadcast(getApplicationContext(), (int) System.currentTimeMillis(), close_notif, PendingIntent.FLAG_UPDATE_CURRENT);

                    String CHANNEL_ID = "m";
                    final NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext() , CHANNEL_ID);

                    for (int notif_is = 0 ; notif_is <= get_Notif_is.length() ; notif_is++){
                        JSONObject one = get_Notif_is.getJSONObject(notif_is);
                        // get object from json
                        String notif_title = one.getString("title");
                        String notif_txt_small = one.getString("small");
                        String notif_txt_big = one.getString("big");
                        if (notif_txt_big.equals("null")){ notif_txt_big = notif_txt_small;}
                        String notif_txt_from = one.getString("from");
                        String notif_sub_text = one.getString("sub_text");
                        String notif_group = one.getString("group");
                        String notif_icon = one.getString("small_icon");
                        String notif_large_icon = one.getString("large_icon");
                        String notif_on_click = one.getString("on_click");
                        String notif_otherBrowser_link = one.getString("link");
                        Boolean notif_otherBrowser_inApp = one.getBoolean("external");


                        switch (notif_icon){
                            case "home":
                                notif_icon = String.valueOf(icon_home);
                                break;
                            case "hert":
                                notif_icon = String.valueOf(icon_hert);
                                break;
                            case "setting":
                                notif_icon = String.valueOf(icon_setting);
                                break;
                            case "more_vert":
                                notif_icon = String.valueOf(icon_moreVert);
                                break;
                            case "about":
                                notif_icon = String.valueOf(icon_about);
                                break;
                            case "contact":
                                notif_icon = String.valueOf(icon_contact);
                                break;
                            case "vision":
                                notif_icon = String.valueOf(icon_vision);
                                break;
                            case "mission":
                                notif_icon = String.valueOf(icon_mission);
                                break;
                            case "website":
                                notif_icon = String.valueOf(icon_website);
                                break;
                            case "net-setting":
                                notif_icon = String.valueOf(icon_net_setting);
                                break;
                            case "chake":
                                notif_icon = String.valueOf(icon_chake);
                                break;
                            case "close":
                                notif_icon = String.valueOf(icon_close);
                                break;
                        }


                        switch (notif_on_click){
                            case "home":
                                onClick_notif = PendingIntent.getActivity(getApplicationContext() , randomNumber , sendURL_about
                                        .putExtra("put_notif","N_Ihome") , 0);
                                break;
                            case "about":
                                onClick_notif = PendingIntent.getActivity(getApplicationContext() , randomNumber , sendURL_about
                                        .putExtra("put_notif","N_about") , 0);
                                break;
                            case "call":
                                onClick_notif = PendingIntent.getActivity(getApplicationContext() , randomNumber , sendURL_about
                                        .putExtra("put_notif","N_call") , 0);
                                break;
                            case "vision":
                                onClick_notif = PendingIntent.getActivity(getApplicationContext() , randomNumber, sendURL_about
                                        .putExtra("put_notif","N_futrue") , 0);
                                break;
                            case "mission":
                                onClick_notif = PendingIntent.getActivity(getApplicationContext() , randomNumber , sendURL_about
                                        .putExtra("put_notif","N_mission") , 0);
                                break;
                            case "website":
                                onClick_notif = PendingIntent.getActivity(getApplicationContext() , randomNumber , sendURL_about
                                        .putExtra("put_notif","website") , 0);
                                break;
                            case "other_website":
                                onClick_notif = PendingIntent.getActivity(getApplicationContext(), randomNumber , sendURL_about
                                        .putExtra("put_notif", "other_website")
                                        .putExtra("url_other_website", notif_otherBrowser_link)
                                        .putExtra("notif_otherBrowser_inApp" , notif_otherBrowser_inApp ), 0);
                                break;
                            case "close":
                                onClick_notif = PendingIntent.getBroadcast(getApplicationContext(), (int)
                                        System.currentTimeMillis(), close_notif, PendingIntent.FLAG_UPDATE_CURRENT);
                                break;
                        }

                        builder.setSmallIcon(Integer.parseInt(notif_icon))
                                .setContentTitle(notif_title)
                                .setContentText(notif_txt_small)
                                .setStyle(new NotificationCompat
                                        .BigTextStyle()
                                        .bigText(notif_txt_big))
                                .setSubText(notif_sub_text)
                                .setGroup(notif_group)
                                .setContentInfo(notif_txt_from)
                                .setContentIntent(onClick_notif)
                                .setWhen(System.currentTimeMillis())
                                .setAutoCancel(true)
                                .setDefaults(Notification.DEFAULT_ALL)
                                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                                .build();

                        Glide.with(getApplicationContext())
                                .asBitmap()
                                .load(notif_large_icon)
                                .into(new SimpleTarget<Bitmap>() {
                                    @Override
                                    public void onResourceReady(Bitmap resource_LargeIcon, Transition<? super Bitmap> transition) {
                                        builder.setLargeIcon(resource_LargeIcon)
                                                .build();
                                    }
                                });

                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
                        notificationManager.notify(1000 + notif_is , builder.build());
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
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