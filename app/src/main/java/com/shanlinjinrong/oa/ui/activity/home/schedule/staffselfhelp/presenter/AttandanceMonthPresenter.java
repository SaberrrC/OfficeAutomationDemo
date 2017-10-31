package com.shanlinjinrong.oa.ui.activity.home.schedule.staffselfhelp.presenter;

import com.shanlinjinrong.oa.common.Api;
import com.shanlinjinrong.oa.net.MyKjHttp;
import com.shanlinjinrong.oa.ui.activity.home.schedule.contract.CreateNoteContract;
import com.shanlinjinrong.oa.ui.activity.home.schedule.staffselfhelp.contract.AttandanceMonthContract;
import com.shanlinjinrong.oa.ui.activity.home.schedule.staffselfhelp.contract.MyAttendenceActivityContract;
import com.shanlinjinrong.oa.ui.base.HttpPresenter;
import com.shanlinjinrong.oa.utils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

import javax.inject.Inject;

/**
 * Created by Administrator on 2017/10/31 0031.
 */

public class AttandanceMonthPresenter extends HttpPresenter<AttandanceMonthContract.View> implements AttandanceMonthContract.Presenter {

    @Inject
    public AttandanceMonthPresenter(MyKjHttp mKjHttp) {
        super(mKjHttp);
    }

    @Override
    public void searchDayRecorder(String data, String id) {
        HttpParams params = new HttpParams();
        params.put("date", "2017-10-17");
        params.put("userId","id");


        mKjHttp.post("http://apimock.shanlinjinrong.online/mockjs/115/queryTimeTagForOnDay", params, new HttpCallBack() {
            @Override
            public void onPreStart() {
                super.onPreStart();
            }

            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);

                LogUtils.e("onSuccess->" + t);
//                try {
//                    JSONObject jo = new JSONObject(t);
//                    switch (Api.getCode(jo)) {
//                        case Api.RESPONSES_CODE_OK:
//                            mView.loadDataSuccess(Api.getDataToJSONObject(jo));
//                            break;
//                        case Api.RESPONSES_CODE_TOKEN_NO_MATCH:
//                        case Api.RESPONSES_CODE_UID_NULL:
//                            mView.uidNull(Api.getCode(jo));
//                            break;
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
//                mView.loadDataFinish();
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
//                mView.loadDataFailed(errorNo, strMsg);
            }
        });
    }

    @Override
    public void sendData(String str) {

    }


}
