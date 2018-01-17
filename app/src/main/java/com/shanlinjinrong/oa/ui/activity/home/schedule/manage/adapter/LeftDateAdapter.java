package com.shanlinjinrong.oa.ui.activity.home.schedule.manage.adapter;

import android.content.Context;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.model.Contacts;
import com.shanlinjinrong.oa.ui.activity.home.schedule.manage.bean.LeftDateBean;

import java.util.List;

/**
 * 作者：王凤旭
 * 创建时间：2018/1/16
 * 功能描述：
 */

public class LeftDateAdapter extends BaseMultiItemQuickAdapter<LeftDateBean> {

    private int mHeight;
    private Context mContext;

    public LeftDateAdapter(List<LeftDateBean> data, Context context, int height) {
        super(data);
        addItemType(Contacts.DEPARTMENT, R.layout.item_left_week_calender_date);
        addItemType(Contacts.EMPLOYEE, R.layout.item_week_calender_empty);
        mHeight = height;
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, LeftDateBean s) {
        if (s.getItemType() == 1) {
            FrameLayout frameLayout = baseViewHolder.getView(R.id.fl_empty);
            frameLayout.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, mHeight / 2));
        } else if (s.getItemType() == 0) {
            TextView tvDate = baseViewHolder.getView(R.id.tv_left_date);
            tvDate.setHeight(mHeight);
            tvDate.setText(s.getDate());

            if (s.getPosition()!= -1){
                tvDate.setTextColor(mContext.getResources().getColor(R.color.text_common_blue_color));
            }else {
                tvDate.setTextColor(mContext.getResources().getColor(R.color.gray_normal));
            }

        }
    }
}
