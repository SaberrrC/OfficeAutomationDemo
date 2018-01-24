package com.shanlinjinrong.oa.ui.activity.main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.retrofit.net.RetrofitConfig;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.db.Friends;
import com.hyphenate.easeui.db.FriendsInfoCacheSvc;
import com.hyphenate.easeui.event.OnCountRefreshEvent;
import com.pgyersdk.update.PgyUpdateManager;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.common.Constants;
import com.shanlinjinrong.oa.manager.AppConfig;
import com.shanlinjinrong.oa.manager.AppManager;
import com.shanlinjinrong.oa.ui.activity.main.bean.AppVersionBean;
import com.shanlinjinrong.oa.ui.activity.main.contract.MainControllerContract;
import com.shanlinjinrong.oa.ui.activity.main.event.UnReadMessageEvent;
import com.shanlinjinrong.oa.ui.activity.main.presenter.MainControllerPresenter;
import com.shanlinjinrong.oa.ui.activity.message.bean.GroupEventListener;
import com.shanlinjinrong.oa.ui.activity.my.ModifyPwdActivity;
import com.shanlinjinrong.oa.ui.base.HttpBaseActivity;
import com.shanlinjinrong.oa.ui.fragment.TabCommunicationFragment;
import com.shanlinjinrong.oa.ui.fragment.TabContactsFragment;
import com.shanlinjinrong.oa.ui.fragment.TabHomePageFragment;
import com.shanlinjinrong.oa.ui.fragment.TabMeFragment;
import com.shanlinjinrong.oa.ui.fragment.TabMsgListFragment;
import com.shanlinjinrong.oa.utils.LoginUtils;
import com.shanlinjinrong.oa.utils.VersionManagementUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import me.leolin.shortcutbadger.ShortcutBadger;
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
public class MainActivity extends HttpBaseActivity<MainControllerPresenter> implements MainControllerContract.View {

    @BindView(R.id.controller)
    ViewPager mController;
    @BindView(R.id.tab_message_icon)
    ImageView tabMessageIcon;
    @BindView(R.id.tab_message_text)
    TextView  tabMessageText;
    @BindView(R.id.tab_contacts_icon)
    ImageView tabContactsIcon;
    @BindView(R.id.tab_contacts_text)
    TextView  tabContactsText;
    @BindView(R.id.tab_group_icon)
    ImageView tabGroupIcon;
    @BindView(R.id.tab_group_text)
    TextView  tabGroupText;
    @BindView(R.id.tab_me_icon)
    ImageView tabMeIcon;
    @BindView(R.id.tab_me_text)
    TextView  tabMeText;
    @BindView(R.id.tab_home_text)
    TextView  tabHomeText;
    @BindView(R.id.tv_msg_unread)
    TextView  tvMsgUnRead;


    @BindView(R.id.tab_message_icon_light)
    ImageView tabMessageIconLight;

    @BindView(R.id.tab_home_icon)
    ImageView tabHomeIcon;

    @BindView(R.id.tab_home_icon_light)
    ImageView tabHomeIconLight;

    @BindView(R.id.tab_contacts_icon_light)
    ImageView tabContactsIconLight;

    @BindView(R.id.tab_group_icon_light)
    ImageView tabGroupIconLight;

    @BindView(R.id.tab_me_icon_light)
    ImageView tabMeIconLight;

    @BindView(R.id.view_message_remind)
    View communicationRedSupport;


    private List<Fragment>       mTabs;
    private FragmentPagerAdapter mAdapter;
    private static final int TAB_MESSAGE  = 0;
    private static final int TAB_CONTACTS = 1;
    private static final int TAB_HOME     = 2;
    private static final int TAB_GROUP    = 3;
    private static final int TAB_ME       = 4;

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

    private TextView[]  mTextViews;
    private ImageView[] mBorderimageViews;  //外部的边框
    private ImageView[] mContentImageViews; //内部的内容

