package com.example.simplepopularmovies;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.simplepopularmovies.adapters.MovieAdapter;
import com.example.simplepopularmovies.model.Movie;
import com.example.simplepopularmovies.utils.JsonUtils;
import com.example.simplepopularmovies.utils.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {
    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;
    private static final String MOVIE_BASE_LIST_URL = "http://api.themoviedb.org/3/movie/";
    private ArrayList movieList = new ArrayList<Movie>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mMovieAdapter = new MovieAdapter(movieList, this);
        mRecyclerView.setAdapter(mMovieAdapter);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        loadMovieData("popular");

    }

    private void loadMovieData(String sortParam) {
        showDataView();

        if(sortParam == "popular") getSupportActionBar().setTitle("Popular Movies");
        if(sortParam == "top_rated") getSupportActionBar().setTitle("Top Rated Movies");

        String urlMovieList = getSearchMovieListURL(sortParam);
        URL requestUrl = NetworkUtils.buildUrl(urlMovieList);
        new FetchTask().execute(requestUrl);
    }

    @Override
    public void onClick(Movie movie) {
        Context context = this;
    }

    private void showDataView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    private String getSearchMovieListURL(String sortParam){
        return MOVIE_BASE_LIST_URL + sortParam;
    }

    public class FetchTask extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
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
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (jsonMovieResponse != null && !jsonMovieResponse.equals("")) {
                Log.v("ASYNC_TASK", "data loaded");
                movieList = (ArrayList) JsonUtils.parseMovieListJson(jsonMovieResponse);
                //mMovieAdapter.setData(movieList);
                mMovieAdapter.notifyDataSetChanged();
                showDataView();
            } else {
                showErrorMessage();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_sort_rated) {
            return true;
        }
        if (id == R.id.action_sort_popular) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
