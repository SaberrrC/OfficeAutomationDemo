package com.shanlinjinrong.oa.ui.activity.home.schedule.manage.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.ui.activity.home.schedule.manage.contact.MonthlyCalendarFragmentContact;
import com.shanlinjinrong.oa.ui.activity.home.schedule.manage.presenter.MonthlyCalendarFragmentPresenter;
import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.bean.PopItem;
import com.shanlinjinrong.oa.ui.activity.home.schedule.staffselfhelp.adapter.ScheduleMonthAdapter;
import com.shanlinjinrong.oa.ui.base.BaseHttpFragment;
import com.shanlinjinrong.oa.utils.DateUtils;
import com.shanlinjinrong.views.listview.decoration.GridItemDecoration;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MonthlyCalendarFragment extends BaseHttpFragment<MonthlyCalendarFragmentPresenter> implements MonthlyCalendarFragmentContact.View, ScheduleMonthAdapter.OnItemClick {


    @BindView(R.id.top_data_list)
    RecyclerView mRecyclerView;
    private View                 mRootView;
    private List<PopItem>        mData;
    private ScheduleMonthAdapter mDatePopAttandanceAdapter;
    private int                  mSelectedDay, mCurrentYear, mCurrentMonth, mCurrentDay, mSelectedMonth;

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

        mData = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        mCurrentYear = calendar.get(Calendar.YEAR);
        mCurrentDay = calendar.get(Calendar.DAY_OF_MONTH);
        mCurrentMonth = calendar.get(Calendar.MONTH) + 1;

        List<PopItem> date = DateUtils.getAttandenceDate(mCurrentMonth, mCurrentDay);
        mData.addAll(date);
        mRecyclerView.addItemDecoration(new GridItemDecoration(getContext(), R.drawable.divider));
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 7));
        mDatePopAttandanceAdapter = new ScheduleMonthAdapter(mData);
        mDatePopAttandanceAdapter.setOnItemClick(this);
        mRecyclerView.setAdapter(mDatePopAttandanceAdapter);
    }

    @Override
    protected void lazyLoadData() {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
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
        Toast.makeText(getContext(), position + 1 + "", Toast.LENGTH_SHORT).show();
    }
}
