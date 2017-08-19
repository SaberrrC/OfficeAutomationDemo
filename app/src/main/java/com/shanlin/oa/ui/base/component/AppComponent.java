package com.shanlin.oa.ui.base.component;

import android.content.Context;

import com.shanlin.oa.net.MyKjHttp;
import com.shanlin.oa.ui.base.module.AppManagerModule;
import com.shanlin.oa.ui.base.module.KjHttpModule;

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
