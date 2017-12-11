package com.shanlinjinrong.oa.ui.activity.message;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
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
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.easeui.widget.EaseAlertDialog;
import com.hyphenate.exceptions.HyphenateException;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.manager.AppConfig;
import com.shanlinjinrong.oa.manager.AppManager;
import com.shanlinjinrong.oa.model.User;
import com.shanlinjinrong.oa.ui.activity.contracts.Contact_Details_Activity;
import com.shanlinjinrong.oa.ui.activity.message.adapter.CommonGroupControlAdapter;
import com.shanlinjinrong.oa.ui.activity.message.chatgroup.ModificationGroupNameActivity;
import com.shanlinjinrong.oa.ui.activity.message.contract.EaseChatDetailsContact;
import com.shanlinjinrong.oa.ui.activity.message.presenter.EaseChatDetailsPresenter;
import com.shanlinjinrong.oa.ui.base.HttpBaseActivity;
import com.shanlinjinrong.views.common.CommonTopView;

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
    CommonTopView topView;
    @BindView(R.id.btn_chat_delete)
    TextView btnChatDelete;
    @BindView(R.id.img_portrait)
    ImageView imgPortrait;
    @BindView(R.id.img_group_person)
    ImageView imgGroupPerson;
    @BindView(R.id.rv_person_show)
    RecyclerView rvPersonShow;
    @BindView(R.id.rl_group_name)
    RelativeLayout rlGroupName;
    @BindView(R.id.tv_modification_name)
    TextView tvModificationName;
    @BindView(R.id.rl_group_person)
    RelativeLayout rlGroupPerson;
    @BindView(R.id.tv_clear_message_record)
    TextView tvClearMessageRecord;
    @BindView(R.id.btn_look_message_record)
    TextView btnLookMessageRecord;
    @BindView(R.id.rl_group_portrait)
    RelativeLayout rlGroupPortrait;
    @BindView(R.id.img_modification_portrait)
    ImageView imgModificationPortrait;
    @BindView(R.id.img_modification_group_name)
    ImageView imgModificationGroupName;
    @BindView(R.id.tv_error_layout)
    TextView tvErrorLayout;
    @BindView(R.id.sv_container_layout)
    ScrollView svContainerLayout;
    @BindView(R.id.tv_modification_person)
    TextView tvModificationPerson;


    private String mGroupId;
    private boolean mIsGroup;
    private String mGroupOwner;
    private String mSearchUserId;
    private EMGroup mGroupServer1;
    private EMGroup mGroupFromServer;
    private List<String> mMemberList;
    private CommonGroupControlAdapter mAdapter;
    private List<GroupUserInfoResponse> mData;
    private EMCursorResult<String> mGroupMemberResult;
    private final int REQUSET_CODE = 101, DELETESUCCESS = -2, RESULTMODIFICATIONNAME = -3;
    private boolean mIsOwner;
    private String mGroupName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ease_chat_details);
        ButterKnife.bind(this);
        initData();
        initView();
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    private void initData() {
        initGroup();
        mData = new ArrayList<>();
    }

    private void initGroup() {
        getGroupInfo();
    }

    private void getGroupInfo() {
        mIsGroup = getIntent().getBooleanExtra("chatType", false);
        mGroupId = getIntent().getStringExtra("groupId");
        if (mGroupId != null) {
            Observable.create(e -> {
                //根据群组ID从本地获取群组基本信息
                mGroupServer1 = EMClient.getInstance().groupManager().getGroupFromServer(mGroupId);
                e.onComplete();
            }).observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(o -> {
                    }, Throwable::printStackTrace, () -> {
                        //TODO 完成后显示页面
                        if (mGroupServer1 != null) {
                            mGroupOwner = mGroupServer1.getOwner();
                            mGroupName = mGroupServer1.getGroupName();
                            tvModificationName.setText(mGroupName);
                            mIsOwner = mGroupOwner.equals("sl_" + AppConfig.getAppConfig(AppManager.mContext).getPrivateCode());
                        }
                        if (mIsGroup) {
                            InitGroupData();
                        }
                    });
        }
    }

    private void InitGroupData() {
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
                if (mIsOwner) {// 由于查询群组列表不包含自己跟群主，需要自己手动添加
                    mSearchUserId = AppConfig.getAppConfig(AppManager.mContext).getPrivateCode();
                } else {
                    mSearchUserId = mGroupOwner.substring(3, mGroupOwner.length()) + "," + AppConfig.getAppConfig(AppManager.mContext).getPrivateCode();
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
        mAdapter = new CommonGroupControlAdapter(R.layout.item_common_person_add, mData);
        rvPersonShow.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvPersonShow.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        rvPersonShow.addOnItemTouchListener(new ItemClick());

        initGroupView();
    }

    private void initGroupView() {
        if (mIsGroup) {
            try {
                rlGroupPerson.setVisibility(View.VISIBLE);
                rlGroupName.setVisibility(View.VISIBLE);
                rlGroupPortrait.setVisibility(View.VISIBLE);
                btnChatDelete.setVisibility(View.VISIBLE);
                if (mGroupServer1 != null)
                    tvModificationName.setText(mGroupServer1.getGroupName());
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    @OnClick({R.id.btn_look_message_record, R.id.tv_clear_message_record, R.id.rl_group_person, R.id.rl_group_portrait})
    public void onViewClicked(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.btn_look_message_record:
                //查看聊天记录
                if (mIsGroup) {
                    intent.putExtra("chatType", EaseConstant.CHATTYPE_GROUP);
                    intent.putExtra("groupId", mGroupId);
                } else {
                    Bundle extras = getIntent().getParcelableExtra("EXTRAS");
                    intent.putExtra("chatType", EaseConstant.CHATTYPE_SINGLE);
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
                        Bundle extras = getIntent().getParcelableExtra("EXTRAS");
                        int chatType = extras.getInt("CHAT_TYPE", EaseConstant.CHATTYPE_SINGLE);
                        String toChatUsername = extras.getString("toChatUsername");
                        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(toChatUsername, EaseCommonUtils.getConversationType(chatType), true);
                        if (conversation != null) {
                            conversation.clearAllMessages();
                        }
                    }
                }, true).show();
                break;
            case R.id.rl_group_person:
                intent.setClass(this, GroupCommonControlActivity.class);
                break;
        }
        startActivity(intent);
    }

    @OnClick(R.id.btn_chat_delete)
    public void deleteChat() {

        //---------------------------------- 群组删除处理 ----------------------------------

        Observable.create(e -> {
            if (mIsOwner) {
                EMClient.getInstance().groupManager().destroyGroup(mGroupId);//解散群组
            } else {
                EMClient.getInstance().groupManager().leaveGroup(mGroupId);//退出群组
            }
            e.onComplete();
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(o -> {
                }, throwable -> {
                    if (mGroupOwner != null)
                        if (mIsOwner) {
                            Toast.makeText(this, "群组解散失败，请从新尝试！", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "群组退出失败，请从新尝试！", Toast.LENGTH_SHORT).show();
                        }
                    throwable.printStackTrace();
                }, () -> {
                    setResult(DELETESUCCESS);
                    finish();
                });
    }

    // ---------------------------------- 更新群组名称 ----------------------------------

    @OnClick(R.id.rl_group_name)
    public void modificationGroupName() {
        Intent intent = new Intent(this, ModificationGroupNameActivity.class);
        intent.putExtra("groupId",mGroupId);
        startActivityForResult(intent, REQUSET_CODE);
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
        tvErrorLayout.setVisibility(View.GONE);
        svContainerLayout.setVisibility(View.VISIBLE);
        try {
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
            if (userInfo.size() > 0)
                tvModificationPerson.setText(userInfo.get(0).getUsername());
        } catch (Throwable e) {
            e.printStackTrace();
        }
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
            switch (mData.get(i).getUsername()) {
                case "add":
                    intent.setClass(EaseChatDetailsActivity.this, SelectedChatAdminActivity.class);
                    break;
                case "delete":
                    intent.setClass(EaseChatDetailsActivity.this, GroupCommonControlActivity.class);
                    intent.putExtra("type", 0);
                    intent.putExtra("groupId", mGroupId);
                    if (!mIsOwner) {
                        intent.putExtra("groupOwner", mGroupOwner);
                    }
                    break;
                default:
                    intent.setClass(EaseChatDetailsActivity.this, Contact_Details_Activity.class);
                    String username = mData.get(i).getUsername();
                    String code = mData.get(i).getCode();
                    String email = mData.get(i).getEmail();
                    String img = mData.get(i).getImg();
                    String sex = mData.get(i).getSex();
                    int uid = mData.get(i).getUid();
                    final User user = new User(username, "", img, sex, "", code, "", "", "", email);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("user", user);
                    intent.putExtras(bundle);
                    break;
            }
            startActivity(intent);
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

            default:
                break;
        }
    }
}
