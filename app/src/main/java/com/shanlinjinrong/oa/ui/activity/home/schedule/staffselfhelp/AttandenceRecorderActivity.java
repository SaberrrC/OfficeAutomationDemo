package com.shanlinjinrong.oa.ui.activity.home.schedule.staffselfhelp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.ui.base.BaseActivity;
import com.shanlinjinrong.views.common.CommonTopView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/10/27 0027.
 */

public class AttandenceRecorderActivity extends BaseActivity{
    @Bind(R.id.top_view)
    CommonTopView mTopView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attandance_recorder_list);
        ButterKnife.bind(this);
        initData();
        initView();
    }

    private void initData() {


    }

    private void initView() {
        mTopView.setRightDrawable(getResources().getDrawable(R.drawable.icon_login_user));
        View leftView = mTopView.getLeftView();
        leftView.setOnClickListener(view -> {
            finish();

        });
    }

}
