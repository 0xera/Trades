package com.github.tifezh.kchartlib.chart.entity;

import java.io.Serializable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity
public class KLineEntity implements IKLine, Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float average;

    public String getDatetime() {
        return minute;
    }

    @Override
    public float getOpenPrice() {
        return open;
    }

    @Override
    public float getHighPrice() {
        return high;
    }

    @Override
    public float getLowPrice() {
        return low;
    }

    @Override
    public float getClosePrice() {
        return close;
    }

    @Override
    public float getMA5Price() {
        return MA5Price;
    }

    @Override
    public float getMA10Price() {
        return MA10Price;
    }

    @Override
    public float getMA20Price() {
        return MA20Price;
    }

    @Override
    public float getDea() {
        return dea;
    }

    @Override
    public float getDif() {
        return dif;
    }

    @Override
    public float getMacd() {
        return macd;
    }

    @Override
    public float getK() {
        return k;
    }

    @Override
    public float getD() {
        return d;
    }

    @Override
    public float getJ() {
        return j;
    }

    @Override
    public float getRsi1() {
        return rsi1;
    }

    @Override
    public float getRsi2() {
        return rsi2;
    }

    @Override
    public float getRsi3() {
        return rsi3;
    }

    @Override
    public float getUp() {
        return up;
    }

    @Override
    public float getMb() {
        return mb;
    }

    @Override
    public float getDn() {
        return dn;
    }

    @Override
    public float getVolume() {
        return volume;
    }

    @Override
    public float getMA5Volume() {
        return MA5Volume;
    }

    @Override
    public float getMA10Volume() {
        return MA10Volume;
    }

    public String minute;
    public String date;
    public float open;
    public float high;
    public float low;
    public float close;
    public float volume;

    public float MA5Price;

    public float MA10Price;

    public float MA20Price;

    public float dea;

    public float dif;

    public float macd;

    public float k;

    public float d;

    public float j;

    public float rsi1;

    public float rsi2;

    public float rsi3;

    public float up;

    public float mb;

    public float dn;

    public float MA5Volume;

    public float MA10Volume;
    public float change;
    public float changePercent;


}