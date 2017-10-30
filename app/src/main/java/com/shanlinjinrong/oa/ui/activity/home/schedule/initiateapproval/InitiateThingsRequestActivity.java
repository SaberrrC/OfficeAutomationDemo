package com.shanlinjinrong.oa.ui.activity.home.schedule.initiateapproval;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.ui.activity.home.schedule.initiateapproval.adapter.CommonalityInitiateAdapter;
import com.shanlinjinrong.oa.ui.activity.home.schedule.initiateapproval.adapter.InitiateThingsTypeAdapter;
import com.shanlinjinrong.oa.ui.activity.home.schedule.initiateapproval.bean.Dialog_Common_bean;
import com.shanlinjinrong.oa.ui.activity.home.schedule.initiateapproval.bean.SelectedTypeBean;
import com.shanlinjinrong.oa.ui.activity.home.schedule.initiateapproval.contract.InitiateThingsRequestActivityContract;
import com.shanlinjinrong.oa.ui.activity.home.schedule.initiateapproval.presenter.InitiateThingsRequestActivityPresenter;
import com.shanlinjinrong.oa.ui.activity.home.schedule.initiateapproval.widget.ApproveDecorationLine;
import com.shanlinjinrong.oa.ui.base.HttpBaseActivity;
import com.shanlinjinrong.oa.utils.CustomDialogUtils;
import com.shanlinjinrong.views.common.CommonTopView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.IllegalFormatCodePointException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 发起申请
 */
public class InitiateThingsRequestActivity extends HttpBaseActivity<InitiateThingsRequestActivityPresenter> implements InitiateThingsRequestActivityContract.View {


    @Bind(R.id.top_view)
    CommonTopView mTopView;
    @Bind(R.id.btn_add_details)
    TextView mBtnAddDetails;
    @Bind(R.id.sv_container_show)
    ScrollView mSvContainerShow;
    @Bind(R.id.rv_commonality_show)
    RecyclerView mRvCommonalityShow;
    @Bind(R.id.tv_commonality_type)
    TextView mTvCommonalityType;
    @Bind(R.id.ll_commonality_type)
    LinearLayout mLlCommonalityType;
    @Bind(R.id.tv_commonality_coder)
    TextView mTvCommonalityCoder;
    @Bind(R.id.ll_commonality_date)
    LinearLayout mLlCommonalityDate;
    @Bind(R.id.tv_commonality_over_time)
    TextView mTvCommonalityOverTime;
    @Bind(R.id.tv_commonality_type_dot)
    TextView mTvCommonalityTypeDot;
    @Bind(R.id.ll_commonality_selected)
    LinearLayout mLlCommonalitySelected;
    @Bind(R.id.tv_commonality_request_date)
    TextView mTvCommonalityRequestDate;
    @Bind(R.id.tv_commonality_type_selected)
    TextView mTvCommonalityTypeSelected;
    @Bind(R.id.ll_commonality_annual_leave)
    LinearLayout mLlCommonalityAnnualLeave;

