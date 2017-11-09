package com.shanlinjinrong.oa.ui.activity.upcomingtasks;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.common.ApiJava;
import com.shanlinjinrong.oa.ui.activity.home.schedule.initiateapproval.bean.SelectedTypeBean;
import com.shanlinjinrong.oa.ui.activity.upcomingtasks.adpter.FinalBaseAdapter;
import com.shanlinjinrong.oa.ui.activity.upcomingtasks.adpter.FinalRecycleAdapter;
import com.shanlinjinrong.oa.ui.activity.upcomingtasks.bean.AgreeDisagreeResultBean;
import com.shanlinjinrong.oa.ui.activity.upcomingtasks.bean.ApporveBodyItemBean;
import com.shanlinjinrong.oa.ui.activity.upcomingtasks.bean.CardResultBean;
import com.shanlinjinrong.oa.ui.activity.upcomingtasks.bean.DeleteBean;
import com.shanlinjinrong.oa.ui.activity.upcomingtasks.bean.OverTimeResultBean;
import com.shanlinjinrong.oa.ui.activity.upcomingtasks.bean.RestResultBean;
import com.shanlinjinrong.oa.ui.activity.upcomingtasks.bean.TackBackResultBean;
import com.shanlinjinrong.oa.ui.activity.upcomingtasks.bean.TraverResultBean;
import com.shanlinjinrong.oa.ui.activity.upcomingtasks.bean.UpcomingSearchResultBean;
import com.shanlinjinrong.oa.ui.activity.upcomingtasks.bean.UpcomingTaskItemBean;
import com.shanlinjinrong.oa.ui.activity.upcomingtasks.contract.UpcomingTasksInfoContract;
import com.shanlinjinrong.oa.ui.activity.upcomingtasks.presenter.UpcomingTasksInfoPresenter;
import com.shanlinjinrong.oa.ui.base.HttpBaseActivity;
import com.shanlinjinrong.oa.utils.TimeDialogFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.shanlinjinrong.oa.R.id.tv_star;
import static com.shanlinjinrong.oa.R.id.tv_time;

public class UpcomingTasksInfoActivity extends HttpBaseActivity<UpcomingTasksInfoPresenter> implements UpcomingTasksInfoContract.View, FinalRecycleAdapter.OnViewAttachListener, FinalBaseAdapter.AdapterListener {

