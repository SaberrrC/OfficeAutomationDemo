package com.shanlinjinrong.oa.ui.activity.message;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.example.retrofit.model.responsebody.GroupUserInfoResponse;
import com.hyphenate.easeui.EaseConstant;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.ui.activity.contracts.Contact_Details_Activity;
import com.shanlinjinrong.oa.ui.activity.message.adapter.CommonGroupControlAdapter;
import com.shanlinjinrong.oa.ui.activity.message.contract.EaseChatDetailsContact;
import com.shanlinjinrong.oa.ui.activity.message.presenter.EaseChatDetailsPresenter;
import com.shanlinjinrong.oa.ui.base.HttpBaseActivity;
import com.shanlinjinrong.views.common.CommonTopView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

//查看群组 成员
public class LookGroupMemberActivity extends HttpBaseActivity<EaseChatDetailsPresenter> implements EaseChatDetailsContact.View {

    @BindView(R.id.rv_look_group_member)
    RecyclerView rvLookGroupMember;
    @BindView(R.id.top_view)
    CommonTopView topView;

    private boolean mIsOwner;
    private final int REQUSET_CODE = 101;
    private List<String> mMemberList;
    private List<GroupUserInfoResponse> mData;
    private CommonGroupControlAdapter mAdapter;
    private String mGroupId, mGroupOwner;

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
        mMemberList = new ArrayList<>();
        mIsOwner = getIntent().getBooleanExtra("isOwner", false);
        mGroupId = getIntent().getStringExtra(EaseConstant.GROUPID);
        mGroupOwner = getIntent().getStringExtra("groupOwner");
        mMemberList = getIntent().getStringArrayListExtra("memberList");
        mPresenter.searchUserListInfo(getIntent().getStringExtra("userCode"));
    }

    private void initView() {
        mAdapter = new CommonGroupControlAdapter(R.layout.item_common_person_add, mData);
        rvLookGroupMember.setAdapter(mAdapter);
        rvLookGroupMember.setLayoutManager(new GridLayoutManager(this, 5, GridLayoutManager.VERTICAL, false));
        rvLookGroupMember.addOnItemTouchListener(new ItemClick());
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
                topView.setAppTitle("群成员" + "（" + userInfo.size() + "）");
                mData.clear();
                mData.addAll(userInfo);
                if (mIsOwner) {
                    mData.add(new GroupUserInfoResponse("add"));
                    mData.add(new GroupUserInfoResponse("delete"));
                } else {
                    mData.add(new GroupUserInfoResponse("add"));
                }
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

    class ItemClick extends OnItemClickListener {
        @Override
        public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
            Intent intent = new Intent();
            switch (mData.get(i).getUsername()) {
                case "add":
                    if (mGroupId != null) {
                        ArrayList<String> selectedAccount = new ArrayList<>();
                        selectedAccount.addAll(mMemberList);
                        selectedAccount.add(mGroupOwner);
                        intent.setClass(LookGroupMemberActivity.this, SelectedGroupContactActivity.class);
                        intent.putStringArrayListExtra("selectedMember", selectedAccount);
                        intent.putExtra("isAddMember", true);
                        intent.putExtra(EaseConstant.GROUPID, mGroupId);
                    }
                    break;
                case "delete":
                    intent.setClass(LookGroupMemberActivity.this, GroupCommonControlActivity.class);
                    intent.putExtra("type", 0);
                    intent.putExtra(EaseConstant.GROUPID, mGroupId);
                    intent.putExtra("isOwner", mIsOwner);
                    break;
                default:
                    intent.setClass(LookGroupMemberActivity.this, Contact_Details_Activity.class);
                    intent.putExtra("user_code", "sl_" + mData.get(i).getCode());
                    intent.putExtra("isSession", true);
                    startActivity(intent);
                    break;
            }
            startActivityForResult(intent, REQUSET_CODE);
        }
    }
}
