package com.example.league.mobilecodingchallenge.api;


import com.example.league.mobilecodingchallenge.model.Account;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface Api {

    @GET("login")
    Call<Account> login(@Header("Authorization") String credentials);

}
