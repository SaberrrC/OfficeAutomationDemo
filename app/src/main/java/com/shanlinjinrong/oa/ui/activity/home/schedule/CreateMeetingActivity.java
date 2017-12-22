package com.shanlinjinrong.oa.ui.activity.home.schedule;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.model.selectContacts.Child;
import com.shanlinjinrong.oa.ui.activity.home.schedule.contract.CreateMeetingContract;
import com.shanlinjinrong.oa.ui.activity.home.schedule.presenter.CreateMeetingPresenter;
import com.shanlinjinrong.oa.ui.activity.main.MainActivity;
import com.shanlinjinrong.oa.ui.base.HttpBaseActivity;
import com.shanlinjinrong.oa.utils.LogUtils;
import com.shanlinjinrong.oa.utils.StringUtils;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.DatePicker;

/**
 * ProjectName: dev-beta-v1.0.1
 * PackageName: com.itcrm.GroupInformationPlatform.ui.activity
 * Author:Created by Tsui on Date:2016/12/8 15:17
 * Description:创建会议
 */
public class CreateMeetingActivity extends HttpBaseActivity<CreateMeetingPresenter> implements CreateMeetingContract.View {
    private static final int ADD_JOIN_PEOPLE = 1;
    @BindView(R.id.tv_title)
    TextView       tvTitle;
    @BindView(R.id.toolbar)
    Toolbar        toolbar;
    @BindView(R.id.toolbar_text_btn)
    TextView       toolbarTextBtn;
    @BindView(R.id.tv_join_people_tips)
    TextView       mTvJoinPeopleTips;
    @BindView(R.id.et_meeting_theme)
    EditText       mEtMeetingTheme;
    @BindView(R.id.tv_select_meeting_room)
    TextView       tvSelectMeetingRoom;
    @BindView(R.id.layout_root)
    RelativeLayout mRootView;
    @BindView(R.id.tv_date)
    TextView       tvDate;
    @BindView(R.id.tv_selected_meeting_room_type)
    TextView       tvSelectedMRType;
    @BindView(R.id.ll_join_people_container)
    LinearLayout   mLlJoinPeopleContainer;
    private StringBuilder    copy;//抄送人id
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

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
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

        Toolbar.LayoutParams lp = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        tvTitle.setLayoutParams(lp);
        toolbar.setNavigationIcon(R.drawable.toolbar_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!StringUtils.isBlank(mEtMeetingTheme.getText().toString().trim()) || !tvDate.getText().toString().trim().equals("点击选择会议日期")) {
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
                if (!StringUtils.isBlank(mEtMeetingTheme.getText().toString().trim()) || !tvDate.getText().toString().trim().equals("点击选择会议日期")) {
                    showTip("是否放弃编辑", "确定", "取消");
                }
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void showTip(String msg, final String posiStr, String negaStr) {
        @SuppressLint("InflateParams") View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_exit_editor, null);
        TextView title = (TextView) dialogView.findViewById(R.id.title);
        title.setText("提示");
        TextView message = (TextView) dialogView.findViewById(R.id.message);
        message.setText(msg);

        final AlertDialog alertDialog = new AlertDialog.Builder(this, R.style.AppTheme_Dialog).create();
        alertDialog.setView(dialogView);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, posiStr, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                finish();

            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, negaStr, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.btn_text_logout));
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.btn_text_logout));
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
                    if (!TextUtils.isEmpty(contactsList.get(i).getUid()))
                        copy.append(contactsList.get(i).getUid() + ",");
                } else if (i == contactsList.size() - 1) {
                    if (!TextUtils.isEmpty(contactsList.get(i).getUid()))
                        copy.append(contactsList.get(i).getUid());
                }


                TextView tv = new TextView(CreateMeetingActivity.this);
                tv.setTextColor(Color.parseColor("#999999"));
                tv.setText(username);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
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

    @OnClick({R.id.report_iv_add_person, R.id.btn_create_meeting})
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
        picker.setSelectedItem(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH));
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
        mPresenter.sendData(begintime, endtime, tvDate.getText().toString().substring(0, 10).trim().replace("/", "-"), mEtMeetingTheme.getText().toString().trim(), copy.toString(), meetingType, roomId);
    }

    private String subSting(String str) {
        int index = str.indexOf("点");
        LogUtils.e("开始或结束时间-》" + str.substring(0, index));
        return str.substring(0, index);
    }

    @Override
    public void sendDataSuccess() {
        showToast("发送成功");
        startActivity(new Intent(CreateMeetingActivity.this, MainActivity.class));
        finish();
    }

    @Override
    public void sendDataFailed(int errCode, String msg) {
        catchWarningByCode(errCode);
    }

    @Override
    public void sendDataFinish() {
        hideLoadingView();
    }

    @Override
    public void uidNull(int code) {
        catchWarningByCode(code);

    }
}
