package com.example.mycommuter.interfaces;

public interface LoginResultCallback {
    void onSuccess(String message, String token);

    void onError(String message);
}
