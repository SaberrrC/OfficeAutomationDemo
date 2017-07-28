package com.hyphenate.easeui.ui;

import com.hyphenate.EMGroupChangeListener;

/**
 * group change listener
 *
 */
public abstract class EaseGroupRemoveListener implements EMGroupChangeListener{

    @Override
    public void onInvitationReceived(String groupId, String groupName, String inviter, String reason) {

    }

    @Override
    public void onApplicationReceived(String groupId, String groupName, String applyer, String reason) {

    }

    @Override
    public void onApplicationAccept(String groupId, String groupName, String accepter) {

    }

    @Override
    public void onApplicationDeclined(String groupId, String groupName, String decliner, String reason) {

    }

    @Override
    public void onInvitationAccepted(String groupId, String inviter, String reason) {

    }

    @Override
    public void onInvitationDeclined(String groupId, String invitee, String reason) {

    }
    
    @Override
    public void onAutoAcceptInvitationFromGroup(String groupId, String inviter, String inviteMessage) {
    }
}
