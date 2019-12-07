package com.ermile.khadijeapp.utility;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

public class SaveManager extends ContextWrapper {

    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    public static final String SH_PREF_NAME = "ShPerfManager_Payamres";


    @SuppressLint("CommitPrefEdits")
    private SaveManager(Context context) {
        super(context);
        sharedPreferences = getSharedPreferences(SH_PREF_NAME, MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }
    public static SaveManager get(Context context) {
        return new SaveManager(context);
    }


    /** App Info */
    public static final String apiV6_URL = "apiV6_URL";

    public static final String salawatCount = "salawatCount";

    public static final String introIsChacked = "introIsChacked";

    public static final String appLanguage = "appLanguage";
    public static final String changeLanguageByUser = "changeLanguageByUser";

    public static final String hasNewVersion = "hasNewVersion";
    public static final String deprecatedVersion = "deprecatedVersion";

    public static final String apiKey = "apiKey";
    public static final String userCode = "userCode";
    public static final String zoneID = "zoneID";

    public void change_apiV6_URL(String local_URL) {
        editor.putString(apiV6_URL, local_URL);
        editor.apply();
    }

    public void change_salawatCount(int count) {
        editor.putInt(salawatCount, count);
        editor.apply();
    }

    public void change_appLanguage(String Language) {
        editor.putString(appLanguage, Language);
        editor.apply();
    }
    public void change_infoLOGIN(String ApiKey,String UserCode,String ZoneID) {
        editor.putString(apiKey, ApiKey);
        editor.putString(userCode, UserCode);
        editor.putString(zoneID, ZoneID);
        editor.apply();
    }



    public void change_hasNewVersion(Boolean HasNewVersion) {
        editor.putBoolean(hasNewVersion, HasNewVersion);
        editor.apply();
    }
    public void change_deprecatedVersion(Boolean Deprecated_Version) {
        editor.putBoolean(deprecatedVersion, Deprecated_Version);
        editor.apply();
    }

    public void change_intriOpen(Boolean IntroIsChacked) {
        editor.putBoolean(introIsChacked, IntroIsChacked);
        editor.apply();
    }

    public void change_LanguageByUser(Boolean ChangeLanguageByUser) {
        editor.putBoolean(changeLanguageByUser, ChangeLanguageByUser);
        editor.apply();
    }


    public Map<String, Integer> getInt_appINFO() {
        HashMap<String, Integer> hashMap = new HashMap<>();
        hashMap.put(salawatCount, sharedPreferences.getInt(salawatCount, 0 ));
        return hashMap;
    }

    public Map<String, Boolean> getboolen_appINFO() {
        HashMap<String, Boolean> hashMap = new HashMap<>();
        hashMap.put(introIsChacked, sharedPreferences.getBoolean(introIsChacked, false ));
        hashMap.put(hasNewVersion, sharedPreferences.getBoolean(hasNewVersion, false ));
        hashMap.put(deprecatedVersion, sharedPreferences.getBoolean(deprecatedVersion, false ));

        hashMap.put(changeLanguageByUser, sharedPreferences.getBoolean(changeLanguageByUser, true ));
        return hashMap;
    }

    public Map<String, String> getstring_appINFO() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(apiV6_URL, sharedPreferences.getString(apiV6_URL, "https://khadije.com/api/v6" ));

        hashMap.put(appLanguage, sharedPreferences.getString(appLanguage, null ));

        hashMap.put(apiKey, sharedPreferences.getString(apiKey, null ));
        hashMap.put(userCode, sharedPreferences.getString(userCode, null ));
        hashMap.put(zoneID, sharedPreferences.getString(zoneID, null ));
        return hashMap;
    }



}
