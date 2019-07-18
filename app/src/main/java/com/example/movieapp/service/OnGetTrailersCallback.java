package com.example.movieapp;

import com.example.movieapp.model.Trailer;

import java.util.List;

public interface OnGetTrailersCallback {
    void onSuccess(List<Trailer> trailers);

    void onError();
}
