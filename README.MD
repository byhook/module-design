### 目录
- [组件化]()
- [一个需求引发的思考]()
- [业务与实现分离]()
- [为啥要这么做呢？]()
- [组件之间的通信]()
- [小结]()

### 组件化
最近几天在整理项目中的要点，组件化相信大家都不陌生，还是复用以前的一张项目架构图，可以看到，项目的架构目前看起来比较清晰了，在最下层沉淀的是我们的公共库，比如`网络库`，`图片库`，`工具类`，`......`等等

![](https://img-blog.csdn.net/20181024225139455?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2J5aG9vaw==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

上层的业务，比如`短视频模块`，`分享模块`，`直播间模块`等等，彼此直接并不会相互依赖，但是今天想说的是`解耦`的问题

### 一个需求引发的思考
由于公司另外一个项目组需要使用我们的核心功能，比如`直播间`，`短视频`等业务模块，其他的会砍掉，当然目前笔者已经踩坑过了关于[多组件分包合包的方案](https://blog.csdn.net/byhook/article/details/80753204)了

现在问题来了，另外一个组是`手机电视`类的项目，它们的App内部已经有依赖`ijkplayer`实现的播放器了，但是我们内部使用的是`阿里云播放器`，当然了直接合并使用我们的一整套`短视频`业务模块，也没有问题，但是无形当中会大幅增加`apk包`的体积(`由于两者下层都是基于ffmeng库封装的`)，相当于一个应用内重复包含了几个播放库，那能不能复用同一套呢？换句话说，能否实现我们的项目编译打包`apk`的时候，加载的是`阿里云播放器`的实现类，而给其他项目组`合包成aar`之后，他们加载自己的`ijkplayer`实现类呢？

### 业务与实现分离

以最典型的`短视频`模块为例子，开发阶段，新建两个`module`，分别对应`video`业务模块和`video-impl`播放器实现类模块，让`video-impl`组件只依赖`common`组件和`video`业务组件，然后让`video-impl`以`application`的方式运行，开发。

笔者这里简化了项目模型，但是基本原理是一致的。

![](https://img-blog.csdn.net/20181024225606504?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2J5aG9vaw==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

在我们自己的`video组件`中抽象我们的播放器的一个`IVideoPlay`的接口
```java
public interface IVideoPlay extends ILifeCycle {

    /**
     * 绑定视频显示容器
     */
    View bindVideoView();

    /**
     * 初始化播放器
     */
    void initPlayer(Context context);

    /**
     * 视频源
     *
     * @param url
     */
    void setRemoteSource(String url);

    /**
     * 重置
     */
    void reset();

    /**
     * 停止播放
     */
    void stop();

    /**
     * 远程视频源
     *
     * @param vid
     * @param auth
     */
    void setRemoteSource(String vid, String auth);

    /**
     * 视频播放回调
     */
    void setVideoPlayCallback(VideoPlayCallback videoPlayCallback);

    /**
     * 获取视频宽度
     *
     * @return
     */
    int getVideoWidth();

    /**
     * 获取视频高度
     *
     * @return
     */
    int getVideoHeight();

    /**
     * 唤起
     */
    void onResume();

    /**
     * 挂起
     */
    void onPause();

}
```
然后在依赖它的上层组件`video-impl`中实现该该接口，如`MediaVideoPlayImpl`，笔者这里为了简化，直接使用系统类来实现的，看下图比较直观：

![](https://img-blog.csdn.net/20181024225527382?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2J5aG9vaw==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

但是有个新问题，那就是我们的`video组件`内部`VideoPlayActivity`都是在下层，如何拿到上层的`MediaVideoPlayImpl`的实现类，实例化，然后播放视频呢？如果直接在下层通过`new`操作符，必然会产生`强依赖`，`上层播放器实现类依赖下层接口`，而`下层业务又需要上层的实现类`，这种循环依赖的尴尬局面。

当然了，笔者经过缜密的思考(反编译某厂SDK)后，确定了一种可行的方案：`动态代理`
```java
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
            return null;
        }
    });
}
```
相当于我们自己通过系统提供的`Proxy.newProxyInstance`拿到对应接口的代理实现类，默认都是空实现，然后在自定义的`InvocationHandler`中的`invoke`方法替换成我们目标的实现类，`如果存在则通过反射实例化，执行返回结果`。

如何才能在运行期间拿到对应接口的实现类呢？
- 第一步：我们可以在最下层的`common`组件中，定义一个`IPlugin`接口，内容为
```java
/**
 * @anchor: andy
 * @date: 2017-08-22
 * @description:
 */
public interface IPlugin {

    /**
     * 待扫描的插件包目录
     */
    String PLUGIN_PACKAGE = "com.onzhou.design.plugin";

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
```
- 第二步：在我们目标的`video-impl`组件中新建包名`com.onzhou.design.plugin(这个包名是约定统一好的，后面进行dex扫描会用到)`，然后新建实现类`VideoPlugin`如下：
```
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
```
- 第三步.：应用启动的时候，我们只需要在`Application`中的`onCreate`方法中，扫描((`具体的扫描方法和工具类，大家可以去看ARouter的源码中都有`)当前`dex`文件中指定包名`com.onzhou.design.plugin`下的所有`IPlugin`插件的实现类，然后通过对应的`loadPluginMapping`方法获取到每个`接口对应实现类的映射`缓存在我们应用内，可以通过在应用内部维护一个`单例`缓存起来，`注意：此时仅仅只是扫描出了接口与实现类之间的映射关系，并未实例化对应的实现类`

最后在我们的`video`业务组件中就可以通过
```java
getService(IVideoPlay.class).initPlayer(context);
```
的方式就可以拿到上层的播放器实现类`MediaVideoPlayImpl`，由于依赖的第三方播放器库都在`video-impl`这个组件中，因此它可以很好的和下层的业务组件分离，仅仅只是完成它播放的核心功能。


### 为啥要这么做呢？

对于一般的应用而言，无论你最终分离多少个业务组件，最终都是在最上层合并成一个`apk`文件，因为最上层的`app`组件，全部都会依赖下层的所有组件：
```java
compile project(':common')
compile project(':share')
compile project(':share-impl')
compile project(':video')
compile project(':video-impl')
......
```
那分离的意义和价值又在哪里呢？其实这个问题又回到了我之前说到的一个`业务上的需求`上去了，因为公司的业务特殊，我们给另外一个组的`SDK包`可能只包含我们的部分业务功能，要做到体积尽可能小，而且`不能侵入我们的核心业务`

```java
embedded project(':common')
embedded project(':share')
embedded project(':video')
```
相当于，我们只把我们的`业务组件和接口`合并成一个最终的`aar包`，那么对于其他使用的人来说，他只需要几个步骤即可：
- 第一步：通过maven的方式依赖我们的`SDK包`
- 第二步：用他们自己内部的播放器，比如`ijkplayer`来实现我们的`IVideoPlay`接口
- 第三步：在他们内部`com.onzhou.design.plugin`包下面，实现`IPlugin`接口，定义好`接口和实现类的映射`

这样在他们的应用启动的时候，调用我们的工具类可以扫描到`dex`文件中的`IPlugin`实现类，进而缓存到所有的`接口和实现类的映射`，那么在进入我们`SDK内部的短视频模块`的时候，我们就可以通过动态代理的方式，拿到对应的实现类，实例化之后完成调用。

### 组件之间的通信
组件之间的通信方式很多种，最常见的就是`Activity`之间的挑战，这个我们可以直接使用`ARouter`来完成，避免`组件之间的强依赖`，还可以通过`广播`，`事件总线框架`等等完成通信。

### 小结：
目前这种方案在项目中已经实践一年多了，不仅能保证我们主项目业务的`并行高效开发`，`业务组件与业务组件除了对下层公共库由依赖，彼此之间没有直接依赖`，同时在`提供SDK合包`的时候，对我们的主业务也没有任何`侵入性`，扩展性很强，当然有的人可能认为，反射会影响一定的性能，但是怎么说呢？首先这个反射并不是平凡调用，我们在内部会有缓存实例的机制，第二点，我觉得`在架构方面，性能可以适当的给扩展性让一让步，很多时候我们过分的追求性能，往往会让整个项目进入死胡同`。

大家可以去看看我之前写的一篇博客
[组件化分包合包方案的坑](https://blog.csdn.net/byhook/article/details/80753204)

模拟组件解耦
https://github.com/byhook/module-design
