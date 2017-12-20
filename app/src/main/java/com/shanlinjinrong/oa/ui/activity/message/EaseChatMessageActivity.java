package com.shanlinjinrong.oa.ui.activity.message;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.db.Friends;
import com.hyphenate.easeui.db.FriendsInfoCacheSvc;
import com.hyphenate.easeui.event.OnConversationFinishEvent;
import com.hyphenate.easeui.onEaseUIFragmentListener;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.easeui.widget.EaseAlertDialog;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.common.Constants;
import com.shanlinjinrong.oa.manager.AppManager;
import com.shanlinjinrong.oa.ui.activity.contracts.Contact_Details_Activity;
import com.shanlinjinrong.oa.ui.activity.main.bean.UserDetailsBean;
import com.shanlinjinrong.oa.ui.activity.main.event.UnReadMessageEvent;
import com.shanlinjinrong.oa.ui.activity.message.bean.GroupEventListener;
import com.shanlinjinrong.oa.ui.activity.message.contract.EaseChatMessageContract;
import com.shanlinjinrong.oa.ui.activity.message.presenter.EaseChatMessagePresenter;
import com.shanlinjinrong.oa.ui.base.HttpBaseActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * ProjectName: dev-beta-v1.0.1
 * PackageName: com.itcrm.GroupInformationPlatform.ui.activity
 * Author:Created by Tsui on Date:2016/12/16 15:10
 * Description:聊天界面 com.hyphenate.easeui.widget.EaseChatMessageList
 */
public class EaseChatMessageActivity extends HttpBaseActivity<EaseChatMessagePresenter> implements EaseChatMessageContract.View, onEaseUIFragmentListener {

