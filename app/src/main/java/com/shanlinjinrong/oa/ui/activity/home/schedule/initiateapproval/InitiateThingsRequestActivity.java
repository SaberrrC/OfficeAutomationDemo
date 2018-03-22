package com.shanlinjinrong.oa.ui.activity.home.schedule.initiateapproval;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.ui.activity.home.schedule.initiateapproval.adapter.InitiateThingsTypeAdapter;
import com.shanlinjinrong.oa.ui.activity.home.schedule.initiateapproval.bean.CommonTypeBean;
import com.shanlinjinrong.oa.ui.activity.home.schedule.initiateapproval.bean.Dialog_Common_bean;
import com.shanlinjinrong.oa.ui.activity.home.schedule.initiateapproval.bean.QueryMonoBean;
import com.shanlinjinrong.oa.ui.activity.home.schedule.initiateapproval.bean.SelectedTypeBean;
import com.shanlinjinrong.oa.ui.activity.home.schedule.initiateapproval.bean.SingReasonBean;
import com.shanlinjinrong.oa.ui.activity.home.schedule.initiateapproval.contract.InitiateThingsRequestActivityContract;
import com.shanlinjinrong.oa.ui.activity.home.schedule.initiateapproval.presenter.InitiateThingsRequestActivityPresenter;
import com.shanlinjinrong.oa.ui.activity.home.schedule.initiateapproval.widget.ApproveDecorationLine;
import com.shanlinjinrong.oa.ui.activity.home.workreport.SelectContactActivity;
import com.shanlinjinrong.oa.ui.base.HttpBaseActivity;
import com.shanlinjinrong.oa.utils.CustomDialogUtils;
import com.shanlinjinrong.oa.utils.DateUtils;
import com.shanlinjinrong.oa.utils.TimeDialogFragment;
import com.shanlinjinrong.oa.utils.YearDateSelected;
import com.shanlinjinrong.oa.utils.YearTimeSelectedListener;
import com.shanlinjinrong.views.common.CommonTopView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.http.HttpParams;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.shanlinjinrong.oa.ui.activity.home.workreport.WorkReportLaunchActivity.SELECT_OK;

/**
 * 发起申请
 */
public class InitiateThingsRequestActivity extends HttpBaseActivity<InitiateThingsRequestActivityPresenter> implements View.OnClickListener, InitiateThingsRequestActivityContract.View, YearTimeSelectedListener {

    @BindView(R.id.top_view)
    CommonTopView mTopView;
    @BindView(R.id.tv_coder_number)
    TextView      mTvCoderNumber;
    @BindView(R.id.tv_not_network)
    TextView      mTvNotNetwork;
    @BindView(R.id.tv_request_date)
    TextView      mTvRequestDate;
    @BindView(R.id.btn_add_details)
    TextView      mBtnAddDetails;
    @BindView(R.id.sv_container_show)
    ScrollView    mSvContainerShow;
    @BindView(R.id.ll_container_view)
    LinearLayout  mLlContainerView;
    @BindView(R.id.tv_commonality_type)
    TextView      mTvCommonalityType;
    @BindView(R.id.tv_commonality_coder)
    TextView      mTvCommonalityCoder;
    @BindView(R.id.ll_commonality_type)
    LinearLayout  mLlCommonalityType;
    @BindView(R.id.tv_commonality_type_dot)
    TextView      mTvCommonalityTypeDot;
    @BindView(R.id.tv_commonality_over_time)
    TextView      mTvCommonalityOverTime;
    @BindView(R.id.tv_commonality_type_date)
    TextView      mTvCommonalityTypeDate;
    @BindView(R.id.tv_commonality_request_date)
    TextView      mTvCommonalityRequestDate;
    @BindView(R.id.tv_commonality_type_selected)
    TextView      mTvCommonalityTypeSelected;
    @BindView(R.id.ll_commonality_annual_leave)
    LinearLayout  mLlCommonalityAnnualLeave;

    private View                      mContentView1;
    private CustomDialogUtils         mDialog;
    private CommonTypeBean            mCommonTypeBean;
    private YearDateSelected          mYearDateSelected;
    private InitiateThingsTypeAdapter mTypeAdapter;
    private int mIndex = 1, mYearPosition = 0;
    private boolean isNextDate, isSeletcedNextType;

    private List<String>             mDate = new ArrayList<>();
    //Dialog 数据源
    private List<Dialog_Common_bean> data  = new ArrayList<>();

    //时间选取 -- 时间选择
    private String mBeginDate, mEndDate, mNext_begin_date, mNext_end_date,
            mSelectedTypeID, mSelectedNextID, mQueryDuration = "", mNextDuration = "";

    //6402签卡申请,6403出差申请,6404休假申请,6405加班申请
    private int REQUESTCODE1 = 6402, REQUESTCODE2 = 6403, REQUESTCODE3 = 6404, REQUESTCODE4 = 6405;
    //交接人
    private String mReceiverId = "", mReceiverName, mReceiverPost, mNextReceiverId, mNextReceiverName, mNextReceiverPost;

    private EditText mEt_common_show2, mEt_common_next_show2, mEt_common_show1, mEt_common_next_show1;
    private LinearLayout mLl_common_show1, mLl_common_next_show1, mLl_common_show3, mLl_common_next_show3, mLl_common_end_time,
            mLl_common_next_end_time, mLl_common_duration, mLl_common_next_duration, mLl_common_card_detail, mLl_common_next_card_detail;

