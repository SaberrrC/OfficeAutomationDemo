package com.shanlinjinrong.oa.ui.activity.home.workreport;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.ui.activity.home.workreport.adapter.CheckDailyReportAdapter;
import com.shanlinjinrong.oa.ui.activity.home.workreport.adapter.CheckReportDecorationLine;
import com.shanlinjinrong.oa.ui.activity.home.workreport.bean.HourReportBean;
import com.shanlinjinrong.oa.ui.activity.home.workreport.bean.ReportPageItem;
import com.shanlinjinrong.oa.ui.activity.home.workreport.bean.WorkReportBean;
import com.shanlinjinrong.oa.ui.activity.home.workreport.contract.CheckDailyReportContract;
import com.shanlinjinrong.oa.ui.activity.home.workreport.presenter.CheckDailyReportPresenter;
import com.shanlinjinrong.oa.ui.base.HttpBaseActivity;
import com.shanlinjinrong.oa.utils.DateUtils;
import com.shanlinjinrong.oa.views.AllRecyclerView;
import com.shanlinjinrong.utils.DeviceUtil;
import com.shanlinjinrong.views.common.CommonTopView;

import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.http.HttpParams;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.shanlinjinrong.oa.ui.activity.home.workreport.WorkReportLaunchActivity.WRITE_REPORT_OK;

/**
 * Created by 丁 on 2017/9/21.
 * 审核日报页面
 */
public class CheckDailyReportActivity extends HttpBaseActivity<CheckDailyReportPresenter> implements CheckDailyReportContract.View, CheckDailyReportAdapter.OnItemClickListener {

    @Bind(R.id.report_list)
    AllRecyclerView mReportList;

    @Bind(R.id.ll_select_date)
    RelativeLayout mSelectDate; // 选择日期

    @Bind(R.id.tv_date)
    TextView mDate; // 日期

    @Bind(R.id.top_view)
    CommonTopView mTopView;//标题栏


    @Bind(R.id.report_scroll_view)
    ScrollView mScroll;

    @Bind(R.id.tv_one_evaluation)
    TextView mOneEvaluation;

    private List<HourReportBean> mHourReportData;

    private List<ReportPageItem> mWorkReportListData;//日报列表数据


    private CheckDailyReportAdapter mCheckDailyReportAdapter;

    private int dailyId;

    private int workScore;

    private int professionalScore;

    private int teamScore;

