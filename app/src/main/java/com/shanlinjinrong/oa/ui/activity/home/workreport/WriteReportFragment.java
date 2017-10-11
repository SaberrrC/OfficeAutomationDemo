package com.shanlinjinrong.oa.ui.activity.home.workreport;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.ui.activity.home.workreport.bean.HourReportBean;
import com.shanlinjinrong.oa.utils.EmojiFilter;

public class WriteReportFragment extends Fragment implements View.OnClickListener {

    private EditText mPlanWork; //计划工作

    private EditText mRealWork; //实际工作

    private EditText mSelfEvaluate; //自评

    private EditText mQuantitative; //数据量化

    private EditText mCheckManEvaluate; // 检查人评价

    private EditText mSupervisorEvaluate; //监督人评价

    private LinearLayout mLlCheckManEvaluate; // 检查人评价栏

    private LinearLayout mLlSupervisorEvaluate; //监督人评价栏

    private TextView mLastPageBtn;//上一项

    private TextView mNextPageBtn;//下一项

    public static int WRITE = 0;//输入状态
    public static int EVALUATION = 1;//评价状态
    public static int READ = 2;//只读状态

    public static String PAGE_STATUS = "page_status";


    OnFragmentStartChangeListener changeListener;//翻页监听

    OnPageBthClickListener mPageBtnClickListener;//按钮点击监听

    public WriteReportFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_write_report, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initData();
    }

    private void initView(View view) {
        mPlanWork = (EditText) view.findViewById(R.id.et_plan_work);
        mRealWork = (EditText) view.findViewById(R.id.et_real_work);
        mSelfEvaluate = (EditText) view.findViewById(R.id.et_self_evaluate);
        mQuantitative = (EditText) view.findViewById(R.id.et_quantitative);
        mCheckManEvaluate = (EditText) view.findViewById(R.id.et_check_man_evaluate);
        mSupervisorEvaluate = (EditText) view.findViewById(R.id.et_supervisor_evaluate);

        mLlCheckManEvaluate = (LinearLayout) view.findViewById(R.id.ll_check_man_evaluate);
        mLlSupervisorEvaluate = (LinearLayout) view.findViewById(R.id.ll_supervisor_evaluate);

        mLastPageBtn = (TextView) view.findViewById(R.id.last_page);
        mNextPageBtn = (TextView) view.findViewById(R.id.next_page);
        mLastPageBtn.setOnClickListener(this);
        mNextPageBtn.setOnClickListener(this);
        InputFilter[] filters = new InputFilter[]{new EmojiFilter(50)};
        mPlanWork.setFilters(filters);
        mRealWork.setFilters(filters);
        mSelfEvaluate.setFilters(filters);
        mQuantitative.setFilters(filters);
        mCheckManEvaluate.setFilters(filters);
        mSupervisorEvaluate.setFilters(filters);
    }

    private void setPageStatus(int status) {
        if (status == EVALUATION) {
            mPlanWork.setEnabled(false);
            mRealWork.setEnabled(false);
            mSelfEvaluate.setEnabled(false);
            mQuantitative.setEnabled(false);
            mLlCheckManEvaluate.setVisibility(View.VISIBLE);
            mLlSupervisorEvaluate.setVisibility(View.VISIBLE);
        } else if (status == READ) {
            mPlanWork.setEnabled(false);
            mRealWork.setEnabled(false);
            mSelfEvaluate.setEnabled(false);
            mQuantitative.setEnabled(false);
            mSupervisorEvaluate.setEnabled(false);
            mCheckManEvaluate.setEnabled(false);
            mLlCheckManEvaluate.setVisibility(View.VISIBLE);
            mLlSupervisorEvaluate.setVisibility(View.VISIBLE);
        }
    }


    private void initData() {
        Bundle extra = getArguments();
        if (extra != null) {
            HourReportBean hourReportBean = extra.getParcelable("hour_report");
            if (hourReportBean != null) {
                mPlanWork.setText(hourReportBean.getWorkPlan());
                mRealWork.setText(hourReportBean.getRealWork());
                mSelfEvaluate.setText(hourReportBean.getSelfEvaluate());
                mQuantitative.setText(hourReportBean.getQuantitative());
                mCheckManEvaluate.setText(hourReportBean.getCheckManEvaluate());
                mSupervisorEvaluate.setText(hourReportBean.getSupervisorEvaluate());
            }
            int pageStatus = extra.getInt(PAGE_STATUS);

            setPageStatus(pageStatus);

            setBtnBackGround(extra.getInt("page"));
        }
    }

    public WriteReportFragment setChangeListener(OnFragmentStartChangeListener changeListener) {
        this.changeListener = changeListener;
        return this;
    }

    public WriteReportFragment setPageBtnClickListener(OnPageBthClickListener mPageBtnClickListener) {
        this.mPageBtnClickListener = mPageBtnClickListener;
        return this;
    }

    public void fragmentChange(int position) {
        if (changeListener != null && mPlanWork != null && mRealWork != null && mSelfEvaluate != null)
            changeListener.fragmentStartChange(position,
                    mPlanWork.getText().toString().trim(),
                    mRealWork.getText().toString().trim(),
                    mSelfEvaluate.getText().toString().trim(),
                    mQuantitative.getText().toString().trim(),
                    mCheckManEvaluate.getText().toString().trim(),
                    mSupervisorEvaluate.getText().toString().trim());
    }

    //第一页：向前翻按钮不可用，最后一页的下一页按钮不可用
    public void setBtnBackGround(int position) {
        if (mLastPageBtn != null && mNextPageBtn != null) {
            if (position == 0) {
                mLastPageBtn.setEnabled(false);
            }
            if (position == 7) {
                mNextPageBtn.setEnabled(false);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (mPageBtnClickListener == null) {
            return;
        }
        if (v.getId() == R.id.last_page) {
            mPageBtnClickListener.onLastPageClick();
        } else if (v.getId() == R.id.next_page) {
            mPageBtnClickListener.onNextPageClick();
        }
    }

    public interface OnFragmentStartChangeListener {
        void fragmentStartChange(int position, String planWork, String realWork, String selfEvaluate, String quantitative, String checkManEvaluate, String supervisorEvaluate);
    }

    public interface OnPageBthClickListener {
        void onLastPageClick();

        void onNextPageClick();
    }
}
