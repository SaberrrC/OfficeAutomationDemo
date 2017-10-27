package com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.adapter.DatePopAdapter;
import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.bean.PopItem;
import com.shanlinjinrong.oa.utils.DateUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by 丁 on 2017/10/12.
 */

public class DatePopWindow {
    private RecyclerView mTopList;
    private TextView mShadow;
    private int mTopMargin;
    private int mHeight;

    private PopupWindow mPopWindow;
    private View mView;
    private Context mContext;
    private List<PopItem> mData;
    private DatePopAdapter mAdapter;

    PopItemClick mItemClick;

    public DatePopWindow(Context context, View view, int topMargin, int height) {
        mTopMargin = topMargin;
        mView = view;
        mContext = context;
        mHeight = height;
        initView();
    }

    private void initView() {
        View rootView = View.inflate(mContext, R.layout.date_select_popwindow, null);
        mTopList = (RecyclerView) rootView.findViewById(R.id.top_data_list);
        mShadow = (TextView) rootView.findViewById(R.id.tv_shadow);
        mShadow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopWindow.dismiss();
            }
        });
        // 设置弹框的宽度跟高度
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        mPopWindow = new PopupWindow(rootView, width, mHeight, false);

        mPopWindow.setFocusable(false);

        mData = new ArrayList<>();

    }


    public void setData(final boolean isDay, int month, int selectPos) {
        mData.clear();
        if (isDay) {
            mData = DateUtils.getDate(month, selectPos);
        } else {
            creatMonth(selectPos);
        }
        mTopList.setLayoutManager(new GridLayoutManager(mContext, isDay ? 7 : 6));
        mAdapter = new DatePopAdapter(mData);
        mTopList.setAdapter(mAdapter);

        mAdapter.setOnItemClick(new DatePopAdapter.OnItemClick() {
            @Override
            public void onItemClicked(int position) {
                if (mItemClick != null) {
                    mItemClick.onPopItemClick(isDay, position);
                    hidden();
                }
            }
        });
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

    /**
     * show popwindow
     */
    public void show() {
        if (!mPopWindow.isShowing()) {
            mPopWindow.showAtLocation(mView, Gravity.START | Gravity.TOP, mTopMargin, mTopMargin);
        }
    }

    /**
     * hide popwindow
     */
    public void hidden() {
        if (mPopWindow.isShowing()) {
            mPopWindow.dismiss();
        }
    }


    /**
     * PopWindow点击事件回调接口
     */
    public interface PopItemClick {
        void onPopItemClick(boolean isDay, int position);
    }

    public DatePopWindow setItemClick(PopItemClick mItemClick) {
        this.mItemClick = mItemClick;
        return this;
    }
}
