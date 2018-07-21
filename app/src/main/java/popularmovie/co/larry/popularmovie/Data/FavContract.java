package popularmovie.co.larry.popularmovie.Data;

import android.net.Uri;
import android.provider.BaseColumns;

public class FavContract {
    public static final String AUTHORITY = "com.example.mirro.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+ AUTHORITY);
    public static final String PATH_FAV = "favouriteMovies";

    public FavContract (){}

    public static final class FavEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAV).build();

        public static final String TABLE_FAVORITE = "favouriteMovies";
        public static final String COLUMN_MOVIE_TITLE = "title";
        public static final String COLUMN_MOVIE_POSTER = "poster";
        public static final String COLUMN_MOVIE_ID = "id";
    }
}
