package com.example.mycommuter.interfaces;

public interface LoginResultCallback {
    void onSuccess(String message, String token,String email);

    void onError(String message);
}
