package com.shanlinjinrong.oa.ui.activity.message.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.model.Contacts;
import com.shanlinjinrong.oa.ui.activity.message.adapter.SelectedContactAdapter;
import com.shanlinjinrong.oa.ui.activity.message.contract.SelectedGroupContactContract;
import com.shanlinjinrong.oa.ui.activity.message.presenter.SelectedGroupContactPresenter;
import com.shanlinjinrong.oa.ui.base.BaseHttpFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressLint("ValidFragment")
public class SelectedGroupContactFragment extends BaseHttpFragment<SelectedGroupContactPresenter> implements SelectedGroupContactContract.View {

    @BindView(R.id.tv_empty_view)
    TextView     mTvEmptyView;
    @BindView(R.id.rv_group_contact)
    RecyclerView rvGroupContact;

    private View                        mRootView;
    private List<Contacts>              mContact;
    private List<String>                mOrgIdKey;
    private SparseArray<List<Contacts>> mLoadContact;
    private List<Contacts>              mGroupUsers;
    private onLoadUsersListener         mListener;
    public  SelectedContactAdapter      mAdapter;
    private onSelectedUsersListener     mUserListener;
    private ArrayList<String>           mSelectedAccount;
    private String                      mUserCode;


    @SuppressLint("ValidFragment")
    public SelectedGroupContactFragment(List<Contacts> groupUsers, SparseArray<List<Contacts>> loadContact, List<String> orgIdKey, onLoadUsersListener listener, onSelectedUsersListener userListener) {
        mListener = listener;
        mOrgIdKey = orgIdKey;
        mGroupUsers = groupUsers;
        mLoadContact = loadContact;
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
        mSelectedAccount = new ArrayList<>();
        mAdapter = new SelectedContactAdapter(mContact);
        mSelectedAccount = getArguments().getStringArrayList("selectedAccount");
        mUserCode = getArguments().getString("userCode");

        List<Contacts> contacts1 = mLoadContact.get(Integer.parseInt(getArguments().getString("orgId", "1")));
        if (contacts1 != null) {
            mContact.clear();
            mContact.addAll(contacts1);
            mAdapter.setNewData(mContact);
            rvGroupContact.requestLayout();
            mAdapter.notifyDataSetChanged();
            mTvEmptyView.setVisibility(View.GONE);
            return;
        }
        //TODO
        if (!TextUtils.isEmpty(mUserCode)) {
            mPresenter.QueryGroupContact(getArguments().getString("orgId", "1"), mUserCode);
            return;
        }
        mPresenter.QueryGroupContact(getArguments().getString("orgId", "1"), mSelectedAccount);
    }

    private void initView() {
        rvGroupContact.setAdapter(mAdapter);
        rvGroupContact.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rvGroupContact.addOnItemTouchListener(new ItemClick());
        rvGroupContact.requestLayout();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void uidNull(String code) {
    }

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    protected void lazyLoadData() {
    }

    @Override
    public void showLoading() {
        showLoadingView();

    }

    @Override
    public void hideLoading() {
        hideLoadingView();
    }

    @Override
    public void QueryGroupContactSuccess(List<Contacts> bean) {
        try {
            if (bean != null) {
                mTvEmptyView.setVisibility(View.GONE);
                mOrgIdKey.add(getArguments().getString("orgId", "1"));

                for (int i = 0; i < bean.size(); i++) {

                }
                mContact.addAll(bean);


                mAdapter.setNewData(mContact);
                rvGroupContact.requestLayout();
                mAdapter.notifyDataSetChanged();
                if (!getArguments().getString("orgId", "1").equals("1")) {
                    mLoadContact.put(Integer.parseInt(getArguments().getString("orgId", "1")), bean);
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void QueryGroupContactFailed(int errorCode, String errorStr) {
        switch (errorCode) {
            case -1:
                mTvEmptyView.setText(R.string.net_no_connection);
                mTvEmptyView.setVisibility(View.VISIBLE);
                showToast(getResources().getString(R.string.net_no_connection));
                break;
        }
    }

    @Override
    public void searchContactSuccess(List<Contacts> bean) {
        mTvEmptyView.setVisibility(View.GONE);
    }

    @Override
    public void searchContactFailed(int errorCode, String errorStr) {
        switch (errorCode) {
            case -1:
                mTvEmptyView.setText(R.string.net_no_connection);
                mTvEmptyView.setVisibility(View.VISIBLE);
                showToast(getResources().getString(R.string.net_no_connection));
                break;
        }
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
                        mGroupUsers.add(mContact.get(i));
                    } else {
                        if(!TextUtils.isEmpty(mUserCode)){
                            for (int j = 0; j < mGroupUsers.size(); j++) {
                               if (mGroupUsers.get(j).getCode().equals(mContact.get(i).getCode())){
                                   mGroupUsers.remove(j);
                                }
                            }
                        }else {
                            mGroupUsers.remove(mContact.get(i));
                        }
                    }
                    mUserListener.selectedUsers(mGroupUsers);

                    Handler handler = new Handler();
                    handler.postDelayed(() -> {
                        mAdapter.setNewData(mContact);
                        rvGroupContact.requestLayout();
                        mAdapter.notifyDataSetChanged();
                    }, 100);
                    break;
            }
        }
    }

    public void updateSelected() {
        try {
            List<Contacts> contacts = mLoadContact.get(Integer.parseInt(getArguments().getString("orgId", "1")));
            if (contacts == null) {
                return;
            }
            mContact.clear();
            mContact.addAll(contacts);
            mAdapter.setNewData(mContact);
            rvGroupContact.requestLayout();
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
