package com.ustcxiaoqie.learn.processoflearning.tools;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/1/4 19:02.
 * Copyright (c) 2017-01-04 Bryant1993 All rights reserved.
 */


public class WeatherJsonParseTool {
    private static final String TAG = "WeatherJsonParseTool";
    private String dataString;
    public WeatherJsonParseTool(String dataString){
        this.dataString = dataString;
    }
    public WeatherInfo parseDataToJSon() throws JSONException {
        JSONObject jsonObject = getJsonFromString();
        if(null == jsonObject) return null;

        WeatherInfo weatherInfo = new WeatherInfo();

        JSONObject jsonData = new JSONObject();

        //创建City对象
        City city = new City();
        JSONObject cityJSOnObject = jsonObject.getJSONObject("city");
        city.setName(cityJSOnObject.getString("name"));
        city.setId(cityJSOnObject.getInt("id"));
        //设置City,Code,Cmt,Message参数
        weatherInfo.setCity(city);
        weatherInfo.setCode(jsonObject.getInt("cod"));
        weatherInfo.setCmt(jsonObject.getInt("cnt"));
        weatherInfo.setMessage((float) jsonObject.getDouble("message"));

        //
        JSONArray weatherList = jsonObject.getJSONArray("list");
        List<WeatherDetail> list = new ArrayList<>();
        for(int i=0;i<3;i++) {
            WeatherDetail weatherDetail = new WeatherDetail();
            JSONObject detail = weatherList.getJSONObject(i);
            weatherDetail.setDt(detail.getInt("dt"));
            weatherDetail.setHumidity(detail.getInt("humidity"));
            weatherDetail.setPressure(detail.getDouble("pressure"));
            weatherDetail.setSpeed((float) detail.getDouble("speed"));

            Temp temp = new Temp();
            JSONObject tempJSon = detail.getJSONObject("temp");
            temp.setDay((float) tempJSon.getDouble("day"));
            temp.setMax((float) tempJSon.getDouble("max"));
            temp.setMin((float) tempJSon.getDouble("min"));
            temp.setNight((float) tempJSon.getDouble("night"));
            weatherDetail.setTemp(temp);

            Weather weather = new Weather();
            JSONArray weatherArray = detail.getJSONArray("weather");
            JSONObject weatherJSon = weatherArray.getJSONObject(0);
            weather.setId(weatherJSon.getInt("id"));
            weather.setDesciption(weatherJSon.getString("description"));
            weather.setMain(weatherJSon.getString("main"));
            weather.setIcon(weatherJSon.getString("icon"));
            weatherDetail.setWeather(weather);

            list.add(weatherDetail);
        }
        weatherInfo.setList(list);
        Log.d(TAG,"listsize"+list.size());
        return weatherInfo;
    }
    private JSONObject getJsonFromString(){
        JSONObject jsonObject = null;
        try{
             jsonObject= new JSONObject(dataString);
        }catch (JSONException e){
            e.printStackTrace();
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        return jsonObject;
    }
}
