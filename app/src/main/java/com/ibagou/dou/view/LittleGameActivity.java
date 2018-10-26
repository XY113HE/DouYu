package com.ibagou.dou.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.animation.AccelerateInterpolator;

import com.ibagou.dou.R;
import com.ibagou.dou.customview.CustomInterpolator;
import com.ibagou.dou.databinding.ActivityLittleGameBinding;
import com.ibagou.dou.util.DisplayUtils;
import com.ibagou.dou.util.RxUtils;
import com.ibagou.dou.util.Tools;

import io.reactivex.functions.Consumer;

/**
 * Created by liumingyu on 2018/8/29.
 */

public class LittleGameActivity extends BaseActivity<ActivityLittleGameBinding>{
    private ActivityLittleGameBinding mBinding;
    private ObjectAnimator animationX;
    private ObjectAnimator animationY;
    private AnimatorSet set;
    private int leftClick = 0;


    @Override
    public ActivityLittleGameBinding onCreateDataBindingView(Bundle savedInstanceState) {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_little_game);





        return mBinding;
    }

    @Override
    public void initView() {
        initClicks();
    }

    private void initClicks() {
        RxUtils.setOnFreeClick(mBinding.start1ClickBtn, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {

                setAnim();
                mBinding.start1.clearAnimation();
                set.start();
            }
        });
    }

    private void setAnim() {
        animationX = ObjectAnimator.ofFloat(mBinding.start1, "translationX", 0, Tools.getScreenWidth() - DisplayUtils.dp2px(150));
        animationX.setDuration(500);
        animationX.setInterpolator(new CustomInterpolator());
        animationY = ObjectAnimator.ofFloat(mBinding.start1, "translationY", 0, Tools.getScreenHeight() - DisplayUtils.dp2px(150) - Tools.getStatusBarHeight() - DisplayUtils.dp2px(leftClick++)*50);
        animationY.setInterpolator(new AccelerateInterpolator());
        animationY.setDuration(500);
        set = new AnimatorSet();
        set.play(animationX).with(animationY);
    }
}
