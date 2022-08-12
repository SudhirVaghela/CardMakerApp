package com.example.cardmakerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgetPassword extends AppCompatActivity {

    ImageView closeicon;
    TextInputLayout et_email;
    Button btn_send;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        closeicon = findViewById(R.id.closeicon);
        et_email = findViewById(R.id.et_email);
        btn_send = findViewById(R.id.btn_send);

        closeicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                validateEmail();
//                startActivity(new Intent(ForgetPassword.this,PasswordChange.class));

            }
        });
    }

    private void validateEmail() {

        String mail = et_email.getEditText().getText().toString();

        if (mail.isEmpty()) {
            et_email.requestFocus();
            et_email.setError("Please Enter email !");

            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
            et_email.requestFocus();
            et_email.setError("Enter email in proper formate");

            return;
        }
        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().forgetpassword(mail);

        call.enqueue(new Callback<ResponseBody>() {


            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){

                    try {
                        String val = new String(response.body().bytes());
                        Log.e("message","message=="+val);
                        Gson gson = new Gson();
                        statusmodel model = gson.fromJson(val, statusmodel.class);

                        if(model.getStatusCode()==200){
                            startActivity(new Intent(ForgetPassword.this,SetNewPassword.class));
                            finish();
                        }else{
                            Toast.makeText(ForgetPassword.this,model.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException  e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(ForgetPassword.this,"failure",Toast.LENGTH_SHORT).show();
            }
        });
    }
}