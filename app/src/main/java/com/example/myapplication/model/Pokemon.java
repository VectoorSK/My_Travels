package com.example.myapplication.model;

import com.google.gson.annotations.SerializedName;


public class Pokemon {
    @SerializedName("name")
    private String name;
    @SerializedName("url")
    private String url;

    public Pokemon(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}
