package com.ustcxiaoqie.learn.processoflearning.tools;


import android.util.Log;

/**
 * Created by Administrator on 2016/12/6 18:25.
 * Copyright (c) 2016-12-06 Bryant1993 All rights reserved.
 */


public class LA {
    private static boolean isDebug = true;
    //防止该类被实例化
    private LA(){
    }
    public static void i(String TAG,String msg) {
        if (isDebug) {
            Log.i(TAG, msg);
        }
    }
    public static void d(String TAG,String msg) {
        if (isDebug) {
            Log.d(TAG, msg);
        }
    }
    public static void w(String TAG,String msg){
        if(isDebug){
            Log.w(TAG,msg);
        }
    }
    public static void e(String TAG,String msg){
        if(isDebug){
            Log.e(TAG,msg);
        }
    }
}
