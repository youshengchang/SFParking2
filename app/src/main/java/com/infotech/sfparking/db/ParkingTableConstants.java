package com.infotech.sfparking.db;

import java.net.PortUnreachableException;

/**
 * Created by youshengchang on 4/27/15.
 */
public class ParkingTableConstants {

    public static final String DATABASE_NAME = "sfparking.db";
    public static final int DATABASE_VERSION = 2;
    public static final String TABLE_PARKING = "parking";

    public static final String COLUMN_ID = "parkingId";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DESC = "desc";
    public static final String COLUMN_INTER = "inter";
    public static final String COLUMN_TEL = "tel";
    public static final String COLUMN_OSPID = "ospid";
    public static final String COLUMN_BFID = "bfid";
    public static final String COLUMN_OCC = "occ";
    public static final String COLUMN_OPER = "oper";
    public static final String COLUMN_PTS = "pts";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_LONGITUDE = "longitude";
    public static final String COLUMN_LATITUDE2 = "latitude2";
    public static final String COLUMN_LONGITUDE2 = "longitude2";


    public static final String TABLE_PARKING_CREATE =
            "CREATE TABLE " + TABLE_PARKING + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_TYPE + " TEXT, " +
                    COLUMN_DESC + " TEXT, " +
                    COLUMN_INTER + " TEXT, " +
                    COLUMN_TEL + " TEXT, " +
                    COLUMN_OSPID + " INTEGER, " +
                    COLUMN_BFID + " INTEGER, " +
                    COLUMN_OCC + " INTEGER, " +
                    COLUMN_OPER + " INTEGER, " +
                    COLUMN_PTS + " INTEGER, " +
                    COLUMN_LATITUDE + " NUMERIC, " +
                    COLUMN_LONGITUDE + " NUMERIC, " +
                    COLUMN_LATITUDE2 + " NUMERIC, " +
                    COLUMN_LONGITUDE2 + " NUMERIC " +
                    ")";

    public static final String TABLE_OPHOURS = "ophours";
    public static final String COLUMN_HOUR_ID = "ophourId";
    public static final String COLUMN_PARKING_ID = "parkingId";
    public static final String COLUMN_FROM = "fromDay";
    public static final String COLUMN_TO = "toDay";
    public static final String COLUMN_BEG = "beg";
    public static final String COLUMN_END = "end";

    public static final String TABLE_OPHOURS_CREATE =
            "CREATE TABLE " + TABLE_OPHOURS + " (" +
             COLUMN_HOUR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
             COLUMN_PARKING_ID + " INTEGER, " +
             COLUMN_FROM + " TEXT, " +
             COLUMN_TO + " TEXT, " +
             COLUMN_BEG + " TEXT, " +
             COLUMN_END + " TEXT, " +
             "FOREIGN KEY(" + COLUMN_PARKING_ID + ")REFERENCES " + TABLE_PARKING + " (" + COLUMN_ID + ")" +
             ")";

    public static final String TABLE_RATES = "rates";
    public static final String COLUMN_RATE_ID = "rateId";
    public static final String COLUMN_RATE = "rate";
    public static final String COLUMN_RQ = "rq";
    public static final String COLUMN_RR = "rr";

    public static final String TABLE_RATES_CREATE =
            "CREATE TABLE " + TABLE_RATES + " (" +
            COLUMN_RATE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_PARKING_ID + " INTEGER, " +
            COLUMN_BEG + " TEXT, " +
            COLUMN_END + " TEXT, " +
            COLUMN_RATE + " NUMERIC, " +
            COLUMN_DESC + " TEXT, " +
            COLUMN_RQ + " TEXT, " +
            COLUMN_RR + " TEXT, " +
            "FOREIGN KEY(" + COLUMN_PARKING_ID + ")REFERENCES " + TABLE_PARKING + " (" + COLUMN_ID + ")" +
            ")";



}