    private List<Dialog_Common_bean> data = new ArrayList<>(); //Dialog 数据源
    private List<String> mData = new ArrayList<>(); //明细数据源
    private CustomDialogUtils mDialog;
    private CommonalityInitiateAdapter mAdapter;
    private InitiateThingsTypeAdapter mTypeAdapter;
    private LinearLayoutManager mLinearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commonality_initiate_approval);
        ButterKnife.bind(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        initData();
        initView();
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    private void initData() {
        initMonoCode();
        mData.add(getIntent().getIntExtra("type", -1) + "");
        switch (getIntent().getIntExtra("type", -1)) {
            case 0://出差类别
                data.add(new Dialog_Common_bean("出差", true));
                data.add(new Dialog_Common_bean("外出", false));
                break;
            case 2:
                data.add(new Dialog_Common_bean("事假", true));
                data.add(new Dialog_Common_bean("婚假", false));
                data.add(new Dialog_Common_bean("年假", false));
                data.add(new Dialog_Common_bean("丧假", false));
                data.add(new Dialog_Common_bean("工伤", false));
                break;
            case 3:
                data.add(new Dialog_Common_bean("忘记打卡", true));
                data.add(new Dialog_Common_bean("考勤机故障", false));
                data.add(new Dialog_Common_bean("地铁故障", false));
                break;
        }
    }

    //获取编码
    private void initMonoCode() {
        mPresenter.getQueryMonoCode(2);
    }

    private void initView() {

        mLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };

        switch (getIntent().getIntExtra("type", -1)) {
            case 0://出差申请
                onBusinessRequest();
                break;
            case 1://加班申请
                overTimeWorkRequest();
                break;
            case 2://休假申请
                annualLeaveRequest();
                break;
            case 3://签卡申请
                registrationCardRequest();
                break;
            default:
                break;
        }

        mTopView.getRightView().setOnClickListener(view -> {
            switch (getIntent().getIntExtra("type", -1)) {
                case 0:
                    //mPresenter.initiateThingsRequest();  //TODO 提交
                    break;
                case 1:
                    //mPresenter.initiateThingsRequest();  //TODO 提交
                    break;
                case 2:
                    //mPresenter.initiateThingsRequest();  //TODO 提交
                    break;
                case 3:
                    //mPresenter.initiateThingsRequest();  //TODO 提交
                    break;
                default:
                    break;
            }
        });
    }

    //出差申请
    private void onBusinessRequest() {
        mAdapter = new CommonalityInitiateAdapter(this, mData);
        mRvCommonalityShow.setLayoutManager(mLinearLayoutManager);
        mRvCommonalityShow.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    //加班申请
    private void overTimeWorkRequest() {
        mTopView.setAppTitle("加班申请");
        mTvCommonalityType.setText("加班类别");
        mBtnAddDetails.setText("+ 添加加班明细");
        mTvCommonalityCoder.setText("加班单编码");
        mLlCommonalitySelected.setVisibility(View.GONE);
        mTvCommonalityTypeDot.setVisibility(View.GONE);
        mTvCommonalityOverTime.setVisibility(View.VISIBLE);
        mAdapter = new CommonalityInitiateAdapter(this, mData);
        mRvCommonalityShow.setLayoutManager(mLinearLayoutManager);
        mRvCommonalityShow.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    //休假申请
    private void annualLeaveRequest() {
        mTopView.setAppTitle("休假申请");
        mTvCommonalityCoder.setText("休假单编码");
        mTvCommonalityType.setText("休假类别");
        mBtnAddDetails.setText("+ 添加休假明细");
        mLlCommonalityAnnualLeave.setVisibility(View.VISIBLE);
        mAdapter = new CommonalityInitiateAdapter(this, mData);
        mLlCommonalityDate.setVisibility(View.VISIBLE);
        mRvCommonalityShow.setLayoutManager(mLinearLayoutManager);
        mRvCommonalityShow.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    //签卡申请
    private void registrationCardRequest() {
        mTopView.setAppTitle("签卡申请");
        mLlCommonalityType.setVisibility(View.GONE);
        mBtnAddDetails.setText("+ 添加签卡明细");
        mAdapter = new CommonalityInitiateAdapter(this, mData);
        mRvCommonalityShow.setLayoutManager(mLinearLayoutManager);
        mRvCommonalityShow.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    @OnClick({R.id.btn_add_details, R.id.ll_commonality_selected})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_add_details:
                mData.add(getIntent().getIntExtra("type", -1) + "");
                mAdapter.setNewData(mData);
                mAdapter.notifyDataSetChanged();
                if (mData.size() > 1) {
                    mBtnAddDetails.setVisibility(View.INVISIBLE);
                    mBtnAddDetails.setClickable(false);
                    return;
                }
                break;
            case R.id.ll_commonality_selected:
                NonTokenDialog();
                break;
        }
    }

    @Override
    public void uidNull(int code) {

    }

    @Override
    public void getQueryMonoCodeSuccess() {

    }

    @Override
    public void getQueryMonoCodeFailure(int errorCode, String str) {

    }

    @Override
    public void initiateThingsRequestSuccess() {

    }

    @Override
    public void initiateThingsRequestFailure(int errorCode, String str) {

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
            mTvCommonalityTypeSelected.setText(bean.getSelectedType());
            for (int i = 0; i < data.size(); i++) {
                //TODO 数据团传递
                if (bean.getPosition() == 3) {

                }
                if (i == bean.getPosition()) {
                    data.get(i).setSelected(true);
                } else {
                    data.get(i).setSelected(false);
                }
            }
            mTypeAdapter.setNewData(data);
            if (mDialog != null) {
                mDialog.cancel();
            }
        }else if (bean.getEvent().equals("showDialog")){
            NonTokenDialog();
        }
    }

    private void NonTokenDialog() {
        try {
            //获取屏幕高宽
            DisplayMetrics metric = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metric);
            int windowsHeight = metric.heightPixels;
            int windowsWight = metric.widthPixels;
            View view = initTypeData();

            CustomDialogUtils.Builder builder = new CustomDialogUtils.Builder(this);

            switch (getIntent().getIntExtra("type", -1)) {
                case 2:
                    mDialog = builder.cancelTouchout(false)
                            .view(view)
                            .heightpx((int) (windowsHeight / 2.5))
                            .widthpx((int) (windowsWight / 1.1))
                            .style(R.style.dialog)
                            .build();
                    break;
                default:
                    mDialog = builder.cancelTouchout(false)
                            .view(view)
                            .heightpx(view.getHeight())
                            .widthpx((int) (windowsWight / 1.1))
                            .style(R.style.dialog)
                            .build();
                    break;
            }

            if (mDialog.isShowing()) {
                mDialog.dismiss();
            }
            mDialog.show();
            mDialog.setCanceledOnTouchOutside(true);
            mTypeAdapter.notifyDataSetChanged();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    //初始化Dialog数据
    private View initTypeData() {
        View inflate = LayoutInflater.from(this).inflate(R.layout.dialog_common_selected_type, null);
        RecyclerView rvSelectedType = (RecyclerView) inflate.findViewById(R.id.rv_selected_type);
        TextView tvTitle = (TextView) inflate.findViewById(R.id.tv_common_type_title);
        switch (getIntent().getIntExtra("type", -1)) {
            case 0:
                tvTitle.setText("出差类别");
                break;
            case 2:
                tvTitle.setText("休假类别");
                break;
            case 3:
                tvTitle.setText("签卡原因");
                break;
        }
        mTypeAdapter = new InitiateThingsTypeAdapter(this, data);
        rvSelectedType.setLayoutManager(new LinearLayoutManager(this));
        rvSelectedType.addItemDecoration(new ApproveDecorationLine(this, data));
        rvSelectedType.setAdapter(mTypeAdapter);
        return inflate;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
