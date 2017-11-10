package com.shanlinjinrong.oa.ui.activity.message;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.widget.Toast;

import com.hyphenate.EMError;
import com.hyphenate.chat.EMCallManager;
import com.hyphenate.chat.EMCallStateChangeListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.db.FriendsInfoCacheSvc;
import com.hyphenate.easeui.model.UserInfoDetailsBean;
import com.hyphenate.easeui.model.UserInfoSelfDetailsBean;
import com.hyphenate.exceptions.EMServiceNotReadyException;
import com.hyphenate.util.EMLog;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.manager.AppConfig;
import com.shanlinjinrong.oa.ui.base.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

@SuppressLint("Registered")
public class CallActivity extends BaseActivity {
    public final static String TAG = "CallActivity";
    protected final int MSG_CALL_MAKE_VIDEO = 0;
    protected final int MSG_CALL_MAKE_VOICE = 1;
    protected final int MSG_CALL_ANSWER = 2;
    protected final int MSG_CALL_REJECT = 3;
    protected final int MSG_CALL_END = 4;
    protected final int MSG_CALL_RLEASE_HANDLER = 5;
    protected final int MSG_CALL_SWITCH_CAMERA = 6;
    public String sideInfo;
    protected String myAccount;
    protected boolean isInComingCall;
    protected boolean isRefused = false;
    protected String username;
    protected CallingState callingState = CallingState.CANCELLED;
    protected String callDruationText;
    protected String msgid;
    protected AudioManager audioManager;
    protected SoundPool soundPool;
    protected Ringtone ringtone;
    protected int outgoing;
    protected EMCallStateChangeListener callStateListener;
    protected boolean isAnswered = false;
    protected int streamID = -1;
    protected String userInfo_self;
    protected String userInfo;
    protected UserInfoSelfDetailsBean userInfoSelfBean;
    protected UserInfoDetailsBean userInfoBean;


    EMCallManager.EMCallPushProvider pushProvider;

