package com.example.myapplication.model;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.List;

public class RetroPokemon {

    @SerializedName("results")
    private List<Pokemon> results;

    public RetroPokemon(List<Pokemon> results) {
        this.results = results;
    }

    public void setResults(List<Pokemon> results) {
        this.results = results;
    }

    public List<Pokemon> getResults() {
        return results;
    }
}
