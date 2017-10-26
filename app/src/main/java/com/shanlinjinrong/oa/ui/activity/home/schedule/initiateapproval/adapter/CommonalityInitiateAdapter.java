package com.shanlinjinrong.oa.ui.activity.home.schedule.initiateapproval.adapter;


import android.view.View;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shanlinjinrong.oa.R;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class CommonalityInitiateAdapter extends BaseQuickAdapter<String> {


    public CommonalityInitiateAdapter(List<String> data) {
        super(R.layout.commonality_initiate_approval_item, data);

    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, String s) {
        if (baseViewHolder.getPosition() == 0) {
            baseViewHolder.setVisible(R.id.img_delete_detail, false);
        }
        switch (s) {
            case "0":
                break;
            case "1":
                baseViewHolder.setVisible(R.id.ll_commonality_show1, false);
                baseViewHolder.setVisible(R.id.ll_commonality_show3, false);
                baseViewHolder.setText(R.id.tv_commonality_show2, "加班原因");
                EditText editText1 = baseViewHolder.getView(R.id.et_commonality_show2);
                editText1.setHint("请填写加班原因");
                break;
            case "2":
                baseViewHolder.setVisible(R.id.ll_commonality_show1, false);
                baseViewHolder.setText(R.id.tv_commonality_show2, "休假事由");
                baseViewHolder.setText(R.id.tv_commonality_show3, "工作交接人");
                baseViewHolder.setText(R.id.tv_commonality_duration, "休假时长");
                EditText editText2 = baseViewHolder.getView(R.id.et_commonality_show2);
                EditText editText3 = baseViewHolder.getView(R.id.et_commonality_show3);
                editText2.setHint("请填写休假事由");
                editText3.setHint("请填写工作交接人");
                break;
            case "3":
                baseViewHolder.setVisible(R.id.et_commonality_show2, false);
                baseViewHolder.setVisible(R.id.ll_commonality_show1, false);
                baseViewHolder.setVisible(R.id.ll_commonality_end_time, false);
                baseViewHolder.setVisible(R.id.ll_commonality_duration, false);
                baseViewHolder.setVisible(R.id.ll_registration_card_detail, true);
                baseViewHolder.setText(R.id.tv_commonality_begin_time, "签卡时间");
                baseViewHolder.setText(R.id.tv_commonality_show2, "签卡原因");
                baseViewHolder.setText(R.id.tv_commonality_show3, "签卡说明");
                EditText editText4 = baseViewHolder.getView(R.id.et_commonality_show2);
                EditText editText5 = baseViewHolder.getView(R.id.et_commonality_show3);
                editText4.setHint("请选择签卡原因");
                editText5.setHint("请填写签卡说明");
                break;
        }

        baseViewHolder.setOnClickListener(R.id.img_delete_detail, view -> {
            EventBus.getDefault().post("removeDetail");
        });
    }


}
