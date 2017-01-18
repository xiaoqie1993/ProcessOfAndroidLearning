package com.ustcxiaoqie.learn.processoflearning;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ustcxiaoqie.learn.processoflearning.activitys.WeatherOfCityActivity;
import com.ustcxiaoqie.learn.processoflearning.tools.City;
import com.ustcxiaoqie.learn.processoflearning.tools.Constant;
import com.ustcxiaoqie.learn.processoflearning.tools.LA;

import java.util.List;

/**
 * Created by Xiaoqie on 2017/1/9.
 */

public class PossiableCityAdapter extends BaseAdapter {
    private static final String TAG = "PossiableCityAdapter";
    private Context mContext;
    private List<City> mList;

    public PossiableCityAdapter(Context context, List list) {
        mContext = context;
        mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder holder ;
        if(null == view) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(R.layout.adapter_possiable_city, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }
        holder.mTextView.setText(mList.get(i).getName());
        int[] bgColor = new int[]{Color.parseColor("#B4CDCD"),Color.parseColor("#B2DFEE")
                ,Color.parseColor("#B0E0E6"),Color.parseColor("#AEEEEE")};
        holder.mTextView.setBackgroundColor(bgColor[i%4]);
        holder.mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((TextView)view).setTextSize(30);
                ((TextView)view).setTextColor(Color.RED);
                Intent intent = new Intent(mContext,WeatherOfCityActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constant.KEY_BUNDLE_CITY_OBJECT,mList.get(i));
                LA.d(TAG,mList.get(i).toString());
                intent.putExtras(bundle);
                mContext.startActivity(intent);
                ((Activity)mContext).finish();
            }
        });
        return view;
    }
    private class ViewHolder{
        private TextView mTextView;
        public ViewHolder(View view){
            mTextView = (TextView) view.findViewById(R.id.city_adapter_city);
            mTextView.setGravity(Gravity.CENTER_VERTICAL);
            mTextView.setTextSize(20);
            mTextView.setTextColor(Color.BLUE);
        }
    }
}
