package com.shanlinjinrong.oa.ui.fragment;

import android.content.Intent;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMConversation.EMConversationType;
import com.hyphenate.easeui.Constant;
import com.hyphenate.easeui.model.EaseAtMessageHelper;
import com.hyphenate.easeui.ui.EaseConversationListFragment;
import com.hyphenate.util.NetUtils;
import com.jakewharton.rxbinding2.view.RxView;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.thirdParty.huanxin.db.InviteMessgeDao;
import com.shanlinjinrong.oa.ui.activity.main.MainActivity;
import com.hyphenate.easeui.event.OnCountRefreshEvent;
import com.shanlinjinrong.oa.utils.LoginUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

public class ConversationListFragment extends EaseConversationListFragment {

    private TextView errorText;
    private LinearLayout ll_error;
    private long lastClickTime = 0;

    @Override
    protected void initView() {
        super.initView();
        View errorView = View.inflate(getActivity(), R.layout.em_chat_neterror_item, null);
        errorItemContainer.addView(errorView);
        errorText = (TextView) errorView.findViewById(R.id.tv_connect_errormsg);
        ll_error = (LinearLayout) errorView.findViewById(R.id.ll_error);
        ll_error.setEnabled(true);
        RxView.clicks(ll_error).throttleFirst(2000, TimeUnit.MILLISECONDS).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                reConnection();
            }
        }, Throwable::printStackTrace);
    }

    private void reConnection() {
        if (!EMClient.getInstance().isConnected()) {
            Log.i("ConversationList", "重连");
            try {
                connectHuanXin();
            } catch (Exception e) {
                e.printStackTrace();
                Log.i("ConversationList", "重连error" + e.toString());
            }
        }
    }

    @Override
    protected void setUpView() {

        // register context menu
        registerForContextMenu(conversationListView);
        conversationListView.setOnItemClickListener((parent, view, position, id) -> {
            long currentTime = Calendar.getInstance().getTimeInMillis();
            if (currentTime - lastClickTime < 1000) {
                lastClickTime = currentTime;
                return;
            }
            lastClickTime = currentTime;
            EMConversation conversation = conversationListView.getItem(position);
            String username = conversation.conversationId();
            if (username.equals(EMClient.getInstance().getCurrentUser()))
                Toast.makeText(getActivity(), R.string.Cant_chat_with_yourself, Toast.LENGTH_SHORT).show();
            else {
                // start chat acitivity
                Intent intent = new Intent(getActivity(), MainActivity.class);
                if (conversation.isGroup()) {
                    if (conversation.getType() == EMConversationType.ChatRoom) {
                        // it's group chat
                        intent.putExtra(Constant.EXTRA_CHAT_TYPE, Constant.CHATTYPE_CHATROOM);
                    } else {
                        intent.putExtra(Constant.EXTRA_CHAT_TYPE, Constant.CHATTYPE_GROUP);
                    }

                }
                // it's single chat
                intent.putExtra(Constant.EXTRA_USER_ID, username);
                startActivity(intent);
            }
        });
        super.setUpView();
    }

    @Override
    protected void onConnectionDisconnected() {
        super.onConnectionDisconnected();
        if (NetUtils.hasNetwork(getActivity())) {
            ll_error.setVisibility(View.VISIBLE);
            errorText.setText(R.string.can_not_connect_chat_server_connection);
        } else {
            ll_error.setVisibility(View.VISIBLE);
            errorText.setText(R.string.the_current_network);
        }
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.em_delete_message, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        boolean deleteMessage = false;
        //        if (item.getItemId() == R.id.delete_message) {
        //            deleteMessage = true;
        //        } else
        if (item.getItemId() == R.id.delete_conversation) {
            deleteMessage = false;
        }
        EMConversation tobeDeleteCons = conversationListView.getItem(((AdapterContextMenuInfo) item.getMenuInfo()).position);
        if (tobeDeleteCons == null) {
            return true;
        }
        if (tobeDeleteCons.getType() == EMConversationType.GroupChat) {
            EaseAtMessageHelper.get().removeAtMeGroup(tobeDeleteCons.conversationId());
        }
        try {
            // delete conversation
            EMClient.getInstance().chatManager().deleteConversation(tobeDeleteCons.conversationId(), true);
            InviteMessgeDao inviteMessgeDao = new InviteMessgeDao(getActivity());
            inviteMessgeDao.deleteMessage(tobeDeleteCons.conversationId());
            EventBus.getDefault().post(new OnCountRefreshEvent());
        } catch (Exception e) {
            e.printStackTrace();
        }
        refresh();
        return true;
    }

    public void connectHuanXin() {
        LoginUtils.loginIm(getContext(), null);
    }

}
