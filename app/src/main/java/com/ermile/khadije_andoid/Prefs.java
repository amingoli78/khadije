package com.ermile.khadije_andoid;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class Prefs extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        Boolean first = prefs.getBoolean("firstoppen",true);

        String myString = prefs.getString("myStringName", "");
        Boolean byy = prefs.getBoolean("by", false);

        Boolean pf_farsi = prefs.getBoolean("farsi", false);
        Boolean pf_arabic = prefs.getBoolean("arabic", false);
        Boolean pf_english = prefs.getBoolean("english", false);
    }
}
