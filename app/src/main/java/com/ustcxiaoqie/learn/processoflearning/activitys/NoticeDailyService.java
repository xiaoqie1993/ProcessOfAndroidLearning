package com.ustcxiaoqie.learn.processoflearning.activitys;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import com.ustcxiaoqie.learn.processoflearning.R;
import com.ustcxiaoqie.learn.processoflearning.tools.LA;

import java.util.Calendar;

public class NoticeDailyService extends Service {
    private static final String TAG = "NoticeDailyService";
    private NoticeBroadCast receiver;

    public NoticeDailyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LA.d(TAG, "Service被创建啦！");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        NoticeBroadCast receiver = new NoticeBroadCast();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_TIME_TICK);
        registerReceiver(receiver, filter);
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private class NoticeBroadCast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Calendar calendar = Calendar.getInstance();
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int month = calendar.get(Calendar.MONTH);
            int year = calendar.get(Calendar.YEAR);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int min = calendar.get(Calendar.MINUTE);
            int sec = calendar.get(Calendar.SECOND);
            LA.d(TAG, "time:" + hour + ":" + min + ":" + sec);
            NotificationManager manager = null;
            Notification notification = null;
            //每天七点半发送通知
            if (hour == 7 && min == 30) {
                manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                Intent intent1 = new Intent(context, StartActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent1, 0);
                Notification.Builder builder = new Notification.Builder(context);
                builder.setContentTitle("早上好")
                        .setWhen(System.currentTimeMillis())
                        .setContentText("今天是" + year + "年" + month + "月" + day + "日")
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setSmallIcon(R.drawable.sun)
                        .setTicker("Weather祝您好心情");
                notification = builder.build();
                notification.flags = Notification.FLAG_AUTO_CANCEL;
                notification.contentIntent = pendingIntent;
                manager.notify(0, notification);
            }
        }

        ;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != receiver) {
            unregisterReceiver(receiver);
            receiver = null;
        }
    }
}
