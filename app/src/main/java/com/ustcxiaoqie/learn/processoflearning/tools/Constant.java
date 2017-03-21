package com.ustcxiaoqie.learn.processoflearning.tools;

/**
 * Created by Administrator on 2016/12/29 18:29.
 * Copyright (c) 2016-12-29 Bryant1993 All rights reserved.
 */


public class Constant {
    //天气数据获取地址
    public final static String DAILY_URL = "http://api.openweathermap.org/data/2.5/forecast/daily?APPID=bca92c96087fb0d353a55aaf8dcda512&units=metric&cnt=3&id=";
    //APP更新地址
    public final static String APP_UPDATE_URL = "http://home.ustc.edu.cn/~zt9307/app.apk";
    public final static String APP_UPDATE_LOG = "http://home.ustc.edu.cn/~zt9307/log.txt";
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
    public static final int DATABASE_VERSION = 3;
    public static final String APP_VERSION = "1.3.6";
    public static final String UPDATE_LOG = "<center><font color = '#FF7F24'>Weather已为最新版本!</font></center><br>" +
            "<center><font color = 'green'>当前版本:"+ APP_VERSION + "</font></center><br>"
            + "<font color = '#DAA520'>更新日期:2017年3月21日<br>"+"</font><br>"
            + "<font color = 'blue'>更新内容:<br>" + "1.修复一些小问题<br>2.App更新测试<font><br>"
            + "<font color = 'blue'>近期更新:<br>" + "1.修复一些小问题<br>2.App更新测试<font><br>"
            +"<font color = 'blue'>" + "3.App更新方式更新,可以在内部完成更新啦<br>4.修复同学反馈的部分问题<font>";

    //IntentFilter值
    public final static String UPDATE_BROADCAST_FILTER = "UPDATE_BROADCAST_FILTER";
}
