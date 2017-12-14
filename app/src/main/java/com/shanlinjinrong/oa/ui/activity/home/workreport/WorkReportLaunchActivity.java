package com.shanlinjinrong.oa.ui.activity.home.workreport;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.common.Api;
import com.shanlinjinrong.oa.common.ApiJava;
import com.shanlinjinrong.oa.common.Constants;
import com.shanlinjinrong.oa.ui.activity.home.workreport.adapter.DecorationLine;
import com.shanlinjinrong.oa.ui.activity.home.workreport.adapter.WorkReportLaunchListAdapter;
import com.shanlinjinrong.oa.ui.activity.home.workreport.bean.HourReportBean;
import com.shanlinjinrong.oa.ui.activity.home.workreport.bean.LaunchReportItem;
import com.shanlinjinrong.oa.ui.activity.home.workreport.contract.WorkReportLaunchActivityContract;
import com.shanlinjinrong.oa.ui.activity.home.workreport.presenter.WorkReportLaunchActivityPresenter;
import com.shanlinjinrong.oa.ui.base.HttpBaseActivity;
import com.shanlinjinrong.oa.utils.DateUtils;
import com.shanlinjinrong.oa.utils.EmojiFilter;
import com.shanlinjinrong.oa.views.AllRecyclerView;
import com.shanlinjinrong.oa.views.DatePicker;
import com.shanlinjinrong.oa.views.KeyboardLinearLayout;
import com.shanlinjinrong.views.common.CommonTopView;

import org.kymjs.kjframe.http.HttpParams;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * create by lvdinghao
 * 发起日报 页面
 */
public class WorkReportLaunchActivity extends HttpBaseActivity<WorkReportLaunchActivityPresenter> implements WorkReportLaunchActivityContract.View, WorkReportLaunchListAdapter.OnItemClickListener {

    // TODO: 2017/8/21 之前字段，先保留，应该不用多选
    public static final int REQUEST_CODE_MULTIPLE = 1;//多选，接收人

    public static final int WRITE_REPORT_OK = 100;//填写日报
    public static final int SELECT_OK = 101;//选择成功，requestcode


    @BindView(R.id.work_report_list)
    AllRecyclerView mWorkReportList;

    @BindView(R.id.tv_date)
    TextView mDate; // 日期

    @BindView(R.id.top_view)
    CommonTopView mTopView;//标题栏

    @BindView(R.id.tv_receiver)
    TextView mReceiver;//接收人

    @BindView(R.id.et_tomorrow_plan)
    EditText mTomorrowPlan;//明日计划

    @BindView(R.id.report_scroll_view)
    ScrollView mScroll;


    @BindView(R.id.layout_root)
    KeyboardLinearLayout mRootLayout;

    private List<HourReportBean> mHourReportData;

    private List<LaunchReportItem> mWorkReportListData;//日报列表数据

    private DatePicker picker;

    private String currentDate;//当前年月日
    private WorkReportLaunchListAdapter mWorkReportListAdapter;
    private String mReceiverId; //接收人ID
    private String mReceiverName; //接收人名称
    private String mReceiverPost; //接收人ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_report);
        ButterKnife.bind(this);
        setTranslucentStatus(this);
        initView(savedInstanceState);
        initDefaultReceiver();
    }

    private void initDefaultReceiver() {
        mPresenter.getDefaultReceiver();
    }

    private void initView(Bundle savedInstanceState) {
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
            //初始化空的时候的数据
            mWorkReportListData = initListData();
            mHourReportData = initHourReportData();
            mDate.setText(DateUtils.getTodayDate(false));
        }


        //如果有提交日报失败，保存本地，加载本地数据
        if (hasLocalData()) {
            getLocalData();
        }

        mWorkReportListAdapter = new WorkReportLaunchListAdapter(this, mWorkReportListData);
        mWorkReportList.setAdapter(mWorkReportListAdapter);
        mWorkReportListAdapter.setItemClickListener(this);
        mWorkReportList.addItemDecoration(new DecorationLine(this, mWorkReportListData));

        mTopView.setRightAction(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkDataIsFull()) {
                    return;
                }
                showLoadingView();
                mPresenter.launchWorkReport(createHttpParams());
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

    private List<HourReportBean> initHourReportData() {
        List<HourReportBean> list = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            HourReportBean hourReport = new HourReportBean("", "", "", "");
            list.add(hourReport);
        }
        return list;
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

