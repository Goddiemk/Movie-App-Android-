package com.example.movieapp;

import com.example.movieapp.model.Genre;

import java.util.List;

public interface OnGetGenresCallback {
    void onSuccess(List<Genre> genres);

    void onError();
}
