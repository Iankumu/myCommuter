package com.example.mycommuter.model;

import com.google.gson.annotations.SerializedName;

public class LocationModel {

    private String latitude, longitude ;
    @SerializedName("currentLatitude")
    private String currentLatitude;
    @SerializedName("currentLongitude")
    private String currentLongitude;
    @SerializedName("destinationLatitude")
    private String destinationLatitude;
    @SerializedName("destinationLongitude")
    private String destinationLongitude;


    public LocationModel() { }

//    public LocationModel(String latitude, String longitude) {
//        this.latitude = latitude;
//        this.longitude = longitude;
//    }

    public LocationModel(String latitude, String longitude, String currentLatitude,String currentLongitude,String destinationLatitude,String destinationLongitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.currentLatitude = currentLatitude;
        this.currentLongitude = currentLongitude;
        this.destinationLatitude = destinationLatitude;
        this.destinationLongitude = destinationLongitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude; }

    public void setLongitude(String longitude) { this.longitude = longitude; }

    public String getCurrentLatitude() {
        return currentLatitude;
    }

    public void setCurrentLatitude(String currentLatitude) {
        this.currentLatitude = currentLatitude;
    }

    public String getCurrentLongitude() {
        return currentLongitude;
    }

    public void setCurrentLongitude(String currentLongitude) {
        this.currentLongitude = currentLongitude;
    }

    public String getDestinationLatitude() {
        return destinationLatitude;
    }

    public void setDestinationLatitude(String destinationLatitude) {
        this.destinationLatitude = destinationLatitude;
    }

    public String getDestinationLongitude() {
        return destinationLongitude;
    }

    public void setDestinationLongitude(String destinationLongitude) {
        this.destinationLongitude = destinationLongitude;
    }
}
