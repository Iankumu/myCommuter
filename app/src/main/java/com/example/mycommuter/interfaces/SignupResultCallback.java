package com.example.mycommuter.interfaces;

public interface SignupResultCallback {
    void onSuccess(String message, String token);

    void onError(String message);
}
