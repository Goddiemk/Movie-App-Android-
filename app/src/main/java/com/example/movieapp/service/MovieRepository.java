package com.example.movieapp.service;

import com.example.movieapp.BuildConfig;
import com.example.movieapp.api.TMDbApi;
import com.example.movieapp.model.GenreResponse;
import com.example.movieapp.model.Movie;
import com.example.movieapp.model.MovieResponse;
import com.example.movieapp.model.ReviewResponse;
import com.example.movieapp.model.TrailerResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieRepository {
    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    private static final String LANGUAGE = "en-US";

    private static MovieRepository repository;
    private TMDbApi api;

    public static final String POPULAR = "popular";
    public static final String TOP_RATED = "top_rated";
    public static final String UPCOMING = "upcoming";

    private MovieRepository(TMDbApi api) {
        this.api = api;
    }

    public static MovieRepository getInstance() {
        if (repository == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            repository = new MovieRepository(retrofit.create(TMDbApi.class));
        }

        return repository;
    }


    public void getMovies(int page, String sortBy, final OnGetMoviesCallback callback) {
        Callback<MovieResponse> call = new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful()) {
                    MovieResponse movieResponse = response.body();
                    if (movieResponse != null && movieResponse.getMovies() != null) {
                        callback.onSuccess(movieResponse.getPage(), movieResponse.getMovies());
                    } else {
                        callback.onError();
                    }
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                callback.onError();
            }
        };

        switch (sortBy) {
            case TOP_RATED:
                api.getTopRatedMovies(BuildConfig.ApiKey, LANGUAGE, page).enqueue(call);
                break;
            case UPCOMING:
                api.getUpcomingMovies(BuildConfig.ApiKey, LANGUAGE, page).enqueue(call);
                break;
            case POPULAR:
                api.getPopularMovies(BuildConfig.ApiKey, LANGUAGE, page).enqueue(call);
                break;
            default:
                api.getPopularMovies(BuildConfig.ApiKey, LANGUAGE, page).enqueue(call);
                break;
        }
    }

    public void getGenres(final OnGetGenresCallback callback) {
        api.getGenres(BuildConfig.ApiKey, LANGUAGE)
                .enqueue(new Callback<GenreResponse>() {
                    @Override
                    public void onResponse(Call<GenreResponse> call, Response<GenreResponse> response) {
                        if (response.isSuccessful()) {
                            GenreResponse genreResponse = response.body();
                            if (genreResponse != null && genreResponse.getGenres() != null) {
                                callback.onSuccess(genreResponse.getGenres());
                            } else {
                                callback.onError();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<GenreResponse> call, Throwable t) {
                        callback.onError();
                    }
                });
    }

    public void getMovie(int movieId, final OnGetMovieCallback callback) {
        api.getMovie(movieId, BuildConfig.ApiKey, LANGUAGE)
                .enqueue(new Callback<Movie>() {
                    @Override
                    public void onResponse(Call<Movie> call, Response<Movie> response) {
                        if (response.isSuccessful()) {
                            Movie movie = response.body();
                            if (movie != null) {
                                callback.onSuccess(movie);
                            } else {
                                callback.onError();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Movie> call, Throwable t) {
                        callback.onError();
                    }
                });
    }

    public void getTrailers(int movieId, final OnGetTrailersCallback callback) {
        api.getTrailers(movieId, BuildConfig.ApiKey, LANGUAGE)
                .enqueue(new Callback<TrailerResponse>() {
                    @Override
                    public void onResponse(Call<TrailerResponse> call, Response<TrailerResponse> response) {
                        if (response.isSuccessful()) {
                            TrailerResponse trailerResponse = response.body();
                            if (trailerResponse != null && trailerResponse.getTrailers() != null) {
                                callback.onSuccess(trailerResponse.getTrailers());
                            } else {
                                callback.onError();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<TrailerResponse> call, Throwable t) {
                        callback.onError();
                    }
                });
    }

    public void getReviews(int movieId, final OnGetReviewsCallback callback) {
        api.getReviews(movieId, BuildConfig.ApiKey, LANGUAGE)
                .enqueue(new Callback<ReviewResponse>() {
                    @Override
                    public void onResponse(Call<ReviewResponse> call, Response<ReviewResponse> response) {
                        if (response.isSuccessful()) {
                            ReviewResponse reviewResponse = response.body();
                            if (reviewResponse != null && reviewResponse.getReviews() != null) {
                                callback.onSuccess(reviewResponse.getReviews());
                            } else {
                                callback.onError();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ReviewResponse> call, Throwable t) {
                        callback.onError();
                    }
                });
    }
}
