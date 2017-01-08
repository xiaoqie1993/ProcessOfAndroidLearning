package com.ustcxiaoqie.learn.processoflearning.views;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;

import com.ustcxiaoqie.learn.processoflearning.R;
import com.ustcxiaoqie.learn.processoflearning.tools.LA;

/**
 * Created by Administrator on 2017/1/6 18:01.
 * Copyright (c) 2017-01-06 Bryant1993 All rights reserved.
 */


public class MyHorizontalScollerView extends HorizontalScrollView {
    private static final String TAG = "MyHorizontalScollerView";
    private int mScreenWidth; //屏幕宽度
    private int mMenuWidth;     //菜单宽度
    private int mMenuPadding;   //菜单距离边界  mMenuPadding= mScreenWidth-mMenuWidth
    private boolean isMenuLeft;
    private boolean isSetOk;
    private   H h;

    public MyHorizontalScollerView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyHorizontalScollerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScreenWidth = ((Activity)context).getWindowManager().getDefaultDisplay().getWidth();
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyHorizontalScollerView,defStyleAttr,0);
        float  mMenuPaddingIndp= typedArray.getDimension(R.styleable.MyHorizontalScollerView_menuPadding,100);
        LA.e(TAG,"mMenuPaddingIndp"+" "+mMenuPaddingIndp);
        mMenuPadding = dip2px(context,mMenuPaddingIndp);
    /*    mMenuPadding = typedArray.getDimensionPixelSize(
                (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 50f,
                        getResources().getDisplayMetrics()),10);// 默认为10DP*/
        isMenuLeft = typedArray.getBoolean(R.styleable.MyHorizontalScollerView_menuLeft,true);
        LA.d(TAG,"mScreenWidth="+mScreenWidth);
        LA.d(TAG,"mMenuPadding="+mMenuPadding);
        mMenuWidth = mScreenWidth - mMenuPadding;
     h = new H();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        if(!isSetOk) {
            ViewGroup layout_parent = (ViewGroup) this.getChildAt(0);
            View content, menu;
            if (isMenuLeft) {
                menu = layout_parent.getChildAt(0);
                content = layout_parent.getChildAt(1);
            } else {
                content = layout_parent.getChildAt(0);
                menu = layout_parent.getChildAt(1);
            }
            menu.getLayoutParams().width = mMenuWidth;
            content.getLayoutParams().width = mScreenWidth;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if(isMenuLeft){
            this.scrollTo(mMenuWidth,0);
        }else{
            this.scrollTo(0,0);
        }
        isSetOk = true;
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        int action = ev.getAction();

        switch (action){
            case MotionEvent.ACTION_MOVE:
                LA.d(TAG,"MOVING"+"  "+getScrollX());
                break;
            case MotionEvent.ACTION_DOWN:
                Log.e(TAG,"DOWn"+"    "+getScrollX());
                break;
            case MotionEvent.ACTION_UP:
                Log.i(TAG,"UP"+"    "+getScrollX());
                if(isMenuLeft) {
                    /*if (this.getScrollX() < mMenuWidth / 2) {
                        this.smoothScrollTo(mMenuWidth, 0);
                    } else {
                        this.smoothScrollTo(0, 0);
                    }*/
        //            this.smoothScrollTo(0,0);

     //               this.smoothScrollTo(0,0);
                new t().start();
                }else {
                    if (this.getScrollX() < mMenuWidth / 2) {
                        this.smoothScrollTo(0, 0);
                    } else {
                        this.smoothScrollTo(mMenuWidth, 0);
                    }
                }
                break;
        }
        return true;
    }

    private int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    private class t extends Thread{
        int a = mMenuWidth;
        @Override
        public void run() {
            while (true) {
                a -= mMenuWidth / 10;
                try {
                    Thread.currentThread().sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Message message = Message.obtain();
                message.obj = a;
                h.sendMessage(message);
            }
        }
    }

    public int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    private class H extends Handler{
        @Override
        public void handleMessage(Message msg) {
            Log.e(TAG,"scollX"+MyHorizontalScollerView.this.getScrollX());
            MyHorizontalScollerView.this.scrollTo((Integer) msg.obj,0);
        }
    }
}
