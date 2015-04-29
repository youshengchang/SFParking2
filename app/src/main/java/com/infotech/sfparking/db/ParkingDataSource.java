package com.infotech.sfparking.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.infotech.sfparking.model.AvailableParking;
import com.infotech.sfparking.model.OpHour;
import com.infotech.sfparking.model.Rate;

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

            ParkingTableConstants.COLUMN_ID,
            ParkingTableConstants.COLUMN_NAME,
            ParkingTableConstants.COLUMN_TYPE,
            ParkingTableConstants.COLUMN_DESC,
            ParkingTableConstants.COLUMN_INTER,
            ParkingTableConstants.COLUMN_TEL,
            ParkingTableConstants.COLUMN_OSPID,
            ParkingTableConstants.COLUMN_BFID,
            ParkingTableConstants.COLUMN_OCC,
            ParkingTableConstants.COLUMN_OPER,
            ParkingTableConstants.COLUMN_PTS,
            ParkingTableConstants.COLUMN_TYPE,
            ParkingTableConstants.COLUMN_LATITUDE,
            ParkingTableConstants.COLUMN_LONGITUDE,
            ParkingTableConstants.COLUMN_LATITUDE2,
            ParkingTableConstants.COLUMN_LONGITUDE2

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
        values.put(ParkingTableConstants.COLUMN_NAME, parking.getName());
        values.put(ParkingTableConstants.COLUMN_TYPE, parking.getType());
        values.put(ParkingTableConstants.COLUMN_DESC, parking.getDesc());
        values.put(ParkingTableConstants.COLUMN_INTER, parking.getInter());
        values.put(ParkingTableConstants.COLUMN_TEL, parking.getTel());
        values.put(ParkingTableConstants.COLUMN_OSPID, parking.getOspid());
        values.put(ParkingTableConstants.COLUMN_BFID, parking.getBfid());
        values.put(ParkingTableConstants.COLUMN_OCC, parking.getOcc());
        values.put(ParkingTableConstants.COLUMN_OPER, parking.getOper());
        values.put(ParkingTableConstants.COLUMN_PTS, parking.getPts());
        values.put(ParkingTableConstants.COLUMN_LATITUDE, parking.getLatitude());
        values.put(ParkingTableConstants.COLUMN_LONGITUDE, parking.getLongitude());
        values.put(ParkingTableConstants.COLUMN_LATITUDE2, parking.getLatitude2());
        values.put(ParkingTableConstants.COLUMN_LONGITUDE2, parking.getLongitude2());
        long insertId = database.insert(ParkingTableConstants.TABLE_PARKING, null, values);
        parking.setParkingId(insertId);
        parking = insertOpHours(parking);
        parking = insertRates(parking);

        return parking;
    }

    private AvailableParking insertRates(AvailableParking parking) {
        List<Rate> parkingRates = parking.getRates();
        ContentValues values;
        long insertId;
        int i;


        for(Rate rate: parkingRates){
            values = new ContentValues();
            i = parkingRates.indexOf(rate);
            values.put(ParkingTableConstants.COLUMN_PARKING_ID, parking.getParkingId());
            values.put(ParkingTableConstants.COLUMN_BEG, rate.getBeginHour());
            values.put(ParkingTableConstants.COLUMN_END, rate.getEndHour());
            values.put(ParkingTableConstants.COLUMN_RATE, rate.getRate());
            values.put(ParkingTableConstants.COLUMN_DESC, rate.getDesc());
            values.put(ParkingTableConstants.COLUMN_RQ, rate.getRateQualifier());
            values.put(ParkingTableConstants.COLUMN_RR, rate.getRateRestriction());
            insertId = database.insert(ParkingTableConstants.TABLE_RATES, null, values);
            rate.setRateId(insertId);
            parkingRates.set(i, rate);



        }
        
        parking.setRates(parkingRates);
        return parking;
    }

    private AvailableParking insertOpHours(AvailableParking parking) {

        List<OpHour> operatingHours = parking.getOpHours();
        ContentValues values;
        long opHourId;
        int i;

        for(OpHour opHour: operatingHours){
            i = operatingHours.indexOf(opHour);
            values = new ContentValues();

            values.put(ParkingTableConstants.COLUMN_PARKING_ID, parking.getParkingId());
            values.put(ParkingTableConstants.COLUMN_FROM, opHour.getFromDay());
            values.put(ParkingTableConstants.COLUMN_TO, opHour.getToDay());
            values.put(ParkingTableConstants.COLUMN_BEG, opHour.getBeginTime());
            values.put(ParkingTableConstants.COLUMN_END, opHour.getEndTime());
            opHourId = database.insert(ParkingTableConstants.TABLE_OPHOURS, null, values);
            opHour.setOpHourId(opHourId);
            operatingHours.set(i, opHour);
        }

        parking.setOpHours(operatingHours);
        return parking;


    }

    public List<AvailableParking> findAll(){

        List<AvailableParking> parkings = new ArrayList<AvailableParking>();
        AvailableParking parking;

        Cursor cursor = database.query(ParkingTableConstants.TABLE_PARKING, allColumns, null, null, null,null,null);

        Log.i(LOGTAG, "Returned " + cursor.getCount() + " rows.");
        if(cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                parking = new AvailableParking();

                parking.setParkingId(cursor.getLong(cursor.getColumnIndex(ParkingTableConstants.COLUMN_ID)));
                parking.setName(cursor.getString(cursor.getColumnIndex(ParkingTableConstants.COLUMN_NAME)));
                parking.setType(cursor.getString(cursor.getColumnIndex(ParkingTableConstants.COLUMN_TYPE)));
                parking.setDesc(cursor.getString(cursor.getColumnIndex(ParkingTableConstants.COLUMN_DESC)));
                parking.setInter(cursor.getString(cursor.getColumnIndex(ParkingTableConstants.COLUMN_INTER)));
                parking.setTel(cursor.getString(cursor.getColumnIndex(ParkingTableConstants.COLUMN_TEL)));
                parking.setOspid(cursor.getInt(cursor.getColumnIndex(ParkingTableConstants.COLUMN_OSPID)));
                parking.setBfid(cursor.getInt(cursor.getColumnIndex(ParkingTableConstants.COLUMN_BFID)));
                parking.setOcc(cursor.getInt(cursor.getColumnIndex(ParkingTableConstants.COLUMN_OCC)));
                parking.setOper(cursor.getInt(cursor.getColumnIndex(ParkingTableConstants.COLUMN_OPER)));
                parking.setPts(cursor.getInt(cursor.getColumnIndex(ParkingTableConstants.COLUMN_PTS)));
                parking.setLatitude(cursor.getDouble(cursor.getColumnIndex(ParkingTableConstants.COLUMN_LATITUDE)));
                parking.setLongitude(cursor.getDouble(cursor.getColumnIndex(ParkingTableConstants.COLUMN_LONGITUDE)));
                parking.setLatitude2(cursor.getDouble(cursor.getColumnIndex(ParkingTableConstants.COLUMN_LATITUDE2)));
                parking.setLongitude2(cursor.getDouble(cursor.getColumnIndex(ParkingTableConstants.COLUMN_LONGITUDE2)));

                parkings.add(parking);
            }
        }
        return parkings;
    }

    public void deleteAll(){

        dbhelper.deleteAll(database);
    }




}
