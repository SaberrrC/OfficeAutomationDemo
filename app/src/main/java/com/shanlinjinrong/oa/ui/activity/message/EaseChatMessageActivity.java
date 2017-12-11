package com.shanlinjinrong.oa.ui.activity.message;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.db.FriendsInfoCacheSvc;
import com.hyphenate.easeui.model.UserInfoDetailsBean;
import com.hyphenate.easeui.onEaseUIFragmentListener;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.manager.AppManager;
import com.shanlinjinrong.oa.ui.activity.contracts.Contact_Details_Activity;
import com.shanlinjinrong.oa.ui.activity.message.contract.EaseChatMessageContract;
import com.shanlinjinrong.oa.ui.activity.message.event.UpdateMessageCountEvent;
import com.shanlinjinrong.oa.ui.activity.message.presenter.EaseChatMessagePresenter;
import com.shanlinjinrong.oa.ui.base.HttpBaseActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;

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
    private String mTitle;
    private EaseChatFragment chatFragment;
    private int mChatType;
    private int CHAT_GROUP = 2;
    private final int REQUEST_CODE = 101, DELETESUCCESS = -2, RESULTMODIFICATIONNAME = -3;
    private Bundle mExtras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ease_chat_message);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initCount();
        initData();
        initView();
    }


    private void initCount() {
        int tempCount = EMClient.getInstance().chatManager().getUnreadMsgsCount();
        if (tempCount == 0) {
            mTvCount.setText("");
            return;
        }
        mTvCount.setText("消息(" + tempCount + ")");
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
        mExtras.putInt("CHAT_TYPE", mChatType);
        chatFragment.setArguments(mExtras);
        getSupportFragmentManager().beginTransaction().replace(R.id.message_list, chatFragment).commit();
    }

    private void initView() {
        mChatType = getIntent().getIntExtra("chatType", 0);
        mTitle = getIntent().getStringExtra("title");//人名字

        if (mChatType == CHAT_GROUP) {
            mTvTitle.setText(getIntent().getStringExtra("groupName"));
        } else {
            mTvTitle.setText(mTitle);
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
                        .putExtra("nike",nike)
                        .putExtra("CODE",CODE)
                        .putExtra("portrait", portrait)
                        .putExtra("post_name",post_name)
                        .putExtra("sex",sex)
                        .putExtra("phone",phone)
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


    @OnClick({R.id.iv_back, R.id.tv_count, R.id.iv_detail})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_count:
                finish();
                break;
            case R.id.iv_detail:
                Intent intent = new Intent(this, EaseChatDetailsActivity.class);
                if (mChatType == CHAT_GROUP) {
                    intent.putExtra("chatType", true);
                    intent.putExtra("groupId", getIntent().getStringExtra("toChatUsername"));
                } else {
                    intent.putExtra("chatType", false);
                    intent.putExtra("EXTRAS", mExtras);
                }
                startActivityForResult(intent, REQUEST_CODE);
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setCount(UpdateMessageCountEvent event) {
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
}