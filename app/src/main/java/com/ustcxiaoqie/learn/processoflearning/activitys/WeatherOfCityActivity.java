package com.ustcxiaoqie.learn.processoflearning.activitys;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ustcxiaoqie.learn.processoflearning.R;
import com.ustcxiaoqie.learn.processoflearning.database.DataBaseHelper;
import com.ustcxiaoqie.learn.processoflearning.http.PostInterface;
import com.ustcxiaoqie.learn.processoflearning.http.WeatherHttpPost;
import com.ustcxiaoqie.learn.processoflearning.tools.AvailableCity;
import com.ustcxiaoqie.learn.processoflearning.tools.Constant;
import com.ustcxiaoqie.learn.processoflearning.tools.LA;
import com.ustcxiaoqie.learn.processoflearning.tools.WeatherInfo;
import com.ustcxiaoqie.learn.processoflearning.tools.WeatherJsonParseTool;

import org.json.JSONException;

/**
 * Created by Xiaoqie on 2017/1/10.
 */
public class WeatherOfCityActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "WeatherOfCityActivity";
    private static final int LEVEL_STARED = 10;
    private static final int LEVEL_NOT_STARED = 0;
    private AvailableCity mCity;
    private WeatherInfo info;
    private DataBaseHelper helper = new DataBaseHelper(WeatherOfCityActivity.this, DataBaseHelper.DB_NAME);
    private TextView mTilte;
    private Button refreshBtn;
    private Button starBtn;
    private SQLiteDatabase mSQLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_weatherofcity);
        Intent intent = getIntent();
        if (null != intent) {
            mCity = (AvailableCity) intent.getSerializableExtra(Constant.KEY_BUNDLE_CITY_OBJECT);
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
        Cursor cursor = helper.queryFromFavoriteCity(mSQLiteDatabase,null,"city_name=?",new String[]{mCity.getName()},null,null,null);
        if(cursor.moveToNext()){
            (starBtn.getBackground()).setLevel(LEVEL_STARED);
        }else{
            (starBtn.getBackground()).setLevel(LEVEL_NOT_STARED);
        }
        cursor.close();
        mSQLiteDatabase.close();
    }

    private void initDatas() {
        WeatherHttpPost post = new WeatherHttpPost(mCity.get_id());
        post.getWeatherInfo(new PostInterface() {
            @Override
            public void getResponse(String json) {
                if (null == json) return;
                WeatherJsonParseTool tool = new WeatherJsonParseTool(json);
                try {
                    info = tool.parseDataToJSon();
                } catch (JSONException e) {
                    e.printStackTrace();
                    info = null;
                }
                if (null == info) return;
                LA.d(TAG, info.toString());
                setDataToViews();
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
                if (level == 0) {
                    (starBtn.getBackground()).setLevel(LEVEL_STARED);
                    insertCityToFavorate(mCity);
                } else {
                    starBtn.getBackground().setLevel(LEVEL_NOT_STARED);
                    deleteCityFromFavorate(mCity);
                }
                break;
        }
    }

    private void deleteCityFromFavorate(final AvailableCity city) {
        mSQLiteDatabase = helper.getWritableDatabase();
        final DataHandler handler1 = new DataHandler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                mSQLiteDatabase.delete(DataBaseHelper.TABLE_FAVORATE_CITIES,"city_name=?",new String[]{mCity.getName()});
                mSQLiteDatabase.close();
                Message message = Message.obtain();
                message.what = 0;
                handler1.sendMessage(message);
            }
        }).start();
    }

    private void insertCityToFavorate(final AvailableCity city) {
        mSQLiteDatabase = helper.getWritableDatabase();
        final DataHandler handler = new DataHandler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                ContentValues values = new ContentValues();
                values.put("city_name", city.getName());
                values.put("city_id", city.get_id());

                LA.d(TAG,"values:"+values.getAsString("city_name"));

                helper.insertIntoFavoriteCity(mSQLiteDatabase,null,values,true);
    //            mSQLiteDatabase.insert(DataBaseHelper.TABLE_FAVORATE_CITIES,null,values);
                mSQLiteDatabase.close();
                Message message = Message.obtain();
                message.what = 1;
                handler.sendMessage(message);
            }
        }).start();
    }

    private class DataHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Toast toast = Toast.makeText(WeatherOfCityActivity.this, "", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            switch (msg.what) {
                case 0:
                    toast.setText("取消收藏");
                    break;
                case 1:
                    toast.setText("收藏成功");
                    break;
            }
            toast.show();
        }
    }


}
