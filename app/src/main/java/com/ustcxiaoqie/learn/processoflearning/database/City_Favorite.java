package com.ustcxiaoqie.learn.processoflearning.database;

/**
 * Created by Administrator on 2017/1/12 18:27.
 * Copyright (c) 2017-01-12 Bryant1993 All rights reserved.
 */


public class City_Favorite {
    private int _id;
    private String city_name;
    private int city_id;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public int getCity_id() {
        return city_id;
    }

    public void setCity_id(int city_id) {
        this.city_id = city_id;
    }
}
