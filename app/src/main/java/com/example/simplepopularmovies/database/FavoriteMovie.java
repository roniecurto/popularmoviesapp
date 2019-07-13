package com.example.simplepopularmovies.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "favorite_movies")
public class FavoriteMovie {
    @PrimaryKey
    private int id;
    private int vote_count;
    private double vote_average;
    private String title;
    private double popularity;
    private String poster_path;
    private String original_language;
    private String original_title;
    private String backdrop_path;
    private String overview;
    private String release_date;

    public FavoriteMovie(
            int id,
            int vote_count,
            double vote_average,
            String title,
            double popularity,
            String poster_path,
            String original_language,
            String original_title,
            String backdrop_path,
            String overview,
            String release_date
    ) {
        this.id = id;
        this.vote_count = vote_count;
        this.vote_average = vote_average;
        this.title = title;
        this.popularity = popularity;
        this.poster_path = poster_path;
        this.original_language = original_language;
        this.original_title = original_title;
        this.backdrop_path = backdrop_path;
        this.overview = overview;
        this.release_date = release_date;
    }

    public int getId(){ return id;}
    public int getVoteCount(){ return vote_count;}
    public Double getVoteAverage(){ return vote_average;}
    public String getTitle(){ return title;}
    public Double getPopularity(){ return popularity;}
    public String getPosterPath(){ return poster_path;}
    public String getOriginalLanguage(){ return original_language;}
    public String getOriginalTitle(){ return original_title;}
    public String getBackdropPath(){ return backdrop_path;}
    public String getOverview(){ return overview;}
    public String getReleaseDate(){ return release_date;}

    public void setId(int id){ this.id = id; }
    public void setVoteCount(int vote_count){ this.vote_count = vote_count; }

}
