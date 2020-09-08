package com.fahrul.movieujian.service;

import com.fahrul.movieujian.model.Movie;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIInterfaceRest {

//    @GET("/")
//    Call<SearchResponse> searchByTitle(@Query("s") String title);

    @GET(".")
    Call<Movie> searchByOMDbId(@Query("t") String q, @Query("apikey") String apiid);

}
