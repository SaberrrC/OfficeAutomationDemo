package com.shanlinjinrong.oa.ui.activity.home.schedule.initiateapproval;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.shanlinjinrong.oa.ui.activity.home.schedule.staffselfhelp.HolidaySearchActivity;
import com.shanlinjinrong.oa.ui.activity.home.workreport.SelectContactActivity;
import com.shanlinjinrong.oa.ui.base.HttpBaseActivity;
import com.shanlinjinrong.oa.utils.CustomDialogUtils;
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

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.shanlinjinrong.oa.ui.activity.home.workreport.WorkReportLaunchActivity.SELECT_OK;

/**
 * 发起申请
 */
public class InitiateThingsRequestActivity extends HttpBaseActivity<InitiateThingsRequestActivityPresenter> implements View.OnClickListener, InitiateThingsRequestActivityContract.View, YearTimeSelectedListener {

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
    @Bind(R.id.tv_commonality_over_time)
    TextView mTvCommonalityOverTime;
    @Bind(R.id.tv_commonality_type_dot)
    TextView mTvCommonalityTypeDot;
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
    @Bind(R.id.tv_not_network)
    TextView mTvNotNetwork;
    @Bind(R.id.tv_commonality_type_date)
    TextView mTvCommonalityTypeDate;

    private ImageView mDelete;
    private View mContentView1;
    private CustomDialogUtils mDialog;
    private int mIndex = 1, mYearPosition = 0;
    private YearDateSelected mYearDateSelected;
    private InitiateThingsTypeAdapter mTypeAdapter;
    private List<String> mDate = new ArrayList<>();
    private List<Dialog_Common_bean> data = new ArrayList<>(); //Dialog 数据源
    private String selectedRequestType, mSelectedTypeID, mSelectedNextID;
    private String mBeginDate, mEndDate, mNext_begin_date, mNext_end_date; //时间选取
    private boolean isNextDate, isQueryDuration, isNextDuration, isSeletcedNextType;
    private int REQUESTCODE1 = 6402, REQUESTCODE2 = 6403, REQUESTCODE3 = 6404, REQUESTCODE4 = 6405; //6402签卡申请,6403出差申请,6404休假申请,6405加班申请
    private String mReceiverId, mReceiverName, mReceiverPost, mNextReceiverId, mNextReceiverName, mNextReceiverPost; //交接人
    private LinearLayout mLl_common_show1, mLl_common_next_show1, mLl_common_show3, mLl_common_next_show3, mLl_common_end_time, mLl_common_next_end_time,
            mLl_common_duration, mLl_common_next_duration, mLl_common_card_detail, mLl_common_next_card_detail;
    private TextView mTv_common_show2, mTv_common_next_show2, mTv_common_next_show3, mTv_common_show3, mTv_common_duration, mTv_common_next_duration,
            mTv_common_detail, mTv_common_next_detail, mTv_common_begin_time, mTv_common_next_begin_time, mTv_selected_show, mTv_selected_next_show,
            mBegin_time, mNext_begin_time, mEnd_time, mNext_end_time, mEt_common_show3, mEt_common_next_show3, mTv_duration_next_number, mTv_duration_number;
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
        initMonoCode();//获取编码
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

    private void submitRequest() {
        mTopView.getRightView().setOnClickListener(view -> {
//            if (!isQueryDuration || !isNextDuration) {
//                initMonoCode();
//                showToast("请重新获取申请时长!");
//                return;
//            }
//            if (TextUtils.isEmpty(mTvCoderNumber.getText().toString().trim())) {
//                showToast("请重新获取" + mTvCommonalityCoder.getText().toString() + "!");
//                return;
//            }
            switch (getIntent().getIntExtra("type", -1)) {
                case 0://出差申请
                    submitEvectionApply();
                    break;
                case 1://加班申请
                    submitAddWorkApply();
                    break;
                case 2:
                    submitFurloughApply();
                    break;
                case 3:
                    submitRegistrationCard();
                    break;
                default:
                    break;
            }
        });
    }

