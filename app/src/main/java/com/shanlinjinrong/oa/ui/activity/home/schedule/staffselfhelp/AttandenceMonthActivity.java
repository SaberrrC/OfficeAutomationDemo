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

import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.ui.activity.home.schedule.event.PeopeNameEvent;
import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.DatePopWindow;
import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.bean.PopItem;
import com.shanlinjinrong.oa.ui.activity.home.schedule.staffselfhelp.adapter.DatePopAttandanceAdapter;
import com.shanlinjinrong.oa.ui.activity.home.schedule.staffselfhelp.contract.AttandanceMonthContract;
import com.shanlinjinrong.oa.ui.activity.home.schedule.staffselfhelp.presenter.AttandanceMonthPresenter;
import com.shanlinjinrong.oa.ui.base.HttpBaseActivity;
import com.shanlinjinrong.oa.utils.DateUtils;
import com.shanlinjinrong.oa.views.MonthSelectPopWindow;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.qqtheme.framework.picker.DatePicker;

/**
 * Created by Administrator on 2017/10/25 0025.
 */

public class AttandenceMonthActivity extends HttpBaseActivity<AttandanceMonthPresenter> implements AttandanceMonthContract.View,View.OnClickListener {
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.ll_chose_time)
    LinearLayout mLlChoseTime;
    @Bind(R.id.ll_count_people)
    LinearLayout ll_count_people;
    @Bind(R.id.ll_currentday_state)
    LinearLayout mLlCurrentdayState;
    @Bind(R.id.tv_time)
    TextView tv_time;
    @Bind(R.id.ll_month)
    LinearLayout mDateLayout;
    @Bind(R.id.iv_divider)
    ImageView iv_divider;
    @Bind(R.id.tv_people)
    TextView tv_people;
    @Bind(R.id.layout_root)
    LinearLayout mRootView;
    private String[] mMonthArrays = {"1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"};







    private DatePicker picker;
    DatePopWindow datePopWindow;
    private List<PopItem> mData;
    private int mSelectedDay ;
    private int mCurrentDay ;
    private int mCurrentYear;
    private int mCurrentMonth ;
    private int mSelectedMonth;
    private int mSelectedYear ;
    MonthSelectPopWindow monthSelectPopWindow;
    private List<Integer> mDays = new ArrayList<>();
    Calendar cal;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_month);
        ButterKnife.bind(this);
        initToolBar();
        initData();
        setTranslucentStatus(this);
//        mPresenter.searchDayRecorder("sd0","sd");

    }

    RecyclerView mRecyclerView;
    DatePopAttandanceAdapter mAdapter;
    View rootView;

    private void initData() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        cal = Calendar.getInstance();
        mCurrentDay=cal.get(Calendar.DAY_OF_MONTH);
        mCurrentMonth=cal.get(Calendar.MONTH)+1;
        mSelectedDay=mCurrentDay;
        tv_time.setText(mCurrentYear+"年"+mCurrentMonth+"月");
        iv_divider.setVisibility(View.GONE);
        mLlChoseTime.setOnClickListener(this);
        ll_count_people.setOnClickListener(this);
        rootView = View.inflate(AttandenceMonthActivity.this, R.layout.date_select_attandance, null);
        mDateLayout.addView(rootView);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.top_data_list);

        mData = new ArrayList<>();
        mData = DateUtils.getDate(5, 20);
        setData(true,5,2);
    }

    public void setData(final boolean isDay, int month, int selectPos) {
        mData.clear();
        if (isDay) {
            mData = DateUtils.getDate(month, selectPos);
        } else {
            creatMonth(selectPos);
        }
        mRecyclerView.setLayoutManager(new GridLayoutManager(AttandenceMonthActivity.this, isDay ? 7 : 6));
        mAdapter = new DatePopAttandanceAdapter(mData);
        mAdapter.setOnItemClick(new DatePopAttandanceAdapter.OnItemClick() {
            @Override
            public void onItemClicked(View v, int position) {
                int arr = 0;
                for(int i=0;i<mData.size();i++){
                    PopItem popItem = mData.get(i);
                    boolean enable = popItem.isEnable();

                    if(!enable){
                        if(!popItem.getContent().equals("1")){
                            arr=arr+1;
                        }else {
//                            return;
                        }

                    }
                }
                mSelectedDay=position-arr;
                Toast.makeText(AttandenceMonthActivity.this,"当前时间--"+mSelectedDay ,Toast.LENGTH_SHORT).show();
//                setData(true, mSelectedMonth,mSelectedDay);
                mAdapter.notifyDataSetChanged();
            }
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

        switch (v.getId()){
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

                                mSelectedYear=Integer.parseInt(year);
                                mSelectedMonth =Integer.parseInt(month);
                                Toast.makeText(AttandenceMonthActivity.this,"当前月份--"+mSelectedYear+"--"+ mSelectedMonth,Toast.LENGTH_SHORT).show();
                                tv_time.setText(year+"年"+month+"月");
                                if(mSelectedMonth ==mCurrentMonth){
                                    setData(true, mSelectedMonth,mCurrentDay);
                                }else {
                                    setData(true, mSelectedMonth,1);
                                }

                                mAdapter.notifyDataSetChanged();
                                monthSelectPopWindow.dismiss();
                            }
                        });
                monthSelectPopWindow.showAtLocation(mRootView, Gravity.BOTTOM,0,0);
//                if (picker == null) {
//                    picker = new DatePicker(AttandenceMonthActivity.this, DatePicker.YEAR_MONTH);
//                }
//                Calendar cal = Calendar.getInstance();
//                picker.setSelectedItem(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1
//                );
//                picker.setSubmitText("完成");
//                picker.setSubmitTextColor(Color.parseColor("#2d9dff"));
//                picker.setTextColor(Color.parseColor("#2d9dff"));
//                picker.setOnDatePickListener(new DatePicker.OnYearMonthPickListener() {
//                    @Override
//                    public void onDatePicked(String year, String month) {
//
//                    }
//                });
//                picker.show();
                break;
            //统计人员选择
            case R.id.ll_count_people:
                Intent intent = new Intent(AttandenceMonthActivity.this,CountPeopleActivity.class);
                startActivity(intent);
                break;
            //考勤记录
            case R.id.ll_currentday_state:
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
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void uidNull(int code) {

    }

    @Override
    public void sendDataSuccess() {

    }

    @Override
    public void sendDataFailed(int errCode, String msg) {

    }

    @Override
    public void sendDataFinish() {

    }




    private int findDay(int month, int day) {
        int dayOne = 0;
        mDays = DateUtils.getDate(month);
        for (int i = 0; i < mDays.size(); i++) {
            if (mDays.get(i) == 1) {
                dayOne = i;
                break;
            }
        }
        return mDays.get(dayOne + day - 1);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFinish(PeopeNameEvent peopeNameEvent) {
        if(!TextUtils.isEmpty(peopeNameEvent.name)){
            tv_people.setText(peopeNameEvent.name);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }




}
