package com.duniyatv.duniyamovies;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.duniyatv.duniyamovies.data.DuniyaMoviesDbContract;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by ashgarhussain on 7/30/17.
 */

public class MoviesHome extends AppCompatActivity {

    private static final String RELEASE_DATE_TAG = "release_date";
    private static final String POSTER_PATH_TAG = "poster_path";
    private static final String PLOT_SYNOPSIS_TAG = "overview";
    private static final String RATING_TAG = "vote_average";
    private static final String TITLE_TAG = "title";
    private static final String MOVIE_ID_TAG = "id";
    private static final String THEMOVIEDB_API_KEY = "dummy";

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    // Constants for logging and referring to a unique loader
    private static final String TAG = MoviesHome.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_home);

        final Context context = this;

        recyclerView = (RecyclerView) findViewById(R.id.movies_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(context, 2);
        recyclerView.setLayoutManager(layoutManager);

        // If network connectivity is not present, then show a toast and stop else continue
        if (!isNetworkConnected(context)){
            showNoNetworkToast(this);
        }
        else {
            setMoviesByPopularity(context);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean returnStatement = false;
        switch (item.getItemId()){
            case R.id.highest_rated:
                Toast.makeText(getApplicationContext(),"Highest Rated Selected",Toast.LENGTH_LONG).show();
                setMoviesByRating(this);
                return true;
            case R.id.most_popular:
                Toast.makeText(getApplicationContext(),"Most Popular Selected",Toast.LENGTH_LONG).show();
                setMoviesByPopularity(this);
                return true;
            case R.id.favorites:
                //Toast.makeText(getApplicationContext(), "Favorites Selected", Toast.LENGTH_LONG).show();
                setMoviesByFavorites(this);
                return true;
            default:
                super.onOptionsItemSelected(item);
        }
        return returnStatement;
    }

    private void setMoviesByRating(final Context context){

        if (!isNetworkConnected(context)){
            showNoNetworkToast(context);
        } else {
            final Uri.Builder uriBuilder = new Uri.Builder();

            final ArrayList<Movie> popularMovies = new ArrayList<>();

            uriBuilder.scheme("http")
                    .authority("api.themoviedb.org")
                    .appendPath("3")
                    .appendPath("movie")
                    .appendPath("top_rated")
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
                                addToMovieList(response.getJSONArray("results"), popularMovies);

                                //populateMovieReviews(popularMovies);
                                if (popularMovies!=null && popularMovies.size() > 0) {
                                    int count = 0;
                                    Iterator iterator = popularMovies.iterator();
                                    while (iterator.hasNext()) {
                                        Movie movie = (Movie) iterator.next();
                                        Log.d("DEBUG", "movie[" + (count++) + "].posterPath = " + movie.getPosterPath());
                                    }
                                    adapter = new MovieRecyclerViewAdapter(context, popularMovies);
                                    recyclerView.setAdapter(adapter);
                                }
                            } catch (JSONException jsone) {
                                Log.e("ERROR", "Caught JSONException");
                                Log.e("ERROR", jsone.toString());
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    });

            jsonObjReq.setTag("DUNIYATV-jsonRequest-001");
            requestQueue.add(jsonObjReq);
        }
    }

    private void setMoviesByPopularity(final Context context){

        if (!isNetworkConnected(context)){
            showNoNetworkToast(context);
        } else {
            final Uri.Builder uriBuilder = new Uri.Builder();

            final ArrayList<Movie> popularMovies = new ArrayList<>();

            uriBuilder.scheme("http")
                    .authority("api.themoviedb.org")
                    .appendPath("3")
                    .appendPath("movie")
                    .appendPath("popular")
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
                                addToMovieList(response.getJSONArray("results"), popularMovies);

                                //populateMovieReviews(popularMovies, context);
                                if (popularMovies!=null && popularMovies.size() > 0) {
                                    int count = 0;
                                    Iterator iterator = popularMovies.iterator();
                                    while (iterator.hasNext()) {
                                        Movie movie = (Movie) iterator.next();
                                        Log.d("DEBUG", "movie[" + (count++) + "].posterPath = " + movie.getPosterPath());
                                    }
                                    adapter = new MovieRecyclerViewAdapter(context, popularMovies);
                                    recyclerView.setAdapter(adapter);
                                }
                            } catch (JSONException jsone) {
                                Log.e("ERROR", "Caught JSONException");
                                Log.e("ERROR", jsone.toString());
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    });

            jsonObjReq.setTag("DUNIYATV-jsonRequest-001");
            requestQueue.add(jsonObjReq);
        }
    }

    private void setMoviesByFavorites(final Context context){

        final String methodName = "setMoviesByFavorites()";
        Log.d(TAG+methodName, "Enter");
        if (!isNetworkConnected(context)){
            showNoNetworkToast(context);
        } else {
            Log.d(TAG+methodName, "Network is connected");
            final ArrayList<Movie> favoriteMovies = new ArrayList<>();

            Cursor cursor = getContentResolver().query(DuniyaMoviesDbContract.DuniyaMovieFavoritesEntry.CONTENT_URI,
                    null,
                    null,
                    null,
                    DuniyaMoviesDbContract.DuniyaMovieFavoritesEntry.COLUMN_MOVIE_TITLE);

            Log.d(TAG+methodName, "Cursor complete : cursor = " + cursor.toString());

            if (cursor != null){
                Log.d(TAG+methodName, "Cursor != null");
                // Iterate through db, create movie list and populate using RcyclerView
                if (cursor.moveToFirst()){
                    do {
                        String posterPath = cursor.getString(cursor.getColumnIndex(DuniyaMoviesDbContract.DuniyaMovieFavoritesEntry.COLUMN_POSTER_PATH));
                        String plotSynopsis = cursor.getString(cursor.getColumnIndex(DuniyaMoviesDbContract.DuniyaMovieFavoritesEntry.COLUMN_SYNOPSIS));
                        double rating = cursor.getDouble(cursor.getColumnIndex(DuniyaMoviesDbContract.DuniyaMovieFavoritesEntry.COLUMN_RATING));
                        String title = cursor.getString(cursor.getColumnIndex(DuniyaMoviesDbContract.DuniyaMovieFavoritesEntry.COLUMN_MOVIE_TITLE));
                        long id = cursor.getInt(cursor.getColumnIndex(DuniyaMoviesDbContract.DuniyaMovieFavoritesEntry.COLUMN_MOVIE_ID));
                        String releaseDate = cursor.getString(cursor.getColumnIndex(DuniyaMoviesDbContract.DuniyaMovieFavoritesEntry.COLUMN_MOVIE_TITLE));

                        Log.d(TAG, "Favorite ->");
                        Log.d(TAG, "posterPath = " + posterPath);
                        Log.d(TAG, "plotSynopsis = " + plotSynopsis);
                        Log.d(TAG, "rating = " + rating);
                        Log.d(TAG, "title = " + title);
                        Log.d(TAG, "id = " + id);
                        Log.d(TAG, "releaseDate = " + releaseDate);

                        Movie newMovie = new Movie(releaseDate, posterPath, plotSynopsis, rating, title, id);

                        favoriteMovies.add(newMovie);

                        if (favoriteMovies!=null && favoriteMovies.size() > 0) {
                            int count = 0;
                            Iterator iterator = favoriteMovies.iterator();
                            while (iterator.hasNext()) {
                                Movie movie = (Movie) iterator.next();
                                Log.d("DEBUG", "movie[" + (count++) + "].posterPath = " + movie.getPosterPath());
                            }
                        }
                        else {
                            Toast.makeText(this, "No Favorites Found", Toast.LENGTH_LONG).show();
                        }
                    } while (cursor.moveToNext());
                } else {
                    Log.d(TAG+methodName, "Cursor may not have returned : No moveToFirst() or no favorites");
                    Toast.makeText(this, "No Favorites Found", Toast.LENGTH_LONG).show();
                }

                adapter = new MovieRecyclerViewAdapter(context, favoriteMovies);
                recyclerView.setAdapter(adapter);
            } else {
                Toast.makeText(this, "No Favorites Found", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void addToMovieList(JSONArray jsonArray, ArrayList<Movie> movieArrayList){

        for (int i=0;i<jsonArray.length();i++){
            try{
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                String releaseDate = jsonObject.getString(RELEASE_DATE_TAG);
                String posterPath = jsonObject.getString(POSTER_PATH_TAG);
                String plotSynopsis = jsonObject.getString(PLOT_SYNOPSIS_TAG);
                double rating = Double.parseDouble(jsonObject.getString(RATING_TAG));
                String title = jsonObject.getString(TITLE_TAG);
                long id = jsonObject.getLong(MOVIE_ID_TAG);

                Log.d("DEBUG", "id = " + id);

                movieArrayList.add(new Movie(releaseDate, posterPath, plotSynopsis, rating,title, id));
            }
            catch(JSONException jsone){
                Log.e("ERROR", "Caught JSONException: "+jsone);
            }
        }
    }

    public static boolean isNetworkConnected(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
        return (activeNetworkInfo != null && activeNetworkInfo.isAvailable() && activeNetworkInfo.isConnected());
    }

    public static void showNoNetworkToast(Context context){
        String noNetworkerror = "No Network connection detected. Please check connectivity and"+
                " restart app.";
        Toast.makeText(context,noNetworkerror,Toast.LENGTH_LONG).show();
    }
}