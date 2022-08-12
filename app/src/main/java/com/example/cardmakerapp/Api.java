package com.example.cardmakerapp;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface Api {


    String BASE_URL = "http://192.168.0.202:8001/api/";

//    String login_BASE_URL = "http://192.168.0.202:8001/api/";


    @Multipart
    @POST("signup")
    Call<ResponseBody> register(@Part("fullname") RequestBody fullname,
                                @Part("email") RequestBody email,
                                @Part("password") RequestBody password,
                                @Part MultipartBody.Part file);


    @FormUrlEncoded
    @POST("login")
    Call<ResponseBody> loginuser(@Field("email") String email,
                                 @Field("password") String pass);


    @FormUrlEncoded
    @POST("validateEmail")
    Call<ResponseBody> forgetpassword(@Field("email") String email);



    @FormUrlEncoded
    @POST("forgotPassword")
    Call<ResponseBody> changePassword(@Field("email") String email,
                                      @Field("newPassword") String newpass,
                                      @Field("confirmPassword") String confirmpass);

    @FormUrlEncoded
    @POST("changePassword")
    Call<ResponseBody> updatePassword(@Field("id") String id,
                                      @Field("email") String email,
                                      @Field("currentPassword") String currentpass,
                                      @Field("newPassword") String newpass,
                                      @Field("confirmPassword") String confirmpass);

    @Multipart
    @POST("editInfo")
    Call<ResponseBody> editinfo(@Part("id") RequestBody id,
                                @Part("email") RequestBody email,
                                @Part("fullname") RequestBody fullname,
                                @Part("newEmail") RequestBody nemail,
                                @Part MultipartBody.Part file);

}

