package com.shanlinjinrong.oa.ui.activity.home.schedule.staffselfhelp.presenter;

import com.shanlinjinrong.oa.net.MyKjHttp;
import com.shanlinjinrong.oa.ui.activity.home.schedule.staffselfhelp.contract.MyAttendenceActivityContract;
import com.shanlinjinrong.oa.ui.base.HttpPresenter;

import javax.inject.Inject;

/**
 * Created by Administrator on 2017/10/31 0031.
 */

public class MyAttendenceActivityPresenter extends HttpPresenter<MyAttendenceActivityContract.View> {

    @Inject
    public MyAttendenceActivityPresenter(MyKjHttp mKjHttp) {
        super(mKjHttp);
    }

}
