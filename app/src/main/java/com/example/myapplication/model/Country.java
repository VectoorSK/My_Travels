package com.example.myapplication.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

public class Country {
    @SerializedName("id")
    private Integer id;
    @SerializedName("country")
    private String country;
    @SerializedName("flag")
    private String flag;
    @SerializedName("date")
    private String date;
    @SerializedName("desc")
    private String desc;
    @SerializedName("steps")
    private String steps;

    public Country(Integer id, String country, String flag, String date, String desc, String steps) {
        this.id = id;
        this.country = country;
        this.flag = flag;
        this.date = date;
        this.desc = desc;
        this.steps = steps;
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

    public String getFlag() {
        return flag;
    }

    public Bitmap getFlagBitmap() {
        try {
            URL url = new URL(flag);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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
}