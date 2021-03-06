package com.shanlinjinrong.oa.ui.activity.home.weeklynewspaper.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
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
import com.shanlinjinrong.oa.utils.EmojiFilter;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WeeklyWorkContentFragment extends Fragment {

    @BindView(R.id.work_plan)
    TextView mWorkPlan;
    @BindView(R.id.et_next_work_plan)
    EditText mEtNextWorkPlan;
    @BindView(R.id.practical_work)
    TextView mPracticalWork;
    @BindView(R.id.et_next_practical_work)
    EditText mEtNextPracticalWork;
    @BindView(R.id.work_analyzes)
    TextView mWorkAnalyzes;
    @BindView(R.id.et_next_work_analyzes)
    EditText mEtNextWorkAnalyzes;
    @BindView(R.id.work_remark)
    TextView mWorkRemark;
    @BindView(R.id.et_next_work_remark)
    EditText mEtNextWorkRemark;
    @BindView(R.id.btn_write_next_work)
    TextView mBtnWriteNextWork;
    @BindView(R.id.btn_back_up_work)
    TextView mBtnBackUpWork;

    private int mPageIndex;
    private String mTopTitle;
    private int mPageIndexMax;
    private boolean mIsWorkContent;
    private boolean mIsEditTextEnabled;
    private SharedPreferences mSharedPreferences;
    private OnPageBthClickListener mPageBthClickListener;

    public WeeklyWorkContentFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weekly_work_content, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
    }

    public WeeklyWorkContentFragment setPageBtnClickListener(OnPageBthClickListener PageBtnClickListener) {
        this.mPageBthClickListener = PageBtnClickListener;
        return this;
    }


    private void initData() {
        initWidget();
        initWorkContent();
    }

    /**
     * 控件初始化
     */
    private void initWidget() {
        Bundle arguments = getArguments();
        mIsWorkContent = arguments.getBoolean("isWorkContent");
        mTopTitle = arguments.getString("title");
        mPageIndex = arguments.getInt("index");
        mPageIndexMax = arguments.getInt("indexMax");
        mIsEditTextEnabled = arguments.getBoolean("isEditTextEnabled",false);

        InputFilter[] filters = new InputFilter[]{new EmojiFilter(50)};
        mEtNextWorkPlan.setFilters(filters);
        mEtNextPracticalWork.setFilters(filters);
        mEtNextWorkAnalyzes.setFilters(filters);
        mEtNextWorkRemark.setFilters(filters);

        if (mIsEditTextEnabled){
            mEtNextWorkPlan.setEnabled(false);
            mEtNextPracticalWork.setEnabled(false);
            mEtNextWorkAnalyzes.setEnabled(false);
            mEtNextWorkRemark.setEnabled(false);
        }

        if (mPageIndex == 0) {
            mBtnBackUpWork.setEnabled(false);
        } else if (mPageIndex == (mPageIndexMax - 1)) {
            mBtnWriteNextWork.setEnabled(false);
        }
    }

    /**
     * 初始化工作内容
     */
    private void initWorkContent() {
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
        } else {
            mWorkPlan.setText("下周工作");
            mPracticalWork.setText("责任人");
            mWorkAnalyzes.setVisibility(View.GONE);
            mEtNextWorkAnalyzes.setVisibility(View.GONE);
            String work_plan = mSharedPreferences.getString(mTopTitle + "next_work", "");
            String practical_work = mSharedPreferences.getString(mTopTitle + "personLiable", "");
            String work_remark = mSharedPreferences.getString(mTopTitle + "remark", "");
            mEtNextWorkPlan.setText(work_plan);
            mEtNextPracticalWork.setText(practical_work);
            mEtNextWorkRemark.setText(work_remark);
        }
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        backStateNotify();
    }

    /**
     * 周报保存的参数
     */
    public void backStateNotify() {
        if (mIsWorkContent) {//本周工作
            if (!mEtNextWorkPlan.getText().toString().trim().equals("") && !mEtNextPracticalWork.getText().toString().trim().equals("") &&
                    !mEtNextWorkAnalyzes.getText().toString().trim().equals("")) {
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
                    !mEtNextWorkAnalyzes.getText().toString().trim().equals(""))) {
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
            if (!mEtNextWorkPlan.getText().toString().trim().equals("") && !mEtNextPracticalWork.getText().toString().trim().equals("")) {
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
            } else if ((!mEtNextWorkPlan.getText().toString().trim().equals("") || !mEtNextPracticalWork.getText().toString().trim().equals(""))) {
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
}
