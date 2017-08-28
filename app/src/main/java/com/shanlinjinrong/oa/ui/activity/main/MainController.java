package com.shanlinjinrong.oa.ui.activity.main;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.UserInfoBean;
import com.hyphenate.easeui.db.Friends;
import com.hyphenate.easeui.db.FriendsInfoCacheSvc;
import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.pgyersdk.update.PgyUpdateManager;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.common.Constants;
import com.shanlinjinrong.oa.manager.AppConfig;
import com.shanlinjinrong.oa.manager.AppManager;
import com.shanlinjinrong.oa.ui.activity.main.contract.MainControllerContract;
import com.shanlinjinrong.oa.ui.activity.main.presenter.MainControllerPresenter;
import com.shanlinjinrong.oa.ui.activity.my.ModifyPwdActivity;
import com.shanlinjinrong.oa.ui.base.MyBaseActivity;
import com.shanlinjinrong.oa.ui.fragment.TabCommunicationFragment;
import com.shanlinjinrong.oa.ui.fragment.TabContactsFragment;
import com.shanlinjinrong.oa.ui.fragment.TabHomePageFragment;
import com.shanlinjinrong.oa.ui.fragment.TabMeFragment;
import com.shanlinjinrong.oa.ui.fragment.TabMsgListFragment;
import com.shanlinjinrong.oa.utils.BadgeUtil;
import com.shanlinjinrong.oa.utils.Utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import q.rorbin.badgeview.QBadgeView;

/**
 * ━━━━━━神兽出没━━━━━━
 * 　　　┏┓　　┏┓
 * 　　┏┛┻━━━┛┻┓
 * 　　┃　　　　　　　┃
 * 　　┃　　　━　　　┃
 * 　　┃　┳┛　┗┳　┃
 * 　　┃　　　　　　　┃
 * 　　┃　　　┻　　　┃
 * 　　┃　　　　　　　┃
 * 　　┗━┓　　　┏━┛
 * 　　　　┃　　　┃  神兽保佑
 * 　　　　┃　　　┃  代码无bug
 * 　　　　┃　　　┗━━━┓
 * 　　　　┃　　　　　　　┣┓
 * 　　　　┃　　　　　　　┏┛
 * 　　　　┗┓┓┏━┳┓┏┛
 * 　　　　　┃┫┫　┃┫┫
 * 　　　　　┗┻┛　┗┻┛
 * ━━━━━━感觉萌萌哒━━━━━━
 */

/**
 * <h3>Description: 首页控制器 </h3>
 * <b>Notes:</b> Created by KevinMeng on 2016/8/26.<br />
 */
public class MainController extends MyBaseActivity<MainControllerPresenter> implements MainControllerContract.View {

    @Bind(R.id.controller)
    ViewPager mController;
    @Bind(R.id.tab_message_icon)
    ImageView tabMessageIcon;
    @Bind(R.id.tab_message_text)
    TextView tabMessageText;
    @Bind(R.id.tab_contacts_icon)
    ImageView tabContactsIcon;
    @Bind(R.id.tab_contacts_text)
    TextView tabContactsText;
    @Bind(R.id.tab_group_icon)
    ImageView tabGroupIcon;
    @Bind(R.id.tab_group_text)
    TextView tabGroupText;
    @Bind(R.id.tab_me_icon)
    ImageView tabMeIcon;
    @Bind(R.id.tab_me_text)
    TextView tabMeText;
    @Bind(R.id.tab_home_text)
    TextView tabHomeText;
    @Bind(R.id.tv_msg_unread)
    TextView tvMsgUnRead;


    @Bind(R.id.tab_message_icon_light)
    ImageView tabMessageIconLight;

    @Bind(R.id.tab_home_icon)
    ImageView tabHomeIcon;

    @Bind(R.id.tab_home_icon_light)
    ImageView tabHomeIconLight;

    @Bind(R.id.tab_contacts_icon_light)
    ImageView tabContactsIconLight;

    @Bind(R.id.tab_group_icon_light)
    ImageView tabGroupIconLight;

    @Bind(R.id.tab_me_icon_light)
    ImageView tabMeIconLight;

    @Bind(R.id.view_message_remind)
    View communicationRedSupport;


    private List<Fragment> mTabs;
    private FragmentPagerAdapter mAdapter;
    private static final int TAB_MESSAGE = 0;
    private static final int TAB_CONTACTS = 1;
    private static final int TAB_HOME = 2;
    private static final int TAB_GROUP = 3;
    private static final int TAB_ME = 4;

    //灰色以及相对应的RGB值
    private int mGrayColor;
    private int mGrayRed;
    private int mGrayGreen;
    private int mGrayBlue;
    //蓝色以及相对应的RGB值
    private int mBlueColor;
    private int mBlueRed;
    private int mBlueGreen;
    private int mBlueBlue;

