package com.example.mycommuter.model;

import android.text.TextUtils;
import android.util.Patterns;

public class LoginUser {

    private String EmailAddress;
    private String Password;


    public LoginUser() {
    }

    public LoginUser(String emailAddress, String password) {
        EmailAddress = emailAddress;
        Password = password;
    }

    public String getEmailAddress() {
        return EmailAddress;
    }


    public void setEmailAddress(String emailAddress) {
        EmailAddress = emailAddress;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getPassword() {
        return Password;
    }


    public int isValidlogin() {
        if (TextUtils.isEmpty(getEmailAddress()))
            return 0;
        else if (!Patterns.EMAIL_ADDRESS.matcher(getEmailAddress()).matches())
            return 1;
        else if (TextUtils.isEmpty(getPassword()))
            return 2;
        else if (getPassword().length() < 8)
            return 3;
        else
            return -1;
    }
    public int isEmailValid(){
        if (TextUtils.isEmpty(getEmailAddress()))
            return 0;
        else if (!Patterns.EMAIL_ADDRESS.matcher(getEmailAddress()).matches())
            return 1;
        else
            return -1;
    }
}