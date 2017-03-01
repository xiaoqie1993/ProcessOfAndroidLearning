package com.ustcxiaoqie.learn.processoflearning.activitys;

import android.os.Bundle;
import android.os.Environment;

import com.ustcxiaoqie.learn.processoflearning.R;
import com.ustcxiaoqie.learn.processoflearning.http.DownloadAPKUtil;
import com.ustcxiaoqie.learn.processoflearning.http.HttpConnectionInterface;
import com.ustcxiaoqie.learn.processoflearning.tools.Constant;
import com.ustcxiaoqie.learn.processoflearning.tools.LA;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static com.ustcxiaoqie.learn.processoflearning.http.HttpByHttpConnection.ERROR_CODE_GETRESPONSEFAILED;
import static com.ustcxiaoqie.learn.processoflearning.http.HttpByHttpConnection.ERROR_CODE_OPENCONNFAILED;
import static com.ustcxiaoqie.learn.processoflearning.http.HttpByHttpConnection.ERROR_CODE_SERVERERROR;
import static com.ustcxiaoqie.learn.processoflearning.http.HttpByHttpConnection.ERROR_CODE_SUCCESS;
import static com.ustcxiaoqie.learn.processoflearning.http.HttpByHttpConnection.ERROR_CODE_URLNOTILLEGAL;

/**
 * Created by Administrator on 2017/3/1 10:29.
 * Copyright (c) 2017-03-01 Bryant1993 All rights reserved.
 */


public class UpdateActivity extends BaseActivity implements HttpConnectionInterface {
    private static final String TAG = "UpdateActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        startDownload();
    }

    private void startDownload() {
        DownloadAPKUtil util = new DownloadAPKUtil(this, Constant.APP_UPDATE_URL);
        util.download();
    }

    @Override
    public void getInputStream(int code, Object inputStream) {
        switch(code){
            case ERROR_CODE_SERVERERROR:
                LA.w(TAG,"SERVERERROR");
              break;
            case ERROR_CODE_SUCCESS:
                LA.i(TAG,"SUCCESS");
                try {
                    getAPKFile((InputStream)inputStream);
                } catch (IOException e) {
                    e.printStackTrace();
                    LA.e(TAG,"Write to file failed!");
                }
                break;
            case ERROR_CODE_URLNOTILLEGAL:
                LA.w(TAG,"URLNOTILLEGAL");
                break;
            case ERROR_CODE_OPENCONNFAILED:
                LA.w(TAG,"OPENCONNFAILED");
                break;
            case ERROR_CODE_GETRESPONSEFAILED:
                LA.w(TAG,"OPENCONNFAILED");
                break;
        }
    }

    private void getAPKFile(InputStream inputStream) throws IOException {
        File file0 = new File(Environment.getExternalStorageDirectory(),"/test/");
        if(!file0.exists()){
            file0.mkdirs();
        }
        File file = new File(file0,"w.apk");
        if(!file.exists()){
            file.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(file);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        byte[] buffer = new byte[1024];
        int b = 0;
        while ((b = inputStream.read(buffer) )!= -1){
            bos.write(b);
        }
        bos.flush();
        bos.close();
        fos.close();
        inputStream.close();
    }
}
