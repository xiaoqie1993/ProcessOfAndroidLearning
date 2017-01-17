package com.ustcxiaoqie.learn.processoflearning.activitys;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ustcxiaoqie.learn.processoflearning.R;
import com.ustcxiaoqie.learn.processoflearning.tools.LA;
import com.ustcxiaoqie.learn.processoflearning.views.CustomDialog;

/**
 * Created by Administrator on 2017/1/17 21:38.
 * Copyright (c) 2017-01-17 Bryant1993 All rights reserved.
 */


public class CallBackActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "CallBackActivity";
    private final static int SEND_MAIL = -2;
    private EditText mEditText;  //输入问题框
    private Button mButton; //提交按钮
    private StringBuilder sb;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_callback);
        sb = new StringBuilder();
        initViews();
    }

    private void initViews() {
        TextView tv = (TextView) findViewById(R.id.deviceInfo_tv);
        sb.append("设备名称:    " + Build.MODEL + "\n");
        sb.append("API:    " + Build.VERSION.SDK + "\n");
        sb.append("Android OS:    " + Build.VERSION.RELEASE + "\n");
        tv.setText(sb.toString());
        mEditText = (EditText) findViewById(R.id.callback_ev);
        mButton = (Button) findViewById(R.id.callback_btn);
        mButton.setOnClickListener(this);
    }

    public int sendMailByIntent(String content) {
        String[] reciver = new String[]{"bryant1993@qq.com"};
        String myCc = "907508273@qq.com";
        String mybody = content;
        Intent myIntent = new Intent(android.content.Intent.ACTION_SEND);
        myIntent.setType("plain/text");
        myIntent.putExtra(android.content.Intent.EXTRA_EMAIL, reciver);
        myIntent.putExtra(android.content.Intent.EXTRA_CC, myCc);
        myIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Weather测试问题反馈");
        myIntent.putExtra(android.content.Intent.EXTRA_TEXT, mybody);
        startActivityForResult(Intent.createChooser(myIntent, "Weather App问题反馈"),SEND_MAIL);
        return 0;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.callback_btn:
                String mContent = mEditText.getText().toString() ;
                LA.d("MainActivity","   "+mContent);
                if (null == mContent || "".equals(mContent)) {
                    CustomDialog.Builder builder = new CustomDialog.Builder(this);
                    CustomDialog dialog = builder.setTitle("FATAL　EXCEPTION !")
                            .setMessage("请简要描述您的问题。")
                            .setPositiveButton("好", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setNegativeButton(null, null)
                            .create();
                    dialog.show();
                    break;
                }
                 sendMailByIntent(sb.toString()+"\n"+mContent);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      /*  *//*super.onActivityResult(requestCode, resultCode, data);*//*
        Toast.makeText(CallBackActivity.this,"code！"+resultCode,Toast.LENGTH_SHORT).show();
        if(requestCode == SEND_MAIL ){
            LA.d(TAG,"success!");
            if(resultCode == RESULT_OK){
                Toast.makeText(CallBackActivity.this,"感谢反馈！",Toast.LENGTH_SHORT).show();
            }else if (resultCode == RESULT_CANCELED){
                Toast.makeText(CallBackActivity.this,"反馈已取消发送！",Toast.LENGTH_SHORT).show();
            }
        }*/
    }
}
