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





    Boolean ok_getnotif = false;
    String notif_title = "موسسه حضرت خدیجه";
    String notif_txt_small = "پیام جدید";
    String notif_txt_big = notif_txt_small;
    String notif_txt_from = " ";
    String notif_sub_text = " ";
    String notif_group = String.valueOf(randomNumber);
    String notif_icon = "home";
    String notif_large_icon = "https://khadije.com/static/images/logo.png";
    String notif_on_click = "khadije://";
    PendingIntent onClick_notifs = null;






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
    // Notification send header and user Token > new Notif for me?
    public void post_smile(){
        // import SharedPreferences
        final SharedPreferences sharedPerf_smile = getSharedPreferences("Prefs", MODE_PRIVATE);
        // import manual Token & Code & lang
        final String apikey = sharedPerf_smile.getString("apikey", null);
        final Boolean farsi = sharedPerf_smile.getBoolean("farsi", false);
        final Boolean arabic = sharedPerf_smile.getBoolean("arabic", false);
        final Boolean english = sharedPerf_smile.getBoolean("english", false);

        // set lang
        String url_postSmile = "";
        if (farsi){
            url_postSmile = "https://khadije.com/api/v6/smile";
        }
        if (arabic){
            url_postSmile = "https://khadije.com/ar/api/v6/smile";
        }
        if (english){
            url_postSmile = "https://khadije.com/en/api/v6/smile";
        }

        // Json <Post Smile> Method
        StringRequest PostSmile_Request = new StringRequest(Request.Method.POST, url_postSmile, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject get_postRequest = new JSONObject(response);

                    // Check New Notif
                    Boolean ok_notif = get_postRequest.getBoolean("ok");
                    if (ok_notif){
                        JSONObject result = get_postRequest.getJSONObject("result");
                        Boolean new_notif = result.getBoolean("notif_new");
                        if (new_notif){
                            Notif_is();
                        }
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
                // Send Header
        {
            @Override
            public Map<String, String> getHeaders()  {
                HashMap<String, String> headers_postSmile = new HashMap<>();
                headers_postSmile.put("apikey", apikey);
                return headers_postSmile;
            }
        }
                ;AppContoroler.getInstance().addToRequestQueue(PostSmile_Request);
    }

    // get Notification and run for user > Yes Notif is ..
    public void Notif_is(){
        // import SharedPreferences
        final SharedPreferences shared_notif_is = getSharedPreferences("Prefs", MODE_PRIVATE);
        // import manual Token & Code & lang
        final String apikey = shared_notif_is.getString("apikey", null);
        final Boolean farsi = shared_notif_is.getBoolean("farsi", false);
        final Boolean arabic = shared_notif_is.getBoolean("arabic", false);
        final Boolean english = shared_notif_is.getBoolean("english", false);

        // set lang for get notif
        String url_notif_is = "";
        if (farsi){
            url_notif_is = "https://khadije.com/api/v6/notif";
        }
        if (arabic){
            url_notif_is = "https://khadije.com/ar/api/v6/notif";
        }
        if (english){
            url_notif_is = "https://khadije.com/en/api/v6/notif";
        }
        // Post Method
        StringRequest Notif_is_Request = new StringRequest(Request.Method.POST, url_notif_is, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject mainObject = new JSONObject(response);
                    ok_getnotif = mainObject.getBoolean("ok");
                    if (ok_getnotif){
                        JSONArray get_Notif_is = mainObject.getJSONArray("result");

                        Intent sendURL_about = new Intent(getApplicationContext() , click_on_notif.class);

                        String CHANNEL_ID = "m";
                        final NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext()
                                , CHANNEL_ID);

                        for (int notif_is = 0 ; notif_is <= get_Notif_is.length() ; notif_is++) {
                            JSONObject one = get_Notif_is.getJSONObject(notif_is);
                            // get object from json
                            notif_title = one.getString("title");
                            notif_txt_small = one.getString("excerpt");
                            notif_txt_big = one.getString("text");
                            if (notif_txt_big.equals("") || notif_txt_big.equals("null")){
                                notif_txt_big = notif_txt_small;
                            }else {notif_txt_big = one.getString("text");}
                            notif_txt_from = one.getString("cat");
                            notif_sub_text = one.getString("footer");
                            notif_group = one.getString("cat");
                            notif_icon = one.getString("icon");
                            notif_large_icon = one.getString("image");
                            notif_on_click = one.getString("url");


                            switch (notif_icon) {
                                case "home":
                                    notif_icon = String.valueOf(icon_home);
                                    break;
                                case "hart":
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
                                case "cheek":
                                    notif_icon = String.valueOf(icon_chake);
                                    break;
                                case "close":
                                    notif_icon = String.valueOf(icon_close);
                                    break;
                                default:
                                    notif_icon = String.valueOf(icon_home);
                            }


                            if (notif_on_click.equals("khadije://")) {
                                onClick_notifs = PendingIntent.getActivity(getApplicationContext(), randomNumber, sendURL_about
                                        .putExtra("put_notif", "N_Ihome"), 0);
                            } else {
                                onClick_notifs = PendingIntent.getActivity(getApplicationContext(), randomNumber, sendURL_about
                                        .putExtra("put_notif", "other_website")
                                        .putExtra("url_other_website", notif_on_click), 0);
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
                                        public void onResourceReady(Bitmap resource_LargeIcon, Transition<? super Bitmap>
                                                transition) {
                                            builder.setLargeIcon(resource_LargeIcon)
                                                    .build();
                                        }
                                    });

                            NotificationManagerCompat notificationManager = NotificationManagerCompat.from
                                    (getApplicationContext());
                            notificationManager.notify(1000 + notif_is, builder.build());
                        }
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
                HashMap<String, String> headers_postSmile = new HashMap<>();
                headers_postSmile.put("apikey", apikey);
                return headers_postSmile;
            }
        }
                ;AppContoroler.getInstance().addToRequestQueue(Notif_is_Request);

    }


}