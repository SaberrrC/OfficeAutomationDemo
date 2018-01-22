package com.shanlinjinrong.oa.ui.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.manager.AppConfig;
import com.shanlinjinrong.oa.manager.AppManager;
import com.shanlinjinrong.oa.ui.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EmailConfirmFinishActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_confirm_finish);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_sure)
    public void onViewClicked() {
        AppConfig.getAppConfig(this).clearLoginInfo();
        startActivity(new Intent(this, LoginActivity.class));
        AppManager.sharedInstance().finishAllActivity();
    }
}
