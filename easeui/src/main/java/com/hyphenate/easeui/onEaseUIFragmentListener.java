package com.hyphenate.easeui;

import com.hyphenate.easeui.domain.VoiceCallBean;

/**
 * 作者：王凤旭
 * 时间：2017/8/4
 * 描述：
 */

public interface onEaseUIFragmentListener {

    void voiceCallListener(VoiceCallBean bean);

    void clickUserInfo(String userName);
}