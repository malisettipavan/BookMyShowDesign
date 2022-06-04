package com.lowleveldesign.bookmyshow.service;

import com.lowleveldesign.bookmyshow.exceptions.NotFoundException;
import com.lowleveldesign.bookmyshow.model.Movie;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MovieService {
    private final Map<String, Movie> movies;

    public MovieService(){
        this.movies = new HashMap<>();
    }

    public Movie createMovie(final String movieName){
        final String movieId = UUID.randomUUID().toString();
        final Movie movie = new Movie(movieId, movieName);
        this.movies.put(movieId, movie);
        return movie;
    }

    public Movie getMovie(final String movieId){
        if(!movies.containsKey(movieId)){
            throw new NotFoundException();
        }
        return movies.get(movieId);
    }
}
