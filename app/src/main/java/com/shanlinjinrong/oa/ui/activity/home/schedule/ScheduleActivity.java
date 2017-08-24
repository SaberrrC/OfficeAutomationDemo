package com.shanlinjinrong.oa.ui.activity.home.schedule;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.dsw.calendar.component.MonthView;
import com.dsw.calendar.entity.CalendarInfo;
import com.dsw.calendar.inter.ListenerMove;
import com.dsw.calendar.views.CircleCalendarView;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.common.Api;
import com.shanlinjinrong.oa.common.Constants;
import com.shanlinjinrong.oa.manager.AppConfig;
import com.shanlinjinrong.oa.model.ScheduleRvItemData;
import com.shanlinjinrong.oa.ui.adapter.CalendarRvAdapter;
import com.shanlinjinrong.oa.ui.base.BaseActivity;
import com.shanlinjinrong.oa.utils.LogUtils;
import com.shanlinjinrong.oa.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * ProjectName: dev-beta-v1.0.1
 * PackageName: com.itcrm.GroupInformationPlatform.ui.activity
 * Author:Created by Tsui on Date:2016/12/7 15:37
 * Description:日程安排
 */
public class ScheduleActivity extends BaseActivity {
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.circlecalendarview)
    CircleCalendarView mCalendar;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.rg_bottom_tips)
    RadioGroup mRadioGroup;
    @Bind(R.id.rb_meeting_plan)
    RadioButton mMeetingPlan;
    @Bind(R.id.rb_me_launch)
    RadioButton mMeLaunch;
    @Bind(R.id.rb_note)
    RadioButton mNote;
    @Bind(R.id.toolbar_text_btn)
    TextView mToolbarTextBtn;
    @Bind(R.id.layout_root)
    LinearLayout mRootView;
    @Bind(R.id.rvCalendar)
    RecyclerView mRecyclerView;
    private PopupWindow popupWindow;
    private String dataPrefix;//当前月的前缀2016-12
    private String currentDate;//当前天
    CalendarRvAdapter mAdapter;
    private String showDataType = "1";//当前展示的数据类型，默认为1
    private JSONObject currentMonthJA = new JSONObject();//当前月的数据
    private static boolean IS_FIRST_ENTER = true;

    private List<ScheduleRvItemData> scheduleRvDataList;
    private ArrayList<CalendarInfo> CalendarInfoList;
    private LinearLayoutManager linearLayoutManager;
    int TempAddDate = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        ButterKnife.bind(this);
        initToolBar();
        setTranslucentStatus(this);
        initWidget();

        handleIntent(getIntent());
        loadData(dataPrefix);
    }

    private void handleIntent(Intent intent) {

        int whichId = intent.getIntExtra("whichId", 1);
        switch (whichId) {
            case 1:
                mMeetingPlan.setChecked(true);
                mMeetingPlan.setTextColor(Color.parseColor("#ffffff"));
                break;
            case 2:
                mMeLaunch.setChecked(true);
                mMeLaunch.setTextColor(Color.parseColor("#ffffff"));
                break;
            case 3:
                mNote.setChecked(true);
                mNote.setTextColor(Color.parseColor("#ffffff"));
                break;
        }
        loadData(currentDate);
    }

    /**
     * @param intent launchMode设置为singtask的时候，getIntent()将不会得到参数，需使用下面的方法
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        handleIntent(intent);
    }

    private void initWidget() {
        scheduleRvDataList = new ArrayList<>();
        mAdapter = new CalendarRvAdapter(scheduleRvDataList, showDataType);
        mRecyclerView.setAdapter(mAdapter);

        linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                String cf_id = "";
                String nt_id = "";
                Intent intent = null;
                switch (Integer.parseInt(showDataType)) {
                    case 1:
                        cf_id = scheduleRvDataList.get(i).getCf_id();
                        intent = new Intent();
                        intent.putExtra("cf_id", cf_id);
                        intent.putExtra("meeting_mode", Constants.MEETING_PLAN);
                        intent.putExtra("isShowJoin", false);

//                        if (Integer.parseInt(cf_id) >= 9999) {
//                            // TODO 为演示做的演示界面
//                            intent.setClass(ScheduleActivity.this, MeetingInfoVideoActivity.class);
//                        } else {
                        intent.setClass(ScheduleActivity.this, MeetingInfoActivity.class);
//                        }
                        break;
                    case 2:
                        cf_id = scheduleRvDataList.get(i).getCf_id();
                        intent = new Intent();
                        intent.putExtra("cf_id", cf_id);
                        intent.putExtra("meeting_mode", Constants.ME_LAUNCH_MEETING);
                        intent.putExtra("isShowJoin", false);
                        intent.setClass(ScheduleActivity.this, MeetingInfoActivity.class);
                        break;

                    case 3:
                        nt_id = scheduleRvDataList.get(i).getCf_id();
                        intent = new Intent();
                        intent.putExtra("note_id", nt_id);
                        intent.setClass(ScheduleActivity.this, NoteDetailActivity.class);
                        break;
                }
                startActivity(intent);
            }
        });
        currentDate = Utils.getDateByTimestamp(System.currentTimeMillis());
        dataPrefix = currentDate.substring(0, currentDate.length() - 3);


//        mCalendar.setDayTheme(new MyIDayTheme());
//        mCalendar.setWeekTheme(new MyIWeekTheme());  CircleCaclendarView

        mCalendar.setListenerMove(new ListenerMove() {

            @Override
            public void leftMove(String year, String month) {
                TempAddDate--;
                int intMonth = Integer.parseInt(month) + TempAddDate;
                String strMonth = String.valueOf(intMonth);

                String date = year + "-" + SubStringMonth(strMonth);
                LogUtils.e("leftMove->" + "date:" + date + ",dataPrefix:" + dataPrefix);

                dataPrefix = date;
                loadData(date);
            }

            @Override
            public void rightMove(String year, String month) {
                TempAddDate++;
                int intMonth = Integer.parseInt(month) + TempAddDate;
                String strMonth = String.valueOf(intMonth);


                String date = year + "-" + SubStringMonth(strMonth);
                dataPrefix = date;
                loadData(date);
            }

            @Override
            public void currentDate(String year, String month) {

            }
        });


        mCalendar.setDateClick(new MonthView.IDateClick() {

            @Override
            public void onClickOnDate(int year, int month, int day) {
                String strMonth = "";
                if (month < 10) {
                    strMonth = "0" + month;
                } else {
                    strMonth = String.valueOf(month);
                }

                String strDay = "";
                if (day < 10) {
                    strDay = "0" + day;
                } else {
                    strDay = String.valueOf(day);
                }
                currentDate = year + "-" + strMonth + "-" + strDay;
                LogUtils.e("onClickOnDate->currentDate:" + currentDate);
                refreshData();
            }
        });

        mMeetingPlan.setChecked(true);
        mMeetingPlan.setTextColor(Color.parseColor("#ffffff"));
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                mMeetingPlan.setTextColor(Color.parseColor("#999999"));
                mMeLaunch.setTextColor(Color.parseColor("#999999"));
                mNote.setTextColor(Color.parseColor("#999999"));
                switch (checkedId) {
                    case R.id.rb_meeting_plan:
                        mMeetingPlan.setTextColor(Color.parseColor("#ffffff"));
                        showDataType = "1";
                        refreshData();
                        break;
                    case R.id.rb_me_launch:
                        mMeLaunch.setTextColor(Color.parseColor("#ffffff"));
                        showDataType = "2";
                        refreshData();
                        break;
                    case R.id.rb_note:
                        mNote.setTextColor(Color.parseColor("#ffffff"));
                        showDataType = "3";
                        refreshData();
                        break;
                }
            }
        });


    }

    public String SubStringMonth(String month) {
        String result = "";
        int monthStr = Integer.parseInt(month);
        if (monthStr <= 9) {
            result = "0" + monthStr;
        } else {
            result = monthStr + "";
        }

        return result;
    }

    private void loadData(String date) {
        showLoadingView();
        HttpParams params = new HttpParams();
        params.put("uid", AppConfig.getAppConfig(this).getPrivateUid());
        params.put("token", AppConfig.getAppConfig(this).getPrivateToken());
        params.put("date", date);
//        LogUtils.e("加载"+date+"的数据");
        initKjHttp().post(Api.AGENDA_INDEX, params, new HttpCallBack() {

            @Override
            public void onPreStart() {
                super.onPreStart();

            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                hideLoadingView();
                LogUtils.e("onSuccess->" + t);
                try {
                    JSONObject jo = new JSONObject(t);
                    switch (Api.getCode(jo)) {
                        case Api.RESPONSES_CODE_OK:
                            currentMonthJA = Api.getDataToJSONObject(jo);
                            LogUtils.e("onSuccess->currentMonthJA:" + currentMonthJA);
                            initData();
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

    private void initData() {
        try {
            Iterator<String> keys = currentMonthJA.keys();
            CalendarInfoList = new ArrayList<>();
            while (keys.hasNext()) {
                String key = keys.next();
                JSONObject jsonObject = currentMonthJA.getJSONObject(key);
                String isred = jsonObject.getString("isred");
                if (isred.equals("1")) {
                    CalendarInfoList.add(new CalendarInfo(subStringYear(key), subStringMonth(key), subStringDay(key), "1"));
                }
            }
            mCalendar.setCalendarInfos(CalendarInfoList);
            refreshData();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!IS_FIRST_ENTER) {
            loadData(dataPrefix);
        }

    }

    @Override
    protected void onPause() {
        IS_FIRST_ENTER = false;
        super.onPause();
    }

    private void refreshData() {
        try {
            scheduleRvDataList.clear();

            Iterator<String> keys = currentMonthJA.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                JSONObject jsonObject = currentMonthJA.getJSONObject(key);


                String isred = jsonObject.getString("isred");

                if (key.equals(currentDate)) {
                    LogUtils.e("key:" + key + ",currentDate:" + currentDate);

                    if (isred.equals("1")) {

                        JSONArray icreate = jsonObject.getJSONArray("icreate");
                        JSONArray meeting = jsonObject.getJSONArray("meeting");
                        JSONArray note = jsonObject.getJSONArray("note");

                        //添加数据
                        if (showDataType.equals("1")) {
                            for (int j = 0; j < meeting.length(); j++) {
                                JSONObject jsonObject2 = meeting.getJSONObject(j);
                                ScheduleRvItemData data = new ScheduleRvItemData();
                                data.setCf_id(jsonObject2.getString("cf_id"));
                                data.setRoomname(jsonObject2.getString("roomname"));
                                data.setTime(jsonObject2.getString("time"));
                                //截取字符串,防止创建房间417问题
                                int length = jsonObject2.getString("theme").trim().length();
                                if (length > 32) {
                                    length -= 32;
                                }
                                String roomName = jsonObject2.getString("theme").trim().substring(0, length);
                                data.setTheme(roomName);
                                scheduleRvDataList.add(data);
                            }
                        } else if (showDataType.equals("2")) {
                            for (int j = 0; j < icreate.length(); j++) {
                                JSONObject jsonObject1 = icreate.getJSONObject(j);
                                ScheduleRvItemData data = new ScheduleRvItemData();
                                data.setCf_id(jsonObject1.getString("cf_id"));
                                data.setRoomname(jsonObject1.getString("roomname"));
                                data.setTime(jsonObject1.getString("time"));
                                //截取字符串,防止创建房间417问题
                                int length = jsonObject1.getString("theme").trim().length();
                                if (length > 32) {
                                    length -= 32;
                                }
                                String roomName = jsonObject1.getString("theme").trim().substring(0, length);
                                data.setTheme(roomName);
                                scheduleRvDataList.add(data);
                            }
                        } else if (showDataType.equals("3")) {
                            for (int g = 0; g < note.length(); g++) {
                                JSONObject jsonObject3 = note.getJSONObject(g);

                                ScheduleRvItemData data = new ScheduleRvItemData();
                                data.setCf_id(jsonObject3.getString("nt_id"));
                                data.setTheme(jsonObject3.getString("content"));
                                //在adapter里边用来区分三种状态的，下面设置的3
                                data.setRoomname("");
                                data.setTime("");

                                scheduleRvDataList.add(data);

                            }
                        }

//                    mAdapter = new CalendarRvAdapter(scheduleRvDataList, showDataType);
//                    mRecyclerView.setAdapter(mAdapter);
//                        // TODO 16.56tianjia
//                        break;
                    }

                }
            }
            mAdapter = new CalendarRvAdapter(scheduleRvDataList, showDataType);
            mRecyclerView.setAdapter(mAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
            LogUtils.e("抛异常了。。。。。。" + e.toString());

        }

    }


    private void initToolBar() {
        if (mToolbar == null) {
            return;
        }
        setTitle("");//必须在setSupportActionBar之前调用
        mToolbar.setTitleTextColor(Color.parseColor("#000000"));
        setSupportActionBar(mToolbar);
        mTvTitle.setText("日程安排");
        Toolbar.LayoutParams lp = new Toolbar.LayoutParams(
                Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        mTvTitle.setLayoutParams(lp);
        mToolbarTextBtn.setVisibility(View.VISIBLE);
        mToolbarTextBtn.setText("创建");
        mToolbarTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDetailDialog();
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

    /**
     *
     */
    public void showDetailDialog() {
        View contentView = LayoutInflater.from(this).inflate(R.layout
                .schedule_popup_window, null);
        if (popupWindow == null) {
            popupWindow = new PopupWindow(contentView, LinearLayout.LayoutParams
                    .WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, false);
            TextView tvMeeting = (TextView) contentView.findViewById(R.id.tv_create_meeting);
            TextView tvVideoMeeting = (TextView) contentView.findViewById(R.id.tv_create_video_meeting);
            TextView tvNote = (TextView) contentView.findViewById(R.id.tv_create_note_book);
            tvMeeting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ScheduleActivity.this, SelectOrdinaryMeetingRoomActivity.class);
                    intent.putExtra("meetingType", "1");
                    startActivity(intent);
                    popupWindow.dismiss();
                }
            });
            tvVideoMeeting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ScheduleActivity.this, SelectVedioMeetingRoomActivity.class);
                    intent.putExtra("meetingType", "2");
                    startActivity(intent);
                    popupWindow.dismiss();
                }
            });
            tvNote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(ScheduleActivity.this, CreateNoteActivity.class));
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

        popupWindow.setFocusable(true);
        popupWindow.showAtLocation(mRootView, Gravity.CENTER, 0, 0);
    }

    public int subStringYear(String year) {
        return Integer.parseInt(year.substring(0, 4));
    }

    public int subStringMonth(String month) {
        //2016-05-12
        String monthStr = month.substring(5, 7);
        return Integer.valueOf(monthStr).intValue();
    }

    public int subStringDay(String day) {
        //2016-05-12
        String dayStr = day.substring(8, 10);
        return Integer.valueOf(dayStr).intValue();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }


}
