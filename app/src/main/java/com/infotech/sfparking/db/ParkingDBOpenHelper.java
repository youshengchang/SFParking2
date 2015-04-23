package com.infotech.sfparking.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by youshengchang on 4/18/2015.
 */
public class ParkingDBOpenHelper extends SQLiteOpenHelper {

    private static final String LOGTAG = "SFPARKING";

    private static final String DATABASE_NAME = "sfparking.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_PARKING = "parking";
    public static final String COLUMN_ID = "parkingId";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_LONGITUDE = "longitude";

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_PARKING + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_TYPE + " TEXT, " +
                    COLUMN_LATITUDE + " NUMERIC, " +
                    COLUMN_LONGITUDE + " NUMERIC " +
                    ")";

    public ParkingDBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PARKING);
        onCreate(db);
    }

    public void deleteAll(SQLiteDatabase db){
        db.execSQL("DELETE FROM " + TABLE_PARKING);
    }
}
