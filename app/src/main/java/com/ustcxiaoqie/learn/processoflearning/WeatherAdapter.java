package com.ustcxiaoqie.learn.processoflearning;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Xiaoqie on 2017/1/1.
 */

public class WeatherAdapter extends BaseAdapter {
    private Context mContext;
    private List<HashMap<String,Object>> mapList;
    public WeatherAdapter(Context context, List<HashMap<String,Object>> mapList){
        this.mContext = context;
        this.mapList = mapList;
    }
    @Override
    public int getCount() {
        return mapList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(null == convertView) {
            LayoutInflater mInflater = LayoutInflater.from(mContext);
            convertView = mInflater.inflate(R.layout.weather_list_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.mCityname.setText((CharSequence) mapList.get(position).get("cityname"));
        viewHolder.mIcon.setImageDrawable(mContext.getResources().getDrawable((Integer) mapList.get(position).get("icon")));
        viewHolder.mDetail.setText((CharSequence) mapList.get(position).get("detail"));
        return convertView;
    }

    private static class ViewHolder {
        private TextView mCityname;
        private ImageView mIcon;
        private TextView mDetail;

        public ViewHolder(View view){
            mCityname = (TextView) view.findViewById(R.id.list_item_cityname);
            mIcon = (ImageView) view.findViewById(R.id.list_item_icon);
            mDetail = (TextView) view.findViewById(R.id.list_item_detail);
        }
    }
}
