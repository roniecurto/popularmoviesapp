package com.example.simplepopularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.simplepopularmovies.database.MovieDatabase;
import com.example.simplepopularmovies.model.Movie;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DetailActivity extends AppCompatActivity {
    Date releaseDate;
    String releaseDateFormatted;
    SimpleDateFormat releaseDateFormatter;

    TextView mMovieTitleHeaderTextView;
    TextView mMovieTitleTextView;
    TextView mMovieOriginalTitleTextView;
    TextView mMovieRatingTextView;
    TextView mMovieReleaseTextView;
    TextView mMovieOverviewTextView;
    ImageView mMoviePosterImageView;

    private Movie movieSelected;

    Button mButton;
    private MovieDatabase mDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intentParent = getIntent();

        releaseDateFormatter = new SimpleDateFormat(getString(R.string.default_date_formatted));
        mMovieTitleHeaderTextView = findViewById(R.id.tv_movie_title_header);
        mMovieTitleTextView = findViewById(R.id.tv_movie_title);
        mMovieOriginalTitleTextView = findViewById(R.id.tv_movie_original_title);
        mMovieRatingTextView = findViewById(R.id.tv_movie_rating);
        mMovieReleaseTextView = findViewById(R.id.tv_movie_release_date);
        mMovieOverviewTextView = findViewById(R.id.tv_movie_overview);
        mMoviePosterImageView = findViewById(R.id.iv_movie_poster);

        initViews();
        //mDb = MovieDatabase.getInstance(getApplicationContext());


        if (intentParent.hasExtra(getString(R.string.intent_movie_selected))) {
            movieSelected = intentParent.getParcelableExtra(getString(R.string.intent_movie_selected));

            if(movieSelected != null){
                populateUI(movieSelected);
            }
        }

    }

    private void populateUI(Movie movieSelected){
        try {
            releaseDate = new SimpleDateFormat(getString(R.string.default_date_format)).parse(movieSelected.getReleaseDate());
            releaseDateFormatted = releaseDateFormatter.format(releaseDate);
        } catch (ParseException excetion){
            releaseDate = null;
        }

        mMovieTitleHeaderTextView.setText(movieSelected.getTitle());
        mMovieTitleTextView.setText(movieSelected.getTitle());
        mMovieOriginalTitleTextView.setText(movieSelected.getOriginalTitle());
        mMovieRatingTextView.setText(movieSelected.getPopularity().toString());
        mMovieReleaseTextView.setText(releaseDateFormatted);
        mMovieOverviewTextView.setText(movieSelected.getOverview());
        Picasso.get()
                .load(movieSelected.getLinkMoviePoster(getString(R.string.default_movie_poster_size)))
                .into(mMoviePosterImageView);
    }

    /**
     * initViews is called from onCreate to init the member variable views
     */
    private void initViews() {
        mButton = findViewById(R.id.favorite_button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFavoriteButtonClicked();
            }
        });
    }

    /**
     * onSaveButtonClicked is called when the "save" button is clicked.
     * It retrieves user input and inserts that new task data into the underlying database.
     */
    public void onFavoriteButtonClicked() {
        Date date = new Date();

        //setting updated at date to create recordo on database
        movieSelected.setUpdatedAt(date);

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                Movie favoriteMovie = mDb.movieDao().findMovie(movieSelected.getId());

                if(favoriteMovie == null){
                    mDb.movieDao().insertMovie(movieSelected);
                }else{
                    mDb.movieDao().deleteMovie(movieSelected);
                }

                finish();
            }
        });
    }




    private void makeMovieFavorite(){

    }
}
