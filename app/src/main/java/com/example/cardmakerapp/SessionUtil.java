package com.example.cardmakerapp;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SessionUtil {

    private SharedPreferences preferences;
    private static final String ISLOGIN = "isLogin";
    private static final String FCMTOKEN = "fcmtoken";
    private static final String USERID = "user_id";
    private static final String NAME = "name";
    private static final String EMAIL = "email";
    private static final String PROFILEIMAGE = "profile_image";
    private static final String APITOKEN = "api_token";
    private static final String MOB = "mobile_no";
    private static final String PASS = "password";
    private static final String DELIVERYSERVICETYPE = "delivery_service_type";
    private static final String ISAVAILABLE = "is_available";
    private static final String PHARMACY_LOGISTIC = "pharmacyLogistic";

    public SessionUtil(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void setFCMToken(String token) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(FCMTOKEN, token);
        editor.apply();
    }

    public String getFcmtoken() {
        return preferences.getString(FCMTOKEN, "");
    }

    public void setData(String userId, String email) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(ISLOGIN, true);
        editor.putString(USERID, userId);

        editor.putString(EMAIL, email);

        editor.apply();
    }

    public boolean isLogin() {
        return preferences.getBoolean(ISLOGIN, false);
    }

    public String getUserid() {
        return preferences.getString(USERID, "");
    }

    public String getName() {
        return preferences.getString(NAME, "");
    }

    public void setName(String name) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(NAME, name);
        editor.apply();
    }

    public String getEmail() {
        return preferences.getString(EMAIL, "");
    }

    public void setEmail(String email) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(EMAIL, email);
        editor.apply();
    }

    public String getProfileImage() {
        return preferences.getString(PROFILEIMAGE, "");
    }

    public void setProfileimage(String profileimage) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PROFILEIMAGE, profileimage);
        editor.apply();
    }

    public String getApiToken() {
        return preferences.getString(APITOKEN, "");
    }

    public String getMob() {
        return preferences.getString(MOB, "");
    }


    public String getPass() {
        return preferences.getString(PASS, "");
    }

    public String getDeliveryservicetype() {
        return preferences.getString(DELIVERYSERVICETYPE, "");
    }

    public void setIsavailable(boolean isavailable) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(ISAVAILABLE, isavailable);
        editor.apply();
    }

    public boolean isAvailable() {
        return preferences.getBoolean(ISAVAILABLE, false);
    }

    public String getPharmacyLogistic(){
        return preferences.getString(PHARMACY_LOGISTIC, "");
    }

    public void logOut() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }
}
