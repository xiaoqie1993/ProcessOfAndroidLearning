package com.ustcxiaoqie.learn.processoflearning;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.ClipDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ustcxiaoqie.learn.processoflearning.http.PostInterface;
import com.ustcxiaoqie.learn.processoflearning.http.WeatherHttpPost;
import com.ustcxiaoqie.learn.processoflearning.tools.LA;
import com.ustcxiaoqie.learn.processoflearning.views.TopBar;
import com.ustcxiaoqie.learn.processoflearning.views.TopBarOnclickListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ProgressListener,PostInterface {
    private static final String TAG = "MainActivity";
    private final static int PROGRESS_ORIGINAL = 0;
    private final static int PROGRESS_MAX = 10000;
    private SharedPreferences sharedpreferences;
    private ImageView image_progress;
    private ImageView weather_icon;
    private TextView Weather_describe;
    private ClipDrawable drawable;
    private String[] cities ;
    private int[] cityids ;
    private int[] icons = new int[]{R.drawable.na,R.drawable.sun,R.drawable.snow,R.drawable.rain,R.drawable.rain_small,R.drawable.rain_heavy,
                            R.drawable.rain_thunder,R.drawable.cloud,R.drawable.cloudy,R.drawable.fog};
    private int iconOrder = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        initDatas();
        initViews();

        getWeather(cityids[1]);
        
    }

    private void getWeather(int cityid) {
        WeatherHttpPost post = new WeatherHttpPost(cityid);
        post.getWeatherInfo(this);
    }

    private void initDatas() {
        sharedpreferences = getSharedPreferences("config.xml",MODE_PRIVATE);
        iconOrder = (sharedpreferences.getInt("icon",0)>-1&sharedpreferences.getInt("icon",0)<icons.length)?sharedpreferences.getInt("icon",0):0;
    }


    private void initViews() {
        Resources resources = getResources();
        image_progress = (ImageView)findViewById(R.id.progress);
        drawable= (ClipDrawable) image_progress.getDrawable();
        drawable.setLevel(PROGRESS_ORIGINAL);//进度条初始值
        weather_icon = (ImageView) findViewById(R.id.weathericon);
        weather_icon.setImageDrawable(resources.getDrawable(icons[iconOrder]));

        Weather_describe = (TextView) findViewById(R.id.weatherdescribe);
        Weather_describe.setText(resources.getStringArray(R.array.weatherdescribe)[iconOrder]);

        TopBar topBar = (TopBar)findViewById(R.id.TopBarId);
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
    public void getResponse(String json) {
        Log.d(TAG,json==null?"fail":json);
    }
}
