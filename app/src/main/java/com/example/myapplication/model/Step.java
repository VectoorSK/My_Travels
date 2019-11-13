package com.example.myapplication.model;

import com.google.gson.annotations.SerializedName;

public class Step {
    @SerializedName("city")
    private String city;
    @SerializedName("img")
    private String img;

    public Step(String city, String img) {
        this.city = city;
        this.img = img;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