    private boolean hasEvaluation = false;//是否评价

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_daily_report);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }


    private void initView() {
        mScroll.setVisibility(View.GONE);
        mReportList.setLayoutManager(new LinearLayoutManager(this));

        dailyId = getIntent().getIntExtra("dailyid", 0);
        mPresenter.loadDailyData(dailyId);
        showLoadingView();
        hasEvaluation = getIntent().getBooleanExtra("has_evaluation", false);
        String userName = getIntent().getStringExtra("user_name");
        mTopView.setAppTitle(userName + getString(R.string.work_report_check_title));
        if (hasEvaluation) {
            mTopView.setRightVisible(View.GONE);
            mOneEvaluation.setVisibility(View.GONE);
            ViewGroup.MarginLayoutParams param = (ViewGroup.MarginLayoutParams) mReportList.getLayoutParams();
            param.topMargin = DeviceUtil.dip2px(this, 10);
            mReportList.setLayoutParams(param);
        } else {
            mTopView.setRightAction(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkRequestData())
                        mPresenter.commitDailyEvaluation(createRequestData());
                }
            });
        }
    }

    private boolean checkRequestData() {
        for (int i = 0; i < mWorkReportListData.size(); i++) {
            if (TextUtils.isEmpty(mWorkReportListData.get(i).getContent())) {
                showToast(getString(R.string.work_report_data_write_not_full));
                return false;
            }
        }


        workScore = Integer.valueOf(mWorkReportListData.get(19).getContent());

        professionalScore = Integer.valueOf(mWorkReportListData.get(20).getContent());

        teamScore = Integer.valueOf(mWorkReportListData.get(21).getContent());

        if (workScore < 0 || workScore > 60) {
            showToast(getString(R.string.work_report_data_work_score_limit));
            return false;
        }

        if (professionalScore < 0 || professionalScore > 20) {
            showToast(getString(R.string.work_report_data_professional_score_limit));
            return false;
        }

        if (teamScore < 0 || teamScore > 20) {
            showToast(getString(R.string.work_report_data_team_score_limit));
            return false;
        }

        for (HourReportBean bean : mHourReportData) {
            if (bean.hasEvaluation()) {
                showToast(getString(R.string.work_report_data_no_evaluation));
                return false;
            }
        }

        return true;
    }

    private HttpParams createRequestData() {
        HttpParams params = new HttpParams();
        JSONObject data = new JSONObject();
        try {
            //检查人评价
            data.put("checkRatingOne", mHourReportData.get(0).getCheckManEvaluate());
            data.put("checkRatingTwo", mHourReportData.get(1).getCheckManEvaluate());
            data.put("checkRatingThree", mHourReportData.get(2).getCheckManEvaluate());
            data.put("checkRatingFour", mHourReportData.get(3).getCheckManEvaluate());
            data.put("checkRatingFive", mHourReportData.get(4).getCheckManEvaluate());
            data.put("checkRatingSix", mHourReportData.get(5).getCheckManEvaluate());
            data.put("checkRatingSeven", mHourReportData.get(6).getCheckManEvaluate());
            data.put("checkRatingEigth", mHourReportData.get(7).getCheckManEvaluate());

            //监督人评价
            data.put("supervisorRatingOne", mHourReportData.get(0).getSupervisorEvaluate());
            data.put("supervisorRatingTwo", mHourReportData.get(1).getSupervisorEvaluate());
            data.put("supervisorRatingThree", mHourReportData.get(2).getSupervisorEvaluate());
            data.put("supervisorRatingFour", mHourReportData.get(3).getSupervisorEvaluate());
            data.put("supervisorRatingFive", mHourReportData.get(4).getSupervisorEvaluate());
            data.put("supervisorRatingSix", mHourReportData.get(5).getSupervisorEvaluate());
            data.put("supervisorRatingSeven", mHourReportData.get(6).getSupervisorEvaluate());
            data.put("supervisorRatingEigth", mHourReportData.get(7).getSupervisorEvaluate());

            //职业素养
            data.put("supervisorBehavior", mWorkReportListData.get(8).getEvaluationSupervisor()); // 个人言行
            data.put("checkBehavior", mWorkReportListData.get(8).getEvaluationCheckMan()); // 个人言行

            data.put("supervisorEnvironMental", mWorkReportListData.get(9).getEvaluationSupervisor());//	环境卫生
            data.put("checkEnvironMental", mWorkReportListData.get(9).getEvaluationCheckMan());//	环境卫生

            data.put("supervisorSave", mWorkReportListData.get(10).getEvaluationSupervisor()); //节约
            data.put("checkSave", mWorkReportListData.get(10).getEvaluationCheckMan()); //节约

            data.put("supervisorCommunication", mWorkReportListData.get(11).getEvaluationSupervisor());// 沟通协调能力
            data.put("checkCommunication", mWorkReportListData.get(11).getEvaluationCheckMan());// 沟通协调能力

            data.put("supervisorAppearance", mWorkReportListData.get(12).getEvaluationSupervisor());// 仪容仪表
            data.put("checkAppearance", mWorkReportListData.get(12).getEvaluationCheckMan());// 仪容仪表

            data.put("supervisorDiscipline", mWorkReportListData.get(13).getEvaluationSupervisor());// 工作纪律
            data.put("checkDiscipline", mWorkReportListData.get(13).getEvaluationCheckMan());// 工作纪律

            //团队合作
            data.put("supervisorJobInitiative", mWorkReportListData.get(14).getEvaluationSupervisor());//	工作主动性
            data.put("checkJobInitiative", mWorkReportListData.get(14).getEvaluationCheckMan());//	工作主动性

            data.put("supervisorCooperation", mWorkReportListData.get(15).getEvaluationSupervisor());// 合作性
            data.put("checkCooperation", mWorkReportListData.get(15).getEvaluationCheckMan());// 合作性

            data.put("supervisorDedicated", mWorkReportListData.get(16).getEvaluationSupervisor());//	 敬业精神
            data.put("checkDedicated", mWorkReportListData.get(16).getEvaluationCheckMan());//	 敬业精神

            data.put("supervisorOrganization", mWorkReportListData.get(17).getEvaluationSupervisor());//服从组织安排
            data.put("checkOrganization", mWorkReportListData.get(17).getEvaluationCheckMan());//服从组织安排


            //工作日志评分
            data.put("worklogScore", workScore);
            //职业素养评分
            data.put("professionalismScore", professionalScore);
            //团队合作评分
            data.put("teamScore", teamScore);

            //    日报id		number
            data.put("dailyId", dailyId);
            //    总分		number
            data.put("totalScore", workScore + professionalScore + teamScore);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        params.putJsonParams(data.toString());
        return params;
    }

    private void setListViewData(WorkReportBean reportData) {
        mHourReportData = initHourReportData(reportData);
        mDate.setText(DateUtils.getTodayDate(false));
        mWorkReportListData = initListData(reportData);
        mCheckDailyReportAdapter = new CheckDailyReportAdapter(this, mWorkReportListData, hasEvaluation);

        mReportList.setAdapter(mCheckDailyReportAdapter);
        mCheckDailyReportAdapter.setItemClickListener(this);
        mReportList.addItemDecoration(new CheckReportDecorationLine(this, mWorkReportListData));
    }


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
    private List<ReportPageItem> initListData(WorkReportBean reportData) {
        List<ReportPageItem> listData = new ArrayList<>();
        //时报 上午九点到12点
        for (int i = 9; i < 12; i++) {
            String title = i + ":00~" + (i + 1) + ":00";
            listData.add(new ReportPageItem(title, getString(R.string.work_report_no_evaluation), CheckDailyReportAdapter.CLICK));
        }

        //下午一点到五点
        for (int i = 13; i < 17; i++) {
            String title = i + ":00~" + (i + 1) + ":00";
            listData.add(new ReportPageItem(title, getString(R.string.work_report_no_evaluation), CheckDailyReportAdapter.CLICK));
        }

        //下午五点到五点半
        listData.add(new ReportPageItem("17:00~17:30", getString(R.string.work_report_no_evaluation), CheckDailyReportAdapter.CLICK));

        //职业素养
        listData.add(new ReportPageItem(getString(R.string.work_report_personal_behavior), reportData.getSelfBehavior(), reportData.getSupervisorBehavior(), reportData.getCheckBehavior(), CheckDailyReportAdapter.WRITE_EVALUATION, true, getString(R.string.work_report_professional_qualities)));
        listData.add(new ReportPageItem(getString(R.string.work_report_environmental_hygiene), reportData.getSelfEnvironmental(), reportData.getSupervisorEnvironMental(), reportData.getCheckEnvironMental(), CheckDailyReportAdapter.WRITE_EVALUATION));
        listData.add(new ReportPageItem(getString(R.string.work_report_save), reportData.getSelfSave(), reportData.getSupervisorSave(), reportData.getCheckSave(), CheckDailyReportAdapter.WRITE_EVALUATION));
        listData.add(new ReportPageItem(getString(R.string.work_report_communication_skills), reportData.getSelfCommunication(), reportData.getSupervisorCommunication(), reportData.getCheckCommunication(), CheckDailyReportAdapter.WRITE_EVALUATION));
        listData.add(new ReportPageItem(getString(R.string.work_report_appearance), reportData.getSelfAppearance(), reportData.getSupervisorAppearance(), reportData.getCheckAppearance(), CheckDailyReportAdapter.WRITE_EVALUATION));
        listData.add(new ReportPageItem(getString(R.string.work_report_work_discipline), reportData.getSelfDiscipline(), reportData.getSupervisorDiscipline(), reportData.getCheckDiscipline(), CheckDailyReportAdapter.WRITE_EVALUATION));

        //团队合作
        listData.add(new ReportPageItem(getString(R.string.work_report_initiative), reportData.getSelfJobInitiative(), reportData.getSupervisorJobInitiative(), reportData.getCheckJobInitiative(), CheckDailyReportAdapter.WRITE_EVALUATION, true, getString(R.string.work_report_teamwork)));
        listData.add(new ReportPageItem(getString(R.string.work_report_cooperation), reportData.getSelfCooperation(), reportData.getSupervisorCooperation(), reportData.getCheckCooperation(), CheckDailyReportAdapter.WRITE_EVALUATION));
        listData.add(new ReportPageItem(getString(R.string.work_report_dedication), reportData.getSelfDedicated(), reportData.getSupervisorDedicated(), reportData.getCheckDedicated(), CheckDailyReportAdapter.WRITE_EVALUATION));
        listData.add(new ReportPageItem(getString(R.string.work_report_obey), reportData.getSelfOrganization(), reportData.getSupervisorOrganization(), reportData.getCheckOrganization(), CheckDailyReportAdapter.WRITE_EVALUATION));


        //工作计划
        listData.add(new ReportPageItem("", reportData.getTomorrowPlan(), CheckDailyReportAdapter.SHOW_PLAN, true, getString(R.string.work_report_plan_work)));

        //工作评分
        listData.add(new ReportPageItem(getString(R.string.work_report_check_work_content), reportData.getWorklogScore(), CheckDailyReportAdapter.WRITE_SCORE, true, getString(R.string.work_report_check_work_score)));
        listData.add(new ReportPageItem(getString(R.string.work_report_check_professional_qualities), reportData.getProfessionalismScore(), CheckDailyReportAdapter.WRITE_SCORE));
        listData.add(new ReportPageItem(getString(R.string.work_report_check_teamwork), reportData.getTeamScore(), CheckDailyReportAdapter.WRITE_SCORE));

        return listData;
    }

    @OnClick({R.id.tv_one_evaluation})
    public void onClick(View v) {
        if (v.getId() == R.id.tv_one_evaluation) {
            doEvaluation();
        }
    }

    /**
     * 一键评价操作
     */
    private void doEvaluation() {
        for (ReportPageItem pageItem : mWorkReportListData) {
            pageItem.setEvaluationSupervisor(getString(R.string.no_problem));
            pageItem.setEvaluationCheckMan(getString(R.string.no_problem));
        }

        for (int i = 0; i < mHourReportData.size(); i++) {
            mHourReportData.get(i).setSupervisorEvaluate(getString(R.string.no_problem));
            mHourReportData.get(i).setCheckManEvaluate(getString(R.string.no_problem));
            if (mHourReportData.get(i).hasEvaluation()) {
                mWorkReportListData.get(i).setContent(getString(R.string.work_report_no_evaluation));
            } else {
                mWorkReportListData.get(i).setContent(getString(R.string.work_report_has_evaluation));
            }
        }
        mCheckDailyReportAdapter.notifyDataSetChanged();
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                mScroll.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        toWriteHourData(position);
    }

    /**
     * 跳转填写时报信息
     */
    private void toWriteHourData(int position) {
        Intent intent = new Intent(CheckDailyReportActivity.this, WriteWorkReportActivity.class);
        Bundle extras = new Bundle();
        extras.putInt("position", position);
        extras.putInt(WriteReportFragment.PAGE_STATUS, hasEvaluation ? WriteReportFragment.READ : WriteReportFragment.EVALUATION);
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
            }
        }
    }

    /**
     * 刷新时报
     */
    private void freshHourReportList() {
        for (int i = 0; i < mHourReportData.size(); i++) {
            if (mHourReportData.get(i).hasEvaluation()) {
                mWorkReportListData.get(i).setContent(getString(R.string.work_report_no_evaluation));
            } else {
                mWorkReportListData.get(i).setContent(getString(R.string.work_report_has_evaluation));
            }
            mCheckDailyReportAdapter.notifyItemChanged(i);
        }

    }

    @Override
    public void loadDataSuccess(WorkReportBean report) {
        setListViewData(report);
        mScroll.setVisibility(View.VISIBLE);
        freshHourReportList();
    }

    @Override
    public void loadDataFailed(String errCode, String errMsg) {
        showToast(getString(R.string.load_report_data_error));
        onBackPressed();
    }

    @Override
    public void loadDataFinish() {
        hideLoadingView();
    }

    @Override
    public void commitSuccess() {
        showToast("汇报已审批成功！");
        setFinishResult();
    }

    private void setFinishResult() {
        Intent intent = new Intent();
        intent.putExtra("evaluation_ok", true);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void commitFailed(String errCode, String errMsg) {
        showToast("汇报审批失败！");
    }


    @Override
    public void uidNull(int code) {

    }
}
