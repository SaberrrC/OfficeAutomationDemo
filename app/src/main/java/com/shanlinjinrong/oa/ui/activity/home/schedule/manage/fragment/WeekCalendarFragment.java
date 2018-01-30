package com.shanlinjinrong.oa.ui.activity.home.schedule.manage.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.aigestudio.wheelpicker.WheelPicker;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.common.Constants;
import com.shanlinjinrong.oa.ui.activity.home.schedule.manage.CalendarRedactActivity;
import com.shanlinjinrong.oa.ui.activity.home.schedule.manage.adapter.LeftDateAdapter;
import com.shanlinjinrong.oa.ui.activity.home.schedule.manage.adapter.LookContentAdapter;
import com.shanlinjinrong.oa.ui.activity.home.schedule.manage.adapter.TestAdapter;
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
import com.shanlinjinrong.oa.utils.CalendarUtils;
import com.shanlinjinrong.oa.utils.CustomDialogUtils;
import com.shanlinjinrong.oa.utils.DateUtils;
import com.shanlinjinrong.oa.utils.ScreenUtils;
import com.shanlinjinrong.oa.views.DatePicker;
import com.shanlinjinrong.views.common.CommonTopView;
import com.squareup.haha.perflib.Main;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.joda.time.DateTime;

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


public class WeekCalendarFragment extends BaseHttpFragment<WeekCalendarFragmentPresenter> implements WheelPicker.OnItemSelectedListener, WeekCalendarFragmentContract.View {

    @BindView(R.id.ll_container_layout)
    LinearLayout mLlContainerLayout;
    private View mRootView;

    /**
     * 周历
     */
    private CustomDialogUtils   mDialog;
    private WriteContentAdapter mAdapter;
    private List<LeftDateBean>  mLlContent;
    private LeftDateAdapter     mDateAdapter;
    private List<LeftDateBean>  mLeftDateList;
    private RecyclerView        mLeftRecyclerView;
    private WheelPicker         mRvEndDateSelected;
    private List<String>        mStartDate, mEndDate;
    private String mStartTime, mEndTime;
    private RecyclerView mHeaderRecyclerView;
    private WheelPicker  mRvStartDateSelected;
    private RecyclerView mContentRecyclerView;
    private int          mHeight, mViewHeight;
    private String mSelectedYear1;
    private String mSelectedMonth1;
    private String mSelectedDay1;
    private int    mSelectedStartHour;
    private int    mSelectedEndHour;


