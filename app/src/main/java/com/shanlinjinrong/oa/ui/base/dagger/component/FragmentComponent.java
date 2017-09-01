package com.shanlinjinrong.oa.ui.base.dagger.component;

import com.shanlinjinrong.oa.ui.base.dagger.annotation.PerFragment;
import com.shanlinjinrong.oa.ui.base.dagger.module.FragmentModule;
import com.shanlinjinrong.oa.ui.fragment.TabContactsFragment;

import dagger.Component;

/**
 * Created by lvdinghao on 16/8/7.
 */
@PerFragment
@Component(dependencies = AppComponent.class, modules = FragmentModule.class)
public interface FragmentComponent {

    void inject(TabContactsFragment contactsFragment);
}
