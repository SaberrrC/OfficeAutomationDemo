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

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

//考勤月历界面
public class AttandenceMonthActivity extends HttpBaseActivity<AttandanceMonthPresenter> implements AttandanceMonthContract.View, View.OnClickListener {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.ll_chose_time)
    LinearLayout mLlChoseTime;
    @BindView(R.id.ll_count_people)
    LinearLayout ll_count_people;
    @BindView(R.id.tv_time)
    TextView tv_time;
    @BindView(R.id.ll_month)
    LinearLayout mDateLayout;
    @BindView(R.id.iv_divider)
    ImageView iv_divider;
    @BindView(R.id.tv_people)
    TextView tv_people;
    @BindView(R.id.layout_root)
    LinearLayout mRootView;
    @BindView(R.id.tv_date)
    TextView tv_date;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_gowork_time)
    TextView tv_gowork_time;
    @BindView(R.id.tv_off_gowork_time)
    TextView tv_off_gowork_time;
    @BindView(R.id.tv_state)
    TextView mTvState;
    @BindView(R.id.tv_empty_layout)
    TextView mTvEmptyLayout;
    @BindView(R.id.ll_currentday_state)
    LinearLayout mLlCurrentdayState;

    private String mDay;
    private Calendar cal;
    private View rootView;
    private int mSelectedDay;
    private int mCurrentDay;
    private int mCurrentYear;
    private int mCurrentMonth;
    private int mSelectedYear;
    private int mSelectedMonth;
    private String mPrivateCode = "";
    private RecyclerView mRecyclerView;
    private DatePopAttandanceAdapter mAdapter;
    private List<PopItem> mData = new ArrayList<>();
    private MonthSelectPopWindow monthSelectPopWindow;
    private List<String> mDateTypeList = new ArrayList<>();
    private HashMap<String, String> mDataTypeMap = new HashMap<>();
    private MyAttandanceResponse myAttandanceResponse = new MyAttandanceResponse();
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
        setData(true, mSelectedMonth, mSelectedDay);
        mPrivateCode = AppConfig.getAppConfig(AppManager.mContext).getPrivateCode();
        doHttp();
    }

    public void setData(final boolean isDay, int month, int selectPos) {
        if (mData != null) {
            mData.clear();
        }
        if (isDay) {
            List<PopItem> date = DateUtils.getAttandenceDate(month, selectPos);
            mData.addAll(date);
        } else {
            creatMonth(selectPos);
        }
//        mRecyclerView.setLayoutManager(new GridLayoutManager(AttandenceMonthActivity.this, isDay ? 7 : 6));
//        mAdapter = new DatePopAttandanceAdapter(mData);
        mAdapter.setOnItemClick((v, position) -> {
            String content = mData.get(position).getContent();
            int i1 = Integer.parseInt(content);
            mSelectedDay = i1;
            if (mSelectedDay < 10) {
                mDay = "0" + mSelectedDay;
            }
            String date = mCurrentYear + "-" + mCurrentMonth + "-" + mDay;

//            doHttp();
//            mPresenter.queryDayAttendance(mPrivateCode, date);
            try {
                MyAttandanceResponse.AllWorkAttendanceListBean allWorkAttendanceListBean = mAllWorkAttendanceList.get(position-2);

                if (!TextUtils.isEmpty(allWorkAttendanceListBean.getCalendar())) {
                    tv_date.setText(allWorkAttendanceListBean.getCalendar());
                }
                if (!TextUtils.isEmpty(allWorkAttendanceListBean.getPsname())) {
                    tv_name.setText(allWorkAttendanceListBean.getPsname());
                }
                if (!TextUtils.isEmpty(allWorkAttendanceListBean.getOnebegintime())) {
                    tv_gowork_time.setText(allWorkAttendanceListBean.getOnebegintime());
                }
                if (!TextUtils.isEmpty(allWorkAttendanceListBean.getTbmstatus())) {
                    mTvState.setText(allWorkAttendanceListBean.getTbmstatus());
                }
                if (!TextUtils.isEmpty(allWorkAttendanceListBean.getTwoendtime())) {
                    tv_off_gowork_time.setText(allWorkAttendanceListBean.getTwoendtime());
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
            setData(true, mSelectedMonth, mSelectedDay);
//            mAdapter.notifyDataSetChanged();
        });
        mRecyclerView.setAdapter(mAdapter);
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

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
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
                                    setData(true, mSelectedMonth, mCurrentDay);
                                } else {
                                    setData(true, mSelectedMonth, 1);
                                }
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
        }
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
    public void uidNull(int code) {

    }


    @Override
    public void sendDataSuccess(MyAttandanceResponse myAttandanceResponse) {
        mAllWorkAttendanceList.clear();
        mAllWorkAttendanceList = myAttandanceResponse.getAllWorkAttendanceList();
        //出差
        List<MyAttandanceResponse.CcWorkAttendanceListBean> ccWorkAttendanceList = myAttandanceResponse.getCcWorkAttendanceList();
        for (MyAttandanceResponse.CcWorkAttendanceListBean item : ccWorkAttendanceList) {
            String key = Integer.parseInt(item.getCalendar().split("-")[2]) + "";
            mDataTypeMap.put(key, getType(item.getTbmstatus()) + "");
        }
        //迟到
        List<MyAttandanceResponse.CdWorkAttendanceListBean> cdWorkAttendanceList = myAttandanceResponse.getCdWorkAttendanceList();
        for (MyAttandanceResponse.CdWorkAttendanceListBean item : cdWorkAttendanceList) {
            String key = Integer.parseInt(item.getCalendar().split("-")[2]) + "";
            mDataTypeMap.put(key, getType(item.getTbmstatus()) + "");
        }
        //加班
        List<MyAttandanceResponse.JbWorkAttendanceListBean> jbWorkAttendanceList = myAttandanceResponse.getJbWorkAttendanceList();
        for (MyAttandanceResponse.JbWorkAttendanceListBean item : jbWorkAttendanceList) {
            String key = Integer.parseInt(item.getCalendar().split("-")[2]) + "";
            mDataTypeMap.put(key, getType(item.getTbmstatus()) + "");
        }
        //旷工
        List<MyAttandanceResponse.KgWorkAttendanceListBean> kgWorkAttendanceList = myAttandanceResponse.getKgWorkAttendanceList();
        for (MyAttandanceResponse.KgWorkAttendanceListBean item : kgWorkAttendanceList) {
            String key = Integer.parseInt(item.getCalendar().split("-")[2]) + "";
            mDataTypeMap.put(key, getType(item.getTbmstatus()) + "");
        }
        //休假
        List<MyAttandanceResponse.XjWorkAttendanceListBean> xjWorkAttendanceList = myAttandanceResponse.getXjWorkAttendanceList();
        for (MyAttandanceResponse.XjWorkAttendanceListBean item : xjWorkAttendanceList) {
            String key = Integer.parseInt(item.getCalendar().split("-")[2]) + "";
            mDataTypeMap.put(key, getType(item.getTbmstatus()) + "");
        }
        //早退
        List<MyAttandanceResponse.ZtWorkAttendanceListBean> ztWorkAttendanceList = myAttandanceResponse.getZtWorkAttendanceList();
        for (MyAttandanceResponse.ZtWorkAttendanceListBean item : ztWorkAttendanceList) {
            String key = Integer.parseInt(item.getCalendar().split("-")[2]) + "";
            mDataTypeMap.put(key, getType(item.getTbmstatus()) + "");
        }
        for (PopItem mDataItrm : mData) {
            String content = mDataItrm.getContent();
            if (mDataItrm.isEnable()) {
                if (mDataTypeMap.containsKey(content)) {
                    mDataItrm.setDateType(Integer.parseInt(mDataTypeMap.get(content)));
                }
            }
        }

        mAdapter.notifyDataSetChanged();
        if (mAllWorkAttendanceList == null)
            return;
        if (mAllWorkAttendanceList.size() > 0) {
            mTvEmptyLayout.setVisibility(View.GONE);
            mLlCurrentdayState.setVisibility(View.VISIBLE);
            MyAttandanceResponse.AllWorkAttendanceListBean allWorkAttendanceListBean = mAllWorkAttendanceList.get(mSelectedDay);

            tv_date.setText(mSelectedYear + "-" + mSelectedMonth + "-" + mSelectedDay);

            if (!TextUtils.isEmpty(allWorkAttendanceListBean.getCalendar())) {
                tv_date.setText(allWorkAttendanceListBean.getCalendar());
            }
            if (!TextUtils.isEmpty(allWorkAttendanceListBean.getPsname())) {
                tv_name.setText(allWorkAttendanceListBean.getPsname());
            }
            if (!TextUtils.isEmpty(allWorkAttendanceListBean.getOnebegintime())) {
                tv_gowork_time.setText(allWorkAttendanceListBean.getOnebegintime());
            }
            if (!TextUtils.isEmpty(allWorkAttendanceListBean.getTbmstatus())) {
                mTvState.setText(allWorkAttendanceListBean.getTbmstatus());
            }
            if (!TextUtils.isEmpty(allWorkAttendanceListBean.getTwoendtime())) {
                tv_off_gowork_time.setText(allWorkAttendanceListBean.getTwoendtime());
            }
        } else {
            mTvEmptyLayout.setText("暂无考勤信息！");
            mTvEmptyLayout.setVisibility(View.VISIBLE);
            mLlCurrentdayState.setVisibility(View.GONE);
        }
    }

//    @Override
//    public void queryDayAttendanceSuccess(List<MyAttendanceResponse> bean) {
//        try {
//            if (bean != null) {
//                if (bean.size() > 0) {
//                    mTvEmptyLayout.setVisibility(View.GONE);
//                    mLlCurrentdayState.setVisibility(View.VISIBLE);
//                } else {
//                    mTvEmptyLayout.setVisibility(View.VISIBLE);
//                    mLlCurrentdayState.setVisibility(View.GONE);
//                    return;
//                }
//                for (int i = 0; i < bean.size(); i++) {
//                    tv_date.setText(bean.get(i).getCalendar());
//                    tv_name.setText(bean.get(i).getPsname());
//                    mTvState.setText(bean.get(i).getTbmstatus());
//                    tv_gowork_time.setText(bean.get(i).getOnebegintime());
//                    tv_off_gowork_time.setText(bean.get(i).getTwoendtime());
//                }
//            } else {
//                mTvEmptyLayout.setText("暂无考勤信息！");
//                mTvEmptyLayout.setVisibility(View.VISIBLE);
//                mLlCurrentdayState.setVisibility(View.GONE);
//            }
//        } catch (Throwable e) {
//            e.printStackTrace();
//        }
//    }

    @Override
    public void sendDataFailed(String errCode, String msg) {

    }

    @Override
    public void sendDataFinish() {

    }

    public void doHttp() {
        mPresenter.sendData(mPrivateCode, mSelectedYear + "", mSelectedMonth + "", AttandenceMonthActivity.this);
    }

    public int getType(String str) {
        if (!TextUtils.isEmpty(str)) {
            switch (str) {
                case "[休假]":
                    return 1;
                case "[加班转调休]":
                    return 2;
                case "[出差]":
                    return 3;
                case "[早退]":
                case "[旷工]":
                case "[迟到]":
                case "[其他]":
                    return 4;
            }
        }
        return 1;
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
