package com.onzhou.design.plugin;

import android.content.Context;

import com.onzhou.design.video.IPlugin;
import com.onzhou.design.video.IVideoPlay;
import com.onzhou.design.videoimpl.MediaVideoPlayImpl;

import java.util.HashMap;
import java.util.Map;

/**
 * @anchor: andy
 * @date: 2018-10-24
 * @description: 会被自动扫描加载
 */
public class VideoPlugin implements IPlugin {

    @Override
    public void initPlugin(Context applicationContext) {

    }

    @Override
    public Map<Class<?>, Class<?>> loadPluginMapping() {
        Map<Class<?>, Class<?>> map = new HashMap<>();
        map.put(IVideoPlay.class, MediaVideoPlayImpl.class);
        return map;
    }
}
