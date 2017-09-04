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

import android.content.Context;
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
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMCallStateChangeListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessageBody;
import com.hyphenate.chat.adapter.message.EMACmdMessageBody;
import com.hyphenate.easeui.adapter.EaseConversationAdapter;
import com.hyphenate.easeui.model.UserInfoDetailsBean;
import com.hyphenate.easeui.model.UserInfoSelfDetailsBean;
import com.hyphenate.exceptions.EMNoActiveCallException;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.EMLog;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.manager.AppConfig;
import com.shanlinjinrong.oa.manager.AppManager;
import com.shanlinjinrong.oa.utils.GlideRoundTransformUtils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

/**
 * 语音通话页面
 */
public class VoiceCallActivity extends CallActivity implements OnClickListener, SensorEventListener {
    private LinearLayout comingBtnContainer;
    private Button hangupBtn;
    private Button refuseBtn;
    private Button answerBtn;
    private ImageView muteImage;
    private ImageView handsFreeImage;
    private SimpleDraweeView mSwingCard;
    private boolean isMuteState;
    private boolean isHandsfreeState;
    private TextView nickTextView;
    public String sideInfo;
    private TextView callStateTextView;
    private boolean endCallTriggerByMe = false;
    private Chronometer chronometer;
    private String st1;
    private LinearLayout voiceContronlLayout;
    private TextView netwrokStatusVeiw;
    private boolean monitor = false;
    private String nike;
    private String portrait;
    private String toUsername;
    private EMMessage mMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            finish();
            return;
        }
        setContentView(R.layout.em_activity_voice_call);

