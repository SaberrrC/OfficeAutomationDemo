package com.shanlinjinrong.oa.ui.activity.message;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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
import com.shanlinjinrong.oa.ui.activity.message.adapter.GroupChatListAdapter;
import com.shanlinjinrong.views.common.CommonTopView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

//聊天群组展示列表
public class GroupChatListActivity extends AppCompatActivity {

    @BindView(R.id.top_view)
    CommonTopView mTopView;
    @BindView(R.id.rv_group_show)
    RecyclerView mRvGroupShow;

    private GroupChatListAdapter mAdapter;
    private List<EMGroup> mGroupList = new ArrayList<>();
    private CompositeSubscription mSubscription;
    @SuppressWarnings("SpellCheckingInspection")
    private final int REQUESTCODE = 101, DELETESUCCESS = -2, RESULTELECTEDCODE = -3;
    private View mFooterView;

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
        initFooterView();
    }

    @SuppressLint("SetTextI18n")
    private void initFooterView() {
        try {
            if (mFooterView == null) {
                mFooterView = getLayoutInflater().inflate(R.layout.group_list_footer_view, (ViewGroup) mRvGroupShow.getParent(), false);
                mAdapter.addFooterView(mFooterView);
            }
            TextView mTvFooterGroupCount = (TextView) mFooterView.findViewById(R.id.tv_group_count);
            if (mGroupList.size() > 0) {
                mTvFooterGroupCount.setText(mGroupList.size() + "个群聊");
            } else {
                if (mAdapter.getFooterLayoutCount() > 0) {
                    mAdapter.removeAllFooterView();
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    //创建群组
    private void createGroup(String[] name, String[] codes) {
        if (name != null && codes != null) {

            //群组名字
            StringBuilder groupName = new StringBuilder(AppConfig.getAppConfig(AppManager.mContext).getPrivateName());
            //默认不加入群主Id
            //codes[codes.length - 1] = "sl_" + AppConfig.getAppConfig(AppManager.mContext).getPrivateCode();

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
            StringBuilder finalGroupName = groupName;
            io.reactivex.Observable.create(e ->
                    EMClient.getInstance().groupManager().createGroup(finalGroupName.toString(), "", codes, "邀请加入群", option)).subscribeOn(io.reactivex.schedulers.Schedulers.io())
                    .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
                    .subscribe(o -> {
                        Toast.makeText(this, "群组创建成功！", Toast.LENGTH_SHORT).show();
                    }, throwable -> {
                        Toast.makeText(this, "群组创建失败！", Toast.LENGTH_SHORT).show();
                    });

        }
    }

    //刷新群组列表
    public void refreshData() {
        //加载群组列表
        Observable.create(subscriber -> {
            try {
                //TODO 服务器异常 异常Code 待处理
                mGroupList = EMClient.getInstance().groupManager().getJoinedGroupsFromServer();
                subscriber.onComplete();
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
                            initFooterView();
                        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (resultCode) {
            case RESULT_OK: //返回选择的群组人员
                break;
            case DELETESUCCESS: //刷新界面
                refreshData(); //TODO 待优化
                break;
            case RESULTELECTEDCODE: //返回选择的群组人员
                String[] names = data.getStringArrayExtra("name");
                String[] codes = data.getStringArrayExtra("code");
                Observable
                        .create(subscriber -> {
                            createGroup(names, codes);
                            subscriber.onComplete();
                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                        }, Throwable::printStackTrace, this::refreshData);
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
