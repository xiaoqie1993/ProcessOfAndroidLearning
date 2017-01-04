package com.ustcxiaoqie.learn.processoflearning.tools;

/**
 * Created by Administrator on 2017/1/4 19:43.
 * Copyright (c) 2017-01-04 Bryant1993 All rights reserved.
 */


public class Weather {
    private int id;  //天气代号
    private String main; //主要天气
    private String desciption; //描述
    private String icon; //天气图标

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public String getDesciption() {
        return desciption;
    }

    public void setDesciption(String desciption) {
        this.desciption = desciption;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Override
    public String toString() {
        return "Weather{" +
                "id=" + id +
                ", main='" + main + '\'' +
                ", desciption='" + desciption + '\'' +
                ", icon='" + icon + '\'' +
                '}';
    }
}
