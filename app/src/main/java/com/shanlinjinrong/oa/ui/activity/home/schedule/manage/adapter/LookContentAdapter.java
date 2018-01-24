package com.shanlinjinrong.oa.ui.activity.home.schedule.manage.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.ui.activity.home.schedule.manage.bean.LeftDateBean;

import java.util.List;

/**
 * 作者：王凤旭
 * 创建时间：2018/1/22
 * 功能描述：
 */

public class LookContentAdapter extends BaseQuickAdapter<LeftDateBean.DataBean> {

    public LookContentAdapter(List<LeftDateBean.DataBean> data) {
        super(R.layout.item_look_calendar_content, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, LeftDateBean.DataBean dataBean) {
        baseViewHolder.setText(R.id.tv_content, dataBean.getTaskTheme());
    }
}
