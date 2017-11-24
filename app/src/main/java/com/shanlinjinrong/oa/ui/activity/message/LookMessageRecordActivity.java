package com.shanlinjinrong.oa.ui.activity.message;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.hyphenate.easeui.widget.EaseChatMessageList;
import com.shanlinjinrong.oa.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

//查看消息记录界面
public class LookMessageRecordActivity extends AppCompatActivity {

    @BindView(R.id.tv_title)
    TextView            mTvTitle;
    @BindView(R.id.message_list)
    EaseChatMessageList messageList;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look_message_record);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        listView = messageList.getListView();
//        messageList.init(toChatUsername, chatType, chatFragmentHelper != null ? chatFragmentHelper.onSetCustomChatRowProvider() : null);
//        setListItemClickListener();
//
//        messageList.getListView().setOnTouchListener(new OnTouchListener() {
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                hideKeyboard();
//                inputMenu.hideExtendMenuContainer();
//                return false;
//            }
//        });
    }

    @OnClick({R.id.iv_back, R.id.iv_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_search:
                break;
        }
    }
}