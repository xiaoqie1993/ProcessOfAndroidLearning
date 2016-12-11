package com.ustcxiaoqie.learn.processoflearning.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.ustcxiaoqie.learn.processoflearning.R;

/**
 * Created by Xiaoqie on 2016/12/11.
 * Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.
 */
public class CircleView extends View {
    private int defaultSize;
    public CircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleView);
        defaultSize = typedArray.getDimensionPixelSize(R.styleable.CircleView_defultSize,100);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getSize(defaultSize,widthMeasureSpec);
        int height = getSize(defaultSize,heightMeasureSpec);
        width = height = Math.min(width,height);
        setMeasuredDimension(width,height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int r = getMeasuredWidth();
        int centerX = getLeft()+r;
        int centerY = getTop()+r;
        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        canvas.drawCircle(centerX,centerY,r,paint);
        paint.setColor(Color.YELLOW);
        paint.setTextSize(10);
        canvas.drawText("Hello",centerX,centerY,paint);

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
}
