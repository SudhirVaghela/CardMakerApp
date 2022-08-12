package com.example.cardmakerapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

public class PasswordChange extends AppCompatActivity {

    TextInputLayout et_currentpass, et_newpass, et_confirmpass;
    Button btn_updatepass;
    ImageView back_arrow_iv;

    SessionUtil sessionUtil;

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_change);
        et_currentpass = findViewById(R.id.et_currentpass);
        et_newpass = findViewById(R.id.et_newpass);
        et_confirmpass = findViewById(R.id.et_confirmpass);
        back_arrow_iv = findViewById(R.id.back_arrow_iv);
        sessionUtil = new SessionUtil(PasswordChange.this);


        back_arrow_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btn_updatepass = findViewById(R.id.btn_updatepass);
        btn_updatepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatepassword();
            }
        });
    }


    private void updatepassword() {

        String currentpass = et_currentpass.getEditText().getText().toString().trim();
        String newpass = et_newpass.getEditText().getText().toString().trim();
        String confirmpass = et_confirmpass.getEditText().getText().toString().trim();

        String id = sessionUtil.getUserid();
        String mail = sessionUtil.getEmail();
        Log.e("id", "id==" + id + mail);

        if (currentpass.isEmpty()) {
            et_currentpass.requestFocus();
            et_currentpass.setError("Field cant`t be empty !");
            return;
        }
        if (!PASSWORD_PATTERN.matcher(newpass).matches()) {
            et_newpass.requestFocus();
            et_newpass.setError("Enter proper password");
            return;
        }
        if (newpass.isEmpty()) {
            et_newpass.requestFocus();
            et_newpass.setError("Field can`t be empty..");
            return;
        }
        if (!PASSWORD_PATTERN.matcher(newpass).matches()) {
            et_newpass.requestFocus();
            et_newpass.setError("Enter proper password");
            return;
        }
        if (confirmpass.isEmpty()) {
            et_confirmpass.requestFocus();
            et_confirmpass.setError("Field cant be empty...");
            return;
        }
        if (!PASSWORD_PATTERN.matcher(confirmpass).matches()) {
            et_confirmpass.requestFocus();
            et_confirmpass.setError("Enter proper password..");
            return;
        }
        if (!newpass.equals(confirmpass)) {
            et_confirmpass.requestFocus();
            et_confirmpass.setError("Password are not matched..");
            return;
        }

        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().updatePassword(id, mail, currentpass, newpass, confirmpass);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful()) {
                    try {
                        String val = new String(response.body().bytes());
                        Toast.makeText(PasswordChange.this, " password change successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(PasswordChange.this, MainActivity.class));
                        finish();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }
}