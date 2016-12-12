package com.testapp.mapproject.sqlite;

import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.testapp.mapproject.model.Parking;

import java.util.ArrayList;
import java.util.List;

import static com.testapp.mapproject.sqlite.ParkingSQLiteHelper.PARKING_COLUMN_ID;
import static com.testapp.mapproject.sqlite.ParkingSQLiteHelper.PARKING_COLUMN_LATITUDE;
import static com.testapp.mapproject.sqlite.ParkingSQLiteHelper.PARKING_COLUMN_LONGITUDE;
import static com.testapp.mapproject.sqlite.ParkingSQLiteHelper.PARKING_COLUMN_NAME;
import static com.testapp.mapproject.sqlite.ParkingSQLiteHelper.PARKING_TABLE_NAME;

/**
 * Created by chung on 12/4/16.
 */

public class DBHelper {

    // Database fields
    private SQLiteDatabase db;
    private ParkingSQLiteHelper dbHelper;

    public DBHelper(Context context) {
        dbHelper = new ParkingSQLiteHelper(context);
    }

    // Adding new parking
    public void addParking(Parking parking) {
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PARKING_COLUMN_ID, parking.getId()); // Parking Name
        values.put(PARKING_COLUMN_NAME, parking.getName()); // Parking Name
        values.put(PARKING_COLUMN_LATITUDE, parking.getLatitude()); // Parking Phone Number
        values.put(PARKING_COLUMN_LATITUDE, parking.getLongitude()); // Parking Phone Number
        // Inserting Row
        db.insert(PARKING_TABLE_NAME, null, values);
        db.close(); // Closing database connection
    }

    // Getting one parking
    public Parking getParking(int id) {
        db = dbHelper.getReadableDatabase();

        /*Cursor cursor = db.query(
                PARKING_TABLE_NAME, // 1. table
                new String[]{PARKING_COLUMN_ID, PARKING_COLUMN_NAME, PARKING_COLUMN_LATITUDE, PARKING_COLUMN_LONGITUDE}, // 2. columns
                PARKING_COLUMN_ID + "=?", // 3. selections
                new String[]{String.valueOf(id)}, // 4. selections args
                null, // 5. group by
                null, // 6. having
                null // 7. order by
        );
        if (cursor != null)
            cursor.moveToFirst();

        Parking parking = new Parking(
                Integer.parseInt(cursor.getString(0)),
                cursor.getString(1),
                cursor.getDouble(2),
                cursor.getDouble(3)
        );*/
        // return parking
        //return parking;
        return null;
    }

    // Getting All parking
    public List<Parking> getAllParking() {
        List<Parking> parkingList = new ArrayList<Parking>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + PARKING_TABLE_NAME;
        db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Parking parking = new Parking();
                parking.setId(Integer.parseInt(cursor.getString(0)));
                parking.setName(cursor.getString(1));
                parking.setLatitude(cursor.getDouble(2));
                parking.setLongitude(cursor.getDouble(3));
                // Adding contact to list
                parkingList.add(parking);
            } while (cursor.moveToNext());
        }

        // return contact list
        return parkingList;
    }

    // Getting parking Count
    public int getParkingCount() {
        String countQuery = "SELECT * FROM " + PARKING_TABLE_NAME;
        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

    // Updating a parking
    public int updateParking(Parking parking) {
        db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PARKING_COLUMN_NAME, parking.getName());
        values.put(PARKING_COLUMN_LATITUDE, parking.getLatitude());
        values.put(PARKING_COLUMN_LONGITUDE, parking.getLongitude());

        // updating row
        return db.update(PARKING_TABLE_NAME, values, PARKING_COLUMN_ID + " = ?", new String[]{String.valueOf(parking.getId())});
    }

    // Deleting a parking
    public void deleteParking(Parking parking) {
        db = dbHelper.getWritableDatabase();
        db.delete(PARKING_TABLE_NAME, PARKING_COLUMN_ID + " = ?",
        new String[] { String.valueOf(parking.getId()) });
        db.close();
    }
}
