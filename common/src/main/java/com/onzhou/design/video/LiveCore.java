package com.onzhou.design.video;

import android.app.Application;

/**
 * @anchor: andy
 * @date: 2018-10-24
 * @description:
 */
public class LiveCore {

    private static LiveCore sInstance;

    private Application application;

    private boolean enableDebug;

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

    public Application getContext() {
        return application;
    }

    public boolean isEnableDebug() {
        return enableDebug;
    }

    /**
     * 初始化操作
     *
     * @param application
     */
    public void init(Application application) {
        //扫描插件映射缓存起来
        init(application, false);
    }

    /**
     * 初始化操作
     *
     * @param application
     */
    public void init(Application application, boolean enableDebug) {
        //扫描插件映射缓存起来
        PluginManager.initPlugins(application, IPlugin.PLUGIN_PACKAGE);
        this.application = application;
        this.enableDebug = enableDebug;
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
