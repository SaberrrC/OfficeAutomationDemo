package com.shanlin.oa.ui.base.module;


import android.support.v4.app.Fragment;

import com.shanlin.oa.ui.annotation.PerFragment;

import dagger.Module;
import dagger.Provides;

@Module
public class FragmentModule {

    private Fragment fragment;

    public FragmentModule(Fragment fragment) {
        this.fragment = fragment;
    }

    @Provides
    @PerFragment
    public Fragment provideFragment(){
        return fragment;
    }
}
