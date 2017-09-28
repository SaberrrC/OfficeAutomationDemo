package com.shanlinjinrong.oa.ui.activity.home.weeklynewspaper.adapter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;

import com.shanlinjinrong.oa.ui.activity.home.weeklynewspaper.fragment.WeeklyWorkContentFragment;

import java.util.List;

/**
 * Created by tonny on 2017/9/27.
 */

public class WeeklyWorkAdapter extends FragmentStatePagerAdapter {
    private List<WeeklyWorkContentFragment> fragments;

    public WeeklyWorkAdapter(FragmentManager fm, List<WeeklyWorkContentFragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public WeeklyWorkContentFragment getItem(int position) {
        return fragments == null ? null : fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments == null ? 0 : fragments.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }


    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }



}
