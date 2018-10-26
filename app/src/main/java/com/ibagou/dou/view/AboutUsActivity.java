package com.ibagou.dou.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import io.reactivex.functions.Consumer;
import com.ibagou.dou.R;
import com.ibagou.dou.databinding.ActivityAboutUsBinding;
import com.ibagou.dou.util.RxUtils;

/**
 * Created by liumingyu on 2018/8/22.
 */

public class AboutUsActivity extends BaseActivity<ActivityAboutUsBinding> {
    private ActivityAboutUsBinding mBinding;

    @Override
    public ActivityAboutUsBinding onCreateDataBindingView(Bundle savedInstanceState) {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_about_us);
        return mBinding;
    }

    @Override
    public void initView() {
        initClicks();
    }

    private void initClicks() {
        RxUtils.setOnClick(mBinding.returnBtn, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                finish();
            }
        });
    }
}
