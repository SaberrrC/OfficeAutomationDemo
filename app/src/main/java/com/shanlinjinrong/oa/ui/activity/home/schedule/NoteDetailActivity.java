package com.shanlinjinrong.oa.ui.activity.home.schedule;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.common.Api;
import com.shanlinjinrong.oa.common.Constants;
import com.shanlinjinrong.oa.manager.AppConfig;
import com.shanlinjinrong.oa.ui.base.BaseActivity;
import com.shanlinjinrong.oa.utils.LogUtils;

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
 * Author:Created by Tsui on Date:2016/12/15 13:34
 * Description:记事本详情
 */
public class NoteDetailActivity extends BaseActivity {
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.toolbar_text_btn)
    TextView mTolbarTextBtn;

    @Bind(R.id.tv_note_date)
    TextView tvNoteDate;
    @Bind(R.id.tv_note_content)
    TextView tvNoteContent;
    @Bind(R.id.rl_root_view)
    RelativeLayout mRootView;
    @Bind(R.id.btn_note_delete)
    Button mBtnNoteDelete;

    private String note_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);
        ButterKnife.bind(this);
        initToolBar();
        setTranslucentStatus(this);
        initData();
        loadData();
    }

    private void initData() {
        note_id = getIntent().getStringExtra("note_id");
        LogUtils.e("note_id->" + note_id);
    }

    private void loadData() {
        showLoadingView();
        HttpParams params = new HttpParams();
        params.put("uid", AppConfig.getAppConfig(this).getPrivateUid());
        params.put("token", AppConfig.getAppConfig(this).getPrivateToken());
        params.put("department_id", AppConfig.getAppConfig(this).getDepartmentId());
        params.put("nt_id", note_id);

        initKjHttp().post(Api.NOTE_DETAIL, params, new HttpCallBack() {

            @Override
            public void onPreStart() {
                super.onPreStart();

            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                LogUtils.e(t);
                JSONObject jo = null;
                try {
                    jo = new JSONObject(t);
                    JSONObject jsonObject = Api.getDataToJSONObject(jo);
                   /* //200;644记事ID不能为空;646选择的记事不存在
                    if (Api.getCode(jo) == Api.RESPONSES_CODE_OK) {

                        tvNoteDate.setText(jo.getString("time"));
                        tvNoteContent.setText(jo.getString("content"));
LogUtils.e("200-->"+jo.getString("time"));
                    } else if (Api.getCode(jo) == 646) {
                        showToast(Api.getInfo(jo));
                        showEmptyView(mRootView, "内容为空", 0, false);
                        mTolbarTextBtn.setVisibility(View.GONE);
                        mBtnNoteDelete.setVisibility(View.GONE);

                    } else if (Api.getCode(jo) == 644) {
                        showToast(Api.getInfo(jo));
                    }*/
                    switch (Api.getCode(jo)) {
                        case Api.RESPONSES_CODE_OK:
                            tvNoteDate.setText(jsonObject.getString("time"));
                            tvNoteContent.setText(jsonObject.getString("content"));
                            break;
                        case 464:
                            showToast(Api.getInfo(jo));
                            showEmptyView(mRootView, "内容为空", 0, false);
                            mTolbarTextBtn.setVisibility(View.GONE);
                            mBtnNoteDelete.setVisibility(View.GONE);
                            break;
                        case 646:
                            showToast(Api.getInfo(jo));
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
                LogUtils.e("errorNo,strMsg--->" + errorNo + "," + strMsg);
                catchWarningByCode(errorNo);
            }
        });
    }

    private void initToolBar() {
        if (mToolbar == null) {
            return;
        }
        setTitle("");//必须在setSupportActionBar之前调用
        mToolbar.setTitleTextColor(Color.parseColor("#000000"));
        setSupportActionBar(mToolbar);
        mTvTitle.setText("记事本详情");
        Toolbar.LayoutParams lp = new Toolbar.LayoutParams(
                Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        mTvTitle.setLayoutParams(lp);
        mTolbarTextBtn.setText("编辑");
        mTolbarTextBtn.setVisibility(View.VISIBLE);
        mTolbarTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(NoteDetailActivity.this, CreateNoteActivity.class);
                intent.putExtra("note_id", note_id);
                intent.putExtra("mode", Constants.EDIT_NOTE);
                intent.putExtra("content", tvNoteContent.getText().toString().trim());
                intent.putExtra("noteData", tvNoteDate.getText().toString().trim());
                startActivity(intent);
            }
        });
        mToolbar.setNavigationIcon(R.drawable.toolbar_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void showDeleteTips(String msg, final String posiStr, String negaStr) {
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

                        deleteNote();

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
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.btn_note_delete)
    public void onClick() {
        showDeleteTips("是否删除", "确定", "取消");
    }

    private void deleteNote() {
        showLoadingView();
        HttpParams params = new HttpParams();
        params.put("uid", AppConfig.getAppConfig(this).getPrivateUid());
        params.put("token", AppConfig.getAppConfig(this).getPrivateToken());
        params.put("department_id", AppConfig.getAppConfig(this).getDepartmentId());
        params.put("nt_id", note_id);

        initKjHttp().post(Api.NOTE_DELETE, params, new HttpCallBack() {

            @Override
            public void onPreStart() {
                super.onPreStart();

            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                LogUtils.e(t);
                JSONObject jo = null;
                try {
                    jo = new JSONObject(t);
                    if (Api.getCode(jo) == Api.RESPONSES_CODE_OK) {

                        showToast("删除成功");
                        startActivity(new Intent(NoteDetailActivity.this, ScheduleActivity.class));
                        finish();
                    }

                    if (Api.getCode(jo) == Api.RESPONSES_CODE_TOKEN_NO_MATCH) {
                        catchWarningByCode(Api.getCode(jo));
                    } else if (Api.getCode(jo) == Api.RESPONSES_CODE_UID_NULL) {
                        catchWarningByCode(Api.getCode(jo));
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
                LogUtils.e("errorNo,strMsg--->" + errorNo + "," + strMsg);
                catchWarningByCode(errorNo);
            }
        });
    }
}
