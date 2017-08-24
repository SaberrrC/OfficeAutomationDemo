package com.shanlinjinrong.oa.ui.activity.home.workreport;

import android.annotation.SuppressLint;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.common.Api;
import com.shanlinjinrong.oa.manager.AppConfig;
import com.shanlinjinrong.oa.model.ReportList;
import com.shanlinjinrong.oa.ui.adapter.WorkReportListAdapter;
import com.shanlinjinrong.oa.ui.base.BaseActivity;
import com.shanlinjinrong.oa.utils.BitmapUtils;
import com.shanlinjinrong.oa.utils.LogUtils;

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
 * <h3>Description: 工作汇报列表（我发起的、收到的、抄送给我的） </h3>
 * <b>Notes:</b> Created by KevinMeng on 2016/9/6.<br />
 */
public class WorkReportListActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.parting_line_top)
    View partLineTop;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.toolbar_text_btn)
    TextView toolbarTextBtn;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.btn_group_box)
    RadioGroup btnGroupBox;
    @Bind(R.id.root)
    RelativeLayout root;
    @Bind(R.id.btn_send)
    RadioButton btnSend;
    @Bind(R.id.btn_send_me)
    RadioButton btnSendMe;
    @Bind(R.id.btn_copy_me)
    RadioButton btnCopyMe;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView recyclerView;
    private String interfaceUrl;
    private List<ReportList> items;
    private List<ReportList> lists = new ArrayList<>();
    private WorkReportListAdapter adapter;
    private boolean isReply = true;//是否带回复
    private int currentState = 1;//当前状态，我发起的1,发送我的2，抄送我的3
    private boolean hasMore = false;
    private boolean isLoading = false;
    private Integer LOAD_MORE_CONTENT = 1;//jia zai更多内容的标识

    private boolean meLaunchMore = true;//我发起的分页有更多，false则没有
    private boolean sendToMeMore = true;//待我审批的分页有更多
    private boolean copyToMeMore = true;//我审批的分页有更多

    private int meLaunchCurrentPage = 1;//我发起的分页页数
    private int sendToMeCurrentPage = 1;//待我审批的分页页数
    private int copyToMeCurrentPage = 1;//我审批的分页页数

    RadioButton rbDateAll;
    RadioButton rbDateCurrentMonth;
    RadioButton rbDateLastMonth;
    RadioButton rbDateLastTwoMonth;
    RadioButton rbDateLastThreeMonth;
    RadioButton rbDateLastSixMonth;
    RadioButton rbTypeAll;
    RadioButton rbTypeWorkReportWeek;
    RadioButton rbTypeWorkReportMonth;

    private int limit = 10;//当前条目数量

    private boolean isFirst = true;

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
    private PopupWindow popupWindow;
    private ArrayList<RadioButton> listDate;
    private ArrayList<RadioButton> listType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_report_list);
        ButterKnife.bind(this);
        initToolBar();
        setTranslucentStatus(this);
        interfaceUrl = Api.REPORT_SEND;
        initWidget();
        initData(getIntent());
        loadData(true, false, "", "");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initData(intent);
    }

    private void initData(Intent intent) {

        int whichList = intent.getIntExtra("whichList", 0);
        if (whichList != 0) {
            switch (whichList) {
                case 1:
                    btnSend.setChecked(true);
                    interfaceUrl = Api.REPORT_SEND;
                    isReply = true;
                    break;
                case 2:
                    btnSendMe.setChecked(true);
                    interfaceUrl = Api.REPORT_SEND_TO_ME;
                    isReply = true;
                    break;
                case 3:
                    btnCopyMe.setChecked(true);
                    interfaceUrl = Api.REPORT_COPY_TO_ME;
                    isReply = false;
                    break;
            }
        }
    }

    private void initWidget() {
        mSwipeRefreshLayout.setColorSchemeColors(Color.parseColor("#0EA7ED"),
                Color.parseColor("#0EA7ED"), Color.parseColor("#0EA7ED"));
        mSwipeRefreshLayout.setOnRefreshListener(this);
        btnGroupBox.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.btn_send:
                        meLaunchMore = true;
                        meLaunchCurrentPage = 1;
                        currentState = 1;
                        interfaceUrl = Api.REPORT_SEND;
                        isReply = true;
                        break;
                    case R.id.btn_send_me:
                        sendToMeMore = true;
                        sendToMeCurrentPage = 1;
                        currentState = 2;
                        interfaceUrl = Api.REPORT_SEND_TO_ME;
                        isReply = true;
                        break;
                    case R.id.btn_copy_me:
                        copyToMeMore = true;
                        copyToMeCurrentPage = 1;
                        currentState = 3;
                        interfaceUrl = Api.REPORT_COPY_TO_ME;
                        isReply = false;
                        break;
                }
                lists.clear();
                try {
                    adapter.removeAllFooterView();
                } catch (Exception e) {
                }
                adapter.notifyDataSetChanged();
                loadData(false, false, "", "");
            }
        });
        recyclerView = new RecyclerView(WorkReportListActivity.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(WorkReportListActivity.this));
        recyclerView.addOnItemTouchListener(new ItemClick());

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                // 0屏幕停止滚动；1:滚动且用户仍在触碰或手指还在屏幕上2：随用户的操作，屏幕上产生的惯性滑动；
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (newState == 0 && lists.size() > 9) {
                    hasMore=true;
                    // 只有LinearLayoutManager才有查找第一个和最后一个可见view位置的方法
                    int lastPosition = layoutManager.findLastVisibleItemPosition();
                    if (lastPosition == lists.size() - 1) {
                        //看到了最后一条，显示加载更多的状态
                        View view = View.inflate(WorkReportListActivity.this, R.layout.load_more_layout, null);
                        switch (currentState) {
                            case 1:
                                hasMore = meLaunchMore;
                                meLaunchCurrentPage++;
                                break;
                            case 2:
                                sendToMeCurrentPage++;
                                hasMore = sendToMeMore;
                                break;
                            case 3:
                                copyToMeCurrentPage++;
                                hasMore = copyToMeMore;
                                break;
                        }

                        if (hasMore) {
                            try {
                                //如果没有在加载，才去加载
                                if (!isLoading) {
                                    isLoading = true;
                                    if (!lists.isEmpty()) {
                                    adapter.addFooterView(view, lists.size());}
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
        root.addView(recyclerView);
        if (interfaceUrl.equals(Api.REPORT_SEND)) {
            adapter = new WorkReportListAdapter(lists);
        } else {
            adapter = new WorkReportListAdapter(lists);
        }
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isFirst) {
            onRefresh();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isFirst = false;
    }

    @Override
    public void onRefresh() {
        loadData(true, false, "", "");
    }

    private void loadData(boolean isPull, final boolean loadMore, String time, String type) {

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
                break;
            case 2:
                params.put("page", sendToMeCurrentPage);
                break;
            case 3:
                params.put("page", copyToMeCurrentPage);
                break;
        }
        params.put("limit", limit);
        params.put("filter_type", type);
        params.put("filter_time", time);
        params.put("uid", AppConfig.getAppConfig(this).getPrivateUid());
        params.put("token", AppConfig.getAppConfig(this).getPrivateToken());
        params.put("department_id", AppConfig.getAppConfig(this).getDepartmentId());
        initKjHttp().post(interfaceUrl, params, new HttpCallBack() {
            @Override
            public void onFinish() {
                super.onFinish();
                hideLoadingView();
                if (null!=mSwipeRefreshLayout ) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                LogUtils.e(t);
                try {
                    JSONObject jo = new JSONObject(t);
                    switch (Api.getCode(jo)) {
                        case Api.RESPONSES_CODE_OK:
                            removeEmptyView();

                            items = new ArrayList<>();
                            JSONArray ja = Api.getDataToJSONArray(jo);
                            for (int i = 0; i < ja.length(); i++) {
                                items.add(new ReportList(ja.getJSONObject(i)));
                            }
                            if (loadMore) {
                                //如果是加载更多的话，需要将最后一个view移除了
                                adapter.removeAllFooterView();
                            }
                            isLoading = false;
                            lists.addAll(items);
                            adapter.notifyDataSetChanged();
                            break;
                        case Api.RESPONSES_CONTENT_EMPTY:
                            clearCurrentDataAndChangeUI();
                            break;
                        case Api.RESPONSES_CODE_DATA_EMPTY:
                            clearCurrentDataAndChangeUI();
                            break;
                        case Api.LIMIT_CONTENT_EMPTY:
                            markNoMoreState();
                            adapter.removeAllFooterView();
                            showToast("没有更多了");
                            break;
                        case Api.RESPONSES_CODE_TOKEN_NO_MATCH:
                            catchWarningByCode(Api.getCode(jo));
                            break;
                        case Api.RESPONSES_CODE_UID_NULL:
                            catchWarningByCode(Api.getCode(jo));
                            break;
                    }
                } catch (JSONException e) {
                    System.out.println(e.toString());
                }
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                hideLoadingView();
                clearCurrentDataAndChangeUI();
                LogUtils.e("onFailure->" + strMsg);
                catchWarningByCode(errorNo);
                super.onFailure(errorNo, strMsg);
            }
        });
    }
// picUrl--->http://public.sl.s1.zhitongoa.com/http://shanlin.oss-cn-shanghai.aliyuncs.com/report/3/1489108124Cut_image_1489108124889.jpg

    /**
     * 没有数据或者网络错误后全部清空并显示空页面
     */
    private void clearCurrentDataAndChangeUI() {
        markNoMoreState();
        clearRecyclerViewData();
        showEmptyView("当前没有工作汇报");
    }

    /**
     * 清空RecyclerView的数据
     */
    private void clearRecyclerViewData() {
        lists.clear();
        adapter.notifyDataSetChanged();
    }

    /**
     * 将加载状态和起始页都置为初始值,这里图方便，直接给所有的置为1或true
     */
    private void changeLoadState() {
        lists.clear();
        adapter.notifyDataSetChanged();
        meLaunchCurrentPage = 1;
        sendToMeCurrentPage = 1;
        copyToMeCurrentPage = 1;
        meLaunchMore = true;
        sendToMeMore = true;
        copyToMeMore = true;

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
                sendToMeMore = false;
                break;
            case 3:
                copyToMeMore = false;
                break;
        }
    }

    class ItemClick extends OnItemClickListener {
        @Override
        public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {

            lists.get(i).setStatus("2");
            adapter.notifyDataSetChanged();

            Intent intent = new Intent(WorkReportListActivity.this,
                    WorkReportSingleInfoActivity.class);
            intent.putExtra("rid", lists.get(i).getId());
            intent.putExtra("isReply", isReply);
            startActivity(intent);
        }
    }

    @OnClick(R.id.toolbar_text_btn)
    public void onClick() {
        //新建按钮
        startActivity(new Intent(this, WorkReportLaunchActivity.class));
    }

    private void showEmptyView(String msgText) {
        @SuppressLint("InflateParams")
        View empty = LayoutInflater.from(this).inflate(R.layout.public_empty_view, null);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        empty.setLayoutParams(lp);
        ImageView imageView = (ImageView) empty.findViewById(R.id.empty_image);
        imageView.setVisibility(View.GONE);
        TextView msg = (TextView) empty.findViewById(R.id.message);
        msg.setText(msgText);
        root.addView(empty);
    }

    private void removeEmptyView() {
        root.removeAllViews();
        root.addView(recyclerView);
    }

    private void initToolBar() {
        if (toolbar == null) {
            return;
        }
        setTitle("");//必须在setSupportActionBar之前调用
        toolbar.setTitleTextColor(Color.parseColor("#000000"));
        setSupportActionBar(toolbar);
        tvTitle.setText("工作汇报");
        Drawable drawable = BitmapUtils.getDrawable(this, R.drawable.tab_msg_top_select);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        tvTitle.setCompoundDrawables(null, null, drawable, null);

        tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindow();
            }
        });

        Toolbar.LayoutParams lp = new Toolbar.LayoutParams(
                Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        tvTitle.setLayoutParams(lp);

        toolbarTextBtn.setText("新建");
        toolbarTextBtn.setVisibility(View.VISIBLE);
        toolbar.setNavigationIcon(R.drawable.toolbar_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void showPopupWindow() {
        View view = View.inflate(this, R.layout.tab_work_report_popwindow, null);
        bind(view);
        addRadioButtonToList();
        Button btnConfirm = (Button) view.findViewById(R.id.btn_confirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer dateValue = checkCheckedListDateRb();
                Integer typeValue = checkCheckedListTypeRb();
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

    private void bind(View view) {
        rbDateAll = (RadioButton) view.findViewById(R.id.rb_date_all);
        rbDateCurrentMonth = (RadioButton) view.findViewById(R.id.rb_date_current_month);
        rbDateLastMonth = (RadioButton) view.findViewById(R.id.rb_date_last_month);
        rbDateLastTwoMonth = (RadioButton) view.findViewById(R.id.rb_date_last_two_month);
        rbDateLastThreeMonth = (RadioButton) view.findViewById(R.id.rb_date_last_three_month);
        rbDateLastSixMonth = (RadioButton) view.findViewById(R.id.rb_date_last_six_month);
        rbTypeAll = (RadioButton) view.findViewById(R.id.rb_type_all);
        rbTypeWorkReportWeek = (RadioButton) view.findViewById(R.id.rb_type_work_report_week);
        rbTypeWorkReportMonth = (RadioButton) view.findViewById(R.id.rb_type_month);

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
        listType.add(rbTypeWorkReportWeek);
        listType.add(rbTypeWorkReportMonth);
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

    /**
     * @param list 清除所有选中的radiobutton
     */
    private void clearListCheckedRb(List<RadioButton> list) {
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setChecked(false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        handler.removeCallbacksAndMessages(null);
    }
}