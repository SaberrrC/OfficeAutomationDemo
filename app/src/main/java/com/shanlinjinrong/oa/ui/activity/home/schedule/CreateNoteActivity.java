package com.shanlinjinrong.oa.ui.activity.home.schedule;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.common.Constants;
import com.shanlinjinrong.oa.listener.PermissionListener;
import com.shanlinjinrong.oa.manager.AppConfig;
import com.shanlinjinrong.oa.thirdParty.iflytek.IflytekUtil;
import com.shanlinjinrong.oa.ui.activity.home.schedule.contract.CreateNoteContract;
import com.shanlinjinrong.oa.ui.activity.home.schedule.presenter.CreateNotePresenter;
import com.shanlinjinrong.oa.ui.activity.main.MainController;
import com.shanlinjinrong.oa.ui.base.HttpBaseActivity;
import com.shanlinjinrong.oa.utils.DateUtils;
import com.shanlinjinrong.oa.utils.LogUtils;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.DatePicker;

/**
 * ProjectName: dev-beta-v1.0.1
 * PackageName: com.itcrm.GroupInformationPlatform.ui.activity
 * Author:Created by Tsui on Date:2016/12/8 15:17
 * Description:创建记事本,也是编译记事本界面
 */
public class CreateNoteActivity extends HttpBaseActivity<CreateNotePresenter> implements CreateNoteContract.View {
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.toolbar_text_btn)
    TextView toolbarTextBtn;
    @Bind(R.id.tv_note_date)
    TextView mTvNoteDate;
    @Bind(R.id.et_note_content)
    EditText mEtNoteContent;
    private DatePicker picker;
    private IflytekUtil iflyUtil;
    private Intent intent;
    private String note_id;
    private String mode = "";
    private String noteContent;
    private String noteDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);
        ButterKnife.bind(this);
        initData();
        initToolBar();
        setTranslucentStatus(this);
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    private void initData() {
        intent = getIntent();
        note_id = intent.getStringExtra("note_id");
        noteDate = intent.getStringExtra("noteData");
        if (null != noteDate && !noteDate.equals("")) {
            mTvNoteDate.setText(noteDate);
        } else {
            mTvNoteDate.setText("点击选择日期");

        }
        noteContent = intent.getStringExtra("content");
        mEtNoteContent.setText(noteContent);

        if (intent.getStringExtra("mode") != null) {
            mode = intent.getStringExtra("mode");
        }
        iflyUtil = new IflytekUtil(this, mEtNoteContent);
    }

    private void initToolBar() {
        if (toolbar == null) {
            return;
        }
        setTitle("");//必须在setSupportActionBar之前调用
        toolbar.setTitleTextColor(Color.parseColor("#000000"));
        setSupportActionBar(toolbar);
        if (mode.equals(Constants.EDIT_NOTE)) {
            tvTitle.setText("记事本详情");
        } else {

            tvTitle.setText("创建记事本");
        }
        Toolbar.LayoutParams lp = new Toolbar.LayoutParams(
                Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        tvTitle.setLayoutParams(lp);
        toolbarTextBtn.setText("完成");
        toolbarTextBtn.setVisibility(View.VISIBLE);
        toolbarTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTvNoteDate.getText().toString().trim().equals("点击选择日期")) {
                    showToast("请选择日期");
                } else {
                    createNote();

                }


            }
        });
        toolbar.setNavigationIcon(R.drawable.toolbar_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (null != noteContent && !noteContent.equals("")) {
                    if (!mEtNoteContent.getText().toString().trim().equals(noteContent)) {
                        showTip("是否放弃编辑", "确定", "取消");
                    } else {
                        finish();
                    }
                } else {
                    if (!mEtNoteContent.getText().toString().trim().equals("")) {
                        showTip("是否放弃编辑", "确定", "取消");
                    } else {

                        finish();
                    }

                }


            }
        });
    }

    public void showTip(String msg, final String posiStr, String negaStr) {
        @SuppressLint("InflateParams")
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_exit_editor, null);
        TextView title = (TextView) dialogView.findViewById(R.id.title);
        title.setText("提示");
        TextView message = (TextView) dialogView.findViewById(R.id.message);
        message.setText(msg);

        final AlertDialog alertDialog = new AlertDialog.Builder(this,
                R.style.AppTheme_Dialog).create();
        alertDialog.setView(dialogView);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, posiStr,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        finish();

                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, negaStr,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(
                getResources().getColor(R.color.btn_text_logout));
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(
                getResources().getColor(R.color.btn_text_logout));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (null != noteContent && !noteContent.equals("")) {
                    if (!mEtNoteContent.getText().toString().trim().equals(noteContent)) {
                        showTip("是否放弃编辑", "确定", "取消");
                    } else {
                        finish();
                    }
                } else {
                    if (!mEtNoteContent.getText().toString().trim().equals("")) {
                        showTip("是否放弃编辑", "确定", "取消");
                    } else {

                        finish();
                    }

                }
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void createNote() {
        showLoadingView();
        LogUtils.e("uid" + "->" + AppConfig.getAppConfig(this).getPrivateUid());
        LogUtils.e("token" + "->" + AppConfig.getAppConfig(this).getPrivateToken());
        LogUtils.e("department_id" + "->" + AppConfig.getAppConfig(this).getDepartmentId());
        LogUtils.e("content" + "->" + mEtNoteContent.getText().toString().trim());
        //time->2016年12月20日
        LogUtils.e("time" + "->" + mTvNoteDate.getText().toString().trim().replace("年", "-")
                .replace("月", "-").replace("日", "").trim());
        LogUtils.e("nt_id" + "->" + note_id);

        mPresenter.createNote(AppConfig.getAppConfig(this).getDepartmentId(),
                mEtNoteContent.getText().toString().trim(),
                mTvNoteDate.getText().toString().trim().replace("年", "-")
                        .replace("月", "-").replace("日", "").trim(),
                note_id, mode);
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

                String currentDate = year + "-" + month + "-" + day;
                if (DateUtils.judeDateOrderByDay(DateUtils.getTodayDate().replace("/", "-"), currentDate)) {
                    tv.setText(year + "年" + month + "月" + day + "日");
                } else {
                    showToast("不能选择过往的日期");
                }

            }
        });
        picker.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.tv_note_date, R.id.btn_voice_input})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_note_date:
                showDoneDatePicker(mTvNoteDate);
                break;
            case R.id.btn_voice_input:
                requestPermission();
                break;
        }
    }

    private void requestPermission() {
        requestRunTimePermission(new String[]{Manifest.permission.RECORD_AUDIO}, new PermissionListener() {
            @Override
            public void onGranted() {
                IflytekUtil iflytekUtil = new IflytekUtil(CreateNoteActivity.this, mEtNoteContent);
                iflytekUtil.showIatDialog();
            }

            @Override
            public void onDenied() {
                showToast("录音权限被拒绝！请手动设置");
            }
        });
    }


    @Override
    public void uidNull(int code) {
        catchWarningByCode(code);
    }

    @Override
    public void createSuccess() {
        showToast("创建记事成功");
        startActivity(new Intent(CreateNoteActivity.this, MainController.class));
        CreateNoteActivity.this.finish();
    }

    @Override
    public void createFailed(int errCode, String msg) {
        catchWarningByCode(errCode);

    }

    @Override
    public void createFinish() {
        hideLoadingView();
    }

    @Override
    public void createResponseOther(String msg) {
        showToast(msg);
    }
}
