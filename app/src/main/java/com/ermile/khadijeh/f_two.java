package com.ermile.khadijeh;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ermile.khadijeh.network.AppContoroler;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 */
public class f_two extends Fragment {


    public f_two() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View viewtwo = inflater.inflate(R.layout.f_two, container, false);


        final WebView webView = viewtwo.findViewById(R.id.webview_two);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        final SwipeRefreshLayout swipe = viewtwo.findViewById(R.id.swipref_two);
        //------------------------------------------------------------
        swipe.setRefreshing(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                swipe.setRefreshing(false);
            }});
        // JSON
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, "http://mimsg.ir/json_app/app.json", null, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    final String url = response.getString("web");
                    webView.loadUrl(url);

                    swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            webView.loadUrl(url);
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });AppContoroler.getInstance().addToRequestQueue(req);
        // END JSON


        return viewtwo;
    }

}
