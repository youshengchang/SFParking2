package com.infotech.sfparking.parser;

import android.util.Log;

import com.infotech.sfparking.model.AvailableParking;
import com.infotech.sfparking.model.OpHour;
import com.infotech.sfparking.model.Rate;
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
            sfParking.setStatus(objParking.optString("STATUS"));
            sfParking.setNumOfRecords(objParking.optInt("NUM_RECORDS"));
            sfParking.setMessage(objParking.optString("MESSAGE"));
            sfParking.setAvlUpdateTimeStamp(objParking.optString("AVAILABILITY_UPDATED_TIMESTAMP"));
            sfParking.setAvlRequestTimeStamp(objParking.optString("AVAILABILITY_REQUEST_TIMESTAMP"));
			JSONArray ar = new JSONArray(objParking.optString("AVL"));
			List<AvailableParking> parkingList = new ArrayList<>();
			String location;
			for (int i = 0; i < ar.length(); i++) {
				
				JSONObject obj = ar.getJSONObject(i);
                AvailableParking parking = parseParkingObj(obj);

//				AvailableParking parking = new AvailableParking();
//                parking.setName(obj.optString("NAME"));
//
//                //parking.setBfid(obj.optInt("BFID"));
//                location = obj.optString("LOC");
//                String[] temp = location.split(",");
//                Log.i("JSON", location);
//                for(int j = 0; j < temp.length; ++j){
//                    Log.i("JSON", "temp: " + temp[j]);
//
//                }
//                parking.setLongitude(Double.parseDouble(temp[0]));
//                parking.setLatitude(Double.parseDouble(temp[1]));
//                //parking.setLatitude(obj.optDouble("LAT"));
//                //parking.setLongitude(obj.optDouble("LONG"));
//
////                if(obj.optInt("OCC"))
////                    parking.setOcc(obj.optInt("OCC"));
////                if(obj.optInt("OPER") != null)
////                    parking.setOper(obj.optInt("OPER"));
////                if(obj.optInt("PTS") != null)
////                    parking.setPts(obj.optInt("PTS"));
//                String test = obj.optString("DESC");
//                Log.i("SFPARK", "test: " + test);
 //               parking.setType(obj.optString("TYPE"));
                if(parking != null){
                    Log.i("SFPARK", String.valueOf(parking));

                    parkingList.add(parking);
                }

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

    private static AvailableParking parseParkingObj(JSONObject obj) throws JSONException {
        if(obj == null)
            return null;

        AvailableParking parking = new AvailableParking();
        List<OpHour> opHourList = new ArrayList<>();
        List<Rate> rateList = new ArrayList<>();

        String name = obj.optString("NAME");
        String type = obj.optString("TYPE");
        String desc = obj.optString("DESC");
        String inter = obj.optString("INTER");
        String tel = obj.optString("TEL");
        int occ = obj.optInt("OCC");
        int ospId = obj.optInt("OSPID");
        int bfId = obj.optInt("BFID");
        int oper = obj.optInt("OPER");
        int pts = obj.optInt("PTS");
        String location = obj.optString("LOC");


        JSONObject ophours = obj.optJSONObject("OPHRS");
        JSONObject rates = obj.optJSONObject("RATES");
        JSONArray ops =null;
        JSONArray rs = null;
        JSONObject opsObject = null;
        JSONObject rsObject = null;

        if(ophours != null){
            ops = ophours.optJSONArray("OPS");
            if(ops == null)
                opsObject = ophours.optJSONObject("OPS");
        }

        if(rates != null){
            rs = rates.optJSONArray("RS");
            if(rs == null)
                rsObject = rates.optJSONObject("RS");
        }




        //if(type.equalsIgnoreCase("ON"))
        //    return null;
        //if(type.equalsIgnoreCase("OFF")){

            name = (name != null)?(name):("");
            desc = (desc != null)?(desc):("");
            inter = (inter != null)?(inter):("");
            tel = (tel != null)?(tel):("");

            String[] temp = location.split(",");
            parking.setType(type);
            parking.setName(name);
            parking.setTel(tel);
            parking.setInter(inter);
            parking.setDesc(desc);
            parking.setOcc(occ);
            parking.setOper(oper);
            parking.setOspid(ospId);
            parking.setPts(pts);
            parking.setLongitude(Double.parseDouble(temp[0]));
            parking.setLatitude(Double.parseDouble(temp[1]));
            if(type.equalsIgnoreCase("ON")) {
                parking.setLongitude(Double.parseDouble(temp[2]));
                parking.setLatitude(Double.parseDouble(temp[3]));


            }
            if(ops != null){
                for(int i = 0; i < ops.length(); i++){
                    JSONObject opHour = ops.getJSONObject(i);
                    OpHour opHourObj = paresOpHour(opHour);
                    if( opHourObj != null){
                        opHourList.add(opHourObj);
                    }

                }
            }else if(opsObject != null) {
                OpHour opHourObj = paresOpHour(opsObject);
                if( opHourObj != null){
                    opHourList.add(opHourObj);
                }

            }

            if(rs != null){
                for(int i = 0; i < rs.length(); i++){
                    JSONObject jsonRate = rs.getJSONObject(i);
                    Rate rate = parseRate(jsonRate);
                    if(rate != null){
                        rateList.add(rate);
                    }
                }
            }else if(rsObject != null){
                Rate rate = parseRate(rsObject);
                if(rate != null){
                    rateList.add(rate);
                }
            }

            parking.setRates(rateList);
            parking.setOpHours(opHourList);

        //}
        return parking;


    }

    private static Rate parseRate(JSONObject jsonRate) throws JSONException {
        if(jsonRate == null)
            return null;
        String begin = jsonRate.optString("BEG");
        String endTime = jsonRate.optString("END");
        double rate = jsonRate.optDouble("RATE");
        String desc = jsonRate.optString("DESC");
        String rq = jsonRate.optString("RQ");
        String rr = jsonRate.optString("RR");

        begin = (begin != null)?begin:("");
        endTime = (endTime != null)?endTime:("");
        desc = (desc != null)?desc:("");
        rq = (rq !=null)?rq:("");
        rr = (rr!=null)?rr:("");

        Rate rateObj = new Rate();
        rateObj.setBeginHour(begin);
        rateObj.setEndHour(endTime);
        rateObj.setDesc(desc);
        rateObj.setRate(rate);
        rateObj.setRateQualifier(rq);
        rateObj.setRateRestriction(rr);


        return rateObj;
    }

    private static OpHour paresOpHour(JSONObject jsonOpHour) throws JSONException {
        if(jsonOpHour == null)
            return null;
        String from = jsonOpHour.optString("FROM");
        String to = jsonOpHour.optString("TO");
        String begin = jsonOpHour.optString("BEG");
        String end = jsonOpHour.optString("END");

        from = (from != null)?from:("");
        to = (to != null)?to:("");
        begin = (begin != null)?begin:("");
        end = (end != null)?end:("");

        OpHour opHour = new OpHour();
        opHour.setBeginTime(begin);
        opHour.setEndTime(end);
        opHour.setFromDay(from);
        opHour.setToDay(to);

        return opHour;

    }
}
