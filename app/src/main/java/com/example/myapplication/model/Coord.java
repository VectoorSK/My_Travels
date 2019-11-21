package com.example.myapplication.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Coord {
    @SerializedName("lat")
    private double latitude;
    @SerializedName("lng")
    private double longitude;

    public Coord(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
