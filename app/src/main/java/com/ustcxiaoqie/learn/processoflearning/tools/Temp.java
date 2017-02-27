package com.ustcxiaoqie.learn.processoflearning.tools;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/1/4 19:45.
 * Copyright (c) 2017-01-04 Bryant1993 All rights reserved.
 */


public class Temp implements Serializable{
    private int day;  //白天
    private int min;  //最低
    private int max; //最高
    private int night; //晚上
    private int eve; //最低
    private int morn; //最低

    @Override
    public String toString() {
        return "Temp{" +
                "day=" + day +
                ", min=" + min +
                ", max=" + max +
                ", night=" + night +
                ", eve=" + eve +
                ", morn=" + morn +
                '}';
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getNight() {
        return night;
    }

    public void setNight(int night) {
        this.night = night;
    }

    public int getEve() {
        return eve;
    }

    public void setEve(int eve) {
        this.eve = eve;
    }

    public int getMorn() {
        return morn;
    }

    public void setMorn(int morn) {
        this.morn = morn;
    }
}
