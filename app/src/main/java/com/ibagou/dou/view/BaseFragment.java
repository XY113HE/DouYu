package com.ibagou.dou.view;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle2.components.support.RxFragment;

/**
 * author: gdw
 * date:2018/8/6
 * description:
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
}
