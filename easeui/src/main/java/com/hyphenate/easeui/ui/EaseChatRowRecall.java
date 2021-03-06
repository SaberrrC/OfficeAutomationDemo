package com.hyphenate.easeui.ui;

import android.content.Context;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.R;
import com.hyphenate.easeui.widget.chatrow.EaseChatRow;

/**
 * 作者:王凤旭
 * 日期:2017/8/14
 * 项目:oa
 * 详述:
 */

public class EaseChatRowRecall extends EaseChatRow {


    private TextView contentView;

    public EaseChatRowRecall(Context context, EMMessage message, int position, BaseAdapter adapter) {
        super(context, message, position, adapter);
    }

    @Override
    protected void onInflateView() {
        inflater.inflate(R.layout.em_row_recall_message, this);
    }

    @Override
    protected void onFindViewById() {
        contentView = (TextView) findViewById(R.id.text_content);
    }

    @Override
    protected void onUpdateView() {

    }

    @Override
    protected void onSetUpView() {
        // 设置显示内容
        String messageStr = null;
        if (message.direct() == EMMessage.Direct.SEND) {
            messageStr = String.format(context.getString(R.string.msg_recall_by_self));
        } else {
            messageStr = String.format(context.getString(R.string.msg_recall_by_user), message.getFrom());
        }
        contentView.setText(messageStr);
    }

    @Override
    protected void onBubbleClick() {

    }
}
