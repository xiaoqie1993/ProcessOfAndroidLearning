package com.ustcxiaoqie.learn.processoflearning;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ustcxiaoqie.learn.processoflearning.views.DeleteFromGridViewListener;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/1/18 21:12.
 * Copyright (c) 2017-01-18 Bryant1993 All rights reserved.
 */


public class CityGridViewAdapter extends BaseAdapter{
    private Context context;
    private List<HashMap<String, Object>> list;
    private DeleteFromGridViewListener mListener;
    public CityGridViewAdapter(Context context, List<HashMap<String, Object>> list, DeleteFromGridViewListener listener) {
        //添加最后面的添加城市图标
        HashMap<String,Object> maps = new HashMap<>();
        maps.put("cityname","添加城市");
        maps.put("icon",R.drawable.add);
        maps.put("delete",1);
        list.add(0,maps);
        this.list =list;
        this.context = context;
        this.mListener = listener;
    }
    @Override
    public int getCount() {
        return list.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (null == convertView) {
            LayoutInflater mInflater = LayoutInflater.from(context);
            convertView = mInflater.inflate(R.layout.city_gridview_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.mCityname.setText((CharSequence) list.get(position).get("cityname"));
        viewHolder.mIcon.setImageDrawable(context.getResources().getDrawable((Integer) list.get(position).get("icon")));
        if((Integer)(list.get(position).get("delete")) == 0){
            viewHolder.icon_delete.setVisibility(View.VISIBLE);
            viewHolder.icon_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.delete(position);
                }
            });

        }else{
            viewHolder.icon_delete.setVisibility(View.INVISIBLE);
        }
        return convertView;
    }

    private  class ViewHolder {
        private TextView mCityname;
        private ImageView mIcon;
        private ImageView icon_delete;

        public ViewHolder(View view) {
            mCityname = (TextView) view.findViewById(R.id.gridview_item_tv);
            mIcon = (ImageView) view.findViewById(R.id.gridview_item_iv);
            icon_delete  = (ImageView) view.findViewById(R.id.icon_delete);
        }
    }
}
