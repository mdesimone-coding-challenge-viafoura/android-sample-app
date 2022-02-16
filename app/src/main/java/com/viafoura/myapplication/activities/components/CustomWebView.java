package com.viafoura.myapplication.activities.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.webkit.WebView;

public class CustomWebView extends WebView {

    public CustomWebView(Context context) {
        super(context);
        initView(context);
    }

    public CustomWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);

    }

    private void initView(Context context){
        this.getSettings().setJavaScriptEnabled(true);
        this.getSettings().setUseWideViewPort(true);
        this.getSettings().setLoadWithOverviewMode(true);
        this.getSettings().setDomStorageEnabled(true);
    }

    public void loadUrl(String url){
        super.loadUrl(url);
    }
}