    private TextView mTv_common_show2, mTv_common_next_show2, mTv_common_next_show3, mTv_common_show3, mTv_common_duration, mTv_common_next_duration,
            mTv_common_detail, mTv_common_next_detail, mTv_common_begin_time, mTv_common_next_begin_time, mTv_selected_show, mTv_selected_next_show, mBegin_time,
            mNext_begin_time, mEnd_time, mNext_end_time, mEt_common_show3, mEt_common_next_show3, mTv_duration_next_number, mTv_duration_number, mTv_common_next_show2_dot, mTv_common_show2_dot, mTv_common_show3_dot, mTv_common_next_show3_dot;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commonality_initiate_approval);
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
        //获取编码
        initMonoCode();
        //休假年度数据
        if (getIntent().getIntExtra("type", -1) == 2) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            mTvCommonalityTypeDate.setText(simpleDateFormat.format(calendar.getTimeInMillis()));
            mDate.add(simpleDateFormat.format(calendar.getTimeInMillis()));
            calendar.add(Calendar.YEAR, -1);
            String date = simpleDateFormat.format(calendar.getTimeInMillis());
            mDate.add(0, date);
            mYearPosition = mDate.size() - 1;
            mYearDateSelected = new YearDateSelected(this, this, mDate, "选择休假年度");
        }
    }

    //获取编码
    private void initMonoCode() {
        switch (getIntent().getIntExtra("type", 0)) {
            case 0://出差编号
                mPresenter.getQueryMonoCode(REQUESTCODE2);
                break;
            case 1://加班编码
                mPresenter.getQueryMonoCode(REQUESTCODE4);
                break;
            case 2://休假编码
                mPresenter.getQueryMonoCode(REQUESTCODE3);
                break;
            case 3://签卡编码
                mPresenter.getQueryMonoCode(REQUESTCODE1);
                break;
        }

    }

    private void initView() {
        initContentView();
        submitRequest();
    }

    private void initContentView() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date currentDate = new Date(System.currentTimeMillis());
        String currentTime = formatter.format(currentDate);
        mTvRequestDate.setText(currentTime); //申请日期
        View contentView = LayoutInflater.from(this).inflate(R.layout.commonality_initiate_approval_item, null);  //加载初始化布局
        ButterKnife.bind(contentView);
        mLlContainerView.addView(contentView);
        initContentView0(contentView);
        switch (getIntent().getIntExtra("type", -1)) {
            case 0://出差申请
                onBusinessRequest();
                break;
            case 1://加班申请
                overTimeWorkRequest();
                break;
            case 2://休假申请
                annualLeaveRequest();
                break;
            case 3://签卡申请
                registrationCardRequest();
                break;
            default:
                break;
        }
    }

    //提交申请
    private void submitRequest() {
        mTopView.getRightView().setOnClickListener(view -> {

            if (TextUtils.isEmpty(mTvCoderNumber.getText().toString().trim())) {
                initMonoCode();
                showToast("提交失败,请获取" + mTvCommonalityCoder.getText().toString() + "!");
                return;
            }

            if (getIntent().getIntExtra("type", -1) != 3) {
                if (mNext_begin_time != null) {
                    if (this.mQueryDuration.equals("") || this.mQueryDuration.equals("0") || this.mQueryDuration.equals("0小时") || this.mQueryDuration.equals("0天") ||
                            this.mNextDuration.equals("0") || this.mNextDuration.equals("") || this.mNextDuration.equals("0小时") || this.mNextDuration.equals("0天")) {
                        showToast(mTopView.getTitleView().getText().toString() + "时长为0,请重新获取!");
                    }
                }
                if (this.mQueryDuration.equals("") || this.mQueryDuration.equals("0") || this.mQueryDuration.equals("0小时") || this.mQueryDuration.equals("0天")) {
                    showToast(mTopView.getTitleView().getText().toString() + "时长为0,请重新获取!");
                    return;
                }
            }


            switch (getIntent().getIntExtra("type", -1)) {
                case 0://出差申请
                    submitEvectionApply();
                    break;
                case 1://加班申请
                    submitAddWorkApply();
                    break;
                case 2://休假申请
                    submitFurloughApply();
                    break;
                case 3://签卡申请
                    submitRegistrationCard();
                    break;
                default:
                    break;
            }
        });
    }

    //------------------------------------出差提交------------------------------------

    private void submitEvectionApply() {
        if (mBegin_time.getText().toString().trim().equals("请选择开始时间")) {
            showToast(mBegin_time.getText().toString() + "！");
            return;
        }
        if (mEnd_time.getText().toString().trim().equals("请选择结束时间")) {
            showToast(mEnd_time.getText().toString() + "！");
            return;
        }
        if (mEt_common_show1.getText().toString().trim().equals("") && !mEt_common_show1.getHint().toString().trim().equals("")) {
            showToast("请填写出差地点！");
            return;
        }
        try {
            JSONObject jsonObject = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("evectionAddress", mEt_common_show1.getText().toString());
            jsonObject1.put("evectionRemark", mEt_common_show2.getText().toString());
            jsonObject1.put("endTime", mEnd_time.getText().toString() + ":00");
            jsonObject1.put("startTime", mBegin_time.getText().toString() + ":00");
            jsonObject1.put("handOverPepole", mReceiverId);
            jsonObject1.put("timeDifference", mQueryDuration);
            jsonArray.put(jsonObject1);
            if (mIndex > 1) {
                if (mNext_begin_time.getText().toString().trim().equals("请选择开始时间")) {
                    showToast(mNext_begin_time.getText().toString() + "！");
                    return;
                }
                if (mNext_end_time.getText().toString().trim().equals("请选择结束时间")) {
                    showToast(mNext_end_time.getText().toString() + "！");
                    return;
                }
                if (mEt_common_next_show1.getText().toString().trim().equals("") && !mEt_common_next_show1.getHint().toString().trim().equals("")) {
                    showToast("请填写出差地点！");
                    return;
                }
                JSONObject jsonObject2 = new JSONObject();
                jsonObject2.put("evectionAddress", mEt_common_next_show1.getText().toString());
                jsonObject2.put("evectionRemark", mEt_common_next_show2.getText().toString());
                jsonObject2.put("endTime", mNext_end_time.getText().toString() + ":00");
                jsonObject2.put("startTime", mNext_begin_time.getText().toString() + ":00");
                jsonObject2.put("handOverPepole", mNextReceiverId);
                jsonObject2.put("timeDifference", mNextDuration);
                jsonArray.put(jsonObject2);
            }
            jsonObject.put("nchrevectionApplyDetail", jsonArray);
            jsonObject.put("billCode", mTvCoderNumber.getText().toString());
            jsonObject.put("type", mSelectedTypeID);
            jsonObject.put("applyDate", mTvRequestDate.getText().toString());
            HttpParams httpParams = new HttpParams();
            httpParams.putJsonParams(jsonObject.toString());

//            EvectionBody body = new EvectionBody();
//            body.setType(mSelectedTypeID);
//            body.setBillCode(mTvCoderNumber.getText().toString());
//            body.setApplyDate(mTvRequestDate.getText().toString());
//            List<EvectionBody.NchrevectionApplyDetailBean> bean = new ArrayList<>();
//            EvectionBody.NchrevectionApplyDetailBean bean1 = new EvectionBody.NchrevectionApplyDetailBean();
//            bean1.setHandOverPepole(mReceiverId);
//            bean1.setTimeDifference(mQueryDuration);
//            bean1.setEndTime(mEnd_time.getText().toString() + ":00");
//            bean1.setStartTime(mBegin_time.getText().toString() + ":00");
//            bean1.setEvectionRemark(mEt_common_show2.getText().toString());
//            bean1.setEvectionAddress(mEt_common_show1.getText().toString());
//            bean.add(bean1);
//            if (mIndex > 1) {
//                EvectionBody.NchrevectionApplyDetailBean bean2 = new EvectionBody.NchrevectionApplyDetailBean();
//                bean2.setTimeDifference(mNextDuration);
//                bean2.setHandOverPepole(mNextReceiverId);
//                bean2.setEndTime(mNext_end_time.getText().toString() + ":00");
//                bean2.setStartTime(mNext_begin_time.getText().toString() + ":00");
//                bean2.setEvectionRemark(mEt_common_next_show2.getText().toString());
//                bean2.setEvectionAddress(mEt_common_next_show1.getText().toString());
//                bean.add(bean2);
//            }
//            body.setNchrevectionApplyDetail(bean);
            mPresenter.submitEvectionApply(httpParams);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    //*********************************加班提交*********************************
    private void submitAddWorkApply() {
        try {
//            if (mBegin_time.getText().toString().trim().equals("请选择开始时间")) {
//                showToast(mBegin_time.getText().toString() + "！");
//                return;
//            }
//            if (mEnd_time.getText().toString().trim().equals("请选择结束时间")) {
//                showToast(mEnd_time.getText().toString() + "！");
//                return;
//            }
//            if (mEt_common_show2.getText().toString().trim().equals("") && !mEt_common_show2.getHint().toString().trim().equals("")) {
//                showToast("请填写加班原因！");
//                return;
//            }
//
//            AddWorkBody body = new AddWorkBody();
//            List<AddWorkBody.DetailListBean> bean = new ArrayList<>();
//            AddWorkBody.DetailListBean bean1 = new AddWorkBody.DetailListBean();
//
//            bean1.setCause(mEt_common_show2.getText().toString());
//            bean1.setEndTime(mEnd_time.getText().toString() + ":00");
//            bean1.setStartTime(mBegin_time.getText().toString() + ":00");
//            bean1.setTimeDifference(mQueryDuration);
//            if (mIndex > 1) {
//                if (mNext_begin_time.getText().toString().trim().equals("请选择开始时间")) {
//                    showToast(mNext_begin_time.getText().toString() + "！");
//                    return;
//                }
//                if (mNext_end_time.getText().toString().trim().equals("请选择结束时间")) {
//                    showToast(mNext_end_time.getText().toString() + "！");
//                    return;
//                }
//                if (mEt_common_next_show2.getText().toString().trim().equals("") && !mEt_common_next_show2.getHint().toString().trim().equals("")) {
//                    showToast("请填写加班原因！");
//                    return;
//                }
//                AddWorkBody.DetailListBean bean2 = new AddWorkBody.DetailListBean();
//                bean1.setCause(mEt_common_next_show2.getText().toString());
//                bean1.setEndTime(mNext_end_time.getText().toString() + ":00");
//                bean1.setStartTime(mNext_begin_time.getText().toString() + ":00");
//                bean1.setTimeDifference(mNextDuration);
//                bean.add(bean2);
//            }
//            bean.add(bean1);
//            body.setDetailList(bean);
//            body.setType(mSelectedTypeID);
//            body.setMonocode(mTvCoderNumber.getText().toString());
//            mPresenter.addWorkApply(body);
            if (mEt_common_show2.getText().toString().trim().equals("") && !mEt_common_show2.getHint().toString().equals("")) {
                showToast("请填写加班原因！");
                return;
            }
            JSONObject jsonObject = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("cause", mEt_common_show2.getText().toString());
            jsonObject1.put("endTime", mEnd_time.getText().toString() + ":00");
            jsonObject1.put("startTime", mBegin_time.getText().toString() + ":00");
            jsonObject1.put("timeDifference", mQueryDuration);
            jsonArray.put(jsonObject1);
            if (mIndex > 1) {
                if (mNext_begin_time.getText().toString().trim().equals("请选择开始时间")) {
                    showToast(mNext_begin_time.getText().toString() + "！");
                    return;
                }
                if (mNext_end_time.getText().toString().trim().equals("请选择结束时间")) {
                    showToast(mNext_end_time.getText().toString() + "！");
                    return;
                }
                if (mEt_common_next_show2.getText().toString().trim().equals("") && !mEt_common_next_show2.getHint().toString().trim().equals("")) {
                    showToast("请填写加班原因！");
                    return;
                }
                JSONObject jsonObject2 = new JSONObject();
                jsonObject2.put("cause", mEt_common_next_show2.getText().toString());
                jsonObject2.put("endTime", mNext_end_time.getText().toString() + ":00");
                jsonObject2.put("startTime", mNext_begin_time.getText().toString() + ":00");
                jsonObject2.put("timeDifference", mNextDuration);
                jsonArray.put(jsonObject2);
            }
            jsonObject.put("detailList", jsonArray);
            jsonObject.put("monocode", mTvCoderNumber.getText().toString());
            jsonObject.put("type", mSelectedTypeID);
            HttpParams httpParams = new HttpParams();
            httpParams.putJsonParams(jsonObject.toString());
            mPresenter.addWorkApply(httpParams);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    //*********************************休假提交*********************************

    private void submitFurloughApply() {
        try {//休假申请
            if (mBegin_time.getText().toString().trim().equals("请选择开始时间")) {
                showToast(mBegin_time.getText().toString() + "！");
                return;
            }
            if (mEnd_time.getText().toString().trim().equals("请选择结束时间")) {
                showToast(mEnd_time.getText().toString() + "！");
                return;
            }
            JSONObject jsonObject = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("endTime", mEnd_time.getText().toString() + ":00");
            jsonObject1.put("startTime", mBegin_time.getText().toString() + ":00");
            jsonObject1.put("handOverPepole", mReceiverId);
            jsonObject1.put("furloughRemark", mEt_common_show2.getText().toString());
            jsonObject1.put("timeDifference", mQueryDuration);
            jsonArray.put(jsonObject1);
            if (mIndex > 1) {
                if (mNext_begin_time.getText().toString().trim().equals("请选择开始时间")) {
                    showToast(mNext_begin_time.getText().toString() + "！");
                    return;
                }
                if (mNext_end_time.getText().toString().trim().equals("请选择结束时间")) {
                    showToast(mNext_end_time.getText().toString() + "！");
                    return;
                }
                JSONObject jsonObject2 = new JSONObject();
                jsonObject2.put("endTime", mNext_end_time.getText().toString() + ":00");
                jsonObject2.put("startTime", mNext_begin_time.getText().toString() + ":00");
                jsonObject2.put("handOverPepole", mNextReceiverId);
                jsonObject2.put("furloughRemark", mEt_common_next_show2.getText().toString());
                jsonObject2.put("timeDifference", mNextDuration);
                jsonArray.put(jsonObject2);
            }
            jsonObject.put("nchrfurloughApplyDetail", jsonArray);
            jsonObject.put("date", mTvCommonalityTypeDate.getText().toString().trim());
            jsonObject.put("billCode", mTvCoderNumber.getText().toString());
            jsonObject.put("applyDate", mTvRequestDate.getText().toString());
            jsonObject.put("type", mSelectedTypeID);
            HttpParams httpParams = new HttpParams();
            httpParams.putJsonParams(jsonObject.toString());
            mPresenter.submitFurlough(httpParams);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //*********************************签卡提交*********************************

    private void submitRegistrationCard() {
        try {
            if (mBegin_time.getText().toString().trim().equals("请选择开始时间")) {
                showToast("请选择签卡时间！");
                return;
            }
            if (mEt_common_show3.getText().toString().trim().equals("") && !mEt_common_show3.getHint().toString().equals("")) {
                showToast("请填写签卡说明！");
                return;
            }
            JSONObject jsonObject = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("signCause", mTv_selected_show.getText().toString().trim());
            jsonObject1.put("signCauseId", mSelectedTypeID);
            jsonObject1.put("signRemark", mEt_common_show3.getText().toString().trim());
            jsonObject1.put("signTime", mBegin_time.getText().toString() + ":00");
            jsonArray.put(jsonObject1);
            if (mIndex > 1) {
                JSONObject jsonObject2 = new JSONObject();
                if (mNext_begin_time.getText().toString().trim().equals("请选择开始时间")) {
                    showToast("请选择签卡时间！");
                    return;
                }
                if (mEt_common_next_show3.getText().toString().trim().equals("") && !mEt_common_next_show3.getHint().toString().equals("")) {
                    showToast("请填写签卡说明！");
                    return;
                }
                jsonObject2.put("signCause", mTv_selected_next_show.getText().toString().trim());
                jsonObject2.put("signCauseId", mSelectedNextID);
                jsonObject2.put("signRemark", mEt_common_next_show3.getText().toString().trim());
                jsonObject2.put("signTime", mNext_begin_time.getText().toString() + ":00");
                jsonArray.put(jsonObject2);
            }
            jsonObject.put("nchrSignDetails", jsonArray);
            jsonObject.put("monocode", mTvCoderNumber.getText().toString());
            jsonObject.put("date", mTvRequestDate.getText().toString().trim());
            HttpParams httpParams = new HttpParams();
            httpParams.putJsonParams(jsonObject.toString());
            mPresenter.submitRegistrationCard(httpParams);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //*********************************明细1**************************
    private void initContentView0(View contentView) {
        mEnd_time = (TextView) contentView.findViewById(R.id.et_commonality_end_time);
        mTv_common_show2 = (TextView) contentView.findViewById(R.id.tv_commonality_show2);
        mTv_common_show2_dot = (TextView) contentView.findViewById(R.id.tv_commonality_show2_dot);
        mEt_common_show2 = (EditText) contentView.findViewById(R.id.et_commonality_show2);
        mTv_selected_show = (TextView) contentView.findViewById(R.id.tv_selected_show);
        mEt_common_show1 = (EditText) contentView.findViewById(R.id.et_commonality_show1);
        mTv_common_show3 = (TextView) contentView.findViewById(R.id.tv_commonality_show3);
        mTv_common_show3_dot = (TextView) contentView.findViewById(R.id.tv_commonality_show3_dot);
        mEt_common_show3 = (TextView) contentView.findViewById(R.id.et_commonality_show3);
        mBegin_time = (TextView) contentView.findViewById(R.id.et_commonality_begin_time);
        mTv_common_detail = (TextView) contentView.findViewById(R.id.tv_commonality_detail);
        mLl_common_show1 = (LinearLayout) contentView.findViewById(R.id.ll_commonality_show1);
        mLl_common_show3 = (LinearLayout) contentView.findViewById(R.id.ll_commonality_show3);
        mTv_common_duration = (TextView) contentView.findViewById(R.id.tv_commonality_duration);
        mLl_common_end_time = (LinearLayout) contentView.findViewById(R.id.ll_commonality_end_time);
        mLl_common_duration = (LinearLayout) contentView.findViewById(R.id.ll_commonality_duration);
        mTv_common_begin_time = (TextView) contentView.findViewById(R.id.tv_commonality_begin_time);
        mLl_common_end_time = (LinearLayout) contentView.findViewById(R.id.ll_commonality_end_time);
        mLl_common_duration = (LinearLayout) contentView.findViewById(R.id.ll_commonality_duration);
        mTv_common_begin_time = (TextView) contentView.findViewById(R.id.tv_commonality_begin_time);
        mLl_common_card_detail = (LinearLayout) contentView.findViewById(R.id.ll_registration_card_detail);
        mTv_duration_number = (TextView) contentView.findViewById(R.id.tv_commonality_duration_number);

        mEt_common_show2.setFilters(new InputFilter[]{new InputFilter.LengthFilter(50)});
        mEt_common_show3.setFilters(new InputFilter[]{new InputFilter.LengthFilter(50)});
        mEt_common_show1.setFilters(new InputFilter[]{new InputFilter.LengthFilter(50)});
        selectedDate();
    }

    //*********************************明细2*********************************
    private void initContentView1(View contentView) {
        mNext_end_time = (TextView) contentView.findViewById(R.id.et_commonality_end_time);
        mTv_common_next_show2 = (TextView) contentView.findViewById(R.id.tv_commonality_show2);
        mTv_common_next_show2_dot = (TextView) contentView.findViewById(R.id.tv_commonality_show2_dot);
        mEt_common_next_show2 = (EditText) contentView.findViewById(R.id.et_commonality_show2);
        mEt_common_next_show1 = (EditText) contentView.findViewById(R.id.et_commonality_show1);
        mTv_common_next_show3 = (TextView) contentView.findViewById(R.id.tv_commonality_show3);
        mTv_common_next_show3_dot = (TextView) contentView.findViewById(R.id.tv_commonality_show3_dot);
        mTv_selected_next_show = (TextView) contentView.findViewById(R.id.tv_selected_show);
        mEt_common_next_show3 = (TextView) contentView.findViewById(R.id.et_commonality_show3);
        mNext_begin_time = (TextView) contentView.findViewById(R.id.et_commonality_begin_time);
        mTv_common_next_detail = (TextView) contentView.findViewById(R.id.tv_commonality_detail);
        mLl_common_next_show3 = (LinearLayout) contentView.findViewById(R.id.ll_commonality_show3);
        mLl_common_next_show1 = (LinearLayout) contentView.findViewById(R.id.ll_commonality_show1);
        mTv_common_next_duration = (TextView) contentView.findViewById(R.id.tv_commonality_duration);
        mLl_common_next_end_time = (LinearLayout) contentView.findViewById(R.id.ll_commonality_end_time);
        mLl_common_next_duration = (LinearLayout) contentView.findViewById(R.id.ll_commonality_duration);
        mTv_common_next_begin_time = (TextView) contentView.findViewById(R.id.tv_commonality_begin_time);
        mLl_common_next_end_time = (LinearLayout) contentView.findViewById(R.id.ll_commonality_end_time);
        mLl_common_next_duration = (LinearLayout) contentView.findViewById(R.id.ll_commonality_duration);
        mTv_common_next_begin_time = (TextView) contentView.findViewById(R.id.tv_commonality_begin_time);
        mLl_common_next_card_detail = (LinearLayout) contentView.findViewById(R.id.ll_registration_card_detail);
        mTv_duration_next_number = (TextView) contentView.findViewById(R.id.tv_commonality_duration_number);

        mEt_common_next_show2.setFilters(new InputFilter[]{new InputFilter.LengthFilter(50)});
        mEt_common_next_show3.setFilters(new InputFilter[]{new InputFilter.LengthFilter(50)});
        mEt_common_next_show1.setFilters(new InputFilter[]{new InputFilter.LengthFilter(50)});

        selectedNextDate();
    }

    private void selectedDate() {
        //开始时间
        mBegin_time.setOnClickListener(view -> {
            TimeDialogFragment timeDialogFragment = new TimeDialogFragment();
            timeDialogFragment.show(getSupportFragmentManager(), "0");
            Bundle bundle = new Bundle();
            bundle.putString("title", "开始时间");
            bundle.putInt("index", 0);
            bundle.putInt("isBegin", 0);
            timeDialogFragment.setArguments(bundle);
        });
        //结束时间
        mEnd_time.setOnClickListener(view -> {
            TimeDialogFragment timeDialogFragment = new TimeDialogFragment();
            timeDialogFragment.show(getSupportFragmentManager(), "0");
            Bundle bundle = new Bundle();
            bundle.putString("title", "结束时间");
            bundle.putInt("index", 0);
            bundle.putInt("isBegin", 1);
            timeDialogFragment.setArguments(bundle);
        });
        if (getIntent().getIntExtra("type", -1) != 3)
            //交接人
            mEt_common_show3.setOnClickListener(view -> {
                Intent intent = new Intent(this, SelectContactActivity.class);
                intent.putExtra("childId", mReceiverId);
                intent.putExtra("isRequest", true);
                startActivityForResult(intent, SELECT_OK);
            });
        mLl_common_card_detail.setOnClickListener(view -> {
            isSeletcedNextType = false;
            if (data != null) {
                if (data.size() == 0) {
                    showToast("获取" + mTv_common_show2.getText().toString() + "失败,正在为您重试！");
                    querySelectedTypeData();
                    return;
                }
                selectedDialog();
                return;
            }
            showToast("获取" + mTv_common_show2.getText().toString() + "失败,正在为您重试！");
            querySelectedTypeData();
        });

    }

    private void selectedNextDate() {
        //开始时间
        mNext_begin_time.setOnClickListener(view -> {
            TimeDialogFragment timeDialogFragment = new TimeDialogFragment();
            timeDialogFragment.show(getSupportFragmentManager(), "0");
            Bundle bundle = new Bundle();
            bundle.putString("title", "开始时间");
            bundle.putInt("index", 1);
            bundle.putInt("isBegin", 0);
            timeDialogFragment.setArguments(bundle);
        });
        //结束时间
        mNext_end_time.setOnClickListener(view -> {
            TimeDialogFragment timeDialogFragment = new TimeDialogFragment();
            timeDialogFragment.show(getSupportFragmentManager(), "0");
            Bundle bundle = new Bundle();
            bundle.putString("title", "结束时间");
            bundle.putInt("index", 1);
            bundle.putInt("isBegin", 1);
            timeDialogFragment.setArguments(bundle);
        });
        //交接人
        if (getIntent().getIntExtra("type", -1) != 3)
            mEt_common_next_show3.setOnClickListener(view -> {
                Intent intent = new Intent(this, SelectContactActivity.class);
                intent.putExtra("childId", mReceiverId);
                intent.putExtra("isRequest", true);
                intent.putExtra("nextReceiver", 1);
                startActivityForResult(intent, SELECT_OK);
            });

        mLl_common_next_card_detail.setOnClickListener(view -> {
            isSeletcedNextType = true;
            if (data != null) {
                if (data.size() == 0) {
                    showToast("获取" + mTv_common_next_show2.getText().toString() + "失败,正在为您重试！");
                    querySelectedTypeData();
                    return;
                }
                selectedDialog();
                return;
            }
            showToast("获取" + mTv_common_next_show2.getText().toString() + "失败,正在为您重试！");
            querySelectedTypeData();
        });
    }

    //出差申请
    private void onBusinessRequest() {
        mTopView.setAppTitle("出差申请");
        mEt_common_show3.setFocusable(false);
        mTvCommonalityTypeSelected.setText("出差 ");
    }

    //加班申请
    private void overTimeWorkRequest() {
        mLl_common_show1.setVisibility(View.GONE);
        mLl_common_show3.setVisibility(View.GONE);
        mTv_common_show2_dot.setVisibility(View.VISIBLE);
        mTopView.setAppTitle("加班申请");
        mTvCommonalityType.setText("加班类别");
        mTvCommonalityCoder.setText("加班编码");
        mTv_duration_number.setText("0小时");
        mTv_common_detail.setText("加班明细");
        mTv_common_show2.setText("加班原因");
        mBtnAddDetails.setText("+ 添加加班明细");
        mEt_common_show2.setHint("请填写加班原因");
        mTvCommonalityTypeSelected.setText("转调休加班 ");
    }

    //休假申请
    private void annualLeaveRequest() {
        mLl_common_show1.setVisibility(View.GONE);
        mLlCommonalityAnnualLeave.setVisibility(View.VISIBLE);
        mTopView.setAppTitle("休假申请");
        mTv_common_detail.setText("休假明细");
        mTvCommonalityType.setText("休假假别");
        mTvCommonalityCoder.setText("休假编码");
        mTv_common_show3.setText("工作交接人");
        mTv_common_show2.setText("休假事由");
        mTv_common_duration.setText("休假时长");
        mEt_common_show2.setHint("请填写休假事由");
        mBtnAddDetails.setText("+ 添加休假明细");
        mEt_common_show3.setHint("请填写工作交接人");
        mEt_common_show3.setFocusable(false);
        mTvCommonalityTypeSelected.setText("转调休加班 ");
    }

    //签卡申请
    private void registrationCardRequest() {
        mEt_common_show2.setVisibility(View.GONE);
        mLl_common_show1.setVisibility(View.GONE);
        mLl_common_end_time.setVisibility(View.GONE);
        mLl_common_duration.setVisibility(View.GONE);
        mLlCommonalityType.setVisibility(View.GONE);
        mLl_common_card_detail.setVisibility(View.VISIBLE);
        mTv_common_show3_dot.setVisibility(View.VISIBLE);
        mTv_common_show2_dot.setVisibility(View.VISIBLE);
        mTv_common_show2.setVisibility(View.VISIBLE);
        mTopView.setAppTitle("签卡申请");
        mTv_common_show2.setText("签卡原因");
        mTvCommonalityCoder.setText("签卡编码");
        mTv_common_show3.setText("签卡说明");
        mTv_common_detail.setText("签卡明细");
        mTv_common_begin_time.setText("签卡时间");
        mBtnAddDetails.setText("+ 添加签卡明细");
        mEt_common_show2.setHint("请选择签卡原因");
        mEt_common_show3.setHint("请填写签卡说明");
        mEt_common_show3.setClickable(false);
    }

    //出差申请
    private void onBusinessRequest1() {
        mEt_common_next_show3.setFocusable(false);
    }

    //加班申请
    private void overTimeWorkRequest1() {
        mLl_common_next_show1.setVisibility(View.GONE);
        mLl_common_next_show3.setVisibility(View.GONE);
        mTv_common_next_show2_dot.setVisibility(View.VISIBLE);
        mTv_common_next_detail.setText("加班明细");
        mTv_duration_next_number.setText("0小时");
        mTv_common_next_show2.setText("加班原因");
        mEt_common_next_show2.setHint("请填写加班原因");
    }

    //休假申请
    private void annualLeaveRequest1() {
        mLl_common_next_show1.setVisibility(View.GONE);
        mTv_common_next_show3.setText("工作交接人");
        mTv_common_next_show2.setText("休假事由");
        mTv_common_next_duration.setText("休假时长");
        mEt_common_next_show2.setHint("请填写休假事由");
        mTv_common_next_detail.setText("休假明细");
        mEt_common_next_show3.setHint("请填写工作交接人");
        mEt_common_next_show3.setFocusable(false);
    }

    //签卡申请
    private void registrationCardRequest1() {
        mEt_common_next_show2.setVisibility(View.GONE);
        mLl_common_next_show1.setVisibility(View.GONE);
        mLl_common_next_end_time.setVisibility(View.GONE);
        mLl_common_next_duration.setVisibility(View.GONE);
        mLl_common_next_card_detail.setVisibility(View.VISIBLE);
        mTv_common_next_show3_dot.setVisibility(View.VISIBLE);
        mTv_common_next_show2_dot.setVisibility(View.VISIBLE);
        mTv_common_next_show2.setVisibility(View.VISIBLE);
        mTv_common_next_show2.setText("签卡原因");
        mTv_common_next_show3.setText("签卡说明");
        mTv_common_next_detail.setText("签卡明细");
        mTv_common_next_begin_time.setText("签卡时间");
        mTv_selected_next_show.setText("请选择签卡原因");
        mEt_common_next_show3.setHint("请填写签卡说明");
        mEt_common_next_show3.setClickable(false);
        if (data.size() > 0)
            mTv_selected_next_show.setText(data.get(0).getContent());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_delete_detail: //删除详情
                mLlContainerView.removeView(mContentView1);
                mBtnAddDetails.setVisibility(View.VISIBLE);
                mBtnAddDetails.setClickable(true);
                mIndex--;
                break;
        }
    }

    @OnClick({R.id.btn_add_details, R.id.tv_commonality_type_selected, R.id.tv_commonality_type_date})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_add_details: //添加明细
                addDetail();
                break;
            case R.id.tv_commonality_type_selected:
                if (mCommonTypeBean != null) {
                    if (mCommonTypeBean.getData().size() == 0) {
                        showToast("获取" + mTvCommonalityType.getText().toString() + "失败,正在为您重试！");
                        querySelectedTypeData();
                        return;
                    }
                    selectedDialog();
                    return;
                }
                showToast("获取" + mTvCommonalityType.getText().toString() + "失败,正在为您重试！");
                querySelectedTypeData();
                break;
            case R.id.tv_commonality_type_date:
                mYearDateSelected.showPositionDateView(mYearPosition);
                break;
        }
    }

    private void addDetail() {
        mContentView1 = LayoutInflater.from(this).inflate(R.layout.commonality_initiate_approval_item, null);
        ImageView mDelete = (ImageView) mContentView1.findViewById(R.id.img_delete_detail);
        mDelete.setOnClickListener(this);
        mLlContainerView.addView(mContentView1);
        initContentView1(mContentView1);
        switch (getIntent().getIntExtra("type", -1)) {
            case 0://出差申请
                onBusinessRequest1();
                break;
            case 1://加班申请
                overTimeWorkRequest1();
                break;
            case 2://休假申请
                annualLeaveRequest1();
                break;
            case 3://签卡申请
                registrationCardRequest1();
                break;
            default:
                break;
        }
        mDelete.setVisibility(View.VISIBLE);
        mIndex++;
        mBtnAddDetails.setVisibility(View.INVISIBLE);
        mBtnAddDetails.setClickable(false);
    }

    @Override
    public void uidNull(String code) {
        catchWarningByCode(code);
    }

    @Override
    public void showLoading() {
        showLoadingView();
    }

    @Override
    public void requestFinish() {
        hideLoadingView();
    }

    @Override
    public void getQueryMonoCodeSuccess(String code) {
        if (!TextUtils.isEmpty(code)) {
            mTvNotNetwork.setVisibility(View.GONE);
            mSvContainerShow.setVisibility(View.VISIBLE);
            mTvCoderNumber.setText(code);
        } else {
            mTvNotNetwork.setText("获取的" + mTvCommonalityCoder.getText().toString() + "为空");
        }
        querySelectedTypeData();
    }

    private void querySelectedTypeData() {
        switch (getIntent().getIntExtra("type", -1)) {
            case 0://出差类别
                mPresenter.queryEvectionType(2);
                break;
            case 1:
                mPresenter.queryEvectionType(1);
                break;
            case 2://休假
                mPresenter.queryEvectionType(0);
                break;
            case 3://签卡
                mPresenter.findSignReason();
                break;
        }
    }

    @Override
    public void submitFailureTips(String msg) {
        if ("auth error".equals(msg)) {
            catchWarningByCode(msg);
            return;
        }
        if (TextUtils.isEmpty(msg.trim())) {
            showToast("服务器异常，请稍后重试！");
        } else {
            showToast(msg.trim());
        }
    }

    @Override
    public void getQueryMonoCodeFailure(int errorCode, String str) {
        if ("auth error".equals(str)) {
            catchWarningByCode(str);
            return;
        }
        if (TextUtils.isEmpty(str)) {
            showToast("服务器异常，请稍后重试！");
        } else {
            showToast(str);
        }
        mTvNotNetwork.setText(str);
    }

    @Override
    public void initiateThingsRequestSuccess() {

    }

    @Override
    public void initiateThingsRequestFailure(int errorCode, String str) {
        if ("auth error".equals(str)) {
            catchWarningByCode(str);
            return;
        }
        switch (errorCode) {
            case -1:
                mTvNotNetwork.setText(R.string.net_no_connection);
                mTvNotNetwork.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void queryEvectionTypeSuccess(CommonTypeBean bean) {
        if (bean != null) {
            data.clear();
            mCommonTypeBean = bean;
            for (int i = 0; i < bean.getData().size(); i++) {
                if (i == 0) {
                    mSelectedTypeID = bean.getData().get(i).getId();
                    mTvCommonalityTypeSelected.setText(bean.getData().get(i).getName() + " ");
                    data.add(new Dialog_Common_bean(bean.getData().get(i).getName(), true, bean.getData().get(i).getId()));
                } else {
                    data.add(new Dialog_Common_bean(bean.getData().get(i).getName(), false, bean.getData().get(i).getId()));
                }
            }
        }
    }

    @Override
    public void queryEvectionTypeFailure(int errorCode, String str) {
        if ("auth error".equals(str)) {
            catchWarningByCode(str);
            return;
        }
    }

    @Override
    public void queryDurationSuccess(QueryMonoBean bean) {
        try {
            if (bean != null) {
                if (isNextDate) {
                    mNextDuration = bean.getData();
                    if (getIntent().getIntExtra("type", -1) == 1) {
                        mTv_duration_next_number.setText(bean.getData() + "小时");

                    } else {
                        if (mSelectedTypeID.equals("1002Z710000000021ZM1") || mSelectedTypeID.equals("1001A1100000000154IU")) {
                            mTv_duration_next_number.setText(bean.getData() + "小时");
                        } else {
                            mTv_duration_next_number.setText(bean.getData() + "天");
                        }
                    }
                } else {
                    mQueryDuration = bean.getData();
                    if (getIntent().getIntExtra("type", -1) == 1) {
                        mTv_duration_number.setText(bean.getData() + "小时");
                    } else {
                        if (mSelectedTypeID.equals("1002Z710000000021ZM1") || mSelectedTypeID.equals("1001A1100000000154IU")) {
                            mTv_duration_number.setText(bean.getData() + "小时");
                        } else {
                            mTv_duration_number.setText(bean.getData() + "天");
                        }
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void queryDurationFailure(int errorCode, String str) {
        if ("auth error".equals(str)) {
            catchWarningByCode(str);
            return;
        }
    }

    @Override
    public void submitEvectionApplySuccess(String str) {
        finish();
        showToast("提交申请成功!");
    }

    @Override
    public void submitEvectionApplyFailure(int errorCode, String str) {
        if ("auth error".equals(str)) {
            catchWarningByCode(str);
            return;
        }
        if (TextUtils.isEmpty(str)) {
            showToast( "服务器异常，请稍后重试！");
            return;
        }
        showToast(str);
    }

    @Override
    public void addWorkApplySuccess(String bean) {
        finish();
        showToast("提交申请成功!");
    }

    @Override
    public void addWorkApplyFailure(int errorCode, String str) {
        if (TextUtils.equals("auth error",str)) {
            catchWarningByCode(str);
            return;
        }
        if (errorCode == -1) {
            if (TextUtils.isEmpty(getString(R.string.string_not_network))) {
                showToast("服务器异常，请稍后重试！");
            } else {
                showToast(getString(R.string.string_not_network));
            }
            return;
        }
        if (!TextUtils.isEmpty(str)) {
            showToast(str);
        } else {
            showToast("服务器异常，请稍后重试！");
        }
    }

    @Override
    public void submitFurloughSuccess(String bean) {
        finish();
        showToast("提交申请成功!");
    }

    @Override
    public void submitFurloughFailure(int errorCode, String str) {
        if ("auth error".equals(str)) {
            catchWarningByCode(str);
            return;
        }
        switch (errorCode) {
            case -1:
                showToast(getString(R.string.string_not_network));
                return;
        }
        if (TextUtils.isEmpty(str)) {
            showToast("服务器异常，请稍后重试！");
        } else {
            showToast(str);
        }
    }

    @Override
    public void findSignReasonSuccess(SingReasonBean bean) {
        if (bean != null) {
            data.clear();
            for (int i = 0; i < bean.getData().size(); i++) {
                if (i == 0) {
                    mSelectedTypeID = bean.getData().get(i).getSIGNCAUSEID();
                    mTv_selected_show.setText(bean.getData().get(i).getSIGNCAUSE());
                    data.add(new Dialog_Common_bean(bean.getData().get(i).getSIGNCAUSE(), true, bean.getData().get(i).getSIGNCAUSEID()));
                } else {
                    data.add(new Dialog_Common_bean(bean.getData().get(i).getSIGNCAUSE(), false, bean.getData().get(i).getSIGNCAUSEID()));
                }
            }
        }
    }

    @Override
    public void findSignReasonFailure(int errorCode, String str) {
        if ("auth error".equals(str)) {
            catchWarningByCode(str);
            return;
        }
    }

    @Override
    public void registrationCardSuccess(String bean) {
        showToast("提交申请成功!");
        if (getIntent().getBooleanExtra("signInRecord", false)) {
            setResult(101);
        }
        finish();
    }

    @Override
    public void registrationCardFailure(int errorCode, String str) {
        if ("auth error".equals(str)) {
            catchWarningByCode(str);
            return;
        }
        switch (errorCode) {
            case -1:
                showToast(getString(R.string.string_not_network));
                return;
        }
        if (TextUtils.isEmpty(str)) {
            showToast("服务器异常，请稍后重试！");
        } else {
            showToast(str);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void removeDeatls(SelectedTypeBean bean) {
        switch (bean.getEvent()) {
            case "removeDetail": //删除明细
                mBtnAddDetails.setVisibility(View.VISIBLE);
                mBtnAddDetails.setClickable(true);
                break;
            case "selectedType": //选择类型
                try {
                    //TODO 选择类型 申请时长清0
                    mQueryDuration = "0";
                    mNextDuration = "0";
                    if (getIntent().getIntExtra("type", -1) == 1) {
                        mTv_duration_number.setText(0 + "小时");
                    } else {
                        if (bean.getSelectedID().equals("1002Z710000000021ZM1") || bean.getSelectedID().equals("1001A1100000000154IU")) {
                            mTv_duration_number.setText(0 + "小时");
                            mBegin_time.setText("请选择开始时间");
                            mEnd_time.setText("请选择结束时间");
                            if (mNext_begin_time != null) {
                                mTv_duration_next_number.setText(0 + "小时");
                                mNext_begin_time.setText("请选择开始时间");
                                mNext_end_time.setText("请选择结束时间");
                            }
                        } else {
                            mTv_duration_number.setText(0 + "天");
                            mBegin_time.setText("请选择开始时间");
                            mEnd_time.setText("请选择结束时间");
                            if (mNext_begin_time != null) {
                                mTv_duration_next_number.setText(0 + "天");
                                mNext_begin_time.setText("请选择开始时间");
                                mNext_end_time.setText("请选择结束时间");
                            }
                        }
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                mSelectedTypeID = bean.getSelectedID();
                if (getIntent().getIntExtra("type", 0) == 3) {
                    if (!isSeletcedNextType) {
                        mTv_selected_show.setText(bean.getSelectedType());
                        int position = bean.getPosition();
                        mSelectedTypeID = data.get(position).getSelectedID();
                    } else {
                        mTv_selected_next_show.setText(bean.getSelectedType());
                        int position = bean.getPosition();
                        mSelectedNextID = data.get(position).getSelectedID();
                    }
                } else {
                    mTvCommonalityTypeSelected.setText(bean.getSelectedType() + " ");
                    int position = bean.getPosition();
                    mSelectedTypeID = data.get(position).getSelectedID();
                }
                for (int i = 0; i < data.size(); i++) {
                    if (i == bean.getPosition()) {
                        data.get(i).setSelected(true);
                    } else {
                        data.get(i).setSelected(false);
                    }
                }
                mTypeAdapter.setNewData(data);
                if (mDialog != null) {
                    mDialog.cancel();
                }
                break;
            case "selectedDate": //请求时长
                if (bean.getPosition() == 0) {
                    DateSelectedUtils(bean);
                } else {
                    DateSelectedNextUtils(bean);
                }
                break;
        }
    }

    //选择时间规则
    private void DateSelectedUtils(SelectedTypeBean bean) {
        if (bean.getIsBegin() == 0) {
            if (!TextUtils.isEmpty(mEnd_time.getText().toString().trim()) && !mEnd_time.getText().toString().equals("请选择结束时间")) {
                long startTime = DateUtils.getTimestampFromString(bean.getSelectedType(), "yyyy-MM-dd HH:mm");
                long endTime = DateUtils.getTimestampFromString(mEnd_time.getText().toString(), "yyyy-MM-dd HH:mm");
                if (startTime > endTime) {
                    showToast("开始时间大于结束时间,请重新选择！");
                    return;
                }
                mBegin_time.setText(bean.getSelectedType());
                queryDurationType();
                return;
            }
            mBegin_time.setText(bean.getSelectedType());
        } else {
            if (!TextUtils.isEmpty(mBegin_time.getText().toString().trim()) && !mBegin_time.getText().toString().equals("请选择开始时间")) {
                long startTime = DateUtils.getTimestampFromString(mBegin_time.getText().toString(), "yyyy-MM-dd HH:mm");
                long endTime = DateUtils.getTimestampFromString(bean.getSelectedType(), "yyyy-MM-dd HH:mm");
                if (startTime > endTime) {
                    showToast("结束时间小于开始时间,请重新选择！");
                    return;
                }
                mEnd_time.setText(bean.getSelectedType());
                queryDurationType();
                return;
            }
            mEnd_time.setText(bean.getSelectedType());
        }
    }

    private void DateSelectedNextUtils(SelectedTypeBean bean) {
        if (bean.getIsBegin() == 0) {
            if (!TextUtils.isEmpty(mNext_end_time.getText().toString().trim()) && !mNext_end_time.getText().toString().equals("请选择结束时间")) {
                long startTime = DateUtils.getTimestampFromString(bean.getSelectedType(), "yyyy-MM-dd HH:mm");
                long endTime = DateUtils.getTimestampFromString(mNext_end_time.getText().toString(), "yyyy-MM-dd HH:mm");
                if (startTime > endTime) {
                    showToast("开始时间大于结束时间,请重新选择！");
                    return;
                }
                mNext_begin_time.setText(bean.getSelectedType());
                queryDurationNextType();
                return;
            }
            mNext_begin_time.setText(bean.getSelectedType());
        } else {
            if (!TextUtils.isEmpty(mNext_begin_time.getText().toString().trim()) && !mNext_begin_time.getText().toString().equals("请选择开始时间")) {
                long startTime = DateUtils.getTimestampFromString(mNext_begin_time.getText().toString(), "yyyy-MM-dd HH:mm");
                long endTime = DateUtils.getTimestampFromString(bean.getSelectedType(), "yyyy-MM-dd HH:mm");
                if (startTime > endTime) {
                    showToast("结束时间小于开始时间,请重新选择！");
                    return;
                }
                mNext_end_time.setText(bean.getSelectedType());
                queryDurationNextType();
                return;
            }
            mNext_end_time.setText(bean.getSelectedType());
            queryDurationNextType();
        }
    }

    //选择时间请求时长
    private void queryDurationType() {
        if (!TextUtils.isEmpty(mTvCoderNumber.getText().toString().trim())) {
            isNextDate = false;
            mBeginDate = mBegin_time.getText().toString().replace(" ", "%20");
            mEndDate = mEnd_time.getText().toString().replace(" ", "%20");
        } else {
            showToast("请获取" + mTvCommonalityCoder.getText().toString() + "!");
        }
        switch (getIntent().getIntExtra("type", -1)) {
            case 0:
                mPresenter.queryDuration(mBeginDate, mEndDate, REQUESTCODE2, mTvCoderNumber.getText().toString(), mSelectedTypeID);
                break;
            case 1:
                mPresenter.queryDuration(mBeginDate, mEndDate, REQUESTCODE4, mTvCoderNumber.getText().toString(), mSelectedTypeID);
                break;
            case 2:
                mPresenter.queryDuration(mBeginDate, mEndDate, REQUESTCODE3, mTvCoderNumber.getText().toString(), mSelectedTypeID);
                break;
        }
    }

    private void queryDurationNextType() {
        if (!TextUtils.isEmpty(mTvCoderNumber.getText().toString().trim())) {
            isNextDate = true;
            mNext_begin_date = mNext_begin_time.getText().toString().replace(" ", "%20");
            mNext_end_date = mNext_end_time.getText().toString().replace(" ", "%20");
        } else {
            showToast("请获取" + mTvCommonalityCoder.getText().toString() + "!");
        }
        switch (getIntent().getIntExtra("type", -1)) {
            case 0:
                mPresenter.queryDuration(mNext_begin_date, mNext_end_date, REQUESTCODE2, mTvCoderNumber.getText().toString(), mSelectedTypeID);
                break;
            case 1:
                mPresenter.queryDuration(mNext_begin_date, mNext_end_date, REQUESTCODE4, mTvCoderNumber.getText().toString(), mSelectedTypeID);
                break;
            case 2:
                mPresenter.queryDuration(mNext_begin_date, mNext_end_date, REQUESTCODE3, mTvCoderNumber.getText().toString(), mSelectedTypeID);
                break;
        }
    }

    private void selectedDialog() {
        try {
            //获取屏幕高宽
            DisplayMetrics metric = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metric);
            int windowsHeight = metric.heightPixels;
            int windowsWight = metric.widthPixels;
            View view = initTypeData();

            CustomDialogUtils.Builder builder = new CustomDialogUtils.Builder(this);

            switch (getIntent().getIntExtra("type", -1)) {
                case 2:
                    mDialog = builder.cancelTouchout(false)
                            .view(view)
                            .heightpx((int) (windowsHeight / 2.5))
                            .widthpx((int) (windowsWight / 1.1))
                            .style(R.style.dialog)
                            .build();
                    break;
                default:
                    mDialog = builder.cancelTouchout(false)
                            .view(view)
                            .heightpx(view.getHeight())
                            .widthpx((int) (windowsWight / 1.1))
                            .style(R.style.dialog)
                            .build();
                    break;
            }

            if (mDialog.isShowing()) {
                mDialog.dismiss();
            }
            mDialog.show();
            mDialog.setCanceledOnTouchOutside(true);
            mTypeAdapter.notifyDataSetChanged();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    //初始化Dialog数据
    private View initTypeData() { //休假类别
        View inflate = LayoutInflater.from(this).inflate(R.layout.dialog_common_selected_type, null);
        RecyclerView rvSelectedType = (RecyclerView) inflate.findViewById(R.id.rv_selected_type);
        TextView tvTitle = (TextView) inflate.findViewById(R.id.tv_common_type_title);
        switch (getIntent().getIntExtra("type", -1)) {
            case 0:
                tvTitle.setText("出差类别");
                break;
            case 1:
                tvTitle.setText("加班类别");
                break;
            case 2:
                tvTitle.setText("休假类别");
                break;
            case 3:
                tvTitle.setText("签卡原因");
                break;
        }
        mTypeAdapter = new InitiateThingsTypeAdapter(this, data);
        rvSelectedType.setLayoutManager(new LinearLayoutManager(this));
        rvSelectedType.addItemDecoration(new ApproveDecorationLine(this));
        rvSelectedType.setAdapter(mTypeAdapter);
        return inflate;
    }

    //交接人
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (data.getIntExtra("nextReceiver", 0)) {
                case 0:
                    mReceiverId = data.getStringExtra("uid");
                    mReceiverName = data.getStringExtra("name");
                    mReceiverPost = data.getStringExtra("post");
                    mEt_common_show3.setText(mReceiverName);
                    break;
                case 1:
                    mNextReceiverId = data.getStringExtra("uid");
                    mNextReceiverName = data.getStringExtra("name");
                    mNextReceiverPost = data.getStringExtra("post");
                    mEt_common_next_show3.setText(mNextReceiverName);
                    break;
            }

        }
    }

    @Override
    public void onSelected(String date, int position) {
        mTvCommonalityTypeDate.setText(date + " ");
        mYearPosition = position;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
