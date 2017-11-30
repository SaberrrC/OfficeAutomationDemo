package com.shanlinjinrong.oa.ui.activity.message;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCursorResult;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.exceptions.HyphenateException;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.manager.AppConfig;
import com.shanlinjinrong.oa.manager.AppManager;
import com.shanlinjinrong.oa.ui.activity.message.adapter.CommonPersonAddAdapter;
import com.shanlinjinrong.oa.ui.activity.message.bean.ChatMessageDetailsBean;
import com.shanlinjinrong.oa.ui.activity.message.chatgroup.ModificationGroupNameActivity;
import com.shanlinjinrong.views.common.CommonTopView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

//群组聊天详情界面
public class EaseChatDetailsActivity extends AppCompatActivity {

    @BindView(R.id.top_view)
    CommonTopView topView;
    @BindView(R.id.btn_chat_delete)
    TextView btnChatDelete;
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


    private String mGroupId;
    private boolean mIsGroup;
    private int RESULTSUCCESS = -2, REQUSET_CODE = 101;
    private CommonPersonAddAdapter mAdapter;
    private List<ChatMessageDetailsBean> mData;
    private String mGroupOwner;
    private EMGroup groupFromServer;
    private EMGroup mGroupServer1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ease_chat_details);
        ButterKnife.bind(this);
        initData();
        initView();
    }

    private void initData() {
        initGroup();
        mData = new ArrayList<>();
        if (mIsGroup) {
            InitGroupData();
        }
    }

    EMCursorResult<String> mGroupMemberResult;
    List<String> mMemberList;

    private void InitGroupData() {
        try {
            final int pageSize = 20;
            Observable.create(e -> {
                do {
                    try {//如果群成员较多，需要多次从服务器获取完成
                        mMemberList = new ArrayList<>();
                        mGroupMemberResult = EMClient.getInstance().groupManager().fetchGroupMembers(mGroupId, mGroupMemberResult != null ? mGroupMemberResult.getCursor() : "", pageSize);
                        mMemberList.addAll(mGroupMemberResult.getData());
                    } catch (HyphenateException e1) {
                        e1.printStackTrace();
                    }
                }
                while (!TextUtils.isEmpty(mGroupMemberResult.getCursor()) && mGroupMemberResult.getData().size() == pageSize);
                e.onComplete();
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(o -> {

                    }, Throwable::printStackTrace, () -> {//TODO 群成团账号
                        mMemberList.add(0, "sl_" + AppConfig.getAppConfig(this).getPrivateCode());
                    });
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void initGroup() {
        mIsGroup = getIntent().getBooleanExtra("chatType", false);
        mGroupId = getIntent().getStringExtra("groupId");
        if (mGroupId != null) {
            Observable.create(e -> {
                try {
                    mGroupServer1 = EMClient.getInstance().groupManager().getGroupFromServer(mGroupId);
                    mGroupOwner = mGroupServer1.getOwner();
                } catch (HyphenateException e1) {
                    e1.printStackTrace();
                }
            }).subscribeOn(Schedulers.io());
        }
        if (mIsGroup) {
            rlGroupPerson.setVisibility(View.VISIBLE);
            rlGroupName.setVisibility(View.VISIBLE);
            tvModificationName.setText(mGroupServer1.getGroupName());
            rlGroupPortrait.setVisibility(View.VISIBLE);
            btnChatDelete.setVisibility(View.VISIBLE);

        } else {

        }
    }

    private void initView() {
        mAdapter = new CommonPersonAddAdapter(mData);
        rvPersonShow.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvPersonShow.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        rvPersonShow.addOnItemTouchListener(new ItemClick());
    }

    @OnClick({R.id.btn_look_message_record, R.id.tv_clear_message_record, R.id.rl_group_name, R.id.rl_group_person, R.id.rl_group_portrait})
    public void onViewClicked(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.btn_look_message_record:
                //TODO 查看聊天记录
                Parcelable parcelable = getIntent().getParcelableExtra("INTENT");
                intent.putExtra("INTENT", parcelable);
                intent.setClass(this, LookMessageRecordActivity.class);
                break;
            case R.id.tv_clear_message_record:
                //TODO 清空聊天消息
                break;
            case R.id.rl_group_name:
                intent.setClass(this, ModificationGroupNameActivity.class);
                break;
            case R.id.rl_group_person:
                intent.setClass(this, SelectedChatAdminActivity.class);
                break;
        }
        startActivity(intent);
    }

    @OnClick(R.id.btn_chat_delete)
    public void deleteChat() {
        // 群组删除处理
        Observable.create(e -> {
            if (mGroupOwner.equals("sl_" + AppConfig.getAppConfig(AppManager.mContext).getPrivateCode())) {
                EMClient.getInstance().groupManager().destroyGroup(mGroupId);//解散群组
            } else {
                EMClient.getInstance().groupManager().leaveGroup(mGroupId);//退出群组
            }
            e.onComplete();
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(o -> {
                }, throwable -> {
                    throwable.printStackTrace();
                    if (mGroupOwner.equals("sl_" + AppConfig.getAppConfig(AppManager.mContext).getPrivateCode())) {
                        Toast.makeText(this, "群组解散失败，请从新尝试！", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "群组退出失败，请从新尝试！", Toast.LENGTH_SHORT).show();
                    }
                }, () -> {
                    setResult(getIntent().getIntExtra("position", 0));
                    finish();
                });
    }

    @OnClick(R.id.rl_group_name)
    public void modificationGroupName() {
        Intent intent = new Intent(this, ModificationGroupNameActivity.class);
        startActivityForResult(intent, REQUSET_CODE);
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
