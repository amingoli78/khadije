package com.ermile.khadije;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class status extends AppCompatActivity {
    GifImageView git_true , gif_false;
    TextView T_title , T_cunt , F_title , F_cunt;
    LinearLayout linearLayout_true , linearLayout_false;

    String status = null;
    String amount = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.status);
        // import SharedPreferences > <Prefs.java>
        final SharedPreferences shared = getSharedPreferences("Prefs", MODE_PRIVATE);
        // import Method for lang > <Prefs.java>
        final Boolean farsi = shared.getBoolean("farsi", false);
        final Boolean arabic = shared.getBoolean("arabic", false);
        final Boolean english = shared.getBoolean("english", false);

        Button back = findViewById(R.id.btn_back_status);
        back.setVisibility(View.VISIBLE);


        linearLayout_true = findViewById(R.id.status_true);
        git_true = findViewById(R.id.paymenT_gif);
        T_title = findViewById(R.id.paymenT_title);
        T_cunt = findViewById(R.id.paymenT_cunt);

        linearLayout_false = findViewById(R.id.status_false);
        gif_false = findViewById(R.id.paymenF_gif);
        F_title = findViewById(R.id.paymenF_title);
        F_cunt = findViewById(R.id.paymenF_cunt);

        // get uri form > Browser
        Uri data = getIntent().getData();
        Uri uri = Uri.parse(String.valueOf(data));
        if (uri.getQueryParameter("status") != null){
            status = uri.getQueryParameter("status");
            amount = uri.getQueryParameter("amount");
        }



        if (status != null){
            if (farsi){
                back.setText("بازگشت");
                T_title.setText("نذرتان قبول");
                T_cunt.setText(" مبلغ " + amount + " با موفقیت پرداخت گردید " );
                F_title.setText("خطا در پرداخت");
                F_cunt.setText(" مبلغ درخواستی " + amount );
            }
            if (arabic){
                back.setText("عودة");
                T_title.setText("تقبل الله");
                T_cunt.setText(" تم دفع مبلغ " + amount + " تومان بنجاح " );
                F_title.setText("خطأ في الدفع");
                F_cunt.setText(" المبلغ المقصود " + amount );
            }
            if (english){
                back.setText("back");
                T_title.setText("Accept");
                T_cunt.setText( "successfully paid Amount " + amount + " Toman  ");
                F_title.setText("Payment error");
                F_cunt.setText(" requested amount " + amount );
            }

            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                    startActivity(new Intent(status.this , MainActivity.class));
                }
            });

            if (status.equals("true")){
                linearLayout_true.setVisibility(View.VISIBLE);
                GifDrawable gifDrawable = null;
                try {
                    gifDrawable = new GifDrawable(getResources(), R.drawable.payment_true);
                    gifDrawable.setLoopCount(1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                git_true.setImageDrawable(gifDrawable);
            }
            else if (status.equals("false")){
                linearLayout_false.setVisibility(View.VISIBLE);
                gif_false.animate()
                        .alpha(1)
                        .setDuration(1000);
                F_title.animate()
                        .alpha(1)
                        .translationY(0)
                        .setDuration(800);
                F_cunt.animate()
                        .alpha(1)
                        .translationY(10)
                        .setDuration(600);

            }else {startActivity(new Intent(this , MainActivity.class));}
        } else {startActivity(new Intent(this , MainActivity.class));}





    }

}

