package com.ustcxiaoqie.learn.processoflearning.tools;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;

/**
 * Created by Administrator on 2017/3/15 16:14.
 * Copyright (c) 2017-03-15 Bryant1993 All rights reserved.
 */


public class HttpTools {
    public static HttpClient getClient() {
        BasicHttpParams params = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(params,5000);
        HttpConnectionParams.setSoTimeout(params,8000);
        return new DefaultHttpClient(params);
    }
}
