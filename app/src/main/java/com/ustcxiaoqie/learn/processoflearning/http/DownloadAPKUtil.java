package com.ustcxiaoqie.learn.processoflearning.http;

/**
 * Created by bryant on 2017/3/1.
 */

public class DownloadAPKUtil {
    private HttpByHttpConnection mHttpByHttpConnection;
    public DownloadAPKUtil(HttpConnectionInterface i,String url,String savedPath){
        mHttpByHttpConnection = new HttpByHttpConnection(i,url,savedPath);
    }
    public void download(){
        mHttpByHttpConnection.connect();
    }
}
