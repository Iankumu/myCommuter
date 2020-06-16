package com.example.mycommuter.model;


public class LocationModel {

    private String currentlatitude, currentlongitude, email,destinationLatitude,destinationLongitude;

    public LocationModel() { }

    public LocationModel(String currentlatitude, String currentlongitude, String destinationLatitude,String destinationLongitude, String email) {
        this.currentlatitude = currentlatitude;
        this.currentlongitude = currentlongitude;
        this.destinationLatitude = destinationLatitude;
        this.destinationLongitude = destinationLongitude;
        this.email = email;
    }

    public String getLatitude() {
        return currentlatitude;
    }

    public String getLongitude() {
        return currentlongitude;
    }

    public String getEmail() { return email; }

    public void setLatitude(String latitude) { this.currentlatitude = latitude; }

    public void setLongitude(String longitude) { this.currentlongitude = longitude; }

    public void setEmail(String email) { this.email = email; }

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
