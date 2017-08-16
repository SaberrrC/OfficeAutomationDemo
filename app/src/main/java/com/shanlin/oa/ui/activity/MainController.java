package com.shanlin.oa.ui.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Pair;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.UserInfoBean;
import com.hyphenate.easeui.db.Friends;
import com.hyphenate.easeui.db.FriendsInfoCacheSvc;
import com.hyphenate.easeui.ui.EaseConversationListFragment;
import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.SDKOptions;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.uinfo.UserInfoProvider;
import com.pgyersdk.update.PgyUpdateManager;
import com.shanlin.oa.R;
import com.shanlin.oa.WelcomePage;
import com.shanlin.oa.common.Api;
import com.shanlin.oa.common.Constants;
import com.shanlin.oa.manager.AppConfig;
import com.shanlin.oa.manager.AppManager;
import com.shanlin.oa.manager.DoubleClickExitHelper;
import com.shanlin.oa.ui.base.BaseActivity;
import com.shanlin.oa.ui.fragment.TabCommunicationFragment;
import com.shanlin.oa.ui.fragment.TabContactsFragment;
import com.shanlin.oa.ui.fragment.TabHomePageFragment;
import com.shanlin.oa.ui.fragment.TabMeFragment;
import com.shanlin.oa.ui.fragment.TabMsgListFragment;
import com.shanlin.oa.utils.BadgeUtil;
import com.shanlin.oa.utils.LogUtils;
import com.shanlin.oa.utils.ScreenUtils;
import com.shanlin.oa.utils.SharedPreferenceUtil;
import com.shanlin.oa.utils.Utils;

import org.json.JSONObject;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import q.rorbin.badgeview.QBadgeView;

