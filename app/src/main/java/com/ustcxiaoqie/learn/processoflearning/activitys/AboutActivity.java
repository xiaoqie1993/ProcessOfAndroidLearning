package com.ustcxiaoqie.learn.processoflearning.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qq.e.ads.banner.ADSize;
import com.qq.e.ads.banner.AbstractBannerADListener;
import com.qq.e.ads.banner.BannerView;
import com.ustcxiaoqie.learn.processoflearning.R;
import com.ustcxiaoqie.learn.processoflearning.tools.ADsConstants;
import com.ustcxiaoqie.learn.processoflearning.tools.Constant;
import com.ustcxiaoqie.learn.processoflearning.views.CustomDialog;

/**
 * Created by Xiaoqie on 2017/1/8.
 */

public class AboutActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "AboutActivity";
    private FrameLayout fl;
    private WebView mWebView;
    private BannerView bv;
    private TextView mTextView;  //检查更新
    private long time; //计算时间差，按一次webview回退，连按（2s内）返回
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        fl = (FrameLayout) findViewById(R.id.bannerContainer);
        mTextView = (TextView) findViewById(R.id.checkUpdate);
        mTextView.setOnClickListener(this);
        TextView textView = (TextView) findViewById(R.id.tv_about_app);
        textView.setOnClickListener(this);
        TextView callback = (TextView) findViewById(R.id.callback_activity_tv);
        callback.setOnClickListener(this);
        StringBuffer sb = new StringBuffer();
        sb.append("<font size = 12 color = '#000000'>当前软件版本:</font>");
        sb.append("<br>");
        sb.append("<font size = 12 color = '#0000FF'>"+ Constant.APP_VERSION+" </font>");
        sb.append("<br><br>");
        sb.append("<font size = 12 color = '#000000'>天气数据来源:</font>");
        sb.append("<br>");
        sb.append("<a href = 'http://openweathermap.org/'>" +
                "<font size = 12 color = '#00FF00'>openweathermap.org</font></a>");
        sb.append("<br>");
        String text = sb.toString();
        Spanned about_text = Html.fromHtml(text);
        textView.setText(about_text);

        mWebView = (WebView) findViewById(R.id.about_author_wv);
        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false;
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient());
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setBuiltInZoomControls(true);// support zoom
        settings.setLoadWithOverviewMode(true);
        mWebView.loadUrl("http://home.ustc.edu.cn/~zt9307");
        this.bv = new BannerView(this, ADSize.BANNER, ADsConstants.APPID,ADsConstants.BannerPosID_BOTTOM);
        LoadADs();
    }

    private void LoadADs() {
        Log.d(TAG,"loading");
        bv.setRefresh(20);
        bv.setADListener(new AbstractBannerADListener() {
            @Override
            public void onNoAD(int i) {
            }
            @Override
            public void onADReceiv() {
                Log.d(TAG,"onADReceiv    "     );
            }
        });
        fl.addView(bv);
        bv.loadAD();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode){
            case KeyEvent.KEYCODE_BACK:
                if(System.currentTimeMillis() - time >2000){
                    if(mWebView.canGoBack()){
                        mWebView.goBack();
                    }else{
                        Toast.makeText(AboutActivity.this,"再按一次返回键返回！",Toast.LENGTH_SHORT).show();
                    }
                    time = System.currentTimeMillis();
                }else {
                    AboutActivity.this.finish();
                }
                break;
        }
        return  true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.checkUpdate:
                showUpdateDialog(AboutActivity.this);
                break;
            case R.id.tv_about_app:
                mWebView.loadUrl("http://openweathermap.org");
                break;
            case R.id.callback_activity_tv:
                startActivity(new Intent(AboutActivity.this,CallBackActivity.class));
                break;
        }
    }

    private void showUpdateDialog(Context context) {
        StringBuilder sb = new StringBuilder();
        sb.append("由于当前缺乏稳定的服务器来存放apk安装包和后台下载程序,");
        sb.append("目前更新依赖于【应用宝】更新接口,");
        sb.append("请下载应用宝点击软件更新来下载(推荐)\n");
        sb.append("或者(不推荐)跳转到浏览器下载。\n");
        sb.append("当前版本: "+ Constant.APP_VERSION);
        CustomDialog.Builder builder = new CustomDialog.Builder(context);
        CustomDialog dialog = builder.setTitle("检查更新")
                .setMessage(sb.toString())
                .setPositiveButton("前去下载", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        String url = "http://a.app.qq.com/o/simple.jsp?pkgname=com.ustcxiaoqie.learn.processoflearning";
                        Uri uri = Uri.parse(url);
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.VIEW");
                        intent.setData(uri);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("我知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                })
                .create();
        dialog.show();
    }
}
