package com.shanlinjinrong.oa.ui.activity.my;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.hyphenate.util.NetUtils;
import com.shanlinjinrong.oa.BuildConfig;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.common.Api;
import com.shanlinjinrong.oa.ui.base.BaseActivity;
import com.shanlinjinrong.oa.views.ProgressWebView;

import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * <h3>Description: 使用帮助 </h3>
 * <b>Notes:</b> Created by KevinMeng on 2016/9/20.<br />
 */
public class UsingHelpActivity extends BaseActivity {

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.web_view)
    ProgressWebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_using_help);
        ButterKnife.bind(this);
        initToolBar();
        setTranslucentStatus(this);

        if (NetUtils.hasNetwork(this)) {
            initWebView();
        } else {
            showToast("当前没有网络连接");
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initWebView() {
        if (webView != null) {
            webView.getSettings().setJavaScriptEnabled(true);
        }
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    view.loadUrl(request.getUrl().toString());
                }
                return true;
            }

        });
        
        webView.loadUrl(BuildConfig.BASE_URL + Api.USINGHELP
                + "?time=" + new Date().getTime());
    }


    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            finish();
        }
    }

    private void initToolBar() {
        if (toolbar == null) {
            return;
        }
        setTitle("");//必须在setSupportActionBar之前调用
        toolbar.setTitleTextColor(Color.parseColor("#000000"));
        setSupportActionBar(toolbar);
        tvTitle.setText("使用帮助");
        Toolbar.LayoutParams lp = new Toolbar.LayoutParams(
                Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        tvTitle.setLayoutParams(lp);

        toolbar.setNavigationIcon(R.drawable.toolbar_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}