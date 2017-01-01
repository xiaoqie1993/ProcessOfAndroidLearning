package com.ustcxiaoqie.learn.processoflearning;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * Created by Xiaoqie on 2017/1/1.
 */

public class WeatherAdapter extends BaseAdapter {
    private Context mContext;
    public WeatherAdapter(Context context){
        this.mContext = context;
    }
    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
