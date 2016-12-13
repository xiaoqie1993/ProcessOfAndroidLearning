package com.ustcxiaoqie.learn.processoflearning.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.ustcxiaoqie.learn.processoflearning.R;

/**
 * Created by Xiaoqie on 2016/12/11.
 * Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.
 */
public class CircleView extends View {
    private static final String TAG = "CircleView";
    private int defaultSize;
    private String text;
    private MyHandler handler;

    public CircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleView);
        defaultSize = typedArray.getDimensionPixelSize(R.styleable.CircleView_defultSize,100);
        text = typedArray.getString(R.styleable.CircleView_textOnCircle);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getSize(defaultSize,widthMeasureSpec);
        int height = getSize(defaultSize,heightMeasureSpec);
        width = height = Math.max(width,height);
        setMeasuredDimension(width,height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.RED);
        canvas.drawText("ssssssssssssssssssssssssssssssssss",0,0,new Paint());
        int r = getMeasuredWidth()/2;
        int centerX = getLeft()+r;
        int centerY = getTop()+r;
        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        canvas.drawCircle(centerX,centerY,r,paint);
       /* paint.setColor(Color.GREEN);
        paint.setTextSize(40);
        canvas.drawText("Hello",centerX,centerY,paint);*/
        handler = new MyHandler(canvas,centerX,centerY);
        Thread t= new DrawText();
        t.setName("new Thread");
        t.start();
    }

    private int getSize(int defaultSize,int measureSpec){
        int size = defaultSize;
        int measuredSize = MeasureSpec.getSize(measureSpec);
        int measuredMode = MeasureSpec.getMode(measureSpec);
        switch (measuredMode){
            case MeasureSpec.AT_MOST:
                size = measuredSize;
                break;
            case MeasureSpec.EXACTLY:
                size = measuredSize;
                break;
            case MeasureSpec.UNSPECIFIED:
                size = defaultSize;
                break;
        }
        return size;
    }

    private class MyHandler extends Handler{
        private Canvas mCanvas;
        private int startX;
        private int startY;
        private int width = 40;
        public MyHandler(Canvas canvas,int startX,int startY){
            mCanvas = canvas;
            this.startY = startY;
            this.startX = startX;
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

           if(msg.what == 0) {
               Log.e(TAG, String.valueOf(msg.obj));

               Paint paint = new Paint();
               paint.setColor(Color.YELLOW);
             //  mCanvas.drawText(String.valueOf(msg.obj), startX-200, startY, paint);

               startX += width;
            }
        }
    }
    private class DrawText extends Thread{
        @Override
        public void run() {
            super.run();
            synchronized (this) {
                for (int i = 0; i < text.length(); i++) {
                    Message message = new Message();
                    message.obj = String.valueOf(text.substring(i, i + 1));
                    message.what = 0;
                    handler.sendMessage(message);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }

            }
        }
    }
}
