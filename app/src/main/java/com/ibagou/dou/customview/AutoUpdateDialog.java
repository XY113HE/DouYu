package com.ibagou.dou.customview;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import io.reactivex.functions.Consumer;
import com.ibagou.dou.R;
import com.ibagou.dou.databinding.DialogAutoUpdateBinding;
import com.ibagou.dou.util.AppHook;
import com.ibagou.dou.util.RxUtils;

/**
 * Created by liumingyu on 2018/8/22.
 */

public class AutoUpdateDialog extends Dialog {
    public AutoUpdateDialog(@NonNull Context context) {
        super(context);
    }

    public AutoUpdateDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }
    public static class Builder{
        private DialogAutoUpdateBinding mBinding;
        private List<String> updateInfos;
        private OnClickListener installBtnClickListener;
        private String updateInfo = "";
        private String newVersion = "v1.xxxx";
        private boolean forceUpdate;

        public Builder setUpdateInfos(List<String> updateInfos){
            this.updateInfos = updateInfos;
            return this;
        }

        public Builder setInstallBtnClickListener(OnClickListener installBtnClickListener){
            this.installBtnClickListener = installBtnClickListener;
            return this;
        }

        public Builder setUpdateInfo(String updateInfo){
            this.updateInfo = updateInfo;
            return this;
        }

        public Builder setNewVersion(String newVersion){
            this.newVersion = newVersion;
            return this;
        }

        public Builder setForceUpdate(boolean forceUpdate){
            this.forceUpdate = forceUpdate;
            return this;
        }



        public AutoUpdateDialog create(){
            mBinding = DataBindingUtil.inflate(LayoutInflater.from(AppHook.get().currentActivity()), R.layout.dialog_auto_update, null, false);
            //设置ProgressDialog 的进度条 true 不明确 false 就是明确
            mBinding.updatingProgressBar.setIndeterminate(false);
            mBinding.updatingProgressBar.setProgress(0);

            mBinding.autoUpdateTitleHint.setText("检测到最新版本v" + newVersion + "，点击立即更新");
            mBinding.autoUpdateNewVersionInfo1.setText(updateInfo);

            if(forceUpdate){
                mBinding.updateDialogInstallBtnLater.setVisibility(View.GONE);
                mBinding.updateBtnDivideView.setVisibility(View.GONE);
            }
            final AutoUpdateDialog dialog = new AutoUpdateDialog(AppHook.get().currentActivity(), R.style.Theme_AppCompat_Dialog);
            dialog.addContentView(mBinding.getRoot(), new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            if(installBtnClickListener != null){
                RxUtils.setOnFreeClick(mBinding.updateDialogInstallBtnRightnow, new Consumer() {
                    @Override
                    public void accept(Object o) throws Exception {
                        installBtnClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                        mBinding.autoUpdateTitleHint.setText("检测到最新版本v" + newVersion);
                        mBinding.autoUpdateNewVersionInfo.setVisibility(View.GONE);
                        mBinding.updateProgressBarLayout.setVisibility(View.VISIBLE);
                        mBinding.updateBtnLayout.setVisibility(View.INVISIBLE);
                    }
                });

                RxUtils.setOnFreeClick(mBinding.updateDialogInstallBtnLater, new Consumer() {
                    @Override
                    public void accept(Object o) throws Exception {
                        installBtnClickListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
                        dialog.dismiss();
                    }
                });
            }

            dialog.setCancelable(false);
            return dialog;
        }

        public void setMax(final int max) {
            AppHook.get().currentActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mBinding.updatingProgressBar.setMax(max);
                }
            });
        }

        public void setProgress(final int progress) {
            AppHook.get().currentActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mBinding.updatingProgressBar.setProgress(progress);
                }
            });
        }
    }



}
