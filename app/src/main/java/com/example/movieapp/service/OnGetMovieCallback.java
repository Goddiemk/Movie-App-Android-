package com.example.movieapp.service;

import com.example.movieapp.model.Movie;

public interface OnGetMovieCallback {
    void onSuccess(Movie movie);

    void onError();
}
