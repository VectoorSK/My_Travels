package com.example.myapplication.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Country {
    @SerializedName("country")
    private String country;
    @SerializedName("latitude")
    private double latitude;
    @SerializedName("longitude")
    private double longitude;
    @SerializedName("name")
    private String name;

    public Country(String country, double latitude, double longitude, String name) {
        this.country = country;
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}