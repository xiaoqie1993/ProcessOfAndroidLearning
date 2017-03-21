package com.ustcxiaoqie.learn.processoflearning.activitys;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.qq.e.ads.banner.ADSize;
import com.qq.e.ads.banner.AbstractBannerADListener;
import com.qq.e.ads.banner.BannerView;
import com.ustcxiaoqie.learn.processoflearning.CityGridViewAdapter;
import com.ustcxiaoqie.learn.processoflearning.ProgressListener;
import com.ustcxiaoqie.learn.processoflearning.R;
import com.ustcxiaoqie.learn.processoflearning.database.DataBaseHelper;
import com.ustcxiaoqie.learn.processoflearning.database.Table_Structure;
import com.ustcxiaoqie.learn.processoflearning.http.PostInterface;
import com.ustcxiaoqie.learn.processoflearning.http.WeatherHttpPost;
import com.ustcxiaoqie.learn.processoflearning.tools.ADsConstants;
import com.ustcxiaoqie.learn.processoflearning.tools.City;
import com.ustcxiaoqie.learn.processoflearning.tools.Constant;
import com.ustcxiaoqie.learn.processoflearning.tools.DataTransferTool;
import com.ustcxiaoqie.learn.processoflearning.tools.LA;
import com.ustcxiaoqie.learn.processoflearning.tools.WeatherInfo;
import com.ustcxiaoqie.learn.processoflearning.views.CustomDialog;
import com.ustcxiaoqie.learn.processoflearning.views.DeleteFromGridViewListener;
import com.ustcxiaoqie.learn.processoflearning.views.TitleView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Administrator on 2017/1/18 20:23.
 * Copyright (c) 2017-01-18 Bryant1993 All rights reserved.
 */


