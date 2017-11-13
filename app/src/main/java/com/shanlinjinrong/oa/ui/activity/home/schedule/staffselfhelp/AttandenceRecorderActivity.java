package com.shanlinjinrong.oa.ui.activity.home.schedule.staffselfhelp;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.retrofit.model.responsebody.CommonAttendanceBean;
import com.example.retrofit.model.responsebody.MyAttandanceResponse;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.ui.base.BaseActivity;
import com.shanlinjinrong.views.common.CommonTopView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

//考勤月历
public class AttandenceRecorderActivity extends BaseActivity {

    @BindView(R.id.top_view)
    CommonTopView mTopView;
    @BindView(R.id.tv_calendar_year)
    TextView mTvCalendarYear;
    @BindView(R.id.recycler_view1)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_empty_attendence)
    TextView mTvEmptyAttendence;

    private AttandenceRecorderAdapter mAdapter;
    private List<MyAttandanceResponse.AllWorkAttendanceListBean> mAllWorkAttendanceList;
    private List<MyAttandanceResponse.AllWorkAttendanceListBean> mData = new ArrayList<>();
    private HashMap<String, CommonAttendanceBean> mHashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attandance_recorder_list);
        ButterKnife.bind(this);
        initData();
        initView();
    }

    private void initData() {
        if (mAllWorkAttendanceList != null) {
            mAllWorkAttendanceList.clear();
        }
        try {
            MyAttandanceResponse attandance = (MyAttandanceResponse) getIntent().getSerializableExtra("attandance");
            if (attandance != null) {
                mAllWorkAttendanceList = attandance.getAllWorkAttendanceList();
                if (mAllWorkAttendanceList != null) {
                    mHashMap = new HashMap<>();
                    for (int i = 0; i < mAllWorkAttendanceList.size(); i++) {
                        CommonAttendanceBean bean = new CommonAttendanceBean();
                        bean.setCalendar(mAllWorkAttendanceList.get(i).getCalendar());
                        bean.setFlg(mAllWorkAttendanceList.get(i).getFlg());
                        bean.setImportsignflag(mAllWorkAttendanceList.get(i).getImportsignflag());
                        bean.setOnebegintime(mAllWorkAttendanceList.get(i).getOnebegintime());
                        bean.setPk_psndoc(mAllWorkAttendanceList.get(i).getPk_psndoc());
                        bean.setPsname(mAllWorkAttendanceList.get(i).getPsname());
                        bean.setSignCause(mAllWorkAttendanceList.get(i).getSignCause());
                        bean.setTbmstatus(mAllWorkAttendanceList.get(i).getTbmstatus());
                        bean.setOnebegintime(mAllWorkAttendanceList.get(i).getOnebegintime());
                        mHashMap.put(mAllWorkAttendanceList.get(i).getCalendar(), bean);
                    }

                    List<MyAttandanceResponse.CdWorkAttendanceListBean> cdWorkAttendanceList = attandance.getCdWorkAttendanceList();
                    if (cdWorkAttendanceList != null) {
                        for (int i = 0; i < cdWorkAttendanceList.size(); i++) {
                            CommonAttendanceBean bean = new CommonAttendanceBean();
                            bean.setCalendar(cdWorkAttendanceList.get(i).getCalendar());
                            bean.setFlg(cdWorkAttendanceList.get(i).getFlg());
                            bean.setImportsignflag(cdWorkAttendanceList.get(i).getImportsignflag());
                            bean.setOnebegintime(cdWorkAttendanceList.get(i).getOnebegintime());
                            bean.setPk_psndoc(cdWorkAttendanceList.get(i).getPk_psndoc());
                            bean.setPsname(cdWorkAttendanceList.get(i).getPsname());
                            bean.setSignCause(cdWorkAttendanceList.get(i).getSignCause());
                            bean.setTbmstatus(cdWorkAttendanceList.get(i).getTbmstatus());
                            bean.setOnebegintime(cdWorkAttendanceList.get(i).getOnebegintime());
                            mHashMap.put(cdWorkAttendanceList.get(i).getCalendar(), bean);
                        }
                    }

                    List<MyAttandanceResponse.KgWorkAttendanceListBean> kgWorkAttendanceList = attandance.getKgWorkAttendanceList();
                    if (kgWorkAttendanceList != null) {
                        for (int i = 0; i < kgWorkAttendanceList.size(); i++) {
                            CommonAttendanceBean bean = new CommonAttendanceBean();
                            bean.setCalendar(kgWorkAttendanceList.get(i).getCalendar());
                            bean.setFlg(kgWorkAttendanceList.get(i).getFlg());
                            bean.setImportsignflag(kgWorkAttendanceList.get(i).getImportsignflag());
                            bean.setOnebegintime(kgWorkAttendanceList.get(i).getOnebegintime());
                            bean.setPk_psndoc(kgWorkAttendanceList.get(i).getPk_psndoc());
                            bean.setPsname(kgWorkAttendanceList.get(i).getPsname());
                            bean.setSignCause(kgWorkAttendanceList.get(i).getSignCause());
                            bean.setTbmstatus(kgWorkAttendanceList.get(i).getTbmstatus());
                            bean.setOnebegintime(kgWorkAttendanceList.get(i).getOnebegintime());
                            mHashMap.put(kgWorkAttendanceList.get(i).getCalendar(), bean);
                        }
                    }

                    List<MyAttandanceResponse.ZtWorkAttendanceListBean> ztWorkAttendanceList = attandance.getZtWorkAttendanceList();
                    if (kgWorkAttendanceList != null) {
                        for (int i = 0; i < ztWorkAttendanceList.size(); i++) {
                            CommonAttendanceBean bean = new CommonAttendanceBean();
                            bean.setCalendar(ztWorkAttendanceList.get(i).getCalendar());
                            bean.setFlg(ztWorkAttendanceList.get(i).getFlg());
                            bean.setImportsignflag(ztWorkAttendanceList.get(i).getImportsignflag());
                            bean.setOnebegintime(ztWorkAttendanceList.get(i).getOnebegintime());
                            bean.setPk_psndoc(ztWorkAttendanceList.get(i).getPk_psndoc());
                            bean.setPsname(ztWorkAttendanceList.get(i).getPsname());
                            bean.setSignCause(ztWorkAttendanceList.get(i).getSignCause());
                            bean.setTbmstatus(ztWorkAttendanceList.get(i).getTbmstatus());
                            bean.setOnebegintime(ztWorkAttendanceList.get(i).getOnebegintime());
                            mHashMap.put(ztWorkAttendanceList.get(i).getCalendar(), bean);
                        }
                    }

                    List<MyAttandanceResponse.JbWorkAttendanceListBean> jbWorkAttendanceList = attandance.getJbWorkAttendanceList();
                    if (kgWorkAttendanceList != null) {
                        for (int i = 0; i < jbWorkAttendanceList.size(); i++) {
                            CommonAttendanceBean bean = new CommonAttendanceBean();
                            bean.setCalendar(jbWorkAttendanceList.get(i).getCalendar());
                            bean.setFlg(jbWorkAttendanceList.get(i).getFlg());
                            bean.setImportsignflag(jbWorkAttendanceList.get(i).getImportsignflag());
                            bean.setOnebegintime(jbWorkAttendanceList.get(i).getOnebegintime());
                            bean.setPk_psndoc(jbWorkAttendanceList.get(i).getPk_psndoc());
                            bean.setPsname(jbWorkAttendanceList.get(i).getPsname());
                            bean.setSignCause(jbWorkAttendanceList.get(i).getSignCause());
                            bean.setTbmstatus(jbWorkAttendanceList.get(i).getTbmstatus());
                            bean.setOnebegintime(jbWorkAttendanceList.get(i).getOnebegintime());
                            mHashMap.put(jbWorkAttendanceList.get(i).getCalendar(), bean);
                        }
                    }

                    List<MyAttandanceResponse.XjWorkAttendanceListBean> xjWorkAttendanceList = attandance.getXjWorkAttendanceList();
                    if (kgWorkAttendanceList != null) {
                        for (int i = 0; i < xjWorkAttendanceList.size(); i++) {
                            CommonAttendanceBean bean = new CommonAttendanceBean();
                            bean.setCalendar(xjWorkAttendanceList.get(i).getCalendar());
                            bean.setFlg(xjWorkAttendanceList.get(i).getFlg());
                            bean.setImportsignflag(xjWorkAttendanceList.get(i).getImportsignflag());
                            bean.setOnebegintime(xjWorkAttendanceList.get(i).getOnebegintime());
                            bean.setPk_psndoc(xjWorkAttendanceList.get(i).getPk_psndoc());
                            bean.setPsname(xjWorkAttendanceList.get(i).getPsname());
                            bean.setSignCause(xjWorkAttendanceList.get(i).getSignCause());
                            bean.setTbmstatus(xjWorkAttendanceList.get(i).getTbmstatus());
                            bean.setOnebegintime(xjWorkAttendanceList.get(i).getOnebegintime());
                            mHashMap.put(xjWorkAttendanceList.get(i).getCalendar(), bean);
                        }
                    }
                }

                for (int i = 0; i < mAllWorkAttendanceList.size(); i++) {
                    try {
                        if (mAllWorkAttendanceList.get(i).getTbmstatus().equals("[出差]"))
                            continue;
                        mData.add(mAllWorkAttendanceList.get(i));
                    } catch (Throwable e) {
                        e.printStackTrace();
                        mData.add(mAllWorkAttendanceList.get(i));
                    }
                }
                if (mAllWorkAttendanceList.size() > 0) {
                    mTvEmptyAttendence.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                }


            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }


    private void initView() {
        mTvCalendarYear.setText("  " + getIntent().getStringExtra("date"));
        View leftView = mTopView.getLeftView();
        leftView.setOnClickListener(view -> {
            finish();
        });
        if (mAllWorkAttendanceList != null) {
            mAdapter = new AttandenceRecorderAdapter(mData);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }
    }

    private class AttandenceRecorderAdapter extends BaseQuickAdapter<MyAttandanceResponse.AllWorkAttendanceListBean> {

        public AttandenceRecorderAdapter(List<MyAttandanceResponse.AllWorkAttendanceListBean> data) {
            super(R.layout.work_month_list_item, data);
        }

        @Override
        protected void convert(BaseViewHolder baseViewHolder, MyAttandanceResponse.AllWorkAttendanceListBean bean) {
            baseViewHolder.setVisible(R.id.ll_currentday_state, true);
            baseViewHolder.setVisible(R.id.tv_empty_layout, false);
            CommonAttendanceBean bean1 = mHashMap.get(bean.getCalendar());
            if (bean1 != null) {
                if (bean1.getSignCause() != null) {
                    if (!TextUtils.isEmpty(bean1.getSignCause())) {
                        baseViewHolder.setVisible(R.id.tv_sign_in, true);
                    } else {
                        baseViewHolder.setVisible(R.id.tv_sign_in, false);
                    }
                }
                baseViewHolder.setText(R.id.tv_name, bean1.getPsname());
                baseViewHolder.setText(R.id.tv_date, bean1.getCalendar());
                baseViewHolder.setText(R.id.tv_gowork_time, bean1.getOnebegintime());
                baseViewHolder.setText(R.id.tv_off_gowork_time, bean1.getTwoendtime());
                baseViewHolder.setText(R.id.tv_state, bean1.getTbmstatus());
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
