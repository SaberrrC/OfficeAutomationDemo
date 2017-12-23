/************************************************************
 *  * Hyphenate CONFIDENTIAL 
 * __________________ 
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved. 
 *
 * NOTICE: All information contained herein is, and remains 
 * the property of Hyphenate Inc.
 * Dissemination of this information or reproduction of this material 
 * is strictly forbidden unless prior written permission is obtained
 * from Hyphenate Inc.
 */
package com.hyphenate.easeui.model;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.EaseUI.EaseSettingsProvider;
import com.hyphenate.easeui.UserDetailsBean;
import com.hyphenate.easeui.db.Friends;
import com.hyphenate.easeui.db.FriendsInfoCacheSvc;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.util.EMLog;
import com.hyphenate.util.EasyUtils;

import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpConfig;
import org.kymjs.kjframe.http.HttpParams;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * new message notifier class
 * <p>
 * this class is subject to be inherited and implement the relative APIs
 */
public class EaseNotifier {
    private final static String TAG = "notify";
    Ringtone ringtone = null;

    protected final static String[] msg_eng = {"sent a message", "sent a picture", "sent a voice",
            "sent location message", "sent a video", "sent a file", "%1 contacts sent %2 messages"
    };
    protected final static String[] msg_ch  = {"发来一条消息", "[图片]", "[语音]", "发来位置信息", "发来一个视频", "[文件]",
            "%1个联系人发来%2条消息"
    };

    protected static int notifyID           = 0525; // start notification id
    protected static int foregroundNotifyID = 0555;

    protected NotificationManager notificationManager = null;

    protected HashSet<String> fromUsers       = new HashSet<String>();
    protected int             notificationNum = 0;

    protected Context                      appContext;
    protected String                       packageName;
    protected String[]                     msgs;
    protected long                         lastNotifiyTime;
    protected AudioManager                 audioManager;
    protected Vibrator                     vibrator;
    protected EaseNotificationInfoProvider notificationInfoProvider;
    private   String                       mNickName;
    private   String                       notifyText;

    public EaseNotifier() {
    }

    /**
     * this function can be override
     *
     * @param context
     * @return
     */
    public EaseNotifier init(Context context) {
        appContext = context;
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        packageName = appContext.getApplicationInfo().packageName;
        if (Locale.getDefault().getLanguage().equals("zh")) {
            msgs = msg_ch;
        } else {
            msgs = msg_eng;
        }

        audioManager = (AudioManager) appContext.getSystemService(Context.AUDIO_SERVICE);
        vibrator = (Vibrator) appContext.getSystemService(Context.VIBRATOR_SERVICE);

        return this;
    }

    /**
     * this function can be override
     */
    public void reset() {
        resetNotificationCount();
        cancelNotificaton();
    }

    void resetNotificationCount() {
        notificationNum = 0;
        fromUsers.clear();
    }

    void cancelNotificaton() {
        if (notificationManager != null)
            notificationManager.cancel(notifyID);
    }

    /**
     * handle the new message
     * this function can be override
     *
     * @param message
     */
    public synchronized void onNewMsg(EMMessage message) {
        if (EaseCommonUtils.isSilentMessage(message)) {
            return;
        }
        EaseSettingsProvider settingsProvider = EaseUI.getInstance().getSettingsProvider();

        if (!settingsProvider.isMsgNotifyAllowed(message)) {
            return;
        }

        // check if app running background
        if (!EasyUtils.isAppRunningForeground(appContext)) {
            sendNotification(message, false);
        } else {
            sendNotification(message, true);
        }

        vibrateAndPlayTone(message);
    }

    public synchronized void onNewMesg(List<EMMessage> messages) {
        if (EaseCommonUtils.isSilentMessage(messages.get(messages.size() - 1))) {
            return;
        }
        EaseSettingsProvider settingsProvider = EaseUI.getInstance().getSettingsProvider();
        if (!settingsProvider.isMsgNotifyAllowed(null)) {
            return;
        }
        // check if app running background
        if (!EasyUtils.isAppRunningForeground(appContext)) {
            EMLog.d(TAG, "app is running in backgroud");
            sendNotification(messages, false);
        } else {
            sendNotification(messages, true);
        }
        vibrateAndPlayTone(messages.get(messages.size() - 1));
    }

    /**
     * send it to notification bar
     * This can be override by subclass to provide customer implementation
     *
     * @param messages
     * @param isForeground
     */
    protected void sendNotification(List<EMMessage> messages, boolean isForeground) {
        for (EMMessage message : messages) {
            if (!isForeground) {
                notificationNum++;
                fromUsers.add(message.getFrom());
            }
        }
        sendNotification(messages.get(messages.size() - 1), isForeground, false);
    }

    protected void sendNotification(EMMessage message, boolean isForeground) {
        sendNotification(message, isForeground, true);
    }

    private static final String APP_CONFIG              = "app_config";
    public static final  String DEFAULT_ARGUMENTS_VALUE = "";

