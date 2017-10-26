package com.shanlinjinrong.oa.ui.activity.upcomingtasks;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.ui.activity.upcomingtasks.adpter.FinalRecycleAdapter;
import com.shanlinjinrong.oa.ui.activity.upcomingtasks.bean.UpcomingTaskItemBean;
import com.shanlinjinrong.oa.ui.activity.upcomingtasks.contract.UpcomingTasksContract;
import com.shanlinjinrong.oa.ui.activity.upcomingtasks.presenter.UpcomingTasksPresenter;
import com.shanlinjinrong.oa.ui.base.HttpBaseActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by saberrrc on 2017/10/26.
 */

public class UpcomingTasksActivity extends HttpBaseActivity<UpcomingTasksPresenter> implements UpcomingTasksContract.View, FinalRecycleAdapter.OnViewAttachListener {

    @Bind(R.id.tv_title)
    TextView           mTvTitle;
    @Bind(R.id.toolbar_image_btn)
    ImageView          mTolbarTextBtn;
    @Bind(R.id.toolbar)
    Toolbar            mToolbar;
    @Bind(R.id.et_content)
    EditText           mEtContent;
    @Bind(R.id.tv_approval)
    TextView           mTvApproval;
    @Bind(R.id.rv_list)
    RecyclerView       mRvList;
    @Bind(R.id.sr_refresh)
    SwipeRefreshLayout mSrRefresh;
    private List<Object> mDatas = new ArrayList<>();
    private FinalRecycleAdapter mFinalRecycleAdapter;
    private boolean hasMore         = false;
    private int     lastVisibleItem = 0;
    private LinearLayoutManager mLinearLayoutManager;
    private Dialog              mChooseDialog;

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
        setContentView(R.layout.activity_upcoming_tasks);
        ButterKnife.bind(this);
        setTranslucentStatus(this);
        init();
    }

    private void init() {
        initToolbar();
        initList();
        initPullToRefresh();
    }

    private void initPullToRefresh() {
        //下拉刷新颜色设置
        mSrRefresh.setColorSchemeColors(getResources().getColor(R.color.tab_bar_text_light));
        //下拉刷新监听
        mSrRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mFinalRecycleAdapter.currentAction = FinalRecycleAdapter.REFRESH;
                if (mDatas.size() > 0) {
                    mDatas.clear();
                }
                mFinalRecycleAdapter.notifyDataSetChanged();
                mSrRefresh.setRefreshing(false);
            }
        });
    }

    private void initList() {
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRvList.setLayoutManager(mLinearLayoutManager);
        Map<Class, Integer> map = FinalRecycleAdapter.getMap();
        map.put(UpcomingTaskItemBean.class, R.layout.layout_item_upcoming_task);
        getData();
        mFinalRecycleAdapter = new FinalRecycleAdapter(mDatas, map, this);
        mRvList.setAdapter(mFinalRecycleAdapter);
        mRvList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //RecyclerView没有拖动而且已经到达了最后一个item，执行自动加载
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == mFinalRecycleAdapter.getItemCount() && mDatas.size() > 9) {
                    mFinalRecycleAdapter.currentAction = FinalRecycleAdapter.LOAD;
                    for (int i = 0; i < 3; i++) {
                        mDatas.add(new UpcomingTaskItemBean());
                    }
                    mFinalRecycleAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = mLinearLayoutManager.findLastVisibleItemPosition();
            }
        });
    }

    private void getData() {
        for (int i = 0; i < 10; i++) {
            mDatas.add(new UpcomingTaskItemBean());
        }
    }

    private void initToolbar() {
        if (mToolbar == null) {
            return;
        }
        mToolbar.setTitle("");//必须在setSupportActionBar之前调用
        mToolbar.setTitleTextColor(Color.parseColor("#000000"));
        mTvTitle.setText("待办事宜");
        Toolbar.LayoutParams lp = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        mTvTitle.setLayoutParams(lp);
        mTolbarTextBtn.setImageResource(R.mipmap.upcoming_filter);
        mTolbarTextBtn.setVisibility(View.VISIBLE);
        mToolbar.setNavigationIcon(R.drawable.toolbar_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @OnClick({R.id.toolbar_image_btn, R.id.tv_approval})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar_image_btn:
                break;
            case R.id.tv_approval:
                showChooseDialog();
                break;
        }
    }

    private void showChooseDialog() {
        if (mChooseDialog == null) {
            mChooseDialog = new Dialog(this, R.style.DialogChoose);
            //点击其他地方消失
            mChooseDialog.setCanceledOnTouchOutside(true);
            //填充对话框的布局
            View viewGateOpen = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_dialog_upcoming_choose, null, false);

            mChooseDialog.setContentView(viewGateOpen);
            Window dialogWindow = mChooseDialog.getWindow();
            dialogWindow.setGravity(Gravity.CENTER);
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            dialogWindow.setAttributes(lp);
        }
        mChooseDialog.show();//显示对话框
    }

    @Override
    public void onBindViewHolder(FinalRecycleAdapter.ViewHolder holder, int position, Object itemData) {
        if (itemData instanceof UpcomingTaskItemBean) {

        }
    }
}
