package com.shanlinjinrong.oa.ui.activity.my;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.hyphenate.util.NetUtils;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.shanlinjinrong.oa.BuildConfig;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.common.Api;
import com.shanlinjinrong.oa.ui.base.BaseActivity;
import com.shanlinjinrong.oa.views.ProgressWebView;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;

/**
 * <h3>Description: 使用帮助 </h3>
 * <b>Notes:</b> Created by KevinMeng on 2016/9/20.<br />
 */
public class UsingHelpActivity extends BaseActivity {

    private static final String APP_CACAHE_DIRNAME = "/webcache";

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.web_view)
    ProgressWebView webView;
    @BindView(R.id.tv_error_layout)
    TextView tvErrorLayout;

    private WebViewClient mWebViewClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_using_help);
        ButterKnife.bind(this);
        initToolBar();
        setTranslucentStatus(this);

        if (NetUtils.hasNetwork(this)) {
            tvErrorLayout.setVisibility(View.GONE);
            initWebView();
        } else {
            showToast(getString(R.string.net_no_connection));
            tvErrorLayout.setVisibility(View.VISIBLE);
        }

        tvErrorLayout.setOnClickListener(view -> {
            if (NetUtils.hasNetwork(UsingHelpActivity.this)) {
                RxView.clicks(tvErrorLayout).throttleFirst(1, TimeUnit.SECONDS).subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        initWebView();
                    }
                }, Throwable::printStackTrace);
            }
        });
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initWebView() {

        if (webView != null) {
            webView.getSettings().setJavaScriptEnabled(true);
        }

        mWebViewClient = new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                showLoadingView();
            }

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
                hideLoadingView();
                tvErrorLayout.setVisibility(View.GONE);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                hideLoadingView();
                tvErrorLayout.setText(getString(R.string.net_no_connection));
            }
        };

        webView.setWebViewClient(mWebViewClient);

        webView.loadUrl(BuildConfig.BASE_URL + Api.USINGHELP + "?time=" + new Date().getTime());

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
    }
}