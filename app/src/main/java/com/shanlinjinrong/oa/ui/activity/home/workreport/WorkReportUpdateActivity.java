package com.shanlinjinrong.oa.ui.activity.home.workreport;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.common.Api;
import com.shanlinjinrong.oa.common.ApiJava;
import com.shanlinjinrong.oa.ui.activity.home.workreport.adapter.DecorationLine;
import com.shanlinjinrong.oa.ui.activity.home.workreport.adapter.WorkReportLaunchListAdapter;
import com.shanlinjinrong.oa.ui.activity.home.workreport.bean.HourReportBean;
import com.shanlinjinrong.oa.ui.activity.home.workreport.bean.LaunchReportItem;
import com.shanlinjinrong.oa.ui.activity.home.workreport.bean.WorkReportBean;
import com.shanlinjinrong.oa.ui.activity.home.workreport.contract.WorkReportUpdateContract;
import com.shanlinjinrong.oa.ui.activity.home.workreport.presenter.WorkReportUpdatePresenter;
import com.shanlinjinrong.oa.ui.base.HttpBaseActivity;
import com.shanlinjinrong.oa.utils.DateUtils;
import com.shanlinjinrong.oa.utils.EmojiFilter;
import com.shanlinjinrong.oa.views.AllRecyclerView;
import com.shanlinjinrong.oa.views.DatePicker;
import com.shanlinjinrong.views.common.CommonTopView;

import org.kymjs.kjframe.http.HttpParams;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * create by lvdinghao
 * 发起日报 页面
 */
public class WorkReportUpdateActivity extends HttpBaseActivity<WorkReportUpdatePresenter> implements WorkReportUpdateContract.View, WorkReportLaunchListAdapter.OnItemClickListener {

    public static final int WRITE_REPORT_OK = 100;//填写日报
    public static final int SELECT_OK = 101;//选择成功，requestcode

    @BindView(R.id.report_scroll_view)
    ScrollView mScrollView;

    @BindView(R.id.work_report_list)
    AllRecyclerView mWorkReportList;

    @BindView(R.id.ll_select_date)
    LinearLayout mSelectDate; // 选择日期

    @BindView(R.id.tv_date)
    TextView mDate; // 日期

    @BindView(R.id.top_view)
    CommonTopView mTopView;//标题栏

    @BindView(R.id.tv_receiver)
    TextView mReceiver;//接收人

    @BindView(R.id.et_tomorrow_plan)
    EditText mTomorrowPlan;//明日计划


    private List<HourReportBean> mHourReportData;

    private List<LaunchReportItem> mWorkReportListData;//日报列表数据

    private DatePicker picker;

    private String currentDate;//当前年月日
    private WorkReportLaunchListAdapter mWorkReportListAdapter;
    private String mReceiverId; //接收人ID
    private String mReceiverName; //接收人名称
    private String mReceiverPost; //接收人ID

