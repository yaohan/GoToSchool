package com.ssdut411.app.bookbar.activity.system;

import android.app.Application;

import com.ssdut411.app.bookbar.utils.L;
import com.ssdut411.app.bookbar.volley.VolleyManager;
import com.ssdut411.app.bookbar.volley.VolleyUtil;


/**
 * Created by yao_han on 2015/12/22.
 */
public class MainApplication extends Application {
    // 单例一个MainApplication
    private static MainApplication instance;

    private String userId,phoneNumber;
    private boolean theme;

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
        VolleyManager.init(getApplicationContext());
    }

    public void clear(){
        userId = null;
        phoneNumber = null;
    }

    public boolean getTheTheme() {
        return theme;
    }

    public void setTheme(Boolean theme) {
        this.theme = theme;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
