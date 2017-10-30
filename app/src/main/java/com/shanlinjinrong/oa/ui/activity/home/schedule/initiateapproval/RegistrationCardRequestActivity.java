package com.shanlinjinrong.oa.ui.activity.home.schedule.initiateapproval;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.ui.activity.home.schedule.initiateapproval.adapter.CommonalityInitiateAdapter;
import com.shanlinjinrong.views.common.CommonTopView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 签卡申请
 */
public class RegistrationCardRequestActivity extends AppCompatActivity {


    @Bind(R.id.top_view)
    CommonTopView mTopView;
    @Bind(R.id.btn_add_details)
    TextView mBtnAddDetails;
    @Bind(R.id.rv_commonality_show)
    RecyclerView mRvCommonalityShow;
    @Bind(R.id.ll_commonality_type)
    LinearLayout mLlCommonalityType;
    @Bind(R.id.tv_commonality_detail)
    TextView mTvCommonalityDetail;

    private List<String> mData = new ArrayList<>();
    private CommonalityInitiateAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commonality_initiate_approval);
        ButterKnife.bind(this);

        initData();
        initView();
    }

    private void initData() {
        mData.add("3");
    }

    private void initView() {
        mTopView.setAppTitle("签卡申请");
        //TODO 提交
        mTopView.getRightView().setOnClickListener(view -> {

        });

        mLlCommonalityType.setVisibility(View.GONE);
        mTvCommonalityDetail.setText("签卡明细");
        mBtnAddDetails.setText("+ 添加签卡明细");
        mAdapter = new CommonalityInitiateAdapter(this,mData);
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
                mData.add("3");
                mAdapter.setNewData(mData);
                mAdapter.notifyDataSetChanged();
                break;
        }
    }
}
