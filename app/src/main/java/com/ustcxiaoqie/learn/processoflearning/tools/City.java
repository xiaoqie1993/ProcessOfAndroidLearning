package com.ustcxiaoqie.learn.processoflearning.tools;

/**
 * Created by Administrator on 2017/1/4 19:37.
 * Copyright (c) 2017-01-04 Bryant1993 All rights reserved.
 */


public class City {
    private int id;     //城市id
    private String name; //名称
    private Coord mCoord; //经纬度
    private String country; //所在国家
    private long population;    //人口
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Coord getCoord() {
        return mCoord;
    }

    public void setCoord(Coord coord) {
        mCoord = coord;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public long getPopulation() {
        return population;
    }

    public void setPopulation(long population) {
        this.population = population;
    }

    private class Coord{
        private double lon; //经度
        private double lat;//纬度

        public Coord(double lon, double lat) {
            this.lon = lon;
            this.lat = lat;
        }

        public double getLon() {
            return lon;
        }

        public void setLon(double lon) {
            this.lon = lon;
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }
    }

    @Override
    public String toString() {
        return "City{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", mCoord=" + mCoord +
                ", country='" + country + '\'' +
                ", population=" + population +
                '}';
    }
}


