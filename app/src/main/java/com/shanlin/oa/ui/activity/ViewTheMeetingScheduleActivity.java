package com.shanlin.oa.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.shanlin.oa.R;
import com.shanlin.oa.common.Api;
import com.shanlin.oa.manager.AppConfig;
import com.shanlin.oa.model.MeetDatils;
import com.shanlin.oa.model.MeetRoom;
import com.shanlin.oa.ui.adapter.ViewTheMeetingScheduleAdapter;
import com.shanlin.oa.ui.base.BaseActivity;
import com.shanlin.oa.utils.DateUtils;
import com.shanlin.oa.utils.LogUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.DatePicker;

/**
 * Created by Administrator on 2017/7/18.
 */

public class ViewTheMeetingScheduleActivity extends BaseActivity {
    @Bind(R.id.toobar_back)
    ImageView toobar_back;
    @Bind(R.id.day_arrangement)
    TextView dayArrangement;
    @Bind(R.id.meet_create)
    TextView meetCreate;
    private DatePicker picker;
    private String currentDate;
    private MeetRoom meetRoom;
    List<MeetDatils> listMeeting = null;
    @Bind(R.id.recyclerView_time_result)
    RecyclerView mRecyclerViewList;
    private  ViewTheMeetingScheduleAdapter viewTheMeetingScheduleAdapter;
    @Bind(R.id.no_meeting)
    TextView noMeeting;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_the_meeting_schedule);
        ButterKnife.bind(this);
        init();
        initWidget();
        initData();
    }
    public void init(){
        meetRoom = (MeetRoom) this.getIntent().getSerializableExtra("meetRoom");
    }
    @OnClick({R.id.toobar_back, R.id.meet_create})
    public void OnClick(View v){
        switch (v.getId()){
            case R.id.toobar_back:
                finish();
                break;
            case R.id.meet_create:
                Intent in=new Intent(this,CreateOridinryMeetingActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("meetRoom", meetRoom);
                bundle.putSerializable("meetingType",
                        getIntent().getStringExtra("meetingType"));
                in.putExtras(bundle);
                startActivity(in);
                break;
        }
    }
    private void initWidget() {

        dayArrangement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDoneDatePicker(dayArrangement);
            }
        });
    }
    private void initData() {
        currentDate = DateUtils.getTodayDate();
//        dayArrangement.setText(currentDate);
        LoadData(currentDate);
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
                LogUtils.e("DateUtils.getTodayDate()->"+ DateUtils.getTodayDate());
                if (DateUtils.judeDateOrderByDay(DateUtils.getTodayDate().replace("/","-"),currentDate)) {
//                    tv.setText(currentDate);
                    LoadData(currentDate);

                }else{
                    showToast("不能选择过往的日期");
                }
            }
        });
        picker.show();
    }
    private void LoadData(String date) {
        showLoadingView();
        HttpParams params = new HttpParams();
        params.put("uid", AppConfig.getAppConfig(this).getPrivateUid());
        params.put("token", AppConfig.getAppConfig(this).getPrivateToken());
        params.put("roomid",meetRoom.getRoom_id());
        params.put("date", date);
        LogUtils.e("--------uid"+ AppConfig.getAppConfig(this).getPrivateUid());
        LogUtils.e("--------token"+ AppConfig.getAppConfig(this).getPrivateToken());
        LogUtils.e("--------日期"+date);
        LogUtils.e("--------roomId"+meetRoom.getRoom_id());
        initKjHttp().post(Api.CONFERENCE_CONFERENCEINFO, params, new HttpCallBack() {
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
                            JSONArray jDepartment = jo.getJSONArray("data");
                            listMeeting=new ArrayList<>();
                            for (int i = 0; i < jDepartment.length(); i++) {
                                JSONObject jsonObject = jDepartment.getJSONObject(i);
                                MeetDatils meetDatils=new MeetDatils(
                                        jsonObject.getString("username"),
                                        jsonObject.getString("theme"),
                                        jsonObject.getString("begintime"),
                                        jsonObject.getString("endtime")
                                  );
                                LogUtils.e("返回的地址信息：----"+jsonObject.getString("username"));
                                listMeeting.add(meetDatils);
                                LogUtils.e("----"+listMeeting.toString());
                                mRecyclerViewList.setVisibility(View.VISIBLE);
                                noMeeting.setVisibility(View.GONE);
                            }
                            viewTheMeetingScheduleAdapter = new
                                    ViewTheMeetingScheduleAdapter(listMeeting);
                            mRecyclerViewList.setLayoutManager(new LinearLayoutManager(ViewTheMeetingScheduleActivity.this));
                            mRecyclerViewList.setAdapter(viewTheMeetingScheduleAdapter);

                            break;
                        case Api.RESPONSES_CODE_DATA_EMPTY:
                            mRecyclerViewList.setVisibility(View.GONE);
                            noMeeting.setVisibility(View.VISIBLE);
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


}
