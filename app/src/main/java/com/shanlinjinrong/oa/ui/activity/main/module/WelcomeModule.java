package com.shanlinjinrong.oa.ui.activity.main.module;

import com.shanlinjinrong.oa.ui.activity.main.contract.WelcomePageContract;
import com.shanlinjinrong.oa.ui.activity.main.presenter.WelcomePagePresenter;
import com.shanlinjinrong.oa.ui.base.dagger.annotation.PerActivity;

import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpConfig;

import dagger.Module;
import dagger.Provides;

/**
 * Created by 丁 on 2017/8/18.
 */
@Module
public class WelcomeModule {
    private WelcomePageContract.View mView;

    public WelcomeModule(WelcomePageContract.View mView) {
        this.mView = mView;
    }

    @PerActivity
    @Provides
    public WelcomePageContract.Presenter providePresenter() {
        KJHttp mHttp = new KJHttp();
        HttpConfig config = new HttpConfig();
        HttpConfig.TIMEOUT = 10000;
        mHttp.setConfig(config);
        return new WelcomePagePresenter(mView, mHttp);
    }
}