    @BindView(R.id.tv_count)
    TextView mTvCount;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.iv_detail)
    LinearLayout mIvDetail;
    @BindView(R.id.img_details_icon)
    ImageView imgDetailsIcon;

    private String mTitle;
    private int mChatType;
    private Bundle mExtras;
    private EaseChatFragment chatFragment;
    private final int REQUEST_CODE = 101, DELETESUCCESS = -2, RESULTMODIFICATIONNAME = -3;
    private long lastClickTime = 0;
    private EMGroup mGroup;
    private boolean mIsResume;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ease_chat_message);
        ButterKnife.bind(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        initData();
        initView();
    }


    private void initCount() {
        int tempCount = EMClient.getInstance().chatManager().getUnreadMsgsCount();
        if (tempCount <= 0) {
            mTvCount.setText("");
            return;
        }
        if (tempCount > 99) {
            mTvCount.setText("消息(99+)");
            return;
        }
        mTvCount.setText("消息(" + tempCount + ")");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setTitle(UnReadMessageEvent event) {
        if (event.unReadCount <= 0) {
            mTvCount.setText("");
            return;
        }
        if (event.unReadCount > 99) {
            mTvCount.setText("消息(99+)");
            return;
        }
        mTvCount.setText("消息(" + event.unReadCount + ")");
        //        EventBus.getDefault().removeStickyEvent(event);
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    private void initData() {
        //传入参数
        chatFragment = new EaseChatFragment();
        chatFragment.setListener(this);
        mExtras = getIntent().getExtras();
        mExtras.putInt("CHAT_TYPE", getIntent().getIntExtra("CHAT_TYPE", 1));
        mExtras.putString(EaseConstant.EXTRA_USER_ID, getIntent().getStringExtra("u_id"));
        chatFragment.setArguments(mExtras);
        getSupportFragmentManager().beginTransaction().replace(R.id.message_list, chatFragment).commit();
    }

    private void initView() {
        mChatType = getIntent().getIntExtra(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
        mTitle = getIntent().getStringExtra("title");//人名字
        if (mChatType == EaseConstant.CHATTYPE_GROUP) {
            if (getIntent().getStringExtra("groupTitle") != null) {
                if (!getIntent().getStringExtra("groupTitle").equals("")) {
                    mTvTitle.setText(getIntent().getStringExtra("groupTitle"));
                } else {
                    remoteGroupName();
                }
            } else {
                remoteGroupName();
            }
            imgDetailsIcon.setImageResource(R.mipmap.icon_chat_group_list);
        } else {
            if (mTitle.equals("")) {
                mPresenter.searchUserDetails(getIntent().getStringExtra("u_id").substring(3, getIntent().getStringExtra("u_id").length()));
            }
            mTvTitle.setText(mTitle);
            imgDetailsIcon.setImageResource(R.mipmap.icon_contacts_details);
        }
    }

    private void remoteGroupName() {
        try {
            String u_id = getIntent().getStringExtra("u_id");
            String groupName = FriendsInfoCacheSvc.getInstance(AppManager.mContext).getNickName(u_id);
            mTvTitle.setText(groupName);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    @OnClick({R.id.iv_back, R.id.tv_count, R.id.iv_detail})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_count:
                finish();
                break;
            case R.id.iv_detail:
                if (getIntent().getBooleanExtra("admin", false)) {
                    return;
                }
                long currentTime = Calendar.getInstance().getTimeInMillis();
                if (currentTime - lastClickTime < 1000) {
                    lastClickTime = currentTime;
                    return;
                }
                Intent intent = new Intent(this, EaseChatDetailsActivity.class);
                if (mChatType == EaseConstant.CHATTYPE_GROUP) {
                    intent.putExtra("chatType", true);
                    intent.putExtra(EaseConstant.GROUPID, getIntent().getStringExtra("u_id"));
                } else {
                    intent.putExtra("chatType", false);
                    intent.putExtra("EXTRAS", mExtras);
                    intent.putExtra("message_to", getIntent().getStringExtra("message_to"));
                    intent.putExtra("message_from", getIntent().getStringExtra("message_from"));
                }
                startActivityForResult(intent, REQUEST_CODE);
                break;
        }
    }

    @Override
    public void voiceCallListener(String toChatUsername, EMMessage mEmMessage) {
        try {
            if (mEmMessage != null) {
                String nike = FriendsInfoCacheSvc.getInstance(AppManager.mContext).getNickName(getIntent().getStringExtra("u_id"));
                String CODE = FriendsInfoCacheSvc.getInstance(AppManager.mContext).getUserId(getIntent().getStringExtra("u_id"));
                String portrait = FriendsInfoCacheSvc.getInstance(AppManager.mContext).getPortrait(getIntent().getStringExtra("u_id"));
                String post_name = FriendsInfoCacheSvc.getInstance(AppManager.mContext).getPost(getIntent().getStringExtra("u_id"));
                String sex = FriendsInfoCacheSvc.getInstance(AppManager.mContext).getSex(getIntent().getStringExtra("u_id"));
                String phone = FriendsInfoCacheSvc.getInstance(AppManager.mContext).getPhone(getIntent().getStringExtra("u_id"));
                String email = FriendsInfoCacheSvc.getInstance(AppManager.mContext).getEmail(getIntent().getStringExtra("u_id"));
                String department_name = FriendsInfoCacheSvc.getInstance(AppManager.mContext).getDepartment(getIntent().getStringExtra("u_id"));

                startActivity(new Intent(this, VoiceCallActivity.class)
                        .putExtra("nike", nike)
                        .putExtra("CODE", CODE)
                        .putExtra("portrait", portrait)
                        .putExtra("post_name", post_name)
                        .putExtra("sex", sex)
                        .putExtra("phone", phone)
                        .putExtra("email", email)
                        .putExtra("department_name", department_name)
                        .putExtra("username", toChatUsername)
                        .putExtra("isomingCall", false));
            } else {
                startActivity(new Intent(this, VoiceCallActivity.class)
                        .putExtra("username", toChatUsername)
                        .putExtra("isomingCall", false));
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void clickUserInfo(String userinfo, EMMessage emMessage) {
        if (userinfo.contains("admin") || userinfo.contains("notice")) {
            return;
        }
        Intent intent = new Intent(this, Contact_Details_Activity.class);
        intent.putExtra("user_code", userinfo);
        intent.putExtra("isSession", true);
        startActivity(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setCountFirst(OnConversationFinishEvent event) {
        initCount();
    }

    @Override
    public void uidNull(int code) {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case DELETESUCCESS:
                setResult(DELETESUCCESS);
                finish();
                break;
            case RESULTMODIFICATIONNAME:
                mTitle = data.getStringExtra("groupName");
                mTvTitle.setText(mTitle);
                //刷新界面
                setResult(DELETESUCCESS);
                break;
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
                remoteGroupName();
                break;
            case Constants.GROUPDISSOLVE:
                if (!event.isEvent() && mIsResume) {
                    EaseAlertDialog alertDialog = new EaseAlertDialog(this, null, "群组已经解散", null, (confirmed, bundle) -> {
                        event.setEvent(true);
                        setResult(DELETESUCCESS);
                        finish();
                    }, false);
                    alertDialog.setCancelable(false);
                    alertDialog.show();
                }
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

    @Override
    public void searchUserDetailsSuccess(UserDetailsBean.DataBean userDetailsBean) {
        FriendsInfoCacheSvc.getInstance(AppManager.mContext).addOrUpdateFriends(new
                Friends("sl_" + userDetailsBean.getCode(), userDetailsBean.getUsername(), "http://" + userDetailsBean.getImg(),
                userDetailsBean.getSex(), userDetailsBean.getPhone(), userDetailsBean.getPostname(),
                userDetailsBean.getOrgan(), userDetailsBean.getEmail(), userDetailsBean.getOid()));
        mTvTitle.setText(userDetailsBean.getUsername());
    }

    @Override
    public void searchUserDetailsFailed() {

    }
}