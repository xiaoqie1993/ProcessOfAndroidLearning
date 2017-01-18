package com.ustcxiaoqie.learn.processoflearning.activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

import com.tencent.connect.share.QQShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.ustcxiaoqie.learn.processoflearning.R;
import com.ustcxiaoqie.learn.processoflearning.tools.Constant;
import com.ustcxiaoqie.learn.processoflearning.tools.LA;
import com.ustcxiaoqie.learn.processoflearning.tools.Weather;
import com.ustcxiaoqie.learn.processoflearning.tools.WeatherDetail;
import com.ustcxiaoqie.learn.processoflearning.tools.WeatherInfo;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Xiaoqie on 2017/1/8.
 */

public class ShareActivity extends Activity implements View.OnClickListener{
    private static final String TAG = "ShareActivity";
    private WeatherInfo mWeatherInfo;
    private Tencent mTencent;
    private ResultListener listener;
    private int flag = 0;
    private int position;
    private final static int FLAG_SHARE_TO_QQ = 1;
    private final static int FLAG_SHARE_TO_QZONE = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager.LayoutParams params  = getWindow().getAttributes();
        params.width  = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT/2;
        params.gravity = Gravity.BOTTOM;
        getWindow().setAttributes(params);
        setContentView(R.layout.activity_share);
        ImageButton share2qq = (ImageButton) findViewById(R.id.share_qq_Btn);
        share2qq.setOnClickListener(this);
        ImageButton share2qzone = (ImageButton) findViewById(R.id.share_qzone_Btn);
        share2qzone.setOnClickListener(this);

        mTencent = Tencent.createInstance(Constant.APP_ID,getApplicationContext());
        listener = new ResultListener();

        Intent i  = getIntent();
        if(null != i){
            try {
                mWeatherInfo = (WeatherInfo) i.getSerializableExtra("weather");
                position = i.getIntExtra("position",0);
                share2qq.setEnabled(true);
                share2qzone.setEnabled(true);
            }catch (Exception e){
                LA.e(TAG,"传递失败！");
                showToasts("获取数据失败！请稍后重试");
                share2qq.setEnabled(false);
                share2qzone.setEnabled(false);
                return;
            }
        }
    }

    @Override
    public void onClick(View v) {
        if(null == mWeatherInfo){
            showToasts("获取数据失败！");
            return;
        }
        switch (v.getId()){
            case R.id.share_qq_Btn:
                flag = FLAG_SHARE_TO_QQ;
                share2QQ();
                break;
            case R.id.share_qzone_Btn:
                flag = FLAG_SHARE_TO_QZONE;
                share2Qzone();
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.overridePendingTransition(R.anim.activity_close,0);
    }
    private void share2Qzone() {
        WeatherDetail detail = mWeatherInfo.getList().get(position);
        Weather weather = detail.getWeather();
        Bundle bundle = new Bundle();
        bundle.putString(QQShare.SHARE_TO_QQ_TARGET_URL,Constant.GET_WEATHER_BY_CITYID+mWeatherInfo.getCity().getId());
        bundle.putString(QQShare.SHARE_TO_QQ_TITLE,mWeatherInfo.getCity().getName()+"     "+"天气");
        //      bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL,"http://img1.gtimg.com/ninja/2/2017/01/ninja148366831425404.jpg");
        bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_URL,"http://wiki.connect.qq.com/%E5%88%86%E4%BA%AB%E6%B6%88%E6%81%AF%E5%88%B0qq%EF%BC%88%E5%AE%9A%E5%90%91%E5%88%86%E4%BA%AB%EF%BC%89_android_sdk");
        bundle.putString(QQShare.SHARE_TO_QQ_SUMMARY,weather.getDesciption()+"\n"+weather.getMain());
        bundle.putString(QQShare.SHARE_TO_QQ_APP_NAME,"Weather"+ Constant.APP_ID);
        mTencent.shareToQzone(ShareActivity.this,bundle,listener);
    }

    private void share2QQ() {
        Bundle bundle = new Bundle();
        WeatherDetail detail = mWeatherInfo.getList().get(0);
        Weather weather = detail.getWeather();
        bundle.putString(QQShare.SHARE_TO_QQ_TARGET_URL,Constant.GET_WEATHER_BY_CITYID+mWeatherInfo.getCity().getId());
        bundle.putString(QQShare.SHARE_TO_QQ_TITLE,mWeatherInfo.getCity().getName()+"     "+"天气");
        //      bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL,"http://img1.gtimg.com/ninja/2/2017/01/ninja148366831425404.jpg");
        bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_URL,"http://wiki.connect.qq.com/%E5%88%86%E4%BA%AB%E6%B6%88%E6%81%AF%E5%88%B0qq%EF%BC%88%E5%AE%9A%E5%90%91%E5%88%86%E4%BA%AB%EF%BC%89_android_sdk");
        bundle.putString(QQShare.SHARE_TO_QQ_SUMMARY,weather.getDesciption()+"\n"+weather.getMain());
        bundle.putString(QQShare.SHARE_TO_QQ_APP_NAME,"Weather"+ Constant.APP_ID);
        mTencent.shareToQQ(ShareActivity.this,bundle,listener);
    }

    private class ResultListener implements IUiListener{

    @Override
        public void onComplete(Object o) {
        int ret = -1;
            try {
            /*  QQCallbackMsg msg = DataTransferTool.getQQMag((JSONObject)o);*/
               ret = ((JSONObject)o).getInt("ret");
            } catch (JSONException e) {
                e.printStackTrace();
                LA.d(TAG,"JSONException");
            }
        if(ret ==0) {
            switch (flag) {
                case FLAG_SHARE_TO_QQ:
                    showToasts("分享到qq成功");
          //          Toast.makeText(ShareActivity.this, "分享到qq成功！", Toast.LENGTH_SHORT).show();
                    break;
                case FLAG_SHARE_TO_QZONE:
                    showToasts("分享到qq空间成功");
                    break;
            }
        }else{
            showToasts("分享失败");
        }
        }

        @Override
        public void onError(UiError uiError) {
            showToasts("分享失败\n"+"错误代码:"+uiError.errorCode);
        }
        @Override
        public void onCancel() {
            showToasts("分享已取消！");
        }
    }

    private void showToasts(String s) {
        Toast.makeText(ShareActivity.this, s,Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(null != mTencent){
            mTencent.onActivityResultData(requestCode,resultCode,data,listener);
        }
    }
}

