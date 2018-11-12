package com.ibagou.dou.view;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.databinding.ViewDataBinding;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.Window;

import com.ibagou.dou.util.UmengUtils;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.umeng.analytics.MobclickAgent;

import com.ibagou.dou.application.DouYuApplication;

/**
 * BaseActivity
 */
public abstract class BaseActivity<DataBindingType extends ViewDataBinding> extends RxAppCompatActivity {
    public SharedPreferences mySharedPreferences;
    public SharedPreferences.Editor editor;
    public AudioManager audio;
    public DataBindingType mDataBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        audio = (AudioManager) getApplication().getSystemService(Context.AUDIO_SERVICE);
        mySharedPreferences = getSharedPreferences(DouYuApplication.SHARE_PREFERENCE_SIGN, Activity.MODE_PRIVATE);
        editor = mySharedPreferences.edit();

        mDataBinding = onCreateDataBindingView(savedInstanceState);
        initView();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        UmengUtils.onPauseToActivity(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        UmengUtils.onResumeToActivity(this);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
//        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
//
//        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
//            audio.adjustStreamVolume(
//                    AudioManager.STREAM_MUSIC,
//                    AudioManager.ADJUST_RAISE,
//                    AudioManager.FLAG_PLAY_SOUND | AudioManager.FLAG_SHOW_UI);
//            return true;
//        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
//            audio.adjustStreamVolume(
//                    AudioManager.STREAM_MUSIC,
//                    AudioManager.ADJUST_LOWER,
//                    AudioManager.FLAG_PLAY_SOUND | AudioManager.FLAG_SHOW_UI);
//            return true;
//        }
//        return false;
    }

    public abstract DataBindingType onCreateDataBindingView(Bundle savedInstanceState);

    public abstract void initView();
}
