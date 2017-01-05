package com.ustcxiaoqie.learn.processoflearning.tools;

import java.util.List;

/**
 * Created by Administrator on 2017/1/4 19:34.
 * Copyright (c) 2017-01-04 Bryant1993 All rights reserved.
 */


public class WeatherInfo {
    private City city;
    private int code;
    private float message;
    private int cmt;
    private List<WeatherDetail> mList;


    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public float getMessage() {
        return message;
    }

    public void setMessage(float message) {
        this.message = message;
    }

    public int getCmt() {
        return cmt;
    }

    public void setCmt(int cmt) {
        this.cmt = cmt;
    }

    public List<WeatherDetail> getList() {
        return mList;
    }

    public void setList(List<WeatherDetail> list) {
        mList = list;
    }

    @Override
    public String toString() {
        return "WeatherInfo{" +
                "city=" + city +
                ", code=" + code +
                ", message=" + message +
                ", cmt=" + cmt +
                ", mList=" + mList +
                '}';
    }
}
