package com.shanlinjinrong.oa.ui.activity.home.schedule.manage.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.ui.activity.home.schedule.manage.bean.LeftDateBean;

import java.util.List;

public class LookContentAdapter extends BaseQuickAdapter<LeftDateBean.DataBean> {

    private Context mContext;
    public LookContentAdapter(List<LeftDateBean.DataBean> data, Context context) {
        super(R.layout.item_look_calendar_content, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, LeftDateBean.DataBean dataBean) {
        if (dataBean.getStatus() == 1){
            baseViewHolder.setBackgroundColor(R.id.tv_content, mContext.getResources().getColor(R.color.F5F5F5));
            baseViewHolder.setText(R.id.tv_content, dataBean.getTaskTheme());
        }else {
            baseViewHolder.setBackgroundColor(R.id.tv_content, mContext.getResources().getColor(R.color.blue_69B0F2));
            baseViewHolder.setText(R.id.tv_content, dataBean.getTaskTheme());
        }
    }
}