    int tempMsgCount = 0;
    private EaseUI                   easeUI;
    private AlertDialog              dialog;
    private QBadgeView               qBadgeView;
    private TabCommunicationFragment tabCommunicationFragment;
    private List<EMMessage> mEMMessage = new ArrayList<>();
    private EMGroup            mGroup;
    private String             mGroupName;
    private EMMessage.ChatType chatType;
    private String             mUserName;
    private String             mQueryInfo;
    private String             mNickName;
    private long               lastNotifiyTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_controller);
        EventBus.getDefault().register(this);
        ButterKnife.bind(this);
        setTranslucentStatus(this);
        initWidget();
        initData();
        LoginIm();//登录环信
        initControllerAndSetAdapter();
        judeIsInitPwd();//判断是否是初始密码
        mPresenter.getAppEdition();
        mPresenter.applyPermission(this);//判断是否有更新
        ShortcutBadger.removeCount(MainActivity.this);
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.RECORD_AUDIO}, 10);
        }
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }


    /**
     * 初始化viewPager控制器并设置适配器
     */
    private void initControllerAndSetAdapter() {
        //设置缓存页数
        mController.setOffscreenPageLimit(4);
        mController.setAdapter(mAdapter);
        mController.addOnPageChangeListener(new PageChangeListener());
        setSelection(TAB_HOME);
        mController.setCurrentItem(TAB_HOME, false);
    }


    private void initWidget() {
        initColor();
        mBorderimageViews = new ImageView[]{tabGroupIcon, tabContactsIcon, tabHomeIcon, tabMessageIcon, tabMeIcon};
        mContentImageViews = new ImageView[]{tabGroupIconLight, tabContactsIconLight, tabHomeIconLight, tabMessageIconLight, tabMeIconLight};

        mTextViews = new TextView[]{tabGroupText, tabContactsText, tabHomeText, tabMessageText, tabMeText};
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
//        String isInitPwd = AppConfig.getAppConfig(AppManager.mContext).get(AppConfig.PREF_KEY_IS_INIT_PWD);
//        if (isInitPwd.equals("1")) {
//            showSetPwdDialog();
//        }
    }

    private void showSetPwdDialog() {
        View dialogView = View.inflate(this, R.layout.dialog_set_pwd_layout, null);
        Button btnSetPwd = (Button) dialogView.findViewById(R.id.btn_set_pwd);
        btnSetPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ModifyPwdActivity.class);
                startActivity(intent);
                dialog.dismiss();
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


    //登录环信
    public void LoginIm() {
        //注册一个监听连接状态的listener
        EMClient.getInstance().addConnectionListener(new MyConnectionListener());
        LoginUtils.loginIm(AppManager.mContext, null);
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
    public void uidNull(String code) {
        catchWarningByCode(code);
    }


    //刷新消息数量
    private final int     BIND_BADGE_REFRESH = -1;
    @SuppressLint("HandlerLeak")
    protected     Handler handler            = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case BIND_BADGE_REFRESH:
                    if (qBadgeView == null) {//绑定小红点
                        qBadgeView = new QBadgeView(AppManager.mContext);
                    }
                    qBadgeView.setBadgeGravity(Gravity.CENTER);
                    qBadgeView.bindTarget(communicationRedSupport).setBadgeNumber(tempMsgCount);
                    //设置Title 数量
                    EventBus.getDefault().post(new UnReadMessageEvent(tempMsgCount));
                    break;
                default:
                    break;
            }
        }
    };

    public void bindBadgeView(int count) {
        tempMsgCount = count;
        handler.sendEmptyMessage(BIND_BADGE_REFRESH);

        //更新Icon 数量
        if (tempMsgCount != 0) {
            ShortcutBadger.applyCount(MainActivity.this, tempMsgCount);
            return;
        }
        ShortcutBadger.removeCount(MainActivity.this);
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

    @OnClick({R.id.tab_message_icon, R.id.tab_message_text, R.id.tab_message_box, R.id.tab_contacts_icon, R.id.tab_contacts_text, R.id.tab_contacts_box, R.id.tab_group_icon, R.id.tab_group_text, R.id.tab_group_box, R.id.tab_me_icon, R.id.tab_me_text, R.id.tab_me_box, R.id.tab_home_box, R.id.tab_home_icon, R.id.view_home_box})
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
            case R.id.view_home_box:
                setSelection(TAB_HOME);
                mController.setCurrentItem(TAB_HOME, false);
                break;
        }
    }

    /**
     * 初始化数据
     */
    private void initData() {
        RetrofitConfig.setAuthToken(AppConfig.getAppConfig(AppManager.mContext).getPrivateToken());
        RetrofitConfig.setUserId(AppConfig.getAppConfig(AppManager.mContext).getPrivateUid());
        RetrofitConfig.getInstance().setUserCode(AppConfig.getAppConfig(AppManager.mContext).getPrivateCode());
        //检测推送页面
        easeUI = EaseUI.getInstance();
        //极光推送设置别名和部门
        String uid = AppConfig.getAppConfig(AppManager.mContext).getPrivateUid();
        if (uid != null) {
            JPushInterface.setAlias(this, uid, new TagAliasCallback() {
                //在TagAliasCallback 的 gotResult 方法，返回对应的参数 alias, tags。并返回对应的状态码：0为成功，其他返回码请参考错误码定义
                @Override
                public void gotResult(int i, String s, Set<String> set) {
                    Log.i("MainActivity", "MainActivity : + gotResult");
                }
            });
            Set<String> set = new HashSet<>();
            set.add(AppConfig.getAppConfig(AppManager.mContext).getDepartmentId());
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
        super.onResume();
        judeIsInitPwd();
        Observable.create(e ->
                EMClient.getInstance().chatManager()
                        .addMessageListener(messageListener))
                .subscribeOn(Schedulers.io()).subscribe();
        if (tabCommunicationFragment != null) {
            if (tabCommunicationFragment.myConversationListFragment != null) {
                tabCommunicationFragment.myConversationListFragment.refresh();
            }
        }
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
            refreshChat(list);
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> list) {
            for (EMMessage msg : list) {
                EMCmdMessageBody cmdMessage = (EMCmdMessageBody) msg.getBody();
                String action = cmdMessage.action();
                if (action.equals("UPDATE_GROUP_INFO")) {
                    String groupName = msg.getStringAttribute("groupName", "");
                    String groupId = msg.getStringAttribute("groupId", "");
                    FriendsInfoCacheSvc.getInstance(AppManager.mContext).addOrUpdateFriends(new Friends(groupId, groupName, ""));
                    EventBus.getDefault().post(new GroupEventListener(Constants.MODIFICATIONNAME));
                }
            }
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

    private void refreshChat(List<EMMessage> list) {
        if (tabCommunicationFragment != null) {
            if (tabCommunicationFragment.myConversationListFragment != null) {
                tabCommunicationFragment.myConversationListFragment.refresh();
            }
        }

        if (!easeUI.hasForegroundActivies()) {
            if (list.size() > 0) {
                easeUI.getNotifier().onNewMsg(list.get(list.size() - 1));
            }
        }
    }

    //开启权限列表

    public void startAppSetting() {
        Intent i = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        i.setData(uri);
        startActivityForResult(i, 100);
    }

    @Override
    public void getAppEditionSuccess(AppVersionBean mAppVersionBean) {
        String androidVersion = mAppVersionBean.getData().getAndroidVersion();
        if (androidVersion.startsWith("v")) {
            androidVersion = androidVersion.substring(1);
        }
        String androidUrl = mAppVersionBean.getData().getAndroidUrl();
        mAppVersionBean.getData().getAndroidUrl();
        String isForceUpdate = mAppVersionBean.getData().getAndroidIsForceUpdate();
        try {
            String appVersionName = VersionManagementUtil.getVersion(MainActivity.this);
            if (appVersionName.startsWith("v")) {
                appVersionName = appVersionName.substring(1);
            }
            int i = VersionManagementUtil.VersionComparison(androidVersion, appVersionName);
            if (i == 1) {//有更新
                if (isForceUpdate.equals("1")) {//强制更新
                    showUpdateDialog(true, androidUrl);
                } else {
                    showUpdateDialog(false, androidUrl);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void showUpdateDialog(boolean iosIsForceUpdate, String androidUrl) {
        View dialogView = LayoutInflater.from(MainActivity.this).inflate(R.layout.public_dialog, null);
        TextView title = (TextView) dialogView.findViewById(R.id.title);
        title.setText("版本更新");
        TextView message = (TextView) dialogView.findViewById(R.id.message);
        if (iosIsForceUpdate) {
            message.setText("请更新至最新版本");
        } else {
            message.setText("是否更新至最新版本");
        }
        final AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this,
                R.style.AppTheme_Dialog).create();
        alertDialog.setView(dialogView);
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse(androidUrl);
                intent.setData(content_url);
                startActivity(intent);
            }
        };
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "更新", listener);
        if (!iosIsForceUpdate) {
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "取消",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
        }
        alertDialog.show();

        if (iosIsForceUpdate) {
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    Uri content_url = Uri.parse(androidUrl);
                    intent.setData(content_url);
                    startActivity(intent);

                }
            });
            alertDialog.setCancelable(false);
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(
                    getResources().getColor(R.color.btn_text_logout));
        } else {
            alertDialog.setCancelable(true);
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(
                    getResources().getColor(R.color.btn_text_logout));
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(
                    getResources().getColor(R.color.btn_text_logout));
        }

    }

    //位置权限回调
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 100:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    try {
                        PgyUpdateManager.register(new WeakReference<Activity>(this).get(), "com.shanlinjinrong.oa.fileprovider");
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(new WeakReference<Activity>(this).get(), "该权限被禁用 无法更新！", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        EMClient.getInstance().chatManager().removeMessageListener(messageListener);
        //   BadgeUtil.setBadgeCount(MainActivity.this, 0, R.drawable.ring_red);
        ShortcutBadger.removeCount(MainActivity.this);
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshCount(OnCountRefreshEvent event) {
        switch (event.getEvent()) {
            case 1:
                runOnUiThread(() -> {
                    if (event.getErrorCode() == EMError.USER_REMOVED) {
                        NonTokenDialog();
                    } else if (event.getErrorCode() == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                        NonTokenDialog();
                    }
                });
                break;
            default:
                bindBadgeView(event.getUnReadMsgCount());
                break;
        }

    }

    //实现ConnectionListener接口
    private static class MyConnectionListener implements EMConnectionListener {
        @Override
        public void onConnected() {
        }

        @Override
        public void onDisconnected(final int error) {
//            OnCountRefreshEvent onCountRefreshEvent = new WeakReference<>(new OnCountRefreshEvent()).get();
//            onCountRefreshEvent.setEvent(1);
//            onCountRefreshEvent.setErrorCode(error);
//            EventBus.getDefault().post(onCountRefreshEvent);
        }
    }
}
