package com.shanlinjinrong.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 含有多层的容器
 */
public class DataItemResult {
    private List<DataItemDetail> dataList;
    DataItemDetail detailInfo;
    private int mCode = 0; //接口的返回值
    private String message = ""; //接口返回message
    private boolean hasError;//返回信息是否有错
    public int maxCount;


    /**
     * 构造函数，初始化列表容器，详细信息容器，数据适配器容器
     */
    public DataItemResult() {
        this.dataList = new ArrayList<>();
        this.detailInfo = new DataItemDetail();
    }

    public DataItemResult setCode(int mCode) {
        this.mCode = mCode;
        return this;
    }

    public int getCode() {
        return mCode;
    }


    public String getMessage() {
        return message;
    }

    public DataItemResult setMessage(String message) {
        this.message = message;
        return this;
    }

    public boolean hasError() {
        return hasError;
    }

    public DataItemResult setHasError(boolean hasError) {
        this.hasError = hasError;
        return this;
    }

    public DataItemResult setDetailInfo(DataItemDetail detailInfo) {
        this.detailInfo = detailInfo;
        return this;
    }


    /**
     * 获取当前数据条数
     *
     * @return int
     */
    public int getDataCount() {
        return this.dataList.size();
    }


    public boolean isEmpty() {
        return dataList == null || dataList.isEmpty();
    }


    /**
     * 添加一个对象
     *
     * @return boolean
     */
    public boolean addItem(DataItemDetail item) {
        return null != item && dataListAddItem(item, -1);

    }

    /**
     * 添加一个对象
     *
     * @return boolean
     */
    public boolean addItem(int position, DataItemDetail item) {
        return null != item && dataListAddItem(item, position);
    }


    /**
     * 往当前列表指定位置添加一个item值（position小于0或者大于列表长度插入到list末端）
     *
     * @param item     DataItemDetail
     * @param position 希望插入到list中的位置，如果position值小于0或者大于等于列表长度那么直接添加到list的末尾位置
     * @return boolean
     */
    private boolean dataListAddItem(DataItemDetail item, int position) {
        if (item == null) {
            return false;
        }

        if (position < 0 || position >= this.dataList.size()) {
            return this.dataList.add(item);
        } else {
            this.dataList.add(position, item);
            return true;
        }
    }


    /**
     * 清除所有元素
     */
    public DataItemResult clear() {
        dataList.clear();
        detailInfo.clear();
        mCode = 0;
        message = "";
        return this;
    }


    /**
     * 通过索引删除一个对象
     *
     * @return DataItemDetail
     */
    public DataItemDetail removeByIndex(int index) {
        if (index < 0 || index >= dataList.size()) {
            return null;
        }
        return this.dataList.remove(index);
    }

    /**
     * 删除一个对象
     *
     * @return boolean
     */
    public boolean removeItem(DataItemDetail item) {
        return this.dataList.remove(item);

    }

    /**
     * 通过索引取得一个对象
     *
     * @return DataItemDetail
     */
    public DataItemDetail getItem(int index) {
        if (index < 0 || index >= this.dataList.size()) {
            return null;
        }

        return this.dataList.get(index);
    }
}