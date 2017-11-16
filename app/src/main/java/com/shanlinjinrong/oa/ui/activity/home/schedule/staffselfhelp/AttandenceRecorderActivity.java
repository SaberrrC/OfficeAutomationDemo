package com.shanlinjinrong.oa.ui.activity.home.schedule.staffselfhelp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.retrofit.model.responsebody.CommonAttendanceBean;
import com.example.retrofit.model.responsebody.MyAttandanceResponse;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.manager.AppConfig;
import com.shanlinjinrong.oa.manager.AppManager;
import com.shanlinjinrong.oa.ui.activity.home.schedule.initiateapproval.InitiateThingsRequestActivity;
import com.shanlinjinrong.oa.ui.activity.home.schedule.initiateapproval.widget.ApproveDecorationLine;
import com.shanlinjinrong.oa.ui.activity.home.schedule.staffselfhelp.adapter.HolidayAdapter;
import com.shanlinjinrong.oa.ui.activity.home.schedule.staffselfhelp.contract.AttandenceRecorderContract;
import com.shanlinjinrong.oa.ui.activity.home.schedule.staffselfhelp.presenter.AttandenceRecorderPresenter;
import com.shanlinjinrong.oa.ui.base.BaseActivity;
import com.shanlinjinrong.oa.ui.base.HttpBaseActivity;
import com.shanlinjinrong.oa.utils.CustomDialogUtils;
import com.shanlinjinrong.views.common.CommonTopView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

//考勤月历
public class AttandenceRecorderActivity extends HttpBaseActivity<AttandenceRecorderPresenter> implements AttandenceRecorderContract.View {

