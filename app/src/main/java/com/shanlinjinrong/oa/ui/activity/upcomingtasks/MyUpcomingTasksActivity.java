package com.shanlinjinrong.oa.ui.activity.upcomingtasks;

import android.app.Dialog;
import android.content.Intent;
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
import android.widget.CheckBox;
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

public class MyUpcomingTasksActivity extends HttpBaseActivity<UpcomingTasksPresenter> implements UpcomingTasksContract.View, FinalRecycleAdapter.OnViewAttachListener, View.OnClickListener {

    @Bind(R.id.tv_title)
    TextView           mTvTitle;
    @Bind(R.id.toolbar_image_btn)
    ImageView          mTolbarTextBtn;
    @Bind(R.id.toolbar)
    Toolbar            mToolbar;
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
    private TextView            mTvAll;
    private TextView            mTvToday;
    private TextView            mTvThree;
    private TextView            mTvWeek;
    private TextView            mTvMouth;
    private TextView            mTvAllType;
    private TextView            mTvOfficeSupplies;
    private TextView            mTvTravel;
    private TextView            mTvOvertime;
    private TextView            mTvRest;
    private TextView            mTvSign;
    private TextView            mTvOk;
    private boolean isShowCheck = false;
    private TextView tvAllState;
    private TextView tvStateChecked;
    private TextView tvStateUnchecked;

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
        setContentView(R.layout.activity_myupcoming_tasks);
        ButterKnife.bind(this);
        //        setTranslucentStatus(this);
        init();
    }

    private void init() {
        initToolbar();
        initList();
        initPullToRefresh();
    }

    private void initPullToRefresh() {
        mSrRefresh.setColorSchemeColors(getResources().getColor(R.color.tab_bar_text_light));
        mSrRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mFinalRecycleAdapter.currentAction = FinalRecycleAdapter.REFRESH;
                if (mDatas.size() > 0) {
                    mDatas.clear();
                }
                for (int i = 0; i < 10; i++) {
                    mDatas.add(new UpcomingTaskItemBean());
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
        mTvTitle.setText("我的申请");
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

    @OnClick({R.id.toolbar_image_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar_image_btn:
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
            View dialogView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_dialog_upcoming_choose, null, false);
            mTvAll = (TextView) dialogView.findViewById(R.id.tv_all);
            mTvToday = (TextView) dialogView.findViewById(R.id.tv_today);
            mTvThree = (TextView) dialogView.findViewById(R.id.tv_three);
            mTvWeek = (TextView) dialogView.findViewById(R.id.tv_week);
            mTvMouth = (TextView) dialogView.findViewById(R.id.tv_mouth);
            mTvAllType = (TextView) dialogView.findViewById(R.id.tv_all_type);
            mTvOfficeSupplies = (TextView) dialogView.findViewById(R.id.tv_office_supplies);
            mTvTravel = (TextView) dialogView.findViewById(R.id.tv_travel);
            mTvOvertime = (TextView) dialogView.findViewById(R.id.tv_overtime);
            mTvRest = (TextView) dialogView.findViewById(R.id.tv_rest);
            mTvSign = (TextView) dialogView.findViewById(R.id.tv_sign);
            mTvOk = (TextView) dialogView.findViewById(R.id.tv_ok);
            tvAllState = (TextView) dialogView.findViewById(R.id.tv_all_state);
            tvStateChecked = (TextView) dialogView.findViewById(R.id.tv_state_checked);
            tvStateUnchecked = (TextView) dialogView.findViewById(R.id.tv_state_unchecked);
            mTvAll.setOnClickListener(this);
            mTvToday.setOnClickListener(this);
            mTvThree.setOnClickListener(this);
            mTvWeek.setOnClickListener(this);
            mTvMouth.setOnClickListener(this);
            mTvAllType.setOnClickListener(this);
            mTvOfficeSupplies.setOnClickListener(this);
            mTvTravel.setOnClickListener(this);
            mTvOvertime.setOnClickListener(this);
            mTvRest.setOnClickListener(this);
            mTvSign.setOnClickListener(this);
            mTvOk.setOnClickListener(this);
            mTvOk.setOnClickListener(this);
            mTvOk.setOnClickListener(this);
            mTvOk.setOnClickListener(this);
            tvAllState.setOnClickListener(this);
            tvStateChecked.setOnClickListener(this);
            tvStateUnchecked.setOnClickListener(this);
            mChooseDialog.setContentView(dialogView);
            Window dialogWindow = mChooseDialog.getWindow();
            dialogWindow.setGravity(Gravity.CENTER);
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            dialogWindow.setAttributes(lp);
        }
        setTimeTextDefault();
        setTypeTextDefault();
        setStateTextDefault();
        mTvAll.setBackgroundResource(R.drawable.shape_upcoming_dialog_item_bg_checked);
        mTvAllType.setBackgroundResource(R.drawable.shape_upcoming_dialog_item_bg_checked);
        mTvAll.setTextColor(getResources().getColor(R.color.white));
        mTvAllType.setTextColor(getResources().getColor(R.color.white));
        mChooseDialog.show();//显示对话框
    }

    @Override
    public void onBindViewHolder(FinalRecycleAdapter.ViewHolder holder, int position, Object itemData) {
        if (itemData instanceof UpcomingTaskItemBean) {
            UpcomingTaskItemBean bean = (UpcomingTaskItemBean) itemData;
            CheckBox cbCheck = (CheckBox) holder.getViewById(R.id.cb_check);
            ImageView ivIcon = (ImageView) holder.getViewById(R.id.iv_icon);
            TextView tvName = (TextView) holder.getViewById(R.id.tv_name);
            TextView tvReason = (TextView) holder.getViewById(R.id.tv_reason);
            TextView tvTime = (TextView) holder.getViewById(R.id.tv_time);
            TextView tvState = (TextView) holder.getViewById(R.id.tv_state);
            if (isShowCheck) {
                cbCheck.setVisibility(View.VISIBLE);
                cbCheck.setChecked(bean.getIsChecked());
            } else {
                cbCheck.setVisibility(View.GONE);
            }
            holder.getRootView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isShowCheck) {
                        bean.setIsChecked(!bean.getIsChecked());
                        cbCheck.setChecked(bean.getIsChecked());
                        return;
                    }
                    Intent intent = new Intent(MyUpcomingTasksActivity.this, UpcomingTasksInfoActivity.class);
                    //                    intent.putExtra();
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_all:
                setTimeTextDefault();
                setTextChecked(mTvAll);
                break;
            case R.id.tv_today:
                setTimeTextDefault();
                setTextChecked(mTvToday);
                break;
            case R.id.tv_three:
                setTimeTextDefault();
                setTextChecked(mTvThree);
                break;
            case R.id.tv_week:
                setTimeTextDefault();
                setTextChecked(mTvWeek);
                break;
            case R.id.tv_mouth:
                setTimeTextDefault();
                setTextChecked(mTvMouth);
                break;
            case R.id.tv_all_type:
                setTypeTextDefault();
                setTextChecked(mTvAllType);
                break;
            case R.id.tv_office_supplies:
                setTypeTextDefault();
                setTextChecked(mTvOfficeSupplies);
                break;
            case R.id.tv_travel:
                setTypeTextDefault();
                setTextChecked(mTvTravel);
                break;
            case R.id.tv_overtime:
                setTypeTextDefault();
                setTextChecked(mTvOvertime);
                break;
            case R.id.tv_rest:
                setTypeTextDefault();
                setTextChecked(mTvRest);
                break;
            case R.id.tv_sign:
                setTypeTextDefault();
                setTextChecked(mTvSign);
                break;
            case R.id.tv_all_state:
                setStateTextDefault();
                setTextChecked(tvAllState);
                break;
            case R.id.tv_state_checked:
                setStateTextDefault();
                setTextChecked(tvStateChecked);
                break;
            case R.id.tv_state_unchecked:
                setStateTextDefault();
                setTextChecked(tvStateUnchecked);
                break;
            case R.id.tv_ok:
                mChooseDialog.dismiss();
                break;
        }
    }

    private void setTextChecked(TextView textView) {
        textView.setBackgroundResource(R.drawable.shape_upcoming_dialog_item_bg_checked);
        textView.setTextColor(getResources().getColor(R.color.white));
    }

    private void setTimeTextDefault() {
        mTvAll.setBackgroundResource(R.drawable.shape_upcoming_dialog_item_bg_normal);
        mTvToday.setBackgroundResource(R.drawable.shape_upcoming_dialog_item_bg_normal);
        mTvThree.setBackgroundResource(R.drawable.shape_upcoming_dialog_item_bg_normal);
        mTvWeek.setBackgroundResource(R.drawable.shape_upcoming_dialog_item_bg_normal);
        mTvMouth.setBackgroundResource(R.drawable.shape_upcoming_dialog_item_bg_normal);
        mTvAll.setTextColor(getResources().getColor(R.color.black_333333));
        mTvToday.setTextColor(getResources().getColor(R.color.black_333333));
        mTvThree.setTextColor(getResources().getColor(R.color.black_333333));
        mTvWeek.setTextColor(getResources().getColor(R.color.black_333333));
        mTvMouth.setTextColor(getResources().getColor(R.color.black_333333));
    }

    private void setTypeTextDefault() {
        mTvAllType.setBackgroundResource(R.drawable.shape_upcoming_dialog_item_bg_normal);
        mTvOfficeSupplies.setBackgroundResource(R.drawable.shape_upcoming_dialog_item_bg_normal);
        mTvTravel.setBackgroundResource(R.drawable.shape_upcoming_dialog_item_bg_normal);
        mTvOvertime.setBackgroundResource(R.drawable.shape_upcoming_dialog_item_bg_normal);
        mTvRest.setBackgroundResource(R.drawable.shape_upcoming_dialog_item_bg_normal);
        mTvSign.setBackgroundResource(R.drawable.shape_upcoming_dialog_item_bg_normal);
        mTvAllType.setTextColor(getResources().getColor(R.color.black_333333));
        mTvOfficeSupplies.setTextColor(getResources().getColor(R.color.black_333333));
        mTvTravel.setTextColor(getResources().getColor(R.color.black_333333));
        mTvOvertime.setTextColor(getResources().getColor(R.color.black_333333));
        mTvRest.setTextColor(getResources().getColor(R.color.black_333333));
        mTvSign.setTextColor(getResources().getColor(R.color.black_333333));
    }

    private void setStateTextDefault() {
        tvAllState.setBackgroundResource(R.drawable.shape_upcoming_dialog_item_bg_normal);
        tvStateChecked.setBackgroundResource(R.drawable.shape_upcoming_dialog_item_bg_normal);
        tvStateUnchecked.setBackgroundResource(R.drawable.shape_upcoming_dialog_item_bg_normal);
        tvAllState.setTextColor(getResources().getColor(R.color.black_333333));
        tvStateChecked.setTextColor(getResources().getColor(R.color.black_333333));
        tvStateUnchecked.setTextColor(getResources().getColor(R.color.black_333333));
    }
}