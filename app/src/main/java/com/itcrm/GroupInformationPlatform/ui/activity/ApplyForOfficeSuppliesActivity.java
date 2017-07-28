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
import com.itcrm.GroupInformationPlatform.R;
import com.itcrm.GroupInformationPlatform.common.Api;
import com.itcrm.GroupInformationPlatform.manager.AppConfig;
import com.itcrm.GroupInformationPlatform.ui.PermissionListener;
import com.itcrm.GroupInformationPlatform.ui.base.BaseActivity;
import com.itcrm.GroupInformationPlatform.utils.IflytekUtil;
import com.itcrm.GroupInformationPlatform.utils.LogUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.DatePicker;

/**
 * ProjectName: dev-beta-v1.0.1
 * PackageName: com.itcrm.GroupInformationPlatform.ui.activity
 * Author:Created by Tsui on Date:2016/11/15 17:48
 * Description：办公用品申请
 */
public class ApplyForOfficeSuppliesActivity extends BaseActivity {
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.tv_select_date)
    TextView mTvSelectDate;

    @Bind(R.id.et_office_supplies_reason_content)
    EditText mEtContent;

    @Bind(R.id.ll_approval_container)
    LinearLayout mLlContainer;
    private DatePicker picker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_office_supplies);
        ButterKnife.bind(this);
        initToolBar();
        setTranslucentStatus(this);
        initWidget();
        loadData();
    }

    private void initWidget() {
        mTvSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDoneDatePicker(mTvSelectDate);
            }
        });
    }

    private void loadData() {
        showLoadingView();
        HttpParams params = new HttpParams();
        params.put("uid", AppConfig.getAppConfig(this).getPrivateUid());
        params.put("token", AppConfig.getAppConfig(this).getPrivateToken());
        params.put("appr_id", "2");
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
                View view = LayoutInflater.from(this).inflate(R.layout.travel_entry_approvel_single
                        , null);
                SimpleDraweeView sdv = (SimpleDraweeView) view.findViewById(R.id.user_portrait);
                sdv.setImageURI("http://"+Uri.parse(portrait));
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

    private void showDoneDatePicker(final TextView tv) {
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

                tv.setText(year + "/" + month + "/" + day);

            }
        });
        picker.show();
    }


    private void initToolBar() {
        if (mToolbar == null) {
            return;
        }
        setTitle("");//必须在setSupportActionBar之前调用
        mToolbar.setTitleTextColor(Color.parseColor("#000000"));
        setSupportActionBar(mToolbar);
        mTvTitle.setText("办公用品申请");
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


    @OnClick({R.id.btn_voice_input, R.id.btn_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_voice_input:
                showDialog();
                break;
            case R.id.btn_submit:
                if (checkSubmitData()){
                    submit();}

                break;

        }
    }

    private boolean checkSubmitData() {
        if (mEtContent.getText().toString().trim().equals("")) {
            showToast("请输入要申请的物品");
            return false;
        }
        if (mTvSelectDate.getText().toString().equals("点击选择时间")) {
            showToast("请选择申领时间");
            return false;
        }
        return true;
    }

    private void submit() {
        showLoadingView();
        HttpParams params = new HttpParams();
        params.put("uid", AppConfig.getAppConfig(this).getPrivateUid());
        params.put("token", AppConfig.getAppConfig(this).getPrivateToken());
        params.put("article_name", mEtContent.getText().toString().trim());
        params.put("application_time", mTvSelectDate.getText().toString());

        LogUtils.e("uid"+ AppConfig.getAppConfig(this).getPrivateUid());
        LogUtils.e("token"+ AppConfig.getAppConfig(this).getPrivateToken());
        LogUtils.e("article_name"+mEtContent.getText().toString().trim());
        LogUtils.e("application_time"+mTvSelectDate.getText().toString().replace("/","-"));

        initKjHttp().post(Api.POSTOFFICE_APPROVAL, params, new HttpCallBack() {

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
                catchWarningByCode(errorNo);
            }
        });
    }

    private void showDialog() {
        requestRunTimePermission(new String[]{Manifest.permission.RECORD_AUDIO}, new PermissionListener() {
            @Override
            public void onGranted() {
                IflytekUtil iflytekUtil = new IflytekUtil(ApplyForOfficeSuppliesActivity.this, mEtContent);
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


}
