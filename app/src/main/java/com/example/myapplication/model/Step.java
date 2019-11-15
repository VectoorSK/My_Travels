package com.example.myapplication.model;

import com.google.gson.annotations.SerializedName;

public class Step {
    @SerializedName("city")
    private String city;
    @SerializedName("img")
    private String img;
    @SerializedName("desc")
    private String desc;

    public Step(String city, String img, String desc) {
        this.city = city;
        this.img = img;
        this.desc = desc;
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
