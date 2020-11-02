package com.example.league.mobilecodingchallenge.model;

import com.google.gson.annotations.SerializedName;

public class Account {

    @SerializedName("api_key")
    private String mApiKey;

    public String getApiKey() {
        return mApiKey;
    }
}
