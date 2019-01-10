package com.ermile.khadije_andoid;


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
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.ermile.khadije_andoid.network.AppContoroler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Intro extends AppCompatActivity {

    public Handler mHandler;
    public boolean continue_or_stop;

    ViewpagersAdapter PagerAdapter;  // for View page
    ViewPager viewpager; //  for dots & Button in XML
    private LinearLayout dotsLayout; // dots in XML
    private TextView[] dots; // for add dots
    public int count = 4; // Slide number
    private Button btnSkip, btnNext ; // Button in XML
    private first_oppen prefManager; // Checking for first time launch


    /**
     * onCreate Method
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new NetCheck().execute();
        // Checking for first time launch - before calling setContentView
        prefManager = new first_oppen(this);
        if (!prefManager.isFirstTimeLaunch()) {
            launchHomeScreen();
            finish();
        }


        // XML
        setContentView(R.layout.intro);
        // Chang ID XML
        dotsLayout = findViewById(R.id.layoutDots); // OOOO
        btnNext = findViewById(R.id.btn_next); //  NEXT
        btnSkip = findViewById(R.id.btn_skip); //  SKIP
        viewpager = findViewById(R.id.view_pagers); // view page in XML
        // set
        PagerAdapter = new ViewpagersAdapter(this); // add Adapter (in line 55)
        viewpager.setAdapter(PagerAdapter); // set Adapter to View pager in XML
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(final int position) {
                addBottomDots(position);
                // changing the next button text 'NEXT' / 'GOT IT'
                final SharedPreferences shared = getSharedPreferences("Prefs", MODE_PRIVATE);
                final SharedPreferences.Editor editor = shared.edit();
                final Boolean farsi = shared.getBoolean("farsi", false);
                final Boolean arabic = shared.getBoolean("arabic", false);
                final Boolean english = shared.getBoolean("english", false);
                String url = "";
                if (farsi){ url = "https://khadije.com/api/v5/android"; }
                if (arabic){ url = "https://khadije.com/ar/api/v5/android" ; }
                if (english){ url = "https://khadije.com/en/api/v5/android"; }
                // JSON Methods
                JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url , null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // get objects
                            JSONObject intro = response.getJSONObject("intro");
                            JSONObject btn = intro.getJSONObject("btn");
                            String btn_next = btn.getString("next");
                            String btn_enter = btn.getString("enter");

                            if (position == count - 1) {
                                // last page. make button text to GOT IT
                                btnNext.setText(btn_enter);
                                btnSkip.setVisibility(View.GONE);
                            } else {
                                // still pages are left
                                btnNext.setText(btn_next);
                                btnSkip.setVisibility(View.VISIBLE);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {  // Error NET
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });AppContoroler.getInstance().addToRequestQueue(req);
                // END JSON

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        }); // for dots next page
        // adding bottom dots
        addBottomDots(0);
        // making notification bar transparent
        changeStatusBarColor();
        // set On Click Listener
        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchHomeScreen();
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // checking for last page
                // if last page home screen will be launched
                int current = getItem(+1);
                if (current < count ) {
                    // move to next screen
                    viewpager.setCurrentItem(current);
                } else {
                    launchHomeScreen();
                }
            }
        });



    }

    /**
     * Change Number page & Dots
     */
    private void addBottomDots(int currentPage) {

        dots = new TextView[count];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }
    private int getItem(int i) {
        return viewpager.getCurrentItem() + i;
    }

    // launch Home = go to new activity
    private void launchHomeScreen() {
        prefManager.setFirstTimeLaunch(false);
        startActivity(new Intent(Intro.this , MainActivity.class) );
        finish();
    }

    /**
     * Making notification bar transparent
     */
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    /**
     * This Moder Adapter
     * View Pager Adapter
     */
    public class ViewpagersAdapter extends PagerAdapter {

        private Context context;
        private LayoutInflater inflater;
        ViewpagersAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount()
        {
            return count;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, final int position) {
            // Static Methods
            inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            final View view = inflater.inflate(R.layout.intro_item , container , false);
            // My item for view slide in XML
            final TextView title = view.findViewById(R.id.txv_title);
            final TextView des = view.findViewById(R.id.txv_des);
            final LinearLayout color_bg = view.findViewById(R.id.background_slide);
            final ImageView imgview = view.findViewById(R.id.img_slide);


            final ProgressBar progress_slide = view.findViewById(R.id.progress_slide);

            if (imgview != null){
                progress_slide.setVisibility(View.GONE);
            }


            final SharedPreferences shared = getSharedPreferences("Prefs", MODE_PRIVATE);
            final SharedPreferences.Editor editor = shared.edit();

            final Boolean farsi = shared.getBoolean("farsi", false);
            final Boolean arabic = shared.getBoolean("arabic", false);
            final Boolean english = shared.getBoolean("english", false);


            String url = "";

            if (farsi){
                url = "https://khadije.com/api/v5/android";
            }
            if (arabic){
                url = "https://khadije.com/ar/api/v5/android" ;
            }
            if (english){
                url = "https://khadije.com/en/api/v5/android";
            }
            // JSON Methods
            JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url , null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        // get objects
                        JSONObject intro = response.getJSONObject("intro");
                        JSONObject btn = intro.getJSONObject("btn");
                        String btn_skip = btn.getString("back");
                        String btn_next = btn.getString("next");
                        btnSkip.setText(btn_skip);
                        btnNext.setText(btn_next);

                        JSONArray intro_slide = intro.getJSONArray("slide");
                        int countAll = intro_slide.length();
                        int array        = countAll - 1;
                        String[] intro_title      = new String[countAll];
                        String[] title_color      = new String[countAll];
                        String[] intro_desc       = new String[countAll];
                        String[] desc_color       = new String[countAll];
                        String[] intro_background = new String[countAll];
                        String[] intro_image      = new String[countAll];
                        for(int i = 0; i <= array; i++)
                        {
                            JSONObject temp_intro = intro_slide.getJSONObject(i);
                            intro_title[i]        =  temp_intro.getString("title");
                            title_color[i]        =  temp_intro.getString("title_color");
                            intro_desc[i]         =  temp_intro.getString("desc");
                            desc_color[i]         =  temp_intro.getString("desc_color");
                            intro_background[i]   =  temp_intro.getString("background");
                            intro_image[i]        =  temp_intro.getString("image");
                        }
                        // Title
                        title.setText(intro_title[position]);
                        title.setTextColor(Color.parseColor(title_color[position]));
                        // Description
                        des.setText(intro_desc[position]);
                        des.setTextColor(Color.parseColor(desc_color[position]));
                        // Color Background
                        color_bg.setBackgroundColor(Color.parseColor(intro_background[position]));
                        // Image
                        Glide.with(context).load(intro_image[position]).into(imgview);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {  // Error NET
                @Override
                public void onErrorResponse(VolleyError error) {
                    progress_slide.setVisibility(View.VISIBLE);
                    title.setVisibility(View.GONE);
                    des.setVisibility(View.GONE);
                    imgview.setVisibility(View.GONE);
                }
            });AppContoroler.getInstance().addToRequestQueue(req);
            // END JSON
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView( (LinearLayout) object);
        }
    }

    /**
     * Async Task to check whether internet connection is working.
     **/

    private class NetCheck extends AsyncTask<String,String,Boolean>
    {

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
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

            if(th == true){
            }
            else{
                View parentLayout = findViewById(android.R.id.content);
                Snackbar snackbar = Snackbar.make(parentLayout, "به اینترنت متصل شوید", Snackbar.LENGTH_INDEFINITE).setDuration(999999999)
                        .setAction("تلاش مجدد", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                new Intro.NetCheck().execute();
                                finish();
                                startActivity(getIntent());
                            }
                        });
                snackbar.setActionTextColor(Color.RED);
                View sbView = snackbar.getView();
                TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(Color.YELLOW);
                snackbar.show();

            }
        }
    }

}
