package com.ustcxiaoqie.learn.processoflearning.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ustcxiaoqie.learn.processoflearning.R;

/**
 * Created by Administrator on 2016/12/8 19:40.
 * Copyright (c) 2016-12-08 Bryant1993 All rights reserved.
 */


public class TopBar extends LinearLayout{


    private Button mLeftBtn;
    private Button mRightBtn;
    private TextView mTitle;
    public TopBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setOrientation(HORIZONTAL);
        this.setId(R.id.TopBarId);
        this.setPadding(2,1,2,0);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TopBar);
        initLeftBtn(context,typedArray);
        initTitle(context,typedArray);
        initRightBtn(context,typedArray);
        typedArray.recycle();
    }

    private void initTitle(Context context, TypedArray typedArray) {
        mTitle = new TextView(context);
        mTitle.setText(typedArray.getText(R.styleable.TopBar_titleText));
        mTitle.setGravity(Gravity.CENTER);
        mTitle.setTextSize(typedArray.getDimension(R.styleable.TopBar_titleTextSize,12));
        mTitle.setTextColor(typedArray.getColor(R.styleable.TopBar_titleTextColor,0000));
        mTitle.setId(R.id.TopBarTitleId);
        LinearLayout.LayoutParams titleParams = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,8);
        titleParams.gravity = Gravity.CENTER;
        addView(mTitle ,titleParams);
    }

    private void initRightBtn(Context context, TypedArray typedArray) {
        mRightBtn = new Button(context);
        mRightBtn .setText(typedArray.getText(R.styleable.TopBar_rightBtnText));
        mRightBtn.setGravity(Gravity.CENTER);
        mRightBtn .setTextColor(typedArray.getColor(R.styleable.TopBar_rightBtnTextColor,0000));
        mRightBtn .setTextSize(typedArray.getDimension(R.styleable.TopBar_rightBtnTextSize,16));
        mRightBtn .setVisibility(typedArray.getBoolean(R.styleable.TopBar_rightBtnVisiable,true)?View.VISIBLE:View.INVISIBLE);
        mRightBtn.setBackground(typedArray.getDrawable(R.styleable.TopBar_rightBtnBackground));
        mRightBtn.setId(R.id.TopBarRightBtnId);
        LinearLayout.LayoutParams rightBtnParams = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,3);
        rightBtnParams.gravity = Gravity.CENTER;
        addView(mRightBtn ,rightBtnParams);
    }

    private void initLeftBtn(Context context,TypedArray typedArray) {
        mLeftBtn = new Button(context);
        mLeftBtn.setText(typedArray.getText(R.styleable.TopBar_leftBtnText));
        mLeftBtn.setGravity(Gravity.CENTER);
        mLeftBtn.setTextColor(typedArray.getColor(R.styleable.TopBar_leftBtnTextColor,0000));
        mLeftBtn.setTextSize(typedArray.getDimension(R.styleable.TopBar_leftBtnTextSize,16));
        mLeftBtn.setVisibility(typedArray.getBoolean(R.styleable.TopBar_leftBtnVisiable,true)?View.VISIBLE:View.INVISIBLE);
        mLeftBtn.setBackground(typedArray.getDrawable(R.styleable.TopBar_leftBtnBackground));
        mLeftBtn.setId(R.id.TopBarLeftBtnId);
        LinearLayout.LayoutParams leftBtnParams = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,3);
        addView(mLeftBtn,leftBtnParams);
    }
}
