package com.ustcxiaoqie.learn.processoflearning.tools;

/**
 * Created by Administrator on 2017/1/4 19:40.
 * Copyright (c) 2017-01-04 Bryant1993 All rights reserved.
 */


public class WeatherDetail {
    private long dt; //时间
    private Temp mTemp; //温度
    private double pressure; //压强
    private int humidity; //湿度
    private Weather mWeather; //天气
    private float speed;  //风速

    public long getDt() {
        return dt;
    }

    public void setDt(long dt) {
        this.dt = dt;
    }

    public Temp getTemp() {
        return mTemp;
    }

    public void setTemp(Temp temp) {
        mTemp = temp;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public Weather getWeather() {
        return mWeather;
    }

    public void setWeather(Weather weather) {
        mWeather = weather;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    @Override
    public String toString() {
        return "WeatherDatail{" +
                "dt=" + dt +
                ", mTemp=" + mTemp +
                ", pressure=" + pressure +
                ", humidity=" + humidity +
                ", mWeather=" + mWeather +
                ", speed=" + speed +
                '}';
    }
}
