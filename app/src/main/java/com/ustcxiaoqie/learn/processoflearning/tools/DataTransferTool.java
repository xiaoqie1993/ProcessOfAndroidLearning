package com.ustcxiaoqie.learn.processoflearning.tools;

import com.ustcxiaoqie.learn.processoflearning.R;
import com.ustcxiaoqie.learn.processoflearning.javabeans.QQCallbackMsg;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Xiaoqie on 2017/1/5.
 */

public class DataTransferTool {
    private static int[] icons = new int[]{R.drawable.na,R.drawable.sun,
            R.drawable.snow,R.drawable.rain,R.drawable.rain_small,R.drawable.rain_heavy,
            R.drawable.rain_thunder,R.drawable.cloud,R.drawable.cloudy,R.drawable.fog};
    public static int getIconFromWeatherDetail(String weatherDetail){
        if(null == weatherDetail) return icons[0];
        if(weatherDetail.contains("sun")||weatherDetail.contains("clear sky")){
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
}
