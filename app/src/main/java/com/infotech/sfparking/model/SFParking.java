package com.infotech.sfparking.model;

import java.util.List;

/**
 * Created by youshengchang on 4/17/2015.
 */
public class SFParking {
    private String status;
    private int numOfRecords;
    private String message;
    private String avlUpdateTimeStamp;
    private String avlRequestTimeStamp;
    private List<AvailableParking> parkingList;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getNumOfRecords() {
        return numOfRecords;
    }

    public void setNumOfRecords(int numOfRecords) {
        this.numOfRecords = numOfRecords;
    }

    public String getAvlUpdateTimeStamp() {
        return avlUpdateTimeStamp;
    }

    public void setAvlUpdateTimeStamp(String avlUpdateTimeStamp) {
        this.avlUpdateTimeStamp = avlUpdateTimeStamp;
    }

    public String getAvlRequestTimeStamp() {
        return avlRequestTimeStamp;
    }

    public void setAvlRequestTimeStamp(String avlRequestTimeStamp) {
        this.avlRequestTimeStamp = avlRequestTimeStamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public List<AvailableParking> getParkingList() {
        return parkingList;
    }

    public void setParkingList(List<AvailableParking> parkingList) {
        this.parkingList = parkingList;
    }

    public void addParking(AvailableParking parking){
        this.parkingList.add(parking);
    }

}