    /**
     * 0：voice call，1：video call
     */
    protected int callType = 0;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
    }

    @Override
    protected void onDestroy() {
        if (soundPool != null)
            soundPool.release();
        if (ringtone != null && ringtone.isPlaying())
            ringtone.stop();
        audioManager.setMode(AudioManager.MODE_NORMAL);
        audioManager.setMicrophoneMute(false);

        if (callStateListener != null)
            EMClient.getInstance().callManager().removeCallStateChangeListener(callStateListener);

        if (pushProvider != null) {
            EMClient.getInstance().callManager().setPushProvider(null);
            pushProvider = null;
        }
        releaseHandler();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        EMLog.d(TAG, "onBackPressed");
        handler.sendEmptyMessage(MSG_CALL_END);
        saveCallRecord();
        finish();
        super.onBackPressed();
    }

    Runnable timeoutHangup = new Runnable() {

        @Override
        public void run() {
            handler.sendEmptyMessage(MSG_CALL_END);
        }
    };

    HandlerThread callHandlerThread = new HandlerThread("callHandlerThread");

    {
        callHandlerThread.start();
    }


    protected String send_CODE;
    protected String send_phone;
    protected String send_sex;
    protected String send_post_title;
    protected String send_username;
    protected String send_portrait;
    protected String send_email;
    protected String send_department_name;
    protected boolean isMakeVoiceCall;


    protected Handler handler = new Handler(callHandlerThread.getLooper()) {
        @Override
        public void handleMessage(Message msg) {
            EMLog.d("EMCallManager CallActivity", "handleMessage ---enter block--- msg.what:" + msg.what);
            switch (msg.what) {
                case MSG_CALL_MAKE_VIDEO:
                case MSG_CALL_MAKE_VOICE:
                    try {
                        if (msg.what == MSG_CALL_MAKE_VIDEO) {
                            EMClient.getInstance().callManager().makeVideoCall(username);
                        } else {
                            String nickName = FriendsInfoCacheSvc.getInstance(CallActivity.this).getNickName("SL_" + myAccount);
                            String portrait = FriendsInfoCacheSvc.getInstance(CallActivity.this).getPortrait("SL_" + myAccount);

                            JSONObject json_self = new JSONObject();
                            JSONObject json = new JSONObject();

                            //TODO 自己的信息
                            json_self.put("CODE_self", AppConfig.getAppConfig(CallActivity.this).get(AppConfig.PREF_KEY_CODE));
                            json_self.put("phone_self", AppConfig.getAppConfig(CallActivity.this).get(AppConfig.PREF_KEY_PHONE));
                            json_self.put("sex_self", AppConfig.getAppConfig(CallActivity.this).get(AppConfig.PREF_KEY_SEX));
                            json_self.put("post_title_self", AppConfig.getAppConfig(CallActivity.this).get(AppConfig.PREF_KEY_POST_NAME));
                            json_self.put("username_self", AppConfig.getAppConfig(CallActivity.this).get(AppConfig.PREF_KEY_USERNAME));
                            json_self.put("portrait_self", AppConfig.getAppConfig(CallActivity.this).get(AppConfig.PREF_KEY_PORTRAITS));
                            json_self.put("email_self", AppConfig.getAppConfig(CallActivity.this).get(AppConfig.PREF_KEY_USER_EMAIL));
                            json_self.put("department_name_self", AppConfig.getAppConfig(CallActivity.this).get(AppConfig.PREF_KEY_DEPARTMENT_NAME));

                            //TODO 对方的信息
                            if (userInfoBean != null) {
                                if (send_CODE == null) {
                                    send_CODE = userInfoBean.CODE;
                                }
                                if (send_phone == null) {
                                    send_phone = userInfoBean.phone;
                                }
                                if (send_sex == null) {
                                    send_sex = userInfoBean.sex;
                                }
                                if (send_post_title == null) {
                                    send_post_title = userInfoBean.post_title;
                                }
                                if (send_username == null) {
                                    send_username = userInfoBean.username;
                                }
                                if (send_portrait == null) {
                                    send_portrait = userInfoBean.portrait;
                                }
                                if (send_email == null) {
                                    send_email = userInfoBean.email;
                                }
                                if (send_department_name == null) {
                                    send_department_name = userInfoBean.department_name;
                                }
                            }
                            json.put("CODE", send_CODE);
                            json.put("phone", send_phone);
                            json.put("sex", send_sex);
                            json.put("post_title", send_post_title);
                            json.put("username", send_username);
                            json.put("portrait", send_portrait);
                            json.put("email", send_email);
                            json.put("department_name", send_department_name);

                             userInfo_self = json_self.toString();
                             userInfo = json.toString();

                            //发起实时语音通讯
                            EMClient.getInstance().callManager().makeVoiceCall(username, userInfo_self + "|" + userInfo);
                            isMakeVoiceCall = true;
                        }
                    } catch (final EMServiceNotReadyException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            public void run() {
                                String st2 = e.getMessage();
                                if (e.getErrorCode() == EMError.CALL_REMOTE_OFFLINE) {
                                    st2 = getResources().getString(R.string.The_other_is_not_online);
                                } else if (e.getErrorCode() == EMError.USER_NOT_LOGIN) {
                                    st2 = getResources().getString(R.string.Is_not_yet_connected_to_the_server);
                                } else if (e.getErrorCode() == EMError.INVALID_USER_NAME) {
                                    st2 = getResources().getString(R.string.illegal_user_name);
                                } else if (e.getErrorCode() == EMError.CALL_BUSY) {
                                    st2 = getResources().getString(R.string.The_other_is_on_the_phone);
                                } else if (e.getErrorCode() == EMError.NETWORK_ERROR) {
                                    st2 = getResources().getString(R.string.can_not_connect_chat_server_connection);
                                } else if (e.getMessage().equals("exception isConnected:false")) {
                                    st2 = getResources().getString(R.string.can_not_connect_chat_server_connection);
                                }
                                Toast.makeText(CallActivity.this, st2, Toast.LENGTH_LONG).show();
                                finish();
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case MSG_CALL_ANSWER:
                    EMLog.d(TAG, "MSG_CALL_ANSWER");
                    if (ringtone != null)
                        ringtone.stop();
                    if (isInComingCall) {
                        try {
                            EMClient.getInstance().callManager().answerCall();
                            isAnswered = true;
                            // meizu MX5 4G, hasDataConnection(context) return status is incorrect
                            // MX5 con.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected() return false in 4G
                            // so we will not judge it, App can decide whether judge the network status

//                        if (NetUtils.hasDataConnection(CallActivity.this)) {
//                            EMClient.getInstance().callManager().answerCall();
//                            isAnswered = true;
//                        } else {
//                            runOnUiThread(new Runnable() {
//                                public void run() {
//                                    final String st2 = getResources().getString(R.string.Is_not_yet_connected_to_the_server);
//                                    Toast.makeText(CallActivity.this, st2, Toast.LENGTH_SHORT).show();
//                                }
//                            });
//                            throw new Exception();
//                        }
                        } catch (Exception e) {
                            e.printStackTrace();
                            saveCallRecord();
                            finish();
                            return;
                        }
                    } else {
                        EMLog.d(TAG, "answer call isInComingCall:false");
                    }
                    break;
                case MSG_CALL_REJECT:
                    if (ringtone != null)
                        ringtone.stop();
                    try {
                        EMClient.getInstance().callManager().rejectCall();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                        saveCallRecord();
                        finish();
                    }
                    callingState = CallingState.REFUSED;
                    break;
                case MSG_CALL_END:
                    if (soundPool != null)
                        soundPool.stop(streamID);
                    try {
                        EMClient.getInstance().callManager().endCall();
                    } catch (Exception e) {
                        saveCallRecord();
                        finish();
                    }

                    break;
                case MSG_CALL_RLEASE_HANDLER:
                    try {
                        EMClient.getInstance().callManager().endCall();
                    } catch (Exception e) {
                    }
                    handler.removeCallbacks(timeoutHangup);
                    handler.removeMessages(MSG_CALL_MAKE_VIDEO);
                    handler.removeMessages(MSG_CALL_MAKE_VOICE);
                    handler.removeMessages(MSG_CALL_ANSWER);
                    handler.removeMessages(MSG_CALL_REJECT);
                    handler.removeMessages(MSG_CALL_END);
                    callHandlerThread.quit();
                    break;
                case MSG_CALL_SWITCH_CAMERA:
                    EMClient.getInstance().callManager().switchCamera();
                    break;
                default:
                    break;
            }
            EMLog.d("EMCallManager CallActivity", "handleMessage ---exit block--- msg.what:" + msg.what);
        }
    };

    void releaseHandler() {
        handler.sendEmptyMessage(MSG_CALL_RLEASE_HANDLER);
    }

    /**
     * play the incoming call ringtone
     */
    protected int playMakeCallSounds() {
        try {
            audioManager.setMode(AudioManager.MODE_RINGTONE);
            audioManager.setSpeakerphoneOn(true);

            // play
            int id = soundPool.play(outgoing, // sound resource
                    0.3f, // left volume
                    0.3f, // right volume
                    1,    // priority
                    -1,   // loop，0 is no loop，-1 is loop forever
                    1);   // playback rate (1.0 = normal playback, range 0.5 to 2.0)
            return id;
        } catch (Exception e) {
            return -1;
        }
    }

    protected void openSpeakerOn() {
        try {
            if (!audioManager.isSpeakerphoneOn())
                audioManager.setSpeakerphoneOn(true);
            audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void closeSpeakerOn() {

        try {
            if (audioManager != null) {
                // int curVolume =
                // audioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL);
                if (audioManager.isSpeakerphoneOn())
                    audioManager.setSpeakerphoneOn(false);
                audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
                // audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL,
                // curVolume, AudioManager.STREAM_VOICE_CALL);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * save call record
     */
    protected void saveCallRecord() {
//        @SuppressWarnings("UnusedAssignment") EMMessage message = null;
//        @SuppressWarnings("UnusedAssignment") EMTextMessageBody txtBody = null;
//        if (!isInComingCall) { // outgoing call
//            message = EMMessage.createSendMessage(EMMessage.Type.TXT);
//            message.setFrom(username);
//        } else {
//            message = EMMessage.createReceiveMessage(EMMessage.Type.TXT);
//            message.setFrom(username);
//        }
//
        String st1 = getResources().getString(R.string.call_duration);
        String st2 = getResources().getString(R.string.Refused);
        String st3 = getResources().getString(R.string.The_other_party_has_refused_to);
        String st4 = getResources().getString(R.string.The_other_is_not_online);
        String st5 = getResources().getString(R.string.The_other_is_on_the_phone);
        String st6 = getResources().getString(R.string.The_other_party_did_not_answer);
        String st7 = getResources().getString(R.string.did_not_answer);
        String st8 = getResources().getString(R.string.Has_been_cancelled);
//        switch (callingState) {
//            case NORMAL:
//                txtBody = new EMTextMessageBody(st1 + callDruationText);
//                break;
//            case REFUSED:
//                txtBody = new EMTextMessageBody(st2);
//                break;
//            case BEREFUSED:
//                txtBody = new EMTextMessageBody(st3);
//                break;
//            case OFFLINE:
//                txtBody = new EMTextMessageBody(st4);
//                break;
//            case BUSY:
//                txtBody = new EMTextMessageBody(st5);
//                break;
//            case NO_RESPONSE:
//                txtBody = new EMTextMessageBody(st6);
//                break;
//            case UNANSWERED:
//                txtBody = new EMTextMessageBody(st7);
//                break;
//            case VERSION_NOT_SAME:
//                txtBody = new EMTextMessageBody(getString(R.string.call_version_inconsistent));
//                break;
//            default:
//                txtBody = new EMTextMessageBody(st8);
//                break;
//        }
//        // set message extension
//        if (callType == 0)
//            message.setAttribute(Constant.MESSAGE_ATTR_IS_VOICE_CALL, true);
//        else
//            message.setAttribute(Constant.MESSAGE_ATTR_IS_VIDEO_CALL, true);
//
//        // set message body
//        message.addBody(txtBody);
//        message.setMsgId(msgid);
//        message.setStatus(Status.SUCCESS);
//
//        // save
//        try {
//            EMClient.getInstance().chatManager().saveMessage(message);
//        } catch (Exception e) {
//            LogUtils.e(e.toString());
//        }
    }

    enum CallingState {
        CANCELLED, NORMAL, REFUSED, BEREFUSED, UNANSWERED, OFFLINE, NO_RESPONSE, BUSY, VERSION_NOT_SAME
    }
}
