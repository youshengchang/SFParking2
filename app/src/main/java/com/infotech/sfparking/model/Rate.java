package com.infotech.sfparking.model;

/**
 * Created by youshengchang on 4/24/15.
 */
public class Rate {
    private long rateId;
    private String beginHour;
    private String endHour;
    private double rate;
    private String desc;
    private String rateQualifier;
    private String rateRestriction;


    public long getRateId() {
        return rateId;
    }

    public void setRateId(long rateId) {
        this.rateId = rateId;
    }

    public String getBeginHour() {
        return beginHour;
    }

    public void setBeginHour(String beginHour) {
        this.beginHour = beginHour;
    }

    public String getEndHour() {
        return endHour;
    }

    public void setEndHour(String endHour) {
        this.endHour = endHour;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getRateQualifier() {
        return rateQualifier;
    }

    public void setRateQualifier(String rateQualifier) {
        this.rateQualifier = rateQualifier;
    }

    public String getRateRestriction() {
        return rateRestriction;
    }

    public void setRateRestriction(String rateRestriction) {
        this.rateRestriction = rateRestriction;
    }

    @Override
    public String toString() {
        return "Rate{" +
                "beginHour='" + beginHour + '\'' +
                ", endHour='" + endHour + '\'' +
                ", rate=" + rate +
                ", desc='" + desc + '\'' +
                ", rateQualifier='" + rateQualifier + '\'' +
                ", rateRestriction='" + rateRestriction + '\'' +
                '}';
    }
}
