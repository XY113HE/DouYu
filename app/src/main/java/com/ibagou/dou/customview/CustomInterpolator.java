package com.ibagou.dou.customview;

import android.view.animation.LinearInterpolator;

/**
 * Created by liumingyu on 2018/8/29.
 */

public class CustomInterpolator extends LinearInterpolator {
    @Override
    public float getInterpolation(float input) {
//        return (float) Math.sin(2*Math.PI/360*(input*90));
        return (float) Math.pow(input, 0.5);
    }
}
