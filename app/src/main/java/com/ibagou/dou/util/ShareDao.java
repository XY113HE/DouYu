package com.ibagou.dou.util;

import android.databinding.DataBindingUtil;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.umeng.socialize.bean.SHARE_MEDIA;

import io.reactivex.functions.Consumer;
import com.ibagou.dou.R;
import com.ibagou.dou.databinding.ActivityMainBinding;
import com.ibagou.dou.databinding.PopShareViewBinding;
import com.ibagou.dou.interf.MainInterf;

/**
 * Created by liumingyu on 2018/8/21.
 */

public class ShareDao {
    private PopupWindow mPop;
    private PopShareViewBinding mBinding;
    private static ShareDao shareDao;
    private MainInterf.MainView mMainView;
    private ActivityMainBinding mMainBinding;
    private String shareContent = "";


    public static ShareDao getShareDao(){
        if(shareDao == null){
            synchronized (ShareDao.class){
                if (shareDao == null) {
                    shareDao = new ShareDao();
                }
            }
        }

        return shareDao;
    }

    private ShareDao(){}

    public void initShareDao(MainInterf.MainView mMainView, ActivityMainBinding mMainBinding){
        this.mMainView = mMainView;
        this.mMainBinding = mMainBinding;
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(mMainView.getContext()), R.layout.pop_share_view, null, false);
        mPop = new PopupWindow(mBinding.getRoot(), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, false);
        initView();
    }

    private void initView() {
        RxUtils.setOnFreeClick(mBinding.shareCancelBtn, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                ShareDao.getShareDao().hideDao();
            }
        });

        RxUtils.setOnClick(mBinding.shareWechatBtn, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                if(Tools.isWXInstalled()) {
                    mMainView.shareAction(SHARE_MEDIA.WEIXIN, shareContent);
                }else{
                    Toast.makeText(mMainView.getContext(), "还没有装微信哦", Toast.LENGTH_SHORT).show();
                }
            }
        });

        RxUtils.setOnClick(mBinding.shareWxcircleBtn, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                if(Tools.isWXInstalled()) {
                    mMainView.shareAction(SHARE_MEDIA.WEIXIN_CIRCLE, shareContent);
                }else{
                    Toast.makeText(mMainView.getContext(), "还没有装微信哦", Toast.LENGTH_SHORT).show();
                }
            }
        });

        RxUtils.setOnClick(mBinding.shareSinaBtn, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                if(Tools.isSinaInstalled()){
                    mMainView.shareAction(SHARE_MEDIA.SINA, shareContent);
                }else{
                    Toast.makeText(mMainView.getContext(), "还没有装新浪微博哦", Toast.LENGTH_SHORT).show();
                }
            }
        });

        RxUtils.setOnClick(mBinding.shareQqBtn, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                if(Tools.isSinaInstalled()){
                    mMainView.shareAction(SHARE_MEDIA.QQ, shareContent);
                }else{
                    Toast.makeText(mMainView.getContext(), "还没有装新浪微博哦", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void showDao(String shareContent){
        this.shareContent = shareContent;
        if(mPop != null && !mPop.isShowing()){
            mPop.showAtLocation(mMainBinding.getRoot(), Gravity.CENTER, 0, 0);
        }
    }

    public void hideDao(){
        if(mPop != null && mPop.isShowing()){
            mPop.dismiss();
        }
    }

    public void destroyDao(){
        if(shareDao != null){
            shareDao = null;
        }
    }
}
