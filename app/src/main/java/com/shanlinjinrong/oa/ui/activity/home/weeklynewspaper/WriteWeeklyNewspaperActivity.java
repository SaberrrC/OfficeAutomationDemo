package com.shanlinjinrong.oa.ui.activity.home.weeklynewspaper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.common.ApiJava;
import com.shanlinjinrong.oa.common.Constants;
import com.shanlinjinrong.oa.manager.AppConfig;
import com.shanlinjinrong.oa.manager.AppManager;
import com.shanlinjinrong.oa.ui.activity.home.weeklynewspaper.adapter.NextWeekWorkContentAdapter;
import com.shanlinjinrong.oa.ui.activity.home.weeklynewspaper.adapter.ThisWeekWorkContentAdapter;
import com.shanlinjinrong.oa.ui.activity.home.weeklynewspaper.bean.LastWeekPlanBean;
import com.shanlinjinrong.oa.ui.activity.home.weeklynewspaper.bean.WeekReportItemBean;
import com.shanlinjinrong.oa.ui.activity.home.weeklynewspaper.bean.WorkContentBean;
import com.shanlinjinrong.oa.ui.activity.home.weeklynewspaper.bean.WorkStateTipNotifyChangeEvent;
import com.shanlinjinrong.oa.ui.activity.home.weeklynewspaper.contract.WriteWeeklyNewspaperActivityContract;
import com.shanlinjinrong.oa.ui.activity.home.weeklynewspaper.presenter.WriteWeeklyNewspaperActivityPresenter;
import com.shanlinjinrong.oa.ui.activity.home.weeklynewspaper.widget.WeekReportDecorationLine;
import com.shanlinjinrong.oa.ui.activity.home.workreport.SelectContactActivity;
import com.shanlinjinrong.oa.ui.activity.home.workreport.WorkReportCheckActivity;
import com.shanlinjinrong.oa.ui.base.HttpBaseActivity;
import com.shanlinjinrong.oa.utils.DateUtils;
import com.shanlinjinrong.oa.views.AllRecyclerView;
import com.shanlinjinrong.oa.views.AutoResizeTextView;
import com.shanlinjinrong.pickerview.OptionsPickerView;
import com.shanlinjinrong.views.common.CommonTopView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.http.HttpParams;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.DatePicker;

import static com.shanlinjinrong.oa.R.id.btn_add_this_week_work;
import static com.shanlinjinrong.oa.ui.activity.home.workreport.WorkReportLaunchActivity.SELECT_OK;

/**
 * 发起周报
 */
public class WriteWeeklyNewspaperActivity extends HttpBaseActivity<WriteWeeklyNewspaperActivityPresenter> implements WriteWeeklyNewspaperActivityContract.View {
    /**
     * 0：提交周报  1：编辑更新周报 2：审核周报
     */
    public static int FUNCTION_COMMIT = 0;
    public static int FUNCTION_EDIT = 1;
    public static int FUNCTION_EVALUATION = 2;

    @BindView(R.id.top_view)
    CommonTopView mTopView;

    @BindView(R.id.tv_date)
    AutoResizeTextView mTvDate;

    @BindView(R.id.rv_work_content)
    AllRecyclerView mRvWorkContent;

    @BindView(R.id.ll_next_work_content)
    LinearLayout mLlNextWorkContent;

    @BindView(R.id.rv_next_work_content)
    AllRecyclerView mRvNextWorkContent;

    @BindView(R.id.tv_receiver)
    TextView mTvReceiver;

    @BindView(R.id.btn_add_this_week_work)
    TextView mAddThisWeekWorkBtn;

    @BindView(R.id.btn_add_next_week_work)
    TextView mAddNextWeekWorkBtn;

    @BindView(R.id.ll_week_report_evaluation)
    LinearLayout mLlWeekEvaluation;

    @BindView(R.id.rl_report_date)
    RelativeLayout mRlReportDateEva;

