package com.shanlinjinrong.views.listview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by 丁 on 2017/8/30.
 * 带有上拉功能的listview
 */
public class RefreshRecyclerView extends RecyclerView {
    private RefreshListAdapter mAdapter;

    private ScrollResult mScrollResult;

    public RefreshRecyclerView(Context context) {
        super(context);
    }

    public RefreshRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RefreshRecyclerView setScrollResult(ScrollResult mScrollResult) {
        this.mScrollResult = mScrollResult;
        return this;
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        mAdapter = (RefreshListAdapter) adapter;
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        // 0屏幕停止滚动；1:滚动且用户仍在触碰或手指还在屏幕上2：随用户的操作，屏幕上产生的惯性滑动；
        LinearLayoutManager layoutManager = (LinearLayoutManager) getLayoutManager();
        if (state == SCROLL_STATE_IDLE && mAdapter.getItems().size() > 9) {
            // 只有LinearLayoutManager才有查找第一个和最后一个可见view位置的方法
            int lastPosition = layoutManager.findLastVisibleItemPosition();
            if (lastPosition == mAdapter.getItems().size() - 1) {
                if (mAdapter.getCurrentPage() < mAdapter.getPageCount()) {
                    //还有数据可以加载
                    mScrollResult.loadMore(mAdapter.getCurrentPage(), mAdapter.getLimitCount());
                } else {
                    mScrollResult.loadFinish();
                }


//                View view = View.inflate(PushListActivity.this, R.layout.load_more_layout, null);
//                if (hasMore) {
//                    //如果没有在加载，才去加载
//                    if (!isLoading) {
//                        isLoading = true;
//                        mAdapter.addFooterView(view, list.size());
//                        handler.sendEmptyMessageDelayed(LOAD_MORE_CONTENT, 1000);
//                    }
//                } else {
//                    handler.sendEmptyMessage(NO_MORE_CONTENT);
//                }
            }
        }
    }


}
