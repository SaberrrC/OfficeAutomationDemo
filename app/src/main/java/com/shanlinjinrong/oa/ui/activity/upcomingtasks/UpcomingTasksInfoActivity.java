package com.shanlinjinrong.oa.ui.activity.upcomingtasks;

import android.app.Dialog;
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
import android.widget.TextView;

import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.ui.activity.home.schedule.initiateapproval.bean.SelectedTypeBean;
import com.shanlinjinrong.oa.ui.activity.upcomingtasks.adpter.FinalBaseAdapter;
import com.shanlinjinrong.oa.ui.activity.upcomingtasks.adpter.FinalRecycleAdapter;
import com.shanlinjinrong.oa.ui.activity.upcomingtasks.bean.UpcomingInfoDetailBodyBean;
import com.shanlinjinrong.oa.ui.activity.upcomingtasks.bean.UpcomingInfoStateBean;
import com.shanlinjinrong.oa.ui.activity.upcomingtasks.bean.UpcomingInfoTopBean;
import com.shanlinjinrong.oa.ui.activity.upcomingtasks.contract.UpcomingTasksInfoContract;
import com.shanlinjinrong.oa.ui.activity.upcomingtasks.presenter.UpcomingTasksInfoPresenter;
import com.shanlinjinrong.oa.ui.base.HttpBaseActivity;
import com.shanlinjinrong.oa.utils.TimeDialogFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.shanlinjinrong.oa.R.id.tv_time;

public class UpcomingTasksInfoActivity extends HttpBaseActivity<UpcomingTasksInfoPresenter> implements UpcomingTasksInfoContract.View, FinalRecycleAdapter.OnViewAttachListener, FinalBaseAdapter.AdapterListener {

    @Bind(R.id.tv_title)
    TextView     mTvTitle;
    @Bind(R.id.toolbar)
    Toolbar      mToolbar;
    @Bind(R.id.rv_content)
    RecyclerView mRecyclerView;
    @Bind(R.id.toolbar_text_btn)
    TextView     mToolbarTextBtn;
    private List<Object> mDatas = new ArrayList<>();
    private FinalRecycleAdapter mFinalRecycleAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private int                 mIndex;
    private boolean             mMove;
    private Dialog              mChooseDialog;
    private ListView            mLvList;
    public static String[] type_0 = {"出差", "外出"};
    public static String[] type_1 = {"type_1", "type_1"};
    public static String[] type_2 = {"事假", "婚假", "年假", "丧假"};
    public static String[] type_3 = {"忘记打卡", "考勤机故障", "地铁故障"};
    private FinalBaseAdapter<String> mFinalBaseAdapter;
    private List<String> typeData          = new ArrayList<>();
    private int          clickItemPosition = 0;
    private TextView mEtCommonalityBeginTime;
    private TextView mEtCommonalityEndTime;
    //    private DatePicker mBeginTimePicker;
    //    private DatePicker mEndTimePicker;

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
        map.put(UpcomingInfoTopBean.class, R.layout.layout_item_upcominginfo_top);
        map.put(UpcomingInfoStateBean.class, R.layout.upcoming_item);
        map.put(UpcomingInfoDetailBodyBean.class, R.layout.layout_item_upcominginfo_detail_body);
        mFinalRecycleAdapter = new FinalRecycleAdapter(mDatas, map, this);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mFinalRecycleAdapter);
    }

    private void initData() {
        mDatas.add(new UpcomingInfoTopBean());
        mDatas.add(new UpcomingInfoStateBean());
    }

    private void initToolbar() {
        if (mToolbar == null) {
            return;
        }
        mToolbar.setTitle("");//必须在setSupportActionBar之前调用
        mToolbar.setTitleTextColor(Color.parseColor("#000000"));
        mTvTitle.setText("朱展宏的出差申请");
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
        if (itemData instanceof UpcomingInfoTopBean) {
            TextView tvId = (TextView) holder.getViewById(R.id.tv_id);
            LinearLayout llType = (LinearLayout) holder.getViewById(R.id.ll_type);
            TextView tvType = (TextView) holder.getViewById(R.id.tv_type);
            TextView tvApplyTime = (TextView) holder.getViewById(R.id.tv_apply_time);
            llType.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showTypeDialog();
                }
            });
            return;
        }
        if (itemData instanceof UpcomingInfoStateBean) {
            ImageView imgDeleteDetail = (ImageView) holder.getViewById(R.id.img_delete_detail);
            mEtCommonalityBeginTime = (TextView) holder.getViewById(R.id.et_commonality_begin_time);
            mEtCommonalityEndTime = (TextView) holder.getViewById(R.id.et_commonality_end_time);
            TextView tvCommonality = (TextView) holder.getViewById(R.id.tv_commonality);
            EditText etCommonalityShow1 = (EditText) holder.getViewById(R.id.et_commonality_show1);//出差地点
            EditText etCommonalityShow2 = (EditText) holder.getViewById(R.id.et_commonality_show2);//出差原因
            EditText etCommonalityShow3 = (EditText) holder.getViewById(R.id.et_commonality_show3);//交接人
            imgDeleteDetail.setVisibility(View.GONE);

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
            return;
        }
        if (itemData instanceof UpcomingInfoDetailBodyBean) {
            TextView tvApprover = (TextView) holder.getViewById(R.id.tv_approver);
            TextView tvTime = (TextView) holder.getViewById(tv_time);
            TextView tvState = (TextView) holder.getViewById(R.id.tv_state);
            TextView tvOption = (TextView) holder.getViewById(R.id.tv_option);
            return;
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
            int type = getIntent().getIntExtra("type", -1);
            type = 1;
            if (typeData.size() > 0) {
                typeData.clear();
            }
            if (type == 0) {
                typeData.addAll(Arrays.asList(type_0));
            }
            if (type == 1) {
                typeData.addAll(Arrays.asList(type_1));
            }
            if (type == 2) {
                typeData.addAll(Arrays.asList(type_2));
            }
            if (type == 3) {
                typeData.addAll(Arrays.asList(type_3));
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

    @OnClick({R.id.toolbar_text_btn, R.id.tv_agree, R.id.tv_disagree})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar_text_btn:
                if (TextUtils.equals(mToolbarTextBtn.getText().toString().trim(), "评阅情况")) {
                    mToolbarTextBtn.setText("查看明细");
                    mDatas.remove(1);
                    mDatas.add(1, new UpcomingInfoDetailBodyBean());
                    mFinalRecycleAdapter.notifyDataSetChanged();
                    mRecyclerView.scrollToPosition(0);
                    return;
                }
                if (TextUtils.equals(mToolbarTextBtn.getText().toString().trim(), "查看明细")) {
                    mToolbarTextBtn.setText("评阅情况");
                    mDatas.remove(1);
                    mDatas.add(1, new UpcomingInfoStateBean());
                    mFinalRecycleAdapter.notifyDataSetChanged();
                    mRecyclerView.scrollToPosition(0);
                }
                break;
            case R.id.tv_agree:
                break;
            case R.id.tv_disagree:
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
}