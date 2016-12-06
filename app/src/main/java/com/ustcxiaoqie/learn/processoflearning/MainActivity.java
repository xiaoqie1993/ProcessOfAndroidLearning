package com.ustcxiaoqie.learn.processoflearning;

import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ProgressListener {
    private static final String TAG = "MainActivity";
    private final static int PROGRESS_ORIGINAL = 200;
    private ImageView image_progress;
    private ImageView image_lamp;
    private ClipDrawable drawable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }


    private void loadProgress() {
        ProgressThread thread = new ProgressThread.Builder(this)
                .setMaxProgress(10000)
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
    }
    private void turnOn(){
        TransitionDrawable drawable = (TransitionDrawable) image_lamp.getDrawable();
        drawable.startTransition(2000);
    }

    @Override
    public void onClick(View view) {
        Log.d(TAG,view.getId()+"");
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
    }
}
