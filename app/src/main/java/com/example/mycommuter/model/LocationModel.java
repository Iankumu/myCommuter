package com.example.mycommuter.model;

public class LocationModel {

    private String latitude, longitude, user_id;

    public LocationModel() { }

    public LocationModel(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public LocationModel(String latitude, String longitude, String user_id) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.user_id = user_id;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getUser_id() { return user_id; }

    public void setLatitude(String latitude) { this.latitude = latitude; }

    public void setLongitude(String longitude) { this.longitude = longitude; }

    public void setUser_id(String user_id) { this.user_id = user_id; }
}