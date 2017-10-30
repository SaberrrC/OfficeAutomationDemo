package com.shanlinjinrong.oa.ui.activity.home.schedule.initiateapproval;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iflytek.cloud.thirdparty.V;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.ui.activity.home.schedule.initiateapproval.adapter.CommonalityInitiateAdapter;
import com.shanlinjinrong.oa.ui.activity.home.schedule.initiateapproval.bean.SelectedTypeBean;
import com.shanlinjinrong.views.common.CommonTopView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 休假申请
 */
public class AnnualLeaveRequestActivity extends AppCompatActivity {

    @Bind(R.id.top_view)
    CommonTopView mTopView;
    @Bind(R.id.btn_add_details)
    TextView mBtnAddDetails;
    @Bind(R.id.tv_commonality_coder)
    TextView mTvCommonalityCoder;
    @Bind(R.id.tv_commonality_type)
    TextView mTvCommonalityType;
    @Bind(R.id.tv_commonality_request_date)
    TextView mTvCommonalityRequestDate;
    @Bind(R.id.rv_commonality_show)
    RecyclerView mRvCommonalityShow;
    @Bind(R.id.ll_commonality_date)
    LinearLayout mLlCommonalityDate;
    @Bind(R.id.ll_commonality_annual_leave)
    LinearLayout mLlCommonalityAnnualLeave;

    private List<String> mData = new ArrayList<>();
    private CommonalityInitiateAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commonality_initiate_approval);
        ButterKnife.bind(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        initData();
        initView();
    }

    private void initData() {
        for (int i = 0; i < 1; i++) {
            mData.add("2");
        }
    }

    private void initView() {
        mTopView.setAppTitle("休假申请");
        //TODO 提交
        mTopView.getRightView().setOnClickListener(view -> {

        });
        mTvCommonalityCoder.setText("休假单编码");
        mTvCommonalityType.setText("休假类别");
        mBtnAddDetails.setText("+ 添加休假明细");
        mLlCommonalityAnnualLeave.setVisibility(View.VISIBLE);
        mAdapter = new CommonalityInitiateAdapter(this,mData);
        mLlCommonalityDate.setVisibility(View.VISIBLE);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        mRvCommonalityShow.setLayoutManager(linearLayoutManager);
        mRvCommonalityShow.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }


    @OnClick(R.id.btn_add_details)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_add_details:
                if (mData.size() == 2) {
                    mBtnAddDetails.setVisibility(View.GONE);
                    return;
                }
                mData.add("2");
                mAdapter.setNewData(mData);
                mAdapter.notifyDataSetChanged();
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void removeDeatls(SelectedTypeBean bean) {
        if (bean.getEvent().equals("removeDetail")) {
            mData.remove(1);
            mAdapter.setNewData(mData);
            mAdapter.notifyDataSetChanged();
            mBtnAddDetails.setVisibility(View.VISIBLE);
            mBtnAddDetails.setClickable(true);
        } else if (bean.getEvent().equals("selectedType")) {
//            mTvCommonalityTypeSelected.setText(bean.getSelectedType());
//            if (mDialog != null) {
//                mDialog.cancel();
//            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
