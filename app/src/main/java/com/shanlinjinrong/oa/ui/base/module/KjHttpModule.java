package com.shanlinjinrong.oa.ui.base.module;

import com.shanlinjinrong.oa.net.MyKjHttp;

import org.kymjs.kjframe.http.HttpConfig;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by 丁 on 2017/8/18.
 */
@Module
public class KjHttpModule {

    public KjHttpModule() {
    }

    @Singleton
    @Provides
    public MyKjHttp provideMyKjHttp() {
        MyKjHttp kjHttp = new MyKjHttp();
        HttpConfig config = new HttpConfig();
        HttpConfig.TIMEOUT = 10000;
        kjHttp.setConfig(config);
        return kjHttp;
    }
}
