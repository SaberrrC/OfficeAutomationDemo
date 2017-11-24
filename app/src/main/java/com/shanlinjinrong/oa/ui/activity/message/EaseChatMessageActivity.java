package com.shanlinjinrong.oa.ui.activity.message;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.model.UserInfoDetailsBean;
import com.hyphenate.easeui.model.UserInfoSelfDetailsBean;
import com.hyphenate.easeui.onEaseUIFragmentListener;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.common.Constants;
import com.shanlinjinrong.oa.manager.AppConfig;
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
import butterknife.OnClick;

/**
 * ProjectName: dev-beta-v1.0.1
 * PackageName: com.itcrm.GroupInformationPlatform.ui.activity
 * Author:Created by Tsui on Date:2016/12/16 15:10
 * Description:聊天界面 com.hyphenate.easeui.widget.EaseChatMessageList
 */
public class EaseChatMessageActivity extends HttpBaseActivity<EaseChatMessagePresenter> implements EaseChatMessageContract.View, onEaseUIFragmentListener {

    @BindView(R.id.tv_count)
    TextView  mTvCount;
    @BindView(R.id.tv_title)
    TextView  mTvTitle;
    @BindView(R.id.iv_detail)
    ImageView mIvDetail;
    private String              u_id;
    private EaseChatFragment    chatFragment;
    private UserInfoDetailsBean mUserInfoDetailsBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ease_chat_message);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        setTitle(u_id);
        initCount();
    }

    private void setTitle(String u_id) {
        mTvTitle.setText(u_id);
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
        userInfo_self = getIntent().getStringExtra("userInfo_self");
        userInfo = getIntent().getStringExtra("userInfo");
        u_id = getIntent().getStringExtra("u_id");

        mUserInfoDetailsBean = new Gson().fromJson(userInfo, UserInfoDetailsBean.class);
        UserInfoSelfDetailsBean userInfoSelfDetailsBean = new Gson().fromJson(userInfo_self, UserInfoSelfDetailsBean.class);

        if (getIntent().getIntExtra("chatType", 0) == 2) {
            setTitle(getIntent().getStringExtra("groupName"));
        } else {
            if (mUserInfoDetailsBean != null && u_id.contains(mUserInfoDetailsBean.getCODE()))
                setTitle(mUserInfoDetailsBean.getUsername());
            else if (userInfoSelfDetailsBean != null && u_id.contains(userInfoSelfDetailsBean.getCODE_self())) {
                setTitle(userInfoSelfDetailsBean.getUsername_self());
            }
        }

        //传入参数
        Bundle args = new Bundle();
        args.putInt(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
        try {
            args.putString(EaseConstant.EXTRA_USER_ID, u_id);
            //对方的信息
            args.putString("to_user_code", getIntent().getStringExtra("code"));
            args.putString("to_user_nike", mUserInfoDetailsBean.getUsername() == null ? "" : mUserInfoDetailsBean.getUsername());
            args.putString("to_user_pic", mUserInfoDetailsBean.getPortrait() == null ? "" : mUserInfoDetailsBean.getPortrait());
            args.putString("to_user_department", mUserInfoDetailsBean.getDepartment_name());
            args.putString("to_user_post", mUserInfoDetailsBean.getPost_title());
            args.putString("to_user_sex", mUserInfoDetailsBean.getSex());
            args.putString("to_user_phone", mUserInfoDetailsBean.getPhone());
            args.putString("to_user_email", mUserInfoDetailsBean.getEmail());
            args.putString("userInfo", userInfo);
            args.putString("userInfo_self", userInfo_self);

            //自己的信息
            args.putString("user_code", AppConfig.getAppConfig(AppManager.mContext).get(AppConfig.PREF_KEY_CODE));
            args.putString("meId", Constants.CID + "_" + AppConfig.getAppConfig(AppManager.mContext).getPrivateCode());
            args.putString("userName", AppConfig.getAppConfig(AppManager.mContext).get(AppConfig.PREF_KEY_USERNAME));
            args.putString("userPic", AppConfig.getAppConfig(AppManager.mContext).get(AppConfig.PREF_KEY_PORTRAITS));
            args.putString("userSex", AppConfig.getAppConfig(AppManager.mContext).get(AppConfig.PREF_KEY_SEX));
            args.putString("userPhone", AppConfig.getAppConfig(AppManager.mContext).get(AppConfig.PREF_KEY_PHONE));
            args.putString("userPost", AppConfig.getAppConfig(AppManager.mContext).get(AppConfig.PREF_KEY_POST_NAME));
            args.putString("userDepartment", AppConfig.getAppConfig(AppManager.mContext).get(AppConfig.PREF_KEY_DEPARTMENT_NAME));
            args.putString("userEmail", AppConfig.getAppConfig(AppManager.mContext).get(AppConfig.PREF_KEY_USER_EMAIL));
            args.putString("userDepartmentId", AppConfig.getAppConfig(AppManager.mContext).get(AppConfig.PREF_KEY_DEPARTMENT));

        } catch (Throwable e) {
            e.printStackTrace();
        }
        String cmy = getIntent().getStringExtra("cmy");
        if (cmy == "cmy") {
            args.putString("to_user_code","sl_010122579");
            args.putString("to_user_nike","to_user_nike");
            args.putString("to_user_pic","to_user_pic");
            args.putString("to_user_department","to_user_department");
            args.putString("to_user_post","to_user_post");
            args.putString("to_user_sex","to_user_sex");
            args.putString("to_user_phone","to_user_phone");
            args.putString("to_user_email","to_user_email");
            args.putString("userInfo","userInfo");
            args.putString("userInfo_self","userInfo_self");

            args.putString("user_code","sl_010088966");
            args.putString("meId","meId");
            args.putString("userName","userName");
            args.putString("userPic","userPic");
            args.putString("userSex","userSex");
            args.putString("userPhone","userPhone");
            args.putString("userPost","userPost");
            args.putString("userDepartment","userDepartment");
            args.putString("userEmail","userEmail");
            args.putString("userDepartmentId","userDepartmentId");
        }
        chatFragment = new EaseChatFragment();
        chatFragment.setListener(this);
        chatFragment.setArguments(args);
        chatFragment.setArguments(getIntent().getExtras());

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
                intent.putExtra("name_self", AppConfig.getAppConfig(AppManager.mContext).get(AppConfig.PREF_KEY_USERNAME));
                intent.putExtra("portraits_self", AppConfig.getAppConfig(AppManager.mContext).get(AppConfig.PREF_KEY_PORTRAITS));
                intent.putExtra("name", mUserInfoDetailsBean.getUsername() == null ? "" : mUserInfoDetailsBean.getUsername());
                intent.putExtra("portraits", mUserInfoDetailsBean.getPortrait() == null ? "" : mUserInfoDetailsBean.getPortrait());
                startActivity(intent);
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
}