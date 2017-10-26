package com.shanlinjinrong.oa.ui.activity.home.schedule.initiateapproval;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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
 * 加班申请
 */
public class OverTimeWorkingActivity extends AppCompatActivity {

    @Bind(R.id.top_view)
    CommonTopView mTopView;
    @Bind(R.id.rv_commonality_show)
    RecyclerView mRvCommonalityShow;
    @Bind(R.id.btn_add_details)
    TextView mBtnAddDetails;
    @Bind(R.id.sv_container_show)
    ScrollView mSvContainerShow;
    @Bind(R.id.tv_commonality_detail)
    TextView mTvCommonalityDetail;
    @Bind(R.id.tv_commonality_coder)
    TextView mTvCommonalityCoder;
    @Bind(R.id.tv_commonality_type)
    TextView mTvCommonalityType;
    @Bind(R.id.tv_commonality_over_time)
    TextView mTvCommonalityOverTime;
    @Bind(R.id.ll_commonality_selected)
    LinearLayout mLlCommonalitySelected;

    private CommonalityInitiateAdapter mAdapter;
    private List<String> mData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commonality_initiate_approval);
        ButterKnife.bind(this);
        initData();
        initView();
    }

    private void initData() {
        for (int i = 0; i < 1; i++) {
            mData.add("1");
        }
    }


    private void initView() {
        mTopView.setAppTitle("加班申请");
        //TODO 提交
        mTopView.getRightView().setOnClickListener(view -> {
        });

        mTvCommonalityType.setText("加班类别");
        mBtnAddDetails.setText("+ 添加加班明细");
        mTvCommonalityDetail.setText("加班明细");
        mTvCommonalityCoder.setText("加班单编码");
        mLlCommonalitySelected.setVisibility(View.GONE);
        mTvCommonalityOverTime.setVisibility(View.VISIBLE);

        mAdapter = new CommonalityInitiateAdapter(mData);
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

    @OnClick({R.id.btn_add_details})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_add_details:
                mData.add("1");
                mAdapter.setNewData(mData);
                mAdapter.notifyDataSetChanged();
                break;
        }
    }
}
