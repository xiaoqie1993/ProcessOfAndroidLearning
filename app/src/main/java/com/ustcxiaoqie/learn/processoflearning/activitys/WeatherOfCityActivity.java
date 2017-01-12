package com.ustcxiaoqie.learn.processoflearning.activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ustcxiaoqie.learn.processoflearning.R;
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

    private TextView mTilte;
    private Button refreshBtn;
    private Button starBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_weatherofcity);
        initViews();
        Intent intent = getIntent();
        if (null != intent) {
            mCity = (AvailableCity) intent.getSerializableExtra(Constant.KEY_BUNDLE_CITY_OBJECT);
        }
        if (null == mCity) return;
        mTilte.setText(mCity.getName());
        initDatas();
    }

    private void initViews() {
        mTilte = (TextView) findViewById(R.id.TopBarTitleId);

        refreshBtn = (Button) findViewById(R.id.TopBarLeftBtnId);
        starBtn = (Button) findViewById(R.id.TopBarRightBtnId);
        (starBtn.getBackground()).setLevel(LEVEL_NOT_STARED);

        mTilte.setOnClickListener(this);
        refreshBtn.setOnClickListener(this);
        starBtn.setOnClickListener(this);
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
        Toast toast = Toast.makeText(WeatherOfCityActivity.this, "", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        switch (view.getId()) {
            case R.id.TopBarRightBtnId:
                int level = starBtn.getBackground().getLevel();
                if (level == 0) {
                    (starBtn.getBackground()).setLevel(LEVEL_STARED);
                    toast.setText("收藏成功");
                } else {
                    starBtn.getBackground().setLevel(LEVEL_NOT_STARED);
                    toast.setText("取消收藏");
                }
                break;
        }
        toast.show();
    }


}
