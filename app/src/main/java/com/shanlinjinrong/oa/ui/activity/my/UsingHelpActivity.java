package com.shanlinjinrong.oa.ui.activity.my;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.ui.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import ren.yale.android.cachewebviewlib.CacheWebView;
import ren.yale.android.cachewebviewlib.WebViewCache;

/**
 * <h3>Description: 使用帮助 </h3>
 * <b>Notes:</b> Created by KevinMeng on 2016/9/20.<br />
 */
public class UsingHelpActivity extends BaseActivity {// extends BaseActivity

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.web_view)
    CacheWebView webView;
    @BindView(R.id.tv_error_layout)
    TextView tvErrorLayout;

    @BindView(R.id.rl_loadding)
    RelativeLayout rlloadding;
    @BindView(R.id.progress_bar)
    ProgressBar progress_bar;

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

    @SuppressLint({"SetJavaScriptEnabled", "JavascriptInterface"})
    private void initWebView() {
        if (webView != null) {
            webView.getSettings().setJavaScriptEnabled(true);
            // 设置允许JS弹窗
            webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

            webView.addJavascriptInterface(this, "android");
            webView.setCacheStrategy(WebViewCache.CacheStrategy.FORCE);
            webView.setEnableCache(false);
            webView.setUserAgent("Android");
            webView.removeJavascriptInterface("searchBoxJavaBridge_");
            webView.removeJavascriptInterface("accessibility");
            webView.removeJavascriptInterface("accessibilityTraversal");
            webView.getSettings().setAllowFileAccess(false);
            webView.getSettings().setSavePassword(false);
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
//        webView.loadUrl("http://api.oa.shanlinjinrong.com/helper/index" + "?time=" + System.currentTimeMillis());
//        webView.loadUrl("http://10.5.203.123:8081/#/UsingHelp" + "?time=" + System.currentTimeMillis());
        webView.loadUrl("file:///android_asset/UsingHelp.html");
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

    @JavascriptInterface
    public void toBack() {
        finish();
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
        toolbar.setNavigationOnClickListener(view -> finish());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}