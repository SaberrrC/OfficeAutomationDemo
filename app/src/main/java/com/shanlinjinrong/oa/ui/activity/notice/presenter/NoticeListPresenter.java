package com.shanlinjinrong.oa.ui.activity.notice.presenter;

import com.shanlinjinrong.oa.common.Api;
import com.shanlinjinrong.oa.model.Notice;
import com.shanlinjinrong.oa.net.MyKjHttp;
import com.shanlinjinrong.oa.ui.activity.notice.contract.NoticeListContract;
import com.shanlinjinrong.oa.ui.base.HttpPresenter;
import com.shanlinjinrong.oa.utils.LogUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by 丁 on 2017/8/19.
 * 消息通知列表presenter
 */
public class NoticeListPresenter extends HttpPresenter<NoticeListContract.View> implements NoticeListContract.Presenter {

    @Inject
    public NoticeListPresenter(MyKjHttp mKjHttp) {
        super(mKjHttp);
    }


    @Override
    public void loadData(String departmentId, int limit, int currentPage, final boolean loadMore) {
        HttpParams params = new HttpParams();
        params.put("limit", limit);
        params.put("page", currentPage);
        params.put("department_id", departmentId);
        mKjHttp.post(Api.MESSAGE_NOTICE, params, new HttpCallBack() {
            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                LogUtils.e(t);
                try {
                    JSONObject jo = new JSONObject(t);
                    switch (Api.getCode(jo)) {
                        case Api.RESPONSES_CODE_OK:
                            List<Notice> listNotices = new ArrayList<>();
                            JSONArray notices = Api.getDataToJSONArray(jo);
                            for (int i = 0; i < notices.length(); i++) {
                                JSONObject jsonObject = notices.getJSONObject(i);
                                Notice notice =
                                        new Notice(jsonObject);
                                listNotices.add(notice);
                            }
                            mView.loadDataSuccess(listNotices, loadMore);
                            break;
                        case Api.RESPONSES_CODE_DATA_EMPTY:
                            mView.loadDataEmpty();
                            break;

                        case Api.LIMIT_CONTENT_EMPTY:
                            mView.loadDataNoMore();
                            break;
                        case Api.RESPONSES_CODE_TOKEN_NO_MATCH:
                        case Api.RESPONSES_CODE_UID_NULL:
//                            mView.uidNull(Api.getCode(jo));
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                mView.loadDataFinish();
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                mView.loadDataFailed(errorNo, strMsg);
            }
        });
    }
}
