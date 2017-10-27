package com.shanlinjinrong.oa.ui.activity.home.schedule.initiateapproval.adapter;

import android.content.Context;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.ui.activity.home.schedule.initiateapproval.bean.SelectedTypeBean;

import org.greenrobot.eventbus.EventBus;

import java.util.List;


public class InitiateThingsTypeAdapter extends BaseQuickAdapter<String> {

    private Context mContext;
    private List<String> mData;

    public InitiateThingsTypeAdapter(Context context, List<String> data) {
        super(R.layout.item_common_selected_type, data);
        mContext = context;
        mData = data;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, String s) {
        baseViewHolder.setText(R.id.tv_commonality_type, s);
        TextView textView = baseViewHolder.getView(R.id.tv_commonality_type);
        if (baseViewHolder.getPosition() == 0) {
            textView.setTextColor(mContext.getResources().getColor(R.color.blue_69B0F2));
        }
        baseViewHolder.setOnClickListener(R.id.tv_commonality_type, view -> {
            for (int i = 0; i < mData.size(); i++) {
                if (i == baseViewHolder.getPosition()) {
                    TextView view2 = baseViewHolder.getView(R.id.tv_commonality_type);
                    view2.setTextColor(mContext.getResources().getColor(R.color.blue_69B0F2));
                } else {
                    TextView view1 = baseViewHolder.getView(R.id.tv_commonality_type);
                    view1.setTextColor(mContext.getResources().getColor(R.color.grey));
                }
            }
            EventBus.getDefault().post(new SelectedTypeBean("selectedType", textView.getText().toString(), baseViewHolder.getPosition()));
        });
    }
}
