package com.ermile.khadijeh;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.widget.TextView;
import android.widget.Toast;

public class deviceinfo extends AppCompatActivity {
    // show device info
    public static final int REQUEST_CODE_PHONE_STATE_READ = 100;
    private int checkedPermission = PackageManager.PERMISSION_DENIED;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deviceinfo);



        // show device info
        checkedPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        if (Build.VERSION.SDK_INT >= 23 && checkedPermission != PackageManager.PERMISSION_GRANTED) {
            requestPermission();
        } else
            checkedPermission = PackageManager.PERMISSION_GRANTED;
        TOST();
        // end show device info




    }

    /////////////////////////////////////////////////////////////
    private void requestPermission() {
        Toast.makeText(deviceinfo.this, "بدون دریافت سطح دسترسی!", Toast.LENGTH_SHORT).show();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE},
                    REQUEST_CODE_PHONE_STATE_READ);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case REQUEST_CODE_PHONE_STATE_READ:
                if (grantResults.length > 0 && grantResults[0]== PackageManager.PERMISSION_GRANTED ) {
                    checkedPermission = PackageManager.PERMISSION_GRANTED;
                }
                break;

        }
    }
    public void TOST(){
        SwipeRefreshLayout swipe = findViewById(R.id.Refresh_deviceinfo);
        swipe.setRefreshing(true);

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                finish();
                startActivity(getIntent());
            }
        });

        TextView textView = findViewById(R.id.txtview_deviceinfo);
        if (textView != null){
            swipe.setRefreshing(false);
        }


        textView.setText(
                        "Board : " + Build.BOARD + "\n"+"bot loader:"+ Build.BOOTLOADER +"\n"+
                        "Brand : " + Build.BRAND + "\n"+"host:"+ Build.HOST+"\n"+
                        "DEVICE : " + Build.DEVICE + "\n"+"product:"+ Build.PRODUCT+"\n"+
                        "Display : " + Build.DISPLAY + "\n"+"tag:"+ Build.TAGS+"\n"+
                        "FINGERPRINT : " + Build.FINGERPRINT + "\n"+"type:"+ Build.TYPE+"\n"+
                        "HARDWARE : " + Build.HARDWARE + "\n"+"UNKNOWN:"+ Build.UNKNOWN+"\n"+
                        "ID : " + Build.ID + "\n"+"user:"+ Build.USER+"\n"+
                        "Manufacturer : " + Build.MANUFACTURER + "\n"+"TIME:"+ Build.TIME +"\n"+
                        "MODEL : " + Build.MODEL + "\n"+
                        "SERIAL : " + Build.SERIAL + "\n"+
                        "VERSION : " + Build.VERSION.SDK_INT + "\n"
        );
    }
}