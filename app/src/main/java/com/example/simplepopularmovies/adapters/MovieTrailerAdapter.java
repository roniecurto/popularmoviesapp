package com.example.simplepopularmovies.adapters;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.simplepopularmovies.R;
import com.example.simplepopularmovies.model.MovieTrailer;

import java.util.ArrayList;
import java.util.List;

public class MovieTrailerAdapter  extends RecyclerView.Adapter<MovieTrailerAdapter.MovieTrailerAdapterViewHolder>{
    private List<MovieTrailer> mMovieTrailerData = new ArrayList<MovieTrailer>();


    /**
     * This is our own custom constructor (it doesn't mirror a superclass constructor).
     * The context is used to inflate the layout file, and the List is the data we want
     * to populate into the lists
     *
     * @param movieReviewItems    List of MovieReviews
     */
    public MovieTrailerAdapter(List<MovieTrailer> movieReviewItems) {
        this.mMovieTrailerData = movieReviewItems;
    }


    /**
     * Cache of the children views for a forecast list item.
     */
    public class MovieTrailerAdapterViewHolder extends RecyclerView.ViewHolder {
        private final TextView mMovieTrailerNameTextView;
        private final Button mMovieWatchTrailerButton;

        private MovieTrailerAdapterViewHolder(View view) {
            super(view);
            mMovieTrailerNameTextView = view.findViewById(R.id.trailer_name);
            mMovieWatchTrailerButton = view.findViewById(R.id.action_watch_movie_trailer);
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
    public MovieTrailerAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_item_trailer, viewGroup, false);
        return new MovieTrailerAdapterViewHolder(view);
    }

    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, we update the contents of the ViewHolder to display the movie
     * details for this particular position, using the "position" argument that is conveniently
     * passed into us.
     *
     * @param movieTrailerAdapterViewHolder The ViewHolder which should be updated to represent the
     *                                  contents of the item at the given position in the data set.
     * @param position                  The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(MovieTrailerAdapterViewHolder movieTrailerAdapterViewHolder, int position) {
        final MovieTrailer movieTrailer = (MovieTrailer) mMovieTrailerData.get(position);

        movieTrailerAdapterViewHolder.mMovieTrailerNameTextView.setText(movieTrailer.getName());

        movieTrailerAdapterViewHolder.mMovieWatchTrailerButton.setOnClickListener(new View.OnClickListener() {
            Context context;

            @Override
            public void onClick(View view) {
                context = view.getContext();

                Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + movieTrailer.getKey()));
                Intent webIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://www.youtube.com/watch?v=" + movieTrailer.getKey()));
                try {
                    context.startActivity(appIntent);
                } catch (ActivityNotFoundException ex) {
                    context.startActivity(webIntent);
                }
            }
        });
    }

    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available in our movie list
     */
    @Override
    public int getItemCount() {
        if(mMovieTrailerData != null) {
            return mMovieTrailerData.size();
        }else{
            return 0;
        }
    }

    public void setData(List movieReviewList){
        mMovieTrailerData = movieReviewList;
        notifyDataSetChanged();
    }

}
