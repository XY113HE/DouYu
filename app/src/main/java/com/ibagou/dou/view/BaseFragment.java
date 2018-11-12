package com.ibagou.dou.view;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ibagou.dou.util.UmengUtils;
import com.trello.rxlifecycle2.components.support.RxFragment;
import com.umeng.analytics.MobclickAgent;

/**
 * BaseFragment
 */
public abstract class BaseFragment<DataBindingType extends ViewDataBinding> extends RxFragment {
    protected DataBindingType mDataBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mDataBinding = onCreateDataBindingView(inflater, container, savedInstanceState);
        initView();
        return mDataBinding.getRoot();
    }

    public abstract DataBindingType onCreateDataBindingView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    public abstract void initView();

    @Override
    public void onResume() {
        super.onResume();
        UmengUtils.onResumeToActivity(getContext());
    }

    @Override
    public void onPause() {
        super.onPause();
        UmengUtils.onPauseToFragment(getContext());
    }
}
