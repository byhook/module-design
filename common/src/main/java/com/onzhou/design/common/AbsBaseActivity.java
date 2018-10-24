package com.onzhou.design.common;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * @anchor: andy
 * @date: 2018-10-24
 * @description:
 */
public abstract class AbsBaseActivity extends AppCompatActivity {

    protected Context context;

    @LayoutRes
    protected int bindTitleView() {
        return View.NO_ID;
    }

    @LayoutRes
    protected abstract int bindContentView();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = this;
        setupViews();
    }

    private void setupViews() {
        int titleViewRes = bindTitleView();
        if (titleViewRes == View.NO_ID) {
            //没有自定义标题栏
            setContentView(bindContentView());
        } else {

        }
    }

}
