package com.shanlinjinrong.oa.ui.activity.upcomingtasks;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.manager.AppConfig;
import com.shanlinjinrong.oa.ui.activity.upcomingtasks.adpter.FinalRecycleAdapter;
import com.shanlinjinrong.oa.ui.activity.upcomingtasks.bean.ApproveBean;
import com.shanlinjinrong.oa.ui.activity.upcomingtasks.bean.UpcomingSearchResultBean;
import com.shanlinjinrong.oa.ui.activity.upcomingtasks.bean.UpcomingTaskItemBean;
import com.shanlinjinrong.oa.ui.activity.upcomingtasks.contract.UpcomingTasksContract;
import com.shanlinjinrong.oa.ui.activity.upcomingtasks.presenter.UpcomingTasksPresenter;
import com.shanlinjinrong.oa.ui.base.HttpBaseActivity;
import com.shanlinjinrong.oa.utils.ThreadUtils;

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

    public static final String PAGE_SIZE  = "20";
    public static final String IS_CHECKED = "Y";
    public static final String NO_CHECK   = "N";
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
    @Bind(R.id.ll_search)
    LinearLayout       mLlSearch;
    @Bind(R.id.tv_approval)
    TextView           mTvApproval;
    @Bind(R.id.rl_check)
    RelativeLayout     mRlCheck;
    @Bind(R.id.et_content)
    EditText           mEtContent;
    private List<Object> mDatas = new ArrayList<>();
    private FinalRecycleAdapter mFinalRecycleAdapter;
    private int lastVisibleItem = 0;
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
    private TextView            mTvAllState;
    private TextView            mTvStateChecked;
    private TextView            mTvStateUnchecked;
    private int     pageNum    = 1;
    private boolean isLoadOver = false;
    private String       mWhichList;
    private LinearLayout mLlState;
    private boolean isShowCheck = false;
    private View mStork;
    private String mTime         = "0";
    private String mBillType     = "";
    private String mApproveState = "";

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
                isLoadOver = false;
                pageNum = 1;
                if (TextUtils.equals(mWhichList, "1")) {
                    mPresenter.getApproveData(mApproveState, mBillType, String.valueOf(pageNum), PAGE_SIZE, mTime);
                    return;
                }
                String privateCode = AppConfig.getAppConfig(MyUpcomingTasksActivity.this).getPrivateCode();
                if (TextUtils.equals(mWhichList, "2")) {
                    mPresenter.getSelectData(privateCode, NO_CHECK, String.valueOf(pageNum), PAGE_SIZE, mTime, mBillType, mEtContent.getText().toString().trim());
                }
                if (TextUtils.equals(mWhichList, "3")) {
                    mPresenter.getSelectData(privateCode, IS_CHECKED, String.valueOf(pageNum), PAGE_SIZE, mTime, mBillType, mEtContent.getText().toString().trim());
                }
            }
        });
    }

    private void initList() {
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRvList.setLayoutManager(mLinearLayoutManager);
        Map<Class, Integer> map = FinalRecycleAdapter.getMap();
        map.put(UpcomingTaskItemBean.DataBean.DataListBean.class, R.layout.layout_item_upcoming_task);
        map.put(UpcomingSearchResultBean.DataBeanX.DataBean.class, R.layout.layout_item_upcoming_task);
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
                    if (isLoadOver) {
                        showToast("没有更多了");
                        return;
                    }
                    pageNum++;
                    mPresenter.getApproveData(mApproveState, mBillType, String.valueOf(pageNum), PAGE_SIZE, mTime);
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
        //approveState	单据状态	string	非必传，单据状态 -1=自由，0=审批未通过，1=审批通过，2=审批进行中，3=提交
        //billType	单据类型	string	非必传，单据类型 6402=签卡申请 6405=加班申请 6403=出差申请 6404休假申请
        //pageNum	当前第几页	string	必传，当前第几页
        //pageSize	每页要显示的条数	string	必传，每页要显示的条数
        //time	申请时间	string	非必传，申请时间0-全部，1-本月，2上月，3-近两月，4-近三个月，5-近六个月
        if (TextUtils.equals(mWhichList, "1")) {
            mPresenter.getApproveData(mApproveState, mBillType, String.valueOf(pageNum), PAGE_SIZE, mTime);
            return;
        }
        String privateCode = AppConfig.getAppConfig(this).getPrivateCode();
        if (TextUtils.equals(mWhichList, "2")) {
            mPresenter.getSelectData(privateCode, NO_CHECK, String.valueOf(pageNum), PAGE_SIZE, mTime, mBillType, mEtContent.getText().toString().trim());
        }
        if (TextUtils.equals(mWhichList, "3")) {
            mPresenter.getSelectData(privateCode, IS_CHECKED, String.valueOf(pageNum), PAGE_SIZE, mTime, mBillType, mEtContent.getText().toString().trim());
        }
    }

    private void initToolbar() {
        if (mToolbar == null) {
            return;
        }
        mToolbar.setTitle("");//必须在setSupportActionBar之前调用
        mToolbar.setTitleTextColor(Color.parseColor("#000000"));
        mWhichList = getIntent().getStringExtra("whichList");
        if (TextUtils.equals(mWhichList, "1")) {
            mTvTitle.setText("我的申请");
            mLlSearch.setVisibility(View.GONE);
        }
        if (TextUtils.equals(mWhichList, "2")) {
            mTvTitle.setText("待办事宜");
            mLlSearch.setVisibility(View.VISIBLE);
            mTvApproval.setVisibility(View.VISIBLE);
        }
        if (TextUtils.equals(mWhichList, "3")) {
            mTvTitle.setText("已办事宜");
            mLlSearch.setVisibility(View.VISIBLE);
            mTvApproval.setVisibility(View.GONE);
        }
        Toolbar.LayoutParams lp = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        mTvTitle.setLayoutParams(lp);
        mTolbarTextBtn.setImageResource(R.mipmap.upcoming_filter);
        mTolbarTextBtn.setVisibility(View.VISIBLE);
        mToolbar.setNavigationIcon(R.drawable.toolbar_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isShowCheck) {
                    isShowCheck = !isShowCheck;
                    if (isShowCheck) {
                        mLlSearch.setVisibility(View.GONE);
                        mTvTitle.setText("选择单据");
                        mRlCheck.setVisibility(View.VISIBLE);
                        mTolbarTextBtn.setVisibility(View.GONE);
                        mTvApproval.setVisibility(View.GONE);
                    } else {
                        mLlSearch.setVisibility(View.VISIBLE);
                        mTvTitle.setText("待办事宜");
                        mRlCheck.setVisibility(View.GONE);
                        mTvApproval.setVisibility(View.VISIBLE);
                        mTolbarTextBtn.setVisibility(View.VISIBLE);
                    }
                    ThreadUtils.runSub(new Runnable() {
                        @Override
                        public void run() {
                            for (Object data : mDatas) {
                                if (data instanceof UpcomingTaskItemBean.DataBean.DataListBean) {
                                    UpcomingTaskItemBean.DataBean.DataListBean bean = (UpcomingTaskItemBean.DataBean.DataListBean) data;
                                    bean.setIsChecked(false);
                                }
                            }
                            ThreadUtils.runMain(new Runnable() {
                                @Override
                                public void run() {
                                    mFinalRecycleAdapter.notifyDataSetChanged();
                                }
                            });
                        }
                    });
                } else {
                    finish();
                }
            }
        });
    }

    @OnClick({R.id.toolbar_image_btn, R.id.iv_search, R.id.tv_approval, R.id.iv_agree, R.id.tv_agree, R.id.iv_disagree, R.id.tv_disagree})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar_image_btn:
                showChooseDialog();
                break;
            case R.id.iv_search:
                if (TextUtils.equals(mWhichList, "1")) {
                    mPresenter.getApproveData(mApproveState, mBillType, String.valueOf(pageNum), PAGE_SIZE, mTime);
                    return;
                }
                String privateCode = AppConfig.getAppConfig(this).getPrivateCode();
                if (TextUtils.equals(mWhichList, "2")) {
                    mPresenter.getSelectData(privateCode, NO_CHECK, String.valueOf(pageNum), PAGE_SIZE, mTime, mBillType, mEtContent.getText().toString().trim());
                }
                if (TextUtils.equals(mWhichList, "3")) {
                    mPresenter.getSelectData(privateCode, IS_CHECKED, String.valueOf(pageNum), PAGE_SIZE, mTime, mBillType, mEtContent.getText().toString().trim());
                }
                break;
            case R.id.tv_approval:
                if (mDatas.size() == 0) {
                    showToast("无单据");
                    return;
                }
                setApproval();
                break;
            case R.id.iv_agree:
            case R.id.tv_agree:
                List<ApproveBean> approveBeanList = new ArrayList<>();
                for (int i = 0; i < mDatas.size(); i++) {
                    if (mDatas.get(i) instanceof UpcomingSearchResultBean.DataBeanX.DataBean) {
                        UpcomingSearchResultBean.DataBeanX.DataBean bean = (UpcomingSearchResultBean.DataBeanX.DataBean) mDatas.get(i);
                        if (bean.getIsChecked()) {
                            approveBeanList.add(new ApproveBean(bean.getBillNo(), "true", bean.getPkBillType()));
                        }
                    }
                    if (mDatas.get(i) instanceof UpcomingTaskItemBean.DataBean.DataListBean) {
                        UpcomingTaskItemBean.DataBean.DataListBean bean = (UpcomingTaskItemBean.DataBean.DataListBean) mDatas.get(i);
                        if (bean.getIsChecked()) {
                            approveBeanList.add(new ApproveBean(bean.getBillCode(), "true", bean.getBillType()));
                        }
                    }
                }
