package com.mashkovska.authentication;

public class Movie {

    private String title;
    private Integer year;
    private Float rating;
    private String description;
    private String poster;

    public Movie(String title, Integer year, Float rating, String description, String poster) {
        this.title = title;
        this.year = year;
        this.rating = rating;
        this.description = description;
        this.poster = poster;
    }


    public String getPoster() {
        return poster;
    }


    public String getDescription() {
        return description;
    }


    public Float getRating() {
        return rating;
    }


    public Integer getYear() {
        return year;
    }


    public String getTitle() {
        return title;
    }


}