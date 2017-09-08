package com.shanlinjinrong.oa.ui.activity.home.schedule.contract;

import com.shanlinjinrong.oa.ui.base.BasePresenter;
import com.shanlinjinrong.oa.ui.base.BaseView;

/**
 * Created by 丁 on 2017/9/4.
 */

public interface CreateNoteContract {

    interface View extends BaseView {
        void createSuccess();

        void createFailed(int errCode, String msg);

        void createFinish();

        void createResponseOther(String msg);
    }

    interface Presenter extends BasePresenter<View> {
        void createNote(String departmentId, String content, String time, String noteId, String mode);
    }
}