    @BindView(R.id.tv_title)
    TextView       mTvTitle;
    @BindView(R.id.toolbar)
    Toolbar        mToolbar;
    @BindView(R.id.rv_content)
    RecyclerView   mRecyclerView;
    @BindView(R.id.toolbar_text_btn)
    TextView       mToolbarTextBtn;
    @BindView(R.id.rl_check)
    RelativeLayout mRlCheck;
    @BindView(R.id.rl_tack_back)
    RelativeLayout mRlTackBack;
    @BindView(R.id.tv_tack_back)
    TextView       mTvTackBack;
    private List<Object> mDatas = new ArrayList<>();
    private FinalRecycleAdapter      mFinalRecycleAdapter;
    private LinearLayoutManager      mLinearLayoutManager;
    private int                      mIndex;
    private boolean                  mMove;
    private Dialog                   mChooseDialog;
    private ListView                 mLvList;
    private FinalBaseAdapter<String> mFinalBaseAdapter;
    private List<String> typeData          = new ArrayList<>();
    private int          clickItemPosition = 0;
    private TextView                                    mEtCommonalityBeginTime;
    private TextView                                    mEtCommonalityEndTime;
    private String                                      mWhichList;
    private UpcomingTaskItemBean.DataBean.DataListBean  mBean;
    private UpcomingSearchResultBean.DataBeanX.DataBean mSearchBean;
    private TextView                                    mTvType;
    private CardResultBean                              mCardResultBean;
    private TraverResultBean                            mTraverResultBean;
    private RestResultBean                              mRestResultBean;
    private OverTimeResultBean                          mOverTimeResultBean;

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
        setContentView(R.layout.activity_upcoming_tasks_info);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initView();
        initData();
    }

    private void initView() {
        initToolbar();
        initList();
    }

    private void initList() {
        Map<Class, Integer> map = new HashMap<>();
        map.put(UpcomingTaskItemBean.DataBean.DataListBean.class, R.layout.layout_item_upcominginfo_top);
        map.put(UpcomingSearchResultBean.DataBeanX.DataBean.class, R.layout.layout_item_upcominginfo_top);
        map.put(CardResultBean.DataBean.NchrSignDetailsBean.class, R.layout.upcoming_item);
        map.put(TraverResultBean.DataBean.NchrevectionApplyDetailBean.class, R.layout.upcoming_item);
        map.put(RestResultBean.DataBean.NchrfurloughApplyDetailBean.class, R.layout.upcoming_item);
        map.put(OverTimeResultBean.DataBean.NchroverTimeApplyDetailBean.class, R.layout.upcoming_item);
        map.put(CardResultBean.DataBean.ApplyWorkFlowsBean.class, R.layout.layout_item_upcominginfo_detail_body);
        map.put(TraverResultBean.DataBean.NchrapplyWorkFlowBean.class, R.layout.layout_item_upcominginfo_detail_body);
        map.put(RestResultBean.DataBean.NchrapplyWorkFlowBean.class, R.layout.layout_item_upcominginfo_detail_body);
        map.put(OverTimeResultBean.DataBean.NchrapplyWorkFlowBean.class, R.layout.layout_item_upcominginfo_detail_body);
        mFinalRecycleAdapter = new FinalRecycleAdapter(mDatas, map, this);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mFinalRecycleAdapter);
    }

    private void initData() {
        if (TextUtils.equals(mWhichList, "1")) {
            mPresenter.getInfoData(mBean.getBillType(), mBean.getBillCode());
            return;
        }
        mPresenter.getInfoData(mSearchBean.getPkBillType(), mSearchBean.getBillNo());
    }

    private void initToolbar() {
        if (mToolbar == null) {
            return;
        }
        mToolbar.setTitle("");
        mToolbar.setTitleTextColor(Color.parseColor("#000000"));
        Intent intent = getIntent();
        if (intent != null) {
            mWhichList = intent.getStringExtra("WhichList");
            if (TextUtils.equals(mWhichList, "1")) {
                mRlCheck.setVisibility(View.GONE);
                mBean = (UpcomingTaskItemBean.DataBean.DataListBean) intent.getSerializableExtra("UPCOMING_INFO");
                if (TextUtils.equals(mBean.getApproveState(), "-1")) {
                    mTvTackBack.setText("删除");
                    mRlTackBack.setVisibility(View.VISIBLE);
                    mTvTackBack.setBackgroundResource(R.drawable.shape_upcominginfo_disagree);
                } else {
                    mTvTackBack.setText("收回");
                    mRlTackBack.setVisibility(View.GONE);
                    mTvTackBack.setBackgroundResource(R.drawable.shape_upcoming_dialog_ok);
                }
                mTvTitle.setText(mBean.getUserName() + "的" + mBean.getBillTypeName());
            }
            if (TextUtils.equals(mWhichList, "2")) {
                mRlCheck.setVisibility(View.VISIBLE);
                mRlTackBack.setVisibility(View.GONE);
                mSearchBean = (UpcomingSearchResultBean.DataBeanX.DataBean) intent.getSerializableExtra("UPCOMING_INFO");
                mTvTitle.setText(mSearchBean.getUserName() + "的" + mSearchBean.getBillTypeName());
            }
            if (TextUtils.equals(mWhichList, "3")) {
                mRlCheck.setVisibility(View.GONE);
                mRlTackBack.setVisibility(View.GONE);
                mSearchBean = (UpcomingSearchResultBean.DataBeanX.DataBean) intent.getSerializableExtra("UPCOMING_INFO");
                mTvTitle.setText(mSearchBean.getUserName() + "的" + mSearchBean.getBillTypeName());
            }
        }
        Toolbar.LayoutParams lp = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        mToolbarTextBtn.setVisibility(View.VISIBLE);
        mToolbarTextBtn.setText("评阅情况");
        mTvTitle.setLayoutParams(lp);
        mToolbar.setNavigationIcon(R.drawable.toolbar_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onBindViewHolder(FinalRecycleAdapter.ViewHolder holder, int position, Object itemData) {
        if (itemData instanceof UpcomingTaskItemBean.DataBean.DataListBean || itemData instanceof UpcomingSearchResultBean.DataBeanX.DataBean) {
            TextView tvIdTitle = (TextView) holder.getViewById(R.id.tv_id_title);
            ImageView ivArrow = (ImageView) holder.getViewById(R.id.iv_arrow);
            TextView tvId = (TextView) holder.getViewById(R.id.tv_id);
            TextView tvStar = (TextView) holder.getViewById(tv_star);
            TextView tvTypeTitle = (TextView) holder.getViewById(R.id.tv_type_title);
            LinearLayout llType = (LinearLayout) holder.getViewById(R.id.ll_type);
            mTvType = (TextView) holder.getViewById(R.id.tv_type);
            TextView tvApplyTimeTitle = (TextView) holder.getViewById(R.id.tv_apply_time_title);
            LinearLayout llTypeAll = (LinearLayout) holder.getViewById(R.id.ll_type_all);
            TextView tvApplyTime = (TextView) holder.getViewById(R.id.tv_apply_time);
            if (itemData instanceof UpcomingTaskItemBean.DataBean.DataListBean) {
                UpcomingTaskItemBean.DataBean.DataListBean bean = (UpcomingTaskItemBean.DataBean.DataListBean) itemData;
                tvId.setText(bean.getBillCode());
                tvApplyTime.setText(bean.getCreationTime());
                String billType = mBean.getBillType();
                setTopTextView(tvIdTitle, tvTypeTitle, llTypeAll, billType);
            }
            if (itemData instanceof UpcomingSearchResultBean.DataBeanX.DataBean) {
                UpcomingSearchResultBean.DataBeanX.DataBean bean = (UpcomingSearchResultBean.DataBeanX.DataBean) itemData;
                tvId.setText(bean.getBillNo());
                tvApplyTime.setText(bean.getSendDate());
                String billType = mSearchBean.getPkBillType();
                setTopTextView(tvIdTitle, tvTypeTitle, llTypeAll, billType);
            }
            return;
        }
        if (itemData instanceof CardResultBean.DataBean.NchrSignDetailsBean ||
                itemData instanceof TraverResultBean.DataBean.NchrevectionApplyDetailBean ||
                itemData instanceof RestResultBean.DataBean.NchrfurloughApplyDetailBean ||
                itemData instanceof OverTimeResultBean.DataBean.NchroverTimeApplyDetailBean) {
            RelativeLayout rlTop = (RelativeLayout) holder.getViewById(R.id.rl_top);
            ImageView imgDeleteDetail = (ImageView) holder.getViewById(R.id.img_delete_detail);
            imgDeleteDetail.setVisibility(View.GONE);
            mEtCommonalityBeginTime = (TextView) holder.getViewById(R.id.et_commonality_begin_time);
            mEtCommonalityEndTime = (TextView) holder.getViewById(R.id.et_commonality_end_time);
            TextView tvCommonalityDetail = (TextView) holder.getViewById(R.id.tv_commonality_detail);
            LinearLayout llCommonalityBeginTime = (LinearLayout) holder.getViewById(R.id.ll_commonality_begin_time);
            llCommonalityBeginTime.setVisibility(View.GONE);
            TextView tvCommonalityBeginDot = (TextView) holder.getViewById(R.id.tv_commonality_begin_dot);
            TextView tvCommonalityBeginTime = (TextView) holder.getViewById(R.id.tv_commonality_begin_time);
            LinearLayout llCommonalityEndTime = (LinearLayout) holder.getViewById(R.id.ll_commonality_end_time);
            llCommonalityEndTime.setVisibility(View.GONE);
            TextView tvCommonalityEndDot = (TextView) holder.getViewById(R.id.tv_commonality_end_dot);
            LinearLayout llCommonalityDuration = (LinearLayout) holder.getViewById(R.id.ll_commonality_duration);
            llCommonalityDuration.setVisibility(View.GONE);
            TextView tvCommonalityDuration = (TextView) holder.getViewById(R.id.tv_commonality_duration);
            TextView tvCommonality = (TextView) holder.getViewById(R.id.tv_commonality);
            LinearLayout llCommonalityShow1 = (LinearLayout) holder.getViewById(R.id.ll_commonality_show1);
            llCommonalityShow1.setVisibility(View.GONE);
            TextView tvCommonalityShow1Dot = (TextView) holder.getViewById(R.id.tv_commonality_show1_dot);
            tvCommonalityShow1Dot.setVisibility(View.GONE);
            TextView tvCommonalityShow1 = (TextView) holder.getViewById(R.id.tv_commonality_show1);
            EditText etCommonalityShow1 = (EditText) holder.getViewById(R.id.et_commonality_show1);
            LinearLayout llCommonalityShow2 = (LinearLayout) holder.getViewById(R.id.ll_commonality_show2);
            llCommonalityShow2.setVisibility(View.GONE);
            TextView tvCommonalityShow2 = (TextView) holder.getViewById(R.id.tv_commonality_show2);
            TextView tvCommonalityShow2Dot = (TextView) holder.getViewById(R.id.tv_commonality_show2_dot);
            tvCommonalityShow2Dot.setVisibility(View.GONE);
            LinearLayout llRegistrationCardDetail = (LinearLayout) holder.getViewById(R.id.ll_registration_card_detail);
            llRegistrationCardDetail.setVisibility(View.GONE);
            EditText etCommonalityShow2 = (EditText) holder.getViewById(R.id.et_commonality_show2);
            LinearLayout llCommonalityShow3 = (LinearLayout) holder.getViewById(R.id.ll_commonality_show3);
            llCommonalityShow3.setVisibility(View.GONE);
            TextView tvCommonalityShow3 = (TextView) holder.getViewById(R.id.tv_commonality_show3);
            EditText etCommonalityShow3 = (EditText) holder.getViewById(R.id.et_commonality_show3);
            TextView tvCommonalityShow3Dot = (TextView) holder.getViewById(R.id.tv_commonality_show3_dot);
            mEtCommonalityBeginTime.setEnabled(false);
            mEtCommonalityEndTime.setEnabled(false);
            etCommonalityShow1.setEnabled(false);
            etCommonalityShow2.setEnabled(false);
            etCommonalityShow3.setEnabled(false);
            rlTop.setVisibility(View.VISIBLE);
            mEtCommonalityBeginTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TimeDialogFragment timeDialogFragment = new TimeDialogFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("title", "开始时间");
                    timeDialogFragment.setArguments(bundle);
                    timeDialogFragment.show(getSupportFragmentManager(), "0");
                }
            });
            mEtCommonalityEndTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TimeDialogFragment timeDialogFragment = new TimeDialogFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("title", "结束时间");
                    timeDialogFragment.setArguments(bundle);
                    timeDialogFragment.show(getSupportFragmentManager(), "1");
                }
            });
            if (itemData instanceof CardResultBean.DataBean.NchrSignDetailsBean) {
                CardResultBean.DataBean.NchrSignDetailsBean bean = (CardResultBean.DataBean.NchrSignDetailsBean) itemData;
                tvCommonalityDetail.setText("签卡明细");
                llCommonalityShow1.setVisibility(View.VISIBLE);
                llCommonalityShow2.setVisibility(View.VISIBLE);
                llCommonalityShow3.setVisibility(View.VISIBLE);
                tvCommonalityShow1Dot.setVisibility(View.VISIBLE);
                tvCommonalityShow2Dot.setVisibility(View.VISIBLE);
                tvCommonalityShow3Dot.setVisibility(View.VISIBLE);
                tvCommonalityShow1.setText("签卡时间");
                tvCommonalityShow2.setText("签卡原因");
                tvCommonalityShow3.setText("签卡说明");
                etCommonalityShow1.setText(bean.getSignTime());
                etCommonalityShow2.setText(TextUtils.isEmpty(bean.getSignCause()) ? "" : bean.getSignCause());
                etCommonalityShow3.setText(TextUtils.isEmpty(bean.getSignRemark()) ? "" : bean.getSignRemark());
                return;
            }
            if (itemData instanceof TraverResultBean.DataBean.NchrevectionApplyDetailBean) {
                TraverResultBean.DataBean.NchrevectionApplyDetailBean bean = (TraverResultBean.DataBean.NchrevectionApplyDetailBean) itemData;
                mTvType.setText(mTraverResultBean.getData().getTypeName());
                tvCommonalityDetail.setText("出差明细");
                llCommonalityBeginTime.setVisibility(View.VISIBLE);
                llCommonalityEndTime.setVisibility(View.VISIBLE);
                llCommonalityDuration.setVisibility(View.VISIBLE);
                llCommonalityShow1.setVisibility(View.VISIBLE);
                llCommonalityShow2.setVisibility(View.VISIBLE);
                llCommonalityShow3.setVisibility(View.VISIBLE);
                tvCommonalityShow1Dot.setVisibility(View.VISIBLE);
                tvCommonalityShow2Dot.setVisibility(View.GONE);
                tvCommonalityShow3Dot.setVisibility(View.GONE);
                mEtCommonalityBeginTime.setText(bean.getStartTime());
                mEtCommonalityEndTime.setText(bean.getEndTime());
                tvCommonalityDuration.setText("申请时长");
                tvCommonality.setText(bean.getTimeDifference() + "小时");
                tvCommonalityShow1.setText("出差地点");
                tvCommonalityShow2.setText("出差原因");
                tvCommonalityShow3.setText("工作交接人");
                etCommonalityShow1.setText(TextUtils.isEmpty(bean.getEvectionAddress()) ? "" : bean.getEvectionAddress());
                etCommonalityShow2.setText(TextUtils.isEmpty(bean.getEvectionRemark()) ? "" : bean.getEvectionRemark());
                etCommonalityShow3.setText(bean.getPsnname());
                return;

            }
            if (itemData instanceof RestResultBean.DataBean.NchrfurloughApplyDetailBean) {
                RestResultBean.DataBean.NchrfurloughApplyDetailBean bean = (RestResultBean.DataBean.NchrfurloughApplyDetailBean) itemData;
                //                mTvType.setText(TextUtils.isEmpty(bean.getsen()) ? "" : bean.getSignRemark());
                mTvType.setText(mRestResultBean.getData().getTypeName());
                tvCommonalityDetail.setText("休假明细");
                llCommonalityBeginTime.setVisibility(View.VISIBLE);
                llCommonalityEndTime.setVisibility(View.VISIBLE);
                llCommonalityDuration.setVisibility(View.VISIBLE);
                llCommonalityShow1.setVisibility(View.VISIBLE);
                llCommonalityShow2.setVisibility(View.VISIBLE);
                tvCommonalityShow1Dot.setVisibility(View.GONE);
                tvCommonalityShow2Dot.setVisibility(View.GONE);
                mEtCommonalityBeginTime.setText(bean.getStartTime());
                mEtCommonalityEndTime.setText(bean.getEndTime());
                tvCommonalityDuration.setText("休假时长");
                tvCommonality.setText(bean.getTimeDifference() + "小时");
                tvCommonalityShow1.setText("休假事由");
                tvCommonalityShow2.setText("工作交接人");
                etCommonalityShow1.setText(TextUtils.isEmpty(bean.getFurloughRemark()) ? "" : bean.getFurloughRemark());
                etCommonalityShow2.setText(bean.getPsnname());
                return;
            }
            if (itemData instanceof OverTimeResultBean.DataBean.NchroverTimeApplyDetailBean) {
                OverTimeResultBean.DataBean.NchroverTimeApplyDetailBean bean = (OverTimeResultBean.DataBean.NchroverTimeApplyDetailBean) itemData;
                mTvType.setText(mOverTimeResultBean.getData().getTypeName());
                tvCommonalityDetail.setText("加班明细");
                llCommonalityBeginTime.setVisibility(View.VISIBLE);
                llCommonalityEndTime.setVisibility(View.VISIBLE);
                llCommonalityDuration.setVisibility(View.VISIBLE);
                llCommonalityShow1.setVisibility(View.VISIBLE);
                tvCommonalityShow1Dot.setVisibility(View.VISIBLE);
                mEtCommonalityBeginTime.setText(bean.getOverTimeBeginTime());
                mEtCommonalityEndTime.setText(bean.getOverTimeEndTime());
                tvCommonalityDuration.setText("申请时长");
                tvCommonality.setText(bean.getOverTimeHour() + "小时");
                tvCommonalityShow1.setText("休假事由");
                etCommonalityShow1.setText(TextUtils.isEmpty(bean.getOverTimeRemark()) ? "" : bean.getOverTimeRemark());
            }
            return;
        }
        if (itemData instanceof CardResultBean.DataBean.ApplyWorkFlowsBean ||
                itemData instanceof TraverResultBean.DataBean.NchrapplyWorkFlowBean ||
                itemData instanceof RestResultBean.DataBean.NchrapplyWorkFlowBean ||
                itemData instanceof OverTimeResultBean.DataBean.NchrapplyWorkFlowBean) {
            TextView tvApprover = (TextView) holder.getViewById(R.id.tv_approver);
            TextView tvTime = (TextView) holder.getViewById(tv_time);
            TextView tvState = (TextView) holder.getViewById(R.id.tv_state);
            TextView tvOption = (TextView) holder.getViewById(R.id.tv_option);
            if (itemData instanceof CardResultBean.DataBean.ApplyWorkFlowsBean) {
                CardResultBean.DataBean.ApplyWorkFlowsBean bean = (CardResultBean.DataBean.ApplyWorkFlowsBean) itemData;
                tvApprover.setText(bean.getCheckUserName());
                tvTime.setText(bean.getDealDate());
                tvState.setText(bean.getIsCheckCH());
                tvOption.setText(TextUtils.isEmpty(bean.getApproveResultCH()) ? "" : bean.getApproveResultCH());
                return;
            }
            if (itemData instanceof TraverResultBean.DataBean.NchrapplyWorkFlowBean) {
                TraverResultBean.DataBean.NchrapplyWorkFlowBean bean = (TraverResultBean.DataBean.NchrapplyWorkFlowBean) itemData;
                tvApprover.setText(bean.getCheckUserName());
                tvTime.setText(bean.getDealDate());
                tvState.setText(bean.getIsCheckCH());
                tvOption.setText(TextUtils.isEmpty(bean.getApproveResultCH()) ? "" : bean.getApproveResultCH());
                tvOption.setText(bean.getApproveResultCH());
                return;
            }
            if (itemData instanceof RestResultBean.DataBean.NchrapplyWorkFlowBean) {
                RestResultBean.DataBean.NchrapplyWorkFlowBean bean = (RestResultBean.DataBean.NchrapplyWorkFlowBean) itemData;
                tvApprover.setText(bean.getCheckUserName());
                tvTime.setText(bean.getDealDate());
                tvState.setText(bean.getIsCheckCH());
                tvOption.setText(TextUtils.isEmpty(bean.getApproveResultCH()) ? "" : bean.getApproveResultCH());
                return;
            }
            if (itemData instanceof OverTimeResultBean.DataBean.NchrapplyWorkFlowBean) {
                OverTimeResultBean.DataBean.NchrapplyWorkFlowBean bean = (OverTimeResultBean.DataBean.NchrapplyWorkFlowBean) itemData;
                tvApprover.setText(bean.getCheckUserName());
                tvTime.setText(TextUtils.isEmpty(bean.getDealDate()) ? "" : bean.getDealDate());
                tvState.setText(bean.getIsCheckCH());
                tvOption.setText(TextUtils.isEmpty(bean.getApproveResultCH()) ? "" : bean.getApproveResultCH());
            }
        }
    }


    private void setTopTextView(TextView tvIdTitle, TextView tvTypeTitle, LinearLayout llTypeAll, String billType) {
        if (TextUtils.equals(billType, "6402")) {//签卡申请
            tvTypeTitle.setText("签卡类别");
            llTypeAll.setVisibility(View.GONE);
            tvIdTitle.setText("签卡单编码");
        }
        if (TextUtils.equals(billType, "6403")) {//出差申请
            tvTypeTitle.setText("出差类别");
            llTypeAll.setVisibility(View.VISIBLE);
            tvIdTitle.setText("出差编码");
        }
        if (TextUtils.equals(billType, "6404")) {//休假申请
            tvTypeTitle.setText("休假类别");
            llTypeAll.setVisibility(View.VISIBLE);
            tvIdTitle.setText("休假单编码");
        }
        if (TextUtils.equals(billType, "6405")) {//加班申请
            tvTypeTitle.setText("加班类别");
            llTypeAll.setVisibility(View.VISIBLE);
            tvIdTitle.setText("加班单编码");
        }
    }

    private void showTypeDialog() {
        if (mChooseDialog == null) {
            mChooseDialog = new Dialog(this, R.style.DialogChoose);
            //点击其他地方消失
            mChooseDialog.setCanceledOnTouchOutside(true);
            //填充对话框的布局
            View dialogView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_dialog_upcominginfo_choose, null, false);
            mLvList = (ListView) dialogView.findViewById(R.id.lv_content);
            mFinalBaseAdapter = null;
            if (typeData.size() > 0) {
                typeData.clear();
            }
            mFinalBaseAdapter = new FinalBaseAdapter<String>(typeData, R.layout.layout_item_upcominginfo, this);
            mLvList.setAdapter(mFinalBaseAdapter);
            mLvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    clickItemPosition = i;
                    mFinalBaseAdapter.notifyDataSetChanged();
                    if (mChooseDialog != null) {
                        mChooseDialog.dismiss();
                    }
                }
            });
            mChooseDialog.setContentView(dialogView);
            Window dialogWindow = mChooseDialog.getWindow();
            dialogWindow.setGravity(Gravity.CENTER);
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            dialogWindow.setAttributes(lp);
        }
        mChooseDialog.show();//显示对话框
    }

    @OnClick({R.id.toolbar_text_btn, R.id.tv_agree, R.id.tv_disagree, R.id.tv_tack_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar_text_btn:
                if (TextUtils.equals(mToolbarTextBtn.getText().toString().trim(), "评阅情况")) {
                    mToolbarTextBtn.setText("查看明细");
                    if (mDatas.size() >= 0) {
                        mDatas.clear();
                    }
                    String billType = "";
                    if (TextUtils.equals(mWhichList, "1")) {
                        mDatas.add(mBean);
                        billType = mBean.getBillType();
                    }
                    if (TextUtils.equals(mWhichList, "2") || TextUtils.equals(mWhichList, "3")) {
                        mDatas.add(mSearchBean);
                        billType = mSearchBean.getPkBillType();
                    }
                    if (TextUtils.equals(billType, "6402")) {//签卡申请
                        mDatas.addAll(mCardResultBean.getData().getApplyWorkFlows());
                    }
                    if (TextUtils.equals(billType, "6403")) {//出差申请
                        mDatas.addAll(mTraverResultBean.getData().getNchrapplyWorkFlow());
                    }
                    if (TextUtils.equals(billType, "6404")) {//休假申请
                        mDatas.addAll(mRestResultBean.getData().getNchrapplyWorkFlow());
                    }
                    if (TextUtils.equals(billType, "6405")) {//加班申请
                        mDatas.addAll(mOverTimeResultBean.getData().getNchrapplyWorkFlow());
                    }
                    mFinalRecycleAdapter.notifyDataSetChanged();
                    mRecyclerView.scrollToPosition(0);
                    return;
                }
                if (TextUtils.equals(mToolbarTextBtn.getText().toString().trim(), "查看明细")) {
                    mToolbarTextBtn.setText("评阅情况");
                    if (mDatas.size() >= 0) {
                        mDatas.clear();
                    }
                    String billType = "";
                    if (TextUtils.equals(mWhichList, "1")) {
                        mDatas.add(mBean);
                        billType = mBean.getBillType();
                    }
                    if (TextUtils.equals(mWhichList, "2") || TextUtils.equals(mWhichList, "3")) {
                        mDatas.add(mSearchBean);
                        billType = mSearchBean.getPkBillType();
                    }
                    if (TextUtils.equals(billType, "6402")) {//签卡申请
                        mDatas.addAll(mCardResultBean.getData().getNchrSignDetails());
                    }
                    if (TextUtils.equals(billType, "6403")) {//出差申请
                        mDatas.addAll(mTraverResultBean.getData().getNchrevectionApplyDetail());
                    }
                    if (TextUtils.equals(billType, "6404")) {//休假申请
                        mDatas.addAll(mRestResultBean.getData().getNchrfurloughApplyDetail());
                    }
                    if (TextUtils.equals(billType, "6405")) {//加班申请
                        mDatas.addAll(mOverTimeResultBean.getData().getNchroverTimeApplyDetail());
                    }
                    mFinalRecycleAdapter.notifyDataSetChanged();
                    mRecyclerView.scrollToPosition(0);
                }
                break;
            case R.id.tv_agree:
                List<ApporveBodyItemBean> list = new ArrayList<>();
                ApporveBodyItemBean apporveBodyItemBean = null;
                if (TextUtils.equals("1", mWhichList)) {
                    apporveBodyItemBean = new ApporveBodyItemBean(mBean.getBillCode(), true, mBean.getBillType());
                } else {
                    apporveBodyItemBean = new ApporveBodyItemBean(mSearchBean.getBillNo(), true, mSearchBean.getPkBillType());
                }
                list.add(apporveBodyItemBean);
                mPresenter.postApproval(list);
                break;
            case R.id.tv_disagree:
                List<ApporveBodyItemBean> list2 = new ArrayList<>();
                ApporveBodyItemBean disApporveBodyItemBean = null;
                if (TextUtils.equals("1", mWhichList)) {
                    disApporveBodyItemBean = new ApporveBodyItemBean(mBean.getBillCode(), true, mBean.getBillType());
                } else {
                    disApporveBodyItemBean = new ApporveBodyItemBean(mSearchBean.getBillNo(), true, mSearchBean.getPkBillType());
                }
                list2.add(disApporveBodyItemBean);
                mPresenter.postApproval(list2);
                break;
            case R.id.tv_tack_back:
                showLoadingView();
                String billCode = "";
                String billType = "";
                if (TextUtils.equals("1", mWhichList)) {
                    billCode = mBean.getBillCode();
                    billType = mBean.getBillType();
                } else {
                    billCode = mSearchBean.getBillNo();
                    billType = mSearchBean.getPkBillType();
                }
                if (TextUtils.equals(mTvTackBack.getText(), "收回")) {
                    mPresenter.postTackBack(billCode, billType);
                }
                if (TextUtils.equals(mTvTackBack.getText(), "删除")) {
                    mPresenter.getDelete(billCode, billType);
                }
                break;
        }
    }

    @Override
    public void bindView(FinalBaseAdapter.FinalViewHolder viewHolder, Object dataBean, int position) {
        TextView tvItem = (TextView) viewHolder.getViewById(R.id.tv_item);
        View stork = viewHolder.getViewById(R.id.stork);
        String text = (String) dataBean;
        tvItem.setText(text);
        if (position == clickItemPosition) {
            tvItem.setTextColor(getResources().getColor(R.color.blue_69B0F2));
        } else {
            tvItem.setTextColor(getResources().getColor(R.color.grey));
        }
        if (position == mFinalBaseAdapter.getCount() - 1) {
            stork.setVisibility(View.INVISIBLE);
        } else {
            stork.setVisibility(View.VISIBLE);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setTime(SelectedTypeBean bean) {
        String tag = bean.getTag();
        if (TextUtils.equals(tag, "0")) {
            mEtCommonalityBeginTime.setText(bean.getSelectedType());
            return;
        }
        if (TextUtils.equals(tag, "1")) {
            mEtCommonalityEndTime.setText(bean.getSelectedType());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onGetApproveInfoSuccess(String json) {
        String billType = "";
        Gson gson = new Gson();
        if (mDatas.size() >= 0) {
            mDatas.clear();
        }
        if (TextUtils.equals(mWhichList, "1")) {
            billType = mBean.getBillType();
            mDatas.add(mBean);
        }
        if (TextUtils.equals(mWhichList, "2") || TextUtils.equals(mWhichList, "3")) {
            billType = mSearchBean.getPkBillType();
            mDatas.add(mSearchBean);
        }
        if (TextUtils.equals(billType, "6402")) {//签卡申请
            mCardResultBean = gson.fromJson(json, CardResultBean.class);
            if (!TextUtils.equals(mCardResultBean.getCode(), ApiJava.REQUEST_CODE_OK)) {
                onGetApproveInfoFailure(mCardResultBean.getCode(), mCardResultBean.getMessage());
                return;
            }
            mDatas.addAll(mCardResultBean.getData().getNchrSignDetails());
            for (CardResultBean.DataBean.ApplyWorkFlowsBean bean : mCardResultBean.getData().getApplyWorkFlows()) {
                if (TextUtils.equals(bean.getIsCheck(), "N") && TextUtils.equals("1", mWhichList)) {
                    mRlTackBack.setVisibility(View.VISIBLE);
                }
            }
        }
        if (TextUtils.equals(billType, "6403")) {//出差申请
            mTraverResultBean = gson.fromJson(json, TraverResultBean.class);
            if (!TextUtils.equals(mTraverResultBean.getCode(), ApiJava.REQUEST_CODE_OK)) {
                onGetApproveInfoFailure(mTraverResultBean.getCode(), mTraverResultBean.getMessage());
                return;
            }
            mDatas.addAll(mTraverResultBean.getData().getNchrevectionApplyDetail());
            for (TraverResultBean.DataBean.NchrapplyWorkFlowBean bean : mTraverResultBean.getData().getNchrapplyWorkFlow()) {
                if (TextUtils.equals(bean.getIsCheck(), "N") && TextUtils.equals("1", mWhichList)) {
                    mRlTackBack.setVisibility(View.VISIBLE);
                }
            }
        }
        if (TextUtils.equals(billType, "6404")) {//休假申请
            mRestResultBean = gson.fromJson(json, RestResultBean.class);
            if (!TextUtils.equals(mRestResultBean.getCode(), ApiJava.REQUEST_CODE_OK)) {
                onGetApproveInfoFailure(mRestResultBean.getCode(), mRestResultBean.getMessage());
                return;
            }
            mDatas.addAll(mRestResultBean.getData().getNchrfurloughApplyDetail());
            for (RestResultBean.DataBean.NchrapplyWorkFlowBean bean : mRestResultBean.getData().getNchrapplyWorkFlow()) {
                if (TextUtils.equals(bean.getIsCheck(), "N") && TextUtils.equals("1", mWhichList)) {
                    mRlTackBack.setVisibility(View.VISIBLE);
                }
            }
        }
        if (TextUtils.equals(billType, "6405")) {//加班申请
            mOverTimeResultBean = gson.fromJson(json, OverTimeResultBean.class);
            if (!TextUtils.equals(mOverTimeResultBean.getCode(), ApiJava.REQUEST_CODE_OK)) {
                onGetApproveInfoFailure(mOverTimeResultBean.getCode(), mOverTimeResultBean.getMessage());
                return;
            }
            mDatas.addAll(mOverTimeResultBean.getData().getNchroverTimeApplyDetail());
            for (OverTimeResultBean.DataBean.NchrapplyWorkFlowBean bean : mOverTimeResultBean.getData().getNchrapplyWorkFlow()) {
                if (TextUtils.equals(bean.getIsCheck(), "N") && TextUtils.equals("1", mWhichList)) {
                    mRlTackBack.setVisibility(View.VISIBLE);
                }
            }
        }
        mFinalRecycleAdapter.notifyDataSetChanged();
    }

    @Override
    public void onGetApproveInfoFailure(String errorNo, String strMsg) {
        hideLoadingView();
        showToast(strMsg);
    }

    @Override
    public void onTackBackSuccess(TackBackResultBean bean) {
        hideLoadingView();
        if (TextUtils.equals(bean.getData().get(0).getStatus(), "1")) {
            showToast("收回成功");
            setResult(101);
            finish();
        } else {
            showToast(bean.getData().get(0).getReason());
        }
    }

    @Override
    public void onApproveSuccess(AgreeDisagreeResultBean resultBean) {
        List<AgreeDisagreeResultBean.DataBean> beanList = resultBean.getData();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < beanList.size(); i++) {
            if (!TextUtils.equals(beanList.get(i).getStatus(), "1")) {
                stringBuilder.append(beanList.get(i).getReason() + "\n");
            }
        }
        if (!TextUtils.isEmpty(stringBuilder.toString().trim())) {
            showToast(stringBuilder.toString().trim());
            return;
        }
        setResult(101);
        finish();

    }

    @Override
    public void onApproveFailure(int errorNo, String strMsg) {
        showToast(strMsg);
    }

    @Override
    public void onDeleteSuccess(DeleteBean bean) {
        hideLoadingView();
        showToast("删除成功");
        setResult(101);
        finish();
    }

    @Override
    public void ondELETEFailure(String s, String strMsg) {
        onGetApproveInfoFailure(s, strMsg);
    }
}