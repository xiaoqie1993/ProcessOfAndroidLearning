package com.ustcxiaoqie.learn.processoflearning.activitys;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.KeyEvent;
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

/**
 * Created by Xiaoqie on 2017/1/8.
 */

public class AboutActivity extends Activity {
    private FrameLayout fl;
    private WebView mWebView;
    private BannerView bv;
    private long time; //计算时间差，按一次webview回退，连按（2s内）返回
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        fl = (FrameLayout) findViewById(R.id.bannerContainer);
        TextView textView = (TextView) findViewById(R.id.tv_about_app);
        StringBuffer sb = new StringBuffer();
        sb.append("<font size = 12 color = '#000000'>当前软件版本:</font>");
        sb.append("<br>");
        sb.append("<font size = 12 color = '#0000FF'>V1.1 Beta </font>");
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
        LoadADs(AboutActivity.this);
    }

    private void LoadADs(Context context) {
        this.bv = new BannerView((Activity)context, ADSize.BANNER, ADsConstants.APPID,ADsConstants.BannerPosID_BOTTOM);
        bv.setRefresh(20);
        bv.setADListener(new AbstractBannerADListener() {
            @Override
            public void onNoAD(int i) {

            }

            @Override
            public void onADReceiv() {

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
}
