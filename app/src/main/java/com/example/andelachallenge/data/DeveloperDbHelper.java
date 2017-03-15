package com.example.andelachallenge.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by codedentwickler on 3/8/17.
 */

public class DeveloperDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "developers.db";
    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_TABLE_ENTRIES_QUERY =
            "CREATE TABLE " + DeveloperContract.DeveloperEntry.TABLE_NAME +
            " ( " + DeveloperContract.DeveloperEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    DeveloperContract.DeveloperEntry.COLUMN_DEVELOPER_USERNAME + " TEXT NOT NULL UNIQUE, " +
                    DeveloperContract.DeveloperEntry.COLUMN_DEVELOPER_GITHUB_PROFILE_URL + " TEXT NOT NULL, " +
                    DeveloperContract.DeveloperEntry.COLUMN_DEVELOPER_IMAGE_URL + " TEXT NOT NULL "+");";

    private static final String DROP_TABLE_QUERY =
            " DROP TABLE " + DeveloperContract.DeveloperEntry.TABLE_NAME+ ";";

    public DeveloperDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_ENTRIES_QUERY);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL(DROP_TABLE_QUERY);
        onCreate(sqLiteDatabase);
    }
}
