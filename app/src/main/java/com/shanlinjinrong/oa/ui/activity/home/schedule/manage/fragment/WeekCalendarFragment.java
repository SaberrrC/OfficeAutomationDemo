package com.shanlinjinrong.oa.ui.activity.home.schedule.manage.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aigestudio.wheelpicker.WheelPicker;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.common.Constants;
import com.shanlinjinrong.oa.ui.activity.home.schedule.manage.CalendarRedactActivity;
import com.shanlinjinrong.oa.ui.activity.home.schedule.manage.adapter.LeftDateAdapter;
import com.shanlinjinrong.oa.ui.activity.home.schedule.manage.adapter.LookContentAdapter;
import com.shanlinjinrong.oa.ui.activity.home.schedule.manage.adapter.SelectedWeekCalendarAdapter;
import com.shanlinjinrong.oa.ui.activity.home.schedule.manage.adapter.WriteContentAdapter;
import com.shanlinjinrong.oa.ui.activity.home.schedule.manage.bean.CalendarScheduleContentBean;
import com.shanlinjinrong.oa.ui.activity.home.schedule.manage.bean.LeftDateBean;
import com.shanlinjinrong.oa.ui.activity.home.schedule.manage.bean.SelectedWeekCalendarEvent;
import com.shanlinjinrong.oa.ui.activity.home.schedule.manage.bean.UpdateTitleBean;
import com.shanlinjinrong.oa.ui.activity.home.schedule.manage.bean.WeekCalendarBean;
import com.shanlinjinrong.oa.ui.activity.home.schedule.manage.contract.WeekCalendarFragmentContract;
import com.shanlinjinrong.oa.ui.activity.home.schedule.manage.presenter.WeekCalendarFragmentPresenter;
import com.shanlinjinrong.oa.ui.activity.home.schedule.manage.widget.MyPagerSnapHelper;
import com.shanlinjinrong.oa.ui.base.BaseHttpFragment;
import com.shanlinjinrong.oa.utils.CustomDialogUtils;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;


public class WeekCalendarFragment extends BaseHttpFragment<WeekCalendarFragmentPresenter> implements WheelPicker.OnItemSelectedListener, WeekCalendarFragmentContract.View {

    @BindView(R.id.ll_container_layout)
    LinearLayout mLlContainerLayout;
    private View mRootView;

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
    private String                      mSelectedYear1;
    private String                      mSelectedMonth1;
    private String                      mSelectedDay1;
    private int                         mSelectedStartTime;
    private int                         mSelectedEndTime;
    private List<LeftDateBean.DataBean> mPopupData;


