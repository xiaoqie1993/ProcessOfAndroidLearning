package com.ustcxiaoqie.learn.processoflearning.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
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


public class TopBar extends LinearLayout implements View.OnClickListener{


    private Button mLeftBtn;
    private Button mRightBtn;
    private TextView mTitle;
    private TopBarOnclickListener mListener;
    public void onTopBarClickListener(TopBarOnclickListener mListener){
        this.mListener = mListener;
    }
    public TopBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setOrientation(HORIZONTAL);
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
        mTitle.setTextSize(typedArray.getDimension(R.styleable.TopBar_titleTextSize,16));
        mTitle.setTextColor(typedArray.getColor(R.styleable.TopBar_titleTextColor,0000));
        mTitle.setOnClickListener(this);
        LinearLayout.LayoutParams titleParams = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,4);
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
        mRightBtn.setBackgroundColor(Color.TRANSPARENT);
        mRightBtn .setOnClickListener(this);
        LinearLayout.LayoutParams rightBtnParams = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,1);
        rightBtnParams.gravity = Gravity.CENTER_VERTICAL;
        addView(mRightBtn ,rightBtnParams);
    }

    private void initLeftBtn(Context context,TypedArray typedArray) {
        mLeftBtn = new Button(context);
        mLeftBtn.setText(typedArray.getText(R.styleable.TopBar_leftBtnText));
        mLeftBtn.setGravity(Gravity.CENTER);
        mLeftBtn.setBackgroundColor(Color.TRANSPARENT);
        mLeftBtn.setTextColor(typedArray.getColor(R.styleable.TopBar_leftBtnTextColor,0000));
        mLeftBtn.setTextSize(typedArray.getDimension(R.styleable.TopBar_leftBtnTextSize,16));
        mLeftBtn.setVisibility(typedArray.getBoolean(R.styleable.TopBar_leftBtnVisiable,true)?View.VISIBLE:View.INVISIBLE);
        // mLeftBtn.setBackground(R.styleable.TopBar_leftBtnBackground);
        mLeftBtn.setOnClickListener(this);
        LinearLayout.LayoutParams leftBtnParams = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,1);
        addView(mLeftBtn,leftBtnParams);
    }
    @Override
    public void onClick(View view) {
        if(view==mLeftBtn){
            mListener.onLeftBtnClicked(mLeftBtn);
        }else if(view == mRightBtn){
            mListener.onRightBtnClicked(mRightBtn);
        }else if(view == mTitle){
            mListener.onTitleClicked(mTitle);
        }
    }
    public Button getmRightBtn() {
        return mRightBtn;
    }

    public TextView getmTitle() {
        return mTitle;
    }

    public Button getmLeftBtn() {
        return mLeftBtn;
    }
}
