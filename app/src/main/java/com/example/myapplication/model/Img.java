package com.example.myapplication.model;

import android.graphics.Picture;
import android.media.Image;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Img {
    @SerializedName("url")
    private String url;
    @SerializedName("caption")
    private String caption;

    public Img(String url, String caption) {
        this.url = url;
        this.caption = caption;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }
}
