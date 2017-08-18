package com.shanlin.oa.views;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;
import android.widget.ProgressBar;

/**
 * 带进度条的WebView
 * @author Kevin Meng
 *
 */
public class ProgressWebView extends WebView {
	
	private ProgressBar progressBar;

	@SuppressWarnings("deprecation")
	public ProgressWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
		progressBar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
		progressBar.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 5, 0, 0));
        addView(progressBar);
        setWebChromeClient(new WebChromeClient());
	}
	
	public class WebChromeClient extends android.webkit.WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
            	progressBar.setVisibility(GONE);
            } else {
                if (progressBar.getVisibility() == GONE)
                	progressBar.setVisibility(VISIBLE);
                progressBar.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }
    }

    @SuppressWarnings("deprecation")
	@Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        LayoutParams lp = (LayoutParams) progressBar.getLayoutParams();
        lp.x = l;
        lp.y = t;
        progressBar.setLayoutParams(lp);
        super.onScrollChanged(l, t, oldl, oldt);
    }
}