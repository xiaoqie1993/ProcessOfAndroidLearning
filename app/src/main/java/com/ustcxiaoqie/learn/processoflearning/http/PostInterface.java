package com.ustcxiaoqie.learn.processoflearning.http;

import com.ustcxiaoqie.learn.processoflearning.tools.WeatherInfo;

import java.util.List;

/**
 * Created by Administrator on 2016/12/29 20:02.
 * Copyright (c) 2016-12-29 Bryant1993 All rights reserved.
 */


public interface PostInterface {
    void getResponse(List<WeatherInfo> list);
}
