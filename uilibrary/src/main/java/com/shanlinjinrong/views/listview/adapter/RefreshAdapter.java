package com.shanlinjinrong.views.listview.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.shanlinjinrong.views.listview.listener.LoadMoreListener;
import com.shanlinjinrong.views.listview.RefreshRecyclerView;
import com.shanlinjinrong.views.listview.cell.BaseCell;
import com.shanlinjinrong.views.listview.cell.EmptyCell;
import com.shanlinjinrong.views.listview.cell.ErrorCell;
import com.shanlinjinrong.views.listview.cell.LoadMoreCell;
import com.shanlinjinrong.views.listview.cell.NoMoreCell;

import java.util.List;

/**
 * Created by 丁 on 2017/9/14.
 * 含有上拉加载更多
 */
public class RefreshAdapter extends BaseAdapter {

    public static final int ERROR_TYPE = 1 << 1;
    public static final int EMPTY_TYPE = 1 << 2;
    public static final int LOAD_MORE_TYPE = 1 << 3;
    public static final int NO_MORE_TYPE = 1 << 4;


    private EmptyCell mEmptyCell; // default  EmptyCell
    private ErrorCell mErrorCell; // default  ErrorCell
    private LoadMoreCell mLoadMoreCell; // default  LoadMoreCell
    private NoMoreCell mNoMoreCell; // default  NoMoreCell


    private View mCustomEmptyView; // custom EmptyCell
    private View mCustomErrorView; // custom ErrorCell
    private View mCustomMoreView;// custom LoadMoreCell
    private View mCustomNoMoreCell;// custom NoMoreCell

    private RefreshRecyclerView mListView;

    private LoadMoreListener mLoadMoreListener;

    private List<BaseCell> mItems;

    private int mMaxItemCount;//最大条数

    private int mPageCount = 1;//总页数

    private int mPageSize = 20;//每页显示数量

    private int mCurrentPage = 1;//当前页

    private Context mContext;

    private boolean isLoadMore;


    public RefreshAdapter(Context mContext, RefreshRecyclerView mListView, List<BaseCell> mItems) {
        super(mItems);
        this.mListView = mListView;
        this.mItems = mItems;
        this.mContext = mContext;
        setScrollListener();
        initCell();
    }

    private void initCell() {
        mEmptyCell = new EmptyCell(null);
        mErrorCell = new ErrorCell(null);
        mLoadMoreCell = new LoadMoreCell(null);
        mNoMoreCell = new NoMoreCell(null);
    }

