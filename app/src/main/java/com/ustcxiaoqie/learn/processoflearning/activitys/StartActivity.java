package com.ustcxiaoqie.learn.processoflearning.activitys;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Process;
import android.support.v4.content.LocalBroadcastManager;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.qq.e.ads.banner.BannerView;
import com.ustcxiaoqie.learn.processoflearning.CityLocationListener;
import com.ustcxiaoqie.learn.processoflearning.ProgressListener;
import com.ustcxiaoqie.learn.processoflearning.R;
import com.ustcxiaoqie.learn.processoflearning.WeatherAdapter;
import com.ustcxiaoqie.learn.processoflearning.database.DataBaseHelper;
import com.ustcxiaoqie.learn.processoflearning.database.Table_Structure;
import com.ustcxiaoqie.learn.processoflearning.http.PostInterface;
import com.ustcxiaoqie.learn.processoflearning.http.WeatherHttpPost;
import com.ustcxiaoqie.learn.processoflearning.services.UpdateService;
import com.ustcxiaoqie.learn.processoflearning.tools.City;
import com.ustcxiaoqie.learn.processoflearning.tools.Constant;
import com.ustcxiaoqie.learn.processoflearning.tools.DataTransferTool;
import com.ustcxiaoqie.learn.processoflearning.tools.Temp;
import com.ustcxiaoqie.learn.processoflearning.tools.WeatherDetail;
import com.ustcxiaoqie.learn.processoflearning.tools.WeatherInfo;
import com.ustcxiaoqie.learn.processoflearning.views.CustomDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/1/18 18:50.
 * Copyright (c) 2017-01-18 Bryant1993 All rights reserved.
 */


public class StartActivity extends BaseActivity implements View.OnClickListener,AdapterView.OnItemClickListener{
    private static final String TAG = "WeatherOfCityActivity";
    private LocationClient mClient;
    private BDLocationListener mListener;
    private static final int LEVEL_STARED = 10;
    private static final int LEVEL_NOT_STARED = 0;
    private City mCity;
    private DataBaseHelper helper = new DataBaseHelper(StartActivity.this, Constant.DATABASE_VERSION);

    //标题栏
    private TextView titleTv_center;
    private TextView titleTv_left;
    private TextView titleTv_right;
    private PopupMenu mPopupMenu;
    //今日天气
    private TextView current_temp;
    private TextView current_range;
    private ImageView icon;
    //列表
    private ListView mListView;
    private List<HashMap<String,Object>> mHashMapList;
    private WeatherAdapter mAdapter;
    private SQLiteDatabase mSQLiteDatabase;
    private BannerView bv;
    private FrameLayout fl; //广告位

