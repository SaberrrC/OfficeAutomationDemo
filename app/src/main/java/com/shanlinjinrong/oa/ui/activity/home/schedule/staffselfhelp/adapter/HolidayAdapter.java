package com.shanlinjinrong.oa.ui.activity.home.schedule.staffselfhelp.adapter;

import android.content.Context;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.ui.activity.home.schedule.initiateapproval.bean.Dialog_Common_bean;
import com.shanlinjinrong.oa.ui.activity.home.schedule.initiateapproval.bean.SelectedTypeBean;
import com.shanlinjinrong.oa.ui.activity.home.schedule.staffselfhelp.event.HolidayEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class HolidayAdapter  extends BaseQuickAdapter<String> {
    private Context mContext;
    private List<String> mData;

    public HolidayAdapter(Context context, List<String> data) {
        super(R.layout.item_common_selected_type, data);
        mContext = context;
        mData = data;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, String s) {
        TextView textView = baseViewHolder.getView(R.id.tv_commonality_type);
        textView.setText(s);

        baseViewHolder.setOnClickListener(R.id.tv_commonality_type, view -> {
            HolidayEvent holidayEvent = new HolidayEvent();
            holidayEvent.setPosition(baseViewHolder.getPosition());
            EventBus.getDefault().post(holidayEvent);
        });
    }
}
