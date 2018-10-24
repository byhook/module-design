package com.onzhou.design.video;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import com.onzhou.design.utils.ClassUtils;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者: andy
 * 时间: 2017-08-22
 * 描述:
 * 会扫描指定包下的所有插件
 */

public class PluginManager {

    private static final String TAG = "PluginProxy";

    /**
     * 缓存映射
     */
    private static Map<Class<?>, Class<?>> mPluginMappings = new HashMap<>();

    /**
     * 缓存类
     */
    private static final Map<Class<?>, Object> mSingletonCaches = new HashMap();

    /**
     * 初始化注册的插件
     *
     * @param context
     * @param packageName
     */
    public static void initPlugins(Context context, String packageName) {
        long time = System.currentTimeMillis();
        List<String> pluginTables = null;
        try {
            pluginTables = ClassUtils.getFileNameByPackageName(context, packageName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //扫描插件加载
        if (pluginTables != null && !pluginTables.isEmpty()) {
            for (String pluginName : pluginTables) {
                loadPlugin(context, pluginName);
            }
        }
        Log.d(TAG, "time: " + (System.currentTimeMillis() - time));
    }

    /**
     * 加载插件
     *
     * @param context
     * @param pluginName
     */
    private static void loadPlugin(Context context, String pluginName) {
        try {
            Class<IPlugin> temp = (Class<IPlugin>) Class.forName(pluginName);
            IPlugin plugin = temp.newInstance();
            Map<Class<?>, Class<?>> pluginMaps = plugin.loadPluginMapping();
            if (pluginMaps != null) {
                mPluginMappings.putAll(pluginMaps);
            }
            //初始化插件
            plugin.initPlugin(context);
            Log.d(TAG, "load plugin " + plugin);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取管理类
     *
     * @param targetClazz
     * @param <T>
     * @return
     */

    public static <T> T getServiceClass(Class<T> targetClazz) {
        if (!targetClazz.isInterface()) {
            throw new IllegalArgumentException("only accept interface: " + targetClazz);
        }
        return (T) mPluginMappings.get(targetClazz);
    }

    /**
     * 获取管理类
     * 动态代理
     * 获取的实例会缓存,可以看成是单例
     *
     * @param targetClazz
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T getService(final Class<T> targetClazz) {
        if (!targetClazz.isInterface()) {
            throw new IllegalArgumentException("only accept interface: " + targetClazz);
        }
        return (T) Proxy.newProxyInstance(targetClazz.getClassLoader(), new Class<?>[]{targetClazz}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) {
                try {
                    return invokeProxy(targetClazz, proxy, method, args);
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                }
                //Log.e(TAG, "proxy " + targetClazz);
                return null;
            }
        });
    }

    private static Object invokeProxy(final Class<?> targetClazz, Object proxy, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException, InstantiationException {
        Object temp;
        if ((temp = mSingletonCaches.get(targetClazz)) == null) {
            Class<?> clazz = mPluginMappings.get(targetClazz);
            //Log.d(TAG, "target " + clazz + " method: " + method.getName());
            if (clazz != null) {
                temp = clazz.newInstance();
                //Log.d(TAG, "target " + temp + " method: " + method.getName());
                mSingletonCaches.put(targetClazz, temp);
                return method.invoke(temp, args);
            }
            //Log.e(TAG, "proxy " + targetClazz);
            return null;
        } else {
            //Log.d(TAG, "target " + temp + " method: " + method.getName());
            return method.invoke(temp, args);
        }
    }

    /**
     * 移除单例
     *
     * @param targetClazz
     */
    public static void removeService(final Class<?> targetClazz) {
        if (targetClazz != null) {
            mSingletonCaches.remove(targetClazz);
        }
    }

    /**
     * 直接反射实例
     *
     * @param targetClazz
     * @param <T>
     * @return 可能为null
     */
    public static <T> T getNewService(Class<T> targetClazz) {
        if (!targetClazz.isInterface()) {
            throw new IllegalArgumentException("only accept interface: " + targetClazz);
        }
        Object temp = null;
        Class<?> clazz = mPluginMappings.get(targetClazz);
        try {
            temp = clazz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (T) temp;
    }

}
