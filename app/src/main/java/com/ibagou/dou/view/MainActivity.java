package com.ibagou.dou.view;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ibagou.dou.BuildConfig;
import com.ibagou.dou.application.DouYuApplication;
import com.ibagou.dou.customview.AutoUpdateDialog;
import com.ibagou.dou.customview.RefreshableView2;
import com.ibagou.dou.model.CheckVersionBean;
import com.ibagou.dou.model.ContentDataBean;
import com.ibagou.dou.network.NetWorkManager;
import com.ibagou.dou.util.DatabaseHelper;
import com.ibagou.dou.util.ToastUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

import com.ibagou.dou.R;
import com.ibagou.dou.adapter.MainTextAdapter;
import com.ibagou.dou.databinding.ActivityMainBinding;
import com.ibagou.dou.interf.MainInterf;
import com.ibagou.dou.model.NetStateBean;
import com.ibagou.dou.rx.RxBus;
import com.ibagou.dou.util.RxUtils;
import com.ibagou.dou.util.ShareDao;
import com.ibagou.dou.util.Tools;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import static com.ibagou.dou.application.DouYuApplication.NET_STATE;

public class MainActivity extends BaseActivity<ActivityMainBinding> implements MainInterf.MainView{
    private ActivityMainBinding mBinding;
    private MainTextAdapter mainTextAdapter;
    private List<ContentDataBean.ItemsBean> data;
    private int myId = 1;
    private int times = 1;
    private boolean isRefreshing = false;
    private String testText = "";
    private String testShareText = "我分享出去咯~~啦啦啦啦~~追不上我吧~~啦啦啦啦";
    private int lastVisibleItem = -1;
    private LinearLayoutManager mLinearLayoutManager;
    //设置下拉加载每次显示最大条目数
    public static final String PAGE_COUNT = "10";
    //测试用的一个最大数据
    private int totalData = 55;
    //当前显示的Page
    private int page = 0;

    private boolean updating = false;
    private AutoUpdateDialog autoUpdateDialog;
    private AutoUpdateDialog.Builder builder;
    private String appNameStr = "DouYuAndroid.apk"; //下载到本地要给这个APP命的名字

