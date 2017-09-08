package com.shanlinjinrong.views.refresh;

import com.shanlinjinrong.views.refresh.indicator.RefreshIndicator;

/**
 *
 */
public interface HeaderRefreshListener {

    void onHeaderReset(BaseRefreshLayout layout);

    /**
     * prepare for loading
     *
     * @param layout
     */
    void onRefreshPrepare(BaseRefreshLayout layout);

    /**
     * perform refreshing UI
     */
    void onRefreshBegin(BaseRefreshLayout layout);

    /**
     * perform UI after refresh
     */
    void onRefreshComplete(BaseRefreshLayout layout);

    void onPositionChange(BaseRefreshLayout layout, boolean isUnderTouch, byte status, RefreshIndicator ptrIndicator);
}
