package com.duniyatv.duniyamovies.data;

/**
 * Created by ashgarhussain on 9/10/17.
 */

import android.net.Uri;
import android.provider.BaseColumns;

public class DuniyaMoviesDbContract {

    // The CONTENT PROVIDER authority
    public static final String AUTHORITY = "com.duniyatv.duniyamovies";

    // The base content URI
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    // The CONTENT PROVIDER path
    public static final String PATH_FAVORTIES = "favorites";

    /*
    The DuniyaMovies class representing the DB.

    The DB structure looks like as follows:

    favorites
    _id, movieTitle(String), movieId(long), posterPath(String), plotSynopsis(String), rating(double)
    1,"Minions",123456,"www.posterpath1.com","kids movie",65.2
    2,"John Wick",127456,"www.posterpath2.com","action movie",67.0
    3,"Hacksaw Ridge",223456,"www.posterpath3.com","war movie",90.2
    */
    public static final class DuniyaMovieFavoritesEntry implements BaseColumns {
        // Favorites content uri = base content URI + path
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORTIES).build();

        public static final String TABLE_NAME = "favorites";
        public static final String COLUMN_MOVIE_TITLE = "movieTitle";
        public static final String COLUMN_MOVIE_ID = "movieId";
        public static final String COLUMN_POSTER_PATH = "posterPath";
        public static final String COLUMN_SYNOPSIS = "synopsis";
        public static final String COLUMN_RATING = "rating";
        public static final String COLUMN_RELEASE_DATE = "releaseDate";
    }
}
