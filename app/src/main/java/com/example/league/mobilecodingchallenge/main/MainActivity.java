package com.example.league.mobilecodingchallenge.main;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.league.mobilecodingchallenge.R;
import com.example.league.mobilecodingchallenge.api.OnRequestCallback;
import com.example.league.mobilecodingchallenge.api.Service;
import com.example.league.mobilecodingchallenge.model.Account;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Service.login("Hello", "World", new OnRequestCallback<Account>() {
            @Override
            public void onSuccess(Account response) {
                Log.v(TAG, response.getApiKey());
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.e(TAG, errorMessage);
            }
        });
    }

}
