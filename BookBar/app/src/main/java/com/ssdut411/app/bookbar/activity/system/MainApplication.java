package com.ssdut411.app.bookbar.activity.system;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.ssdut411.app.bookbar.utils.L;
import com.ssdut411.app.bookbar.utils.SPUtils;
import com.ssdut411.app.bookbar.volley.VolleyManager;
import com.ssdut411.app.bookbar.volley.VolleyUtil;


/**
 * Created by yao_han on 2015/12/22.
 */
public class MainApplication extends Application {
    // 单例一个MainApplication
    private static MainApplication instance;

    private String userId, phoneNumber;
    private boolean theme;
    private Context context;

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
        context = getApplicationContext();
        // 打印日志信息
        L.isDebug = true;
        VolleyManager.init(getApplicationContext());

        userId = SPUtils.get(context, "userId", "").toString();
        if (userId.length() == 0) {
            userId = null;
        }
        phoneNumber = SPUtils.get(context, "phoneNumber", "").toString();
        if (phoneNumber.length() == 0) {
            phoneNumber = null;
        }

    }

    public void clear() {
        userId = null;
        phoneNumber = null;
        SPUtils.clear(context);
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
        SPUtils.put(context, "userId", userId);
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        SPUtils.put(context, "phoneNumber", phoneNumber);
    }
}
