package com.shanlinjinrong.views.baserecycler.base;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.shanlinjinrong.views.baserecycler.cell.EmptyCell;
import com.shanlinjinrong.views.baserecycler.cell.ErrorCell;
import com.shanlinjinrong.views.baserecycler.cell.LoadMoreCell;
import com.shanlinjinrong.views.baserecycler.cell.LoadingCell;


/**
 * 封装了四种基础Cell的RvBasicAdapter
 * Created by k.huang on 2017/2/20.
 */

public class SimpleRVBasicAdapter extends RVBasicAdapter {
    public static final int ERROR_TYPE = Integer.MAX_VALUE - 1;
    public static final int EMPTY_TYPE = Integer.MAX_VALUE - 2;
    public static final int LOADING_TYPE = Integer.MAX_VALUE - 3;
    public static final int LOAD_MORE_TYPE = Integer.MAX_VALUE - 4;

    private EmptyCell mEmptyCell;
    private ErrorCell mErrorCell;
    private LoadingCell mLoadingCell;
    private LoadMoreCell mLoadMoreCell;
    //LoadMore 是否已显示
    private boolean mIsShowLoadMore = false;
    private boolean mIsShowError = false;
    private boolean mIsShowLoading = false;
    private boolean mIsShowEmpty = false;

    public SimpleRVBasicAdapter() {
        mEmptyCell = new EmptyCell(null);
        mErrorCell = new ErrorCell(null);
        mLoadingCell = new LoadingCell(null);
        mLoadMoreCell = new LoadMoreCell(null);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager){
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) manager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int viewType = getItemViewType(position);
                    // span size表示一个item的跨度，跨度了多少个span
                    // 当viewType是以下几种类型，默认为一行。
                    return (viewType == ERROR_TYPE || viewType == EMPTY_TYPE ||
                            viewType == LOADING_TYPE || viewType == LOAD_MORE_TYPE ) ? gridLayoutManager.getSpanCount() : 1;
                }
            });
        }
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        // 处理StaggeredGridLayoutManager（瀑布流） 显示这个Span
        int position = holder.getAdapterPosition();
        int viewType = getItemViewType(position);
        if (isStaggeredGridLayout(holder)) {
            if (viewType == ERROR_TYPE || viewType == EMPTY_TYPE || viewType == LOADING_TYPE
                    || viewType == LOAD_MORE_TYPE) {

                StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
                //设置显示整个span
                params.setFullSpan(true);
            }
        }
    }

    /**
     * 判断是否是瀑布流布局
     */
    private boolean isStaggeredGridLayout(RecyclerView.ViewHolder holder) {
        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        if (layoutParams != null && layoutParams instanceof StaggeredGridLayoutManager.LayoutParams) {
            return true;
        }
        return false;
    }

    @Override
    public void clear() {
        mIsShowError = false;
        mIsShowLoading = false;
        mIsShowEmpty = false;
        mIsShowLoadMore = false;
        super.clear();
    }

    public boolean isShowEmpty() {
        return mIsShowEmpty;
    }

    public boolean isShowLoading() {
        return mIsShowLoading;
    }

    public boolean isShowError() {
        return mIsShowError;
    }

    public boolean isShowLoadMore(){
        return mIsShowLoadMore;
    }

    /***************loading***************/

    /**
     * 显示LoadingView，请求数据时调用数据请求完毕调用{@link #hideLoading()}
     */
    public void showLoading(){
        clear();
        mIsShowLoading = true;
        add(mLoadingCell);
    }

    /**
     * 列表Loading 状态显示的View，默认全屏高度
     */
    public void showLoading(View loadingView){
        showLoading(loadingView,0);
    }

    /**
     * 指定列表Loading 状态显示的View，并指定View显示高度
     */
    public void showLoading(View loadingView,int height){
        if (null == loadingView){
            showLoading();
            return;
        }
        clear();
        mIsShowLoading = true;
        mLoadingCell.setView(loadingView);
        mLoadingCell.setHeight(height);
        add(mLoadingCell);
    }

    /**
     * 列表显示LoadingView并保留keepCount个Item
     *
     * {@link #showLoading(View, int)}
     * @param keepCount 保留的条目数量
     */
    public void showLoadingKeepCount(int keepCount) {
        showLoadingKeepCount(keepCount, 0);
    }

    /**
     * 列表Loading状态显示的View，保留keepCountg个Item，并指定高度
     *
     * {@link #showLoadingKeepCount(int, int, View)}
     * @param keepCount 保留item的个数
     * @param height    View显示的高度
     */
    public void showLoadingKeepCount(int keepCount, int height) {
        showLoadingKeepCount(keepCount, height, null);
    }

    /**
     * 列表Loading状态显示的View，保留keepCountg个Item，并指定高度，指定显示的View
     * @param keepCount    保留item的个数
     * @param height       View显示的高度
     * @param loadingView  显示的View
     */
    public void showLoadingKeepCount(int keepCount,int height,View loadingView){
        if (keepCount < 0 || keepCount > mData.size()) {
            return;
        }
        remove(keepCount, mData.size() - keepCount);
        checkNotContainSpecailCell();
        mIsShowLoading = true;
        if (loadingView != null) {
            mLoadingCell.setView(loadingView);
        }
        mLoadingCell.setHeight(height);
        add(mLoadingCell);
    }

    /**
     * 隐藏LoadingView
     */
    public void hideLoading() {
        if (mData.contains(mLoadingCell)) {
            mData.remove(mLoadingCell);
            mIsShowLoading = false;
        }
    }

    /***************empty***************/

    /**
     * 显示空view
     * 当页面没有数据的时候，调用{@link #showEmpty()}显示空View，给用户提示
     */
    public void showEmpty() {
        clear();
        mIsShowEmpty = true;
        add(mEmptyCell);
    }

    /**
     * 显示指定的空状态View,默认显示屏幕高度
     *
     * {@link #showEmpty(View, int)}
     */
    public void showEmpty(View emptyView) {
        showEmpty(emptyView, 0);
    }

    /**
     * 显示指定的空状态View，并指定View显示的高度
     *
     * @param emptyView  页面为空状态显示的View
     * @param viewHeight view显示的高
     */
    public void showEmpty(View emptyView, int viewHeight) {
        if (emptyView == null) {
            showEmpty();
            return;
        }
        clear();
        mIsShowEmpty = true;
        mEmptyCell.setView(emptyView);
        mEmptyCell.setHeight(viewHeight);
        add(mEmptyCell);
    }

    /**
     * 显示空状态View，并保留keepCount个Item
     *
     * {@link #showEmpty(View, int)}
     */
    public void showEmptyKeepCount(int keepCount) {
        showEmptyKeepCount(keepCount, 0);
    }

    /**
     * 显示空状态View，保留keepCount个Item,并指定View显示的高
     *
     * @param keepCount 保留的Item个数
     * @param height    View 显示的高度
     * {@link #showEmptyKeepCount(int, int, View)}
     */
    public void showEmptyKeepCount(int keepCount, int height) {
        showEmptyKeepCount(keepCount, height, null);
    }

    /**
     * 显示空状态View，保留keepCount个Item,并指定View和View显示的高
     *
     * @param keepCount 保留的Item个数
     * @param height    显示的View的高
     * @param view      显示的View，null 则显示默认View
     */
    public void showEmptyKeepCount(int keepCount, int height, View view) {
        if (keepCount < 0 || keepCount > mData.size()) {
            return;
        }
        remove(keepCount, mData.size() - keepCount);
        checkNotContainSpecailCell();
        mIsShowEmpty = true;
        if (view != null) {
            mEmptyCell.setView(view);
        }
        mEmptyCell.setHeight(height);
        add(mEmptyCell);
    }

    /**
     * 隐藏空View
     */
    public void hideEmpty() {
        if (mData.contains(mEmptyCell)) {
            remove(mEmptyCell);
            mIsShowEmpty = false;
        }
    }

    /***************error***************/

    /**
     * 显示错误提示View
     * 当网络请求发生错误，需要在界面给出错误提示时
     *
     * @see #showErrorKeepCount(int)
     */
    public void showError() {
        clear();
        mIsShowError = true;
        add(mErrorCell);
    }

    /**
     * 指定列表发生错误时显示的View，默认为全屏高度
     *
     * @see {@link #showError(View, int)}
     */
    public void showError(View errorView) {
        showError(errorView, 0);
    }

    /**
     * 指定列表发生错误时显示的View，并指定View高度
     *
     * @param errorView    指定的view
     * @param viewHeight   view显示的高度
     */
    public void showError(View errorView, int viewHeight) {
        if (errorView == null) {
            showError();
            return;
        }
        clear();
        mIsShowError = true;
        mErrorCell.setHeight(viewHeight);
        mErrorCell.setView(errorView);
        add(mErrorCell);
    }

    /**
     * 显示错误提示View
     * 当网络请求发生错误，需要在界面给出错误提示时，并保留keepCount 条Item
     *
     * @param keepCount 保留Item数量
     */
    public void showErrorKeepCount(int keepCount) {
        showErrorKeepCount(keepCount, 0);
    }

    /**
     * 显示错误提示View，并指定保留的item数和View显示的高
     *
     * @param keepCount 保留的item数
     * @param height    view显示的高
     */
    public void showErrorKeepCount(int keepCount, int height) {
        showErrorKeepCount(keepCount, height, null);
    }

    /**
     * 显示错误提示View，并指定保留的item数和View显示的高
     *
     * @param keepCount 保留的item数
     * @param height    view显示的高
     * @param errorView 指定显示的View，null 则显示默认View
     */
    public void showErrorKeepCount(int keepCount, int height, View errorView) {
        if (keepCount < 0 || keepCount > mData.size()) {
            return;
        }
        remove(keepCount, mData.size() - keepCount);
        checkNotContainSpecailCell();
        mIsShowError = true;
        if (errorView != null) {
            mErrorCell.setView(errorView);
        }
        mErrorCell.setHeight(height);
        add(mErrorCell);
    }

    /**
     * 隐藏错误提示
     */
    public void hideErorr() {
        if (mData.contains(mErrorCell)) {
            remove(mErrorCell);
            mIsShowError = false;
        }
    }


    /***************loadMore***************/

    /**
     * 显示LoadMoreView
     * 当列表滑动到底部时，调用提示加载更多，加载完数据。
     */
    public void showLoadMore() {
        if (mData.contains(mLoadMoreCell)) {
            return;
        }
        checkNotContainSpecailCell();
        add(mLoadMoreCell);
        mIsShowLoadMore = true;
    }

    /**
     * 指定显示的LoadMore View,高度为默认高度
     * {@link #showLoadMore(View, int)}
     */
    public void showLoadMore(View loadMoreView) {
        showLoadMore(loadMoreView, 0);
    }

    /**
     * 指定显示的LoadMoreView,并指定显示的高度
     *
     * @param loadMoreView  指定的view
     * @param height        view显示的高度
     */
    public void showLoadMore(View loadMoreView, int height) {
        if (loadMoreView == null) {
            return;
        }
        checkNotContainSpecailCell();
        //设置默认高度
        if (height == 0) {
            int defaultHeight = LoadMoreCell.mDefaultHeight;
            mLoadMoreCell.setHeight(defaultHeight);
        } else {
            mLoadMoreCell.setHeight(height);
        }
        mLoadMoreCell.setView(loadMoreView);
        mIsShowLoadMore = true;
        add(mLoadMoreCell);
    }

    /**
     * 隐藏LoadMoreView
     */
    public void hideLoadMore() {
        if (mData.contains(mLoadMoreCell)) {
            remove(mLoadMoreCell);
            mIsShowLoadMore = false;
        }
    }

    /**
     * 检查列表是否已经包含了这4种Cell
     */
    private void checkNotContainSpecailCell() {
        if (mData.contains(mEmptyCell)) {
            mData.remove(mEmptyCell);
        }
        if (mData.contains(mErrorCell)) {
            mData.remove(mErrorCell);
        }
        if (mData.contains(mLoadingCell)) {
            mData.remove(mLoadingCell);
        }
        if (mData.contains(mLoadMoreCell)) {
            mData.remove(mLoadMoreCell);
        }
    }
}
