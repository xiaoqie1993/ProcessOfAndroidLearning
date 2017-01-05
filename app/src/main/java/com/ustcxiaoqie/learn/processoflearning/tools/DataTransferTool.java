package com.ustcxiaoqie.learn.processoflearning.tools;

import com.ustcxiaoqie.learn.processoflearning.R;

/**
 * Created by Xiaoqie on 2017/1/5.
 */

public class DataTransferTool {
    private static int[] icons = new int[]{R.drawable.na,R.drawable.sun,
            R.drawable.snow,R.drawable.rain,R.drawable.rain_small,R.drawable.rain_heavy,
            R.drawable.rain_thunder,R.drawable.cloud,R.drawable.cloudy,R.drawable.fog};
    public static int getIconFromWeatherDetail(String weatherDetail){
        if(null == weatherDetail) return icons[0];
        if(weatherDetail.contains("sun")){
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
}
