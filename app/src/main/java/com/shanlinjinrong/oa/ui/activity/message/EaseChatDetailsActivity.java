package com.shanlinjinrong.oa.ui.activity.message;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.example.retrofit.model.responsebody.GroupUserInfoResponse;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMCursorResult;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.db.Friends;
import com.hyphenate.easeui.db.FriendsInfoCacheSvc;
import com.hyphenate.easeui.event.OnMessagesRefreshEvent;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.easeui.widget.EaseAlertDialog;
import com.hyphenate.exceptions.HyphenateException;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.common.Constants;
import com.shanlinjinrong.oa.manager.AppConfig;
import com.shanlinjinrong.oa.manager.AppManager;
import com.shanlinjinrong.oa.ui.activity.contracts.Contact_Details_Activity;
import com.shanlinjinrong.oa.ui.activity.message.adapter.CommonGroupControlAdapter;
import com.shanlinjinrong.oa.ui.activity.message.bean.GroupEventListener;
import com.shanlinjinrong.oa.ui.activity.message.chatgroup.ModificationGroupNameActivity;
import com.shanlinjinrong.oa.ui.activity.message.contract.EaseChatDetailsContact;
import com.shanlinjinrong.oa.ui.activity.message.presenter.EaseChatDetailsPresenter;
import com.shanlinjinrong.oa.ui.base.HttpBaseActivity;
import com.shanlinjinrong.oa.utils.Utils;
import com.shanlinjinrong.views.common.CommonTopView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

//群组聊天详情界面
public class EaseChatDetailsActivity extends HttpBaseActivity<EaseChatDetailsPresenter> implements EaseChatDetailsContact.View {

    @BindView(R.id.top_view)
    CommonTopView  topView;
    @BindView(R.id.btn_chat_delete)
    TextView       btnChatDelete;
    @BindView(R.id.img_portrait)
    ImageView      imgPortrait;
    @BindView(R.id.img_group_person)
    ImageView      imgGroupPerson;
    @BindView(R.id.rv_person_show)
    RecyclerView   rvPersonShow;
    @BindView(R.id.rl_group_name)
    RelativeLayout rlGroupName;
    @BindView(R.id.tv_modification_name)
    TextView       tvModificationName;
    @BindView(R.id.rl_group_person)
    RelativeLayout rlGroupPerson;
    @BindView(R.id.tv_clear_message_record)
    TextView       tvClearMessageRecord;
    @BindView(R.id.btn_look_message_record)
    TextView       btnLookMessageRecord;
    @BindView(R.id.rl_group_portrait)
    RelativeLayout rlGroupPortrait;
    @BindView(R.id.img_modification_portrait)
    ImageView      imgModificationPortrait;
    @BindView(R.id.img_modification_group_name)
    ImageView      imgModificationGroupName;
    @BindView(R.id.tv_error_layout)
    TextView       tvErrorLayout;
    @BindView(R.id.sv_container_layout)
    ScrollView     svContainerLayout;
    @BindView(R.id.tv_modification_person)
    TextView       tvModificationPerson;
    @BindView(R.id.ll_look_more)
    LinearLayout   llLookMore;

