package com.shanlinjinrong.oa.ui.activity.message;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.ui.activity.message.adapter.CommonPersonAddAdapter;
import com.shanlinjinrong.oa.ui.activity.message.bean.ChatMessageDetailsBean;
import com.shanlinjinrong.oa.ui.activity.message.chatgroup.ModificationGroupNameActivity;
import com.shanlinjinrong.views.common.CommonTopView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

//群组聊天详情界面
public class EaseChatDetailsActivity extends AppCompatActivity {

    @BindView(R.id.top_view)
    CommonTopView topView;
    @BindView(R.id.btn_chat_delete)
    Button btnChatDelete;
    @BindView(R.id.img_portrait)
    ImageView imgPortrait;
    @BindView(R.id.img_group_person)
    ImageView imgGroupPerson;
    @BindView(R.id.rv_person_show)
    RecyclerView rvPersonShow;
    @BindView(R.id.rl_group_name)
    RelativeLayout rlGroupName;
    @BindView(R.id.tv_modification_name)
    TextView tvModificationName;
    @BindView(R.id.rl_group_person)
    RelativeLayout rlGroupPerson;
    @BindView(R.id.tv_clear_message_record)
    TextView tvClearMessageRecord;
    @BindView(R.id.btn_look_message_record)
    TextView btnLookMessageRecord;
    @BindView(R.id.rl_group_portrait)
    RelativeLayout rlGroupPortrait;
    @BindView(R.id.img_modification_portrait)
    ImageView imgModificationPortrait;
    @BindView(R.id.img_modification_group_name)
    ImageView imgModificationGroupName;


    private CommonPersonAddAdapter mAdapter;
    private List<ChatMessageDetailsBean> mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ease_chat_details);
        ButterKnife.bind(this);

        initData();
        initView();
    }

    private void initData() {
        mData = new ArrayList<>();
        ChatMessageDetailsBean bean1 = new ChatMessageDetailsBean();
        bean1.setName(getIntent().getStringExtra("name_self"));
        bean1.setPortraist(getIntent().getStringExtra("portraits_self"));
        ChatMessageDetailsBean bean2 = new ChatMessageDetailsBean();
        bean2.setName(getIntent().getStringExtra("name"));
        bean2.setPortraist(getIntent().getStringExtra("portraits"));
        mData.add(bean1);
        mData.add(bean2);
        mData.add(mData.size(), new ChatMessageDetailsBean());
    }

    private void initView() {
        mAdapter = new CommonPersonAddAdapter(mData);
        rvPersonShow.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvPersonShow.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        rvPersonShow.addOnItemTouchListener(new ItemClick());
    }

    @OnClick({R.id.btn_look_message_record, R.id.tv_clear_message_record, R.id.btn_chat_delete, R.id.rl_group_name, R.id.rl_group_person, R.id.rl_group_portrait})
    public void onViewClicked(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.btn_look_message_record:
                //TODO 查看聊天记录
                intent.setClass(this, LookMessageRecordActivity.class);
                break;
            case R.id.tv_clear_message_record:
                //TODO 清空聊天消息
                break;
            case R.id.btn_chat_delete:
                //TODO 群组删除处理
                finish();
                break;
            case R.id.rl_group_name:
                intent.setClass(this, ModificationGroupNameActivity.class);
                break;
            case R.id.rl_group_person:
                intent.setClass(this, SelectedChatAdminActivity.class);
                break;
            case R.id.rl_group_portrait:
                break;
        }
        startActivity(intent);
    }

    class ItemClick extends OnItemClickListener {
        @Override
        public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
            if (i == mData.size() - 1) {
                Intent intent = new Intent(EaseChatDetailsActivity.this, SelectedChatAdminActivity.class);
                startActivity(intent);
            }
        }
    }
}
