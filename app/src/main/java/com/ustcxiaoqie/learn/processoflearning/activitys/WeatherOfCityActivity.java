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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ustcxiaoqie.learn.processoflearning.ProgressListener;
import com.ustcxiaoqie.learn.processoflearning.R;
import com.ustcxiaoqie.learn.processoflearning.WeatherAdapter;
import com.ustcxiaoqie.learn.processoflearning.database.DataBaseHelper;
import com.ustcxiaoqie.learn.processoflearning.http.PostInterface;
import com.ustcxiaoqie.learn.processoflearning.http.WeatherHttpPost;
import com.ustcxiaoqie.learn.processoflearning.tools.City;
import com.ustcxiaoqie.learn.processoflearning.tools.Constant;
import com.ustcxiaoqie.learn.processoflearning.tools.DataTransferTool;
import com.ustcxiaoqie.learn.processoflearning.tools.LA;
import com.ustcxiaoqie.learn.processoflearning.tools.WeatherInfo;

import java.util.ArrayList;
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
    private DataBaseHelper helper = new DataBaseHelper(WeatherOfCityActivity.this);
    private TextView mTilte;
    private Button refreshBtn;
    private Button starBtn;

    private ListView mListView;
    private List<HashMap<String,Object>> mHashMapList;
    private WeatherAdapter mAdapter;
    private SQLiteDatabase mSQLiteDatabase;

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
    }

    private void initViews() {

        mTilte = (TextView) findViewById(R.id.TopBarTitleId);
        mTilte.setText(mCity.getName());
        refreshBtn = (Button) findViewById(R.id.TopBarLeftBtnId);
        starBtn = (Button) findViewById(R.id.TopBarRightBtnId);


        mTilte.setOnClickListener(this);
        refreshBtn.setOnClickListener(this);
        starBtn.setOnClickListener(this);
        mSQLiteDatabase = helper.getWritableDatabase();
        Cursor cursor = helper.queryFromFavoriteCity(mSQLiteDatabase,new String[]{"city_name","city_id"},null,null,null,null,null);
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
        mAdapter = new WeatherAdapter(WeatherOfCityActivity.this,mHashMapList);
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
            public void setImageProgress(int progress) {

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
        mSQLiteDatabase.delete(DataBaseHelper.TABLE_FAVORATE_CITIES,"city_name=?",new String[]{city.getName()});
        helper.close();
        (starBtn.getBackground()).setLevel(LEVEL_NOT_STARED);
        toast.setText("取消收藏");
        Cursor cursor = helper.queryFromFavoriteCity(helper.getWritableDatabase(),new String[]{"city_name"},null,null,null,null,null);
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
        values.put("city_name", city.getName());
        values.put("city_id", city.getId());
        helper.insertIntoFavoriteCity(mSQLiteDatabase, null, values, true);
        helper.close();
        (starBtn.getBackground()).setLevel(LEVEL_STARED);
        toast.setText("收藏成功");

        Cursor cursor = helper.queryFromFavoriteCity(helper.getWritableDatabase(),new String[]{"city_name"},null,null,null,null,null);
        while(cursor.moveToNext()){
            LA.d(TAG,"cursor0   "+cursor.getString(0));
        }
        cursor.close();
        helper.close();

    }


}
