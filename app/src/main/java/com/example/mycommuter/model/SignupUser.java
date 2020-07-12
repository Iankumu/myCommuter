package com.example.mycommuter.model;

import android.text.TextUtils;
import android.util.Patterns;

import com.google.gson.annotations.SerializedName;

public class SignupUser {
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


    public SignupUser() {
    }

    public SignupUser(String username, String emailAddress, String password, String confirmPassword, String access_token) {
        this.username = username;
        this.emailAddress = emailAddress;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.access_token = access_token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }


    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int isValidSignup() {
        if (TextUtils.isEmpty(getEmailAddress()))
            return 0;
        else if (!Patterns.EMAIL_ADDRESS.matcher(getEmailAddress()).matches())
            return 1;

        else if (getPassword().length() < 8)
            return 2;
        else if (!getConfirmPassword().equals(getPassword()))
            return 3;
        else
            return -1;
    }
}

