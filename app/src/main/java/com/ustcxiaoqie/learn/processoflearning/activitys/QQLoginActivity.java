package com.ustcxiaoqie.learn.processoflearning.activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.tencent.connect.UserInfo;
import com.tencent.connect.share.QQShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.ustcxiaoqie.learn.processoflearning.R;
import com.ustcxiaoqie.learn.processoflearning.tools.Constant;
import com.ustcxiaoqie.learn.processoflearning.tools.DataTransferTool;
import com.ustcxiaoqie.learn.processoflearning.tools.LA;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Xiaoqie on 2017/1/5.
 */

public class QQLoginActivity extends Activity {
    private static final String TAG = "QQLoginActivity";
    private Tencent mTencent;
    private LoginListener listener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qqlogin);
        mTencent = Tencent.createInstance(Constant.APP_ID,getApplicationContext());
        listener = new LoginListener();
        (findViewById(R.id.qqLoginBtn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qqLogin();
            }
        });
        (findViewById(R.id.qqShareBtn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   share2QQ();
            //    share2Qzone();
                getUserInfo();
            }
        });
    }

    private void getUserInfo() {
        UserInfo info = new UserInfo(getApplicationContext(),mTencent.getQQToken());
        info.getUserInfo(new BaseApiListener());
    }

    private void share2Qzone() {
        Bundle bundle = new Bundle();
        bundle.putString(QQShare.SHARE_TO_QQ_TARGET_URL,"http://qq.com");
        bundle.putString(QQShare.SHARE_TO_QQ_TITLE,"ShareTest");
        //      bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL,"http://img1.gtimg.com/ninja/2/2017/01/ninja148366831425404.jpg");
        bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_URL,"http://wiki.connect.qq.com/%E5%88%86%E4%BA%AB%E6%B6%88%E6%81%AF%E5%88%B0qq%EF%BC%88%E5%AE%9A%E5%90%91%E5%88%86%E4%BA%AB%EF%BC%89_android_sdk");
        bundle.putString(QQShare.SHARE_TO_QQ_SUMMARY,"QQ分享接口");
        bundle.putString(QQShare.SHARE_TO_QQ_APP_NAME,"Weather"+Constant.APP_ID);
        mTencent.shareToQzone(QQLoginActivity.this,bundle,listener);
    }
    private void share2QQ() {

        Bundle bundle = new Bundle();
        bundle.putString(QQShare.SHARE_TO_QQ_TARGET_URL,"http://qq.com");
        bundle.putString(QQShare.SHARE_TO_QQ_TITLE,"ShareTest");
  //      bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL,"http://img1.gtimg.com/ninja/2/2017/01/ninja148366831425404.jpg");
        bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_URL,"http://wiki.connect.qq.com/%E5%88%86%E4%BA%AB%E6%B6%88%E6%81%AF%E5%88%B0qq%EF%BC%88%E5%AE%9A%E5%90%91%E5%88%86%E4%BA%AB%EF%BC%89_android_sdk");
        bundle.putString(QQShare.SHARE_TO_QQ_SUMMARY,"QQ分享接口");
        bundle.putString(QQShare.SHARE_TO_QQ_APP_NAME,"Weather"+Constant.APP_ID);
        mTencent.shareToQQ(QQLoginActivity.this,bundle,listener);
    }

    private void qqLogin() {
        mTencent.login(QQLoginActivity.this,"all",listener);

    }
    private class LoginListener implements IUiListener{

        @Override
        public void onComplete(Object o) {

            LA.d(TAG,o.getClass()+""); //打印结果:class org.json.JSONObject
            try {
                LA.d(TAG,o.toString()+"");
                LA.d(TAG,(DataTransferTool.getQQMag((JSONObject)o)).toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onError(UiError uiError) {

            LA.d(TAG,"Failed");
        }

        @Override
        public void onCancel() {
            LA.d(TAG,"Cancled");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mTencent.onActivityResultData(requestCode,resultCode,data,listener);
    }

    private class BaseApiListener implements IUiListener {


        @Override
        public void onComplete(Object o) {
            LA.d(TAG,o.toString());
        }

        @Override
        public void onError(UiError uiError) {
            LA.d(TAG,"bb");
        }

        @Override
        public void onCancel() {
            LA.d(TAG,"ccc");
        }
    }
}
