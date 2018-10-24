package com.onzhou.design;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.onzhou.design.common.AbsBaseActivity;
import com.onzhou.design.ui.VideoPlayActivity;

public class MainActivity extends AbsBaseActivity {

    @Override
    protected int bindContentView() {
        return R.layout.activity_main;
    }

    public void onVideoClick(View view) {
        //可以使用ARouter来完成调整,这里只作测试
        VideoPlayActivity.intentStart(this);
    }
}
