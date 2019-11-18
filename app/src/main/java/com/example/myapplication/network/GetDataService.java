package com.example.myapplication.network;

import com.example.myapplication.model.Country;
import com.example.myapplication.model.RetroPhoto;
import com.example.myapplication.model.RetroPokemon;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface GetDataService {

    // @GET("/photos")
    // Call<List<RetroPhoto>> getAllPhotos();

    //@GET("/api/v2/pokemon?offset=0&limit=151")
    //Call<RetroPokemon> getAllPokemons();

    @GET("/VectoorSK/TravelAPI/master/API2.json")
    Call<List<Country>> getAllCountries();
}
