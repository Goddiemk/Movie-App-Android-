package com.example.movieapp;

import java.util.List;

import retrofit2.Call;

public interface OnGetMoviesCallback {

    void onSuccess(int page, List<Movie> movies);

    void onError();
}
