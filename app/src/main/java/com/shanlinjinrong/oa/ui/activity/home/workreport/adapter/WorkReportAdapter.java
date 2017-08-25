package com.shanlinjinrong.oa.ui.activity.home.workreport.adapter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.shanlinjinrong.oa.ui.activity.home.workreport.WriteReportFragment;

import java.util.List;

/**
 * Created by 丁 on 2017/8/25.
 * 填写时报页面adapter
 */
public class WorkReportAdapter extends FragmentPagerAdapter {
    private List<WriteReportFragment> fragments;

    public WorkReportAdapter(FragmentManager fm, List<WriteReportFragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public WriteReportFragment getItem(int position) {
        return fragments == null ? null : fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments == null ? 0 : fragments.size();
    }
}