    private void setScrollListener() {
        mListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //如果正在加载更多，就不做任何操作，防止多次加载
                if (isLoadMore)
                    return;
                // 0屏幕停止滚动；1:滚动且用户仍在触碰或手指还在屏幕上2：随用户的操作，屏幕上产生的惯性滑动；
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (newState == RecyclerView.SCROLL_STATE_IDLE && mItems.size() > 0) {
                    // 只有LinearLayoutManager才有查找第一个和最后一个可见view位置的方法
                    int lastPosition = layoutManager.findLastVisibleItemPosition();
                    if (lastPosition == mItems.size() - 1) {
                        Log.i("RefreshAdapter", "currentPage = " + getCurrentPage() + " ;; pageCount = " + getPageCount());
                        if (getCurrentPage() < getPageCount()) {
                            //还有数据可以加载
                            if (mLoadMoreListener != null)
                                if (mCustomMoreView != null)
                                    showLoadMore(mCustomMoreView);
                                else
                                    showLoadMore();
                            mLoadMoreListener.loadMore(getCurrentPage() + 1, getLimitCount());
                        } else {
                            //最后一页，没有数据可以加载
                            //显示没有加载更多样式
                            mLoadMoreListener.noMoreData();
                            if (mCustomNoMoreCell != null)
                                showNoMore(mCustomNoMoreCell);
                            else
                                showNoMore();
                        }
                    }
                }
            }
        });
    }

    //追加数据
    public void appendData(List<BaseCell> items) {
        if (items == null)
            return;
        mItems.addAll(items);
        if (isLoadMore) {
            mCurrentPage++;
            hideLoadMore();
        }
        notifyDataSetChanged();
    }

    public void refreshData() {
        mItems.clear();
        mCurrentPage = 1;
        notifyDataSetChanged();
    }

    public RefreshAdapter setCustomEmptyView(View mCustomEmptyView) {
        this.mCustomEmptyView = mCustomEmptyView;
        return this;
    }

    public RefreshAdapter setCustomErrorView(View mCustomErrorView) {
        this.mCustomErrorView = mCustomErrorView;
        return this;
    }

    public RefreshAdapter setCustomMoreView(View mCustomMoreView) {
        this.mCustomMoreView = mCustomMoreView;
        return this;
    }

    public RefreshAdapter setCustomNoMoreCell(View mCustomNoMoreCell) {
        this.mCustomNoMoreCell = mCustomNoMoreCell;
        return this;
    }


    public ErrorCell getErrorCell() {
        return mErrorCell;
    }

    public EmptyCell getEmptyCell() {
        return mEmptyCell;
    }

    public LoadMoreCell getLoadMoreCell() {
        return mLoadMoreCell;
    }

    public NoMoreCell getNoMoreCell() {
        return mNoMoreCell;
    }

    public void setPage(int mMaxItemCount, int mLimitCount) {
        this.mMaxItemCount = mMaxItemCount;
        this.mPageSize = mLimitCount;
        mPageCount = mMaxItemCount / mLimitCount;
    }

    public void setMaxItemCount(int mMaxItemCount) {
        this.mMaxItemCount = mMaxItemCount;
        mPageCount = mMaxItemCount / mPageSize;
    }


    public int getCurrentPage() {
        return mCurrentPage;
    }

    public int getPageCount() {
        return mPageCount;
    }

    public int getLimitCount() {
        return mPageSize;
    }


    public RefreshAdapter setLoadMoreListener(LoadMoreListener mLoadMoreListener) {
        this.mLoadMoreListener = mLoadMoreListener;
        return this;
    }

    /**
     * 隐藏LoadMoreView
     */
    public void hideLoadMore() {
        mItems.remove(mLoadMoreCell);
        isLoadMore = false;
    }

    /**
     * 显示LoadMoreView
     * 当列表滑动到底部时，调用提示加载更多，加载完数据。
     */
    public void showLoadMore() {
        if (mItems.contains(mLoadMoreCell)) {
            return;
        }
        add(mLoadMoreCell);
        isLoadMore = true;
    }

    /**
     * 指定显示的LoadMore View
     */
    public void showLoadMore(View loadMoreView) {
        if (loadMoreView == null) {
            return;
        }
        mLoadMoreCell.setCustomView(loadMoreView);
        showLoadMore();
    }


    /**
     * 显示Empty
     * 数据为空时，显示空布局。
     */
    public void showEmpty() {
        if (mItems.contains(mEmptyCell)) {
            return;
        }
        add(mEmptyCell);
    }

    /**
     * 指定显示的Empty View
     */
    public void showEmpty(View empty) {
        if (empty == null) {
            return;
        }
        mEmptyCell.setCustomView(empty);
        showEmpty();
    }

    /**
     * 隐藏Empty
     */
    public void hideEmpty() {
        mItems.remove(mNoMoreCell);
    }


    /**
     * 显示NoMoreCell
     * 当列表滑动到底部时，没有更多数据，提示。
     */
    public void showNoMore() {
        if (mItems.contains(mNoMoreCell)) {
            return;
        }
        add(mNoMoreCell);
        hideNoMore();
    }

    /**
     * 指定显示的noMore View
     */
    public void showNoMore(View noMoreView) {
        if (noMoreView == null) {
            return;
        }
        mNoMoreCell.setCustomView(noMoreView);
        showLoadMore();
    }


    /**
     * 隐藏NoMore
     */
    public void hideNoMore() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mItems.remove(mNoMoreCell);
                notifyDataSetChanged();
            }
        }, 1500);
    }

}
