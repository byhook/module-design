package com.onzhou.design.test;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.onzhou.design.common.AbsBaseActivity;
import com.onzhou.design.video.LiveCore;
import com.onzhou.design.videoimpl.R;
import com.onzhou.design.ui.VideoPlayActivity;

/**
 * @anchor: andy
 * @date: 2018-10-24
 * @description:
 */
public class TestVideoActivity extends AbsBaseActivity {

    @Override
    protected int bindContentView() {
        return R.layout.activity_test_video_play;
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        //测试写法
        LiveCore.getInstance().init(getApplication());
    }

    public void onVideoPlayClick(View view) {
        VideoPlayActivity.intentStart(this);
    }

}
