package com.ustcxiaoqie.learn.processoflearning.activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.ustcxiaoqie.learn.processoflearning.R;
import com.ustcxiaoqie.learn.processoflearning.javabeans.QQCallbackMsg;


/**
 * Created by Xiaoqie on 2016/12/11.
 * Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.
 */
public class TestViewActivity extends Activity{
    private Tencent mTencent;
    private BaseUiListener listener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testviews);
       /* mTencent = Tencent.createInstance(Constant.APP_ID,getApplicationContext());
        listener= new BaseUiListener();
        mTencent.login(TestViewActivity.this,"all",listener);*/
    }

    /**
     * Created by Administrator on 2016/12/31 15:28.
     * Copyright (c) 2016-12-31 Bryant1993 All rights reserved.
     */


    private  class BaseUiListener implements IUiListener {
        @Override
        public void onComplete(Object o) {
            QQCallbackMsg callbackMsg = new QQCallbackMsg();

        }

        @Override
        public void onError(UiError uiError) {

        }

        @Override
        public void onCancel() {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(null != mTencent){
            mTencent.onActivityResult(requestCode, resultCode, data);
        }
    }
}
