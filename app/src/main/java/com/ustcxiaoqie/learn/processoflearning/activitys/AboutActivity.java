package com.ustcxiaoqie.learn.processoflearning.activitys;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.ustcxiaoqie.learn.processoflearning.R;

/**
 * Created by Xiaoqie on 2017/1/8.
 */

public class AboutActivity extends Activity {
    private WebView mWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        TextView textView = (TextView) findViewById(R.id.tv_about_app);
        StringBuffer sb = new StringBuffer();
        sb.append("<font size = 12 color = '#000000'>当前软件版本:</font>");
        sb.append("<br>");
        sb.append("<font size = 12 color = '#0000FF'>V1.0.4</font>");
        sb.append("<br>");
        sb.append("<font size = 12 color = '#000000'>天气数据来源:</font>");
        sb.append("<br>");
        sb.append("<a href = 'http://openweathermap.org/'>" +
                "<font size = 12 color = '#00FF00'>openweathermap.org</font></a>");
        sb.append("<br>");
        String text = sb.toString();
        Spanned about_text = Html.fromHtml(text);
        textView.setText(about_text);

        mWebView = (WebView) findViewById(R.id.about_author_wv);
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);

    }

    public void click(View view){
        mWebView.loadUrl("http://home.ustc.edu.cn/~zt9307");
    }
}
