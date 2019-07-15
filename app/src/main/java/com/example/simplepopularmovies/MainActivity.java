package com.example.simplepopularmovies;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.example.simplepopularmovies.adapters.MovieAdapter;
import com.example.simplepopularmovies.constants.Constant;
import com.example.simplepopularmovies.database.MovieDatabase;
import com.example.simplepopularmovies.model.Movie;
import com.example.simplepopularmovies.model.viewmodels.MovieViewModel;
import com.example.simplepopularmovies.utils.JsonUtils;
import com.example.simplepopularmovies.utils.NetworkUtils;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity
        extends AppCompatActivity
        implements
            MovieAdapter.MovieAdapterOnClickHandler,
            LoaderManager.LoaderCallbacks<String>,
            SharedPreferences.OnSharedPreferenceChangeListener
{
    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;
    private List movieList = new ArrayList<Movie>();
    GridLayoutManager layoutManager;
    int spanCount = 2;
    private MovieDatabase movieDatabase;
    Boolean isMovieDatabaseSet = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        //checking device orientation
        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) spanCount = 4;

        layoutManager = new GridLayoutManager(this, spanCount);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mMovieAdapter = new MovieAdapter(movieList, this);
        mRecyclerView.setAdapter(mMovieAdapter);

        setupSharedPreferences();
    }


    private void loadMovieData(SharedPreferences sharedPreferences) {
        String settingSortPreference = sharedPreferences.getString(getString(R.string.preference_sort_key), getString(R.string.preference_sort_default_option));
        showDataView();

        if(settingSortPreference.equals(getString(R.string.key_sort_popular)) ||
            settingSortPreference.equals(getString(R.string.key_sort_top_rated)))
        {
            if(settingSortPreference.equals(getString(R.string.key_sort_popular))) getSupportActionBar().setTitle(R.string.title_popular_movie);
            if(settingSortPreference.equals(getString(R.string.key_sort_top_rated))) getSupportActionBar().setTitle(R.string.title_top_rated_movie);
            setupAsyncTaskLoader(settingSortPreference);
        }else if(settingSortPreference.equals(getString(R.string.key_sort_favorites))){
            getSupportActionBar().setTitle(R.string.title_top_favorite_movies);
            setupFavoriteMoviesViewModel();
        }
    }

    @Override
    public void onClick(int itemClicked) {
        Context context = this;
        Movie movieClicked = (Movie) movieList.get(itemClicked);

        if(movieClicked != null) {
            Class destinationClass = DetailActivity.class;
            Intent intentToStartDetailActivity = new Intent(context, destinationClass);
            intentToStartDetailActivity.putExtra(getString(R.string.intent_movie_selected), (Movie) movieClicked);
            startActivity(intentToStartDetailActivity);
        }
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
        return Constant.MOVIE_BASE_LIST_URL + sortParam;
    }

    private void setupAsyncTaskLoader(String sortParam){
        String urlMovieList = getSearchMovieListURL(sortParam);

        Bundle queryBundle = new Bundle();
        queryBundle.putString(Constant.SEARCH_QUERY_URL,urlMovieList.toString());

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<String> movieListSearchLoader = loaderManager.getLoader(Constant.LOADER_MOVIE_LIST);

        if(movieListSearchLoader == null){
            loaderManager.initLoader(Constant.LOADER_MOVIE_LIST, queryBundle, this);
        }else{
            loaderManager.restartLoader(Constant.LOADER_MOVIE_LIST, queryBundle, this);
        }
    }

    private void setupFavoriteMoviesViewModel(){
        if(!isMovieDatabaseSet) {
            movieDatabase = MovieDatabase.getInstance(getApplicationContext());
        }

        MovieViewModel viewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        viewModel.getMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                mLoadingIndicator.setVisibility(View.INVISIBLE);
                movieList = movies;
                mMovieAdapter.setData(movies);
            }
        });

        isMovieDatabaseSet = true;
    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int i, @Nullable final Bundle bundle) {
        return new AsyncTaskLoader<String>(this) {
            String movieListRawJson;

            @Override
            protected void onStartLoading() {
                super.onStartLoading();

                if(bundle == null) return;

                if(movieListRawJson != null){
                    deliverResult(movieListRawJson);
                }else{
                    mLoadingIndicator.setVisibility(View.VISIBLE);
                    forceLoad();
                }
            }

            @Nullable
            @Override
            public String loadInBackground() {
                String movieQueryUrlString = bundle.getString(Constant.SEARCH_QUERY_URL);

                if(movieQueryUrlString == null || TextUtils.isEmpty(movieQueryUrlString)){
                    return null;
                }

                try {
                    URL searchUrl = NetworkUtils.buildUrl(movieQueryUrlString);
                    return NetworkUtils.getResponseFromHttpUrl(searchUrl);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void deliverResult(@Nullable String data) {
                movieListRawJson = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        if (data != null && !data.equals("")) {
            movieList = (List) JsonUtils.parseMovieListJson(data);
            mMovieAdapter.setData(movieList);
            showDataView();
        } else {
            showErrorMessage();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //outState.putParcelableArrayList(getString(R.string.key_parcelable_movie_list), movieList);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(startSettingsActivity);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupSharedPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        loadMovieData(sharedPreferences);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    // Updates the screen if the shared preferences change. This method is required when you make a
    // class implement OnSharedPreferenceChangedListener
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.preference_sort_key))) {
            loadMovieData(sharedPreferences);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }
}
