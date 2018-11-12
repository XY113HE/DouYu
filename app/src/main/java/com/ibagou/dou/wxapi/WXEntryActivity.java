package com.ibagou.dou.wxapi;

import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;

import com.ibagou.dou.BuildConfig;
import com.ibagou.dou.R;
import com.ibagou.dou.application.DouYuApplication;
import com.ibagou.dou.customview.AutoUpdateDialog;
import com.ibagou.dou.databinding.ActivityWxEntryBinding;
import com.ibagou.dou.databinding.FragmentSettingBinding;
import com.ibagou.dou.model.BaseBean;
import com.ibagou.dou.model.CheckVersionBean;
import com.ibagou.dou.network.NetWorkManager;
import com.ibagou.dou.view.BaseActivity;
import com.ibagou.dou.view.MainActivity;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class WXEntryActivity extends BaseActivity<ActivityWxEntryBinding> implements IWXAPIEventHandler {
    private IWXAPI iwxapi;
    private ActivityWxEntryBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
//        // 隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //接收到分享以及登录的intent传递handleIntent方法，处理结果
        iwxapi = WXAPIFactory.createWXAPI(this, DouYuApplication.WX_APP_ID, false);
        iwxapi.handleIntent(getIntent(), this);
    }


    @Override
    public void onReq(BaseReq baseReq) {
    }


    //请求回调结果处理
    @Override
    public void onResp(BaseResp baseResp) {
        //登录回调
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                String code = ((SendAuth.Resp) baseResp).code;
                //获取用户信息
                getAccessToken(code);
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED://用户拒绝授权
                finish();
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL://用户取消
                finish();
                break;
            default:
                finish();
                break;
        }
    }

    private void getAccessToken(String code) {
        //获取授权
        Log.e("lmy", "getAccessToken: " + code);
//        NetWorkManager.getInstance()
//                .getDouYuNetApi()
//                .wxLogin(code)
//                .compose(WXEntryActivity.this.<BaseBean>bindToLifecycle())
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<BaseBean>() {
//                    @Override
//                    public void accept(BaseBean baseBean) throws Exception {
//
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) throws Exception {
//
//                    }
//                });
    }

    @Override
    protected void onPause() {
        overridePendingTransition(0, 0);
        super.onPause();
    }

    @Override
    public ActivityWxEntryBinding onCreateDataBindingView(Bundle savedInstanceState) {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_wx_entry);
        return mBinding;
    }

    @Override
    public void initView() {

    }


}
