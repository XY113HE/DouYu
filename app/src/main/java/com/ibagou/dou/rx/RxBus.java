package com.ibagou.dou.rx;


import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * Created by liumingyu on 2018/8/21.ΩΩ
 */
public class RxBus {
    private static volatile RxBus staticRxBus;
    private final Subject subject;

    public RxBus() {
        subject = PublishSubject.create().toSerialized();
    }

    public Throwable e;


    /**
     * Double Checked locking单例
     * 保证多线程安全
     *
     * @return
     */
    public static RxBus getRxBus() {
        RxBus rxBus = staticRxBus;
        if (rxBus == null) {
            synchronized (RxBus.class) {
                rxBus = staticRxBus;
                if (rxBus == null) {
                    rxBus = new RxBus();
                    staticRxBus = rxBus;
                }
            }
        }
        return rxBus;
    }

    /**
     * 发送事件
     *
     * @param o
     */
    public void post(Object o) {
        subject.onNext(o);
    }

    /**
     * 接收事件
     * ofType是filter（过滤）+cast（类型判断）
     *
     * @param event
     * @param <T>
     * @return
     */
    public <T> Observable<T> toObservable(Class<T> event) {
        return subject.ofType(event);
    }

}