    private int mDailyId; //日报ID
    private long lastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_report);
        ButterKnife.bind(this);
        setTranslucentStatus(this);
        initView(savedInstanceState);
    }


    private void initView(Bundle savedInstanceState) {
        mScrollView.setVisibility(View.GONE);
        mWorkReportList.setLayoutManager(new LinearLayoutManager(this));
        if (savedInstanceState != null) {
            mHourReportData = savedInstanceState.getParcelableArrayList("hour_report_list");
            mWorkReportListData = savedInstanceState.getParcelableArrayList("work_report_list");
            mReceiverName = savedInstanceState.getString("receiverName", mReceiverName);
            mReceiverId = savedInstanceState.getString("receiverId", mReceiverId);
            mReceiverPost = savedInstanceState.getString("receiverPost", mReceiverPost);
            mDate.setText(savedInstanceState.getString("time", mDate.getText().toString()));
            mTomorrowPlan.setText(savedInstanceState.getString("tomorrowPlan", mTomorrowPlan.getText().toString()));
        } else {
            //从接口读取数据
            mDailyId = getIntent().getIntExtra("dailyid", 0);
            mPresenter.getReportData(mDailyId);
            showLoadingView();
        }

        mTopView.setAppTitle(getString(R.string.work_report_edit_report_title));
        mTopView.setRightText(getString(R.string.work_report_update_text));
        mTopView.setRightAction(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkDataIsFull()) {
                    return;
                }
                showLoadingView();
                mPresenter.updateReport(createHttpParams());
            }
        });

        mTomorrowPlan.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //请求父容器不拦截view的滑动事件
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.getParent().requestDisallowInterceptTouchEvent(false);
                }
                return false;
            }
        });

        InputFilter[] filters = new InputFilter[]{new EmojiFilter(800)};
        mTomorrowPlan.setFilters(filters);

    }


    private void setListViewData(WorkReportBean reportData) {

        mDate.setText(DateUtils.longToDateString(reportData.getTime(), "yyyy-MM-dd"));//设置日期


        //设置接收人
        mReceiverId = reportData.getCheckManId();
        mReceiverName = reportData.getCheckMan();
        mReceiverPost = reportData.getPostName();
        mReceiver.setText(mReceiverName + "-" + mReceiverPost);

        //明日计划
        mTomorrowPlan.setText(reportData.getTomorrowPlan());

        mWorkReportListData = initListData(reportData);
        mWorkReportListAdapter = new WorkReportLaunchListAdapter(this, mWorkReportListData);
        mWorkReportList.setAdapter(mWorkReportListAdapter);
        mWorkReportListAdapter.setItemClickListener(this);
        mWorkReportList.addItemDecoration(new DecorationLine(this, mWorkReportListData));
        mHourReportData = initHourReportData(reportData);
    }

    /**
     * 创建时报条目数据 对应每个小时的时间
     */
    private List<HourReportBean> initHourReportData(WorkReportBean reportData) {
        List<HourReportBean> list = new ArrayList<>();
        //数据类别 mWorkPlan, mRealWork, mSelfEvaluate, mQuantitative, mCheckManEvaluate, mSupervisorEvaluate
        HourReportBean hourReportOne = new HourReportBean(reportData.getWorkPlanOne(),
                reportData.getWorkOne(), reportData.getSelfRatingOne(), reportData.getWorkResultOne(),
                reportData.getCheckRatingOne(), reportData.getSupervisorRatingOne());
        list.add(hourReportOne);

        HourReportBean hourReportTwo = new HourReportBean(reportData.getWorkPlanTwo(),
                reportData.getWorkTwo(), reportData.getSelfRatingTwo(), reportData.getWorkResultTwo(),
                reportData.getCheckRatingTwo(), reportData.getSupervisorRatingTwo());
        list.add(hourReportTwo);

        HourReportBean hourReportThree = new HourReportBean(reportData.getWorkPlanThree(),
                reportData.getWorkThree(), reportData.getSelfRatingThree(), reportData.getWorkResultThree(),
                reportData.getCheckRatingThree(), reportData.getSupervisorRatingThree());
        list.add(hourReportThree);

        HourReportBean hourReportFour = new HourReportBean(reportData.getWorkPlanFour(),
                reportData.getWorkFour(), reportData.getSelfRatingFour(), reportData.getWorkResultFour(),
                reportData.getCheckRatingFour(), reportData.getSupervisorRatingFour());
        list.add(hourReportFour);


        HourReportBean hourReportFive = new HourReportBean(reportData.getWorkPlanFive(),
                reportData.getWorkFive(), reportData.getSelfRatingFive(), reportData.getWorkResultFive(),
                reportData.getCheckRatingFive(), reportData.getSupervisorRatingFive());
        list.add(hourReportFive);

        HourReportBean hourReportSix = new HourReportBean(reportData.getWorkPlanSix(),
                reportData.getWorkSix(), reportData.getSelfRatingSix(), reportData.getWorkResultSix(),
                reportData.getCheckRatingSix(), reportData.getSupervisorRatingSix());
        list.add(hourReportSix);

        HourReportBean hourReportSeven = new HourReportBean(reportData.getWorkPlanSeven(),
                reportData.getWorkSeven(), reportData.getSelfRatingSeven(), reportData.getWorkResultSeven(),
                reportData.getCheckRatingSeven(), reportData.getSupervisorRatingSeven());
        list.add(hourReportSeven);

        HourReportBean hourReportEight = new HourReportBean(reportData.getWorkPlanEigth(),
                reportData.getWorkEigth(), reportData.getSelfRatingEigth(), reportData.getWorkResultEigth(),
                reportData.getCheckRatingEigth(), reportData.getSupervisorRatingEigth());
        list.add(hourReportEight);


        return list;
    }

    /**
     * 初始化整个列表的显示数据
     *
     * @return List<>
     */
    private List<LaunchReportItem> initListData(WorkReportBean reportData) {
        List<LaunchReportItem> listData = new ArrayList<>();
        //时报 上午九点到12点
        for (int i = 9; i < 12; i++) {
            String title = i + ":00~" + (i + 1) + ":00";
            listData.add(new LaunchReportItem(title, getString(R.string.work_report_no_write), WorkReportLaunchListAdapter.CLICK_TYPE));
        }

        //下午一点到五点
        for (int i = 13; i < 17; i++) {
            String title = i + ":00~" + (i + 1) + ":00";
            listData.add(new LaunchReportItem(title, getString(R.string.work_report_no_write), WorkReportLaunchListAdapter.CLICK_TYPE));
        }

        //下午五点到五点半
        listData.add(new LaunchReportItem("17:00~17:30", getString(R.string.work_report_no_write), WorkReportLaunchListAdapter.CLICK_TYPE));

        //职业素养
        listData.add(new LaunchReportItem(getString(R.string.work_report_personal_behavior), reportData.getSelfBehavior(), WorkReportLaunchListAdapter.WRITE_TYPE, true, getString(R.string.work_report_professional_qualities)));
        listData.add(new LaunchReportItem(getString(R.string.work_report_environmental_hygiene), reportData.getSelfEnvironmental(), WorkReportLaunchListAdapter.WRITE_TYPE));
        listData.add(new LaunchReportItem(getString(R.string.work_report_save), reportData.getSelfSave(), WorkReportLaunchListAdapter.WRITE_TYPE));
        listData.add(new LaunchReportItem(getString(R.string.work_report_communication_skills), reportData.getSelfCommunication(), WorkReportLaunchListAdapter.WRITE_TYPE));
        listData.add(new LaunchReportItem(getString(R.string.work_report_appearance), reportData.getSelfAppearance(), WorkReportLaunchListAdapter.WRITE_TYPE));
        listData.add(new LaunchReportItem(getString(R.string.work_report_work_discipline), reportData.getSelfDiscipline(), WorkReportLaunchListAdapter.WRITE_TYPE));

        //团队合作
        listData.add(new LaunchReportItem(getString(R.string.work_report_initiative), reportData.getSelfJobInitiative(), WorkReportLaunchListAdapter.WRITE_TYPE, true, getString(R.string.work_report_teamwork)));
        listData.add(new LaunchReportItem(getString(R.string.work_report_cooperation), reportData.getSelfCooperation(), WorkReportLaunchListAdapter.WRITE_TYPE));
        listData.add(new LaunchReportItem(getString(R.string.work_report_dedication), reportData.getSelfDedicated(), WorkReportLaunchListAdapter.WRITE_TYPE));
        listData.add(new LaunchReportItem(getString(R.string.work_report_obey), reportData.getSelfOrganization(), WorkReportLaunchListAdapter.WRITE_TYPE));

        return listData;
    }


    /**
     * 构建请求数据
     */
    @NonNull
    private HttpParams createHttpParams() {
        HttpParams params = new HttpParams();
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("checkman", mReceiverName);
        jsonObject.put("checkmanId", Integer.valueOf(mReceiverId));

        //自评
        jsonObject.put("selfRatingOne", mHourReportData.get(0).getSelfEvaluate());
        jsonObject.put("selfRatingTwo", mHourReportData.get(1).getSelfEvaluate());
        jsonObject.put("selfRatingThree", mHourReportData.get(2).getSelfEvaluate());
        jsonObject.put("selfRatingFour", mHourReportData.get(3).getSelfEvaluate());
        jsonObject.put("selfRatingFive", mHourReportData.get(4).getSelfEvaluate());
        jsonObject.put("selfRatingSix", mHourReportData.get(5).getSelfEvaluate());
        jsonObject.put("selfRatingSeven", mHourReportData.get(6).getSelfEvaluate());
        jsonObject.put("selfRatingEigth", mHourReportData.get(7).getSelfEvaluate());

        //计划工作
        jsonObject.put("workPlanOne", mHourReportData.get(0).getWorkPlan());
        jsonObject.put("workPlanTwo", mHourReportData.get(1).getWorkPlan());
        jsonObject.put("workPlanThree", mHourReportData.get(2).getWorkPlan());
        jsonObject.put("workPlanFour", mHourReportData.get(3).getWorkPlan());
        jsonObject.put("workPlanFive", mHourReportData.get(4).getWorkPlan());
        jsonObject.put("workPlanSix", mHourReportData.get(5).getWorkPlan());
        jsonObject.put("workPlanSeven", mHourReportData.get(6).getWorkPlan());
        jsonObject.put("workPlanEigth", mHourReportData.get(7).getWorkPlan());

        //实际工作
        jsonObject.put("workOne", mHourReportData.get(0).getRealWork());
        jsonObject.put("workTwo", mHourReportData.get(1).getRealWork());
        jsonObject.put("workThree", mHourReportData.get(2).getRealWork());
        jsonObject.put("workFour", mHourReportData.get(3).getRealWork());
        jsonObject.put("workFive", mHourReportData.get(4).getRealWork());
        jsonObject.put("workSix", mHourReportData.get(5).getRealWork());
        jsonObject.put("workSeven", mHourReportData.get(6).getRealWork());
        jsonObject.put("workEigth", mHourReportData.get(7).getRealWork());


        //数据量化
        jsonObject.put("workResultOne", mHourReportData.get(0).getQuantitative());
        jsonObject.put("workResultTwo", mHourReportData.get(1).getQuantitative());
        jsonObject.put("workResultThree", mHourReportData.get(2).getQuantitative());
        jsonObject.put("workResultFour", mHourReportData.get(3).getQuantitative());
        jsonObject.put("workResultFive", mHourReportData.get(4).getQuantitative());
        jsonObject.put("workResultSix", mHourReportData.get(5).getQuantitative());
        jsonObject.put("workResultSeven", mHourReportData.get(6).getQuantitative());
        jsonObject.put("workResultEigth", mHourReportData.get(7).getQuantitative());

        //职业素养
        jsonObject.put("selfBehavior", mWorkReportListData.get(8).getContent()); // 个人言行
        jsonObject.put("selfEnvironMental", mWorkReportListData.get(9).getContent());//	环境卫生
        jsonObject.put("selfSave", mWorkReportListData.get(10).getContent()); //节约
        jsonObject.put("selfCommunication", mWorkReportListData.get(11).getContent());// 沟通协调能力
        jsonObject.put("selfAppearance", mWorkReportListData.get(12).getContent());// 仪容仪表
        jsonObject.put("selfDiscipline", mWorkReportListData.get(13).getContent());// 工作纪律

        //团队合作
        jsonObject.put("selfJobInitiative", mWorkReportListData.get(14).getContent());//	工作主动性
        jsonObject.put("selfCooperation", mWorkReportListData.get(15).getContent());// 合作性
        jsonObject.put("selfDedicated", mWorkReportListData.get(16).getContent());//	 敬业精神
        jsonObject.put("selfOrganization", mWorkReportListData.get(17).getContent());//服从组织安排


        jsonObject.put("supervisor", mReceiverName); //监督人
        jsonObject.put("supervisorId", Integer.valueOf(mReceiverId));//监督人id

        jsonObject.put("dailyId", mDailyId);//日报ID
        jsonObject.put("time", mDate.getText().toString());//时间
        jsonObject.put("tomorrowPlan", mTomorrowPlan.getText().toString());//明日计划

        params.putJsonParams(jsonObject.toString());

        return params;
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putParcelableArrayList("hour_report_list", (ArrayList<? extends Parcelable>) mHourReportData);
        outState.putParcelableArrayList("work_report_list", (ArrayList<? extends Parcelable>) mWorkReportListData);
        outState.putString("receiverName", mReceiverName);
        outState.putString("receiverId", mReceiverId);
        outState.putString("receiverPost", mReceiverPost);
        outState.putString("time", mDate.getText().toString());
        outState.putString("tomorrowPlan", mTomorrowPlan.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mHourReportData = savedInstanceState.getParcelableArrayList("hour_report_list");
        mWorkReportListData = savedInstanceState.getParcelableArrayList("work_report_list");
        mReceiverName = savedInstanceState.getString("receiverName", mReceiverName);
        mReceiverId = savedInstanceState.getString("receiverId", mReceiverId);
        mReceiverPost = savedInstanceState.getString("receiverPost", mReceiverPost);
        mDate.setText(savedInstanceState.getString("time", mDate.getText().toString()));
        mTomorrowPlan.setText(savedInstanceState.getString("tomorrowPlan", mTomorrowPlan.getText().toString()));
    }

    //检测数据是否完整
    private boolean checkDataIsFull() {
        for (int i = 0; i < mWorkReportListData.size(); i++) {
            if ("".equals(mWorkReportListData.get(i).getContent())) {
                Toast.makeText(WorkReportUpdateActivity.this, getString(R.string.work_report_data_write_not_full), Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        if (TextUtils.isEmpty(mTomorrowPlan.getText().toString())) {
            Toast.makeText(WorkReportUpdateActivity.this, getString(R.string.work_report_data_write_not_full), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(mReceiverId) || TextUtils.isEmpty(mReceiverName)) {
            Toast.makeText(WorkReportUpdateActivity.this, getString(R.string.work_report_please_write_receiver), Toast.LENGTH_SHORT).show();
            return false;
        }

        for (HourReportBean bean : mHourReportData) {
            if (bean.checkHasEmpty()) {
                Toast.makeText(WorkReportUpdateActivity.this, getString(R.string.work_report_data_write_not_full), Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }


    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    public void uidNull(String code) {
        catchWarningByCode(ApiJava.NOT_EXIST_TOKEN);
    }

    /**
     * 跳转填写时报信息
     */
    private void toWriteHourData(int position) {
        Intent intent = new Intent(WorkReportUpdateActivity.this, WriteWorkReportActivity.class);
        Bundle extras = new Bundle();
        extras.putInt("position", position);
        extras.putString("date", mDate.getText().toString());
        extras.putParcelableArrayList("hour_report_list", (ArrayList<? extends Parcelable>) mHourReportData);
        intent.putExtras(extras);
        startActivityForResult(intent, WRITE_REPORT_OK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            if (bundle == null)
                return;
            if (requestCode == WRITE_REPORT_OK) {
                //填写日报返回
                mHourReportData = bundle.getParcelableArrayList("hour_report_list");
                freshHourReportList();
            } else if (requestCode == SELECT_OK) {
                //选择接收人返回
                mReceiverId = data.getStringExtra("uid");
                mReceiverName = data.getStringExtra("name");
                mReceiverPost = data.getStringExtra("post");
                mReceiver.setText(mReceiverName + "-" + mReceiverPost);
            }
        }
    }

    /**
     * 刷新时报
     */
    private void freshHourReportList() {
        for (int i = 0; i < mHourReportData.size(); i++) {
            if (mHourReportData.get(i).checkAllEmpty()) {
                mWorkReportListData.get(i).setContent(getString(R.string.work_report_no_write));
            } else if (mHourReportData.get(i).checkIsFull()) {
                mWorkReportListData.get(i).setContent(getString(R.string.work_report_has_write));
            } else {
                mWorkReportListData.get(i).setContent(getString(R.string.work_report_need_perfect));
            }
            mWorkReportListAdapter.notifyItemChanged(i);
        }

    }


    /**
     * 选择日期底部弹出窗
     */
    private void showDoneDatePicker(final TextView tv) {
        if (picker == null) {
            picker = new DatePicker(this, true);
        }
        if (!TextUtils.isEmpty(tv.getText().toString())) {
            String[] srtArr = tv.getText().toString().split("-");
            picker.setSelectedItem(Integer.valueOf(srtArr[0]), Integer.valueOf(srtArr[1]), Integer.valueOf(srtArr[2]));
        } else {
            picker.setCurrentDate();
        }
        picker.setSubmitText("确认");
        picker.setSubmitTextColor(Color.parseColor("#2d9dff"));
        picker.setTextColor(Color.parseColor("#2d9dff"));
        picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {
                currentDate = year + "-" + month + "-" + day;
                tv.setText(currentDate);
            }
        });
        picker.show();
    }

    @OnClick({R.id.ll_select_date, R.id.ll_select_receiver})
    public void onClick(View view) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime < 1000) {
            lastClickTime = currentTime;
            return;
        }
        lastClickTime = currentTime;
        switch (view.getId()) {
            case R.id.ll_select_date:
                showDoneDatePicker(mDate);
                break;
            case R.id.ll_select_receiver:
                Intent intent = new Intent(this, SelectContactActivity.class);
                intent.putExtra("childId", mReceiverId);
                startActivityForResult(intent, SELECT_OK);
                break;
        }
    }

    @Override
    public void onItemClick(int position) {
        toWriteHourData(position);
    }


    @Override
    public void getReportSuccess(WorkReportBean workReport) {
        setListViewData(workReport);
        mScrollView.setVisibility(View.VISIBLE);
        freshHourReportList();
    }

    @Override
    public void getReportFailed(String errCode, String errMsg) {
        if ("auth error".equals(errMsg)) {
            catchWarningByCode(ApiJava.NOT_EXIST_TOKEN);
            return;
        }
        showToast(getString(R.string.load_report_data_error));
        onBackPressed();
    }

    @Override
    public void requestFinish() {
        hideLoadingView();
    }

    @Override
    public void updateReportSuccess() {
        showToast(getString(R.string.work_report_update_success));
        setFinishResult();
    }

    private void setFinishResult() {
        Intent intent = new Intent();
        intent.putExtra("refresh_data", true);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void updateReportFailed(String errMsg) {
        if ("auth error".equals(errMsg)){
            catchWarningByCode(ApiJava.NOT_EXIST_TOKEN);
        }
        showToast(getString(R.string.work_report_update_failed));
    }


    @Override
    public void onBackPressed() {
        showBackTip("是否放弃编辑", "确定", "取消");
    }
}
