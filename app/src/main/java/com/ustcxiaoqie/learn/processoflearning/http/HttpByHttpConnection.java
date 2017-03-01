package com.ustcxiaoqie.learn.processoflearning.http;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

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
    public static final int ERROR_CODE_SERVERERROR = 1;
    public static final int ERROR_CODE_SUCCESS = 0;
    public static final int ERROR_CODE_URLNOTILLEGAL = -1;
    public static final int ERROR_CODE_OPENCONNFAILED = -2;
    public static final int ERROR_CODE_GETRESPONSEFAILED= -3;
    private HttpConnectionInterface mHttpConnectionInterface;
    private String SeverUrl;
    public HttpByHttpConnection(HttpConnectionInterface httpClientMethodInterface, @NonNull String url){
        this.mHttpConnectionInterface = httpClientMethodInterface;
        this.SeverUrl = url;
    }
    public void connect(){
        Async async = new Async();
        async.execute();
    }
    private class Async extends AsyncTask<Void,Void,Object[]>{
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
        protected void onPostExecute(Object[] o) {
            super.onPostExecute(o);
            mHttpConnectionInterface.getInputStream((int)o[0],o[1]);
        }
    }
}
