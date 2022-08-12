package com.example.cardmakerapp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class statusmodel {
    @Expose
    @SerializedName("statusCode")
    private int statusCode;

    @Expose
    @SerializedName("message")
    private String message;

    @SerializedName("data")
    @Expose
    private Data data;

    public Data getData() {
        return data;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }
}
