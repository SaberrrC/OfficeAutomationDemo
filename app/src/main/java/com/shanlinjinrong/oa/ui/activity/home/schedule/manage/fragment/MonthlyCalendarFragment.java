package com.shanlinjinrong.oa.ui.activity.home.schedule.manage.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.common.Constants;
import com.shanlinjinrong.oa.ui.activity.home.schedule.manage.CalendarRedactActivity;
import com.shanlinjinrong.oa.ui.activity.home.schedule.manage.adapter.MonthContentAdapter;
import com.shanlinjinrong.oa.ui.activity.home.schedule.manage.bean.CalendarScheduleContentBean;
import com.shanlinjinrong.oa.ui.activity.home.schedule.manage.bean.SelectedWeekCalendarEvent;
import com.shanlinjinrong.oa.ui.activity.home.schedule.manage.bean.UpdateTitleBean;
import com.shanlinjinrong.oa.ui.activity.home.schedule.manage.contract.MonthlyCalendarFragmentContract;
import com.shanlinjinrong.oa.ui.activity.home.schedule.manage.presenter.MonthlyCalendarFragmentPresenter;
import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.bean.MonthlyCalenderPopItem;
import com.shanlinjinrong.oa.ui.activity.home.schedule.staffselfhelp.adapter.ScheduleMonthAdapter;
import com.shanlinjinrong.oa.ui.base.BaseHttpFragment;
import com.shanlinjinrong.oa.utils.DateUtils;
import com.shanlinjinrong.oa.utils.ScreenUtils;
import com.shanlinjinrong.oa.views.MonthCalenderMonthSelectPopWindow;
import com.shanlinjinrong.views.listview.decoration.GridItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Observable;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MonthlyCalendarFragment extends BaseHttpFragment<MonthlyCalendarFragmentPresenter> implements MonthlyCalendarFragmentContract.View, ScheduleMonthAdapter.OnItemClick {

    @BindView(R.id.top_data_list)
    RecyclerView mRecyclerView;
    private View mRootView;
    private List<MonthlyCalenderPopItem> mData;

    private ScheduleMonthAdapter mScheduleMonthAdapter;
    private int mSelectedDay, mCurrentYear, mCurrentMonth, mCurrentDay, mSelectedMonth, mSelectedYear;
    private List<CalendarScheduleContentBean.DataBean> mPopupData;
    private MonthCalenderMonthSelectPopWindow monthSelectPopWindow;
    private int mViewHeight;
    private int mViewWidth;
    private Dialog dialog;
    private String startDate;
    private String endDate;
    private String popwindowCurDataStr;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_mothly_calendar, container, false);
        ButterKnife.bind(this, mRootView);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    private void initData() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);


        SimpleDateFormat currentFormat1 = new SimpleDateFormat("yyyy");
        String currentYear = currentFormat1.format(calendar.getTime());
        SimpleDateFormat currentFormat2 = new SimpleDateFormat("MM");
        String currentMonth = currentFormat2.format(calendar.getTime());
        SimpleDateFormat currentFormat3 = new SimpleDateFormat("dd");
        String CurrentDay = currentFormat3.format(calendar.getTime());

        mData = new ArrayList<>();
        mCurrentYear = Integer.parseInt(currentYear);
        mCurrentDay = Integer.parseInt(CurrentDay);
        mCurrentMonth = Integer.parseInt(currentMonth);


        mSelectedYear = mCurrentYear;
        mSelectedDay = mCurrentDay;
        mSelectedMonth = mCurrentMonth;
        //测量布局的宽高
        int mHeight = ScreenUtils.dp2px(getContext(), 97) + ScreenUtils.getStatusHeight(getContext());
        mViewHeight = (ScreenUtils.getScreenHeight(getContext()) - mHeight) / 6;
        mViewWidth = ScreenUtils.getScreenWidth(getContext()) / 7;

        mRecyclerView.addItemDecoration(new GridItemDecoration(getContext(), R.drawable.divider));
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 7));

        mScheduleMonthAdapter = new ScheduleMonthAdapter(mData, mViewHeight, mViewWidth);
        mScheduleMonthAdapter.setOnItemClick(this);
        mRecyclerView.setAdapter(mScheduleMonthAdapter);
        setData(mCurrentMonth, mCurrentDay);

    }


    @Override
    protected void lazyLoadData() {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }

    }

    @Override
    public void uidNull(String code) {
        catchWarningByCode(code);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @SuppressLint("LongLogTag")
    @Override
    public void GetScheduleByDateSuccess(CalendarScheduleContentBean bean) {
        try {
            if (bean == null) {
                return;
            }
            List<CalendarScheduleContentBean.DataBean> DataBeanLists = bean.getData();
            for (int i = 0; i < mData.size(); i++) {
                String taskDate = getPopItemDataString(mData.get(i));
                List<CalendarScheduleContentBean.DataBean> data = new ArrayList<>();
                List<CalendarScheduleContentBean.DataBean> dataSort = new ArrayList<>();
                for (int j = 0; j < DataBeanLists.size(); j++) {
                    if (taskDate.equals(DataBeanLists.get(j).getTaskDate())) {
                        data.add(DataBeanLists.get(j));
                    }
                }
                Collections.sort(data, new Comparator<CalendarScheduleContentBean.DataBean>() {
                    @Override
                    public int compare(CalendarScheduleContentBean.DataBean o1, CalendarScheduleContentBean.DataBean o2) {
                        if (DateUtils.getTimestampFromString(o1.getStartTime(), "yyyy-MM-dd HH:mm:ss") >= DateUtils.getTimestampFromString(o2.getStartTime(), "yyyy-MM-dd HH:mm:ss")) {
                            return 1;
                        } else {
                            return -1;
                        }
                    }
                });

                mData.get(i).setData(data);
            }
            mScheduleMonthAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 获取时间   taskDate 2018-01-27
     *
     * @param monthlyCalenderPopItem
     * @return
     */
    private String getPopItemDataString(MonthlyCalenderPopItem monthlyCalenderPopItem) {
        String content = monthlyCalenderPopItem.getContent();
        mSelectedDay = Integer.parseInt(content);
        String mDay = (mSelectedDay < 10) ? "0" + mSelectedDay : mSelectedDay + "";
        String month = (mSelectedMonth < 10) ? "0" + mSelectedMonth : mSelectedMonth + "";
        String date = mSelectedYear + "-" + month + "-" + mDay;
        return date;
    }

    @Override
    public void GetScheduleByDateFailure(int errorCode, String errorMsg) {
        if ("auth error".equals(errorMsg)) {
            catchWarningByCode(errorMsg);
            return;
        }
        if (errorCode == -1) {
            showToast(getString(R.string.net_no_connection));
            return;
        }

    }

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    public void setData(int month, int selectPos) {
        if (mData != null) {
            mData.clear();
        }
        List<MonthlyCalenderPopItem> date = DateUtils.getMonthlyScheduleCalendarDate(mSelectedYear, month, selectPos);
        mData.addAll(date);
        mScheduleMonthAdapter.notifyDataSetChanged();

        String monthStr = (month < 10) ? "0" + month : month + "";
        String mDayStr = (selectPos < 10) ? "0" + selectPos : selectPos + "";
        String dateStr = mSelectedYear + "年" + monthStr + "月" + mDayStr + "日";
        String dateStr2 = mSelectedYear + "-" + monthStr + "-" + mDayStr;
        popwindowCurDataStr = mSelectedYear + "年" + monthStr + "月";
        EventBus.getDefault().post(new UpdateTitleBean(dateStr, "monthLUpdateTitle"));
        EventBus.getDefault().post(new SelectedWeekCalendarEvent(dateStr2, "slideRecyclerViewPosition"));
        startDate = mSelectedYear + "-" + month + "-" + "01";
        endDate = mSelectedYear + "-" + month + "-" + DateUtils.getDaysByYearMonth(mSelectedYear, month);
        mPresenter.getScheduleByDate(startDate, endDate);
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
        mScheduleMonthAdapter.notifyDataSetChanged();
        String content = mData.get(position).getContent();
        mSelectedDay = Integer.parseInt(content);
        String mDay = (mSelectedDay < 10) ? "0" + mSelectedDay : mSelectedDay + "";
        String month = (mSelectedMonth < 10) ? "0" + mSelectedMonth : mSelectedMonth + "";
        String date = mSelectedYear + "年" + month + "月" + mDay + "日";
        String dateStr2 = mSelectedYear + "-" + month + "-" + mDay;
        EventBus.getDefault().post(new SelectedWeekCalendarEvent(dateStr2, "slideRecyclerViewPosition"));
        EventBus.getDefault().post(new UpdateTitleBean(date, "monthLUpdateTitle"));
        List<CalendarScheduleContentBean.DataBean> data = mData.get(position).getData();

        if (data != null && data.size() >= 1) {
            showPopWindow(v, data, position + 1, mSelectedYear + "", month, mDay);
        } else {
            Intent intent1 = new Intent(getContext(), CalendarRedactActivity.class);
            intent1.putExtra(Constants.CALENDARYEAR, mSelectedYear + "");
            intent1.putExtra(Constants.CALENDARMONTH, month);
            intent1.putExtra(Constants.CALENDARDATE, mDay);
            intent1.putExtra(Constants.CALENDARTYPE, Constants.WRITECALENDAR);
            startActivityForResult(intent1, 101);
        }
    }

    private void showPopWindow(View v, List<CalendarScheduleContentBean.DataBean> data, int clickPosition, String mSelectedYear1, String mSelectedMonth1, String mSelectedDay1) {
        dialog = new Dialog(getContext(), R.style.CustomDialog);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_month_calendar_content, null);
        RecyclerView rvContent = (RecyclerView) view.findViewById(R.id.rv_content);
        ImageView addCalendar = (ImageView) view.findViewById(R.id.img_add_calendar);

        mPopupData = new ArrayList<>();
        mPopupData.addAll(data);
        MonthContentAdapter adapter = new MonthContentAdapter(data);
        rvContent.setAdapter(adapter);
        rvContent.addOnItemTouchListener(new ItemClick());
        //添加新事件
        addCalendar.setOnClickListener(view1 -> {
            Intent intent1 = new Intent(getContext(), CalendarRedactActivity.class);
            intent1.putExtra(Constants.CALENDARYEAR, mSelectedYear1);
            intent1.putExtra(Constants.CALENDARMONTH, mSelectedMonth1);
            intent1.putExtra(Constants.CALENDARDATE, mSelectedDay1);

            intent1.putExtra(Constants.CALENDARTYPE, Constants.WRITECALENDAR);
            startActivityForResult(intent1, 101);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        });
        rvContent.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayout.VERTICAL, false));
        adapter.notifyDataSetChanged();

        dialog.setContentView(view);
        //获取到当前Activity的Window
        Window dialog_window = dialog.getWindow();
        WindowManager.LayoutParams dialog_window_attributes = dialog_window.getAttributes();

        dialog_window_attributes.width = (int) (mViewWidth * 2.5);

        int mViewHeightWrapcontent = dialog_window_attributes.height;
        if (data.size() >= 5) {
            dialog_window_attributes.height = (int) (mViewHeight * 3);
        } else {
            dialog_window_attributes.height = mViewHeightWrapcontent;
        }

        if ((clickPosition - 1) % 7 >= 4) {
            dialog_window_attributes.x = mViewWidth * (7 - (clickPosition - 1) % 7);
            if (((clickPosition - 1) / 7) >= 3) {
                dialog_window.setGravity(Gravity.RIGHT | Gravity.BOTTOM);
                if (((clickPosition - 1) / 7) == 3) {
                    dialog_window_attributes.y = mViewHeight * 2;
                } else if (((clickPosition - 1) / 7) == 4 || ((clickPosition - 1) / 7) == 5) {
                    dialog_window_attributes.y = mViewHeight;
                }

            } else {
                dialog_window.setGravity(Gravity.RIGHT | Gravity.TOP);
                dialog_window_attributes.y = ((clickPosition - 1) / 7) * mViewHeight + ScreenUtils.dp2px(getContext(), 96);
            }
        } else {
            dialog_window_attributes.x = mViewWidth * ((clickPosition - 1) % 7);
            if (((clickPosition - 1) / 7) >= 3) {
                dialog_window.setGravity(Gravity.LEFT | Gravity.BOTTOM);
                if (((clickPosition - 1) / 7) == 3) {
                    dialog_window_attributes.y = mViewHeight * 2;
                } else if (((clickPosition - 1) / 7) == 4 || ((clickPosition - 1) / 7) == 5) {
                    dialog_window_attributes.y = mViewHeight;
                }
            } else {
                dialog_window.setGravity(Gravity.LEFT | Gravity.TOP);
                dialog_window_attributes.y = ((clickPosition - 1) / 7) * mViewHeight + ScreenUtils.dp2px(getContext(), 96);
            }
        }
        dialog_window.setAttributes(dialog_window_attributes);
        dialog.show();
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
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getTitleViewClicked(UpdateTitleBean bean) {
        if ("MonthlyCalendarFragment".equals(bean.getEvent()) && "MonthlyCalendarFragment".equals(bean.getTitle())) {
            selectMonthPopwindow(popwindowCurDataStr);
        } else if (bean.getEvent().equals("updateTitle")) {
            String dateStr = bean.getTitle() + "xxxx";
            int year = Integer.parseInt(dateStr.substring(0, 4));
            int mSelectedMonth = Integer.parseInt(dateStr.substring(5, 7));
            int day = Integer.parseInt(dateStr.substring(8, 10));
            mSelectedYear = year;
            popwindowCurDataStr = mSelectedYear + "年" + mSelectedMonth + "月";
//            if (mSelectedMonth == mCurrentMonth) {
//                setData(mSelectedMonth, day);
//            } else {
//                setData(mSelectedMonth, 1);
//            }
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void RefreshData(SelectedWeekCalendarEvent bean) {
        if (bean.getEvent().equals("upDateCalendarScheduleSuccess")) {
            mPresenter.getScheduleByDate(startDate, endDate);
        }
    }

    private void selectMonthPopwindow(String popwindowCurDataStr) {
        monthSelectPopWindow = new MonthCalenderMonthSelectPopWindow(getActivity(),
                new MonthCalenderMonthSelectPopWindow.PopListener() {
                    @Override
                    public void cancle() {
                        monthSelectPopWindow.dismiss();
                    }

                    @Override
                    public void confirm(String year, String month) {
                        mSelectedYear = Integer.parseInt(year);
                        mSelectedMonth = Integer.parseInt(month);
                        if (mSelectedMonth == mCurrentMonth) {
                            setData(mSelectedMonth, mCurrentDay);
                        } else {
                            setData(mSelectedMonth, 1);
                        }
                        mRecyclerView.requestLayout();
                        mScheduleMonthAdapter.notifyDataSetChanged();
                        monthSelectPopWindow.dismiss();
                    }
                }, popwindowCurDataStr);
        monthSelectPopWindow.showAtLocation(mRootView, Gravity.BOTTOM, 0, 0);
    }
}
