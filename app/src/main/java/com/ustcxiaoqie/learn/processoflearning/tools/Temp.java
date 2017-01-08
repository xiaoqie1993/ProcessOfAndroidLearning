package com.ustcxiaoqie.learn.processoflearning.tools;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/1/4 19:45.
 * Copyright (c) 2017-01-04 Bryant1993 All rights reserved.
 */


public class Temp implements Serializable{
    private float day;  //白天
    private float min;  //最低
    private float max; //最高
    private float night; //晚上
    private float eve; //最低
    private float morn; //最低

    public float getDay() {
        return day;
    }

    public void setDay(float day) {
        this.day = day;
    }

    public float getMin() {
        return min;
    }

    public void setMin(float min) {
        this.min = min;
    }

    public float getMax() {
        return max;
    }

    public void setMax(float max) {
        this.max = max;
    }

    public float getNight() {
        return night;
    }

    public void setNight(float night) {
        this.night = night;
    }

    public float getEve() {
        return eve;
    }

    public void setEve(float eve) {
        this.eve = eve;
    }

    public float getMorn() {
        return morn;
    }

    public void setMorn(float morn) {
        this.morn = morn;
    }

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
}