    private boolean isResume = true;
    private List<WeatherInfo> mWeatherInfoList = new ArrayList<>();
    private LocalBroadcastManager mLocalBroadcastManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_start);
        initViews();
        //获取当前城市
   //     startGetLocation();
        startService(new Intent(StartActivity.this,NoticeDailyService.class));
        startService(new Intent(StartActivity.this,UpdateService.class));
    }

    private void startGetLocation() {
        mClient = new LocationClient(getApplicationContext());
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");
        //可选，默认gcj02，设置返回的定位结果坐标系
        option.setIsNeedAddress(true);
        //可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);
        //可选，默认false,设置是否使用gps
        option.setLocationNotify(true);
        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIgnoreKillProcess(false);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);
        //可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);
        //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        mClient.setLocOption(option);


        mListener = new CityLocationListener(this);
        mClient.registerLocationListener(mListener);
        mClient.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isResume){
            initDatas();
            isResume = false;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        isResume = true;
    }

    private void initViews() {

        fl = (FrameLayout) findViewById(R.id.weatherofcity_banner);
        titleTv_left = (TextView) findViewById(R.id.title_tv_left);
        titleTv_center = (TextView) findViewById(R.id.title_tv_center);
        titleTv_right = (TextView) findViewById(R.id.title_tv_right);
        titleTv_left.setOnClickListener(this);
        titleTv_right.setOnClickListener(this);
        titleTv_center.setOnClickListener(this);
        titleTv_right.setText("●●●");
        titleTv_left.setText("刷新");
        titleTv_right.setTextColor(Color.BLACK);
        mPopupMenu = new PopupMenu(this,titleTv_right);
        mPopupMenu.getMenuInflater().inflate(R.menu.activity_man_setting,mPopupMenu.getMenu());
        mPopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_main_setting:
                        startActivity(new Intent(StartActivity.this,AboutActivity.class));
                        overridePendingTransition(R.anim.activity_open,0);
                        break;
                    case R.id.menu_main_manage:
                        startActivity(new Intent(StartActivity.this,CityManagerActivity.class));
                        break;
                    case R.id.menu_main_search:
                        startActivity(new Intent(StartActivity.this,SearchActivity.class));
                        overridePendingTransition(R.anim.activity_open,0);
                        break;
                    case R.id.menu_main_more:
                        startActivity(new Intent(StartActivity.this,CallBackActivity.class));
                        break;
                }
                return false;
            }
        });

        current_temp = (TextView) findViewById(R.id.current_temp_tv);
        current_range = (TextView) findViewById(R.id.range_temp_tv);
        icon = (ImageView) findViewById(R.id.icon_weather);

        mHashMapList = new ArrayList<>();
        mListView = (ListView) findViewById(R.id.moreweather_lv);
        mAdapter = new WeatherAdapter(StartActivity.this,mHashMapList,WeatherAdapter.FLAG_WEATHER_OF_CITY_ACTIVITY);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
    }

    private void initDatas() {
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
        DataBaseHelper helper = new DataBaseHelper(this,Constant.DATABASE_VERSION);
        final SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = helper.queryFromFavoriteCity(db,new String[]{Table_Structure.TABLE_FAVORATE_CITIES.city_name, Table_Structure.TABLE_FAVORATE_CITIES.city_id}
                ,null,null,null,null,null);
        mCity = new City();
        if(cursor.moveToNext()){
            mCity.setName(cursor.getString(0));
            mCity.setId(cursor.getInt(1));
        }else{
            //默认值
            CustomDialog.Builder builder = new CustomDialog.Builder(StartActivity.this);
            CustomDialog dialog = builder.setTitle("提示")
                    .setMessage("无收藏城市，默认为合肥.可点击上方城市名管理城市")
                    .setNegativeButton(null,null)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create();
            dialog.setCancelable(false);
            dialog.show();
            mCity.setName("Hefei");
            mCity.setId(1808722);
        }
        cursor.close();
        helper.close();
        final List<City> mList = new ArrayList<>();
        startActivity(new Intent(StartActivity.this, DialogActivity.class));
        mList.add(mCity);
        WeatherHttpPost post = new WeatherHttpPost(mList);
        post.getWeatherInfo(new PostInterface() {
            @Override
            public void getResponse(List<WeatherInfo> list) {
                Intent intent = new Intent(Constant.DIALOG_ACTIVITY_FINISH);
                if (null == list || list.size() == 0){
                    intent.putExtra(Constant.PROGRESS, Constant.PROGRESS_FAILED);
                    mLocalBroadcastManager.sendBroadcast(intent);
                    CustomDialog.Builder builder = new CustomDialog.Builder(StartActivity.this);
                    CustomDialog dialog = builder.setTitle("无网络连接")
                            .setMessage("请连接到互联网")
                            .setNegativeButton(null,null)
                            .setPositiveButton("退出", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Process.killProcess(Process.myPid());
                                }
                            })
                            .create();
                    dialog.setCancelable(false);
                    dialog.show();
                    return;
                }
                mWeatherInfoList = list;
                setAboveViews(list);
                mHashMapList.clear();
                mHashMapList.addAll(DataTransferTool.transferListToListMap(list));
                mAdapter.notifyDataSetChanged();
                intent.putExtra(Constant.PROGRESS, Constant.PROGRESS_FINISH);
                mLocalBroadcastManager.sendBroadcast(intent);

            }},new ProgressListener() {
            @Override
            public void setLoadProgress(int progress) {
                Intent intent = new Intent(Constant.DIALOG_ACTIVITY_FINISH);
                intent.putExtra(Constant.PROGRESS, progress);
                mLocalBroadcastManager.sendBroadcast(intent);
            }
        });
    }


    private void setAboveViews(List<WeatherInfo> mlist) {
        WeatherInfo weatherInfo = mlist.get(0);
        List<WeatherDetail> mDetailList = weatherInfo.getList();
        WeatherDetail detail = mDetailList.get(0); //今天的天气信息
        Temp temp = detail.getTemp();  //今天的温度
        float temp_day = temp.getDay();
        float temp_min = temp.getMin();
        float temp_max = temp.getMax();
        String weather_describe = detail.getWeather().getDesciption();
        String tv2 = detail.getWeather().getDesciption();
        current_temp.setText(temp_day + "℃");
        icon.setImageDrawable(
                getResources().getDrawable(DataTransferTool.getIconFromWeatherDetail(weather_describe)));
        current_range.setText(tv2);
        titleTv_center.setText(weatherInfo.getCity().getName());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_tv_left:
                initDatas();
      //          startActivity(new Intent(StartActivity.this,CityManagerActivity.class));
                break;
            case R.id.title_tv_right:
                mPopupMenu.show();
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(null != bv){
            bv.destroy();
            bv = null;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        WeatherInfo weatherInfo = mWeatherInfoList.get(0);
        Intent i = new Intent();
        i.setClass(StartActivity.this, ShareActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("weather", weatherInfo);
        bundle.putInt("position", position);
        i.putExtras(bundle);
        startActivity(i);
        this.overridePendingTransition(R.anim.activity_open, 0);
    }
}
