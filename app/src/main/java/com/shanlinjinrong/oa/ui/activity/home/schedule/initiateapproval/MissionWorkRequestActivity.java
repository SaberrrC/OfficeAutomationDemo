package com.shanlinjinrong.oa.ui.activity.home.schedule.initiateapproval;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.ui.activity.home.schedule.initiateapproval.adapter.CommonalityInitiateAdapter;
import com.shanlinjinrong.oa.ui.activity.home.schedule.initiateapproval.adapter.MissionWorkTypeAdapter;
import com.shanlinjinrong.oa.ui.activity.home.schedule.initiateapproval.bean.SelectedTypeBean;
import com.shanlinjinrong.oa.ui.activity.home.schedule.initiateapproval.contract.MissionWorkRequestActivityContract;
import com.shanlinjinrong.oa.ui.activity.home.schedule.initiateapproval.presenter.MissionWorkRequestActivityPresenter;
import com.shanlinjinrong.oa.ui.activity.home.schedule.initiateapproval.widget.ApproveDecorationLine;
import com.shanlinjinrong.oa.ui.base.HttpBaseActivity;
import com.shanlinjinrong.oa.utils.CustomDialogUtils;
import com.shanlinjinrong.views.common.CommonTopView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.kymjs.kjframe.http.HttpParams;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 出差申请
 */
public class MissionWorkRequestActivity extends HttpBaseActivity<MissionWorkRequestActivityPresenter> implements MissionWorkRequestActivityContract.View {

    @Bind(R.id.top_view)
    CommonTopView mTopView;
    @Bind(R.id.rv_commonality_show)
    RecyclerView mRvCommonalityShow;
    @Bind(R.id.btn_add_details)
    TextView mBtnAddDetails;
    @Bind(R.id.tv_commonality_type_selected)
    TextView mTvCommonalityTypeSelected;


    private List<String> mData = new ArrayList<>();
    private CommonalityInitiateAdapter mAdapter;
    private MissionWorkTypeAdapter mTypeAdapter;

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
        for (int i = 0; i < 1; i++) {
            mData.add("0");
        }
        initMonoCode();
    }

    private void initMonoCode() {
        mPresenter.getQueryMonoCode(2);
    }

    private void initView() {
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

    @OnClick({R.id.btn_add_details, R.id.ll_commonality_selected})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_add_details:
                mData.add("0");
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
            if (mDialog != null) {
                mDialog.cancel();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    private CustomDialogUtils mDialog;


    //TODO 存在问题 选中item 颜色没有改变
    private void NonTokenDialog() {
        try {
            //获取屏幕高宽
            DisplayMetrics metric = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metric);
            int windowsHeight = metric.heightPixels;
            int windowsWight = metric.widthPixels;
            View view = initTypeData();


            CustomDialogUtils.Builder builder = new CustomDialogUtils.Builder(this);
            mDialog = builder.cancelTouchout(false)
                    .view(view)
                    .heightpx(view.getHeight())
                    .widthpx((int) (windowsWight / 1.1))
                    .style(R.style.dialog)
                    .build();
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

    private View initTypeData() {
        View inflate = LayoutInflater.from(this).inflate(R.layout.dialog_common_selected_type, null);
        RecyclerView rvSelectedType = (RecyclerView) inflate.findViewById(R.id.rv_selected_type);
        List<String> data = new ArrayList<>();
        data.add("出差");
        data.add("外出");
        mTypeAdapter = new MissionWorkTypeAdapter(this, data);
        rvSelectedType.setLayoutManager(new LinearLayoutManager(this));
        rvSelectedType.addItemDecoration(new ApproveDecorationLine(this, data));
        rvSelectedType.setAdapter(mTypeAdapter);
        TextView tvTitle = (TextView) inflate.findViewById(R.id.tv_common_type_title);
        tvTitle.setText("出差类别");
        return inflate;
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
}
