package com.onzhou.design.core;

import android.app.Application;

/**
 * @anchor: andy
 * @date: 2018-10-24
 * @description:
 */
public class LiveCore {

    private static LiveCore sInstance;

    private Application application;

    /**
     * 获取单例
     *
     * @return
     */
    public static LiveCore getInstance() {
        if (sInstance == null) {
            synchronized (LiveCore.class) {
                if (sInstance == null) {
                    sInstance = new LiveCore();
                }
            }
        }
        return sInstance;
    }

    /**
     * 初始化操作
     *
     * @param application
     */
    public void init(Application application) {
        this.application = application;
    }

    /**
     * 动态获取对应的实现类
     *
     * @param targetClazz
     * @param <T>
     * @return
     */
    public static <T> T getService(final Class<T> targetClazz) {
        return PluginManager.getService(targetClazz);
    }

}
