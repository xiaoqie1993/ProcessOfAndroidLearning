package com.ustcxiaoqie.learn.processoflearning.database;

/**
 * Created by Administrator on 2017/1/12 18:27.
 * Copyright (c) 2017-01-12 Bryant1993 All rights reserved.
 */


public class Table_Structure {
    //数据表 TABLE_FAVORATE_CITIES 字段名
    public static class TABLE_FAVORATE_CITIES{
        public static int _id;   //自动递增
        public static String city_name = "city_name"; //城市名称
        public static String city_id = "city_id";      //城市id
        public static String time_favorite = "time_favorite";  //收藏时间
    }



    private class TABLE_FAVORATE_CITIES_Version_2 {
        private int _id;   //自动递增
        private String city_name = "city_name"; //城市名称
        private String city_id = "city_id";      //城市id
        private String time_favorite = "time_favorite";  //收藏时间
    }

    private  class TABLE_FAVORATE_CITIES_Version_1 {
        private int _id;   //自动递增
        private String city_name; //城市名称
        private int city_id;      //城市id
    }


    //**********************************************************************************

    /**
     * 此表用来保存更新提示
     */
    public static class TABLE_UPDATE_NOTICE {
        public static int versionCode; //新的版本号
        public static String versionName; //新的版本名称
        public static int notice; //这个新的版本时候继续提示更新  0 不提示  1提示
    }
}
