package com.shanlin.oa.ui.base.component;

import android.app.Activity;

import com.shanlin.oa.ui.annotation.PerFragment;
import com.shanlin.oa.ui.base.module.FragmentModule;
import dagger.Component;

/**
 * Created by lvdinghao on 16/8/7.
 */
@PerFragment
@Component(dependencies = AppComponent.class, modules = FragmentModule.class)
public interface FragmentComponent {
    Activity getActivity();
}
