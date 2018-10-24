package com.onzhou.design.videoplay;

import android.content.Context;
import android.content.Intent;

import com.onzhou.design.common.AbsBaseActivity;

/**
 * @anchor: andy
 * @date: 2018-10-24
 * @description:
 */
public class VideoPlayActivity extends AbsBaseActivity {

    public static void intentStart(Context context) {
        Intent intent = new Intent(context, VideoPlayActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int bindContentView() {
        return R.layout.activity_video_play;
    }

}