    private int                         memberCount;
    private String                      mGroupId;
    private EMGroup                     mGroupServer1;
    private ArrayList<String>           mMemberList;
    private List<GroupUserInfoResponse> mData;
    private CommonGroupControlAdapter   mAdapter;
    private EMConversation              mConversation;
    private EMCursorResult<String>      mGroupMemberResult;
    private boolean                     mIsGroup, mIsOwner, mIsResume;
    private String mGroupOwner, mGroupName, message_to, message_from;
    private String mSearchUserId = "", mQueryUserInfo = "";
    private final int REQUSET_CODE = 101, REFRESHSUCCESS = -2, RESULTMODIFICATIONNAME = -3, MODIFICATIONOWNER = -4, FINISHRESULT = -5, DISSOLVEGROUP = 600, LISTENERGROUPAME = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ease_chat_details);
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
        mIsGroup = getIntent().getBooleanExtra("chatType", false);
        mGroupId = getIntent().getStringExtra(EaseConstant.GROUPID);
        mData = new ArrayList<>();
        getGroupInfo();
    }

    private void getGroupInfo() {
        if (mGroupId != null) {
            showLoadingView();
            Observable.create(e -> {
                //根据群组ID从本地获取群组基本信息
                mGroupServer1 = EMClient.getInstance().groupManager().getGroupFromServer(mGroupId);
                e.onComplete();
            }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(o -> {
            }, throwable -> {
                hideLoadingView();
                if (throwable instanceof HyphenateException) {
                    int errorCode = ((HyphenateException) throwable).getErrorCode();
                    if (errorCode >= 600 && errorCode <= 700) {
                        if (Utils.isActivityRunning(this, getClass().getName())) {
                            EaseAlertDialog alertDialog = new EaseAlertDialog(this, null, "群组已经解散", null, (confirmed, bundle) -> {
                                setResult(REFRESHSUCCESS);
                                finish();
                            }, false);
                            alertDialog.setCancelable(false);
                            alertDialog.show();
                        }
                    }
                }
            }, () -> {
                if (mGroupServer1 != null) {
                    mGroupOwner = mGroupServer1.getOwner();
                    mGroupName = mGroupServer1.getGroupName();
                    if (mGroupName != null) {
                        if (mGroupName.length() > 10) {
                            mGroupName = mGroupName.substring(0, 10) + "...";
                        }
                    }
                    tvModificationName.setText(mGroupName);
                    mIsOwner = mGroupOwner.equals("sl_" + AppConfig.getAppConfig(AppManager.mContext).getPrivateCode());
                    if (!mIsOwner) {
                        imgGroupPerson.setVisibility(View.INVISIBLE);
                        btnChatDelete.setText("退出群聊");
                    } else {
                        imgGroupPerson.setVisibility(View.VISIBLE);
                    }
                }
                if (mIsGroup) {
                    InitGroupData();
                }
            });
        } else {
            //单聊 查询群用户信息
            message_to = getIntent().getStringExtra("message_to");
            message_from = getIntent().getStringExtra("message_from");
            mSearchUserId = message_from.substring(3, message_from.length()) + "," + message_to.substring(3, message_to.length());
            showLoadingView();
            mPresenter.searchUserListInfo(mSearchUserId);
        }
        if (mIsGroup) {
            if (!TextUtils.isEmpty(mGroupId)) {
                mConversation = EMClient.getInstance().chatManager().getConversation(mGroupId);
            }
        } else {
            Bundle extras = getIntent().getParcelableExtra("EXTRAS");
            String toChatUsername = extras.getString("toChatUsername");
            int chatType = extras.getInt("CHAT_TYPE", EaseConstant.CHATTYPE_SINGLE);
            mConversation = EMClient.getInstance().chatManager().getConversation(toChatUsername, EaseCommonUtils.getConversationType(chatType), true);
        }
    }

    private void InitGroupData() {
        try {
            mQueryUserInfo = "";
            final int pageSize = 500;
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
            }, throwable -> {
                throwable.printStackTrace();
                hideLoadingView();
            }, () -> {//TODO 群成团账号
                if (mIsOwner) {
                    mMemberList.add(0, "sl_" + AppConfig.getAppConfig(AppManager.mContext).getPrivateCode());
                } else {
                    mMemberList.add(0, mGroupOwner);
                }

                for (int i = 0; i < mMemberList.size(); i++) {
                    String userCode = mMemberList.get(i).substring(3, mMemberList.get(i).length());
                    if (i == mMemberList.size() - 1) {
                        mSearchUserId += userCode;
                        continue;
                    }

                    mSearchUserId += userCode + ",";
                }

                for (int i = 0; i < mMemberList.size(); i++) {
                    if (!mIsOwner) {
                        if (mMemberList.get(i).equals("sl_" + AppConfig.getAppConfig(AppManager.mContext).getPrivateCode())) {
                            mMemberList.remove(i);
                            mMemberList.add(1, "sl_" + AppConfig.getAppConfig(AppManager.mContext).getPrivateCode());
                            break;
                        }
                    }
                }

                if (mMemberList.size() > 24) {
                    memberCount = mIsOwner ? 23 : 24;
                } else {
                    memberCount = mMemberList.size();
                }
                mQueryUserInfo = "";
                for (int i = 0; i < memberCount; i++) {
                    String userCode = mMemberList.get(i).substring(3, mMemberList.get(i).length());
                    if (i == memberCount - 1) {
                        mQueryUserInfo += userCode;
                        continue;
                    }
                    mQueryUserInfo += userCode + ",";
                }

                //查询群用户信息
                mPresenter.searchUserListInfo(mQueryUserInfo);
            });
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        mAdapter = new CommonGroupControlAdapter(R.layout.item_common_person_add, mData);
        rvPersonShow.setLayoutManager(new GridLayoutManager(this, 5, GridLayoutManager.VERTICAL, false));
        rvPersonShow.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        rvPersonShow.addOnItemTouchListener(new ItemClick());
        initGroupView();
    }

    private void initGroupView() {
        if (mIsGroup) {
            try {
                if (mIsGroup) {
                    topView.setAppTitle("群聊天详情");

                } else {
                    topView.setAppTitle("聊天详情");
                }
                rlGroupPerson.setVisibility(View.VISIBLE);
                rlGroupName.setVisibility(View.VISIBLE);
                rlGroupPortrait.setVisibility(View.VISIBLE);
                btnChatDelete.setVisibility(View.VISIBLE);
                if (mGroupServer1 != null) {
                    if (mGroupServer1.getGroupName().length() > 10) {
                        mGroupName = mGroupServer1.getGroupName().substring(0, 10) + "...";
                    }
                    tvModificationName.setText(mGroupName);
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    @OnClick({R.id.btn_look_message_record, R.id.tv_clear_message_record, R.id.rl_group_person, R.id.ll_look_more})
    public void onViewClicked(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.btn_look_message_record:
                //查看聊天记录
                if (mConversation == null) {
                    return;
                }
                if (mConversation.getAllMessages().size() == 0) {
                    showToast("当前没有聊天记录");
                    return;
                }
                if (mIsGroup) {
                    intent.putExtra(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_GROUP);
                    intent.putExtra(EaseConstant.GROUPID, mGroupId);
                } else {
                    Bundle extras = getIntent().getParcelableExtra("EXTRAS");
                    intent.putExtra(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
                    intent.putExtra("EXTRAS", extras);
                }
                intent.setClass(this, LookMessageRecordActivity.class);
                break;
            case R.id.tv_clear_message_record:
                //清空聊天消息
                new EaseAlertDialog(this, null, "是否清空所有聊天记录", null, new EaseAlertDialog.AlertDialogUser() {

                    @Override
                    public void onResult(boolean confirmed, Bundle bundle) {
                        if (!confirmed) {
                            return;
                        }
                        if (mConversation != null) {
                            mConversation.clearAllMessages();
                            EventBus.getDefault().post(new OnMessagesRefreshEvent());
                            showToast("聊天记录清除成功");
                            return;
                        }
                        showToast("聊天记录清除失败");
                    }
                }, true).show();
                return;
            case R.id.rl_group_person:
                if (!mIsOwner) {
                    return;
                }
                intent.setClass(this, GroupCommonControlActivity.class);
                intent.putExtra("type", 1);
                intent.putExtra(EaseConstant.GROUPID, mGroupId);
                if (mIsOwner) {
                    intent.putExtra("groupOwner", mGroupOwner);
                    intent.putExtra(Constants.ISGROUPOWNER, mIsOwner);
                }
                break;
            case R.id.ll_look_more:
                intent.setClass(this, LookGroupMemberActivity.class);
                intent.putExtra("userCode", mSearchUserId);
                intent.putExtra(EaseConstant.GROUPID, mGroupId);
                intent.putExtra("groupOwner", mGroupOwner);
                intent.putExtra("isOwner", mIsOwner);
                intent.putStringArrayListExtra("memberList", mMemberList);
                break;
        }
        startActivityForResult(intent, REQUSET_CODE);
    }

    @OnClick(R.id.btn_chat_delete)
    public void deleteChat() {

        //---------------------------------- 群组删除处理 ----------------------------------

        if (mGroupOwner != null)
            if (mIsOwner) {
                new EaseAlertDialog(this, null, "是否解散群组", null, (confirmed, bundle) -> {
                    if (!confirmed) {
                        return;
                    }
                    showLoadingView();
                    dissolveGroup();
                }, true).show();
                return;
            }
        showLoadingView();
        dissolveGroup();
    }

    //退出或解散群组
    private void dissolveGroup() {
        Observable.create(e -> {
            if (mIsOwner) {
                EMClient.getInstance().groupManager().destroyGroup(mGroupId);//解散群组
            } else {
                EMClient.getInstance().groupManager().leaveGroup(mGroupId);//退出群组
            }
            e.onComplete();
        }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(o -> {
        }, throwable -> {
            hideLoadingView();
            if (mGroupOwner != null)
                if (mIsOwner) {
                    Toast.makeText(this, "群组解散失败，请从新尝试！", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "群组退出失败，请从新尝试！", Toast.LENGTH_SHORT).show();
                }
            throwable.printStackTrace();
        }, () -> {
            try {
                hideLoadingView();
                if (mGroupOwner != null)
                    if (mIsOwner) {
                        Toast.makeText(this, "解散成功！", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "退出成功！", Toast.LENGTH_SHORT).show();
                    }
            } catch (Throwable e) {
                e.printStackTrace();
            }
            setResult(REFRESHSUCCESS);
            finish();
        });
    }

    // ---------------------------------- 更新群组名称 ----------------------------------

    @OnClick(R.id.rl_group_name)
    public void modificationGroupName() {
        if (!mIsOwner) {
            return;
        }
        Intent intent = new Intent(this, ModificationGroupNameActivity.class);
        intent.putExtra(EaseConstant.GROUPID, mGroupId);
        intent.putExtra(Constants.ISGROUPOWNER, mIsOwner);
        intent.putExtra(Constants.GROUPNAME, tvModificationName.getText().toString().trim());
        startActivityForResult(intent, REQUSET_CODE);
    }

    @Override
    public void uidNull(String code) {
        catchWarningByCode(code);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {
        hideLoadingView();
    }

    @Override
    public void searchUserListInfoSuccess(List<GroupUserInfoResponse> userInfo) {
        hideLoadingView();
        tvErrorLayout.setVisibility(View.GONE);
        svContainerLayout.setVisibility(View.VISIBLE);
        try {
            mData.clear();
            if (mGroupId != null) {
                if (mMemberList.size() > 23) {
                    llLookMore.setVisibility(View.VISIBLE);
                }
                for (int i = 0; i < userInfo.size(); i++) {
                    if (userInfo.get(i).getCode().equals(mGroupOwner.substring(3, mGroupOwner.length()))) {
                        if (mData.size() > 1) {
                            mData.add(mData.size() - 1, mData.get(0));
                            mData.remove(0);
                        }
                        mData.add(0, userInfo.get(i));
                        continue;
                    }
                    if (!mIsOwner) {
                        if (userInfo.get(i).getCode().equals(AppConfig.getAppConfig(AppManager.mContext).getPrivateCode())) {
                            if (mData.size() > 0) {
                                mData.add(1, userInfo.get(i));
                            } else {
                                mData.add(userInfo.get(i));
                            }
                            continue;
                        }
                    }
                    mData.add(userInfo.get(i));
                }

                String groupOwner = mGroupOwner.substring(3, mGroupOwner.length());
                try {
                    for (int i = 0; i < mMemberList.size(); i++) {
                        if (userInfo.size() > i)
                            if (userInfo.get(i).getCode().equals(groupOwner)) {
                                tvModificationPerson.setText(userInfo.get(i).getUsername());
                                break;
                            }
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }

            } else {
                mData.addAll(userInfo);
            }

            if (mIsOwner) {
                mData.add(new GroupUserInfoResponse("add"));
                mData.add(new GroupUserInfoResponse("delete"));
            } else {
                mData.add(new GroupUserInfoResponse("add"));
            }

            mAdapter.setNewData(mData);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        rvPersonShow.requestLayout();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void searchUserListInfoFailed(int errorCode, String errorMsg) {
        Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
        //TODO 加载loading 显示
        hideLoadingView();
        tvErrorLayout.setVisibility(View.VISIBLE);
        svContainerLayout.setVisibility(View.GONE);
        tvErrorLayout.setText(errorMsg);
    }

    // ---------------------------------- 添加群成员跳转 ----------------------------------

    class ItemClick extends OnItemClickListener {
        @Override
        public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
            Intent intent = new Intent();
            if (i > mData.size() || mData.size() == 0) {
                return;
            }
            switch (mData.get(i).getUsername()) {
                case "add":
                    if (mGroupId != null) {
                        ArrayList<String> selectedAccount = new ArrayList<>();
                        selectedAccount.addAll(mMemberList);
                        selectedAccount.add(mGroupOwner);
                        intent.setClass(EaseChatDetailsActivity.this, SelectedGroupContactActivity.class);
                        intent.putStringArrayListExtra("selectedMember", selectedAccount);
                        intent.putExtra("isAddMember", true);
                        intent.putExtra(EaseConstant.GROUPID, mGroupId);
                        intent.putExtra(Constants.SELECTEDTYEPE, 1);
                        //增加群人员 更新数据库
                        if (FriendsInfoCacheSvc.getInstance(AppManager.mContext).getNickName(mGroupId).equals("")) {
                            FriendsInfoCacheSvc.getInstance(AppManager.mContext).addOrUpdateFriends(new Friends(mGroupId, mGroupName, ""));
                        }
                    } else {
                        intent.setClass(EaseChatDetailsActivity.this, SelectedGroupContactActivity.class);
                        intent.putExtra("type", 0);
                        intent.putExtra("isOwner", mIsOwner);
                        intent.putExtra(Constants.SELECTEDTYEPE, 1);
                        String from = message_from.substring(3, message_from.length());
                        String to = message_to.substring(3, message_to.length());
                        if (from.equals(AppConfig.getAppConfig(AppManager.mContext).getPrivateCode())) {
                            intent.putExtra("userCode", to);
                            String nickName = FriendsInfoCacheSvc.getInstance(AppManager.mContext).getNickName(message_to);
                            intent.putExtra("userName", nickName);
                        } else {
                            intent.putExtra("userCode", from);
                            String nickName = FriendsInfoCacheSvc.getInstance(AppManager.mContext).getNickName(message_from);
                            intent.putExtra("userName", nickName);
                        }
                    }
                    break;
                case "delete":
                    intent.setClass(EaseChatDetailsActivity.this, GroupCommonControlActivity.class);
                    intent.putExtra("type", 0);
                    intent.putExtra(EaseConstant.GROUPID, mGroupId);
                    intent.putExtra("isOwner", mIsOwner);
                    break;
                default:
                    intent.setClass(EaseChatDetailsActivity.this, Contact_Details_Activity.class);
                    intent.putExtra("user_code", "sl_" + mData.get(i).getCode());
                    intent.putExtra("isSession", true);
                    break;
            }
            startActivityForResult(intent, REQUSET_CODE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mIsResume = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mIsResume = false;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshGroup(GroupEventListener event) {
        switch (event.getEvent()) {
            case Constants.MODIFICATIONNAME:
                String groupName = FriendsInfoCacheSvc.getInstance(AppManager.mContext).getNickName(mGroupId);
                tvModificationName.setText(groupName);
                break;
            case Constants.GROUPDISSOLVE:
                if (!event.isEvent() && mIsResume) {
                    if (Utils.isActivityRunning(this, getClass().getName())) {
                        EaseAlertDialog alertDialog = new EaseAlertDialog(this, null, "群组已经解散", null, (confirmed, bundle) -> {
                            event.setEvent(true);
                            setResult(REFRESHSUCCESS);
                            finish();
                        }, false);
                        alertDialog.setCancelable(false);
                        alertDialog.show();
                    }
                }
                break;
            case Constants.GROUPMEMBERQUIT:
            case Constants.GROUPMEMBERADD:
            case Constants.GROUPOWNERCHANGE:
                mSearchUserId = "";
                initData();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case RESULTMODIFICATIONNAME:
                mGroupName = data.getStringExtra("groupName");
                tvModificationName.setText(mGroupName);
                Intent intent = new Intent();
                intent.putExtra("groupName", mGroupName);
                setResult(RESULTMODIFICATIONNAME, intent);
                break;
            case REFRESHSUCCESS:
                if (mGroupId == null) {
                    setResult(REFRESHSUCCESS);
                    finish();
                    return;
                }
                setResult(RESULTMODIFICATIONNAME);
                getGroupInfo();
                break;
            case MODIFICATIONOWNER:
                getGroupInfo();
                break;
            case FINISHRESULT:
                setResult(REFRESHSUCCESS);
                finish();
                break;
            default:
                break;
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