//import com.hyphenate.chatuidemo.db.Friends;
//import com.hyphenate.chatuidemo.db.FriendsInfoCacheSvc;
//import com.hyphenate.chatuidemo.ui.EaseConversationListFragment;
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
public class MainController extends BaseActivity {

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
    private DoubleClickExitHelper doubleClickExitHelper;
    private EaseConversationListFragment conversationListFragment;

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
        LoginIm();
        initEaseData();
        initmControllerAndSetAdapter();
        doubleClickExitHelper = new DoubleClickExitHelper(this);
        judeIsInitPwd();//判断是否是初始密码
        applyPermission();//判断是否有更新
    }


    /**
     * 初始化viewPager控制器并设置适配器
     */
    private void initmControllerAndSetAdapter() {
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
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 登录
                String account = Constants.CID + "_" + AppConfig.getAppConfig(
                        MainController.this).get(AppConfig.PREF_KEY_CODE);
                String token = AppConfig.getAppConfig(MainController.this).get(
                        AppConfig.PREF_KEY_YX_TOKEN);
                LogUtils.e("account:" + account + ",token:" + token);
                if (loginRequest == null) {
                    loginRequest = NIMClient.getService(AuthService.class).login(new LoginInfo(
                            account, token));

                    loginRequest.setCallback(new RequestCallback<LoginInfo>() {
                        @Override
                        public void onSuccess(LoginInfo param) {
                            LogUtils.e("云信login success。。。");
                            loginRequest = null;
                        }

                        @Override
                        public void onFailed(int code) {
                            loginRequest = null;
                            LogUtils.e("云信login Failed。。。" + code);
                        }

                        @Override
                        public void onException(Throwable exception) {
                            loginRequest = null;
                            LogUtils.e("云信login Failed。。。" + exception);
                        }
                    });
                }
            }
        }
        ).start();
    }

    // 如果返回值为 null，则全部使用默认参数。
    private SDKOptions options() {
        SDKOptions options = new SDKOptions();

        // 如果将新消息通知提醒托管给 SDK 完成，需要添加以下配置。否则无需设置。
        StatusBarNotificationConfig config = new StatusBarNotificationConfig();
        config.notificationEntrance = WelcomePage.class; // 点击通知栏跳转到该Activity
        config.notificationSmallIconId = R.mipmap.ic_launcher;
        // 呼吸灯配置
        config.ledARGB = Color.GREEN;
        config.ledOnMs = 1000;
        config.ledOffMs = 1500;
        // 通知铃声的uri字符串
        config.notificationSound = "android.resource://com.netease.nim.demo/raw/msg";
        options.statusBarNotificationConfig = config;

        // 配置保存图片，文件，log 等数据的目录
        // 如果 options 中没有设置这个值，SDK 会使用下面代码示例中的位置作为 SDK 的数据目录。
        // 该目录目前包含 log, file, image, audio, video, thumb 这6个目录。
        // 如果第三方 APP 需要缓存清理功能， 清理这个目录下面个子目录的内容即可。
        String sdkPath = Environment.getExternalStorageDirectory() + "/" + getPackageName() + "/nim";
        options.sdkStorageRootPath = sdkPath;

        // 配置是否需要预下载附件缩略图，默认为 true
        options.preloadAttach = true;

        // 配置附件缩略图的尺寸大小。表示向服务器请求缩略图文件的大小
        // 该值一般应根据屏幕尺寸来确定， 默认值为 Screen.width / 2
        options.thumbnailSize = ScreenUtils.getScreenWidth(this) / 2;

        // 用户资料提供者, 目前主要用于提供用户资料，用于新消息通知栏中显示消息来源的头像和昵称
        options.userInfoProvider = new UserInfoProvider() {
            @Override
            public UserInfo getUserInfo(String account) {
                return null;
            }

            @Override
            public int getDefaultIconResId() {
                return R.mipmap.ic_launcher;
            }

            @Override
            public Bitmap getTeamIcon(String tid) {
                return null;
            }

            @Override
            public Bitmap getAvatarForMessageNotifier(String account) {
                return null;
            }

            @Override
            public String getDisplayNameForMessageNotifier(String account, String sessionId,
                                                           SessionTypeEnum sessionType) {
                return null;
            }
        };
        return options;
    }

    public void LoginIm() {
        try {
            LogUtils.e("登录Im，工号->" + AppConfig.getAppConfig(this).get(AppConfig.PREF_KEY_CODE));
            EMClient.getInstance().login(Constants.CID + "_" +
                    AppConfig.getAppConfig(this).get(AppConfig.PREF_KEY_CODE), "123456", new EMCallBack() {//回调

                @Override
                public void onSuccess() {
                    EMClient.getInstance().groupManager().loadAllGroups();
                    EMClient.getInstance().chatManager().loadAllConversations();
                    LogUtils.e("登录聊天服务器成功！");
                    String u_id = Constants.CID + "_" + AppConfig.getAppConfig(MainController.this).getPrivateCode();
                    String u_name = AppConfig.getAppConfig(MainController.this).get(AppConfig.PREF_KEY_USERNAME);
                    String u_pic = AppConfig.getAppConfig(MainController.this).get(AppConfig.PREF_KEY_PORTRAITS);
                    String sex = AppConfig.getAppConfig(MainController.this).get(AppConfig.PREF_KEY_SEX);
                    String phone = AppConfig.getAppConfig(MainController.this).get(AppConfig.PREF_KEY_PHONE);
                    String post = AppConfig.getAppConfig(MainController.this).get(AppConfig.PREF_KEY_POST_NAME);
                    String department = AppConfig.getAppConfig(MainController.this).get(AppConfig.PREF_KEY_DEPARTMENT_NAME);
                    String email = AppConfig.getAppConfig(MainController.this).get(AppConfig.PREF_KEY_USER_EMAIL);
                    String departmentId = AppConfig.getAppConfig(MainController.this).get(AppConfig.PREF_KEY_DEPARTMENT);
                    FriendsInfoCacheSvc.getInstance(MainController.this)
                            .addOrUpdateFriends(new Friends(u_id, u_name, u_pic, sex, phone, post, department, email,departmentId));
                }

                @Override
                public void onProgress(int progress, String status) {

                }

                @Override
                public void onError(int code, String message) {
                    LogUtils.e("登录聊天服务器失败！" + "code:" + code + "," + message);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    /**
     * 刷新通讯小红点现实的数量
     */
    public void refreshCommCount() {
        new Thread(new Runnable() {
            @Override
            public void run() {
//                try {
                loadConversationList();
//                } catch (Exception e) {
//                    LogUtils.e("加载环信抛异常了");
//                }
            }
        }).start();
    }

    /**
     * 刷新消息小红点现实的数量
     */
    public void refreshUnReadMsgCount() {
        LogUtils.e("刷新msg的小红点方法执行");
        new Thread(new Runnable() {
            @Override
            public void run() {
                loadUnReadMsg();
            }
        }).start();

    }

    /**
     * 获取未读消息数量
     */
    private void loadUnReadMsg() {
//
        HttpParams params = new HttpParams();
        params.put("token", AppConfig.getAppConfig(this).getPrivateToken());
        params.put("uid", AppConfig.getAppConfig(this).getPrivateUid());
        initKjHttp().post(Api.TAB_UN_READ_MSG_COUNT, params, new HttpCallBack() {
            @Override
            public void onSuccess(String t) {
                LogUtils.e(t);
                super.onSuccess(t);
                try {
                    JSONObject jo = new JSONObject(t);

                    switch (Api.getCode(jo)) {
                        case Api.RESPONSES_CODE_OK:
                            JSONObject jsonObject = Api.getDataToJSONObject(jo);
                            String num = jsonObject.getString("num");
                            tvMsgUnRead.setVisibility(View.VISIBLE);
                            tvMsgUnRead.setText(num);
                            break;
                        case Api.RESPONSES_CODE_DATA_EMPTY:
                            tvMsgUnRead.setVisibility(View.GONE);
                            break;
                        case Api.RESPONSES_CODE_UID_NULL:
                            catchWarningByCode(Api.getCode(jo));
                            break;
                    }

                } catch (Exception e) {
                }
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
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
                        userInfoBean.userDepartment, userInfoBean.userEmail,userInfoBean.userDepartmentId));
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

    protected List<EMConversation> loadConversationList() {
        // get all conversations
        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
        if (conversations.isEmpty()) {

        }
        List<Pair<Long, EMConversation>> sortList = new ArrayList<>();
        synchronized (conversations) {
            for (EMConversation conversation : conversations.values()) {
                if (conversation.getAllMessages().size() != 0) {
                    //java.lang.NullPointerException: Attempt to invoke virtual method
                    // 'long com.hyphenate.chat.EMMessage.getMsgTime()' on a null object reference
                    sortList.add(new Pair<Long, EMConversation>(conversation.getLastMessage().getMsgTime(), conversation));
                }
            }
        }
        List<EMConversation> list = new ArrayList<EMConversation>();
        for (Pair<Long, EMConversation> sortItem : sortList) {
            list.add(sortItem.second);
        }
        int tempCount = 0;
        for (int i = 0; i < list.size(); i++) {
            int size = list.get(i).getUnreadMsgCount();
            tempCount = tempCount + size;
        }
        tempMsgCount = tempCount;
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
        return list;
    }

    //存储权限判断
    private void applyPermission() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (SharedPreferenceUtil.getShouldAskPermission(this, "firstshould") &&
                    ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE) == false) {//第一次已被拒绝
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("权限开启");
                builder.setMessage("更新功能无法正常使用，去权限列表开启该权限");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startAppSettig();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
            } else {//
                SharedPreferenceUtil.setShouldAskPermission(this, "firstshould",
                        ActivityCompat.shouldShowRequestPermissionRationale(this,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE));
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
            }

        } else {
//            PgyUpdateManager.setIsForced(true);
            PgyUpdateManager.register(this, "com.shanlin.oa.provider.fileprovider");
        }
    }

    //开启权限列表
    private void startAppSettig() {
        Intent i = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        i.setData(uri);
        startActivityForResult(i, 100);
    }

    //位置权限回调
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 100:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    PgyUpdateManager.register(this, "com.shanlin.oa.provider.fileprovider");
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