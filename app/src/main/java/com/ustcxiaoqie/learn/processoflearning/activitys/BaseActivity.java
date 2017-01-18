package com.ustcxiaoqie.learn.processoflearning.activitys;

import android.app.Activity;
import android.os.Process;
import android.view.KeyEvent;

/**
 * Created by Administrator on 2017/1/18 13:59.
 * Copyright (c) 2017-01-18 Bryant1993 All rights reserved.
 */


public class BaseActivity extends Activity {
    private long firsttime = 0;
    private final static int TIME = 1000; //连按间隔
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode){
            case KeyEvent.KEYCODE_BACK:
                if(System.currentTimeMillis() - firsttime > TIME){
                    firsttime = System.currentTimeMillis();
                }else{
                    Process.killProcess(Process.myPid()); //退出APP
                }
            break;
        }
        return true;
    }
}
