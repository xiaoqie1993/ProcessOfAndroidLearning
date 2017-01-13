package com.ustcxiaoqie.learn.processoflearning.http;

import android.os.AsyncTask;
import android.util.Log;

import com.ustcxiaoqie.learn.processoflearning.ProgressListener;
import com.ustcxiaoqie.learn.processoflearning.tools.City;
import com.ustcxiaoqie.learn.processoflearning.tools.Constant;
import com.ustcxiaoqie.learn.processoflearning.tools.WeatherInfo;
import com.ustcxiaoqie.learn.processoflearning.tools.WeatherJsonParseTool;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/29 19:28.
 * Copyright (c) 2016-12-29 Bryant1993 All rights reserved.
 */


public class WeatherHttpPost {
    private static final String TAG = "WeatherHttpPost";
    private List<City> mCityList;

    public WeatherHttpPost(List<City> cityList){
        this.mCityList = cityList;
    }
    public void getWeatherInfo(PostInterface i, ProgressListener progressListener){
        String hostUrl = Constant.getUrl;
        HttpAsyncTask task = new HttpAsyncTask(hostUrl,i,progressListener);
        task.execute("");
    }

    private class HttpAsyncTask extends AsyncTask<String,Integer,List<WeatherInfo>> {
        private String hostUrl;
        private PostInterface i;
        private List<WeatherInfo> mWeatherInfoList;  //返回的天气数据集合
        private ProgressListener mProgressListener;
        public HttpAsyncTask(String hostUrl,PostInterface i,ProgressListener progressListener) {
            this.hostUrl = hostUrl;
            this.i = i;
            this.mProgressListener = progressListener;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mWeatherInfoList = new ArrayList<>();
        }
        @Override
        protected List<WeatherInfo> doInBackground(String... params) {
            WeatherJsonParseTool tool = new WeatherJsonParseTool();
            for (int i = 0 ; i< mCityList.size();i++){
                try {
                    URI uri = new URI(hostUrl+mCityList.get(i).getId());
                    HttpGet get = new HttpGet(uri);
                    HttpClient client = getClient();
                    HttpResponse response = client.execute(get);
                    if(response.getStatusLine().getStatusCode() == 200){
                        try {
                            WeatherInfo info = tool.parseDataToJSon(EntityUtils.toString(response.getEntity()));
                            mWeatherInfoList.add(info);
                            publishProgress(i+1);
                            continue;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else{
                        Log.d(TAG,"  "+response.getStatusLine().getStatusCode());
                    }
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return mWeatherInfoList;
        }

        private HttpClient getClient() {
            BasicHttpParams params = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(params,5000);
            HttpConnectionParams.setSoTimeout(params,8000);
            return new DefaultHttpClient(params);
        }

        @Override
        protected void onPostExecute(List<WeatherInfo> list) {
            super.onPostExecute(list);
            i.getResponse(list);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            mProgressListener.setImageProgress(values[0]);
        }


    }

}
