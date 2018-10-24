package com.onzhou.design.video;

import android.content.Context;
import android.view.SurfaceView;
import android.view.View;

/**
 * 作者: andy
 * 时间: 2017-11-01
 * 描述:
 * 短视频的播放接口
 */

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
