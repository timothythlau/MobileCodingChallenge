package com.example.league.mobilecodingchallenge.api;

public interface OnRequestCallback<T> {
    void onSuccess(T response);

    void onFailure(String errorMessage);
}
