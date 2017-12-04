package com.shanlinjinrong.oa.ui.activity.message;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.db.FriendsInfoCacheSvc;
import com.hyphenate.easeui.onEaseUIFragmentListener;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.shanlinjinrong.oa.R;
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

    private String mTitleName;
    private String mUserCode;
    private EaseChatFragment chatFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ease_chat_message);
        ButterKnife.bind(this);
        initToolBar();
    }

    private void initData() {
        mTitleName = getIntent().getStringExtra("title");
        mUserCode = getIntent().getStringExtra("u_id");
        tvTitle.setText(mTitleName);

        //传入参数
        Bundle args = new Bundle();
        args.putInt(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
        args.putString(EaseConstant.EXTRA_USER_ID, mUserCode);
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
        tvTitle.setText(mTitleName);
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

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }
}
