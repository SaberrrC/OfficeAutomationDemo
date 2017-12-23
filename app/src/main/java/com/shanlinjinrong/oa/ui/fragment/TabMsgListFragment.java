package com.shanlinjinrong.oa.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.ui.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * ProjectName: dev-beta-v1.0.1
 * PackageName: com.itcrm.GroupInformationPlatform.ui.fragment
 * Author:Created by Tsui on Date:2016/11/9 11:02
 * Description:通知列表fragment
 */
//TODO 废弃
public class TabMsgListFragment extends BaseFragment {

    @BindView(R.id.layout_root)
    RelativeLayout mRootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, inflater.inflate(R.layout.tab_msg_list, container, false));
        return mRootView;
    }

    @Override
    protected void lazyLoadData() {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
