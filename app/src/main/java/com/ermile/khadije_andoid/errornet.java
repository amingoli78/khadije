package com.ermile.khadije_andoid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class errornet extends AppCompatActivity {

    TextView title_one,chakeDevice,reload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.errornet);

        title_one = findViewById(R.id.no_net_title);
        chakeDevice = findViewById(R.id.link_setting_net);
        reload = findViewById(R.id.reload_net);

        chakeDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
            }
        });

        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(errornet.this,MainActivity.class));
                finish();
            }
        });

        // save  BY Shared Preferences
        final SharedPreferences shared = getSharedPreferences("Prefs", MODE_PRIVATE);
        final SharedPreferences.Editor editor = shared.edit();

        final Boolean farsi = shared.getBoolean("farsi", false);
        final Boolean arabic = shared.getBoolean("arabic", false);
        final Boolean english = shared.getBoolean("english", false);


        if (arabic) {
            title_one.setText("خطأ في اتصال الإنترنت!");
            chakeDevice.setText("تحقق من إعدادات الشبكة");
            reload.setText("حاول مرة أخرى");
        }
        if (english) {
            title_one.setText("Internet connection error!");
            chakeDevice.setText("Check network settings");
            reload.setText("Try again");
        }



    }
}
