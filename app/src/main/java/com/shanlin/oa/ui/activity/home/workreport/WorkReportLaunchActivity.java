package com.shanlin.oa.ui.activity.home.workreport;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.shanlin.common.CommonTopView;
import com.shanlin.oa.R;
import com.shanlin.oa.ui.activity.home.workreport.adapter.DecorationLine;
import com.shanlin.oa.ui.activity.home.workreport.adapter.WorkReportListAdapter;
import com.shanlin.oa.ui.activity.home.workreport.bean.HourReportBean;
import com.shanlin.oa.ui.activity.home.workreport.bean.ItemBean;
import com.shanlin.oa.ui.activity.home.workreport.contract.WorkReportLaunchActivityContract;
import com.shanlin.oa.ui.activity.home.workreport.presenter.WorkReportLaunchActivityPresenter;
import com.shanlin.oa.ui.base.MyBaseActivity;
import com.shanlin.oa.utils.DateUtils;
import com.shanlin.oa.views.AllRecyclerView;

import org.kymjs.kjframe.http.HttpParams;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.DatePicker;

/**
 * create by lvdinghao
 * 发起日报 页面
 */
public class WorkReportLaunchActivity extends MyBaseActivity<WorkReportLaunchActivityPresenter> implements WorkReportLaunchActivityContract.View, WorkReportListAdapter.OnItemClickListener {

    // TODO: 2017/8/21 之前字段，先保留，应该不用多选
    public static final int REQUEST_CODE_MULTIPLE = 1;//多选，接收人

    public static final int WRITE_REPORT_OK = 100;//填写日报
    public static final int SELECT_OK = 101;//选择成功，requestcode


    @Bind(R.id.work_report_list)
    AllRecyclerView mWorkReportList;

    @Bind(R.id.ll_select_date)
    RelativeLayout mSelectDate; // 选择日期

    @Bind(R.id.tv_date)
    TextView mDate; // 日期

    @Bind(R.id.top_view)
    CommonTopView mTopView;//标题栏

    @Bind(R.id.tv_receiver)
    TextView mReceiver;//接收人

    @Bind(R.id.et_tomorrow_plan)
    EditText mTomorrowPlan;//明日计划


    private Map<String, HourReportBean> mHourReportData;

    private List<ItemBean> mWorkReportListData;//日报列表数据

    private DatePicker picker;

