package com.shanlinjinrong.oa.ui.activity.home.workreport;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.ui.activity.home.workreport.bean.HourReportBean;

public class WriteReportFragment extends Fragment implements View.OnClickListener {

    private EditText mPlanWork; //计划工作

    private EditText mRealWork; //实际工作

    private EditText mSelfEvaluate; //自评

    private TextView mLastPageBtn;//上一项

    private TextView mNextPageBtn;//下一项

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
        mLastPageBtn = (TextView) view.findViewById(R.id.last_page);
        mNextPageBtn = (TextView) view.findViewById(R.id.next_page);
        mLastPageBtn.setOnClickListener(this);
        mNextPageBtn.setOnClickListener(this);
    }


    private void initData() {
        Bundle extra = getArguments();
        if (extra != null) {
            HourReportBean hourReportBean = extra.getParcelable("hour_report");
            if (hourReportBean != null) {
                mPlanWork.setText(hourReportBean.getWorkPlan());
                mRealWork.setText(hourReportBean.getRealWork());
                mSelfEvaluate.setText(hourReportBean.getSelfEvaluate());
            }
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
        if (changeListener != null)
            changeListener.fragmentStartChange(position, mPlanWork.
                    getText().toString(), mRealWork.getText().toString(), mSelfEvaluate.getText().toString());
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
        void fragmentStartChange(int position, String planWork, String realWork, String selfEvaluate);
    }

    public interface OnPageBthClickListener {
        void onLastPageClick();

        void onNextPageClick();
    }
}
