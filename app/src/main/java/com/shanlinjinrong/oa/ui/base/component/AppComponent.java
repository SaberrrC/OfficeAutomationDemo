package com.shanlinjinrong.oa.ui.base.component;

import android.content.Context;

import com.shanlinjinrong.oa.net.MyKjHttp;
import com.shanlinjinrong.oa.ui.base.module.AppManagerModule;
import com.shanlinjinrong.oa.ui.base.module.KjHttpModule;

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
