package popularmovie.co.larry.popularmovie.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FavDbHelper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "favouriteMovies.db";
    private static final int DATABASE_VERSION = 1;


    public FavDbHelper(Context context){
        super(context, DATABASE_NAME,null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_FAV_TABLE = "CREATE TABLE " + FavContract.FavEntry.TABLE_FAVORITE
                + "(" + FavContract.FavEntry._ID  + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + FavContract.FavEntry.COLUMN_MOVIE_TITLE + " TEXT NOT NULL,"
                + FavContract.FavEntry.COLUMN_MOVIE_ID + " INTEGER  NOT NULL UNIQUE,"
                + FavContract.FavEntry.COLUMN_MOVIE_POSTER + " TEXT "
                + ");";
        db.execSQL(SQL_CREATE_FAV_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ FavContract.FavEntry.TABLE_FAVORITE);
        onCreate(db);
    }
}
