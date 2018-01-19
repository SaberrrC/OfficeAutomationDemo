package com.shanlinjinrong.oa.ui.activity.home.schedule.manage;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.aigestudio.wheelpicker.WheelPicker;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.ui.activity.home.schedule.manage.adapter.LeftDateAdapter;
import com.shanlinjinrong.oa.ui.activity.home.schedule.manage.adapter.SelectedWeekCalendarAdapter;
import com.shanlinjinrong.oa.ui.activity.home.schedule.manage.adapter.WriteContentAdapter;
import com.shanlinjinrong.oa.ui.activity.home.schedule.manage.bean.LeftDateBean;
import com.shanlinjinrong.oa.ui.activity.home.schedule.manage.bean.SelectedWeekCalendarEvent;
import com.shanlinjinrong.oa.ui.activity.home.schedule.manage.bean.WeekCalendarBean;
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
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class ScheduleWeekCalendarActivity extends AppCompatActivity implements ScheduleMonthAdapter.OnItemClick, WheelPicker.OnItemSelectedListener {


    @BindView(R.id.topView)
    CommonTopView mTopView;
    @BindView(R.id.ll_container_layout)
    LinearLayout mLlContainerLayout;
    @BindView(R.id.ed_input_content)
    EditText mEdInputContent;
    @BindView(R.id.ll_voice_content)
    LinearLayout mLlVoiceContent;
    @BindView(R.id.ll_month)
    LinearLayout mDateLayout;

    @BindView(R.id.sv_container_layout)
    ScrollView mSvContainerLayout;


    private int mSelectedDay;
    private int mSelectedMonth;
    private int mCurrentYear;
    private int mCurrentDay;
    private int mCurrentMonth;
    private int mHeight;
    private int mViewHeight;
    private int mViewWidth;
    private List<WeekCalendarBean> mListDate;
    private List<TextView> mTextViews;
    private WriteContentAdapter mAdapter;
    private String mTitleDay;
    private RecyclerView mContentRecyclerView;
    private List<LeftDateBean> mLeftDateList;
    private RecyclerView mLeftRecyclerView;
    private List<LeftDateBean> mLlContent;
    private RecyclerView mHeaderRecyclerView;
    private SelectedWeekCalendarAdapter mSelectedAdapter;
    private boolean isMatchShow = true;
    private View rootView;
    private ScheduleMonthAdapter mDatePopAttandanceAdapter;
    private RecyclerView mRecyclerView;
    private List<PopItem> mData = new ArrayList<>();
    private LeftDateBean.DataBean mContentDataBean;
    private LeftDateAdapter mDateAdapter;
    private int mIndex;
    private CustomDialogUtils mDialog;
    private int mIndexTitle;
    private int mPosition;
    private WheelPicker mRvStartDateSelected;
    private WheelPicker mRvEndDateSelected;
    private List<String> mStartDate;
    private List<String> mEndDate;
    private String mStartTime;
    private String mEndTime;

    public ScheduleWeekCalendarActivity() {
    }


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


    public static Date getThisWeekMonday(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        // 获得当前日期是一个星期的第几天
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
        if (1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        // 获得当前日期是一个星期的第几天
        int day = cal.get(Calendar.DAY_OF_WEEK);
        // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);
        return cal.getTime();
    }

    public Date getMondayOfThisWeek(Calendar calendar) {

        int day_of_week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (day_of_week == 0) {
            day_of_week = 7;
        }
        calendar.add(Calendar.DATE, -day_of_week + 1);
        return calendar.getTime();
    }

    @SuppressLint("SimpleDateFormat")
    private void initData() {

        //得到当前天
        Calendar calendar = Calendar.getInstance();

        mCurrentYear = calendar.get(Calendar.YEAR);
        mCurrentDay = calendar.get(Calendar.DAY_OF_MONTH);
        mCurrentMonth = calendar.get(Calendar.MONTH) + 1;


        Observable.create(e -> {
            String[] weeks = {"日", "一", "二", "三", "四", "五", "六"};
            mListDate = new ArrayList<>();
            int year = calendar.get(Calendar.YEAR);
            SimpleDateFormat currentFormat1 = new SimpleDateFormat("yyyy");
            String currentYear = currentFormat1.format(calendar.getTime());
            SimpleDateFormat currentFormat2 = new SimpleDateFormat("MM");
            String currentMonth = currentFormat2.format(calendar.getTime());
            SimpleDateFormat currentFormat3 = new SimpleDateFormat("dd");
            String currentDay = currentFormat3.format(calendar.getTime());

            int lastYear = year - 1;
            calendar.set(lastYear, 0, 1);
            Date lastDay = getMondayOfThisWeek(calendar);
            SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
            String lastyear = yearFormat.format(lastDay);
            SimpleDateFormat MonthFormat = new SimpleDateFormat("MM");
            String month = MonthFormat.format(lastDay);
            SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
            String lastday = dayFormat.format(lastDay);
            String lastWeek = weeks[calendar.get(Calendar.DAY_OF_WEEK) - 1];
            WeekCalendarBean weekCalendarBean = new WeekCalendarBean(lastWeek, lastday, month, lastyear);
            mListDate.add(weekCalendarBean);
            for (int i = 0; i < 1095; i++) {
                calendar.add(Calendar.DATE, +1);
                SimpleDateFormat nextYearFormat = new SimpleDateFormat("yyyy");
                String nextYear = nextYearFormat.format(calendar.getTime());
                SimpleDateFormat nextMonthFormat = new SimpleDateFormat("MM");
                String nextMonth = nextMonthFormat.format(calendar.getTime());
                SimpleDateFormat nextDayFormat = new SimpleDateFormat("dd");
                String nextDay = nextDayFormat.format(calendar.getTime());
                String nextWeek = weeks[calendar.get(Calendar.DAY_OF_WEEK) - 1];

                if (nextDay.equals(currentDay + "") && nextMonth.equals(currentMonth + "") && nextYear.equals(currentYear + "")) {
                    mIndex = i;
                    mIndexTitle = i;
                }
                WeekCalendarBean nextWeekBean = new WeekCalendarBean(nextWeek, nextDay, nextMonth, nextYear);
                mListDate.add(nextWeekBean);
            }

            int index = mIndex + 1;
            for (int i = 0; i < weeks.length; i++) {
                WeekCalendarBean week = mListDate.get(index - i);
                if ("一".equals(week.getWeek())) {
                    mIndex = index - i;
                }
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

//            Random random = new Random();
//            for (int i = 0; i < mLlContent.size(); i++) {
//                int i1 = random.nextInt(4);
//                SparseArray<LeftDateBean.DataBean> dataBeanSparseArray = new SparseArray<>();
//                for (int j = 0; j < i1; j++) {
//                    mContentDataBean = new LeftDateBean.DataBean();
//                    mContentDataBean.setContentCount("共3");
//                    mContentDataBean.setTitle("设计OB需求");
//                    dataBeanSparseArray.put(j, mContentDataBean);
//                }
//                mLlContent.get(i).setData(dataBeanSparseArray);
//            }
            e.onComplete();
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                }, Throwable::printStackTrace, this::initView);
    }

    private void initView() {

        mTopView.setAppTitle(mListDate.get(mIndexTitle + 1).getYear() + "年" + mListDate.get(mIndexTitle + 1).getMonth() + "月" + mListDate.get(mIndexTitle + 1).getDay() + "日");

        //测量布局的宽高
        mHeight = ScreenUtils.dp2px(this, 57) + ScreenUtils.getStatusHeight(this);
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
        mHeaderRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayout.HORIZONTAL, false) {
            @Override
            public boolean canScrollHorizontally() {
                return false;
            }
        });
        mHeaderRecyclerView.setBackgroundColor(getResources().getColor(R.color.gray_99EFEFEF));
        mHeaderRecyclerView.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, mViewHeight));
        mHeaderRecyclerView.addOnItemTouchListener(new ItemDateClick());
        mHeaderRecyclerView.scrollToPosition(mIndex);
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

        //监听键盘搜索键
        mEdInputContent.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_DONE) {
                if (mDialog.isShowing()) {
                    mDialog.dismiss();
                }
                mDialog.show();
                mDialog.setCanceledOnTouchOutside(true);
            }

            //收起键盘
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(mEdInputContent.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            return true;
        });
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
                if (mLlVoiceContent.getVisibility() == LinearLayout.VISIBLE) {
                    mLlVoiceContent.setVisibility(View.INVISIBLE);
                }
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
        //改变 左侧选中时间段的颜色
        RefreshChangeView(event);
        //现实编辑框
        if (mLlVoiceContent.getVisibility() != LinearLayout.VISIBLE) {
            mLlVoiceContent.setVisibility(View.VISIBLE);
        }

        switch (event.getEvent()) {
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


    @OnClick(R.id.ed_input_content)
    public void onViewClicked() {
        getKeyboardHeight();
    }


    private void getKeyboardHeight() {
        //注册布局变化监听
        getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //判断窗口可见区域大小
                Rect r = new Rect();
                getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
                //如果屏幕高度和Window可见区域高度差值大于整个屏幕高度的1/3，则表示软键盘显示中，否则软键盘为隐藏状态。
                int heightDifference = ScreenUtils.getScreenHeight(ScheduleWeekCalendarActivity.this) - (r.bottom - r.top);
                boolean isKeyboardShowing = heightDifference > ScreenUtils.getScreenHeight(ScheduleWeekCalendarActivity.this) / 3;
                if (isKeyboardShowing) {
                    changeScrollView();
                    //移除布局变化监听
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        getWindow().getDecorView().getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    } else {
                        getWindow().getDecorView().getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    }
                }
            }
        });
    }

    private void changeScrollView() {
        new Handler().postDelayed(() -> {
            //将ScrollView滚动到底
            mSvContainerLayout.fullScroll(View.FOCUS_DOWN);
        }, 100);
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
