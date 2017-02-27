package com.ustcxiaoqie.learn.processoflearning.tools;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.ustcxiaoqie.learn.processoflearning.R;
import com.ustcxiaoqie.learn.processoflearning.javabeans.QQCallbackMsg;

import org.apache.http.util.EncodingUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Xiaoqie on 2017/1/5.
 */

public class DataTransferTool {
    private static final String TAG = "DataTransferTool";
    private static int[] icons = new int[]{R.drawable.na,R.drawable.sun,
            R.drawable.snow,R.drawable.rain,R.drawable.rain_small,R.drawable.rain_heavy,
            R.drawable.rain_thunder,R.drawable.cloud,R.drawable.cloudy,R.drawable.fog};
    public static int getIconFromWeatherDetail(String weatherDetail){
        if(null == weatherDetail) return icons[0];
        LA.d(TAG,weatherDetail+"wwwww");
        if(weatherDetail.contains("sun")||weatherDetail.contains("clear sky")||
                weatherDetail.contains("sky is clear")){
            return icons[1];
        }else if(weatherDetail.contains("rain")){
            if(weatherDetail.contains("thunder")){
              return icons[6];
            } else if(weatherDetail.contains("light")){
                return icons[4];
            }else if(weatherDetail.contains("heavy")){
                return icons[5];
            }
            return icons[3];
        }else if(weatherDetail.contains("snow")){
            return icons[2];
        }else if(weatherDetail.contains("cloud")){
            if(weatherDetail.contains("couldy")){
                return icons[8];
            }
            return icons[7];
        }else if(weatherDetail.contains("fog")){
            return icons[9];
        }
        return icons[0];
    }

    public static QQCallbackMsg getQQMag(JSONObject jsonObject) throws JSONException {
        QQCallbackMsg callbackMsg = new QQCallbackMsg();
        callbackMsg.setRet(jsonObject.getInt("ret"));
    //    callbackMsg.setAccess_token(jsonObject.getString("access_token"));
        callbackMsg.setAuthority_cost(jsonObject.getInt("authority_cost"));
        callbackMsg.setExpires_in(jsonObject.getInt("expires_in"));
        callbackMsg.setLogin_cost(jsonObject.getInt("login_cost"));
        callbackMsg.setMsg(jsonObject.getString("msg"));
        callbackMsg.setOpenid(jsonObject.getString("openid"));
        callbackMsg.setPay_token(jsonObject.getString("pay_token"));
        callbackMsg.setPf(jsonObject.getString("pf"));
        callbackMsg.setPfkey(jsonObject.getString("pfkey"));
        callbackMsg.setQuery_authority_cost(jsonObject.getInt("query_authority_cost"));
        return callbackMsg;
    }

    public static List<City> getAvailableCityList(Context context) throws IOException, JSONException {
        List<City> list = new ArrayList<>();
        AssetManager manager = context.getAssets();
        InputStream is = manager.open("cities.txt");
        BufferedInputStream bis = new BufferedInputStream(is);
        StringBuffer sb = new StringBuffer();
        byte[] buffer = new byte[1024];
        while((bis.read(buffer) != -1)){
            sb.append(EncodingUtils.getString(buffer,"UTF-8"));

        }
        bis.close();
        is.close();
        Log.d("SearchActivity",sb.toString());
        JSONArray array =new JSONArray(sb.toString());

        for(int p = 0 ;p<array.length() ; p++){
            City city = new City();
            JSONObject object = array.getJSONObject(p);
            city.setName(object.getString("name"));
            city.setId(object.getInt("_id"));
            list.add(city);
            }
        return list;
    }


    /**
     *
     * @param list  多个城市的天气数据列表
     * @return  用于WeatherAdapter的MapList
     */
    public static List<HashMap<String,Object>> transferListToListMap(List<WeatherInfo> list){
        if (null == list || list.size() == 0) return null;
        List<HashMap<String,Object>> mapList = new ArrayList<>();
        WeatherInfo info = list.get(0);
        List<WeatherDetail> list_detail = info.getList();
        for (int s = 0; s < list_detail.size(); s++) {
            WeatherDetail detail = list_detail.get(s);
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("temp", detail.getTemp().getDay()+"℃");
            map.put("icon", DataTransferTool.getIconFromWeatherDetail(detail.getWeather().getDesciption()));
            map.put("detail", detail.getWeather().getDesciption());
            mapList.add(map);
        }
      return mapList;
    }
}
