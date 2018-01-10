/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.shanlinjinrong.oa.ui.activity.message;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMCallSession;
import com.hyphenate.chat.EMCallStateChangeListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.db.FriendsInfoCacheSvc;
import com.hyphenate.easeui.utils.EncryptionUtil;
import com.hyphenate.easeui.utils.GlideRoundTransformUtils;
import com.hyphenate.exceptions.EMNoActiveCallException;
import com.hyphenate.exceptions.EMServiceNotReadyException;
import com.hyphenate.exceptions.HyphenateException;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.manager.AppConfig;
import com.shanlinjinrong.oa.manager.AppManager;
import com.shanlinjinrong.oa.ui.activity.message.bean.CallEventBean;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.w3c.dom.Text;

import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * 语音通话页面
 */
@SuppressWarnings("FieldCanBeLocal")
public class VoiceCallActivity extends CallActivity implements OnClickListener, SensorEventListener {

    //private Button          mBtnHangup;
    private TextView    mTvCallTips;
    private ImageButton mBtnRefuse;
    private ImageButton mBtnAnswer;

    private TextView     mTvAnswer;
    private EMMessage    mMessage;
    private ImageButton  mMuteImage;
    private TextView     mTvNickName;
    private Chronometer  mChronometer;
    private LinearLayout mLlVoiceAnswer;

    private LinearLayout    mLlContainerState;
    private LinearLayout    mLlVoiceSilence;
    private LinearLayout    mLlVoiceOut;
    private TextView        mTvCallState;
    private ImageButton     mImgHandsFree;
    //private TextView        mTvNetworkStatus;
    private CircleImageView mSwingCard;
    //private LinearLayout    mLlVoiceControl;
    //private LinearLayout    mComingBtnContainer;

