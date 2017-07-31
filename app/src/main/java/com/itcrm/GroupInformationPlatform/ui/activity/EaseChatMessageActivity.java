package com.itcrm.GroupInformationPlatform.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.db.FriendsInfoCacheSvc;
import com.hyphenate.easeui.ui.ContextMenuActivity;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.easeui.widget.chatrow.EaseCustomChatRowProvider;
import com.itcrm.GroupInformationPlatform.R;
import com.itcrm.GroupInformationPlatform.common.Api;
import com.itcrm.GroupInformationPlatform.common.Constants;
import com.itcrm.GroupInformationPlatform.manager.AppConfig;
import com.itcrm.GroupInformationPlatform.model.Contacts;
import com.itcrm.GroupInformationPlatform.ui.adapter.TabContactsAdapter;
import com.itcrm.GroupInformationPlatform.ui.base.BaseActivity;
import com.itcrm.GroupInformationPlatform.utils.LogUtils;
import com.itcrm.GroupInformationPlatform.utils.StringUtils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * ProjectName: dev-beta-v1.0.1
 * PackageName: com.itcrm.GroupInformationPlatform.ui.activity
 * Author:Created by Tsui on Date:2016/12/16 15:10
 * Description:聊天界面 com.hyphenate.easeui.widget.EaseChatMessageList
 */
public class EaseChatMessageActivity extends BaseActivity  {


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
    private List<Contacts> items = new ArrayList<>();

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


        //new出EaseChatFragment或其子类的实例
        chatFragment = new EaseChatFragment();
        /**
         * 长按消息监听，用于复制消息
         */
        chatFragment.setChatFragmentListener(new EaseChatFragment.EaseChatFragmentHelper(){
            @Override
            public void onSetMessageAttributes(EMMessage message) {
                Log.i("easechat","onSetMessageAttributes");
            }

            @Override
            public void onEnterToChatDetails() {
                Log.i("easechat","onEnterToChatDetails");
            }

            @Override
            public void onAvatarClick(String username) {
                Log.i("easechat","onAvatarClick"+username);

            }

            @Override
            public void onAvatarLongClick(String username) {
                Log.i("easechat","onAvatarLongClick");
            }

            @Override
            public boolean onMessageBubbleClick(EMMessage message) {
                Log.i("easechat","onMessageBubbleClick");
                return false;
            }

            @Override
            public void onMessageBubbleLongClick(EMMessage message) {
                // no message forward when in chat room
                try {
                    startActivityForResult((new Intent(EaseChatMessageActivity.this, ContextMenuActivity.class)).putExtra("message",message)
                                    .putExtra("ischatroom", chatFragment.getType() == EaseConstant.CHATTYPE_CHATROOM),
                            chatFragment.getBackCode());
                }catch (Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            public boolean onExtendMenuItemClick(int itemId, View view) {
                Log.i("easechat","onExtendMenuItemClick");
                return false;
            }

            @Override
            public EaseCustomChatRowProvider onSetCustomChatRowProvider() {
                Log.i("easechat","EaseCustomChatRowProvider");
                return null;
            }

        });







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


    /**
     * 加载通讯录数据
     *
     * @param departmentId 部门ID
     */
    private void loadData(String departmentId) {
        showLoadingView();
        HttpParams params = new HttpParams();
        params.put("uid", AppConfig.getAppConfig(this).getPrivateUid());
        params.put("token", AppConfig.getAppConfig(this).getPrivateToken());
        params.put("department_id", departmentId);
        initKjHttp().post(Api.GET_CONTACTS, params, new HttpCallBack() {
            @Override
            public void onFinish() {
                super.onFinish();
                hideLoadingView();
            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                LogUtils.e(t.toString());

                try {
                    JSONObject jo = new JSONObject(t);
                    switch (Api.getCode(jo)) {
                        case Api.RESPONSES_CODE_OK:
                            if (items.size() > 0 || items != null) {
                                items.clear();
                            }
                            JSONArray jDepartment = Api.getDataToJSONObject(jo)
                                    .getJSONArray("department");
                            for (int i = 0; i < jDepartment.length(); i++) {
                                JSONObject d = jDepartment.getJSONObject(i);
                                Contacts c = new Contacts(d);
                                items.add(c);
                            }
                            JSONArray jEmployee = Api.getDataToJSONObject(jo)
                                    .getJSONArray("employee");
                            for (int i = 0; i < jEmployee.length(); i++) {
                                JSONObject e = jEmployee.getJSONObject(i);
                                Contacts c = new Contacts(e);
                                items.add(c);
                            }
                            break;
                        case Api.RESPONSES_CODE_DATA_EMPTY:
//                            showEmptyView(mRlRecyclerViewContainer, "您还没有联系人", 0, false);
                            break;
                        case Api.RESPONSES_CODE_TOKEN_NO_MATCH:
                            catchWarningByCode(Api.getCode(jo));
                            break;
                    }
                } catch (JSONException e) {
                    System.out.println(e.toString());
                }
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                hideLoadingView();
                String info = "";
                switch (errorNo) {
                    case Api.RESPONSES_CODE_NO_NETWORK:
                        info = "请确认是否已连接网络！";
                        break;
                    case Api.RESPONSES_CODE_NO_RESPONSE:
                        info = "网络不稳定，请重试！";
                        break;
                }
                super.onFailure(errorNo, strMsg);
            }
        });
    }


}
