package com.ustcxiaoqie.learn.processoflearning.views;

import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/12/8 21:06.
 * Copyright (c) 2016-12-08 Bryant1993 All rights reserved.
 */


public interface TopBarOnclickListener {
    void onLeftBtnClicked(Button leftBtn);
    void onRightBtnClicked(Button rightBtn);
    void onTitleClicked(TextView title);
}
