package com.shanlinjinrong.oa.ui.activity.message;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.model.UserInfoDetailsBean;
import com.hyphenate.easeui.onEaseUIFragmentListener;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.shanlinjinrong.oa.R;
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
    private String mTitle;
    private EaseChatFragment chatFragment;
    private UserInfoDetailsBean mUserInfoDetailsBean;
    private int mChatType;
    private int CHAT_GROUP = 2;
    private final int REQUEST_CODE = 101, RESULT_CODE = -2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ease_chat_message);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initCount();
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

        mChatType = getIntent().getIntExtra("chatType", 0);
        //TODO 暂做更改
//        userInfo_self = getIntent().getStringExtra("userInfo_self");
//        userInfo = getIntent().getStringExtra("userInfo");
        mTitle = getIntent().getStringExtra("title");
//        mUserInfoDetailsBean = new Gson().fromJson(userInfo, UserInfoDetailsBean.class);
//        UserInfoSelfDetailsBean userInfoSelfDetailsBean = new Gson().fromJson(userInfo_self, UserInfoSelfDetailsBean.class);

        if (mChatType == CHAT_GROUP) {
            mTvTitle.setText(getIntent().getStringExtra("groupName"));
        } else {
            //TODO 暂作修改 消息透传
//            if (mUserInfoDetailsBean != null && u_id.contains(mUserInfoDetailsBean.getCODE()))
//                mTvTitle.setText(mUserInfoDetailsBean.getUsername());
//            else if (userInfoSelfDetailsBean != null && u_id.contains(userInfoSelfDetailsBean.getCODE_self())) {
//                mTvTitle.setText(userInfoSelfDetailsBean.getUsername_self());
//            }
            mTvTitle.setText(mTitle);
        }
        try {

            //TODO 消息透传 修改
//            args.putString(EaseConstant.EXTRA_USER_ID, u_id);
//            //对方的信息
//            args.putString("to_user_code", getIntent().getStringExtra("code"));
//            args.putString("to_user_nike", mUserInfoDetailsBean.getUsername() == null ? "" : mUserInfoDetailsBean.getUsername());
//            args.putString("to_user_pic", mUserInfoDetailsBean.getPortrait() == null ? "" : mUserInfoDetailsBean.getPortrait());
//            args.putString("to_user_department", mUserInfoDetailsBean.getDepartment_name());
//            args.putString("to_user_post", mUserInfoDetailsBean.getPost_title());
//            args.putString("to_user_sex", mUserInfoDetailsBean.getSex());
//            args.putString("to_user_phone", mUserInfoDetailsBean.getPhone());
//            args.putString("to_user_email", mUserInfoDetailsBean.getEmail());

//            args.putString("userInfo", userInfo);
//            args.putString("userInfo_self", userInfo_self);

//            //自己的信息
//            args.putString("user_code", AppConfig.getAppConfig(AppManager.mContext).get(AppConfig.PREF_KEY_CODE));
//            args.putString("meId", Constants.CID + "_" + AppConfig.getAppConfig(AppManager.mContext).getPrivateCode());
//            args.putString("userName", AppConfig.getAppConfig(AppManager.mContext).get(AppConfig.PREF_KEY_USERNAME));
//            args.putString("userPic", AppConfig.getAppConfig(AppManager.mContext).get(AppConfig.PREF_KEY_PORTRAITS));
//            args.putString("userSex", AppConfig.getAppConfig(AppManager.mContext).get(AppConfig.PREF_KEY_SEX));
//            args.putString("userPhone", AppConfig.getAppConfig(AppManager.mContext).get(AppConfig.PREF_KEY_PHONE));
//            args.putString("userPost", AppConfig.getAppConfig(AppManager.mContext).get(AppConfig.PREF_KEY_POST_NAME));
//            args.putString("userDepartment", AppConfig.getAppConfig(AppManager.mContext).get(AppConfig.PREF_KEY_DEPARTMENT_NAME));
//            args.putString("userEmail", AppConfig.getAppConfig(AppManager.mContext).get(AppConfig.PREF_KEY_USER_EMAIL));
//            args.putString("userDepartmentId", AppConfig.getAppConfig(AppManager.mContext).get(AppConfig.PREF_KEY_DEPARTMENT));

        } catch (Throwable e) {
            e.printStackTrace();
        }
        chatFragment = new EaseChatFragment();
        chatFragment.setListener(this);
        Bundle extras = getIntent().getExtras();
        extras.putString("PAGE_TYPE", "CHAT");
        extras.putString("PAGE_TYPE", "HISTORY");
        extras.putInt("CHAT_TYPE", mChatType);
        chatFragment.setArguments(extras);
        getSupportFragmentManager().beginTransaction().replace(R.id.message_list, chatFragment).commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void voiceCallListener(String toChatUsername, EMMessage mEmMessage) {
        try {
            if (mEmMessage != null) {
                userInfo = mEmMessage.getStringAttribute("userInfo", "");
                userInfo_self = mEmMessage.getStringAttribute("userInfo_self", "");
                final UserInfoDetailsBean userInfoDetailsBean = new Gson().fromJson(userInfo, UserInfoDetailsBean.class);
                startActivity(new Intent(this, VoiceCallActivity.class).putExtra("nike", userInfoDetailsBean.username).putExtra("CODE", userInfoDetailsBean.CODE).putExtra("portrait", userInfoDetailsBean.portrait).putExtra("post_name", userInfoDetailsBean.post_title).putExtra("sex", userInfoDetailsBean.sex).putExtra("phone", userInfoDetailsBean.phone).putExtra("email", userInfoDetailsBean.email).putExtra("department_name", userInfoDetailsBean.department_name).putExtra("username", toChatUsername).putExtra("isomingCall", false));
            } else {
                startActivity(new Intent(this, VoiceCallActivity.class).putExtra("username", toChatUsername).putExtra("isomingCall", false));
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    String userInfo_self;
    String userInfo;

    @Override
    public void clickUserInfo(String userinfo, EMMessage emMessage) {
        if (userinfo.contains("admin") || userinfo.contains("notice")) {
            return;
        }
        Intent intent = new Intent(this, Contact_Details_Activity.class);
        intent.putExtra("user_code", userinfo);
        intent.putExtra("isSession", true);
        userInfo = emMessage.getStringAttribute("userInfo", "");
        userInfo_self = emMessage.getStringAttribute("userInfo_self", "");
        intent.putExtra("userInfo_self", userInfo_self);
        intent.putExtra("userInfo", userInfo);
        startActivity(intent);
    }


    @Override
    protected void onResume() {
        super.onResume();
        initData();
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
        if (resultCode == RESULT_CODE) {
            setResult(RESULT_CODE);
            finish();
        }
    }
}