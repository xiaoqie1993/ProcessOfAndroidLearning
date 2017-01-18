package com.ustcxiaoqie.learn.processoflearning.activitys;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.qq.e.ads.banner.ADSize;
import com.qq.e.ads.banner.AbstractBannerADListener;
import com.qq.e.ads.banner.BannerView;
import com.ustcxiaoqie.learn.processoflearning.CityGridViewAdapter;
import com.ustcxiaoqie.learn.processoflearning.R;
import com.ustcxiaoqie.learn.processoflearning.database.DataBaseHelper;
import com.ustcxiaoqie.learn.processoflearning.database.Table_Structure;
import com.ustcxiaoqie.learn.processoflearning.tools.ADsConstants;
import com.ustcxiaoqie.learn.processoflearning.tools.Constant;
import com.ustcxiaoqie.learn.processoflearning.tools.LA;
import com.ustcxiaoqie.learn.processoflearning.views.DeleteFromGridViewListener;
import com.ustcxiaoqie.learn.processoflearning.views.TitleView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/1/18 20:23.
 * Copyright (c) 2017-01-18 Bryant1993 All rights reserved.
 */


public class CityManagerActivity extends BaseActivity implements DeleteFromGridViewListener,View.OnClickListener{
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
                Cursor c = helper.queryFromFavoriteCity(db,new String[]{Table_Structure.TABLE_FAVORATE_CITIES.city_name},null,null,null,null,null);
                while (c.moveToNext()){
                    String cityname = c.getString(0);
                    HashMap<String,Object> map = new HashMap<String, Object>();
                    map.put("cityname",cityname);
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
        fl = (FrameLayout) findViewById(R.id.banner_ad);
    }

    @Override
    public void delete(int position) {
        //得到待删除对象的position


        //从数据库删除
        SQLiteDatabase db = helper.getWritableDatabase();
        helper.deleteFromFavoriteCity(db, Table_Structure.TABLE_FAVORATE_CITIES.city_name+"=?",new String[]{(String) data.get(position).get("cityname")});
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

    private class dataHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    changerightTextViewState();
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
}
