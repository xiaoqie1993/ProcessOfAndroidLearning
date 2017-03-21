package com.ustcxiaoqie.learn.processoflearning.services;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;

import com.ustcxiaoqie.learn.processoflearning.http.HttpForUpdateLog;
import com.ustcxiaoqie.learn.processoflearning.http.HttpResultInterface;
import com.ustcxiaoqie.learn.processoflearning.tools.Constant;
import com.ustcxiaoqie.learn.processoflearning.tools.LA;

import org.json.JSONException;
import org.json.JSONObject;


public class UpdateService extends Service implements HttpResultInterface{
    private static final String TAG = "UpdateService";

    @Override
    public void onCreate() {
        super.onCreate();
        LA.d(TAG,"onCreate");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent,int flags, int startId) {
        LA.d(TAG,"onStartCommand");
        HttpForUpdateLog check = new HttpForUpdateLog(Constant.APP_UPDATE_LOG,this);
        check.getResult();
        return START_NOT_STICKY;
    }

    @Override
    public void getResult(String rs) {
        try {
            JSONObject js = new JSONObject(rs);
            final int code = (int) js.get("VersionCode");
            LA.d(TAG,"new !!");
            final String codeName = (String) js.get("VersionName");
            int currentCode = this.getPackageManager().getPackageInfo(this.getPackageName(),0).versionCode;
            if(currentCode < code){
                Intent intent = new Intent(Constant.UPDATE_BROADCAST_FILTER);
                Bundle bundle = new Bundle();
                bundle.putString("codename",codeName);
                bundle.putInt("code",code);
                intent.putExtras(bundle);
                LocalBroadcastManager.getInstance(UpdateService.this).sendBroadcast(intent);
            }else{

            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}
