package com.infotech.sfparking.model;

/**
 * Created by youshengchang on 4/24/15.
 */
public class OpHour {
    private long opHourId;
    private String fromDay;
    private String toDay;
    private String beginTime;
    private String endTime;


    public long getOpHourId() {
        return opHourId;
    }

    public void setOpHourId(long opHourId) {
        this.opHourId = opHourId;
    }

    public String getFromDay() {
        return fromDay;
    }

    public void setFromDay(String fromDay) {
        this.fromDay = fromDay;
    }

    public String getToDay() {
        return toDay;
    }

    public void setToDay(String toDay) {
        this.toDay = toDay;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }


    @Override
    public String toString() {
        return "OpHour{" +
                "opHourId=" + opHourId +
                ", fromDay='" + fromDay + '\'' +
                ", toDay='" + toDay + '\'' +
                ", beginTime='" + beginTime + '\'' +
                ", endTime='" + endTime + '\'' +
                '}';
    }
}
