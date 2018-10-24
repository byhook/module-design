package com.onzhou.design.video;

/**
 * 作者: Andy
 * 时间: 2017-10-31
 * 描述:
 */

public interface VideoPlayCallback {

    /**
     * 第一帧加载成功
     */
    void onFirstFrameStart();

    /**
     * 加载结束
     */
    void onLoadEnd();

    /**
     * 视频播放错误
     */
    void onVideoPlayError(int error, String message);

    /**
     * 视频播放完成
     */
    void onVideoPlayComplete();

}
