package com.shanlinjinrong.oa.ui.activity.home.schedule.initiateapproval.adapter;

import android.content.Context;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.ui.activity.home.schedule.initiateapproval.bean.Dialog_Common_bean;
import com.shanlinjinrong.oa.ui.activity.home.schedule.initiateapproval.bean.SelectedTypeBean;

import org.greenrobot.eventbus.EventBus;

import java.util.List;


public class InitiateThingsTypeAdapter extends BaseQuickAdapter<Dialog_Common_bean> {

    private Context mContext;
    private List<Dialog_Common_bean> mData;

    public InitiateThingsTypeAdapter(Context context, List<Dialog_Common_bean> data) {
        super(R.layout.item_common_selected_type, data);
        mContext = context;
        mData = data;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, Dialog_Common_bean s) {
        baseViewHolder.setText(R.id.tv_commonality_type, s.getContent());
        TextView textView = baseViewHolder.getView(R.id.tv_commonality_type);
        if (s.isSelected()) {
            baseViewHolder.setTextColor(R.id.tv_commonality_type,mContext.getResources().getColor(R.color.blue_69B0F2));
        }else {
            baseViewHolder.setTextColor(R.id.tv_commonality_type,mContext.getResources().getColor(R.color.gray_d5d5d5));
        }
        baseViewHolder.setOnClickListener(R.id.tv_commonality_type, view -> {
            EventBus.getDefault().post(new SelectedTypeBean("selectedType", textView.getText().toString(), baseViewHolder.getPosition()));
        });
    }
}
