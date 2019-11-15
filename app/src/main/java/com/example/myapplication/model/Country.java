package com.example.myapplication.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class Country {
    @SerializedName("id")
    private Integer id;
    @SerializedName("country")
    private String country;
    @SerializedName("continent")
    private String continent;
    @SerializedName("flag")
    private String flag;
    @SerializedName("date_from")
    private String date_from;
    @SerializedName("date_to")
    private String date_to;
    @SerializedName("desc")
    private String desc;
    @SerializedName("steps")
    private String steps;
    @SerializedName("steps_array")
    private List<Step> steps_array;

    public Country(Integer id, String country, String continent, String flag, String date_from, String date_to, String desc, String steps, List<Step> steps_array) {
        this.id = id;
        this.country = country;
        this.continent = continent;
        this.flag = flag;
        this.date_from = date_from;
        this.date_to = date_to;
        this.desc = desc;
        this.steps = steps;
        this.steps_array = steps_array;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getContinent() {
        return continent;
    }

    public void setContinent(String continent) {
        this.continent = continent;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getDate_from() { return date_from; }

    public void setDate_from(String date_from) {
        this.date_from = date_from;
    }

    public String getDate_to() {
        return date_to;
    }

    public void setDate_to(String date_to) {
        this.date_to = date_to;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public List<Step> getSteps_array() {
        return steps_array;
    }

    public void setSteps_array(List<Step> steps_array) {
        this.steps_array = steps_array;
    }
}