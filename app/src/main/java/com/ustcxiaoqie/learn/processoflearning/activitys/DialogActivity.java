package com.ustcxiaoqie.learn.processoflearning.activitys;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ustcxiaoqie.learn.processoflearning.R;
import com.ustcxiaoqie.learn.processoflearning.tools.Constant;
import com.ustcxiaoqie.learn.processoflearning.tools.LA;

/**
 * Created by Xiaoqie on 2017/1/5.
 */

public class DialogActivity extends Activity {
    private static final String TAG = "DialogActivity";
    private LocalBroadcastManager mLocalBroadcastManager;
    private CloseThisByBroadCastReceiver mReceiver;
    private TextView mTextView;  //进度提示文字
    private ProgressBar mProgressBar;   //进度
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_activity);
        mTextView = (TextView) findViewById(R.id.progress_text);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        IntentFilter filter = new IntentFilter(Constant.DIALOG_ACTIVITY_FINISH);
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(DialogActivity.this);
        mReceiver = new CloseThisByBroadCastReceiver();
        mLocalBroadcastManager.registerReceiver(mReceiver,filter);
    }
    private class CloseThisByBroadCastReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            int progress = intent.getIntExtra(Constant.PROGRESS,0);
            LA.d(TAG,"progress  "+progress);
            switch (progress){
                case Constant.PROGRESS_START:
                    break;
                case Constant.PROGRESS_HALF:
                    break;
                case Constant.PROGRESS_FINISH:
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            DialogActivity.this.finish();
                        }
                    },500);
                    break;
                case Constant.PROGRESS_FAILED:
                    Toast.makeText(DialogActivity.this, "加载失败\n请稍后重试!", Toast.LENGTH_SHORT).show();
                    DialogActivity.this.finish();
                    break;
            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(null != mLocalBroadcastManager){
            mLocalBroadcastManager.unregisterReceiver(mReceiver);
        }
    }
}
