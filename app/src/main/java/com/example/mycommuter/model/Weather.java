package com.example.mycommuter.model;

import com.google.gson.annotations.SerializedName;

public class Weather {
    @SerializedName("date")
    private String date;
    @SerializedName("city")
    private String city;
    @SerializedName("temp")
    private String temp;
    @SerializedName("feels_like")
    private String feels_like;
    @SerializedName("main")
    private String main;
    @SerializedName("description")
    private String description;

    public Weather() {
    }

    public Weather(String date, String city, String temp, String feels_like, String main, String description) {
        this.date = date;
        this.city = city;
        this.temp = temp;
        this.feels_like = feels_like;
        this.main = main;
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getFeels_like() {
        return feels_like;
    }

    public void setFeels_like(String feels_like) {
        this.feels_like = feels_like;
    }

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
