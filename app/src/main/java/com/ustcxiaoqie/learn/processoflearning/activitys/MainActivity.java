package com.ustcxiaoqie.learn.processoflearning.activitys;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.ClipDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ustcxiaoqie.learn.processoflearning.ProgressListener;
import com.ustcxiaoqie.learn.processoflearning.R;
import com.ustcxiaoqie.learn.processoflearning.WeatherAdapter;
import com.ustcxiaoqie.learn.processoflearning.http.PostInterface;
import com.ustcxiaoqie.learn.processoflearning.http.WeatherHttpPost;
import com.ustcxiaoqie.learn.processoflearning.tools.DataTransferTool;
import com.ustcxiaoqie.learn.processoflearning.tools.LA;
import com.ustcxiaoqie.learn.processoflearning.tools.Temp;
import com.ustcxiaoqie.learn.processoflearning.tools.WeatherDetail;
import com.ustcxiaoqie.learn.processoflearning.tools.WeatherInfo;
import com.ustcxiaoqie.learn.processoflearning.tools.WeatherJsonParseTool;
import com.ustcxiaoqie.learn.processoflearning.views.TopBar;
import com.ustcxiaoqie.learn.processoflearning.views.TopBarOnclickListener;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener, ProgressListener,PostInterface {
    private static final String TAG = "MainActivity";
    private final static int PROGRESS_ORIGINAL = 0;
    private final static int PROGRESS_MAX = 10000;
    private SharedPreferences sharedpreferences;
    private ImageView image_progress;
    private ImageView weather_icon;
    private TextView Weather_describe;
    private ListView mListView;
    private WeatherAdapter mWeatherAdapter;
    private List<WeatherInfo> mInfoArrayList = new ArrayList<WeatherInfo>();  //保存所有城市的天气信息
    private List<HashMap<String,Object>> mHashMapList;
    private TopBar topBar;
    private ClipDrawable drawable;
    private String[] cities ;
    private int[] cityids ;
    private int iconOrder = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        initDatas();
        initViews();
        startActivity(new Intent(MainActivity.this,DialogActivity.class));
        for(int id:cityids) {
            getWeather(id);
        }
    }

    private void getWeather(int cityid) {
        WeatherHttpPost post = new WeatherHttpPost(cityid);
        post.getWeatherInfo(this);
    }

    private void initDatas() {
        mHashMapList = new ArrayList<>();
        //初始配置
        HashMap<String,Object> map = new HashMap<>();
        map.put("cityname","合肥");
        map.put("icon",R.drawable.na);
        map.put("detail","未知");
        mHashMapList.add(map);
    }


    private void initViews() {
        Resources resources = getResources();
        image_progress = (ImageView)findViewById(R.id.progress);
        drawable= (ClipDrawable) image_progress.getDrawable();
        drawable.setLevel(PROGRESS_ORIGINAL);//进度条初始值
        weather_icon = (ImageView) findViewById(R.id.weathericon);
        weather_icon.setImageDrawable(resources.getDrawable(R.drawable.na));

        Weather_describe = (TextView) findViewById(R.id.weatherdescribe);
        Weather_describe.setText(resources.getStringArray(R.array.weatherdescribe)[iconOrder]);

        mListView = (ListView) findViewById(R.id.weatherlists);
        mWeatherAdapter= new WeatherAdapter(this,mHashMapList);
        mListView.setAdapter(mWeatherAdapter);
        mListView.setOnItemClickListener(this);
        mListView.setOnItemLongClickListener(this);

        topBar = (TopBar)findViewById(R.id.TopBarId);
        topBar.onTopBarClickListener(new TopBarOnclickListener() {
            @Override
            public void onLeftBtnClicked(Button leftBtn) {
                LA.d(TAG,"LeftBtnClicked");
            }

            @Override
            public void onRightBtnClicked(Button rightBtn) {
                LA.d(TAG,"rightBtnClicked");
            }

            @Override
            public void onTitleClicked(TextView title) {
                LA.d(TAG,"TitleClicked");
            }
        });
        cities = resources.getStringArray(R.array.cities);
        cityids = resources.getIntArray(R.array.cityIds);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void setImageProgress(int progress) {
        drawable.setLevel(progress);
        if(progress==PROGRESS_MAX||progress==PROGRESS_ORIGINAL){
            image_progress.setVisibility(View.INVISIBLE);

        }else {
            image_progress.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void getResponse(final String json) {
        Log.d(TAG,json==null?"fail":json);
        final BackgroundDataHandler handler = new BackgroundDataHandler();
        final WeatherJsonParseTool tool = new WeatherJsonParseTool(json);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message = Message.obtain();

                try {
                   WeatherInfo info = tool.parseDataToJSon();
                    mInfoArrayList.add(info);
                    if(mInfoArrayList.size() == cityids.length) {
                        //全部加载完毕，开始通知更新UI
                        message.what = 0;
                        handler.sendMessage(message);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG,"Error!");
                    message.what =1;
                    handler.sendMessage(message);
                }
            }
        }).start();

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
        WeatherInfo weatherInfo = mInfoArrayList.get(position);
        List<WeatherDetail> mDetailList = weatherInfo.getList();
        String city = weatherInfo.getCity().getName();
        WeatherDetail detail = mDetailList.get(0); //今天的天气信息
        Temp temp = detail.getTemp();  //今天的温度
        float temp_day = temp.getDay();
        float temp_min = temp.getMin();
        float temp_max = temp.getMax();
        String weather_describe = detail.getWeather().getDesciption();
        String tv2 = weather_describe+"\n"+"Current:"+temp_day+"℃"
                +"\n"+"Range:"+temp_min+"℃"+"~"+temp_max+"℃";
        weather_icon.setImageDrawable(
                getResources().getDrawable(DataTransferTool.getIconFromWeatherDetail(weather_describe)));
        Weather_describe.setText(tv2);
        topBar.getmTitle().setText(city);
    }


    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return false;
    }

    private class BackgroundDataHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    mHashMapList.clear();
                    for(int s= 0;s<mInfoArrayList.size();s++) {

                        WeatherInfo info = mInfoArrayList.get(s);
                        Log.d(TAG,"tool:"+info.toString());
                        HashMap<String, Object> map = new HashMap<String,Object>();
                        map.put("cityname", info.getCity().getName());
                        map.put("icon", DataTransferTool.getIconFromWeatherDetail(info.getList().get(0).getWeather().getDesciption()));
                        map.put("detail", info.getList().get(0).getWeather().getDesciption());
                        mHashMapList.add(map);
                    }
                    mWeatherAdapter.notifyDataSetChanged();
                    break;
                case 1:
                    break;
            }
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        Log.d(TAG,"finalized!");
    }
}
