package com.shanlinjinrong.oa.ui.activity.my.contract;

import com.shanlinjinrong.oa.ui.base.BasePresenter;
import com.shanlinjinrong.oa.ui.base.BaseView;

/**
 * Created by 丁 on 2017/8/19.
 * 修改密码相关接口
 */
public interface ModifyPswActivityContract {
    interface View extends BaseView {

        void modifySuccess(); // 修改成功

        void modifyFailed( String msg); // 修改失败

        void modifyFinish(); //修改请求结束
    }

    interface Presenter extends BasePresenter<View> {
        void modifyPsw(String oldPsw, String newPsw); //修改密码
    }
}
