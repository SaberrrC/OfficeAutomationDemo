package com.shanlin.oa.ui.adapter;

import android.graphics.Color;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shanlin.oa.R;
import com.shanlin.oa.model.approval.Approval;

import java.util.List;

/**
 * ProjectName: dev-beta-v1.0.1
 * PackageName: com.itcrm.GroupInformationPlatform.ui.adapter
 * Author:Created by Tsui on Date:2016/11/16 10:39
 * Description:审批列表条目适配
 */
public class ApprovalListAdapter extends BaseQuickAdapter<Approval> {
    List<Approval> data;
    private boolean isMeLaunch = false;

    public ApprovalListAdapter(List<Approval> data, boolean isMeLaunch) {
        super(R.layout.approval_list_item, data);
        this.data = data;
        this.isMeLaunch = isMeLaunch;
    }

    @Override
    protected void convert(BaseViewHolder holder, Approval approval) {
        String status = approval.getStatus();
        View view = holder.getView(R.id.launch_approval_state);
        if (null!=status) {
            switch (Integer.parseInt(status)) {
                case 1://审批
                    view.setBackgroundColor(Color.parseColor("#F8931F"));
                    break;
                case 2://通过
                    view.setBackgroundColor(Color.parseColor("#11C48C"));
                    break;
                case 3://驳回
                    view.setBackgroundColor(Color.parseColor("#ED1B24"));
                    break;

            }
        }

        holder.setText(R.id.tv_launch_type, approval.getAppr_name())
                .setText(R.id.tv_launch_date, approval.getCreate_time())
                .setText(R.id.tv_launch_name, approval.getUsername());
    }
}
