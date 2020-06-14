package com.example.mycommuter.model;

import android.text.TextUtils;
import android.util.Patterns;

import com.google.gson.annotations.SerializedName;

import java.util.regex.Pattern;

public class User {
    @SerializedName("name")
    private String username;
    @SerializedName("email")
    private String emailAddress;
    @SerializedName("password")
    private String password;
    @SerializedName("password_confirmation")
    private String confirmPassword;
    @SerializedName("access_token")
    private String access_token;

    public User(String username, String emailAddress, String password, String confirmPassword) {
        this.setUsername(username);
        this.setEmailAddress(emailAddress);
        this.setPassword(password);
        this.setConfirmPassword(confirmPassword);

    }

    public User(String emailAddress, String password) {

        this.setEmailAddress(emailAddress);
        this.setPassword(password);


    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public String getAccess_token() {
        return access_token;
    }


}
