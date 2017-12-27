package com.shanlinjinrong.oa.ui.activity.home.schedule.presenter;

import com.shanlinjinrong.oa.common.Api;
import com.shanlinjinrong.oa.common.Constants;
import com.shanlinjinrong.oa.net.MyKjHttp;
import com.shanlinjinrong.oa.ui.activity.home.schedule.contract.CreateNoteContract;
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

public class CreateNotePresenter extends HttpPresenter<CreateNoteContract.View> implements CreateNoteContract.Presenter {

    @Inject
    public CreateNotePresenter(MyKjHttp mKjHttp) {
        super(mKjHttp);
    }


    @Override
    public void createNote(String departmentId, String content, String time, String noteId, String mode) {
        HttpParams params = new HttpParams();
        String url;
        if (mode.equals(Constants.EDIT_NOTE)) {
            params.put("nt_id", noteId);
            url = Api.NOTE_EDIT;
        } else {
            url = Api.NOTE_ADD;
        }
        params.put("department_id", departmentId);
        params.put("content", content);
        params.put("time", time);

        mKjHttp.post(url, params, new HttpCallBack() {


            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                LogUtils.e(t);
                JSONObject jo = null;
                try {
                    jo = new JSONObject(t);
                    if (Api.getCode(jo) == Api.RESPONSES_CODE_OK) {
                        mView.createSuccess();
                    } else if ((Api.getCode(jo) == Api.RESPONSES_CODE_UID_NULL)) {
//                        mView.uidNull(Api.getCode(jo));
                    } else if ((Api.getCode(jo) == Api.RESPONSES_CODE_TOKEN_NO_MATCH)) {
//                        mView.uidNull(Api.getCode(jo));
                    } else {
                        mView.createResponseOther(Api.getInfo(jo));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFinish() {
                super.onFinish();
                mView.createFinish();
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                LogUtils.e("errorNo,strMsg--->" + errorNo + "," + strMsg);
                mView.createFailed(errorNo, strMsg);
            }
        });
    }
}
