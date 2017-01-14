package com.ustcxiaoqie.learn.processoflearning.activitys;

import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ClipDrawable;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
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
import com.ustcxiaoqie.learn.processoflearning.tools.Temp;
import com.ustcxiaoqie.learn.processoflearning.tools.WeatherDetail;
import com.ustcxiaoqie.learn.processoflearning.tools.WeatherInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener,
        PostInterface {
    private static final String TAG = "MainActivity";
    private final static int PROGRESS_ORIGINAL = 0;
    private final static int PROGRESS_MAX = 10000;
    private static boolean flag_fristInt = true;
    private LocalBroadcastManager mLocalBroadcastManager;


    private Button topBar_LeftBtn;
    private Button topBar_rightBtn;
    private TextView topBar_titleTv;

    private ImageView image_progress;
    private ImageView weather_icon;
    private TextView Weather_describe;
    private ListView mListView;
    private WeatherAdapter mWeatherAdapter;

    private List<WeatherInfo> mInfoArrayList = new ArrayList<>();  //保存所有城市的天气信息
    private List<HashMap<String, Object>> mHashMapList;    //适配器天气源
    private List<City> mFavoriteList;

    private ClipDrawable drawable;
    private int iconOrder = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initDatas();
        initViews();
        getAllWeathers();
    }

    private void getAllWeathers() {
        mFavoriteList.clear();
        //获取收藏城市
        DataBaseHelper helper = new DataBaseHelper(MainActivity.this,Constant.DATABASE_VERSION);
        SQLiteDatabase database = helper.getWritableDatabase();
        Cursor cursor = helper.queryFromFavoriteCity(database, new String[]{"city_name", "city_id","time_favorite"}, null, null, null, null, null);
        while (cursor.moveToNext()) {
            City city = new City();
            city.setName(cursor.getString(0));
            city.setId(cursor.getInt(1));
            LA.d(TAG,(cursor.getString(0))+"    "+(cursor.getInt(1))+"  "+(cursor.getString(2)));
            mFavoriteList.add(city);
        }
        cursor.close();
        helper.close();

        if (mFavoriteList.size() == 0) {
            mHashMapList.clear();
            topBar_titleTv.setText("请添加城市");
            mWeatherAdapter.notifyDataSetChanged();
            Toast toast = Toast.makeText(MainActivity.this,"请添加城市",Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
            return; //再次判断，若仍为0则直接返回
        }

        getWeather(mFavoriteList);
        startActivity(new Intent(MainActivity.this, DialogActivity.class));
    }

    private void getWeather(List<City> city) {
        WeatherHttpPost post = new WeatherHttpPost(city);
        post.getWeatherInfo(this, new ProgressListener() {
            @Override
            public void setImageProgress(int progress) {
                drawable.setLevel((progress / mFavoriteList.size()) * PROGRESS_MAX);
            }
        });
    }

    private void initDatas() {
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
        mHashMapList = new ArrayList<>();
        mFavoriteList = new ArrayList<>();
    }


    private void initViews() {
        Resources resources = getResources();
        findViewById(R.id.about_app).setOnClickListener(this);
        image_progress = (ImageView) findViewById(R.id.progress);
        drawable = (ClipDrawable) image_progress.getDrawable();
        drawable.setLevel(PROGRESS_ORIGINAL);//进度条初始值

        weather_icon = (ImageView) findViewById(R.id.weathericon);
        weather_icon.setImageDrawable(resources.getDrawable(R.drawable.na));

        Weather_describe = (TextView) findViewById(R.id.weatherdescribe);
        Weather_describe.setText(resources.getStringArray(R.array.weatherdescribe)[iconOrder]);

        mListView = (ListView) findViewById(R.id.weatherlists);
        mWeatherAdapter = new WeatherAdapter(this, mHashMapList);
        mListView.setAdapter(mWeatherAdapter);
        mListView.setOnItemClickListener(this);
        mListView.setOnItemLongClickListener(this);

        topBar_LeftBtn = (Button) findViewById(R.id.TopBarLeftBtnId);
        topBar_rightBtn = (Button) findViewById(R.id.TopBarRightBtnId);
        topBar_titleTv = (TextView) findViewById(R.id.TopBarTitleId);
        topBar_LeftBtn.setOnClickListener(this);
        topBar_rightBtn.setOnClickListener(this);
        topBar_titleTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.TopBarLeftBtnId:
                getAllWeathers();
                break;
            case R.id.TopBarRightBtnId:
                startActivity(new Intent(MainActivity.this, SearchActivity.class));
                break;
            case R.id.TopBarTitleId:
                break;
            case R.id.about_app:
                startActivity(new Intent(MainActivity.this, AboutActivity.class));
                break;
        }
    }

    @Override
    public void getResponse(List<WeatherInfo> list) {
        if (null == list) return;
        LA.d(TAG,"liszt "+list.size()+"");
        mInfoArrayList = list;
        //设置上面显示内容
        setAboveViews();
        Intent intent = new Intent(Constant.DIALOG_ACTIVITY_FINISH);
        mHashMapList.clear();
        for (int s = 0; s < list.size(); s++) {
            WeatherInfo info = list.get(s);
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("cityname", info.getCity().getName());
            map.put("icon", DataTransferTool.getIconFromWeatherDetail(info.getList().get(0).getWeather().getDesciption()));
            map.put("detail", info.getList().get(0).getWeather().getDesciption());
            mHashMapList.add(map);
        }
        LA.d(TAG,mHashMapList.size()+"aaa");
        mWeatherAdapter.notifyDataSetChanged();

        intent.putExtra(Constant.PROGRESS, Constant.PROGRESS_FINISH);
        mLocalBroadcastManager.sendBroadcast(intent);
        drawable.setLevel(PROGRESS_ORIGINAL);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        /**
         * parent:listview
         * view : item view
         * position : 当前item在listview的位置
         * id :
         */
        //获取当前项的所有天气信息

        City city = mInfoArrayList.get(position).getCity();

        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.KEY_BUNDLE_CITY_OBJECT, city);
        Intent intent = new Intent();
        intent.putExtras(bundle);
        intent.setClass(MainActivity.this, WeatherOfCityActivity.class);
        startActivity(intent);
    }

    private void setAboveViews() {
        WeatherInfo weatherInfo = mInfoArrayList.get(0);
        List<WeatherDetail> mDetailList = weatherInfo.getList();
        String city = weatherInfo.getCity().getName();
        WeatherDetail detail = mDetailList.get(0); //今天的天气信息
        Temp temp = detail.getTemp();  //今天的温度
        float temp_day = temp.getDay();
        float temp_min = temp.getMin();
        float temp_max = temp.getMax();
        String weather_describe = detail.getWeather().getDesciption();
        String tv2 = weather_describe + "\n" + "Current:" + temp_day + "℃"
                + "\n" + "Range:" + temp_min + "℃" + "~" + temp_max + "℃";
        weather_icon.setImageDrawable(
                getResources().getDrawable(DataTransferTool.getIconFromWeatherDetail(weather_describe)));
        Weather_describe.setText(tv2);
        topBar_titleTv.setText(city);
    }


    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        WeatherInfo weatherInfo = mInfoArrayList.get(position);
        Intent i = new Intent();
        i.setClass(MainActivity.this, ShareActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("weather", weatherInfo);
        i.putExtras(bundle);
        startActivity(i);
        this.overridePendingTransition(R.anim.activity_open, 0);
        return true;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        LA.d(TAG, "finalized!");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mLocalBroadcastManager) {
            mLocalBroadcastManager = null;
        }
    }
}
