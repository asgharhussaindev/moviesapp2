package com.duniyatv.duniyamovies;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.duniyatv.duniyamovies.data.DuniyaMoviesDbContract;
import com.squareup.picasso.Picasso;
import org.json.JSONException;
import org.json.JSONObject;
import android.view.ViewGroup.LayoutParams;
import android.widget.Toast;

/**
 * Created by ashgarhussain on 8/1/17.
 */

public class DisplayMovieDetailsActivity extends AppCompatActivity {

    private static final String THE_MOVIE_DB_POSTER_PREFIX = "http://image.tmdb.org/t/p/w185";
    private static final String DESCRIPTION_HEADING = "DESCRIPTION: ";
    private static final String RATING_HEADING = "POPULARITY: ";
    private static final String RELEASE_DATE_HEADING = "RELEASE DATE: ";
    private static final String REVIEW_ID_TAG = "content";
    private static final String TRAILER_KEY_TAG = "key";
    private static final String THEMOVIEDB_API_KEY = "dummy";
    private static final String GREY_LINE_COLOR ="#BDBDBD";

    Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_details);
        movie = new Movie();

        Bundle bundle = getIntent().getExtras();
        movie.setTitle(bundle.getString("title"));
        movie.setReleaseDate(bundle.getString("release_date"));
        movie.setPosterPath(bundle.getString("poster_path"));
        movie.setPlotSynopsis(bundle.getString("plot_synopsis"));
        movie.setRating(bundle.getDouble("rating"));
        movie.setId(bundle.getLong("id"));

        Log.d("DEBUG","movie.title = " + movie.getTitle());
        Log.d("DEBUG","movie.releaseDate = " + movie.getReleaseDate());
        Log.d("DEBUG","movie.posterPath = " + movie.getPosterPath());
        Log.d("DEBUG","movie.plotSynopsis = " + movie.getPlotSynopsis());
        Log.d("DEBUG","movie.rating = " + movie.getRating());
        Log.d("DEBUG","movie.id = " + Long.toString(movie.getId()));

        TextView movieTitle = (TextView) findViewById(R.id.movie_title);
        ImageView image = (ImageView) findViewById(R.id.details_image);
        TextView synopsis = (TextView) findViewById(R.id.movie_description);

        TextView rating = (TextView) findViewById(R.id.details_rating);
        TextView releaseDate = (TextView) findViewById(R.id.details_release_date);

        movieTitle.setText(movie.getTitle());
        movieTitle.setTypeface(null, Typeface.BOLD);

        String completePosterPath = THE_MOVIE_DB_POSTER_PREFIX+movie.getPosterPath();
        Picasso.with(this).load(completePosterPath).into(image);

        String plotSynopsis = DESCRIPTION_HEADING + movie.getPlotSynopsis();
        synopsis.setText(plotSynopsis);

        String ratingString = RATING_HEADING + Double.toString(movie.getRating());
        rating.setText(ratingString);

        String releaseDateString = RELEASE_DATE_HEADING + movie.getReleaseDate();
        releaseDate.setText(releaseDateString);

        populateMovieReviews(movie);
        populateMovieTrailers(movie);
    }

    /**
     * Fetches movie reviews from the TheMovieDB.com service and stores them in a list.
     * @param movie The movie that has been selected.
     */
    private void populateMovieReviews(final Movie movie){
        final Uri.Builder uriBuilder = new Uri.Builder();

        if (movie == null){}// Add set code reference here

        uriBuilder.scheme("http")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendPath("movie")
                .appendPath(Long.toString(movie.getId()))
                .appendPath("reviews")
                .appendQueryParameter("api_key", THEMOVIEDB_API_KEY)
                .build();

        Log.d("DEBUG", "uriBuilder.toString() = " + uriBuilder.toString());

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.GET, uriBuilder.toString(), null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int jsonResponseLength = response.getJSONArray("results").length();
                            String [] reviews = new String[jsonResponseLength];

                            if (jsonResponseLength > 0) {
                                for (int i = 0; i < jsonResponseLength; i++) {

                                    JSONObject jsonObject =
                                            response.getJSONArray("results").getJSONObject(i);

                                    if (jsonObject != null){
                                        String review = jsonObject.getString(REVIEW_ID_TAG);
                                        if (review != null){
                                            movie.setReview(review);
                                            reviews[i] = review;
                                        }
                                    }
                                }
                            }
                            else movie.setReview("No Review Found. Sorry");

                            displayMovieReviews(reviews);
                        } catch (JSONException jsone) {
                            Log.e("ERROR", "Caught JSONException");
                            Log.e("ERROR", jsone.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ERROR", "A volley error has occured");
                    }
                });
        jsonObjReq.setTag("DUNIYATV-jsonRequest-002");
        requestQueue.add(jsonObjReq);
    }

    /**
     * Fetches movie trailers from the TheMovieDB.com service and stores them in a list.
     * @param movie The movie that has been selected.
     */
    private void populateMovieTrailers(final Movie movie){
        final Uri.Builder uriBuilder = new Uri.Builder();

        if (movie == null){}// Add set code reference here

        uriBuilder.scheme("http")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendPath("movie")
                .appendPath(Long.toString(movie.getId()))
                .appendPath("videos")
                .appendQueryParameter("api_key", THEMOVIEDB_API_KEY)
                .build();

        Log.d("DEBUG", "uriBuilder.toString() = " + uriBuilder.toString());

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.GET, uriBuilder.toString(), null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int jsonResponseLength = response.getJSONArray("results").length();
                            String keys[] = new String[jsonResponseLength];
                            for (int i = 0; i < jsonResponseLength;i++) {
                                JSONObject jsonObject =
                                        response.getJSONArray("results").getJSONObject(i);

                                String key = jsonObject.getString(TRAILER_KEY_TAG);
                                Log.d("DEBUG", "key = " + key);
                                movie.addTrailer(key);

                                keys[i] = key;
                            }
                            displayMovieTrailers(keys);
                        } catch (JSONException jsone) {
                            Log.e("ERROR", "Caught JSONException");
                            Log.e("ERROR", jsone.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ERROR", "A volley error has occured");
                    }
                });
        jsonObjReq.setTag("DUNIYATV-jsonRequest-002");
        requestQueue.add(jsonObjReq);
    }

    /**
     * Dynamically populates the movie trailers on the details page as buttons.
     * @param keys A list of URLs that indicates trailer locations.
     */
    private void displayMovieTrailers(String [] keys) {

        final String youtubeUrl = "https://www.youtube.com/watch?v=";

        LinearLayout trailerLinearLayout =
                (LinearLayout) findViewById(R.id.trailer_linear_layout);

        // If keys are null or zero then just show a text view saying there are no trailers
        if (keys.length > 0) {
            for (int i = 0; i < keys.length; i++) {
                final String key = keys[i];
                LinearLayout movieInfoLayout = new LinearLayout(this);
                movieInfoLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                        LayoutParams.MATCH_PARENT));
                Button button = new Button(this);
                button.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                        LayoutParams.WRAP_CONTENT));
                button.setText("Trailer " + i);
                button.setTag("Trailer " + i);

                button.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view){
                        String youtubeUri = youtubeUrl + key;
                        Log.d("DEBUG","youtubeUri = " + youtubeUri);
                        Intent trailerPlaybackIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(youtubeUri));
                        Intent chooser = Intent.createChooser(trailerPlaybackIntent, "Open With");

                        if (trailerPlaybackIntent.resolveActivity(getPackageManager()) != null){
                            startActivity(chooser);
                        }
                    }
                });
                movieInfoLayout.addView(button);
                trailerLinearLayout.addView(movieInfoLayout);
            }
        }
        else{
            // Add a text view here showing that there were no trailers found
            LinearLayout noTrailerLinearLayout = new LinearLayout(this);
            noTrailerLinearLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT));
            TextView noTrailerTextView = new TextView(this);
            noTrailerTextView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT));
            noTrailerTextView.setText("No Trailers Found");

            noTrailerLinearLayout.addView(noTrailerTextView);
            trailerLinearLayout.addView(noTrailerLinearLayout);
        }
    }

    /**
     * Dynamically populates movie review on the details page as TextViews.
     * @param reviews A list of reviews stored as strings.
     */
    private void displayMovieReviews(String [] reviews) {

        LinearLayout reviewLinearLayout =
            (LinearLayout) findViewById(R.id.review_linear_layout);

        if (reviews.length > 0){
            for (int i = 0; i < reviews.length; i++){
                LinearLayout movieInfoLayout = new LinearLayout(this);
                movieInfoLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                        LayoutParams.WRAP_CONTENT));
                TextView reviewTextView = new TextView(this);
                reviewTextView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT));
                reviewTextView.setText(reviews[i]);

                movieInfoLayout.addView(reviewTextView);
                reviewLinearLayout.addView(movieInfoLayout);

                // Add a horizontal line after each review

                View horizontalLine = new View(this);
                horizontalLine.setLayoutParams(new LinearLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT, 5));
                horizontalLine.setBackgroundColor(Color.parseColor(GREY_LINE_COLOR));
                reviewLinearLayout.addView(horizontalLine);
            }
        }
        else{
            LinearLayout movieInfoLayout = new LinearLayout(this);
            movieInfoLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT));
            TextView reviewTextView = new TextView(this);
            reviewTextView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT));
            reviewTextView.setText("No Reviews Found");

            movieInfoLayout.addView(reviewTextView);
            reviewLinearLayout.addView(movieInfoLayout);
        }
    }

    public void onClickAddFavorite(View view){
        String movieTitle = movie.getTitle();
        long movieId = movie.getId();
        String posterPath = movie.getPosterPath();
        String synopsis = movie.getPlotSynopsis();
        double rating = movie.getRating();
        String releaseDate = movie.getReleaseDate();

        if (movieTitle.length() == 0){
            Toast.makeText(getBaseContext(), "ERROR: No movie title found", Toast.LENGTH_LONG).show();
            return;
        }

        if (movieId <= 0){
            Toast.makeText(getBaseContext(), "ERROR: No movie id found", Toast.LENGTH_LONG).show();
            return;
        }

        Toast.makeText(getBaseContext(), "movieTitle=" + movieTitle + "\nmovieId = " + movieId,
                Toast.LENGTH_LONG).show();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DuniyaMoviesDbContract.DuniyaMovieFavoritesEntry.COLUMN_MOVIE_TITLE, movieTitle);
        contentValues.put(DuniyaMoviesDbContract.DuniyaMovieFavoritesEntry.COLUMN_MOVIE_ID, movieId);
        contentValues.put(DuniyaMoviesDbContract.DuniyaMovieFavoritesEntry.COLUMN_POSTER_PATH, posterPath);
        contentValues.put(DuniyaMoviesDbContract.DuniyaMovieFavoritesEntry.COLUMN_SYNOPSIS, synopsis);
        contentValues.put(DuniyaMoviesDbContract.DuniyaMovieFavoritesEntry.COLUMN_RATING, rating);
        contentValues.put(DuniyaMoviesDbContract.DuniyaMovieFavoritesEntry.COLUMN_RELEASE_DATE, releaseDate);

        try {
            Uri uri = getContentResolver().insert(DuniyaMoviesDbContract.DuniyaMovieFavoritesEntry.CONTENT_URI, contentValues);
            if (uri != null){
                Toast.makeText(getBaseContext(), uri.toString()+" added as favorite", Toast.LENGTH_LONG).show();
            }
        } catch (android.database.SQLException e){
            Toast.makeText(this,"Could not add favorite", Toast.LENGTH_LONG);
        }


    }
}