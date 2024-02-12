package com.example.walletwise.Earnings.EarnOpenHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Earning {
    private long id;

    private String StartDate;
    private String StartTime;
    private String endDate;
    private String endTime;
    private double money;
    private double tip;

    public Earning(String startDate, String startTime, String endDate, String endTime, double money,double tip) {
        StartDate = startDate;
        StartTime = startTime;
        this.endDate = endDate;
        this.endTime = endTime;
        this.money = money;
        this.tip=tip;
    }

    public String getStartDate() {
        return StartDate;
    }

    public void setStartDate(String startDate) {
        StartDate = startDate;
    }

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public double getTip() {
        return tip;
    }

    public void setTip(double tip) {
        this.tip = tip;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long calculateMinutes() {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm dd/MM/yyyy");
        long start = 0;
        long end = 0;
        try {
            start = format.parse(getStartTime() + " " + getStartDate()).getTime();
            end = format.parse(getEndTime() + " " + getEndDate()).getTime();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return (end - start) /1000 / 60;
    }

}
