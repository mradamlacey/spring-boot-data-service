package com.cbre.dataservices.models;

public class GeoLocation {

    private double latitude;
    private double longitude;

    public double getLatitude(){
        return latitude;
    }
    public void setLatitude(double lat){
        latitude = lat;
    }

    public double getLongitude(){
        return longitude;
    }
    public void setLongitude(double longitude){
        this.longitude = longitude;
    }
}
