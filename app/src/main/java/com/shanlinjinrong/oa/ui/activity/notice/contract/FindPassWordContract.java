package com.shanlinjinrong.oa.ui.activity.notice.contract;

import com.shanlinjinrong.oa.ui.base.BasePresenter;
import com.shanlinjinrong.oa.ui.base.BaseView;

/**
 * Created by 丁 on 2017/8/19.
 * 找回密码接口
 */
public class FindPassWordContract {
    public interface View extends BaseView {
        void findSuccess(); // 成功

        void findFinish(); //请求结束

        void findFailed(); //失败
    }

    public interface Presenter extends BasePresenter<View> {
        void findPassword(String email); //邮箱找回密码
    }
}
