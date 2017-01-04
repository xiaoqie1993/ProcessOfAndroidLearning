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
        //设置City,Code,Cmt,Message参数
        weatherInfo.setCity(city);
        weatherInfo.setCode(jsonObject.getInt("cod"));
        weatherInfo.setCmt(jsonObject.getInt("cnt"));
        weatherInfo.setMessage((float) jsonObject.getDouble("message"));

        //
        JSONArray weatherList = jsonObject.getJSONArray("list");
        List<WeatherDatail> list = new ArrayList<>();
        for(int i=0;i<3;i++) {
            WeatherDatail weatherDatail = new WeatherDatail();
            JSONObject detail = weatherList.getJSONObject(i);
            weatherDatail.setDt(detail.getInt("dt"));
            weatherDatail.setHumidity(detail.getInt("humidity"));
            weatherDatail.setPressure(detail.getDouble("pressure"));
            weatherDatail.setSpeed((float) detail.getDouble("speed"));

            Temp temp = new Temp();
            JSONObject tempJSon = detail.getJSONObject("temp");
            temp.setDay((float) tempJSon.getDouble("day"));
            temp.setMax((float) tempJSon.getDouble("max"));
            temp.setMin((float) tempJSon.getDouble("min"));
            temp.setNight((float) tempJSon.getDouble("night"));
            weatherDatail.setTemp(temp);

            Weather weather = new Weather();
            JSONArray weatherArray = detail.getJSONArray("weather");
            JSONObject weatherJSon = weatherArray.getJSONObject(0);
            weather.setId(weatherJSon.getInt("id"));
            weather.setDesciption(weatherJSon.getString("description"));
            weather.setMain(weatherJSon.getString("main"));
            weather.setIcon(weatherJSon.getString("icon"));
            weatherDatail.setWeather(weather);

            list.add(weatherDatail);
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
        }
        return jsonObject;
    }
}
