package com.shanlinjinrong.views.listview;

/**
 * Created by 丁 on 2017/8/30.
 */

public interface ScrollResult {
    void loadMore(int curPage, int limitCount);

    void loadFinish();//数据加载完成，没有数据可以加载
}
