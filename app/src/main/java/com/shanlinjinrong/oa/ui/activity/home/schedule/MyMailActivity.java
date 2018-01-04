package com.shanlinjinrong.oa.ui.activity.home.schedule;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.common.Api;
import com.shanlinjinrong.oa.ui.base.BaseActivity;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import ren.yale.android.cachewebviewlib.CacheWebView;
import ren.yale.android.cachewebviewlib.WebViewCache;

/**
 * <h3>Description: 使用帮助 </h3>
 * <b>Notes:</b> Created by KevinMeng on 2016/9/20.<br />
 */
public class MyMailActivity extends BaseActivity {
    private static final String APP_CACAHE_DIRNAME = "/webcache";
    @BindView(R.id.tv_title)
    TextView     tvTitle;
    @BindView(R.id.toolbar)
    Toolbar      toolbar;
    @BindView(R.id.web_view)
    CacheWebView webView;
    @BindView(R.id.tv_error_layout)
    TextView     tvErrorLayout;

    @BindView(R.id.rl_loadding)
    RelativeLayout rlloadding;
    @BindView(R.id.progress_bar)
    ProgressBar    progress_bar;
    private WebViewClient mWebViewClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_using_help);
        ButterKnife.bind(this);
        initToolBar();
        setTranslucentStatus(this);
        initWebView();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initWebView() {
        if (webView != null) {
            webView.getSettings().setJavaScriptEnabled(true);
            webView.setCacheStrategy(WebViewCache.CacheStrategy.FORCE);
            webView.setEnableCache(true);
            webView.setUserAgent("Android");
        }
        mWebViewClient = new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                rlloadding.setVisibility(View.VISIBLE);
                tvErrorLayout.setVisibility(View.VISIBLE);
                tvErrorLayout.setText(getString(R.string.loadding));
                progress_bar.setVisibility(View.VISIBLE);
            }

            @Override
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

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                rlloadding.setVisibility(View.GONE);

            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                // hideLoadingView();
                rlloadding.setVisibility(View.VISIBLE);
                progress_bar.setVisibility(View.GONE);
                tvErrorLayout.setVisibility(View.VISIBLE);
                tvErrorLayout.setText(getString(R.string.net_no_connection));
            }
        };

        webView.setWebViewClient(mWebViewClient);

        webView.loadUrl(Api.MY_MAIL + "?time=" + System.currentTimeMillis());

        webView.setWebViewClient(mWebViewClient);
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
        tvTitle.setText("我的邮箱");
        Toolbar.LayoutParams lp = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
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
    }
}