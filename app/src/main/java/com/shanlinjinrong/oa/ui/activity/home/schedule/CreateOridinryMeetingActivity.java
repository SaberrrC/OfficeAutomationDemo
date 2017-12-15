package com.shanlinjinrong.oa.ui.activity.home.schedule;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.Toast;

import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.common.Api;
import com.shanlinjinrong.oa.manager.AppConfig;
import com.shanlinjinrong.oa.model.MeetRoom;
import com.shanlinjinrong.oa.model.selectContacts.Child;
import com.shanlinjinrong.oa.ui.activity.main.MainActivity;
import com.shanlinjinrong.oa.ui.base.BaseActivity;
import com.shanlinjinrong.oa.utils.DateUtils;
import com.shanlinjinrong.oa.utils.LogUtils;
import com.shanlinjinrong.oa.utils.StringUtils;
import com.shanlinjinrong.pickerview.OptionsPickerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.DatePicker;


/**
 * Created by Administrator on 2017/7/19.
 */

public class CreateOridinryMeetingActivity extends BaseActivity {
    private static final int ADD_JOIN_PEOPLE = 1;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_text_btn)
    TextView toolbarTextBtn;
    @BindView(R.id.et_meeting_theme)
    EditText mEtMeetingTheme;

    @BindView(R.id.tv_ordiny_meet_date)
    TextView tvOridinyMeetDate;
    @BindView(R.id.tv_meet_time)
    TextView tvMeetTime;


    @BindView(R.id.layout_root)
    RelativeLayout mRootView;

    @BindView(R.id.tv_select_meeting_room)
    TextView tvSelectMeetingRoom;
    @BindView(R.id.ll_join_people_container)
    LinearLayout mLlJoinPeopleContainer;
    private StringBuilder copy;//抄送人id
    private ArrayList<Child> contactsList; //抄送人数组
    private MeetRoom meetRoom;
    private DatePicker picker;
    private String currentDate;//当前年月日
    private OptionsPickerView beginTimeView;
    private OptionsPickerView endTimeView;
    private ArrayList<String> beginTimes;
    private ArrayList<String> endTimes;
    private String begintime;//开始时间
    private String endtime;//结束时间
    private String meetingType;
    private long lastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_oridiny_meet);
        ButterKnife.bind(this);
        initToolBar();
        setTranslucentStatus(this);
        init();
        initData();

    }

    /**
     * 选择开始时间并请求判断结束时间
     */
    private void showBeginTimeView() {
        beginTimeView = new OptionsPickerView.Builder(this,
                new OptionsPickerView.OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {

                    }
                }).isDialog(true).build();
        beginTimeView.setTitle("请选择开始时间");
        beginTimeView.setPicker(beginTimes);//添加数据
        beginTimeView.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                String RoomName = getRoomName();
                begintime = beginTimes.get(options1);
                if (begintime.equals("24:00")) {
                    Toast.makeText(CreateOridinryMeetingActivity.this, RoomName + "  "+ currentDate + "会议排期时间已到上限!", Toast.LENGTH_SHORT).show();
                    return;
                }
                tvMeetTime.setText(beginTimes.get(options1));
                sendBeginTime();
            }
        });
        beginTimeView.setCyclic(false);
        beginTimeView.show();
    }

    @NonNull
    private String getRoomName() {
        String RoomName = "";
        for (int j = 0; j < meetRoom.getRoomname().length(); j++) {
            char charAt = meetRoom.getRoomname().charAt(j);
            if ("（".equals(String.valueOf(charAt))) {
                break;
            }
            RoomName +=  charAt;
        }
        return RoomName;
    }

    /**
     * 返回结束时间
     */
    private void showEndTimeView() {
        endTimeView = new OptionsPickerView.Builder(this,
                new OptionsPickerView.OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                    }
                }).isDialog(true).build();
        endTimeView.setTitle("请选择结束时间");
        endTimeView.setPicker(endTimes);//添加数据
        endTimeView.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                tvMeetTime.setText(tvMeetTime.getText().toString() + "-" + endTimes.get(options1));
                endtime = endTimes.get(options1);
            }
        });
        endTimeView.setCyclic(false);
        endTimeView.show();
    }

    public void init() {
        beginTimes = new ArrayList<>();
        endTimes = new ArrayList<>();
        meetRoom = (MeetRoom) this.getIntent().getSerializableExtra("meetRoom");
        LogUtils.e("会议室名字：" + meetRoom.getRoomname());
    }

    private void initData() {
        mLlJoinPeopleContainer.setVisibility(View.GONE);
        contactsList = new ArrayList<>();
        copy = new StringBuilder();


        tvSelectMeetingRoom.setText(meetRoom.getRoomname());


        Intent intent = getIntent();
        meetingType = intent.getStringExtra("meetingType");
        if (null != meetingType && meetingType.equals("1")) {
            tvTitle.setText("创建普通会议");
        } else {
            tvTitle.setText("创建视频会议");
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
        tvTitle.setText("创建普通会议");
        tvTitle.setLayoutParams(lp);
        toolbar.setNavigationIcon(R.drawable.toolbar_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long currentTime = Calendar.getInstance().getTimeInMillis();
                if (currentTime - lastClickTime < 500) {
                    lastClickTime = currentTime;
                    return;
                }
                if (!StringUtils.isBlank(mEtMeetingTheme.getText().toString().trim())
                        || !tvOridinyMeetDate.getText().toString().trim().equals("点击选择会议日期")
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
                        || !tvOridinyMeetDate.getText().toString().trim().equals("点击选择会议日期")
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
//            mTvJoinPeopleTips.setVisibility(View.GONE);
            mLlJoinPeopleContainer.setVisibility(View.VISIBLE);
            copy.setLength(0);
            for (int i = 0; i < contactsList.size(); i++) {
                String username = contactsList.get(i).getUsername();

                if (i != contactsList.size() - 1) {
                    copy.append(contactsList.get(i).getUid() + ",");
                } else if (i == contactsList.size() - 1) {
                    copy.append(contactsList.get(i).getUid());
                }


                TextView tv = new TextView(CreateOridinryMeetingActivity.this);
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
    }

    @OnClick({R.id.report_iv_add_person, R.id.btn_create_meeting,
            R.id.tv_ordiny_meet_date, R.id.tv_meet_time})
    public void onClick(View view) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime < 1000) {
            lastClickTime = currentTime;
            return;
        }
        switch (view.getId()) {
            //点击选择会议日期
            case R.id.tv_ordiny_meet_date:
                showDoneDatePicker(tvOridinyMeetDate);
                break;
            case R.id.tv_meet_time:
                if (tvOridinyMeetDate.getText().toString().trim().equals("请选择会议日期")) {
                    showToast("请先选择日期");
                } else {
                    showBeginTimeView();
                }
                break;
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
                if (tvOridinyMeetDate.getText().toString().trim().equals("请选择会议日期")) {
                    showToast("会议日期不能为空!");
                    return;
                }
                if (tvMeetTime.getText().toString().trim().equals("请选择会议时间")) {
                    showToast("会议时间不能为空!");
                    return;
                }

                if (contactsList.size() < 1) {
                    showToast("没有选择参会人");
                    return;
                }

                if (tvMeetTime.getText().toString().trim().length() <= 8) {
                    showToast("会议室结束时间未选择!");
                    return;
                }

                addMeeting();

                break;
        }
    }

    /**
     * 选择日期底部弹出窗
     */
    private void showDoneDatePicker(final TextView tv) {
        if (picker == null) {
            picker = new DatePicker(this, DatePicker.YEAR_MONTH_DAY);
        }
        Calendar cal = Calendar.getInstance();
        picker.setSelectedItem(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1,
                cal.get(Calendar.DAY_OF_MONTH));
        picker.setSubmitText("确认");
        picker.setSubmitTextColor(Color.parseColor("#2d9dff"));
        picker.setTextColor(Color.parseColor("#2d9dff"));
        picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {
                currentDate = year + "-" + month + "-" + day;
                LogUtils.e("DateUtils.getTodayDate()->" + DateUtils.getTodayDate());
                if (DateUtils.judeDateOrderByDay(DateUtils.getTodayDate().replace("/", "-"), currentDate)) {
                    tv.setText(currentDate);
                    sendData(currentDate);
                } else {
                    showToast("不能选择过往的日期");
                }
            }
        });
        picker.show();
    }

    private void sendBeginTime() {
        showLoadingView();
        HttpParams params = new HttpParams();
        params.put("begintime", String.valueOf(tvMeetTime.getText().toString().trim().substring(0, tvMeetTime.length() - 3)));
        params.put("date", currentDate);
        params.put("roomid", meetRoom.getRoom_id());
        params.put("uid", AppConfig.getAppConfig(this).getPrivateUid());
        params.put("token", AppConfig.getAppConfig(this).getPrivateToken());

        LogUtils.e("--------时间：" + String.valueOf(tvMeetTime.getText().toString().trim().substring(0, tvMeetTime.length() - 3)));
        LogUtils.e("---------roomId:" + meetRoom.getRoom_id());
        LogUtils.e("---------日期：" + currentDate);
        LogUtils.e("------uid:" + AppConfig.getAppConfig(this).getPrivateUid());
        LogUtils.e("------token:" + AppConfig.getAppConfig(this).getPrivateToken());
        initKjHttp().post(Api.CONFERENCE_GETOCCUPYTIMEBYBEGINTIME, params, new HttpCallBack() {

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
                            JSONObject data = Api.getDataToJSONObject(jo);
                            JSONArray time = data.getJSONArray("time");
                            for (int i = 0; i < time.length(); i++) {
                                endTimes.add(time.getString(i));
                            }
                            showEndTimeView();

                            break;
                        case Api.RESPONSES_CODE_DATA_EMPTY:
                            break;
                        case Api.RESPONSES_CODE_TOKEN_NO_MATCH:
                            catchWarningByCode(Api.getCode(jo));
                            break;
                        case Api.RESPONSES_CODE_UID_NULL:
                            catchWarningByCode(Api.getCode(jo));
                            break;
                    }
                } catch (JSONException e) {
                    System.out.println(e.toString());
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

    private void addMeeting() {
        showLoadingView();
        HttpParams params = new HttpParams();
        params.put("uid", AppConfig.getAppConfig(this).getPrivateUid());
        params.put("token", AppConfig.getAppConfig(this).getPrivateToken());
        params.put("date", currentDate);
        params.put("roomid", meetRoom.getRoom_id());
        params.put("attentees", copy.toString());
        params.put("theme", mEtMeetingTheme.getText().toString().trim());
        params.put("type", meetingType);
        params.put("begintime", begintime);
        params.put("endtime", endtime);

//        LogUtils.e("uid:"+AppConfig.getAppConfig(this).getPrivateUid());
//        LogUtils.e("token"+ AppConfig.getAppConfig(this).getPrivateToken());
//        LogUtils.e("begintime"+begintime);
//        LogUtils.e("endtime"+endtime);
//        LogUtils.e("date"+tvDate.getText().toString().substring(0,10).trim());
//        LogUtils.e("theme"+mEtMeetingTheme.getText().toString().trim());
//        LogUtils.e("attentees"+copy.toString());
//        LogUtils.e("type"+meetingType);
//        LogUtils.e("roomid"+roomId);


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
                    if (Api.getCode(jo) == Api.RESPONSES_CODE_OK) {

                        showToast("发送成功");
                        startActivity(new Intent(CreateOridinryMeetingActivity.this, MainActivity.class));
                        finish();

                    } else if ((Api.getCode(jo) == Api.RESPONSES_CODE_UID_NULL)) {
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


    private void sendData(String date) {

        showLoadingView();
        HttpParams params = new HttpParams();
        params.put("uid", AppConfig.getAppConfig(this).getPrivateUid());
        params.put("token", AppConfig.getAppConfig(this).getPrivateToken());
        params.put("date", date);
        params.put("roomid", meetRoom.getRoom_id());

        LogUtils.e("---------日期：" + date);
        LogUtils.e("---------roomId:" + meetRoom.getRoom_id());
        initKjHttp().post(Api.CONFERENCE_GETOCCUPYTIME, params, new HttpCallBack() {

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
                            beginTimes.clear();
                            JSONObject data = Api.getDataToJSONObject(jo);
                            JSONArray time = data.getJSONArray("time");
                            for (int i = 0; i < time.length(); i++) {

                                beginTimes.add(time.getString(i));
                                //System.out.println(time.getString(i));

                            }

                            break;
                        case Api.RESPONSES_CODE_DATA_EMPTY:
                            break;
                        case Api.RESPONSES_CODE_TOKEN_NO_MATCH:
                            catchWarningByCode(Api.getCode(jo));
                            break;
                        case Api.RESPONSES_CODE_UID_NULL:
                            catchWarningByCode(Api.getCode(jo));
                            break;
                    }
                } catch (JSONException e) {
                    System.out.println(e.toString());
                }
            }
//            @Override
//            public void onSuccess(String t) {
//                super.onSuccess(t);
//                LogUtils.e("创建会议返回数据：" + t);
//                JSONObject jo = null;
//                try {
//                    jo = new JSONObject(t);
//                    if (Api.getCode(jo) == Api.RESPONSES_CODE_OK) {
//
//                        showToast("发送成功");
//                        startActivity(new Intent(CreateOridinryMeetingActivity.this, ScheduleActivity.class));
//                        finish();
//
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }

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
