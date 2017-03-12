package com.ustcxiaoqie.learn.processoflearning.http;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.ustcxiaoqie.learn.processoflearning.tools.LA;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 该类主要通过httpConneciton获取数据的InputStream和对应的返回code
 * Created by Administrator on 2017/3/1 10:32.
 * Copyright (c) 2017-03-01 Bryant1993 All rights reserved.
 */


public class HttpByHttpConnection {
    private static final String TAG = "HttpByHttpConnection";
    public static final int ERROR_CODE_SERVERERROR = 1;
    public static final int ERROR_CODE_SUCCESS = 0;
    public static final int ERROR_CODE_URLNOTILLEGAL = -1;
    public static final int ERROR_CODE_OPENCONNFAILED = -2;
    public static final int ERROR_CODE_GETRESPONSEFAILED= -3;
    private HttpConnectionInterface mHttpConnectionInterface;
    private String SeverUrl;
    private String savedPath;
    public HttpByHttpConnection(HttpConnectionInterface httpClientMethodInterface, @NonNull String url,@NonNull String savedPath){
        this.mHttpConnectionInterface = httpClientMethodInterface;
        this.SeverUrl = url;
        this.savedPath = savedPath;
    }
    public void connect(){
        Async async = new Async();
        async.execute();
    }
    private class Async extends AsyncTask<Void,Integer[],Object[]>{
        @Override
        protected Object[] doInBackground(Void... params) {
            Object[] objects = new Object[2];
            URL url = null;
            HttpURLConnection connection = null;
            try {
                url = new URL(SeverUrl);

                connection = (HttpURLConnection) url.openConnection();
                // 设置请求方式
                connection.setRequestMethod("POST");
                // 设置编码格式
                connection.setRequestProperty("Charset", "UTF-8");
                // 设置容许输出
                connection.setDoInput(true);
            } catch (MalformedURLException e) {
                e.printStackTrace();
                objects[0] = new Integer(ERROR_CODE_URLNOTILLEGAL);
                objects[1] = null;
                return objects;
            } catch (IOException e) {
                e.printStackTrace();
                objects[0] = new Integer(ERROR_CODE_OPENCONNFAILED);
                objects[1] = null;
                return objects;
            }
            try {
                int code ;
                InputStream is;
                if(connection.getResponseCode() == 200){
                    is = connection.getInputStream();
                    int length = connection.getContentLength();
                    //保存文件大小和已下载大小
                    Integer[] progress = new Integer[2];
                    progress[0] = length;
                    progress[1] = 0;
                    publishProgress(progress);  //更新下载进度
                    File filedir = new File("/mnt/scard/t");
                    if(filedir.exists()){
                        filedir.mkdir();
                    }
                    File file = new File(savedPath,"w.apk");
                    if(!file.exists()){
                        file.createNewFile();
                    }
                    FileOutputStream fos = new FileOutputStream(file);
                    BufferedOutputStream bfos = new BufferedOutputStream(fos);
                    int b = 0;
                    byte[] buffer = new byte[1024];
                    while ((b = is.read(buffer)) != -1){
                        bfos.write(b);
                        progress[1] += b;
                        publishProgress(progress);
                    }
                    bfos.close();
                    fos.close();
                    is.close();
                    code = ERROR_CODE_SUCCESS;
                }else {
                    is = null;
                    code = ERROR_CODE_SERVERERROR;
                }
                objects[0] = new Integer(code);
                objects[1] = is;
            } catch (IOException e) {
                e.printStackTrace();
                objects[0] = new Integer(ERROR_CODE_GETRESPONSEFAILED);
                objects[1] = null;
            }
            return objects;
        }

        @Override
        protected void onProgressUpdate(Integer[]... values) {
            super.onProgressUpdate(values);
            LA.d(TAG,values[0][1]+"/"+values[0][0]);
        }

        @Override
        protected void onPostExecute(Object[] o) {
            super.onPostExecute(o);
            mHttpConnectionInterface.getInputStream((int)o[0]);
        }
    }
}
