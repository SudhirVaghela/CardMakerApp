package com.example.cardmakerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

public class FullImageView extends AppCompatActivity {

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image_view);
        imageView = findViewById(R.id.fullscrn_imagev);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            int picture = bundle.getInt("image");
            imageView.setImageResource(picture);
        }
    }
}