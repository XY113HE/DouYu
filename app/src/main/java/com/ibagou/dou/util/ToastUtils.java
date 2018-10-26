package com.ibagou.dou.util;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.Toast;

import com.ibagou.dou.R;
import com.ibagou.dou.databinding.MyToastViewBinding;

import static com.ibagou.dou.application.DouYuApplication.id;
import static com.ibagou.dou.view.MainActivity.PAGE_COUNT;

/**
 * Created by liumingyu on 2018/8/26.
 */

public class ToastUtils {
    private Toast toast;
    private Context context;
    public static final int MY_TOAST_TOP = 1;
    public static final int MY_TOAST_BOTTOM = 2;
    private int tempId;
    private Handler handler = new Handler(Looper.getMainLooper());

    public ToastUtils(Context context){
        this.context = context;
        toast = new Toast(context);
    }

    private ToastUtils setContent(String content){
        MyToastViewBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.my_toast_view, null, false);
        binding.toastContent.setText(content);
        toast.setView(binding.getRoot());
        return this;
    }

    public ToastUtils setType(int type){
        switch (type){
            case MY_TOAST_TOP:
                setContent("已为您选出"+PAGE_COUNT+"条精彩内容");
                toast.setGravity(Gravity.TOP, 0, DisplayUtils.dp2px(58));
                tempId = 1;
                break;
            case MY_TOAST_BOTTOM:
                setContent("休息一下！今天先看到这");
                toast.setGravity(Gravity.BOTTOM, 0, DisplayUtils.dp2px(20));
                tempId = 2;
                break;
        }
        return this;
    }

    public void show(){
        if(tempId != id) {
            id = tempId;
            toast.show();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    id = -1;
                }
            }, 1000);
        }

    }
}