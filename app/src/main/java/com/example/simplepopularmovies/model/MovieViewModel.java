package com.example.simplepopularmovies.model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;


import com.example.simplepopularmovies.database.MovieDatabase;
import com.example.simplepopularmovies.model.Movie;

import java.util.ArrayList;

public class MovieViewModel extends AndroidViewModel {

    private LiveData<ArrayList<Movie>> movies;

    public MovieViewModel(Application application) {
        super(application);
        MovieDatabase database = MovieDatabase.getInstance(this.getApplication());
        movies = database.movieDao().loadAllMovies();
    }

    public LiveData<ArrayList<Movie>> getMovies() {
        return movies;
    }
}
