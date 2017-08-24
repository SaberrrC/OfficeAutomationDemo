package com.shanlinjinrong.oa.ui.activity.home.schedule;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.common.Api;
import com.shanlinjinrong.oa.manager.AppConfig;
import com.shanlinjinrong.oa.ui.base.BaseActivity;
import com.shanlinjinrong.oa.utils.DateUtils;
import com.shanlinjinrong.oa.utils.LogUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.qqtheme.framework.picker.DatePicker;

/**
 * ProjectName: dev-beta-v1.0.1
 * PackageName: com.itcrm.GroupInformationPlatform.ui.activity
 * Author:Created by Tsui on Date:2017/2/17 10:59
 * Description:选择会议室activity
 */
public class SelectMeetingRoomActivity extends BaseActivity {

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.toolbar_text_btn)
    TextView mToolBarBtn;
    @Bind(R.id.ll_meeting_room_container)
    LinearLayout mMeetingRoomContainer;
    @Bind(R.id.ll_meeting_Layout)
    LinearLayout mMeetingLayout;
    @Bind(R.id.cb_need_meeting_room)
    CheckBox mCbNeedRoom;
    @Bind(R.id.tv_select_meeting_room)
    TextView mTvSelectMeetingRoom;
    @Bind(R.id.tv_no_meeting_room_tips)
    TextView mTvNoMeetingRoom;
    private DatePicker picker;
    private String currentDate;
    private String meetingType;//会议类型，1普通，2视频
    private boolean isNeedMR = true;//是否需要会议室
    private String beginTime;
    private String endTime;
    private JSONArray meetRoomJa;
    private String roomId;
    private String roomName;
    private boolean noMeetingRoom = true;//是否有会议室？
    private PopupWindow popupWindow;
    private View mRootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_meeting_room);
        ButterKnife.bind(this);
        setTranslucentStatus(this);
        initToolBar();
        initWidget();
        initData();
    }

    private void initWidget() {
          mRootView = findViewById(R.id.layout_root);
        mCbNeedRoom.setChecked(true);//默认选中
        mCbNeedRoom.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    LoadData(currentDate);
                    isNeedMR = true;
                } else {
                    setAllCheckBoxDisable();
                    isNeedMR = false;
                }
            }
        });
        mTvSelectMeetingRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDoneDatePicker(mTvSelectMeetingRoom);
            }
        });
    }

    private void initData() {
        meetingType = getIntent().getStringExtra("meetingType");
        currentDate = DateUtils.getTodayDate();
        mTvSelectMeetingRoom.setText(currentDate);
        LoadData(currentDate);
    }

    private void LoadData(String date) {
        showLoadingView();
        HttpParams params = new HttpParams();
        params.put("uid", AppConfig.getAppConfig(this).getPrivateUid());
        params.put("token", AppConfig.getAppConfig(this).getPrivateToken());
        params.put("date", date);
        initKjHttp().post(Api.CONFERENCE_SELECTMEETROOM, params, new HttpCallBack() {
            @Override
            public void onFinish() {
                super.onFinish();
                hideLoadingView();
            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                LogUtils.e(t.toString());

                try {
                    JSONObject jo = new JSONObject(t);
                    switch (Api.getCode(jo)) {
                        case Api.RESPONSES_CODE_OK:

                            setDataForWidget(Api.getDataToJSONObject(jo));

                            break;
                        case Api.RESPONSES_CODE_DATA_EMPTY:
                            mTvNoMeetingRoom.setVisibility(View.VISIBLE);
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
            public void onFailure(int errorNo, String strMsg) {
                LogUtils.e("onFailure" + errorNo + strMsg);
                hideLoadingView();
                catchWarningByCode(errorNo);
                super.onFailure(errorNo, strMsg);
            }
        });
    }

    private void setDataForWidget(JSONObject dataObj) {
        try {
            if (isNeedMR) {
                addMeetingRoomLayoutAndSetData(dataObj);
            } else {
                setAllCheckBoxDisable();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            LogUtils.e("e->" + e.toString());
        }
    }

    /**
     * @param
     */
    private void addMeetingRoomLayoutAndSetData(JSONObject dataObj) throws JSONException {
        meetRoomJa = dataObj.getJSONArray("meet_room");
        JSONArray meetInfoJa = dataObj.getJSONArray("meet_info");

        int meetInfoSize = meetInfoJa.length();
        mMeetingRoomContainer.removeAllViews();
        if (meetRoomJa.length() > 0) {
            for (int i = 0; i < meetRoomJa.length(); i++) {
                JSONObject mrJo = meetRoomJa.getJSONObject(i);
                String mrRoomId = mrJo.getString("room_id");//会议室中的roomId
                String roomName = mrJo.getString("roomname");
                String address = mrJo.getString("address");
                String floor = mrJo.getString("floor");
                final String detailLocation=address+floor;

                //动态添加会议室布局;每一个会议室的布局有12个子View，对应11个时间段，第一个view是会议室名字(TextView)
                LinearLayout itemMeetingRoom = (LinearLayout) View.inflate(this, R.layout
                        .select_metting_room_item_layout, null);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout
                        .LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.FILL_PARENT);
                itemMeetingRoom.setLayoutParams(lp);
                TextView tvRoomName = (TextView) itemMeetingRoom.getChildAt(0);
                tvRoomName.setText(roomName);
                tvRoomName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //显示会议详细地址 detailLocation
                        showLocation(detailLocation);
                    }
                });

                //将已经在使用的会议室置为不可用
                for (int j = 0; j < meetInfoSize; j++) {
                    JSONObject miJo = meetInfoJa.getJSONObject(j);
                    String miRoomId = miJo.getString("roomid");//会议详情的roomId
                    String whichTime = miJo.getString("time_list");//会议详情的时间段
                    if (miRoomId.equals(mrRoomId)) {
                        //如果会议详情的roomId和会议室中的roomId一样，那么，就循环往对应的会议室时间段设置状态
                        itemMeetingRoom.getChildAt(Integer.parseInt(whichTime)).setEnabled(false);

                    }
                }

                mMeetingRoomContainer.addView(itemMeetingRoom);
            }

            mMeetingLayout.setVisibility(View.VISIBLE);
        } else {
            mCbNeedRoom.setChecked(false);
            noMeetingRoom = false;
            mMeetingLayout.setVisibility(View.GONE);
            mTvNoMeetingRoom.setVisibility(View.VISIBLE);
        }

    }

    /**
     * 将所有的checkBox置为不可用
     */
    private void setAllCheckBoxDisable() {
        int childCount = mMeetingRoomContainer.getChildCount();
        for (int i = 0; i < childCount; i++) {
            LinearLayout itemRoomView = (LinearLayout) mMeetingRoomContainer.getChildAt(i);

            for (int j = 1; j < itemRoomView.getChildCount(); j++) {
                //第0个位textView,不必设置
                itemRoomView.getChildAt(j).setEnabled(false);
            }
        }
    }

    private void initToolBar() {
        if (toolbar == null) {
            return;
        }
        setTitle("");//必须在setSupportActionBar之前调用
        toolbar.setTitleTextColor(Color.parseColor("#000000"));
        setSupportActionBar(toolbar);
        tvTitle.setText("请选择会议室");
        Toolbar.LayoutParams lp = new Toolbar.LayoutParams(
                Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        tvTitle.setLayoutParams(lp);
        mToolBarBtn.setText("下一步");
        mToolBarBtn.setVisibility(View.VISIBLE);
        mToolBarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkMRAndGoCR();
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

    /**
     * 检查会议室并跳转到创建会议界面
     */
    private void checkMRAndGoCR() {
        if (isNeedMR) {
            if (!checkMeetingRoom()) {
                return;
            }
        }
        if (mTvSelectMeetingRoom.getText().toString().trim().equals("点击选择开会日期")) {
            showToast("请选择开会日期");
            return;
        }
        Intent intent = new Intent(this, CreateMeetingActivity.class);
        intent.putExtra("date", mTvSelectMeetingRoom.getText().toString().trim());
        intent.putExtra("begintime", beginTime);
        intent.putExtra("endtime", endTime);
        intent.putExtra("meetingType", meetingType);
        intent.putExtra("roomId", roomId);
        intent.putExtra("roomName", roomName);
        startActivity(intent);
        finish();
    }

    private boolean checkMeetingRoom() {
        int childCount = mMeetingRoomContainer.getChildCount();
        Map<Integer, List<Integer>> map = new HashMap<>();
        for (int i = 0; i < childCount; i++) {//2
            LinearLayout itemRoomView = (LinearLayout) mMeetingRoomContainer.getChildAt(i);

            List<Integer> list = new ArrayList<>();
            for (int j = 1; j < itemRoomView.getChildCount(); j++) {
                //第0个位textView,不必统计
                boolean checked = ((CheckBox) itemRoomView.getChildAt(j)).isChecked();
                if (checked) {
                    list.add(j);
                }
            }
            if (!list.isEmpty()) {
                map.put(i, list);
            }
        }

        if (map.size() > 1) {
            showToast("请选择同一会议室的相邻时段");
            return false;
        } else if (map.isEmpty()) {
            //如果没有会议室
            if (noMeetingRoom) {
                showToast("请选择会议室");
                return false;
            }

        } else if (map.size() == 1) {
            Iterator<Map.Entry<Integer, List<Integer>>> iterator = map.entrySet().iterator();
            List<Integer> times = null;
            Integer key = 0;
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                key = (Integer) entry.getKey();
                times = (List) entry.getValue();
            }
            try {
                JSONObject mrJo = meetRoomJa.getJSONObject(key);
                roomId = mrJo.getString("room_id");//会议室中的roomId
                roomName = mrJo.getString("roomname");
            } catch (Exception e) {

            }
            //{1,3,5,6};
            for (int i = 0; i < times.size() - 1; i++) {
                if (times.get(i + 1) - times.get(i) > 1) {
                    showToast("请选择同一会议室的相邻时段");
                    return false;
                }
            }
            beginTime = judeTimeStage(times.get(0), true);

            endTime = judeTimeStage(times.get(times.size() - 1), false);
        } else {
            showToast("请选择同一会议室的相邻时段");
            return false;
        }
        return true;
    }

    /**
     * 根据选中CheckBox索引判断开始和结束时间
     */
    private String judeTimeStage(Integer time, boolean isBeginTime) {
        switch (time) {
            case 1:
                return isBeginTime ? "0" : "1";
            case 2:
                return isBeginTime ? "1" : "2";
            case 3:
                return isBeginTime ? "2" : "3";
            case 4:
                return isBeginTime ? "3" : "4";
            case 5:
                return isBeginTime ? "4" : "5";
            case 6:
                return isBeginTime ? "5" : "6";
            case 7:
                return isBeginTime ? "6" : "7";
            case 8:
                return isBeginTime ? "7" : "8";
            case 9:
                return isBeginTime ? "8" : "9";
            case 10:
                return isBeginTime ? "9" : "10";
            case 11:
                return isBeginTime ? "10" : "11";
            case 12:
                return isBeginTime ? "11" : "12";
            case 13:
                return isBeginTime ? "12" : "13";
            case 14:
                return isBeginTime ? "13" : "14";
            case 15:
                return isBeginTime ? "14" : "15";
            case 16:
                return isBeginTime ? "15" : "16";
            case 17:
                return isBeginTime ? "16" : "17";
            case 18:
                return isBeginTime ? "17" : "18";
            case 19:
                return isBeginTime ? "18" : "19";
            case 20:
                return isBeginTime ? "19" : "20";
            case 21:
                return isBeginTime ? "20" : "21";
            case 22:
                return isBeginTime ? "21" : "22";
            case 23:
                return isBeginTime ? "22" : "23";
            case 24:
                return isBeginTime ? "23" : "24";
        }

        return null;
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
                currentDate = year + "-" + month + "-" + day;
                LogUtils.e("DateUtils.getTodayDate()->"+DateUtils.getTodayDate());
                if (DateUtils.judeDateOrderByDay(DateUtils.getTodayDate().replace("/","-"),currentDate)) {
                    tv.setText(currentDate);
                    LoadData(currentDate);
                }else{
                    showToast("不能选择过往的日期");
                }
            }
        });
        picker.show();
    }


    private void showLocation(final String location) {

        LayoutInflater factory = LayoutInflater.from(this);

        @SuppressLint("InflateParams")
        View view = factory.inflate(R.layout.activity_select_meeting_room_pop, null);

        popupWindow = new PopupWindow(view, LinearLayout.LayoutParams
                .WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, false);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());

        TextView tvLocation = (TextView) view.findViewById(R.id.tv_detail_location);
        tvLocation.setText(location);

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

        popupWindow.setAnimationStyle(R.style.dialog_pop_anim_style);
        popupWindow.showAtLocation(mRootView, Gravity.CENTER, 0, 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
