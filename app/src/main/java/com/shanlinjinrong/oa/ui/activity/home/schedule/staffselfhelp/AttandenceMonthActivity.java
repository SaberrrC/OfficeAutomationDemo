package com.shanlinjinrong.oa.ui.activity.home.schedule.staffselfhelp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.retrofit.model.responsebody.CountResponse1;
import com.example.retrofit.model.responsebody.MyAttandanceResponse;
import com.example.retrofit.model.responsebody.MyAttendanceResponse;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.manager.AppConfig;
import com.shanlinjinrong.oa.manager.AppManager;
import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.bean.PopItem;
import com.shanlinjinrong.oa.ui.activity.home.schedule.staffselfhelp.adapter.DatePopAttandanceAdapter;
import com.shanlinjinrong.oa.ui.activity.home.schedule.staffselfhelp.contract.AttandanceMonthContract;
import com.shanlinjinrong.oa.ui.activity.home.schedule.staffselfhelp.presenter.AttandanceMonthPresenter;
import com.shanlinjinrong.oa.ui.base.HttpBaseActivity;
import com.shanlinjinrong.oa.utils.DateUtils;
import com.shanlinjinrong.oa.views.MonthSelectPopWindow;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

//考勤月历界面
public class AttandenceMonthActivity extends HttpBaseActivity<AttandanceMonthPresenter> implements AttandanceMonthContract.View, View.OnClickListener, DatePopAttandanceAdapter.OnItemClick {

    @BindView(R.id.tv_title)
    TextView     tvTitle;
    @BindView(R.id.toolbar)
    Toolbar      toolbar;
    @BindView(R.id.ll_chose_time)
    LinearLayout mLlChoseTime;
    @BindView(R.id.ll_count_people)
    LinearLayout ll_count_people;
    @BindView(R.id.tv_time)
    TextView     tv_time;
    @BindView(R.id.ll_month)
    LinearLayout mDateLayout;
    @BindView(R.id.iv_divider)
    ImageView    iv_divider;
    @BindView(R.id.tv_people)
    TextView     tv_people;
    @BindView(R.id.layout_root)
    LinearLayout mRootView;
    @BindView(R.id.tv_date)
    TextView     tv_date;
    @BindView(R.id.tv_name)
    TextView     tv_name;
    @BindView(R.id.tv_gowork_time)
    TextView     tv_gowork_time;
    @BindView(R.id.tv_off_gowork_time)
    TextView     tv_off_gowork_time;
    @BindView(R.id.tv_state)
    TextView     mTvState;
    @BindView(R.id.tv_empty_layout)
    TextView     mTvEmptyLayout;
    @BindView(R.id.ll_currentday_state)
    LinearLayout mLlCurrentdayState;
    @BindView(R.id.tv_sign_in)
    ImageView    tvsignin;