    private String currentDate;//当前年月日
    private WorkReportListAdapter mWorkReportListAdapter;
    private String mReceiverId; //接收人ID


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_report);
        ButterKnife.bind(this);
        initView();
        initDefaultReceiver();
    }

    private void initDefaultReceiver() {
        showLoadingView();
        mPresenter.getDefaultReceiver();
    }

    private void initView() {
        mWorkReportList.setLayoutManager(new LinearLayoutManager(this));
        mWorkReportListData = initListData();
        mWorkReportListAdapter = new WorkReportListAdapter(this, mWorkReportListData);
        mWorkReportList.setAdapter(mWorkReportListAdapter);
        mWorkReportListAdapter.setItemClickListener(this);
        mWorkReportList.addItemDecoration(new DecorationLine(this, mWorkReportListData));

        mHourReportData = new HashMap<>();

        mDate.setText(DateUtils.getTodayDate(false));

        mTopView.setTopViewFocus();
        mTopView.setRightAction(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkDataIsFull()) {
                    Toast.makeText(WorkReportLaunchActivity.this, "数据不完整", Toast.LENGTH_SHORT).show();
                    return;
                }
                showLoadingView();
                mPresenter.launchWorkReport(createHttpParams());
            }
        });
    }

    /**
     * 构建请求数据
     */
    @NonNull
    private HttpParams createHttpParams() {
        HttpParams params = new HttpParams();
        JSONObject jsonObject = new JSONObject();
//        params.put("checkman", "");
//        params.put("checkmanId", "");

        //自评
        jsonObject.put("selfRatingOne", mHourReportData.get("0").getSelfEvaluate());
        jsonObject.put("selfRatingTwo", mHourReportData.get("1").getSelfEvaluate());
        jsonObject.put("selfRatingThree", mHourReportData.get("2").getSelfEvaluate());
        jsonObject.put("selfRatingFour", mHourReportData.get("3").getSelfEvaluate());
        jsonObject.put("selfRatingFive", mHourReportData.get("4").getSelfEvaluate());
        jsonObject.put("selfRatingSix", mHourReportData.get("5").getSelfEvaluate());
        jsonObject.put("selfRatingSeven", mHourReportData.get("6").getSelfEvaluate());
        jsonObject.put("selfRatingEigth", mHourReportData.get("7").getSelfEvaluate());

        //计划工作
        jsonObject.put("workPlanOne", mHourReportData.get("0").getWorkPlan());
        jsonObject.put("workPlanTwo", mHourReportData.get("1").getWorkPlan());
        jsonObject.put("workPlanThree", mHourReportData.get("2").getWorkPlan());
        jsonObject.put("workPlanFour", mHourReportData.get("3").getWorkPlan());
        jsonObject.put("workPlanFive", mHourReportData.get("4").getWorkPlan());
        jsonObject.put("workPlanSix", mHourReportData.get("5").getWorkPlan());
        jsonObject.put("workPlanSeven", mHourReportData.get("6").getWorkPlan());
        jsonObject.put("workPlanEigth", mHourReportData.get("7").getWorkPlan());

        //实际工作
        jsonObject.put("workOne", mHourReportData.get("0").getRealWork());
        jsonObject.put("workTwo", mHourReportData.get("1").getRealWork());
        jsonObject.put("workThree", mHourReportData.get("2").getRealWork());
        jsonObject.put("workFour", mHourReportData.get("3").getRealWork());
        jsonObject.put("workFive", mHourReportData.get("4").getRealWork());
        jsonObject.put("workSix", mHourReportData.get("5").getRealWork());
        jsonObject.put("workSeven", mHourReportData.get("6").getRealWork());
        jsonObject.put("workEigth", mHourReportData.get("7").getRealWork());

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


//        params.put("supervisor", ""); //监督人
//        params.put("supervisorId", "");//监督人id
        jsonObject.put("thereCipientId", mReceiverId);//接收人
        jsonObject.put("time", mDate.getText().toString());//时间
        jsonObject.put("tomorrowPlan", mTomorrowPlan.getText().toString());//明日计划

        params.putJsonParams(jsonObject.toString());
        return params;
    }

    //检测数据是否完整
    private boolean checkDataIsFull() {
        for (int i = 0; i < mWorkReportListData.size(); i++) {
            if (mWorkReportListData.get(i).getContent().equals("")) {
                return false;
            }
        }
        if (TextUtils.isEmpty(mReceiverId) || TextUtils.isEmpty(mTomorrowPlan.getText().toString())) {
            Toast.makeText(this, "数据不完整", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (mHourReportData.isEmpty() || mHourReportData.size() < 8) {
            return false;
        }

        return true;
    }

    private List<ItemBean> initListData() {
        List<ItemBean> listData = new ArrayList<>();
        //时报 上午九点到12点
        for (int i = 9; i < 12; i++) {
            String title = i + ":00~" + (i + 1) + ":00";
            listData.add(new ItemBean(title, getString(R.string.work_report_no_write), WorkReportListAdapter.CLICK_TYPE));
        }

        //下午一点到五点
        for (int i = 13; i < 17; i++) {
            String title = i + ":00~" + (i + 1) + ":00";
            listData.add(new ItemBean(title, getString(R.string.work_report_no_write), WorkReportListAdapter.CLICK_TYPE));
        }

        //下午五点到五点半
        listData.add(new ItemBean("17:00~17:30", getString(R.string.work_report_no_write), WorkReportListAdapter.CLICK_TYPE));

        //职业素养
        listData.add(new ItemBean(getString(R.string.work_report_personal_behavior), "", WorkReportListAdapter.WRITE_TYPE, true, getString(R.string.work_report_professional_qualities)));
        listData.add(new ItemBean(getString(R.string.work_report_environmental_hygiene), "", WorkReportListAdapter.WRITE_TYPE));
        listData.add(new ItemBean(getString(R.string.work_report_save), "", WorkReportListAdapter.WRITE_TYPE));
        listData.add(new ItemBean(getString(R.string.work_report_communication_skills), "", WorkReportListAdapter.WRITE_TYPE));
        listData.add(new ItemBean(getString(R.string.work_report_appearance), "", WorkReportListAdapter.WRITE_TYPE));
        listData.add(new ItemBean(getString(R.string.work_report_work_discipline), "", WorkReportListAdapter.WRITE_TYPE));

        //团队合作
        listData.add(new ItemBean(getString(R.string.work_report_initiative), "", WorkReportListAdapter.WRITE_TYPE, true, getString(R.string.work_report_teamwork)));
        listData.add(new ItemBean(getString(R.string.work_report_cooperation), "", WorkReportListAdapter.WRITE_TYPE));
        listData.add(new ItemBean(getString(R.string.work_report_dedication), "", WorkReportListAdapter.WRITE_TYPE));
        listData.add(new ItemBean(getString(R.string.work_report_obey), "", WorkReportListAdapter.WRITE_TYPE));

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
        String title = DateUtils.getBiDisplayDateByTimestamp(System.currentTimeMillis()) + " " + mWorkReportListData.get(position).getTitle() + "日报";
        extras.putString("title", title);
        extras.putInt("position", position);
        extras.putParcelable("hour_report", mHourReportData.get(position + ""));
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
                int position = bundle.getInt("position");
                HourReportBean hourReportBean = bundle.getParcelable("hour_report");
                mHourReportData.put(position + "", hourReportBean);
                mWorkReportListData.get(position).setContent(getString(R.string.work_report_has_write));
                mWorkReportListAdapter.notifyItemChanged(position);
            } else if (requestCode == SELECT_OK) {
                mReceiver.setText(data.getStringExtra("name") + "-" + data.getStringExtra("post"));
                mReceiverId = data.getStringExtra("uid");
            }
        }
    }


    /**
     * 选择日期底部弹出窗
     */
    private void showDoneDatePicker(final TextView tv) {
        if (picker == null) {
            picker = new DatePicker(this, DatePicker.YEAR_MONTH_DAY);
        }
        Calendar cal = Calendar.getInstance();
        picker.setSelectedItem(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1,
                cal.get(Calendar.DAY_OF_MONTH));
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
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        onBackPressed();
    }

    @Override
    public void reportFailed(int errCode, String errMsg) {
        Toast.makeText(this, errMsg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void requestFinish() {
        hideLoadingView();
    }

    @Override
    public void getDefaultReceiverSuccess(String id, String name, String post) {
        mReceiverId = id;
        mReceiver.setText(name + "-" + post);
    }


    @Override
    public void getDefaultReceiverFailed(int errCode, String errMsg) {
//        onBackPressed();
        Toast.makeText(this, errMsg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getDefaultReceiverEmpty(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
