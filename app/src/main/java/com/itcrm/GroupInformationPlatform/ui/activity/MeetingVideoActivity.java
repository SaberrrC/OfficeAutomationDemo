package com.itcrm.GroupInformationPlatform.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.itcrm.GroupInformationPlatform.R;
import com.itcrm.GroupInformationPlatform.common.Constants;
import com.itcrm.GroupInformationPlatform.manager.AppConfig;
import com.itcrm.GroupInformationPlatform.manager.AppManager;
import com.itcrm.GroupInformationPlatform.model.JoinVideoMember;
import com.itcrm.GroupInformationPlatform.ui.PermissionListener;
import com.itcrm.GroupInformationPlatform.ui.activity.netease.MsgHelper;
import com.itcrm.GroupInformationPlatform.ui.adapter.JoinVideoMeetingListAdapter;
import com.itcrm.GroupInformationPlatform.ui.base.BaseActivity;
import com.itcrm.GroupInformationPlatform.utils.LogUtils;
import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.avchat.AVChatCallback;
import com.netease.nimlib.sdk.avchat.AVChatManager;
import com.netease.nimlib.sdk.avchat.AVChatStateObserver;
import com.netease.nimlib.sdk.avchat.constant.AVChatType;
import com.netease.nimlib.sdk.avchat.constant.AVChatUserRole;
import com.netease.nimlib.sdk.avchat.constant.AVChatVideoCropRatio;
import com.netease.nimlib.sdk.avchat.constant.AVChatVideoScalingType;
import com.netease.nimlib.sdk.avchat.model.AVChatAudioFrame;
import com.netease.nimlib.sdk.avchat.model.AVChatCameraCapturer;
import com.netease.nimlib.sdk.avchat.model.AVChatChannelInfo;
import com.netease.nimlib.sdk.avchat.model.AVChatData;
import com.netease.nimlib.sdk.avchat.model.AVChatNetworkStats;
import com.netease.nimlib.sdk.avchat.model.AVChatParameters;
import com.netease.nimlib.sdk.avchat.model.AVChatSessionStats;
import com.netease.nimlib.sdk.avchat.model.AVChatVideoCapturerFactory;
import com.netease.nimlib.sdk.avchat.model.AVChatVideoFrame;
import com.netease.nimlib.sdk.avchat.model.AVChatVideoRender;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomInfo;
import com.netease.nimlib.sdk.chatroom.model.EnterChatRoomResultData;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.model.CustomNotification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <h3>Description: 视频会议 </h3>
 * <b>Notes:</b> Created by KevinMeng on 2016/8/31.<br />
 */
