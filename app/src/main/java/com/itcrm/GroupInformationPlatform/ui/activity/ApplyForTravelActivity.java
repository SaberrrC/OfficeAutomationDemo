package com.itcrm.GroupInformationPlatform.ui.activity;

import android.Manifest;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.itcrm.GroupInformationPlatform.R;
import com.itcrm.GroupInformationPlatform.common.Api;
import com.itcrm.GroupInformationPlatform.manager.AppConfig;
import com.itcrm.GroupInformationPlatform.model.TravalSingle;
import com.itcrm.GroupInformationPlatform.ui.PermissionListener;
import com.itcrm.GroupInformationPlatform.ui.base.BaseActivity;
import com.itcrm.GroupInformationPlatform.utils.DateUtils;
import com.itcrm.GroupInformationPlatform.utils.IflytekUtil;
import com.itcrm.GroupInformationPlatform.utils.LogUtils;
import com.itcrm.GroupInformationPlatform.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.DatePicker;

/**
 * ProjectName: dev-beta-v1.0.1
 * PackageName: com.itcrm.GroupInformationPlatform.ui.activity
 * Author:Created by Tsui on Date:2016/11/15 14:37
 * Description:差旅申请
 */
public class ApplyForTravelActivity extends BaseActivity {
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.et_travel_reason_content)
    EditText mEtContent;
    @Bind(R.id.tv_travel_start_time)
    TextView tvTravelStartTime;
    @Bind(R.id.tv_travel_end_time)
    TextView tvTravelEndTime;
    @Bind(R.id.et_travel_duration)
    EditText etTravelDuration;
    @Bind(R.id.ll_travel_list)
    LinearLayout mLlTravelList;
    @Bind(R.id.ll_approval_container)
    LinearLayout mLlContainer;
    private DatePicker picker;
    private int totalDays = 0;//总共的天数
    List<TravalSingle> tsList = new ArrayList<>();
    private String tempStartDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_entry);
        ButterKnife.bind(this);
        initToolBar();
        setTranslucentStatus(this);
        loadData();
    }

    private void loadData() {

        showLoadingView();
        HttpParams params = new HttpParams();
        params.put("uid", AppConfig.getAppConfig(this).getPrivateUid());
        params.put("token", AppConfig.getAppConfig(this).getPrivateToken());
        params.put("appr_id", "4");
        params.put("isleader", AppConfig.getAppConfig(this).get(AppConfig.PREF_KEY_IS_LEADER));
        params.put("oid", AppConfig.getAppConfig(this).getDepartmentId());

        initKjHttp().post(Api.GET_APPROVERS, params, new HttpCallBack() {

            @Override
            public void onPreStart() {
                super.onPreStart();

            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                LogUtils.e(t);
                try {
                    JSONObject jo = new JSONObject(t);
                    switch (Api.getCode(jo)) {
                        case Api.RESPONSES_CODE_OK:
                            JSONArray ja = Api.getDataToJSONArray(jo);
                            setDataForWidget(ja);
                            break;
                        case Api.RESPONSES_CODE_DATA_EMPTY:
//                                showEmptyView(mRootView, "内容为空", 0, false);
                            break;
                        case Api.RESPONSES_CODE_TOKEN_NO_MATCH:
                            catchWarningByCode(Api.getCode(jo));
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                hideLoadingView();
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);

                catchWarningByCode(errorNo);
            }
        });
    }

    private void setDataForWidget(JSONArray ja) {
        if (ja.length() > 0) {
            mLlContainer.removeAllViews();
        }
        for (int i = 0; i < ja.length(); i++) {
            try {
                String username = ja.getJSONObject(i).getString("username");
                String portrait = ja.getJSONObject(i).getString("portrait");
                View view = LayoutInflater.from(this).inflate(R.layout
                        .travel_entry_approvel_single, null);
                SimpleDraweeView sdv = (SimpleDraweeView) view.findViewById(R.id.user_portrait);

                sdv.setImageURI("http://"+Uri.parse(portrait));
                TextView tvLeader = (TextView) view.findViewById(R.id.tv_leader_name);
                LinearLayout.LayoutParams params = new LinearLayout
                        .LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(40, 0, 0, 0);
                params.gravity = Gravity.CENTER;
                tvLeader.setText(username);
                mLlContainer.addView(view, params);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }


    private void initToolBar() {
        if (mToolbar == null) {
            return;
        }
        setTitle("");//必须在setSupportActionBar之前调用
        mToolbar.setTitleTextColor(Color.parseColor("#000000"));
        setSupportActionBar(mToolbar);
        mTvTitle.setText("差旅申请");
        Toolbar.LayoutParams lp = new Toolbar.LayoutParams(
                Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        mTvTitle.setLayoutParams(lp);
        mToolbar.setNavigationIcon(R.drawable.toolbar_back);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((!(mEtContent.getText().toString().equals("")))) {
                    showBackTip("是否放弃编辑", "确定", "取消");
                } else {
                    finish();
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if ((!(mEtContent.getText().toString().equals("")))) {
                    showBackTip("是否放弃编辑", "确定", "取消");
                }
                break;
        }
        return super.onKeyDown(keyCode, event);
    }


    private void showDialog() {
        requestRunTimePermission(new String[]{Manifest.permission.RECORD_AUDIO}, new PermissionListener() {
            @Override
            public void onGranted() {
                IflytekUtil iflytekUtil = new IflytekUtil(ApplyForTravelActivity.this, mEtContent);
                iflytekUtil.showIatDialog();
            }

            @Override
            public void onDenied() {
                showToast("录音权限被拒绝！请手动设置");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.tv_travel_start_time, R.id.tv_travel_end_time, R.id.btn_voice_input,
            R.id.tv_add_travel, R.id.btn_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_travel_start_time:
                showDoneDatePicker(tvTravelStartTime, false);
                break;
            case R.id.tv_travel_end_time:
                showDoneDatePicker(tvTravelEndTime, true);
                break;
            case R.id.btn_voice_input:
                //语音录入
                showDialog();
                break;
            case R.id.tv_add_travel:
                //添加行程
                addTravel();
                break;
            case R.id.btn_submit:
                if (etTravelDuration.getText().toString().trim().equals("")) {
                    showToast("出差天数不能为空");
                    return;
                } else if (mEtContent.getText().toString().trim().equals("")) {
                    showToast("出差事由不能为空");
                    return;
                } else {
                    submit();

                }
                break;

        }
    }


    /**
     * 添加行程
     */
    private void addTravel() {
        LinearLayout llTravelView = (LinearLayout) LayoutInflater.from(this).inflate(
                R.layout.travel_entry_single, null);
        final TextView tvStart = (TextView) llTravelView.findViewById(R.id.tv_travel_start_time);
        final TextView tvEnd = (TextView) llTravelView.findViewById(R.id.tv_travel_end_time);
        tvStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDoneDatePicker(tvStart, false);
            }
        });
        tvEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDoneDatePicker(tvEnd, true);
            }
        });
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, Utils.dip2px(12), 0, 0);
        mLlTravelList.addView(llTravelView, params);
    }

    private void showDoneDatePicker(final TextView tv, final boolean isEndDate) {
        if (picker == null) {
            picker = new DatePicker(this, DatePicker.YEAR_MONTH_DAY);
        }
        Calendar cal = Calendar.getInstance();
        picker.setSelectedItem(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1,
                cal.get(Calendar.DAY_OF_MONTH));
        picker.setSubmitText("完成");
        picker.setSubmitTextColor(Color.parseColor("#2d9dff"));
        picker.setTextColor(Color.parseColor("#2d9dff"));
        picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {
                String date=year + "/" + month + "/" + day;
                tv.setText(date);
                if (isEndDate) {
                    totalDays=totalDays+DateUtils.getDayD_Value(tempStartDate,date);
                    etTravelDuration.setText(String.valueOf(totalDays));

                    etTravelDuration.setSelection(String.valueOf(totalDays).length());

                }else{
                    tempStartDate=date;
                }

            }
        });
        picker.show();
    }

    private void submit() {
        if (!getTravalTotalData()) {
            return;
        }
        showLoadingView();
        HttpParams params = new HttpParams();
        params.put("uid", AppConfig.getAppConfig(this).getPrivateUid());
        params.put("token", AppConfig.getAppConfig(this).getPrivateToken());
        Gson gson = new Gson();
        LogUtils.e("gson->" + gson.toJson(tsList));
        params.put("text", gson.toJson(tsList));

        params.put("day", etTravelDuration.getText().toString().trim());
        params.put("remark", mEtContent.getText().toString().trim());
        LogUtils.e("uid->" + AppConfig.getAppConfig(this).getPrivateUid() + "token->" + AppConfig
                .getAppConfig(this)
                .getPrivateToken() + "day->" + etTravelDuration.getText().toString().trim() + "remark->" + mEtContent.getText().toString().trim());
        initKjHttp().post(Api.POST_TRIPA_PPROVAL, params, new HttpCallBack() {

            @Override
            public void onPreStart() {
                super.onPreStart();

            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                LogUtils.e(t);
                try {
                    JSONObject jo = new JSONObject(t);
                    if ((Api.getCode(jo) == Api.RESPONSES_CODE_OK)) {
                        showToast("发送成功");
                        finish();
                    } else {
                        tsList.clear();
                        showToast(Api.getInfo(jo));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                tsList.clear();
                hideLoadingView();
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                LogUtils.e(strMsg);
                tsList.clear();
                catchWarningByCode(errorNo);
            }
        });
    }

    public boolean getTravalTotalData() {
        int childCount = mLlTravelList.getChildCount();
        LogUtils.e("mLlTravelList-->childCount:" + childCount);
        for (int i = 0; i < childCount; i++) {
            EditText etPlace = (EditText) mLlTravelList.getChildAt(i).findViewById(R.id.et_travel_place_name);
            String place = etPlace.getText().toString().trim();
            if (place.equals("")) {
                showToast("出差地点不能为空");
                return false;
            }
            TextView tvStartTime = (TextView) mLlTravelList.getChildAt(i).findViewById(R.id.tv_travel_start_time);
            String startTime = tvStartTime.getText().toString().trim();
            if (startTime.equals("") || startTime.equals("点击选择时间")) {
                showToast("开始时间不能为空");
                return false;
            }
            TextView tvEndTime = (TextView) mLlTravelList.getChildAt(i).findViewById(R.id.tv_travel_end_time);
            String endTime = tvEndTime.getText().toString().trim();
            if (endTime.equals("") || endTime.equals("点击选择时间")) {
                showToast("结束时间不能为空");
                return false;
            }

            EditText etTravelTool = (EditText) mLlTravelList.getChildAt(i).findViewById(R.id.et_travel_tool);
            String travelTool = etTravelTool.getText().toString().trim();
            if (travelTool.equals("")) {
                showToast("出差工具不能为空");
                return false;
            }

            TravalSingle ts = new TravalSingle(place, startTime,
                    endTime, travelTool);
            int dayD_value = DateUtils.getDayD_Value(startTime, endTime);

            tsList.add(ts);
        }
        return true;
    }
}
