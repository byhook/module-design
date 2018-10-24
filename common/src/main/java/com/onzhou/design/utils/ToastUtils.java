package com.onzhou.design.utils;

import android.content.Context;
import android.widget.Toast;

import com.onzhou.design.video.LiveCore;

/**
 * 作者: andy
 * 时间: 2016-12-20
 * 描述:
 * 吐司工具类
 */

public class ToastUtils {

    private static Context getAppContext() {
        return LiveCore.getInstance().getContext();
    }

    private static boolean isEnableDebug() {
        return LiveCore.getInstance().isEnableDebug();
    }

    /**
     * 调试模式下可显示
     *
     * @param msg
     */
    public static void showDebug(String msg) {
        if (isEnableDebug()) {
            Toast.makeText(getAppContext(), msg, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 短暂显示
     *
     * @param msg
     */
    public static void showShort(CharSequence msg) {
        Toast.makeText(getAppContext(), msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 长时间显示
     *
     * @param msg
     */
    public static void showLong(CharSequence msg) {
        Toast.makeText(getAppContext(), msg, Toast.LENGTH_LONG).show();
    }

}
