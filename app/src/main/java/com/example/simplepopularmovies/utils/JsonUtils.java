package com.example.simplepopularmovies.utils;

import android.util.Log;

import com.example.simplepopularmovies.model.Movie;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class JsonUtils {
    public static List parseMovieListJson(String json) {
        try {
            ArrayList movieList = new ArrayList<Movie>();
            JSONObject movieListJson = new JSONObject(json);
            JSONArray movies = movieListJson.getJSONArray("results");
            JSONObject movie;
            Movie movieObject;
            JSONArray genresIDs;

            if(movies.length() > 0) {
                for (int i = 0; i < movies.length(); i++) {
                    movie = movies.getJSONObject(i);

                    genresIDs = movie.optJSONArray("genre_ids");
                    int [] genres = new int[genresIDs.length()];
                    if(null != genresIDs){
                        for (int k = 0; k < genresIDs.length(); k++){
                            genres[k] = genresIDs.optInt(k);
                        }
                    }

                    movieObject = new Movie(
                            movie.getInt("vote_count"),
                            movie.getInt("id"),
                            movie.getDouble("vote_average"),
                            movie.getString("title"),
                            movie.getDouble("popularity"),
                            movie.getString("poster_path"),
                            movie.getString("original_language"),
                            movie.getString("original_title"),
                            genres,
                            movie.getString("backdrop_path"),
                            movie.getBoolean("adult"),
                            movie.getString("overview"),
                            new SimpleDateFormat("yyyy-MM-dd").parse(movie.getString("release_date"))
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
}

