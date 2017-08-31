package com.shanlinjinrong.views.listview;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by 丁 on 2017/8/30.
 */

public class RefreshListAdapter<T> extends RecyclerView.Adapter {

    private RefreshRecyclerView mListView;

    private ScrollResult mScrollResult;

    private List<T> mItems;

    private int mMaxItemCount;//最大条数

    private int mPageCount = 1;//总页数

    private int mLimitCount = 20;//每页显示数量

    private int nCurrentPage = 1;//当前页

    private Context mContext;


    public RefreshListAdapter(RefreshRecyclerView mListView, List<T> mItems, Context mContext) {
        this.mListView = mListView;
        this.mItems = mItems;
        this.mContext = mContext;
        setScrollListener();
    }

    private void setScrollListener() {
        mListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                // 0屏幕停止滚动；1:滚动且用户仍在触碰或手指还在屏幕上2：随用户的操作，屏幕上产生的惯性滑动；
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (newState == RecyclerView.SCROLL_STATE_IDLE && mItems.size() > 0) {
                    // 只有LinearLayoutManager才有查找第一个和最后一个可见view位置的方法
                    int lastPosition = layoutManager.findLastVisibleItemPosition();
                    if (lastPosition == mItems.size() - 1) {
                        if (getCurrentPage() < getPageCount()) {
                            //还有数据可以加载
                            mScrollResult.loadMore(getCurrentPage(), getLimitCount());
                        } else {
                            mScrollResult.loadFinish();
                        }

                    }
                }
            }
        });
    }

    public void setPage(int mMaxItemCount, int mLimitCount) {
        this.mMaxItemCount = mMaxItemCount;
        this.mLimitCount = mLimitCount;
        mPageCount = mMaxItemCount / mLimitCount;
    }

    public void setMaxItemCount(int mMaxItemCount) {
        this.mMaxItemCount = mMaxItemCount;
        mPageCount = mMaxItemCount / mLimitCount;
    }


    public int getCurrentPage() {
        return nCurrentPage;
    }

    public int getPageCount() {
        return mPageCount;
    }

    public int getLimitCount() {
        return mLimitCount;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == getItemCount()) {
            return new FooterHolder(inflater.inflate(0, parent, false));
        }
        return new ItemHolder(inflater.inflate(0, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//        if (holder instanceof FooterHolder){
//
//
//        }
    }

    @Override
    public int getItemCount() {
        return mItems == null ? 0 : mItems.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public List<T> getItems() {
        return mItems;
    }


    private class FooterHolder extends RecyclerView.ViewHolder {
        public FooterHolder(View itemView) {
            super(itemView);
        }
    }

    private class ItemHolder extends RecyclerView.ViewHolder {
        public ItemHolder(View itemView) {
            super(itemView);
        }
    }
}
