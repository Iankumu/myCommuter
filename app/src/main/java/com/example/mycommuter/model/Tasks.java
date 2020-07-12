package com.example.mycommuter.model;

import android.text.TextUtils;
import android.util.Patterns;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Tasks {
    @SerializedName("title")
    @Expose

    String title;
    @SerializedName("descritpion")
    @Expose

    String descritpion;
    @SerializedName("created_at")
    @Expose

    String created_at;
    @SerializedName("due")
    @Expose

    String due;


    public Tasks() {
    }

    public Tasks(String title, String descritpion, String due) {
        this.title = title;
        this.descritpion = descritpion;

        this.due = due;
    }




    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescritpion() {
        return descritpion;
    }

    public void setDescritpion(String descritpion) {
        this.descritpion = descritpion;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getDue() {
        return due;
    }

    public void setDue(String due) {
        this.due = due;
    }

    public int isValidTask() {
        if (TextUtils.isEmpty(getTitle()))
            return 0;

        else
            return -1;
    }
}