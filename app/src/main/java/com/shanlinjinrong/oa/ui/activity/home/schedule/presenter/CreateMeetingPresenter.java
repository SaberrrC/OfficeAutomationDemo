package com.shanlinjinrong.oa.ui.activity.home.schedule.presenter;

import com.shanlinjinrong.oa.common.Api;
import com.shanlinjinrong.oa.net.MyKjHttp;
import com.shanlinjinrong.oa.ui.activity.home.schedule.contract.CreateMeetingContract;
import com.shanlinjinrong.oa.ui.base.HttpPresenter;
import com.shanlinjinrong.oa.utils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

import javax.inject.Inject;

/**
 * Created by 丁 on 2017/9/4.
 */

public class CreateMeetingPresenter extends HttpPresenter<CreateMeetingContract.View> implements CreateMeetingContract.Presenter {

    @Inject
    public CreateMeetingPresenter(MyKjHttp mKjHttp) {
        super(mKjHttp);
    }

    @Override
    public void sendData(String beginTime, String endTime, String date, String theme, String attentees, String type, String roomId) {
        HttpParams params = new HttpParams();
        params.put("begintime", beginTime);
        params.put("endtime", endTime);
        params.put("date", date);
        params.put("theme", theme);
        params.put("attentees", attentees);
        params.put("type", type);
        params.put("roomid", roomId);

        mKjHttp.post(Api.CONFERENCE_SETCONF, params, new HttpCallBack() {
            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                LogUtils.e("创建会议返回数据：" + t);
                try {
                    JSONObject jo = new JSONObject(t);
                    if (Api.getCode(jo) == Api.RESPONSES_CODE_OK) {
                        mView.sendDataSuccess();
                    } else if ((Api.getCode(jo) == Api.RESPONSES_CODE_UID_NULL)) {
                        mView.uidNull(Api.getCode(jo));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFinish() {
                super.onFinish();
                mView.sendDataFinish();
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                LogUtils.e("errorNo,strMsg--->" + errorNo + "," + strMsg);
                mView.sendDataFailed(errorNo, strMsg);
            }
        });
    }
}
