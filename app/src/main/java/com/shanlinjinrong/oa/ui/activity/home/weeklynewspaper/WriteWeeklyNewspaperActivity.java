package com.shanlinjinrong.oa.ui.activity.home.weeklynewspaper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
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
import com.shanlinjinrong.oa.ui.activity.home.weeklynewspaper.bean.WorkContentBean;
import com.shanlinjinrong.oa.ui.activity.home.weeklynewspaper.bean.WorkStateTipNotifyChangeEvent;
import com.shanlinjinrong.oa.ui.activity.home.weeklynewspaper.contract.WriteWeeklyNewspaperActivityContract;
import com.shanlinjinrong.oa.ui.activity.home.weeklynewspaper.presenter.WriteWeeklyNewspaperActivityPresenter;
import com.shanlinjinrong.oa.ui.activity.home.workreport.SelectContactActivity;
import com.shanlinjinrong.oa.ui.base.HttpBaseActivity;
import com.shanlinjinrong.oa.utils.DateUtils;
import com.shanlinjinrong.oa.views.AllRecyclerView;
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

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.DatePicker;

import static com.shanlinjinrong.oa.ui.activity.home.workreport.WorkReportLaunchActivity.SELECT_OK;

/**
 * 发起周报
 */
public class WriteWeeklyNewspaperActivity extends HttpBaseActivity<WriteWeeklyNewspaperActivityPresenter> implements WriteWeeklyNewspaperActivityContract.View {

    @Bind(R.id.top_view)
    CommonTopView mTopView;
    @Bind(R.id.tv_date)
    TextView mTvDate;
    @Bind(R.id.rv_work_content)
    AllRecyclerView mRvWorkContent;
    @Bind(R.id.ll_next_work_content)
    LinearLayout mLlNextWorkContent;
    @Bind(R.id.rv_next_work_content)
    AllRecyclerView mRvNextWorkContent;
    @Bind(R.id.tv_receiver)
    TextView mTvReceiver;

    private DatePicker picker;
    private int workPlanIndex;
    private int workContentIndex;
    private String mCurrentDate;//当前年月日
    private ArrayList<String> beginTimeList;
    private ThisWeekWorkContentAdapter mAdapter;
    private NextWeekWorkContentAdapter mNextAdapter;
    private WorkContentBean mWorkContentBean;
    private List<WorkContentBean> mData = new ArrayList<>();
    private List<WorkContentBean> mNextData = new ArrayList<>();
    private SharedPreferences mSharedPreferences; //记录每个账号的填写情况
    private OptionsPickerView beginTimeView;

