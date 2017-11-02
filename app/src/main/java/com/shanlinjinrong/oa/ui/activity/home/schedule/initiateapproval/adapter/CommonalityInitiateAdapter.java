package com.shanlinjinrong.oa.ui.activity.home.schedule.initiateapproval.adapter;


import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.ui.activity.home.schedule.initiateapproval.bean.CommonalityInitiateBean;
import com.shanlinjinrong.oa.ui.activity.home.schedule.initiateapproval.bean.SelectedTypeBean;
import com.shanlinjinrong.oa.ui.activity.home.schedule.initiateapproval.bean.SubmitRequestBean;
import com.shanlinjinrong.oa.utils.TimeDialogFragment;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.OnClick;

public class CommonalityInitiateAdapter extends BaseQuickAdapter<CommonalityInitiateBean> {

    private Context mContext;
    private int pos;

    private SubmitRequestBean mSubmitRequestBean;

    public CommonalityInitiateAdapter(Context context, List<CommonalityInitiateBean> data,SubmitRequestBean submitRequestBean) {
        super(R.layout.commonality_initiate_approval_item, data);
        mSubmitRequestBean = submitRequestBean;
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, CommonalityInitiateBean s) {
        switch (s.getIndex()) {
            case "0":
                TextView begin_time = baseViewHolder.getView(R.id.et_commonality_begin_time);
                TextView end_time = baseViewHolder.getView(R.id.et_commonality_end_time);

                String s1 = begin_time.getText().toString();
                String s2 = end_time.getText().toString();

                baseViewHolder.setOnClickListener(R.id.et_commonality_begin_time, view -> {
                    Toast.makeText(mContext, "开始时间", Toast.LENGTH_SHORT).show();
                });
                baseViewHolder.setOnClickListener(R.id.et_commonality_end_time, view -> {
                    Toast.makeText(mContext, "结束时间", Toast.LENGTH_SHORT).show();
                });
                break;
            case "1":
                baseViewHolder.setVisible(R.id.ll_commonality_show1, false);
                baseViewHolder.setVisible(R.id.ll_commonality_show3, false);
                baseViewHolder.setText(R.id.tv_commonality_show2, "加班原因");
                baseViewHolder.setText(R.id.tv_commonality_detail, "加班明细");
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
                baseViewHolder.setText(R.id.tv_commonality_detail, "签卡明细");
                baseViewHolder.setText(R.id.tv_selected_show, s.getSelectedTitle());
                EditText editText4 = baseViewHolder.getView(R.id.et_commonality_show2);
                EditText editText5 = baseViewHolder.getView(R.id.et_commonality_show3);

                editText4.setHint("请选择签卡原因");
                editText5.setHint("请填写签卡说明");
                break;
        }

        baseViewHolder.setOnClickListener(R.id.img_delete_detail, view -> {
            EventBus.getDefault().post(new SelectedTypeBean("removeDetail"));
        });

        baseViewHolder.setOnClickListener(R.id.ll_registration_card_detail, view -> {
            EventBus.getDefault().post(new SelectedTypeBean("showDialog", baseViewHolder.getPosition()));
        });
    }
}
