package com.hyphenate.easeui;

import com.hyphenate.chat.EMMessage;

/**
 * 作者：王凤旭
 * 时间：2017/8/4
 * 描述：
 */

public interface onEaseUIFragmentListener {

    void voiceCallListener(String toChatUsername,EMMessage mEmMessage);

    void clickUserInfo(String userInfo, EMMessage emMessage);
}
