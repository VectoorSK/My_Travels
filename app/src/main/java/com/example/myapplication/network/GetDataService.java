package com.example.myapplication.network;

import com.example.myapplication.model.Border;
import com.example.myapplication.model.Country;
import com.example.myapplication.model.Travel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface GetDataService {

    @GET("/VectoorSK/TravelAPI/master/travels2.json")
    Call<List<Travel>> getAllTravels();

    @GET("/VectoorSK/TravelAPI/master/countries.json")
    Call<List<Country>> getAllCountries();

    @GET("/VectoorSK/TravelAPI/master/borders.json")
    Call<List<Border>> getAllBorders();
}
