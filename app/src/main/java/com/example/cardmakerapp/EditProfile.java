package com.example.cardmakerapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
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

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfile extends AppCompatActivity {

    ImageView iv_back;
    CircleImageView editprofile_image, civ_editprofile;
    TextInputLayout et_name, et_newmail;
    Button btn_update;

    Uri selectedimageuri;
    int SELECT_PICTURE = 200;
    SessionUtil sessionUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        iv_back = findViewById(R.id.iv_back);
        editprofile_image = findViewById(R.id.editprofile_image);
        civ_editprofile = findViewById(R.id.civ_editprofile);
        et_name = findViewById(R.id.et_name);
        et_newmail = findViewById(R.id.et_newmail);
        btn_update = findViewById(R.id.btn_update);
        sessionUtil = new SessionUtil(EditProfile.this);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        civ_editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageChoose();
            }
        });

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateinfo();
            }
        });
    }

    private void imageChoose() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK && data != null) {
            selectedimageuri = data.getData();

            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedimageuri);
                Log.e("image", "image==" + bitmap);
                editprofile_image.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void updateinfo() {

        String fullname = et_name.getEditText().getText().toString();
        String nemail = et_newmail.getEditText().getText().toString();

        String id = sessionUtil.getUserid();
        String email = sessionUtil.getEmail();
//        Log.e("id","id=="+id+email);

        if (fullname.isEmpty()) {
            et_name.requestFocus();
            et_name.setError("Please Enter your name !");

            return;
        }

        if (email.isEmpty()) {
            et_newmail.requestFocus();
            et_newmail.setError("Please Enter email !");

            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            et_newmail.requestFocus();
            et_newmail.setError("Enter email in proper formate");

            return;
        }

        MultipartBody.Part profileBody = null;
        File photoFile = null;
        try {
            photoFile = getFile1(EditProfile.this, selectedimageuri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        RequestBody requestPhotoFile = RequestBody.create(MediaType.parse(getFileMimeTypeFromUri(EditProfile.this, selectedimageuri)), photoFile);
        profileBody = MultipartBody.Part.createFormData("image", photoFile.getName(), requestPhotoFile);

        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().editinfo(createPartFromString(id), createPartFromString(email), createPartFromString(fullname), createPartFromString(nemail), profileBody);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String val = new String(response.body().bytes());
                        Log.e("data", "data==" + val);
                        Toast.makeText(EditProfile.this, "Profile Edited Successfully..", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(EditProfile.this, Profilescreen.class));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(EditProfile.this, "Profile does not edited..", Toast.LENGTH_SHORT).show();
            }
        });
    }

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
