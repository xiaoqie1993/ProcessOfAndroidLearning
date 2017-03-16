package com.ustcxiaoqie.learn.processoflearning.services;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.IBinder;

import com.ustcxiaoqie.learn.processoflearning.http.HttpForUpdateLog;
import com.ustcxiaoqie.learn.processoflearning.http.HttpResultInterface;
import com.ustcxiaoqie.learn.processoflearning.tools.Constant;
import com.ustcxiaoqie.learn.processoflearning.views.CustomDialog;

import org.json.JSONException;
import org.json.JSONObject;

public class UpdateService extends Service implements HttpResultInterface{
    public UpdateService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent,int flags, int startId) {
        HttpForUpdateLog check = new HttpForUpdateLog(Constant.APP_UPDATE_LOG,this);
        check.getResult();
        return START_NOT_STICKY;
    }

    @Override
    public void getResult(String rs) {
        try {
            JSONObject js = new JSONObject(rs);
            int code = (int) js.get("VersionCode");
            String codeName = (String) js.get("VersionName");
            int currentCode = this.getPackageManager().getPackageInfo(this.getPackageName(),0).versionCode;
            if(currentCode < code){
                CustomDialog.Builder builder =new CustomDialog.Builder(this);
                builder.setTitle("检测到新版本"+codeName);

            }else{

            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}
