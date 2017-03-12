package com.ustcxiaoqie.learn.processoflearning.tools;

/**
 * Created by Administrator on 2016/12/29 18:29.
 * Copyright (c) 2016-12-29 Bryant1993 All rights reserved.
 */


public class Constant {
    //天气数据获取地址
    public final static String DAILY_URL = "http://api.openweathermap.org/data/2.5/forecast/daily?APPID=bca92c96087fb0d353a55aaf8dcda512&units=metric&cnt=3&id=";
    public final static String MORE_DAYS_URL = "http://api.openweathermap.org/data/2.5/forecast?&appid=bca92c96087fb0d353a55aaf8dcda512&units=metric&cnt=3&id=";
    //APP更新地址
    public final static String APP_UPDATE_URL0 = "http://bryant1993.vicp.io:8080/KuaWeb/app.apk";
    public final static String APP_UPDATE_URL = "http://202.38.71.174:8080/KuaWeb/app.apk";
    public final static String GET_WEATHER_BY_CITYID =
            "http://openweathermap.org/city/";  //后  +  cityid

    public final static String APP_ID = "101372267";
    public final static String DIALOG_ACTIVITY_FINISH = "START_OR_FINISH";
    public final static String PROGRESS = "PROGRESS";
    public final static String KEY_BUNDLE_CITY_OBJECT = "CITY";


    public final static int PROGRESS_START = 0;
    public final static int PROGRESS_HALF = 50;
    public final static int PROGRESS_FINISH = 100;
    public final static int PROGRESS_FAILED = -1;
    //版本控制
    public static final int DATABASE_VERSION = 2;
    public static final String APP_VERSION = "v1.3.3";
}
