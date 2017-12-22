package com.shanlinjinrong.oa.ui.activity.message;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.exceptions.HyphenateException;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.common.Constants;
import com.shanlinjinrong.oa.manager.AppConfig;
import com.shanlinjinrong.oa.manager.AppManager;
import com.shanlinjinrong.oa.ui.activity.message.adapter.GroupChatListAdapter;
import com.shanlinjinrong.oa.ui.activity.message.bean.GroupEventListener;
import com.shanlinjinrong.oa.ui.base.BaseActivity;
import com.shanlinjinrong.oa.views.ClearEditText;
import com.shanlinjinrong.views.common.CommonTopView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

//聊天群组展示列表
public class GroupChatListActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, View.OnKeyListener {

    @BindView(R.id.top_view)
    CommonTopView      mTopView;
    @BindView(R.id.rv_group_show)
    RecyclerView       mRvGroupShow;
    @BindView(R.id.sr_refresh)
    SwipeRefreshLayout mSrRefresh;
    @BindView(R.id.search_et_input)
    ClearEditText      mSearchEtInput;
    @BindView(R.id.tv_error_layout)
    TextView           tvErrorLayout;

    private View                 mFooterView;
    private GroupChatListAdapter mAdapter;
    private       List<EMGroup> mGroupList       = new ArrayList<>();
    private       List<EMGroup> mSearchGroupList = new ArrayList<>();
    @SuppressWarnings("SpellCheckingInspection")
    private final int           REQUESTCODE      = 101, DELETESUCCESS = -2, RESULTELECTEDCODE = -3;
    private long lastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat_list);
        ButterKnife.bind(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        initData();
        initView();
    }

    private void initData() {
        refreshData();
    }

    private void initView() {
        mTopView.getRightView().setOnClickListener(view -> {
            long currentTime = Calendar.getInstance().getTimeInMillis();
            if (currentTime - lastClickTime < 1000) {
                lastClickTime = currentTime;
                return;
            }
            Intent intent = new Intent(this, SelectedGroupContactActivity.class);
            intent.putExtra(Constants.SELECTEDTYEPE, 1);
            startActivityForResult(intent, REQUESTCODE);
        });
        initSearchView();
        mSearchEtInput.setOnKeyListener(this);
        mSrRefresh.setOnRefreshListener(this);
        mSrRefresh.setColorSchemeColors(Color.parseColor("#0EA7ED"), Color.parseColor("#0EA7ED"), Color.parseColor("#0EA7ED"));
        mSrRefresh.setRefreshing(true);
        mAdapter = new GroupChatListAdapter(mGroupList);
        mRvGroupShow.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRvGroupShow.setAdapter(mAdapter);
        mRvGroupShow.addOnItemTouchListener(new ItemClick());
        mAdapter.notifyDataSetChanged();
        initFooterView();
    }

    private void initSearchView() {
        //EditText 自动搜索,间隔->输入停止500毫秒后自动搜索
        RxTextView.textChanges(mSearchEtInput)
                .debounce(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(charSequence -> {
                    if (charSequence.toString().trim().equals("")) {
                        showLoadingView();
                        mAdapter.setNewData(mGroupList);
                        mRvGroupShow.requestLayout();
                        mAdapter.notifyDataSetChanged();
                        hideLoadingView();
                    } else {
                        showLoadingView();
                        mSearchGroupList.clear();
                        for (int i = 0; i < mGroupList.size(); i++) {
                            if (mGroupList.get(i).getGroupName().contains(charSequence.toString().trim())) {
                                mSearchGroupList.add(mGroupList.get(i));
                            }
                        }
                        mAdapter.setNewData(mSearchGroupList);
                        mRvGroupShow.requestLayout();
                        mAdapter.notifyDataSetChanged();
                        hideLoadingView();
                    }
                });
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
            } catch (HyphenateException e) {
                e.printStackTrace();
            }
            subscriber.onComplete();
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                        }, throwable -> {
                            mSrRefresh.setRefreshing(false);
                            throwable.printStackTrace();
                        },
                        () -> {
                            mAdapter.setNewData(mGroupList);
                            mRvGroupShow.requestLayout();
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
    public void onRefresh() {
        initData();
        updateLayout();
    }

    @Override
    public boolean onKey(View view, int j, KeyEvent keyEvent) {
        if (j == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
            if (mSearchEtInput.getText().toString().trim().equals("")) {
                showLoadingView();
                mAdapter.setNewData(mGroupList);
                mRvGroupShow.requestLayout();
                mAdapter.notifyDataSetChanged();
                hideLoadingView();
            } else {
                showLoadingView();
                mSearchGroupList.clear();
                for (int i = 0; i < mGroupList.size(); i++) {
                    if (mGroupList.get(i).getGroupName().contains(mSearchEtInput.getText().toString().trim())) {
                        mSearchGroupList.add(mGroupList.get(i));
                    }
                }
                mAdapter.setNewData(mSearchGroupList);
                mRvGroupShow.requestLayout();
                mAdapter.notifyDataSetChanged();
                hideLoadingView();
            }
            return true;
        }
        return false;
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshGroup(GroupEventListener event) {
        switch (event.getEvent()) {
            case Constants.MODIFICATIONNAME:
                refreshData();
                break;
            case Constants.GROUPDISSOLVE:
                refreshData();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
