package com.shanlinjinrong.oa.ui.activity.my;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.manager.AppConfig;
import com.shanlinjinrong.oa.ui.activity.my.contract.FeedbackActivityContract;
import com.shanlinjinrong.oa.ui.activity.my.presenter.FeedbackActivityPresenter;
import com.shanlinjinrong.oa.ui.base.MyBaseActivity;
import com.shanlinjinrong.oa.utils.StringUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <h3>Description: 用户反馈</h3>
 * <b>Notes:</b> Created by KevinMeng on 2016/8/26.<br/>
 */
public class FeedbackActivity extends MyBaseActivity<FeedbackActivityPresenter> implements FeedbackActivityContract.View {

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.toolbar_text_btn)
    TextView toolbarTextBtn;
    @Bind(R.id.feedback_text)
    EditText feedbackText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        ButterKnife.bind(this);
        initToolBar();
        setTranslucentStatus(this);
        //限制EditText长度
        feedbackText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(200)});
    }

    @Override
    protected void initInject() {
         getActivityComponent().inject(this);
    }

    @OnClick(R.id.toolbar_text_btn)
    public void onClick() {
        String tvContent = this.feedbackText.getText().toString();
        if (feedbackText.getText().toString().equals("")) {
            showToast("提交内容不能为空");
        } else if(StringUtils.isEmoji(tvContent)){
            showToast("反馈内容不能包含特殊字符");
        } else {
            sendFeedback();
        }
    }

    private void sendFeedback() {
        showLoadingView();
        mPresenter.sendFeedback(AppConfig.getAppConfig(this).getDepartmentId(),feedbackText.getText().toString());
    }

    private void initToolBar() {
        if (toolbar == null) {
            return;
        }
        setTitle("");//必须在setSupportActionBar之前调用
        toolbar.setTitleTextColor(Color.parseColor("#000000"));
        setSupportActionBar(toolbar);
        tvTitle.setText("用户反馈");
        Toolbar.LayoutParams lp = new Toolbar.LayoutParams(
                Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        tvTitle.setLayoutParams(lp);

        toolbarTextBtn.setText("提交");
        toolbarTextBtn.setVisibility(View.VISIBLE);
        toolbar.setNavigationIcon(R.drawable.toolbar_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (feedbackText.getText().toString().equals("")) {
                    finish();
                } else {
                    confirmBackTips();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (feedbackText.getText().toString().equals("")) {
            finish();
        } else {
            confirmBackTips();
        }
    }

    private void confirmBackTips() {
        @SuppressLint("InflateParams")
        View dialogView = LayoutInflater.from(this).inflate(R.layout.public_dialog, null);
        TextView title = (TextView) dialogView.findViewById(R.id.title);
        title.setText("提示");
        TextView message = (TextView) dialogView.findViewById(R.id.message);
        message.setText("内容未保存是否退出?");

        final AlertDialog alertDialog = new AlertDialog.Builder(this,
                R.style.AppTheme_Dialog).create();
        alertDialog.setView(dialogView);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "退出",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        dialog.dismiss();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(
                getResources().getColor(R.color.btn_text_logout));
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(
                getResources().getColor(R.color.btn_text_logout));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public void feedbackSuccess() {
        showToast("感谢您的反馈");
        finish();
    }

    @Override
    public void feedbackFinish() {
        hideLoadingView();
    }

    @Override
    public void feedbackFailed(int errorNo, String strMsg) {
        showToast(strMsg);
    }

    @Override
    public void uidNull(int code) {
        catchWarningByCode(code);
    }
}