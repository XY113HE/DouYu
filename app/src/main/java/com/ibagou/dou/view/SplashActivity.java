package com.ibagou.dou.view;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;

import com.ibagou.dou.R;
import com.ibagou.dou.databinding.ActivitySplashBinding;

/**
 * Created by liumingyu on 2018/10/16.
 */

public class SplashActivity extends BaseActivity<ActivitySplashBinding> {
    private ActivitySplashBinding mBinding;

    @Override
    public ActivitySplashBinding onCreateDataBindingView(Bundle savedInstanceState) {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return mBinding;
        }
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
        return mBinding;
    }

    @Override
    public void initView() {

    }
}
