package com.example.simplepopularmovies.model.repositories;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;

import com.example.simplepopularmovies.database.MovieDao;
import com.example.simplepopularmovies.database.MovieDatabase;
import com.example.simplepopularmovies.model.Movie;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class MovieRepository {
    private static final String TAG = MovieRepository.class.getSimpleName();

    private MovieDao mMovieDao;
    private LiveData<List<Movie>> mFavorites;

    public MovieRepository(Application application) {
        MovieDatabase db = MovieDatabase.getInstance(application);
        mMovieDao = db.movieDao();
    }

    public LiveData<List<Movie>> getFavorites() {
        mFavorites = mMovieDao.loadAllMovies();
        return mFavorites;
    }

    public Movie getSingleFilm(int id) {
        try {
            return new findFavoriteMovieAsyncTask(mMovieDao).execute(id).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void insertFavorite(Movie movie) {
        Log.v("Repository Insert Movie", movie.getTitle());
        new createFavoriteMovieAsyncTask(mMovieDao).execute(movie);
    }

    public void removeFavorite(Movie movie) {
        Log.v("Repository Remove Movie", movie.getTitle());
        new removeFavoriteMovieAsyncTask(mMovieDao).execute(movie);
    }


    //Add to Favorite
    private static class createFavoriteMovieAsyncTask extends AsyncTask<Movie, Void, Void> {
        private MovieDao mMovieDao;

        createFavoriteMovieAsyncTask(MovieDao mMovieDao) {
            this.mMovieDao = mMovieDao;
        }


        @Override
        protected Void doInBackground(Movie... movies) {
            Movie currentMovie = movies[0];
            Log.v("Rep Insert doBackground", currentMovie.getTitle());
            mMovieDao.insertMovie(currentMovie);
            return null;
        }
    }

    private static class removeFavoriteMovieAsyncTask extends AsyncTask<Movie, Void, Void> {
        private MovieDao mMovieDao;

        removeFavoriteMovieAsyncTask(MovieDao mMovieDao) {
            this.mMovieDao = mMovieDao;
        }


        @Override
        protected Void doInBackground(Movie... movies) {
            Movie currentMovie = movies[0];
            Log.v("Rep Remove Background", currentMovie.getTitle());
            mMovieDao.deleteMovie(currentMovie);
            return null;
        }
    }

    private static class findFavoriteMovieAsyncTask extends AsyncTask<Integer, Void, Movie> {
        private MovieDao mMovieDao;

        findFavoriteMovieAsyncTask(MovieDao movieDao) {
            this.mMovieDao = movieDao;
        }

        @Override
        protected Movie doInBackground(Integer... ints) {
            int currentInt = ints[0];
            return mMovieDao.findMovie(currentInt);
        }
    }
}

