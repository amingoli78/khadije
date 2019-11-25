package com.ermile.khadijeapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.ermile.khadijeapp.R;
import com.github.chrisbanes.photoview.PhotoView;

public class ImageViewer extends AppCompatActivity {
    PhotoView photoViewer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);
        photoViewer = findViewById(R.id.photoViewer);

        final String image = getIntent().getStringExtra("image");
        Glide.with(this).load(image).into(photoViewer);
    }
}