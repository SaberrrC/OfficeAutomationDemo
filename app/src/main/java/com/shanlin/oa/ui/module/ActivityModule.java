package com.shanlin.oa.ui.module;

import android.app.Activity;
import com.shanlin.oa.ui.annotation.PerActivity;
import dagger.Module;
import dagger.Provides;

/**
 * Created by lvdinghao on 16/08/2017.
 */

@Module
public class ActivityModule {
    private Activity mActivity;

    public ActivityModule(Activity activity) {
        this.mActivity = activity;
    }

    @Provides
    @PerActivity
    public Activity provideActivity() {
        return mActivity;
    }


}
