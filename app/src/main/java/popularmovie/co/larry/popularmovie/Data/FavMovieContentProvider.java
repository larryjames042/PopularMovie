package popularmovie.co.larry.popularmovie.Data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class FavMovieContentProvider extends ContentProvider {
    public static final int FAV = 100;
    public static final int FAV_WITH_ID = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher(){
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(FavContract.AUTHORITY, FavContract.PATH_FAV, FAV);
        uriMatcher.addURI(FavContract.AUTHORITY, FavContract.PATH_FAV + "/#" , FAV_WITH_ID);
        return uriMatcher;
    }

    private FavDbHelper mFavDbHelper;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mFavDbHelper = new FavDbHelper(context);

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db = mFavDbHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);
        Cursor cursor;
        switch (match){
            case FAV:
                cursor = db.query(
                        FavContract.FavEntry.TABLE_FAVORITE,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            default:
                throw new android.database.SQLException("Failed to query data"+ uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(),uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = mFavDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);

        Uri returnUri;

        switch (match){
            case FAV:
                long id= db.insert(FavContract.FavEntry.TABLE_FAVORITE, null, values);
                if(id>0){
                    returnUri = ContentUris.withAppendedId(FavContract.FavEntry.CONTENT_URI, id);
                }else {
                    throw new android.database.SQLException("Failed to insert row into "+ uri);

                }
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri : " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mFavDbHelper.getWritableDatabase();
        int returnRows = 0;
        int match = sUriMatcher.match(uri);
        switch (match){
            case FAV_WITH_ID :
                String id = uri.getPathSegments().get(1);
                returnRows = db.delete(FavContract.FavEntry.TABLE_FAVORITE, FavContract.FavEntry._ID + "=? " , new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri : " + uri);
        }

        if(returnRows!=0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return returnRows;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
