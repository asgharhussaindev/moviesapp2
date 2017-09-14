package com.duniyatv.duniyamovies;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import java.util.List;

import static com.duniyatv.duniyamovies.MoviesHome.isNetworkConnected;
import static com.duniyatv.duniyamovies.MoviesHome.showNoNetworkToast;

/**
 * Created by ashgarhussain on 7/30/17.
 */


public class MovieRecyclerViewAdapter extends RecyclerView.Adapter<MovieRecyclerViewAdapter.ViewHolder>{

    private static final String THE_MOVIE_DB_POSTER_PREFIX = "http://image.tmdb.org/t/p/w92";

    final private List<Movie> movieList;
    final private Context movieContext;

    public MovieRecyclerViewAdapter(Context context, List<Movie> movies){
        movieList = movies;
        movieContext = context;
    }

    private Context getContext(){
        return movieContext;
    }

    @Override
    public MovieRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View movieView = inflater.inflate(R.layout.movie_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(movieView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MovieRecyclerViewAdapter.ViewHolder viewHolder, int position){

        final Movie movie = movieList.get(position);
        final ImageView imageView = viewHolder.imageView;

        Context context = viewHolder.imageView.getContext();

        String completePosterPath = THE_MOVIE_DB_POSTER_PREFIX + movie.getPosterPath();
        Log.d("DEBUG","completePosterPath = " + completePosterPath);

        if (!isNetworkConnected(context)){
            showNoNetworkToast(context);
        }
        Picasso.with(context).load(completePosterPath).into(imageView);

        viewHolder.setItem(movie);
        viewHolder.setContext(context);
    }

    @Override
    public int getItemCount(){
        return movieList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        final private ImageView imageView;
        private Movie movie;
        private Context context;

        public ViewHolder(View itemView){
            super(itemView);
            itemView.setOnClickListener(this);
            imageView = (ImageView) itemView.findViewById(R.id.movie_icon);
        }

        public void setItem(Movie movie){
            this.movie = movie;
        }

        public void setContext(Context context){
            this.context = context;
        }

        @Override
        public void onClick(View view){
            Log.d("DEBUG","Clicked Item");
            Intent intent = new Intent(context, DisplayMovieDetailsActivity.class);
            intent.putExtra("release_date", movie.getReleaseDate());
            intent.putExtra("poster_path", movie.getPosterPath());
            intent.putExtra("plot_synopsis", movie.getPlotSynopsis());
            intent.putExtra("rating", movie.getRating());
            intent.putExtra("title", movie.getTitle());
            intent.putExtra("id", movie.getId());
            this.context.startActivity(intent);
        }
    }
}