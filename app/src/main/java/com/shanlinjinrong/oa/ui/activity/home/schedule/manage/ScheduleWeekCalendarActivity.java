package com.shanlinjinrong.oa.ui.activity.home.schedule.manage;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.ui.activity.home.schedule.manage.adapter.LeftDateAdapter;
import com.shanlinjinrong.oa.ui.activity.home.schedule.manage.adapter.SelectedWeekCalendarAdapter;
import com.shanlinjinrong.oa.ui.activity.home.schedule.manage.adapter.WriteContentAdapter;
import com.shanlinjinrong.oa.ui.activity.home.schedule.manage.bean.LeftDateBean;
import com.shanlinjinrong.oa.ui.activity.home.schedule.manage.bean.SelectedWeekCalendarEvent;
import com.shanlinjinrong.oa.ui.activity.home.schedule.manage.bean.WeekCalendarBean;
import com.shanlinjinrong.oa.utils.ScreenUtils;
import com.shanlinjinrong.views.common.CommonTopView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ScheduleWeekCalendarActivity extends AppCompatActivity {


    @BindView(R.id.topView)
    CommonTopView mTopView;
    @BindView(R.id.ll_container_layout)
    LinearLayout  mLlContainerLayout;
    @BindView(R.id.ed_input_content)
    EditText      mEdInputContent;
    @BindView(R.id.ll_voice_content)
    LinearLayout  mLlVoiceContent;

    private int                         mHeight;
    private int                         mViewHeight;
    private int                         mViewWidth;
    private List<WeekCalendarBean>      mListDate;
    private List<TextView>              mTextViews;
    private WriteContentAdapter         mAdapter;
    private String                      mTitleDay;
    private RecyclerView                mContentRecyclerView;
    private List<LeftDateBean>          mLeftDateList;
    private RecyclerView                mLeftRecyclerView;
    private List<LeftDateBean>          mLlContent;
    private RecyclerView                mHeaderRecyclerView;
    private SelectedWeekCalendarAdapter mSelectedAdapter;
    private LeftDateBean.DataBean       mContentDataBean;
    private LeftDateAdapter             mDateAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_week_calendar);
        ButterKnife.bind(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        initData();
        initView();
    }


    @SuppressLint("SimpleDateFormat")
    private void initData() {
        String[] weeks = {"日", "一", "二", "三", "四", "五", "六"};

        mListDate = new ArrayList<>();
        Date date = new Date();

        //得到当前天
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        WeekCalendarBean weekCalendarBean = new WeekCalendarBean();
        calendar.add(Calendar.DAY_OF_MONTH, 0);
        Date time = calendar.getTime();
        calendar.setTime(time);
        String week = weeks[calendar.get(Calendar.DAY_OF_WEEK) - 1];
        SimpleDateFormat format = new SimpleDateFormat("dd");
        String day = format.format(time);

        //Title 展示天
        SimpleDateFormat format1 = new SimpleDateFormat("MM月dd日");
        mTitleDay = format1.format(time);

        weekCalendarBean.setDay(day);
        weekCalendarBean.setWeek(week);
        mListDate.add(weekCalendarBean);
        mTextViews = new ArrayList<>();
        //前几天
        for (int i = 1; i < 5; i++) {
            weekCalendarBean = new WeekCalendarBean();
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            time = calendar.getTime();
            calendar.setTime(time);
            week = weeks[calendar.get(Calendar.DAY_OF_WEEK) - 1];
            format = new SimpleDateFormat("dd");
            day = format.format(time);
            weekCalendarBean.setDay(day);
            weekCalendarBean.setWeek(week);
            mListDate.add(0, weekCalendarBean);
        }

        //后几天
        for (int i = 1; i < 5; i++) {
            weekCalendarBean = new WeekCalendarBean();
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            time = calendar.getTime();
            calendar.setTime(time);
            week = weeks[calendar.get(Calendar.DAY_OF_WEEK) - 1];
            format = new SimpleDateFormat("dd");
            day = format.format(time);
            weekCalendarBean.setDay(day);
            weekCalendarBean.setWeek(week);
            mListDate.add(weekCalendarBean);
        }

        //左侧时间的数据
        mLeftDateList = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            LeftDateBean leftDateBean = new LeftDateBean();
            if (i == 0 || i == 11) {
                leftDateBean.setItemType(1);
            } else {
                leftDateBean.setItemType(0);
            }
            leftDateBean.setDate(i + 8 + "点");
            leftDateBean.setPosition(-1);
            mLeftDateList.add(leftDateBean);
        }

        //中间站位数据
        mLlContent = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            LeftDateBean leftDateBean = new LeftDateBean();
            mLlContent.add(leftDateBean);
        }


        Random random = new Random();
        for (int i = 0; i < mLlContent.size(); i++) {
            int i1 = random.nextInt(4);
            SparseArray<LeftDateBean.DataBean> dataBeanSparseArray = new SparseArray<>();
            for (int j = 0; j < i1; j++) {
                mContentDataBean = new LeftDateBean.DataBean();
                mContentDataBean.setContentCount("共3");
                mContentDataBean.setTitle("设计OB需求");
                dataBeanSparseArray.put(j, mContentDataBean);
            }
            mLlContent.get(i).setData(dataBeanSparseArray);
        }

    }

    private void initView() {

        mTopView.setAppTitle(mTitleDay);

        //测量布局的宽高
        mHeight = ScreenUtils.dp2px(this, 101) + ScreenUtils.getStatusHeight(this);
        mViewHeight = (ScreenUtils.getScreenHeight(this) - mHeight) / 11;
        mViewWidth = (ScreenUtils.getScreenWidth(this) - ScreenUtils.dp2px(this, 41)) / 7;

        //左侧时间展示
        mLeftRecyclerView = new RecyclerView(this);
        mDateAdapter = new LeftDateAdapter(mLeftDateList, this, mViewHeight);
        mLeftRecyclerView.setAdapter(mDateAdapter);
        mLeftRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayout.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        mLeftRecyclerView.setLayoutParams(new RecyclerView.LayoutParams(ScreenUtils.dp2px(this, 40), RecyclerView.LayoutParams.MATCH_PARENT));
        mLlContainerLayout.addView(mLeftRecyclerView);

        //边界线
        View view = new View(this);
        view.setLayoutParams(new ViewGroup.LayoutParams(1, ViewGroup.LayoutParams.MATCH_PARENT));
        view.setBackground(getResources().getDrawable(R.color.gray_d5d5d5));
        mLlContainerLayout.addView(view);

        //中间内容部分
        mContentRecyclerView = new RecyclerView(this);
        mContentRecyclerView.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.MATCH_PARENT));
        mLlContainerLayout.addView(mContentRecyclerView);
        mAdapter = new WriteContentAdapter(mLlContent, this, mViewHeight);

        //中间时间选择List
        mHeaderRecyclerView = new RecyclerView(this);
        mSelectedAdapter = new SelectedWeekCalendarAdapter(mListDate, mViewWidth);
        mHeaderRecyclerView.setAdapter(mSelectedAdapter);
        mHeaderRecyclerView.setBackgroundColor(getResources().getColor(R.color.gray_99EFEFEF));
        mHeaderRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayout.HORIZONTAL, false));
        mHeaderRecyclerView.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, mViewHeight));
        mHeaderRecyclerView.addOnItemTouchListener(new ItemDateClick());
        mAdapter.addHeaderView(mHeaderRecyclerView);
        mContentRecyclerView.setAdapter(mAdapter);
        mContentRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayout.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        //刷新界面
        mDateAdapter.notifyDataSetChanged();
        mSelectedAdapter.notifyDataSetChanged();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 添加内容的点击事件
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ChangeLeftView(SelectedWeekCalendarEvent event) {
        //改变 左侧选中时间段的颜色
        RefreshChangeView(event);

        switch (event.getEvent()) {
            //查看当前事件
            case "lookEvent":
                break;
            //选择某个事件
            case "selectedView":
                break;
            //弹出 事件列表
            case "popupWindow":
                break;
            default:
                RefreshChangeView(event);
                break;
        }
    }

    /**
     * 选择时间的点击事件
     */
    class ItemDateClick extends OnItemClickListener {
        @Override
        public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
            Toast.makeText(ScheduleWeekCalendarActivity.this, mListDate.get(i).getDay(), Toast.LENGTH_LONG).show();

            Random random = new Random();
            for (int x = 0; x < mLlContent.size(); x++) {
                int i1 = random.nextInt(4);
                SparseArray<LeftDateBean.DataBean> dataBeanSparseArray = new SparseArray<>();
                for (int j = 0; j < i1; j++) {
                    mContentDataBean = new LeftDateBean.DataBean();
                    mContentDataBean.setContentCount("共3");
                    mContentDataBean.setTitle("设计OB需求");
                    dataBeanSparseArray.put(j, mContentDataBean);
                }
                mLlContent.get(x).setData(dataBeanSparseArray);
            }

            mAdapter.setNewData(mLlContent);
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 刷新选中时间颜色
     */
    private void RefreshChangeView(SelectedWeekCalendarEvent event) {
        for (int i = 0; i < mLeftDateList.size(); i++) {
            mLeftDateList.get(i).setPosition(-1);
        }

        for (int i = 0; i < mLlContent.size(); i++) {
            mLlContent.get(i).setSelected(false);
        }

        if (event.getPosition() == 10) {
            mLeftDateList.get(event.getPosition()).setPosition(event.getPosition());
        } else {
            mLeftDateList.get(event.getPosition()).setPosition(event.getPosition());
            mLeftDateList.get(event.getPosition() + 1).setPosition(event.getPosition());
        }
        mDateAdapter.setNewData(mLeftDateList);
        mAdapter.notifyItemChanged(event.getPosition());
        mAdapter.notifyItemChanged(event.getPosition() + 1);

        mLlContent.get(event.getPosition() - 1).setSelected(true);
        mAdapter.setNewData(mLlContent);
        mAdapter.notifyItemChanged(event.getPosition() - 1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
