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

import com.bigkoo.pickerview.OptionsPickerView;
import com.shanlin.oa.R;
import com.shanlin.oa.common.Api;
import com.shanlin.oa.manager.AppConfig;
import com.shanlin.oa.model.MeetRoom;
import com.shanlin.oa.model.selectContacts.Child;
import com.shanlin.oa.ui.base.BaseActivity;
import com.shanlin.oa.utils.DateUtils;
import com.shanlin.oa.utils.LogUtils;
import com.shanlin.oa.utils.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.UUID;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.DatePicker;


/**
 * Created by Administrator on 2017/7/19.
 */

public class CreateVedioMeetingActivity extends BaseActivity {
    private static final int ADD_JOIN_PEOPLE = 1;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.toolbar_text_btn)
    TextView toolbarTextBtn;
    @Bind(R.id.et_meeting_theme)
    EditText mEtMeetingTheme;

    @Bind(R.id.tv_ordiny_meet_date)
    TextView tvOridinyMeetDate;
    @Bind(R.id.tv_meet_time)
    TextView tvMeetTime;


    @Bind(R.id.layout_root)
    RelativeLayout mRootView;

    @Bind(R.id.tv_select_meeting_room)
    TextView tvSelectMeetingRoom;
    @Bind(R.id.ll_join_people_container)
    LinearLayout mLlJoinPeopleContainer;
    private StringBuilder copy;//抄送人id
    private ArrayList<Child> contactsList; //抄送人数组
    private MeetRoom meetRoom;
    private DatePicker picker;
    private String currentDate;//当前年月日
    private OptionsPickerView beginTimeView;
    private OptionsPickerView endTimeView;
    //    private ArrayList<String> beginTimes;
    private String[] dataTimes = {"0:00", "2:00", "3:00", "4:00", "5:00", "6:00", "7:00", "8:00", "9:00", "10:00", "11:00", "12:00",
            "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00", "23:00"};
    ArrayList<String> dataTimesList = new ArrayList<>(Arrays.asList(dataTimes));
    private ArrayList<String> beginTimeList;
    private String begintime;//开始时间
    private String endtime;//结束时间
    private String meetingType;

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

    private void showBeginTimeView() {
        beginTimeView = new OptionsPickerView.Builder(this,
                new OptionsPickerView.OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        //返回的分别是三个级别的选中位置
//                String tx = beginTimes.get(options1);
//                tvMeetTime.setText(beginTimes.get(options1));

                    }
                }).isDialog(true).build();
        beginTimeView.setTitle("请选择开始时间");

        beginTimeList = new ArrayList<>();
        for (int i = 0; i < dataTimesList.size() - 1; i++) {
            beginTimeList.add(dataTimesList.get(i));
        }
        beginTimeView.setPicker(beginTimeList);//添加数据
        beginTimeView.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                tvMeetTime.setText(beginTimeList.get(options1));
                begintime = beginTimeList.get(options1);
//                sendBeginTime();
                showEndTimeView(markEndTimesForBegin(options1));
            }

        });
        beginTimeView.setCyclic(false);
        beginTimeView.show();

    }

    private ArrayList<String> markEndTimesForBegin(int position) {
        ArrayList<String> list = new ArrayList<>();
        for (int i = position + 1; i < dataTimesList.size(); i++) {
            list.add(dataTimesList.get(i));
        }
        return list;
    }


    private void showEndTimeView(final ArrayList<String> times) {
        endTimeView = new OptionsPickerView.Builder(this,
                new OptionsPickerView.OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                    }
                }).isDialog(true).build();
        endTimeView.setTitle("请选择结束时间");
        endTimeView.setPicker(times);//添加数据
        endTimeView.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {

                tvMeetTime.setText(tvMeetTime.getText().toString() + "-" + times.get(options1));
                endtime = times.get(options1);
            }
        });
        endTimeView.setCyclic(false);
        endTimeView.show();
    }

    public void init() {
//        beginTimes = new ArrayList<>();
//        endTimes = new ArrayList<>();
//        meetRoom = (MeetRoom) this.getIntent().getSerializableExtra("meetRoom");
//        LogUtils.e("会议室名字："+meetRoom.getRoomname());
    }

    private void initData() {
        mLlJoinPeopleContainer.setVisibility(View.GONE);
        contactsList = new ArrayList<>();
        copy = new StringBuilder();


        tvSelectMeetingRoom.setText("-");


        Intent intent = getIntent();
        meetingType = intent.getStringExtra("meetingType");
        if (null != meetingType && meetingType.equals("1")) {
            tvTitle.setText("创建普通会议");
//            tvSelectedMRType.setText("普通会议");
            //tvSelectEdMRType
        } else {
            tvTitle.setText("创建视频会议");
//            tvSelectedMRType.setText("视频会议");
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


                TextView tv = new TextView(CreateVedioMeetingActivity.this);
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

    @OnClick({R.id.report_iv_add_person, R.id.btn_create_meeting,
            R.id.tv_ordiny_meet_date, R.id.tv_meet_time})
    public void onClick(View view) {
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

                addMeeting();

                break;
        }
    }


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
//                    sendData(currentDate);
                } else {
                    showToast("不能选择过往的日期");
                }
            }
        });
        picker.show();
    }

    //private  void sendBeginTime(){
