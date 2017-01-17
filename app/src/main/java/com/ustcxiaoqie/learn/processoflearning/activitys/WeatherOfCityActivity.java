package com.ustcxiaoqie.learn.processoflearning.activitys;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.qq.e.ads.banner.ADSize;
import com.qq.e.ads.banner.AbstractBannerADListener;
import com.qq.e.ads.banner.BannerView;
import com.ustcxiaoqie.learn.processoflearning.ProgressListener;
import com.ustcxiaoqie.learn.processoflearning.R;
import com.ustcxiaoqie.learn.processoflearning.WeatherAdapter;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Xiaoqie on 2017/1/10.
 */
public class WeatherOfCityActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "WeatherOfCityActivity";
    private static final int LEVEL_STARED = 10;
    private static final int LEVEL_NOT_STARED = 0;
    private City mCity;
    private WeatherInfo info;
    private DataBaseHelper helper = new DataBaseHelper(WeatherOfCityActivity.this,Constant.DATABASE_VERSION);
    private TextView mTilte;
    private Button refreshBtn;
    private Button starBtn;

    private ListView mListView;
    private List<HashMap<String,Object>> mHashMapList;
    private WeatherAdapter mAdapter;
    private SQLiteDatabase mSQLiteDatabase;
    private BannerView bv;
    private FrameLayout fl; //广告位

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_weatherofcity);
        Intent intent = getIntent();
        if (null != intent) {
            mCity = (City) intent.getSerializableExtra(Constant.KEY_BUNDLE_CITY_OBJECT);
        }
        if (null == mCity) return;
        initViews();
        initDatas();
        initBannerView();
        loadBanner();
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


    private void initViews() {

        fl = (FrameLayout) findViewById(R.id.weatherofcity_banner);
        mTilte = (TextView) findViewById(R.id.TopBarTitleId);
        mTilte.setText(mCity.getName());
        refreshBtn = (Button) findViewById(R.id.TopBarLeftBtnId);
        starBtn = (Button) findViewById(R.id.TopBarRightBtnId);


        mTilte.setOnClickListener(this);
        refreshBtn.setOnClickListener(this);
        starBtn.setOnClickListener(this);
        mSQLiteDatabase = helper.getWritableDatabase();
        Cursor cursor = helper.queryFromFavoriteCity(mSQLiteDatabase,
                new String[]{Table_Structure.TABLE_FAVORATE_CITIES.city_name,
                        Table_Structure.TABLE_FAVORATE_CITIES.city_id},null,null,null,null,null);
        while(cursor.moveToNext()) {
            if(cursor.getString(0).equals(mCity.getName())){
                starBtn.getBackground().setLevel(LEVEL_STARED);
                break;
            }
            starBtn.getBackground().setLevel(LEVEL_NOT_STARED);
        }
        cursor.close();
        helper.close();
        mHashMapList = new ArrayList<>();
        mListView = (ListView) findViewById(R.id.moreweather_lv);
        mAdapter = new WeatherAdapter(WeatherOfCityActivity.this,mHashMapList,WeatherAdapter.FLAG_WEATHER_OF_CITY_ACTIVITY);
        mListView.setAdapter(mAdapter);
    }

    private void initDatas() {
        final List<City> mList = new ArrayList<>();
        mList.add(mCity);
        WeatherHttpPost post = new WeatherHttpPost(mList);
        post.getWeatherInfo(new PostInterface() {
            @Override
            public void getResponse(List<WeatherInfo> list) {
                LA.d(TAG,"size  "+list.size());
                mHashMapList.clear();
                mHashMapList.addAll(DataTransferTool.transferListToListMap(list));
                mAdapter.notifyDataSetChanged();
            }},new ProgressListener() {
            @Override
            public void setLoadProgress(int progress) {

            }
        });
    }


    private void setDataToViews() {
        mTilte.setText(info.getCity().getName());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.TopBarRightBtnId:
                int level = starBtn.getBackground().getLevel();
                if (level == LEVEL_NOT_STARED) {
                    insertCityToFavorate(mCity);
                } else {
                    deleteCityFromFavorate(mCity);
                }
                break;
        }
    }

    private void deleteCityFromFavorate(final City city) {
        Toast toast = Toast.makeText(WeatherOfCityActivity.this, "", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        mSQLiteDatabase = helper.getWritableDatabase();
        mSQLiteDatabase.delete(DataBaseHelper.TABLE_NAME_FAVORATE_CITIES,
                Table_Structure.TABLE_FAVORATE_CITIES.city_name+"=?",new String[]{city.getName()});
        helper.close();
        (starBtn.getBackground()).setLevel(LEVEL_NOT_STARED);
        toast.setText("取消收藏");
        Cursor cursor = helper.queryFromFavoriteCity(helper.getWritableDatabase(),
                new String[]{Table_Structure.TABLE_FAVORATE_CITIES.city_name},null,null,null,null,null);
        while(cursor.moveToNext()){
            LA.d(TAG,"cursor1   "+cursor.getString(0));
        }
        cursor.close();
        helper.close();

    }

    private void insertCityToFavorate(final City city) {
        Toast toast = Toast.makeText(WeatherOfCityActivity.this, "", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        mSQLiteDatabase = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Table_Structure.TABLE_FAVORATE_CITIES.city_name, city.getName());
        values.put(Table_Structure.TABLE_FAVORATE_CITIES.city_id, city.getId());
        values.put(Table_Structure.TABLE_FAVORATE_CITIES.time_favorite, new Date().toString());
        helper.insertIntoFavoriteCity(mSQLiteDatabase, null, values, true);
        helper.close();
        (starBtn.getBackground()).setLevel(LEVEL_STARED);
        toast.setText("收藏成功");

        Cursor cursor = helper.queryFromFavoriteCity(helper.getWritableDatabase(),
                new String[]{Table_Structure.TABLE_FAVORATE_CITIES.city_name},null,null,null,null,null);
        while(cursor.moveToNext()){
            LA.d(TAG,"cursor0   "+cursor.getString(0));
        }
        cursor.close();
        helper.close();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(null != bv){
            bv.destroy();
            bv = null;
        }
    }
}
