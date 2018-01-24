package com.shanlinjinrong.oa.ui.activity.home.schedule.manage.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.ui.activity.home.schedule.manage.contract.MonthlyCalendarFragmentContract;
import com.shanlinjinrong.oa.ui.activity.home.schedule.manage.bean.UpdateTitleBean;
import com.shanlinjinrong.oa.ui.activity.home.schedule.manage.presenter.MonthlyCalendarFragmentPresenter;
import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.bean.PopItem;
import com.shanlinjinrong.oa.ui.activity.home.schedule.staffselfhelp.adapter.ScheduleMonthAdapter;
import com.shanlinjinrong.oa.ui.base.BaseHttpFragment;
import com.shanlinjinrong.oa.utils.DateUtils;
import com.shanlinjinrong.oa.utils.ScreenUtils;
import com.shanlinjinrong.oa.views.MonthSelectPopWindow;
import com.shanlinjinrong.views.listview.decoration.GridItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MonthlyCalendarFragment extends BaseHttpFragment<MonthlyCalendarFragmentPresenter> implements MonthlyCalendarFragmentContract.View, ScheduleMonthAdapter.OnItemClick {

    @BindView(R.id.top_data_list)
    RecyclerView mRecyclerView;
    private View mRootView;
    private List<PopItem> mData;
    private ScheduleMonthAdapter mScheduleMonthAdapter;
    private int mSelectedDay, mCurrentYear, mCurrentMonth, mCurrentDay, mSelectedMonth, mSelectedYear;
    private List<PopItem> listDate;
    private MonthSelectPopWindow monthSelectPopWindow;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!EventBus.getDefault().isRegistered(getActivity())) {
            EventBus.getDefault().register(getActivity());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_mothly_calendar, container, false);
        ButterKnife.bind(this, mRootView);
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
        String currentDay = currentFormat3.format(calendar.getTime());
       // int lastYear = year - 1;// calendar.set(lastYear, 0, 1);
        //更新Title
        EventBus.getDefault().post(new UpdateTitleBean(currentYear + "年" + currentMonth + "月" + currentDay + "日", "monthLUpdateTitle"));
        mData = new ArrayList<>();
        mCurrentYear = calendar.get(Calendar.YEAR);
        mCurrentDay = calendar.get(Calendar.DAY_OF_MONTH);
        mCurrentMonth = calendar.get(Calendar.MONTH) + 1;

        mSelectedYear = mCurrentYear;
        mSelectedDay = mCurrentDay;
        mSelectedMonth = mCurrentMonth;
        //测量布局的宽高
        int mHeight = ScreenUtils.dp2px(getContext(), 97) + ScreenUtils.getStatusHeight(getContext());
        int mViewHeight = (ScreenUtils.getScreenHeight(getContext()) - mHeight) / 6;

        listDate = DateUtils.getAttandenceDate(mCurrentMonth, mCurrentDay);
        mData.addAll(listDate);
        mRecyclerView.addItemDecoration(new GridItemDecoration(getContext(), R.drawable.divider));
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 7));
        mScheduleMonthAdapter = new ScheduleMonthAdapter(mData, mViewHeight);
        mScheduleMonthAdapter.setOnItemClick(this);
        mRecyclerView.setAdapter(mScheduleMonthAdapter);
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

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    public void setData(int month, int selectPos) {
        if (mData != null) {
            mData.clear();
        }
        List<PopItem> date = DateUtils.getAttandenceDate(month, selectPos);
        mData.addAll(date);
        mScheduleMonthAdapter.notifyDataSetChanged();
        String dateStr = mSelectedYear + "年" + month + "月" + selectPos + "日";
        EventBus.getDefault().post(new UpdateTitleBean(dateStr, "monthLUpdateTitle"));
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
        Toast.makeText(getContext(), position + 1 + "", Toast.LENGTH_SHORT).show();
        EventBus.getDefault().post(new UpdateTitleBean(date, "monthLUpdateTitle"));

    }

    private void selectMonthPopwindow() {
        monthSelectPopWindow = new MonthSelectPopWindow(getActivity(),
                new MonthSelectPopWindow.PopListener() {
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
                });
        monthSelectPopWindow.showAtLocation(mRootView, Gravity.BOTTOM, 0, 0);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getTitleViewClicked(UpdateTitleBean bean) {
        if ("MonthlyCalendarFragment".equals(bean.getEvent()) && "MonthlyCalendarFragment".equals(bean.getTitle())) {
            selectMonthPopwindow();
        }
    }
}