//        jsonObject.put("thereCipientId", mReceiverId);//接收人
        jsonObject.put("time", mDate.getText().toString());//时间
        jsonObject.put("tomorrowPlan", mTomorrowPlan.getText().toString());//明日计划

        params.putJsonParams(jsonObject.toString());

        Log.i("WorkReport", "WorkReport : " + jsonObject.toString());
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
            if (mWorkReportListData.get(i).getContent().equals("")) {
                Toast.makeText(WorkReportLaunchActivity.this, getString(R.string.work_report_data_write_not_full), Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        if (TextUtils.isEmpty(mTomorrowPlan.getText().toString())) {
            Toast.makeText(WorkReportLaunchActivity.this, getString(R.string.work_report_data_write_not_full), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(mReceiverId) || TextUtils.isEmpty(mReceiverName)) {
            Toast.makeText(WorkReportLaunchActivity.this, getString(R.string.work_report_please_write_receiver), Toast.LENGTH_SHORT).show();
            return false;
        }

        for (HourReportBean bean : mHourReportData) {
            if (bean.checkHasEmpty()) {
                Toast.makeText(WorkReportLaunchActivity.this, getString(R.string.work_report_data_write_not_full), Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        return true;
    }

    /**
     * 初始化整个列表的显示数据
     *
     * @return List<LaunchReportItem>
     */
    private List<LaunchReportItem> initListData() {
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
        listData.add(new LaunchReportItem(getString(R.string.work_report_personal_behavior), "", WorkReportLaunchListAdapter.WRITE_TYPE, true, getString(R.string.work_report_professional_qualities)));
        listData.add(new LaunchReportItem(getString(R.string.work_report_environmental_hygiene), "", WorkReportLaunchListAdapter.WRITE_TYPE));
        listData.add(new LaunchReportItem(getString(R.string.work_report_save), "", WorkReportLaunchListAdapter.WRITE_TYPE));
        listData.add(new LaunchReportItem(getString(R.string.work_report_communication_skills), "", WorkReportLaunchListAdapter.WRITE_TYPE));
        listData.add(new LaunchReportItem(getString(R.string.work_report_appearance), "", WorkReportLaunchListAdapter.WRITE_TYPE));
        listData.add(new LaunchReportItem(getString(R.string.work_report_work_discipline), "", WorkReportLaunchListAdapter.WRITE_TYPE));

        //团队合作
        listData.add(new LaunchReportItem(getString(R.string.work_report_initiative), "", WorkReportLaunchListAdapter.WRITE_TYPE, true, getString(R.string.work_report_teamwork)));
        listData.add(new LaunchReportItem(getString(R.string.work_report_cooperation), "", WorkReportLaunchListAdapter.WRITE_TYPE));
        listData.add(new LaunchReportItem(getString(R.string.work_report_dedication), "", WorkReportLaunchListAdapter.WRITE_TYPE));
        listData.add(new LaunchReportItem(getString(R.string.work_report_obey), "", WorkReportLaunchListAdapter.WRITE_TYPE));

        return listData;
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    public void uidNull(int code) {

    }

    /**
     * 跳转填写时报信息
     */
    private void toWriteHourData(int position) {
        Intent intent = new Intent(WorkReportLaunchActivity.this, WriteWorkReportActivity.class);
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
    public void reportSuccess(String msg) {
        Toast.makeText(this, getString(R.string.work_report_send_sucess), Toast.LENGTH_SHORT).show();
        clearLocalData();
        finish();
    }

    @Override
    public void reportFailed(String errCode, String errMsg) {

        if (errMsg.equals("auth error")) {
            catchWarningByCode(Api.RESPONSES_CODE_UID_NULL);
            return;
        }
        switch (errCode) {
            case ApiJava.REQUEST_HAD_REPORTED:
                Toast.makeText(this, getString(R.string.work_report_current_date_had_report), Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(this, getString(R.string.work_report_send_failed), Toast.LENGTH_SHORT).show();
        }
        saveDataToLocal();
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
        mReceiver.setText(name + "-" + post);
    }


    @Override
    public void getDefaultReceiverFailed(String errMsg) {
//        onBackPressed();
//        Toast.makeText(this, errMsg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 提交失败，保存已填写的数据
     */
    private void saveDataToLocal() {
        SharedPreferences.Editor edit = getSharedPreferences(Constants.WORK_REPORT_TEMP_DATA, Context.MODE_PRIVATE).edit();
        edit.putBoolean("local_data", true);
        for (int i = 0; i < mHourReportData.size(); i++) {
            edit.putString("HourReportData_RealWork" + i, mHourReportData.get(i).getRealWork());
            edit.putString("HourReportData_WorkPlan" + i, mHourReportData.get(i).getWorkPlan());
            edit.putString("HourReportData_SelfEvaluate" + i, mHourReportData.get(i).getSelfEvaluate());
            edit.putString("HourReportData_Quantitative" + i, mHourReportData.get(i).getQuantitative());
        }

        for (int i = 8; i < mWorkReportListData.size(); i++) {
            edit.putString("WorkReportListData" + i, mWorkReportListData.get(i).getContent());
        }

        edit.putString("receiverName", mReceiverName);
        edit.putString("receiverId", mReceiverId);
        edit.putString("receiverPost", mReceiverPost);
        edit.putString("time", mDate.getText().toString());
        edit.putString("tomorrowPlan", mTomorrowPlan.getText().toString());
        edit.apply();
    }

    /**
     * 提交成功后，把保存的数据清除
     */
    private void clearLocalData() {
        SharedPreferences.Editor edit = getSharedPreferences(Constants.WORK_REPORT_TEMP_DATA, Context.MODE_PRIVATE).edit();
        edit.clear();
        edit.apply();
    }

    private void getLocalData() {
        SharedPreferences sp = getSharedPreferences(Constants.WORK_REPORT_TEMP_DATA, Context.MODE_PRIVATE);
        for (int i = 0; i < mHourReportData.size(); i++) {
            HourReportBean bean = mHourReportData.get(i);
            bean.setWorkPlan(sp.getString("HourReportData_WorkPlan" + i, ""));
            bean.setRealWork(sp.getString("HourReportData_RealWork" + i, ""));
            bean.setSelfEvaluate(sp.getString("HourReportData_SelfEvaluate" + i, ""));
            bean.setQuantitative(sp.getString("HourReportData_Quantitative" + i, ""));
            mWorkReportListData.get(i).setContent(getString(R.string.work_report_has_write));
        }

        for (int i = 8; i < mWorkReportListData.size(); i++) {
            mWorkReportListData.get(i).setContent(sp.getString("WorkReportListData" + i, ""));
        }

        mReceiverName = sp.getString("receiverName", mReceiverName);
        mReceiverId = sp.getString("receiverId", mReceiverId);
        mReceiverPost = sp.getString("receiverPost", mReceiverPost);
        mReceiver.setText(mReceiverName + "-" + mReceiverPost);
        mDate.setText(sp.getString("time", ""));
        mTomorrowPlan.setText(sp.getString("tomorrowPlan", ""));
    }

    private boolean hasLocalData() {
        SharedPreferences sp = getSharedPreferences(Constants.WORK_REPORT_TEMP_DATA, Context.MODE_PRIVATE);
        return sp.getBoolean("local_data", false);
    }

    @Override
    public void getDefaultReceiverEmpty(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        showBackTip("是否放弃编辑", "确定", "取消");
    }
}
