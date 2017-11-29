package com.shanlinjinrong.oa.ui.activity.message;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.hyphenate.easeui.widget.EaseChatMessageList;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.ui.activity.message.contract.LookMessageRecordContract;
import com.shanlinjinrong.oa.ui.activity.message.presenter.LookMessageRecordPresenter;
import com.shanlinjinrong.oa.ui.base.HttpBaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

//查看消息记录界面
public class LookMessageRecordActivity extends HttpBaseActivity<LookMessageRecordPresenter> implements LookMessageRecordContract.View {

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

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    private void initView() {
        listView = messageList.getListView();
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

    @Override
    public void uidNull(int code) {

    }
}