    private List<LeftDateBean.DataBean> mPopupData;
    private Dialog                      mPopupDialog;
    private int                         mIntervalWeek;
    private DateTime                    mInitialDateTime;
    private TestAdapter                 mTestAdapter;
    private List<WeekCalendarBean>      mWeekCalendarBeans;
    private String                      mCurrentDay;
    private DatePicker                  picker;
    private String                      mCurrentYear;
    private String                      mCurrentMonth;
    //    private TextView                    mTvErrorView;

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
        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        SimpleDateFormat currentFormat1 = new SimpleDateFormat("yyyy");
        mCurrentYear = currentFormat1.format(calendar.getTime());
        SimpleDateFormat currentFormat2 = new SimpleDateFormat("MM");
        mCurrentMonth = currentFormat2.format(calendar.getTime());
        SimpleDateFormat currentFormat3 = new SimpleDateFormat("dd");
        mCurrentDay = currentFormat3.format(calendar.getTime());
        int lastYear = year - 1;
        calendar.set(lastYear, 0, 1);
        //更新Title
        EventBus.getDefault().post(new UpdateTitleBean(mCurrentYear + "年" + mCurrentMonth + "月" + mCurrentDay + "日", "updateTitle"));
        Observable.create(e -> {
            //时间 数据
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
            for (int i = 0; i < 12; i++) {
                LeftDateBean leftDateBean = new LeftDateBean();
                List<LeftDateBean.DataBean> dataBean = new ArrayList<>();
                leftDateBean.setData(dataBean);
                mLlContent.add(leftDateBean);
            }
            e.onComplete();
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                }, Throwable::printStackTrace, () -> {
                    String initDate = mCurrentYear + mCurrentMonth + mCurrentDay;
                    mSelectedYear1 = mCurrentYear;
                    mSelectedMonth1 = mCurrentMonth;
                    mSelectedDay1 = mCurrentDay;
                    queryCalendar(initDate, initDate);
                    initView();
                });

    }

    private void queryCalendar(String startDate, String endDate) {
        mPresenter.QueryCalendarSchedule(startDate, endDate);
    }

    private void initView() {

        //布局 宽高
        mHeight = ScreenUtils.dp2px(getContext(), 57) + ScreenUtils.getStatusHeight(getContext()) + ScreenUtils.dp2px(getContext(), 85);
        mViewHeight = (ScreenUtils.getScreenHeight(getContext()) - mHeight) / 10;

        //左侧 时间
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

        FrameLayout frameLayout = new FrameLayout(getContext());
        frameLayout.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, ScreenUtils.dp2px(getContext(), 71) - mViewHeight));
        mDateAdapter.addHeaderView(frameLayout);

        //垂直 边界线
        View view = new View(getContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(1, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(0, 0, 0, ScreenUtils.dp2px(getContext(), 12));
        view.setLayoutParams(layoutParams);
        view.setBackground(getResources().getDrawable(R.color.gray_d5d5d5));
        mLlContainerLayout.addView(view);

        //中间 内容
        mContentRecyclerView = new RecyclerView(getContext());
        mContentRecyclerView.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.MATCH_PARENT));
        mLlContainerLayout.addView(mContentRecyclerView);


