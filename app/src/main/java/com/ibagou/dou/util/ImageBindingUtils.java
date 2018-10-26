package com.ibagou.dou.util;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.ibagou.dou.application.DouYuApplication;
/**
 * liumingyu
 */
public class ImageBindingUtils {
    public static void loadImg(final ImageView view, int resId){
        Glide.with(DouYuApplication.getInstance())
                .load(resId)
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        view.setImageDrawable(resource);
                    }
                });
    }

    public static void loadImg(final ImageView view, String url){
        Glide.with(DouYuApplication.getInstance())
                .load(url)
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        view.setImageDrawable(resource);
                    }
                });
    }
}
