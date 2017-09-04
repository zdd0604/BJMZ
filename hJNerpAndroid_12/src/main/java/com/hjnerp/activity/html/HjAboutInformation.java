package com.hjnerp.activity.html;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.hjnerp.common.ActivitySupport;
import com.hjnerp.widget.WaitDialogRectangle;
import com.hjnerpandroid.R;

/**
 * zdd
 * 加载网址的类
 */
public class HjAboutInformation extends ActivitySupport {
    private WebView webView;
    private int title;
    private String webUrl;
    private ImageView web_error;
    protected WaitDialogRectangle waitDialogRectangle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.activity_hj_about_informatio);

        waitDialogRectangle = new WaitDialogRectangle(this);
        waitDialogRectangle.show();
        waitDialogRectangle.setText("正在加载...");
        Bundle bundle = this.getIntent().getExtras();
        title = bundle.getInt("title");
        webUrl = bundle.getString("weburl");
        mActionBar.setTitle(title);

        web_error = (ImageView) findViewById(R.id.web_error);

        webView = (WebView) findViewById(R.id.wv_abouthj);
        webView.getSettings().setJavaScriptEnabled(true); // 允许运行js
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.setWebChromeClient(new MyWebViewClient());
        webView.setInitialScale(150);
        WebSettings setting = webView.getSettings();
        //不显示webview缩放按钮
        setting.setDisplayZoomControls(false);
//        setting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//        setting.setUseWideViewPort(true);
//        setting.setLoadWithOverviewMode(true);

        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);

        webView.reload();
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

        webView.setWebViewClient(new WebViewClient() { // 点击超链之后在本页面打开
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                waitDialogRectangle.dismiss();
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                waitDialogRectangle.dismiss();
                webView.setVisibility(View.GONE);
                web_error.setVisibility(View.VISIBLE);
                web_error.setImageResource(R.drawable.meiyouwangluo);
            }

        });
        webView.loadUrl(webUrl);
    }

    private class MyWebViewClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
        }
    }
}
