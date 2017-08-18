package com.shanlin.oa.ui.activity;

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
import com.shanlin.oa.ui.base.BaseActivity;
import com.shanlin.oa.utils.AndroidAdjustResizeBugFix;
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
 * ProjectName: dev-beta-v1.0.1
 * PackageName: com.itcrm.GroupInformationPlatform.ui.activity
 * Author:Created by Tsui on Date:2016/11/9 15:59
 * Description:
 */
public class FindPassWordActivity extends BaseActivity {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.et_email)
    EditText mEtEmail;
    @Bind(R.id.tv_reminder)
    TextView mEtReminder;
    @Bind(R.id.btn_get_pwd)
    TextView mBtnGetPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_password);
        ButterKnife.bind(this);
        initToolBar();
        //CXP添加,修复软键盘adjustResize属性的bug
        AndroidAdjustResizeBugFix.assistActivity(this);
        setTranslucentStatus(this);
    }

    private void initToolBar() {
        if (toolbar == null) {
            return;
        }
        setTitle("");//必须在setSupportActionBar之前调用
        toolbar.setTitleTextColor(Color.parseColor("#000000"));
        setSupportActionBar(toolbar);
        tvTitle.setText("找回密码");
        Toolbar.LayoutParams lp = new Toolbar.LayoutParams(
                Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        tvTitle.setLayoutParams(lp);

        toolbar.setNavigationIcon(R.drawable.toolbar_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @OnClick(R.id.btn_get_pwd)
    public void onClick() {
        if (check()) {
            showLoadingView();
            HttpParams params = new HttpParams();
            params.put("email", mEtEmail.getText().toString().trim());
            initKjHttp().post(Api.FIND_PASSWORD, params, new HttpCallBack() {
                @Override
                public void onFinish() {
                    super.onFinish();
                    hideLoadingView();
                }

                @Override
                public void onSuccess(String t) {
                    super.onSuccess(t);
                    hideLoadingView();
                    JSONObject jo = null;
                    LogUtils.e(t.toString());
                    try {
                        jo = new JSONObject(t);
                        if (Api.getCode(jo) == Api.SEND_FAILD_TRY_AGIN) {
                            mEtReminder.setText("邮件发送失败,请重试");
                        } else if (Api.getCode(jo) == Api.RESPONSES_CODE_OK) {
                            mEtReminder.setText("随机密码已经发送至您的邮箱，请登录后设置新的密码");
                            mBtnGetPwd.setClickable(false);
                            mBtnGetPwd.setBackgroundColor(Color.parseColor("#999999"));
                        } else if ((Api.getCode(jo) == Api.RESPONSES_CODE_UID_NULL)) {
                            catchWarningByCode(Api.getCode(jo));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(int errorNo, String strMsg) {
                    hideLoadingView();
                    LogUtils.e(strMsg);
                    super.onFailure(errorNo, strMsg);
                }
            });
        }

    }

    private boolean check() {
        if (mEtEmail.getText().toString().equals("")) {
            showToast("请输入您的邮箱帐号");
            return false;
        }
        if (!Utils.isRegex(Constants.Regex.REGEX_EMAIL, mEtEmail.getText().toString())) {
            showToast("请您输入正确的邮箱帐号");
            return false;
        }

        return true;
    }
}
