package com.example.movieapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.PopupMenu;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.example.movieapp.model.Genre;
import com.example.movieapp.model.Movie;
import com.example.movieapp.service.MovieRepository;
import com.example.movieapp.service.OnGetGenresCallback;
import com.example.movieapp.service.OnGetMoviesCallback;
import com.example.movieapp.service.OnMoviesClickCallback;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView movieList;
    private MovieAdapter movieAdapter;
    private MovieRepository movieRepository;

    private List<Genre> movieGenres;
    private boolean isFetching;
    private int currentPage = 1;

    private String sortBy = MovieRepository.POPULAR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialize the repository
        movieRepository = MovieRepository.getInstance();

        movieList = findViewById(R.id.movies_list);

        setupOnScrollListener();

        getGenres();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_movies, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort:
                showSortMenu();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showSortMenu() {
        PopupMenu sortMenu = new PopupMenu(this, findViewById(R.id.sort));
        sortMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // Resets the currentPage after sorting
                currentPage = 1;

                switch (item.getItemId()) {
                    case R.id.popular:
                        sortBy = MovieRepository.POPULAR;
                        getMovies(currentPage);
                        return true;
                    case R.id.top_rated:
                        sortBy = MovieRepository.TOP_RATED;
                        getMovies(currentPage);
                        return true;
                    case R.id.upcoming:
                        sortBy = MovieRepository.UPCOMING;
                        getMovies(currentPage);
                        return true;
                    default:
                        return false;
                }
            }
        });

        sortMenu.inflate(R.menu.menu_movies_sort);
        sortMenu.show();
    }

    private void setupOnScrollListener() {
        final LinearLayoutManager manager = new LinearLayoutManager(this);
        movieList.setLayoutManager(manager);
        movieList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                int totalItemCount = manager.getItemCount();
                int visibleItemCount = manager.getChildCount();
                int firstVisibleItem = manager.findFirstVisibleItemPosition();

                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                    if (!isFetching) {
                        getMovies(currentPage + 1);
                    }
                }
            }
        });
    }

    private void getGenres() {
        movieRepository.getGenres(new OnGetGenresCallback() {
            @Override
            public void onSuccess(List<Genre> genres) {
                movieGenres = genres;
                getMovies(currentPage);
            }

            @Override
            public void onError() {
                showError();
            }
        });
    }

    private void getMovies(int page) {
        isFetching = true;
        movieRepository.getMovies(page, sortBy, new OnGetMoviesCallback() {
            @Override
            public void onSuccess(int page, List<Movie> movies) {
                Log.d("MoviesRepository", "Current Page = " + page);
                if (movieAdapter == null) {
                    movieAdapter = new MovieAdapter(movies, movieGenres, callback);
                    movieList.setAdapter(movieAdapter);
                } else {
                    if (page == 1) {
                        movieAdapter.clearMovies();
                    }
                    movieAdapter.appendMovies(movies);
                }
                currentPage = page;
                isFetching = false;

                setTitle();
            }

            @Override
            public void onError() {
                showError();
            }
        });
    }

    OnMoviesClickCallback callback = new OnMoviesClickCallback() {
        @Override
        public void onClick(Movie movie) {
            Intent intent = new Intent(MainActivity.this, MovieActivity.class);
            intent.putExtra(MovieActivity.MOVIE_ID, movie.getId());
            startActivity(intent);
        }
    };

    private void setTitle() {
        switch (sortBy) {
            case MovieRepository.POPULAR:
                setTitle(getString(R.string.popular));
                break;
            case MovieRepository.TOP_RATED:
                setTitle(getString(R.string.top_rated));
                break;
            case MovieRepository.UPCOMING:
                setTitle(getString(R.string.upcoming));
                break;
        }
    }

    public void showError() {
        Toast.makeText(MainActivity.this, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
    }
}
