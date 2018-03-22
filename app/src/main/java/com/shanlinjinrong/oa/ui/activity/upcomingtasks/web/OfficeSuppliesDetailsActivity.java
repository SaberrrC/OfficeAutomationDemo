package com.shanlinjinrong.oa.ui.activity.upcomingtasks.web;

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

import com.example.retrofit.net.ApiConstant;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.manager.AppConfig;
import com.shanlinjinrong.oa.ui.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import ren.yale.android.cachewebviewlib.CacheWebView;
import ren.yale.android.cachewebviewlib.WebViewCache;

public class OfficeSuppliesDetailsActivity extends BaseActivity {

    @BindView(R.id.web_view)
    CacheWebView   mWebView;
    @BindView(R.id.tv_error_layout)
    TextView       tvErrorLayout;
    @BindView(R.id.tv_title)
    TextView       tvTitle;
    @BindView(R.id.rl_loadding)
    RelativeLayout rlloadding;
    @BindView(R.id.progress_bar)
    ProgressBar    progress_bar;
    @BindView(R.id.toolbar)
    Toolbar        toolbar;
    private WebViewClient mWebViewClient;

    private int     mWhichState = 0;
    private boolean isError     = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_office_supplies_details);
        ButterKnife.bind(this);
        //setTranslucentStatus(this);
        initToolBar();
        initWebView();
    }

    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
    private void initWebView() {
        if (mWebView != null) {
            mWebView.getSettings().setJavaScriptEnabled(true);
            // 设置允许JS弹窗
            mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

            mWebView.addJavascriptInterface(this, "android");

            mWebView.setCacheStrategy(WebViewCache.CacheStrategy.FORCE);
            mWebView.setEnableCache(false);
            mWebView.setUserAgent("Android");
            mWebView.removeJavascriptInterface("searchBoxJavaBridge_");
            mWebView.removeJavascriptInterface("accessibility");
            mWebView.removeJavascriptInterface("accessibilityTraversal");
            mWebView.getSettings().setAllowFileAccess(false);
            mWebView.getSettings().setDomStorageEnabled(true);
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
                if (isError) {
                    return;
                }
                rlloadding.setVisibility(View.GONE);

            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                hideLoadingView();
                isError = true;
                mWebView.setVisibility(View.GONE);
                progress_bar.setVisibility(View.GONE);
                rlloadding.setVisibility(View.VISIBLE);
                tvErrorLayout.setVisibility(View.VISIBLE);
                tvErrorLayout.setText(getString(R.string.net_no_connection));
                toolbar.setVisibility(View.VISIBLE);
            }
        };
        mWebView.setWebViewClient(mWebViewClient);
        String id = getIntent().getStringExtra("id");
        String which = getIntent().getStringExtra("which");
        String state = getIntent().getStringExtra("state");
        String taskId = getIntent().getStringExtra("taskId");
        mWhichState = Integer.parseInt(which);
        switch (mWhichState) {
            case 1:
                mWebView.loadUrl(ApiConstant.HTML5_URL_HOST + "#/TodoDetails?token=" + AppConfig.getAppConfig(this).getPrivateToken() + "&uid=" + AppConfig.getAppConfig(this).getPrivateUid() + "&baseUrl=" + ApiConstant.baseUrl + "&isExamine=" + mWhichState + "&id=" + id + "&state=" + state);
                break;
            case 2:
                mWebView.loadUrl(ApiConstant.HTML5_URL_HOST + "#/TodoDetails?isExamine=" + mWhichState + "&token=" + AppConfig.getAppConfig(this).getPrivateToken() + "&uid=" + AppConfig.getAppConfig(this).getPrivateUid() + "&baseUrl=" + ApiConstant.baseUrl + "&id=" + id + "&taskId=" + taskId + "&state=" + state);
                break;
            case 3:
                mWebView.loadUrl(ApiConstant.HTML5_URL_HOST + "#/TodoDetails?isExamine=" + mWhichState + "&token=" + AppConfig.getAppConfig(this).getPrivateToken() + "&uid=" + AppConfig.getAppConfig(this).getPrivateUid() + "&baseUrl=" + ApiConstant.baseUrl + "&id=" + id + "&taskId=" + taskId + "&state=" + state);
                break;
            default:
                break;
        }
        mWebView.setWebViewClient(mWebViewClient);
    }

    @JavascriptInterface
    public void toBack() {
        runOnUiThread(this::finish);
    }

    @JavascriptInterface
    public void toBackRefresh(String msg) {
        runOnUiThread(() -> {
            showToast(msg);
            setResult(-100);
            finish();
        });
    }

    @JavascriptInterface
    public void toLogin() {
        runOnUiThread(() -> {
            catchWarningByCode("401");
        });
    }

    @Override
    public void onBackPressed() {
//        if (mWebView.canGoBack()) {
//            mWebView.goBack();
//        } else {
        finish();
//        }
    }


    private void initToolBar() {
        if (toolbar == null) {
            return;
        }
        setTitle("");//必须在setSupportActionBar之前调用
        toolbar.setTitleTextColor(Color.parseColor("#000000"));
        setSupportActionBar(toolbar);
        tvTitle.setText("办公用品详情");
        Toolbar.LayoutParams lp = new Toolbar.LayoutParams(
                Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        tvTitle.setLayoutParams(lp);

        toolbar.setNavigationIcon(R.drawable.toolbar_back);
        toolbar.setNavigationOnClickListener(view -> finish());
        toolbar.setVisibility(View.GONE);
    }
}
