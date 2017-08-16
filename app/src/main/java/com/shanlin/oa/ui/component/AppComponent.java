package com.shanlin.oa.ui.component;

import android.content.Context;

import com.shanlin.oa.net.Api;
import com.shanlin.oa.ui.module.AppModule;
import com.shanlin.oa.ui.module.RetrofitModule;
import javax.inject.Singleton;
import dagger.Component;

/**
 * Created by lvdinghao on 16/08/2017.
 */
@Singleton
@Component(modules = {AppModule.class, RetrofitModule.class})
public interface AppComponent {
    Context getContext();
    Api getApi();
}