//                mPresenter.postAgree();
                break;
            case R.id.iv_disagree:
            case R.id.tv_disagree:
                List<ApproveBean> disApproveBeanList = new ArrayList<>();
                for (int i = 0; i < mDatas.size(); i++) {
                    if (mDatas.get(i) instanceof UpcomingSearchResultBean.DataBeanX.DataBean) {
                        UpcomingSearchResultBean.DataBeanX.DataBean bean = (UpcomingSearchResultBean.DataBeanX.DataBean) mDatas.get(i);
                        if (bean.getIsChecked()) {
                            disApproveBeanList.add(new ApproveBean(bean.getBillNo(), "false", bean.getPkBillType()));
                        }
                    }
                    if (mDatas.get(i) instanceof UpcomingTaskItemBean.DataBean.DataListBean) {
                        UpcomingTaskItemBean.DataBean.DataListBean bean = (UpcomingTaskItemBean.DataBean.DataListBean) mDatas.get(i);
                        if (bean.getIsChecked()) {
                            disApproveBeanList.add(new ApproveBean(bean.getBillCode(), "false", bean.getBillType()));
                        }
                    }
                }
//                mPresenter.postDisagree();
                break;
        }
    }

    private void setApproval() {
        isShowCheck = !isShowCheck;
        if (isShowCheck) {
            mLlSearch.setVisibility(View.GONE);
            mRlCheck.setVisibility(View.VISIBLE);
            mTvTitle.setText("选择单据");
            mTolbarTextBtn.setVisibility(View.GONE);
            mTvApproval.setVisibility(View.GONE);
        } else {
            mLlSearch.setVisibility(View.VISIBLE);
            mRlCheck.setVisibility(View.GONE);
            mTvTitle.setText("待办事宜");
            mTvApproval.setVisibility(View.VISIBLE);
            mTolbarTextBtn.setVisibility(View.VISIBLE);
        }
        ThreadUtils.runSub(new Runnable() {
            @Override
            public void run() {
                for (Object data : mDatas) {
                    if (data instanceof UpcomingTaskItemBean.DataBean.DataListBean) {
                        UpcomingTaskItemBean.DataBean.DataListBean bean = (UpcomingTaskItemBean.DataBean.DataListBean) data;
                        bean.setIsChecked(false);
                    }
                }
                ThreadUtils.runMain(new Runnable() {
                    @Override
                    public void run() {
                        mFinalRecycleAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
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
            mTvAllState = (TextView) dialogView.findViewById(R.id.tv_all_state);
            mTvStateChecked = (TextView) dialogView.findViewById(R.id.tv_state_checked);
            mTvStateUnchecked = (TextView) dialogView.findViewById(R.id.tv_state_unchecked);
            mLlState = (LinearLayout) dialogView.findViewById(R.id.ll_state);
            mStork = dialogView.findViewById(R.id.stork);
            if (TextUtils.equals(mWhichList, "1")) {
                mLlState.setVisibility(View.VISIBLE);
                mStork.setVisibility(View.VISIBLE);
            } else {
                mLlState.setVisibility(View.GONE);
                mStork.setVisibility(View.GONE);
            }
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
            mTvAllState.setOnClickListener(this);
            mTvStateChecked.setOnClickListener(this);
            mTvStateUnchecked.setOnClickListener(this);
            mChooseDialog.setContentView(dialogView);
            Window dialogWindow = mChooseDialog.getWindow();
            dialogWindow.setGravity(Gravity.CENTER);
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            dialogWindow.setAttributes(lp);
        }
        //        setTimeTextDefault();
        //        setTypeTextDefault();
        //        setStateTextDefault();
        //        mTvAll.setBackgroundResource(R.drawable.shape_upcoming_dialog_item_bg_checked);
        //        mTvAllType.setBackgroundResource(R.drawable.shape_upcoming_dialog_item_bg_checked);
        //        mTvAllState.setBackgroundResource(R.drawable.shape_upcoming_dialog_item_bg_checked);
        //        mTvAll.setTextColor(getResources().getColor(R.color.white));
        //        mTvAllType.setTextColor(getResources().getColor(R.color.white));
        //        mTvAllState.setTextColor(getResources().getColor(R.color.white));
        mChooseDialog.show();
    }

    @Override
    public void onBindViewHolder(FinalRecycleAdapter.ViewHolder holder, int position, Object itemData) {
        if (itemData instanceof UpcomingTaskItemBean.DataBean.DataListBean || itemData instanceof UpcomingSearchResultBean.DataBeanX.DataBean) {
            CheckBox cbCheck = (CheckBox) holder.getViewById(R.id.cb_check);
            ImageView ivIcon = (ImageView) holder.getViewById(R.id.iv_icon);
            TextView tvName = (TextView) holder.getViewById(R.id.tv_name);
            TextView tvReason = (TextView) holder.getViewById(R.id.tv_reason);
            TextView tvTime = (TextView) holder.getViewById(R.id.tv_time);
            TextView tvState = (TextView) holder.getViewById(R.id.tv_state);
            if (itemData instanceof UpcomingTaskItemBean.DataBean.DataListBean) {
                UpcomingTaskItemBean.DataBean.DataListBean bean = (UpcomingTaskItemBean.DataBean.DataListBean) itemData;
                String billType = bean.getBillType();
                setItemIcon(ivIcon, billType);
                tvName.setText(bean.getUserName());
                tvReason.setText(bean.getBillTypeName());
                tvTime.setText(bean.getCreationTime());
                tvState.setText(bean.getApproveStateName());
                String approveState = bean.getApproveState();
                if (TextUtils.equals(approveState, "1")) {
                    tvState.setTextColor(Color.parseColor("#57C887"));
                } else {
                    tvState.setTextColor(getResources().getColor(R.color.black));
                }
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
                        intent.putExtra("WhichList", mWhichList);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("UPCOMING_INFO", bean);
                        intent.putExtras(bundle);
                        startActivityForResult(intent, 100);
                    }
                });
            }
            if (itemData instanceof UpcomingSearchResultBean.DataBeanX.DataBean) {
                UpcomingSearchResultBean.DataBeanX.DataBean bean = (UpcomingSearchResultBean.DataBeanX.DataBean) itemData;
                String pkBillType = bean.getPkBillType();
                setItemIcon(ivIcon, pkBillType);
                tvName.setText(bean.getUserName());
                tvReason.setText(bean.getBillTypeName());
                tvTime.setText(bean.getSendDate());
                tvState.setText(bean.getIsCheck());
                tvState.setTextColor(getResources().getColor(R.color.black));
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
                        intent.putExtra("WhichList", mWhichList);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("UPCOMING_INFO", bean);
                        intent.putExtras(bundle);
                        startActivityForResult(intent, 100);
                    }
                });
            }
        }
    }

    private void setItemIcon(ImageView ivIcon, String billType) {
        if (TextUtils.equals(billType, "6402")) {//签卡申请
            ivIcon.setBackgroundResource(R.mipmap.upcoming_card);
        }
        if (TextUtils.equals(billType, "6405")) {//加班申请
            ivIcon.setBackgroundResource(R.mipmap.upcoming_overtime);
        }
        if (TextUtils.equals(billType, "6403")) {//出差申请
            ivIcon.setBackgroundResource(R.mipmap.upcoming_travel);
        }
        if (TextUtils.equals(billType, "6404")) {//休假申请
            ivIcon.setBackgroundResource(R.mipmap.upcoming_holiday);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_all:
                setTimeTextDefault();
                setTextChecked(mTvAll);
                mTime = "0";
                break;
            case R.id.tv_today:
                setTimeTextDefault();
                setTextChecked(mTvToday);
                mTime = "1";
                break;
            case R.id.tv_three:
                setTimeTextDefault();
                setTextChecked(mTvThree);
                mTime = "2";
                break;
            case R.id.tv_week:
                setTimeTextDefault();
                setTextChecked(mTvWeek);
                mTime = "3";
                break;
            case R.id.tv_mouth:
                setTimeTextDefault();
                setTextChecked(mTvMouth);
                mTime = "4";
                break;
            case R.id.tv_all_type:
                setTypeTextDefault();
                setTextChecked(mTvAllType);
                mBillType = "";
                break;
            case R.id.tv_office_supplies:
                setTypeTextDefault();
                setTextChecked(mTvOfficeSupplies);
                break;
            case R.id.tv_travel:
                setTypeTextDefault();
                setTextChecked(mTvTravel);
                mBillType = "6403";
                break;
            case R.id.tv_overtime:
                setTypeTextDefault();
                setTextChecked(mTvOvertime);
                mBillType = "6405";
                break;
            case R.id.tv_rest:
                setTypeTextDefault();
                setTextChecked(mTvRest);
                mBillType = "6404";
                break;
            case R.id.tv_sign:
                setTypeTextDefault();
                setTextChecked(mTvSign);
                mBillType = "6402";
                break;
            case R.id.tv_all_state:
                setStateTextDefault();
                setTextChecked(mTvAllState);
                mApproveState = "";
                break;
            case R.id.tv_state_checked:
                setStateTextDefault();
                setTextChecked(mTvStateChecked);
                mApproveState = "1";
                break;
            case R.id.tv_state_unchecked:
                setStateTextDefault();
                setTextChecked(mTvStateUnchecked);
                mApproveState = "2";
                break;
            case R.id.tv_ok:
                mFinalRecycleAdapter.currentAction = FinalRecycleAdapter.REFRESH;
                isLoadOver = false;
                pageNum = 1;
                mPresenter.getApproveData(mApproveState, mBillType, String.valueOf(pageNum), PAGE_SIZE, mTime);
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
        mTvAllState.setBackgroundResource(R.drawable.shape_upcoming_dialog_item_bg_normal);
        mTvStateChecked.setBackgroundResource(R.drawable.shape_upcoming_dialog_item_bg_normal);
        mTvStateUnchecked.setBackgroundResource(R.drawable.shape_upcoming_dialog_item_bg_normal);
        mTvAllState.setTextColor(getResources().getColor(R.color.black_333333));
        mTvStateChecked.setTextColor(getResources().getColor(R.color.black_333333));
        mTvStateUnchecked.setTextColor(getResources().getColor(R.color.black_333333));
    }

    @Override
    public void onGetApproveDataSuccess(UpcomingTaskItemBean bean) {
        mSrRefresh.setRefreshing(false);
        if (mFinalRecycleAdapter.currentAction == FinalRecycleAdapter.REFRESH) {
            if (mDatas.size() > 0) {
                mDatas.clear();
            }
        }
        if (bean.getData() == null) {
            return;
        }
        List<UpcomingTaskItemBean.DataBean.DataListBean> dataList = bean.getData().getDataList();
        if (dataList == null) {
            return;
        }
        if (dataList.size() < Integer.parseInt(PAGE_SIZE)) {
            isLoadOver = true;
        }
        mDatas.addAll(dataList);
        mRvList.requestLayout();
        mFinalRecycleAdapter.notifyDataSetChanged();
        if (mFinalRecycleAdapter.currentAction == FinalRecycleAdapter.REFRESH) {
            if (mFinalRecycleAdapter.getItemCount() - 1 >= 0) {
                mRvList.scrollToPosition(0);
            }
        }
    }

    @Override
    public void onGetApproveDataFailure(int errorNo, String strMsg) {
        showToast(strMsg);
        mSrRefresh.setRefreshing(false);
    }

    @Override
    public void onSearchSuccess(UpcomingSearchResultBean bean) {
        mSrRefresh.setRefreshing(false);
        if (mFinalRecycleAdapter.currentAction == FinalRecycleAdapter.REFRESH) {
            if (mDatas.size() > 0) {
                mDatas.clear();
            }
        }
        if (bean.getData() == null) {
            return;
        }
        List<UpcomingSearchResultBean.DataBeanX.DataBean> dataList = bean.getData().getData();
        if (dataList == null) {
            return;
        }
        if (dataList.size() < Integer.parseInt(PAGE_SIZE)) {
            isLoadOver = true;
        }
        mDatas.addAll(dataList);
        mRvList.requestLayout();
        mFinalRecycleAdapter.notifyDataSetChanged();
        if (mFinalRecycleAdapter.currentAction == FinalRecycleAdapter.REFRESH) {
            if (mFinalRecycleAdapter.getItemCount() - 1 >= 0) {
                mRvList.scrollToPosition(0);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 101) {
            mSrRefresh.post(new Runnable() {
                @Override
                public void run() {
                    mFinalRecycleAdapter.currentAction = FinalRecycleAdapter.REFRESH;
                    isLoadOver = false;
                    pageNum = 1;
                    if (TextUtils.equals(mWhichList, "1")) {
                        mPresenter.getApproveData(mApproveState, mBillType, String.valueOf(pageNum), PAGE_SIZE, mTime);
                        return;
                    }
                    String privateCode = AppConfig.getAppConfig(MyUpcomingTasksActivity.this).getPrivateCode();
                    if (TextUtils.equals(mWhichList, "2")) {
                        mPresenter.getSelectData(privateCode, NO_CHECK, String.valueOf(pageNum), PAGE_SIZE, mTime, mBillType, mEtContent.getText().toString().trim());
                    }
                    if (TextUtils.equals(mWhichList, "3")) {
                        mPresenter.getSelectData(privateCode, IS_CHECKED, String.valueOf(pageNum), PAGE_SIZE, mTime, mBillType, mEtContent.getText().toString().trim());
                    }
                    mSrRefresh.setRefreshing(true);
                }
            });
        }
    }
}