    @BindView(R.id.ll_select_date)
    RelativeLayout mSelectDate;

    @BindView(R.id.tv_report_date)
    TextView mTvDateEva;

    @BindView(R.id.et_work_report_evaluation)
    EditText mEtWorkReportEvaluation;

    @BindView(R.id.ll_select_receiver)
    RelativeLayout mSelectReceiver;

    @BindView(R.id.report_scroll_view)
    ScrollView mScroll;

    private DatePicker picker;
    private int workPlanIndex;
    private int workContentIndex;
    private String mCurrentDate;
    private List<String> beginTimeList;
    private ThisWeekWorkContentAdapter mAdapter;
    private NextWeekWorkContentAdapter mNextAdapter;
    private WorkContentBean mWorkContentBean;
    private List<WorkContentBean> mData = new ArrayList<>();
    private List<WorkContentBean> mNextData = new ArrayList<>();
    /**
     * 记录每个账号的填写情况
     */
    private SharedPreferences mSharedPreferences;
    private OptionsPickerView beginTimeView;

    /**
     * 接收人ID
     */
    private String mReceiverId;

    /**
     * 接收人名称
     */
    private String mReceiverName;

    /**
     * 接收人ID
     */
    private String mReceiverPost;

    private String mUserName;
    private boolean hasEvaluation = false;
    private int mDailyId;