    @BindView(R.id.top_view)
    CommonTopView mTopView;
    @BindView(R.id.tv_calendar_year)
    TextView mTvCalendarYear;
    @BindView(R.id.recycler_view1)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_empty_attendence)
    TextView mTvEmptyAttendence;

    private AttandenceRecorderAdapter mAdapter;
    private List<MyAttandanceResponse.AllWorkAttendanceListBean> mAllWorkAttendanceList;
    private List<MyAttandanceResponse.AllWorkAttendanceListBean> mData = new ArrayList<>();
    private HashMap<String, CommonAttendanceBean> mHashMap;
    private CustomDialogUtils mDialog;
    private MyAttandanceResponse mAttandance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attandance_recorder_list);
        ButterKnife.bind(this);
        mAttandance = (MyAttandanceResponse) getIntent().getSerializableExtra("attandance");
        initData(mAttandance);
        initView();
    }


    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }


    private void initData(MyAttandanceResponse response) {
        if (mAllWorkAttendanceList != null) {
            mAllWorkAttendanceList.clear();
        }
        try {

            if (response != null) {
                mAllWorkAttendanceList = response.getAllWorkAttendanceList();
                if (mAllWorkAttendanceList != null) {
                    mHashMap = new HashMap<>();
                    for (int i = 0; i < mAllWorkAttendanceList.size(); i++) {
                        CommonAttendanceBean bean = new CommonAttendanceBean();
                        bean.setCalendar(mAllWorkAttendanceList.get(i).getCalendar());
                        bean.setFlg(mAllWorkAttendanceList.get(i).getFlg());
                        bean.setImportsignflag(mAllWorkAttendanceList.get(i).getImportsignflag());
                        bean.setOnebegintime(mAllWorkAttendanceList.get(i).getOnebegintime());
                        bean.setPk_psndoc(mAllWorkAttendanceList.get(i).getPk_psndoc());
                        bean.setPsname(mAllWorkAttendanceList.get(i).getPsname());
                        bean.setSignCause(mAllWorkAttendanceList.get(i).getSignCause());
                        bean.setTbmstatus(mAllWorkAttendanceList.get(i).getTbmstatus());
                        bean.setOnebegintime(mAllWorkAttendanceList.get(i).getOnebegintime());
                        mHashMap.put(mAllWorkAttendanceList.get(i).getCalendar(), bean);
                    }

                    List<MyAttandanceResponse.CdWorkAttendanceListBean> cdWorkAttendanceList = response.getCdWorkAttendanceList();
                    if (cdWorkAttendanceList != null) {
                        for (int i = 0; i < cdWorkAttendanceList.size(); i++) {
                            CommonAttendanceBean bean = new CommonAttendanceBean();
                            bean.setCalendar(cdWorkAttendanceList.get(i).getCalendar());
                            bean.setFlg(cdWorkAttendanceList.get(i).getFlg());
                            bean.setImportsignflag(cdWorkAttendanceList.get(i).getImportsignflag());
                            bean.setOnebegintime(cdWorkAttendanceList.get(i).getOnebegintime());
                            bean.setPk_psndoc(cdWorkAttendanceList.get(i).getPk_psndoc());
                            bean.setPsname(cdWorkAttendanceList.get(i).getPsname());
                            bean.setSignCause(cdWorkAttendanceList.get(i).getSignCause());
                            bean.setTbmstatus(cdWorkAttendanceList.get(i).getTbmstatus());
                            bean.setOnebegintime(cdWorkAttendanceList.get(i).getOnebegintime());
                            mHashMap.put(cdWorkAttendanceList.get(i).getCalendar(), bean);
                        }
                    }

                    List<MyAttandanceResponse.KgWorkAttendanceListBean> kgWorkAttendanceList = response.getKgWorkAttendanceList();
                    if (kgWorkAttendanceList != null) {
                        for (int i = 0; i < kgWorkAttendanceList.size(); i++) {
                            CommonAttendanceBean bean = new CommonAttendanceBean();
                            bean.setCalendar(kgWorkAttendanceList.get(i).getCalendar());
                            bean.setFlg(kgWorkAttendanceList.get(i).getFlg());
                            bean.setImportsignflag(kgWorkAttendanceList.get(i).getImportsignflag());
                            bean.setOnebegintime(kgWorkAttendanceList.get(i).getOnebegintime());
                            bean.setPk_psndoc(kgWorkAttendanceList.get(i).getPk_psndoc());
                            bean.setPsname(kgWorkAttendanceList.get(i).getPsname());
                            bean.setSignCause(kgWorkAttendanceList.get(i).getSignCause());
                            bean.setTbmstatus(kgWorkAttendanceList.get(i).getTbmstatus());
                            bean.setOnebegintime(kgWorkAttendanceList.get(i).getOnebegintime());
                            mHashMap.put(kgWorkAttendanceList.get(i).getCalendar(), bean);
                        }
                    }

                    List<MyAttandanceResponse.ZtWorkAttendanceListBean> ztWorkAttendanceList = response.getZtWorkAttendanceList();
                    if (kgWorkAttendanceList != null) {
                        for (int i = 0; i < ztWorkAttendanceList.size(); i++) {
                            CommonAttendanceBean bean = new CommonAttendanceBean();
                            bean.setCalendar(ztWorkAttendanceList.get(i).getCalendar());
                            bean.setFlg(ztWorkAttendanceList.get(i).getFlg());
                            bean.setImportsignflag(ztWorkAttendanceList.get(i).getImportsignflag());
                            bean.setOnebegintime(ztWorkAttendanceList.get(i).getOnebegintime());
                            bean.setPk_psndoc(ztWorkAttendanceList.get(i).getPk_psndoc());
                            bean.setPsname(ztWorkAttendanceList.get(i).getPsname());
                            bean.setSignCause(ztWorkAttendanceList.get(i).getSignCause());
                            bean.setTbmstatus(ztWorkAttendanceList.get(i).getTbmstatus());
                            bean.setOnebegintime(ztWorkAttendanceList.get(i).getOnebegintime());
                            mHashMap.put(ztWorkAttendanceList.get(i).getCalendar(), bean);
                        }
                    }

                    List<MyAttandanceResponse.JbWorkAttendanceListBean> jbWorkAttendanceList = response.getJbWorkAttendanceList();
                    if (kgWorkAttendanceList != null) {
                        for (int i = 0; i < jbWorkAttendanceList.size(); i++) {
                            CommonAttendanceBean bean = new CommonAttendanceBean();
                            bean.setCalendar(jbWorkAttendanceList.get(i).getCalendar());
                            bean.setFlg(jbWorkAttendanceList.get(i).getFlg());
                            bean.setImportsignflag(jbWorkAttendanceList.get(i).getImportsignflag());
                            bean.setOnebegintime(jbWorkAttendanceList.get(i).getOnebegintime());
                            bean.setPk_psndoc(jbWorkAttendanceList.get(i).getPk_psndoc());
                            bean.setPsname(jbWorkAttendanceList.get(i).getPsname());
                            bean.setSignCause(jbWorkAttendanceList.get(i).getSignCause());
                            bean.setTbmstatus(jbWorkAttendanceList.get(i).getTbmstatus());
                            bean.setOnebegintime(jbWorkAttendanceList.get(i).getOnebegintime());
                            mHashMap.put(jbWorkAttendanceList.get(i).getCalendar(), bean);
                        }
                    }

                    List<MyAttandanceResponse.XjWorkAttendanceListBean> xjWorkAttendanceList = response.getXjWorkAttendanceList();
                    if (kgWorkAttendanceList != null) {
                        for (int i = 0; i < xjWorkAttendanceList.size(); i++) {
                            CommonAttendanceBean bean = new CommonAttendanceBean();
                            bean.setCalendar(xjWorkAttendanceList.get(i).getCalendar());
                            bean.setFlg(xjWorkAttendanceList.get(i).getFlg());
                            bean.setImportsignflag(xjWorkAttendanceList.get(i).getImportsignflag());
                            bean.setOnebegintime(xjWorkAttendanceList.get(i).getOnebegintime());
                            bean.setPk_psndoc(xjWorkAttendanceList.get(i).getPk_psndoc());
                            bean.setPsname(xjWorkAttendanceList.get(i).getPsname());
                            bean.setSignCause(xjWorkAttendanceList.get(i).getSignCause());
                            bean.setTbmstatus(xjWorkAttendanceList.get(i).getTbmstatus());
                            bean.setOnebegintime(xjWorkAttendanceList.get(i).getOnebegintime());
                            mHashMap.put(xjWorkAttendanceList.get(i).getCalendar(), bean);
                        }
                    }
                }

                for (int i = 0; i < mAllWorkAttendanceList.size(); i++) {
                    try {
                        if (mAllWorkAttendanceList.get(i).getTbmstatus().equals("[出差]"))
                            continue;
                        mData.add(mAllWorkAttendanceList.get(i));
                    } catch (Throwable e) {
                        e.printStackTrace();
                        try {
                            mData.add(mAllWorkAttendanceList.get(i));
                        } catch (Throwable e1) {
                            e1.printStackTrace();
                        }
                    }
                }
                if (mAllWorkAttendanceList.size() > 0) {
                    mTvEmptyAttendence.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }


    private void initView() {
        mTvCalendarYear.setText("  " + getIntent().getStringExtra("date"));
        View leftView = mTopView.getLeftView();
        leftView.setOnClickListener(view -> {
            finish();
        });
        if (mAllWorkAttendanceList != null) {
            mAdapter = new AttandenceRecorderAdapter(mData);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }
    }

    private String mCalendar;

    @Override
    public void uidNull(int code) {
        catchWarningByCode(code);
    }

    @Override
    public void showLoading() {
        showLoadingView();
    }

    @Override
    public void sendDataSuccess(MyAttandanceResponse myAttandanceResponse) {
        try {
            if (myAttandanceResponse != null) {
                mAttandance = myAttandanceResponse;
                initData(mAttandance);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendDataFailed(int errCode, String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void sendDataFinish() {
        hideLoadingView();
    }

    private class AttandenceRecorderAdapter extends BaseQuickAdapter<MyAttandanceResponse.AllWorkAttendanceListBean> {


        public AttandenceRecorderAdapter(List<MyAttandanceResponse.AllWorkAttendanceListBean> data) {
            super(R.layout.work_month_list_item, data);
        }

        @Override
        protected void convert(BaseViewHolder baseViewHolder, MyAttandanceResponse.AllWorkAttendanceListBean bean) {
            baseViewHolder.setVisible(R.id.ll_currentday_state, true);
            baseViewHolder.setVisible(R.id.tv_empty_layout, false);
            CommonAttendanceBean bean1 = mHashMap.get(bean.getCalendar());
            if (bean1 != null) {
                if (bean1.getSignCause() != null) {
                    if (!TextUtils.isEmpty(bean1.getSignCause().trim())) {
                        baseViewHolder.setVisible(R.id.tv_sign_in, true);
                    } else {
                        baseViewHolder.setVisible(R.id.tv_sign_in, false);
                    }
                }
                baseViewHolder.setText(R.id.tv_name, bean1.getPsname());
                baseViewHolder.setText(R.id.tv_date, bean1.getCalendar());
                baseViewHolder.setText(R.id.tv_gowork_time, bean1.getOnebegintime());
                baseViewHolder.setText(R.id.tv_off_gowork_time, bean1.getTwoendtime());
                if (bean1.getTbmstatus() != null) {
                    if (bean1.getTbmstatus().equals("[迟到]")) {
                        baseViewHolder.setTextColor(R.id.tv_gowork_time, Color.RED);
                    }
                    if (bean1.getTbmstatus().equals("[早退]")) {
                        baseViewHolder.setTextColor(R.id.tv_off_gowork_time, Color.RED);
                    }
                }
            }
            baseViewHolder.setOnClickListener(R.id.ll_currentday_state, view -> {
                try {

                    mCalendar = bean.getCalendar();
                    if (bean1.getSignCause() == null) {
                        try {
                            selectedDate(bean1);
                        } catch (Throwable e) {
                            e.printStackTrace();
                            NonTokenDialog(bean1.getCalendar());
                        }
                    } else if (TextUtils.isEmpty(bean1.getSignCause())) {
                        try {
                            selectedDate(bean1);
                        } catch (Throwable e) {
                            e.printStackTrace();
                            NonTokenDialog(bean1.getCalendar());
                        }
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            });
            try {
                if (bean1.getTbmstatus() != null) {
                    if (bean1.getTbmstatus().contains("[出差]")) {
                        baseViewHolder.setText(R.id.tv_state, "[出差]");
                        return;
                    }
                    if (bean1.getTbmstatus().contains("[外出]")) {
                        baseViewHolder.setText(R.id.tv_state, "[出差]");
                        return;
                    }
                    if (bean1.getTbmstatus().contains("[加班]")) {
                        baseViewHolder.setText(R.id.tv_state, "[加班]");
                        return;
                    }
                    if (bean1.getTbmstatus().contains("[转调休加班]")) {
                        baseViewHolder.setText(R.id.tv_state, "[加班]");
                        return;
                    }
                    if (bean1.getTbmstatus().contains("[加班费加班]")) {
                        baseViewHolder.setText(R.id.tv_state, "[加班]");
                        return;
                    }
                    if (bean1.getTbmstatus().contains("[休假]")) {
                        baseViewHolder.setText(R.id.tv_state, "[休假]");
                        return;
                    }
                    if (bean1.getTbmstatus().contains("[事假]")) {
                        baseViewHolder.setText(R.id.tv_state, "[休假]");
                        return;
                    }
                    if (bean1.getTbmstatus().contains("[婚假]")) {
                        baseViewHolder.setText(R.id.tv_state, "[休假]");
                        return;
                    }
                    if (bean1.getTbmstatus().contains("[年假]")) {
                        baseViewHolder.setText(R.id.tv_state, "[休假]");
                        return;
                    }
                    if (bean1.getTbmstatus().contains("[丧假]")) {
                        baseViewHolder.setText(R.id.tv_state, "[休假]");
                        return;
                    }
                    if (bean1.getTbmstatus().contains("[工伤]")) {
                        baseViewHolder.setText(R.id.tv_state, "[休假]");
                        return;
                    }
                    if (bean1.getTbmstatus().contains("[加班转调休]")) {
                        baseViewHolder.setText(R.id.tv_state, "[休假]");
                        return;
                    }
                    if (bean1.getTbmstatus().contains("[哺乳假]")) {
                        baseViewHolder.setText(R.id.tv_state, "[休假]");
                        return;
                    }
                    if (bean1.getTbmstatus().contains("[带薪病假]")) {
                        baseViewHolder.setText(R.id.tv_state, "[休假]");
                        return;
                    }
                    if (bean1.getTbmstatus().contains("[不带薪病假]")) {
                        baseViewHolder.setText(R.id.tv_state, "[休假]");
                        return;
                    }
                    if (bean1.getTbmstatus().contains("[带薪产假]")) {
                        baseViewHolder.setText(R.id.tv_state, "[休假]");
                        return;
                    }
                    if (bean1.getTbmstatus().contains("[不带薪产假]")) {
                        baseViewHolder.setText(R.id.tv_state, "[休假]");
                        return;
                    }
                    if (bean1.getTbmstatus().contains("[陪产假]")) {
                        baseViewHolder.setText(R.id.tv_state, "[休假]");
                        return;
                    }
                    if (bean1.getTbmstatus().contains("[产检假]")) {
                        baseViewHolder.setText(R.id.tv_state, "[休假]");
                        return;
                    }
                    if (bean1.getTbmstatus().contains("[特殊哺乳假]")) {
                        baseViewHolder.setText(R.id.tv_state, "[休假]");
                        return;
                    }
                    if (bean1.getTbmstatus().contains("[特殊产前假]")) {
                        baseViewHolder.setText(R.id.tv_state, "[休假]");
                        return;
                    }
                    if (bean1.getTbmstatus().contains("[签卡]")) {
                        baseViewHolder.setText(R.id.tv_state, "[签卡]");
                        return;
                    }
                    if (bean1.getTbmstatus().contains("[忘记打卡]")) {
                        baseViewHolder.setText(R.id.tv_state, "[忘记打卡]");
                        return;
                    }
                    if (bean1.getTbmstatus().contains("[考勤机故障]")) {
                        baseViewHolder.setText(R.id.tv_state, "[考勤机故障]");
                        return;
                    }
                    if (bean1.getTbmstatus().contains("[地铁故障]")) {
                        baseViewHolder.setText(R.id.tv_state, "[地铁故障]");
                        return;
                    }
                    if (bean1.getTbmstatus().contains("[旷工]")) {
                        baseViewHolder.setText(R.id.tv_state, "[旷工]");
                        return;
                    }
                    if (bean1.getTbmstatus().contains("[迟到]")) {
                        baseViewHolder.setText(R.id.tv_state, "[迟到]");
                        return;
                    }
                    if (bean1.getTbmstatus().contains("[早退]")) {
                        baseViewHolder.setText(R.id.tv_state, "[早退]");
                        return;
                    }
                    baseViewHolder.setText(R.id.tv_state, bean1.getTbmstatus());
                } else {
                    baseViewHolder.setText(R.id.tv_state, "");
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }

        private void selectedDate(CommonAttendanceBean bean) {
            String year = bean.getCalendar().substring(0, 4);
            String month = bean.getCalendar().substring(5, 7);
            String day = bean.getCalendar().substring(8, bean.getCalendar().length());
            String date = year + "年" + month + "月" + day + "日";
            NonTokenDialog(date);
        }
    }

    public void NonTokenDialog(String str) {
        try {
            //获取屏幕高宽
            DisplayMetrics metric = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metric);
            int windowsHeight = metric.heightPixels;
            int windowsWight = metric.widthPixels;
            View inflate = LayoutInflater.from(this).inflate(R.layout.common_custom_dialog, null);
            TextView btn_cancel = (TextView) inflate.findViewById(R.id.btn_cancel);
            TextView tv_content = (TextView) inflate.findViewById(R.id.tv_non_token_tip);
            btn_cancel.setVisibility(View.VISIBLE);
            tv_content.setText("是否对" + str + "进行签卡");
            CustomDialogUtils.Builder builder = new CustomDialogUtils.Builder(this);
            mDialog = builder.cancelTouchout(false)
                    .view(inflate)
                    .heightpx((int) (windowsHeight / 4.5))
                    .widthpx((int) (windowsWight / 1.4))
                    .style(R.style.dialog)
                    .addViewOnclick(R.id.tv_non_token_confirm, view -> {
                        Intent intent = new Intent(AttandenceRecorderActivity.this, InitiateThingsRequestActivity.class);
                        intent.putExtra("type", 3);
                        intent.putExtra("signInRecord", true);
                        startActivityForResult(intent, 101);
                        if (mDialog.isShowing()) {
                            mDialog.cancel();
                        }
                    }).addViewOnclick(R.id.btn_cancel, view -> {
                        if (mDialog.isShowing()) {
                            mDialog.cancel();
                        }
                    })
                    .build();
            if (mDialog.isShowing()) {
                mDialog.dismiss();
            }
            mDialog.setCancelable(false);
            mDialog.show();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 101) {
            mPresenter.sendData(AppConfig.getAppConfig(AppManager.mContext).getPrivateCode(), getIntent().getStringExtra("year"), getIntent().getStringExtra("month"), this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
