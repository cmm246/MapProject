package com.testapp.mapproject.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by chung on 12/4/16.
 */

public class ParkingSQLiteHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "parking.db";

    public static final String PARKING_TABLE_NAME = "parking";
    public static final String PARKING_COLUMN_ID = "id";
    public static final String PARKING_COLUMN_NAME = "name";
    public static final String PARKING_COLUMN_LATITUDE = "latitude";
    public static final String PARKING_COLUMN_LONGITUDE = "longitude";

    // Database creation sql statement
    private static final String CREATE_PARKING_TABLE = "create table " + PARKING_TABLE_NAME + "( "
            + PARKING_COLUMN_ID + " integer primary key, "
            + PARKING_COLUMN_NAME + " text not null, "
            + PARKING_COLUMN_LATITUDE + " real not null, "
            + PARKING_COLUMN_LONGITUDE  + " real not null);";

    public ParkingSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_PARKING_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(ParkingSQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + PARKING_TABLE_NAME);
        onCreate(db);
    }
}
