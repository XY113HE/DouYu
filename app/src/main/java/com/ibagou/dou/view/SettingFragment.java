package com.ibagou.dou.view;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import com.ibagou.dou.R;
import com.ibagou.dou.application.DouYuApplication;
import com.ibagou.dou.databinding.ActivityMainBinding;
import com.ibagou.dou.databinding.FragmentSettingBinding;
import com.ibagou.dou.model.ClearCacheSuccessBean;
import com.ibagou.dou.rx.RxBus;
import com.ibagou.dou.util.AppHook;
import com.ibagou.dou.util.GlideCacheUtil;
import com.ibagou.dou.util.RxUtils;
import com.ibagou.dou.util.Tools;

/**
 * Created by liumingyu on 2018/8/22.
 */

public class SettingFragment extends BaseFragment{
    private FragmentSettingBinding mBinding;
    private ActivityMainBinding mainBinding;
    private String lastCache = "0.0B";

    @Override
    public ViewDataBinding onCreateDataBindingView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting, container, false);
        return mBinding;
    }


    @Override
    public void initView() {
        setAutoDownSwitchOn(((BaseActivity)AppHook.get().currentActivity()).mySharedPreferences.getBoolean(DouYuApplication.AUTO_DOWNLOAD, true));

        initClicks();
        initRxBus();
    }



    private void initRxBus() {
        RxBus.getRxBus().toObservable(ClearCacheSuccessBean.class)
                .compose(this.<ClearCacheSuccessBean>bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ClearCacheSuccessBean>() {
                    @Override
                    public void accept(ClearCacheSuccessBean bean){
                        mBinding.settingClearCacheTv.setText("0.0B");
//                        Toast.makeText(AppHook.getApp(), "本次为您清理了"+lastCache+"缓存", Toast.LENGTH_SHORT).show();
                        Toast toast = Toast.makeText(AppHook.getApp(), "缓存清除成功", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable){

                    }
                });
    }

    private void initClicks() {
        RxUtils.setOnClick(mBinding.settingClearCacheLayout, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                if(mBinding.settingClearCacheTv.getText().toString().equals("0.0B")){
                    return;
                }
                lastCache = GlideCacheUtil.getCacheSize();
                GlideCacheUtil.clearImageAllCache();
            }
        });

        RxUtils.setOnClick(mBinding.settingAboutUsLayout, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                Tools.jumpToActivity(AboutUsActivity.class);
            }
        });


        RxUtils.setOnClick(mBinding.settingCloseArea, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                mainBinding.mainDrawerLayout.closeDrawer(mainBinding.leftDrawerLayout);
            }
        });

        RxUtils.setOnFreeClick(mBinding.switchBtnClose, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                setAutoDownSwitchOn(true);
                ((BaseActivity)AppHook.get().currentActivity()).editor.putBoolean(DouYuApplication.AUTO_DOWNLOAD, true).commit();
            }
        });

        RxUtils.setOnFreeClick(mBinding.switchBtnOpen, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                setAutoDownSwitchOn(false);
                ((BaseActivity)AppHook.get().currentActivity()).editor.putBoolean(DouYuApplication.AUTO_DOWNLOAD, false).commit();
            }
        });
    }

    private void setAutoDownSwitchOn(boolean switchState){
        if(switchState){
            mBinding.switchBtnClose.setVisibility(View.GONE);
            mBinding.switchBtnOpen.setVisibility(View.VISIBLE);
        }else{
            mBinding.switchBtnClose.setVisibility(View.VISIBLE);
            mBinding.switchBtnOpen.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    public void setMainBinding(ActivityMainBinding mainBinding) {
        this.mainBinding = mainBinding;
    }

    public void refreshCacheInfo() {
        mBinding.settingClearCacheTv.setText(GlideCacheUtil.getCacheSize());
    }
}
