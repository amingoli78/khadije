package com.ermile.khadije.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ermile.khadije.R;

public class Main extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mains);
        LinearLayout linearLayout = findViewById(R.id.linearLayout_Main);

        String[] urlLink2 = {"https://khadije.com/static/images/homepage-v3/salavat.jpg","https://khadije.com/static/images/homepage-v3/salavat.jpg"};
        int[] onClickLink2 = {1,1};
        link2(this,linearLayout,urlLink2,onClickLink2);


        link1(this,linearLayout,"https://khadije.com/static/images/homepage-v3/header-logo-en.png",1);

        String[] title = {"title","GO"};
        title_Link(this,linearLayout,title,"https://www.google.com",2);

        String[] urlLink4 = {"https://khadije.com/static/images/homepage-v3/salavat.jpg","https://khadije.com/static/images/homepage-v3/salavat.jpg","https://khadije.com/static/images/homepage-v3/salavat.jpg","https://khadije.com/static/images/homepage-v3/salavat.jpg"};
        int[] onClickLink4 = {1,1,1,1};
        link2(this,linearLayout,urlLink4,onClickLink4);


        title_NoLink(this,linearLayout,"Title No Link",2);





    }






    /* Link */
    @SuppressLint("ResourceType")
    private void link1(Context context, LinearLayout linearParent, String url , int onClick){
        ImageView imageView = new ImageView(context);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(5,3,5,3);
        imageView.setLayoutParams(layoutParams);
        Glide.with(context).load(url).into(imageView);
        imageView.setTag("link1");
        imageView.setId(onClick);
        imageView.setOnClickListener(this);

        linearParent.addView(imageView);
    }

    @SuppressLint("ResourceType")
    private void link2(Context context,LinearLayout linearParent, String[] url,int[] onClick){

        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        LinearLayout.LayoutParams param_linear = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        linearLayout.setLayoutParams(param_linear);

        linearParent.addView(linearLayout);

        for (int i = 0; i < url.length; i++) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.weight = 3 ;
            layoutParams.setMargins(5,3,5,3);

            ImageView imageView = new ImageView(context);

            imageView.setLayoutParams(layoutParams);
            Glide.with(context).load(url[i]).into(imageView);
            imageView.setTag("link1");
            imageView.setId(onClick[i]);
            imageView.setOnClickListener(this);

            linearLayout.addView(imageView);
        }

    }

    @SuppressLint("ResourceType")
    private void link4(Context context,LinearLayout linearParent, String[] url,int[] onClick){

        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        LinearLayout.LayoutParams param_linear = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        linearLayout.setLayoutParams(param_linear);

        linearParent.addView(linearLayout);

        for (int i = 0; i < url.length; i++) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.weight = 3 ;
            layoutParams.setMargins(5,3,5,3);

            ImageView imageView = new ImageView(context);

            imageView.setLayoutParams(layoutParams);
            Glide.with(context).load(url[i]).into(imageView);
            imageView.setTag("link1");
            imageView.setId(onClick[i]);
            imageView.setOnClickListener(this);

            linearLayout.addView(imageView);
        }

    }

    /* Title */
    private void title_Link(Context context,LinearLayout linearParent,String[] title,String url,int onCLick){
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setId(onCLick);
        LinearLayout.LayoutParams param_linear = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        param_linear.leftMargin = 50;
        param_linear.rightMargin = 50;
        param_linear.topMargin = 5;
        param_linear.bottomMargin = 5;
        linearLayout.setLayoutParams(param_linear);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setBackgroundColor(Color.parseColor("#12A5A4A4"));
        linearParent.addView(linearLayout);

        linearLayout.setOnClickListener(this);


        for (int i = 0; i < title.length; i++) {

            TextView textView = new TextView(context);
            textView.setId(onCLick);
            LinearLayout.LayoutParams param_textview = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);

            param_textview.weight =3;
            param_textview.rightMargin = 15;
            param_textview.leftMargin = 15;
            param_textview.topMargin = 5;
            param_textview.bottomMargin = 5;
            textView.setLayoutParams(param_textview);

            textView.setTextSize(15);
            textView.setTextColor(Color.BLACK);
            textView.setText(title[i]);
            textView.setTag(url);

            if (i == 1){
                textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            }

            linearLayout.addView(textView);

            textView.setOnClickListener(this);
        }

    }

    private void title_NoLink(Context context,LinearLayout linearParent,String title,int onCLick){
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setId(onCLick);
        LinearLayout.LayoutParams param_linear = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        param_linear.leftMargin = 50;
        param_linear.rightMargin = 50;
        param_linear.topMargin = 5;
        param_linear.bottomMargin = 5;
        linearLayout.setLayoutParams(param_linear);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setBackgroundColor(Color.parseColor("#12A5A4A4"));
        linearParent.addView(linearLayout);

        linearLayout.setOnClickListener(this);


        TextView textView = new TextView(context);
        textView.setId(onCLick);
        LinearLayout.LayoutParams param_textview = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);

        param_textview.weight =3;
        param_textview.rightMargin = 15;
        param_textview.leftMargin = 15;
        param_textview.topMargin = 5;
        param_textview.bottomMargin = 5;
        textView.setLayoutParams(param_textview);

        textView.setTextSize(15);
        textView.setTextColor(Color.BLACK);
        textView.setText(title);

        linearLayout.addView(textView);

        textView.setOnClickListener(this);

    }






    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case 1:
                Toast.makeText(this, view.getId()+" | "+view.getTag(), Toast.LENGTH_SHORT).show();
                break;
            case 2:
                Toast.makeText(this, "link: "+view.getId()+" | "+view.getTag(), Toast.LENGTH_SHORT).show();
        }

    }
}
