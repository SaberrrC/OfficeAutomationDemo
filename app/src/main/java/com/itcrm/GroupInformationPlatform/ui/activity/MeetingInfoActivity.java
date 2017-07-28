package com.itcrm.GroupInformationPlatform.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.itcrm.GroupInformationPlatform.utils.Utils;

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
 * Description:会议详情
 */
public class MeetingInfoActivity extends BaseActivity {
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
    @Bind(R.id.tv_meeting_room_tips)
    TextView tvMeetingRoomTips;
    @Bind(R.id.tv_selected_meeting_room)
    TextView tvSelectedMeetingRoom;
    @Bind(R.id.tv_meeting_theme)
    TextView tvMeetingTheme;
    @Bind(R.id.tv_meeting_type)
    TextView tvMeetingType;
    @Bind(R.id.ll_join_people_item)
    LinearLayout llJoinPeopleItem;
    @Bind(R.id.ll_confirm_layout)
    LinearLayout llConfirmLayout;
    @Bind(R.id.bottom_layout)
    RelativeLayout mBottomLayout;


    @Bind(R.id.ll_join_meeting_people_container)
    LinearLayout ll_join_meeting_people_container;
    @Bind(R.id.btn_cancel_meeting)
    Button btnCancelMeeting;
    @Bind(R.id.btn_start_meeting)
    Button btnStartMeeting;
    @Bind(R.id.btn_meeting_past)
    Button btnMeetingPast;
    @Bind(R.id.btn_join_meeting)
    Button btnJoinMeeting;
    @Bind(R.id.btn_can_not_join_meeting)
    Button btnCanNotJoinMeeting;
    @Bind(R.id.btn_meeting_be_cancel)
    Button btnMeetingBeCancel;
    @Bind(R.id.rootView)
    RelativeLayout mRootView;
    private int cf_id;//会议室id
    private String meeting_mode;//会议模式
    private PopupWindow popupWindow;
    private String date;
    private String begintime;
    private String endtime;
    private boolean isShowJoin;
    private String meetingType;//会议类型
    private String meetingTheme;
    private String joinPeopleStr;//要加入会议的人员
    private String currentUid;//当前用户uid
    private boolean isCreateMeetingMen = false;//是否是会议创建人

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metting_detail);
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
                            mBottomLayout.setVisibility(View.INVISIBLE);
                            llConfirmLayout.setVisibility(View.INVISIBLE);
                            catchWarningByCode(Api.getCode(jo));
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    LogUtils.e("loadMeetingRoomData:" + e.toString());
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
        mRootView.setVisibility(View.VISIBLE);
        try {
            LogUtils.e("setDataForMeetingRoom:jsonObject->" + jsonObject.toString());
            meetingTheme = jsonObject.getString("theme");
            tvMeetingTheme.setText(meetingTheme);
            date = jsonObject.getString("date");
            begintime = jsonObject.getString("begintime");
            endtime = jsonObject.getString("endtime");
            String time = "";


            if (jsonObject.getString("roomname").equals("")) {
                tvSelectedMeetingRoom.setText("无");
                time = date;
            } else {
                tvSelectedMeetingRoom.setText(jsonObject.getString("roomname"));
                time = date + " " + begintime + ":00 ~ "
                        + endtime + ":00";
            }

            tvDate.setText(time);
            meetingType = jsonObject.getString("type");
            if (meetingType.equals("1")) {
                tvMeetingType.setText("普通会议");
            } else {
                tvMeetingType.setText("视频会议");
            }

            //判断会议是否过期
            //1未过期, 2 过期
            String expire = jsonObject.getString("expire");
            LogUtils.e("expire->" + expire);
            if (expire.equals("2")) {
                //过期
                btnMeetingPast.setVisibility(View.VISIBLE);
                toolbarTextBtn.setVisibility(View.GONE);
                btnCancelMeeting.setVisibility(View.GONE);
                llConfirmLayout.setVisibility(View.GONE);
                toolbarTextBtn.setVisibility(View.GONE);
            } else if (expire.equals("1")) {
                if (isShowJoin) {
                    llConfirmLayout.setVisibility(View.VISIBLE);
                } else {
                    llConfirmLayout.setVisibility(View.GONE);

                }
                initWidget();
            } else if (expire.equals("3")) {
                //已取消
                btnMeetingPast.setVisibility(View.GONE);
                toolbarTextBtn.setVisibility(View.GONE);
                btnCancelMeeting.setVisibility(View.GONE);
                btnMeetingBeCancel.setVisibility(View.VISIBLE);
                llConfirmLayout.setVisibility(View.GONE);
                toolbarTextBtn.setVisibility(View.GONE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            LogUtils.e(e.toString());
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

        joinPeopleStr = ja.toString();

        ll_join_meeting_people_container.removeAllViews();
        for (int i = 0; i < ja.length(); i++) {
            try {
                JSONObject jsonObject = ja.getJSONObject(i);

                View view = View.inflate(MeetingInfoActivity.this,
                        R.layout.metting_detail_join_people_item, null);
                ((TextView) view.findViewById(R.id.tv_name)).setText(jsonObject.getString("username"));
                String status = jsonObject.getString("status");

                TextView tvStatus = (TextView) view.findViewById(R.id.tv_status);
                tvStatus.setText(status);
                //
                String is_createman = jsonObject.getString("is_createman");

                if (is_createman.equals("1")) {
                    tvStatus.setTextColor(Color.parseColor("#10c48b"));
                    if (currentUid.equals(jsonObject.getString("uid"))) {
                        isCreateMeetingMen = true;
                        toolbarTextBtn.setText("刷新");
                        toolbarTextBtn.setVisibility(View.VISIBLE);
                    } else {
                        isCreateMeetingMen = false;
                    }
                } else {
                    if (status.equals("未查看")) {
                        tvStatus.setTextColor(Color.parseColor("#c1272d"));

                    } else if (status.equals("参会")) {
                        tvStatus.setTextColor(Color.parseColor("#10c48b"));

                    } else if (status.equals("不参会")) {
                        tvStatus.setTextColor(Color.parseColor("#0EA7ED"));

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
            }

        }
    }

    private void initData() {
        Intent intent = getIntent();
        meeting_mode = intent.getStringExtra("meeting_mode");
        cf_id = Integer.parseInt(intent.getStringExtra("cf_id"));
        isShowJoin = intent.getBooleanExtra("isShowJoin", false);

        currentUid = AppConfig.getAppConfig(this).getPrivateUid();

    }


    private void initWidget() {
        if (meeting_mode.equals(Constants.MEETING_PLAN)) {
            //日程安排的会议安排，应该。。。
            btnCancelMeeting.setVisibility(View.GONE);
            btnMeetingPast.setVisibility(View.GONE);
            if (meeting_mode.equals("1")) {
                toolbarTextBtn.setVisibility(View.GONE);
            }
            if (meetingType.equals("2")) {
                toolbarTextBtn.setVisibility(View.VISIBLE);
                toolbarTextBtn.setText("加入会议");
            }
        } else if (meeting_mode.equals(Constants.ME_LAUNCH_MEETING)) {
            //日程安排的我发起的，应该。。。,开始会议只有视频会议才有的按钮，其他地方都没做显示或隐藏，就这有
            btnCancelMeeting.setVisibility(View.VISIBLE);
            btnMeetingPast.setVisibility(View.GONE);
            toolbarTextBtn.setVisibility(View.VISIBLE);
            if (meetingType.equals("2")) {
                btnStartMeeting.setVisibility(View.VISIBLE);
            }

        } else if (meeting_mode.equals(Constants.PUSH_CONFORM_MEETING)) {
            //推送消息过来的，应该有确认和取消功能
            btnCancelMeeting.setVisibility(View.GONE);
            btnMeetingPast.setVisibility(View.GONE);
            toolbarTextBtn.setVisibility(View.GONE);
        }
    }

    private void initToolBar() {
        if (toolbar == null) {
            return;
        }
        setTitle("");//必须在setSupportActionBar之前调用
        toolbar.setTitleTextColor(Color.parseColor("#000000"));
        setSupportActionBar(toolbar);
        tvTitle.setText("会议详情");
        Toolbar.LayoutParams lp = new Toolbar.LayoutParams(
                Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        tvTitle.setLayoutParams(lp);
        toolbarTextBtn.setVisibility(View.GONE);
        toolbarTextBtn.setText("刷新");
        toolbarTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (toolbarTextBtn.getText().toString().trim().equals("加入会议")) {
                    Intent intent = new Intent(MeetingInfoActivity.this, MeetingVideoActivity.class);
                    intent.putExtra("isCreate", false);
                    intent.putExtra("joinPeopleStr", joinPeopleStr);
                    //房间名称就是会议主题
                    intent.putExtra("roomName", meetingTheme);
                    startActivity(intent);
                } else {
                    loadMeetingPeopleData();
                }
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

    @OnClick({R.id.btn_cancel_meeting, R.id.btn_join_meeting, R.id.btn_can_not_join_meeting, R.id.btn_start_meeting})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_cancel_meeting:
                showCancleDialog();

                break;
            case R.id.btn_join_meeting:
                //参会 1
                judeIsCanJoin("1");
                break;
            case R.id.btn_can_not_join_meeting:
                //不参会 2
                confirmJoinMetting("2");
                break;
            case R.id.btn_start_meeting:
                //开始会议
                Intent intent = new Intent(MeetingInfoActivity.this, MeetingVideoActivity.class);
                intent.putExtra("isCreate", true);
                //房间名称就是会议主题
                intent.putExtra("roomName", meetingTheme);
                intent.putExtra("joinPeopleStr", joinPeopleStr);
                startActivity(intent);
                break;
        }
    }


    public void showCancleDialog() {
        View contentView = LayoutInflater.from(this).inflate(R.layout
                .meeting_cancle_dialog, null);
        if (popupWindow == null) {
            popupWindow = new PopupWindow(contentView, Utils.dip2px(240), Utils.dip2px(110), false);
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
        LogUtils.e("status->" + status);
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
                LogUtils.e("confirm->" + t);
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

    private void judeIsCanJoin(final String status) {
        showLoadingView();
        HttpParams params = new HttpParams();
        params.put("begintime", begintime);
        params.put("date", date);
        params.put("endtime", endtime);
        params.put("uid", AppConfig.getAppConfig(this).getPrivateUid());
        params.put("token", AppConfig.getAppConfig(this).getPrivateToken());

        LogUtils.e("begintime" + begintime);
        LogUtils.e("date" + date);
        LogUtils.e("endtime" + endtime);

        initKjHttp().post(Api.CONFERENCE_JUDGE, params, new HttpCallBack() {

            @Override
            public void onPreStart() {
                super.onPreStart();

            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                hideLoadingView();
                LogUtils.e("judeIsCanJoin->" + t);
                try {
                    JSONObject jo = new JSONObject(t);
                    //716为有会议，200为无会议
                    if (Api.getCode(jo) == Api.RESPONSES_CODE_OK) {
                        confirmJoinMetting(status);
                    } else if (Api.getCode(jo) == 716) {
                        showTip("当前时间已有其他会议安排,是否继续参加", "确定", "取消", status);
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
                LogUtils.e(errorNo + strMsg);
            }
        });
    }

    public void showTip(String msg, final String posiStr, String negaStr, final String status) {
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
                        confirmJoinMetting(status);

                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, negaStr,
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