    /**
     * send it to notification bar
     * This can be override by subclass to provide customer implementation
     *
     * @param message
     */
    protected void sendNotification(final EMMessage message, final boolean isForeground, final boolean numIncrease) {
        try {
            notifyText = " ";
            switch (message.getType()) {
                case TXT:
                    notifyText = notifyText.replace(" ", ":");
                    notifyText += ((EMTextMessageBody) message.getBody()).getMessage();
                    break;
                case IMAGE:
                    notifyText += msgs[1];
                    break;
                case VOICE:

                    notifyText += msgs[2];
                    break;
                case LOCATION:
                    notifyText += msgs[3];
                    break;
                case VIDEO:
                    notifyText += msgs[4];
                    break;
                case FILE:
                    notifyText += msgs[5];
                    break;
            }

            PackageManager packageManager = appContext.getPackageManager();
            String appname = (String) packageManager.getApplicationLabel(appContext.getApplicationInfo());

            // notification title
            final String contentTitle = appname;
            if (notificationInfoProvider != null) {
                //推送处理
                Observable.create(new Observable.OnSubscribe<Object>() {
                    @Override
                    public void call(Subscriber<? super Object> subscriber) {
                        mNickName = FriendsInfoCacheSvc.getInstance(appContext).getNickName(message.getFrom());
                        if (TextUtils.isEmpty(mNickName)) {
                            String userCode = message.getFrom().substring(3, message.getFrom().length());
                            String token = appContext.getSharedPreferences(APP_CONFIG, Context.MODE_PRIVATE).getString("pref_key_private_token", DEFAULT_ARGUMENTS_VALUE);
                            String uid = appContext.getSharedPreferences(APP_CONFIG, Context.MODE_PRIVATE).getString("pref_key_user_uid", DEFAULT_ARGUMENTS_VALUE);
                            KJHttp kjHttp = new KJHttp();
                            HttpConfig config = new HttpConfig();
                            HttpConfig.TIMEOUT = 30000;
                            kjHttp.setConfig(config);
                            kjHttp.cleanCache();
                            HttpParams httpParams = new HttpParams();
                            httpParams.putHeaders("token", token);
                            httpParams.putHeaders("uid", uid);
                            //TODO 生产
                            kjHttp.get(EaseConstant.PHP_URL + "user/getinfo/?code=" + userCode, httpParams, new HttpCallBack() {

                                @Override
                                public void onFailure(int errorNo, String strMsg) {
                                    super.onFailure(errorNo, strMsg);

                                    Observable.create(new Observable.OnSubscribe<Object>() {
                                        @Override
                                        public void call(Subscriber<? super Object> subscriber) {
                                            FriendsInfoCacheSvc.
                                                    getInstance(appContext).
                                                    addOrUpdateFriends(new Friends("sl_" + message.getFrom(), "匿名用户", ""));
                                            mNickName = "匿名用户";
                                            subscriber.onCompleted();
                                        }
                                    }).subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(new Action1<Object>() {
                                                @Override
                                                public void call(Object o) {
                                                }
                                            }, new Action1<Throwable>() {
                                                @Override
                                                public void call(Throwable throwable) {
                                                    throwable.printStackTrace();
                                                }
                                            }, new Action0() {
                                                @Override
                                                public void call() {
                                                    initNotify(message, numIncrease, isForeground, contentTitle);
                                                }
                                            });
                                }

                                @Override
                                public void onSuccess(String t) {
                                    super.onSuccess(t);
                                    final UserDetailsBean userDetailsBean = new Gson().fromJson(t, UserDetailsBean.class);
                                    if (userDetailsBean != null) {
                                        try {
                                            switch (userDetailsBean.getCode()) {
                                                case 200:
                                                    Observable.create(new Observable.OnSubscribe<Object>() {
                                                        @Override
                                                        public void call(Subscriber<? super Object> subscriber) {
                                                            FriendsInfoCacheSvc.
                                                                    getInstance(appContext).
                                                                    addOrUpdateFriends(new Friends("sl_" + userDetailsBean.getData().get(0).getCode(),
                                                                            userDetailsBean.getData().get(0).getUsername(), "http://" + userDetailsBean.getData().get(0).getImg()));
                                                            mNickName = userDetailsBean.getData().get(0).getUsername();
                                                            subscriber.onCompleted();
                                                        }
                                                    }).subscribeOn(Schedulers.io())
                                                            .observeOn(AndroidSchedulers.mainThread())
                                                            .subscribe(new Action1<Object>() {
                                                                @Override
                                                                public void call(Object o) {
                                                                }
                                                            }, new Action1<Throwable>() {
                                                                @Override
                                                                public void call(Throwable throwable) {
                                                                    throwable.printStackTrace();
                                                                }
                                                            }, new Action0() {
                                                                @Override
                                                                public void call() {
                                                                    initNotify(message, numIncrease, isForeground, contentTitle);
                                                                }
                                                            });
                                                    break;
                                            }
                                        } catch (Throwable e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            });
                            return;
                        }

                        if (!TextUtils.isEmpty(mNickName)) {
                            subscriber.onCompleted();
                        }
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Action1<Object>() {
                            @Override
                            public void call(Object o) {
                            }
                        }, new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                            }
                        }, new Action0() {
                            @Override
                            public void call() {
                                initNotify(message, numIncrease, isForeground, contentTitle);
                            }
                        });


//                String customNotifyText = notificationInfoProvider.getDisplayedText(message);
//                String customCotentTitle = notificationInfoProvider.getTitle(message);
//                if (customNotifyText != null){
//                    notifyText = customNotifyText;
//                }
//
//                if (customCotentTitle != null){
//                    contentTitle = customCotentTitle;
//                }

            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initNotify(EMMessage message, boolean numIncrease, boolean isForeground, String contentTitle) {
        notifyText = mNickName + notifyText;
        // create and send notificaiton
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(appContext)
                .setSmallIcon(appContext.getApplicationInfo().icon)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true);

        Intent msgIntent = appContext.getPackageManager().getLaunchIntentForPackage(packageName);
        if (notificationInfoProvider != null) {
            msgIntent = notificationInfoProvider.getLaunchIntent(message);
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(appContext, notifyID, msgIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (numIncrease) {
            // prepare latest event info section
            if (!isForeground) {
                notificationNum++;
                fromUsers.add(message.getFrom());
            }
        }

        int fromUsersNum = fromUsers.size();
//                                String summaryBody = msgs[6].replaceFirst("%1", Integer.toString(fromUsersNum)).replaceFirst("%2", notifyText);

        if (notificationInfoProvider != null) {
            // lastest text
            String customSummaryBody = notificationInfoProvider.getLatestText(message, fromUsersNum, notificationNum);
            if (customSummaryBody != null) {
                notifyText = customSummaryBody;
            }

            // small icon
            int smallIcon = notificationInfoProvider.getSmallIcon(message);
            if (smallIcon != 0) {
                mBuilder.setSmallIcon(smallIcon);
            }
        }

        mBuilder.setContentTitle(contentTitle);
        mBuilder.setTicker(notifyText);
        mBuilder.setContentText(notifyText);
        mBuilder.setContentIntent(pendingIntent);
        // mBuilder.setNumber(notificationNum);
        Notification notification = mBuilder.build();

        if (isForeground) {
            notificationManager.notify(foregroundNotifyID, notification);
            notificationManager.cancel(foregroundNotifyID);
        } else {
            notificationManager.notify(notifyID, notification);
        }
    }

    /**
     * vibrate and  play tone
     */
    //TODO  播放声音跟震动
    public void vibrateAndPlayTone(EMMessage message) {
        if (System.currentTimeMillis() - lastNotifiyTime < 1000) {
            return;
        }

        try {
            lastNotifiyTime = System.currentTimeMillis();

            EaseSettingsProvider settingsProvider = EaseUI.getInstance().getSettingsProvider();

            if (settingsProvider.isMsgVibrateAllowed(message)) {
                long[] pattern = new long[]{0, 180, 80, 120};
                vibrator.vibrate(pattern, -1);
            }

            if (settingsProvider.isMsgSoundAllowed(message)) {
                if (ringtone == null) {
                    Uri notificationUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                    ringtone = RingtoneManager.getRingtone(appContext, notificationUri);
                    if (ringtone == null) {
                        return;
                    }
                }

                if (!ringtone.isPlaying()) {
                    String vendor = Build.MANUFACTURER;
                    ringtone.play();

                    if (vendor != null && vendor.toLowerCase().contains("samsung")) {
                        Thread ctlThread = new Thread() {
                            public void run() {
                                try {
                                    Thread.sleep(3000);
                                    if (ringtone.isPlaying()) {
                                        ringtone.stop();
                                    }
                                } catch (Exception e) {
                                }
                            }
                        };
                        ctlThread.start();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * set notification info Provider
     *
     * @param provider
     */
    public void setNotificationInfoProvider(EaseNotificationInfoProvider provider) {
        notificationInfoProvider = provider;
    }

    public interface EaseNotificationInfoProvider {
        /**
         * set the notification content, such as "you received a new image from xxx"
         *
         * @param message
         * @return null-will use the default text
         */
        String getDisplayedText(EMMessage message);

        /**
         * set the notification content: such as "you received 5 message from 2 contacts"
         *
         * @param message
         * @param fromUsersNum- number of message sender
         * @param messageNum    -number of messages
         * @return null-will use the default text
         */
        String getLatestText(EMMessage message, int fromUsersNum, int messageNum);

        /**
         * 设置notification标题
         *
         * @param message
         * @return null- will use the default text
         */
        String getTitle(EMMessage message);

        /**
         * set the small icon
         *
         * @param message
         * @return 0- will use the default icon
         */
        int getSmallIcon(EMMessage message);

        /**
         * set the intent when notification is pressed
         *
         * @param message
         * @return null- will use the default icon
         */
        Intent getLaunchIntent(EMMessage message);
    }
}
