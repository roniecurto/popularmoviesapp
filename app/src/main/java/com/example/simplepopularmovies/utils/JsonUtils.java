package com.example.simplepopularmovies.utils;

import com.example.simplepopularmovies.model.Movie;
import com.example.simplepopularmovies.model.MovieReview;
import com.example.simplepopularmovies.model.MovieTrailer;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class JsonUtils {
    public static List parseMovieListJson(String json) {
        List<Movie> movieList = new ArrayList<Movie>();
        Movie movieObject;
        try {
            JSONObject movieListJson = new JSONObject(json);
            JSONArray movies = movieListJson.getJSONArray("results");
            JSONObject movie;

            if (movies.length() > 0) {
                for (int i = 0; i < movies.length(); i++) {
                    movie = movies.getJSONObject(i);

                    movieObject = new Movie(
                            movie.getInt("id"),
                            movie.getInt("vote_count"),
                            movie.getDouble("vote_average"),
                            movie.getString("title"),
                            movie.getDouble("popularity"),
                            movie.getString("poster_path"),
                            movie.getString("original_language"),
                            movie.getString("original_title"),
                            movie.getString("overview"),
                            movie.getString("release_date")
                    );
                    movieList.add(movieObject);
                }
            }

            return movieList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List parseMovieTrailersListJson(String json) {
        List<MovieTrailer> movieTrailerList = new ArrayList<MovieTrailer>();
        MovieTrailer movieTrailerObject;
        try {
            JSONObject movieTrailerListJson = new JSONObject(json);
            JSONArray movieTrailers = movieTrailerListJson.getJSONArray("results");
            JSONObject movieTrailer;

            if (movieTrailers.length() > 0) {
                for (int i = 0; i < movieTrailers.length(); i++) {
                    movieTrailer = movieTrailers.getJSONObject(i);

                    movieTrailerObject = new MovieTrailer(
                            movieTrailer.getString("id"),
                            movieTrailer.getString("key"),
                            movieTrailer.getString("name"),
                            movieTrailer.getString("site"),
                            movieTrailer.getString("size"),
                            movieTrailer.getString("type")
                    );

                    movieTrailerList.add(movieTrailerObject);
                }
            }

            return movieTrailerList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List parseMovieReviewsListJson(String json) {
        List<MovieReview> movieReviewList = new ArrayList<MovieReview>();
        MovieReview movieReviewObject;
        try {
            JSONObject movieReviewListJson = new JSONObject(json);
            JSONArray movieReviews = movieReviewListJson.getJSONArray("results");
            JSONObject movieReview;

            if (movieReviews.length() > 0) {
                for (int i = 0; i < movieReviews.length(); i++) {
                    movieReview = movieReviews.getJSONObject(i);

                    movieReviewObject = new MovieReview(
                            movieReview.getString("id"),
                            movieReview.getString("author"),
                            movieReview.getString("content"),
                            movieReview.getString("url")
                    );

                    movieReviewList.add(movieReviewObject);
                }
            }

            return movieReviewList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

