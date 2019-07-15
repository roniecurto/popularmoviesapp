package com.example.simplepopularmovies.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.simplepopularmovies.constants.Constant;

import java.util.Date;

@Entity(tableName = "movies")
public class Movie implements Parcelable {
    @PrimaryKey(autoGenerate = false)
    public int id;
    public int vote_count;
    public double vote_average;
    public String title;
    public double popularity;
    public String poster_path;
    public String original_language;
    public String original_title;
    public String overview;
    public String release_date;
    public Date updated_at;

    @Ignore
    public Movie(
            int id,
            int vote_count,
            double vote_average,
            String title,
            double popularity,
            String poster_path,
            String original_language,
            String original_title,
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
        this.overview = overview;
        this.release_date = release_date;
    }

    public Movie(
            int id,
            int vote_count,
            double vote_average,
            String title,
            double popularity,
            String poster_path,
            String original_language,
            String original_title,
            String overview,
            String release_date,
            Date updated_at
    ) {
        this.id = id;
        this.vote_count = vote_count;
        this.vote_average = vote_average;
        this.title = title;
        this.popularity = popularity;
        this.poster_path = poster_path;
        this.original_language = original_language;
        this.original_title = original_title;
        this.overview = overview;
        this.release_date = release_date;
        this.updated_at = updated_at;
    }


    private Movie(Parcel in){
        id = in.readInt();
        title = in.readString();
        poster_path = in.readString();
        vote_average = in.readDouble();
        popularity = in.readDouble();
        original_title = in.readString();
        release_date = in.readString();
        overview = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getLinkMoviePoster(String imageSize){
        String basePathURL = Constant.MOVIE_POSTER_BASE_URL;
        //String [] imageAvailableSizes = {"w92", "w154", "w185", "w342", "w500", "w780", "original"};
        return basePathURL + imageSize + this.poster_path;
    }


    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeString(poster_path);
        parcel.writeDouble(vote_average);
        parcel.writeDouble(popularity);
        parcel.writeString(original_title);
        parcel.writeString(release_date);
        parcel.writeString(overview);
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel parcel) {
            return new Movie(parcel);
        }

        @Override
        public Movie[] newArray(int i) {
            return new Movie[i];
        }
    };


    // Getters
    public int getId(){ return id; }
    public int getVoteCount(){ return vote_count; }
    public Double getVoteAverage(){ return vote_average; }
    public String getTitle(){ return title; }
    public Double getPopularity(){ return popularity; }
    public String getPosterPath(){ return poster_path; }
    public String getOriginalLanguage(){ return original_language; }
    public String getOriginalTitle(){ return original_title; }
    public String getOverview(){ return overview; }
    public String getReleaseDate(){ return release_date; }
    public Date getUpdatedAt(){ return updated_at; }

    //Setters
    public void setId(int id){ this.id = id; }
    public void setVoteCount(int vote_count){ this.vote_count = vote_count; }
    public void setVoteAverage(Double vote_average){ this.vote_average = vote_average; }
    public void setTitle(String title){ this.title = title; }
    public void setPopularity(Double popularity){ this.popularity = popularity; }
    public void setPosterPath(String poster_path){ this.poster_path = poster_path; }
    public void setOriginalLanguage(String original_language){ this.original_language = original_language; }
    public void setOriginalTitle(String original_title){ this.original_title = original_title; }
    public void setReleaseDate(String release_date){ this.release_date = release_date; }
    public void setUpdatedAt(Date updated_at){ this.updated_at = updated_at; }

}
