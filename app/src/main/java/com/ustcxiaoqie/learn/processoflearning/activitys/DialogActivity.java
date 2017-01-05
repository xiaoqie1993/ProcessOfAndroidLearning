package com.ustcxiaoqie.learn.processoflearning.activitys;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.ustcxiaoqie.learn.processoflearning.R;

/**
 * Created by Xiaoqie on 2017/1/5.
 */

public class DialogActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_activity);
        // 这里你可以进行一些等待时的操作，我这里用8秒后显示Toast代理等待操作
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){

                DialogActivity.this.finish();
                Toast.makeText(getApplicationContext(), "获取成功", Toast.LENGTH_SHORT).show();
            }
        }, 8000);
    }
}
