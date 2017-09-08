package com.shanlinjinrong.views.refresh;

import android.content.Context;
import android.util.AttributeSet;

public class RefreshLayout extends BaseRefreshLayout {

    private DefaultHeader mDefaultHeader;

    public RefreshLayout(Context context) {
        super(context);
        initViews();
    }

    public RefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    public RefreshLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initViews();
    }

    private void initViews() {
        mDefaultHeader = new DefaultHeader(getContext());
        setHeaderView(mDefaultHeader);
        setRefreshUiListener(mDefaultHeader);
    }

    public DefaultHeader getHeader() {
        return mDefaultHeader;
    }

}