    private String mReceiverId; //接收人ID
    private String mReceiverName; //接收人名称
    private String mReceiverPost; //接收人ID
    private List<String> mondayData1;
    private List<String> mondayData2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_weekly_newspaper);
        ButterKnife.bind(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        initData();
        initView();
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    private void initData() {

        mPresenter.getLastWeek();

        mSharedPreferences = getSharedPreferences(AppConfig.getAppConfig(AppManager.sharedInstance()).getPrivateCode() +
                Constants.WORK_WEEKLY_TEMP_DATA, Context.MODE_PRIVATE);
        int workContentSize = mSharedPreferences.getInt("workContentSize", 4);
        int workPlanSize = mSharedPreferences.getInt("workPlanSize", 4);

        //周报日期 数据初始化
        mondayData1 = DateUtils.getMondayData1("yyyy—MM-dd");
        mondayData2 = DateUtils.getMondayData2("yyyy-MM-dd");

        beginTimeList = new ArrayList<>();
        for (int i = 0; i < mondayData1.size(); i++) {
            beginTimeList.add(mondayData2.get(i));
            beginTimeList.add(0, mondayData1.get(i));
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

    private void initView() {
        for (int i = 0; i < mData.size(); i++) {
            String this_work_content_state = mSharedPreferences.getString(mData.get(i).getTitle() + "this_work_content_state", "2");
            if (this_work_content_state.equals("0")) {
                mData.get(i).setState("已填写");
            } else if (this_work_content_state.equals("1")) {
                mData.get(i).setState("待完善");
            } else {
                mData.get(i).setState("未填写");
            }
        }

        mTvDate.setText(beginTimeList.get(beginTimeList.size() / 2) + "至" + beginTimeList.get(beginTimeList.size() / 2 + 1));
        mAdapter = new ThisWeekWorkContentAdapter(mData);
        mRvWorkContent.setLayoutManager(new LinearLayoutManager(this));
        mRvWorkContent.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        mRvWorkContent.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                WorkContentBean workContentBean = mData.get(i);
                Intent intent = new Intent(WriteWeeklyNewspaperActivity.this, WeeklyWorkContentActivity.class);
                intent.putExtra("TopTitle", workContentBean.getTitle());
                intent.putExtra("isWorkContent", true);
                startActivity(intent);
            }
        });

        for (int i = 0; i < mNextData.size(); i++) {
            String this_work_content_state = mSharedPreferences.getString(mNextData.get(i).getTitle() + "this_work_plan_state", "2");
            if (this_work_content_state.equals("0")) {
                mNextData.get(i).setState("已填写");
            } else if (this_work_content_state.equals("1")) {
                mNextData.get(i).setState("待完善");
            } else {
                mNextData.get(i).setState("未填写");
            }
        }
        mRvNextWorkContent.setLayoutManager(new LinearLayoutManager(this));
        mNextAdapter = new NextWeekWorkContentAdapter(mNextData);
        mRvNextWorkContent.setAdapter(mNextAdapter);
        mNextAdapter.notifyDataSetChanged();

        mRvNextWorkContent.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                WorkContentBean workContentBean = mNextData.get(i);
                Intent intent = new Intent(WriteWeeklyNewspaperActivity.this, WeeklyWorkContentActivity.class);
                intent.putExtra("TopTitle", workContentBean.getTitle());
                intent.putExtra("isWorkContent", false);
                startActivity(intent);
            }
        });

        mPresenter.getDefaultReceiver();
    }

    @OnClick({R.id.ll_select_date, R.id.btn_add_this_week_work, R.id.btn_add_next_week_work, R.id.ll_select_receiver, R.id.topview_right_view})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.topview_right_view:
                if (mTvReceiver.getText().toString().equals("请选择")) {
                    Toast.makeText(this, "请选择接收人", Toast.LENGTH_SHORT).show();
                    return;
                }
                for (int i = 0; i < mData.size(); i++) {
                    if (mData.get(i).getState().equals("待完善")) {
                        //Toast.makeText(this, "\"工作内容 " + (i + 1) + "\"" + "待完善，请完善后发起!", Toast.LENGTH_SHORT).show();
                        Toast.makeText(this, "当前有未填写汇报内容,请检查", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                for (int i = 0; i < mNextData.size(); i++) {
                    if (mNextData.get(i).getState().equals("待完善")) {
                        //Toast.makeText(this, "\"工作计划 " + (i + 1) + "\"" + "待完善，请完善后发起!", Toast.LENGTH_SHORT).show();
                        Toast.makeText(this, "当前有未填写汇报内容,请检查", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                if (!mNextData.get(0).getState().equals("已填写")) {
                    Toast.makeText(this, "请填写下周计划", Toast.LENGTH_SHORT).show();
                    break;
                }
                if (!mData.get(0).getState().equals("已填写")) {
                    Toast.makeText(this, "请填写本周工作", Toast.LENGTH_SHORT).show();
                    break;
                }
                mPresenter.addWeekReport(initHttpParams());
                break;
            case R.id.ll_select_date:
                showBeginTimeView();
                break;
            case R.id.btn_add_this_week_work:
                if (workContentIndex < 8) {
                    workContentIndex++;
                    //记录每个账号的填写情况
                    SharedPreferences.Editor workContentEdit = getSharedPreferences(AppConfig.getAppConfig(AppManager.
                            sharedInstance()).getPrivateCode() + Constants.WORK_WEEKLY_TEMP_DATA, Context.MODE_PRIVATE).edit();
                    workContentEdit.putInt("workContentSize", workContentIndex);
                    workContentEdit.apply();
                    mWorkContentBean = new WorkContentBean("工作内容 " + workContentIndex, "未填写");
                    mData.add(mWorkContentBean);
                    mAdapter.setNewData(mData);
                    mAdapter.notifyDataSetChanged();
                    mRvWorkContent.scrollToPosition(mData.size() - 1);
                } else {
                    Toast.makeText(this, "添加工作已达上限！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_add_next_week_work:
                if (mNextData.size() >= 8) {
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
                    mNextAdapter.notifyDataSetChanged();
                }
                break;
            case R.id.ll_select_receiver:
                Intent intent = new Intent(this, SelectContactActivity.class);
                intent.putExtra("childId", mReceiverId);
                startActivityForResult(intent, SELECT_OK);
                break;
        }
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
                jsonWeeklySummary.put("difference", work_plan);
                jsonWeeklySummary.put("remark", practical_work);
                jsonWeeklySummary.put("work", work_analyzes);
                jsonWeeklySummary.put("workPlan", work_remark);
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
            jsonObject.put("nextWeekPlane", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        httpParams.putJsonParams(jsonObject.toString());
        return httpParams;
    }

    @Override
    public void uidNull(int code) {

    }

    @Override
    public void showLoading() {
        showLoadingView();
    }

    @Override
    public void requestFinish() {

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

    }

    @Override
    public void getDefaultReceiverEmpty(String msg) {

    }

    @Override
    public void sendWeeklyReportSuccess(String msg) {
        Toast.makeText(this, getString(R.string.work_weekly_send_success), Toast.LENGTH_SHORT).show();
        clearMemory();
        onBackPressed();
    }

    private void clearMemory() {
        SharedPreferences.Editor edit = getSharedPreferences(AppConfig.getAppConfig(AppManager.
                sharedInstance()).getPrivateCode() + Constants.WORK_WEEKLY_TEMP_DATA, Context.MODE_PRIVATE).edit();
        edit.clear();
        edit.apply();
    }

    @Override
    public void sendWeeklyReportFailure(String code, String msg) {
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
                //结束时间
                String endTime = null;
                if (beginTimeList.size() - 1 == options1) {
                    endTime = DateUtils.getIntervalDate1(currentTime, 7, "yyyy-MM-dd");
                } else {
                     endTime = WriteWeeklyNewspaperActivity.this.beginTimeList.get(options1 + 1);
                }
                mTvDate.setText(currentTime + "至" + endTime);
            }
        });
        beginTimeView.setSelectOptions(mondayData1.size());
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
                    mAdapter.notifyDataSetChanged();
                }
            }
        }
        for (int i = 0; i < mNextData.size(); i++) {
            if (state.getPosition().equals(mNextData.get(i).getTitle())) {
                if (!state.isWorkContent()) {
                    mNextData.get(i).setState(state.getState());
                    mNextAdapter.setNewData(mNextData);
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
    }
}
