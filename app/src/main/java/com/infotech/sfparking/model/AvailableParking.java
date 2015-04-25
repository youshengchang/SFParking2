package com.infotech.sfparking.model;

import java.util.List;

/**
 * Created by youshengchang on 4/17/2015.
 */
public class AvailableParking {
    public long getParkingId() {
        return parkingId;
    }

    public void setParkingId(long parkingId) {
        this.parkingId = parkingId;
    }

    private long parkingId;
    private String type;
    private int ospid;
    private String desc;
    private int bfid;
    private String name;
    private String tel;
    private String inter;
    private int occ;
    private int oper;
    private int pts;
    private double latitude;
    private double longitude;
    private List<OpHour> opHours;
    private List<Rate> rates;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getBfid() {
        return bfid;
    }

    public void setBfid(int bfid) {
        this.bfid = bfid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOcc() {
        return occ;
    }

    public void setOcc(int occ) {
        this.occ = occ;
    }

    public int getOper() {
        return oper;
    }

    public void setOper(int oper) {
        this.oper = oper;
    }

    public int getPts() {
        return pts;
    }

    public void setPts(int pts) {
        this.pts = pts;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getOspid() {
        return ospid;
    }

    public void setOspid(int ospid) {
        this.ospid = ospid;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<OpHour> getOpHours() {
        return opHours;
    }

    public void setOpHours(List<OpHour> opHours) {
        this.opHours = opHours;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getInter() {
        return inter;
    }

    public void setInter(String inter) {
        this.inter = inter;
    }

    public List<Rate> getRates() {
        return rates;
    }

    public void setRates(List<Rate> rates) {
        this.rates = rates;
    }

    public void addOpHour(OpHour opHour){
        opHours.add(opHour);
    }

    public void addRate(Rate rate){
        rates.add(rate);
    }




    @Override
    public String toString() {
        return "AvailableParking{" +
                "parkingId=" + parkingId +
                ", type='" + type + '\'' +
                ", ospid=" + ospid +
                ", desc='" + desc + '\'' +
                ", bfid=" + bfid +
                ", name='" + name + '\'' +
                ", tel='" + tel + '\'' +
                ", inter='" + inter + '\'' +
                ", occ=" + occ +
                ", oper=" + oper +
                ", pts=" + pts +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", opHours=" + opHours +
                ", rates=" + rates +
                '}';
    }
}
