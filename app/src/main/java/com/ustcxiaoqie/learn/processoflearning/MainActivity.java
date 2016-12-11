package com.ustcxiaoqie.learn.processoflearning;

import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ustcxiaoqie.learn.processoflearning.tools.LA;
import com.ustcxiaoqie.learn.processoflearning.views.TopBar;
import com.ustcxiaoqie.learn.processoflearning.views.TopBarOnclickListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ProgressListener {
    private static final String TAG = "MainActivity";
    private final static int PROGRESS_ORIGINAL = 0;
    private final static int PROGRESS_MAX = 10000;
    private ImageView image_progress;
    private ImageView image_lamp;
    private ClipDrawable drawable;
    private String[] cities ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        initViews();
    }


    private void loadProgress() {
        ProgressThread thread = new ProgressThread.Builder(this)
                .setMaxProgress(PROGRESS_MAX)
                .setProgress(PROGRESS_ORIGINAL)
                .build();
        thread.start();
    }


    private void initViews() {
        image_progress = (ImageView)findViewById(R.id.progress);
        image_lamp = (ImageView)findViewById(R.id.lamp);
        drawable= (ClipDrawable) image_progress.getDrawable();
        drawable.setLevel(PROGRESS_ORIGINAL);//进度条初始值
        findViewById(R.id.leftBtn).setOnClickListener(this);
        findViewById(R.id.rightBtn).setOnClickListener(this);
        ((Button)findViewById(R.id.leftBtn)).setText("ON");
        ((Button)findViewById(R.id.rightBtn)).setText("Start");
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
        cities = getResources().getStringArray(R.array.cities);
    }
    private void turnOn(){
        TransitionDrawable drawable = (TransitionDrawable) image_lamp.getDrawable();
        drawable.startTransition(2000);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.leftBtn:
                turnOn();
                break;
            case R.id.rightBtn:
                loadProgress();
                break;
        }
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
}
