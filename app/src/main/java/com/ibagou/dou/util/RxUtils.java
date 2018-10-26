package com.ibagou.dou.util;

import android.view.View;

import com.jakewharton.rxbinding2.view.RxView;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.trello.rxlifecycle2.components.support.RxAppCompatDialogFragment;
import com.trello.rxlifecycle2.components.support.RxFragment;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import com.ibagou.dou.view.BaseActivity;

/**
 * liumingyu
 */
public class RxUtils {
    /**
     * 点击事件
     *
     * @param view
     * @param activity
     * @param consumer
     */
    public static void setOnClick(View view, RxAppCompatActivity activity, Consumer consumer) {
        RxView.clicks(view)
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .throttleFirst(1000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumer, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
    }

    /**
     * 点击事件
     *
     * @param view
     * @param fragment
     * @param consumer
     */
    public static void setOnClick(View view, RxFragment fragment, Consumer consumer) {
        RxView.clicks(view)
                .compose(fragment.bindUntilEvent(FragmentEvent.DESTROY))
                .throttleFirst(1000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumer, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
    }

    /**
     * 点击事件
     *
     * @param view
     * @param dialogFragment
     * @param consumer
     */
    public static void setOnClick(View view, RxAppCompatDialogFragment dialogFragment, Consumer consumer) {
        RxView.clicks(view)
                .compose(dialogFragment.bindUntilEvent(FragmentEvent.DESTROY))
                .throttleFirst(1000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumer, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
    }
    /**
     * 点击事件
     *
     * @param view
     * @param consumer
     */
    public static void setOnClicks(View view,  Consumer consumer) {
        Observable observable= RxView.clicks(view);
        if (AppHook.get().currentActivity() instanceof BaseActivity) {
            observable .compose(((BaseActivity)AppHook.get().currentActivity()).bindUntilEvent(ActivityEvent.DESTROY));
        }
        observable .throttleFirst(1000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumer, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                    }
                });
        try {
            RxView.clicks(view)
                    .compose(((BaseActivity)AppHook.get().currentActivity()).bindUntilEvent(ActivityEvent.DESTROY))
                    .throttleFirst(1000, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(consumer, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {

                        }
                    });
        } catch (Exception e){
            RxView.clicks(view)
                    .throttleFirst(1000, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(consumer, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {

                        }
                    });
        }
    }

    public static void setOnFreeClick(View view, RxFragment fragment, Consumer consumer) {
        RxView.clicks(view)
                .compose(fragment.bindUntilEvent(FragmentEvent.DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumer, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
    }

    public static void setOnFreeClick(View view, RxAppCompatActivity activity, Consumer consumer) {
        RxView.clicks(view)
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumer, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
    }

    public static Disposable setOnFreeClick(View view, Consumer consumer) {
        return RxView.clicks(view)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumer, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
    }

    /**
     * 点击事件
     *
     * @param view
     * @param consumer
     * @return Disposable
     */
    public static Disposable setOnClick(View view, Consumer consumer) {
        return RxView.clicks(view)
                .throttleFirst(1000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumer, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                    }
                });
    }

    //计时器
    public static Observable<Integer> countdown(int time) {
        if (time < 0) time = 0;
        final int countTime = time;
        return Observable.interval(0, 1, TimeUnit.SECONDS)
                // .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<Long, Integer>() {
                    @Override
                    public Integer apply(Long aLong) throws Exception {
                        return countTime - aLong.intValue();
                    }
                })
                .take(countTime + 1);

    }
}
