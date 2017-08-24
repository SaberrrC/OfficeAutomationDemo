package com.shanlinjinrong.oa.ui.base.module;

import android.content.Context;

import com.shanlinjinrong.oa.manager.AppManager;

import dagger.Module;
import dagger.Provides;

/**
 * Created by 丁 on 2017/8/18.
 * AppManagerModule
 */
@Module
public class AppManagerModule {
    AppManager mApp;

    public AppManagerModule(AppManager mApp) {
        this.mApp = mApp;
    }

    @Provides
    public Context provideAppManager() {
        return mApp;
    }
}