    private int mFunction = FUNCTION_COMMIT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_weekly_newspaper);
        ButterKnife.bind(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mUserName = extras.getString("user_name");
            hasEvaluation = extras.getBoolean(WorkReportCheckActivity.HAS_EVALUATION, false);
            mDailyId = extras.getInt("dailyid");
            mFunction = extras.getInt("function");
        }

        if (mFunction == FUNCTION_COMMIT) {
            mPresenter.getLastWeek();
            initData();
            initView();
        } else if (mFunction == FUNCTION_EDIT) {
            mScroll.setVisibility(View.GONE);
            mTopView.setRightText("更新");
            mTopView.setAppTitle(mUserName + "工作周报");
            mPresenter.getWeReportData(mDailyId);
            showLoadingView();
        } else {
            mScroll.setVisibility(View.GONE);
            mSelectDate.setVisibility(View.GONE);
            mAddThisWeekWorkBtn.setVisibility(View.GONE);
            mAddNextWeekWorkBtn.setVisibility(View.GONE);
            mSelectReceiver.setVisibility(View.GONE);
            mLlWeekEvaluation.setVisibility(View.VISIBLE);
            mRlReportDateEva.setVisibility(View.VISIBLE);
            mTopView.setAppTitle(mUserName + "工作周报");

            if (hasEvaluation) {
                mEtWorkReportEvaluation.setEnabled(false);
                mTopView.setRightVisible(View.GONE);
            }
            mPresenter.getWeReportData(mDailyId);
            showLoadingView();
        }


    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    private void initData() {
        mSharedPreferences = getSharedPreferences(AppConfig.getAppConfig(AppManager.sharedInstance()).getPrivateCode() +
                Constants.WORK_WEEKLY_TEMP_DATA, Context.MODE_PRIVATE);
        int workContentSize = mSharedPreferences.getInt("workContentSize", 4);
        int workPlanSize = mSharedPreferences.getInt("workPlanSize", 4);
        initListData(workContentSize, workPlanSize);
    }

    private void putInt(String key, int value) {
        if (mSharedPreferences == null) {
            mSharedPreferences = getSharedPreferences(AppConfig.getAppConfig(AppManager.sharedInstance()).getPrivateCode() +
                    Constants.WORK_WEEKLY_TEMP_DATA, Context.MODE_PRIVATE);
        }
        mSharedPreferences.edit().putInt(key, value).apply();
    }

    private void putString(String key, String value) {
        if (mSharedPreferences == null) {
            mSharedPreferences = getSharedPreferences(AppConfig.getAppConfig(AppManager.sharedInstance()).getPrivateCode() +
                    Constants.WORK_WEEKLY_TEMP_DATA, Context.MODE_PRIVATE);
        }
        mSharedPreferences.edit().putString(key, value).apply();
    }

    private void initListData(int workContentSize, int workPlanSize) {
        //周报日期 数据初始化
        List<String> mondayData1 = DateUtils.getMondayData1("yyyy-MM-dd");
        beginTimeList = new ArrayList<>();
        int size = mondayData1.size();
        for (int i = 0; i < size; i++) {
            beginTimeList.add(mondayData1.get(size - i - 1));
        }
        for (int i = 0; i < workContentSize; i++) {
            mWorkContentBean = new WorkContentBean();
            mWorkContentBean.setState("未填写");
            workContentIndex = i + 1;
            mWorkContentBean.setTitle("工作内容 " + workContentIndex);
            mData.add(mWorkContentBean);
        }

        for (int i = 0; i < workPlanSize; i++) {
            workPlanIndex = i + 1;
            mWorkContentBean = new WorkContentBean();
            mWorkContentBean.setState("未填写");
            workPlanIndex = i + 1;
            mWorkContentBean.setTitle("工作计划 " + workPlanIndex);
            mNextData.add(mWorkContentBean);
        }
    }

    private void setData(WeekReportItemBean weekReportItem) {
        int workContentSize = weekReportItem.getWeeklySummary().size();
        int workPlanSize = weekReportItem.getWeekPlane().size();

        putInt("workContentSize", workContentSize);
        putInt("workPlanSize", workPlanSize);

        //周报日期 数据初始化
        initListData(workContentSize, workPlanSize);
    }

    private void initView() {
        for (int i = 0; i < mData.size(); i++) {
            String this_work_content_state = mSharedPreferences.getString(mData.get(i).getTitle() + "this_work_content_state", "2");
            if ("0".equals(this_work_content_state)) {
                mData.get(i).setState("已填写");
            } else if ("1".equals(this_work_content_state)) {
                mData.get(i).setState("待完善");
            } else {
                mData.get(i).setState("未填写");
            }
        }

        mTvDate.setText(DateUtils.getCurrentWeek("至", "yyyy-MM-dd"));
        mAdapter = new ThisWeekWorkContentAdapter(mData);
        mRvWorkContent.setLayoutManager(new LinearLayoutManager(this) {
                                            @Override
                                            public boolean canScrollVertically() {
                                                return false;
                                            }
                                        }
        );
        mRvWorkContent.setAdapter(mAdapter);
        mRvWorkContent.addItemDecoration(new WeekReportDecorationLine(this, mData));
        mRvWorkContent.requestLayout();
        mAdapter.notifyDataSetChanged();

        mRvWorkContent.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                WorkContentBean workContentBean = mData.get(i);
                Intent intent = new Intent(WriteWeeklyNewspaperActivity.this, WeeklyWorkContentActivity.class);
                intent.putExtra("TopTitle", workContentBean.getTitle());
                intent.putExtra("isWorkContent", true);
                intent.putExtra("index", i);
                startActivity(intent);
            }
        });

        for (int i = 0; i < mNextData.size(); i++) {
            String this_work_content_state = mSharedPreferences.getString(mNextData.get(i).getTitle() + "this_work_plan_state", "2");
            if ("0".equals(this_work_content_state)) {
                mNextData.get(i).setState("已填写");
            } else if ("1".equals(this_work_content_state)) {
                mNextData.get(i).setState("待完善");
            } else {
                mNextData.get(i).setState("未填写");
            }
        }
        mRvNextWorkContent.setLayoutManager(new LinearLayoutManager(this) {
                                                @Override
                                                public boolean canScrollVertically() {
                                                    return false;
                                                }
                                            }
        );
        mNextAdapter = new NextWeekWorkContentAdapter(mNextData);
        mRvNextWorkContent.setAdapter(mNextAdapter);
        mRvNextWorkContent.addItemDecoration(new WeekReportDecorationLine(this, mNextData));
        mRvWorkContent.requestLayout();
        mNextAdapter.notifyDataSetChanged();

        mRvNextWorkContent.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                WorkContentBean workContentBean = mNextData.get(i);
                Intent intent = new Intent(WriteWeeklyNewspaperActivity.this, WeeklyWorkContentActivity.class);
                intent.putExtra("TopTitle", workContentBean.getTitle());
                intent.putExtra("isWorkContent", false);
                intent.putExtra("index", i);
                startActivity(intent);
            }
        });

        mPresenter.getDefaultReceiver();
    }

    private void setEvaluationView(WeekReportItemBean weekReportItem) {
        for (int i = 0; i < mData.size(); i++) {
            int state = weekReportItem.getWeeklySummary().get(i).getWriteState();
            if (state == 0) {
                mData.get(i).setState("已填写");
            } else if (state == 1) {
                mData.get(i).setState("待完善");
            } else {
                mData.get(i).setState("未填写");
            }

            putString(mData.get(i).getTitle() + "work_plan", weekReportItem.getWeeklySummary().get(i).getWorkPlan());
            putString(mData.get(i).getTitle() + "practical_work", weekReportItem.getWeeklySummary().get(i).getWork());
            putString(mData.get(i).getTitle() + "work_analyzes", weekReportItem.getWeeklySummary().get(i).getDifference());
            putString(mData.get(i).getTitle() + "work_remark", weekReportItem.getWeeklySummary().get(i).getRemark());
        }

        String startTime = DateUtils.longToDateString(weekReportItem.getStartTime(), "yyyy-MM-dd");
        String endTime = DateUtils.longToDateString(weekReportItem.getEndTime(), "yyyy-MM-dd");
        mTvDateEva.setText(startTime + "至" + endTime);
        mTvDate.setText(startTime + "至" + endTime);

        mReceiverId = weekReportItem.getCheckmanId() + "";
        mReceiverName = weekReportItem.getCheckman();
        mReceiverPost = weekReportItem.getPostName();
        mTvReceiver.setText(mReceiverName + "-" + mReceiverPost);

        mEtWorkReportEvaluation.setText(weekReportItem.getCheckmainRating() == null ? "" : weekReportItem.getCheckmainRating());

        mAdapter = new ThisWeekWorkContentAdapter(mData);
        mRvWorkContent.setLayoutManager(new LinearLayoutManager(this) {
                                            @Override
                                            public boolean canScrollVertically() {
                                                return false;
                                            }
                                        }
        );
        mRvWorkContent.setAdapter(mAdapter);
        mRvWorkContent.requestLayout();
        mAdapter.notifyDataSetChanged();

        mRvWorkContent.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                WorkContentBean workContentBean = mData.get(i);
                Intent intent = new Intent(WriteWeeklyNewspaperActivity.this, WeeklyWorkContentActivity.class);
                intent.putExtra("TopTitle", workContentBean.getTitle());
                intent.putExtra("isWorkContent", true);
                intent.putExtra("index", i);
                if (mFunction == FUNCTION_EVALUATION) {
                    intent.putExtra("isEditTextEnabled", true);
                }
                startActivity(intent);
            }
        });

        for (int i = 0; i < mNextData.size(); i++) {
            int state = weekReportItem.getWeekPlane().get(i).getWriteState();
            if (state == 0) {
                mNextData.get(i).setState("已填写");
            } else if (state == 1) {
                mNextData.get(i).setState("待完善");
            } else {
                mNextData.get(i).setState("未填写");
            }
            putString(mNextData.get(i).getTitle() + "next_work", weekReportItem.getWeekPlane().get(i).getNextWorkPlan());
            putString(mNextData.get(i).getTitle() + "personLiable", weekReportItem.getWeekPlane().get(i).getPersonLiable());
            putString(mNextData.get(i).getTitle() + "remark", weekReportItem.getWeekPlane().get(i).getRemark());
        }
        mRvNextWorkContent.setLayoutManager(new LinearLayoutManager(this) {
                                                @Override
                                                public boolean canScrollVertically() {
                                                    return false;
                                                }
                                            }
        );
        mNextAdapter = new NextWeekWorkContentAdapter(mNextData);
        mRvNextWorkContent.setAdapter(mNextAdapter);
        mRvWorkContent.requestLayout();
        mNextAdapter.notifyDataSetChanged();

        mRvNextWorkContent.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                WorkContentBean workContentBean = mNextData.get(i);
                Intent intent = new Intent(WriteWeeklyNewspaperActivity.this, WeeklyWorkContentActivity.class);
                intent.putExtra("TopTitle", workContentBean.getTitle());
                intent.putExtra("isWorkContent", false);
                intent.putExtra("index", i);
                if (mFunction == FUNCTION_EVALUATION) {
                    intent.putExtra("isEditTextEnabled", true);
                }
                startActivity(intent);
            }
        });
    }

    @OnClick({R.id.ll_select_date, btn_add_this_week_work, R.id.btn_add_next_week_work, R.id.ll_select_receiver, R.id.topview_right_view, R.id.top_view})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.topview_right_view:
                if (mFunction == FUNCTION_COMMIT) {
                    //提交周报
                    if (checkData())
                        mPresenter.addWeekReport(initHttpParams());
                } else if (mFunction == FUNCTION_EDIT) {
                    //编辑周报
                    if (checkData())
                        mPresenter.updateWeekReport(initHttpParams());
                } else if (mFunction == FUNCTION_EVALUATION) {
                    //审核周报
                    mPresenter.evaluationReport(mDailyId, mEtWorkReportEvaluation.getText().toString().trim());
                }


                break;
            case R.id.ll_select_date:
                showBeginTimeView();
                break;
            case btn_add_this_week_work:
                if (workContentIndex < 9) {
                    workContentIndex++;
                    //记录每个账号的填写情况
                    SharedPreferences.Editor workContentEdit = getSharedPreferences(AppConfig.getAppConfig(AppManager.
                            sharedInstance()).getPrivateCode() + Constants.WORK_WEEKLY_TEMP_DATA, Context.MODE_PRIVATE).edit();
                    workContentEdit.putInt("workContentSize", workContentIndex);
                    workContentEdit.apply();
                    mWorkContentBean = new WorkContentBean("工作内容 " + workContentIndex, "未填写");
                    mData.add(mWorkContentBean);
                    mAdapter.setNewData(mData);
                    mRvWorkContent.requestLayout();
                    mAdapter.notifyDataSetChanged();
                    mRvWorkContent.scrollToPosition(mData.size() - 1);
                } else {
                    Toast.makeText(this, "添加工作已达上限！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_add_next_week_work:
                if (mNextData.size() >= 9) {
                    Toast.makeText(this, "添加计划已达上限！", Toast.LENGTH_SHORT).show();
                } else {
                    workPlanIndex++;
                    //记录每个账号的填写情况
                    SharedPreferences.Editor workPlanEdit = getSharedPreferences(AppConfig.getAppConfig(AppManager
                            .sharedInstance()).getPrivateCode() + Constants.WORK_WEEKLY_TEMP_DATA, Context.MODE_PRIVATE).edit();
                    workPlanEdit.putInt("workPlanSize", workPlanIndex);
                    workPlanEdit.apply();
                    mWorkContentBean = new WorkContentBean("工作计划 " + workPlanIndex, "未填写");
                    mNextData.add(mWorkContentBean);
                    mNextAdapter.setNewData(mNextData);
                    mRvWorkContent.requestLayout();
                    mNextAdapter.notifyDataSetChanged();
                    mScroll.fullScroll(ScrollView.FOCUS_DOWN);
                }
                break;
            case R.id.ll_select_receiver:
                Intent intent = new Intent(this, SelectContactActivity.class);
                intent.putExtra("childId", mReceiverId);
                startActivityForResult(intent, SELECT_OK);
                break;
        }
    }

    private boolean checkData() {
        if ("请选择".equals(mTvReceiver.getText().toString())) {
            Toast.makeText(this, "请选择接收人", Toast.LENGTH_SHORT).show();
            return false;
        }
        for (int i = 0; i < mData.size(); i++) {
            if ("待完善".equals(mData.get(i).getState())) {
                Toast.makeText(this, "当前有未填写汇报内容,请检查", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        for (int i = 0; i < mNextData.size(); i++) {
            if ("待完善".equals(mNextData.get(i).getState())) {
                Toast.makeText(this, "当前有未填写汇报内容,请检查", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        if (!"已填写".equals(mData.get(0).getState())) {
            Toast.makeText(this, "请填写本周工作", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!"已填写".equals(mNextData.get(0).getState())) {
            Toast.makeText(this, "请填写下周计划", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private HttpParams initHttpParams() {
        HttpParams httpParams = new HttpParams();
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        int workContentSize = mSharedPreferences.getInt("workContentSize", 4);
        int workPlanSize = mSharedPreferences.getInt("workPlanSize", 4);
        for (int i = 0; i < workContentSize; i++) {
            JSONObject jsonWeeklySummary = new JSONObject();
            String work_plan = mSharedPreferences.getString(mData.get(i).getTitle() + "work_plan", "");
            String practical_work = mSharedPreferences.getString(mData.get(i).getTitle() + "practical_work", "");
            String work_analyzes = mSharedPreferences.getString(mData.get(i).getTitle() + "work_analyzes", "");
            String work_remark = mSharedPreferences.getString(mData.get(i).getTitle() + "work_remark", "");
            try {
                jsonWeeklySummary.put("difference", work_analyzes);
                jsonWeeklySummary.put("remark", work_remark);
                jsonWeeklySummary.put("work", practical_work);
                jsonWeeklySummary.put("workPlan", work_plan);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonArray.put(jsonWeeklySummary);
        }
        JSONArray jsonArray1 = new JSONArray();
        for (int i = 0; i < workPlanSize; i++) {
            JSONObject jsonWeekPlane = new JSONObject();
            String work_plan = mSharedPreferences.getString(mNextData.get(i).getTitle() + "next_work", "");
            String practical_work = mSharedPreferences.getString(mNextData.get(i).getTitle() + "personLiable", "");
            String work_remark = mSharedPreferences.getString(mNextData.get(i).getTitle() + "remark", "");
            try {
                jsonWeekPlane.put("nextWorkPlan", work_plan);
                jsonWeekPlane.put("personLiable", practical_work);
                jsonWeekPlane.put("remark", work_remark);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonArray1.put(jsonWeekPlane);
        }
        try {
            String Date = mTvDate.getText().toString();
            String startTime = Date.substring(0, 10);
            String endTime = Date.substring(11, Date.length());
            jsonObject.put("startTime", startTime);
            jsonObject.put("endTime", endTime);
            jsonObject.put("checkman", mReceiverName);
            jsonObject.put("checkmanId", mReceiverId);
            jsonObject.put("weeklySummary", jsonArray);
            jsonObject.put("nextWeekPlane", jsonArray1);
            if (mFunction == FUNCTION_EDIT) {
                jsonObject.put("id", mDailyId);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        httpParams.putJsonParams(jsonObject.toString());
        return httpParams;
    }

    @Override
    public void uidNull(String code) {
        catchWarningByCode(code);
    }

    @Override
    public void showLoading() {
        showLoadingView();
    }

    @Override
    public void requestFinish() {
        hideLoadingView();
    }

    @Override
    public void getDefaultReceiverSuccess(String id, String name, String post) {
        mReceiverId = id;
        mReceiverName = name;
        mReceiverPost = post;
        mTvReceiver.setText(name + "-" + post);
    }

    @Override
    public void getDefaultReceiverFailed(String errMsg) {
        if ("auth error".equals(errMsg)) {
            catchWarningByCode(ApiJava.REQUEST_TOKEN_NOT_EXIST);
        }
    }

    @Override
    public void getDefaultReceiverEmpty(String msg) {

    }

    @Override
    public void getLastWeekPlanSuccess(List<LastWeekPlanBean.DataBean> data) {
        try {
            initLastData(data);
            changeWeekContent(data);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void changeWeekContent(List<LastWeekPlanBean.DataBean> data) {
        for (int i = 0; i < data.size(); i++) {
            SharedPreferences.Editor edit = getSharedPreferences(AppConfig.getAppConfig(AppManager.sharedInstance()).getPrivateCode() +
                    Constants.WORK_WEEKLY_TEMP_DATA, Context.MODE_PRIVATE).edit();
            String nextWorkPlan = data.get(i).getNextWorkPlan();
            String remark = data.get(i).getRemark();
            if (!"".equals(nextWorkPlan.trim()) || !"".equals(remark.trim())) {
                mData.get(i).setState("待完善");
            }
            edit.putString(mData.get(i).getTitle() + "work_plan", data.get(i).getNextWorkPlan());
            edit.putString(mData.get(i).getTitle() + "work_remark", data.get(i).getRemark());
            edit.apply();
        }
        mRvWorkContent.requestLayout();
        mAdapter.notifyDataSetChanged();
    }

    private void initLastData(List<LastWeekPlanBean.DataBean> data) {
        if (data.size() > 4) {
            mData.clear();
            putInt("workContentSize", data.size());
            for (int i = 0; i < data.size(); i++) {
                mWorkContentBean = new WorkContentBean();
                mWorkContentBean.setState("未填写");
                workContentIndex = i + 1;
                mWorkContentBean.setTitle("工作内容 " + workContentIndex);
                mData.add(mWorkContentBean);
            }
        }
    }

    @Override
    public void getLastWeekPlanFailure(int code, String msg) {
        if ("auth error".equals(msg)) {
            catchWarningByCode(ApiJava.REQUEST_TOKEN_NOT_EXIST);
        }
    }

    @Override
    public void sendWeeklyReportSuccess(String msg) {
        Toast.makeText(this, getString(R.string.work_weekly_send_success), Toast.LENGTH_SHORT).show();
        clearMemory();
        finish();
    }

    private void clearMemory() {
        SharedPreferences.Editor edit = getSharedPreferences(AppConfig.getAppConfig(AppManager.
                sharedInstance()).getPrivateCode() + Constants.WORK_WEEKLY_TEMP_DATA, Context.MODE_PRIVATE).edit();
        edit.clear();
        edit.apply();
    }

    @Override
    public void sendWeeklyReportFailure(String code, String msg) {
        if ("auth error".equals(msg)) {
            catchWarningByCode(ApiJava.REQUEST_TOKEN_NOT_EXIST);
            return;
        }
        switch (code) {
            case ApiJava.REQUEST_HAD_REPORTED:
                Toast.makeText(this, getString(R.string.work_report_current_date_had_report), Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(this, getString(R.string.work_weekly_send_failed), Toast.LENGTH_SHORT).show();
                break;
        }
        hideLoadingView();
    }

    @Override
    public void sendWeeklyReportFinish() {
        hideLoadingView();
    }

    @Override
    public void getReportSuccess(WeekReportItemBean weekReportItem) {
        mScroll.setVisibility(View.VISIBLE);
        setData(weekReportItem);
        setEvaluationView(weekReportItem);
    }

    @Override
    public void getReportFailed(String code, String msg) {
        if ("auth error".equals(msg)) {
            catchWarningByCode(ApiJava.REQUEST_TOKEN_NOT_EXIST);
            return;
        }
        showToast("获取周报信息失败！");
        finish();
    }

    @Override
    public void evaluationReportSuccess() {
        showToast("汇报已审批成功！");
        setFinishResult();
    }

    @Override
    public void evaluationReportFailed(String code, String msg) {
        showToast("汇报审核失败！");
    }

    @Override
    public void updateWeekReportSuccess() {
        showToast("更新周报成功！");
        setFinishResult();
    }

    @Override
    public void updateWeekReportFailed(String code, String msg) {
        showToast("更新周报失败！");
    }

    private void setFinishResult() {
        Intent intent = new Intent();
        intent.putExtra("evaluation_ok", true);
        intent.putExtra("refresh_data", true);
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * 时间选择器
     */
    private void showBeginTimeView() {
        beginTimeView = new OptionsPickerView.Builder(this,
                new OptionsPickerView.OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                    }
                }).isDialog(true).build();
        beginTimeView.setTitle("请选择发送时间");


        beginTimeView.setPicker(this.beginTimeList);//添加数据
        beginTimeView.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //当前时间
                String currentTime = WriteWeeklyNewspaperActivity.this.beginTimeList.get(options1);

                mTvDate.setText(DateUtils.getDateWeek(currentTime, "至", "yyyy-MM-dd"));
            }
        });
        beginTimeView.setSelectOptions(beginTimeList.size());
        beginTimeView.setCyclic(false);
        beginTimeView.show();
    }

    /**
     * 选择日期底部弹出窗
     */
    private void showDoneDatePicker(final TextView tv) {
        if (picker == null) {
            picker = new DatePicker(this, DatePicker.YEAR_MONTH);
        }
        Calendar cal = Calendar.getInstance();
        if (!TextUtils.isEmpty(tv.getText().toString())) {
            String[] srtArr = tv.getText().toString().split("-");
            picker.setSelectedItem(Integer.valueOf(srtArr[0]), Integer.valueOf(srtArr[1]), Integer.valueOf(srtArr[2]));
        } else {
            picker.setSelectedItem(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1,
                    cal.get(Calendar.DAY_OF_MONTH));
        }
        picker.setSubmitText("确认");
        picker.setSubmitTextColor(Color.parseColor("#2d9dff"));
        picker.setTextColor(Color.parseColor("#2d9dff"));
        picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {
                mCurrentDate = year + "-" + month + "-" + day;
                tv.setText(mCurrentDate);
            }
        });
        picker.show();
    }

    /**
     * 周报填写情况刷新
     *
     * @param state
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void WorkStateNotifyChange(WorkStateTipNotifyChangeEvent state) {
        for (int i = 0; i < mData.size(); i++) {
            if (state.getPosition().equals(mData.get(i).getTitle())) {
                if (state.isWorkContent()) {
                    mData.get(i).setState(state.getState());
                    mAdapter.setNewData(mData);
                    mRvWorkContent.requestLayout();
                    mAdapter.notifyDataSetChanged();
                }
            }
        }
        for (int i = 0; i < mNextData.size(); i++) {
            if (state.getPosition().equals(mNextData.get(i).getTitle())) {
                if (!state.isWorkContent()) {
                    mNextData.get(i).setState(state.getState());
                    mNextAdapter.setNewData(mNextData);
                    mRvWorkContent.requestLayout();
                    mNextAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    /**
     * 选择接收人返回
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == requestCode && resultCode == RESULT_OK) {
            mReceiverId = data.getStringExtra("uid");
            mReceiverName = data.getStringExtra("name");
            mReceiverPost = data.getStringExtra("post");
            mTvReceiver.setText(mReceiverName + "-" + mReceiverPost);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        clearMemory();
    }

    @Override
    public void onBackPressed() {
        boolean isFunction = mFunction == FUNCTION_COMMIT && (!"未填写".equals(mData.get(0).getState()) || !"未填写".equals(mNextData.get(0).getState()));
        if (isFunction) {
            showBackTip("是否放弃编辑", "确定", "取消");
        } else {
            finish();
        }
    }
}
