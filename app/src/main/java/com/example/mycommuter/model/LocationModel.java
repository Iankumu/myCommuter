package com.example.mycommuter.model;

public class LocationModel {

    private String latitude, longitude, nav_current_lat, nav_current_long, nav_dest_lat, nav_dest_long;

    public LocationModel() { }

    public LocationModel(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public LocationModel(String nav_current_lat, String nav_current_long, String nav_dest_lat, String nav_dest_long) {
        this.nav_current_lat = nav_current_lat;
        this.nav_current_long = nav_current_long;
        this.nav_dest_lat = nav_dest_lat;
        this.nav_dest_long = nav_dest_long;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLatitude(String latitude) { this.latitude = latitude; }

    public void setLongitude(String longitude) { this.longitude = longitude; }

    public String getNav_current_lat() { return nav_current_lat;}

    public String getNav_current_long() { return nav_current_long;}

    public String getNav_dest_lat() { return nav_dest_lat; }

    public String getNav_dest_long() { return nav_dest_long; }
}