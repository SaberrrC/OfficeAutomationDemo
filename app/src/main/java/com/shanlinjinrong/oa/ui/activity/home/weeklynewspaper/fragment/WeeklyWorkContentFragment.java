package com.shanlinjinrong.oa.ui.activity.home.weeklynewspaper.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.common.Constants;
import com.shanlinjinrong.oa.manager.AppConfig;
import com.shanlinjinrong.oa.manager.AppManager;
import com.shanlinjinrong.oa.ui.activity.home.weeklynewspaper.bean.WorkStateTipNotifyChangeEvent;
import com.shanlinjinrong.oa.ui.activity.home.workreport.WriteReportFragment;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WeeklyWorkContentFragment extends Fragment {

    @Bind(R.id.work_plan)
    TextView mWorkPlan;
    @Bind(R.id.et_next_work_plan)
    EditText mEtNextWorkPlan;
    @Bind(R.id.practical_work)
    TextView mPracticalWork;
    @Bind(R.id.et_next_practical_work)
    EditText mEtNextPracticalWork;
    @Bind(R.id.work_analyzes)
    TextView mWorkAnalyzes;
    @Bind(R.id.et_next_work_analyzes)
    EditText mEtNextWorkAnalyzes;
    @Bind(R.id.work_remark)
    TextView mWorkRemark;
    @Bind(R.id.et_next_work_remark)
    EditText mEtNextWorkRemark;

    private boolean mIsWorkContent;
    private String mTopTitle;
    private SharedPreferences mSharedPreferences;
    private OnPageBthClickListener mPageBthClickListener;

    public WeeklyWorkContentFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weekly_work_content, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
    }

    public WeeklyWorkContentFragment setPageBtnClickListener(WeeklyWorkContentFragment.OnPageBthClickListener PageBtnClickListener) {
        this.mPageBthClickListener = PageBtnClickListener;
        return this;
    }


    private void initData() {
        Bundle arguments = getArguments();
        mIsWorkContent = arguments.getBoolean("isWorkContent");
        mTopTitle = arguments.getString("title");

        mSharedPreferences = getContext().getSharedPreferences(AppConfig.getAppConfig(AppManager.
                sharedInstance()).getPrivateCode() + Constants.WORK_WEEKLY_TEMP_DATA, Context.MODE_PRIVATE);

        if (mIsWorkContent) {
            mWorkPlan.setText("计划工作");
            mPracticalWork.setText("实际工作");
            mWorkAnalyzes.setVisibility(View.VISIBLE);
            mEtNextWorkAnalyzes.setVisibility(View.VISIBLE);
            String work_plan = mSharedPreferences.getString(mTopTitle + "work_plan", "");
            String practical_work = mSharedPreferences.getString(mTopTitle + "practical_work", "");
            String work_analyzes = mSharedPreferences.getString(mTopTitle + "work_analyzes", "");
            String work_remark = mSharedPreferences.getString(mTopTitle + "work_remark", "");
            mEtNextWorkPlan.setText(work_plan);
            mEtNextPracticalWork.setText(practical_work);
            mEtNextWorkAnalyzes.setText(work_analyzes);
            mEtNextWorkRemark.setText(work_remark);
            // getWorkContentIndex();
            //WorkSelectionState(mWeeklySize);
        } else {
            mWorkPlan.setText("下周工作");
            mPracticalWork.setText("责任人");
            mWorkAnalyzes.setVisibility(View.GONE);
            mEtNextWorkAnalyzes.setVisibility(View.GONE);
            //mWeeklySize = mSharedPreferences.getInt("workPlanSize", 4);
            String work_plan = mSharedPreferences.getString(mTopTitle + "next_work", "");
            String practical_work = mSharedPreferences.getString(mTopTitle + "personLiable", "");
            String work_remark = mSharedPreferences.getString(mTopTitle + "remark", "");
            mEtNextWorkPlan.setText(work_plan);
            mEtNextPracticalWork.setText(practical_work);
            mEtNextWorkRemark.setText(work_remark);
            //getWorkContentIndex();
            // WorkSelectionState(mWeeklySize);
        }
    }

    /**
     * 周报保存的参数
     */
    public void backStateNotify() {
        if (mIsWorkContent) {//本周工作
            if (!mEtNextWorkPlan.getText().toString().trim().equals("") && !mEtNextPracticalWork.getText().toString().trim().equals("") &&
                    !mEtNextWorkAnalyzes.getText().toString().trim().equals("") && !mEtNextWorkRemark.getText().toString().trim().equals("")) {
                WorkStateTipNotifyChangeEvent state = new WorkStateTipNotifyChangeEvent();
                state.setState("已填写");
                state.setPosition(mTopTitle);
                state.setWorkContent(true);
                EventBus.getDefault().post(state);
                SharedPreferences.Editor edit = getContext().getSharedPreferences(AppConfig.getAppConfig(AppManager.sharedInstance()).getPrivateCode() +
                        Constants.WORK_WEEKLY_TEMP_DATA, Context.MODE_PRIVATE).edit();
                edit.putString(mTopTitle + "work_plan", mEtNextWorkPlan.getText().toString() + "");
                edit.putString(mTopTitle + "practical_work", mEtNextPracticalWork.getText().toString() + "");
                edit.putString(mTopTitle + "work_analyzes", mEtNextWorkAnalyzes.getText().toString() + "");
                edit.putString(mTopTitle + "work_remark", mEtNextWorkRemark.getText().toString() + "");
                //工作内容填写情况 0完成 1待填写 2未填写
                edit.putString(mTopTitle + "this_work_content_state", "0");
                edit.apply();
            } else if ((!mEtNextWorkPlan.getText().toString().trim().equals("") || !mEtNextPracticalWork.getText().toString().trim().equals("") ||
                    !mEtNextWorkAnalyzes.getText().toString().trim().equals("") || !mEtNextWorkRemark.getText().toString().trim().equals(""))) {
                WorkStateTipNotifyChangeEvent state = new WorkStateTipNotifyChangeEvent();
                state.setState("待完善");
                state.setPosition(mTopTitle);
                state.setWorkContent(true);
                EventBus.getDefault().post(state);
                SharedPreferences.Editor edit = getContext().getSharedPreferences(AppConfig.getAppConfig(AppManager.sharedInstance()).getPrivateCode() +
                        Constants.WORK_WEEKLY_TEMP_DATA, Context.MODE_PRIVATE).edit();
                edit.putString(mTopTitle + "work_plan", mEtNextWorkPlan.getText().toString() + "");
                edit.putString(mTopTitle + "practical_work", mEtNextPracticalWork.getText().toString() + "");
                edit.putString(mTopTitle + "work_analyzes", mEtNextWorkAnalyzes.getText().toString() + "");
                edit.putString(mTopTitle + "work_remark", mEtNextWorkRemark.getText().toString() + "");
                //工作内容填写情况 0完成 1待填写 2未填写
                edit.putString(mTopTitle + "this_work_content_state", "1");
                edit.apply();
            } else {
                WorkStateTipNotifyChangeEvent state = new WorkStateTipNotifyChangeEvent();
                state.setState("未填写");
                state.setPosition(mTopTitle);
                state.setWorkContent(true);
                EventBus.getDefault().post(state);
                SharedPreferences.Editor edit = getContext().getSharedPreferences(AppConfig.getAppConfig(AppManager.sharedInstance()).getPrivateCode() +
                        Constants.WORK_WEEKLY_TEMP_DATA, Context.MODE_PRIVATE).edit();
                edit.putString(mTopTitle + "work_plan", "");
                edit.putString(mTopTitle + "practical_work", "");
                edit.putString(mTopTitle + "work_analyzes", "");
                edit.putString(mTopTitle + "work_remark", "");
                //工作内容填写情况 0完成 1待填写 2未填写
                edit.putString(mTopTitle + "this_work_content_state", "2");
                edit.apply();
            }
        } else {//下周计划
            if (!mEtNextWorkPlan.getText().toString().trim().equals("") && !mEtNextPracticalWork.getText().toString().trim().equals("") &&
                    !mEtNextWorkRemark.getText().toString().trim().equals("")) {
                WorkStateTipNotifyChangeEvent state = new WorkStateTipNotifyChangeEvent();
                state.setState("已填写");
                state.setPosition(mTopTitle);
                state.setWorkContent(false);
                EventBus.getDefault().post(state);
                SharedPreferences.Editor edit = getContext().getSharedPreferences(AppConfig.getAppConfig(AppManager.sharedInstance()).getPrivateCode() +
                        Constants.WORK_WEEKLY_TEMP_DATA, Context.MODE_PRIVATE).edit();
                edit.putString(mTopTitle + "next_work", mEtNextWorkPlan.getText().toString() + "");
                edit.putString(mTopTitle + "personLiable", mEtNextPracticalWork.getText().toString() + "");
                edit.putString(mTopTitle + "remark", mEtNextWorkRemark.getText().toString() + "");
                //工作内容填写情况 0完成 1待填写 2未填写
                edit.putString(mTopTitle + "this_work_plan_state", "0");
                edit.apply();
            } else if ((!mEtNextWorkPlan.getText().toString().trim().equals("") || !mEtNextPracticalWork.getText().toString().trim().equals("") ||
                    !mEtNextWorkRemark.getText().toString().trim().equals(""))) {
                WorkStateTipNotifyChangeEvent state = new WorkStateTipNotifyChangeEvent();
                state.setState("待完善");
                state.setPosition(mTopTitle);
                state.setWorkContent(false);
                EventBus.getDefault().post(state);
                SharedPreferences.Editor edit = getContext().getSharedPreferences(AppConfig.getAppConfig(AppManager.sharedInstance()).getPrivateCode() +
                        Constants.WORK_WEEKLY_TEMP_DATA, Context.MODE_PRIVATE).edit();
                edit.putString(mTopTitle + "next_work", mEtNextWorkPlan.getText().toString() + "");
                edit.putString(mTopTitle + "personLiable", mEtNextPracticalWork.getText().toString() + "");
                edit.putString(mTopTitle + "remark", mEtNextWorkRemark.getText().toString() + "");
                //工作内容填写情况 0完成 1待填写 2未填写
                edit.putString(mTopTitle + "this_work_plan_state", "1");
                edit.apply();
            } else {
                WorkStateTipNotifyChangeEvent state = new WorkStateTipNotifyChangeEvent();
                state.setState("未填写");
                state.setPosition(mTopTitle);
                state.setWorkContent(false);
                EventBus.getDefault().post(state);
                SharedPreferences.Editor edit = getContext().getSharedPreferences(AppConfig.getAppConfig(AppManager.sharedInstance()).getPrivateCode() +
                        Constants.WORK_WEEKLY_TEMP_DATA, Context.MODE_PRIVATE).edit();
                edit.putString(mTopTitle + "next_work", "");
                edit.putString(mTopTitle + "personLiable", "");
                edit.putString(mTopTitle + "remark", "");
                //工作内容填写情况 0完成 1待填写 2未填写
                edit.putString(mTopTitle + "this_work_plan_state", "2");
                edit.apply();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        backStateNotify();
        ButterKnife.unbind(this);
    }


    @OnClick({R.id.btn_write_next_work, R.id.btn_back_up_work})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_write_next_work:
                mPageBthClickListener.onNextPageClick();
                break;
            case R.id.btn_back_up_work:
                mPageBthClickListener.onLastPageClick();
                break;
        }
    }

    public interface OnPageBthClickListener {
        void onLastPageClick();

        void onNextPageClick();
    }
}
