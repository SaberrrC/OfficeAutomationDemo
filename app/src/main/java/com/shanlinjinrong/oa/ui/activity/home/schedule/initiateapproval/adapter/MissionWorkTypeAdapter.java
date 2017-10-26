package com.shanlinjinrong.oa.ui.activity.home.schedule.initiateapproval.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shanlinjinrong.oa.R;

import java.util.List;


public class MissionWorkTypeAdapter extends BaseQuickAdapter<String> {


    public MissionWorkTypeAdapter(List<String> data) {
        super(R.layout.item_common_selected_type,data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, String s) {
        baseViewHolder.setText(R.id.tv_commonality_type,s);
    }
}
