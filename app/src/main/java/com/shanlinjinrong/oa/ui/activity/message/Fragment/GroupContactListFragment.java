package com.shanlinjinrong.oa.ui.activity.message.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.flipboard.bottomsheet.commons.BottomSheetFragment;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.model.Contacts;
import com.shanlinjinrong.oa.ui.activity.message.adapter.SelectedUserAdapter;
import com.shanlinjinrong.oa.ui.activity.message.bean.DeleteContactEvent;
import com.shanlinjinrong.oa.ui.activity.message.bean.GroupUsers;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

@SuppressLint("ValidFragment")
public class GroupContactListFragment extends BottomSheetFragment {

    @BindView(R.id.rv_group_contact)
    RecyclerView rvGroupContact;

    private View mRootView;
    private SelectedUserAdapter mAdapter;
    private List<Contacts> mData = new ArrayList<>();

    public GroupContactListFragment(List<Contacts> data) {
        mData = data;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_group_contact_list, container, false);
        ButterKnife.bind(this, mRootView);
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
        mAdapter = new SelectedUserAdapter(mData);
        rvGroupContact.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rvGroupContact.setAdapter(mAdapter);
        rvGroupContact.addOnItemTouchListener(new ItemClick());
        mAdapter.notifyDataSetChanged();
    }

    public class ItemClick extends OnItemClickListener {
        @Override
        public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
            String code = mData.get(i).getCode();
            mData.remove(i);
            mAdapter.setNewData(mData);
            mAdapter.notifyDataSetChanged();
            EventBus.getDefault().post(new DeleteContactEvent("delete", i, code, mData.size()));
        }
    }
}
