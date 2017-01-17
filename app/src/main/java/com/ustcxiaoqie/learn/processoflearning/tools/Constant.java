package com.ustcxiaoqie.learn.processoflearning.tools;

/**
 * Created by Administrator on 2016/12/29 18:29.
 * Copyright (c) 2016-12-29 Bryant1993 All rights reserved.
 */


public class Constant {
    public final static String myAPIKey = "bca92c96087fb0d353a55aaf8dcda512";
    public final static String getUrl0 = "http://api.openweathermap.org/data/2.5/weather?&appid=bca92c96087fb0d353a55aaf8dcda512?id=";
    public final static String getUrl1 = "http://api.openweathermap.org/data/2.5/forecast/daily?APPID=bca92c96087fb0d353a55aaf8dcda512&units=metric&cnt=3&id=1796989";
    public final static String getUrl = "http://api.openweathermap.org/data/2.5/forecast/daily?APPID=bca92c96087fb0d353a55aaf8dcda512&units=metric&cnt=3&id=";
    public final static String postUrl = "http://api.openweathermap.org/data/2.5/weather";
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

    public static final int DATABASE_VERSION = 2;


    public static final String APP_VERSION = "v1.1";
}