public class MeetingVideoActivity extends BaseActivity implements AVChatStateObserver {

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.video_layout)
    ViewGroup videoLayout;
    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.tv_member_ratio)
    TextView mTvMemberRatio;//成员比例
    @Bind(R.id.cb_sound)
    CheckBox mCbSound;//
    @Bind(R.id.rl_handler)
    RelativeLayout mRlHandler;
    @Bind(R.id.rl_leave_meeting)
    RelativeLayout mRlLeveRoom;//创建者没有该选项

    private String currentSpeakerMember;//当前说话成员

    private boolean videoImageTag = true;
    private boolean isCreate = true;//是否是创建者
    private String roomName;//会议名称(会议主题)
    private AbortableFuture<EnterChatRoomResultData> enterRequest;
    AVChatVideoRender memberRender; // 成员头像画布
    JoinVideoMeetingListAdapter mAdapter;//当前参会成员列表适配器
    /**
     * 聊天室基本信息
     */
    private ChatRoomInfo roomInfo;
    private String roomCreator;
    private String joinPeopleStr;//参会人
    private String createMen;//创建会议的人
    private List<JoinVideoMember> mLists = new ArrayList<>();//预期参加会议的人员
    private List<JoinVideoMember> mRealityLists = new ArrayList<>();//当前加入会议的人员
    private boolean isCanHand = true;//判断当前是否能举手
    private String myAccount;//当前自己的账号
    private String currentCreateAccount;//当前创建者的账号

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_video);
        //保持屏幕常亮
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        ButterKnife.bind(this);
        initView();
        initToolBar();
        setTranslucentStatus(this);
        initData();
        registerObservers(true);
    }

    private void initView() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(manager);
        mAdapter = new JoinVideoMeetingListAdapter(mRealityLists);
        mRecyclerView.setAdapter(mAdapter);


        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int position) {
                TextView tvState = (TextView) view.findViewById(R.id.tv_state);
                //要切换的成员头像
                String wantSpeakAccount = Constants.CID + "_" + mRealityLists.get(position).getUid();
                if (tvState.getText().toString().trim().equals("切换")) {
                    //同意对方的连麦请求
                    MsgHelper.getInstance().sendP2PCustomNotification(roomName, "2",
                            wantSpeakAccount);
                    changeRole();
                    refreshMemberState(wantSpeakAccount);


                }
            }
        });

        mCbSound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //将设置是否播放其他用户的语音数据。
                    if (currentCreateAccount != null) {
                        AVChatManager.getInstance().muteRemoteAudio(currentCreateAccount, true);
                    }
                } else {
                    if (currentCreateAccount != null) {
                        AVChatManager.getInstance().muteRemoteAudio(currentCreateAccount, false);
                    }
                }
            }
        });
    }

    private void initData() {
        myAccount = Constants.CID + "_" + AppConfig.getAppConfig(this).getPrivateCode();
        initIntentData();
        //默认加载会议创建人的视频
        AVChatManager.getInstance().muteLocalVideo(true);
        CheckPermission();
    }

    /**
     * 检查权限
     */
    private void CheckPermission() {
        requestRunTimePermission(new String[]{Manifest.permission.CAMERA, Manifest.permission
                .RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE}, new PermissionListener() {
            @Override
            public void onGranted() {
                LogUtils.e("iscreate:" + isCreate);
                if (isCreate) {
                    createRoom();
                } else {
                    joinRoom();
                }
            }

            @Override
            public void onDenied() {
                showToast("权限被拒绝！无法创建视频会议");
                finish();
            }
        });
    }

    private void createRoom() {
        AVChatManager.getInstance().createRoom(roomName, "",
                new AVChatCallback<AVChatChannelInfo>() {
                    @Override
                    public void onSuccess(AVChatChannelInfo avChatChannelInfo) {
                        joinRoom();
                    }

                    @Override
                    public void onFailed(int code) {
                        LogUtils.e("createRoom。。。" + code);
                    }

                    @Override
                    public void onException(Throwable exception) {
                        exception.printStackTrace();
                        LogUtils.e("createRoom。。。");
                    }
                });
    }

    private AVChatCameraCapturer mVideoCapturer;

    /**
     * 加入会议
     */
    private void joinRoom() {
        LogUtils.e("正在加入房间。。。");
        // rtc init
        AVChatManager.getInstance().enableRtc();
        AVChatManager.getInstance().enableVideo();
        if (isCreate) {
            mVideoCapturer = AVChatVideoCapturerFactory.createCameraCapturer();
            AVChatManager.getInstance().setupVideoCapturer(mVideoCapturer);
        }
        AVChatManager.getInstance().setParameter(AVChatParameters.KEY_SESSION_MULTI_MODE_USER_ROLE, AVChatUserRole.NORMAL);
        AVChatManager.getInstance().setParameter(AVChatParameters.KEY_AUDIO_REPORT_SPEAKER, true);
        AVChatManager.getInstance().setParameter(AVChatParameters.KEY_VIDEO_FIXED_CROP_RATIO, AVChatVideoCropRatio.CROP_RATIO_1_1);
        AVChatManager.getInstance().joinRoom2(roomName, AVChatType.VIDEO, new AVChatCallback<AVChatData>() {
            @Override
            public void onSuccess(AVChatData avChatData) {
                currentCreateAccount = avChatData.getAccount();
                LogUtils.e("joinRoom成功。。。");
            }

            @Override
            public void onFailed(int code) {
                LogUtils.e("joinRoom失败。。。" + code);
                showToast("加入会议失败，请重试");
                finish();
            }

            @Override
            public void onException(Throwable exception) {
                exception.printStackTrace();
                LogUtils.e("joinRoom失败。。。");
            }
        });
    }


    private void leaveRoom() {
        AVChatManager.getInstance().leaveRoom2(roomName, new AVChatCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                LogUtils.e("成功离开房间");
            }

            @Override
            public void onFailed(int code) {
                LogUtils.e("离开房间失败");
            }

            @Override
            public void onException(Throwable exception) {
                LogUtils.e("离开房间失败");
            }
        });
    }


    /**
     * 获取Intent数据
     */
    private void initIntentData() {
        Intent intent = getIntent();
        isCreate = intent.getBooleanExtra("isCreate", false);
        roomName = intent.getStringExtra("roomName");
        joinPeopleStr = intent.getStringExtra("joinPeopleStr");
        JSONArray ja = JSON.parseArray(joinPeopleStr);
        getCreateMen(ja);
        addSelfToMemberList();
    }

    /**
     * 将自己添加到成员列表
     */
    private void addSelfToMemberList() {
        mRealityLists.add(getJoinMeetingMemberInfo(AppConfig.getAppConfig(this).getPrivateUid()));
        setTopMemberRatioAndRefreshMember();
    }

    /**
     * 设置成员比例
     */
    private void setTopMemberRatioAndRefreshMember() {
        //成员(5/7)
        mTvMemberRatio.setText("成员" + " ( " + mRealityLists.size() + "/" + mLists.size() + " ) ");
        mAdapter.notifyDataSetChanged();
        LogUtils.e(mAdapter.getItemCount() + "");
    }

    /**
     * @param uid 根据传入的uid获取参会人的信息
     * @return
     */
    private JoinVideoMember getJoinMeetingMemberInfo(String uid) {
        for (int i = 0; i < mLists.size(); i++) {
            if (mLists.get(i).getUid().equals(uid)) {
                return mLists.get(i);
            }
        }
        return null;
    }


    /**
     * @param account 根据传入的用户名，查询预期参加成员列表，结果添加到视频下成员列表,1：互动 2：没有互动
     */
    private void addMember(String account) {
        int index = account.indexOf("_");
        String uid = account.substring(index + 1, account.length());


        for (int i = 0; i < mLists.size(); i++) {
            if (mLists.get(i).getUid().equals(uid)) {
                JoinVideoMember joinVideoMember = mLists.get(i);

                //判断当前实际开会人的列表是否含有要加入的人，没有，添加
                boolean isHas = false;
                for (int j = 0; j < mRealityLists.size(); j++) {
                    if (mRealityLists.get(j).getUid().equals(uid)) {
                        isHas = true;
                        break;
                    }
                }

                if (!isHas) {
                    mRealityLists.add(joinVideoMember);
                }
                setTopMemberRatioAndRefreshMember();
                break;
            }

        }


    }

    /**
     * @param account 根据传入的用户名，刷新视频下成员列表,1：互动 2：没有互动
     */
    private void refreshMemberState(String account) {
        int index = account.indexOf("_");
        String uid = account.substring(index + 1, account.length());
        for (int i = 0; i < mRealityLists.size(); i++) {
            if (mRealityLists.get(i).getUid().equals(uid)) {
                mRealityLists.get(i).setState("1");
                mRealityLists.get(i).setHand(false);
            } else {
                mRealityLists.get(i).setState("2");
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    /**
     * @param account 根据传入的用户名，查询预期参加成员列表，结果添加到视频下成员列表
     */
    private void removeMember(String account) {
        int index = account.indexOf("_");
        String uid = account.substring(index + 1, account.length());
        if (!mRealityLists.isEmpty()) {
            for (int i = 0; i < mRealityLists.size(); i++) {
                if (mRealityLists.get(i).getUid().equals(uid)) {
                    mRealityLists.remove(i);
                }
            }
            setTopMemberRatioAndRefreshMember();
        }

    }

    /**
     * @param ja 得到创建房间人
     */
    private void getCreateMen(JSONArray ja) {
        for (int i = 0; i < ja.size(); i++) {
            String is_createman = ja.getJSONObject(i).getString("is_createman");
            String uid = ja.getJSONObject(i).getString("uid");
            String username = ja.getJSONObject(i).getString("username");
            String post = ja.getJSONObject(i).getString("post");
            JoinVideoMember joinVideoMember = new JoinVideoMember(uid, username, post);
            if (is_createman.equals("1")) {
                createMen = Constants.CID + "_" + uid;
                //默认通话的成员是主播
                currentSpeakerMember = createMen;
                //默认进来后是主播视频
                joinVideoMember.setState("1");
            } else {
                joinVideoMember.setState("2");
            }

            mLists.add(joinVideoMember);
        }
    }


    private void showMemberVideo(String account) {
        LogUtils.e("account->" + account);
        AVChatVideoRender render = new AVChatVideoRender(AppManager.mContext);
        if (render != null && isCreate) {
            AVChatManager.getInstance().setupLocalVideoRender(render, false, AVChatVideoScalingType.SCALE_ASPECT_FIT);
            AVChatManager.getInstance().startVideoPreview();
            videoLayout.removeAllViews();
            videoLayout.addView(render);
            render.setZOrderMediaOverlay(true);
        } else if (render != null && !isCreate) {
            AVChatManager.getInstance().setupRemoteVideoRender(account, render, false, AVChatVideoScalingType.SCALE_ASPECT_FIT);
            AVChatManager.getInstance().startVideoPreview();
            videoLayout.removeAllViews();
            videoLayout.addView(render);
            render.setZOrderMediaOverlay(true);
        }
    }

    private void registerObservers(boolean register) {
        AVChatManager.getInstance().observeAVChatState(this, register);
        NIMClient.getService(MsgServiceObserve.class).observeCustomNotification(customNotification, register);
    }

    public void showTip(String msg, final String posiStr, String negaStr) {
        @SuppressLint("InflateParams")
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_exit_editor, null);
        TextView title = (TextView) dialogView.findViewById(R.id.title);
        title.setText("提示");
        TextView message = (TextView) dialogView.findViewById(R.id.message);
        message.setText(msg);

        final AlertDialog alertDialog = new AlertDialog.Builder(this,
                R.style.AppTheme_Dialog).create();
        alertDialog.setView(dialogView);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, posiStr,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, negaStr,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(
                getResources().getColor(R.color.btn_text_logout));
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(
                getResources().getColor(R.color.btn_text_logout));
    }


    @Override
    public void onBackPressed() {
        showTip("确定离开会议?", "离开", "取消");
    }


    /************************
     * AVChatStateObserver实现方法Start
     ***********************/
    @Override
    public void onTakeSnapshotResult(String account, boolean success, String file) {

    }

    /**
     * @param netType 本地客户端网络类型发生改变时回调，会通知当前网络类型。
     */
    @Override
    public void onConnectionTypeChanged(int netType) {

    }

    @Override
    public void onAVRecordingCompletion(String account, String filePath) {

    }

    @Override
    public void onAudioRecordingCompletion(String filePath) {

    }

    @Override
    public void onLowStorageSpaceWarning(long availableSize) {

    }

    @Override
    public void onFirstVideoFrameAvailable(String account) {
    }

    @Override
    public void onVideoFpsReported(String account, int fps) {

    }

    @Override
    public boolean onVideoFrameFilter(AVChatVideoFrame frame, boolean maybeDualInput) {
        return false;
    }

    @Override
    public boolean onAudioFrameFilter(AVChatAudioFrame frame) {
        return false;
    }

    @Override
    public void onJoinedChannel(int code, String audioFile, String videoFile) {
        LogUtils.e("onJoinedChannel...");
    }

    /**
     * 自己成功离开频道回调
     */
    @Override
    public void onLeaveChannel() {

    }

    /**
     * @param account 其他用户音视频服务器连接成功后，会回调 onUserJoined，可以获取当前通话的用户帐号。
     */
    @Override
    public void onUserJoined(String account) {
        addMember(account);
        showMemberVideo(account);
    }

    /**
     * @param account // @param event   －1,用户超时离开  0,正常退出
     * @param event
     */
    @Override
    public void onUserLeave(String account, int event) {
        //如果是创建会议的人，那么，所有人也离开房间，否则，刷新界面
        if (account.equals(createMen)) {
            showToast("主持人结束了会议");
            finish();
        } else {
            removeMember(account);
        }
    }

    @Override
    public void onProtocolIncompatible(int status) {

    }

    /**
     * 通话过程中，服务器断开，会回调 onDisconnectServer。
     */
    @Override
    public void onDisconnectServer() {

    }

    /**
     * @param user
     * @param quality 通话过程中网络状态发生变化，会回调 onNetworkQuality。
     */
    @Override
    public void onNetworkQuality(String user, int quality, AVChatNetworkStats stats) {

    }


    /**
     * 自己的音视频连接建立，会回调 onCallEstablished。音频切换到正在通话的界面，并开始计时等处理。视频则通过为用户
     * 设置对应画布并添加到相应的 layout 上显示图像。
     */
    @Override
    public void onCallEstablished() {
        LogUtils.e("createMen:" + "createMen" + myAccount);
        String selfAccount = Constants.CID + "_" + AppConfig.getAppConfig(this).getPrivateCode();
        LogUtils.e("createMen:" + "selfAccount" + selfAccount);
        if (selfAccount.equals(myAccount)) {
            showMemberVideo(myAccount);
        }
    }

    /**
     * @param code
     * @param desc 音视频设备状态发生改变时，会回调 onDeviceEvent。
     */
    @Override
    public void onDeviceEvent(int code, String desc) {

    }

    @Override
    public void onFirstVideoFrameRendered(String user) {
    }

    @Override
    public void onVideoFrameResolutionChanged(String user, int width, int height, int rotate) {

    }

    @Override
    public void onAudioDeviceChanged(int device) {

    }

    @Override
    public void onReportSpeaker(Map<String, Integer> speakers, int mixedEnergy) {
        LogUtils.e("有人在说话：" + speakers.toString());

        Set<String> set =
                speakers.keySet();

        for (String str : set) {
            LogUtils.e("onReportSpeaker:" + str);
        }

    }

    @Override
    public void onAudioMixingEvent(int event) {

    }

    @Override
    public void onSessionStats(AVChatSessionStats sessionStats) {

    }

    @Override
    public void onLiveEvent(int event) {

    }

    /************************
     * AVChatStateObserver实现方法End
     ***********************/


    //TODO 有人举手
    private Observer<CustomNotification> customNotification = new Observer<CustomNotification>() {
        @Override
        public void onEvent(CustomNotification customNotification) {
            String content = customNotification.getContent();
            String command = "0";
            //1：有人举手发言   2 ：主持人同意连麦请求  3：  收到更改视频画面的通知

            try {
                JSONObject json = JSON.parseObject(content);
                JSONObject data = json.getJSONObject("data");
                command = data.getString("command");

            } catch (Exception e) {

            }
            if (command.equals("2")) {
                LogUtils.e("主持人同意连麦请求");
                changeIdentity();

            } else if (command.equals("3")) {

                LogUtils.e("收到更改视频画面的通知");

                if (!myAccount.equals(customNotification.getFromAccount())) {
                    LogUtils.e("当前不是我自己发送的通知。。。myAccount:" + myAccount + ",getFromAccount:" + customNotification.getFromAccount());
                    changeRole();
                    isCanHand = true;
                } else {
                    isCanHand = false;
                }


                refreshVideoAndUI(customNotification);
            } else if (command.equals("1")) {
                // 有人举手发言
                if (isCreate) {
                    LogUtils.e("有人举手发言:" + customNotification.getFromAccount());
                    showHands(customNotification.getFromAccount());
                }

            }
        }
    };

    private void changeRole() {
        AVChatManager.getInstance().muteLocalAudio(true);
        //是否打开观众角色, 设置观众角色后所有的语音和视频数据的采集和发送会关闭，仅允许接收和播放远端其他用户的数据。
        AVChatManager.getInstance().enableAudienceRole(true);
    }

    /**
     * @param customNotification 刷新视频和更改UI
     */
    private void refreshVideoAndUI(CustomNotification customNotification) {
        currentSpeakerMember = customNotification.getFromAccount();
        showMemberVideo(customNotification.getFromAccount());
        refreshMemberState(customNotification.getFromAccount());
    }

    /**
     * 主持人同意连麦请求,更改身份，通知所有人转变画面
     */
    private void changeIdentity() {

        changeSelfToMaster();

        sendNotificationTomuberChangeVideo();

    }

    /**
     * 发送通知给所有人更改视频
     */
    private void sendNotificationTomuberChangeVideo() {
        refreshMemberState(myAccount);

        for (int i = 0; i < mRealityLists.size(); i++) {
            String uid = mRealityLists.get(i).getUid();
            String account = Constants.CID + "_" + uid;
            MsgHelper.getInstance().sendP2PCustomNotification(roomName, "3",
                    account);
        }
    }

    /**
     * 将自己改变为主持人
     */
    private void changeSelfToMaster() {
        //muteLocalAudio mute:静音 这个是禁止发送语音
        AVChatManager.getInstance().muteLocalAudio(false);
        AVChatManager.getInstance().muteLocalVideo(false);
        //是否打开观众角色, 设置观众角色后所有的语音和视频数据的采集和发送会关闭，仅允许接收和播放远端其他用户的数据。
//        AVChatManager.getInstance().startLive();
        AVChatManager.getInstance().enableAudienceRole(false);
    }

    /**
     * @param account 有人发言，显示布局前面的手的标识
     */
    private void showHands(String account) {
        int index = account.indexOf("_");
        String uid = account.substring(index + 1, account.length());

        for (int i = 0; i < mRealityLists.size(); i++) {
            if (mRealityLists.get(i).getUid().equals(uid)) {
                mRealityLists.get(i).setHand(true);
                break;
            } else {
                mRealityLists.get(i).setHand(false);
            }
        }
        mAdapter.notifyDataSetChanged();
    }


    private void initToolBar() {
        if (toolbar == null) {
            return;
        }
        setTitle("");//必须在setSupportActionBar之前调用
        toolbar.setTitleTextColor(Color.parseColor("#000000"));
        setSupportActionBar(toolbar);
        tvTitle.setText("视频会议");
        Toolbar.LayoutParams lp = new Toolbar.LayoutParams(
                Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        tvTitle.setLayoutParams(lp);

        toolbar.setNavigationIcon(R.drawable.toolbar_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTip("确定离开会议?", "离开", "取消");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        registerObservers(false);
        leaveRoom();
    }

    @OnClick({R.id.rl_handler, R.id.rl_leave_meeting})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_handler://举手layout

                if (isCreate) {
                    sendNotificationTomuberChangeVideo();
                    changeSelfToMaster();
                } else {
                    // 申请互动
                    if (isCanHand) {
                        isCanHand = false;
                        MsgHelper.getInstance().sendP2PCustomNotification(roomName, "1",
                                createMen);
                    } else {
                        showToast("你已经举手，请稍后");
                    }
                }
                break;
            case R.id.rl_leave_meeting:
                showTip("确定离开会议?", "离开", "取消");
                break;
        }
    }
}