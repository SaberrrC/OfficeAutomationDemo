package com.shanlinjinrong.oa.ui.activity.message;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.retrofit.model.responsebody.GroupUserInfoResponse;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.ui.activity.message.adapter.CommonGroupControlAdapter;
import com.shanlinjinrong.oa.ui.activity.message.contract.EaseChatDetailsContact;
import com.shanlinjinrong.oa.ui.activity.message.presenter.EaseChatDetailsPresenter;
import com.shanlinjinrong.oa.ui.base.HttpBaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

//查看群组 成员
public class LookGroupMemberActivity extends HttpBaseActivity<EaseChatDetailsPresenter> implements EaseChatDetailsContact.View {

    @BindView(R.id.rv_look_group_member)
    RecyclerView rvLookGroupMember;

    private CommonGroupControlAdapter mAdapter;
    private List<GroupUserInfoResponse> mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look_group_member);
        ButterKnife.bind(this);
        initData();
        initView();
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    private void initData() {
        mData = new ArrayList<>();
        mPresenter.searchUserListInfo(getIntent().getStringExtra("userCode"));
    }

    private void initView() {
        mAdapter = new CommonGroupControlAdapter(R.layout.item_common_person_add, mData);
        rvLookGroupMember.setAdapter(mAdapter);
        rvLookGroupMember.setLayoutManager(new GridLayoutManager(this, 5, GridLayoutManager.VERTICAL, false));
        mAdapter.notifyDataSetChanged();
    }


    @Override
    public void uidNull(int code) {
        catchWarningByCode(code);
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
    public void searchUserListInfoSuccess(List<GroupUserInfoResponse> userInfo) {
        try {
            if (userInfo != null) {
                mData.clear();
                mData.addAll(userInfo);
                mAdapter.setNewData(mData);
                mAdapter.notifyDataSetChanged();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void searchUserListInfoFailed(int errorCode, String errorMsg) {

    }
}
