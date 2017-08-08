package com.shanlin.oa.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.shanlin.oa.R;
import com.shanlin.oa.common.Api;
import com.shanlin.oa.manager.AppConfig;
import com.shanlin.oa.model.approval.Approval;
import com.shanlin.oa.ui.adapter.ApprovalListAdapter;
import com.shanlin.oa.ui.base.BaseActivity;
import com.shanlin.oa.utils.BitmapUtils;
import com.shanlin.oa.utils.LogUtils;
import com.shanlin.oa.utils.StringUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * ProjectName: dev-beta-v1.0.1
 * PackageName: com.itcrm.GroupInformationPlatform.ui.activity
 * Author:Created by Tsui on Date:2016/11/15 9:50
 * Description:审批列表
 */
public class ApprovalListActivity extends BaseActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.toolbar_text_btn)
    TextView mTolbarTextBtn;
    @Bind(R.id.parting_line_top)
    View partLineTop;
    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    ApprovalListAdapter mAdapter;
    @Bind(R.id.approvalListContentView)
    RelativeLayout mRootView;
    @Bind(R.id.btn_group_box)
    RadioGroup btnGroupBox;

    @Bind(R.id.tv_empty_view)
    TextView mTvEmptyView;

    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<Approval> list;
    private int currentState = 1;//当前状态，我发起的1,待我审批2，我审批的3
    private boolean isFirstIn = true;
    private int meLaunchCurrentPage = 1;//我发起的分页页数
    private int waitApprovalCurrentPage = 1;//待我审批的分页页数
    private int meApprovaledCurrentPage = 1;//我审批的分页页数

    private boolean meLaunchMore = true;//我发起的分页有更多，false则没有
    private boolean waitApprovalMore = true;//待我审批的分页有更多
    private boolean meApprovaledMore = true;//我审批的分页有更多
    private int limit = 10;//当前条目数量
    private PopupWindow popupWindow;

    @Bind(R.id.rb_me_launch)
    RadioButton rbMeLaunch;
    @Bind(R.id.rb_wait_approval)
    RadioButton rbWaitApproval;
    @Bind(R.id.rb_me_approvaled)
    RadioButton rbMeApprovaled;

    RadioButton rbDateAll;
    RadioButton rbDateCurrentMonth;
    RadioButton rbDateLastMonth;
    RadioButton rbDateLastTwoMonth;
    RadioButton rbDateLastThreeMonth;
    RadioButton rbDateLastSixMonth;
    RadioButton rbTypeAll;
    RadioButton rbTypeLeave;
    RadioButton rbTypeOverTime;
    RadioButton rbTypeOfficesSupplies;
    RadioButton rbTypeTravel;
    RadioButton rbTypeBleave;
    RadioButton rbTypeNotice;

    private ArrayList<RadioButton> listDate;
    private ArrayList<RadioButton> listType;


    private boolean hasMore = false;
    private boolean isLoading = false;
    private Integer LOAD_MORE_CONTENT = 1;//jia zai更多内容的标识

    private String requestUrl;//变量：请求的url，根据状态不同来切换url
    private Integer NO_MORE_CONTENT = 0;
    public Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == NO_MORE_CONTENT) {
                showToast("没有更多了");
                mSwipeRefreshLayout.setRefreshing(false);
            }
            if (msg.what == LOAD_MORE_CONTENT) {
                loadData(false, true, "", "");
            }
            return false;
        }
    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approval_list);
        ButterKnife.bind(this);
        initToolBar();
        setTranslucentStatus(this);
        initWidget();
        initData(getIntent());
        loadData(true, false, "", "");
    }

    private void initData(Intent intent) {
        currentState = intent.getIntExtra("whichList", 1);

        int whichList = intent.getIntExtra("whichList", 0);

        if (whichList != 0) {
            switch (whichList) {
                case 1:
                    rbMeLaunch.setChecked(true);
                    meLaunchCurrentPage = 1;
                    currentState = 1;
                    meLaunchMore = true;
                    break;
                case 2:
                    rbWaitApproval.setChecked(true);
                    waitApprovalCurrentPage = 1;
                    waitApprovalMore = true;
                    currentState = 2;
                    break;
                case 3:
                    rbMeApprovaled.setChecked(true);
                    meApprovaledCurrentPage = 1;
                    meApprovaledMore = true;
                    currentState = 3;
                    break;
            }
            list.clear();//需要清空list，不然我发起的或者待我审批的列表内容会显示到同一个页面
            clearCurrentDataAndChangeUI();
            loadData(false, false, "", "");
        } else {
            rbMeLaunch.setChecked(true);
        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
        initData(intent);
    }

    private void handleIntent(Intent intent) {

        if (intent != null) {
            String oa_id = intent.getStringExtra("oa_id");
            if (!StringUtils.isBlank(oa_id)) {
                //循环将list中的那个已读的找出来移除
                for (int i = 0; i < list.size(); i++) {
                    String oa_id1 = list.get(i).getOa_id();
                    if (oa_id1.equals(oa_id)) {
                        list.remove(i);
                        mAdapter.notifyDataSetChanged();
                    }

                }
            }
        }
    }

    private void initWidget() {

        btnGroupBox.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_me_launch:
                        rbMeLaunch.setChecked(true);
                        meLaunchCurrentPage = 1;
                        currentState = 1;
                        meLaunchMore = true;
                        break;
                    case R.id.rb_wait_approval:
                        rbWaitApproval.setChecked(true);
                        waitApprovalCurrentPage = 1;
                        waitApprovalMore = true;
                        currentState = 2;
                        break;
                    case R.id.rb_me_approvaled:
                        rbMeApprovaled.setChecked(true);
                        meApprovaledCurrentPage = 1;
                        meApprovaledMore = true;
                        currentState = 3;
                        break;
                }
                list.clear();//需要清空list，不然我发起的或者待我审批的列表内容会显示到同一个页面
                try {
                    mAdapter.removeAllFooterView();
                } catch (Exception e) {
                }
                loadData(false, false, "", "");
            }
        });
        mSwipeRefreshLayout.setColorSchemeColors(Color.parseColor("#0EA7ED"),
                Color.parseColor("#0EA7ED"), Color.parseColor("#0EA7ED"));
        mSwipeRefreshLayout.setOnRefreshListener(this);
        linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        list = new ArrayList<>();
        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter adapter, View view, int i) {
                Intent intent = null;
                if (currentState == 1) {
                    //我发起的
                    //审批分类ID 1公告2办公用品3请假4出差

                    String apprId = list.get(i).getAppr_id();
                    switch (Integer.parseInt(apprId)) {
                        case 1:
                            intent = new Intent(ApprovalListActivity.this,
                                    MeLaunchNoticeActivity.class);
                            break;
                        case 2:
                            intent = new Intent(ApprovalListActivity.this,
                                    MeLaunchOfficesSuppliesActivity.class);
                            break;
                        case 3:
                            intent = new Intent(ApprovalListActivity.this,
                                    MeLaunchLeaveActivity.class);

                            break;
                        case 4:
                            intent = new Intent(ApprovalListActivity.this,
                                    MeLaunchTravalActivity.class);
                            break;
                        case 5:
                            intent = new Intent(ApprovalListActivity.this,
                                    MeLaunchOverTimeActivity.class);
                            break;
                        case 6:
                            intent = new Intent(ApprovalListActivity.this,
                                    MeLaunchPublicOutActivity.class);
                            break;
                    }
                    if (intent != null) {
                        intent.putExtra("appr_id", list.get(i).getAppr_id());
                        intent.putExtra("oal_id", list.get(i).getOal_id());
                        intent.putExtra("oa_id", list.get(i).getOa_id());
                        intent.putExtra("isCanReply", false);
                        intent.putExtra("titleName", "我发起的");

                    }
                }
                if (currentState == 2) {
                    //待我审批
                    //审批分类ID 1公告2办公用品3请假4出差


                    String apprId = list.get(i).getAppr_id();
                    LogUtils.e("apprId->" + apprId);
                    switch (Integer.parseInt(apprId)) {
                        case 1:
                            intent = new Intent(ApprovalListActivity.this,
                                    MeLaunchNoticeActivity.class);
                            break;
                        case 2:
                            intent = new Intent(ApprovalListActivity.this,
                                    MeLaunchOfficesSuppliesActivity.class);
                            break;
                        case 3:
                            intent = new Intent(ApprovalListActivity.this,
                                    MeLaunchLeaveActivity.class);

                            break;
                        case 4:
                            intent = new Intent(ApprovalListActivity.this,
                                    MeLaunchTravalActivity.class);
                            break;
                        case 5:
                            intent = new Intent(ApprovalListActivity.this,
                                    MeLaunchOverTimeActivity.class);
                            break;
                        case 6:
                            intent = new Intent(ApprovalListActivity.this,
                                    MeLaunchPublicOutActivity.class);
                            break;
                    }
                    if (intent != null) {
                        intent.putExtra("isCanReply", true);
                        intent.putExtra("appr_id", list.get(i).getAppr_id());
                        intent.putExtra("oa_id", list.get(i).getOa_id());
                        intent.putExtra("oal_id", list.get(i).getOal_id());
                        intent.putExtra("titleName", "待我审批");
                    }


                }
                if (currentState == 3) {
                    //我审批的
                    //审批分类ID 1公告2办公用品3请假4出差


                    String apprId = list.get(i).getAppr_id();
                    switch (Integer.parseInt(apprId)) {
                        case 1:
                            intent = new Intent(ApprovalListActivity.this,
                                    MeLaunchNoticeActivity.class);
                            break;
                        case 2:
                            intent = new Intent(ApprovalListActivity.this,
                                    MeLaunchOfficesSuppliesActivity.class);
                            break;
                        case 3:
                            intent = new Intent(ApprovalListActivity.this,
                                    MeLaunchLeaveActivity.class);

                            break;
                        case 4:
                            intent = new Intent(ApprovalListActivity.this,
                                    MeLaunchTravalActivity.class);
                            break;
                        case 5:
                            intent = new Intent(ApprovalListActivity.this,
                                    MeLaunchOverTimeActivity.class);
                            break;
                        case 6:
                            intent = new Intent(ApprovalListActivity.this,
                                    MeLaunchPublicOutActivity.class);
                            break;

                    }
                    if (intent != null) {
                        intent.putExtra("appr_id", list.get(i).getAppr_id());
                        intent.putExtra("oa_id", list.get(i).getOa_id());
                        intent.putExtra("oal_id", list.get(i).getOal_id());
                        intent.putExtra("isCanReply", false);
                        intent.putExtra("titleName", "我审批的");
                    }
                }
                if (intent != null) {
                    startActivity(intent);
                }
            }
        });

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                // 0屏幕停止滚动；1:滚动且用户仍在触碰或手指还在屏幕上2：随用户的操作，屏幕上产生的惯性滑动；
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (newState == 0 && list.size() > 9) {
                    hasMore=true;
                    // 只有LinearLayoutManager才有查找第一个和最后一个可见view位置的方法
                    int lastPosition = layoutManager.findLastVisibleItemPosition();
                    if (lastPosition == list.size() - 1) {
                        //看到了最后一条，显示加载更多的状态
                        View view = View.inflate(ApprovalListActivity.this, R.layout.load_more_layout, null);


                        switch (currentState) {
                            case 1:
                                hasMore = meLaunchMore;
                                meLaunchCurrentPage++;
                                break;
                            case 2:
                                waitApprovalCurrentPage++;
                                hasMore = waitApprovalMore;
                                break;
                            case 3:
                                meApprovaledCurrentPage++;
                                hasMore = meApprovaledMore;
                                break;
                        }

                        if (hasMore) {
                            try {
                                //如果没有在加载，才去加载
                                if (!isLoading) {
                                    isLoading = true;
                                    if (!list.isEmpty()) {
                                        mAdapter.addFooterView(view, list.size());
                                    }
                                    handler.sendEmptyMessageDelayed(LOAD_MORE_CONTENT, 1000);
                                }
                            } catch (Exception e) {
                            }

                        } else {
                            handler.sendEmptyMessage(NO_MORE_CONTENT);
                        }
                    }
                }
            }
        });


        if (currentState == 1) {
            mAdapter = new ApprovalListAdapter(list, true);
        } else {
            mAdapter = new ApprovalListAdapter(list, false);
        }

        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onRefresh() {
        loadData(true, false, "", "");
    }

    /**
     * @param isPull 是否是下拉刷新或加载更多触发的
     */

    private void loadData(boolean isPull, final boolean loadMore, String time, String where) {
        if (!isPull && !loadMore) {
            showLoadingView();
            changeLoadState();
        }
        if (isPull) {
            mSwipeRefreshLayout.setRefreshing(true);
            changeLoadState();
        }
        HttpParams params = new HttpParams();
        switch (currentState) {
            case 1:
                params.put("page", meLaunchCurrentPage);
                LogUtils.e("page:" + meLaunchCurrentPage);
                break;
            case 2:
                params.put("page", waitApprovalCurrentPage);
                LogUtils.e("page:" + waitApprovalCurrentPage);
                break;
            case 3:
                params.put("page", meApprovaledCurrentPage);
                LogUtils.e("page:" + meApprovaledCurrentPage);
                break;
        }
        params.put("limit", limit);
        params.put("time", time);
        params.put("where", where);
        params.put("uid", AppConfig.getAppConfig(this).getPrivateUid());
        params.put("token", AppConfig.getAppConfig(this).getPrivateToken());
        if (currentState == 1) {
            requestUrl = Api.APPROVAL_LIST_ME_LAUNCH;
        } else if (currentState == 2) {
            requestUrl = Api.APPROVAL_LIST_WAIT_APPROVAL;
        } else if (currentState == 3) {
            requestUrl = Api.APPROVAL_LIST_ME_APPROVALED;
        }
        initKjHttp().post(requestUrl, params, new HttpCallBack() {

            @Override
            public void onPreStart() {
                super.onPreStart();

            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                LogUtils.e("ApprovalListActivity->" + t);
                try {
                    JSONObject jo = new JSONObject(t);
                    if (Api.getCode(jo) ==Api.RESPONSES_CODE_UID_NULL){
                        catchWarningByCode(Api.getCode(jo));
                    }
                    switch (Api.getCode(jo)) {
                        case Api.RESPONSES_CODE_OK:

                            mTvEmptyView.setVisibility(View.GONE);

                            ArrayList<Approval> listApproval = new ArrayList<>();
                            JSONArray notices = Api.getDataToJSONArray(jo);
                            Approval notice;
                            for (int i = 0; i < notices.length(); i++) {
                                JSONObject jsonObject = notices.getJSONObject(i);
                                if (currentState == 1) {
                                    notice =
                                            new Approval(jsonObject, true);
                                } else {
                                    notice =
                                            new Approval(jsonObject, false);
                                }
                                listApproval.add(notice);
                            }
                            if (loadMore) {
                                //如果是加载更多的话，需要将最后一个view移除了
                                mAdapter.removeAllFooterView();
                            }
                            isLoading = false;

                            list.addAll(listApproval);
                            mAdapter.notifyDataSetChanged();
                            break;
                        case Api.RESPONSES_CODE_DATA_EMPTY:
                            clearCurrentDataAndChangeUI();
                            break;
                        case Api.RESPONSES_CONTENT_EMPTY:
                            clearCurrentDataAndChangeUI();
                            break;
                        case Api.LIMIT_CONTENT_EMPTY:
                            mAdapter.removeAllFooterView();
                            markNoMoreState();
                            showToast("没有更多了");
                            break;
                        case Api.RESPONSES_CODE_TOKEN_NO_MATCH:
                            catchWarningByCode(Api.getCode(jo));
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                hideLoadingView();
                if (null!=mSwipeRefreshLayout ) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                catchWarningByCode(errorNo);
                mTvEmptyView.setVisibility(View.VISIBLE);
            }
        });
    }

    /**
     * 没有数据或者网络错误后全部清空并显示空页面
     */
    private void clearCurrentDataAndChangeUI() {
        markNoMoreState();
        clearRecyclerViewData();
        try {
            mAdapter.removeAllFooterView();
        } catch (Exception e) {
        }
        mTvEmptyView.setVisibility(View.VISIBLE);
    }

    /**
     * 将加载状态和起始页都置为初始值
     */
    private void changeLoadState() {
        list.clear();
        mAdapter.notifyDataSetChanged();
        meLaunchCurrentPage = 1;
        waitApprovalCurrentPage = 1;
        meApprovaledCurrentPage = 1;
        meLaunchMore = true;
        waitApprovalMore = true;
        meApprovaledMore = true;
    }

    /**
     * 标记加载更多的状态
     */
    private void markNoMoreState() {
        switch (currentState) {
            case 1:
                meLaunchMore = false;
                break;
            case 2:
                waitApprovalMore = false;
                break;
            case 3:
                meApprovaledMore = false;
                break;
        }
    }

    /**
     * 清空RecyclerView的数据
     */
    private void clearRecyclerViewData() {
        list.clear();
        mAdapter.notifyDataSetChanged();
    }

    private void bind(View view) {
        rbDateAll = (RadioButton) view.findViewById(R.id.rb_date_all);
        rbDateCurrentMonth = (RadioButton) view.findViewById(R.id.rb_date_current_month);
        rbDateLastMonth = (RadioButton) view.findViewById(R.id.rb_date_last_month);
        rbDateLastTwoMonth = (RadioButton) view.findViewById(R.id.rb_date_last_two_month);
        rbDateLastThreeMonth = (RadioButton) view.findViewById(R.id.rb_date_last_three_month);
        rbDateLastSixMonth = (RadioButton) view.findViewById(R.id.rb_date_last_six_month);

        rbTypeAll = (RadioButton) view.findViewById(R.id.rb_type_all);
        rbTypeLeave = (RadioButton) view.findViewById(R.id.rb_type_leave);
        rbTypeOverTime = (RadioButton) view.findViewById(R.id.rb_type_over_time);
        rbTypeOfficesSupplies = (RadioButton) view.findViewById(R.id.rb_type_offices_supplies);
        rbTypeTravel = (RadioButton) view.findViewById(R.id.rb_type_travel);
        rbTypeNotice = (RadioButton) view.findViewById(R.id.rb_type_notice);
        rbTypeBleave = (RadioButton) view.findViewById(R.id.rb_type_bleave);
    }

    private void addRadioButtonToList() {
        listDate = new ArrayList<>();
        listDate.add(rbDateAll);
        listDate.add(rbDateCurrentMonth);
        listDate.add(rbDateLastMonth);
        listDate.add(rbDateLastTwoMonth);
        listDate.add(rbDateLastThreeMonth);
        listDate.add(rbDateLastSixMonth);
        setListenerForEachRb(listDate);
        listType = new ArrayList<>();
        listType.add(rbTypeAll);
        listType.add(rbTypeNotice);
        listType.add(rbTypeOfficesSupplies);
        listType.add(rbTypeLeave);
        listType.add(rbTypeTravel);
        listType.add(rbTypeOverTime);
        listType.add(rbTypeBleave);
        setListenerForEachRb(listType);

        //默认选中所有的
        rbDateAll.setChecked(true);
        rbTypeAll.setChecked(true);
    }

    /**
     * @param
     */
    private void setListenerForEachRb(final ArrayList<RadioButton> listRb) {
        for (int i = 0; i < listRb.size(); i++) {
            final RadioButton rb = listRb.get(i);
            rb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        clearListCheckedRb(listRb);
                        rb.setChecked(true);
                    }
                }
            });
        }
    }

    private Integer checkCheckedListTypeRb() {
        for (int i = 0; i < listType.size(); i++) {
            if (listType.get(i).isChecked()) {
                return i;
            }
        }
        return 0;
    }

    private Integer checkCheckedListDateRb() {
        for (int i = 0; i < listDate.size(); i++) {
            if (listDate.get(i).isChecked()) {
                return i;
            }
        }
        return 0;
    }

    /**
     * @param list 清除所有选中的radiobutton
     */
    private void clearListCheckedRb(List<RadioButton> list) {
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setChecked(false);
        }
    }

    private void initToolBar() {
        if (mToolbar == null) {
            return;
        }
        setTitle("");//必须在setSupportActionBar之前调用
        mToolbar.setTitleTextColor(Color.parseColor("#000000"));
        setSupportActionBar(mToolbar);
        mTvTitle.setText("审批列表");

        Drawable drawable = BitmapUtils.getDrawable(this, R.drawable.tab_msg_top_select);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        mTvTitle.setCompoundDrawables(null, null, drawable, null);

        mTvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindow();
            }
        });


        Toolbar.LayoutParams lp = new Toolbar.LayoutParams(
                Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        mTvTitle.setLayoutParams(lp);
        mTolbarTextBtn.setText("新建");
        mTolbarTextBtn.setVisibility(View.VISIBLE);
        mTolbarTextBtn.setOnClickListener(this);
        mToolbar.setNavigationIcon(R.drawable.toolbar_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isFirstIn) {
            onRefresh();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isFirstIn = false;
    }

    private void showPopupWindow() {
        View view = View.inflate(this, R.layout.tab_approval_list_popwindow, null);
        bind(view);
        addRadioButtonToList();
        Button btnConfirm = (Button) view.findViewById(R.id.btn_confirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer dateValue = checkCheckedListDateRb();
                Integer typeValue = checkCheckedListTypeRb();
                LogUtils.e("dateValue->" + dateValue + ",typeValue->" + typeValue);
                loadData(false, false, String.valueOf(dateValue), String.valueOf(typeValue));
                popupWindow.dismiss();
            }
        });
        popupWindow = new PopupWindow(view, LinearLayout.LayoutParams
                .WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, false);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.7f;
        getWindow().setAttributes(lp);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });
        popupWindow.setFocusable(true);
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            popupWindow.setAnimationStyle(R.style.top_filter_pop_anim_style);
            popupWindow.showAsDropDown(partLineTop, 0, 0, Gravity.CENTER);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        handler.removeCallbacksAndMessages(null);
    }

    @OnClick({R.id.toolbar_text_btn})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.toolbar_text_btn:
                startActivity(new Intent(this, LaunchApprovalActivity.class));
                break;
        }
    }


}
