package com.shanlinjinrong.oa.ui.activity.home.schedule.staffselfhelp;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.retrofit.model.responsebody.QueryPayResponse;
import com.iflytek.cloud.thirdparty.V;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.ui.activity.home.schedule.staffselfhelp.contract.PayQueryContract;
import com.shanlinjinrong.oa.ui.activity.home.schedule.staffselfhelp.presenter.PayQueryPresenter;
import com.shanlinjinrong.oa.ui.base.HttpBaseActivity;
import com.shanlinjinrong.oa.utils.YearDateSelected;
import com.shanlinjinrong.oa.utils.YearTimeSelectedListener;
import com.shanlinjinrong.views.common.CommonTopView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

//薪资查询
public class PayQueryActivity extends HttpBaseActivity<PayQueryPresenter> implements PayQueryContract.View, YearTimeSelectedListener {

    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.tv_remark)
    TextView mTvRemark;
    @BindView(R.id.top_view)
    CommonTopView mTopView;
    @BindView(R.id.tv_job_number)
    TextView mTvJobNumber;
    @BindView(R.id.tv_id_number)
    TextView mTvIdNumber;
    @BindView(R.id.tv_date_selected)
    TextView mTvDateSelected;
    @BindView(R.id.tv_work_cycle)
    TextView mTvWorkCycle;
    @BindView(R.id.tv_basic_salary)
    TextView mTvBasicSalary;
    @BindView(R.id.tv_verify_salary)
    TextView mTvVerifySalary;
    @BindView(R.id.tv_other_allowance)
    TextView mTvOtherAllowance;
    @BindView(R.id.tv_logistics_standard)
    TextView mTvLogisticsStandard;
    @BindView(R.id.tv_logistics_num)
    TextView mTvLogisticsNum;
    @BindView(R.id.tv_after_tax_minus)
    TextView mTvAfterTaxMinus;
    @BindView(R.id.tv_reality_salary)
    TextView mTvRealitySalary;
    @BindView(R.id.tv_card_name)
    TextView mTvCardName;
    @BindView(R.id.tv_card_number)
    TextView mTvCardNumber;
    @BindView(R.id.tv_logistics_salary)
    TextView mTvLogisticsSalary;
    @BindView(R.id.tv_year_award)
    TextView mTvYearAward;
    @BindView(R.id.tv_performance_count)
    TextView mTvPerformanceCount;
    @BindView(R.id.tv_other_before_tax_add)
    TextView mTvOtherBeforeTaxAdd;
    @BindView(R.id.tv_check_ninus)
    TextView mTvCheckMinus;
    @BindView(R.id.tv_other_after_tax_minus)
    TextView mTvOtherAfterTaxMinus;
    @BindView(R.id.tv_float_salary)
    TextView mTvFloatSalary;
    @BindView(R.id.tv_should_salary)
    TextView mTvShouldSalary;
    @BindView(R.id.tv_social_security)
    TextView mTvSocialSecurity;
    @BindView(R.id.tv_reserved_funds)
    TextView mTvReservedFunds;
    @BindView(R.id.tv_before_tax_salary)
    TextView mTvBeforeTaxSalary;
    @BindView(R.id.tv_personal_tax)
    TextView mTvPersonalTax;
    @BindView(R.id.tv_after_tax_add)
    TextView mTvAfterTaxAdd;
    @BindView(R.id.ll_commonality_selected)
    LinearLayout mLlCommonalitySelected;
    @BindView(R.id.tv_heating_allowance)
    TextView mTvHeatingAllowance;
    @BindView(R.id.tv_not_network)
    TextView mTvNotNetwork;
    @BindView(R.id.sl_container_layout)
    ScrollView mSlContainer;
    @BindView(R.id.ll_container_layout)
    LinearLayout mLlContainerLayout;

    private List<String> mDate = new ArrayList<>();

    private YearDateSelected mYearDateSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_query);
        ButterKnife.bind(this);
        initData();
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    private void initData() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        mTvDateSelected.setText(simpleDateFormat.format(calendar.getTimeInMillis()));
        String year = simpleDateFormat.format(calendar.getTimeInMillis()).replace("年", "");
        String month = year.replace("月", "");
        mPresenter.payQueryInfo(month);
        mDate.add(simpleDateFormat.format(calendar.getTimeInMillis()));
        for (int i = 0; i < 12; i++) {
            calendar.add(Calendar.MONTH, -1);
            String date = simpleDateFormat.format(calendar.getTimeInMillis());
            mDate.add(0, date);
        }
        mYearDateSelected = new YearDateSelected(this, this, mDate, "选择查询时间");
    }

    @Override
    public void uidNull(int code) {
        catchWarningByCode(code);
    }

    @Override
    public void showLoading() {
        showLoadingView();
    }

    @Override
    public void payQueryInfoSuccess(QueryPayResponse bean) {
        mTvNotNetwork.setVisibility(View.GONE);
        mSlContainer.setVisibility(View.VISIBLE);
        mLlContainerLayout.setVisibility(View.VISIBLE);
        if (bean != null) {
            mTvRemark.setText(bean.getRemark());
            mTvName.setText(bean.getUserName());
            mTvCardName.setText(bean.getCardName());
            mTvIdNumber.setText(bean.getIdNumber());
            mTvJobNumber.setText(bean.getJobNumber());
            mTvYearAward.setText(bean.getYearAward());
            mTvWorkCycle.setText(bean.getWorkCycle());
            mTvCheckMinus.setText(bean.getCheckMinus());
            mTvCardNumber.setText(bean.getCardNumber());
            mTvAfterTaxAdd.setText(bean.getAfterTaxAdd());
            mTvBasicSalary.setText(bean.getBasicSalary());
            mTvFloatSalary.setText(bean.getFloatSalary());
            mTvPersonalTax.setText(bean.getPersonalTax());
            mTvShouldSalary.setText(bean.getShouldSalary());
            mTvVerifySalary.setText(bean.getVerifySalary());
            mTvLogisticsNum.setText(bean.getLogisticsNum());
            mTvReservedFunds.setText(bean.getReservedFunds());
            mTvAfterTaxMinus.setText(bean.getAfterTaxMinus());
            mTvRealitySalary.setText(bean.getRealitySalary());
            mTvOtherAllowance.setText(bean.getOtherAllowance());
            mTvSocialSecurity.setText(bean.getSocialSecurity());
            mTvLogisticsSalary.setText(bean.getLogisticsSalary());
            mTvBeforeTaxSalary.setText(bean.getBeforeTaxSalary());
            mTvHeatingAllowance.setText(bean.getHeatingAllowance());
            mTvPerformanceCount.setText(bean.getPerformanceCount());
            mTvOtherBeforeTaxAdd.setText(bean.getOtherBeforeTaxAdd());
            mTvLogisticsStandard.setText(bean.getLogisticsStandard());
            mTvOtherAfterTaxMinus.setText(bean.getOtherAfterTaxMinus());
        }
    }

    @Override
    public void payQueryInfoFailed(int errCode, String msg) {
        mTvNotNetwork.setText(msg);
        mSlContainer.setVisibility(View.GONE);
        mTvNotNetwork.setVisibility(View.VISIBLE);
        mLlContainerLayout.setVisibility(View.GONE);
    }

    @Override
    public void payQueryInfoFinish() {
        hideLoadingView();
    }

    @OnClick(R.id.tv_date_selected)
    public void onViewClicked() {
        mYearDateSelected.showBeginTimeView();
    }

    @Override
    public void onSelected(String date, int position) {
        mTvDateSelected.setText(date);
        String year = date.replace("年", "");
        String selectedDate = year.replace("月", "");
        mPresenter.payQueryInfo(selectedDate);
    }
}
