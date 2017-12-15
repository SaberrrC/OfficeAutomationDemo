package com.shanlinjinrong.oa.ui.activity.message;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMGroupManager;
import com.hyphenate.chat.EMGroupOptions;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.db.Friends;
import com.hyphenate.easeui.db.FriendsInfoCacheSvc;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.manager.AppConfig;
import com.shanlinjinrong.oa.manager.AppManager;
import com.shanlinjinrong.oa.model.Contacts;
import com.shanlinjinrong.oa.ui.activity.message.Fragment.GroupContactListFragment;
import com.shanlinjinrong.oa.ui.activity.message.Fragment.SelectedGroupContactFragment;
import com.shanlinjinrong.oa.ui.activity.message.adapter.SelectedContactAdapter;
import com.shanlinjinrong.oa.ui.activity.message.bean.DeleteContactEvent;
import com.shanlinjinrong.oa.ui.activity.message.contract.SelectedGroupContactContract;
import com.shanlinjinrong.oa.ui.activity.message.presenter.SelectedGroupContactPresenter;
import com.shanlinjinrong.oa.ui.base.HttpBaseActivity;
import com.shanlinjinrong.views.common.CommonTopView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

//群组 选择人员
public class SelectedGroupContactActivity extends HttpBaseActivity<SelectedGroupContactPresenter> implements SelectedGroupContactContract.View, SelectedGroupContactFragment.onLoadUsersListener, SelectedGroupContactFragment.onSelectedUsersListener {

    @BindView(R.id.top_view)
    CommonTopView mTopView;
    @BindView(R.id.tv_empty_view)
    TextView mTvErrorView;
    @BindView(R.id.search_et_input)
    EditText mSearchContact;
    @BindView(R.id.tv_selected_contact)
    TextView mTvSelectedContact;
    @BindView(R.id.rv_search_contact)
    RecyclerView mRvSearchContact;
    @BindView(R.id.ll_selected_contact)
    LinearLayout mLlSelectedContact;
    @BindView(R.id.bottom_container_layout)
    BottomSheetLayout bottomContainerLayout;


