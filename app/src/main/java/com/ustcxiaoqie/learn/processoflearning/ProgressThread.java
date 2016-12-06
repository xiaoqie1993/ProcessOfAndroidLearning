package com.ustcxiaoqie.learn.processoflearning;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.ustcxiaoqie.learn.processoflearning.tools.LA;

/**
 * Created by Administrator on 2016/12/6 17:02.
 * Copyright (c) 2016-12-06 Bryant1993 All rights reserved.
 */


public class ProgressThread extends Thread {
    private static final String TAG = "ProgressThread";
    private static final int UPDATE_PROGRESS = 0;
    private int maxProgress;
    private int progress;
    private ProgressHandler handler;
    private ProgressListener listener;
    private ProgressThread(){
        handler = new ProgressHandler();
    }
    @Override
    public void run() {
        synchronized (ProgressHandler.class) {
            while (progress != maxProgress) {
                progress += 100;
                Message message = new Message();
                message.what = UPDATE_PROGRESS;
                message.obj = progress;
                handler.sendMessage(message);
                try {
                    Thread.sleep(100);
                    Log.d(TAG, progress + "");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    LA.e(TAG, "后台线程发生异常");
                }
            }
            Log.d(TAG, "Progress:" + progress);
            Log.d(TAG, "加载完成");
        }
    }
    
    private class ProgressHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.d(TAG,"shoudao"+(int)msg.obj);
            switch (msg.what){
                case UPDATE_PROGRESS:
                    listener.setImageProgress((Integer) msg.obj);
                    break;
            }
        }
    }

    public static class Builder{
        private int maxProgress;
        private int progress;
        private ProgressListener listener;
        public Builder(ProgressListener listener){
            //默认配置
            this.listener = listener;
            this.progress = 0;
            this.maxProgress = 10000;
        }
        public Builder setMaxProgress(int maxProgress){
            this.maxProgress = maxProgress;
            return this;
        }
        public Builder setProgress(int progress){
            this.progress = progress;
            return this;
        }
        private void apply(ProgressThread thread){
            thread.progress = progress;
            thread.maxProgress = maxProgress;
            thread.listener = listener;
        }
        public ProgressThread build(){
            ProgressThread thread = new ProgressThread();
            apply(thread);
            return thread;
        }
    }
}
