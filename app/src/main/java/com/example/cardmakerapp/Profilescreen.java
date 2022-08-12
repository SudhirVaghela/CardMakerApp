package com.example.cardmakerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class Profilescreen extends AppCompatActivity {


    ImageView back_arrou_iv;
    Button btn_ur_post,btn_Change_password,btn_edtprofile,btn_policy,btn_signout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilescreen);
        back_arrou_iv = findViewById(R.id.back_arrou_iv);
        btn_Change_password = findViewById(R.id.btn_Change_password);
        btn_edtprofile = findViewById(R.id.btn_edtprofile);

        btn_edtprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Profilescreen.this,EditProfile.class));
                finish();
            }
        });

        btn_Change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Profilescreen.this,PasswordChange.class));
                finish();
            }
        });


        back_arrou_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}