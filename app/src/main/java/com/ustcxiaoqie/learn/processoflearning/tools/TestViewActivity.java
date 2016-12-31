package com.ustcxiaoqie.learn.processoflearning.tools;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.ustcxiaoqie.learn.processoflearning.R;


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
        setContentView(R.layout.test);
        mTencent = Tencent.createInstance(Constant.APP_ID,getApplicationContext());
        listener= new BaseUiListener();
    }

    /**
     * Created by Administrator on 2016/12/31 15:28.
     * Copyright (c) 2016-12-31 Bryant1993 All rights reserved.
     */


    private  class BaseUiListener implements IUiListener {
        @Override
        public void onComplete(Object o) {

        }

        @Override
        public void onError(UiError uiError) {

        }

        @Override
        public void onCancel() {

        }
    }

    public void share(View view)
    {
        Bundle bundle = new Bundle();
        //这条分享消息被好友点击后的跳转URL。
        bundle.putString(Constants.PARAM_TARGET_URL, "http://connect.qq.com/");
        //分享的标题。注：PARAM_TITLE、PARAM_IMAGE_URL、PARAM_SUMMARY不能全为空，最少必须有一个是有值的。
        bundle.putString(Constants.PARAM_TITLE, "我在测试");
        //分享的图片URL
        bundle.putString(Constants.PARAM_IMAGE_URL, "http://img3.cache.netease.com/photo/0005/2013-03-07/8PBKS8G400BV0005.jpg");
        //分享的消息摘要，最长50个字
        bundle.putString(Constants.PARAM_SUMMARY, "测试");
        //手Q客户端顶部，替换“返回”按钮文字，如果为空，用返回代替
        bundle.putString(Constants.PARAM_APPNAME, "??我在测试");
        //标识该消息的来源应用，值为应用名称+AppId。
        bundle.putString(Constants.PARAM_APP_SOURCE, "星期几" + Constant.APP_ID);
        mTencent.shareToQQ(this, bundle , listener);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(null != mTencent){
            mTencent.onActivityResult(requestCode, resultCode, data);
        }
    }
}
