package com.onzhou.design.core;

import android.content.Context;

import java.util.Map;

/**
 * @anchor: andy
 * @date: 2017-08-22
 * @description:
 */
public interface IPlugin {

    /**
     * 初始化插件
     *
     * @param applicationContext
     */
    void initPlugin(Context applicationContext);

    /**
     * 获取该插件模块的
     * 所有映射
     *
     * @return
     */
    Map<Class<?>, Class<?>> loadPluginMapping();

}
