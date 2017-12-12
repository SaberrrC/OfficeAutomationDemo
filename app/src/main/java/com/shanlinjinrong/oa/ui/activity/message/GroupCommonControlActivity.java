package com.shanlinjinrong.oa.ui.activity.message;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.example.retrofit.model.responsebody.GroupUserInfoResponse;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCursorResult;
import com.hyphenate.exceptions.HyphenateException;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.manager.AppConfig;
import com.shanlinjinrong.oa.manager.AppManager;
import com.shanlinjinrong.oa.model.Contacts;
import com.shanlinjinrong.oa.ui.activity.message.adapter.SelectedContactAdapter;
import com.shanlinjinrong.oa.ui.activity.message.contract.EaseChatDetailsContact;
import com.shanlinjinrong.oa.ui.activity.message.presenter.EaseChatDetailsPresenter;
import com.shanlinjinrong.oa.ui.base.HttpBaseActivity;
import com.shanlinjinrong.oa.views.ClearEditText;
import com.shanlinjinrong.views.common.CommonTopView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

//更换群主  删除群成员
public class GroupCommonControlActivity extends HttpBaseActivity<EaseChatDetailsPresenter> implements EaseChatDetailsContact.View {

    @BindView(R.id.top_view)
    CommonTopView mTopView;
    @BindView(R.id.search_et_cancle)
    TextView searchEtCancle;
    @BindView(R.id.search_et_input)
    ClearEditText searchEtInput;
    @BindView(R.id.rv_content_layout)
    RecyclerView mRvContentLayout;

    private String mGroupId;
    private String mGroupOwner;
    private List<String> mMemberList;
    private ArrayList<Contacts> mData;
    private ArrayList<Contacts> mDelete;
    private SelectedContactAdapter mAdapter;
    private EMCursorResult<String> mGroupMemberResult;
    private int DELETEGROUPUSER = 0, MODIFICATIONONWER = 1, DELETESUCCESS = -2;
    private int type;
    private String mSearchUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_common_control);
        ButterKnife.bind(this);
        initData();
        initView();
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    private void initData() {
        mDelete = new ArrayList<>();
        mData = new ArrayList<>();
        mGroupId = getIntent().getStringExtra("groupId");
        mGroupOwner = getIntent().getStringExtra("groupOwner");
        type = getIntent().getIntExtra("type", -1);
        initGroupList();
    }

    private void initGroupList() {
        try {
            final int pageSize = 20;
            Observable.create(e -> {
                do {
                    try {//如果群成员较多，需要多次从服务器获取完成
                        mMemberList = new ArrayList<>();
                        mGroupMemberResult = EMClient.getInstance().groupManager().fetchGroupMembers(mGroupId, mGroupMemberResult != null ? mGroupMemberResult.getCursor() : "", pageSize);
                        mMemberList.addAll(mGroupMemberResult.getData());
                    } catch (HyphenateException e1) {
                        e1.printStackTrace();
                    }
                }
                while (!TextUtils.isEmpty(mGroupMemberResult.getCursor()) && mGroupMemberResult.getData().size() == pageSize);
                e.onComplete();
            }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(o -> {

            }, Throwable::printStackTrace, () -> {//TODO 群成团账号

                //TODO 过滤群主
                if (mGroupOwner == null) {
                    mSearchUserId = AppConfig.getAppConfig(AppManager.mContext).getPrivateCode();
                }

                for (int i = 0; i < mMemberList.size(); i++) {
                    String usercode = mMemberList.get(i).substring(3, mMemberList.get(i).length());
                    mSearchUserId += "," + usercode;
                }

                //查询群用户信息
                mPresenter.searchUserListInfo(mSearchUserId);
            });
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        if (type == 0) {
            mTopView.setAppTitle("删除人员");
        }
        mTopView.getRightView().setOnClickListener(view -> {
            if (type == 0) {
                showLoadingView();
                Observable.create((ObservableOnSubscribe<Contacts>) e -> {
                    if (mDelete.size() == 0) {
                        showToast("请选择要删除的人员！");
                    }
                    for (int i = 0; i < mDelete.size(); i++) {
                        EMClient.getInstance().groupManager().removeUserFromGroup(mGroupId, "sl_" + mDelete.get(i).getCode());
                    }
                    e.onComplete();
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(contacts -> {
                        }, throwable -> {
                            throwable.printStackTrace();
                            hideLoadingView();
                            showToast("删除人员失败！");
                        }, () -> {
                            hideLoadingView();
                            showToast("删除人员成功！");
                            setResult(DELETESUCCESS);
                            finish();
                        });
            } else if (type == 1) {
                if (mDelete == null) {
                    return;
                }
                if (mDelete.size() == 0) {
                    showToast("请选择要更换的群主！");
                    return;
                }
                showLoadingView();
                Observable.create(e -> {
                    EMClient.getInstance().groupManager().changeOwner(mGroupId, "sl_" + mDelete.get(0).code);
                    e.onComplete();
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                        }, throwable -> {
                            hideLoadingView();
                            throwable.printStackTrace();
                            showToast("更换群主失败！");
                        }, () -> {
                            hideLoadingView();
                            showToast("更换群主成功");
                            Intent intent = new Intent();
                            intent.putExtra("groupOwner", mDelete.get(0).getUsername());
                            setResult(DELETESUCCESS,intent);
                            finish();
                        });
            }
        });


        mAdapter = new SelectedContactAdapter(mData);
        mRvContentLayout.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRvContentLayout.setAdapter(mAdapter);
        mRvContentLayout.addOnItemTouchListener(new ItemClick());
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
            mData.clear();
            for (GroupUserInfoResponse user : userInfo) {
                Contacts contacts = new Contacts();
                contacts.setCode(user.getCode());
                contacts.setUsername(user.getUsername());
                contacts.setItemType(1);
                mData.add(contacts);
            }
            mAdapter.setNewData(mData);
            mAdapter.notifyDataSetChanged();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void searchUserListInfoFailed(int errorCode, String errorMsg) {
    }

    public class ItemClick extends OnItemClickListener {
        @Override
        public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
            mData.get(i).setChecked(!mData.get(i).isChecked());
            switch (type) {
                case 0:
                    mDelete.clear();
                    for (int j = 0; j < mData.size(); j++) {
                        if (mData.get(j).isChecked()) {
                            mDelete.add(mData.get(j));
                        }
                    }
                    mAdapter.setNewData(mData);
                    mAdapter.notifyDataSetChanged();
                    break;
                case 1:
                    mDelete.clear();
                    for (int j = 0; j < mData.size(); j++) {
                        if (j != i) {
                            mData.get(j).setChecked(false);
                        } else {
                            mDelete.add(mData.get(j));
                        }
                    }
                    mAdapter.setNewData(mData);
                    mAdapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }

        }
    }
}
