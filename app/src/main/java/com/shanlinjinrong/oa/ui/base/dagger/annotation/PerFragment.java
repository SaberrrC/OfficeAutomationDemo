package com.shanlinjinrong.oa.ui.base.dagger.annotation;

import java.lang.annotation.Retention;

import javax.inject.Scope;

import static java.lang.annotation.RetentionPolicy.RUNTIME;


@Scope
@Retention(RUNTIME)
public @interface PerFragment {
}
