package com.shanlinjinrong.oa.ui.activity.upcomingtasks.web;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.manager.AppConfig;

import butterknife.BindView;
import butterknife.ButterKnife;
import ren.yale.android.cachewebviewlib.CacheWebView;
import ren.yale.android.cachewebviewlib.WebViewCache;

public class OfficeSuppliesDetailsActivity extends AppCompatActivity {


    @BindView(R.id.web_view)
    CacheWebView mWebView;

    private WebViewClient mWebViewClient;

    private int mWhichState = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_office_supplies_details);
        ButterKnife.bind(this);
        //setTranslucentStatus(this);
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
            mWebView.setEnableCache(true);
            mWebView.setUserAgent("Android");
            mWebView.removeJavascriptInterface("searchBoxJavaBridge_");
            mWebView.removeJavascriptInterface("accessibility");
            mWebView.removeJavascriptInterface("accessibilityTraversal");
            mWebView.getSettings().setAllowFileAccess(false);
        }


        mWebViewClient = new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                //rlloadding.setVisibility(View.VISIBLE);
                //tvErrorLayout.setVisibility(View.VISIBLE);
                //tvErrorLayout.setText(getString(R.string.loadding));
                //progress_bar.setVisibility(View.VISIBLE);
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
                //rlloadding.setVisibility(View.GONE);

            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                // hideLoadingView();
                //rlloadding.setVisibility(View.VISIBLE);
                //progress_bar.setVisibility(View.GONE);
                //tvErrorLayout.setVisibility(View.VISIBLE);
                //tvErrorLayout.setText(getString(R.string.net_no_connection));
            }
        };

        mWebView.setWebViewClient(mWebViewClient);

        String which = getIntent().getStringExtra("which");
        mWhichState = Integer.parseInt(which);

        mWebView.loadUrl("http://10.0.2.2:8080/#/TodoDetails?which=" + mWhichState + "&Token" + AppConfig.getAppConfig(this).getPrivateToken() + "&uid" + AppConfig.getAppConfig(this).getPrivateUid());

        mWebView.setWebViewClient(mWebViewClient);
    }

    @JavascriptInterface
    public void toBack() {
        runOnUiThread(this::finish);
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            finish();
        }
    }
}
