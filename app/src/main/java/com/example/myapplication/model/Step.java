package com.example.myapplication.model;

import android.graphics.Picture;
import android.media.Image;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Step {
    @SerializedName("id")
    private Integer id;
    @SerializedName("city")
    private String city;
    @SerializedName("lat")
    private double lat;
    @SerializedName("lng")
    private double lng;
    @SerializedName("img")
    private String img;
    @SerializedName("desc")
    private String desc;
    @SerializedName("pictures")
    private List<Img> pictures;

    public Step(Integer id, String city, double lat, double lng, String img, String desc, List<Img> pictures) {
        this.id = id;
        this.city = city;
        this.lat = lat;
        this.lng = lng;
        this.img = img;
        this.desc = desc;
        this.pictures = pictures;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
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

    public List<Img> getPictures() {
        return pictures;
    }

    public void setPictures(List<Img> pictures) {
        this.pictures = pictures;
    }

    public int getNbImg() {
        return pictures.size();
    }
}
