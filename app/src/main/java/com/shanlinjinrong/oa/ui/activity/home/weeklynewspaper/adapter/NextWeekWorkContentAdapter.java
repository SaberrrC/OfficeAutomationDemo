package com.shanlinjinrong.oa.ui.activity.home.weeklynewspaper.adapter;

import android.graphics.Color;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.ui.activity.home.weeklynewspaper.bean.WorkContentBean;

import java.util.List;

/**
 * Created by tonny on 2017/9/20.
 */

public class NextWeekWorkContentAdapter extends BaseQuickAdapter<WorkContentBean> {
    public NextWeekWorkContentAdapter(List<WorkContentBean> data) {
        super(R.layout.work_content_list_item,data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, WorkContentBean workContentBean) {
        baseViewHolder.setText(R.id.tv_work_content, workContentBean.getTitle());
        if (workContentBean.getState().equals("已填写")) {
            baseViewHolder.setTextColor(R.id.tv_work_state, Color.rgb(155,155,155));
        }else {
            baseViewHolder.setTextColor(R.id.tv_work_state, Color.rgb(0,0,0));
        }
        baseViewHolder.setText(R.id.tv_work_state, workContentBean.getState());
    }
}
