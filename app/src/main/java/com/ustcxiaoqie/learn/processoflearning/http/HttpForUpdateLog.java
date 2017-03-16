package com.ustcxiaoqie.learn.processoflearning.http;

import android.os.AsyncTask;

import com.ustcxiaoqie.learn.processoflearning.tools.Constant;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 该类主要通过httpConneciton获取数据的InputStream和对应的返回code
 * Created by Administrator on 2017/3/1 10:32.
 * Copyright (c) 2017-03-01 Bryant1993 All rights reserved.
 */


public class HttpForUpdateLog {
    private static final String TAG = "HttpByHttpConnection";
    public static final int ERROR_CODE_SERVERERROR = 1;
    public static final int ERROR_CODE_SUCCESS = 0;
    public static final int ERROR_CODE_URLNOTILLEGAL = -1;
    public static final int ERROR_CODE_OPENCONNFAILED = -2;
    public static final int ERROR_CODE_GETRESPONSEFAILED= -3;
    private HttpResultInterface i;
    private String severUrl;
    private StringBuffer result ;

    public HttpForUpdateLog(String severUrl,HttpResultInterface i){
        this.severUrl = severUrl;
        this.i = i;
        result = new StringBuffer();
    }

    public void getResult(){
        new HttpAsync().execute();
    }
    private class HttpAsync extends AsyncTask<Void,Void,String>{
        @Override
        protected String doInBackground(Void... params) {
            try {
                URL url =new URL(Constant.APP_UPDATE_LOG);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(false);
                connection.setDoInput(true);
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(8000);
                connection.setRequestMethod("GET");
                connection.connect();
                InputStream is = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(reader);
                String line = null;
                while ((line = br.readLine()) != null) {
                    result.append(line);
                }
                br.close();
                reader.close();
                is.close();
                return result.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            i.getResult(s);
        }
    }

}
