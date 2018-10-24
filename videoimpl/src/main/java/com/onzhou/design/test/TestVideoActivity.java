package com.onzhou.design.test;

import android.view.View;

import com.onzhou.design.common.AbsBaseActivity;
import com.onzhou.design.videoimpl.R;
import com.onzhou.design.videoplay.VideoPlayActivity;

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

    public void onVideoPlayClick(View view) {
        VideoPlayActivity.intentStart(this);
    }

}
