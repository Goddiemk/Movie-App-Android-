package com.example.movieapp.service;

import com.example.movieapp.model.Movie;

import java.util.List;

public interface OnGetMoviesCallback {

    void onSuccess(int page, List<Movie> movies);

    void onError();
}
