package com.ustcxiaoqie.learn.processoflearning.http;

import android.os.AsyncTask;
import android.util.Log;

import com.ustcxiaoqie.learn.processoflearning.tools.Constant;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by Administrator on 2016/12/29 19:28.
 * Copyright (c) 2016-12-29 Bryant1993 All rights reserved.
 */


public class WeatherHttpPost {
    private static final String TAG = "WeatherHttpPost";
    private int cityid ;
    private final static int PROGRESS_START = 0;
    private final static int PROGRESS_MIDDLE = 50;
    private final static int PROGRESS_END = 100;
    private final static int PROGRESS_FAILED = -1;

    public WeatherHttpPost(int cityid){
        this.cityid = cityid;
    }
    public void getWeatherInfo(PostInterface i){
        String postUrl = Constant.getUrl;
        HttpAsyncTask task = new HttpAsyncTask(postUrl,i);
        task.execute("");
    }

    private class HttpAsyncTask extends AsyncTask<String,Integer,String> {
        private String postUrl;
        private PostInterface i;
        public HttpAsyncTask(String postUrl,PostInterface i) {
            this.postUrl = postUrl;
            this.i = i;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... params) {
            try {
                publishProgress(PROGRESS_START);
                URI uri = new URI(postUrl);
                HttpGet get = new HttpGet(uri);
/*                HttpPost post = new HttpPost(uri);
                List<NameValuePair> list = new ArrayList<>();
                BasicNameValuePair pair = new BasicNameValuePair("appkey",Constant.myAPIKey);
                BasicNameValuePair pair1 = new BasicNameValuePair("id",String.valueOf(cityid));
                list.add(pair);
                list.add(pair1);
                post.setEntity(new UrlEncodedFormEntity(list, HTTP.UTF_8));
 */
                HttpClient client = getClient();
                HttpResponse response = client.execute(get);
                publishProgress(PROGRESS_MIDDLE);
                if(response.getStatusLine().getStatusCode() == 200){
                    publishProgress(PROGRESS_END);
                    return EntityUtils.toString(response.getEntity());
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
            publishProgress(PROGRESS_FAILED);
            return null;
        }

        private HttpClient getClient() {
            BasicHttpParams params = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(params,5000);
            HttpConnectionParams.setSoTimeout(params,8000);
            return new DefaultHttpClient(params);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            i.getResponse(s);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            Log.d(TAG,values[0]+"");
            switch (values[0]){
                case PROGRESS_START:
                    break;
                case PROGRESS_MIDDLE:
                    break;
                case PROGRESS_END:
                    break;
                case PROGRESS_FAILED:
                    break;
            }
        }


    }

}
