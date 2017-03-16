package com.ustcxiaoqie.learn.processoflearning.services;

import android.app.Service;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.IBinder;
import android.view.WindowManager;

import com.ustcxiaoqie.learn.processoflearning.database.DataBaseHelper;
import com.ustcxiaoqie.learn.processoflearning.database.Table_Structure;
import com.ustcxiaoqie.learn.processoflearning.http.HttpForUpdateLog;
import com.ustcxiaoqie.learn.processoflearning.http.HttpResultInterface;
import com.ustcxiaoqie.learn.processoflearning.tools.AppUpdateUtil;
import com.ustcxiaoqie.learn.processoflearning.tools.Constant;
import com.ustcxiaoqie.learn.processoflearning.tools.LA;
import com.ustcxiaoqie.learn.processoflearning.views.CustomDialog;

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
                LA.d(TAG,"new !");
                final DataBaseHelper helper = new DataBaseHelper(this,Constant.DATABASE_VERSION);
                final SQLiteDatabase db = helper.getWritableDatabase();
                Cursor cursor = helper.queryFromAPPUPDATENOPTICE(db
                        ,new String[]{Table_Structure.TABLE_UPDATE_NOTICE.notice}
                        ,Table_Structure.TABLE_UPDATE_NOTICE.versionCode+"=?"
                        ,new String[]{String.valueOf(code)}
                        ,null,null,null);
                boolean show =true;
                if(cursor.moveToNext()){
                    //查询这个版本的信息
                    show = (cursor.getInt(2) == Table_Structure.TABLE_UPDATE_NOTICE.SHOW);
                }
                cursor.close();
                LA.d(TAG,""+show);
                if(!show){
                    helper.close();
                    return;
                }
                CustomDialog.Builder builder =new CustomDialog.Builder(this);
                builder.setTitle("检测到新版本")
                        .setMessage("新版本"+codeName+"可用")
                        .setPositiveButton("立即下载", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                new AppUpdateUtil(UpdateService.this).downFile(Constant.APP_UPDATE_URL);
                            }
                        })
                        .setNegativeButton("不再提醒", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                ContentValues values = new ContentValues();
                                values.put(Table_Structure.TABLE_UPDATE_NOTICE.versionCode,code);
                                values.put(Table_Structure.TABLE_UPDATE_NOTICE.versionName,codeName);
                                values.put(Table_Structure.TABLE_UPDATE_NOTICE.versionCode,Table_Structure.TABLE_UPDATE_NOTICE.NOT_SHOW);
                                helper.insertIntoAPPUPDATENOPTICE(db,values);
                                helper.close();
                            }
                        });
                final CustomDialog dialog = builder.create();
                dialog.getWindow().setType((WindowManager.LayoutParams.TYPE_SYSTEM_ALERT));
                final Handler handler= new Handler();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                dialog.show();
                            }
                        });
                    }
                }).start();
            }else{

            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}
