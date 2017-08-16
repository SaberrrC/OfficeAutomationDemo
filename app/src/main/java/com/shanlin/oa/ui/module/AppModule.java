package com.shanlin.oa.ui.module;

import android.content.Context;

import com.shanlin.oa.manager.AppManager;

import dagger.Module;
import dagger.Provides;

/**
 * Created by lvdinghao on 16/08/2017.
 */
@Module
public class AppModule {
    final AppManager mApp;

    public AppModule(AppManager mApp) {
        this.mApp = mApp;
    }

    @Provides
    public Context providesContext(){
        return mApp;
    }


}
