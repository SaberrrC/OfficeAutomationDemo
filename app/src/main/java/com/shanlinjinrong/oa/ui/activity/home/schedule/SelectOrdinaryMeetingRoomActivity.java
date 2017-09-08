package com.shanlinjinrong.oa.ui.activity.home.schedule;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.shanlinjinrong.oa.R;
import com.shanlinjinrong.oa.common.Api;
import com.shanlinjinrong.oa.manager.AppConfig;
import com.shanlinjinrong.oa.model.MeetRoom;
import com.shanlinjinrong.oa.ui.activity.home.schedule.adapter.SelectOrdinaryMeetingRoomAdapter;
import com.shanlinjinrong.oa.ui.base.BaseActivity;
import com.shanlinjinrong.oa.utils.LogUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SelectOrdinaryMeetingRoomActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.toobar_back)
    ImageView toobar_back;
    @Bind(R.id.select_meet_room_list)
    RecyclerView mRecyclerViewList;
    private SelectOrdinaryMeetingRoomAdapter selectOrdinaryMeetingRoomAdapter;
    List<MeetRoom> listMeeting = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_ordinary_meeting_room);
        ButterKnife.bind(this);
        initWidget();
        loadData();
    }

    private void initWidget() {
        mRecyclerViewList.addOnItemTouchListener(new ItemClick());
    }
    @SuppressLint("SetTextI18n")
    private void showContactsInfo(final MeetRoom meetRoom) {
        Intent intent=new Intent(SelectOrdinaryMeetingRoomActivity.this,ViewTheMeetingScheduleActivity.class);
        Bundle bundle=new Bundle();
        bundle.putSerializable("meetRoom",meetRoom);
        intent.putExtra("meetingType", getIntent().getStringExtra("meetingType"));
        intent.putExtras(bundle);

        startActivity(intent);

    }
    class ItemClick extends OnItemClickListener {
        @Override
        public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                    showContactsInfo(listMeeting.get(i));
        }
    }
    /**
     * 加载会议室数据
     *
     * @param
     */
    private void loadData() {
        showLoadingView();
        HttpParams params = new HttpParams();
        params.put("uid", AppConfig.getAppConfig(this).getPrivateUid());
        params.put("token", AppConfig.getAppConfig(this).getPrivateToken());
        initKjHttp().post(Api.CONFERENCE_SELECTMEETINGROOMNEW, params, new HttpCallBack() {
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
                                MeetRoom meetRoom=new MeetRoom(
                                        jsonObject.getString("room_id"),
                                        jsonObject.getString("roomname"),
                                        jsonObject.getString("address"),
                                        jsonObject.getString("floor"),
                                        jsonObject.getString("device"),
                                        jsonObject.getString("nop"),
                                        jsonObject.getString("begintime"));
                                LogUtils.e("返回的地址信息：----"+jsonObject.getString("address"));
                                listMeeting.add(meetRoom);
                                LogUtils.e("----"+listMeeting.toString());
                            }
                            selectOrdinaryMeetingRoomAdapter = new
                                    SelectOrdinaryMeetingRoomAdapter(listMeeting);
                            mRecyclerViewList.setLayoutManager(new LinearLayoutManager(SelectOrdinaryMeetingRoomActivity.this));
                            mRecyclerViewList.setAdapter(selectOrdinaryMeetingRoomAdapter);


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
                hideLoadingView();
                String info = "";
                switch (errorNo) {
                    case Api.RESPONSES_CODE_NO_NETWORK:
                        info = "请确认是否已连接网络！";
                        break;
                    case Api.RESPONSES_CODE_NO_RESPONSE:
                        info = "网络不稳定，请重试！";
                        break;
                }
                showEmptyView(null, info, 0, false);
                super.onFailure(errorNo, strMsg);
            }
        });
    }

    @OnClick(R.id.toobar_back)
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toobar_back:
                finish();
                break;
        }
    }
}
