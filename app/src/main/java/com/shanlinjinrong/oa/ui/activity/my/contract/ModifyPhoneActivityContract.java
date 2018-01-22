package com.shanlinjinrong.oa.ui.activity.my.contract;

import com.shanlinjinrong.oa.ui.base.BasePresenter;
import com.shanlinjinrong.oa.ui.base.BaseView;

/**
 * Created by 丁 on 2017/8/19.
 * 修改手机号相关接口
 */
public interface ModifyPhoneActivityContract {
    interface View extends BaseView {
        void modifySuccess(String newNumber); // 修改成功

        void modifyFailed(int errorCode, String msg); // 修改失败


        void modifyFinish(); //修改请求结束
    }

    interface Presenter extends BasePresenter<View> {
        void modifyPhone(String phoneNum); //修改手机号
    }
}
