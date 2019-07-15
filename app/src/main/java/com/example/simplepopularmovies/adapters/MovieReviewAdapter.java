package com.example.simplepopularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.simplepopularmovies.R;
import com.example.simplepopularmovies.model.MovieReview;

import java.util.ArrayList;
import java.util.List;

public class MovieReviewAdapter extends RecyclerView.Adapter<MovieReviewAdapter.MovieReviewAdapterViewHolder> {
    private List<MovieReview> mMovieReviewData = new ArrayList<MovieReview>();


    /**
     * This is our own custom constructor (it doesn't mirror a superclass constructor).
     * The context is used to inflate the layout file, and the List is the data we want
     * to populate into the lists
     *
     * @param movieReviewItems    List of MovieReviews
     */
    public MovieReviewAdapter(List<MovieReview> movieReviewItems) {
        this.mMovieReviewData = movieReviewItems;
    }



    /**
     * Cache of the children views for a forecast list item.
     */
    public class MovieReviewAdapterViewHolder extends RecyclerView.ViewHolder {
        private final TextView mMovieReviewAuthorTextView;
        private final TextView mMovieReviewContentTextView;

        private MovieReviewAdapterViewHolder(View view) {
            super(view);
            mMovieReviewAuthorTextView = view.findViewById(R.id.review_author);
            mMovieReviewContentTextView = view.findViewById(R.id.review_content);
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
    public MovieReviewAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_item_review, viewGroup, false);
        return new MovieReviewAdapterViewHolder(view);
    }

    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, we update the contents of the ViewHolder to display the movie
     * details for this particular position, using the "position" argument that is conveniently
     * passed into us.
     *
     * @param movieReviewAdapterViewHolder The ViewHolder which should be updated to represent the
     *                                  contents of the item at the given position in the data set.
     * @param position                  The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(MovieReviewAdapterViewHolder movieReviewAdapterViewHolder, int position) {
        MovieReview movieReview = (MovieReview) mMovieReviewData.get(position);
        movieReviewAdapterViewHolder.mMovieReviewAuthorTextView.setText(movieReview.getAuthor());
        movieReviewAdapterViewHolder.mMovieReviewContentTextView.setText(movieReview.getContent());
    }

    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available in our movie list
     */
    @Override
    public int getItemCount() {
        if(mMovieReviewData != null) {
            return mMovieReviewData.size();
        }else{
            return 0;
        }
    }

    public void setData(List movieReviewList){
        mMovieReviewData = movieReviewList;
        notifyDataSetChanged();
    }

}
