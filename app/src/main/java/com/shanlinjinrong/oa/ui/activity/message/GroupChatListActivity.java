package com.shanlinjinrong.oa.ui.activity.message;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.exceptions.HyphenateException;
import com.iflytek.cloud.thirdparty.V;
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

//聊天群组展示列表
public class GroupChatListActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.top_view)
    CommonTopView mTopView;
    @BindView(R.id.rv_group_show)
    RecyclerView mRvGroupShow;
    @BindView(R.id.sr_refresh)
    SwipeRefreshLayout mSrRefresh;
    @BindView(R.id.tv_error_layout)
    TextView tvErrorLayout;

    private View mFooterView;
    private GroupChatListAdapter mAdapter;
    private List<EMGroup> mGroupList = new ArrayList<>();
    @SuppressWarnings("SpellCheckingInspection")
    private final int REQUESTCODE = 101, DELETESUCCESS = -2, RESULTELECTEDCODE = -3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat_list);
        ButterKnife.bind(this);
        initData();
        initView();
    }

    private void initData() {
        refreshData();
    }

    private void initView() {
        mTopView.getRightView().setOnClickListener(view -> {
            Intent intent = new Intent(this, SelectedGroupContactActivity.class);
            startActivityForResult(intent, REQUESTCODE);
        });

        mSrRefresh.setOnRefreshListener(this);
        mSrRefresh.setColorSchemeColors(Color.parseColor("#0EA7ED"), Color.parseColor("#0EA7ED"), Color.parseColor("#0EA7ED"));
        mSrRefresh.post(() -> mSrRefresh.setRefreshing(true));
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

    //刷新群组列表
    public void refreshData() {
        //加载群组列表
        Observable.create(subscriber -> {
            try {
                mGroupList = EMClient.getInstance().groupManager().getJoinedGroupsFromServer();
                subscriber.onComplete();
            } catch (HyphenateException e) {
                e.printStackTrace();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                        }, throwable -> {
                            mSrRefresh.setRefreshing(false);
                            throwable.printStackTrace();
                        },
                        () -> {
                            mAdapter.setNewData(mGroupList);
                            mAdapter.notifyDataSetChanged();
                            mSrRefresh.setRefreshing(false);
                            initFooterView();
                            updateLayout();
                        });
    }

    private void updateLayout() {
        try {
            if (mGroupList != null && mGroupList.size() == 0) {
                tvErrorLayout.setVisibility(View.VISIBLE);
            } else {
                tvErrorLayout.setVisibility(View.GONE);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
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
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onRefresh() {
        initData();
        updateLayout();
    }

    public class ItemClick extends OnItemClickListener {
        @Override
        public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
            Intent intent = new Intent(GroupChatListActivity.this, EaseChatMessageActivity.class);
            intent.putExtra("groupTitle", mGroupList.get(i).getGroupName());
            intent.putExtra("chatType", EaseConstant.CHATTYPE_GROUP);
            intent.putExtra("u_id", mGroupList.get(i).getGroupId());
            intent.putExtra("userHead", AppConfig.getAppConfig(AppManager.mContext).get(AppConfig.PREF_KEY_PORTRAITS));
            intent.putExtra("userCode", AppConfig.getAppConfig(AppManager.mContext).getPrivateCode());
            startActivityForResult(intent, REQUESTCODE);
        }
    }
}
