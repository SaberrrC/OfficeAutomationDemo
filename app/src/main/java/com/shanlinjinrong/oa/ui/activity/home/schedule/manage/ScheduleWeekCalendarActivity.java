package com.shanlinjinrong.oa.ui.activity.home.schedule.manage;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.aigestudio.wheelpicker.WheelPicker;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.ui.activity.home.schedule.manage.adapter.LeftDateAdapter;
import com.shanlinjinrong.oa.ui.activity.home.schedule.manage.adapter.SelectedWeekCalendarAdapter;
import com.shanlinjinrong.oa.ui.activity.home.schedule.manage.adapter.WriteContentAdapter;
import com.shanlinjinrong.oa.ui.activity.home.schedule.manage.bean.LeftDateBean;
import com.shanlinjinrong.oa.ui.activity.home.schedule.manage.bean.SelectedWeekCalendarEvent;
import com.shanlinjinrong.oa.ui.activity.home.schedule.manage.bean.WeekCalendarBean;
import com.shanlinjinrong.oa.ui.activity.home.schedule.manage.widget.MyPagerSnapHelper;
import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.bean.PopItem;
import com.shanlinjinrong.oa.ui.activity.home.schedule.staffselfhelp.adapter.ScheduleMonthAdapter;
import com.shanlinjinrong.oa.utils.CustomDialogUtils;
import com.shanlinjinrong.oa.utils.DateUtils;
import com.shanlinjinrong.oa.utils.ScreenUtils;
import com.shanlinjinrong.views.common.CommonTopView;
import com.shanlinjinrong.views.listview.decoration.GridItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 周历 月历
 */
public class ScheduleWeekCalendarActivity extends AppCompatActivity implements WheelPicker.OnItemSelectedListener, ScheduleMonthAdapter.OnItemClick {

    @BindView(R.id.topView)
    CommonTopView mTopView;
    @BindView(R.id.ll_container_layout)
    LinearLayout  mLlContainerLayout;
    @BindView(R.id.progress_bar)
    ProgressBar   mProgressBar;
    @BindView(R.id.ll_month)
    LinearLayout  mDateLayout;

    /**
     * 月历
     */
    private View rootView;
    private boolean isMatchShow = true;
    private ScheduleMonthAdapter mDatePopAttandanceAdapter;
    private RecyclerView         mRecyclerView;
    private List<PopItem> mData = new ArrayList<>();
    private int mSelectedDay, mCurrentYear, mCurrentMonth, mCurrentDay, mSelectedMonth;

