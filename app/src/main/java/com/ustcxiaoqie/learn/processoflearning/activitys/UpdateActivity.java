package com.ustcxiaoqie.learn.processoflearning.activitys;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ustcxiaoqie.learn.processoflearning.R;
import com.ustcxiaoqie.learn.processoflearning.http.HttpForUpdateLog;
import com.ustcxiaoqie.learn.processoflearning.http.HttpResultInterface;
import com.ustcxiaoqie.learn.processoflearning.tools.AppUpdateUtil;
import com.ustcxiaoqie.learn.processoflearning.tools.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/3/1 10:29.
 * Copyright (c) 2017-03-01 Bryant1993 All rights reserved.
 */


public class UpdateActivity extends BaseActivity implements HttpResultInterface,View.OnClickListener{
    private static final String TAG = "UpdateActivity";
    private TextView updateMessage_tv;
    private TextView title_tv;
    private Button updateBtn;
    private boolean hasUpdate = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        initViews();
        checkforUpdate();
        startDownload();
    }

    private void checkforUpdate() {
        HttpForUpdateLog check = new HttpForUpdateLog(Constant.APP_UPDATE_LOG, this);
        check.getResult();
    }

    private void initViews() {
        updateMessage_tv = (TextView) findViewById(R.id.updateMessage);
        title_tv = (TextView) findViewById(R.id.updateTitle);
        title_tv.setText("检查更新中");
        updateBtn = (Button) findViewById(R.id.update_btn);
        updateBtn.setOnClickListener(this);
        updateBtn.setEnabled(false);
    }

    private void startDownload() {
     /*   DownloadAPKUtil util = new DownloadAPKUtil(this, Constant.APP_UPDATE_URL,this.getFilesDir().getPath());
        util.download();*/
    }

    @Override
    public void getResult(String rs) {
        try {
            JSONObject js = new JSONObject(rs);
            int code = (int) js.get("VersionCode");
            String codeName = (String) js.get("VersionName");
            int currentCode = this.getPackageManager().getPackageInfo(this.getPackageName(),0).versionCode;
            if(currentCode < code){
                //有新的更新
                updateBtn.setEnabled(true);
                title_tv.setText("新版本"+codeName+"可用！");
                hasUpdate = true;
                update(js);
            }else{
                updateBtn.setEnabled(true);
                updateMessage_tv.setText(Html.fromHtml(Constant.UPDATE_LOG));
                title_tv.setText("版本信息");
                updateMessage_tv.setGravity(Gravity.CENTER_VERTICAL);
                updateBtn.setText("返回主页面");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    //    updateMessage_tv.setText(null == rs ? "null" : rs);
    }

    private void update(JSONObject js) throws JSONException {
        StringBuffer sb = new StringBuffer();
        sb.append("<font color ='red' ><b1>更新说明:</b1></font>" + "<br>");
        JSONArray array = js.getJSONArray("Message");
        for(int i = 0 ;i<array.length();i++){
            sb.append((i+1)+"."+(String)array.get(i)+"<br>");
        }
        updateMessage_tv.setText(Html.fromHtml(sb.toString()));
        updateBtn.setText("立即更新");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.update_btn:
                if(hasUpdate) {
                    startDownLoadAPKFile();
                }else {
                    startActivity(new Intent(UpdateActivity.this,StartActivity.class));
                }
                break;
        }
    }

    private void startDownLoadAPKFile() {
        new AppUpdateUtil(this).downFile(Constant.APP_UPDATE_URL);
    }
}
