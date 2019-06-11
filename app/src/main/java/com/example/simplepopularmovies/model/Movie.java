package com.example.simplepopularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Movie implements Parcelable {
    int vote_count;
    int id;
    double vote_average;
    String title;
    double popularity;
    String poster_path;
    String original_language;
    String original_title;
    int [] genre_ids;
    String backdrop_path;
    Boolean adult;
    String overview;
    Date release_date;

    /*
        "vote_count": 194,
        "id": 320288,
        "video": false,
        "vote_average": 6.3,
        "title": "Dark Phoenix",
        "popularity": 356.672,
        "poster_path": "/kZv92eTc0Gg3mKxqjjDAM73z9cy.jpg",
        "original_language": "en",
        "original_title": "Dark Phoenix",
        "genre_ids":[878,12,28],
        "backdrop_path": "/phxiKFDvPeQj4AbkvJLmuZEieDU.jpg",
        "adult": false,
        "overview": "The X-Men face their most formidable and powerful foe when one of their own, Jean Grey, starts to spiral out of control. During a rescue mission in outer space, Jean is nearly killed when she's hit by a mysterious cosmic force. Once she returns home, this force not only makes her infinitely more powerful, but far more unstable. The X-Men must now band together to save her soul and battle aliens that want to use Grey's new abilities to rule the galaxy.",
        "release_date": "2019-06-05"
    */

    public Movie(
            int vote_count,
            int id,
            double vote_average,
            String title,
            double popularity,
            String poster_path,
            String original_language,
            String original_title,
            int [] genre_ids,
            String backdrop_path,
            Boolean adult,
            String overview,
            Date release_date
    ) {
        this.vote_count = vote_count;
        this.id = id;
        this.vote_average = vote_average;
        this.title = title;
        this.popularity = popularity;
        this.poster_path = poster_path;
        this.original_language = original_language;
        this.original_title = original_title;
        this.genre_ids = genre_ids;
        this.backdrop_path = backdrop_path;
        this.adult = adult;
        this.overview = overview;
        this.release_date = release_date;
    }

    private Movie(Parcel in){
        id = in.readInt();
        title = in.readString();
        poster_path = in.readString();
        vote_average = in.readDouble();
        popularity = in.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String toString() { return this.title + "--" + this.poster_path + "--" + this.id; }

    public String getLinkMoviePoster(String imageSize){
        String basePathURL = "http://image.tmdb.org/t/p/";
        //String [] imageAvailableSizes = {"w92", "w154", "w185", "w342", "w500", "w780", "original"};
        return basePathURL + imageSize + this.poster_path;
    }

    public String getTitle(){
        return this.title;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(poster_path);
        parcel.writeInt(id);
        parcel.writeDouble(vote_average);
        parcel.writeDouble(popularity);
    }

    public final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel parcel) {
            return new Movie(parcel);
        }

        @Override
        public Movie[] newArray(int i) {
            return new Movie[i];
        }
    };
}
