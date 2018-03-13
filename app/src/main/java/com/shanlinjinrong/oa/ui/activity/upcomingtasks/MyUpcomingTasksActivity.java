package com.shanlinjinrong.oa.ui.activity.upcomingtasks;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.retrofit.model.responsebody.ApporveBodyItemBean;
import com.google.gson.Gson;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.manager.AppConfig;
import com.shanlinjinrong.oa.model.CommonRequestBean;
import com.shanlinjinrong.oa.ui.activity.home.approval.OfficeSuppliesActivity;
import com.shanlinjinrong.oa.ui.activity.upcomingtasks.adpter.FinalRecycleAdapter;
import com.shanlinjinrong.oa.ui.activity.upcomingtasks.bean.AgreeDisagreeResultBean;
import com.shanlinjinrong.oa.ui.activity.upcomingtasks.bean.OfficeSuppliesListBean;
import com.shanlinjinrong.oa.ui.activity.upcomingtasks.bean.UpcomingSearchResultBean;
import com.shanlinjinrong.oa.ui.activity.upcomingtasks.bean.UpcomingTaskItemBean;
import com.shanlinjinrong.oa.ui.activity.upcomingtasks.contract.UpcomingTasksContract;
import com.shanlinjinrong.oa.ui.activity.upcomingtasks.presenter.UpcomingTasksPresenter;
import com.shanlinjinrong.oa.ui.activity.upcomingtasks.web.OfficeSuppliesDetailsActivity;
import com.shanlinjinrong.oa.ui.base.HttpBaseActivity;
import com.shanlinjinrong.oa.utils.DateUtils;
import com.shanlinjinrong.oa.utils.ThreadUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.shanlinjinrong.oa.R.id.bottom;
import static com.shanlinjinrong.oa.R.id.default_activity_button;
import static com.shanlinjinrong.oa.R.id.swipe_content;
import static com.shanlinjinrong.oa.R.id.tv;
import static com.shanlinjinrong.oa.R.id.tv_name;
import static com.shanlinjinrong.oa.R.id.tv_state_checked;

public class MyUpcomingTasksActivity extends HttpBaseActivity<UpcomingTasksPresenter> implements UpcomingTasksContract.View, FinalRecycleAdapter.OnViewAttachListener, View.OnClickListener {

