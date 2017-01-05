package com.ustcxiaoqie.learn.processoflearning.activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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
        (findViewById(R.id.qqLoginBtn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToLogin();
            }
        });
    }

    private void goToLogin() {
        mTencent = Tencent.createInstance(Constant.APP_ID,getApplicationContext());
        listener = new LoginListener();
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
}
