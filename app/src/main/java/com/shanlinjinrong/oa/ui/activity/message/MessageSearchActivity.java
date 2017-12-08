package com.shanlinjinrong.oa.ui.activity.message;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.hyphenate.easeui.EaseConstant;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.ui.activity.message.contract.MessageSearchContract;
import com.shanlinjinrong.oa.ui.activity.message.presenter.MessageSearchPresenter;
import com.shanlinjinrong.oa.ui.base.HttpBaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MessageSearchActivity extends HttpBaseActivity<MessageSearchPresenter> implements MessageSearchContract.View {
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.lv_list)
    ListView mLvList;
    private Bundle mBundle;
    private int    chatType;
    private String toChatUsername;

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    public void uidNull(int code) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_search);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        Intent intent = getIntent();
        mBundle = intent.getParcelableExtra("EXTRAS");
        chatType = mBundle.getInt("CHAT_TYPE", EaseConstant.CHATTYPE_SINGLE);
        toChatUsername = mBundle.getString("toChatUsername");
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}
