package com.shanlinjinrong.views.baserecycler.cell;

/**
 * Cell基类
 * Created by k.huang on 2017/2/20.
 */

public abstract class RVBasicCell<T> implements Cell {
    public T mData; //需要绑定的数据

    public RVBasicCell(T t){
        mData = t;
    }

    @Override
    public void releaseResource() {
        // do nothing
        // 子类去重写
    }
}
