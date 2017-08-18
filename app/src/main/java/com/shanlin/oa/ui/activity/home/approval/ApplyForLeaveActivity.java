package com.shanlin.oa.ui.activity.home.approval;

import android.Manifest;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.shanlin.oa.R;
import com.shanlin.oa.common.Api;
import com.shanlin.oa.manager.AppConfig;
import com.shanlin.oa.listener.PermissionListener;
import com.shanlin.oa.ui.base.BaseActivity;
import com.shanlin.oa.utils.DateUtils;
import com.shanlin.oa.thirdParty.iflytek.IflytekUtil;
import com.shanlin.oa.utils.LogUtils;
import com.shanlin.oa.utils.Utils;
import com.shanlin.pickerview.TimePickerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.TimePicker;

/**
 * ProjectName: dev-beta-v1.0.1
 * PackageName: com.itcrm.GroupInformationPlatform.ui.activity
 * Author:Created by Tsui on Date:2016/11/15 16:37
 * Description:请假申请
 */
public class ApplyForLeaveActivity extends BaseActivity {
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.et_leave_reason_content)
    EditText mEtLeaveContent;
    @Bind(R.id.layout_root)
    LinearLayout mRootView;
    @Bind(R.id.tv_travel_start_time)
    TextView tvTravelStartTime;
    @Bind(R.id.tv_type)
    TextView tvType;
    @Bind(R.id.tv_travel_end_time)
    TextView tvTravelEndTime;
    @Bind(R.id.tv_leave_days)
    EditText tvLeaveDurations;
    @Bind(R.id.user_portrait)
    SimpleDraweeView mUserPortrait;
    @Bind(R.id.tv_leader_name)
    TextView mTvLeaderName;
    @Bind(R.id.ll_approval_container)
    LinearLayout mLlContainer;
    private TimePicker picker;
    private PopupWindow popupWindowTYpe;
    private TimePickerView pvTime;
    private String beginTimeTemp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_entry);
        ButterKnife.bind(this);
        initToolBar();
        setTranslucentStatus(this);
        initData();
        loadData();
    }

    private void loadData() {
        showLoadingView();
        HttpParams params = new HttpParams();
        params.put("uid", AppConfig.getAppConfig(this).getPrivateUid());
        params.put("token", AppConfig.getAppConfig(this).getPrivateToken());
        params.put("appr_id", "3");
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
                LogUtils.e("leaveEntryActivity-->" + t);
                try {
                    JSONObject jo = new JSONObject(t);
                    switch (Api.getCode(jo)) {
                        case Api.RESPONSES_CODE_OK:
                            JSONArray ja = Api.getDataToJSONArray(jo);
                            setDataForWidget(ja);
                            break;
                        case Api.RESPONSES_CODE_DATA_EMPTY:
                            showEmptyView(mRootView, "内容为空", 0, false);
                            break;
                        case Api.RESPONSES_CODE_TOKEN_NO_MATCH:
                            catchWarningByCode(Api.getCode(jo));
                            break;
                        case Api.RESPONSES_CODE_UID_NULL:
                            catchWarningByCode(Api.getCode(jo));
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    LogUtils.e(e);
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
                LogUtils.e("onFailure->" + strMsg);
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
                LogUtils.e("头像url——》" + portrait);
                TextView tvLeader = (TextView) view.findViewById(R.id.tv_leader_name);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout
                        .LayoutParams.WRAP_CONTENT,
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

    private void initData() {
        //解决editText与ScrollView之间的冲突
        mEtLeaveContent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                //触摸的是EditText并且当前EditText可以滚动则将事件交给EditText处理；否则将事件交由其父类处理
                if ((view.getId() == R.id.et_leave_reason_content && canVerticalScroll(mEtLeaveContent))) {
                    view.getParent().requestDisallowInterceptTouchEvent(true);
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        view.getParent().requestDisallowInterceptTouchEvent(false);
                    }
                }
                return false;

            }
        });
    }

    /**
     * EditText竖直方向是否可以滚动
     *
     * @param editText 需要判断的EditText
     * @return true：可以滚动  false：不可以滚动
     */

    private boolean canVerticalScroll(EditText editText) {
        //滚动的距离
        int scrollY = editText.getScrollY();
        //控件内容的总高度
        int scrollRange = editText.getLayout().getHeight();
        //控件实际显示的高度
        int scrollExtent = editText.getHeight() - editText.getCompoundPaddingTop() - editText.getCompoundPaddingBottom();
        //控件内容总高度与实际显示高度的差值
        int scrollDifference = scrollRange - scrollExtent;

        if (scrollDifference == 0) {
            return false;
        }

        return (scrollY > 0) || (scrollY < scrollDifference - 1);
    }

    private boolean check() {
        if (tvType.getText().toString().trim().equals("点击选择")) {
            showToast("请选择请假类型");
            return false;
        }
        if (tvTravelStartTime.getText().toString().trim().equals("点击选择时间")
                || tvTravelStartTime.getText().toString().trim().equals("点击选择时间")) {
            showToast("请选择时间");
            return false;
        }
        if (!DateUtils.judeDateOrder(tvTravelStartTime.getText().toString().trim(), tvTravelEndTime.getText().toString().trim())) {
            showToast("结束时间不能早于开始时间");
            return false;
        }
        if (mEtLeaveContent.getText().toString().trim().equals("")) {
            showToast("请假原因不能为空");
            return false;
        }

        return true;
    }

    private void submit() {
        showLoadingView();


        HttpParams params = new HttpParams();
        params.put("day", tvLeaveDurations.getText().toString().trim());
        params.put("end_time", tvTravelEndTime.getText().toString().trim());
        params.put("remark", mEtLeaveContent.getText().toString().trim());
        params.put("start_time", tvTravelStartTime.getText().toString().trim());
        params.put("token", AppConfig.getAppConfig(this).getPrivateToken());
        params.put("uid", AppConfig.getAppConfig(this).getPrivateUid());
        params.put("type", getType());
        initKjHttp().post(Api.POSTOFF_WORK_APPROVAL, params, new HttpCallBack() {

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
                    }else if ((Api.getCode(jo) == Api.RESPONSES_CODE_UID_NULL)){
                        catchWarningByCode(Api.getCode(jo));
                    }else {
                        showToast(Api.getInfo(jo));
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
                LogUtils.e("onFailure-->" + strMsg);
                catchWarningByCode(errorNo);
            }
        });
    }

    @NonNull
    private String getType() {
        String type = tvType.getText().toString().trim();
        if (type.equals("年假")) {
            type = "1";
        } else if (type.equals("婚假")) {
            type = "2";
        } else if (type.equals("产假")) {
            type = "3";
        } else if (type.equals("陪产假")) {
            type = "4";
        } else if (type.equals("丧假")) {
            type = "5";
        } else if (type.equals("病假")) {
            type = "6";
        } else if (type.equals("事假")) {
            type = "7";
        } else if (type.equals("调休")) {
            type = "8";
        }
        return type;
    }

    private void initToolBar() {
        if (mToolbar == null) {
            return;
        }
        setTitle("");//必须在setSupportActionBar之前调用
        mToolbar.setTitleTextColor(Color.parseColor("#000000"));
        setSupportActionBar(mToolbar);
        mTvTitle.setText("请假申请");
        Toolbar.LayoutParams lp = new Toolbar.LayoutParams(
                Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        mTvTitle.setLayoutParams(lp);
        mToolbar.setNavigationIcon(R.drawable.toolbar_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((!(mEtLeaveContent.getText().toString().equals("")))) {
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
                if ((!(mEtLeaveContent.getText().toString().equals("")))) {
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
                IflytekUtil iflytekUtil = new IflytekUtil(ApplyForLeaveActivity.this, mEtLeaveContent);
                iflytekUtil.showIatDialog();
            }

            @Override
            public void onDenied() {
                showToast("录音权限被拒绝！请手动设置");
            }
        });

    }

    @OnClick({R.id.btn_voice_input, R.id.parting_line, R.id.tv_travel_start_time,
            R.id.tv_travel_end_time, R.id.btn_submit, R.id.tv_type})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_voice_input:
                showDialog();
                break;
            case R.id.tv_type:
                //隐藏输入框
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘
                showPopDialog();
                break;
            case R.id.btn_submit:
                if (check()) {
                    submit();
                }
                break;
            case R.id.tv_travel_start_time:
                showDoneDatePicker(tvTravelStartTime, false);
                break;
            case R.id.tv_travel_end_time:
                showDoneDatePicker(tvTravelEndTime, true);
                break;

        }
    }

    private void showPopDialog() {
        final ListView listView = new ListView(this);
        listView.setBackgroundColor(Color.parseColor("#ffffff"));
        listView.setPadding(0, 20, 0, 20);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                Utils.dip2px(140),
                Utils.dip2px(100)
        );
        listView.setLayoutParams(params);
        final String[] array = {"事假", "年假", "调休", "病假", "婚假", "产假", "陪产假", "丧假"};
        listView.setAdapter(new ArrayAdapter<>(this, R.layout.leave_approval_list_item, array));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                tvType.setText(array[position]);
                LogUtils.e("当前请假类型选择的是--》" + array[position]);

                popupWindowTYpe.dismiss();
            }
        });
        popupWindowTYpe = new PopupWindow(listView, FrameLayout.LayoutParams
                .MATCH_PARENT, Utils.dip2px(206), false);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.7f;
        getWindow().setAttributes(lp);
        popupWindowTYpe.setOutsideTouchable(true);
        popupWindowTYpe.setBackgroundDrawable(new BitmapDrawable());
        popupWindowTYpe.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });
        popupWindowTYpe.showAtLocation(mRootView, Gravity.BOTTOM, 0, 0);
    }

    private void showDoneDatePicker(final TextView tv, final boolean isEndTime) {
        if (pvTime == null) {
            pvTime = new TimePickerView(this, TimePickerView.Type.ALL);
            Class<? extends TimePickerView> pvTimeClass = pvTime.getClass();
            try {
                TimePickerView timePickerView = pvTimeClass.newInstance();
                try {
                    Field btnSubmit = pvTimeClass.getDeclaredField("btnSubmit");


                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }

            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }
        Calendar calendar = Calendar.getInstance();
        pvTime.setRange(calendar.get(Calendar.YEAR) - 1, calendar.get(Calendar.YEAR) + 1);//要在setTime 之前才有效果哦
        pvTime.setTime(new Date());
        pvTime.setCyclic(false);
        pvTime.setCancelable(true);
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {
                tv.setText(DateUtils.getTime(date).replace("-", "/"));
                if (isEndTime) {
                    boolean isOrder = DateUtils.judeDateOrder(tvTravelStartTime.getText()
                            .toString(), DateUtils.getTime(date));
                    if (isOrder) {
                        int hourD_value = DateUtils.getHourD_Value(tvTravelStartTime.getText()
                                .toString(), DateUtils.getTime(date));
                        tvLeaveDurations.setText(String.valueOf(hourD_value));
                        tvLeaveDurations.setFocusable(true);
                        tvLeaveDurations.setFocusableInTouchMode(true);
                        tvLeaveDurations.requestFocus();
                    } else {
                        showToast("结束时间必须晚于开始时间");
                        tv.setText("点击选择时间");
                        tvLeaveDurations.setText("");
                    }
                }
            }
        });
        pvTime.show();

    }
    /*private void showDoneDatePicker(final TextView tv) {
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

                tv.setText(year + "-" + month + "-" + day);

            }
        });
        picker.show();
    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }


}