    public static final int BACK_PRESS = 1;
    public static final int UPDATE_SUCCESS = 2;
    private boolean wantExit = false;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what){
                case BACK_PRESS:
                    wantExit = false;
                    break;
                case UPDATE_SUCCESS://下载成功，安装最新apk
                    update();
                    break;

            }
            return false;
        }
    });
    private String download_uri = "";


    @Override
    public ActivityMainBinding onCreateDataBindingView(Bundle savedInstanceState) {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        return mBinding;
    }


    @Override
    public void onResume() {
        super.onResume();
        //如果是Wifi可用的状态下，进行自动更新的监测
        //每次清成false可以控制每次都监测，否则，只监测一次
//        updating = false;
        boolean autoDounload = mySharedPreferences.getBoolean(DouYuApplication.AUTO_DOWNLOAD, true);
        if(autoDounload && DouYuApplication.getInstance().getWifiEnable() && !updating){
            updating = true;
            NetWorkManager.getInstance()
                    .getDouYuNetApi()
                    .checkVersion(BuildConfig.VERSION_NAME)
                    .compose(MainActivity.this.<CheckVersionBean>bindToLifecycle())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<CheckVersionBean>() {
                        @Override
                        public void accept(CheckVersionBean checkVersionBean) throws Exception {
                            Log.e("lmy", "accept: " + new Gson().toJson(checkVersionBean));
                            if(checkVersionBean.getData().getNew_version().equals("YES")){

                                //初始化更新Dialog
                                builder = new AutoUpdateDialog.Builder()
                                        .setUpdateInfos(null)
                                        .setInstallBtnClickListener(new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                if(i == DialogInterface.BUTTON_POSITIVE){
                                                    downFile(download_uri);
                                                }else if(i == DialogInterface.BUTTON_NEGATIVE){
                                                    autoUpdateDialog.dismiss();
//                                                  finish();
                                                }
                                            }
                                        });


                                download_uri = checkVersionBean.getData().getDownload_uri();
                                builder.setUpdateInfo(checkVersionBean.getData().getNote());
                                builder.setNewVersion(checkVersionBean.getData().getVersion());
                                builder.setForceUpdate(checkVersionBean.getData().getForce().equals("YES"));

                                autoUpdateDialog = builder.create();
                                autoUpdateDialog.show();
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                        }
                    });
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {


        //首次进入导航显示
        if(mySharedPreferences.getBoolean(DouYuApplication.FIRST_LAUNCH, true)){
            mBinding.mainGuildPage.setVisibility(View.VISIBLE);
        }else{
            mBinding.mainGuildPage.setVisibility(View.GONE);
        }

        testText = getResources().getString(R.string.test_text);

        initData();
        initListener();
        initDao();
        initRxBus();

        //判断Application中获取的网络状态
        if(mySharedPreferences.getBoolean(NET_STATE, false)){
            netUseable();
        }else{
            netUnable();
        }

        mainTextAdapter = new MainTextAdapter(this);
        mLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mBinding.mainRv.setLayoutManager(mLinearLayoutManager);
        mBinding.mainRv.setAdapter(mainTextAdapter);
        Tools.viewToFullScreen(mBinding.leftDrawerLayout);
        final SettingFragment fragment = new SettingFragment();
        fragment.setMainBinding(mBinding);
        getSupportFragmentManager().beginTransaction().replace(R.id.left_drawer_layout, fragment).commit();
        mBinding.mainDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                fragment.refreshCacheInfo();
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }

    private void initRxBus() {
        RxBus.getRxBus().toObservable(NetStateBean.class)
                .compose(this.<NetStateBean>bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<NetStateBean>() {
                    @Override
                    public void accept(NetStateBean netStateBean){
                        if(netStateBean.isNetAccess()){
                            netUseable();
                        }else{
                            netUnable();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable){

                    }
                });
    }

    private void netUseable(){
        mBinding.mainNoDataLayout.setVisibility(View.GONE);
        mBinding.mainDataLayout.setVisibility(View.VISIBLE);
        mBinding.mainRefreshBtn.setVisibility(View.VISIBLE);
    }

    private void netUnable(){
        mBinding.mainNoDataLayout.setVisibility(View.VISIBLE);
        mBinding.mainDataLayout.setVisibility(View.GONE);
        mBinding.mainRefreshBtn.setVisibility(View.GONE);
    }

    private void initDao() {
        ShareDao.getShareDao().initShareDao(this, mBinding);
    }

    private void initData() {
        page++;
        NetWorkManager.getInstance()
                .getDouYuNetApi()
                .getPage(page+"", PAGE_COUNT)
                .compose(this.<ContentDataBean>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ContentDataBean>() {
                    @Override
                    public void accept(ContentDataBean contentDataBean) throws Exception {
                        data = contentDataBean.getItems();
                        if(data.size() < 10){
                            mainTextAdapter.setHasMore(false);
                        }else{
                            mainTextAdapter.setHasMore(true);
                        }
                        mainTextAdapter.setData(data);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                    }
                });
    }

    private void initListener() {
        RxUtils.setOnClick(mBinding.mainRefreshBtn, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                if(!isRefreshing) {
                    isRefreshing = true;
                    startRefreshAnim();
                    refreshData("bottombtn");
                }
            }
        });

        RxUtils.setOnClick(mBinding.drawerMenuBtn, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                mBinding.mainDrawerLayout.openDrawer(mBinding.leftDrawerLayout);
            }
        });

        RxUtils.setOnClick(mBinding.netStateRefresh, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                mBinding.netStateRefreshProgressbar.setVisibility(View.VISIBLE);
                mBinding.mainNoDataLayout.setVisibility(View.GONE);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SystemClock.sleep(1000);
                        switch (Tools.getNetState()){
                            case 1:
                                break;
                            case 2:
                                break;
                            case 3:
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(MainActivity.this, "没有网络链接", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                break;
                            case 4:
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(MainActivity.this, "没有网络链接", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                break;
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mBinding.netStateRefreshProgressbar.setVisibility(View.GONE);
                            }
                        });
                    }
                }).start();
            }
        });

        //recyclerView上拉刷新监听
        setRvPullUpRefresh();

        mBinding.refreshableView.setOnRefreshListener(new RefreshableView2.PullToRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData("pulldown");
            }
        }, 0);


        RxUtils.setOnClick(mBinding.startDouyuBtn, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                editor.putBoolean(DouYuApplication.FIRST_LAUNCH, false).commit();
                mBinding.mainGuildPage.setVisibility(View.GONE);
            }
        });
    }

    /**
     * 设置上拉加载的监听
     */
    private void setRvPullUpRefresh() {
        // 实现上拉加载重要步骤，设置滑动监听器，RecyclerView自带的ScrollListener
        mBinding.mainRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                // 在newState为滑到底部时
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    // 如果没有隐藏footView，那么最后一个条目的位置就比我们的getItemCount少1，自己可以算一下
                    if (mainTextAdapter.isFadeTips() == false && lastVisibleItem + 1 == mainTextAdapter.getItemCount()) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    SystemClock.sleep(500);
                                    // 然后调用updateRecyclerview方法更新RecyclerView
                                    updateRecyclerView();
                                }
                            }).start();
                    }

                    // 如果隐藏了提示条，我们又上拉加载时，那么最后一个条目就要比getItemCount要少2
                    if (mainTextAdapter.isFadeTips() == true && lastVisibleItem + 2 == mainTextAdapter.getItemCount()) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                SystemClock.sleep(500);
                                // 然后调用updateRecyclerview方法更新RecyclerView
                                updateRecyclerView();
                            }
                        }).start();
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                // 在滑动完成后，拿到最后一个可见的item的位置
                lastVisibleItem = mLinearLayoutManager.findLastVisibleItemPosition();
            }
        });
    }

    /**
     *
     * 上拉加载时调用的更新RecyclerView的方法
     */
    private void updateRecyclerView() {
        // 获取从fromIndex到toIndex的数据
        /**
         * 获取更多页的数据
         */
        page++;
        NetWorkManager.getInstance()
                .getDouYuNetApi()
                .getPage(page+"", PAGE_COUNT)
                .compose(this.<ContentDataBean>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ContentDataBean>() {
                    @Override
                    public void accept(ContentDataBean contentDataBean) throws Exception {
                        data = contentDataBean.getItems();
                        if(data.size() > 0){
                            mainTextAdapter.updateList(data, true);
                            if(data.size() < 10){
                                mainTextAdapter.setHasMore(false);
                            }else{
                                mainTextAdapter.setHasMore(true);
                            }
                        }else{
                            mainTextAdapter.updateList(null, false);
                        }

//                        mainTextAdapter.setData(data);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                    }
                });
    }

    /**
     * 开启刷新按钮的动画
     */
    private void startRefreshAnim() {
        Animation circle_anim = AnimationUtils.loadAnimation(this, R.anim.refresh_brn_anim);
        LinearInterpolator interpolator = new LinearInterpolator();  //设置匀速旋转，在xml文件中设置会出现卡顿
        circle_anim.setInterpolator(interpolator);
        if (circle_anim != null) {
            mBinding.mainRefreshBtn.startAnimation(circle_anim);  //开始动画
        }
    }
    /**
     * 刷新数据
     *  0，数据请求完成后
     *  1，清楚刷新按钮旋转动画
     *  2，将列表滑到顶部
     *  3，开启刷新完成动画
     *  4，适配器中设置新数据
     *  5，设置正在刷新状态为false
     */
    private void refreshData(final String type) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(1000);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(type.equals("pulldown")) {
                            mBinding.refreshableView.finishRefreshing();
                        }else if(type.equals("bottombtn")) {
                            mBinding.mainRefreshBtn.clearAnimation();
                        }
                        mBinding.mainRv.scrollToPosition(0);
                        startRefreshFinishedAnim();

                        initData();
                        mainTextAdapter.setFadeTips(false);
                        isRefreshing = false;

                    }
                });
            }
        }).start();



    }

    /**
     * 刷新结束动画
     *  1.显示刷新提示指定时间
     *  2.隐藏提示View
     */
    private void startRefreshFinishedAnim() {
        new ToastUtils(this).setType(ToastUtils.MY_TOAST_TOP).show();
    }

    /**
     * 友盟分享监听
     */
    private UMShareListener umShareListener = new UMShareListener() {
        /**
         * @descrption 分享开始的回调
         * @param platform 平台类型
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        /**
         * @descrption 分享成功的回调
         * @param platform 平台类型
         */
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toast.makeText(MainActivity.this,"已分享",Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(MainActivity.this,"分享失败" + t.getMessage(),Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(MainActivity.this,"分享取消",Toast.LENGTH_LONG).show();

        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public Context getContext() {
        return MainActivity.this;
    }

    @Override
    public void shareAction(final SHARE_MEDIA platform, final String shareContent) {
        final RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if(aBoolean){
                            UMImage umImage = new UMImage(MainActivity.this, "http://dou.ibagou.com/static/imgs/72.png");
                            UMWeb web = new UMWeb("http://dou.ibagou.com/app");
                            web.setTitle("逗娱 - 分享快乐");//标题
                            web.setThumb(umImage);  //缩略图
                            web.setDescription(shareContent);//描述
                            new ShareAction(MainActivity.this)
                                    .setPlatform(platform)//传入平台
                                    .withMedia(web)
                                    .setCallback(umShareListener)//回调监听器
                                    .share();
                            ShareDao.getShareDao().hideDao();
                        }else{
                            Toast.makeText(MainActivity.this, "权限拒绝，分享失败", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                });


    }

    @Override
    public MainActivity getActivity() {
        return MainActivity.this;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(wantExit){
                finish();
            }else {
                wantExit = true;
                Toast.makeText(this, "再按一次退出逗娱", Toast.LENGTH_SHORT).show();
                handler.sendEmptyMessageDelayed(BACK_PRESS, 2000);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        DatabaseHelper.getInstance(DouYuApplication.getInstance()).close();
        DouYuApplication.getInstance().unRegisterWifiStateReceiver();
        super.onDestroy();
    }

    private void downFile(final String url) {
        autoUpdateDialog.show();
        final RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if(aBoolean) {
                            NetWorkManager
                                    .getInstance()
                                    .getDouYuNetApi()
                                    .downloadFile(url)
                                    .compose(MainActivity.this.<ResponseBody>bindToLifecycle())
                                    .subscribeOn(Schedulers.io())
                                    .subscribe(new Observer<ResponseBody>() {//取消监听

                                        @Override
                                        public void onSubscribe(Disposable d) {

                                        }

                                        @Override
                                        public void onNext(ResponseBody responseBody) {
                                            try {
                                                long length = responseBody.contentLength();
                                                builder.setMax((int) length);//设置进度条的最大值
                                                InputStream is = responseBody.byteStream();
                                                FileOutputStream fileOutputStream = null;
                                                if (is != null) {
                                                    File file = new File(
                                                            Environment.getExternalStorageDirectory() + "/" + appNameStr);
                                                    if (!file.exists()) {
                                                        file.createNewFile();
                                                    }
                                                    fileOutputStream = new FileOutputStream(file);
                                                    byte[] buf = new byte[1024];
                                                    int ch = -1;
                                                    int count = 0;
                                                    while ((ch = is.read(buf)) != -1) {
                                                        fileOutputStream.write(buf, 0, ch);
                                                        count += ch;
                                                        if (length > 0) {
                                                            builder.setProgress(count);
                                                        }
                                                    }
                                                }
                                                fileOutputStream.flush();
                                                if (fileOutputStream != null) {
                                                    fileOutputStream.close();
                                                }
                                                down();
                                            } catch (IOException e) {
                                            }
                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                        }

                                        @Override
                                        public void onComplete() {
                                        }
                                    });
                        }else{
                            Toast.makeText(MainActivity.this, "权限拒绝，更新失败", Toast.LENGTH_SHORT).show();
                            autoUpdateDialog.dismiss();
                        }
                    }
                });


    }


    private void down() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                autoUpdateDialog.cancel();
                update();
            }
        });

    }

    private void update() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        //判断是否是AndroidN以及更高的版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".fileProvider", new File(Environment.getExternalStorageDirectory(), appNameStr));
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");

        } else {
            intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory(), appNameStr)), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        startActivity(intent);
    }
}
