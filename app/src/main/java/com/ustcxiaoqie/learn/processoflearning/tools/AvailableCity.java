package com.ustcxiaoqie.learn.processoflearning.tools;

import java.io.Serializable;

/**
 * Created by Xiaoqie on 2017/1/9.
 * 城市列表类
 */

public class AvailableCity implements Serializable{
    private int _id;
    private String name;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "AvailableCity{" +
                "_id=" + _id +
                ", name='" + name + '\'' +
                '}';
    }


}
