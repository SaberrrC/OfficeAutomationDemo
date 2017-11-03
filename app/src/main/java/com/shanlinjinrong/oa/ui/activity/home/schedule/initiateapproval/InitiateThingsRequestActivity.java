package com.shanlinjinrong.oa.ui.activity.home.schedule.initiateapproval;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.ui.activity.home.schedule.initiateapproval.adapter.CommonalityInitiateAdapter;
import com.shanlinjinrong.oa.ui.activity.home.schedule.initiateapproval.adapter.InitiateThingsTypeAdapter;
import com.shanlinjinrong.oa.ui.activity.home.schedule.initiateapproval.bean.CommonalityInitiateBean;
import com.shanlinjinrong.oa.ui.activity.home.schedule.initiateapproval.bean.Dialog_Common_bean;
import com.shanlinjinrong.oa.ui.activity.home.schedule.initiateapproval.bean.QueryMonoBean;
import com.shanlinjinrong.oa.ui.activity.home.schedule.initiateapproval.bean.SelectedTypeBean;
import com.shanlinjinrong.oa.ui.activity.home.schedule.initiateapproval.bean.SubmitRequestBean;
import com.shanlinjinrong.oa.ui.activity.home.schedule.initiateapproval.contract.InitiateThingsRequestActivityContract;
import com.shanlinjinrong.oa.ui.activity.home.schedule.initiateapproval.presenter.InitiateThingsRequestActivityPresenter;
import com.shanlinjinrong.oa.ui.activity.home.schedule.initiateapproval.widget.ApproveDecorationLine;
import com.shanlinjinrong.oa.ui.activity.home.workreport.SelectContactActivity;
import com.shanlinjinrong.oa.ui.base.HttpBaseActivity;
import com.shanlinjinrong.oa.utils.CustomDialogUtils;
import com.shanlinjinrong.oa.utils.TimeDialogFragment;
import com.shanlinjinrong.views.common.CommonTopView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.shanlinjinrong.oa.ui.activity.home.workreport.WorkReportLaunchActivity.SELECT_OK;

/**
 * 发起申请
 */
public class InitiateThingsRequestActivity extends HttpBaseActivity<InitiateThingsRequestActivityPresenter> implements View.OnClickListener, InitiateThingsRequestActivityContract.View {

    @Bind(R.id.top_view)
    CommonTopView mTopView;
    @Bind(R.id.btn_add_details)
    TextView mBtnAddDetails;
    @Bind(R.id.sv_container_show)
    ScrollView mSvContainerShow;
    @Bind(R.id.tv_commonality_type)
    TextView mTvCommonalityType;
    @Bind(R.id.ll_commonality_type)
    LinearLayout mLlCommonalityType;
    @Bind(R.id.tv_commonality_coder)
    TextView mTvCommonalityCoder;
    @Bind(R.id.ll_commonality_date)
    LinearLayout mLlCommonalityDate;
    @Bind(R.id.tv_commonality_over_time)
    TextView mTvCommonalityOverTime;
    @Bind(R.id.tv_commonality_type_dot)
    TextView mTvCommonalityTypeDot;
    @Bind(R.id.ll_commonality_selected)
    LinearLayout mLlCommonalitySelected;
    @Bind(R.id.tv_commonality_request_date)
    TextView mTvCommonalityRequestDate;
    @Bind(R.id.tv_commonality_type_selected)
    TextView mTvCommonalityTypeSelected;
    @Bind(R.id.ll_commonality_annual_leave)
    LinearLayout mLlCommonalityAnnualLeave;
    @Bind(R.id.ll_container_view)
    LinearLayout mLlContainerView;
    @Bind(R.id.tv_coder_number)
    TextView mTvCoderNumber;
    @Bind(R.id.tv_request_date)
    TextView mTvRequestDate;

