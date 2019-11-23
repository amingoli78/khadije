package com.ermile.khadijeapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.ermile.khadijeapp.R;
import com.ermile.khadijeapp.utility.set_language_device;

public class Video_View extends AppCompatActivity {


    @Override
    protected void onResume() {
        new set_language_device(this);
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_view);
    }
}
