package com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.presenter;

import com.shanlinjinrong.oa.common.Api;
import com.shanlinjinrong.oa.net.MyKjHttp;
import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.bean.ReservationRecordBean;
import com.shanlinjinrong.oa.ui.activity.home.schedule.meetingdetails.concract.MeetingReservationRecordActivityContract;
import com.shanlinjinrong.oa.ui.base.HttpPresenter;

import org.json.JSONArray;
import org.json.JSONObject;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

import java.util.List;

import javax.inject.Inject;

/**
 * 新会议室记录详情
 */

public class MeetingReservationRecordActivityPresenter extends HttpPresenter<MeetingReservationRecordActivityContract.View> implements MeetingReservationRecordActivityContract.Presenter {

    @Inject
    public MeetingReservationRecordActivityPresenter(MyKjHttp mKjHttp) {
        super(mKjHttp);
    }

    private ReservationRecordBean.DataBean dataBean;

    @Override
    public void getMeetingRecord(HttpParams httpParams, int page, int num, final boolean isLoadMore, final List<ReservationRecordBean.DataBean> data) {
        mKjHttp.phpJsonGet(Api.NEW_MEETING_RECORD + "?page=" + page + "&num=" + num, httpParams, new HttpCallBack() {

            @Override
            public void onPreStart() {
                super.onPreStart();
            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                try {
                    ReservationRecordBean reservationRecordBean = new ReservationRecordBean();
                    JSONObject jsonObject = new JSONObject(t);
                    reservationRecordBean.setCode(jsonObject.getInt("code"));
                    switch (reservationRecordBean.getCode()) {
                        case Api.RESPONSES_CODE_OK:
                            if (!isLoadMore) {
                                data.clear();
                            }
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                dataBean = new ReservationRecordBean.DataBean();
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                dataBean.setContent(jsonObject1.getString("content"));
                                dataBean.setTitle(jsonObject1.getString("title"));
                                dataBean.setEnd_time(jsonObject1.getString("end_time"));
                                dataBean.setMeeting_place(jsonObject1.getString("meeting_place"));
                                dataBean.setRoom_id(jsonObject1.getInt("room_id"));
                                dataBean.setId(jsonObject1.getInt("id"));
                                dataBean.setTitle(jsonObject1.getString("title"));
                                dataBean.setRoomname(jsonObject1.optString("roomname"));
                                dataBean.setStart_time(jsonObject1.getString("start_time") + "");
                                if (i == jsonArray.length() - 1) {
                                    dataBean.setItemType(0);
                                } else {
                                    dataBean.setItemType(1);
                                }
                                data.add(dataBean);
                            }
                            if (mView != null)
                                mView.getMeetingRecordSuccess(data);
                            break;
                        case Api.RESPONSES_CODE_NO_CONTENT:
                            if (data.size() == 0) {
                                if (mView != null)
                                    mView.getMeetingRecordEmpty();
                            } else {
                                if (mView != null)
                                    mView.removeFooterView();
                            }
                            break;
                        case Api.RESPONSES_CODE_TOKEN_NO_MATCH:
                        case Api.RESPONSES_CODE_UID_NULL:
                            if (mView != null)
                                mView.uidNull(jsonObject.getInt("code"));
                            break;
                        default:
                            if (mView != null)
                                mView.getMeetingRecordFailed(jsonObject.getInt("code"), jsonObject.getString("info"));
                            break;
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                    if (mView != null)
                        mView.getMeetingRecordFailed(-2, e.getMessage());
                }
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                try {
                    if (mView != null)
                        mView.getMeetingRecordFailed(errorNo, strMsg);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

        });
    }
}
