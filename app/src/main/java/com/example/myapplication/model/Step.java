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
    @SerializedName("img")
    private String img;
    @SerializedName("desc")
    private String desc;
    @SerializedName("pictures")
    private List<Img> pictures;

    public Step(Integer id, String city, String img, String desc, List<Img> pictures) {
        this.id = id;
        this.city = city;
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
