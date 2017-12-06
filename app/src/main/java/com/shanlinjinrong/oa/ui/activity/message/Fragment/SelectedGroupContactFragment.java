package com.shanlinjinrong.oa.ui.activity.message.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.model.Contacts;
import com.shanlinjinrong.oa.ui.activity.message.adapter.SelectedContactAdapter;
import com.shanlinjinrong.oa.ui.activity.message.bean.GroupUsers;
import com.shanlinjinrong.oa.ui.activity.message.contract.SelectedGroupContactContract;
import com.shanlinjinrong.oa.ui.activity.message.presenter.SelectedGroupContactPresenter;
import com.shanlinjinrong.oa.ui.base.BaseHttpFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressLint("ValidFragment")
public class SelectedGroupContactFragment extends BaseHttpFragment<SelectedGroupContactPresenter> implements SelectedGroupContactContract.View {

    @BindView(R.id.rv_group_contact)
    RecyclerView rvGroupContact;

    private View mRootView;
    private List<Contacts> mContact;
    private SparseArray<Contacts> mLoadContact;
    private List<Contacts> mGroupUsers = new ArrayList<>();
    private onLoadUsersListener mListener;
    public SelectedContactAdapter mAdapter;
    private onSelectedUsersListener mUserListener;


    @SuppressLint("ValidFragment")
    public SelectedGroupContactFragment(List<Contacts> groupUsers, SparseArray<Contacts> loadContact, onLoadUsersListener listener, onSelectedUsersListener userListener) {
        mLoadContact = loadContact;
        mListener = listener;
        mGroupUsers.addAll(groupUsers);
        mUserListener = userListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_selected_group_contact, container, false);
        ButterKnife.bind(this, mRootView);
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        initView();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initData() {
        mContact = new ArrayList<>();
        mAdapter = new SelectedContactAdapter(mContact);
        Contacts users = mLoadContact.get(Integer.parseInt(getArguments().getString("orgId", "1") + "0"));
        if (users != null) {
            mContact.clear();
            for (int i = 0; i < mLoadContact.size(); i++) {
                Contacts contacts = mLoadContact.get(Integer.parseInt(getArguments().getString("orgId", "1") + i));
                if (contacts == null) {
                    continue;
                }
                mContact.add(contacts);
            }
            mAdapter.setNewData(mContact);
            mAdapter.notifyDataSetChanged();
            return;
        }
        mPresenter.QueryGroupContact(getArguments().getString("orgId", "1"));
    }

    private void initView() {
        rvGroupContact.setAdapter(mAdapter);
        rvGroupContact.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rvGroupContact.addOnItemTouchListener(new ItemClick());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void uidNull(int code) {
    }

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    protected void lazyLoadData() {
    }

    @Override
    public void QueryGroupContactSuccess(List<Contacts> bean) {
        try {
            if (bean != null) {
                mContact.addAll(bean);
                mAdapter.setNewData(mContact);
                mAdapter.notifyDataSetChanged();
                if (!getArguments().getString("orgId", "1").equals("1"))
                    for (int i = 0; i < bean.size(); i++) {
                        int orgId = Integer.parseInt(getArguments().getString("orgId", "1") + i);
                        mLoadContact.put(orgId, bean.get(i));
                    }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void searchContactSuccess(List<Contacts> bean) {

    }

    class ItemClick extends OnItemClickListener {
        @Override
        public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
            switch (mContact.get(i).getItemType()) {
                case Contacts.DEPARTMENT:
                    mListener.loadUsers(mContact.get(i).getDepartmentName(), mContact.get(i).getDepartmentId());
                    break;
                case Contacts.EMPLOYEE:
                    mContact.get(i).setChecked(!mContact.get(i).isChecked());

                    if (mContact.get(i).isChecked()) {
//                        GroupUsers users = new GroupUsers();
//                        users.setOrgId(getArguments().getString("orgId", "1"));
//                        users.save(mContact.get(i));
                        mGroupUsers.add(mContact.get(i));
                        mUserListener.selectedUsers(mGroupUsers);
                    }
                    mAdapter.setNewData(mContact);
                    mAdapter.notifyDataSetChanged();
                    break;
            }
        }
    }

    public void updateSelected() {
        try {
            for (int i = 0; i < mLoadContact.size(); i++) {
                Contacts contacts = mLoadContact.get(Integer.parseInt(getArguments().getString("orgId", "1") + i));
                if (contacts == null) {
                    continue;
                }
                mContact.add(contacts);
            }
            mAdapter.setNewData(mContact);
            mAdapter.notifyDataSetChanged();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public interface onLoadUsersListener {
        void loadUsers(String title, String orgId);
    }

    public interface onSelectedUsersListener {
        void selectedUsers(List<Contacts> groupUsers);
    }

    public boolean onBackPressed() {
        return getArguments().getBoolean("isBack");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
