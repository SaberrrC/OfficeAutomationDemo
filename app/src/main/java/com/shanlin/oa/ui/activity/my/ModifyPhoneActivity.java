package com.shanlin.oa.ui.activity.my;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.shanlin.oa.R;
import com.shanlin.oa.common.Api;
import com.shanlin.oa.common.Constants;
import com.shanlin.oa.manager.AppConfig;
import com.shanlin.oa.ui.base.BaseActivity;
import com.shanlin.oa.utils.LogUtils;
import com.shanlin.oa.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <h3>Description: 修改电话 </h3>
 * <b>Notes:</b> Created by KevinMeng on 2016/8/31.<br />
 */
public class ModifyPhoneActivity extends BaseActivity {

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.toolbar_text_btn)
    TextView toolbarTextBtn;
    @Bind(R.id.tv_tips)
    TextView tvTips;
    @Bind(R.id.user_phone)
    EditText userPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_phone);
        ButterKnife.bind(this);
        initToolBar();
        setTranslucentStatus(this);
        userPhone.setText(AppConfig.getAppConfig(this).get(AppConfig.PREF_KEY_PHONE));
        userPhone.setSelection(userPhone.getText().length());
    }

    @OnClick(R.id.toolbar_text_btn)
    public void onClick() {

        tvTips.setText("");

        final String newNumber = userPhone.getText().toString();
        //如果号码与之前的一样，不去访问网络
        if (newNumber.equals(AppConfig.getAppConfig(this).get(AppConfig.PREF_KEY_PHONE))) {
            showToast("不能与之前的电话号码相同");
            return;
        }
        //修改按钮
        if (check()) {
            HttpParams params = new HttpParams();
            params.put("uid", AppConfig.getAppConfig(this).getPrivateUid());
            params.put("token", AppConfig.getAppConfig(this).getPrivateToken());
            params.put("department_id", AppConfig.getAppConfig(this).getDepartmentId());
            params.put("phone", newNumber);
            initKjHttp().post(Api.PHONENUMBER_UPDATE, params, new HttpCallBack() {
                @Override
                public void onSuccess(String t) {
                    JSONObject jo = null;
                    LogUtils.e(t);
                    try {
                        jo = new JSONObject(t);
                        int code = Api.getCode(jo);

                        if (code == Api.RESPONSES_CODE_OK) {

                            showToast("修改成功");
                            AppConfig.getAppConfig(ModifyPhoneActivity.this).set(
                                    AppConfig.PREF_KEY_PHONE, newNumber);
                            setResult(RESULT_OK);
                            finish();
                        } else if (Api.getCode(jo) == Api.RESPONSES_CODE_TOKEN_NO_MATCH) {
                            catchWarningByCode(Api.getCode(jo));
                        } else if (Api.getCode(jo) == Api.RESPONSES_CODE_UID_NULL) {
                            catchWarningByCode(Api.getCode(jo));
                        } else {
                            showToast(Api.getInfo(jo));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    super.onSuccess(t);
                }

                @Override
                public void onFailure(int errorNo, String strMsg) {
                    super.onFailure(errorNo, strMsg);
                    catchWarningByCode(errorNo);
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                }
            });
        }
    }

    private boolean check() {

        if (userPhone.getText().toString().equals("")) {
            tvTips.setText("请输入您的电话号码");
            return false;
        }
        if (!Utils.isRegex(Constants.Regex.REGEX_PHONE, (userPhone.getText()).toString())) {
            tvTips.setText("手机号码格式不正确");
            return false;
        }
        return true;
    }

    private void initToolBar() {
        if (toolbar == null) {
            return;
        }
        setTitle("");//必须在setSupportActionBar之前调用
        toolbar.setTitleTextColor(Color.parseColor("#000000"));
        setSupportActionBar(toolbar);
        tvTitle.setText("资料修改");
        Toolbar.LayoutParams lp = new Toolbar.LayoutParams(
                Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        tvTitle.setLayoutParams(lp);

        toolbarTextBtn.setText("修改");
        toolbarTextBtn.setVisibility(View.VISIBLE);
        toolbar.setNavigationIcon(R.drawable.toolbar_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}