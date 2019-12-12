package com.mashkovska.authentication;

import android.app.Application;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApplicationEx extends Application {

    private MovieApi movieService;
    private FirebaseAuth auth;
    private FirebaseUser user;

    public void onCreate()
    {
        super.onCreate();
        auth = FirebaseAuth.getInstance();
        movieService = createMovieApiService();
    }

    public FirebaseUser getUser() { return user; }

    public FirebaseAuth getAuth(){
        return auth;
    }

    public MovieApi getMovieService(){
        return movieService;
    }

    public MovieApi createMovieApiService(){
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://us-central1-androidapplication-46f40.cloudfunctions.net/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(MovieApi.class);
    }
}