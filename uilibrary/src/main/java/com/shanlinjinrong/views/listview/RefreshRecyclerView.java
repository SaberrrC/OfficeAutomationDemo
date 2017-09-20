package com.shanlinjinrong.views.listview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.shanlinjinrong.views.listview.adapter.BaseAdapter;
import com.shanlinjinrong.views.listview.adapter.RefreshAdapter;
import com.shanlinjinrong.views.listview.listener.LoadMoreListener;

/**
 * Created by 丁 on 2017/8/30.
 * 带有上拉功能的listview
 */
public class RefreshRecyclerView extends RecyclerView {
    private RefreshAdapter mAdapter;

    public RefreshRecyclerView(Context context) {
        super(context);
    }

    public RefreshRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setOnItemClickListener(BaseAdapter.OnItemClickListener mOnItemClickListener) {
        RefreshAdapter adapter = (RefreshAdapter) getAdapter();
        adapter.setOnItemClickListener(mOnItemClickListener);
    }

//    public void setAdapter(@RefAdapter RefreshAdapter adapter) {
//        super.setAdapter(adapter);
//        mAdapter = adapter;
//    }

    public void setLoadMoreListener(LoadMoreListener mLoadMoreListener) {
        RefreshAdapter adapter = (RefreshAdapter) getAdapter();
        adapter.setLoadMoreListener(mLoadMoreListener);
    }


//    @Override
//    public void onScrollStateChanged(int state) {
//        super.onScrollStateChanged(state);
//        // 0屏幕停止滚动；1:滚动且用户仍在触碰或手指还在屏幕上2：随用户的操作，屏幕上产生的惯性滑动；
//        LinearLayoutManager layoutManager = (LinearLayoutManager) getLayoutManager();
//        if (state == RecyclerView.SCROLL_STATE_IDLE && getAdapter().getItemCount() > 0) {
//            // 只有LinearLayoutManager才有查找第一个和最后一个可见view位置的方法
//            int lastPosition = layoutManager.findLastVisibleItemPosition();
//            if (lastPosition == getAdapter().getItemCount() - 1) {
//                if (mAdapter.getCurrentPage() < mAdapter.getPageCount()) {
//                    //还有数据可以加载
//                    if (mLoadMoreListener != null)
//                        mLoadMoreListener.loadMore(getCurrentPage(), getLimitCount());
//                }
//
//            }
//        }
//    }


}
