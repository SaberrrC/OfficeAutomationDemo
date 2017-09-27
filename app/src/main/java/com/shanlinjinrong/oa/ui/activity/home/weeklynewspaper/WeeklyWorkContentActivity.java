package com.shanlinjinrong.oa.ui.activity.home.weeklynewspaper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.iflytek.cloud.thirdparty.V;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.common.Constants;
import com.shanlinjinrong.oa.manager.AppConfig;
import com.shanlinjinrong.oa.manager.AppManager;
import com.shanlinjinrong.oa.ui.activity.home.weeklynewspaper.bean.WorkStateTipNotifyChangeEvent;
import com.shanlinjinrong.views.common.CommonTopView;

import org.greenrobot.eventbus.EventBus;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 发起周报、下周计划 内容界面
 */
public class WeeklyWorkContentActivity extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.top_view)
    CommonTopView mTopView;
    @Bind(R.id.et_next_work_plan)
    EditText mEtNextWorkPlan;
    @Bind(R.id.et_next_practical_work)
    EditText mEtNextPracticalWork;
    @Bind(R.id.et_next_work_analyzes)
    EditText mEtNextWorkAnalyzes;
    @Bind(R.id.btn_write_next_work)
    TextView mBtnLookNextWork;
    @Bind(R.id.btn_back_up_work)
    TextView mBtnBackUpWork;
    @Bind(R.id.et_next_work_remark)
    EditText mEtNextWorkRemark;
    @Bind(R.id.work_plan)
    TextView mWorkPlan;
    @Bind(R.id.practical_work)
    TextView mPracticalWork;
    @Bind(R.id.work_analyzes)
    TextView mWorkAnalyzes;

    private int mIndex;
    private int mWeeklySize;
    private int mWeekly_Size;
    private String mTopTitle;
    private boolean mIsWorkContent;
    private Matcher mWorkPlanMatcher;
    private Matcher mPracticalWorkMatcher;
    private Matcher mWorkAnalyzesMatcher;
    private Matcher mWorkRemarkMatcher;
    private SharedPreferences mSharedPreferences;
    private Pattern mCompile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_newspaper_work_content);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        initEditText();
        mTopTitle = getIntent().getStringExtra("TopTitle");
        mTopView.setAppTitle(mTopTitle);
        mTopView.getLeftView().setOnClickListener(this);
        mIsWorkContent = getIntent().getBooleanExtra("isWorkContent", false);
        mSharedPreferences = getSharedPreferences(AppConfig.getAppConfig(AppManager.
                sharedInstance()).getPrivateCode() + Constants.WORK_WEEKLY_TEMP_DATA, Context.MODE_PRIVATE);
        if (mIsWorkContent) {
            mWorkPlan.setText("计划工作");
            mPracticalWork.setText("实际工作");
            mWorkAnalyzes.setVisibility(View.VISIBLE);
            mEtNextWorkAnalyzes.setVisibility(View.VISIBLE);
            mWeeklySize = mSharedPreferences.getInt("workContentSize", 4);
            String work_plan = mSharedPreferences.getString(mTopTitle + "work_plan", "");
            String practical_work = mSharedPreferences.getString(mTopTitle + "practical_work", "");
            String work_analyzes = mSharedPreferences.getString(mTopTitle + "work_analyzes", "");
            String work_remark = mSharedPreferences.getString(mTopTitle + "work_remark", "");
            mEtNextWorkPlan.setText(work_plan);
            mEtNextPracticalWork.setText(practical_work);
            mEtNextWorkAnalyzes.setText(work_analyzes);
            mEtNextWorkRemark.setText(work_remark);
            getWorkContentIndex();
            WorkSelectionState(mWeeklySize);
        } else {
            mWorkPlan.setText("下周工作");
            mPracticalWork.setText("责任人");
            mWorkAnalyzes.setVisibility(View.GONE);
            mEtNextWorkAnalyzes.setVisibility(View.GONE);
            mWeeklySize = mSharedPreferences.getInt("workPlanSize", 4);
            String work_plan = mSharedPreferences.getString(mTopTitle + "next_work", "");
            String practical_work = mSharedPreferences.getString(mTopTitle + "personLiable", "");
            String work_remark = mSharedPreferences.getString(mTopTitle + "remark", "");
            mEtNextWorkPlan.setText(work_plan);
            mEtNextPracticalWork.setText(practical_work);
            mEtNextWorkRemark.setText(work_remark);
            getWorkContentIndex();
            WorkSelectionState(mWeeklySize);
        }
    }

    private void initEditText() {
        mEtNextWorkPlan.setFilters(new InputFilter[]{new InputFilter.LengthFilter(50)});
        mEtNextPracticalWork.setFilters(new InputFilter[]{new InputFilter.LengthFilter(50)});
        mEtNextWorkAnalyzes.setFilters(new InputFilter[]{new InputFilter.LengthFilter(50)});
        mEtNextWorkRemark.setFilters(new InputFilter[]{new InputFilter.LengthFilter(50)});
        mCompile = Pattern.compile("[0-9]*");
        mEtNextWorkPlan.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    String workPlan = mEtNextWorkPlan.getText().toString();
                    mWorkPlanMatcher = mCompile.matcher(workPlan);
                    if (mWorkPlanMatcher.matches() && !TextUtils.isEmpty(workPlan.trim())) {
                        Toast.makeText(WeeklyWorkContentActivity.this, "工作计划不能仅输入数字！", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        mEtNextPracticalWork.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    String practicalWork = mEtNextPracticalWork.getText().toString();
                    mPracticalWorkMatcher = mCompile.matcher(practicalWork);
                    if (mPracticalWorkMatcher.matches() && !TextUtils.isEmpty(practicalWork.trim())) {
                        Toast.makeText(WeeklyWorkContentActivity.this, "工作计划不能仅输入数字！", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        mEtNextWorkAnalyzes.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    String workAnalyzes = mEtNextWorkAnalyzes.getText().toString();
                    mWorkAnalyzesMatcher = mCompile.matcher(workAnalyzes);
                    if (mWorkAnalyzesMatcher.matches() && !TextUtils.isEmpty(workAnalyzes.trim())) {
                        Toast.makeText(WeeklyWorkContentActivity.this, "工作计划不能仅输入数字！", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        mEtNextWorkRemark.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    String workRemark = mEtNextWorkRemark.getText().toString();
                    mWorkRemarkMatcher = mCompile.matcher(workRemark);
                    if (mWorkRemarkMatcher.matches() && !TextUtils.isEmpty(workRemark.trim())) {
                        Toast.makeText(WeeklyWorkContentActivity.this, "工作计划不能仅输入数字！", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void WorkSelectionState(int weeklySize) {
        if (mWeekly_Size == 1) {
            mBtnBackUpWork.setBackground(getResources().getDrawable(R.drawable.btn_gray_round_bg));
            mBtnBackUpWork.setClickable(false);
        } else if (mWeekly_Size >= weeklySize) {
            mBtnLookNextWork.setBackground(getResources().getDrawable(R.drawable.btn_gray_round_bg));
            mBtnLookNextWork.setClickable(false);
        } else {
            mBtnLookNextWork.setClickable(true);
            mBtnBackUpWork.setClickable(true);
        }
    }

    @OnClick({R.id.btn_write_next_work, R.id.btn_back_up_work})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_write_next_work:
                backStateNotify();
                Intent intent = new Intent(this, WeeklyWorkContentActivity.class);
                if (mIsWorkContent) {
                    intent.putExtra("TopTitle", "工作内容 " + mIndex);
                    intent.putExtra("isWorkContent", true);
                } else {
                    intent.putExtra("TopTitle", "工作计划 " + mIndex);
                }
                startActivity(intent);
                WorkSelectionState(mWeeklySize);
                finish();
                break;
            case R.id.btn_back_up_work:
                backStateNotify();
                Intent intent1 = new Intent(this, WeeklyWorkContentActivity.class);
                mIndex = mIndex - 2;
                if (mIsWorkContent) {
                    intent1.putExtra("TopTitle", "工作内容 " + mIndex);
                    intent1.putExtra("isWorkContent", true);
                } else {
                    intent1.putExtra("TopTitle", "工作计划 " + mIndex);
                }
                startActivity(intent1);
                WorkSelectionState(mWeeklySize);
                finish();
                break;
        }
    }


    @Override
    public void onClick(View view) {
        backStateNotify();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        backStateNotify();
    }

    /**
     * 周报保存的参数
     */
    private void backStateNotify() {
        if (mWorkPlanMatcher == null) {
            String workPlan = mEtNextWorkPlan.getText().toString();
            mWorkPlanMatcher = mCompile.matcher(workPlan);
        }
        if (mPracticalWorkMatcher == null) {
            String practicalWork = mEtNextPracticalWork.getText().toString();
            mPracticalWorkMatcher = mCompile.matcher(practicalWork);
        }
        if (mWorkAnalyzesMatcher == null) {
            String workAnalyzes = mEtNextWorkAnalyzes.getText().toString();
            mWorkAnalyzesMatcher = mCompile.matcher(workAnalyzes);
        }
        if (mWorkRemarkMatcher == null) {
            String workRemark = mEtNextWorkRemark.getText().toString();
            mWorkRemarkMatcher = mCompile.matcher(workRemark);
        }

        if (mIsWorkContent) {//本周工作
            if (!mEtNextWorkPlan.getText().toString().trim().equals("") && !mEtNextPracticalWork.getText().toString().trim().equals("") &&
                    !mEtNextWorkAnalyzes.getText().toString().trim().equals("") && !mEtNextWorkRemark.getText().toString().trim().equals("") &&
                    !mWorkPlanMatcher.matches() && !mPracticalWorkMatcher.matches() && !mWorkAnalyzesMatcher.matches() && !mWorkRemarkMatcher.matches()) {
                WorkStateTipNotifyChangeEvent state = new WorkStateTipNotifyChangeEvent();
                state.setState("已填写");
                state.setPosition(mTopTitle);
                state.setWorkContent(true);
                EventBus.getDefault().post(state);
                SharedPreferences.Editor edit = getSharedPreferences(AppConfig.getAppConfig(AppManager.sharedInstance()).getPrivateCode() +
                        Constants.WORK_WEEKLY_TEMP_DATA, Context.MODE_PRIVATE).edit();
                edit.putString(mTopTitle + "work_plan", mEtNextWorkPlan.getText().toString() + "");
                edit.putString(mTopTitle + "practical_work", mEtNextPracticalWork.getText().toString() + "");
                edit.putString(mTopTitle + "work_analyzes", mEtNextWorkAnalyzes.getText().toString() + "");
                edit.putString(mTopTitle + "work_remark", mEtNextWorkRemark.getText().toString() + "");
                //工作内容填写情况 0完成 1待填写 2未填写
                edit.putString(mTopTitle + "this_work_content_state", "0");
                edit.apply();
                finish();
            } else if ((!mEtNextWorkPlan.getText().toString().trim().equals("") || !mEtNextPracticalWork.getText().toString().trim().equals("") ||
                    !mEtNextWorkAnalyzes.getText().toString().trim().equals("") || !mEtNextWorkRemark.getText().toString().trim().equals("")) && (
                    mWorkPlanMatcher.matches() || mPracticalWorkMatcher.matches() || mWorkAnalyzesMatcher.matches() || mWorkRemarkMatcher.matches())) {
                WorkStateTipNotifyChangeEvent state = new WorkStateTipNotifyChangeEvent();
                state.setState("待完善");
                state.setPosition(mTopTitle);
                state.setWorkContent(true);
                EventBus.getDefault().post(state);
                SharedPreferences.Editor edit = getSharedPreferences(AppConfig.getAppConfig(AppManager.sharedInstance()).getPrivateCode() +
                        Constants.WORK_WEEKLY_TEMP_DATA, Context.MODE_PRIVATE).edit();
                edit.putString(mTopTitle + "work_plan", mEtNextWorkPlan.getText().toString() + "");
                edit.putString(mTopTitle + "practical_work", mEtNextPracticalWork.getText().toString() + "");
                edit.putString(mTopTitle + "work_analyzes", mEtNextWorkAnalyzes.getText().toString() + "");
                edit.putString(mTopTitle + "work_remark", mEtNextWorkRemark.getText().toString() + "");
                //工作内容填写情况 0完成 1待填写 2未填写
                edit.putString(mTopTitle + "this_work_content_state", "1");
                edit.apply();
                finish();
            } else {
                WorkStateTipNotifyChangeEvent state = new WorkStateTipNotifyChangeEvent();
                state.setState("未填写");
                state.setPosition(mTopTitle);
                state.setWorkContent(true);
                EventBus.getDefault().post(state);
                SharedPreferences.Editor edit = getSharedPreferences(AppConfig.getAppConfig(AppManager.sharedInstance()).getPrivateCode() +
                        Constants.WORK_WEEKLY_TEMP_DATA, Context.MODE_PRIVATE).edit();
                edit.putString(mTopTitle + "work_plan", "");
                edit.putString(mTopTitle + "practical_work", "");
                edit.putString(mTopTitle + "work_analyzes", "");
                edit.putString(mTopTitle + "work_remark", "");
                //工作内容填写情况 0完成 1待填写 2未填写
                edit.putString(mTopTitle + "this_work_content_state", "2");
                edit.apply();
                finish();
            }
        } else {//下周计划
            if (!mEtNextWorkPlan.getText().toString().trim().equals("") && !mEtNextPracticalWork.getText().toString().trim().equals("") &&
                    !mEtNextWorkRemark.getText().toString().trim().equals("") && !mWorkPlanMatcher.matches() && !mPracticalWorkMatcher.matches() && !mWorkRemarkMatcher.matches()) {
                WorkStateTipNotifyChangeEvent state = new WorkStateTipNotifyChangeEvent();
                state.setState("已填写");
                state.setPosition(mTopTitle);
                state.setWorkContent(false);
                EventBus.getDefault().post(state);
                SharedPreferences.Editor edit = getSharedPreferences(AppConfig.getAppConfig(AppManager.sharedInstance()).getPrivateCode() +
                        Constants.WORK_WEEKLY_TEMP_DATA, Context.MODE_PRIVATE).edit();
                edit.putString(mTopTitle + "next_work", mEtNextWorkPlan.getText().toString() + "");
                edit.putString(mTopTitle + "personLiable", mEtNextPracticalWork.getText().toString() + "");
                edit.putString(mTopTitle + "remark", mEtNextWorkRemark.getText().toString() + "");
                //工作内容填写情况 0完成 1待填写 2未填写
                edit.putString(mTopTitle + "this_work_plan_state", "0");
                edit.apply();
                finish();
            } else if ((!mEtNextWorkPlan.getText().toString().trim().equals("") || !mEtNextPracticalWork.getText().toString().trim().equals("") ||
                    !mEtNextWorkRemark.getText().toString().trim().equals("")) && (mWorkPlanMatcher.matches() || mPracticalWorkMatcher.matches() || mWorkRemarkMatcher.matches())) {
                WorkStateTipNotifyChangeEvent state = new WorkStateTipNotifyChangeEvent();
                state.setState("待完善");
                state.setPosition(mTopTitle);
                state.setWorkContent(false);
                EventBus.getDefault().post(state);
                SharedPreferences.Editor edit = getSharedPreferences(AppConfig.getAppConfig(AppManager.sharedInstance()).getPrivateCode() +
                        Constants.WORK_WEEKLY_TEMP_DATA, Context.MODE_PRIVATE).edit();
                edit.putString(mTopTitle + "next_work", mEtNextWorkPlan.getText().toString() + "");
                edit.putString(mTopTitle + "personLiable", mEtNextPracticalWork.getText().toString() + "");
                edit.putString(mTopTitle + "remark", mEtNextWorkRemark.getText().toString() + "");
                //工作内容填写情况 0完成 1待填写 2未填写
                edit.putString(mTopTitle + "this_work_plan_state", "1");
                edit.apply();
                finish();
            } else {
                WorkStateTipNotifyChangeEvent state = new WorkStateTipNotifyChangeEvent();
                state.setState("未填写");
                state.setPosition(mTopTitle);
                state.setWorkContent(false);
                EventBus.getDefault().post(state);
                SharedPreferences.Editor edit = getSharedPreferences(AppConfig.getAppConfig(AppManager.sharedInstance()).getPrivateCode() +
                        Constants.WORK_WEEKLY_TEMP_DATA, Context.MODE_PRIVATE).edit();
                edit.putString(mTopTitle + "next_work", "");
                edit.putString(mTopTitle + "personLiable", "");
                edit.putString(mTopTitle + "remark", "");
                //工作内容填写情况 0完成 1待填写 2未填写
                edit.putString(mTopTitle + "this_work_plan_state", "2");
                edit.apply();
                finish();
            }
        }
    }

    private void getWorkContentIndex() {
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(mTopTitle);
        mIndex = Integer.parseInt(m.replaceAll("").trim());
        mWeekly_Size = mIndex;
        mIndex++;
    }
}
