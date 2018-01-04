package com.shanlinjinrong.oa.ui.activity.notice.presenter;

import com.shanlinjinrong.oa.common.ApiJava;
import com.shanlinjinrong.oa.net.MyKjHttp;
import com.shanlinjinrong.oa.ui.activity.notice.contract.NoticesSingleInfoContract;
import com.shanlinjinrong.oa.ui.base.HttpPresenter;
import com.shanlinjinrong.oa.utils.LogUtils;

import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

import javax.inject.Inject;

/**
 * Created by 丁 on 2017/8/19.
 * 登录页面presenter
 */
public class NoticesSingleInfoPresenter extends HttpPresenter<NoticesSingleInfoContract.View> implements NoticesSingleInfoContract.Presenter {

    @Inject
    public NoticesSingleInfoPresenter(MyKjHttp mKjHttp) {
        super(mKjHttp);
    }


    @Override
    public void markReadNotice(String Nid, String departmentId) {
        HttpParams params = new HttpParams();
        params.put("department_id", departmentId);
        params.put("nid", Nid);
        mKjHttp.post(ApiJava.NOTICE_HAD_READ, params, new HttpCallBack() {
            @Override
            public void onSuccess(String t) {
                LogUtils.e("标记已读" + t);
                super.onSuccess(t);
                mView.markSuccess();
            }
        });
    }
}
