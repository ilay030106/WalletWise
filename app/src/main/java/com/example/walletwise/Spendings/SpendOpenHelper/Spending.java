package com.example.walletwise.Spendings.SpendOpenHelper;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;

public class Spending  {
    private long id;
    private String desc;
    private double price;
    private String Type;
    private String Date;
    private String Time;
    private byte[] pic;




    public Spending(String desc, double price, String type, String date, String time,byte[] pic) {
        this.desc = desc;
        this.price = price;
        this.Type = type;
        this.Date = date;
        this.Time = time;
        this.pic = pic;


    }



    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        price = price;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public byte[] getPic() {
        return pic;
    }

    public void setPic(byte[] pic) {
        this.pic = pic;
    }


}