public class CityManagerActivity extends BaseActivity implements
        DeleteFromGridViewListener,View.OnClickListener,AdapterView.OnItemClickListener {
    private static final String TAG = "CityManagerActivity";
    private GridView mGridView; //城市列表
    private TitleView mTitleView;
    private TextView leftText;
    private TextView rightText;
    private DataBaseHelper helper;
    private List<HashMap<String, Object>> data = new ArrayList<>();
    private CityGridViewAdapter adapter;
    private int time_righttext_pressed;

    private boolean isFirstResume = true;

    private FrameLayout fl;
    private BannerView bv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citymanager);
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isFirstResume){
            isFirstResume = false;
            initDatas();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        isFirstResume = true;
    }

    private void initDatas() {
        helper = new DataBaseHelper(this, Constant.DATABASE_VERSION);
        final dataHandler handler= new dataHandler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                SQLiteDatabase db = helper.getWritableDatabase();
                Cursor c = helper.queryFromFavoriteCity(db,new String[]{Table_Structure.TABLE_FAVORATE_CITIES.city_name
                ,Table_Structure.TABLE_FAVORATE_CITIES.city_id},null,null,null,null,null);
               //跳出内存循环到外层  out  ---- continue out 配对
                out:while (c.moveToNext()) {
                    boolean next = false;
                    String cityname = c.getString(0);
                    //若列表中已有城市则直接进入下一次循环
                    for (HashMap<String, Object> m : data) {
                        LA.d(TAG, "data:" + " " + m.get("cityname"));
                        LA.d(TAG, "cityname:" + " " + cityname);
                        if (null != m && m.get("cityname").equals(cityname)) {
                          continue out;
                           /* next = true;
                            break;*/
                        }
                    }
                    if(next)  continue;
                    int cityid = c.getInt(1);
                    HashMap<String,Object> map = new HashMap<String, Object>();
                    map.put("cityname",cityname);
                    map.put("cityid",cityid);
                    map.put("icon",R.mipmap.ic_launcher);
                    map.put("delete",1);
                    data.add(map);
                }
                LA.d(TAG,data.size()+"");
                c.close();
                helper.close();
                handler.sendEmptyMessage(0);
            }
        }).start();
        initBannerView();
        loadBanner();
    }

    private void initViews() {
        mGridView = (GridView) findViewById(R.id.cities_gridview);
        mTitleView = (TitleView) findViewById(R.id.title_citymanager);
        leftText = mTitleView.getLeftTextView();
        rightText = mTitleView.getRightTextView();
        leftText.setOnClickListener(this);
        rightText.setOnClickListener(this);
        adapter = new CityGridViewAdapter(this, data,this);
        mGridView.setAdapter(adapter);
        mGridView.setOnItemClickListener(this);
        fl = (FrameLayout) findViewById(R.id.banner_ad);
    }

    @Override
    public void delete(int position) {
        //从数据库删除
        SQLiteDatabase db = helper.getWritableDatabase();
        helper.deleteFromFavoriteCity(db, Table_Structure.TABLE_FAVORATE_CITIES.city_name+"=?",
                new String[]{(String) data.get(position).get("cityname")});
        helper.close();
        //从列表删除
        data.remove(position);
        adapter.notifyDataSetChanged();
        if(data.size() == 0){
            rightText.setText("添加");
            rightText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(CityManagerActivity.this,SearchActivity.class));
                }
            });
        }else{
            rightText.setText("编辑");
            rightText.setOnClickListener(CityManagerActivity.this);
        }
        Toast.makeText(CityManagerActivity.this,"删除成功！",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_tv_right:
                if(data.size() == 0) {
                    changerightTextViewState();
                    Toast.makeText(CityManagerActivity.this,"无收藏城市！",Toast.LENGTH_SHORT).show();
                    return;
                }

                    for (HashMap<String, Object> map : data) {
                        if (time_righttext_pressed % 2 == 0) {
                            map.put("delete", 0);
                        } else {
                            map.put("delete", 1);
                        }
                    }
                time_righttext_pressed++;
                adapter.notifyDataSetChanged();
                break;
            case R.id.title_tv_left:
                this.finish();
                break;

        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        LA.d(TAG,"position="+position);
        HashMap<String,Object> map =  data.get(position);
        City city = new City();
        city.setName((String) map.get("cityname"));
        city.setId((int) map.get("cityid"));
        Intent i = new Intent(CityManagerActivity.this,WeatherOfCityActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.KEY_BUNDLE_CITY_OBJECT,city);
        i.putExtras(bundle);
        startActivity(i);
    }

    private class dataHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    changerightTextViewState();
                    updateCityIcon();
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    }

    private void changerightTextViewState(){
        if(data.size() == 0){
            rightText.setText("添加");
            rightText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(CityManagerActivity.this,SearchActivity.class));
                }
            });
        }else{
            rightText.setText("编辑");
            rightText.setOnClickListener(CityManagerActivity.this);
        }
    }

    private void loadBanner() {
        if(null == bv){
            initBannerView();
        }
        bv.loadAD();
    }

    private void initBannerView() {
        bv = new BannerView(this, ADSize.BANNER, ADsConstants.APPID,ADsConstants.BannerPosID_BOTTOM);
        bv.setADListener(new AbstractBannerADListener() {
            @Override
            public void onNoAD(int i) {

            }

            @Override
            public void onADReceiv() {

            }
        });
        bv.setRefresh(20);
        fl.addView(bv);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    //用当日天气图标来更新已收藏城市的图标
    private void updateCityIcon(){
        List<City> cityList = new ArrayList<>();
        for(Iterator<HashMap<String,Object>> iterator = data.iterator(); iterator.hasNext();){
            City city = new City();
            HashMap<String,Object> map = iterator.next();
            city.setName((String) map.get("cityname"));
            city.setId((int) map.get("cityid"));
            cityList.add(city);
        }
        WeatherHttpPost post = new WeatherHttpPost(cityList);
        post.getWeatherInfo(new PostInterface() {
            @Override
            public void getResponse(List<WeatherInfo> list) {
                if (null == list || list.size() == 0) {
                    CustomDialog.Builder builder = new CustomDialog.Builder(CityManagerActivity.this);
                    CustomDialog dialog = builder.setTitle("无网络连接")
                            .setMessage("请连接到互联网")
                            .setNegativeButton(null, null)
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
                //天气信息转换为图标
                List<HashMap<String, Object>> list1 = DataTransferTool.transferListToListMap2(list);
                LA.e(TAG,"list.size=  "+list.size()+"");
                if (null == list1 || list1.size() == 0 || data.size() != list1.size()) {
                    LA.e(TAG,"error:");
                    try{
                        LA.e(TAG,"date.size=  "+data.size()+"");
                        LA.e(TAG,"list1= null? "+ (null == list1));
                        LA.e(TAG,"list1.size=  "+list1.size()+"");
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    return;
                }
                for (int i = 0; i < list1.size(); i++) {
                    HashMap<String, Object> map = data.get(i);
                    map.put("icon", (Integer) list1.get(i).get("icon"));
                }
                adapter.notifyDataSetChanged();
            }
        }, new ProgressListener() {
            @Override
            public void setLoadProgress(int progress) {

            }
        });
    }
}
