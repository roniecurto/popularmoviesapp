package com.example.simplepopularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.simplepopularmovies.R;
import com.example.simplepopularmovies.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {
    private ArrayList<Movie> mMovieData;

    /*
     * An on-click handler that we've defined to make it easy for an Activity to interface with
     * our RecyclerView
     */
    private final MovieAdapterOnClickHandler mClickHandler;

    /**
     * The interface that receives onClick messages.
     */
    public interface MovieAdapterOnClickHandler {
        void onClick(Movie movie);
    }

    /**
     * This is our own custom constructor (it doesn't mirror a superclass constructor).
     * The context is used to inflate the layout file, and the List is the data we want
     * to populate into the lists
     *
     * @param movieItems    ArrayList of Movies
     * @param clickHandler The on-click handler for this adapter. This single handler is called
     *                     when an item is clicked.
     */
    public MovieAdapter(ArrayList<Movie> movieItems, MovieAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
        this.mMovieData = movieItems;
    }



    /**
     * Cache of the children views for a forecast list item.
     */
    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //private final ImageView mMovieImageView;
        private final TextView mMovieTextView;
        //private Movie movieClicked;

        private MovieAdapterViewHolder(View view) {
            super(view);
            //mMovieImageView = view.findViewById(R.id.movie_poster);
            mMovieTextView = view.findViewById(R.id.tv_movie_title);
            view.setOnClickListener(this);
        }

        /**
         * This gets called by the child views during a click.
         *
         * @param v The View that was clicked
         */
        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mClickHandler.onClick((Movie) mMovieData.get(adapterPosition));
        }
    }

    /**
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     *
     * @param viewGroup The ViewGroup that these ViewHolders are contained within.
     * @param viewType  If your RecyclerView has more than one type of item (which ours doesn't) you
     *                  can use this viewType integer to provide a different layout. See
     *                  {@link android.support.v7.widget.RecyclerView.Adapter#getItemViewType(int)}
     *                  for more details.
     * @return A new MovieAdapterViewHolder that holds the View for each list item
     */
    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.list_item, viewGroup, false);
        return new MovieAdapterViewHolder(view);
    }

    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, we update the contents of the ViewHolder to display the movie
     * details for this particular position, using the "position" argument that is conveniently
     * passed into us.
     *
     * @param movieAdapterViewHolder The ViewHolder which should be updated to represent the
     *                                  contents of the item at the given position in the data set.
     * @param position                  The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(MovieAdapterViewHolder movieAdapterViewHolder, int position) {
        Movie movieClicked = (Movie) mMovieData.get(position);
        if(position < 2) {
            Log.v("MovieAdapter", movieClicked.toString());
        }
        movieAdapterViewHolder.mMovieTextView.setText(movieClicked.toString());
        /*mClickHandler.onClick(movieClicked);*/

        /*Picasso.get()
                .load(movieClicked.getLinkMoviePoster("w185"))
                .into(movieAdapterViewHolder.mMovieImageView);
        */
    }

    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available in our movie list
     */
    @Override
    public int getItemCount() {
        return mMovieData.size();
    }
}

