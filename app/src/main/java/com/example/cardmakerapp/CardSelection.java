package com.example.cardmakerapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class CardSelection extends AppCompatActivity {


    RelativeLayout rl_profile;
    CardView cardView;
    ImageView imageView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_selection);
        cardView = findViewById(R.id.cardview);
        imageView1 = findViewById(R.id.imageview1);
        rl_profile = findViewById(R.id.rl_profile);

        rl_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CardSelection.this,Profilescreen.class));
                finish();
            }
        });

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CardSelection.this,EditCardActivity.class));
                finish();
            }
        });
    }
}