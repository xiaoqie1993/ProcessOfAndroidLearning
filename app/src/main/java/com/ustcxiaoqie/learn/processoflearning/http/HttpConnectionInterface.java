package com.ustcxiaoqie.learn.processoflearning.http;

/**
 * Created by Administrator on 2017/3/1 10:31.
 * Copyright (c) 2017-03-01 Bryant1993 All rights reserved.
 */


public interface HttpConnectionInterface {
    /**
     *  HttpConnection请求返回值
     * @param code    请求返回码
     */
    void getInputStream(int code);
}