    public Date getMondayOfThisWeek(Calendar calendar) {
        int day_of_week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (day_of_week == 0) {
            day_of_week = 7;
        }
        calendar.add(Calendar.DATE, -day_of_week + 1);
        return calendar.getTime();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_week_calendar, container, false);
        ButterKnife.bind(this, mRootView);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        return mRootView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initDta();
    }

    @Override
    protected void lazyLoadData() {
    }

    private void initDta() {
        mWeeks = new String[]{"日", "一", "二", "三", "四", "五", "六"};
        mListDate = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        SimpleDateFormat currentFormat1 = new SimpleDateFormat("yyyy");
        String currentYear = currentFormat1.format(calendar.getTime());
        SimpleDateFormat currentFormat2 = new SimpleDateFormat("MM");
        String currentMonth = currentFormat2.format(calendar.getTime());
        SimpleDateFormat currentFormat3 = new SimpleDateFormat("dd");
        String currentDay = currentFormat3.format(calendar.getTime());
        int lastYear = year - 1;
        calendar.set(lastYear, 0, 1);
        //更新Title
        EventBus.getDefault().post(new UpdateTitleBean(currentYear + "年" + currentMonth + "月" + currentDay + "日", "updateTitle"));
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
//                if (i == 0) {
//                    SparseArray<LeftDateBean.DataBean> dataBean = new SparseArray<>();
//                    LeftDateBean.DataBean dataBean1 = new LeftDateBean.DataBean();
//                    dataBean1.setTitle("测试1");
//                    dataBean1.setContentCount("3");
//                    dataBean1.setDate("1月22日");
//                    dataBean1.setTitle("测试Title1");
//                    dataBean1.setStartTime(15);
//                    dataBean1.setEndTime(17);
//                    dataBean.put(0, dataBean1);
//                    leftDateBean.setData(dataBean);
//
//                    LeftDateBean.DataBean dataBean2 = new LeftDateBean.DataBean();
//                    dataBean2.setTitle("测试2");
//                    dataBean2.setTitle("测试Title2");
//                    dataBean2.setDate("1月22日");
//                    dataBean2.setContentCount("3");
//                    dataBean2.setContent("312312312");
//                    dataBean2.setStartTime(9);
//                    dataBean2.setEndTime(12);
//
//                    dataBean.put(1, dataBean2);
//                    leftDateBean.setData(dataBean);
//
//                    LeftDateBean.DataBean dataBean3 = new LeftDateBean.DataBean();
//                    dataBean3.setTitle("saasdasd");
//                    dataBean3.setContentCount("3");
//                    dataBean.put(2, dataBean3);
//                    leftDateBean.setData(dataBean);
////
////                    LeftDateBean.DataBean dataBean4 = new LeftDateBean.DataBean();
////                    dataBean4.setTitle("saasdasd");
////                    dataBean4.setContentCount("3");
////                    dataBean.put(3, dataBean4);
////                    leftDateBean.setData(dataBean);
//                } else {
//                    SparseArray<LeftDateBean.DataBean> dataBean = new SparseArray<>();
//                    dataBean.put(0, new LeftDateBean.DataBean());
//
//                    leftDateBean.setData(dataBean);
//                }
                SparseArray<LeftDateBean.DataBean> dataBean = new SparseArray<>();
                leftDateBean.setData(dataBean);
                mLlContent.add(leftDateBean);
            }
            e.onComplete();
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                }, Throwable::printStackTrace, () -> {

                    queryCalendar(mIndex, mIndexTitle2);
                    initView();
                });

    }

    private void queryCalendar(int position0, int position1) {
        String startYear = mListDate.get(position0).getYear().get(position1);
        String startMonth = mListDate.get(position0).getMonth().get(position1);
        String startDay = mListDate.get(position0).getDay().get(position1);

        String endYear = mListDate.get(position0).getYear().get(position1);
        String endMonth = mListDate.get(position0).getMonth().get(position1);
        String endDay = mListDate.get(position0).getDay().get(position1);

        String startDate = startYear + "-" + startMonth + "-" + startDay;
        String endDate = endYear + "-" + endMonth + "-" + endDay;

        mPresenter.QueryCalendarSchedule(startDate, endDate);
    }

    private void initView() {
        mSelectedYear1 = mListDate.get(mIndexTitle1).getYear().get(mIndexTitle2);
        mSelectedMonth1 = mListDate.get(mIndexTitle1).getMonth().get(mIndexTitle2);
        mSelectedDay1 = mListDate.get(mIndexTitle1).getDay().get(mIndexTitle2);


        //测量布局的宽高
        mHeight = ScreenUtils.dp2px(getContext(), 57) + ScreenUtils.getStatusHeight(getContext());
        mViewHeight = (ScreenUtils.getScreenHeight(getContext()) - mHeight) / 11;

        //左侧时间展示
        mLeftRecyclerView = new RecyclerView(getContext());
        mDateAdapter = new LeftDateAdapter(mLeftDateList, getContext(), mViewHeight);
        mLeftRecyclerView.setAdapter(mDateAdapter);
        mLeftRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayout.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        mLeftRecyclerView.setLayoutParams(new RecyclerView.LayoutParams(ScreenUtils.dp2px(getContext(), 40), RecyclerView.LayoutParams.MATCH_PARENT));
        mLlContainerLayout.addView(mLeftRecyclerView);

        //边界线
        View view = new View(getContext());
        view.setLayoutParams(new ViewGroup.LayoutParams(1, ViewGroup.LayoutParams.MATCH_PARENT));
        view.setBackground(getResources().getDrawable(R.color.gray_d5d5d5));
        mLlContainerLayout.addView(view);

        //中间内容部分
        mContentRecyclerView = new RecyclerView(getContext());
        mContentRecyclerView.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.MATCH_PARENT));
        mLlContainerLayout.addView(mContentRecyclerView);
        mAdapter = new WriteContentAdapter(mLlContent, getContext(), mViewHeight);

        //中间时间选择List
        mHeaderRecyclerView = new RecyclerView(getContext());
        mSelectedAdapter = new SelectedWeekCalendarAdapter(mListDate, getContext());
        mHeaderRecyclerView.setAdapter(mSelectedAdapter);
        mHeaderRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayout.HORIZONTAL, false));
        mHeaderRecyclerView.setBackgroundColor(getResources().getColor(R.color.gray_99EFEFEF));
        mHeaderRecyclerView.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, mViewHeight));
        mHeaderRecyclerView.scrollToPosition(mIndex);

        mListDate.get(mIndex).getIsSelected().set(mIndexTitle2, true);

        MyPagerSnapHelper mMySnapHelper = new MyPagerSnapHelper();
        mMySnapHelper.attachToRecyclerView(mHeaderRecyclerView);

        mAdapter.addHeaderView(mHeaderRecyclerView);
        mContentRecyclerView.setAdapter(mAdapter);
        mContentRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayout.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        //刷新界面
        mDateAdapter.notifyDataSetChanged();
        mSelectedAdapter.notifyDataSetChanged();
        mAdapter.notifyDataSetChanged();
        CustomDialogUtils.Builder builder = new CustomDialogUtils.Builder(getContext());
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.dialog_date_selected, null);
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
                Toast.makeText(getContext(), "开始时间大于结束时间，请重新选择！", Toast.LENGTH_SHORT).show();
                return;
            } else if (startTime == endTime) {
                Toast.makeText(getContext(), "开始时间不能和结束时间相同，请重新选择！", Toast.LENGTH_SHORT).show();
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
                .heightpx((int) (ScreenUtils.getScreenHeight(getContext()) / 4.2))
                .widthpx((int) (ScreenUtils.getScreenWidth(getContext()) / 1.1))
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
     * 添加内容的点击事件
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ChangeLeftView(SelectedWeekCalendarEvent event) {
        switch (event.getEvent()) {
            case "TopDate":
                mSelectedYear1 = mListDate.get(event.getPosition()).getYear().get(event.getIndex());
                mSelectedMonth1 = mListDate.get(event.getPosition()).getMonth().get(event.getIndex());
                mSelectedDay1 = mListDate.get(event.getPosition()).getDay().get(event.getIndex());

                //查询 选中周 日程
                queryCalendar(event.getPosition(), event.getIndex());

                //更新 Title
                EventBus.getDefault().post(new UpdateTitleBean(mSelectedYear1 + "年" + mSelectedMonth1 + "月" + mSelectedDay1 + "日", "updateTitle"));
                mSelectedAdapter.setNewData(mListDate);
                mSelectedAdapter.notifyDataSetChanged();
                break;
            //查看当前事件
            case "lookEvent":

                break;
            case "changeView":
                RefreshChangeView(event);
                Intent intent = new Intent(getContext(), CalendarRedactActivity.class);
                intent.putExtra(Constants.CALENDARYEAR, mSelectedYear1);
                intent.putExtra(Constants.CALENDARMONTH, mSelectedMonth1);
                intent.putExtra(Constants.CALENDARDATE, mSelectedDay1);
                intent.putExtra(Constants.CALENDARSTARTTIME, mSelectedStartTime);
                intent.putExtra(Constants.CALENDARENDTIME, mSelectedEndTime);
                intent.putExtra(Constants.CALENDARTYPE, Constants.WRITECALENDAR);
                intent.putExtra(Constants.SELECTEDPOSITION, event.getPosition());
                startActivity(intent);
                break;
            //选择某个事件
            case "selectedView":

                break;
            //弹出 事件列表
            case "popupWindow":
                RefreshChangeView(event);
                Dialog dialog = new Dialog(getContext(), R.style.CustomDialog);
                View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_week_calendar_content, null);
                ImageView addCalendar = (ImageView) view.findViewById(R.id.img_add_calendar);
                TextView tvContentCount = (TextView) view.findViewById(R.id.tv_content_count);

                tvContentCount.setText("共" + mLlContent.get(event.getPosition() - 1).getData().size());
                //添加新事件
                addCalendar.setOnClickListener(view1 -> {
                    Intent intent1 = new Intent(getContext(), CalendarRedactActivity.class);
                    intent1.putExtra(Constants.CALENDARYEAR, mSelectedYear1);
                    intent1.putExtra(Constants.CALENDARMONTH, mSelectedMonth1);
                    intent1.putExtra(Constants.CALENDARDATE, mSelectedDay1);
                    intent1.putExtra(Constants.CALENDARSTARTTIME, mSelectedStartTime);
                    intent1.putExtra(Constants.CALENDARENDTIME, mSelectedEndTime);
                    intent1.putExtra(Constants.CALENDARTYPE, Constants.WRITECALENDAR);
                    intent1.putExtra(Constants.SELECTEDPOSITION, event.getPosition());

                    startActivity(intent1);
                });

                //展示可查看的事件
                RecyclerView rvContent = (RecyclerView) view.findViewById(R.id.rv_content);
                SparseArray<LeftDateBean.DataBean> data = mLlContent.get(event.getPosition() - 1).getData();

                mPopupData = new ArrayList<>();
                for (int i = 0; i < data.size(); i++) {
                    mPopupData.add(data.get(i));
                }

                LookContentAdapter adapter = new LookContentAdapter(mPopupData);

                rvContent.setAdapter(adapter);
                rvContent.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayout.VERTICAL, false));
                rvContent.addOnItemTouchListener(new ItemClick());
                adapter.notifyDataSetChanged();

                dialog.setContentView(view);
                //获取到当前Activity的Window
                Window dialog_window = dialog.getWindow();
                WindowManager.LayoutParams dialog_window_attributes = dialog_window.getAttributes();
                //设置宽度
                dialog_window_attributes.width = ScreenUtils.getScreenWidth(getContext()) - ScreenUtils.dp2px(getContext(), 45);
                //设置高度
                int position = (event.getPosition() + 1) * mViewHeight;

                dialog_window_attributes.height = 350;
                dialog_window_attributes.y = (position - (ScreenUtils.getScreenHeight(getContext()) / 2 - (ScreenUtils.getStatusHeight(getContext()) + ScreenUtils.dp2px(getContext(), 56))));

                dialog_window_attributes.x = ScreenUtils.dp2px(getContext(), 19);

                dialog_window.setAttributes(dialog_window_attributes);
                dialog.show();
                break;
            case Constants.SELECTEDTIME:

                String startTime = event.getStartTime();

                for (int i = 0; i < mListDate.size(); i++) {

                    List<String> year = mListDate.get(i).getYear();
                    List<String> month = mListDate.get(i).getMonth();
                    List<String> day = mListDate.get(i).getDay();

                    for (int j = 0; j < year.size(); j++) {
                        mListDate.get(i).getIsSelected().set(j, false);
                        if (startTime.equals(year.get(j) + "年" + month.get(j) + "月" + day.get(j) + "日")) {
                            mHeaderRecyclerView.scrollToPosition(i);


                            mListDate.get(i).getIsSelected().set(j, true);
                            mSelectedAdapter.setNewData(mListDate);
                            mSelectedAdapter.notifyDataSetChanged();
                        }
                    }
                }
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

        mSelectedStartTime = event.getPosition() + 8;
        mSelectedEndTime = event.getPosition() + 9;

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
    public void uidNull(String code) {
        catchWarningByCode(code);
    }

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    public void showLoading() {
        showLoadingView();
    }

    @Override
    public void hideLoading() {
        hideLoadingView();
    }

    @Override
    public void QueryCalendarScheduleSuccess(CalendarScheduleContentBean bean) {
        if (bean.getData() == null) {
            return;
        }

        try {
            for (int i = 0; i < bean.getData().size(); i++) {
                if (bean.getData().get(i).getStartTime().contains("09:00:00")) {
                    CalendarScheduleContentBean.DataBean dataBean = bean.getData().get(i);
                    LeftDateBean.DataBean leftDateBean = new LeftDateBean.DataBean();
                    leftDateBean.setEndTime(dataBean.getEndTime());
                    leftDateBean.setStartTime(dataBean.getStartTime());
                    leftDateBean.setId(dataBean.getId());
                    leftDateBean.setTaskType(dataBean.getTaskType());
                    leftDateBean.setStatus(dataBean.getStatus());
                    leftDateBean.setTaskDetail(dataBean.getTaskDetail());
                    leftDateBean.setTaskTheme(dataBean.getTaskTheme());
                    leftDateBean.setTaskDate(dataBean.getTaskDate());

                    mLlContent.get(0).getData().put(0, leftDateBean);
                    mAdapter.notifyDataSetChanged();

                } else if (bean.getData().get(i).getStartTime().contains("10:00:00")) {

                } else if (bean.getData().get(i).getStartTime().contains("11:00:00")) {

                } else if (bean.getData().get(i).getStartTime().contains("12:00:00")) {

                } else if (bean.getData().get(i).getStartTime().contains("13:00:00")) {

                } else if (bean.getData().get(i).getStartTime().contains("14:00:00")) {

                } else if (bean.getData().get(i).getStartTime().contains("15:00:00")) {

                } else if (bean.getData().get(i).getStartTime().contains("16:00:00")) {

                } else if (bean.getData().get(i).getStartTime().contains("17:00:00")) {

                } else if (bean.getData().get(i).getStartTime().contains("18:00:00")) {

                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }

    @Override
    public void QueryCalendarScheduleFailure(int errorCode, String errorMsg) {

    }

    public class ItemClick extends OnItemClickListener {

        @Override
        public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
            Intent intent = new Intent(getContext(), CalendarRedactActivity.class);
            intent.putExtra(Constants.CALENDARTITLE, mPopupData.get(i).getTaskTheme());
            intent.putExtra(Constants.CALENDARCONTENT, mPopupData.get(i).getTaskDetail());
            intent.putExtra(Constants.CALENDARSTARTTIME, mPopupData.get(i).getStartTime());
            intent.putExtra(Constants.CALENDARENDTIME, mPopupData.get(i).getEndTime());
            intent.putExtra(Constants.CALENDARDATE, mPopupData.get(i).getTaskDate());
            intent.putExtra(Constants.CALENDARID, mPopupData.get(i).getId());
            intent.putExtra(Constants.CALENDARTYPE, mPopupData.get(i).getTaskType());
            intent.putExtra(Constants.CALENDARTYPE, Constants.LOOKCALENDAR);
            intent.putExtra(Constants.CALENDARSTATUS, mPopupData.get(i).getStatus());
            intent.putExtra(Constants.SELECTEDPOSITION, i);
            startActivity(intent);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