//		DemoHelper.getInstance().isVoiceCalling = true;
        callType = 0;

        myAccount = AppConfig.getAppConfig(this).get(AppConfig.PREF_KEY_CODE);

        comingBtnContainer = (LinearLayout) findViewById(R.id.ll_coming_call);
        refuseBtn = (Button) findViewById(R.id.btn_refuse_call);
        answerBtn = (Button) findViewById(R.id.btn_answer_call);
        hangupBtn = (Button) findViewById(R.id.btn_hangup_call);
        muteImage = (ImageView) findViewById(R.id.iv_mute);
        mSwingCard = (SimpleDraweeView) findViewById(R.id.swing_card);
        handsFreeImage = (ImageView) findViewById(R.id.iv_handsfree);
        callStateTextView = (TextView) findViewById(R.id.tv_call_state);
        nickTextView = (TextView) findViewById(R.id.tv_nick);
        TextView durationTextView = (TextView) findViewById(R.id.tv_calling_duration);
        chronometer = (Chronometer) findViewById(R.id.chronometer);
        voiceContronlLayout = (LinearLayout) findViewById(R.id.ll_voice_control);
        netwrokStatusVeiw = (TextView) findViewById(R.id.tv_network_status);

        refuseBtn.setOnClickListener(this);
        answerBtn.setOnClickListener(this);
        hangupBtn.setOnClickListener(this);
        muteImage.setOnClickListener(this);
        handsFreeImage.setOnClickListener(this);

        //TODO 接收上个界面的用户信息
        username = getIntent().getStringExtra("username");
        send_CODE = getIntent().getStringExtra("CODE");
        send_phone = getIntent().getStringExtra("phone");
        send_sex = getIntent().getStringExtra("sex");
        send_post_title = getIntent().getStringExtra("post_name");
        send_username = getIntent().getStringExtra("nike");
        send_portrait = getIntent().getStringExtra("portrait");
        send_email = getIntent().getStringExtra("email");
        send_department_name = getIntent().getStringExtra("department_name");

        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                        | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        addCallStateListener();
        msgid = UUID.randomUUID().toString();

        //readUserInfoDetailsMessage();
        toUsername = getIntent().getStringExtra("toUsername");
        nike = getIntent().getStringExtra("nike");
        portrait = getIntent().getStringExtra("portrait");
        isInComingCall = getIntent().getBooleanExtra("isComingCall", false);

        if (!TextUtils.isEmpty(nike)) {
            nickTextView.setText(nike);
        }

        if (!TextUtils.isEmpty(portrait)) {
            Glide.with(AppManager.mContext)
                    .load(portrait)
                    .error(R.drawable.ease_default_avatar)
                    .transform(new CenterCrop(AppManager.mContext), new GlideRoundTransformUtils(AppManager.mContext, 5))
                    .placeholder(R.drawable.ease_default_avatar).into(mSwingCard);
        }
        if (!isInComingCall) {// outgoing call
            soundPool = new SoundPool(1, AudioManager.STREAM_RING, 0);
            outgoing = soundPool.load(this, R.raw.em_outgoing, 1);

            comingBtnContainer.setVisibility(View.INVISIBLE);
            hangupBtn.setVisibility(View.VISIBLE);
            st1 = getResources().getString(R.string.Are_connected_to_each_other);
            callStateTextView.setText(st1);

            handler.sendEmptyMessage(MSG_CALL_MAKE_VOICE);

            handler.postDelayed(new Runnable() {
                public void run() {
                    streamID = playMakeCallSounds();
                }
            }, 300);
        } else { // incoming call
            voiceContronlLayout.setVisibility(View.INVISIBLE);
            Uri ringUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            audioManager.setMode(AudioManager.MODE_RINGTONE);
            audioManager.setSpeakerphoneOn(true);
            ringtone = RingtoneManager.getRingtone(this, ringUri);
            ringtone.play();
        }
        final int MAKE_CALL_TIMEOUT = 50 * 1000;
        handler.removeCallbacks(timeoutHangup);
        handler.postDelayed(timeoutHangup, MAKE_CALL_TIMEOUT);

        mManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        //获取系统服务POWER_SERVICE，返回一个PowerManager对象
        localPowerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        //获取PowerManager.WakeLock对象,后面的参数|表示同时传入两个值,最后的是LogCat里用的Tag
        localWakeLock = this.localPowerManager.newWakeLock(32, "MyPower");//第一个参数为电源锁级别，第二个是日志tag
    }

    //调用距离传感器，控制屏幕
    private SensorManager mManager;//传感器管理对象
    //屏幕开关
    private PowerManager localPowerManager = null;//电源管理对象
    private PowerManager.WakeLock localWakeLock = null;//电源锁

    @Override
    public void onSensorChanged(SensorEvent event) {
        float[] its = event.values;
        //Log.d(TAG,"its array:"+its+"sensor type :"+event.sensor.getType()+" proximity type:"+Sensor.TYPE_PROXIMITY);
        if (its != null && event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            //经过测试，当手贴近距离感应器的时候its[0]返回值为0.0，当手离开时返回1.0
            if (its[0] == 0.0) {
                // 贴近手机
                if (localWakeLock.isHeld()) {
                    return;
                } else {
                    localWakeLock.acquire();// 申请设备电源锁
                }
            } else {
                // 远离手机
                if (localWakeLock.isHeld()) {
                    return;
                } else {
                    localWakeLock.setReferenceCounted(false);
                    // 释放设备电源锁
                    localWakeLock.release();
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }


    JSONObject sendUserInfo_self;
    JSONObject sendUserInfo;


    @Override
    protected void onResume() {
        super.onResume();
        try {
            //TODO 语音携带体
            String callExt = EMClient.getInstance().callManager().getCurrentCallSession().getExt();
            Log.d("callExt", "实时语音携带的消息：" + callExt);
            for (int i = 0; i < callExt.length(); i++) {
                char charAt = callExt.charAt(i);
                String string = new String(String.valueOf(charAt));
                if (string.equals("|")) {
                    userInfo_self = callExt.substring(0, i);
                    userInfo = callExt.substring(i + 1, callExt.length());
                    break;
                }
            }

            sendUserInfo_self = new JSONObject(userInfo_self);
            sendUserInfo = new JSONObject(userInfo);

            userInfoSelfBean = new Gson().fromJson(userInfo_self, UserInfoSelfDetailsBean.class);
            userInfoBean = new Gson().fromJson(userInfo, UserInfoDetailsBean.class);

            //TODO 消息 展示
            EaseConversationAdapter.requestNamePic(userInfoSelfBean.username_self, userInfoSelfBean.portrait_self);

            if (!TextUtils.isEmpty(userInfoSelfBean.username_self))
                nickTextView.setText(userInfoSelfBean.username_self);
            if (!TextUtils.isEmpty(userInfoSelfBean.portrait_self))
                Glide.with(AppManager.mContext)
                        .load(userInfoSelfBean.portrait_self)
                        .error(R.drawable.ease_default_avatar)
                        .transform(new CenterCrop(AppManager.mContext), new GlideRoundTransformUtils(AppManager.mContext, 5))
                        .placeholder(R.drawable.ease_default_avatar)
                        .into(mSwingCard);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        mManager.registerListener((SensorEventListener) this, mManager.getDefaultSensor(Sensor.TYPE_PROXIMITY),// 距离感应器
                SensorManager.SENSOR_DELAY_NORMAL);//注册传感器，第一个参数为距离监听器，第二个是传感器类型，第三个是延迟类型
    }

    /**
     * set call state listener
     */
    void addCallStateListener() {
        callStateListener = new EMCallStateChangeListener() {

            @Override
            public void onCallStateChanged(CallState callState, final CallError error) {
                // Message msg = handler.obtainMessage();
                EMLog.d("EMCallManager", "onCallStateChanged:" + callState);
                switch (callState) {

                    case CONNECTING:  // 正在连接对方
                        isThroughTo = false;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                callStateTextView.setText(st1);
                            }
                        });
                        break;
                    case CONNECTED: // 双方已经建立连接
                        isThroughTo = false;
                        //获取扩展内容
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String st3 = getResources().getString(R.string.have_connected_with);
                                callStateTextView.setText(st3);
                            }
                        });
                        break;

                    case ACCEPTED:// 电话接通成功
                        isThroughTo = true;
                        handler.removeCallbacks(timeoutHangup);
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                try {
                                    if (soundPool != null)
                                        soundPool.stop(streamID);
                                } catch (Exception e) {
                                }
                                if (!isHandsfreeState)
                                    closeSpeakerOn();
                                //show relay or direct call, for testing purpose
                                ((TextView) findViewById(R.id.tv_is_p2p)).setText(EMClient.getInstance().callManager().isDirectCall()
                                        ? R.string.direct_call : R.string.relay_call);
                                chronometer.setVisibility(View.VISIBLE);
                                chronometer.setBase(SystemClock.elapsedRealtime());
                                // duration start
                                chronometer.start();
                                String str4 = getResources().getString(R.string.In_the_call);
                                callStateTextView.setText(str4);
                                callingState = CallingState.NORMAL;
                                startMonitor();
                            }
                        });
                        break;
                    case NETWORK_UNSTABLE:
                        runOnUiThread(new Runnable() {
                            public void run() {
                                netwrokStatusVeiw.setVisibility(View.VISIBLE);
                                if (error == CallError.ERROR_NO_DATA) {
                                    netwrokStatusVeiw.setText(R.string.no_call_data);
                                } else {
                                    netwrokStatusVeiw.setText(R.string.network_unstable);
                                }
                            }
                        });
                        break;
                    case NETWORK_NORMAL:
                        runOnUiThread(new Runnable() {
                            public void run() {
                                netwrokStatusVeiw.setVisibility(View.INVISIBLE);
                            }
                        });
                        break;
                    case VOICE_PAUSE:
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(getApplicationContext(), "VOICE_PAUSE", Toast.LENGTH_SHORT).show();
                            }
                        });
                        break;
                    case VOICE_RESUME:
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(getApplicationContext(), "VOICE_RESUME", Toast.LENGTH_SHORT).show();
                            }
                        });
                        break;
                    case DISCONNECTED: // 电话断了
                        isThroughTo = false;
                        handler.removeCallbacks(timeoutHangup);
                        @SuppressWarnings("UnnecessaryLocalVariable") final CallError fError = error;
                        runOnUiThread(new Runnable() {
                            private void postDelayedCloseMsg() {
                                handler.postDelayed(new Runnable() {

                                    @Override
                                    public void run() {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                removeCallStateListener();
                                                saveCallRecord();
                                                Animation animation = new AlphaAnimation(1.0f, 0.0f);
                                                animation.setDuration(800);
                                                findViewById(R.id.root_layout).startAnimation(animation);
                                                finish();
                                            }
                                        });
                                    }
                                }, 200);
                            }

                            @Override
                            public void run() {
                                chronometer.stop();
                                callDruationText = chronometer.getText().toString();
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
                                String st11 = getResources().getString(R.string.hang_up);

                                String str = null;
                                if (fError == CallError.REJECTED) {
                                    callingState = CallingState.BEREFUSED;
                                    callStateTextView.setText(st2);
                                    str = st2;
                                } else if (fError == CallError.ERROR_TRANSPORT) {
                                    callStateTextView.setText(st3);
                                    str = st3;
                                } else if (fError == CallError.ERROR_UNAVAILABLE) {
                                    callingState = CallingState.OFFLINE;
                                    callStateTextView.setText(st4);
                                    str = st4;
                                } else if (fError == CallError.ERROR_BUSY) {
                                    callingState = CallingState.BUSY;
                                    callStateTextView.setText(st5);
                                    str = st5;
                                } else if (fError == CallError.ERROR_NORESPONSE) {
                                    callingState = CallingState.NO_RESPONSE;
                                    callStateTextView.setText(st6);
                                    str = st6;
                                } else if (fError == CallError.ERROR_LOCAL_SDK_VERSION_OUTDATED || fError == CallError.ERROR_REMOTE_SDK_VERSION_OUTDATED) {
                                    callingState = CallingState.VERSION_NOT_SAME;
                                    callStateTextView.setText(R.string.call_version_inconsistent);
                                } else {
                                    if (isRefused) {
                                        callingState = CallingState.REFUSED;
                                        callStateTextView.setText(st1);
                                        str = st1;
                                    } else if (isAnswered) {
                                        callingState = CallingState.NORMAL;
                                        if (endCallTriggerByMe) {
//                                        callStateTextView.setText(st7);
                                        } else {
                                            callStateTextView.setText(st8);
                                            str = st8;
                                        }
                                    } else {
                                        if (isInComingCall) {
                                            callingState = CallingState.UNANSWERED;
                                            callStateTextView.setText(st9);
                                            str = st9;
                                        } else {
                                            if (callingState != CallingState.NORMAL) {
                                                callingState = CallingState.CANCELLED;
                                                callStateTextView.setText(st10);
                                                str = st10;
                                            } else {
                                                callStateTextView.setText(st11);
                                            }
                                        }
                                    }
                                }
                                if (str != null) {
                                    Toast.makeText(VoiceCallActivity.this, str, Toast.LENGTH_SHORT).show();
                                }
                                postDelayedCloseMsg();
                            }

                        });

                        break;

                    default:
                        break;
                }

            }
        };
        EMClient.getInstance().callManager().addCallStateChangeListener(callStateListener);
    }

    void removeCallStateListener() {
        EMClient.getInstance().callManager().removeCallStateChangeListener(callStateListener);
    }

    private boolean isThroughTo;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_refuse_call:
                isRefused = true;
                refuseBtn.setEnabled(false);
                handler.sendEmptyMessage(MSG_CALL_REJECT);
                //TODO 拒接
                String s = AppConfig.getAppConfig(VoiceCallActivity.this).get(AppConfig.PREF_KEY_CODE);
                if (isThroughTo) {

                } else if (!username.equals("sl_" + AppConfig.getAppConfig(VoiceCallActivity.this).get(AppConfig.PREF_KEY_CODE))) {
                    mMessage = EMMessage.createTxtSendMessage("已拒接", username);
                    readUserInfoDetailsMessage();
                    sendUserInfoDetailsMessage(mMessage);
                    //发送消息
                    EMClient.getInstance().chatManager().sendMessage(mMessage);
                    isThroughTo = false;
                }
                break;
            case R.id.btn_answer_call:
                answerBtn.setEnabled(false);
                closeSpeakerOn();
                callStateTextView.setText("正在接听...");
                comingBtnContainer.setVisibility(View.INVISIBLE);
                hangupBtn.setVisibility(View.VISIBLE);
                voiceContronlLayout.setVisibility(View.VISIBLE);
                handler.sendEmptyMessage(MSG_CALL_ANSWER);
                //TODO 接通
                isThroughTo = true;
                break;
            case R.id.btn_hangup_call:
                hangupBtn.setEnabled(false);
                chronometer.stop();
                endCallTriggerByMe = true;
                callStateTextView.setText(getResources().getString(R.string.hanging_up));
                handler.sendEmptyMessage(MSG_CALL_END);
                //TODO 挂断
                if (isThroughTo) {
                    mMessage = EMMessage.createTxtSendMessage("通话时长:" + chronometer.getText().toString(), username);
                    readUserInfoDetailsMessage();
                    sendUserInfoDetailsMessage(mMessage);
                    isThroughTo = false;
                    //发送消息
                    EMClient.getInstance().chatManager().sendMessage(mMessage);
                } else if (!username.equals("sl_" + AppConfig.getAppConfig(VoiceCallActivity.this).get(AppConfig.PREF_KEY_CODE))) {
                    mMessage = EMMessage.createTxtSendMessage("已取消", username);
                    readUserInfoDetailsMessage();
                    sendUserInfoDetailsMessage(mMessage);
                    isThroughTo = false;
                    //发送消息
                    EMClient.getInstance().chatManager().sendMessage(mMessage);
                }
                break;
            case R.id.iv_mute:
                if (isMuteState) {
                    muteImage.setImageResource(R.drawable.em_icon_mute_normal);
                    try {
                        EMClient.getInstance().callManager().resumeVoiceTransfer();
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                    }
                    isMuteState = false;
                } else {
                    muteImage.setImageResource(R.drawable.em_icon_mute_on);
                    try {
                        EMClient.getInstance().callManager().pauseVoiceTransfer();
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                    }
                    isMuteState = true;
                }
                break;
            case R.id.iv_handsfree:
                if (isHandsfreeState) {
                    handsFreeImage.setImageResource(R.drawable.em_icon_speaker_normal);
                    closeSpeakerOn();
                    isHandsfreeState = false;
                } else {
                    handsFreeImage.setImageResource(R.drawable.em_icon_speaker_on);
                    openSpeakerOn();
                    isHandsfreeState = true;
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (mManager != null) {
                //释放电源锁
                localWakeLock.release();
                //注销传感器监听
                mManager.unregisterListener(this);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        try {
            EMClient.getInstance().callManager().endCall();
        } catch (EMNoActiveCallException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        callDruationText = chronometer.getText().toString();
    }

    /**
     * for debug & testing, you can remove this when release
     */
    void startMonitor() {
        monitor = true;
        new Thread(new Runnable() {
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        ((TextView) findViewById(R.id.tv_is_p2p)).setText(EMClient.getInstance().callManager().isDirectCall()
                                ? R.string.direct_call : R.string.relay_call);
                    }
                });
                while (monitor) {
                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                    }
                }
            }
        }, "CallMonitor").start();
    }


    private void readUserInfoDetailsMessage() {
        try {
            //TODO TEXT 携带消息
            JSONObject object_self = new JSONObject();
            JSONObject object = new JSONObject();
            if (userInfo_self == null) {
                userInfo_self = "";
            }

            if (userInfo == null) {
                userInfo = "";
            }

            if (userInfo_self.equals("")) {
                object_self.put("CODE_self", AppConfig.getAppConfig(this).get(AppConfig.PREF_KEY_CODE));
                object_self.put("phone_self", AppConfig.getAppConfig(this).get(AppConfig.PREF_KEY_POST_NAME));
                object_self.put("sex_self", AppConfig.getAppConfig(this).get(AppConfig.PREF_KEY_SEX));
                object_self.put("post_title_self", AppConfig.getAppConfig(this).get(AppConfig.PREF_KEY_POST_NAME));
                object_self.put("username_self", AppConfig.getAppConfig(this).get(AppConfig.PREF_KEY_USERNAME));
                object_self.put("portrait_self", AppConfig.getAppConfig(this).get(AppConfig.PREF_KEY_PORTRAITS));
                object_self.put("email_self", AppConfig.getAppConfig(this).get(AppConfig.PREF_KEY_USER_EMAIL));
                object_self.put("department_name_self", AppConfig.getAppConfig(this).get(AppConfig.PREF_KEY_DEPARTMENT_NAME));

                userInfo_self = object_self.toString();
            }
            if (userInfo.equals("")) {
                object.put("CODE", getIntent().getStringExtra("CODE"));
                object.put("phone", getIntent().getStringExtra("phone"));
                object.put("sex", getIntent().getStringExtra("sex"));
                object.put("post_title", getIntent().getStringExtra("post_name"));
                object.put("username", getIntent().getStringExtra("nike"));
                object.put("portrait", getIntent().getStringExtra("portrait"));
                object.put("email", getIntent().getStringExtra("email"));
                object.put("department_name", getIntent().getStringExtra("department_name"));
                userInfo = object.toString();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    JSONObject sendJson_self;
    JSONObject sendJson;

    private void sendUserInfoDetailsMessage(EMMessage message) {
        String from = message.getFrom();
        //  userInfo_self = message.getStringAttribute("userInfo_self", "");
        //   userInfo = message.getStringAttribute("userInfo", "");
        //UserInfoSelfDetailsBean bean = new Gson().fromJson(userInfo_self, UserInfoSelfDetailsBean.class);
        try {
            String newUserInfo_self = null;
            String newUserInfo = null;
            if (userInfoSelfBean == null) {
                sendJson_self = new JSONObject(userInfo_self);
                sendJson = new JSONObject(userInfo);
                mMessage.setAttribute("userInfo_self", sendJson_self);
                mMessage.setAttribute("userInfo", sendJson);
                return;
            }
            if (!from.equals("sl_" + userInfoSelfBean.CODE_self)) {
                newUserInfo_self = userInfo_self.replaceAll("_self", "");
                newUserInfo = userInfo.replace("phone", "phone_self");
                newUserInfo = newUserInfo.replace("CODE", "CODE_self");
                newUserInfo = newUserInfo.replace("sex", "sex_self");
                newUserInfo = newUserInfo.replace("post_title", "post_title_self");
                newUserInfo = newUserInfo.replace("username", "username_self");
                newUserInfo = newUserInfo.replace("portrait", "portrait_self");
                newUserInfo = newUserInfo.replace("/portrait_self", "/portrait/");
                newUserInfo = newUserInfo.replace("email", "email_self");
                newUserInfo = newUserInfo.replace("department_name", "department_name_self");
                sendJson_self = new JSONObject(newUserInfo_self);
                sendJson = new JSONObject(newUserInfo);
                mMessage.setAttribute("userInfo_self", sendJson);
                mMessage.setAttribute("userInfo", sendJson_self);
            } else {
                sendJson_self = new JSONObject(userInfo_self);
                sendJson = new JSONObject(userInfo);
                mMessage.setAttribute("userInfo_self", sendJson_self);
                mMessage.setAttribute("userInfo", sendJson);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
