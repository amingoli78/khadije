package com.ermile.khadije;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.View;

public class Prefs extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        final Boolean first = prefs.getBoolean("firstoppen",true);

        String myTokeng = prefs.getString("myTokengName", "");
        String myTokeng_code = prefs.getString("myTokengName_code", "");
        Boolean token_sending = prefs.getBoolean("token_sending", false);

        Boolean pf_farsi = prefs.getBoolean("farsi", false);
        Boolean pf_arabic = prefs.getBoolean("arabic", false);
        Boolean pf_english = prefs.getBoolean("english", false);

        String url = prefs.getString("url", "https://khadije.com");

        //get Token
        String token = prefs.getString("token", null);

        //add user
        Boolean login_one = prefs.getBoolean("login_one", false);
        String usercode = prefs.getString("usercode", null);
        String zoneid = prefs.getString("zoneid", null);
        String apikey = prefs.getString("apikey", null);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        }, 400);
    }
}
