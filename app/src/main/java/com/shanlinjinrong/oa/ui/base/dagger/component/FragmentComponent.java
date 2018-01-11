package com.shanlinjinrong.oa.ui.base.dagger.component;

import com.shanlinjinrong.oa.ui.activity.message.Fragment.SelectedGroupContactFragment;
import com.shanlinjinrong.oa.ui.base.dagger.annotation.PerFragment;
import com.shanlinjinrong.oa.ui.base.dagger.module.FragmentModule;
import com.shanlinjinrong.oa.ui.fragment.TabContactsFragment;
import com.shanlinjinrong.oa.ui.fragment.TabMeFragment;

import dagger.Component;

@PerFragment
@Component(dependencies = AppComponent.class, modules = FragmentModule.class)
public interface FragmentComponent {

    void inject(TabContactsFragment contactsFragment);

    void inject(SelectedGroupContactFragment selectedGroupContactFragment);

    void inject(TabMeFragment tabMeFragment);
}
