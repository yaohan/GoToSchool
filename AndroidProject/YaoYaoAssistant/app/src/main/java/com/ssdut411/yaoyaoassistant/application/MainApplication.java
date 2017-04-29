package com.ssdut411.yaoyaoassistant.application;

import com.ssdut411.yaoyaoassistant.api.volley.VolleyManager;
import com.ssdut411.yaoyaoassistant.exception.BaseExceptionHandler;
import com.ssdut411.yaoyaoassistant.utils.L;

/**
 * Created by yao_han on 2015/12/22.
 */
public class MainApplication extends BaseApplication {
    // 单例一个MainApplication
    private static MainApplication instance;

    /**
     * 得到MainApplication实例
     *
     * @return
     */
    public static MainApplication getInstance() {
        return MainApplication.instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        // 打印日志信息
        L.isDebug = true;
        // 初始化Volley
        VolleyManager.init(context);
        L.i("初始化Volley");
    }


    /**
     * 获取默认的未捕获异常处理器
     */
    @Override
    public BaseExceptionHandler getDefaultUncaughtExceptionHandler() {
//        return new CrashExceptionHandler(applicationContext);
        return null;
    }

}
