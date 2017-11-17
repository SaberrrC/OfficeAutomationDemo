package com.shanlinjinrong.oa.ui.activity.message;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
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
import com.shanlinjinrong.oa.ui.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * ProjectName: dev-beta-v1.0.1
 * PackageName: com.itcrm.GroupInformationPlatform.ui.activity
 * Author:Created by Tsui on Date:2016/12/16 15:10
 * Description:聊天界面 com.hyphenate.easeui.widget.EaseChatMessageList
 */
public class EaseChatMessageActivity extends BaseActivity implements onEaseUIFragmentListener {


    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.layout_root)
    LinearLayout mRootView;
    private String u_id;
    private EaseChatFragment chatFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ease_chat_message);
        ButterKnife.bind(this);
        initToolBar();
    }

    private void initData() {
        userInfo_self = getIntent().getStringExtra("userInfo_self");
        userInfo = getIntent().getStringExtra("userInfo");
        u_id = getIntent().getStringExtra("u_id");

        UserInfoDetailsBean userInfoDetailsBean = new Gson().fromJson(userInfo, UserInfoDetailsBean.class);
        UserInfoSelfDetailsBean userInfoSelfDetailsBean = new Gson().fromJson(userInfo_self, UserInfoSelfDetailsBean.class);

        if (userInfoDetailsBean != null && u_id.contains(userInfoDetailsBean.getCODE()))
            tvTitle.setText(userInfoDetailsBean.getUsername());
        else if (userInfoSelfDetailsBean != null && u_id.contains(userInfoSelfDetailsBean.getCODE_self())) {
            tvTitle.setText(userInfoSelfDetailsBean.getUsername_self());
        }

        //传入参数
        Bundle args = new Bundle();
        args.putInt(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
        try {
            args.putString(EaseConstant.EXTRA_USER_ID, u_id);
            //对方的信息
            args.putString("to_user_code", getIntent().getStringExtra("code"));
            args.putString("to_user_nike", userInfoDetailsBean.getUsername() == null ? "" : userInfoDetailsBean.getUsername());
            args.putString("to_user_pic", userInfoDetailsBean.getPortrait() == null ? "" : userInfoDetailsBean.getPortrait());
            args.putString("to_user_department", userInfoDetailsBean.getDepartment_name());
            args.putString("to_user_post", userInfoDetailsBean.getPost_title());
            args.putString("to_user_sex", userInfoDetailsBean.getSex());
            args.putString("to_user_phone", userInfoDetailsBean.getPhone());
            args.putString("to_user_email", userInfoDetailsBean.getEmail());

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
        chatFragment = new EaseChatFragment();
        chatFragment.setListener(this);
        chatFragment.setArguments(args);

        getSupportFragmentManager().beginTransaction().replace(R.id.message_list, chatFragment).commit();
    }

    private void initToolBar() {
        if (toolbar == null) {
            return;
        }
        setTitle("");//必须在setSupportActionBar之前调用
        toolbar.setTitleTextColor(Color.parseColor("#000000"));
        setSupportActionBar(toolbar);
        tvTitle.setText(u_id);
        Toolbar.LayoutParams lp = new Toolbar.LayoutParams(
                Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        tvTitle.setLayoutParams(lp);

        toolbar.setNavigationIcon(R.drawable.toolbar_back);
        toolbar.setNavigationOnClickListener(view -> finish());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void voiceCallListener(String toChatUsername, EMMessage mEmMessage) {
        try {
            if (mEmMessage != null) {
                userInfo = mEmMessage.getStringAttribute("userInfo", "");
                userInfo_self = mEmMessage.getStringAttribute("userInfo_self", "");
                final UserInfoDetailsBean userInfoDetailsBean = new Gson().fromJson(userInfo, UserInfoDetailsBean.class);
                startActivity(new Intent(this, VoiceCallActivity.class)
                        .putExtra("nike", userInfoDetailsBean.username)
                        .putExtra("CODE", userInfoDetailsBean.CODE)
                        .putExtra("portrait", userInfoDetailsBean.portrait)
                        .putExtra("post_name", userInfoDetailsBean.post_title)
                        .putExtra("sex", userInfoDetailsBean.sex)
                        .putExtra("phone", userInfoDetailsBean.phone)
                        .putExtra("email", userInfoDetailsBean.email)
                        .putExtra("department_name", userInfoDetailsBean.department_name)
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
}