//    showLoadingView();
//    HttpParams params = new HttpParams();
//    params.put("begintime", String.valueOf(tvMeetTime.getText().toString().trim().substring(0,tvMeetTime.length()-3)));
//    params.put("date",currentDate);
//    params.put("roomid", 0);
//    params.put("uid", AppConfig.getAppConfig(this).getPrivateUid());
//    params.put("token", AppConfig.getAppConfig(this).getPrivateToken());
//
//    LogUtils.e("--------时间："+String.valueOf(tvMeetTime.getText().toString().trim().substring(0,tvMeetTime.length()-3)));
////    LogUtils.e("---------roomId:"+meetRoom.getRoom_id());
//    LogUtils.e("---------日期："+currentDate);
//    LogUtils.e("------uid:"+AppConfig.getAppConfig(this).getPrivateUid());
//    LogUtils.e("------token:"+AppConfig.getAppConfig(this).getPrivateToken());
//    initKjHttp().post(Api.CONFERENCE_GETOCCUPYTIMEBYBEGINTIME, params, new HttpCallBack() {
//
//        @Override
//        public void onPreStart() {
//            super.onPreStart();
//
//        }
//        @Override
//        public void onSuccess(String t) {
//
//            super.onSuccess(t);
//            LogUtils.e(t);
//            try {
//                JSONObject jo = new JSONObject(t);
//                switch (Api.getCode(jo)) {
//                    case Api.RESPONSES_CODE_OK:
//                        JSONObject data = Api.getDataToJSONObject(jo);
//                        JSONArray time = data.getJSONArray("time");
//                        for (int i = 0; i < time.length(); i++) {
//                            endTimes.add(time.getString(i));
//                        }
//                        showEndTimeView();
//
//                        break;
//                    case Api.RESPONSES_CODE_DATA_EMPTY:
//                        break;
//                    case Api.RESPONSES_CODE_TOKEN_NO_MATCH:
//                        catchWarningByCode(Api.getCode(jo));
//                        break;
//                }
//            } catch (JSONException e) {
//                System.out.println(e.toString());
//            }
//        }
//
//        @Override
//        public void onFinish() {
//            super.onFinish();
//            hideLoadingView();
//        }
//
//        @Override
//        public void onFailure(int errorNo, String strMsg) {
//            super.onFailure(errorNo, strMsg);
//            LogUtils.e("errorNo,strMsg--->" + errorNo + "," + strMsg);
//            catchWarningByCode(errorNo);
//        }
//    });
//}
    private void addMeeting() {
        showLoadingView();
        HttpParams params = new HttpParams();
        params.put("uid", AppConfig.getAppConfig(this).getPrivateUid());
        params.put("token", AppConfig.getAppConfig(this).getPrivateToken());
        params.put("date", currentDate);
        params.put("roomid", 0);
        params.put("attentees", copy.toString());
        String roomName = mEtMeetingTheme.getText().toString().trim() + UUID.randomUUID().toString().replaceAll("-", "");
        params.put("theme", roomName);
        params.put("type", meetingType);
        params.put("begintime", begintime);
        params.put("endtime", endtime);

        LogUtils.e("开始时间-----" + begintime);
        LogUtils.e("结束时间-----" + endtime);
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
                        startActivity(new Intent(CreateVedioMeetingActivity.this, ScheduleActivity.class));
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


//    private void sendData(String date) {
//
//        showLoadingView();
//        HttpParams params = new HttpParams();
//        params.put("uid", AppConfig.getAppConfig(this).getPrivateUid());
//        params.put("token", AppConfig.getAppConfig(this).getPrivateToken());
//        params.put("date", date);
//        params.put("roomid", 0);
//
//        LogUtils.e("---------日期："+date);
//
//        initKjHttp().post(Api.CONFERENCE_GETOCCUPYTIME, params, new HttpCallBack() {
//
//            @Override
//            public void onPreStart() {
//                super.onPreStart();
//
//            }
//            @Override
//            public void onSuccess(String t) {
//
//                super.onSuccess(t);
//                LogUtils.e(t);
//                try {
//                    JSONObject jo = new JSONObject(t);
//                    switch (Api.getCode(jo)) {
//                        case Api.RESPONSES_CODE_OK:
//                            JSONObject data = Api.getDataToJSONObject(jo);
//                            JSONArray time = data.getJSONArray("time");
//                            for (int i = 0; i < time.length(); i++) {
//                                beginTimes.add(time.getString(i));
//                                //System.out.println(time.getString(i));
//
//                        }
//
//                            break;
//                        case Api.RESPONSES_CODE_DATA_EMPTY:
//                            break;
//                        case Api.RESPONSES_CODE_TOKEN_NO_MATCH:
//                            catchWarningByCode(Api.getCode(jo));
//                            break;
//                    }
//                } catch (JSONException e) {
//                    System.out.println(e.toString());
//                }
//            }
////            @Override
////            public void onSuccess(String t) {
////                super.onSuccess(t);
////                LogUtils.e("创建会议返回数据：" + t);
////                JSONObject jo = null;
////                try {
////                    jo = new JSONObject(t);
////                    if (Api.getCode(jo) == Api.RESPONSES_CODE_OK) {
////
////                        showToast("发送成功");
////                        startActivity(new Intent(CreateOridinryMeetingActivity.this, ScheduleActivity.class));
////                        finish();
////
////                    }
////
////                } catch (JSONException e) {
////                    e.printStackTrace();
////                }
////
////            }
//
//            @Override
//            public void onFinish() {
//                super.onFinish();
//                hideLoadingView();
//            }
//
//            @Override
//            public void onFailure(int errorNo, String strMsg) {
//                super.onFailure(errorNo, strMsg);
//                LogUtils.e("errorNo,strMsg--->" + errorNo + "," + strMsg);
//                catchWarningByCode(errorNo);
//            }
//        });
//    }


}