    /**
     * 周历
     */
    private String[]                    mWeeks;
    private CustomDialogUtils           mDialog;
    private WriteContentAdapter         mAdapter;
    private List<WeekCalendarBean>      mListDate;
    private List<LeftDateBean>          mLlContent;
    private LeftDateAdapter             mDateAdapter;
    private List<LeftDateBean>          mLeftDateList;
    private SelectedWeekCalendarAdapter mSelectedAdapter;
    private RecyclerView                mLeftRecyclerView;
    private WheelPicker                 mRvEndDateSelected;
    private List<String>                mStartDate, mEndDate;
    private String mStartTime, mEndTime;
    private RecyclerView mHeaderRecyclerView;
    private WheelPicker  mRvStartDateSelected;
    private RecyclerView mContentRecyclerView;
    private int          mHeight, mIndex, mIndexTitle1, mIndexTitle2, mPosition, mViewHeight;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_week_calendar);
        ButterKnife.bind(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        initData();
    }

    public Date getMondayOfThisWeek(Calendar calendar) {

        int day_of_week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (day_of_week == 0) {
            day_of_week = 7;
        }
        calendar.add(Calendar.DATE, -day_of_week + 1);
        return calendar.getTime();
    }

    @SuppressWarnings("SpellCheckingInspection")
    private void initData() {
        mWeeks = new String[]{"日", "一", "二", "三", "四", "五", "六"};
        mListDate = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();

        mCurrentYear = calendar.get(Calendar.YEAR);
        mCurrentDay = calendar.get(Calendar.DAY_OF_MONTH);
        mCurrentMonth = calendar.get(Calendar.MONTH) + 1;

        int year = calendar.get(Calendar.YEAR);
        SimpleDateFormat currentFormat1 = new SimpleDateFormat("yyyy");
        String currentYear = currentFormat1.format(calendar.getTime());
        SimpleDateFormat currentFormat2 = new SimpleDateFormat("MM");
        String currentMonth = currentFormat2.format(calendar.getTime());
        SimpleDateFormat currentFormat3 = new SimpleDateFormat("dd");
        String currentDay = currentFormat3.format(calendar.getTime());
        int lastYear = year - 1;
        calendar.set(lastYear, 0, 1);

        mTopView.setAppTitle(currentYear + "年" + currentMonth + "月");
        Observable.create(e -> {
            for (int i = 0; i < 156; i++) {
                List<String> days = new ArrayList<>();
                List<String> weeks = new ArrayList<>();
                List<String> months = new ArrayList<>();
                List<String> years = new ArrayList<>();
                List<Boolean> booleans = new ArrayList<>();
                for (int j = 0; j < 7; j++) {
                    if (i == 0 && j == 0) {
                        Date lastDay = getMondayOfThisWeek(calendar);
                        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
                        String lastyear = yearFormat.format(lastDay);
                        SimpleDateFormat MonthFormat = new SimpleDateFormat("MM");
                        String lastmonth = MonthFormat.format(lastDay);
                        SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
                        String lastday = dayFormat.format(lastDay);
                        String lastWeek = mWeeks[calendar.get(Calendar.DAY_OF_WEEK) - 1];
                        days.add(lastday);
                        weeks.add(lastWeek);
                        months.add(lastmonth);
                        years.add(lastyear);
                        booleans.add(false);
                        continue;
                    }
                    calendar.add(Calendar.DATE, +1);
                    SimpleDateFormat nextYearFormat = new SimpleDateFormat("yyyy");
                    String nextYear = nextYearFormat.format(calendar.getTime());
                    SimpleDateFormat nextMonthFormat = new SimpleDateFormat("MM");
                    String nextMonth = nextMonthFormat.format(calendar.getTime());
                    SimpleDateFormat nextDayFormat = new SimpleDateFormat("dd");
                    String nextDay = nextDayFormat.format(calendar.getTime());
                    String nextWeek = mWeeks[calendar.get(Calendar.DAY_OF_WEEK) - 1];

                    days.add(nextDay);
                    weeks.add(nextWeek);
                    months.add(nextMonth);
                    years.add(nextYear);
                    booleans.add(false);
                    if (nextDay.equals(currentDay + "") && nextMonth.equals(currentMonth + "") && nextYear.equals(currentYear + "")) {
                        mIndex = i;
                        mIndexTitle1 = i;
                        mIndexTitle2 = j;
                    }
                }
                mListDate.add(new WeekCalendarBean(weeks, days, months, years, booleans));
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
            e.onComplete();
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                }, Throwable::printStackTrace, this::initView);
    }

    private void initView() {
        mProgressBar.setVisibility(View.GONE);
        mLlContainerLayout.setVisibility(View.VISIBLE);
        mTopView.setAppTitle(mListDate.get(mIndexTitle1).getYear().get(mIndexTitle2) + "年" + mListDate.get(mIndexTitle1).getMonth().get(mIndexTitle2) + "月");

        //测量布局的宽高
        mHeight = ScreenUtils.dp2px(this, 57) + ScreenUtils.getStatusHeight(this);
        mViewHeight = (ScreenUtils.getScreenHeight(this) - mHeight) / 11;

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
        mSelectedAdapter = new SelectedWeekCalendarAdapter(mListDate, this);
        mHeaderRecyclerView.setAdapter(mSelectedAdapter);
        mHeaderRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayout.HORIZONTAL, false));
        mHeaderRecyclerView.setBackgroundColor(getResources().getColor(R.color.gray_99EFEFEF));
        mHeaderRecyclerView.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, mViewHeight));
        mHeaderRecyclerView.scrollToPosition(mIndex);

        MyPagerSnapHelper mMySnapHelper = new MyPagerSnapHelper();
        mMySnapHelper.attachToRecyclerView(mHeaderRecyclerView);

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
        initMonthSchedule();
        CustomDialogUtils.Builder builder = new CustomDialogUtils.Builder(this);
        View inflate = LayoutInflater.from(this).inflate(R.layout.dialog_date_selected, null);
        mRvStartDateSelected = (WheelPicker) inflate.findViewById(R.id.rv_start_date_selected);
        mRvEndDateSelected = (WheelPicker) inflate.findViewById(R.id.rv_end_date_selected);
        WheelPicker rvEmptyWheelPicker = (WheelPicker) inflate.findViewById(R.id.empty_wheel_picker);

        CommonTopView title = (CommonTopView) inflate.findViewById(R.id.top_view);
        title.getLeftView().setOnClickListener(view1 -> {
            if (mDialog.isShowing()) {
                mDialog.dismiss();
            }
        });

        title.getRightView().setOnClickListener(view12 -> {
            int startTime = Integer.parseInt(mStartTime.substring(0, 2));
            int endTime = Integer.parseInt(mEndTime.substring(0, 2));
            if (startTime > endTime) {
                Toast.makeText(ScheduleWeekCalendarActivity.this, "开始时间大于结束时间，请重新选择！", Toast.LENGTH_SHORT).show();
                return;
            } else if (startTime == endTime) {
                Toast.makeText(ScheduleWeekCalendarActivity.this, "开始时间不能和结束时间相同，请重新选择！", Toast.LENGTH_SHORT).show();
                return;
            } else {
                //TODO 成功事件处理
                try {
                    if (mDialog.isShowing()) {
                        mDialog.dismiss();
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });

        mDialog = builder.cancelTouchout(false)
                .view(inflate)
                .heightpx((int) (ScreenUtils.getScreenHeight(ScheduleWeekCalendarActivity.this) / 4.2))
                .widthpx((int) (ScreenUtils.getScreenWidth(ScheduleWeekCalendarActivity.this) / 1.1))
                .style(R.style.dialog)
                .build();

        mStartDate = new ArrayList<>();
        mEndDate = new ArrayList<>();
        List<String> emptyDate = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            mStartDate.add(i + 9 + ":" + "00");
            mEndDate.add(i + 10 + ":" + "00");
        }

        mRvStartDateSelected.setData(mStartDate);
        mRvEndDateSelected.setData(mEndDate);
        rvEmptyWheelPicker.setData(emptyDate);

        mRvStartDateSelected.setOnItemSelectedListener(this);
        mRvEndDateSelected.setOnItemSelectedListener(this);
    }

    /***
     * 月历
     */
    public void initMonthSchedule() {
        rootView = View.inflate(ScheduleWeekCalendarActivity.this, R.layout.date_select_schedulemonth, null);
        mDateLayout.addView(rootView);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.top_data_list);

        mTopView.getRightView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isMatchShow) {
                    isMatchShow = false;
                    mLlContainerLayout.setVisibility(View.GONE);
                    mDateLayout.setVisibility(View.VISIBLE);
                } else {
                    isMatchShow = true;
                    mLlContainerLayout.setVisibility(View.VISIBLE);
                    mDateLayout.setVisibility(View.GONE);
                }

            }
        });
        if (mData != null) {
            mData.clear();
        }

        List<PopItem> date = DateUtils.getAttandenceDate(mCurrentMonth, mCurrentDay);
        mData.addAll(date);
        mRecyclerView.addItemDecoration(new GridItemDecoration(this, R.drawable.divider));
        mRecyclerView.setLayoutManager(new GridLayoutManager(ScheduleWeekCalendarActivity.this, 7));
        mDatePopAttandanceAdapter = new ScheduleMonthAdapter(mData);
        mDatePopAttandanceAdapter.setOnItemClick(this);
        mRecyclerView.setAdapter(mDatePopAttandanceAdapter);
    }


    /**
     * 添加内容的点击事件
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ChangeLeftView(SelectedWeekCalendarEvent event) {
        switch (event.getEvent()) {
            case "addData":
                for (int i = mListDate.size(); i < mListDate.size() + 20; i++) {
                    List<String> day = new ArrayList<>();
                    List<String> week = new ArrayList<>();
                    List<String> month = new ArrayList<>();
                    List<String> year = new ArrayList<>();
                    List<Boolean> booleans = new ArrayList<>();
                    for (int j = 0; j < 7; j++) {
                        day.add(j + "");
                        week.add(mWeeks[j] + "");
                        booleans.add(false);
                    }
                    mListDate.add(new WeekCalendarBean(week, day, month, year, booleans));
                }

                mSelectedAdapter.setNewData(mListDate);
                mSelectedAdapter.notifyDataSetChanged();

                break;
            case "TopDate":
                Toast.makeText(this, mListDate.get(event.getPosition()).getYear().get(event.getIndex()) + "年"
                        + mListDate.get(event.getPosition()).getMonth().get(event.getIndex()) + "月"
                        + mListDate.get(event.getPosition()).getDay().get(event.getIndex()) + "日", Toast.LENGTH_SHORT).show();
                mTopView.setAppTitle(mListDate.get(event.getPosition()).getYear().get(event.getIndex()) + "年" + mListDate.get(event.getPosition()).getMonth().get(event.getIndex()) + "月");
                mSelectedAdapter.setNewData(mListDate);
                mSelectedAdapter.notifyDataSetChanged();
                break;
            //查看当前事件
            case "lookEvent":
                break;
            //选择某个事件
            case "selectedView":
                break;
            //弹出 事件列表
            case "popupWindow":
                Dialog dialog = new Dialog(this, R.style.CustomDialog);
                dialog.setContentView(R.layout.dialog_week_calendar_content);
                //获取到当前Activity的Window
                Window dialog_window = dialog.getWindow();
                WindowManager.LayoutParams dialog_window_attributes = dialog_window.getAttributes();
                //设置宽度
                dialog_window_attributes.width = ScreenUtils.getScreenWidth(this) - ScreenUtils.dp2px(this, 45);
                //设置高度
                dialog_window_attributes.height = 400;
                dialog_window_attributes.x = ScreenUtils.dp2px(this, 19);

                //TODO 高度存在问题
                int position = (event.getPosition() + 2) * mViewHeight;
                //dialog_window_attributes.y = ((int) event.getRowY() - ScreenUtils.getStatusHeight(this)) - ScreenUtils.getScreenHeight(this) / 2;
                dialog_window_attributes.y = (position - ScreenUtils.getStatusHeight(this)) - ScreenUtils.getScreenHeight(this) / 2;
                dialog_window.setAttributes(dialog_window_attributes);
                dialog.show();
                break;
            default:
                mPosition = event.getPosition();
                mRvStartDateSelected.setSelectedItemPosition(mPosition - 1);
                mRvEndDateSelected.setSelectedItemPosition(mPosition - 1);
                mStartTime = mStartDate.get(mPosition - 1);
                mEndTime = mEndDate.get(mPosition - 1);
                RefreshChangeView(event);
                break;
        }
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
        mDatePopAttandanceAdapter.notifyDataSetChanged();
        String content = mData.get(position).getContent();
        mSelectedDay = Integer.parseInt(content);
        String mDay = (mSelectedDay < 10) ? "0" + mSelectedDay : mSelectedDay + "";
        String month = (mSelectedMonth < 10) ? "0" + mSelectedMonth : mSelectedMonth + "";
        String date = mCurrentYear + "-" + month + "-" + mDay;
        Toast.makeText(this, position + 1 + "", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onItemSelected(WheelPicker picker, Object data, int position) {
        switch (picker.getId()) {
            case R.id.rv_start_date_selected:
                mStartTime = mStartDate.get(position);
                break;
            case R.id.rv_end_date_selected:
                mEndTime = mEndDate.get(position);
                break;
            default:
                break;
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
