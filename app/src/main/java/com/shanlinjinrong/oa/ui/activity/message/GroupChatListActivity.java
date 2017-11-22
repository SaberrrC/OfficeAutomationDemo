package com.shanlinjinrong.oa.ui.activity.message;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.ui.activity.message.adapter.GroupChatListAdapter;
import com.shanlinjinrong.oa.ui.activity.message.bean.ChatMessageDetailsBean;
import com.shanlinjinrong.views.common.CommonTopView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GroupChatListActivity extends AppCompatActivity {

    @BindView(R.id.top_view)
    CommonTopView mTopView;
    @BindView(R.id.ed_search_group)
    EditText edSearchGroup;
    @BindView(R.id.rv_group_show)
    RecyclerView mRvGroupShow;

    private GroupChatListAdapter mAdapter;
    private List<ChatMessageDetailsBean> mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat_list);
        ButterKnife.bind(this);

        initData();
        initView();
    }

    private void initData() {
        mData = new ArrayList<>();
        mData.add(new ChatMessageDetailsBean());
        mData.add(new ChatMessageDetailsBean());
        mData.add(new ChatMessageDetailsBean());
        mData.add(new ChatMessageDetailsBean());
    }

    private void initView() {
        mTopView.getRightView().setOnClickListener(view -> {
            //TODO 创建群主 选择联系人界面
            //Intent intent = new Intent(this, );
            //startActivity(intent);

        });
        mAdapter = new GroupChatListAdapter(mData);
        mRvGroupShow.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRvGroupShow.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }
}
