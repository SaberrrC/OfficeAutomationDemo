package com.itcrm.GroupInformationPlatform.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.itcrm.GroupInformationPlatform.R;
import com.itcrm.GroupInformationPlatform.common.Api;
import com.itcrm.GroupInformationPlatform.common.Constants;
import com.itcrm.GroupInformationPlatform.manager.AppConfig;
import com.itcrm.GroupInformationPlatform.ui.base.BaseActivity;
import com.itcrm.GroupInformationPlatform.utils.LogUtils;

import org.json.JSONArray;
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
 * Author:Created by Tsui on Date:2016/12/14 11:07
 * Description:视频会议详情信息
 */
public class MeetingInfoVideoActivity extends BaseActivity {
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.toolbar_text_btn)
    TextView toolbarTextBtn;
    @Bind(R.id.tv_meeting_time_tips)
    TextView tvMeetingTimeTips;
    @Bind(R.id.tv_date)
    TextView tvDate;
    @Bind(R.id.tv_meeting_theme)
    TextView tvMeetingTheme;
    @Bind(R.id.ll_join_people_item)
    LinearLayout llJoinPeopleItem;
    @Bind(R.id.ll_confirm_layout)
    LinearLayout llConfirmLayout;
    @Bind(R.id.ll_join_meeting_people_container)
    LinearLayout ll_join_meeting_people_container;
    @Bind(R.id.btn_cancel_meeting)
    Button btnCancelMeeting;
    @Bind(R.id.btn_meeting_past)
    Button btnMeetingPast;
    @Bind(R.id.btn_join_meeting)
    Button btnJoinMeeting;
    @Bind(R.id.btn_can_not_join_meeting)
    Button btnCanNotJoinMeeting;
    @Bind(R.id.layout_root)
    RelativeLayout mRootView;
    private int cf_id;//会议室id
    private String meeting_mode;
    private PopupWindow popupWindow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metting_detail_video);

        //TODO 视频会议Demo界面
        ButterKnife.bind(this);
        initToolBar();
        setTranslucentStatus(this);
        initData();
        loadMeetingRoomData();

    }

    private void loadMeetingRoomData() {
        showLoadingView();
        HttpParams params = new HttpParams();
        params.put("cf_id", cf_id);
        params.put("uid", AppConfig.getAppConfig(this).getPrivateUid());
        params.put("token", AppConfig.getAppConfig(this).getPrivateToken());

        initKjHttp().post(Api.CONFERENCE_CONFVIEW_DATETIME, params, new HttpCallBack() {

            @Override
            public void onPreStart() {
                super.onPreStart();

            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                hideLoadingView();
                LogUtils.e("loadMeetingRoomData->" + t);
                try {
                    JSONObject jo = new JSONObject(t);
                    JSONObject jsonObject = Api.getDataToJSONObject(jo);
                    switch (Api.getCode(jo)) {
                        case Api.RESPONSES_CODE_OK:
                            setDataForMeetingRoom(jsonObject);
                            break;
                        case Api.RESPONSES_CODE_DATA_EMPTY:
                            break;
                        case Api.RESPONSES_CONTENT_EMPTY:
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
                loadMeetingPeopleData();
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                catchWarningByCode(errorNo);
            }
        });
    }

    private void setDataForMeetingRoom(JSONObject jsonObject) {
        try {
            tvMeetingTheme.setText(jsonObject.getString("theme"));
            String date = jsonObject.getString("date") + " " + jsonObject.getString("begintime") + ":00 ~ "
                    + jsonObject.getString("endtime") + ":00";
            tvDate.setText(date);

            //判断会议是否过期
            String expire = jsonObject.getString("expire");
            if (expire.equals("2")) {
                //已过期
                btnMeetingPast.setVisibility(View.VISIBLE);
                toolbarTextBtn.setVisibility(View.GONE);
                btnCancelMeeting.setVisibility(View.GONE);
                llConfirmLayout.setVisibility(View.GONE);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void loadMeetingPeopleData() {
        showLoadingView();
        HttpParams params = new HttpParams();
        params.put("cf_id", cf_id);
        params.put("uid", AppConfig.getAppConfig(this).getPrivateUid());
        params.put("token", AppConfig.getAppConfig(this).getPrivateToken());

        initKjHttp().post(Api.CONFERENCE_CONFVIEW_USERS, params, new HttpCallBack() {

            @Override
            public void onPreStart() {
                super.onPreStart();

            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                hideLoadingView();
                LogUtils.e("loadMeetingPeopleData->" + t);
                try {
                    JSONObject jo = new JSONObject(t);
                    switch (Api.getCode(jo)) {
                        case Api.RESPONSES_CODE_OK:
                            JSONObject dataToJSONObject = Api.getDataToJSONObject(jo);
                            JSONArray cf_users = dataToJSONObject.getJSONArray("cf_users");
                            setMeetingPeopleData(cf_users);
                            break;
                        case Api.RESPONSES_CODE_DATA_EMPTY:
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

    private void setMeetingPeopleData(JSONArray ja) {

        ll_join_meeting_people_container.removeAllViews();
        for (int i = 0; i < ja.length(); i++) {
            try {
                JSONObject jsonObject = ja.getJSONObject(i);

                View view = View.inflate(MeetingInfoVideoActivity.this,
                        R.layout.metting_detail_join_people_item, null);
                ((TextView) view.findViewById(R.id.tv_name)).setText(jsonObject.getString("username"));
                String status = jsonObject.getString("status");

                TextView tvStatus = (TextView) view.findViewById(R.id.tv_status);
                tvStatus.setText(status);
                //
                String is_createman = jsonObject.getString("is_createman");
                if (is_createman.equals("1")) {
                    tvStatus.setTextColor(Color.parseColor("#47cf38"));
                }else{
                    if (status.equals("未查看")) {
                        tvStatus.setTextColor(Color.parseColor("#eb4a4a"));

                    } else if (status.equals("参会")) {
                        tvStatus.setTextColor(Color.parseColor("#47cf38"));

                    } else if (status.equals("不参会")) {
                        tvStatus.setTextColor(Color.parseColor("#3f9ae7"));

                    }
                }

                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout
                        .LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                if (i != 0) {
                    lp.setMargins(0, 20, 0, 0);
                }
                ll_join_meeting_people_container.addView(view, lp);
            } catch (JSONException e) {
                e.printStackTrace();
                LogUtils.e("setMeetingPeopleData-->异常了");
            }

        }
    }

    private void initData() {
        Intent intent = getIntent();
        meeting_mode = intent.getStringExtra("meeting_mode");
        cf_id = Integer.parseInt(intent.getStringExtra("cf_id"));

        //根据获取的intent数据来对UI进行布局
        initWidget();
    }

    private void initWidget() {
        if (meeting_mode.equals(Constants.MEETING_PLAN)) {
            //日程安排的会议安排，应该。。。
            btnCancelMeeting.setVisibility(View.GONE);
            btnMeetingPast.setVisibility(View.GONE);
            llConfirmLayout.setVisibility(View.GONE);
        } else if (meeting_mode.equals(Constants.ME_LAUNCH_MEETING)) {
            //日程安排的我发起的，应该。。。
            btnCancelMeeting.setVisibility(View.VISIBLE);
            btnMeetingPast.setVisibility(View.GONE);
            llConfirmLayout.setVisibility(View.GONE);
        } else if (meeting_mode.equals(Constants.PUSH_CONFORM_MEETING)) {
            //推送消息过来的，应该有确认和取消功能
            btnCancelMeeting.setVisibility(View.GONE);
            btnMeetingPast.setVisibility(View.GONE);
            llConfirmLayout.setVisibility(View.VISIBLE);
        }
    }

    private void initToolBar() {
        if (toolbar == null) {
            return;
        }
        setTitle("");//必须在setSupportActionBar之前调用
        toolbar.setTitleTextColor(Color.parseColor("#000000"));
        setSupportActionBar(toolbar);
        tvTitle.setText("视频会议详情");
        Toolbar.LayoutParams lp = new Toolbar.LayoutParams(
                Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        tvTitle.setLayoutParams(lp);
        toolbarTextBtn.setVisibility(View.VISIBLE);
        toolbarTextBtn.setText("刷新");
        toolbarTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadMeetingPeopleData();
            }
        });
        toolbar.setNavigationIcon(R.drawable.toolbar_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @OnClick({R.id.btn_cancel_meeting, R.id.btn_join_meeting, R.id.btn_can_not_join_meeting,
            R.id.btn_add_video})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_cancel_meeting:
                showCancleDialog();
                break;
            case R.id.btn_join_meeting:
                //参会 1
                confirmJoinMetting("1");
                break;
            case R.id.btn_can_not_join_meeting:
                //不参会 2
                confirmJoinMetting("2");
                break;
            case R.id.btn_add_video:
                goVideoActivity();
                break;
        }
    }

    /**
     * 去视频界面
     */
    private void goVideoActivity() {
        Intent intent = new Intent();
        intent.setClass(MeetingInfoVideoActivity.this, MeetingVideoActivity.class);
        intent.putExtra("isCreate",true);
        startActivity(intent);
    }


    public void showCancleDialog() {
        View contentView = LayoutInflater.from(this).inflate(R.layout
                .meeting_cancle_dialog, null);
        if (popupWindow == null) {
            popupWindow = new PopupWindow(contentView, LinearLayout.LayoutParams
                    .WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, false);
            TextView btnSubmit = (TextView) contentView.findViewById(R.id.tv_yes);
            TextView btnCancel = (TextView) contentView.findViewById(R.id.tv_no);
            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cancleMetting();
                }
            });
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                }
            });
            popupWindow.setOutsideTouchable(true);
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
        }


        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.5f;
        getWindow().setAttributes(lp);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });


        popupWindow.showAtLocation(mRootView, Gravity.CENTER, 0, 0);
    }


    private void confirmJoinMetting(String status) {
        showLoadingView();
        HttpParams params = new HttpParams();
        params.put("cf_id", cf_id);
        params.put("status", status);
        params.put("uid", AppConfig.getAppConfig(this).getPrivateUid());
        params.put("token", AppConfig.getAppConfig(this).getPrivateToken());

        initKjHttp().post(Api.CONFERENCE_IFATTENDANCE, params, new HttpCallBack() {

            @Override
            public void onPreStart() {
                super.onPreStart();

            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                hideLoadingView();
                LogUtils.e("confirmJoinMetting->" + t);
                try {
                    JSONObject jo = new JSONObject(t);

                    if (Api.getCode(jo) == Api.RESPONSES_CODE_OK) {
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

    /**
     * 取消会议
     */
    private void cancleMetting() {
        showLoadingView();
        HttpParams params = new HttpParams();
        params.put("cf_id", cf_id);
        params.put("uid", AppConfig.getAppConfig(this).getPrivateUid());
        params.put("token", AppConfig.getAppConfig(this).getPrivateToken());

        initKjHttp().post(Api.CONFERENCE_CANCELCONF, params, new HttpCallBack() {

            @Override
            public void onPreStart() {
                super.onPreStart();

            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                hideLoadingView();
                try {
                    JSONObject jo = new JSONObject(t);

                    if (Api.getCode(jo) == Api.RESPONSES_CODE_OK) {
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