    private String   mDay;
    private Calendar cal;
    private View     rootView;
    private int      mSelectedDay;
    private int      mCurrentDay;
    private int      mCurrentYear;
    private int      mCurrentMonth;
    private int      mSelectedYear;
    private int      mSelectedMonth;
    private boolean  isSelectedDay;
    private String mPrivateCode = "";
    private RecyclerView             mRecyclerView;
    private DatePopAttandanceAdapter mAdapter;
    private List<PopItem> mData = new ArrayList<>();
    private MonthSelectPopWindow monthSelectPopWindow;
    private List<String>                                         mDateTypeList          = new ArrayList<>();
    private HashMap<String, String>                              mDataTypeMap           = new HashMap<>();
    private MyAttandanceResponse                                 myAttandanceResponse   = new MyAttandanceResponse();
    private List<MyAttandanceResponse.AllWorkAttendanceListBean> mAllWorkAttendanceList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_month);
        ButterKnife.bind(this);
        initToolBar();
        initData();
        setTranslucentStatus(this);
    }

    private void initData() {
        cal = Calendar.getInstance();
        mCurrentDay = cal.get(Calendar.DAY_OF_MONTH);
        mCurrentMonth = cal.get(Calendar.MONTH) + 1;
        mCurrentYear = cal.get(Calendar.YEAR);

        mSelectedYear = mCurrentYear;
        mSelectedDay = mCurrentDay;
        mSelectedMonth = mCurrentMonth;

        tv_time.setText(mCurrentYear + "年" + mCurrentMonth + "月");
        iv_divider.setVisibility(View.GONE);
        mLlChoseTime.setOnClickListener(this);
        ll_count_people.setOnClickListener(this);
        rootView = View.inflate(AttandenceMonthActivity.this, R.layout.date_select_attandance, null);
        mDateLayout.addView(rootView);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.top_data_list);
        setData(mSelectedYear, true, mSelectedMonth, mSelectedDay);
        mPrivateCode = AppConfig.getAppConfig(AppManager.mContext).getPrivateCode();
        doHttp();

    }

    public void creatMonth(int selectPos) {
        String[] numArray = {"一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"};
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH);
        for (int i = 0; i < numArray.length; i++) {
            PopItem item = new PopItem(numArray[i], !(i < month), i == selectPos);
            mData.add(item);
        }
    }

    public void setData(int year, final boolean isDay, int month, int selectPos) {
        if (mData != null) {
            mData.clear();
        }
        if (isDay) {
            List<PopItem> date = DateUtils.getAttandenceDate(year, month, selectPos);
            mData.addAll(date);
        } else {
            creatMonth(selectPos);
        }
        mRecyclerView.setLayoutManager(new GridLayoutManager(AttandenceMonthActivity.this, isDay ? 7 : 6));
        mAdapter = new DatePopAttandanceAdapter(mData);
        mAdapter.setOnItemClick(this);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    private void initToolBar() {
        if (toolbar == null) {
            return;
        }
        setTitle("");//必须在setSupportActionBar之前调用
        toolbar.setTitleTextColor(Color.parseColor("#000000"));
        setSupportActionBar(toolbar);
        tvTitle.setText("考勤月历");
        Toolbar.LayoutParams lp = new Toolbar.LayoutParams(
                Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        tvTitle.setLayoutParams(lp);

        toolbar.setNavigationIcon(R.drawable.toolbar_back);
        toolbar.setNavigationOnClickListener(view -> finish());
    }

    @Override
    public void uidNull(String code) {
        catchWarningByCode(code);
    }

    public void doHttp() {
        String month = mSelectedMonth + "";
        if (mSelectedMonth < 10) {
            month = "0" + mSelectedMonth;
        }
        mPresenter.sendData(mPrivateCode, mSelectedYear + "", month + "", AttandenceMonthActivity.this);
    }

    @Override
    public void sendDataSuccess(MyAttandanceResponse myAttandanceResponse) {
        mAllWorkAttendanceList.clear();
        mDataTypeMap.clear();
        mAllWorkAttendanceList = myAttandanceResponse.getAllWorkAttendanceList();

        //所有考勤
        List<MyAttandanceResponse.AllWorkAttendanceListBean> allWorkAttendanceList = myAttandanceResponse.getAllWorkAttendanceList();
        for (MyAttandanceResponse.AllWorkAttendanceListBean item : allWorkAttendanceList) {
            if (item.getSignCause() != null) {
                continue;
            }
            try {
                String key = Integer.parseInt(item.getCalendar().split("-")[2]) + "";
                mDataTypeMap.put(key, getType(item.getTbmstatus()) + "");
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }

        //迟到
        List<MyAttandanceResponse.CdWorkAttendanceListBean> cdWorkAttendanceList = myAttandanceResponse.getCdWorkAttendanceList();
        for (MyAttandanceResponse.CdWorkAttendanceListBean item : cdWorkAttendanceList) {
            if (item.getSignCause() != null) {
                continue;
            }
            String key = Integer.parseInt(item.getCalendar().split("-")[2]) + "";
            mDataTypeMap.put(key, getType("[迟到]") + "");
        }
        //旷工
        List<MyAttandanceResponse.KgWorkAttendanceListBean> kgWorkAttendanceList = myAttandanceResponse.getKgWorkAttendanceList();
        for (MyAttandanceResponse.KgWorkAttendanceListBean item : kgWorkAttendanceList) {
            if (item.getSignCause() != null) {
                continue;
            }
            String key = Integer.parseInt(item.getCalendar().split("-")[2]) + "";
            mDataTypeMap.put(key, getType("[旷工]") + "");
        }
        //早退
        List<MyAttandanceResponse.ZtWorkAttendanceListBean> ztWorkAttendanceList = myAttandanceResponse.getZtWorkAttendanceList();
        for (MyAttandanceResponse.ZtWorkAttendanceListBean item : ztWorkAttendanceList) {
            if (item.getSignCause() != null) {
                continue;
            }
            String key = Integer.parseInt(item.getCalendar().split("-")[2]) + "";
            mDataTypeMap.put(key, getType("[早退]") + "");
        }

        //休假
        List<MyAttandanceResponse.XjWorkAttendanceListBean> xjWorkAttendanceList = myAttandanceResponse.getXjWorkAttendanceList();
        for (MyAttandanceResponse.XjWorkAttendanceListBean item : xjWorkAttendanceList) {
            if (item.getSignCause() != null) {
                continue;
            }
            String key = Integer.parseInt(item.getCalendar().split("-")[2]) + "";
            mDataTypeMap.put(key, getType("[休假]") + "");
        }

        //加班
        List<MyAttandanceResponse.JbWorkAttendanceListBean> jbWorkAttendanceList = myAttandanceResponse.getJbWorkAttendanceList();
        for (MyAttandanceResponse.JbWorkAttendanceListBean item : jbWorkAttendanceList) {
            if (item.getSignCause() != null) {
                continue;
            }
            String key = Integer.parseInt(item.getCalendar().split("-")[2]) + "";
            mDataTypeMap.put(key, getType("[加班]") + "");
        }

        //出差
        List<MyAttandanceResponse.CcWorkAttendanceListBean> ccWorkAttendanceList = myAttandanceResponse.getCcWorkAttendanceList();
        for (MyAttandanceResponse.CcWorkAttendanceListBean item : ccWorkAttendanceList) {
            if (item.getSignCause() != null) {
                continue;
            }
            String key = Integer.parseInt(item.getCalendar().split("-")[2]) + "";
            mDataTypeMap.put(key, getType("[出差]") + "");
        }

        for (PopItem mDataItrm : mData) {
            String content = mDataItrm.getContent();
            if (mDataItrm.isEnable()) {
                if (mDataTypeMap.containsKey(content)) {
                    mDataItrm.setDateType(Integer.parseInt(mDataTypeMap.get(content)));
                }
            }
        }
        mRecyclerView.requestLayout();
        mAdapter.notifyDataSetChanged();

        if (!isSelectedDay) {
            isSelectedDay = true;
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String date = format.format(new Date());

            mPresenter.queryDayAttendance(mPrivateCode, date);
        } else {
            String year = tv_time.getText().toString().substring(0, tv_time.getText().toString().indexOf("年"));
            String month = mSelectedMonth + "";
            if (mSelectedMonth < 10) {
                month = "0" + mSelectedMonth;
            }
            String date = year + "-" + month + "-" + "01";
            mPresenter.queryDayAttendance(mPrivateCode, date);
        }
    }

    @Override
    public void queryDayAttendanceSuccess(List<MyAttendanceResponse> bean) {
        try {
            List<String> data = new ArrayList<>();
            if (bean != null) {
                if (bean.size() > 0) {
                    mTvEmptyLayout.setVisibility(View.GONE);
                    mLlCurrentdayState.setVisibility(View.VISIBLE);
                } else {
                    mTvEmptyLayout.setVisibility(View.VISIBLE);
                    mLlCurrentdayState.setVisibility(View.GONE);
//                    mTvEmptyLayout.setText("暂无日考勤记录！");
                    return;
                }

                //出差 >加班 >休假> 签卡>旷工>迟到>早退
                for (int i = 0; i < bean.size(); i++) {

                    tv_date.setText(bean.get(i).getCalendar());
                    tv_name.setText(bean.get(i).getPsname());
                    tv_gowork_time.setText(bean.get(i).getOnebegintime());
                    tv_off_gowork_time.setText(bean.get(i).getTwoendtime());
                    try {
                        if (bean.get(i).getTbmstatus() == null) {
                            mTvState.setText("");
                            return;
                        }
                        if (bean.get(i).getTbmstatus().contains("[出差]")) {
                            mTvState.setText("[出差]");
                            return;
                        }
                        if (bean.get(i).getTbmstatus().contains("[外出]")) {
                            mTvState.setText("[出差]");
                            return;
                        }
                        if (bean.get(i).getTbmstatus().contains("[加班]")) {
                            mTvState.setText("[加班]");
                            return;
                        }
                        if (bean.get(i).getTbmstatus().contains("[转调休加班]")) {
                            mTvState.setText("[加班]");
                            return;
                        }
                        if (bean.get(i).getTbmstatus().contains("[加班费加班]")) {
                            mTvState.setText("[加班]");
                            return;
                        }
                        if (bean.get(i).getTbmstatus().contains("[休假]")) {
                            mTvState.setText("[休假]");
                            return;
                        }
                        if (bean.get(i).getTbmstatus().contains("[事假]")) {
                            mTvState.setText("[休假]");
                            return;
                        }
                        if (bean.get(i).getTbmstatus().contains("[婚假]")) {
                            mTvState.setText("[休假]");
                            return;
                        }
                        if (bean.get(i).getTbmstatus().contains("[年假]")) {
                            mTvState.setText("[休假]");
                            return;
                        }
                        if (bean.get(i).getTbmstatus().contains("[丧假]")) {
                            mTvState.setText("[休假]");
                            return;
                        }
                        if (bean.get(i).getTbmstatus().contains("[工伤]")) {
                            mTvState.setText("[休假]");
                            return;
                        }
                        if (bean.get(i).getTbmstatus().contains("[加班转调休]")) {
                            mTvState.setText("[休假]");
                            return;
                        }
                        if (bean.get(i).getTbmstatus().contains("[哺乳假]")) {
                            mTvState.setText("[休假]");
                            return;
                        }
                        if (bean.get(i).getTbmstatus().contains("[带薪病假]")) {
                            mTvState.setText("[休假]");
                            return;
                        }
                        if (bean.get(i).getTbmstatus().contains("[不带薪病假]")) {
                            mTvState.setText("[休假]");
                            return;
                        }
                        if (bean.get(i).getTbmstatus().contains("[带薪产假]")) {
                            mTvState.setText("[休假]");
                            return;
                        }
                        if (bean.get(i).getTbmstatus().contains("[不带薪产假]")) {
                            mTvState.setText("[休假]");
                            return;
                        }
                        if (bean.get(i).getTbmstatus().contains("[陪产假]")) {
                            mTvState.setText("[休假]");
                            return;
                        }
                        if (bean.get(i).getTbmstatus().contains("[产检假]")) {
                            mTvState.setText("[休假]");
                            return;
                        }
                        if (bean.get(i).getTbmstatus().contains("[特殊哺乳假]")) {
                            mTvState.setText("[休假]");
                            return;
                        }
                        if (bean.get(i).getTbmstatus().contains("[特殊产前假]")) {
                            mTvState.setText("[休假]");
                            return;
                        }

                        if (bean.get(i).getSignCause() == null || "".equals(bean.get(i).getSignCause())) {
                            tvsignin.setVisibility(View.GONE);
                        } else {
                            tvsignin.setVisibility(View.VISIBLE);
                        }
                        //出差 >加班 >休假> 签卡>旷工>迟到>早退
                        if (bean.get(i).getTbmstatus().contains("[签卡]")) {
                            mTvState.setText("[签卡]");
                            return;
                        }
                        if (bean.get(i).getTbmstatus().contains("[忘记打卡]")) {
                            mTvState.setText("[忘记打卡]");
                            return;
                        }
                        if (bean.get(i).getTbmstatus().contains("[考勤机故障]")) {
                            mTvState.setText("[考勤机故障]");
                            return;
                        }
                        if (bean.get(i).getTbmstatus().contains("[地铁故障]")) {
                            mTvState.setText("[地铁故障]");
                            return;
                        }
                        if (bean.get(i).getTbmstatus().contains("[旷工]")) {
                            mTvState.setText("[旷工]");
                            return;
                        }
                        if (bean.get(i).getTbmstatus().contains("[迟到]")) {
                            mTvState.setText("[迟到]");
                            return;
                        }
                        if (bean.get(i).getTbmstatus().contains("[早退]")) {
                            mTvState.setText("[早退]");
                            return;
                        }
                        mTvState.setText(bean.get(i).getTbmstatus());
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            } else {
                mTvEmptyLayout.setText("暂无日考勤信息！");
                mTvEmptyLayout.setVisibility(View.VISIBLE);
                mLlCurrentdayState.setVisibility(View.GONE);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendDataFailed(int errCode, String msg) {
        mTvEmptyLayout.setVisibility(View.VISIBLE);
        mLlCurrentdayState.setVisibility(View.GONE);
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        mTvEmptyLayout.setText(msg);
    }

    public int getType(String str) {
        if (!TextUtils.isEmpty(str)) {
            switch (str) {
                case "[休假]":
                    return 1;
                case "[加班转调休]":
                case "[加班]":
                    return 2;
                case "[出差]":
                case "[外出]":
                    return 3;
                case "[早退]":
                case "[旷工]":
                case "[迟到]":
                case "[其他]":
                    return 4;
                default:
                    return 0;
            }
        }
        return 0;
    }

    @Override
    public void onItemClicked(View v, int position) {
        for (int i = 0; i < mData.size(); i++) {
            if (i == position) {
                mData.get(position).setSelect(true);
            } else {
                mData.get(i).setSelect(false);
            }
        }
        mRecyclerView.requestLayout();
        mAdapter.notifyDataSetChanged();
        String content = mData.get(position).getContent();
        mSelectedDay = Integer.parseInt(content);
        String mDay = (mSelectedDay < 10) ? "0" + mSelectedDay : mSelectedDay + "";
        String month = (mSelectedMonth < 10) ? "0" + mSelectedMonth : mSelectedMonth + "";
        String year = tv_time.getText().toString().substring(0, tv_time.getText().toString().indexOf("年"));
        String date = year + "-" + month + "-" + mDay;
        mPresenter.queryDayAttendance(mPrivateCode, date);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            //统计时间选择
            case R.id.ll_chose_time:
                monthSelectPopWindow = new MonthSelectPopWindow(AttandenceMonthActivity.this,
                        new MonthSelectPopWindow.PopListener() {
                            @Override
                            public void cancle() {
                                monthSelectPopWindow.dismiss();
                            }

                            @Override
                            public void confirm(String year, String month) {
                                mSelectedYear = Integer.parseInt(year);
                                mSelectedMonth = Integer.parseInt(month);

                                tv_time.setText(year + "年" + month + "月");
                                if (mSelectedMonth == mCurrentMonth) {
                                    setData(mSelectedYear, true, mSelectedMonth, mCurrentDay);
                                } else {
                                    setData(mSelectedYear, true, mSelectedMonth, 1);
                                }
                                mRecyclerView.requestLayout();
                                mAdapter.notifyDataSetChanged();
                                monthSelectPopWindow.dismiss();
                                doHttp();
                            }
                        });
                monthSelectPopWindow.showAtLocation(mRootView, Gravity.BOTTOM, 0, 0);
                break;
            //统计人员选择
            case R.id.ll_count_people:
                Intent intent = new Intent(AttandenceMonthActivity.this, CountPeopleActivity.class);
                startActivityForResult(intent, 100);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            try {
                CountResponse1 people = (CountResponse1) data.getSerializableExtra("people");
                tv_people.setText(people.getPsname());
                mPrivateCode = people.getCode();
                doHttp();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }
}