    private ImageView[] mBorderimageViews;  //外部的边框
    private ImageView[] mContentImageViews; //内部的内容
    private TextView[] mTextViews;

    int tempMsgCount = 0;
    private QBadgeView qBadgeView;
    private AlertDialog dialog;
    TabCommunicationFragment tabCommunicationFragment;

    private AbortableFuture<LoginInfo> loginRequest;

    public static MainController instance = null;
    private EaseUI easeUI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        setContentView(R.layout.activity_main_controller);
        ButterKnife.bind(this);
        setTranslucentStatus(this);
        initWidget();
        initData();
        LoginIm();//登录环信
        initEaseData();//初始化登录云信
        initControllerAndSetAdapter();
        judeIsInitPwd();//判断是否是初始密码
        mPresenter.applyPermission(this);//判断是否有更新
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }


    /**
     * 初始化viewPager控制器并设置适配器
     */
    private void initControllerAndSetAdapter() {
        mController.setAdapter(mAdapter);
        mController.addOnPageChangeListener(new PageChangeListener());
        setSelection(TAB_HOME);
        mController.setCurrentItem(TAB_HOME, false);
    }


    private void initWidget() {
        initColor();
        mBorderimageViews = new ImageView[]{tabGroupIcon, tabContactsIcon, tabHomeIcon, tabMessageIcon,
                tabMeIcon};
        mContentImageViews = new ImageView[]{tabGroupIconLight, tabContactsIconLight, tabHomeIconLight,
                tabMessageIconLight, tabMeIconLight};

        mTextViews = new TextView[]{tabGroupText, tabContactsText, tabHomeText, tabMessageText, tabMeText
        };
    }

    private void initColor() {
        mGrayColor = getResources().getColor(R.color.tab_bar_text_gray);
        mGrayRed = Color.red(mGrayColor);
        mGrayGreen = Color.green(mGrayColor);
        mGrayBlue = Color.blue(mGrayColor);
        mBlueColor = getResources().getColor(R.color.tab_bar_text_light);
        mBlueRed = Color.red(mBlueColor);
        mBlueGreen = Color.green(mBlueColor);
        mBlueBlue = Color.blue(mBlueColor);
    }

    /**
     * 判断是否是初始密码
     */
    private void judeIsInitPwd() {
        //1初始密码，2已修改
        String isInitPwd = AppConfig.getAppConfig(this).get(AppConfig.PREF_KEY_IS_INIT_PWD);
        if (isInitPwd.equals("1")) {
            showSetPwdDialog();
        }
    }

    private void showSetPwdDialog() {
        View dialogView = View.inflate(this, R.layout.dialog_set_pwd_layout, null);
        Button btnSetPwd = (Button) dialogView.findViewById(R.id.btn_set_pwd);
        btnSetPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainController.this, ModifyPwdActivity.class);
                startActivity(intent);
                dialog.dismiss();
                MainController.this.finish();
            }
        });
        dialog = new AlertDialog.Builder(this, R.style.AppTheme_Dialog).create();
        dialog.setView(dialogView);
        dialog.setCancelable(false);
        dialog.show();
    }

    /**
     * 设置索引  当前导航页边框绿色，内容实心绿，其他页边框灰色，内容透明
     *
     * @param position
     */
    private void setSelection(int position) {
        for (int i = 0; i < mBorderimageViews.length; i++) {
            if (i == position) {
                mBorderimageViews[i].setColorFilter(mBlueColor, PorterDuff.Mode.SRC_IN);
                mContentImageViews[i].setAlpha(1f);
                mTextViews[i].setTextColor(mBlueColor);
            } else {
                mBorderimageViews[i].setColorFilter(mGrayColor, PorterDuff.Mode.SRC_IN);
                mContentImageViews[i].setAlpha(0f);
                mTextViews[i].setTextColor(mGrayColor);
            }
        }
    }


    /**
     * 初始化云信视频的相关数据
     */
    private void initEaseData() {
        String account = "SL_" + AppConfig.getAppConfig(
                MainController.this).get(AppConfig.PREF_KEY_CODE);
        String token = AppConfig.getAppConfig(MainController.this).get(
                AppConfig.PREF_KEY_YX_TOKEN);
        mPresenter.initEase(loginRequest, account, token);
    }


    //登录环信
    public void LoginIm() {
        mPresenter.loginIm(this);
    }

    public void refreshCommCount() {
        new Thread(new Runnable() {
            @Override
            public void run() {
//                try {
                mPresenter.loadConversationList();
//                } catch (Exception e) {
//                    LogUtils.e("加载环信抛异常了");
//                }
            }
        }).start();
    }

    public void refreshUnReadMsgCount() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mPresenter.loadUnReadMsg();
            }
        }).start();
    }

    @Override
    public void loadUnReadMsgOk(String num) {
        tvMsgUnRead.setVisibility(View.VISIBLE);
        tvMsgUnRead.setText(num);
    }

    @Override
    public void loadUnReadMsgEmpty() {
        tvMsgUnRead.setVisibility(View.GONE);
    }

    @Override
    public void uidNull(int code) {
        catchWarningByCode(code);
    }

    @Override
    public void bindBadgeView(int msgCount) {
        tempMsgCount = msgCount;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //绑定小红点
                if (qBadgeView == null) {
                    qBadgeView = new QBadgeView(AppManager.mContext);
                }
                qBadgeView.setBadgeGravity(Gravity.CENTER);
                qBadgeView.bindTarget(communicationRedSupport).setBadgeNumber(tempMsgCount);
            }
        });
    }


    class PageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            if (positionOffset > 0) {
                if (positionOffset < 0.5) {
                    //  滑动到一半前，上一页的边框保持绿色不变，下一页的边框由灰色变为绿色
                    mBorderimageViews[position].setColorFilter(mBlueColor, PorterDuff.Mode.SRC_IN);
                    mBorderimageViews[position + 1].setColorFilter(getGrayToBLue(positionOffset), PorterDuff.Mode.SRC_IN);
                    //   上一页的内容保持由实心变为透明，下一页的内容保持透明
                    mContentImageViews[position].setAlpha((1 - 2 * positionOffset));
                    mContentImageViews[position + 1].setAlpha(0f);
                    //文字颜色变化
                    mTextViews[position].setTextColor(mBlueColor);
                    mTextViews[position + 1].setTextColor(getGrayToBLue(positionOffset));

                } else {
                    //滑动到一半后，上一页的边框由绿色变为灰色，下一页边框保持绿色不变
                    mBorderimageViews[position].setColorFilter(getBlueToGray(positionOffset), PorterDuff.Mode.SRC_IN);
                    mBorderimageViews[position + 1].setColorFilter(mBlueColor, PorterDuff.Mode.SRC_IN);
                    //上一页的内容保持透明，下一页的内容由透明变为实心绿色
                    mContentImageViews[position].setAlpha(0f);
                    mContentImageViews[position + 1].setAlpha(2 * positionOffset - 1);
                    mTextViews[position].setTextColor(getBlueToGray(positionOffset));
                    mTextViews[position + 1].setTextColor(mBlueColor);
                }
            }

        }

        @Override
        public void onPageSelected(int position) {
            setSelection(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }

    /**
     * 偏移量在 0——0.5区间 ，左边一项颜色不变，右边一项颜色从灰色变为蓝色，根据两点式算出变化函数
     *
     * @param positionOffset
     * @return
     */
    private int getGrayToBLue(float positionOffset) {
        int red = (int) (positionOffset * (mBlueRed - mGrayRed) * 2 + mGrayRed);
        int green = (int) (positionOffset * (mBlueGreen - mGrayGreen) * 2 + mGrayGreen);
        int blue = (int) ((positionOffset) * (mBlueBlue - mGrayBlue) * 2 + mGrayBlue);
        return Color.argb(255, red, green, blue);
    }

    /**
     * 偏移量在 0.5--1 区间，颜色从蓝色变成灰色，根据两点式算出变化函数
     *
     * @param positionOffset
     * @return
     */
    private int getBlueToGray(float positionOffset) {
        int red = (int) (positionOffset * (mGrayRed - mBlueRed) * 2 + 2 * mBlueRed - mGrayRed);
        int green = (int) (positionOffset * (mGrayGreen - mBlueGreen) * 2 + 2 * mBlueGreen - mGrayGreen);
        int blue = (int) (positionOffset * (mGrayBlue - mBlueBlue) * 2 + 2 * mBlueBlue - mGrayBlue);
        return Color.argb(255, red, green, blue);
    }

    @OnClick({R.id.tab_message_icon, R.id.tab_message_text, R.id.tab_message_box,
            R.id.tab_contacts_icon, R.id.tab_contacts_text, R.id.tab_contacts_box,
            R.id.tab_group_icon, R.id.tab_group_text, R.id.tab_group_box, R.id.tab_me_icon,
            R.id.tab_me_text, R.id.tab_me_box, R.id.tab_home_box, R.id.tab_home_icon})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tab_message_icon:
            case R.id.tab_message_text:
            case R.id.tab_message_box:
                setSelection(TAB_GROUP);
                mController.setCurrentItem(TAB_GROUP, false);
                break;
            case R.id.tab_contacts_icon:
            case R.id.tab_contacts_text:
            case R.id.tab_contacts_box:
                setSelection(TAB_CONTACTS);
                mController.setCurrentItem(TAB_CONTACTS, false);
                break;
            case R.id.tab_group_icon:
            case R.id.tab_group_text:
            case R.id.tab_group_box:
                setSelection(TAB_MESSAGE);
                mController.setCurrentItem(TAB_MESSAGE, false);
                break;
            case R.id.tab_me_icon:
            case R.id.tab_me_text:
            case R.id.tab_me_box:
                setSelection(TAB_ME);
                mController.setCurrentItem(TAB_ME, false);
                break;
            case R.id.tab_home_icon:
            case R.id.tab_home_box:
                setSelection(TAB_HOME);
                mController.setCurrentItem(TAB_HOME, false);
                break;
        }
    }

    /**
     * 初始化数据
     */
    private void initData() {
        //检测推送页面
        easeUI = EaseUI.getInstance();
        //极光推送设置别名和部门
        String uid = AppConfig.getAppConfig(this).getPrivateUid();
        if (uid != null) {
            JPushInterface.setAlias(this, uid, new TagAliasCallback() {
                //在TagAliasCallback 的 gotResult 方法，返回对应的参数 alias, tags。并返回对应的状态码：0为成功，其他返回码请参考错误码定义
                @Override
                public void gotResult(int i, String s, Set<String> set) {
                }
            });
            Set<String> set = new HashSet<>();
            set.add(AppConfig.getAppConfig(this).getDepartmentId());
            JPushInterface.setTags(this, set, null);
        }
        mTabs = new ArrayList<>();


        tabCommunicationFragment = new TabCommunicationFragment();
        mTabs.add(tabCommunicationFragment);

        TabContactsFragment tabContactsFragment = new TabContactsFragment();
        mTabs.add(tabContactsFragment);

        TabHomePageFragment tabHomePageFragment = new TabHomePageFragment();
        mTabs.add(tabHomePageFragment);


        TabMsgListFragment tabMessageFragment = new TabMsgListFragment();
        mTabs.add(tabMessageFragment);

        TabMeFragment tabMeFragment = new TabMeFragment();
        mTabs.add(tabMeFragment);

        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

            //获取下标
            @Override
            public Fragment getItem(int position) {
                return mTabs.get(position);
            }

            @Override
            public int getCount() {
                return mTabs.size();
            }
        };
    }


    @Override
    protected void onResume() {
        judeIsInitPwd();
        //添加环信监听
        EMClient.getInstance().chatManager().addMessageListener(messageListener);
        refreshCommCount();
        refreshUnReadMsgCount();
        super.onResume();
    }

    @Override
    protected void onStop() {
        if (!Utils.isRunningForeground(this)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (tempMsgCount != 0) {
                        BadgeUtil.setBadgeCount(MainController.this, tempMsgCount, R.drawable.ring_red);
                    }
                }
            }).start();
        }
        super.onStop();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    EMMessageListener messageListener = new EMMessageListener() {
        @Override
        public void onMessageReceived(final List<EMMessage> list) {
            if (tabCommunicationFragment != null) {
                if (tabCommunicationFragment.myConversationListFragment != null) {
                    tabCommunicationFragment.myConversationListFragment.refresh();
                }
            }
            /**
             * im通知，具有通知功能
             */
            for (EMMessage message : list) {

                if (!easeUI.hasForegroundActivies()) {
                    easeUI.getNotifier().onNewMsg(message);
                }
                //获取个人信息
                String userInfo = message.getStringAttribute("userInfo", "");
                UserInfoBean userInfoBean = new Gson().fromJson(userInfo, UserInfoBean.class);
                FriendsInfoCacheSvc.getInstance(AppManager.mContext).addOrUpdateFriends(new
                        Friends(userInfoBean.userId, userInfoBean.userName, userInfoBean.userPic,
                        userInfoBean.userSex, userInfoBean.userPhone, userInfoBean.userPost,
                        userInfoBean.userDepartment, userInfoBean.userEmail, userInfoBean.userDepartmentId));
            }
            refreshCommCount();
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> list) {

        }

        @Override
        public void onMessageRead(List<EMMessage> list) {

        }

        @Override
        public void onMessageDelivered(List<EMMessage> list) {

        }

        @Override
        public void onMessageRecalled(List<EMMessage> list) {

        }


        @Override
        public void onMessageChanged(EMMessage emMessage, Object o) {

        }
    };

    //开启权限列表
    public void startAppSetting() {
        Intent i = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        i.setData(uri);
        startActivityForResult(i, 100);
    }

    @Override
    public void easeInitFinish(AbortableFuture<LoginInfo> loginRequest) {
        this.loginRequest = loginRequest;
    }

    //位置权限回调
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 100:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    PgyUpdateManager.register(this, "com.shanlinjinrong.oa.fileprovider");
                } else {
                    Toast.makeText(this, "该权限被禁用 无法更新！", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        ButterKnife.unbind(this);
        EMClient.getInstance().chatManager().removeMessageListener(messageListener);
        super.onDestroy();
    }

}