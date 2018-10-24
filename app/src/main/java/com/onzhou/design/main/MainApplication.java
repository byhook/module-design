package com.onzhou.design.main;

import android.app.Application;

import com.onzhou.design.core.LiveCore;

/**
 * @anchor: andy
 * @date: 2018-10-24
 * @description:
 */
public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //注意多进程的情况
        LiveCore.getInstance().init(this);
    }
}
