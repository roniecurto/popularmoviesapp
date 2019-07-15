package com.example.simplepopularmovies;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.simplepopularmovies.adapters.MovieReviewAdapter;
import com.example.simplepopularmovies.adapters.MovieTrailerAdapter;
import com.example.simplepopularmovies.constants.Constant;
import com.example.simplepopularmovies.database.MovieDatabase;
import com.example.simplepopularmovies.model.Movie;
import com.example.simplepopularmovies.model.MovieReview;
import com.example.simplepopularmovies.model.MovieTrailer;
import com.example.simplepopularmovies.model.repositories.MovieRepository;
import com.example.simplepopularmovies.utils.JsonUtils;
import com.example.simplepopularmovies.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DetailActivity extends AppCompatActivity{
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
    private Movie movieFavorite;

    private RecyclerView mRecyclerViewReviews;
    private RecyclerView mRecyclerViewTrailers;

    private MovieReviewAdapter mMovieReviewAdapter;
    private MovieTrailerAdapter mMovieTrailerAdapter;

    private List movieReviewList = new ArrayList<MovieReview>();
    private List movieTrailerList = new ArrayList<MovieTrailer>();

    LinearLayoutManager layoutManagerReviews;
    LinearLayoutManager layoutManagerTrailers;

    Button mFavoriteButton;
    private MovieDatabase mDb;
    MovieRepository movieRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intentParent = getIntent();
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        movieRepository = new MovieRepository(getApplication());

        mRecyclerViewReviews = (RecyclerView) findViewById(R.id.recyclerview_reviews);
        mRecyclerViewTrailers = (RecyclerView) findViewById(R.id.recyclerview_trailers);

        releaseDateFormatter = new SimpleDateFormat(getString(R.string.default_date_formatted));
        mMovieTitleHeaderTextView = findViewById(R.id.tv_movie_title_header);
        mMovieTitleTextView = findViewById(R.id.tv_movie_title);
        mMovieOriginalTitleTextView = findViewById(R.id.tv_movie_original_title);
        mMovieRatingTextView = findViewById(R.id.tv_movie_rating);
        mMovieReleaseTextView = findViewById(R.id.tv_movie_release_date);
        mMovieOverviewTextView = findViewById(R.id.tv_movie_overview);
        mMoviePosterImageView = findViewById(R.id.iv_movie_poster);

        if (intentParent.hasExtra(getString(R.string.intent_movie_selected))) {
            movieSelected = intentParent.getParcelableExtra(getString(R.string.intent_movie_selected));

            if(movieSelected != null){
                populateUI(movieSelected);

                layoutManagerReviews = new LinearLayoutManager(this);
                mRecyclerViewReviews.setLayoutManager(layoutManagerReviews);
                mRecyclerViewReviews.setHasFixedSize(true);
                mMovieReviewAdapter = new MovieReviewAdapter(movieReviewList);
                mRecyclerViewReviews.setAdapter(mMovieReviewAdapter);
                setupAsyncTask("reviews");

                layoutManagerTrailers = new LinearLayoutManager(this);
                mRecyclerViewTrailers.setLayoutManager(layoutManagerTrailers);
                mRecyclerViewTrailers.setHasFixedSize(true);
                mMovieTrailerAdapter = new MovieTrailerAdapter(movieTrailerList);
                mRecyclerViewTrailers.setAdapter(mMovieTrailerAdapter);
                setupAsyncTask("videos");

                movieFavorite = movieRepository.getSingleFilm(movieSelected.getId());

                initViews(movieFavorite);
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

    private void setupAsyncTask(String infoType){
        String baseUrl = Constant.MOVIE_BASE_LIST_URL;
        String movieId = String.valueOf(movieSelected.getId());

        URL requestMovieAdditionalInfoUrl = NetworkUtils.buildUrlAdditionalInfo(baseUrl, movieId, infoType);

        if(infoType == "reviews") {
            new MovieReviewsTask().execute(requestMovieAdditionalInfoUrl);
        }else if(infoType == "videos") {
            new MovieTrailersTask().execute(requestMovieAdditionalInfoUrl);
        }else {
            return;
        }
    }

    private void initViews(Movie movieFavoriteDatabase) {
        mFavoriteButton = findViewById(R.id.favorite_button);

        if(movieFavoriteDatabase == null){
            mFavoriteButton.setText(getString(R.string.label_action_favorite));
        }else {
            mFavoriteButton.setText(getString(R.string.label_action_unfavorite));
        }

        mFavoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFavoriteButtonClicked();
            }
        });
    }

    public void onFavoriteButtonClicked() {
        Date date = new Date();

        //check if movie is on database
        if(movieFavorite == null){
            movieSelected.setUpdatedAt(date);
            movieRepository.insertFavorite(movieSelected);
            movieFavorite = movieSelected;
            mFavoriteButton.setText(getString(R.string.label_action_unfavorite));
        }else{
            movieRepository.removeFavorite(movieSelected);
            mFavoriteButton.setText(getString(R.string.label_action_favorite));
            movieFavorite = null;
        }
    }

    public class MovieReviewsTask extends AsyncTask<URL, Void, String> {
        List<MovieReview> movieReviewsList = new ArrayList<MovieReview>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(URL... params) {
            String jsonMovieResponse = null;
            URL searchUrl = params[0];

            if (params.length == 0) return null;

            try {
                jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(searchUrl);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return jsonMovieResponse;
        }

        @Override
        protected void onPostExecute(String jsonMovieResponse) {
            if (jsonMovieResponse != null && !jsonMovieResponse.equals("")) {
                movieReviewsList = (ArrayList) JsonUtils.parseMovieReviewsListJson(jsonMovieResponse);
                mMovieReviewAdapter.setData(movieReviewsList);
                //showDataView();
            } else {
                //showErrorMessage();
            }
        }
    }

    public class MovieTrailersTask extends AsyncTask<URL, Void, String> {
        List<MovieTrailer> movieTrailersList = new ArrayList<MovieTrailer>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(URL... params) {
            String jsonMovieResponse = null;
            URL searchUrl = params[0];

            if (params.length == 0) return null;

            try {
                jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(searchUrl);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return jsonMovieResponse;
        }

        @Override
        protected void onPostExecute(String jsonMovieResponse) {
            if (jsonMovieResponse != null && !jsonMovieResponse.equals("")) {
                movieTrailersList = (ArrayList) JsonUtils.parseMovieTrailersListJson(jsonMovieResponse);
                mMovieTrailerAdapter.setData(movieTrailersList);
                //showDataView();
            } else {
                //showErrorMessage();
            }
        }
    }
}
