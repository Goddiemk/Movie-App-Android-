package com.example.movieapp;

public interface OnGetMovieCallback {
    void onSuccess(Movie movie);

    void onError();
}
