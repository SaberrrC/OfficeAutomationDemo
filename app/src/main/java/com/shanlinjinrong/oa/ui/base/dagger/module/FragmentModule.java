package com.shanlinjinrong.oa.ui.base.dagger.module;


import android.support.v4.app.Fragment;

import com.shanlinjinrong.oa.ui.base.dagger.annotation.PerFragment;

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
