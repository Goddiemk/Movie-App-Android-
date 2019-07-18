package com.example.movieapp;

import com.example.movieapp.model.Movie;

public interface OnGetMovieCallback {
    void onSuccess(Movie movie);

    void onError();
}
