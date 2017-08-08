package com.shanlin.oa.ui.activity;

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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shanlin.oa.R;
import com.shanlin.oa.common.Api;
import com.shanlin.oa.manager.AppConfig;
import com.shanlin.oa.model.selectContacts.Child;
import com.shanlin.oa.ui.base.BaseActivity;
import com.shanlin.oa.utils.LogUtils;
import com.shanlin.oa.utils.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.DatePicker;

/**
 * ProjectName: dev-beta-v1.0.1
 * PackageName: com.itcrm.GroupInformationPlatform.ui.activity
 * Author:Created by Tsui on Date:2016/12/8 15:17
 * Description:创建会议
 */
public class CreateMeetingActivity extends BaseActivity {
    private static final int ADD_JOIN_PEOPLE = 1;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.toolbar_text_btn)
    TextView toolbarTextBtn;
    @Bind(R.id.tv_join_people_tips)
    TextView mTvJoinPeopleTips;
    @Bind(R.id.et_meeting_theme)
    EditText mEtMeetingTheme;
    @Bind(R.id.tv_select_meeting_room)
    TextView tvSelectMeetingRoom;
    @Bind(R.id.layout_root)
    RelativeLayout mRootView;
    @Bind(R.id.tv_date)
    TextView tvDate;
    @Bind(R.id.tv_selected_meeting_room_type)
    TextView tvSelectedMRType;
    @Bind(R.id.ll_join_people_container)
    LinearLayout mLlJoinPeopleContainer;
    private StringBuilder copy;//抄送人id
    private ArrayList<Child> contactsList; //抄送人数组
    String roomId = "";
    private String meetingType;
    private String date;
    private String begintime;
    private String endtime;
    private String roomName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_metting);
        ButterKnife.bind(this);
        initToolBar();
        setTranslucentStatus(this);
        initData();
    }

    private void initData() {
        mLlJoinPeopleContainer.setVisibility(View.GONE);
        contactsList = new ArrayList<>();
        copy = new StringBuilder();

        Intent intent = getIntent();
        meetingType = intent.getStringExtra("meetingType");
        date = intent.getStringExtra("date");
        begintime = intent.getStringExtra("begintime");
        endtime = intent.getStringExtra("endtime");
        roomId = intent.getStringExtra("roomId");
        if (null == roomId) {
            begintime = "0";
            endtime = "0";
            roomId = "0";
        }
        roomName = intent.getStringExtra("roomName");

        LogUtils.e(begintime + "," + endtime + "," + roomId + "," + roomName);

        if (null != meetingType && meetingType.equals("1")) {
            tvTitle.setText("创建普通会议");
            tvSelectedMRType.setText("普通会议");
            //tvSelectEdMRType
        } else {
            tvTitle.setText("创建视频会议");
            tvSelectedMRType.setText("视频会议");
        }
        if (null == begintime || "0".equals(begintime)) {
            tvDate.setText(date);
        } else {
            tvDate.setText(date + "  " + begintime + ":00~" + endtime + ":00");

        }
        if (null == roomName) {
            tvSelectMeetingRoom.setText("不需要会议室");
        } else {
            tvSelectMeetingRoom.setText(roomName);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ADD_JOIN_PEOPLE) {
            addCopyPersonOperate(data);
        }
    }

    private void initToolBar() {
        if (toolbar == null) {
            return;
        }
        setTitle("");//必须在setSupportActionBar之前调用
        toolbar.setTitleTextColor(Color.parseColor("#000000"));
        setSupportActionBar(toolbar);

        Toolbar.LayoutParams lp = new Toolbar.LayoutParams(
                Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        tvTitle.setLayoutParams(lp);
        toolbar.setNavigationIcon(R.drawable.toolbar_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!StringUtils.isBlank(mEtMeetingTheme.getText().toString().trim())
                        || !tvDate.getText().toString().trim().equals("点击选择会议日期")
                        ) {
                    showTip("是否放弃编辑", "确定", "取消");
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
                if (!StringUtils.isBlank(mEtMeetingTheme.getText().toString().trim())
                        || !tvDate.getText().toString().trim().equals("点击选择会议日期")
                        ) {
                    showTip("是否放弃编辑", "确定", "取消");
                }
                break;
        }
        return super.onKeyDown(keyCode, event);
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

    /**
     * 添加抄送人操作
     */
    private void addCopyPersonOperate(Intent data) {
        this.contactsList.clear();
//        this.contactsList=data.getParcelableArrayListExtra("contacts");
        ArrayList<Child> contacts = data.getParcelableArrayListExtra("contacts");
        contactsList.addAll(contacts);
        //清空view
        mLlJoinPeopleContainer.removeAllViews();
        if (contactsList != null) {
            mTvJoinPeopleTips.setVisibility(View.GONE);
            mLlJoinPeopleContainer.setVisibility(View.VISIBLE);
            copy.setLength(0);
            for (int i = 0; i < contactsList.size(); i++) {
                String username = contactsList.get(i).getUsername();

                if (i != contactsList.size() - 1) {
                    copy.append(contactsList.get(i).getUid() + ",");
                } else if (i == contactsList.size() - 1) {
                    copy.append(contactsList.get(i).getUid());
                }


                TextView tv = new TextView(CreateMeetingActivity.this);
                tv.setTextColor(Color.parseColor("#999999"));
                tv.setText(username);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams
                        .WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                if (i == 0) {
                    lp.setMargins(0, 0, 0, 0);

                } else {
                    lp.setMargins(0, 20, 0, 0);

                }
                mLlJoinPeopleContainer.addView(tv, lp);
            }

        }
        LogUtils.e("参会人:" + contactsList.toString());
        LogUtils.e("从选择会议过来的参会人:" + contacts.toString());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @OnClick({
            R.id.report_iv_add_person, R.id.btn_create_meeting})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.report_iv_add_person:
                LogUtils.e("点检按钮时候的与会人-》" + contactsList.toString());
                Intent intent = new Intent(this, SelectJoinPeopleActivity.class);
                intent.putParcelableArrayListExtra("selectedContacts", contactsList);
                startActivityForResult(intent, ADD_JOIN_PEOPLE);
                break;
            case R.id.btn_create_meeting:
                if (StringUtils.isBlank(mEtMeetingTheme.getText().toString().trim())) {
                    showToast("会议主题不能为空");
                    return;
                }
                if (tvDate.getText().toString().trim().equals("点击选择会议日期")) {
                    showToast("会议日期不能为空");
                    return;
                }


                if (contactsList.size() < 1) {
                    showToast("没有选择参会人");
                    return;
                }
                sendData();
                break;
        }
    }

    /**
     * 显示完成样式的日期选择器
     */
    private void showDoneDatePicker() {
        final DatePicker picker = new DatePicker(this, DatePicker.YEAR_MONTH_DAY);
        Calendar cal = Calendar.getInstance();
        picker.setSelectedItem(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1,
                cal.get(Calendar.DAY_OF_MONTH));
        picker.setSubmitText("完成");
        picker.setSubmitTextColor(Color.parseColor("#2d9dff"));
        picker.setTextColor(Color.parseColor("#2d9dff"));
        picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {

                tvDate.setText(year + "-" + month + "-" + day);

            }
        });
        picker.show();
    }

    private void sendData() {
        showLoadingView();
        HttpParams params = new HttpParams();
        params.put("uid", AppConfig.getAppConfig(this).getPrivateUid());
        params.put("token", AppConfig.getAppConfig(this).getPrivateToken());
        params.put("begintime", begintime);
        params.put("endtime", endtime);
        params.put("date", tvDate.getText().toString().substring(0,10).trim().replace("/","-"));
        params.put("theme", mEtMeetingTheme.getText().toString().trim());
        params.put("attentees", copy.toString());
        params.put("type", meetingType);
        params.put("roomid", roomId);

        LogUtils.e("uid:"+AppConfig.getAppConfig(this).getPrivateUid());
        LogUtils.e("token"+ AppConfig.getAppConfig(this).getPrivateToken());
        LogUtils.e("begintime"+begintime);
        LogUtils.e("endtime"+endtime);
        LogUtils.e("date"+tvDate.getText().toString().substring(0,10).trim());
        LogUtils.e("theme"+mEtMeetingTheme.getText().toString().trim());
        LogUtils.e("attentees"+copy.toString());
        LogUtils.e("type"+meetingType);
        LogUtils.e("roomid"+roomId);



        initKjHttp().post(Api.CONFERENCE_SETCONF, params, new HttpCallBack() {

            @Override
            public void onPreStart() {
                super.onPreStart();

            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                LogUtils.e("创建会议返回数据：" + t);
                JSONObject jo = null;
                try {
                    jo = new JSONObject(t);
                    if (Api.getCode(jo) ==Api.RESPONSES_CODE_UID_NULL){
                        catchWarningByCode(Api.getCode(jo));
                    }
                    if (Api.getCode(jo) == Api.RESPONSES_CODE_OK) {

                        showToast("发送成功");
                        startActivity(new Intent(CreateMeetingActivity.this, ScheduleActivity.class));
                        finish();

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

    private String subSting(String str) {
        int index = str.indexOf("点");
        LogUtils.e("开始或结束时间-》" + str.substring(0, index));
        return str.substring(0, index);
    }
}
