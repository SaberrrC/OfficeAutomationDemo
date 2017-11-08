package com.shanlinjinrong.oa.ui.activity.home.weeklynewspaper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.common.Constants;
import com.shanlinjinrong.oa.manager.AppConfig;
import com.shanlinjinrong.oa.manager.AppManager;
import com.shanlinjinrong.oa.ui.activity.home.weeklynewspaper.adapter.WeeklyWorkAdapter;
import com.shanlinjinrong.oa.ui.activity.home.weeklynewspaper.bean.WorkStateTipNotifyChangeEvent;
import com.shanlinjinrong.oa.ui.activity.home.weeklynewspaper.fragment.WeeklyWorkContentFragment;
import com.shanlinjinrong.oa.ui.activity.home.workreport.adapter.WorkReportAdapter;
import com.shanlinjinrong.views.common.CommonTopView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 发起周报、下周计划 内容界面
 */
public class WeeklyWorkContentActivity extends AppCompatActivity implements WeeklyWorkContentFragment.OnPageBthClickListener, ViewPager.OnPageChangeListener {

    @BindView(R.id.top_view)
    CommonTopView mTopView;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    private int pageIndex;
    private int mPlanSize;
    private int mWeeklySize;
    private String mTopTitle;
    private boolean mIsWorkContent;
    private boolean mIsEditTextEnabled;
    private SharedPreferences mSharedPreferences;
    private List<WeeklyWorkContentFragment> mWeeklyWorkContentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_newspaper_work_content);
        ButterKnife.bind(this);
        initData();
        initView();
    }

    private void initView() {
        mTopView.setAppTitle(mTopTitle);
        mViewPager.setAdapter(new WeeklyWorkAdapter(getSupportFragmentManager(), mWeeklyWorkContentList));
        mViewPager.setOffscreenPageLimit(1);
        mViewPager.setCurrentItem(pageIndex);
        EventBus.getDefault().postSticky(mTopTitle);
        mViewPager.addOnPageChangeListener(this);
    }

    private void initData() {
        mSharedPreferences = getSharedPreferences(AppConfig.getAppConfig(AppManager.
                sharedInstance()).getPrivateCode() + Constants.WORK_WEEKLY_TEMP_DATA, Context.MODE_PRIVATE);
        mWeeklySize = mSharedPreferences.getInt("workContentSize", 4);
        mPlanSize = mSharedPreferences.getInt("workPlanSize", 4);
        mTopTitle = getIntent().getStringExtra("TopTitle");
        pageIndex = getIntent().getIntExtra("index", 0);
        mIsEditTextEnabled = getIntent().getBooleanExtra("isEditTextEnabled", false);
        mIsWorkContent = getIntent().getBooleanExtra("isWorkContent", false);
        if (mIsWorkContent) {
            mWeeklyWorkContentList = new ArrayList<>();
            for (int i = 0; i < mWeeklySize; i++) {
                WeeklyWorkContentFragment weeklyWorkContentFragment = new WeeklyWorkContentFragment();
                Bundle bundle = new Bundle();
                bundle.putBoolean("isWorkContent", mIsWorkContent);
                bundle.putString("title", "工作内容 " + (i + 1));
                bundle.putInt("index", i);
                bundle.putInt("indexMax", mWeeklySize);
                bundle.putBoolean("isEditTextEnabled", mIsEditTextEnabled);
                weeklyWorkContentFragment.setArguments(bundle);
                weeklyWorkContentFragment.setPageBtnClickListener(this);
                mWeeklyWorkContentList.add(weeklyWorkContentFragment);
            }
        } else {
            mWeeklyWorkContentList = new ArrayList<>();
            for (int i = 0; i < mPlanSize; i++) {
                WeeklyWorkContentFragment weeklyWorkContentFragment = new WeeklyWorkContentFragment();
                Bundle bundle = new Bundle();
                bundle.putBoolean("isWorkContent", mIsWorkContent);
                bundle.putString("title", "工作计划 " + (i + 1));
                bundle.putInt("index", i);
                bundle.putInt("indexMax", mPlanSize);
                bundle.putBoolean("isEditTextEnabled", mIsEditTextEnabled);
                weeklyWorkContentFragment.setArguments(bundle);
                weeklyWorkContentFragment.setPageBtnClickListener(this);
                mWeeklyWorkContentList.add(weeklyWorkContentFragment);
            }
        }
    }

    @Override
    public void onLastPageClick() {
        int currentItem = mViewPager.getCurrentItem();
        if (currentItem > 0) {
            mViewPager.setCurrentItem(--currentItem);
        }
    }

    @Override
    public void onNextPageClick() {
        int currentItem = mViewPager.getCurrentItem();
        if (currentItem < mWeeklyWorkContentList.size() - 1) {
            mViewPager.setCurrentItem(++currentItem);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mWeeklyWorkContentList.get(pageIndex).backStateNotify();
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (mIsWorkContent) {
            mWeeklyWorkContentList.get(position);
            mTopView.setAppTitle("工作计划 " + (position + 1));
            mWeeklyWorkContentList.get(position).backStateNotify();
        } else {
            mTopView.setAppTitle("工作计划 " + (position + 1));
            mWeeklyWorkContentList.get(position).backStateNotify();
        }
        pageIndex = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