    //*********************************出差提交*******************************
    private void submitEvectionApply() {
        try {
            JSONObject jsonObject = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("evectionAddress", mEt_common_show1.getText().toString());
            jsonObject1.put("evectionRemark", mEt_common_show2.getText().toString());
            jsonObject1.put("endTime", mEnd_time.getText().toString() + ":00");
            jsonObject1.put("startTime", mBegin_time.getText().toString() + ":00");
            jsonObject1.put("handOverPepole", mReceiverId);
            jsonObject1.put("timeDifference", mTvCoderNumber.getText().toString());
            jsonArray.put(jsonObject1);
            if (mIndex > 1) {
                JSONObject jsonObject2 = new JSONObject();
                jsonObject2.put("evectionAddress", mEt_common_next_show1.getText().toString());
                jsonObject2.put("evectionRemark", mEt_common_next_show2.getText().toString());
                jsonObject2.put("endTime", mNext_end_time.getText().toString() + ":00");
                jsonObject2.put("startTime", mNext_begin_time.getText().toString() + ":00");
                jsonObject2.put("handOverPepole", mNextReceiverId);
                jsonObject2.put("timeDifference", mTvCoderNumber.getText().toString());
                jsonArray.put(jsonObject2);
            }
            jsonObject.put("detailList", jsonArray);
            jsonObject.put("billCode", mTvCoderNumber.getText().toString());
            jsonObject.put("type", mSelectedTypeID);
            jsonObject.put("applyDate", mTvRequestDate.getText().toString());
            HttpParams httpParams = new HttpParams();
            httpParams.putJsonParams(jsonObject.toString());
            mPresenter.submitEvectionApply(httpParams);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //*********************************加班提交*********************************
    private void submitAddWorkApply() {
        try {
            JSONObject jsonObject = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("cause", mEt_common_show2.getText().toString());
            jsonObject1.put("endTime", mEnd_time.getText().toString() + ":00");
            jsonObject1.put("startTime", mBegin_time.getText().toString() + ":00");
            jsonObject1.put("timeDifference", mTv_duration_number.getText().toString());
            jsonArray.put(jsonObject1);
            if (mIndex > 1) {
                JSONObject jsonObject2 = new JSONObject();
                jsonObject2.put("cause", mEt_common_next_show2.getText().toString());
                jsonObject2.put("endTime", mEnd_time.getText().toString() + ":00");
                jsonObject2.put("startTime", mNext_begin_time.getText().toString() + ":00");
                jsonObject2.put("timeDifference", mTv_duration_next_number.getText().toString());
                jsonArray.put(jsonObject2);
            }
            jsonObject.put("detailList", jsonArray);
            jsonObject.put("monocode", mTvCoderNumber.getText().toString());
            jsonObject.put("type", mSelectedTypeID);
            HttpParams httpParams = new HttpParams();
            httpParams.putJsonParams(jsonObject.toString());
            mPresenter.addWorkApply(httpParams);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //*********************************休假提交*********************************
    private void submitFurloughApply() {
        try {//休假申请
            JSONObject jsonObject = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("endTime", mEnd_time.getText().toString() + ":00");
            jsonObject1.put("startTime", mBegin_time.getText().toString() + ":00");
            jsonObject1.put("handOverPepole", mReceiverId);
            jsonObject1.put("FurloughRemark", mEt_common_show2.getText().toString());
            jsonObject1.put("timeDifference", mTv_duration_number.getText().toString());
            jsonArray.put(jsonObject1);
            if (mIndex > 1) {
                JSONObject jsonObject2 = new JSONObject();
                jsonObject2.put("endTime", mNext_end_time.getText().toString() + ":00");
                jsonObject2.put("startTime", mNext_begin_time.getText().toString() + ":00");
                jsonObject2.put("handOverPepole", mNextReceiverId);
                jsonObject2.put("FurloughRemark", mEt_common_next_show2.getText().toString());
                jsonObject2.put("timeDifference", mTv_duration_next_number.getText().toString());
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
        mEt_common_show2 = (EditText) contentView.findViewById(R.id.et_commonality_show2);
        mTv_selected_show = (TextView) contentView.findViewById(R.id.tv_selected_show);
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
        mTv_duration_number = (TextView) contentView.findViewById(R.id.tv_commonality_duration_number);
        selectedDate();
    }

    //*********************************明细2*********************************
    private void initContentView1(View contentView) {
        mNext_end_time = (TextView) contentView.findViewById(R.id.et_commonality_end_time);
        mTv_common_next_show2 = (TextView) contentView.findViewById(R.id.tv_commonality_show2);
        mEt_common_next_show2 = (EditText) contentView.findViewById(R.id.et_commonality_show2);
        mEt_common_next_show1 = (EditText) contentView.findViewById(R.id.et_commonality_show1);
        mTv_common_next_show3 = (TextView) contentView.findViewById(R.id.tv_commonality_show3);
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
        if (getIntent().getIntExtra("type", -1) != 3)
            mEt_common_show3.setOnClickListener(view -> { //交接人
                Intent intent = new Intent(this, SelectContactActivity.class);
                intent.putExtra("childId", mReceiverId);
                intent.putExtra("isRequest", true);
                startActivityForResult(intent, SELECT_OK);
            });
        mLl_common_card_detail.setOnClickListener(view -> {
            isSeletcedNextType = false;
            NonTokenDialog();
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
        if (getIntent().getIntExtra("type", -1) != 3)
            mEt_common_next_show3.setOnClickListener(view -> { //交接人
                Intent intent = new Intent(this, SelectContactActivity.class);
                intent.putExtra("childId", mReceiverId);
                intent.putExtra("isRequest", true);
                intent.putExtra("nextReceiver", 1);
                startActivityForResult(intent, SELECT_OK);
            });
        mLl_common_next_card_detail.setOnClickListener(view -> {
            isSeletcedNextType = true;
            NonTokenDialog();
        });
    }

    //出差申请
    private void onBusinessRequest() {
        mTopView.setAppTitle("出差申请");
        mEt_common_show3.setFocusable(false);
    }

    //加班申请
    private void overTimeWorkRequest() {
        mLl_common_show1.setVisibility(View.GONE);
        mLl_common_show3.setVisibility(View.GONE);
        mTopView.setAppTitle("加班申请");
        mTvCommonalityType.setText("加班类别");
        mTv_common_detail.setText("加班明细");
        mTv_common_show2.setText("加班原因");
        mEt_common_show2.setHint("请填写加班原因");
    }

    //休假申请
    private void annualLeaveRequest() {
        mLl_common_show1.setVisibility(View.GONE);
        mLlCommonalityAnnualLeave.setVisibility(View.VISIBLE);
        mTopView.setAppTitle("休假申请");
        mTvCommonalityType.setText("休假类别");
        mTv_common_show3.setText("工作交接人");
        mTv_common_show2.setText("休假事由");
        mTv_common_duration.setText("休假时长");
        mEt_common_show2.setHint("请填写休假事由");
        mEt_common_show3.setHint("请填写工作交接人");
        mEt_common_show3.setFocusable(false);
    }

    //签卡申请
    private void registrationCardRequest() {
        mEt_common_show2.setVisibility(View.GONE);
        mLl_common_show1.setVisibility(View.GONE);
        mLl_common_end_time.setVisibility(View.GONE);
        mLl_common_duration.setVisibility(View.GONE);
        mLlCommonalityType.setVisibility(View.GONE);
        mLl_common_card_detail.setVisibility(View.VISIBLE);
        mTopView.setAppTitle("签卡申请");
        mTv_common_show2.setText("签卡原因");
        mTv_common_show3.setText("签卡说明");
        mTv_common_detail.setText("签卡明细");
        mTv_common_begin_time.setText("签卡时间");
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
        mEt_common_next_show3.setFocusable(false);
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
                NonTokenDialog();
                break;
            case R.id.tv_commonality_type_date:
                mYearDateSelected.showPositionDateView(mYearPosition);
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
        showLoadingView();
    }

    @Override
    public void requestFinish() {
        hideLoadingView();
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
    public void getQueryMonoCodeFailure(int errorCode, String str) {
        switch (errorCode) {
            case -1:
                showToast(getString(R.string.net_no_connection));
                mTvNotNetwork.setText(R.string.string_not_network);
                mTvNotNetwork.setVisibility(View.VISIBLE);
                hideLoadingView();
                break;
        }

    }

    @Override
    public void initiateThingsRequestSuccess() {


    }

    @Override
    public void initiateThingsRequestFailure(int errorCode, String str) {
        switch (errorCode) {
            case -1:
                showToast(getString(R.string.net_no_connection));
                mTvNotNetwork.setText(R.string.string_not_network);
                mTvNotNetwork.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void queryEvectionTypeSuccess(CommonTypeBean bean) {
        if (bean != null) {
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

    }

    @Override
    public void queryDurationSuccess(QueryMonoBean bean) {
        if (bean != null) {
            if (isNextDate) {
                isQueryDuration = true;
                mTv_duration_next_number.setText(bean.getData());
            } else {
                mTv_duration_number.setText(bean.getData());
                isNextDuration = true;
            }
        }
    }

    @Override
    public void queryDurationFailure(int errorCode, String str) {

    }

    @Override
    public void submitEvectionApplySuccess(String str) {
        finish();
        showToast("提交申请成功!");
    }

    @Override
    public void submitEvectionApplyFailure(int errorCode, String str) {
        switch (errorCode) {
            case -1:
                showToast(getString(R.string.string_not_network));
                return;
        }
        Toast.makeText(this, "提交申请失败!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void addWorkApplySuccess(String bean) {
        finish();
        showToast("提交申请成功!");
    }

    @Override
    public void addWorkApplyFailure(int errorCode, String str) {
        switch (errorCode) {
            case -1:
                showToast(getString(R.string.string_not_network));
                return;
        }
        Toast.makeText(this, "提交申请失败!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void submitFurloughSuccess(String bean) {
        finish();
        showToast("提交申请成功!");
    }

    @Override
    public void submitFurloughFailure(int errorCode, String str) {
        switch (errorCode) {
            case -1:
                showToast(getString(R.string.string_not_network));
                return;
        }
        Toast.makeText(this, "提交申请失败!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void findSignReasonSuccess(SingReasonBean bean) {
        if (bean != null) {
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

    }

    @Override
    public void registrationCardSuccess(String bean) {
        showToast("提交申请成功!");
        finish();
    }

    @Override
    public void registrationCardFailure(int errorCode, String str) {
        switch (errorCode) {
            case -1:
                showToast(getString(R.string.string_not_network));
                return;
        }
        Toast.makeText(this, "提交申请失败!", Toast.LENGTH_SHORT).show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void removeDeatls(SelectedTypeBean bean) {
        switch (bean.getEvent()) {
            case "removeDetail": //删除明细
                mBtnAddDetails.setVisibility(View.VISIBLE);
                mBtnAddDetails.setClickable(true);
                break;
            case "selectedType": //选择类型
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
            case "showDialog":
                NonTokenDialog();
                break;
            case "selectedDate":
                if (bean.getPosition() == 0) {
                    if (bean.getIsBegin() == 0) {
                        mBegin_time.setText(bean.getSelectedType());
                    } else {
                        mEnd_time.setText(bean.getSelectedType());
                        if (!TextUtils.isEmpty(mTvCoderNumber.getText().toString().trim())) {
                            isNextDate = false;
                            mBeginDate = mBegin_time.getText().toString().replace(" ", "%20");
                            mEndDate = mEnd_time.getText().toString().replace(" ", "%20");
                        }
                        switch (getIntent().getIntExtra("type", -1)) {
                            case 0:
                                mPresenter.queryDuration(mBeginDate, mEndDate, REQUESTCODE2, mTvCoderNumber.getText().toString());
                                break;
                            case 1:
                                mPresenter.queryDuration(mBeginDate, mEndDate, REQUESTCODE4, mTvCoderNumber.getText().toString());
                                break;
                            case 2:
                                mPresenter.queryDuration(mBeginDate, mEndDate, REQUESTCODE3, mTvCoderNumber.getText().toString());
                                break;
                        }
                    }
                } else {
                    if (bean.getIsBegin() == 0) {
                        mNext_begin_time.setText(bean.getSelectedType());
                    } else {
                        mNext_end_time.setText(bean.getSelectedType());
                        if (!TextUtils.isEmpty(mTvCoderNumber.getText().toString().trim())) {
                            isNextDate = true;
                            mNext_begin_date = mNext_begin_time.getText().toString().replace(" ", "%20");
                            mNext_end_date = mNext_end_time.getText().toString().replace(" ", "%20");
                        }
                        switch (getIntent().getIntExtra("type", -1)) {
                            case 0:
                                mPresenter.queryDuration(mNext_begin_date, mNext_end_date, REQUESTCODE2, mTvCoderNumber.getText().toString());
                                break;
                            case 1:
                                mPresenter.queryDuration(mNext_begin_date, mNext_end_date, REQUESTCODE4, mTvCoderNumber.getText().toString());
                                break;
                            case 2:
                                mPresenter.queryDuration(mNext_begin_date, mNext_end_date, REQUESTCODE3, mTvCoderNumber.getText().toString());
                                break;
                        }
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
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void onSelected(String date, int position) {
        mTvCommonalityTypeDate.setText(date + " ");
        mYearPosition = position;
    }
}
