package com.onzhou.design.videoimpl;

import android.content.Context;
import android.media.MediaPlayer;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.onzhou.design.video.IVideoPlay;
import com.onzhou.design.video.VideoPlayCallback;

import java.io.IOException;

/**
 * @anchor: andy
 * @date: 2018-10-24
 * @description:
 */
public class MediaVideoPlayImpl implements IVideoPlay {

    private static final String TAG = "VideoPlay";

    private Context mContext;

    private MediaPlayer mMediaPlayer;

    private SurfaceView mSurfaceView;

    private VideoPlayCallback mVideoPlayCallback;

    @Override
    public View bindVideoView() {
        mSurfaceView = new SurfaceView(mContext);
        mSurfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (mMediaPlayer != null) {
                    mMediaPlayer.setDisplay(holder);
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });
        return mSurfaceView;
    }

    @Override
    public void initPlayer(Context context) {
        this.mContext = context;
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.setDisplay(null);
            mMediaPlayer.release();
        }
        mMediaPlayer = new MediaPlayer();
        //初始化回调监听
        initMediaListener();
    }

    private void initMediaListener() {
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mMediaPlayer.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
                if (!mMediaPlayer.isPlaying()) {
                    mMediaPlayer.start();
                }
            }
        });
        mMediaPlayer.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                return false;
            }
        });
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

            }

        });
        mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                return false;
            }
        });
    }

    @Override
    public void setRemoteSource(String url) {
        if (!TextUtils.isEmpty(url) && mMediaPlayer != null) {
            try {
                mMediaPlayer.setDataSource(url);
                mMediaPlayer.setLooping(true);
                mMediaPlayer.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void reset() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void setRemoteSource(String vid, String auth) {

    }

    @Override
    public void setVideoPlayCallback(VideoPlayCallback videoPlayCallback) {
        this.mVideoPlayCallback = videoPlayCallback;
    }

    @Override
    public int getVideoWidth() {
        return mMediaPlayer.getVideoWidth();
    }

    @Override
    public int getVideoHeight() {
        return mMediaPlayer.getVideoHeight();
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onDestroy() {
        setVideoPlayCallback(null);
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }
}