    public static final String PAGE_SIZE  = "20";
    public static final String IS_CHECKED = "Y";
    public static final String NO_CHECK   = "N";
    @BindView(R.id.tv_title)
    TextView           mTvTitle;
    @BindView(R.id.toolbar_image_btn)
    ImageView          mTolbarTextBtn;
    @BindView(R.id.toolbar)
    Toolbar            mToolbar;
    @BindView(R.id.rv_list)
    RecyclerView       mRvList;
    @BindView(R.id.sr_refresh)
    SwipeRefreshLayout mSrRefresh;
    @BindView(R.id.ll_search)
    LinearLayout       mLlSearch;
    @BindView(R.id.tv_approval)
    TextView           mTvApproval;
    @BindView(R.id.rl_check)
    RelativeLayout     mRlCheck;
    @BindView(R.id.et_content)
    EditText           mEtContent;
    @BindView(R.id.tv_error_show)
    TextView           mTvErrorShow;
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
    private String  mTime          = "0";
    private String  mTimeCode      = "0";
    //默认签卡申请
    private String  mBillType      = "6402";
    private String  mApproveState  = "";
    private String  mGloableStatus = "-100";
    private boolean isSearch       = false;
    private AlertDialog mAlertDialog;
    private TextView    mTvStateApproving;
    private TextView    mTvStateTackback;
    private TextView    mTvStateDisagree;
    private boolean isOfficeSupplies = false;

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    public void uidNull(String code) {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myupcoming_tasks);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        initToolbar();
        initList();
        initPullToRefresh();
        initEdittext();
    }

    private void initEdittext() {
        mEtContent.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    showLoadingView();
                    searchItem();
                }
                hideKeyBoard();
                return true;
            }
        });
    }

    private void hideKeyBoard() {
        InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(mEtContent.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void initPullToRefresh() {
        mSrRefresh.setColorSchemeColors(getResources().getColor(R.color.tab_bar_text_light));
        mSrRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initRefreshMode();
                getListData();
            }
        });
    }

    private void getListData() {
        if (isOfficeSupplies) {
            if (TextUtils.equals(mWhichList, "1")) {
                mPresenter.getOfficeSuppliesApproveData(mTimeCode, mGloableStatus, String.valueOf(pageNum), PAGE_SIZE);
            } else if (TextUtils.equals(mWhichList, "2")) {
                mPresenter.getOfficeSuppliesManage("1", mTimeCode, String.valueOf(pageNum), PAGE_SIZE);
            } else if (TextUtils.equals(mWhichList, "3")) {
                mPresenter.getOfficeSuppliesManage("2", mTimeCode, String.valueOf(pageNum), PAGE_SIZE);
            }
        } else {
            if (TextUtils.equals(mWhichList, "1")) {
                mPresenter.getApproveData(mApproveState, mBillType, String.valueOf(pageNum), PAGE_SIZE, mTime);
                return;
            }
            String privateCode = AppConfig.getAppConfig(MyUpcomingTasksActivity.this).getPrivateCode();
            if (TextUtils.equals(mWhichList, "2")) {
                mPresenter.getSelectData(privateCode, NO_CHECK, String.valueOf(pageNum), PAGE_SIZE, mTime, mBillType, isSearch ? mEtContent.getText().toString().trim() : "");
            }
            if (TextUtils.equals(mWhichList, "3")) {
                mPresenter.getSelectData(privateCode, IS_CHECKED, String.valueOf(pageNum), PAGE_SIZE, mTime, mBillType, isSearch ? mEtContent.getText().toString().trim() : "");
            }
        }
    }

    private void initRefreshMode() {
        mFinalRecycleAdapter.currentAction = FinalRecycleAdapter.REFRESH;
        isLoadOver = false;
        pageNum = 1;
    }

    private void initList() {
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRvList.setLayoutManager(mLinearLayoutManager);
        Map<Class, Integer> map = FinalRecycleAdapter.getMap();
        map.put(UpcomingTaskItemBean.DataBean.DataListBean.class, R.layout.layout_item_upcoming_task);
        map.put(UpcomingSearchResultBean.DataBeanX.DataBean.class, R.layout.layout_item_upcoming_task);
        map.put(OfficeSuppliesListBean.DataBean.ListBean.class, R.layout.layout_item_upcoming_task);
        getData();
        mFinalRecycleAdapter = new FinalRecycleAdapter(mDatas, map, this);
        mRvList.setAdapter(mFinalRecycleAdapter);
        mRvList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //RecyclerView没有拖动而且已经到达了最后一个item，执行自动加载
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == mFinalRecycleAdapter.getItemCount() && !mSrRefresh.isRefreshing()) {
                    mFinalRecycleAdapter.currentAction = FinalRecycleAdapter.LOAD;
                    if (isLoadOver) {
                        showToast("没有更多了");
                        return;
                    }
                    pageNum++;
                    getListData();
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
        mSrRefresh.post(() -> {
            mSrRefresh.setRefreshing(true);
            getListData();
        });
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
                    setApproveTextWithState();
                    for (Object data : mDatas) {
                        if (data instanceof UpcomingTaskItemBean.DataBean.DataListBean) {
                            UpcomingTaskItemBean.DataBean.DataListBean bean = (UpcomingTaskItemBean.DataBean.DataListBean) data;
                            bean.setIsChecked(false);
                        }
                        if (data instanceof UpcomingSearchResultBean.DataBeanX.DataBean) {
                            UpcomingSearchResultBean.DataBeanX.DataBean bean = (UpcomingSearchResultBean.DataBeanX.DataBean) data;
                            bean.setIsChecked(false);
                        }
                    }
                    mRvList.setVisibility(View.VISIBLE);
                    mRvList.requestLayout();
                    mFinalRecycleAdapter.notifyDataSetChanged();
                } else {
                    finish();
                }
            }
        });
    }

    private void setApproveTextWithState() {
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
            if (TextUtils.equals(mWhichList, "2")) {
                mTvApproval.setVisibility(View.VISIBLE);
            }
            if (TextUtils.equals(mWhichList, "3")) {
                mTvApproval.setVisibility(View.GONE);
            }
            mTolbarTextBtn.setVisibility(View.VISIBLE);
        }
    }

    @OnClick({R.id.toolbar_image_btn, R.id.iv_search, R.id.tv_approval, R.id.iv_agree, R.id.tv_agree, R.id.iv_disagree, R.id.tv_disagree})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar_image_btn:
                hideKeyBoard();
                showChooseDialog();
                break;
            case R.id.iv_search:
                searchItem();
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
                List<ApporveBodyItemBean> approveBeanList = getApporveBodyItemBeenList(true);
                if (approveBeanList.size() == 0) {
                    showToast("请选择单据");
                    return;
                }
                showLoadingView();


                mPresenter.postAgreeDisagree(approveBeanList, isOfficeSupplies);
                break;
            case R.id.iv_disagree:
            case R.id.tv_disagree:
                List<ApporveBodyItemBean> disApproveBeanList = getApporveBodyItemBeenList(false);
                if (disApproveBeanList.size() == 0) {
                    showToast("请选择单据");
                    return;
                }
                showLoadingView();
                mPresenter.postAgreeDisagree(disApproveBeanList, isOfficeSupplies);
                break;
            default:
                break;
        }
    }

    @NonNull
    private List<ApporveBodyItemBean> getApporveBodyItemBeenList(boolean approve) {

        List<ApporveBodyItemBean> approveBeanList = new ArrayList<>();
        for (int i = 0; i < mDatas.size(); i++) {
            if (mDatas.get(i) instanceof UpcomingSearchResultBean.DataBeanX.DataBean) {
                UpcomingSearchResultBean.DataBeanX.DataBean bean = (UpcomingSearchResultBean.DataBeanX.DataBean) mDatas.get(i);
                if (bean.getIsChecked()) {
                    approveBeanList.add(new ApporveBodyItemBean(bean.getBillNo(), approve, bean.getPkBillType()));
                }
            }
            if (mDatas.get(i) instanceof UpcomingTaskItemBean.DataBean.DataListBean) {
                UpcomingTaskItemBean.DataBean.DataListBean bean = (UpcomingTaskItemBean.DataBean.DataListBean) mDatas.get(i);
                if (bean.getIsChecked()) {
                    approveBeanList.add(new ApporveBodyItemBean(bean.getBillCode(), approve, bean.getBillType()));
                }
            }
            if (mDatas.get(i) instanceof OfficeSuppliesListBean.DataBean.ListBean) {
                OfficeSuppliesListBean.DataBean.ListBean bean = (OfficeSuppliesListBean.DataBean.ListBean) mDatas.get(i);
                if (bean.isChecked()) {
                    if (approve) {

                        approveBeanList.add(new ApporveBodyItemBean("0", "1", bean.getTaskId(), bean.getProcessInstanceId()));
                    } else {
                        approveBeanList.add(new ApporveBodyItemBean("1", "1onApproveFailure", bean.getTaskId(), bean.getProcessInstanceId()));
                    }
                }
            }
        }

        return approveBeanList;
    }

    private void searchItem() {
        String trim = mEtContent.getText().toString().trim();
        if (TextUtils.isEmpty(trim)) {
            showToast("请输入搜索内容");
            isSearch = false;
            return;
        }
        isSearch = true;
        String privateCode = AppConfig.getAppConfig(this).getPrivateCode();
        if (TextUtils.equals(mWhichList, "2")) {
            mPresenter.getSelectData(privateCode, NO_CHECK, String.valueOf(pageNum), PAGE_SIZE, mTime, mBillType, isSearch ? mEtContent.getText().toString().trim() : "");
        }
        if (TextUtils.equals(mWhichList, "3")) {
            mPresenter.getSelectData(privateCode, IS_CHECKED, String.valueOf(pageNum), PAGE_SIZE, mTime, mBillType, isSearch ? mEtContent.getText().toString().trim() : "");
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
            mTvApproval.setVisibility(View.VISIBLE);
            if (TextUtils.equals(mWhichList, "2")) {
                mTvTitle.setText("待办事宜");
            }
            if (TextUtils.equals(mWhichList, "3")) {
                mTvTitle.setText("已办事宜");
                mTvApproval.setVisibility(View.GONE);
            }
            mTolbarTextBtn.setVisibility(View.VISIBLE);
        }
        ThreadUtils.runSub(new Runnable() {
            @Override
            public void run() {
                for (Object data : mDatas) {
                    if (data instanceof UpcomingTaskItemBean.DataBean.DataListBean) {
                        UpcomingTaskItemBean.DataBean.DataListBean bean = (UpcomingTaskItemBean.DataBean.DataListBean) data;
                        bean.setIsChecked(false);
                    } else if (data instanceof UpcomingSearchResultBean.DataBeanX.DataBean) {
                        UpcomingSearchResultBean.DataBeanX.DataBean bean = (UpcomingSearchResultBean.DataBeanX.DataBean) data;
                        bean.setIsChecked(false);
                    } else if (data instanceof OfficeSuppliesListBean.DataBean.ListBean) {
                        OfficeSuppliesListBean.DataBean.ListBean bean = (OfficeSuppliesListBean.DataBean.ListBean) data;
                        bean.setChecked(false);
                    }
                }
                ThreadUtils.runMain(new Runnable() {
                    @Override
                    public void run() {
                        mRvList.setVisibility(View.VISIBLE);
                        mRvList.requestLayout();
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
            mTvOfficeSupplies = (TextView) dialogView.findViewById(R.id.tv_office_supplies);
            mTvTravel = (TextView) dialogView.findViewById(R.id.tv_travel);
            mTvOvertime = (TextView) dialogView.findViewById(R.id.tv_overtime);
            mTvRest = (TextView) dialogView.findViewById(R.id.tv_rest);
            mTvSign = (TextView) dialogView.findViewById(R.id.tv_sign);
            mTvOk = (TextView) dialogView.findViewById(R.id.tv_ok);
            mTvAllState = (TextView) dialogView.findViewById(R.id.tv_all_state);
            mTvStateChecked = (TextView) dialogView.findViewById(tv_state_checked);
            mTvStateUnchecked = (TextView) dialogView.findViewById(R.id.tv_state_unchecked);
            mTvStateApproving = (TextView) dialogView.findViewById(R.id.tv_state_approving);
            mTvStateTackback = (TextView) dialogView.findViewById(R.id.tv_state_tackback);
            mTvStateDisagree = (TextView) dialogView.findViewById(R.id.tv_state_disagree);
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
            mTvStateApproving.setOnClickListener(this);
            mTvStateTackback.setOnClickListener(this);
            mTvStateDisagree.setOnClickListener(this);
            mChooseDialog.setContentView(dialogView);
            Window dialogWindow = mChooseDialog.getWindow();
            dialogWindow.setGravity(Gravity.CENTER);
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            dialogWindow.setAttributes(lp);
        }
        mChooseDialog.show();
    }

    @Override
    public void onBindViewHolder(FinalRecycleAdapter.ViewHolder holder, int position, Object itemData) {
        if (itemData instanceof UpcomingTaskItemBean.DataBean.DataListBean || itemData instanceof UpcomingSearchResultBean.DataBeanX.DataBean || itemData instanceof OfficeSuppliesListBean.DataBean.ListBean) {
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
                holder.getRootView().setOnClickListener(view -> {
                    if (isShowCheck) {
                        bean.isChecked = !bean.isChecked;
                        cbCheck.setChecked(bean.getIsChecked());
                        return;
                    }
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("UPCOMING_INFO", bean);
                    startActivityToInfo(bundle);
                });
            } else if (itemData instanceof UpcomingSearchResultBean.DataBeanX.DataBean) {
                UpcomingSearchResultBean.DataBeanX.DataBean bean = (UpcomingSearchResultBean.DataBeanX.DataBean) itemData;
                String pkBillType = bean.getPkBillType();
                setItemIcon(ivIcon, pkBillType);
                tvName.setText(bean.getUserName());
                tvReason.setText(bean.getBillTypeName());
                tvTime.setText(bean.getSendDate());
                tvState.setText(bean.getIsCheck());
                if (TextUtils.equals(mWhichList, "2")) {
                    tvState.setTextColor(getResources().getColor(R.color.black));
                }
                if (TextUtils.equals(mWhichList, "3")) {
                    tvState.setTextColor(Color.parseColor("#57C887"));
                }
                if (isShowCheck) {
                    cbCheck.setVisibility(View.VISIBLE);
                    cbCheck.setChecked(bean.getIsChecked());
                } else {
                    cbCheck.setVisibility(View.GONE);
                }
                holder.getRootView().setOnClickListener(view -> {
                    if (isShowCheck) {
                        bean.setIsChecked(!bean.getIsChecked());
                        cbCheck.setChecked(bean.getIsChecked());
                        return;
                    }
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("UPCOMING_INFO", bean);
                    startActivityToInfo(bundle);
                });
            } else if (itemData instanceof OfficeSuppliesListBean.DataBean.ListBean) {
                try {
                    OfficeSuppliesListBean.DataBean.ListBean bean = (OfficeSuppliesListBean.DataBean.ListBean) itemData;
                    switch (mWhichList) {
                        case "1":
                            tvName.setText(AppConfig.getAppConfig(this).getPrivateName());
                            tvReason.setText(bean.getTypeName());
                            tvTime.setText(bean.getStartTime());
                            break;
                        case "2":
                            tvName.setText(bean.getStartedBy());
                            tvTime.setText(bean.getStartTime());
                            tvReason.setText(bean.getProcessDefinitionName());
                            break;
                        case "3":
                            tvName.setText(bean.getStartedBy());
                            tvTime.setText(bean.getStartTime());
                            tvReason.setText(bean.getProcessDefinitionName());
                            break;
                        default:
                            break;
                    }
                    switch (bean.getGlobalStatus()) {
                        case "3":
                            tvState.setText("未审批");
                            break;
                        case "2":
                            tvState.setText("审批中");
                            break;
                        case "1":
                            tvState.setText("已通过");
                            break;
                        case "0":
                            tvState.setText("已驳回");
                            break;
                        case "-1":
                            tvState.setText("已收回");
                            break;
                        default:
                            tvState.setText("未审批");
                            break;
                    }
                    if (isShowCheck) {
                        cbCheck.setVisibility(View.VISIBLE);
                        cbCheck.setChecked(bean.isChecked());
                    } else {
                        cbCheck.setVisibility(View.GONE);
                    }

                    holder.getRootView().setOnClickListener(view -> {
                        if (isShowCheck) {
                            bean.isChecked = !bean.isChecked;
                            cbCheck.setChecked(bean.isChecked());
                            return;
                        }
                        Intent intent = new Intent(MyUpcomingTasksActivity.this, OfficeSuppliesDetailsActivity.class);
                        intent.putExtra("which", mWhichList);
                        intent.putExtra("state", ((OfficeSuppliesListBean.DataBean.ListBean) itemData).getGlobalStatus());
                        switch (mWhichList) {
                            case "1":
                                intent.putExtra("id", ((OfficeSuppliesListBean.DataBean.ListBean) itemData).getId());
                                break;
                            case "2":
                                intent.putExtra("id", ((OfficeSuppliesListBean.DataBean.ListBean) itemData).getProcessInstanceId());
                                intent.putExtra("taskId", ((OfficeSuppliesListBean.DataBean.ListBean) itemData).getTaskId());
                            case "3":
                                intent.putExtra("id", ((OfficeSuppliesListBean.DataBean.ListBean) itemData).getProcessInstanceId());
                                intent.putExtra("taskId", ((OfficeSuppliesListBean.DataBean.ListBean) itemData).getTaskId());
                                break;
                            default:
                                break;
                        }
                        startActivityForResult(intent, 101);
                    });
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void startActivityToInfo(Bundle bundle) {
        Intent intent = new Intent(MyUpcomingTasksActivity.this, UpcomingTasksInfoActivity.class);
        intent.putExtra("WhichList", mWhichList);
        intent.putExtras(bundle);
        startActivityForResult(intent, 100);
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
                mTime = "";
                mTimeCode = "0";
                break;
            case R.id.tv_today:
                setTimeTextDefault();
                setTextChecked(mTvToday);
                mTime = "1";
                mTimeCode = "1";
                break;
            case R.id.tv_three:
                setTimeTextDefault();
                setTextChecked(mTvThree);
                mTime = "2";
                mTimeCode = "2";
                break;
            case R.id.tv_week:
                setTimeTextDefault();
                setTextChecked(mTvWeek);
                mTime = "3";
                mTimeCode = "3";
                break;
            case R.id.tv_mouth:
                setTimeTextDefault();
                setTextChecked(mTvMouth);
                mTime = "4";
                mTimeCode = "4";
                break;
//            case R.id.tv_all_type://改为办公用品
//                setTypeTextDefault();
//                setTextChecked(mTvAllType);
//                mBillType = "";
//                break;
            case R.id.tv_office_supplies:
                setTypeTextDefault();
                setTextChecked(mTvOfficeSupplies);
                isOfficeSupplies = true;
                break;
            case R.id.tv_travel:
                setTypeTextDefault();
                setTextChecked(mTvTravel);
                mBillType = "6403";
                isOfficeSupplies = false;
                break;
            case R.id.tv_overtime:
                setTypeTextDefault();
                setTextChecked(mTvOvertime);
                mBillType = "6405";
                isOfficeSupplies = false;
                break;
            case R.id.tv_rest:
                setTypeTextDefault();
                setTextChecked(mTvRest);
                mBillType = "6404";
                isOfficeSupplies = false;
                break;
            case R.id.tv_sign:
                setTypeTextDefault();
                setTextChecked(mTvSign);
                mBillType = "6402";
                isOfficeSupplies = false;
                break;
            case R.id.tv_all_state:
                setStateTextDefault();
                setTextChecked(mTvAllState);
                mApproveState = "";
                mGloableStatus = "-100";
                break;
            case tv_state_checked:
                setStateTextDefault();
                setTextChecked(mTvStateChecked);
                mApproveState = "1";
                mGloableStatus = "1";
                break;
            case R.id.tv_state_unchecked:
                setStateTextDefault();
                setTextChecked(mTvStateUnchecked);
                mApproveState = "3";
                mGloableStatus = "3";
                break;
            case R.id.tv_state_approving:
                setStateTextDefault();
                setTextChecked(mTvStateApproving);
                mApproveState = "2";
                mGloableStatus = "2";
                break;
            case R.id.tv_state_tackback:
                setStateTextDefault();
                setTextChecked(mTvStateTackback);
                mApproveState = "-1";
                mGloableStatus = "-1";
                break;
            case R.id.tv_state_disagree:
                setStateTextDefault();
                setTextChecked(mTvStateDisagree);
                mApproveState = "0";
                mGloableStatus = "0";
                break;
            case R.id.tv_ok:
                initRefreshMode();
                if (TextUtils.equals(mWhichList, "1")) {
                    if (isOfficeSupplies) {
                        mPresenter.getOfficeSuppliesApproveData(mTimeCode, mGloableStatus, String.valueOf(pageNum), PAGE_SIZE);
                    } else {
                        mPresenter.getApproveData(mApproveState, mBillType, String.valueOf(pageNum), PAGE_SIZE, mTime);
                    }
                    mChooseDialog.dismiss();
                    return;
                }
                mEtContent.setText("");
                isSearch = false;
                String privateCode = AppConfig.getAppConfig(MyUpcomingTasksActivity.this).getPrivateCode();
                if (TextUtils.equals(mWhichList, "2")) {
                    if (isOfficeSupplies) {
                        //待办
                        mPresenter.getOfficeSuppliesManage("1", mTimeCode, String.valueOf(pageNum), PAGE_SIZE);
                    } else {
                        mPresenter.getSelectData(privateCode, NO_CHECK, String.valueOf(pageNum), PAGE_SIZE, mTime, mBillType, isSearch ? mEtContent.getText().toString().trim() : "");
                    }
                }
                if (TextUtils.equals(mWhichList, "3")) {
                    if (isOfficeSupplies) {
                        //已办
                        mPresenter.getOfficeSuppliesManage("2", mTimeCode, String.valueOf(pageNum), PAGE_SIZE);
                    } else {
                        mPresenter.getSelectData(privateCode, IS_CHECKED, String.valueOf(pageNum), PAGE_SIZE, mTime, mBillType, isSearch ? mEtContent.getText().toString().trim() : "");
                    }
                }
                mChooseDialog.dismiss();
                break;
            default:
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
        mTvOfficeSupplies.setBackgroundResource(R.drawable.shape_upcoming_dialog_item_bg_normal);
        mTvTravel.setBackgroundResource(R.drawable.shape_upcoming_dialog_item_bg_normal);
        mTvOvertime.setBackgroundResource(R.drawable.shape_upcoming_dialog_item_bg_normal);
        mTvRest.setBackgroundResource(R.drawable.shape_upcoming_dialog_item_bg_normal);
        mTvSign.setBackgroundResource(R.drawable.shape_upcoming_dialog_item_bg_normal);
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
        mTvStateApproving.setBackgroundResource(R.drawable.shape_upcoming_dialog_item_bg_normal);
        mTvStateTackback.setBackgroundResource(R.drawable.shape_upcoming_dialog_item_bg_normal);
        mTvStateDisagree.setBackgroundResource(R.drawable.shape_upcoming_dialog_item_bg_normal);
        mTvAllState.setTextColor(getResources().getColor(R.color.black_333333));
        mTvStateChecked.setTextColor(getResources().getColor(R.color.black_333333));
        mTvStateUnchecked.setTextColor(getResources().getColor(R.color.black_333333));
        mTvStateApproving.setTextColor(getResources().getColor(R.color.black_333333));
        mTvStateTackback.setTextColor(getResources().getColor(R.color.black_333333));
        mTvStateDisagree.setTextColor(getResources().getColor(R.color.black_333333));
    }

    @Override
    public void onGetApproveDataSuccess(UpcomingTaskItemBean bean) {
        mRvList.setVisibility(View.VISIBLE);
        mTvErrorShow.setVisibility(View.GONE);
        mSrRefresh.setRefreshing(false);
        if (mFinalRecycleAdapter.currentAction == FinalRecycleAdapter.REFRESH) {
            if (mDatas.size() > 0) {
                mDatas.clear();
            }
            if (bean.getData() == null) {
                mRvList.setVisibility(View.GONE);
                mTvErrorShow.setVisibility(View.VISIBLE);
                mTvErrorShow.setText("暂无内容");
                return;
            }
            if (bean.getData().getDataList() == null || bean.getData().getDataList().size() == 0) {
                mRvList.setVisibility(View.GONE);
                mTvErrorShow.setVisibility(View.VISIBLE);
                mTvErrorShow.setText("暂无内容");
                return;
            }
            if (mDatas.size() > 0) {
                mDatas.clear();
            }
        }
        if (bean.getData() == null) {
            mRvList.setVisibility(View.VISIBLE);
            mRvList.requestLayout();
            mFinalRecycleAdapter.notifyDataSetChanged();
            return;
        }
        List<UpcomingTaskItemBean.DataBean.DataListBean> dataList = bean.getData().getDataList();
        if (dataList == null) {
            mRvList.setVisibility(View.VISIBLE);
            mRvList.requestLayout();
            mFinalRecycleAdapter.notifyDataSetChanged();
            return;
        }
        if (dataList.size() < Integer.parseInt(PAGE_SIZE)) {
            isLoadOver = true;
        }
        mDatas.addAll(dataList);
        mRvList.requestLayout();
        mRvList.setVisibility(View.VISIBLE);
        mRvList.requestLayout();
        mFinalRecycleAdapter.notifyDataSetChanged();
        if (mFinalRecycleAdapter.currentAction == FinalRecycleAdapter.REFRESH) {
            if (mFinalRecycleAdapter.getItemCount() - 1 >= 0) {
                mRvList.scrollToPosition(0);
            }
        }
    }

    @Override
    public void onGetApproveDataSuccess(OfficeSuppliesListBean.DataBean bean) {
        mRvList.setVisibility(View.VISIBLE);
        mTvErrorShow.setVisibility(View.GONE);
        mSrRefresh.setRefreshing(false);
        if (mFinalRecycleAdapter.currentAction == FinalRecycleAdapter.REFRESH) {
            if (mDatas.size() > 0) {
                mDatas.clear();
            }
            if (bean == null) {
                mRvList.setVisibility(View.GONE);
                mTvErrorShow.setVisibility(View.VISIBLE);
                mTvErrorShow.setText("暂无内容");
                return;
            }
            if (bean.getList() == null || bean.getList().size() == 0) {
                mRvList.setVisibility(View.GONE);
                mTvErrorShow.setVisibility(View.VISIBLE);
                mTvErrorShow.setText("暂无内容");
                return;
            }
            if (mDatas.size() > 0) {
                mDatas.clear();
            }
        }
        if (bean == null) {
            mRvList.setVisibility(View.VISIBLE);
            mRvList.requestLayout();
            mFinalRecycleAdapter.notifyDataSetChanged();
            return;
        }
        List<OfficeSuppliesListBean.DataBean.ListBean> dataList = bean.getList();
        if (dataList == null) {
            mRvList.setVisibility(View.VISIBLE);
            mRvList.requestLayout();
            mFinalRecycleAdapter.notifyDataSetChanged();
            return;
        }
        if (dataList.size() < Integer.parseInt(PAGE_SIZE)) {
            isLoadOver = true;
        }
        mDatas.addAll(dataList);
        mRvList.requestLayout();
        mRvList.setVisibility(View.VISIBLE);
        mRvList.requestLayout();
        mFinalRecycleAdapter.notifyDataSetChanged();
        if (mFinalRecycleAdapter.currentAction == FinalRecycleAdapter.REFRESH) {
            if (mFinalRecycleAdapter.getItemCount() - 1 >= 0) {
                mRvList.scrollToPosition(0);
            }
        }
    }

    @Override
    public void onGetApproveDataSuccess(OfficeSuppliesListBean bean) {
        mRvList.setVisibility(View.VISIBLE);
        mTvErrorShow.setVisibility(View.GONE);
        mSrRefresh.setRefreshing(false);
        if (mFinalRecycleAdapter.currentAction == FinalRecycleAdapter.REFRESH) {
            if (mDatas.size() > 0) {
                mDatas.clear();
            }
            if (bean.getData() == null) {
                mRvList.setVisibility(View.GONE);
                mTvErrorShow.setVisibility(View.VISIBLE);
                mTvErrorShow.setText("暂无内容");
                return;
            }
            if (bean.getData().getList() == null || bean.getData().getList().size() == 0) {
                mRvList.setVisibility(View.GONE);
                mTvErrorShow.setVisibility(View.VISIBLE);
                mTvErrorShow.setText("暂无内容");
                return;
            }
            if (mDatas.size() > 0) {
                mDatas.clear();
            }
        }
        if (bean.getData() == null) {
            mRvList.setVisibility(View.VISIBLE);
            mRvList.requestLayout();
            mFinalRecycleAdapter.notifyDataSetChanged();
            return;
        }
        List<OfficeSuppliesListBean.DataBean.ListBean> dataList = bean.getData().getList();
        if (dataList == null) {
            mRvList.setVisibility(View.VISIBLE);
            mRvList.requestLayout();
            mFinalRecycleAdapter.notifyDataSetChanged();
            return;
        }
        if (dataList.size() < Integer.parseInt(PAGE_SIZE)) {
            isLoadOver = true;
        }
        mDatas.addAll(dataList);
        mRvList.requestLayout();
        mRvList.setVisibility(View.VISIBLE);
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
        hideLoadingView();
        mSrRefresh.setRefreshing(false);
        if ("auth error".equals(strMsg)) {
            catchWarningByCode(strMsg);
            return;
        }
        if (errorNo == -1) {
            mRvList.setVisibility(View.GONE);
            mTvErrorShow.setVisibility(View.VISIBLE);
            mTvErrorShow.setText("网络不通，请检查网络连接！");
            return;
        }
        if (strMsg.contains("HttpException")) {
            mRvList.setVisibility(View.GONE);
            mTvErrorShow.setVisibility(View.VISIBLE);
            mTvErrorShow.setText("服务器异常，请稍后重试！");
            return;
        }
        if (strMsg.contains("SocketTimeoutException")) {
            mRvList.setVisibility(View.GONE);
            mTvErrorShow.setVisibility(View.VISIBLE);
            mTvErrorShow.setText("网络不通，请检查网络连接！");
            return;
        }
        if (strMsg.contains("NullPointerException")) {
            mRvList.setVisibility(View.GONE);
            mTvErrorShow.setVisibility(View.VISIBLE);
            mTvErrorShow.setText("网络不通，请检查网络连接！");
            return;
        }
        if (strMsg.contains("ConnectException")) {
            mRvList.setVisibility(View.GONE);
            mTvErrorShow.setVisibility(View.VISIBLE);
            mTvErrorShow.setText("网络不通，请检查网络连接！");
            return;
        }
        if (strMsg.contains("server error")) {
            mRvList.setVisibility(View.GONE);
            mTvErrorShow.setVisibility(View.VISIBLE);
            mTvErrorShow.setText("服务器异常，请稍后重试！");
            return;
        }
        mRvList.setVisibility(View.VISIBLE);
        mTvErrorShow.setVisibility(View.GONE);
        showToast(strMsg);
    }

    private void setNoItemList(int errorNo) {
        mDatas.clear();
        mRvList.setVisibility(View.VISIBLE);
        mRvList.requestLayout();
        mFinalRecycleAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSearchSuccess(UpcomingSearchResultBean bean) {
        mRvList.setVisibility(View.VISIBLE);
        mTvErrorShow.setVisibility(View.GONE);
        hideLoadingView();
        mSrRefresh.setRefreshing(false);
        if (mFinalRecycleAdapter.currentAction == FinalRecycleAdapter.REFRESH) {
            if (bean.getData() == null) {
                mRvList.setVisibility(View.GONE);
                mTvErrorShow.setVisibility(View.VISIBLE);
                mTvErrorShow.setText("暂无内容");
                return;
            }
            if (bean.getData().getData() == null || bean.getData().getData().size() == 0) {
                mRvList.setVisibility(View.GONE);
                mTvErrorShow.setVisibility(View.VISIBLE);
                mTvErrorShow.setText("暂无内容");
                return;
            }
            if (mDatas.size() > 0) {
                mDatas.clear();
            }
        }
        if (bean.getData() == null) {
            mRvList.setVisibility(View.VISIBLE);
            mRvList.requestLayout();
            mFinalRecycleAdapter.notifyDataSetChanged();
            return;
        }
        List<UpcomingSearchResultBean.DataBeanX.DataBean> dataList = bean.getData().getData();
        if (dataList == null) {
            mRvList.setVisibility(View.VISIBLE);
            mRvList.requestLayout();
            mFinalRecycleAdapter.notifyDataSetChanged();
            return;
        }
        if (dataList.size() < Integer.parseInt(PAGE_SIZE)) {
            isLoadOver = true;
        }
        mDatas.addAll(dataList);
        mRvList.requestLayout();
        if (isShowCheck) {
            mRlCheck.setVisibility(View.VISIBLE);
            mLlSearch.setVisibility(View.GONE);
            mTvTitle.setText("选择单据");
            mTolbarTextBtn.setVisibility(View.GONE);
            mTvApproval.setVisibility(View.GONE);
        } else {
            mLlSearch.setVisibility(View.VISIBLE);
            mRlCheck.setVisibility(View.GONE);
            mTvApproval.setVisibility(View.VISIBLE);
            if (TextUtils.equals(mWhichList, "2")) {
                mTvTitle.setText("待办事宜");
            }
            if (TextUtils.equals(mWhichList, "3")) {
                mTvTitle.setText("已办事宜");
                mTvApproval.setVisibility(View.GONE);
            }
            mTolbarTextBtn.setVisibility(View.VISIBLE);
        }
        ThreadUtils.runSub(() -> {
            for (Object data : mDatas) {
                if (data instanceof UpcomingTaskItemBean.DataBean.DataListBean) {
                    UpcomingTaskItemBean.DataBean.DataListBean bean1 = (UpcomingTaskItemBean.DataBean.DataListBean) data;
                    bean1.setIsChecked(false);
                }
                if (data instanceof UpcomingSearchResultBean.DataBeanX.DataBean) {
                    UpcomingSearchResultBean.DataBeanX.DataBean bean1 = (UpcomingSearchResultBean.DataBeanX.DataBean) data;
                    bean1.setIsChecked(false);
                }
            }
            ThreadUtils.runMain(() -> {
                mRvList.setVisibility(View.VISIBLE);
                mRvList.requestLayout();
                mFinalRecycleAdapter.notifyDataSetChanged();
            });
        });
        mRvList.setVisibility(View.VISIBLE);
        mRvList.requestLayout();
        mFinalRecycleAdapter.notifyDataSetChanged();
        if (mFinalRecycleAdapter.currentAction == FinalRecycleAdapter.REFRESH) {
            if (mFinalRecycleAdapter.getItemCount() - 1 >= 0) {
                mRvList.scrollToPosition(0);
            }
        }
    }

    @Override
    public void onApproveSuccess(AgreeDisagreeResultBean resultBean, List<ApporveBodyItemBean> list) {
        mRvList.setVisibility(View.VISIBLE);
        mTvErrorShow.setVisibility(View.GONE);
        hideLoadingView();
        List<AgreeDisagreeResultBean.DataBean> beanList = resultBean.getData();
        StringBuilder stringBuilder = new StringBuilder();
        boolean show = false;
        for (int i = 0; i < beanList.size(); i++) {
            if (TextUtils.equals(beanList.get(i).getStatus(), "1")) {
                stringBuilder.append(beanList.get(i).getReason() + "\n");
            }
            if (TextUtils.equals(beanList.get(i).getStatus(), "2")) {
                show = true;
            }
        }
        if (show) {
            showDetailDialog(beanList);
            return;
        }
        showToast("操作成功");
        initRefreshMode();
        mSrRefresh.setRefreshing(true);
        mSrRefresh.post(() -> ThreadUtils.runMainDelayed(() -> getListData(), 0));
    }

    @Override
    public void onApproveSuccess(String str) {
        hideLoadingView();
        showToast(str);
        initRefreshMode();
        mSrRefresh.setRefreshing(true);
        mSrRefresh.post(() -> ThreadUtils.runMainDelayed(() -> getListData(), 0));
    }

    @Override
    public void onSearchFailure(int errorNo, String strMsg) {
        hideLoadingView();
        mSrRefresh.setRefreshing(false);
        if ("auth error".equals(strMsg)) {
            catchWarningByCode(strMsg);
            return;
        }
        if (errorNo == -1) {
            mRvList.setVisibility(View.GONE);
            mTvErrorShow.setVisibility(View.VISIBLE);
            mSrRefresh.setRefreshing(false);
            hideLoadingView();
            mTvErrorShow.setText("网络不通，请检查网络连接！");
            return;
        }
        if (errorNo == 8192) {
            mRvList.setVisibility(View.GONE);
            mTvErrorShow.setVisibility(View.VISIBLE);
            mTvErrorShow.setText(strMsg);
            mDatas.clear();
            mRvList.setVisibility(View.VISIBLE);
            mRvList.requestLayout();
            mFinalRecycleAdapter.notifyDataSetChanged();
            return;
        }
        if (strMsg.contains("HttpException")) {
            mRvList.setVisibility(View.GONE);
            mTvErrorShow.setVisibility(View.VISIBLE);
            mTvErrorShow.setText("服务器异常，请稍后重试！");
            return;
        }
        if (strMsg.contains("SocketTimeoutException")) {
            mRvList.setVisibility(View.GONE);
            mTvErrorShow.setVisibility(View.VISIBLE);
            mTvErrorShow.setText("网络不通，请检查网络连接！");
            return;
        }
        if (strMsg.contains("NullPointerException")) {
            mRvList.setVisibility(View.GONE);
            mTvErrorShow.setVisibility(View.VISIBLE);
            mTvErrorShow.setText("网络不通，请检查网络连接！");
            return;
        }
        if (strMsg.contains("ConnectException")) {
            mRvList.setVisibility(View.GONE);
            mTvErrorShow.setVisibility(View.VISIBLE);
            mTvErrorShow.setText("网络不通，请检查网络连接！");
            return;
        }
        if (errorNo == 20000) {
            mDatas.clear();
            mRvList.setVisibility(View.GONE);
            mTvErrorShow.setVisibility(View.VISIBLE);
            mTvErrorShow.setText(strMsg);
            mRvList.requestLayout();
            mFinalRecycleAdapter.notifyDataSetChanged();
            return;
        }
        mRvList.setVisibility(View.VISIBLE);
        mTvErrorShow.setVisibility(View.GONE);
        mSrRefresh.setRefreshing(false);
        showToast(strMsg);
    }

    @Override
    public void onSearchFailure(String message) {
        hideLoadingView();
        showToast(message);
        initRefreshMode();
        mSrRefresh.setRefreshing(true);
        mSrRefresh.post(() -> ThreadUtils.runMainDelayed(() -> getListData(), 0));
    }

    private void showDetailDialog(List<AgreeDisagreeResultBean.DataBean> beanList) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("系统提示，您刚刚批量审批的单据中，");
        for (int i = 0; i < beanList.size(); i++) {
            if (!TextUtils.equals(beanList.get(i).getStatus(), "1")) {
                stringBuilder.append(beanList.get(i).getReason() + ",");
            }
        }
        if (mAlertDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(stringBuilder.toString().trim() + "的单据审批失败，请重新审批。");
            builder.setCancelable(true);
            builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    mAlertDialog.dismiss();
                    initRefreshMode();
                    mSrRefresh.setRefreshing(true);
                    mSrRefresh.post(new Runnable() {
                        @Override
                        public void run() {
                            ThreadUtils.runMainDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    getListData();
                                }
                            }, 0);
                        }
                    });
                }
            });
            mAlertDialog = builder.create();
        }
        mAlertDialog.setMessage(stringBuilder.toString().trim() + "的单据审批失败，请重新审批。");
        mAlertDialog.show();
    }

    @Override
    public void onApproveFailure(int errorNo, String strMsg) {
        hideLoadingView();
        if ("auth error".equals(strMsg)) {
            catchWarningByCode(strMsg);
            return;
        }
        if (errorNo == -1) {
            mRvList.setVisibility(View.GONE);
            mTvErrorShow.setVisibility(View.VISIBLE);
            mTvErrorShow.setText("网络不通，请检查网络连接！");
            return;
        }
        if (strMsg.contains("HttpException")) {
            mRvList.setVisibility(View.GONE);
            mTvErrorShow.setVisibility(View.VISIBLE);
            mTvErrorShow.setText("服务器异常，请稍后重试！");
            return;
        }
        if (strMsg.contains("SocketTimeoutException")) {
            mRvList.setVisibility(View.GONE);
            mTvErrorShow.setVisibility(View.VISIBLE);
            mTvErrorShow.setText("网络不通，请检查网络连接！");
            return;
        }
        if (strMsg.contains("NullPointerException")) {
            mRvList.setVisibility(View.GONE);
            mTvErrorShow.setVisibility(View.VISIBLE);
            mTvErrorShow.setText("网络不通，请检查网络连接！");
            return;
        }
        if (strMsg.contains("ConnectException")) {
            mRvList.setVisibility(View.GONE);
            mTvErrorShow.setVisibility(View.VISIBLE);
            mTvErrorShow.setText("网络不通，请检查网络连接！");
            return;
        }
        if (strMsg.contains("server error")) {
            mRvList.setVisibility(View.GONE);
            mTvErrorShow.setVisibility(View.VISIBLE);
            mTvErrorShow.setText("服务器异常，请稍后重试！");
            return;
        }
        if (errorNo == 20000) {
            mDatas.clear();
            mRvList.setVisibility(View.VISIBLE);
            mRvList.requestLayout();
            mFinalRecycleAdapter.notifyDataSetChanged();
            return;
        }
        mRvList.setVisibility(View.VISIBLE);
        mTvErrorShow.setVisibility(View.GONE);
        showToast(strMsg);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 101) {
            mRvList.setVisibility(View.VISIBLE);
            mTvErrorShow.setVisibility(View.GONE);
            mSrRefresh.setRefreshing(true);
            mSrRefresh.post(new Runnable() {
                @Override
                public void run() {
                    ThreadUtils.runMainDelayed(new Runnable() {
                        @Override
                        public void run() {
                            initRefreshMode();
                            getListData();
                        }
                    }, 0);
                }
            });
        } else if (resultCode == -100) {
            //刷新 办公 用品 数据
            switch (mWhichList) {
                case "1":
                    mPresenter.getOfficeSuppliesApproveData(mTimeCode, mGloableStatus, String.valueOf(pageNum), PAGE_SIZE);
                    break;
                case "2":
                    mPresenter.getOfficeSuppliesManage("1", mTimeCode, String.valueOf(pageNum), PAGE_SIZE);
                    break;
                case "3":
                    mPresenter.getOfficeSuppliesManage("2", mTimeCode, String.valueOf(pageNum), PAGE_SIZE);
                    break;
                default:
                    break;
            }

        }
    }

    @Override
    public void onBackPressed() {
        if (isShowCheck) {
            isShowCheck = !isShowCheck;
            setApproveTextWithState();
            for (Object data : mDatas) {
                if (data instanceof UpcomingTaskItemBean.DataBean.DataListBean) {
                    UpcomingTaskItemBean.DataBean.DataListBean bean = (UpcomingTaskItemBean.DataBean.DataListBean) data;
                    bean.setIsChecked(false);
                }
                if (data instanceof UpcomingSearchResultBean.DataBeanX.DataBean) {
                    UpcomingSearchResultBean.DataBeanX.DataBean bean = (UpcomingSearchResultBean.DataBeanX.DataBean) data;
                    bean.setIsChecked(false);
                }
            }
            mTvErrorShow.setVisibility(View.GONE);
            mRvList.setVisibility(View.VISIBLE);
            mRvList.requestLayout();
            mFinalRecycleAdapter.notifyDataSetChanged();
        } else {
            super.onBackPressed();
        }
    }
}