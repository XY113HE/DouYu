package com.ibagou.dou.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import java.util.List;

import com.ibagou.dou.BuildConfig;
import com.ibagou.dou.application.DouYuApplication;

/**
 * Created by liumingyu on 2018/8/21.
 */

public class Tools {
    //判断微信是否安装
    public static boolean isWXInstalled(){
        return isAppInstalled("com.tencent.mm");
    }

    //判断新浪微博是否安装
    public static boolean isSinaInstalled(){
        return isAppInstalled("com.sina.weibo");
    }

    private static boolean isAppInstalled(String packageName){
        final PackageManager packageManager = DouYuApplication.getInstance().getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals(packageName)) {
                    return true;
                }
            }
        }
        return false;
    }

    //设置View为全屏大小
    public static void viewToFullScreen(View view){
        DisplayMetrics metric = new DisplayMetrics();
        AppHook.get().currentActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
        ViewGroup.LayoutParams leftParams = view.getLayoutParams();
        leftParams.height = metric.heightPixels;
        leftParams.width = metric.widthPixels;
        view.setLayoutParams(leftParams);
    }

    //跳转到指定Activity
    public static void jumpToActivity(Class cla){
        Intent intent = new Intent(AppHook.get().currentActivity(), cla);
        AppHook.get().currentActivity().startActivity(intent);
    }


    //获取网络可用状态
    public static int getNetState(){
        ConnectivityManager manager = (ConnectivityManager) DouYuApplication.getInstance()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
        if (activeNetwork != null) { // connected to the internet
            if (activeNetwork.isConnected()) {
                if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                    // connected to wifi
                    DouYuApplication.getInstance().setWifi(true);
                    DouYuApplication.getInstance().setNetState(true);
                    return 1;//wifi已连接
                } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                    // connected to the mobile provider's data plan
                    DouYuApplication.getInstance().setMobile(true);
                    DouYuApplication.getInstance().setNetState(true);
                    return 2;//移动数据已开启
                }
            } else {
                DouYuApplication.getInstance().setNetState(false);
                return 3;//网络数据异常
            }
        } else {   // not connected to the internet
            DouYuApplication.getInstance().setWifi(false);
            DouYuApplication.getInstance().setMobile(false);
            DouYuApplication.getInstance().setConnected(false);
            DouYuApplication.getInstance().setNetState(false);
            return 4;//没有网络连接

        }

        return 3;

    }


    //获取状态栏高度,如果没有状态栏，则返回0
    public static int getStatusBarHeight() {
        int result = 0;
        int resourceId = DouYuApplication.getInstance().getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = DouYuApplication.getInstance().getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static int getScreenWidth(){
        WindowManager wm = (WindowManager)(AppHook.getApp()).getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getWidth();
    }

    public static int getScreenHeight(){
        WindowManager wm = (WindowManager)(AppHook.getApp()).getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getHeight();
    }

    //无论是否设置了带有ActionBar的主题
    public static float getActionBarHeight(){
        TypedArray actionbarSizeTypedArray = DouYuApplication.getInstance().obtainStyledAttributes(new int[] { android.R.attr.actionBarSize });
        float actionBarHeight = actionbarSizeTypedArray.getDimension(0, 0);
        actionbarSizeTypedArray.recycle();
        return actionBarHeight;
    }
    /**
     * 通过渠道名获取渠道id
     *
     * @return
     */
    public static String getChannelId() {
        return BuildConfig.CHANNEL_NAME;
    }
}
