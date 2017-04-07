package com.ustcxiaoqie.learn.processoflearning.tools;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by USER on 2016-03-10.
 */
public class AppUpdateUtil {
    private static final String TAG = "AppUpdateUtil";
    private ProgressDialog Progress;
    private Context context;
    private File mFile;

    public AppUpdateUtil() {
    }

    public AppUpdateUtil(Context context) {
        this.context = context;
    }

    public void downFile(final String url) {
        Progress = new ProgressDialog(context); // 进度条，在下载的时候实时更新进度，提高用户友好度
        Progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        Progress.setTitle("正在下载");
        Progress.setMessage("请稍候...");
        Progress.setProgress(0);
        Progress.show();
        new Thread() {
            public void run() {
                HttpClient client = new DefaultHttpClient();
                HttpGet get = new HttpGet(url);
                HttpResponse response;
                try {
                    response = client.execute(get);
                    HttpEntity entity = response.getEntity();
                    int length = (int) entity.getContentLength(); // 获取文件大小
                    Progress.setMax(length); // 设置进度条的总长度
                    InputStream is = entity.getContent();
                    FileOutputStream fileOutputStream = null;
                    if (is != null) {
                //        String path = context.getApplicationContext().getFilesDir().getAbsolutePath();
                        String path = Environment.getExternalStorageDirectory().getPath();
                        mFile = new File(path, "weather.apk");
                        if(!mFile.exists()){
                            mFile.createNewFile();
                        }else {
                            mFile.delete();
                            mFile.createNewFile();
                        }
                        fileOutputStream = new FileOutputStream(mFile);
                        byte[] buf = new byte[1024]; // 缓冲区
                        int ch = -1;
                        int process = 0;
                        while ((ch = is.read(buf)) != -1) {
                            fileOutputStream.write(buf, 0, ch);
                            process += ch;
                            Progress.setProgress(process); // 这里就是关键的实时更新进度了！
                        }
                    }
                    fileOutputStream.flush();
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                        is.close();
                    }
                    Progress.cancel();
                    update();
                } catch (ClientProtocolException e) {
                    LA.d(TAG,"ClientProtocolException");
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                    LA.d(TAG,"ClientProtocolException");
                }
            }

        }.start();
    }

    // 安装文件，一般固定写法
    private void update() {
        String path = context.getApplicationContext().getFilesDir().getAbsolutePath();
        String type = "application/vnd.android.package-archive";
       // File file = new File(path,"Weather.apk");
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(mFile), type);
        context.startActivity(intent);
    }
}
