package com.ggg.common;

import android.app.Application;
import android.content.Context;

import com.alibaba.android.arouter.launcher.ARouter;

public class BaseApplication extends Application {
    private static BaseApplication instance;
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        // 直接开启日志和调试模式，不依赖 BuildConfig.DEBUG（library的DEBUG恒为false）
        ARouter.openLog();
        ARouter.openDebug();

        // 初始化ARouter
        ARouter.init(this);
    }

    // 获取全局Context
    public static Context getAppContext() {
        return instance.getApplicationContext();
    }
}
