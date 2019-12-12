package com.mashkovska.authentication;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface MovieApi {

    @GET("/movies/")
    Call<List<Movie>> getAllMovies();
}