package com.example.cardmakerapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SetNewPassword extends AppCompatActivity {

    TextInputLayout et_newpassword, et_confirmpassword, et_mail;
    ImageView close_iv;
    Button btn_setpass;
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_new_password);

        et_mail = findViewById(R.id.et_mail);
        et_newpassword = findViewById(R.id.et_newpassword);
        et_confirmpassword = findViewById(R.id.et_confirmpassword);

        close_iv = findViewById(R.id.close_iv);
        btn_setpass = findViewById(R.id.btn_setpass);

        close_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btn_setpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatePassword();
            }
        });
    }

    private void updatePassword() {
        String email = et_mail.getEditText().getText().toString().trim();
        String newpass = et_newpassword.getEditText().getText().toString().trim();
        String confirmpass = et_confirmpassword.getEditText().getText().toString().trim();


        if (email.isEmpty()) {
            et_mail.requestFocus();
            et_mail.setError("Please Enter email !");

            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            et_mail.requestFocus();
            et_mail.setError("Enter email in proper formate");

            return;

        }

        if (newpass.isEmpty()) {
            et_newpassword.requestFocus();
            et_newpassword.setError("Please Enter password ");

            return;
        }
        if (!PASSWORD_PATTERN.matcher(newpass).matches()) {
            et_newpassword.requestFocus();
            et_newpassword.setError("Enter Proper password");

            return;
        }
        if (newpass.length() < 8) {
            et_newpassword.requestFocus();
            et_newpassword.setError("Enter password in proper formate");

            return;
        }

        if (!newpass.equals(confirmpass)) {
            et_confirmpassword.requestFocus();
            et_confirmpassword.setError("pasword is not matched");

            return;
        }

        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().changePassword(email, newpass, confirmpass);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful()) {
                    try {
                        String val = new String(response.body().bytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(SetNewPassword.this, " password change successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SetNewPassword.this, MainActivity.class));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(SetNewPassword.this, " password does not change.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}