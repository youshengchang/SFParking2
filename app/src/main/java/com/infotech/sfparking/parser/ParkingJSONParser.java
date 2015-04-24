package com.infotech.sfparking.parser;

import android.util.Log;

import com.infotech.sfparking.model.AvailableParking;
import com.infotech.sfparking.model.SFParking;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ParkingJSONParser {
	
	public static List<AvailableParking> parseFeed(String content) {
	
		try {
            JSONObject objParking = new JSONObject(content);
            SFParking sfParking = new SFParking();
            sfParking.setStatus(objParking.getString("STATUS"));
            sfParking.setNumOfRecords(objParking.getInt("NUM_RECORDS"));
            sfParking.setMessage(objParking.getString("MESSAGE"));
            sfParking.setAvlUpdateTimeStamp(objParking.getString("AVAILABILITY_UPDATED_TIMESTAMP"));
            sfParking.setAvlRequestTimeStamp(objParking.getString("AVAILABILITY_REQUEST_TIMESTAMP"));
			JSONArray ar = new JSONArray(objParking.getString("AVL"));
			List<AvailableParking> parkingList = new ArrayList<>();
			String location;
			for (int i = 0; i < ar.length(); i++) {
				
				JSONObject obj = ar.getJSONObject(i);
				AvailableParking parking = new AvailableParking();
                parking.setName(obj.getString("NAME"));
                //parking.setBfid(obj.getInt("BFID"));
                location = obj.getString("LOC");
                String[] temp = location.split(",");
                Log.i("JSON", location);
                for(int j = 0; j < temp.length; ++j){
                    Log.i("JSON", "temp: " + temp[j]);

                }
                parking.setLongitude(Double.parseDouble(temp[0]));
                parking.setLatitude(Double.parseDouble(temp[1]));
                //parking.setLatitude(obj.getDouble("LAT"));
                //parking.setLongitude(obj.getDouble("LONG"));

//                if(obj.getInt("OCC"))
//                    parking.setOcc(obj.getInt("OCC"));
//                if(obj.getInt("OPER") != null)
//                    parking.setOper(obj.getInt("OPER"));
//                if(obj.getInt("PTS") != null)
//                    parking.setPts(obj.getInt("PTS"));
                parking.setType(obj.getString("TYPE"));
                parkingList.add(parking);
			}
			
			return parkingList;
		} catch (JSONException e) {
            Log.i("JSON", "JSON Exception: " + e.getMessage());
            if(e.getMessage().contains("OCC"))
                Log.i("JSON", "OCC type");
			//e.printStackTrace();
			return null;
		}
		
	}
}
