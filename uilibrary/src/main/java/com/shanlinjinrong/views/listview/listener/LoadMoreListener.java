package com.shanlinjinrong.views.listview.listener;

/**
 * Created by 丁 on 2017/8/30.
 */

public interface LoadMoreListener {
    void loadMore(int loadPage, int limitCount);

    void noMoreData();//数据加载完成，没有数据可以加载
}
