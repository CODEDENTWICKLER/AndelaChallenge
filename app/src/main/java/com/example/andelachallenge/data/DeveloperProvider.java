package com.example.andelachallenge.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.example.andelachallenge.data.DeveloperContract.DeveloperEntry;

/**
 * Created by codedentwickler on 3/8/17.
 */

public class DeveloperProvider extends ContentProvider {

    private DeveloperDbHelper mDbHelper;

    private static final int DEVELOPERS = 10;
    private static final int DEVELOPERS_ID = 11;

    private static UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(DeveloperContract.AUTHORITY, DeveloperEntry.TABLE_NAME, DEVELOPERS);
        sUriMatcher.addURI(DeveloperContract.AUTHORITY, DeveloperEntry.TABLE_NAME + "/#",DEVELOPERS_ID);

    }

    @Override
    public boolean onCreate() {

        mDbHelper = new DeveloperDbHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String s1) {

        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        Cursor cursor;

        int match = sUriMatcher.match(uri);

        switch (match){

            case DEVELOPERS:
                cursor = database.query(DeveloperEntry.TABLE_NAME, null, null, null, null,null,null);
                break;

            default:
                throw new IllegalArgumentException("Cannot perform query on unknown URI" +uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {

        final int match = sUriMatcher.match(uri);
        SQLiteDatabase database = mDbHelper.getWritableDatabase();


        switch (match){

            case DEVELOPERS:

                long id = database.insert(DeveloperEntry.TABLE_NAME, null, contentValues);

            getContext().getContentResolver().notifyChange(uri, null);
                return ContentUris.withAppendedId(uri, id);

            default:
                throw new IllegalArgumentException("Insertion Cannot be done on uri"+uri);

        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionsArgs) {

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        final int match = sUriMatcher.match(uri);
        switch (match){
            case DEVELOPERS:
                int id = database.delete(DeveloperEntry.TABLE_NAME, null, null);
                getContext().getContentResolver().notifyChange(uri, null);
                return id;

            case DEVELOPERS_ID:

                int id2 = database.delete(DeveloperEntry.TABLE_NAME, selection, selectionsArgs);
                getContext().getContentResolver().notifyChange(uri, null);
                return id2;
            default:
                throw new IllegalArgumentException("Deletion cannot be done on unknown uri"+uri);
        }
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {

        final int match = sUriMatcher.match(uri);
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        switch (match){
            case DEVELOPERS_ID:
                int id = database.update(DeveloperEntry.TABLE_NAME, contentValues, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);
                return id;

            default:
                throw new IllegalArgumentException("Cannot update row with unknown uri"+uri);
        }

    }

}
