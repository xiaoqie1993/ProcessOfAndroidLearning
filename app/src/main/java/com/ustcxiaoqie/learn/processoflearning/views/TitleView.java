package com.ustcxiaoqie.learn.processoflearning.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ustcxiaoqie.learn.processoflearning.R;

/**
 * Created by Xiaoqie on 2017/1/18.
 */

public class TitleView extends LinearLayout{
    public TextView leftTextView;
    public TextView centerTextView;
    public TextView rightTextView;
    private String lefttext;
    private String centertext;
    private String righttext;
    public TitleView(Context context, AttributeSet attr) {
        super(context,attr);
        TypedArray typedArray = context.obtainStyledAttributes(attr,R.styleable.TitleView);
        lefttext = typedArray.getString(R.styleable.TitleView_lefttext);
        centertext = typedArray.getString(R.styleable.TitleView_centertext);
        righttext = typedArray.getString(R.styleable.TitleView_righttext);
        initViews(context);
    }

    private void initViews(Context context) {
        LinearLayout ll = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.title,null,false);
        leftTextView = (TextView) ll.findViewById(R.id.title_tv_left);
        centerTextView = (TextView) ll.findViewById(R.id.title_tv_center);
        rightTextView = (TextView) ll.findViewById(R.id.title_tv_right);
        leftTextView.setText(lefttext);
        centerTextView.setText(centertext);
        rightTextView.setText(righttext);
        addView(ll, LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
    }

    public TextView getLeftTextView() {
        return leftTextView;
    }

    public void setLeftTextView(TextView leftTextView) {
        this.leftTextView = leftTextView;
    }

    public TextView getCenterTextView() {
        return centerTextView;
    }

    public void setCenterTextView(TextView centerTextView) {
        this.centerTextView = centerTextView;
    }

    public TextView getRightTextView() {
        return rightTextView;
    }

    public void setRightTextView(TextView rightTextView) {
        this.rightTextView = rightTextView;
    }
}
