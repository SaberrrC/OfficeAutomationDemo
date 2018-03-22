package com.shanlinjinrong.oa.utils;

import android.content.Context;


import com.shanlinjinrong.pickerview.OptionsPickerView;

import java.util.List;

public class YearDateSelected<T> {

    private OptionsPickerView beginTimeView;

    private YearTimeSelectedListener mListener;

    private Context mContext;

    private List<T> mData;

    private String mTitle;

    public YearDateSelected(YearTimeSelectedListener listener, Context context, List<T> data, String title) {
        mListener = listener;
        mContext = context;
        mData = data;
        mTitle = title;
    }

    /**
     * 时间选择器
     */
    public void showBeginTimeView() {
        beginTimeView = new OptionsPickerView.Builder(mContext,
                (options1, options2, options3, v) -> {
                }).isDialog(true).build();
        beginTimeView.setTitle(mTitle);
        beginTimeView.setPicker(mData);
        beginTimeView.setSelectOptions(mData.size() - 1);
        beginTimeView.setOnoptionsSelectListener((options1, option2, options3, v) -> {
            //当前时间
            String currentTime = (String) mData.get(options1);
            mListener.onSelected(currentTime, options1);
        });
        beginTimeView.setCancelable(true);
        beginTimeView.setCyclic(false);
        beginTimeView.show();
    }

    public void showPositionDateView(int position) {
        beginTimeView = new OptionsPickerView.Builder(mContext,
                (options1, options2, options3, v) -> {
                }).isDialog(true).build();
        beginTimeView.setTitle(mTitle);
        beginTimeView.setPicker(mData);
        beginTimeView.setSelectOptions(position);
        beginTimeView.setOnoptionsSelectListener((options1, option2, options3, v) -> {
            //当前时间
            String currentTime = (String) mData.get(options1);
            mListener.onSelected(currentTime, options1);
        });
        beginTimeView.setCancelable(true);
        beginTimeView.setCyclic(false);
        beginTimeView.show();
    }
}
