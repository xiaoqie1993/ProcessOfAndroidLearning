package com.ustcxiaoqie.learn.processoflearning;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


/**
 * Created by Xiaoqie on 2017/1/1.
 */

public class WeatherAdapter extends BaseAdapter {
    private static final String TAG = "WeatherAdapter";
    public static final int FLAG_MAIN_ACTITY = 0;
    public static final int FLAG_WEATHER_OF_CITY_ACTIVITY = 1;
    private Context mContext;
    private int flag_Activiy = -1; //这个Adapter适配的Activity页面id
    private List<HashMap<String,Object>> mapList;
    private String[] weeks;
    private int today;//今天星期几
    public WeatherAdapter(Context context, List<HashMap<String,Object>> mapList,int flag_Activiy){
        this.mContext = context;
        this.mapList = mapList;
        this.flag_Activiy = flag_Activiy;
        weeks = context.getResources().getStringArray(R.array.weeks);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        today = calendar.get(Calendar.DAY_OF_WEEK);
        if(flag_Activiy < 0){
            throw new IllegalArgumentException("选择的Activity没有进行适配！");
        }
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

        switch (flag_Activiy){
            case FLAG_MAIN_ACTITY:
                viewHolder.mCityname.setText((CharSequence) mapList.get(position).get("cityname"));
                viewHolder.mIcon.setImageDrawable(mContext.getResources().getDrawable((Integer) mapList.get(position).get("icon")));
                viewHolder.mDetail.setText((CharSequence) mapList.get(position).get("detail"));
                break;
            case FLAG_WEATHER_OF_CITY_ACTIVITY:
                if(position == 0){
                    viewHolder.mCityname.setText("Today");
                }else{
                    viewHolder.mCityname.setText(weeks[((today-2+position)%7)]);
                }
                viewHolder.mIcon.setImageDrawable(mContext.getResources().getDrawable((Integer) mapList.get(position).get("icon")));
                viewHolder.mDetail.setText((CharSequence) mapList.get(position).get("temp"));
                break;
            default:
                throw  new IllegalArgumentException("选择的Activity没有进行适配！");
        }
        return convertView;
    }

    private class ViewHolder {
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
