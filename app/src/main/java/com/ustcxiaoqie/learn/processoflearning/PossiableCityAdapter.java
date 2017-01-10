package com.ustcxiaoqie.learn.processoflearning;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ustcxiaoqie.learn.processoflearning.activitys.WeatherOfCityActivity;
import com.ustcxiaoqie.learn.processoflearning.tools.AvailableCity;
import com.ustcxiaoqie.learn.processoflearning.tools.Constant;

import java.util.List;

/**
 * Created by Xiaoqie on 2017/1/9.
 */

public class PossiableCityAdapter extends BaseAdapter {
    private Context mContext;
    private List<AvailableCity> mList;

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
        holder.mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((TextView)view).setTextSize(30);
                ((TextView)view).setTextColor(Color.RED);
                Intent intent = new Intent(mContext,WeatherOfCityActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constant.KEY_BUNDLE_CITY_OBJECT,mList.get(i));
                intent.putExtras(bundle);
                mContext.startActivity(intent);
                ((Activity)mContext).finish();
            }
        });
        return view;
    }
    private static class ViewHolder{
        private TextView mTextView;
        public ViewHolder(View view){
            mTextView = (TextView) view.findViewById(R.id.city_adapter_city);
        }
    }
}
