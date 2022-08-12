package com.example.cardmakerapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {


    TextInputLayout edittext_pass, edittxt_email;
    TextView tv_new_account, tv_forgetpass;
    Button login_btn;
    SessionUtil sessionUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edittxt_email = findViewById(R.id.edittxt_email);
        edittext_pass = findViewById(R.id.edittext_pass);
        login_btn = findViewById(R.id.login_btn);
        tv_forgetpass = findViewById(R.id.tv_forgetpass);
        tv_new_account = findViewById(R.id.tv_new_account);
        sessionUtil = new SessionUtil(MainActivity.this);


        tv_forgetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ForgetPassword.class));
                finish();
            }
        });
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userlogin();
            }
        });
        tv_new_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
                finish();
            }
        });
    }


    private void userlogin() {

        String mail = edittxt_email.getEditText().getText().toString();
        String pass = edittext_pass.getEditText().getText().toString();


        if (mail.isEmpty()) {
            edittxt_email.requestFocus();
            edittxt_email.setError("Please Enter email !");
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
            edittxt_email.requestFocus();
            edittxt_email.setError("Enter email in proper formate");
            return;
        }

        if (pass.isEmpty()) {
            edittext_pass.requestFocus();
            edittext_pass.setError("Please Enter password ");
            return;
        }
        if (pass.length() < 8) {
            edittext_pass.requestFocus();
            edittext_pass.setError("Enter password in proper formate");
            return;
        }

        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().loginuser(mail, pass);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String val = new String(response.body().bytes());
                        Log.e("data", "data==" + val);
                        Gson gson = new Gson();
                        statusmodel model = gson.fromJson(val, statusmodel.class);

                        if (model.getStatusCode() == 200) {
                            sessionUtil.setData(model.getData().getUserId(), model.getData().getEmail());
                            Toast.makeText(MainActivity.this, model.getMessage(), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(MainActivity.this, CardSelection.class));

                        } else if (model.getStatusCode() == 404) {
                            Toast.makeText(MainActivity.this, model.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(MainActivity.this, "failure", Toast.LENGTH_SHORT).show();
            }
        });
    }
}