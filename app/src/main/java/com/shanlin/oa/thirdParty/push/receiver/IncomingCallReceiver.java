package com.shanlin.oa.thirdParty.push.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

import com.shanlin.oa.thirdParty.huanxin.observer.PhoneCallStateObserver;

/**
 * 作者：王凤旭
 * 时间：2017/8/3
 * 描述：来电状态监听
 */

public class IncomingCallReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (TelephonyManager.ACTION_PHONE_STATE_CHANGED.equals(action)) {
            final String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            PhoneCallStateObserver.getInstance().onCallStateChanged(state);
        }
    }
}