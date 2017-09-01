package com.shanlinjinrong.oa.ui.base.dagger.component;

import android.content.Context;

import com.shanlinjinrong.oa.net.MyKjHttp;
import com.shanlinjinrong.oa.ui.base.dagger.module.AppManagerModule;
import com.shanlinjinrong.oa.ui.base.dagger.module.KjHttpModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by 丁 on 2017/8/18.
 */
@Singleton
@Component(modules = {AppManagerModule.class, KjHttpModule.class})
public interface AppComponent {
    Context getContext();

    MyKjHttp getMyKjHttp();
}