//        mTvErrorView = new TextView(getContext());
//        mTvErrorView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//        mTvErrorView.setGravity(Gravity.CENTER);
//        mTvErrorView.setTextSize(14);
//        mContentRecyclerView.setVisibility(View.GONE);
//        mLlContainerLayout.addView(mTvErrorView);


        mAdapter = new WriteContentAdapter(mLlContent, getContext(), mViewHeight);

        //中间时间选择List
        mHeaderRecyclerView = new RecyclerView(getContext());

        int year = Integer.parseInt(mCurrentYear);
        int initStartYear = year - 3;
        String initStartDate = initStartYear + "-" + mCurrentMonth + "-" + mCurrentDay;
        int initEndYear = year + 3;
        String initEndDate = initEndYear + "-" + mCurrentMonth + "-" + mCurrentDay;

        mIntervalWeek = CalendarUtils.getIntervalWeek(new DateTime(initStartDate), new DateTime(initEndDate), 1);
        mWeekCalendarBeans = new ArrayList<>();

        for (int i = 0; i < mIntervalWeek; i++) {
            List<Boolean> isSelected = new ArrayList<>();
            for (int j = 0; j < 7; j++) {
                isSelected.add(false);
            }
            WeekCalendarBean weekCalendarBean = new WeekCalendarBean();
            weekCalendarBean.setIsSelected(isSelected);
            mWeekCalendarBeans.add(weekCalendarBean);
        }

        //今天
        mInitialDateTime = new DateTime().withTimeAtStartOfDay();
        int mCurrPage = CalendarUtils.getIntervalWeek(new DateTime(initStartDate), mInitialDateTime, 1);
        mTestAdapter = new TestAdapter(mWeekCalendarBeans, mCurrentDay, mInitialDateTime, mCurrPage);
        mHeaderRecyclerView.setAdapter(mTestAdapter);
        mHeaderRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayout.HORIZONTAL, false));
        mHeaderRecyclerView.setBackgroundColor(getResources().getColor(R.color.gray_99EFEFEF));
        mHeaderRecyclerView.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, ScreenUtils.dp2px(getContext(), 71)));
        mHeaderRecyclerView.scrollToPosition(mCurrPage);

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
        mTestAdapter.notifyDataSetChanged();
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
                String year = event.getDate().replace("年", "-");
                String month = year.replace("月", "-");
                String day = month.replace("日", "");

                mSelectedYear1 = event.getDate().substring(0, 4);
                mSelectedMonth1 = event.getDate().substring(5, 7);
                mSelectedDay1 = event.getDate().substring(8, 10);

                mPresenter.QueryCalendarSchedule(day, day);
                //更新 Title
                EventBus.getDefault().post(new UpdateTitleBean(event.getDate(), "updateTitle"));

                mWeekCalendarBeans.get(event.getPosition()).getIsSelected().set(event.getIndex(), true);
                mTestAdapter.setNewData(mWeekCalendarBeans);
                mTestAdapter.notifyItemChanged(event.getPosition());
                break;
            case "changeView":
                RefreshChangeView(event);
                Intent intent = new Intent(getContext(), CalendarRedactActivity.class);

                intent.putExtra(Constants.CALENDARYEAR, mSelectedYear1);
                intent.putExtra(Constants.CALENDARMONTH, mSelectedMonth1);
                intent.putExtra(Constants.CALENDARDATE, mSelectedDay1);

                intent.putExtra(Constants.CALENDARTYPE, Constants.WRITECALENDAR);
                intent.putExtra(Constants.SELECTEDPOSITION, event.getPosition());

                intent.putExtra(Constants.CALENDARSTARTHOUR, mSelectedStartHour);
                intent.putExtra(Constants.CALENDARENDHOUR, mSelectedEndHour);
                intent.putExtra(Constants.CALENDARSTARTMIN, 0);
                intent.putExtra(Constants.CALENDARENDMIN, 0);
                startActivityForResult(intent, 101);
                break;
            //弹出 事件列表
            case "popupWindow":
                RefreshChangeView(event);
                mPopupDialog = new Dialog(getContext(), R.style.CustomDialog);
                View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_week_calendar_content, null);
                ImageView addCalendar = (ImageView) view.findViewById(R.id.img_add_calendar);
                //TextView tvContentCount = (TextView) view.findViewById(R.id.tv_content_count);
                //tvContentCount.setText("共" + mLlContent.get(event.getPosition() - 1).getData().size());

                //添加新事件
                addCalendar.setOnClickListener(view1 -> {
                    Intent intent1 = new Intent(getContext(), CalendarRedactActivity.class);

                    intent1.putExtra(Constants.CALENDARYEAR, mSelectedYear1);
                    intent1.putExtra(Constants.CALENDARMONTH, mSelectedMonth1);
                    intent1.putExtra(Constants.CALENDARDATE, mSelectedDay1);

                    intent1.putExtra(Constants.CALENDARTYPE, Constants.WRITECALENDAR);
                    intent1.putExtra(Constants.SELECTEDPOSITION, event.getPosition());

                    intent1.putExtra(Constants.CALENDARSTARTHOUR, mSelectedStartHour);
                    intent1.putExtra(Constants.CALENDARENDHOUR, 0);
                    intent1.putExtra(Constants.CALENDARSTARTMIN, mSelectedEndHour);
                    intent1.putExtra(Constants.CALENDARENDMIN, 0);
                    startActivityForResult(intent1, 101);
                    if (mPopupDialog.isShowing()) {
                        mPopupDialog.dismiss();
                    }
                });

                //展示可查看的事件
                RecyclerView rvContent = (RecyclerView) view.findViewById(R.id.rv_content);
                List<LeftDateBean.DataBean> data = mLlContent.get(event.getPosition() - 1).getData();

                mPopupData = new ArrayList<>();
                mPopupData.addAll(data);

                LookContentAdapter adapter = new LookContentAdapter(mPopupData, getContext());

                rvContent.setAdapter(adapter);
                rvContent.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayout.VERTICAL, false));
                rvContent.addOnItemTouchListener(new ItemClick());
                adapter.notifyDataSetChanged();

                mPopupDialog.setContentView(view);
                //获取到当前Activity的Window
                Window dialog_window = mPopupDialog.getWindow();
                WindowManager.LayoutParams dialog_window_attributes = dialog_window.getAttributes();
                //设置宽度
                dialog_window_attributes.width = ScreenUtils.getScreenWidth(getContext()) - ScreenUtils.dp2px(getContext(), 45);
                //设置高度
                int position = (event.getPosition() + 1) * mViewHeight;

                dialog_window_attributes.height = 350;
                dialog_window_attributes.y = (position - (ScreenUtils.getScreenHeight(getContext()) / 2 - (ScreenUtils.getStatusHeight(getContext()) + ScreenUtils.dp2px(getContext(), 71))));

                dialog_window_attributes.x = ScreenUtils.dp2px(getContext(), 19);

                dialog_window.setAttributes(dialog_window_attributes);
                mPopupDialog.show();
                break;

            case "mWeekCalendarFragment":
                showDoneDatePicker(event.getDate());
                break;

            //TODO Title 时间选择
            case Constants.SELECTEDTIME:
                try {
                    if (isResumed()) {
                        Observable.create(e -> {
                            for (int i = 0; i < mWeekCalendarBeans.size(); i++) {
                                for (int j = 0; j < 7; j++) {
                                    mWeekCalendarBeans.get(i).getIsSelected().set(j, false);
                                }
                            }
                            e.onComplete();
                        }).observeOn(AndroidSchedulers.mainThread())
                                .subscribeOn(Schedulers.io())
                                .subscribe(o -> {
                                }, Throwable::printStackTrace, () -> {
                                    String currentTime = event.getStartTime();
                                    String TitleDate = DateUtils.getBiDisplayDateByTimestamp(DateUtils.getTimestampFromString(currentTime, "yyyy-MM-dd"));
                                    EventBus.getDefault().post(new UpdateTitleBean(TitleDate, "updateTitle"));
                                    DateTime StartTime = new DateTime("1901-01-01");
                                    DateTime currentItem = new DateTime(currentTime);
                                    int intervalWeek = CalendarUtils.getIntervalWeek(StartTime, currentItem, 1);
                                    SimpleDateFormat sdf = new SimpleDateFormat("E");
                                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                                    Date parse = format.parse(event.getEndTime());
                                    String week = sdf.format(parse);
                                    switch (week) {
                                        case "周一":
                                            mWeekCalendarBeans.get(intervalWeek).getIsSelected().set(0, true);
                                            break;
                                        case "周二":
                                            mWeekCalendarBeans.get(intervalWeek).getIsSelected().set(1, true);
                                            break;
                                        case "周三":
                                            mWeekCalendarBeans.get(intervalWeek).getIsSelected().set(2, true);
                                            break;
                                        case "周四":
                                            mWeekCalendarBeans.get(intervalWeek).getIsSelected().set(3, true);
                                            break;
                                        case "周五":
                                            mWeekCalendarBeans.get(intervalWeek).getIsSelected().set(4, true);
                                            break;
                                        case "周六":
                                            mWeekCalendarBeans.get(intervalWeek).getIsSelected().set(5, true);
                                            break;
                                        case "周日":
                                            mWeekCalendarBeans.get(intervalWeek).getIsSelected().set(6, true);
                                            break;
                                        default:
                                            mWeekCalendarBeans.get(intervalWeek).getIsSelected().set(0, true);
                                            break;
                                    }
                                    mTestAdapter.setNewData(mWeekCalendarBeans);
                                    mHeaderRecyclerView.scrollToPosition(intervalWeek);
                                    mTestAdapter.notifyItemChanged(intervalWeek);
                                });
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                break;
            //刷新页面
            case "upDateCalendarScheduleSuccess":
                String startDate = mSelectedYear1 + "-" + mSelectedMonth1 + "-" + mSelectedDay1;
                queryCalendar(startDate, startDate);
                break;

            //TODO  日期滑动
            case "slideRecyclerViewPosition":
                String dataStr = event.getDate();

                break;
            default:
                for (int i = 0; i < mWeekCalendarBeans.size(); i++) {
                    for (int j = 0; j < 7; j++) {
                        mWeekCalendarBeans.get(i).getIsSelected().set(j, false);
                    }
                }
                int mCurrPage = CalendarUtils.getIntervalWeek(new DateTime("1970-01-01"), new DateTime(event.getTitleDate()), 1);
                mHeaderRecyclerView.scrollToPosition(mCurrPage);

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

        mSelectedStartHour = event.getPosition() + 8;
        mSelectedEndHour = event.getPosition() + 9;

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


    /**
     * 选择日期底部弹出窗
     * 2018年01月09日  格式
     */
    private void showDoneDatePicker(String dateStr) {
        try {
            if (picker == null) {
                picker = new DatePicker(getActivity(), false);
            }

            if (!TextUtils.isEmpty(dateStr)) {
                dateStr = dateStr + "xxxx";
                int year = Integer.parseInt(dateStr.substring(0, 4));
                int month = Integer.parseInt(dateStr.substring(5, 7));
                int day = Integer.parseInt(dateStr.substring(8, 10));
                picker.setSelectedItem(year, month, day);
            } else {
                picker.setCurrentDate();
            }
            picker.setSubmitText("确认");
            picker.setSubmitTextColor(Color.parseColor("#2d9dff"));
            picker.setTextColor(Color.parseColor("#2d9dff"));
            picker.setOnDatePickListener((DatePicker.OnYearMonthDayPickListener) (year, month, day) -> {
                String mSelectedDay = year + "年" + month + "月" + day + "日";
                EventBus.getDefault().post(new UpdateTitleBean(mSelectedDay, "updateTitle"));
                String startDate = year + "-" + month + "-" + day;

                ScrowTimeView(startDate);

                queryCalendar(startDate, startDate);
            });
            picker.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void ScrowTimeView(String startDate) {
        for (int i = 0; i < mWeekCalendarBeans.size(); i++) {
            for (int j = 0; j < 7; j++) {
                mWeekCalendarBeans.get(i).getIsSelected().set(j, false);
            }
        }

        int mCurrPage = CalendarUtils.getIntervalWeek(new DateTime("1970-01-01"), new DateTime(startDate), 1);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(format.parse(startDate));
        } catch (Throwable e) {
            e.printStackTrace();
        }
        int i = c.get(Calendar.DAY_OF_WEEK);
        if (i == 1) {
            mWeekCalendarBeans.get(mCurrPage).getIsSelected().set(6, true);
        } else {
            mWeekCalendarBeans.get(mCurrPage).getIsSelected().set(i - 2, true);
        }
        mHeaderRecyclerView.scrollToPosition(mCurrPage);
        mTestAdapter.notifyItemChanged(mCurrPage);
    }

    @Override
    public void QueryCalendarScheduleSuccess(CalendarScheduleContentBean bean) {
        if (bean.getData() == null) {
            return;
        }

//        if (mContentRecyclerView.getVisibility() == View.GONE) {
//            mContentRecyclerView.setVisibility(View.VISIBLE);
//            mTvErrorView.setVisibility(View.GONE);
//        }

        try {
            for (int i = 0; i < mLlContent.size(); i++) {
                mLlContent.get(i).getData().clear();
            }
            for (int i = 0; i < bean.getData().size(); i++) {
                String startTime = bean.getData().get(i).getStartTime().substring(11, bean.getData().get(i).getStartTime().length() - 6);
                if (startTime.contains("09")) {
                    CalendarScheduleContentBean.DataBean dataBean = bean.getData().get(i);
                    LeftDateBean.DataBean leftDateBean = new LeftDateBean.DataBean();
                    leftDateBean.setEndTime(dataBean.getEndTime());
                    leftDateBean.setStartTime(dataBean.getStartTime());
                    leftDateBean.setId(dataBean.getId());
                    leftDateBean.setTaskType(dataBean.getTaskType());
                    leftDateBean.setStatus(dataBean.getStatus());
                    leftDateBean.setTaskDetail(dataBean.getTaskDetail());
                    leftDateBean.setAddress(dataBean.getAddress());
                    leftDateBean.setTaskTheme(dataBean.getTaskTheme());
                    leftDateBean.setTaskId(dataBean.getTaskId());
                    leftDateBean.setTaskDate(dataBean.getTaskDate());
                    mLlContent.get(0).getData().add(leftDateBean);
                } else if (startTime.contains("10")) {
                    CalendarScheduleContentBean.DataBean dataBean = bean.getData().get(i);
                    LeftDateBean.DataBean leftDateBean = new LeftDateBean.DataBean();
                    leftDateBean.setEndTime(dataBean.getEndTime());
                    leftDateBean.setStartTime(dataBean.getStartTime());
                    leftDateBean.setId(dataBean.getId());
                    leftDateBean.setTaskType(dataBean.getTaskType());
                    leftDateBean.setStatus(dataBean.getStatus());
                    leftDateBean.setAddress(dataBean.getAddress());
                    leftDateBean.setTaskId(dataBean.getTaskId());
                    leftDateBean.setTaskDetail(dataBean.getTaskDetail());
                    leftDateBean.setTaskTheme(dataBean.getTaskTheme());
                    leftDateBean.setTaskDate(dataBean.getTaskDate());
                    mLlContent.get(1).getData().add(leftDateBean);
                } else if (startTime.contains("11")) {
                    CalendarScheduleContentBean.DataBean dataBean = bean.getData().get(i);
                    LeftDateBean.DataBean leftDateBean = new LeftDateBean.DataBean();
                    leftDateBean.setEndTime(dataBean.getEndTime());
                    leftDateBean.setStartTime(dataBean.getStartTime());
                    leftDateBean.setTaskId(dataBean.getTaskId());
                    leftDateBean.setId(dataBean.getId());
                    leftDateBean.setAddress(dataBean.getAddress());
                    leftDateBean.setTaskType(dataBean.getTaskType());
                    leftDateBean.setStatus(dataBean.getStatus());
                    leftDateBean.setTaskDetail(dataBean.getTaskDetail());
                    leftDateBean.setTaskTheme(dataBean.getTaskTheme());
                    leftDateBean.setTaskDate(dataBean.getTaskDate());
                    mLlContent.get(2).getData().add(leftDateBean);
                } else if (startTime.contains("12")) {
                    CalendarScheduleContentBean.DataBean dataBean = bean.getData().get(i);
                    LeftDateBean.DataBean leftDateBean = new LeftDateBean.DataBean();
                    leftDateBean.setEndTime(dataBean.getEndTime());
                    leftDateBean.setStartTime(dataBean.getStartTime());
                    leftDateBean.setId(dataBean.getId());
                    leftDateBean.setAddress(dataBean.getAddress());
                    leftDateBean.setTaskId(dataBean.getTaskId());
                    leftDateBean.setTaskType(dataBean.getTaskType());
                    leftDateBean.setStatus(dataBean.getStatus());
                    leftDateBean.setTaskDetail(dataBean.getTaskDetail());
                    leftDateBean.setTaskTheme(dataBean.getTaskTheme());
                    leftDateBean.setTaskDate(dataBean.getTaskDate());
                    mLlContent.get(3).getData().add(leftDateBean);
                } else if (startTime.contains("13")) {
                    CalendarScheduleContentBean.DataBean dataBean = bean.getData().get(i);
                    LeftDateBean.DataBean leftDateBean = new LeftDateBean.DataBean();
                    leftDateBean.setEndTime(dataBean.getEndTime());
                    leftDateBean.setStartTime(dataBean.getStartTime());
                    leftDateBean.setId(dataBean.getId());
                    leftDateBean.setAddress(dataBean.getAddress());
                    leftDateBean.setTaskType(dataBean.getTaskType());
                    leftDateBean.setStatus(dataBean.getStatus());
                    leftDateBean.setTaskId(dataBean.getTaskId());
                    leftDateBean.setTaskDetail(dataBean.getTaskDetail());
                    leftDateBean.setTaskTheme(dataBean.getTaskTheme());
                    leftDateBean.setTaskDate(dataBean.getTaskDate());
                    mLlContent.get(4).getData().add(leftDateBean);
                } else if (startTime.contains("14")) {
                    CalendarScheduleContentBean.DataBean dataBean = bean.getData().get(i);
                    LeftDateBean.DataBean leftDateBean = new LeftDateBean.DataBean();
                    leftDateBean.setEndTime(dataBean.getEndTime());
                    leftDateBean.setStartTime(dataBean.getStartTime());
                    leftDateBean.setId(dataBean.getId());
                    leftDateBean.setAddress(dataBean.getAddress());
                    leftDateBean.setTaskType(dataBean.getTaskType());
                    leftDateBean.setStatus(dataBean.getStatus());
                    leftDateBean.setTaskDetail(dataBean.getTaskDetail());
                    leftDateBean.setTaskId(dataBean.getTaskId());
                    leftDateBean.setTaskTheme(dataBean.getTaskTheme());
                    leftDateBean.setTaskDate(dataBean.getTaskDate());
                    mLlContent.get(5).getData().add(leftDateBean);
                } else if (startTime.contains("15")) {
                    CalendarScheduleContentBean.DataBean dataBean = bean.getData().get(i);
                    LeftDateBean.DataBean leftDateBean = new LeftDateBean.DataBean();
                    leftDateBean.setEndTime(dataBean.getEndTime());
                    leftDateBean.setStartTime(dataBean.getStartTime());
                    leftDateBean.setId(dataBean.getId());
                    leftDateBean.setAddress(dataBean.getAddress());
                    leftDateBean.setTaskType(dataBean.getTaskType());
                    leftDateBean.setTaskId(dataBean.getTaskId());
                    leftDateBean.setStatus(dataBean.getStatus());
                    leftDateBean.setTaskDetail(dataBean.getTaskDetail());
                    leftDateBean.setTaskTheme(dataBean.getTaskTheme());
                    leftDateBean.setTaskDate(dataBean.getTaskDate());
                    mLlContent.get(6).getData().add(leftDateBean);
                } else if (startTime.contains("16")) {
                    CalendarScheduleContentBean.DataBean dataBean = bean.getData().get(i);
                    LeftDateBean.DataBean leftDateBean = new LeftDateBean.DataBean();
                    leftDateBean.setEndTime(dataBean.getEndTime());
                    leftDateBean.setStartTime(dataBean.getStartTime());
                    leftDateBean.setTaskId(dataBean.getTaskId());
                    leftDateBean.setId(dataBean.getId());
                    leftDateBean.setTaskType(dataBean.getTaskType());
                    leftDateBean.setAddress(dataBean.getAddress());
                    leftDateBean.setStatus(dataBean.getStatus());
                    leftDateBean.setTaskDetail(dataBean.getTaskDetail());
                    leftDateBean.setTaskTheme(dataBean.getTaskTheme());
                    leftDateBean.setTaskDate(dataBean.getTaskDate());
                    mLlContent.get(7).getData().add(leftDateBean);
                } else if (startTime.contains("17")) {
                    CalendarScheduleContentBean.DataBean dataBean = bean.getData().get(i);
                    LeftDateBean.DataBean leftDateBean = new LeftDateBean.DataBean();
                    leftDateBean.setEndTime(dataBean.getEndTime());
                    leftDateBean.setStartTime(dataBean.getStartTime());
                    leftDateBean.setId(dataBean.getId());
                    leftDateBean.setTaskType(dataBean.getTaskType());
                    leftDateBean.setStatus(dataBean.getStatus());
                    leftDateBean.setTaskDetail(dataBean.getTaskDetail());
                    leftDateBean.setTaskId(dataBean.getTaskId());
                    leftDateBean.setAddress(dataBean.getAddress());
                    leftDateBean.setTaskTheme(dataBean.getTaskTheme());
                    leftDateBean.setTaskDate(dataBean.getTaskDate());
                    mLlContent.get(8).getData().add(leftDateBean);
                } else if (startTime.contains("18")) {
                    CalendarScheduleContentBean.DataBean dataBean = bean.getData().get(i);
                    LeftDateBean.DataBean leftDateBean = new LeftDateBean.DataBean();
                    leftDateBean.setEndTime(dataBean.getEndTime());
                    leftDateBean.setStartTime(dataBean.getStartTime());
                    leftDateBean.setAddress(dataBean.getAddress());
                    leftDateBean.setId(dataBean.getId());
                    leftDateBean.setTaskType(dataBean.getTaskType());
                    leftDateBean.setStatus(dataBean.getStatus());
                    leftDateBean.setTaskId(dataBean.getTaskId());
                    leftDateBean.setTaskDetail(dataBean.getTaskDetail());
                    leftDateBean.setTaskTheme(dataBean.getTaskTheme());
                    leftDateBean.setTaskDate(dataBean.getTaskDate());
                    mLlContent.get(9).getData().add(leftDateBean);
                }
            }
            mAdapter.notifyDataSetChanged();
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }

    @Override
    public void QueryCalendarScheduleFailure(int errorCode, String errorMsg) {
        if ("auth error".equals(errorMsg)) {
            catchWarningByCode(errorMsg);
            return;
        }
        if (errorCode == -1) {
            showToast(getString(R.string.net_no_connection));
            for (int i = 0; i < mLlContent.size(); i++) {
                mLlContent.get(i).getData().clear();
            }
            mAdapter.notifyDataSetChanged();
        }else {
            showToast(errorMsg);
        }

//        mContentRecyclerView.setVisibility(View.GONE);
//        mTvErrorView.setVisibility(View.VISIBLE);
//        mTvErrorView.setText(errorMsg);
    }

    public class ItemClick extends OnItemClickListener {

        @Override
        public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
            Intent intent = new Intent(getContext(), CalendarRedactActivity.class);
            intent.putExtra(Constants.CALENDARTITLE, mPopupData.get(i).getTaskTheme());
            intent.putExtra(Constants.CALENDARCONTENT, mPopupData.get(i).getTaskDetail());
            intent.putExtra(Constants.CALENDARDATE, mPopupData.get(i).getTaskDate());
            intent.putExtra(Constants.CALENDARID, mPopupData.get(i).getId());
            intent.putExtra(Constants.CALENDARTYPE, mPopupData.get(i).getTaskType());
            intent.putExtra(Constants.CALENDARSTATUS, mPopupData.get(i).getStatus());
            intent.putExtra(Constants.SELECTEDTASTID, mPopupData.get(i).getTaskId());
            intent.putExtra(Constants.CALENDARADDRESS, mPopupData.get(i).getAddress());
            intent.putExtra(Constants.SELECTEDPOSITION, i);

            try {
                String startTime = mPopupData.get(i).getStartTime().substring(mPopupData.get(i).getStartTime().length() - 8, mPopupData.get(i).getStartTime().length());
                int startHourExtra = Integer.parseInt(startTime.substring(0, 2));
                int startMinExtra = Integer.parseInt(startTime.substring(3, 5));
                String endTime = mPopupData.get(i).getEndTime().substring(mPopupData.get(i).getEndTime().length() - 8, mPopupData.get(i).getEndTime().length());
                int endHourExtra = Integer.parseInt(endTime.substring(0, 2));
                int endMinExtra = Integer.parseInt(endTime.substring(3, 5));

                intent.putExtra(Constants.CALENDARSTARTHOUR, startHourExtra);
                intent.putExtra(Constants.CALENDARENDHOUR, endHourExtra);

                intent.putExtra(Constants.CALENDARSTARTMIN, startMinExtra);
                intent.putExtra(Constants.CALENDARENDMIN, endMinExtra);
            } catch (Throwable e) {
                e.printStackTrace();
            }

            startActivityForResult(intent, 101);
            if (mPopupDialog.isShowing()) {
                mPopupDialog.dismiss();
            }
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
