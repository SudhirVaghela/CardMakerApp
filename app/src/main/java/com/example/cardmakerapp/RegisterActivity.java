package com.example.cardmakerapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {


    CircleImageView profile_image, civ_edit;
    TextInputLayout et_fullname, inputlayout, edittext_pass;
    ImageView iv_close;
    Button signup_btn;
    int SELECT_PICTURE = 300;
    Uri selectedimageuri;
    statusmodel model;
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        iv_close = findViewById(R.id.iv_close);
        profile_image = findViewById(R.id.profile_image);
        civ_edit = findViewById(R.id.civ_edit);

        et_fullname = findViewById(R.id.et_fullname);
        inputlayout = findViewById(R.id.inputlayout);
        edittext_pass = findViewById(R.id.edittext_pass);


        signup_btn = findViewById(R.id.btn_signin);

        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registeruser();
            }
        });

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            }
        });

        civ_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageChoose();
            }
        });
    }
    private void imageChoose() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK && data != null) {
            selectedimageuri = data.getData();

            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedimageuri);
                Log.e("image", "image==" + bitmap);
                profile_image.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void registeruser() {
        String fullname = et_fullname.getEditText().getText().toString().trim();
        String email = inputlayout.getEditText().getText().toString().trim();
        String password = edittext_pass.getEditText().getText().toString().trim();

        if (fullname.isEmpty()) {
            et_fullname.requestFocus();
            et_fullname.setError("Please Enter your name !");

            return;
        }
        if (!fullname.isEmpty()&&et_fullname.isErrorEnabled()) {
            et_fullname.setErrorEnabled(false);

            return;
        }

        if (email.isEmpty()) {
            inputlayout.requestFocus();
            inputlayout.setError("Please Enter email !");

            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            inputlayout.requestFocus();
            inputlayout.setError("Enter email in proper formate");

            return;

        }
        if (password.isEmpty()) {
            edittext_pass.requestFocus();
            edittext_pass.setError("Please Enter password ");


            return;
        }
        if (!PASSWORD_PATTERN.matcher(password).matches()) {
            edittext_pass.requestFocus();
            edittext_pass.setError("Enter Proper password");

            return;
        }
        if (password.length() < 8) {
            edittext_pass.requestFocus();
            edittext_pass.setError("Enter password in proper formate");

            return;
        }
// below part is for converting image to multipart

        MultipartBody.Part profileBody = null;
        File photoFile = null;
        try {
            photoFile = getFile1(RegisterActivity.this, selectedimageuri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        RequestBody requestPhotoFile = RequestBody.create(MediaType.parse(getFileMimeTypeFromUri(RegisterActivity.this, selectedimageuri)), photoFile);
        profileBody = MultipartBody.Part.createFormData("image", photoFile.getName(), requestPhotoFile);

// to send data through apicall back method

        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().register(createPartFromString(fullname), createPartFromString(email), createPartFromString(password), profileBody);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String val = new String(response.body().bytes());
                        Log.e("data", "data==" + val);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(RegisterActivity.this,"Registration succeeded..", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // these methods takes file from device and convert it to multipart and send to server

    public static File getFile1(Context context, Uri uri) throws IOException {
        File destinationFilename = new File(context.getFilesDir().getPath() + File.separatorChar + queryName(context, uri));
        try (InputStream ins = context.getContentResolver().openInputStream(uri)) {
            createFileFromStream(ins, destinationFilename);
        } catch (Exception ex) {
            Log.e("Save File", ex.getMessage());
            ex.printStackTrace();
        }
        return destinationFilename;
    }

    public static void createFileFromStream(InputStream ins, File destination) {
        try (OutputStream os = new FileOutputStream(destination)) {
            byte[] buffer = new byte[4096];
            int length;
            while ((length = ins.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
            os.flush();
        } catch (Exception ex) {
            Log.e("Save File", ex.getMessage());
            ex.printStackTrace();
        }
    }

    private static String queryName(Context context, Uri uri) {
        Cursor returnCursor =
                context.getContentResolver().query(uri, null, null, null, null);
        assert returnCursor != null;
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        returnCursor.moveToFirst();
        String name = returnCursor.getString(nameIndex);
        returnCursor.close();
        return name;
    }

    public static String getFileMimeTypeFromUri(Context mContext, Uri fileUri) {
        String result = "";

        if (mContext != null && fileUri != null) {
            try {
                result = mContext.getContentResolver().getType(fileUri);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static RequestBody createPartFromString(String descriptionString) {
        if (descriptionString == null)
            return RequestBody.create(MultipartBody.FORM, "");
        return RequestBody.create(
                MultipartBody.FORM, descriptionString);

    }
}