package com.infotech.sfparking.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.infotech.sfparking.model.AvailableParking;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by youshengchang on 4/18/2015.
 */
public class ParkingDataSource {
    public static final String LOGTAG="SFPARKING";

    ParkingDBOpenHelper dbhelper;
    SQLiteDatabase database;

    private static String[] allColumns = {

            ParkingDBOpenHelper.COLUMN_ID,
            ParkingDBOpenHelper.COLUMN_NAME,
            ParkingDBOpenHelper.COLUMN_TYPE,
            ParkingDBOpenHelper.COLUMN_LATITUDE,
            ParkingDBOpenHelper.COLUMN_LONGITUDE

    };

    public ParkingDataSource(Context context) {
        this.dbhelper = new ParkingDBOpenHelper(context);
    }

    public void open() {
        Log.i(LOGTAG, "Database opened");
        database = dbhelper.getWritableDatabase();
    }

    public void close() {
        Log.i(LOGTAG, "Database closed");
        dbhelper.close();
    }

    public AvailableParking create(AvailableParking parking) {
        ContentValues values = new ContentValues();
        values.put(ParkingDBOpenHelper.COLUMN_NAME, parking.getName());
        values.put(ParkingDBOpenHelper.COLUMN_TYPE, parking.getType());
        values.put(ParkingDBOpenHelper.COLUMN_LATITUDE, parking.getLatitude());
        values.put(ParkingDBOpenHelper.COLUMN_LONGITUDE, parking.getLongitude());
        long insertId = database.insert(ParkingDBOpenHelper.TABLE_PARKING, null, values);
        parking.setParkingId(insertId);
        return parking;
    }

    public List<AvailableParking> findAll(){

        List<AvailableParking> parkings = new ArrayList<AvailableParking>();
        AvailableParking parking;

        Cursor cursor = database.query(ParkingDBOpenHelper.TABLE_PARKING, allColumns, null, null, null,null,null);

        Log.i(LOGTAG, "Returned " + cursor.getCount() + " rows.");
        if(cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                parking = new AvailableParking();

                parking.setParkingId(cursor.getLong(cursor.getColumnIndex(ParkingDBOpenHelper.COLUMN_ID)));
                parking.setName(cursor.getString(cursor.getColumnIndex(ParkingDBOpenHelper.COLUMN_NAME)));
                parking.setType(cursor.getString(cursor.getColumnIndex(ParkingDBOpenHelper.COLUMN_TYPE)));
                parking.setLatitude(cursor.getDouble(cursor.getColumnIndex(ParkingDBOpenHelper.COLUMN_LATITUDE)));
                parking.setLongitude(cursor.getDouble(cursor.getColumnIndex(ParkingDBOpenHelper.COLUMN_LONGITUDE)));

                parkings.add(parking);
            }
        }
        return parkings;
    }

    public void deleteAll(){

        dbhelper.deleteAll(database);
    }




}
