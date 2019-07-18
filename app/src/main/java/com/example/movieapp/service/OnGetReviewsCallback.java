package com.example.movieapp.service;

import com.example.movieapp.model.Review;

import java.util.List;

public interface OnGetReviewsCallback {
    void onSuccess(List<Review> reviews);

    void onError();
}
