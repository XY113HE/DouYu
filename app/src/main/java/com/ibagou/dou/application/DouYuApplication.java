package com.ibagou.dou.application;

import android.app.Activity;
import android.app.Application;
import android.content.IntentFilter;
import android.content.SharedPreferences;

import com.ibagou.dou.network.api.DouYuNetApi;
import com.ibagou.dou.util.DatabaseHelper;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;

import com.ibagou.dou.model.NetStateBean;
import com.ibagou.dou.rx.RxBus;
import com.ibagou.dou.util.AppHook;
import com.ibagou.dou.receiver.NetworkConnectChangedReceiver;


/**
 * liumingyu
 */
public class DouYuApplication extends Application{
    public static boolean DEBUG = true;
    private static DouYuApplication _instance;
    public static final String SHARE_PREFERENCE_SIGN = "pipixia_spsign";
    public static final String AUTO_DOWNLOAD = "auto_download";
    public static final String NET_STATE = "netState";
    public static final String FIRST_LAUNCH = "";
    private SharedPreferences sharedPreferences;
    private NetworkConnectChangedReceiver mNetworkChangeListener;
    public static int id = -1;//控制吐司的弹出时间间隔用


    //微信
    public static final String WX_APP_ID = "wx30e72bcc9a16ffca";
    public static final String WX_APP_SECRET = "55d2bca8a2967af27fe7f7838c2f4ef9";

    //新浪微博
    public static final String SINA_APP_KEY = "24722306";
    public static final String SINA_APP_SECRET = "c55a1c1641b53cd6c0e048dbcca01cd6";

    //QQ分享
    public static final String QQ_APP_ID = "1107828083";
    public static final String QQ_APP_KEY = "EzAq2gtkwYxlxUV5";


    //友盟
    public static final String UM_APP_KEY = "5bcd6df5f1f5563762000153";

    private boolean wifiEnable = false;
    private IWXAPI iwxapi;

    {
        PlatformConfig.setWeixin(WX_APP_ID, WX_APP_SECRET);
        PlatformConfig.setSinaWeibo(SINA_APP_KEY, SINA_APP_SECRET,"http://sns.whalecloud.com");
        PlatformConfig.setQQZone(QQ_APP_ID, QQ_APP_KEY);
    }



    @Override
    public void onCreate() {
        super.onCreate();
        DatabaseHelper.getInstance(this);
        _instance = this;
        UMConfigure.init(this, UM_APP_KEY,"umeng", UMConfigure.DEVICE_TYPE_PHONE,"");

        AppHook.get().hookApplicationWatcher(this);

        sharedPreferences = getSharedPreferences(SHARE_PREFERENCE_SIGN, Activity.MODE_PRIVATE);

        mNetworkChangeListener = new NetworkConnectChangedReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        filter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        filter.addAction("android.net.wifi.STATE_CHANGE");
        registerReceiver(mNetworkChangeListener, filter);

        iwxapi = WXAPIFactory.createWXAPI(this, WX_APP_ID, true);
        iwxapi.registerApp(WX_APP_ID);
    }

    public IWXAPI getIWXApi(){
        return iwxapi;
    }


    public static DouYuApplication getInstance() {
        return _instance;
    }


    @Override
    public void onTerminate() {
        AppHook.get().onTerminate(this);
        super.onTerminate();
    }


    public void setWifi(boolean b) {
        wifiEnable = b;
    }

    public void setMobile(boolean b) {
        if(b) {
            wifiEnable = false;
        }
    }

    public void setConnected(boolean b) {
        if (!b) {
            wifiEnable = false;
        }
    }

    public void setNetState(boolean b){
        if(!b){
            wifiEnable = false;
        }
        RxBus.getRxBus().post(new NetStateBean(b));
        sharedPreferences.edit().putBoolean(NET_STATE, b).apply();
    }

    public boolean getWifiEnable(){
        return wifiEnable;
    }

    public void unRegisterWifiStateReceiver(){
        try {
            unregisterReceiver(mNetworkChangeListener);
        }catch (Exception e){
        }
    }

}
