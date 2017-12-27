package com.shanlinjinrong.oa.ui.activity.my.contract;

import com.hyphenate.easeui.UserDetailsBean;
import com.shanlinjinrong.oa.common.Api;
import com.shanlinjinrong.oa.ui.base.BasePresenter;
import com.shanlinjinrong.oa.ui.base.BaseView;

import java.io.File;

/**
 * Created by 丁 on 2017/8/19.
 * userinfo相关接口
 */
public interface UserInfoActivityContract {
    interface View extends BaseView {

        void upLoadSuccess(String portrait); // 上传成功

        void upLoadFailed(int errorCode, String msg); // 上传失败

        void upLoadFinish(); //上传请求结束

        void queryUserInfoSuccess(UserDetailsBean userDetailsBean);

        void queryUserInfoFailed();
    }

    interface Presenter extends BasePresenter<View> {
        void upLoadPortrait(File portrait); //修改密码

        void queryUserInfo(); //刷新个人信息
    }
}