    private int mIndex = 1;
    private ImageView mDelete;
    private View mContentView1;
    private CustomDialogUtils mDialog;
    private CommonalityInitiateAdapter mAdapter;
    private SubmitRequestBean mSubmitRequestBean;
    private CommonalityInitiateBean mInitiateBean;
    private InitiateThingsTypeAdapter mTypeAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private List<Dialog_Common_bean> data = new ArrayList<>(); //Dialog 数据源
    private List<CommonalityInitiateBean> mData = new ArrayList<>(); //明细数据源
    private String mReceiverId, mReceiverName, mReceiverPost, mNextReceiverId, mNextReceiverName, mNextReceiverPost; //交接人
    private LinearLayout mLl_common_show1, mLl_common_next_show1, mLl_common_show3, mLl_common_next_show3, mLl_common_end_time, mLl_common_next_end_time,
            mLl_common_duration, mLl_common_next_duration, mLl_common_card_detail, mLl_common_next_card_detail;
    private TextView mTv_common_show2, mTv_common_next_show2, mTv_common_next_show3, mTv_common_show3, mTv_common_duration, mTv_common_next_duration,
            mTv_common_detail, mTv_common_next_detail, mTv_common_begin_time, mTv_common_next_begin_time, mTv_selected_show, mTv_selected_next_show,
            mBegin_time, mNext_begin_time, mEnd_time, mNext_end_time, mEt_common_show3, mEt_common_next_show3;
    private EditText mEt_common_show2, mEt_common_next_show2, mEt_common_show1, mEt_common_next_show1;

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
        initMonoCode();
        mInitiateBean = new CommonalityInitiateBean("", "忘记打卡", getIntent().getIntExtra("type", -1) + "");
        mData.add(mInitiateBean);

    }

    //获取编码
    private void initMonoCode() {
        switch (getIntent().getIntExtra("type", 0)) {
            case 0://出差编号
                mPresenter.getQueryMonoCode(6403);
                break;
            case 1://加班
                mPresenter.getQueryMonoCode(6405);
                break;
            case 2:
                mPresenter.getQueryMonoCode(6404);
                break;
            case 3:
                mPresenter.getQueryMonoCode(6403);
                break;
        }

    }

    private void initView() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date currentDate = new Date(System.currentTimeMillis());
        String currentTime = formatter.format(currentDate);
        mTvRequestDate.setText(currentTime);
        mSubmitRequestBean = new SubmitRequestBean();
        mLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        //加载初始化布局
        View contentView = LayoutInflater.from(this).inflate(R.layout.commonality_initiate_approval_item, null);
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
        mTopView.getRightView().setOnClickListener(view -> {
            switch (getIntent().getIntExtra("type", -1)) {
                case 0:
                    try {//出差提交
                        JSONObject jsonObject = new JSONObject();
                        JSONArray jsonArray = new JSONArray();
                        JSONObject jsonObject1 = new JSONObject();
                        jsonObject1.put("address", mEt_common_show1.getText().toString());
                        jsonObject1.put("cause", mEt_common_show2.getText().toString());
                        jsonObject1.put("endTime", mEnd_time.getText().toString());
                        jsonObject1.put("startTime", mBegin_time.getText().toString());
                        jsonObject1.put("handOverPepole", mEt_common_show3.getText().toString());//TODO Number
                        jsonObject1.put("timeDifference", mTv_common_duration.getText().toString());//TODO Number
                        jsonArray.put(jsonObject1);
                        if (mIndex > 1) {
                            JSONObject jsonObject2 = new JSONObject();
                            jsonObject2.put("address", mEt_common_next_show1.getText().toString());
                            jsonObject2.put("cause", mEt_common_next_show2.getText().toString());
                            jsonObject2.put("endTime", mNext_end_time.getText().toString());
                            jsonObject2.put("startTime", mNext_begin_time.getText().toString());
                            jsonObject2.put("handOverPepole", mEt_common_next_show3.getText().toString());//TODO Number
                            jsonObject2.put("timeDifference", mTv_common_next_duration.getText().toString());//TODO Number
                            jsonArray.put(jsonObject2);
                        }
                        jsonObject.put("detailList", jsonArray);
                        jsonObject.put("monocode", mTvCoderNumber.getText().toString());
                        jsonObject.put("type", 0);
                        //mPresenter.initiateThingsRequest();  //TODO 提交
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 1:
                    try {//加班申请
                        JSONObject jsonObject = new JSONObject();
                        JSONArray jsonArray = new JSONArray();
                        JSONObject jsonObject1 = new JSONObject();
                        jsonObject1.put("cause", mEt_common_show2.getText().toString());
                        jsonObject1.put("endTime", mEnd_time.getText().toString());
                        jsonObject1.put("startTime", mBegin_time.getText().toString());
                        jsonObject1.put("timeDifference", mTv_common_duration.getText().toString());//TODO Number
                        jsonArray.put(jsonObject1);
                        if (mIndex > 1) {
                            JSONObject jsonObject2 = new JSONObject();
                            jsonObject2.put("cause", mEt_common_next_show2.getText().toString());
                            jsonObject2.put("endTime", mNext_end_time.getText().toString());
                            jsonObject2.put("startTime", mNext_begin_time.getText().toString());
                            jsonObject2.put("timeDifference", mTv_common_next_duration.getText().toString());//TODO Number
                            jsonArray.put(jsonObject2);
                        }
                        jsonObject.put("detailList", jsonArray);
                        jsonObject.put("monocode", mTvCoderNumber.getText().toString());
                        jsonObject.put("type", 1);
                        //mPresenter.initiateThingsRequest();  //TODO 提交
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    try {//休假申请
                        JSONObject jsonObject = new JSONObject();
                        JSONArray jsonArray = new JSONArray();
                        JSONObject jsonObject1 = new JSONObject();
                        jsonObject1.put("endTime", mEnd_time);
                        jsonObject1.put("startTime", mBegin_time);
                        jsonObject1.put("handOverPepole", mEt_common_show3.getText().toString());
                        jsonObject1.put("messge", mEt_common_show2.getText().toString());
                        jsonObject1.put("timeDifference", mTv_common_duration.getText().toString());
                        jsonArray.put(jsonObject1);
                        if (mIndex > 1) {
                            JSONObject jsonObject2 = new JSONObject();
                            jsonObject1.put("endTime", mNext_end_time.getText().toString());
                            jsonObject1.put("startTime", mNext_begin_time.getText().toString());
                            jsonObject1.put("handOverPepole", mEt_common_next_show3.getText().toString());
                            jsonObject1.put("messge", mEt_common_next_show2.getText().toString());
                            jsonObject1.put("timeDifference", mTv_common_next_duration.getText().toString());
                            jsonArray.put(jsonObject2);
                        }
                        jsonObject.put("applyDate", jsonArray);
                        jsonObject.put("date", jsonArray);
                        jsonObject.put("monocode", mTvCoderNumber.getText().toString());
                        jsonObject.put("type", 2);
                        //mPresenter.initiateThingsRequest();  //TODO 提交
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 3:
                    try {
                        JSONObject jsonObject = new JSONObject();
                        JSONArray jsonArray = new JSONArray();
                        JSONObject jsonObject1 = new JSONObject();
                        jsonObject.put("signCause", "");
                        jsonObject.put("signCauseId", "");
                        jsonObject.put("signRemark", "");
                        jsonObject.put("signTime", "");
                        JSONObject jsonObject2 = new JSONObject();

                        jsonObject.put("applyDate", jsonArray);
                        jsonObject.put("monocode", "");
                        jsonObject.put("userId", "");
                        //mPresenter.initiateThingsRequest();  //TODO 提交
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        });
    }

    private void initContentView0(View contentView) {
        mEnd_time = (TextView) contentView.findViewById(R.id.et_commonality_end_time);
        mTv_selected_show = (TextView) contentView.findViewById(R.id.tv_selected_show);
        mTv_selected_show = (TextView) contentView.findViewById(R.id.tv_selected_show);
        mTv_common_show2 = (TextView) contentView.findViewById(R.id.tv_commonality_show2);
        mEt_common_show2 = (EditText) contentView.findViewById(R.id.et_commonality_show2);
        mEt_common_show1 = (EditText) contentView.findViewById(R.id.et_commonality_show1);
        mTv_common_show3 = (TextView) contentView.findViewById(R.id.tv_commonality_show3);
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
        mLl_common_card_detail = (LinearLayout) contentView.findViewById(R.id.ll_registration_card_detail);
        selectedDate();
    }

    private void initContentView1(View contentView) {
        mNext_end_time = (TextView) contentView.findViewById(R.id.et_commonality_end_time);
        mTv_selected_next_show = (TextView) contentView.findViewById(R.id.tv_selected_show);
        mTv_selected_next_show = (TextView) contentView.findViewById(R.id.tv_selected_show);
        mTv_common_next_show2 = (TextView) contentView.findViewById(R.id.tv_commonality_show2);
        mEt_common_next_show2 = (EditText) contentView.findViewById(R.id.et_commonality_show2);
        mEt_common_next_show1 = (EditText) contentView.findViewById(R.id.et_commonality_show1);
        mTv_common_next_show3 = (TextView) contentView.findViewById(R.id.tv_commonality_show3);
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
        mLl_common_next_card_detail = (LinearLayout) contentView.findViewById(R.id.ll_registration_card_detail);
        selectedNextDate();
    }

    private void selectedDate() {
        mBegin_time.setOnClickListener(view -> {    //开始时间
            TimeDialogFragment timeDialogFragment = new TimeDialogFragment();
            timeDialogFragment.show(getSupportFragmentManager(), "0");
            Bundle bundle = new Bundle();
            bundle.putString("title", "开始时间");
            bundle.putInt("index", 0);
            bundle.putInt("isBegin", 0);
            timeDialogFragment.setArguments(bundle);
        });
        mEnd_time.setOnClickListener(view -> {    //结束时间
            TimeDialogFragment timeDialogFragment = new TimeDialogFragment();
            timeDialogFragment.show(getSupportFragmentManager(), "0");
            Bundle bundle = new Bundle();
            bundle.putString("title", "结束时间");
            bundle.putInt("index", 0);
            bundle.putInt("isBegin", 1);
            timeDialogFragment.setArguments(bundle);
        });
        mEt_common_show3.setOnClickListener(view -> {
            Intent intent = new Intent(this, SelectContactActivity.class);
            intent.putExtra("childId", mReceiverId);
            startActivityForResult(intent, SELECT_OK);
        });
    }

    private void selectedNextDate() {
        mNext_begin_time.setOnClickListener(view -> {   //开始时间
            TimeDialogFragment timeDialogFragment = new TimeDialogFragment();
            timeDialogFragment.show(getSupportFragmentManager(), "0");
            Bundle bundle = new Bundle();
            bundle.putString("title", "开始时间");
            bundle.putInt("index", 1);
            bundle.putInt("isBegin", 0);
            timeDialogFragment.setArguments(bundle);
        });
        mNext_end_time.setOnClickListener(view -> {   //结束时间
            TimeDialogFragment timeDialogFragment = new TimeDialogFragment();
            timeDialogFragment.show(getSupportFragmentManager(), "0");
            Bundle bundle = new Bundle();
            bundle.putString("title", "结束时间");
            bundle.putInt("index", 1);
            bundle.putInt("isBegin", 1);
            timeDialogFragment.setArguments(bundle);
        });
        mEt_common_next_show3.setOnClickListener(view -> {
            Intent intent = new Intent(this, SelectContactActivity.class);
            intent.putExtra("childId", mReceiverId);
            intent.putExtra("nextReceiver", 1);
            startActivityForResult(intent, SELECT_OK);
        });
    }

    //出差申请
    private void onBusinessRequest() {
        mTopView.setAppTitle("出差申请");
    }

    //加班申请
    private void overTimeWorkRequest() {
        mLl_common_show1.setVisibility(View.GONE);
        mLl_common_show3.setVisibility(View.GONE);
        mTopView.setAppTitle("加班申请");
        mTv_common_detail.setText("加班明细");
        mTv_common_show2.setText("加班原因");
        mEt_common_show2.setHint("请填写加班原因");
    }

    //休假申请
    private void annualLeaveRequest() {
        mLl_common_show1.setVisibility(View.GONE);
        mTopView.setAppTitle("休假申请");
        mTv_common_show3.setText("工作交接人");
        mTv_common_show2.setText("休假事由");
        mTv_common_duration.setText("休假时长");
        mEt_common_show2.setHint("请填写休假事由");
        mEt_common_show3.setHint("请填写工作交接人");
    }

    //签卡申请
    private void registrationCardRequest() {
        mEt_common_show2.setVisibility(View.GONE);
        mLl_common_show1.setVisibility(View.GONE);
        mLl_common_end_time.setVisibility(View.GONE);
        mLl_common_duration.setVisibility(View.GONE);
        mLl_common_card_detail.setVisibility(View.VISIBLE);
        mTopView.setAppTitle("签卡申请");
        mTv_common_show2.setText("签卡原因");
        mTv_common_show3.setText("签卡说明");
        mTv_common_detail.setText("签卡明细");
        mTv_common_begin_time.setText("签卡时间");
        mEt_common_show2.setHint("请选择签卡原因");
        mEt_common_show3.setHint("请填写签卡说明");
    }

    //出差申请
    private void onBusinessRequest1() {
    }

    //加班申请
    private void overTimeWorkRequest1() {
        mLl_common_next_show1.setVisibility(View.GONE);
        mLl_common_next_show3.setVisibility(View.GONE);
        mTv_common_next_detail.setText("加班明细");
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
        mEt_common_next_show3.setHint("请填写工作交接人");
    }

    //签卡申请
    private void registrationCardRequest1() {
        mEt_common_next_show2.setVisibility(View.GONE);
        mLl_common_next_show1.setVisibility(View.GONE);
        mLl_common_next_end_time.setVisibility(View.GONE);
        mLl_common_next_duration.setVisibility(View.GONE);
        mLl_common_next_card_detail.setVisibility(View.VISIBLE);
        mTv_common_next_show2.setText("签卡原因");
        mTv_common_next_show3.setText("签卡说明");
        mTv_common_next_detail.setText("签卡明细");
        mTv_common_next_begin_time.setText("签卡时间");
        mEt_common_next_show2.setHint("请选择签卡原因");
        mEt_common_next_show3.setHint("请填写签卡说明");
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_delete_detail:
                mLlContainerView.removeView(mContentView1);
                mBtnAddDetails.setVisibility(View.VISIBLE);
                mBtnAddDetails.setClickable(true);
                mIndex--;
                break;
        }
    }

    @OnClick({R.id.btn_add_details, R.id.ll_commonality_selected})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_add_details: //添加明细
                addDetail();
                break;
            case R.id.ll_commonality_selected:
                NonTokenDialog();
                break;
        }
    }

    private void addDetail() {
        mContentView1 = LayoutInflater.from(this).inflate(R.layout.commonality_initiate_approval_item, null);
        mDelete = (ImageView) mContentView1.findViewById(R.id.img_delete_detail);
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
    public void uidNull(int code) {
        catchWarningByCode(code);
    }

    @Override
    public void showLoading() {
//        showLoadingView();
    }

    @Override
    public void requestFinish() {
//            hideLoadingView();
    }

    @Override
    public void requestNetworkError() {

    }

    @Override
    public void getQueryMonoCodeSuccess(QueryMonoBean bean) {
        if (bean != null) {
            mTvCoderNumber.setText(bean.getData());
        }
        switch (getIntent().getIntExtra("type", -1)) {
            case 0://出差类别
                mPresenter.queryEvectionType(2);
                data.add(new Dialog_Common_bean("外出", true));
                data.add(new Dialog_Common_bean("出差", false));
                break;
            case 1:
                mPresenter.queryEvectionType(1);
                break;
            case 2://休假
                mPresenter.queryEvectionType(0);
                data.add(new Dialog_Common_bean("事假", true));
                data.add(new Dialog_Common_bean("婚假", false));
                data.add(new Dialog_Common_bean("年假", false));
                data.add(new Dialog_Common_bean("丧假", false));
                data.add(new Dialog_Common_bean("工伤", false));
                break;
            case 3://签卡
                data.add(new Dialog_Common_bean("忘记打卡", true));
                data.add(new Dialog_Common_bean("考勤机故障", false));
                data.add(new Dialog_Common_bean("地铁故障", false));
                break;
        }
    }

    @Override
    public void getQueryMonoCodeFailure(int errorCode, String str) {

    }

    @Override
    public void initiateThingsRequestSuccess() {

    }

    @Override
    public void initiateThingsRequestFailure(int errorCode, String str) {

    }

    @Override
    public void queryEvectionTypeSuccess() {

    }

    @Override
    public void queryEvectionTypeFailure(int errorCode, String str) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void removeDeatls(SelectedTypeBean bean) {
        switch (bean.getEvent()) {
            case "removeDetail": //删除明细
                mData.remove(1);
                mAdapter.setNewData(mData);
                mAdapter.notifyDataSetChanged();
                mBtnAddDetails.setVisibility(View.VISIBLE);
                mBtnAddDetails.setClickable(true);
                break;
            case "selectedType": //选择类型
                mTvCommonalityTypeSelected.setText(bean.getSelectedType());
                for (int i = 0; i < data.size(); i++) {
                    mData.get(mData.size() - 1).setSelectedTitle(bean.getSelectedType());
                    mAdapter.setNewData(mData);
                    mAdapter.notifyDataSetChanged();
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
            case "showDialog":
                NonTokenDialog();
                break;
            case "selectedDate":
                if (bean.getPosition() == 0) {
                    if (bean.getIsBegin() == 0) {
                        mBegin_time.setText(bean.getSelectedType());
                    } else {
                        mEnd_time.setText(bean.getSelectedType());
                    }
                } else {
                    if (bean.getIsBegin() == 0) {
                        mNext_begin_time.setText(bean.getSelectedType());
                    } else {
                        mNext_end_time.setText(bean.getSelectedType());
                    }
                }
                break;
        }
    }

    private void NonTokenDialog() {
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
    private View initTypeData() {
        View inflate = LayoutInflater.from(this).inflate(R.layout.dialog_common_selected_type, null);
        RecyclerView rvSelectedType = (RecyclerView) inflate.findViewById(R.id.rv_selected_type);
        TextView tvTitle = (TextView) inflate.findViewById(R.id.tv_common_type_title);
        switch (getIntent().getIntExtra("type", -1)) {
            case 0:
                tvTitle.setText("出差类别");
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
        rvSelectedType.addItemDecoration(new ApproveDecorationLine(this, data));
        rvSelectedType.setAdapter(mTypeAdapter);
        return inflate;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (data.getIntExtra("nextReceiver", 0)) {
                case 0:
                    mReceiverId = data.getStringExtra("uid");
                    mReceiverName = data.getStringExtra("name");
                    mReceiverPost = data.getStringExtra("post");
                    mEt_common_show3.setText(mReceiverName + "-" + mReceiverPost);
                    break;
                case 1:
                    mNextReceiverId = data.getStringExtra("uid");
                    mNextReceiverName = data.getStringExtra("name");
                    mNextReceiverPost = data.getStringExtra("post");
                    mEt_common_next_show3.setText(mNextReceiverName + "-" + mNextReceiverPost);
                    break;
            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
