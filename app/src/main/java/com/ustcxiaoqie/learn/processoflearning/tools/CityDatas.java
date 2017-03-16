package com.ustcxiaoqie.learn.processoflearning.tools;

import android.content.Context;
import android.support.annotation.NonNull;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by bryant on 2017/3/12.
 */

public class CityDatas {
    //获取所有城市信息
    public static List<City> getAllCityList(Context context){
        List<City> list = new ArrayList<>();
        try {
            list = DataTransferTool.getAvailableCityList(context);
        } catch (IOException e) {
            e.printStackTrace();
            LA.d(TAG,"IO异常！");
            list = null;
        } catch (JSONException e) {
            e.printStackTrace();
            LA.d(TAG,"JSON异常！");
            list = null;
        }
        return list;
    }
    //根据关键词查找可能存在的城市

    /**
     *
     * @param context
     * @param cityname 查找关键词
     * @return
     */
    public static List<City> findPossiableCity(Context context,@NonNull String cityname){
        List<City> list = getAllCityList(context);
        List<City> possiableCityList = new ArrayList<>();
        if(null == list) return null;
        possiableCityList.clear();
        for(int i = 0 ; i< list.size();i++) {
            if(list.get(i).getName().toLowerCase().contains(cityname.toLowerCase())){
                possiableCityList.add(list.get(i));
                if(possiableCityList.size()>3) break;  //只保留4个待选项
            }
        }
        return possiableCityList;
    }
}
