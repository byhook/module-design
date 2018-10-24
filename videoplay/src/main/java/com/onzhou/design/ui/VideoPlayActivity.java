package com.onzhou.design.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.onzhou.design.common.AbsBaseActivity;
import com.onzhou.design.video.IVideoPlay;
import com.onzhou.design.video.LiveCore;
import com.onzhou.design.video.VideoPlayCallback;
import com.onzhou.design.videoplay.R;

/**
 * @anchor: andy
 * @date: 2018-10-24
 * @description:
 */
public class VideoPlayActivity extends AbsBaseActivity implements VideoPlayCallback {

    private ViewGroup mRootLayer;

    private IVideoPlay mVideoPlay;

    public static void intentStart(Context context) {
        Intent intent = new Intent(context, VideoPlayActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int bindContentView() {
        return R.layout.activity_video_play;
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setupViews();
        setupVideo();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mVideoPlay != null) {
            mVideoPlay.onDestroy();
        }
    }

    private void setupViews() {
        mRootLayer = (ViewGroup) findViewById(R.id.video_play_root_layer);
    }

    private void setupVideo() {
        mVideoPlay = LiveCore.getService(IVideoPlay.class);
        mVideoPlay.initPlayer(this);
        mVideoPlay.setVideoPlayCallback(this);
        //绑定显示控件
        View videoView = mVideoPlay.bindVideoView();
        mRootLayer.addView(videoView, 0);

        String videoPath = "http://player.alicdn.com/video/aliyunmedia.mp4";
        mVideoPlay.setRemoteSource(videoPath);
    }

    @Override
    public void onFirstFrameStart() {

    }

    @Override
    public void onLoadEnd() {

    }

    @Override
    public void onVideoPlayError(int error, String message) {

    }

    @Override
    public void onVideoPlayComplete() {

    }

}
