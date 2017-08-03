package com.shanlin.oa.ui.activity;

import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.db.FriendsInfoCacheSvc;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.shanlin.oa.R;
import com.shanlin.oa.common.Constants;
import com.shanlin.oa.manager.AppConfig;
import com.shanlin.oa.ui.base.BaseActivity;
import com.shanlin.oa.utils.StringUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * ProjectName: dev-beta-v1.0.1
 * PackageName: com.itcrm.GroupInformationPlatform.ui.activity
 * Author:Created by Tsui on Date:2016/12/16 15:10
 * Description:聊天界面 com.hyphenate.easeui.widget.EaseChatMessageList
 */
public class EaseChatMessageActivity extends BaseActivity {


    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.layout_root)
    RelativeLayout mRootView;
    private String u_id;
    private EaseChatFragment chatFragment;
    private String to_user_nike;
    private String to_user_pic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ease_chat_message);
        ButterKnife.bind(this);
        initToolBar();
        //TODO 暂时注释掉
//        setTranslucentStatus(this);
//        initWidget();
        initData();
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

        u_id = getIntent().getStringExtra("u_id");
        to_user_nike = getIntent().getStringExtra("usernike");
        to_user_pic = getIntent().getStringExtra("user_pic");
        if ("http://".equals(to_user_pic)) {
            to_user_pic = "";
        }

        boolean blank = StringUtils.isBlank(to_user_nike);
        if (!blank) {
            tvTitle.setText(to_user_nike);
        } else {
            if (!StringUtils.isBlank(String.valueOf(u_id))) {

                tvTitle.setText(FriendsInfoCacheSvc.getInstance(this).getNickName(u_id));
            } else {
                tvTitle.setText(u_id);
            }
        }

        //传入参数
        Bundle args = new Bundle();
        args.putInt(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
        //对方的信息
        args.putString(EaseConstant.EXTRA_USER_ID, u_id);
        args.putString("to_user_nike", to_user_nike);
        args.putString("to_user_pic", to_user_pic);

        //自己的信息
        args.putString("meId", Constants.CID + "_" + AppConfig.getAppConfig(this).getPrivateCode());
        args.putString("userName", AppConfig.getAppConfig(this).get(AppConfig.PREF_KEY_USERNAME));
        args.putString("userPic", AppConfig.getAppConfig(this).get(AppConfig.PREF_KEY_PORTRAITS));

        chatFragment = new EaseChatFragment();
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
}
