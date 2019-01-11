package com.ermile.khadije_andoid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Setting extends AppCompatActivity {

    TextView change_lang;
    Button fa,ar,en,us;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
        // save  BY Shared Preferences
        final SharedPreferences shared = getSharedPreferences("Prefs", MODE_PRIVATE);
        final SharedPreferences.Editor editor = shared.edit();

        change_lang = findViewById(R.id.change_setting);
        fa = findViewById(R.id.farsi_setting);
        ar = findViewById(R.id.arabic_setting);
        en = findViewById(R.id.english_setting);
        us = findViewById(R.id.about_setting);

        final Boolean farsi = shared.getBoolean("farsi", false);
        final Boolean arabic = shared.getBoolean("arabic", false);
        final Boolean english = shared.getBoolean("english", false);


        if (farsi) {
            fa.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.lang_changed));
        }
        if (arabic) {
            ar.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.lang_changed));
        }
        if (english) {
            en.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.lang_changed));
        }




        fa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putBoolean("farsi",true);
                editor.putBoolean("arabic",false);
                editor.putBoolean("english",false);
                editor.apply();
                Toast.makeText(Setting.this, " برنامه را مجددا اجرا کنید.", Toast.LENGTH_SHORT).show();
                EXIT();
            }
        });
        ar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putBoolean("arabic",true);
                editor.putBoolean("farsi",false);
                editor.putBoolean("english",false);
                editor.apply();
                Toast.makeText(Setting.this, "تشغيل البرنامج مرة أخرى.", Toast.LENGTH_SHORT).show();
                EXIT();

            }
        });
        en.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putBoolean("english",true);
                editor.putBoolean("farsi",false);
                editor.putBoolean("arabic",false);
                editor.apply();
                Toast.makeText(Setting.this, "run app again.", Toast.LENGTH_SHORT).show();
                EXIT();
            }
        });
        us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Setting.this, about_us.class));
            }
        });

    }

    public void EXIT(){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("EXIT", true);
        startActivity(intent);
    }
}