    private List<String> mOrgIdKey;
    private final int RESULT_CODE = -3, REFRESHSUCCESS = -2;
    private List<Contacts> mGroupUsers;
    private InputMethodManager inputManager;
    private List<Contacts> mSearchData;
    private SelectedContactAdapter mUserAdapter;
    private GroupContactListFragment mBottomFragment;
    private SparseArray<List<Contacts>> mCacheContact;
    private List<SelectedGroupContactFragment> mFragments;
    private ArrayList<String> mSelectedAccount;
    private EMGroup mGroup;
    private String[] mUserNames;
    private String[] mUserCodes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_group_contact);
        ButterKnife.bind(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        initData();
        initView();
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    private void initData() {
        mOrgIdKey = new ArrayList<>();
        mFragments = new ArrayList<>();
        mGroupUsers = new ArrayList<>();
        mSearchData = new ArrayList<>();
        mCacheContact = new SparseArray<>();
        mSelectedAccount = new ArrayList<>();
        mSelectedAccount = getIntent().getStringArrayListExtra("selectedMember");
        if (getIntent().getIntExtra("type", -1) == 0) {
            Contacts contacts1 = new Contacts();
            contacts1.setChecked(true);
            contacts1.setItemType(1);
            contacts1.setUsername(getIntent().getStringExtra("userName"));
            contacts1.setCode(getIntent().getStringExtra("userCode"));
            mGroupUsers.add(contacts1);
            mTopView.setRightText("确认(1)");
            mTvSelectedContact.setText("1");
        }
    }

    private void initView() {

        //------------------------------- 监听联系人搜索 -------------------------------

        RxTextView.textChanges(mSearchContact)
                .debounce(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(charSequence -> {


                    if (mSearchContact.getText().toString().trim().equals("")) {
                        if (mFragments.size() > 0) {
                            mTopView.setAppTitle(mFragments.get(0).getArguments().getString("title", "选择成员"));
                            mTopView.setLeftText("上一级");
                            return;
                        }
                        mTopView.setAppTitle("选择成员");
                        mTopView.setLeftText("返回");
                        mRvSearchContact.setVisibility(View.GONE);
                        return;
                    }
                    mTopView.setAppTitle("搜索");
                    mTopView.setLeftText("上一级");
                    mTvErrorView.setVisibility(View.VISIBLE);
                    mPresenter.searchContact(mSearchContact.getText().toString().trim(), mSelectedAccount);

                    //-------------------------------       键盘隐藏       -------------------------------

                    try {
                        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

        //-------------------------------   初始化视图   -------------------------------

        SelectedGroupContactFragment fragment = new SelectedGroupContactFragment(mGroupUsers, mCacheContact, mOrgIdKey, this, this);
        Bundle bundle = new Bundle();
        bundle.putBoolean("isBack", false);
        bundle.putStringArrayList("selectedAccount", mSelectedAccount);
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.fl_container_layout, fragment).commit();

        mUserAdapter = new SelectedContactAdapter(mSearchData);
        mRvSearchContact.setAdapter(mUserAdapter);
        mRvSearchContact.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRvSearchContact.addOnItemTouchListener(new OnItemClick());
        mUserAdapter.notifyDataSetChanged();

        inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        mTopView.getRightView().setOnClickListener(view -> {

            if (mGroupUsers.size() == 0) {
                showToast("请选择人员！");
                return;
            }

            //返回 -> 选择人员
            mUserNames = new String[mGroupUsers.size()];
            mUserCodes = new String[mGroupUsers.size()];

            for (int i = 0; i < mGroupUsers.size(); i++) {
                mUserNames[i] = mGroupUsers.get(i).getUsername();
                mUserCodes[i] = "sl_" + mGroupUsers.get(i).getCode();
            }
            showLoadingView();
            if (getIntent().getBooleanExtra("isAddMember", false)) {
                addMember(mUserCodes);
            } else {
                createGroup(mUserNames, mUserCodes);
            }
        });
    }

    private void addMember(String[] member) {
        String groupId = getIntent().getStringExtra(EaseConstant.GROUPID);
        Observable.create(e -> {
            EMClient.getInstance().groupManager().inviteUser(groupId, member, null);
            e.onComplete();
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(o -> {
                }, throwable -> {
                    hideLoadingView();
                    throwable.printStackTrace();
                    showToast("邀请成员失败，请稍后重试！");
                }, () -> {
                    hideLoadingView();
                    showToast("邀请成员成功！");
                    setResult(REFRESHSUCCESS);
                    finish();
                });
    }

    //创建群组
    private void createGroup(String[] name, String[] codes) {
        if (name != null && codes != null) {

            //群组名字
            StringBuilder groupName = new StringBuilder(AppConfig.getAppConfig(AppManager.mContext).getPrivateName());
            //默认不加入群主Id
            //codes[codes.length - 1] = "sl_" + AppConfig.getAppConfig(AppManager.mContext).getPrivateCode();

            for (String aName : name) {
                groupName.append(",").append(aName);
            }

            //群名字上限10字符
            if (groupName.length() > 10) {
                groupName = new StringBuilder(groupName.substring(0, 10));
                groupName.append("...");
            }

            //群组默认参数
            EMGroupOptions option = new EMGroupOptions();
            option.maxUsers = 200;//上限200人
            //EMGroupStylePrivateOnlyOwnerInvite——私有群，只有群主可以邀请人；
            //EMGroupStylePrivateMemberCanInvite——私有群，群成员也能邀请人进群；
            //EMGroupStylePublicJoinNeedApproval——公开群，加入此群除了群主邀请，只能通过申请加入此群；
            //EMGroupStylePublicOpenJoin ——公开群，任何人都能加入此群。
            option.style = EMGroupManager.EMGroupStyle.EMGroupStylePrivateMemberCanInvite;
            StringBuilder finalGroupName = groupName;
            Observable.create(e -> {
                        mGroup = EMClient.getInstance().groupManager().createGroup(finalGroupName.toString(), "", codes, "邀请加入群", option);

                        e.onComplete();
                    }
            ).subscribeOn(io.reactivex.schedulers.Schedulers.io())
                    .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
                    .subscribe(o -> {
                    }, throwable -> {
                        hideLoadingView();
                        Toast.makeText(this, "群组创建失败！", Toast.LENGTH_SHORT).show();
                    }, () -> {
                        hideLoadingView();
                        Toast.makeText(this, "群组创建成功！", Toast.LENGTH_SHORT).show();
                        setResult(REFRESHSUCCESS);

                        //建群后 全体通知
                        StringBuilder content = new StringBuilder("邀请");
                        for (int i = 0; i < mGroupUsers.size(); i++) {
                            if (i == 0) {
                                content.append(mGroupUsers.get(i).getUsername());
                                continue;
                            }
                            content.append(",").append(mGroupUsers.get(i).getUsername());
                        }
                        content.append("加入群组");
                        //创建一条文本消息，content为消息文字内容，toChatUsername为对方用户或者群聊的id，后文皆是如此
                        EMMessage message = EMMessage.createTxtSendMessage(content.toString(), mGroup.getGroupId());
                        //如果是群聊，设置chattype，默认是单聊
                        message.setChatType(EMMessage.ChatType.GroupChat);
                        //发送消息
                        EMClient.getInstance().chatManager().sendMessage(message);

                        if (mGroup != null)
                            FriendsInfoCacheSvc.getInstance(AppManager.mContext).addOrUpdateFriends(new Friends(mGroup.getGroupId(), mGroup.getGroupName(), "", "", "", "", "", "", ""));
                        finish();
                    });
        }
    }

    @OnClick(R.id.ll_selected_contact)
    public void onSelectedContact() {

        //-------------------------------  BottomFragment  -------------------------------

        if (mGroupUsers.size() > 0) {
            if (mBottomFragment == null) {
                mBottomFragment = new GroupContactListFragment(mGroupUsers);
            }
            if (bottomContainerLayout.isSheetShowing()) {
                mBottomFragment.dismiss();
                bottomContainerLayout.dismissSheet();
            } else {
                mBottomFragment.show(getSupportFragmentManager(), R.id.bottom_container_layout);
                mBottomFragment.updateBottomData();
            }
        }
    }


    @Override
    public void loadUsers(String title, String orgId) {
        mTopView.setAppTitle(title);
        SelectedGroupContactFragment fragment = new SelectedGroupContactFragment(mGroupUsers, mCacheContact, mOrgIdKey, this, this);
        Bundle bundle = new Bundle();
        bundle.putString("orgId", orgId);
        bundle.putString("title", title);
        bundle.putBoolean("isBack", true);
        bundle.putStringArrayList("selectedAccount", mSelectedAccount);
        fragment.setArguments(bundle);
        mFragments.add(0, fragment);
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.fragment_animation_enter, R.anim.fragmemt_animation_exit)
                .add(R.id.fl_container_layout, fragment).commit();
    }

    @Override
    public void selectedUsers(List<Contacts> groupUsers) {
        mTvSelectedContact.setText(String.valueOf(groupUsers.size()));
        mTopView.setRightText(groupUsers.size() != 0 ? "确认" + "(" + groupUsers.size() + ")" : "确认");
    }


    //TODO 选择逻辑问题
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDeleteContact(DeleteContactEvent event) {

        //------------------------------- 更新通讯录 列表 -------------------------------

        try {
            for (int i = 0; i < mOrgIdKey.size(); i++) {
                List<Contacts> contacts = mCacheContact.get(Integer.parseInt(mOrgIdKey.get(i)));
                if (contacts != null) {
                    for (int j = 0; j < contacts.size(); j++) {
                        if (event.getCode().equals(contacts.get(j).getCode())) {
                            contacts.get(j).setChecked(false);
                            mCacheContact.put(Integer.parseInt(mOrgIdKey.get(i)), contacts);
                            break;
                        }
                    }
                }
            }
            mFragments.get(0).updateSelected();
        } catch (Throwable e) {
            e.printStackTrace();
        }

        //-------------------------------  搜索人员 -------------------------------

        try {
            if (mRvSearchContact.getVisibility() == View.VISIBLE) {

                for (int i = 0; i < mSearchData.size(); i++) {
                    if (mSearchData.get(i).getCode().equals(event.getCode())) {
                        mSearchData.get(i).setChecked(!mSearchData.get(i).isChecked());
                    }
                }

                mUserAdapter.setNewData(mSearchData);
                mUserAdapter.notifyDataSetChanged();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

        if (event.getSize() == 0)
            hideBottomView();

        mTvSelectedContact.setText(String.valueOf(mGroupUsers.size()));
        mTopView.setRightText(mGroupUsers.size() != 0 ? "确认" + "(" + mGroupUsers.size() + ")" : "确认");
    }

    private boolean hideBottomView() {
        if (mBottomFragment != null) {
            if (bottomContainerLayout.isSheetShowing()) {
                mBottomFragment.dismiss();
                bottomContainerLayout.dismissSheet();
                return true;
            }
        }
        return false;
    }

    @Override
    public void onBackPressed() {

        //-------------------------------  模拟Activity  -------------------------------

        if (hideBottomView())
            return;

        //-------------------------------  搜索界面  -------------------------------

        if (mRvSearchContact.getVisibility() == View.VISIBLE) {
            mRvSearchContact.setVisibility(View.GONE);
            mSearchContact.setText("");
            return;
        }

        //-------------------------------  选择人员界面  -------------------------------

        for (SelectedGroupContactFragment fragment : mFragments) {
            if (fragment.onBackPressed()) {
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.fragment_animation_pop_enter, R.anim.fragment_animation_pop_exit)
                        .remove(fragment).commit();
                mFragments.remove(0);
                if (mFragments.size() > 0) {
                    mTopView.setAppTitle(mFragments.get(0).getArguments().getString("title", "选择成员"));
                    mTopView.setLeftText("上一级");
                    return;
                }
                mTopView.setAppTitle("选择成员");
                mTopView.setLeftText("返回");
                return;
            }
        }

        finish();
        super.onBackPressed();
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
    public void QueryGroupContactSuccess(List<Contacts> bean) {
        mTvErrorView.setVisibility(View.GONE);
    }

    @Override
    public void QueryGroupContactFailed(int errorCode, String errorStr) {
        switch (errorCode) {
            case -1:
                mTvErrorView.setText(R.string.net_no_connection);
                mTvErrorView.setVisibility(View.VISIBLE);
                showToast(getResources().getString(R.string.net_no_connection));
                break;
        }
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void searchContactSuccess(List<Contacts> bean) {

        mTvErrorView.setVisibility(View.GONE);

        //-------------------------------  还原上一次选中的人员  -------------------------------

        if (bean != null) {
            mSearchData.clear();
            mRvSearchContact.setVisibility(View.VISIBLE);
            //遍历搜索数据 更新选择人员
            for (int i = 0; i < mGroupUsers.size(); i++) {
                for (Contacts contacts : bean) {
                    if (contacts.getCode().equals(mGroupUsers.get(i).getCode())) {
                        contacts.setChecked(true);
                    }
                }
            }

            mSearchData.addAll(bean);
            mUserAdapter.setNewData(mSearchData);
            mUserAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void searchContactFailed(int errorCode, String errorStr) {
        switch (errorCode) {
            case -1:
                mTvErrorView.setText(R.string.net_no_connection);
                mTvErrorView.setVisibility(View.VISIBLE);
                showToast(getResources().getString(R.string.net_no_connection));
                break;
        }
    }

    class OnItemClick extends OnItemClickListener {
        @SuppressWarnings("LoopStatementThatDoesntLoop")
        @Override
        public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {

            mSearchData.get(i).setChecked(!mSearchData.get(i).isChecked());

            if (mSearchData.get(i).isChecked()) {
                for (int j = 0; j < mGroupUsers.size(); j++) {
                    if (mSearchData.get(i).getCode().equals(mGroupUsers.get(j).getCode())) {
                        mGroupUsers.remove(j);
                    }
                }
                mGroupUsers.add(mSearchData.get(i));
            } else {
                for (int j = 0; j < mGroupUsers.size(); j++) {
                    if (mSearchData.get(i).getCode().equals(mGroupUsers.get(j).getCode())) {
                        mGroupUsers.remove(j);
                    }
                }
            }

            //更新UI
            mTvSelectedContact.setText(String.valueOf(mGroupUsers.size()));
            mTopView.setRightText(mGroupUsers.size() != 0 ? "确认" + "(" + mGroupUsers.size() + ")" : "确认");

            //防止Item点击效果错位问题
            Handler handler = new Handler();
            handler.postDelayed(() -> {
                mUserAdapter.setNewData(mSearchData);
                mUserAdapter.notifyDataSetChanged();
            }, 100);

            // ---------------------------------- 更新通讯录 ----------------------------------

            try {
                String orgId = "";
                List<Contacts> contacts = null;
                for (int j = 0; j < mOrgIdKey.size(); j++) {
                    orgId = mOrgIdKey.get(j);
                    if (!orgId.equals("1")) {
                        contacts = mCacheContact.get(Integer.parseInt(orgId));
                        for (int k = 0; k < contacts.size(); k++) {
                            if (contacts.get(k).getCode() != null) {
                                if (mSearchData.get(i).getCode().equals(contacts.get(k).getCode())) {
                                    contacts.get(k).setChecked(mSearchData.get(i).isChecked());
                                    mCacheContact.remove(Integer.parseInt(orgId));
                                    mCacheContact.put(Integer.parseInt(orgId), contacts);
                                    break;
                                }
                            }
                        }
                    }
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
