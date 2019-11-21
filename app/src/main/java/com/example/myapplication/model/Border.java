package com.example.myapplication.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Border {
    @SerializedName("country")
    private String country;
    @SerializedName("code")
    private String code;
    @SerializedName("borders")
    private List<Coord> borders;

    public Border(String country, String code, List<Coord> borders) {
        this.country = country;
        this.code = code;
        this.borders = borders;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<Coord> getBorders() {
        return borders;
    }

    public void setBorders(List<Coord> borders) {
        this.borders = borders;
    }
}
