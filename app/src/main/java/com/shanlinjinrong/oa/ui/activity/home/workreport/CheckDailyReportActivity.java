package com.shanlinjinrong.oa.ui.activity.home.workreport;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
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
import com.shanlinjinrong.views.common.CommonTopView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.DatePicker;

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

    private DatePicker picker;

    private String currentDate;//当前年月日

    private CheckDailyReportAdapter mCheckDailyReportAdapter;

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
        mReportList.setLayoutManager(new LinearLayoutManager(this));
        mTopView.setRightAction(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mPresenter.loadDailyData(52);
    }

    private void setListViewData(WorkReportBean reportData) {
        mHourReportData = initHourReportData(reportData);
        mDate.setText(DateUtils.getTodayDate(false));
        mWorkReportListData = initListData(reportData);
        mCheckDailyReportAdapter = new CheckDailyReportAdapter(this, mWorkReportListData);
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
        listData.add(new ReportPageItem(getString(R.string.work_report_personal_behavior), reportData.getSelfBehavior(), "", CheckDailyReportAdapter.WRITE_EVALUATION, true, getString(R.string.work_report_professional_qualities)));
        listData.add(new ReportPageItem(getString(R.string.work_report_environmental_hygiene), reportData.getSelfEnvironmental(), "", CheckDailyReportAdapter.WRITE_EVALUATION));
        listData.add(new ReportPageItem(getString(R.string.work_report_save), reportData.getSelfSave(), "", CheckDailyReportAdapter.WRITE_EVALUATION));
        listData.add(new ReportPageItem(getString(R.string.work_report_communication_skills), reportData.getSelfCommunication(), "", CheckDailyReportAdapter.WRITE_EVALUATION));
        listData.add(new ReportPageItem(getString(R.string.work_report_appearance), reportData.getSelfAppearance(), "", CheckDailyReportAdapter.WRITE_EVALUATION));
        listData.add(new ReportPageItem(getString(R.string.work_report_work_discipline), reportData.getSelfDiscipline(), "", CheckDailyReportAdapter.WRITE_EVALUATION));

        //团队合作
        listData.add(new ReportPageItem(getString(R.string.work_report_initiative), reportData.getSelfJobInitiative(), "", CheckDailyReportAdapter.WRITE_EVALUATION, true, getString(R.string.work_report_teamwork)));
        listData.add(new ReportPageItem(getString(R.string.work_report_cooperation), reportData.getSelfCooperation(), "", CheckDailyReportAdapter.WRITE_EVALUATION));
        listData.add(new ReportPageItem(getString(R.string.work_report_dedication), reportData.getSelfDedicated(), "", CheckDailyReportAdapter.WRITE_EVALUATION));
        listData.add(new ReportPageItem(getString(R.string.work_report_obey), reportData.getSelfOrganization(), "", CheckDailyReportAdapter.WRITE_EVALUATION));


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
            pageItem.setEvaluation(getString(R.string.no_problem));
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
//        mScroll.scrollTo();
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
        extras.putBoolean("is_from_check_page", true);
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
    }

    @Override
    public void loadDataFailed(int errCode, String errMsg) {

    }

    @Override
    public void loadDataFinish() {

    }

    @Override
    public void loadDataEmpty() {

    }

    @Override
    public void uidNull(int code) {

    }
}
