package com.shanlinjinrong.oa.utils;

import android.content.Context;
import android.view.View;

import com.shanlinjinrong.pickerview.OptionsPickerView;

import java.util.List;

public class YearDateSelected {

    private OptionsPickerView beginTimeView;

    private YearTimeSelectedListener mListener;

    private Context mContext;

    private List<String> mData;

    private String mTitle;

    public YearDateSelected(YearTimeSelectedListener listener, Context context, List<String> data, String title) {
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
                new OptionsPickerView.OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                    }
                }).isDialog(true).build();
        beginTimeView.setTitle(mTitle);
        beginTimeView.setPicker(mData);
        beginTimeView.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //当前时间
                String currentTime = mData.get(options1);
                mListener.onSelected(currentTime);
            }
        });
        beginTimeView.setSelectOptions(mData.size());
        beginTimeView.setCyclic(false);
        beginTimeView.show();
    }
}