    private String mNikeName = "", mPortrait = "", mToUsername = "";
    private boolean mIsThroughTo, mIsMuteState, mIsHandsFreeState, mEndCallTriggerByMe;
    //调用距离传感器 ->传感器管理对象
    private         SensorManager         mManager           = null;
    //电源管理对象 ->屏幕开关
    protected       PowerManager          mLocalPowerManager = null;
    //电源锁
    private         PowerManager.WakeLock localWakeLock      = null;
    private         CompositeSubscription mUnSunscribe       = new CompositeSubscription();
    protected final int                   MAKE_CALL_TIMEOUT  = 50 * 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            finish();
            return;
        }
        setContentView(R.layout.em_activity_voice_call);

        initData();
        initView();

        //发起语音 接收语音 -> 声音处理
        //Local voice_call_state
        if (!isInComingCall) {
            //本地发起语音
            initLocalCall();
        } else { //Remote voice_call_state
            //远程语音
            initRemoteCall();
        }

        //超时挂断处理
        initTimeOutHangUp();

        //初始化 电源锁
        initLocalPower();
    }

    //初始化数据源
    private void initData() {

        callType = 0;
        send_phone = getIntent().getStringExtra("phone");
        send_sex = getIntent().getStringExtra("sex");
        send_post_title = getIntent().getStringExtra("post_name");
        send_username = getIntent().getStringExtra("nike");
        send_portrait = getIntent().getStringExtra("portrait");
        send_email = getIntent().getStringExtra("email");
        myAccount = AppConfig.getAppConfig(this).get(AppConfig.PREF_KEY_CODE);
        send_department_name = getIntent().getStringExtra("department_name");
        EMCallSession currentCallSession = EMClient.getInstance().callManager().getCurrentCallSession();
        if (currentCallSession != null) {
            username = currentCallSession.getRemoteName();
        }
        if (username.equals("")) {
            username = getIntent().getStringExtra("username");
        }
        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                        | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        mToUsername = getIntent().getStringExtra(" ");
        mNikeName = getIntent().getStringExtra("nike");
        mPortrait = getIntent().getStringExtra("portrait");
        isInComingCall = getIntent().getBooleanExtra("isComingCall", false);
    }

    //初始化视图
    private void initView() {

        mMuteImage = (ImageButton) findViewById(R.id.iv_mute);
        mTvNickName = (TextView) findViewById(R.id.tv_nick);
        mTvCallTips = (TextView) findViewById(R.id.tv_call_tips);
        mLlVoiceAnswer = (LinearLayout) findViewById(R.id.ll_btn_voice_answer);
        mLlVoiceSilence = (LinearLayout) findViewById(R.id.ll_btn_voice_silence);
        mLlContainerState = (LinearLayout) findViewById(R.id.ll_container_state);
        mLlVoiceOut = (LinearLayout) findViewById(R.id.ll_btn_voice_out);
        mBtnRefuse = (ImageButton) findViewById(R.id.btn_refuse_call);
        mBtnAnswer = (ImageButton) findViewById(R.id.btn_voice_answer);
        mTvAnswer = (TextView) findViewById(R.id.tv_voice_answer);
//        mBtnHangup = (Button) findViewById(R.id.btn_hangup_call);
        mTvCallState = (TextView) findViewById(R.id.tv_call_state);
        mImgHandsFree = (ImageButton) findViewById(R.id.iv_handsfree);
        mChronometer = (Chronometer) findViewById(R.id.chronometer);
        mSwingCard = (CircleImageView) findViewById(R.id.iv_img_user);
//        mTvNetworkStatus = (TextView) findViewById(R.id.tv_network_status);
//        mLlVoiceControl = (LinearLayout) findViewById(R.id.ll_voice_control);
//        mComingBtnContainer = (LinearLayout) findViewById(R.id.ll_coming_call);

        if (getIntent().getBooleanExtra("isComingCall", false)) {
            if (FriendsInfoCacheSvc.getInstance(AppManager.mContext).getNickName(username).equals("")) {
                //监听 查询个人信息接口
                if (!EventBus.getDefault().isRegistered(this)) {
                    EventBus.getDefault().register(this);
                }
                mPresenter.searchUserDetails(username.substring(3, username.length()));
            } else {
                mPresenter.searchUserDetails(username.substring(3, username.length()));
                String nickName = FriendsInfoCacheSvc.getInstance(AppManager.mContext).getNickName(username);
                String portrait = FriendsInfoCacheSvc.getInstance(AppManager.mContext).getPortrait(username);
                if (!TextUtils.isEmpty(portrait)) {
                    Glide.with(AppManager.mContext)
                            .load(portrait)
                            .dontAnimate()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .error(R.drawable.ease_user_portraits)
                            .transform(new CenterCrop(AppManager.mContext), new GlideRoundTransformUtils(AppManager.mContext, 5))
                            .placeholder(R.drawable.ease_user_portraits)
                            .into(mSwingCard);
                } else {
                    Glide.with(AppManager.mContext).load(R.drawable.ease_user_portraits).asBitmap().into(mSwingCard);
                }
                mTvNickName.setText(nickName);
            }
        } else {
            String nickName = FriendsInfoCacheSvc.getInstance(AppManager.mContext).getNickName(username);
            String portrait = FriendsInfoCacheSvc.getInstance(AppManager.mContext).getPortrait(username);


            if (!TextUtils.isEmpty(portrait)) {
                Glide.with(AppManager.mContext)
                        .load(portrait)
                        .dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .error(R.drawable.ease_user_portraits)
                        .transform(new CenterCrop(AppManager.mContext), new GlideRoundTransformUtils(AppManager.mContext, 5))
                        .placeholder(R.drawable.ease_user_portraits)
                        .into(mSwingCard);
            } else {
                Glide.with(AppManager.mContext).load(R.drawable.ease_user_portraits).asBitmap().into(mSwingCard);
            }

            mTvNickName.setText(nickName);
        }

        addCallStateListener();
        mBtnRefuse.setOnClickListener(this);
        mBtnAnswer.setOnClickListener(this);
        mImgHandsFree.setOnClickListener(this);
        mMuteImage.setOnClickListener(this);
    }

    //接收语音 -> 声音
    private void initRemoteCall() {
//        mLlVoiceControl.setVisibility(View.INVISIBLE);
        mLlVoiceAnswer.setVisibility(View.VISIBLE);
        Uri ringUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        audioManager.setMode(AudioManager.MODE_RINGTONE);
        audioManager.setSpeakerphoneOn(true);
        ringtone = RingtoneManager.getRingtone(this, ringUri);
        ringtone.play();
    }

    //发起语音 -> 声音
    private void initLocalCall() {
//        mBtnHangup.setVisibility(View.VISIBLE);
//        mComingBtnContainer.setVisibility(View.INVISIBLE);
        soundPool = new SoundPool(1, AudioManager.STREAM_RING, 0);
        outgoing = soundPool.load(VoiceCallActivity.this, R.raw.em_outgoing, 1);
        mTvCallState.setText(getResources().getString(R.string.Are_connected_to_each_other));

        Observable.just("").observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(s -> {
                    try {//发起实时语音通讯
                        EMClient.getInstance().callManager().makeVoiceCall(username);
                        isMakeVoiceCall = true;
                    } catch (final EMServiceNotReadyException e) {
                        e.printStackTrace();
                        runOnUiThread(() -> {
                            String message = e.getMessage();
                            if (e.getErrorCode() == EMError.CALL_REMOTE_OFFLINE) {
                                message = getResources().getString(R.string.The_other_is_not_online);
                            } else if (e.getErrorCode() == EMError.USER_NOT_LOGIN) {
                                message = getResources().getString(R.string.Is_not_yet_connected_to_the_server);
                            } else if (e.getErrorCode() == EMError.INVALID_USER_NAME) {
                                message = getResources().getString(R.string.illegal_user_name);
                            } else if (e.getErrorCode() == EMError.CALL_BUSY) {
                                message = getResources().getString(R.string.The_other_is_on_the_phone);
                            } else if (e.getErrorCode() == EMError.NETWORK_ERROR) {
                                message = getResources().getString(R.string.can_not_connect_chat_server_connection);
                            } else if (e.getMessage().equals("exception isConnected:false")) {
                                message = getResources().getString(R.string.can_not_connect_chat_server_connection);
                            }
                            showToast(message);
                            finish();
                        });
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }, Throwable::printStackTrace);

        Observable.just("")
                .delay(300, TimeUnit.MILLISECONDS)
                .subscribe(s -> streamID = playMakeCallSounds(), Throwable::printStackTrace);
    }

    //初始化 电源锁
    private void initLocalPower() {
        //调用距离传感器，控制屏幕
        mManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        //获取系统服务POWER_SERVICE，返回一个PowerManager对象
        mLocalPowerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        //获取PowerManager.WakeLock对象,后面的参数|表示同时传入两个值,最后的是LogCat里用的Tag -> 第一个参数为电源锁级别，第二个是日志tag
        assert this.mLocalPowerManager != null;
        localWakeLock = this.mLocalPowerManager.newWakeLock(32, "MyPower");
    }

    //超时 挂断处理
    private void initTimeOutHangUp() {
        handler.removeCallbacks(timeoutHangup);
        handler.postDelayed(timeoutHangup, MAKE_CALL_TIMEOUT);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        try {
            float[] its = event.values;
            if (its != null && event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
                //贴近距离感应器的时候 its[0] 返回值为0.0，离开时返回1.0
                if (its[0] == 0.0) {
                    // 贴近手机 -> 申请设备电源锁
                    if (!localWakeLock.isHeld()) {
                        localWakeLock.acquire(100 * 60 * 1000L);
                    }
                } else {
                    // 远离手机 -> 释放设备电源锁
                    if (!localWakeLock.isHeld()) {
                        localWakeLock.setReferenceCounted(false);
                        // 释放设备电源锁
                        localWakeLock.release();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    protected void onResume() {
        super.onResume();
        //距离感应器 -> 注册传感器，第一个参数为距离监听器，第二个是传感器类型，第三个是延迟类型
        mManager.registerListener(this, mManager.getDefaultSensor(Sensor.TYPE_PROXIMITY), SensorManager.SENSOR_DELAY_NORMAL);
    }

    /**
     * 通话状态监听
     */
    void addCallStateListener() {
        mCallStateListener = (callState, error) -> {
            switch (callState) {
                //正在连接对方
                case CONNECTING:
                    ActivityCompat.requestPermissions(VoiceCallActivity.this, new String[]{Manifest.permission.RECORD_AUDIO}, 10);
                    mIsThroughTo = false;
                    runOnUiThread(() -> mTvCallState.setText(getResources().getString(R.string.Are_connected_to_each_other)));
                    break;
                //双方已经建立连接
                case CONNECTED:
                    mIsThroughTo = false;
                    //获取扩展内容
                    runOnUiThread(() -> {
                        if (!isInComingCall) {
                            mTvCallState.setText(getResources().getString(R.string.have_connected_with));
                        } else {
                            mTvCallState.setText("邀请你语音聊天");
                        }
                    });
                    break;
                //电话接通成功
                case ACCEPTED:
                    mIsThroughTo = true;
                    handler.removeCallbacks(timeoutHangup);
                    runOnUiThread(() -> {
                        if (soundPool != null) {
                            soundPool.stop(streamID);
                        }
                        if (!mIsHandsFreeState) {
                            closeSpeakerOn();
                        }

                        if (!isInComingCall) {
                            mLlContainerState.setBackgroundColor(getResources().getColor(R.color.bg_call_state));
                            mLlVoiceSilence.setVisibility(View.VISIBLE);
                            mLlVoiceOut.setVisibility(View.VISIBLE);
                            mLlVoiceAnswer.setVisibility(View.GONE);
                        }
                        mTvCallState.setText(getResources().getString(R.string.In_the_call));
                        handler.postDelayed(() -> runOnUiThread(() -> mTvCallState.setVisibility(View.GONE)), 1000);
                        //通话时长 提示
                        mChronometer.setVisibility(View.VISIBLE);
                        mChronometer.setBase(SystemClock.elapsedRealtime());
                        // 开启计时
                        mChronometer.start();
                        callingState = CallingState.NORMAL;
                    });
                    break;
                //网络不稳定
                case NETWORK_UNSTABLE:
//                    runOnUiThread(() -> {
//                        mTvNetworkStatus.setVisibility(View.VISIBLE);
//                        if (error == EMCallStateChangeListener.CallError.ERROR_NO_DATA) {
//                            mTvNetworkStatus.setText(R.string.no_call_data);
//                        } else {
//                            mTvNetworkStatus.setText(R.string.network_unstable);
//                        }
//                    });
                    break;
                //网络正常
                case NETWORK_NORMAL:
//                    runOnUiThread(() -> mTvNetworkStatus.setVisibility(View.INVISIBLE));
                    break;
                // 关闭声音
                case VOICE_PAUSE:
                    runOnUiThread(() -> {
                        showToast("语音中断");
//                        mTvCallTips.setVisibility(View.VISIBLE);
//                        mTvCallTips.setText("语音中断");
                    });
//                    handler.postDelayed(() -> runOnUiThread(() -> mTvCallTips.setVisibility(View.GONE)), 1000);
                    break;
                // 开启声音
                case VOICE_RESUME:
                    runOnUiThread(() -> {
                        showToast("语音继续");
//                        mTvCallTips.setVisibility(View.VISIBLE);
//                        mTvCallTips.setText("语音继续");
                    });
//                    handler.postDelayed(() -> runOnUiThread(() -> mTvCallTips.setVisibility(View.GONE)), 1000);
                    break;
                // 电话断了
                case DISCONNECTED:
                    mIsThroughTo = false;
                    handler.removeCallbacks(timeoutHangup);
                    @SuppressWarnings("UnnecessaryLocalVariable") final EMCallStateChangeListener.CallError fError = error;
                    Observable.just("")
                            .subscribeOn(AndroidSchedulers.mainThread())
                            .subscribe(s -> {
//                                mTvCallTips.setVisibility(View.VISIBLE);
                                mChronometer.stop();
                                callDruationText = mChronometer.getText().toString();
                                String st1 = getResources().getString(R.string.Refused);
                                String st2 = getResources().getString(R.string.The_other_party_refused_to_accept);
                                String st3 = getResources().getString(R.string.Connection_failure);
                                String st4 = getResources().getString(R.string.The_other_party_is_not_online);
                                String st5 = getResources().getString(R.string.The_other_is_on_the_phone_please);
                                String st6 = getResources().getString(R.string.The_other_party_did_not_answer_new);
                                String st7 = getResources().getString(R.string.hang_up);
                                String st8 = getResources().getString(R.string.The_other_is_hang_up);
                                String st9 = getResources().getString(R.string.did_not_answer);
                                String st10 = getResources().getString(R.string.Has_been_cancelled);
                                String str = null;
                                if (fError == EMCallStateChangeListener.CallError.REJECTED) {
                                    callingState = CallingState.BEREFUSED;
                                    showToast(st2);
//                                    mTvCallTips.setText(st2);
                                    str = st2;
                                } else if (fError == EMCallStateChangeListener.CallError.ERROR_TRANSPORT) {
                                    showToast(st3);
//                                    mTvCallTips.setText(st3);
                                    str = st3;
                                } else if (fError == EMCallStateChangeListener.CallError.ERROR_UNAVAILABLE) {
                                    callingState = CallingState.OFFLINE;
                                    showToast(st4);
//                                    mTvCallTips.setText(st4);
                                    str = st4;
                                } else if (fError == EMCallStateChangeListener.CallError.ERROR_BUSY) {
                                    callingState = CallingState.BUSY;
                                    showToast(st5);
//                                    mTvCallTips.setText(st5);
                                    str = st5;
                                } else if (fError == EMCallStateChangeListener.CallError.ERROR_NORESPONSE) {
                                    callingState = CallingState.NO_RESPONSE;
                                    showToast(st6);
//                                    mTvCallTips.setText(st6);
                                    str = st6;
                                } else if (fError == EMCallStateChangeListener.CallError.ERROR_LOCAL_SDK_VERSION_OUTDATED || fError == EMCallStateChangeListener.CallError.ERROR_REMOTE_SDK_VERSION_OUTDATED) {
                                    callingState = CallingState.VERSION_NOT_SAME;
//                                    mTvCallTips.setText(R.string.call_version_inconsistent);

                                    showToast(getString(R.string.call_version_inconsistent));
                                } else {
                                    if (isRefused) {
                                        callingState = CallingState.REFUSED;
//                                        mTvCallTips.setText(st1);
                                        showToast(st1);
                                        str = st1;
                                    } else if (isAnswered) {
                                        callingState = CallingState.NORMAL;
                                        if (!mEndCallTriggerByMe) {
//                                            mTvCallTips.setText(st8);
                                            showToast(st8);
                                        } else {
                                            mTvCallTips.setText("通话结束");
                                        }
                                    } else {
                                        if (isInComingCall) {
                                            callingState = CallingState.UNANSWERED;
//                                            mTvCallTips.setText(st9);
                                            showToast(st9);
                                            str = st9;
                                        } else {
                                            if (callingState != CallingState.NORMAL) {
                                                callingState = CallingState.CANCELLED;
//                                                mTvCallTips.setText(st10);
                                                showToast(st10);
                                                str = st10;
                                            } else {
//                                                mTvCallTips.setText(st7);
                                                showToast(st7);
                                            }
                                        }
                                    }
                                }
                                handler.postDelayed(() -> runOnUiThread(() -> {
                                    removeCallStateListener();
                                    Animation animation = new AlphaAnimation(1.0f, 0.0f);
                                    animation.setDuration(800);
                                    findViewById(R.id.root_layout).startAnimation(animation);
                                    finish();
                                }), 200);
                            }, Throwable::printStackTrace);
                    break;
                default:
                    break;
            }
        };
        EMClient.getInstance().callManager().addCallStateChangeListener(mCallStateListener);
    }

    void removeCallStateListener() {
        EMClient.getInstance().callManager().removeCallStateChangeListener(mCallStateListener);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_refuse_call:
                if (isInComingCall) {
                    isRefused = true;
                    mBtnRefuse.setEnabled(false);
                    handler.sendEmptyMessage(MSG_CALL_REJECT);
                    //拒接
                    if (!username.equals("sl_" + AppConfig.getAppConfig(AppManager.mContext).get(AppConfig.PREF_KEY_CODE))) {
                        mMessage = EMMessage.createTxtSendMessage(EncryptionUtil.getEncryptionStr("通话已拒接", EMClient.getInstance().getCurrentUser()), username);
                        //发送消息通话已拒接
                        EMClient.getInstance().chatManager().sendMessage(mMessage);
                        mIsThroughTo = false;
                    }
                } else {
                    mBtnRefuse.setEnabled(false);
                    mChronometer.stop();
                    mEndCallTriggerByMe = true;
                    handler.sendEmptyMessage(MSG_CALL_END);
                    //挂断
                    if (mIsThroughTo) {
                        mMessage = EMMessage.createTxtSendMessage(EncryptionUtil.getEncryptionStr("通话时长:" + mChronometer.getText().toString(), EMClient.getInstance().getCurrentUser()), username);
                        mIsThroughTo = false;
                        //发送消息
                        EMClient.getInstance().chatManager().sendMessage(mMessage);
                    } else {
                        mMessage = EMMessage.createTxtSendMessage(EncryptionUtil.getEncryptionStr("通话已取消", EMClient.getInstance().getCurrentUser()), username);
                        mIsThroughTo = false;
                        //发送消息
                        EMClient.getInstance().chatManager().sendMessage(mMessage);
                    }
                }
                break;
            case R.id.btn_voice_answer:
                mLlContainerState.setBackgroundColor(getResources().getColor(R.color.bg_call_state));
                if (ContextCompat.checkSelfPermission(VoiceCallActivity.this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(VoiceCallActivity.this, new String[]{Manifest.permission.RECORD_AUDIO}, 10);
                }
                closeSpeakerOn();
                mTvCallState.setText("连接中");
                mLlVoiceSilence.setVisibility(View.VISIBLE);
                mLlVoiceOut.setVisibility(View.VISIBLE);
                mLlVoiceAnswer.setVisibility(View.GONE);
                handler.sendEmptyMessage(MSG_CALL_ANSWER);
                //接通
                mIsThroughTo = true;
                break;
            case R.id.iv_mute:
                if (mIsMuteState) {
                    mMuteImage.setBackground(getResources().getDrawable(R.drawable.em_icon_mute_normal));
                    try {
                        EMClient.getInstance().callManager().resumeVoiceTransfer();
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                    }
                    mIsMuteState = false;
                } else {
                    mMuteImage.setBackground(getResources().getDrawable(R.drawable.em_icon_mute_on));
                    try {
                        EMClient.getInstance().callManager().pauseVoiceTransfer();
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                    }
                    mIsMuteState = true;
                }
                break;
            case R.id.iv_handsfree:
                if (mIsHandsFreeState) {
                    mImgHandsFree.setBackground(getResources().getDrawable(R.drawable.em_icon_speaker_normal));
                    closeSpeakerOn();
                    mIsHandsFreeState = false;
                } else {
                    mImgHandsFree.setBackground(getResources().getDrawable(R.drawable.em_icon_speaker_on));
                    openSpeakerOn();
                    mIsHandsFreeState = true;
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        callDruationText = mChronometer.getText().toString();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void CallInfoEvent(CallEventBean bean) {
        if (bean.getEvent().equals("callSuccess")) {

            if (!TextUtils.isEmpty(bean.getPortaits())) {
                Glide.with(AppManager.mContext)
                        .load(bean.getPortaits())
                        .dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .error(R.drawable.ease_default_avatar)
                        .transform(new CenterCrop(AppManager.mContext), new GlideRoundTransformUtils(AppManager.mContext, 5))
                        .placeholder(R.drawable.ease_default_avatar)
                        .into(mSwingCard);
            } else {
                Glide.with(AppManager.mContext).load(R.drawable.ease_default_avatar).asBitmap().into(mSwingCard);
            }
            mTvNickName.setText(bean.getUserName());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        try {
            if (mManager != null) {
                //释放电源锁
                localWakeLock.release();
                //注销传感器监听
                mManager.unregisterListener(this);
            }
            if (mIsThroughTo) {
                EMClient.getInstance().callManager().endCall();
            }
        } catch (EMNoActiveCallException e) {
            e.printStackTrace();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}
