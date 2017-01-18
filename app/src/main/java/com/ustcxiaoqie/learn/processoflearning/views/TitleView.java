package com.ustcxiaoqie.learn.processoflearning.views;

import android.content.Context;
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
    public TitleView(Context context) {
        super(context);
        initViews(context);
    }

    private void initViews(Context context) {
        LinearLayout ll = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.title,null,false);
        leftTextView = (TextView) ll.findViewById(R.id.title_tv_left);
        centerTextView = (TextView) ll.findViewById(R.id.title_tv_center);
        rightTextView = (TextView) ll.findViewById(R.id.title_tv_right);
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
