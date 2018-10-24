package com.onzhou.design.share;

import android.view.View;

import com.onzhou.design.common.AbsBaseActivity;
import com.onzhou.design.utils.ToastUtils;

/**
 * @anchor: andy
 * @date: 2018-10-24
 * @description:
 */
public class ShareActivity extends AbsBaseActivity {

    @Override
    protected int bindContentView() {
        return R.layout.activity_share;
    }

    public void onShareClick(View view) {
        ToastUtils.showShort("点我分享!");
    }

}
