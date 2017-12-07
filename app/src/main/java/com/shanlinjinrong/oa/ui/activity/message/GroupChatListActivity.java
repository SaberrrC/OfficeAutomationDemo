package com.shanlinjinrong.oa.ui.activity.message;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMGroupManager;
import com.hyphenate.chat.EMGroupOptions;
import com.hyphenate.exceptions.HyphenateException;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.manager.AppConfig;
import com.shanlinjinrong.oa.manager.AppManager;
import com.shanlinjinrong.oa.model.selectContacts.Child;
import com.shanlinjinrong.oa.ui.activity.home.schedule.SelectJoinPeopleActivity;
import com.shanlinjinrong.oa.ui.activity.message.adapter.GroupChatListAdapter;
import com.shanlinjinrong.views.common.CommonTopView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

//聊天群组展示列表
public class GroupChatListActivity extends AppCompatActivity {

    @BindView(R.id.top_view)
    CommonTopView mTopView;
//    @BindView(R.id.ed_search_group)
//    EditText edSearchGroup;
    @BindView(R.id.rv_group_show)
    RecyclerView mRvGroupShow;

    private GroupChatListAdapter mAdapter;
    private List<EMGroup> mGroupList;
    private CompositeSubscription mSubscription;
    @SuppressWarnings("SpellCheckingInspection")
    private final int REQUESTCODE = 101, RESULTSUCCESS = -2, RESULTELECTEDCODE = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat_list);
        ButterKnife.bind(this);
        initData();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initData() {
        mSubscription = new CompositeSubscription();
        refreshData();
    }

    private void initView() {
        mTopView.getRightView().setOnClickListener(view -> {
            Intent intent = new Intent(this, SelectedGroupContactActivity.class);
            startActivityForResult(intent, REQUESTCODE);
        });

        mAdapter = new GroupChatListAdapter(mGroupList);
        mRvGroupShow.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRvGroupShow.setAdapter(mAdapter);
        mRvGroupShow.addOnItemTouchListener(new ItemClick());
        mAdapter.notifyDataSetChanged();
    }

    //创建群组
    private void createGroup(String[] name, String[] codes) {
        if (name != null && codes != null) {

            //群组名字
            StringBuilder groupName = new StringBuilder(AppConfig.getAppConfig(AppManager.mContext).getPrivateName());
            codes[codes.length-1] = "sl_" + AppConfig.getAppConfig(AppManager.mContext).getPrivateCode();
            for (String aName : name) {
                groupName.append(",").append(aName);
            }

            //群名字上限10字符
            if (groupName.length() > 10) {
                groupName = new StringBuilder(groupName.substring(0, 10));
                groupName.append("...");
            }

            //群组默认参数
            EMGroupOptions option = new EMGroupOptions();
            option.maxUsers = 200;//上限200人
            //EMGroupStylePrivateOnlyOwnerInvite——私有群，只有群主可以邀请人；
            //EMGroupStylePrivateMemberCanInvite——私有群，群成员也能邀请人进群；
            //EMGroupStylePublicJoinNeedApproval——公开群，加入此群除了群主邀请，只能通过申请加入此群；
            //EMGroupStylePublicOpenJoin ——公开群，任何人都能加入此群。

            option.style = EMGroupManager.EMGroupStyle.EMGroupStylePrivateMemberCanInvite;
            try {
                EMClient.getInstance().groupManager().createGroup(groupName.toString(), "", codes, "邀请加入群", option);
            } catch (HyphenateException e) {
                Toast.makeText(this, "群组创建失败", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

    //刷新群组列表
    public void refreshData() {
        //加载群组列表
        Subscription subscribe = Observable.create(subscriber -> {
            try {
                mGroupList = EMClient.getInstance().groupManager().getJoinedGroupsFromServer();

                subscriber.onCompleted();
            } catch (HyphenateException e) {
                e.printStackTrace();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                        }, Throwable::printStackTrace,
                        () -> {
                            mAdapter.setNewData(mGroupList);
                            mAdapter.notifyDataSetChanged();
                        });
        mSubscription.add(subscribe);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (resultCode) {
            case RESULT_OK: //返回选择的群组人员
                break;
            case RESULTSUCCESS: //刷新界面
                refreshData(); //TODO 待优化 本地做删除

            case RESULTELECTEDCODE: //返回选择的群组人员
                String[] names = data.getStringArrayExtra("name");
                String[] codes = data.getStringArrayExtra("code");
                Subscription subscribe = Observable
                        .create(subscriber -> {
                            createGroup(names, codes);
                            subscriber.onCompleted();
                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                        }, Throwable::printStackTrace, this::refreshData);
                mSubscription.add(subscribe);

                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSubscription.unsubscribe();
    }

    public class ItemClick extends OnItemClickListener {
        @Override
        public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
            Intent intent = new Intent(GroupChatListActivity.this, EaseChatMessageActivity.class);
            intent.putExtra("groupName", mGroupList.get(i).getGroupName());
            intent.putExtra("chatType", 2);
            intent.putExtra("toChatUsername", mGroupList.get(i).getGroupId());
            intent.putExtra("userHead", AppConfig.getAppConfig(AppManager.mContext).get(AppConfig.PREF_KEY_PORTRAITS));
            intent.putExtra("userCode", AppConfig.getAppConfig(AppManager.mContext).getPrivateCode());
            startActivityForResult(intent, REQUESTCODE);
        }
    }
}
