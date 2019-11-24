package com.ermile.khadijeapp.api;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ermile.khadijeapp.Static.format;
import com.ermile.khadijeapp.Static.statics;
import com.ermile.khadijeapp.Static.url;
import com.ermile.khadijeapp.utility.FileManager;
import com.ermile.khadijeapp.utility.Network;
import com.ermile.khadijeapp.utility.SaveManager;

public class GetAndroidDetail {

    public static void GetJson(final Context context, final JsonLocalListener jsonLocalListener){
        String AppLanguage = SaveManager.get(context).getstring_appINFO().get(SaveManager.appLanguage);
        String urlApp = SaveManager.get(context).getstring_appINFO().get(SaveManager.apiV6_URL)+ url.app;

        StringRequest get_local = new StringRequest(Request.Method.GET,urlApp , new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                jsonLocalListener.onGetJson_Online(response);

            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                String AppLanguage = SaveManager.get(context).getstring_appINFO().get(SaveManager.appLanguage);
                String valueJson = FileManager.read_FromAsset(context,AppLanguage, format.json, statics.UTF8);
                jsonLocalListener.onGetJson_Offline(valueJson);
            }
        });
        get_local.setRetryPolicy(new DefaultRetryPolicy(5 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Network.getInstance().addToRequestQueue(get_local);

    }
    public interface JsonLocalListener {
        void onGetJson_Online(String ResponeOnline);
        void onGetJson_Offline(String ResponeOffline);

    }


}
