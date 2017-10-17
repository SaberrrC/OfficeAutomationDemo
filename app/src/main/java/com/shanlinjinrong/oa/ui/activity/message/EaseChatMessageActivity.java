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
import com.shanlinjinrong.oa.ui.activity.contracts.Contact_Details_Activity;
import com.shanlinjinrong.oa.ui.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * ProjectName: dev-beta-v1.0.1
 * PackageName: com.itcrm.GroupInformationPlatform.ui.activity
 * Author:Created by Tsui on Date:2016/12/16 15:10
 * Description:聊天界面 com.hyphenate.easeui.widget.EaseChatMessageList
 */
public class EaseChatMessageActivity extends BaseActivity implements onEaseUIFragmentListener {


    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.layout_root)
    LinearLayout mRootView;
    private String u_id;
    private EaseChatFragment chatFragment;
    //    private String to_user_nike;
//    private String to_code;
//    private String department_name;
//    private String post_name;
//    private String sex;
//    private String phone;
//    private String email;
//    private String to_user_pic;
    private static final int REQUEST_CODE_CONTEXT_MENU = 14;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ease_chat_message);
        ButterKnife.bind(this);
        initToolBar();
        //TODO 暂时注释掉
//        setTranslucentStatus(this);
//        initWidget();
    }

    private void initWidget() {
        // * 解决透明状态栏下，布局无法自动拉起的问题  手动设置View的高度
        final View decorView = getWindow().getDecorView();
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                decorView.getWindowVisibleDisplayFrame(rect);
                int screenHeight = decorView.getRootView().getHeight();
                int heightDifferent = screenHeight - rect.bottom;
                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mRootView.getLayoutParams();
                lp.setMargins(0, 0, 0, heightDifferent);
                mRootView.requestLayout();
            }
        });
    }

    private void initData() {

//        to_user_nike = getIntent().getStringExtra("usernike");
//        to_user_pic = getIntent().getStringExtra("user_pic");

//        department_name = getIntent().getStringExtra("department_name");
//        post_name = getIntent().getStringExtra("post_name");
//        sex = getIntent().getStringExtra("sex");
//        phone = getIntent().getStringExtra("phone");
//        email = getIntent().getStringExtra("email");
//        to_code = getIntent().getStringExtra("code");
//        if ("http://".equals(to_user_pic)) {
//            to_user_pic = "";
//        }
//
//        boolean blank = StringUtils.isBlank(to_user_nike);
//        if (!blank) {
//            tvTitle.setText(to_user_nike);
//        } else {
//            if (!StringUtils.isBlank(String.valueOf(u_id))) {
//                tvTitle.setText(FriendsInfoCacheSvc.getInstance(this).getNickName(u_id));
//            }
//        }


        userInfo_self = getIntent().getStringExtra("userInfo_self");
        userInfo = getIntent().getStringExtra("userInfo");
        u_id = getIntent().getStringExtra("u_id");

        UserInfoSelfDetailsBean userInfoSelfDetailsBean = new Gson().fromJson(userInfo_self, UserInfoSelfDetailsBean.class);

        tvTitle.setText(userInfoSelfDetailsBean.getUsername_self());


        //传入参数
        Bundle args = new Bundle();
        args.putInt(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
        try {
            String code = getIntent().getStringExtra("code");
            args.putString(EaseConstant.EXTRA_USER_ID, u_id);
            //对方的信息
            args.putString("to_user_code", getIntent().getStringExtra("code"));
            args.putString("to_user_nike", userInfoSelfDetailsBean.getUsername_self());
            args.putString("to_user_pic", userInfoSelfDetailsBean.getPortrait_self());
            args.putString("to_user_department", userInfoSelfDetailsBean.getDepartment_name_self());
            args.putString("to_user_post", userInfoSelfDetailsBean.getPost_title_self());
            args.putString("to_user_sex", userInfoSelfDetailsBean.getSex_self());
            args.putString("to_user_phone", userInfoSelfDetailsBean.getPhone_self());
            args.putString("to_user_email", userInfoSelfDetailsBean.getEmail_self());


            args.putString("userInfo", userInfo);
            args.putString("userInfo_self", userInfo_self);

            //自己的信息
            args.putString("user_code", AppConfig.getAppConfig(this).get(AppConfig.PREF_KEY_CODE));
            args.putString("meId", Constants.CID + "_" + AppConfig.getAppConfig(this).getPrivateCode());
            args.putString("userName", AppConfig.getAppConfig(this).get(AppConfig.PREF_KEY_USERNAME));
            args.putString("userPic", AppConfig.getAppConfig(this).get(AppConfig.PREF_KEY_PORTRAITS));
            args.putString("userSex", AppConfig.getAppConfig(this).get(AppConfig.PREF_KEY_SEX));
            args.putString("userPhone", AppConfig.getAppConfig(this).get(AppConfig.PREF_KEY_PHONE));
            args.putString("userPost", AppConfig.getAppConfig(this).get(AppConfig.PREF_KEY_POST_NAME));
            args.putString("userDepartment", AppConfig.getAppConfig(this).get(AppConfig.PREF_KEY_DEPARTMENT_NAME));
            args.putString("userEmail", AppConfig.getAppConfig(this).get(AppConfig.PREF_KEY_USER_EMAIL));
            args.putString("userDepartmentId", AppConfig.getAppConfig(this).get(AppConfig.PREF_KEY_DEPARTMENT));

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
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public void voiceCallListener(String toChatUsername, EMMessage mEmMessage) {
        try {
            if (mEmMessage != null) {
                userInfo = mEmMessage.getStringAttribute("userInfo", "");
                userInfo_self = mEmMessage.getStringAttribute("userInfo_self", "");
                final UserInfoDetailsBean userInfoDetailsBean = new Gson().fromJson(userInfo, UserInfoDetailsBean.class);
                //final UserInfoSelfDetailsBean userInfoSelfDetailsBean = new Gson().fromJson(userInfo_self, UserInfoSelfDetailsBean.class);
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
        if (userinfo.equals("sl_sl_admin")) {
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
